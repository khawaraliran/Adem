/**
 * 
 */
package org.compiere.process;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.I_AD_Org;
import org.compiere.model.I_AD_Process;
import org.compiere.model.I_AD_User_Roles;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_R_MailText;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MOrg;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUser;
import org.compiere.model.MUserRoles;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.wtc.util.EMailUtil;
import org.wtc.util.WTCUtil;

/**
 * @author PhaniKiran.Gutha
 *
 *This process create Tax Form Receivable/Issubale Report and Enqueue('s) Mails.
 *There Three Scenario's handled here
 *1.  When the business partner parameter has value then based on the receivable/issuable
 *    Report pdf file will be prepared and
 *     1.1 email will be enqueued for Business Partner if Receivable  Mail Template -- Missing Forms Receivable
 *     1.2 Email will be enqueued for The Purchase Team  			  Mail Template -- Missing Forms Issubale
 *     
 *2. when business partner not supplied and Receivable is true then appropriate report Pdf file gets prepared
 *   and the email will be enqueued for the individual business partner as well as for the  sales team  
 *   Mail Template -- Missing Forms Receivable ,  Sales Team Missing Forms Receivable
 *   
 * 3. when business partner not supplied and Receivable is false then appropriate report Pdf file gets prepared
 *   and the email will be enqueued  for the  Purchase team  
 *   Mail Template -- Missing Forms Issubale
 *   
 *   parameter from User is not passed then process will try to get the organization supervisor to enque the mail
 *   if the supervisor of the organization doesnot exists then system will find the user who has SystemAdmin role 
 *   acess (only the first one who has acess to the admin role) and will enque the mail
 *   
 *   Used AD_Messages
 *   
 *   IssueNo		Author			ChangeID		Description
 *   1778		PhaniKiran.Gutha    20111214515		1. getBusinessPartnersHaveMissingForms query to identify the business has invoice expecting forms Type Parameter to Timestamp and check null
 *   												2. enqueueMailForBps and enqueueConslidateMail if report from the process returned null then dont enque mail, to avoid null pointer exception
 *   													and increate failure count
 *    
 */
public class TaxFormReminder extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: TaxFormReminder.java 1009 2012-02-09 09:16:13Z suman $";


	private static CLogger log = CLogger.getCLogger("TaxFormReminder");
	
	private Integer 	p_C_BPartner_ID 	= null;
	private Integer 	p_ReportProcess_ID 	= null;
	private Timestamp 	p_FromDate 			= null;
	private Timestamp 	p_ToDate 			= null;
	private Integer 	p_MailTemplate 		= null;
	private Boolean 	p_receivable 		= Boolean.FALSE;
	private Integer 	p_AD_Client_ID 		= null;
	private Properties 	ctx 				= null;
	private Integer 	p_FromUser_ID 		= null;
	private int 	 	m_enqueued 			= 0;
	private int  		m_failed 			= 0;
	private Integer     p_ADOrg_ID			= null;
	
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		
		for (int i = 0; i < para.length; i++){
			
			String name = para[i].getParameterName();
			
			if( name.equals( I_C_Invoice.COLUMNNAME_C_BPartner_ID ) ){
				
				Object obj= para[i].getParameter();
				p_C_BPartner_ID = obj != null ? ( (BigDecimal)obj ).intValue() : null;
				
			} else if( name.equals( I_AD_Process.COLUMNNAME_AD_Process_ID ) ){
				
				Object obj= para[i].getParameter();
				p_ReportProcess_ID = obj != null ? ( (BigDecimal)obj ).intValue() : null;
				
			}else if( name.equals( I_AD_Process.COLUMNNAME_AD_Client_ID ) ){
				
				Object obj= para[i].getParameter();
				p_AD_Client_ID = obj != null ? ( (BigDecimal)obj ).intValue() : null;
				
			}else if( name.equals( "FromDate") ){
				
				Object obj= para[i].getParameter();
				p_FromDate = obj != null ? (Timestamp)obj : null;
				
			}else if( name.equals( "ToDate" ) ){
				
				Object obj= para[i].getParameter();
				p_ToDate = obj != null ? (Timestamp)obj : null;
				
			} else if( name.equals( I_R_MailText.COLUMNNAME_R_MailText_ID ) ){
				
				Object obj= para[i].getParameter();
				p_MailTemplate = obj != null ? ( (BigDecimal)obj ).intValue() : null;
				
			}  else if( name.equals( I_C_Invoice.COLUMNNAME_AD_User_ID ) ){
				
				Object obj= para[i].getParameter();
				p_FromUser_ID = obj != null ? ( (BigDecimal)obj ).intValue() : null;
				
			}else if( name.equals( I_C_Invoice.COLUMNNAME_IsSOTrx ) ){
				
				p_receivable= para[i].getParameterAsBoolean();
				
			}else if( name.equals( I_C_Invoice.COLUMNNAME_AD_Org_ID) ){
				
				Object obj= para[i].getParameter();
				p_ADOrg_ID = obj != null ? ( (BigDecimal)obj ).intValue() : null;
				
			}else {
				
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}

		ctx = new Properties();
		ctx.putAll( Env.getCtx() );
		Env.setContext(ctx, "#AD_Client_ID",p_AD_Client_ID );
		
	}

	
	protected String doIt() throws Exception {
		
		if( p_ReportProcess_ID == null ){
			p_ReportProcess_ID = WTCUtil.getAD_Process_ID( EagleConstants.TAX_FORM_REMINDER_PROCESS ); 
		}
		
		MMailText template = this.getMailTemplate( p_MailTemplate , p_AD_Client_ID , p_receivable ); 
		MUser fromUser = null;
		if( p_FromUser_ID != null ){
			fromUser = new MUser(ctx, p_FromUser_ID, null );
		}
		
		if( p_C_BPartner_ID != null ){
			
			Integer bps[] = this.getBusinessPartnersHaveMissingForms( p_C_BPartner_ID , p_AD_Client_ID , p_ADOrg_ID );
			this.enqueueMailForBps(bps, template, fromUser );
			
		} else if( p_C_BPartner_ID == null && p_receivable ){
			
			if( p_ADOrg_ID == null ){
				
				List<MOrg> orgs = new Query( ctx, I_AD_Org.Table_Name , I_AD_Org.COLUMNNAME_AD_Client_ID + " = " + p_AD_Client_ID ,null).setOnlyActiveRecords( Boolean.TRUE ).list();
				
				for( MOrg org : orgs ){
					
					Integer[] bps = this.getBusinessPartnersHaveMissingForms(p_C_BPartner_ID ,p_AD_Client_ID , org.getAD_Org_ID() );
					
					if( bps.length > 0 ){	
						
						if( fromUser == null || (fromUser!= null && fromUser.get_ID() ==0 )){
							fromUser = (MUser) org.getInfo().getSupervisor();
						}
						this.enqueueMailForBps(bps, template, fromUser );
						template =  WTCUtil.getMailTemplate( p_AD_Client_ID , EagleConstants.MISSING_RECEIVABLE_FORM_SALESTEAM_MAIL_TEMPLATE, ctx, get_TrxName() );
						this.enqueueConslidateMail(template, (MUser) org.getInfo().getSupervisor(), fromUser );
					}
				}
			} else {
				
				Integer[] bps = this.getBusinessPartnersHaveMissingForms(p_C_BPartner_ID ,p_AD_Client_ID , p_ADOrg_ID );
				if( bps.length > 0 ){
					MOrg org = new MOrg(ctx, p_ADOrg_ID , null );
					if( fromUser == null || (fromUser!= null && fromUser.get_ID() ==0 )){
						fromUser = (MUser) org.getInfo().getSupervisor();
					}

					this.enqueueMailForBps(bps, template, (MUser) org.getInfo().getSupervisor() );

					template =  WTCUtil.getMailTemplate( p_AD_Client_ID , EagleConstants.MISSING_RECEIVABLE_FORM_SALESTEAM_MAIL_TEMPLATE, ctx, get_TrxName() );

					this.enqueueConslidateMail(template,  fromUser , (MUser) org.getInfo().getSupervisor() ); 
				}
			}
			
		} else if( p_C_BPartner_ID == null && !p_receivable ){


			if( p_ADOrg_ID == null ){

				List<MOrg> orgs = new Query( ctx, I_AD_Org.Table_Name , I_AD_Org.COLUMNNAME_AD_Client_ID + " = " + p_AD_Client_ID ,null).setOnlyActiveRecords( Boolean.TRUE ).list();

				for( MOrg org : orgs ){
					if( fromUser == null || (fromUser!= null && fromUser.get_ID() ==0 )){
						fromUser = (MUser) org.getInfo().getSupervisor();
					}
					Integer[] bps = this.getBusinessPartnersHaveMissingForms(p_C_BPartner_ID ,p_AD_Client_ID , org.get_ID() );
					if( bps.length > 0 ){
						this.enqueueConslidateMail(template,  (MUser) org.getInfo().getSupervisor() , fromUser );
					}
				}
			} else {

				Integer[] bps = this.getBusinessPartnersHaveMissingForms(p_C_BPartner_ID ,p_AD_Client_ID ,p_ADOrg_ID );
				if( bps.length > 0 ){
					MOrg org = new MOrg(ctx, p_ADOrg_ID, null );
					if( fromUser == null || (fromUser!= null && fromUser.get_ID() ==0 )){
						fromUser = (MUser) org.getInfo().getSupervisor();
					}

					this.enqueueConslidateMail(template,fromUser, (MUser) org.getInfo().getSupervisor() );
				}
			}
	
		}
		return " Mail('s) Enqueued :" + m_enqueued + " Failed to Enqueue :" + m_failed ;
	}
	
	 /**
     * This Method Is Responsible For Getting The  PDF Report From A Jasper Process
     * @param ProcessID
     * @param fromDate
     * @param toDate
     * @param C_BPartner_ID
     * @param isSOTrx
     * @param trxName
     * @return
     */
    public  File getInvoicePDF(int ProcessID ,
                                        java.sql.Timestamp fromDate ,
                                        java.sql.Timestamp toDate ,
                                        Integer C_BPartner_ID ,
                                        Integer AD_Client_ID,
                                        Integer AD_Org_ID,
                                        Boolean isSOTrx ,
                                        String trxName)     {
       
        if(ProcessID > 0   && isSOTrx != null && trxName != null ) {
       
        	if( C_BPartner_ID == null ){
        		C_BPartner_ID =0; 
        	}
        	
        	ArrayList<ProcessInfoParameter> perameterList = new ArrayList<ProcessInfoParameter>();
            ProcessInfoParameter[] pars=null;
           if( fromDate != null ){
            perameterList.add (new ProcessInfoParameter("FromDate",fromDate, null, null, null));
           }
           if( toDate != null ){
            perameterList.add (new ProcessInfoParameter("ToDate",toDate, null, null, null));
           }
           if( C_BPartner_ID != null && C_BPartner_ID != 0 ){
            perameterList.add (new ProcessInfoParameter( "C_BPartner_ID" , new BigDecimal( C_BPartner_ID ), null, null, null)); 
           }
            perameterList.add (new ProcessInfoParameter("IsSOTrx" ,isSOTrx == Boolean.TRUE ? "Y" : "N", null, null, null));
            
            if( AD_Org_ID != null && AD_Org_ID != 0 ){
            	perameterList.add (new ProcessInfoParameter(I_C_Invoice.COLUMNNAME_AD_Org_ID ,new BigDecimal( AD_Org_ID ), null, null, null)); 
            }
            if( AD_Client_ID != null && AD_Client_ID != 0 ){ 
            	perameterList.add (new ProcessInfoParameter(I_C_Invoice.COLUMNNAME_AD_Client_ID ,AD_Client_ID, null, null, null));
            }
           
            pars = new ProcessInfoParameter[perameterList.size()];
            perameterList.toArray(pars);
            
           
         File file =  WTCUtil.getPDFReportFile( ProcessID, pars );
         
         if( file != null ){
        	 
        	 String directoryName = MSysConfig.getValue("REPORT_PDF_DIRECTORY_NAME","");
        	return  WTCUtil.moveToHomeDir(file, directoryName);
         }
          
            
            return null;
        }
        else{
            log.log(Level.SEVERE,"Inforamtion Not Sufficient For Getting The Attachemnt Object.");
            return null;
        }
    }
   
    /**
     * 
     * @return MMailText po object 
     * @throws Exception
     */
   private MMailText getMailTemplate( Integer mailTemplate , int AD_Client_ID , Boolean receivable ) throws Exception {
	   
	   MMailText template = null;
		if( mailTemplate == null ){
			MClient cl = new MClient(ctx, null);
			if( receivable ){
				
				 template = WTCUtil.getMailTemplate( AD_Client_ID , EagleConstants.MISSING_RECEIVABLE_FORM_MAIL_TEMPLATE, ctx, get_TrxName() );
				 
				
				if( template == null ){
					
					String logStr =  Msg.getMsg(ctx , EagleConstants.MAIL_TEMPLATE_NOT_CONFIGURED , new Object[]{ EagleConstants.MISSING_RECEIVABLE_FORM_MAIL_TEMPLATE  , cl.getName()});
					
					addLog( logStr );
					throw new IllegalArgumentException( logStr );
				}
				
			} else {
				
				 template = WTCUtil.getMailTemplate( AD_Client_ID , EagleConstants.MISSING_ISUUABLE_FORM_MAIL_TEMPLATE, ctx, get_TrxName() );
				
				if( template == null ){
					
					String logStr =  Msg.getMsg(ctx , EagleConstants.MAIL_TEMPLATE_NOT_CONFIGURED , new Object[]{ EagleConstants.MISSING_ISUUABLE_FORM_MAIL_TEMPLATE   , cl.getName()}); 
					addLog( logStr );
					throw new IllegalArgumentException( logStr ); 
				}
			}
		} else {
			
			return  new MMailText(ctx, mailTemplate, get_TrxName() );
		}
		
		return template;
   }
   
   
   /**
    * find out the business partner has any invoice's based on the parameters have expecting the Forms and returns the bpartnerid again
    * if bpartner is null or zero returns the list of bpartner id's who have invoices expecting the forms
    * @param bpartnerId
    * @return array of BpartnerId('s)
    */
   private Integer[] getBusinessPartnersHaveMissingForms( Integer bpartnerId , int ad_client_ID , Integer ad_Org_ID ){
	   
	   List<Integer> bps = new ArrayList<Integer>();
	   //20111214515
	   String sql = " SELECT  DISTINCT( cb.C_BPartner_ID ) " + 
	   " From C_Invoice ci " +
	   " JOIN C_BPartner cb ON (ci.C_BPartner_ID = cb.C_BPartner_ID ) " +
	   " Where  ci.AD_Client_ID = ? AND ci.formstatus = 'E' AND ci.docstatus = 'CO'" + 
	   " AND ci.isSOTrx = ? " +												    //1
	   " AND (  CASE " +
	   " WHEN  (( ? :: timestamp  IS null) AND ( ? :: timestamp  IS null)) THEN  1=1" +   					// fromdate,todate 3,4
	   " ELSE   ( CASE WHEN  ( ? :: timestamp  IS null ) THEN    ci.DateInvoiced <= ? " + 		// fromdate,todate 5,6
	   " ELSE ( CASE WHEN  ( ? :: timestamp IS null ) THEN    ci.DateInvoiced   >=  ? " +		//todate,fromdate  7,8
	   " ELSE  ci.DateInvoiced BETWEEN ? AND ?  END) END ) END )" ;				// fromdate,todate 9,10
	   
	   if( bpartnerId !=null && bpartnerId != 0 ){
		   sql = sql + " AND cb.C_BPartner_ID = ? " ;
	   }
	   
	   if( ad_Org_ID != null ){
		   sql = sql + " AND ci.AD_Org_ID = ? " ;
	   }
	   
	   PreparedStatement pstmt = null;
	   ResultSet rs = null;

	   try{
		   pstmt  = DB.prepareStatement(sql , get_TrxName());
		   pstmt.setInt(1,  ad_client_ID );
		   pstmt.setString(2,  p_receivable == Boolean.TRUE ? "Y" : "N");
		   pstmt.setTimestamp(3 ,  p_FromDate ); 
		   pstmt.setTimestamp(4 ,  p_ToDate );
		   pstmt.setTimestamp(5 ,  p_FromDate );
		   pstmt.setTimestamp( 6 ,  p_ToDate );
		   pstmt.setTimestamp(7 ,  p_ToDate );
		   pstmt.setTimestamp(8 ,  p_FromDate );
		   pstmt.setTimestamp(9 ,  p_FromDate );
		   pstmt.setTimestamp(10 ,  p_ToDate );
		   int index =11; 
		   if( bpartnerId !=null && bpartnerId != 0 ){
			   pstmt.setInt( index++ ,  bpartnerId );
		   }

		   if( ad_Org_ID != null ){
			   pstmt.setInt( index++ ,  ad_Org_ID );
		   }
		   rs = pstmt.executeQuery();
		   
		   
		   while( rs.next() ){
			   
			   bps.add( rs.getInt(1));
		   }
		   
	   } catch( Exception e ){
		   log.severe( e.getMessage());
	   }finally{
		   DB.close(rs, pstmt);
	   }
	   
		   return bps.toArray( new Integer[bps.size()] );
   }

   
   private void enqueueMailForBps( Integer[] bps , MMailText template , MUser fromUser ){
	   
	   if( fromUser == null || (fromUser!= null && fromUser.get_ID() ==0 )){

		   fromUser = this.getDefaultUser();
	   }
	   
	   ArrayList<Object> toList = new ArrayList<Object>();
		ArrayList<Object> ccList = new ArrayList<Object>();
		ArrayList<Object> bccList = new ArrayList<Object>();
		HashMap<String, String> eventMap = new HashMap<String,String>();
		for( Integer partner : bps ){

			MBPartner bp = new MBPartner(ctx, partner , get_TrxName() );
			MUser[] users = bp.getContacts( Boolean.TRUE );
			
			if( users.length < 0 ){
				log.log( Level.SEVERE, "Tax Form reminder for the business partner " + bp.getName() + " is not enqued as Email address is not configured " );
				continue;
			}
			
			MUser toUser = bp.getContacts( Boolean.TRUE )[0];
			toList.add( toUser );
			File reportFile = getInvoicePDF(p_ReportProcess_ID, p_FromDate, p_ToDate, partner,p_AD_Client_ID , p_ADOrg_ID , p_receivable, get_TrxName() );
			//20111214515
			boolean enqueued = Boolean.FALSE;
			
			if( reportFile != null ) {
				
				eventMap.put( EMailUtil.INPUT_FILES, reportFile.getAbsolutePath() );
				String mailPropertyText = EMailUtil.getEventData(eventMap);
				template.setUser( toUser );
				template.setBPartner( bp );
				
				try{
					enqueued = EMailUtil.enqueueEmail( fromUser,
														toList, 
														ccList, 
														bccList, 
														template.getMailHeader(), 
														template.getMailText(),
														I_C_BPartner.Table_ID, 
														partner, 
														mailPropertyText, 
														get_TrxName());



				} catch( Exception e ){
					log.severe( e.getMessage() );
				}
			}
			
			if( enqueued ){
				m_enqueued++;
			}else {
				m_failed++;
			}
		}
   }
   
   /**
    * 
    * @param template
    * @param fromUser
    * @param toUser
    */
   private void enqueueConslidateMail( MMailText template , MUser fromUser , MUser toUser){

	   ArrayList<Object> toList = new ArrayList<Object>();
	   ArrayList<Object> ccList = new ArrayList<Object>();
	   ArrayList<Object> bccList = new ArrayList<Object>();
	   HashMap<String, String> eventMap = new HashMap<String,String>();



	   try{

		   if( toUser == null || (toUser!= null && toUser.get_ID() ==0 )){
			   MClient cl = new MClient(ctx, p_AD_Client_ID, null );
			   String email = cl.getRequestEMail();
			   toList.add( email );
		   } else {
			   toList.add( toUser );
		   }

		   if( fromUser == null || (fromUser!= null && fromUser.get_ID() ==0 )){

			   fromUser = this.getDefaultUser();
		   }

		   File reportFile = getInvoicePDF( p_ReportProcess_ID, p_FromDate, p_ToDate, null,p_AD_Client_ID , p_ADOrg_ID, p_receivable, get_TrxName() );
		   //20111214515
		   boolean enqueued = Boolean.FALSE;
		   
		   if( reportFile != null ) {
			   
			   eventMap.put( EMailUtil.INPUT_FILES, reportFile.getAbsolutePath() );
			   String mailPropertyText = EMailUtil.getEventData(eventMap);
			   template.setUser( toUser );

			   enqueued = EMailUtil.enqueueEmail( fromUser,
														   toList, 
														   ccList, 
														   bccList, 
														   template.getMailHeader(), 
														   template.getMailText(),
														   MUser.Table_ID, 
														   fromUser.get_ID(), 
														   mailPropertyText, 
														   get_TrxName());
		   }

		   if( enqueued ){
			   m_enqueued++;
		   }else {
			   m_failed++;
		   }

	   } catch( Exception e ){
		   log.severe( e.getMessage() );
		   m_failed++;
	   }
   }
   
   private MUser getDefaultUser(){

			MUserRoles roles = new Query( ctx, I_AD_User_Roles.Table_Name ,
										I_AD_User_Roles.COLUMNNAME_AD_Role_ID + " = 0  AND "+  I_AD_User_Roles.COLUMNNAME_AD_Client_ID + " IN (0, " + p_AD_Client_ID +" )" , 
											null ).setOrderBy( "ORDER BY AD_Role_ID DESC").setOnlyActiveRecords( Boolean.TRUE ).first();

			return (MUser) roles.getAD_User();
   }
}
