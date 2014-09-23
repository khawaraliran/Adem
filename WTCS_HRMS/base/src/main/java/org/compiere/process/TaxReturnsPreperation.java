/**
 * 
 */
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_InvoiceTax;
import org.compiere.model.I_C_Period;
import org.compiere.model.I_C_Tax;
import org.compiere.model.I_WTC_TaxIndicator;
import org.compiere.model.MInvoiceTax;
import org.compiere.model.MPeriod;
import org.compiere.model.MTax;
import org.compiere.model.MWTCTaxReturnForm;
import org.compiere.model.MWTCTaxReturnFormLine;
import org.compiere.model.X_WTC_TaxIndicator;
import org.compiere.model.X_WTC_TaxReturnFormLine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TaxUtil;
import org.ecosoft.model.I_LCO_PaymentWithholding;
import org.ecosoft.model.MLCOPaymentWithholding;

/**
 * @author phani
 * 
 * 
 * Modifications 
 * 
 * Issue No 	Author		ChangeID		Description 
 * ******************************************************
 * 1717			Ranjit		20111207034     SQL statements is corrected
 * 1753         Phani		20111212823		Consider collectableon of the tax indicator while retriving invoice taxes
 * 
 *
 */
public class TaxReturnsPreperation extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: TaxReturnsPreperation.java 1009 2012-02-09 09:16:13Z suman $";

	private Integer p_C_Period_ID ;
	private Integer p_taxIndicator_ID;
	private int recordCount=0;
	MWTCTaxReturnForm form = null;
	private Properties ctx = null;
	private String trxName = null;
	private String processMsg = null;
	public String getProcessMsg() {
		return processMsg;
	}


	public void setProcessMsg(String processMsg) {
		this.processMsg = processMsg;
	}


	/**
	 * 
	 */
	public TaxReturnsPreperation() {
	}

	
	public TaxReturnsPreperation( Properties ctx, String trxName ) {
		
		this.ctx = ctx;
		this.trxName = trxName;
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		
		for (int i = 0; i < para.length; i++){
			
			String name = para[i].getParameterName();
			
			if( name.equals( I_WTC_TaxIndicator.COLUMNNAME_WTC_TaxIndicator_ID ) ){
				
				p_taxIndicator_ID = para[i].getParameterAsInt();
				
			} else if(  name.equals( I_C_Period.COLUMNNAME_C_Period_ID ) ){
				
				p_C_Period_ID = para[i].getParameterAsInt();
				
			} else {
				
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		
		log.log(Level.INFO, "Parameters taxindiccator " + p_taxIndicator_ID + "------ Period ----" + p_C_Period_ID );
		this.ctx = getCtx();
		this.trxName = get_TrxName();
		form = new MWTCTaxReturnForm( this.ctx, this.getRecord_ID(), this.trxName );
		form.deleteLines();
		prepareTaxReturnLines(p_taxIndicator_ID, p_C_Period_ID , getRecord_ID() );
		if( processMsg != null ){ //2011141028
			addLog( processMsg );
		}
		addLog( " Tax Return Created no : " + recordCount );
		return  "";//" Tax Return Created no : " + recordCount ;
	}
	
	public boolean prepareTaxReturnLines( int p_taxIndicator_ID , int p_C_Period_ID , int recordId ){
		
		if ( form == null ){
			form = new MWTCTaxReturnForm( this.ctx, recordId , this.trxName );
		}
		 
		String where = null; 
		List<MTax> taxes = TaxUtil.getTaxesOfIndicator( p_taxIndicator_ID , 
														this.ctx,
														where ,
														null );
		
		
		
		BigDecimal grandTotal = Env.ZERO;
		
		for( MTax tax : taxes ){
			
			BigDecimal returnAmtTax =  this.createTaxReturnLines( tax , p_C_Period_ID , recordId ) ;
			grandTotal = grandTotal.add( returnAmtTax );
			
		}
		
		
		BigDecimal inputCredit = form.getInputCredit();
		
			grandTotal = grandTotal.add( inputCredit );
		form.setGrandTotal( grandTotal );
		form.setbroughtforward( inputCredit ); 
		form.setpreparedocument( "Y" );
		if( grandTotal.signum() > 0 ){
			form.setOpenAmt( grandTotal );
		}
		if(form.save()){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Bug No : 1717
		Tax Preperation process should create tax compuation line for the completed invoice's only

	 * @param taxId
	 * @param isSoTrx
	 * @return
	 */
	public List<MInvoiceTax> getInvoiceTaxesOfTax( int taxId , boolean isSoTrx , int periodId ){

		List<MInvoiceTax> iTaxes = new ArrayList<MInvoiceTax>();

		StringBuffer SQL = new StringBuffer( "SELECT cit.* FROM " + I_C_InvoiceTax.Table_Name + " cit"
				+ " JOIN " + I_C_Invoice.Table_Name + " c ON ( c." + I_C_Invoice.COLUMNNAME_C_Invoice_ID + " = " + "cit." + I_C_InvoiceTax.COLUMNNAME_C_Invoice_ID + ") "
				+ " JOIN " + I_C_Tax.Table_Name + " ct ON ( ct." +I_C_Tax.COLUMNNAME_C_Tax_ID + " = " + " cit." + I_C_InvoiceTax.COLUMNNAME_C_Tax_ID + " )"
				+ " JOIN " + I_WTC_TaxIndicator.Table_Name + " wti ON ( wti." + I_WTC_TaxIndicator.COLUMNNAME_WTC_TaxIndicator_ID + " = " + " ct." + I_C_Tax.COLUMNNAME_WTC_TaxIndicator_ID + ")"
				+ " WHERE  c.docstatus IN ( 'CO','CL' ) AND cit."  
				+ I_C_InvoiceTax.COLUMNNAME_C_Tax_ID + " = ?  AND c." + I_C_Invoice.COLUMNNAME_Processed  + " = 'Y' " );

		if( periodId >0 ){
			SQL.append( " AND (  c." + I_C_Invoice.COLUMNNAME_DateInvoiced + " >=  ?  "+" AND c." + I_C_Invoice.COLUMNNAME_DateInvoiced + " <= ? )"  );
		}

		SQL.append( " AND cit." + I_C_InvoiceTax.COLUMNNAME_WTC_TaxReturnForm_ID + " IS NULL AND " );


		if( isSoTrx ){//20111212823

			SQL.append( " CASE WHEN wti.collectableon ='SO' THEN  " )
			.append( "  c.C_DocType_ID IN (select cd.C_DocType_ID from C_DocType cd where cd.docbasetype in ('ARI','APC') ) " )
			.append( " ELSE c.C_DocType_ID IN (select cd.C_DocType_ID from C_DocType cd where cd.docbasetype in ('API','ARC') ) END ");


		}else {//20111212823

			SQL.append( " CASE WHEN wti.collectableon ='PO' THEN c.C_DocType_ID IN " +
			"(select cd.C_DocType_ID from C_DocType cd where cd.docbasetype in ('ARI','APC') )" )
			.append( "  ELSE c.C_DocType_ID IN (select cd.C_DocType_ID from C_DocType cd where cd.docbasetype in ('API','ARC') ) END " );

		}

		PreparedStatement pstmt = DB.prepareStatement( SQL.toString() , null );
		ResultSet rs = null;

		try{

			pstmt.setInt( 1 , taxId );

			if( periodId >0 ){

				MPeriod p = new MPeriod( this.ctx, periodId, null );
				pstmt.setTimestamp(2, p.getStartDate() );
				pstmt.setTimestamp(3, p.getEndDate() );
			}
			rs = pstmt.executeQuery();

			while( rs.next() ){

				MInvoiceTax iTax = new MInvoiceTax( this.ctx, rs, this.trxName );
				iTaxes.add( iTax );
			}


		}catch( Exception e ){

			log.log( Level.SEVERE , e.getMessage() );

		} finally{

			DB.close(rs, pstmt);
		}

		return iTaxes;
	}

	/**
	 * create Tax Return line for the particular tax only when the tax is adjustable
	 * this will be known using the column issalestax in tax entity
	 * @param tax
	 * @return the input-output tax amount
	 */
	private BigDecimal createTaxReturnLines( MTax tax , int periodId , int recordId ){
		
		BigDecimal input 			= Env.ZERO;
		BigDecimal output 			= Env.ZERO;
		BigDecimal paymentInput 	= Env.ZERO;
		BigDecimal paymentOutput	= Env.ZERO;
		
		if( !tax.isSalesTax() ){
			
			input = this.createTaxReturnLine( tax.get_ID(), Boolean.FALSE, Boolean.TRUE ,periodId , recordId );
			
			//
			//  If this tax participate in the TDS calculation then get the tax details from 
			//  LCO_PaymentWitholding which are processed AND is Calc On Payment is true 
			//
			
			paymentInput = this.createPaymentTaxReturnLines ( tax.get_ID(), 
															  Boolean.FALSE, 
															  Boolean.TRUE, 
															  periodId, 
															  recordId );
			
			log.info("Invoice input amount = " + input + ". TDS Payment input amount =  " + paymentInput );
			
			input = input.add( paymentInput );
			
						
		} else {
			
			log.log( Level.WARNING , " Tax is Sales Tax so it cannot be considered as Either Input/Output " + tax );
		}
		
		output = this.createTaxReturnLine( tax.get_ID(), Boolean.TRUE, Boolean.FALSE , periodId , recordId);
		
		paymentOutput = this.createPaymentTaxReturnLines ( tax.get_ID(), 
														   Boolean.TRUE, 
														   Boolean.FALSE, 
														   periodId, 
														   recordId );
		
		
		log.info("Invoice output amount = " + output + ". TDS Payment output amount =  " + paymentOutput );
		
		output = output.add( paymentOutput );
		
 		
		BigDecimal retValue = Env.ZERO;
		
		if( !tax.isSalesTax() ){
			
			retValue = output.subtract( input );
		}else{
			retValue = output;
		}
		return retValue;
	}
	
	/**
	 * <P>
	 * Crates the payment tax return lines. <BR>
	 * 
	 * Get the tax details from the LCO_PaymentWithholding table with following condition :
	 * 
	 *  <LI> 1. If the tax involves in the TDS calculation. </LI> <BR>
	 *  <LI> 2. If the payment withholding entry processed. </LI> <BR>  
	 *  <LI> 3. If the tax is calculated at payment level i.e. Is Calc On Payment = TRUE </LI> <BR>
	 *  </P>
	 *  
	 * @param taxId				- Tax Id 
	 * @param isSoTrx			- TRUE if sales transaction else FALSE
	 * @param paymentInput		- TRUE if payment input else FALSE
	 * @param periodId			- Period Id
	 * @param recordId			- Tax Return Form Id
	 * @return					- Tax Amount
	 */
	private BigDecimal createPaymentTaxReturnLines( int taxId, 
													Boolean isSoTrx,
													Boolean paymentInput, 
													int periodId, 
													int recordId) {//20111212823
		
		List < MLCOPaymentWithholding > paymentTDSTaxList = this.getPaymentProcessedTaxes( taxId, isSoTrx , periodId );
		BigDecimal taxAmt = Env.ZERO;
		BigDecimal baseAmt = Env.ZERO;
		
		if (null != paymentTDSTaxList && paymentTDSTaxList.size() > 0) {

			for (MLCOPaymentWithholding paymentTDSTax : paymentTDSTaxList) {

				taxAmt = taxAmt.add(paymentTDSTax.getTaxAmt());
				baseAmt = baseAmt.add(paymentTDSTax.getTaxBaseAmt());

			}

			MWTCTaxReturnFormLine taxReturnLine = new MWTCTaxReturnFormLine( this.ctx, 
																			 0, 
																			 this.trxName);
			taxReturnLine.setC_Tax_ID(taxId);
			taxReturnLine.setAssessableValue(baseAmt);
			taxReturnLine.setTaxAmount(taxAmt);
			taxReturnLine.setInvoiceCount(paymentTDSTaxList.size());
			taxReturnLine.setWTC_TaxReturnForm_ID(recordId);
			
			if( taxReturnLine.getWTC_TaxReturnForm().getWTC_TaxIndicator().getCollectableOn().equalsIgnoreCase( X_WTC_TaxIndicator.COLLECTABLEON_Purchase ) ){
				taxReturnLine.setTaxType( ( paymentInput ? X_WTC_TaxReturnFormLine.TAXTYPE_Output : X_WTC_TaxReturnFormLine.TAXTYPE_Input ) );
				
			}else {
				taxReturnLine.setTaxType( ( paymentInput ? X_WTC_TaxReturnFormLine.TAXTYPE_Input : X_WTC_TaxReturnFormLine.TAXTYPE_Output ) ); 
			}
			
			taxReturnLine.saveEx();

			log.fine( " TDS Payment Tax return form line created "
					 + taxReturnLine);
			
			recordCount++;
		}
	
		return taxAmt;

	}

	/**
	 * Get the taxes 
	 * @param taxId
	 * @param isSoTrx
	 * @param periodId
	 * @return
	 */
	public List<MLCOPaymentWithholding> getPaymentProcessedTaxes( int taxId, Boolean isSoTrx,
			int periodId) {

		List<MLCOPaymentWithholding> paymentTaxList = new ArrayList<MLCOPaymentWithholding>();
		
		StringBuffer sql = new StringBuffer ( " SELECT * FROM " ); // 20111207034
							sql.append ( I_LCO_PaymentWithholding.Table_Name + " lpw ");
							sql.append( "JOIN " );
							sql.append( I_C_Invoice.Table_Name + " ci ");
							sql.append( " ON lpw." + I_LCO_PaymentWithholding.COLUMNNAME_C_Invoice_ID );
							sql.append( " = " );
							sql.append( " ci." + I_C_Invoice.COLUMNNAME_C_Invoice_ID );
							sql.append( " WHERE ci." + I_C_Invoice.COLUMNNAME_IsSOTrx );
							sql.append(" = ? ");															// 1
							sql.append(" AND lpw." + I_LCO_PaymentWithholding.COLUMNNAME_C_Tax_ID );
							sql.append(" = ? ");															// 2
							sql.append(" AND lpw." + I_LCO_PaymentWithholding.COLUMNNAME_IsCalcOnPayment );
							sql.append(" = 'Y' ");
							sql.append(" AND lpw." + I_LCO_PaymentWithholding.COLUMNNAME_Processed );
							sql.append(" = 'Y' ");
							sql.append( " AND lpw." + I_LCO_PaymentWithholding.COLUMNNAME_WTC_TaxReturnForm_ID + " IS NULL" );
							
							
							if( periodId >= Env.ZERO.intValue()) {
								
								sql.append(" AND lpw. " + I_LCO_PaymentWithholding.COLUMNNAME_DateTrx );
								sql.append(" BETWEEN ? AND ? ");											// 3 to 4
							}
		
		PreparedStatement pstmt = DB.prepareStatement( sql.toString() , null );
		ResultSet rs = null;
		MPeriod period = null;
		
		try {
			
			if( periodId >=0 ){
				
				period = new MPeriod( this.ctx, periodId, null );
				
//				if( null == period ) {
//					
//					log.log(Level.SEVERE, " Period does not exist with period id = " + periodId );
//					return null;
//				}
			}
			
			pstmt.setString( 1 , ( isSoTrx  ? "Y" : "N") );
			pstmt.setInt( 2 , taxId );
			pstmt.setTimestamp(3, period.getStartDate());
			pstmt.setTimestamp(4, period.getEndDate());
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ){
				
				MLCOPaymentWithholding paymentTDSTax = new MLCOPaymentWithholding( this.ctx, rs, this.trxName );
				paymentTaxList.add( paymentTDSTax );
			}
			
			
		}catch( Exception e ){
			
			log.log( Level.SEVERE , e.getMessage() );
			
		} finally{
			
			DB.close(rs, pstmt);
		}
		
		return paymentTaxList;
	}

	/**
	 * create tax returnline for the i.e input tax return lines and output tax return lines
	 * @param taxId  -- taxid for which return line should be created
	 * @param isSoTrx  --  
	 * @param input  -- true input/Output
	 * @return taxAmount
	 */
	private BigDecimal createTaxReturnLine( int taxId, boolean isSoTrx , boolean input , int periodId , int recordId ){
		
		List<MInvoiceTax> invoiceTaxes = this.getInvoiceTaxesOfTax( taxId, isSoTrx , periodId );
		BigDecimal taxAmt = Env.ZERO;
		BigDecimal baseAmt = Env.ZERO;
		
		if( invoiceTaxes.size() > 0 ) {
			
			for( MInvoiceTax iTax : invoiceTaxes ){

				taxAmt = taxAmt.add( iTax.getTaxAmt().abs() );
				baseAmt = baseAmt.add( iTax.getTaxBaseAmt() );

			}

			MWTCTaxReturnFormLine taxReturnLine = new MWTCTaxReturnFormLine( this.ctx, 0, this.trxName );
			taxReturnLine.setC_Tax_ID( taxId );
			taxReturnLine.setAssessableValue( baseAmt );
			taxReturnLine.setTaxAmount( taxAmt );
			taxReturnLine.setInvoiceCount( invoiceTaxes.size() );
			taxReturnLine.setWTC_TaxReturnForm_ID( recordId );
			MWTCTaxReturnForm returForm = new MWTCTaxReturnForm(ctx, recordId, trxName);
//			if( returForm.getWTC_TaxIndicator().getCollectableOn().equalsIgnoreCase( X_WTC_TaxIndicator.COLLECTABLEON_Purchase ) ){//20111212823
//				taxReturnLine.setTaxType( ( input ? X_WTC_TaxReturnFormLine.TAXTYPE_Output : X_WTC_TaxReturnFormLine.TAXTYPE_Input ) ); 
//				
//			}else {
				taxReturnLine.setTaxType( ( input ? X_WTC_TaxReturnFormLine.TAXTYPE_Input : X_WTC_TaxReturnFormLine.TAXTYPE_Output ) );  
//			}
			  
			taxReturnLine.saveEx();

			log.fine( " Tax return form line created " + taxReturnLine );
			recordCount++;
			
		}
		return taxAmt;
	}
	
	
}
