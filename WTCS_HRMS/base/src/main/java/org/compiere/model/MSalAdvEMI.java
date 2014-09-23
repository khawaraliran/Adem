package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.eevolution.model.MHRPeriod;


public class MSalAdvEMI extends X_HR_Sal_Adv_EMI {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: MSalAdvEMI.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	private static final long serialVersionUID = 167858585L;


	public MSalAdvEMI(Properties ctx, int HR_Sal_Adv_EMI_ID, String trxName) {
		super(ctx, HR_Sal_Adv_EMI_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MSalAdvEMI(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	
	protected boolean beforeSave (boolean newRecord)
	{
		X_HR_Sal_Adv_Req advreq=new X_HR_Sal_Adv_Req(this.getCtx(), this.getHR_Sal_Adv_Req_ID(), this.get_TrxName());
		advreq.getC_BPartner_ID();
		advreq.getrequestdate();
		
  		if(is_ValueChanged(X_HR_Sal_Adv_EMI.COLUMNNAME_skipinpayslip)) {
  			if(this.isskipinpayslip()) {
				createX_HR_Sal_Adv_EMI(true);
  		  	}else  {
  				BigDecimal emiAmount=this.getemiamount();
  				if(emiAmount != null) {
  				  this.setactualemiamount(emiAmount);
  				}else {
  				  this.setactualemiamount(new BigDecimal(0));
  				}
  				 deleteEmiEntry();
  			}
  		}
  		
  		if(is_ValueChanged(MSalAdvEMI.COLUMNNAME_ispartial))  {
  			if(this.ispartial()){
  				if((this.getactualemiamount().compareTo(this.getemiamount())) <=0 )	{
  						createX_HR_Sal_Adv_EMI(false);
  				}else {
  					differenceOfEmiAmounts(true);
  				}
  			}else  {
  				if((this.getactualemiamount().compareTo(this.getemiamount())) <=0 )	{
  					this.setactualemiamount(this.getemiamount());
  					deleteEmiEntry();
  				}else {
  					differenceOfEmiAmounts(false);
  					this.setactualemiamount(this.getemiamount());
  				}
  			}
  		}else if(is_ValueChanged(MSalAdvEMI.COLUMNNAME_actualemiamount) && this.ispartial()) 	{
  			String msg=Msg.getMsg(Env.getCtx(),EagleConstants.NOT_ALLOW_TO_CHANGE_EMI_ACTUAL_AMOUNT);
  			throw new AdempiereException(msg);
  		}
		return true;
	}

	
	
	
	/**
	 * This Method Will Create X_HR_Sal_Adv_EMI Entry
	 * @param isSkipPaySlip
	 */
	private void createX_HR_Sal_Adv_EMI(Boolean isSkipPaySlip) 
	{
		X_HR_Sal_Adv_EMI salAdvEmi = new X_HR_Sal_Adv_EMI(this.getCtx(), 0, this.get_TrxName());
			BigDecimal emiAmount=this.getemiamount();
			
			salAdvEmi.setHR_Sal_Adv_Req_ID(this.getHR_Sal_Adv_Req_ID());
			
			String whereClause = MSalAdvReq.COLUMNNAME_HR_Sal_Adv_Req_ID+"="+this.getHR_Sal_Adv_Req_ID();
			List<MSalAdvEMI> salEmi = new Query(getCtx(),MSalAdvEMI.Table_Name,whereClause ,get_TrxName()).
															setOrderBy(MSalAdvEMI.COLUMNNAME_LineNo+" DESC").
															list();
			MSalAdvEMI lastEmi = salEmi.get(0);
			
			Integer periodID =  null;
			int lineNO = 0;
			
			if( lastEmi != null ) {
				periodID = lastEmi.getHR_Period_ID();
				lineNO   = lastEmi.getLineNo();
			}else {
				log.log(Level.SEVERE, "we Don't Have Emi Object");
				String msg=Msg.getMsg(Env.getCtx(), EagleConstants.ADVANCE_SALARY_EMI_NOT_FOUND);
				throw new AdempiereException(msg);
			}
			
			
			//Checking this Emi is having Period Or Not
			if(periodID != null && periodID.intValue() < 1 ) {
				String msg=Msg.getMsg(Env.getCtx(), EagleConstants.PeriodNotFound);
		    	throw new AdempiereException(msg);
			}
			
			periodID = MHRPeriod.getNextPeriodId(this.getCtx(), periodID, this.get_TrxName());
			lineNO=lineNO+1;	
					
		    if(periodID.intValue() < 1) {
		    	String msg=Msg.getMsg(Env.getCtx(), EagleConstants.PeriodNextNotFound);
		    	throw new AdempiereException(msg);
		    }
			salAdvEmi.setLineNo(lineNO);
			if(emiAmount != null) {
				if(isSkipPaySlip){
			      salAdvEmi.setemiamount(emiAmount);
			      salAdvEmi.setactualemiamount(emiAmount);
				}else{
					BigDecimal actualEmiAmount=this.getactualemiamount();
					
					if(actualEmiAmount != null)	{
						BigDecimal emiFinalAmount=emiAmount.subtract(actualEmiAmount);
						if(emiFinalAmount == null){
							emiFinalAmount=Env.ZERO;
						}
						
						salAdvEmi.setemiamount(emiFinalAmount);
						salAdvEmi.setactualemiamount(emiFinalAmount);
					}
				}
			}else {
				salAdvEmi.setemiamount(new BigDecimal(0));
				salAdvEmi.setactualemiamount(new BigDecimal(0));
			}
			
			salAdvEmi.setskipinpayslip(false);
			salAdvEmi.setispartial(false);
			salAdvEmi.setHR_Period_ID(periodID);
			salAdvEmi.sethr_ref_sal_adv_emi_id(this.getHR_Sal_Adv_EMI_ID());
			
			if(!salAdvEmi.save()){
				log.log(Level.SEVERE, "Unable To Create EMI Entry ");
			}
	}
	
	
	
	private ArrayList<MSalAdvEMI> getSalAdvEMI() {
		String whereClause=MSalAdvEMI.COLUMNNAME_LineNo +" > "+this.getLineNo() +" AND "+
						   MSalAdvEMI.COLUMNNAME_HR_Sal_Adv_Req_ID+" = "+this.getHR_Sal_Adv_Req_ID();
		
		ArrayList<MSalAdvEMI> emiList=(ArrayList)new Query(Env.getCtx(), MSalAdvEMI.Table_Name, whereClause, this.get_TrxName()).setOrderBy(MSalAdvEMI.COLUMNNAME_HR_Sal_Adv_EMI_ID).list();
		
		return emiList;
	}
	
	/**
	 *  This Delete Function CAn Be CAlled From Two Places , Of Before Save Method.
	 *  In Each Case It Deletes EMI Entries But
	 *  Case 1: If IsSkipPayment is Selected Then a New Entry Is Created , If It is De_selected That Entry Is deleted.
	 *  Case 2: If IsPartial is Selected Then a New Entry Is Created , If It is De_selected That Entry Is deleted.
	 *  
	 *  But We Have Another Situation Is If IsPartial is selected Then IsSkipPayment will be disappeared ,
	 *  But At That Situation If IsSkipPayment selected and a New Entry is Created.
	 *  
	 *  We Have Two new EMI's For This Particular EMI , When WE DEselecting This Newly Created 
	 *  EMI's Should Be Deleted In the Order LIFO model
	 *  i.e., WE Can't Select  IsSkipPayment After Selecting IsPartial , But We Can Select IsPartial After 
	 *  Selecting IsSkipPayment. Vice Versa We Can't De_select IsSkipPayment Without De_selecting IsPartial
	 */
	private void deleteEmiEntry()
	{
		 String whereClause=MSalAdvEMI.COLUMNNAME_hr_ref_sal_adv_emi_id+"="+ this.getHR_Sal_Adv_EMI_ID();
		 MSalAdvEMI salEmiList=new Query(Env.getCtx(),MSalAdvEMI.Table_Name,whereClause,this.get_TrxName()).setOrderBy(MSalAdvEMI.COLUMNNAME_HR_Sal_Adv_EMI_ID+"  DESC ").first(); 
		 
		 if(salEmiList != null)
		 {
			 if(!salEmiList.delete(true))
			 {
				 log.log(Level.SEVERE, "EMI Entry Can't Be Deleted,EMI ID IS: "+salEmiList.getHR_Sal_Adv_EMI_ID());
			 }
		 }
	}
	
	
	
	
	/**
	 * This Emi Will Adjust Emi Amounts Between IT's DEcendents.
	 * @param isParial
	 */
	private void differenceOfEmiAmounts(Boolean isParial) 
	{
		BigDecimal diff=null;
		if((this.getactualemiamount()) != null && (this.getemiamount()) != null)
		{
		  diff=(this.getactualemiamount()).subtract(this.getemiamount());
		}
		
		
		//Here We are Getting The X_HR_Sal_Adv_EMI entries , which are grater than This X_HR_Sal_Adv_EMI Line Number
		ArrayList<MSalAdvEMI> list  =(ArrayList<MSalAdvEMI> )getSalAdvEMI();
		
		if(list != null)
		{
		 for (X_HR_Sal_Adv_EMI value : list) 
   		 {
			 if(value != null)
			 {
					if(diff != null && (value.getemiamount()) != null)
					{
						if(isParial)
						{
							if((diff.compareTo(value.getemiamount())) <=0 )
							{
								BigDecimal resultentValue=value.getemiamount().subtract(diff);
								value.setactualemiamount(resultentValue);
								if(! value.save())
								{
								   log.log(Level.SEVERE, "X_HR_Sal_Adv_EMI=>actualemiamount  Is Not Updated With New Value:"+resultentValue);
								}
								// here updating only one po form the list.
								break;
							}
							else
							{
								value.setactualemiamount(new BigDecimal(0));
								diff=diff.subtract(value.getemiamount());
								if(!value.save())
								{
								   log.log(Level.SEVERE, "X_HR_Sal_Adv_EMI=>actualemiamount  Is Not Updated With New Value:"+0);
								}
							}
						}
						else
						{
							if((diff.compareTo(value.getactualemiamount())) == 1 )
							{
								
								BigDecimal resultentValue=value.getemiamount();
								
								if(resultentValue == null)
								{
									resultentValue=Env.ZERO;
								}
								diff=diff.subtract(resultentValue);
								
								resultentValue=resultentValue.add(value.getactualemiamount());
								value.setactualemiamount(resultentValue);
								if(!value.save())
								{
								   log.log(Level.SEVERE, "X_HR_Sal_Adv_EMI=>actualemiamount  Is Not Updated With New Value:"+resultentValue);
								}
							}
							else
							{
								BigDecimal actualAmount=value.getactualemiamount();
								if(actualAmount == null)
								{
									actualAmount=Env.ZERO;
								}
								actualAmount=actualAmount.add(diff);
								value.setactualemiamount(actualAmount);
								if(!value.save())
								{
								   log.log(Level.SEVERE, "X_HR_Sal_Adv_EMI=>actualemiamount  Is Not Updated With New Value:"+0);
								}
								break;
							}
						}
					}
			 }
	     }
	   }
    }
}
