/**
 * 
 */
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_InvoiceTax;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceTax;
import org.compiere.model.MTax;
import org.compiere.model.MWTCTaxLine;
import org.compiere.model.Query;
import org.compiere.model.X_C_DocType;
import org.compiere.model.X_C_Invoice;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TaxUtil;

/**
 * Picks up the inovoice's of the business partner/invoice which are expecting the Form,
 * and recalculates the tax amount which has got rebated as expecting the form and prepare's
 * new ARTaxDebitNote.
 * @author PhaniKiran.Gutha
 *
 */
public class InvoiceTaxRecalculation extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: InvoiceTaxRecalculation.java 1009 2012-02-09 09:16:13Z suman $";

	private Integer p_C_Invoice_ID=null;
	private Integer p_C_BPartner_ID=null;
	
	 /** Manual Selection        */
    private boolean     p_Selection = false;
    
    /** Number of Invoices      */
    private int         m_created = 0;
	
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		
		for (int i = 0; i < para.length; i++){
			
			String name = para[i].getParameterName();
			
			if( name.equals( I_C_Invoice.COLUMNNAME_C_Invoice_ID ) ){
				
				Object obj= para[i].getParameter();
				BigDecimal db = obj != null ? (BigDecimal)obj : null;
				p_C_Invoice_ID = db != null ? db.intValue()  : null; 
				
			}if( name.equals( I_C_Invoice.COLUMNNAME_C_BPartner_ID ) ){
				
				Object obj= para[i].getParameter();
				BigDecimal db = obj != null ? (BigDecimal)obj : null;
				p_C_BPartner_ID = db != null ? db.intValue()  : null; 
				
			}  else if (name.equals("Selection")){
				p_Selection = "Y".equals(para[i].getParameter());
			}else {
				
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}

	}

	/**
	 * Creates DebitNote('s) for the invoices selected in TaxDebitNote Creation Form.
	 * Create DebitNote manually for the 
	 */
	protected String doIt() throws Exception {
		
		if(!p_Selection ){
			
			return  this.createARTaxDebitNotes( p_C_BPartner_ID , p_C_Invoice_ID, getCtx(), get_TrxName() );
			
		} else {
			
			 String sql = "SELECT c.C_Invoice_ID FROM C_Invoice c, T_Selection "
		            + "WHERE c.DocStatus='CO'  AND c.AD_Client_ID=? " //AND c.IsSOTrx='Y'
		            + "AND c.C_Invoice_ID = T_Selection.T_Selection_ID " 
		            + "AND T_Selection.AD_PInstance_ID=? ";
		        
		        PreparedStatement pstmt = null;
		        
		        try
		        {
		            pstmt = DB.prepareStatement(sql, get_TrxName());
		            pstmt.setInt(1, Env.getAD_Client_ID(getCtx()));
		            pstmt.setInt(2, getAD_PInstance_ID());
		            ResultSet rs = pstmt.executeQuery();
		            
		            while (rs.next())
		            {
		            	this.createARTaxDebitNotes( null , rs.getInt(1), getCtx(), get_TrxName() );
		            	m_created++;
		            }
		        }
		        catch (Exception ex)
		        {
		            log.log(Level.SEVERE, sql, ex);
		        }
		        finally
		        {
		            try
		            {
		                pstmt.close();
		            }
		            catch (Exception ex)
		            {
		                log.log(Level.SEVERE, "Could not close prepared statement");
		            }
		        }
		        return "@Created@ = " + m_created;
		    }
		}
	
	/**
	 * @BugNo 1621
	 * 
	 * <li> Creates Debit Notes for the Invoice / Invoice's of the Business partner </li>
	 * <li> DB Transaction should be handled explicitly, by the caller </li>
	 * @param C_Invoice_ID
	 * @param ctx
	 * @param trxName
	 * @return
	 * @throws Exception
	 */
	public String createARTaxDebitNotes( Integer C_BPartner_ID ,Integer C_Invoice_ID , Properties ctx , String trxName ) throws Exception {
		
		
		if( C_Invoice_ID == null && C_BPartner_ID == null ){
			
			log.severe( "Inovice and businesspartner passed as null" );
			return  Msg.getMsg( ctx , EagleConstants.INVOICE_TAX_RECALCULATION_BPARTNER );
			
			
		} else if( C_Invoice_ID != null ){
			
			MInvoice Inv = new MInvoice( ctx, C_Invoice_ID, trxName );
			
			if( Inv.getWTC_FormType_ID() == 0 || !Inv.getFormStatus().equals( X_C_Invoice.FORMSTATUS_Expected )){
				return Msg.getMsg(ctx, EagleConstants.TAX_RECALC_NOT_REQ_TOISRE_FORM , new Object[]{ Inv.getDocumentInfo() } );
			}
			
			HashMap<Integer,HashMap<Integer,BigDecimal>> pendingTaxAmt= this.calculatePendingTax( Inv );
			return this.createTaxDebitNote( 	Inv,
										pendingTaxAmt,
										trxName, 
										ctx);
			
		} else if( C_Invoice_ID == null && C_BPartner_ID != null ){
			
			List<MInvoice> invoices = InvoiceTaxRecalculation.getFormExpectedInvoices(C_BPartner_ID, ctx, trxName);
			
			for( MInvoice Inv : invoices ){
				
				HashMap<Integer,HashMap<Integer,BigDecimal>> tmpPendingTaxAmt = this.calculatePendingTax( Inv ) ;
				return this.createTaxDebitNote( Inv,
											tmpPendingTaxAmt,
											trxName, 
											ctx);
			}
		}
		
		return null;
		

	}
	
	/**
	 * Bug No 1621
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @param bp
	 * @param pendingTaxAmt
	 * @param salesRep_ID
	 * @param trxName
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	private String createTaxDebitNote( MInvoice arInv,
										HashMap<Integer,HashMap<Integer,BigDecimal>> pendingTaxAmt,
										String trxName,
										Properties ctx) throws Exception{
//		Create Invoice
		MInvoice invoice = new MInvoice (ctx, 0, trxName );
		invoice.setClientOrg( arInv.getAD_Client_ID() , arInv.getAD_Org_ID() );
		invoice.setC_DocTypeTarget_ID( this.getDebitNote( arInv , ctx ) );	
		invoice.setBPartner( (MBPartner) arInv.getC_BPartner() );
		invoice.setSalesRep_ID( arInv.getSalesRep_ID() );
		invoice.setC_Currency_ID( arInv.getC_Currency_ID());
		invoice.setRef_Invoice_ID( arInv.get_ID() );
		invoice.setIsSOTrx( arInv.isSOTrx() );
		
		if( invoice.save() ){
			
			Set set = pendingTaxAmt.entrySet();
			Iterator i = set.iterator();
			
			while(i.hasNext()) {
				
				Map.Entry<Integer,HashMap<Integer,BigDecimal>> me = (Map.Entry<Integer,HashMap<Integer,BigDecimal>>)i.next();
				HashMap<Integer,BigDecimal> taxhm =  me.getValue() ;
				
				Set taxset = taxhm.entrySet();
				Iterator taxIterator = taxset.iterator();
				while( taxIterator.hasNext() ) {
					Map.Entry<Integer,BigDecimal> taxme = ( Map.Entry<Integer,BigDecimal> )taxIterator.next();
					MInvoiceTax retValue = new MInvoiceTax( ctx, 0 , trxName );
					retValue.set_TrxName(trxName);
					retValue.setAD_Org_ID( arInv.getAD_Org_ID() );
					retValue.setAD_Org_ID( arInv.getAD_Org_ID() );
					retValue.setC_Invoice_ID( invoice.get_ID() );
					retValue.setC_Tax_ID( taxme.getKey() );
					retValue.setIsTaxIncluded( Boolean.FALSE );
					retValue.setTaxBaseAmt( Env.ZERO );
					retValue.setTaxAmt( taxme.getValue() );
					retValue.setC_InvoiceLine_ID( me.getKey() );

					if( !retValue.save() ){
						throw new IllegalStateException(  Msg.getMsg( ctx, EagleConstants.TAX_RECALC_FAILED_TO_CREATE_TAX_FOR_DEBITNOTE )  );
					}
				}

			}
			
		} else {
			throw new IllegalStateException( Msg.getMsg( ctx, EagleConstants.TAX_RECALC_FAILED_TO_CREATE_TAXDEBITNOTE ) );
		}
		
		MInvoiceTax[] taxes = invoice.getTaxes( Boolean.TRUE );
		BigDecimal grandTotal = Env.ZERO;
		
		for( MInvoiceTax iTax : taxes ){
			grandTotal = grandTotal.add( iTax.getTaxAmt() );
			
		}
		
		invoice.setGrandTotal( grandTotal );
		
		if( !invoice.save() ){
			
			throw new IllegalStateException( Msg.getMsg( ctx, EagleConstants.TAX_RECALC_FAILED_TO_CREATE_TAXDEBITNOTE ) );
			
		} else {
			
			Boolean processed = invoice.processIt( DocAction.ACTION_Complete );
			
			if( !processed || !invoice.getDocStatus().equals( X_C_Invoice.DOCSTATUS_Completed ) ){
				throw new IllegalStateException( Msg.getMsg( ctx, EagleConstants.TAX_RECALC_FAILED_TO_CREATE_TAXDEBITNOTE , new Object[]{ invoice.getProcessMsg()} ) );  
			}
		}
		
		arInv.setFormStatus( X_C_Invoice.FORMSTATUS_Settled );
		
		if( !arInv.save() ){
			throw new IllegalStateException( Msg.getMsg(ctx, EagleConstants.FAILED_TO_SETTLE_INVOICE , new Object[]{ arInv.getDocumentInfo() } ) );
		}
		
		addLog( invoice.get_ID(), invoice.getDateInvoiced() , null ,invoice.getDocumentInfo() );
		return invoice.getDocumentNo();
	}
	
	
	/**
	 * @BugNo : 1621
	 * calculate pending tax which needs to be calculate as the form submitted for the invoice
	 * @param Invoice  -- MInvoice
	 * @return
	 */
	private HashMap<Integer,HashMap<Integer,BigDecimal>> calculatePendingTax(  MInvoice arInv ){
		
		
		MInvoiceTax[] iTaxes = arInv.getTaxes( Boolean.TRUE );
		HashMap<Integer,HashMap<Integer,BigDecimal>> hm = new HashMap<Integer,HashMap<Integer,BigDecimal>>();
		
		int formType = arInv.getWTC_FormType_ID();
		
		if( formType !=0 ){
			
			for( MInvoiceTax iTax : iTaxes ){
				
				if( !hm.containsKey( iTax.getC_InvoiceLine_ID() ) ){
					
					HashMap<Integer,BigDecimal> taxhm = new HashMap<Integer,BigDecimal>();
					hm.put( iTax.getC_InvoiceLine_ID(), taxhm );
				}
				
				createPendingTaxMap(iTax, hm.get( iTax.getC_InvoiceLine_ID() ) , formType, arInv.getPrecision() );
				
				
			
			}
			
			
		}
		
		return hm;
	}
	
	/**
	 *  Calculate
	 * @param iTax
	 * @param hm
	 * @param FormType
	 * @param precision
	 */
	public void createPendingTaxMap( MInvoiceTax iTax, 
			HashMap<Integer,BigDecimal> hm , 
			Integer FormType , 
			int precision ){
		
		if( iTax != null && iTax.get_ID() !=0 && hm == null ){
			hm = new HashMap<Integer,BigDecimal>();
		} else if( iTax == null ){
			return;
		}
		
		MTax cTax = (MTax) iTax.getC_Tax();
		BigDecimal existingTaxAmt =iTax.getTaxAmt();
		BigDecimal actualTaxAmt = Env.ZERO;
		BigDecimal newTaxAmt = Env.ZERO;
		List<MWTCTaxLine> taxLines = cTax.getTaxLines( cTax.get_ID(), 
												Boolean.TRUE, 
												FormType );
		
		if( taxLines != null && !taxLines.isEmpty() ){
			
			newTaxAmt  = cTax.calculateTax( iTax.getTaxBaseAmt(), 
														iTax.isTaxIncluded(), 
														precision, 
															Boolean.FALSE,
															null );
			
			actualTaxAmt = newTaxAmt.subtract( existingTaxAmt );
			
			
			if( hm.containsKey( cTax.get_ID() ) ){
				BigDecimal existingValue = hm.get( cTax.get_ID() );
				existingValue.add( actualTaxAmt );
				hm.put( cTax.get_ID() , existingValue );
			} else {
				
				hm.put( cTax.get_ID() ,  actualTaxAmt  );
			}
			
			MTax[] childTaxes = ( (MTax)iTax.getC_Tax() ).getChildTaxes( Boolean.FALSE );
			
			for( MTax childTax : childTaxes ){
				
				

				BigDecimal childTaxAmt = childTax.calculateTax(newTaxAmt, iTax.isTaxIncluded(), precision , null );
				MInvoiceTax existTax = new Query( iTax.getCtx(), iTax.get_TableName(), 
						I_C_InvoiceTax.COLUMNNAME_C_Invoice_ID + " = " + iTax.getC_Invoice_ID() + " AND "+
						I_C_InvoiceTax.COLUMNNAME_C_InvoiceLine_ID + " = " + iTax.getC_InvoiceLine_ID() +" AND " +
						I_C_InvoiceTax.COLUMNNAME_C_Tax_ID + " = " + childTax.getC_Tax_ID() , iTax.get_TrxName() ).first();

				if( existTax != null ){
					
					BigDecimal childExistingTaxAmt = existTax.getTaxAmt();
					BigDecimal finalTaxAmt = childTaxAmt.subtract( childExistingTaxAmt ); 

					if( hm.containsKey( childTax.get_ID() ) ){
						BigDecimal existingValue = hm.get( childTax.get_ID() );
						existingValue.add( finalTaxAmt );
						hm.put( childTax.get_ID() , existingValue );
					} else {

						hm.put( childTax.get_ID() ,  finalTaxAmt  );
					}
				}
			}
			
		}
		
		
		
	}
	/**
	 * 
	 * @param C_BPartner_ID
	 * @param ctx
	 * @param trxName
	 * @return the list of MInvoice's which are expected to submit the forms and completed
	 */
	public static List<MInvoice> getFormExpectedInvoices( int C_BPartner_ID , Properties ctx , String trxName ){
		
		return new Query( ctx,
				          I_C_Invoice.Table_Name , 
				          I_C_Invoice.COLUMNNAME_FormStatus + " = '" + X_C_Invoice.FORMSTATUS_Expected + "' AND " + I_C_Invoice.COLUMNNAME_DocAction + " IN ( 'CO','CL')  ", 
				          trxName ).list();
	}

	/**
	 * This method return the Appropriate Debit Note for the Invoice Document Type
	 * @param Inv  MInvoice
	 * @return  AR/AP Tax Debit Note based on the AR/AP Invoice
	 */
	private int getDebitNote( MInvoice Inv , Properties ctx){
		
		int debitNoteId =  0;
		MDocType dt = (MDocType) Inv.getC_DocType();
		if( Inv.getC_DocType().getDocBaseType().equals(X_C_DocType.DOCBASETYPE_APInvoice ) ){
			
			debitNoteId = TaxUtil.getDocType(  Inv.getAD_Client_ID() , ctx , EagleConstants.AP_TAX_DEBIT_NOTE ) ;
			
		} else if(  Inv.getC_DocType().getDocBaseType().equals(X_C_DocType.DOCBASETYPE_ARInvoice ) ){
			
			debitNoteId = TaxUtil.getDocType(  Inv.getAD_Client_ID() , ctx , EagleConstants.AR_TAX_DEBIT_NOTE ) ;
			
		}
		
		return debitNoteId;
	}
}
