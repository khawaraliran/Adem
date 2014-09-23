package org.adempiere.webui.apps.form;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Combobox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.panel.ADButtonDailog;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.GridTab;
import org.compiere.model.I_WTC_ClientStatutory;
import org.compiere.model.I_WTC_TaxIndicator;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.MTable;
import org.compiere.model.MWTCTaxReturnForm;
import org.compiere.model.Query;
import org.compiere.model.X_WTC_ClientStatutory;
import org.compiere.model.X_WTC_TaxIndicator;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

/**
 *  This Form is used to make the payment from any tab which has charge associated with
 * it.
 * 
 * @author Arunkumar
 * 
 * 
 * 
 * <pre>
 * 
 * @author PhaniKiran.Gutha
 * Date   [06/12/2011]
 * <li><b>Modifications done</b></li>
 * 		<li> When the payment changed by the user the appropriate fileds should get cleared</li>
 * 		<li> Before going to prepare the payment , check for the required data filled by the user </li>
 * 		<li> Set payamount to the payment along with the payment allocate </li>
 * 		<li> set paymentfromCreatePayment in to the mpayment to avoid the tax return allocation at payment allocation</li>
 * 		<li> prepare and complete the payment. if the payment completeIt doesnot return the status complete then tax compuation allocation should not happen</li>
 * 		<li> after creation and completion of payment sucess fully create an entry into the taxcompuation payment allocation table with the appropriate details</li>
 *</pre>
 *
 *IssueNo		Author			changeId			Description
 *-----------------------------------------------------------------
 *1762		  PhaniKiran.Gutha	2011120408			Tax Compuation Payment Allocation should be created first and saved then only tax computation form should be updated with open amount
 *1790        Arunkumar.muddada [20111223:5:00]     WE Have Decided That We have To Use Table ID , We Can Make Payment To Any Charge Or Invoice .
 *													For Tracking Which Payment is Given on What Cause We Have TO Maintain Table ID 
 *1762		  PhaniKiran.Gutha	20112770324			bank account and cash book should retrive all the existing for the organization.
 *
 */
public class CreatePaymentReceipt extends ADButtonDailog  {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: CreatePaymentReceipt.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 
	 */
	private static final long serialVersionUID = 16544L;
	private static final CLogger log = CLogger.getCLogger(CreatePaymentReceipt.class);

    private Integer businessPartnerId	= null;
    private int chargeId				= 0;
    private int recordId				= 0; 
    private Boolean isPayment			= true;
    private BigDecimal openAmount		= Env.ZERO;
    private BigDecimal grandTotal 		= Env.ZERO;
    
    private Grid grid 				 	= new Grid();	
	private Decimalbox paymentAmount 	= new Decimalbox();
	private Textbox discountAmount 	 	= new Textbox();
	private Textbox checkNumber 	 	= new Textbox();
	private Textbox routingNumber 	 	= new Textbox();
	private Textbox micrNumber 		 	= new Textbox();
	private Combobox bankAccount 	 	= new Combobox();
	private Combobox paymentMode 	 	= new Combobox();
	private Combobox cashBook 		 	= new Combobox();

	private Button ok 				 	= new Button();
	private Button cancel 			 	= new Button();
	private Rows rows 				 	= new Rows();
	private Row  row1 				 	= new Row();
	private Row  row2 				 	= new Row();
	private Row  row3 				 	= new Row();
	private Row  row4 				 	= new Row();
	private Row  row5 				 	= new Row();
	private Row  row6 				 	= new Row();

	Label advance 					 	= null;
	Label advanceAmount 				= null;
	GridTab curTab=null;
	private Integer bankAccoountID 		= null;
	private String mode 				= null;
	
	ProcessInfo pi						= null;
	String tableName					= null;
	String keyColumnName				= null;
	String whereClause					= null;
	
	public CreatePaymentReceipt()
	{
		super();
		
		this.setTitle("Payment ");
		this.setBorder("normal");
		this.setWidth("600px");
		this.setHeight("300px");
		this.setSizable(true);
		this.setClosable(true);
		
		this.setMaximizable(true);
		this.setSizable(true);

		this.setFocus(true);
	
		SessionManager.getAppDesktop().registerWindow(this);
		
	}
	
	public void init()
	{
		prepare();  //This Method Will Get All the Parameters 
		
		if( openAmount.compareTo( Env.ZERO ) != 1 ){
			
			Label l =  new Label( " Open Amount is less than or equal to zero. cannot make payment " );
			l.setWidth("100%");
			l.setStyle( "font-weight:bold; color:red" );
			this.appendChild(l);
			this.setTitle(" Cannot make payment ");
			this.setBorder("normal");
			this.setWidth("100x");
			this.setHeight("50px");
			this.setSizable(false);
			this.setClosable(true);
			
			this.setMaximizable(false);
			this.setSizable(false);

			this.setFocus(true);
			return; 
		}
		Panel popup = new Panel();		
		popup.setWidth("500px");
		popup.setHeight("500px");
		popup.setParent(this);  
		grid.setParent(popup);	
		grid.setStyle("border-style:none none none none; background-color:white; cellspacing:9px");
		
		rows.setParent(grid);	
		
		row1.setParent(rows);	
		row1.setWidth("100%");
		row1.setHeight("50px");
		row1.setStyle("border-style:none none none ;");		
		Hbox hbox = new Hbox();		
		hbox.setParent(row1);		
		hbox.setStyle("cellspacing:6px");		
		hbox.setStyle("border-style:none none none ;");
		Div div14 = new Div();	
		div14.setWidth("10px");
		hbox.appendChild(div14);
		Label first = new Label("Payment Amount :");		
		hbox.appendChild(first.rightAlign());		
		paymentAmount.setWidth("145px");
		hbox.appendChild(paymentAmount);
		paymentAmount.addEventListener(Events.ON_BLUR, this);
			
	
		row2.setParent(rows);	
		row2.setWidth("100%");
		row2.setStyle("border-style:none none none ; background-color:white; float:left;");
		row2.setHeight("35px");
		Hbox hbox2 = new Hbox();
		hbox2.setParent(row2);
		hbox2.setWidth("303px");
		hbox2.setStyle("cellspacing:3px");
		hbox2.setStyle("border-style:none none none ;");
		hbox2.appendChild(new Label("Payment Mode  :").rightAlign());
		hbox2.appendChild(paymentMode);		
		
		
		row3.setStyle("border-style:none none none ; background-color:white;");
		row3.setWidth("100%");
		
		row4.setStyle("border-style:none none none ; background-color:white;");
		row5.setStyle("border-style:none none none ; background-color:white;");
		row6.setParent(rows);
		row6.setStyle("border-style:none none none ;");	
		
		
		if(businessPartnerId != null || businessPartnerId  !=0) {
			
			Env.setContext(Env.getCtx(), "bpartnerNo", businessPartnerId);
		}
			
		String[] paymentModeArray = {"Cheque","Cash"};		
		for (int i = 0; i < paymentModeArray.length; i++)
		{
			paymentMode.appendItem(paymentModeArray[i],paymentModeArray[i]);
		}
		paymentMode.addEventListener(Events.ON_SELECT, this);
		
		
		
		
		ok.setLabel("Ok");
		ok.addEventListener(Events.ON_CLICK, this);
		ok.setWidth("70px");
		ok.setHeight("20px");
		cancel.setLabel("Cancel");
		cancel.setWidth("70px");
		cancel.setHeight("20px");
		cancel.addEventListener(Events.ON_CLICK, this);
		
		Hbox hbox6 = new Hbox();
		hbox6.setParent(row5);
		hbox6.setWidth("370px");
		hbox6.setStyle("cellspacing:3px");
		hbox6.setStyle("border-style:none none none ;");
		hbox6.appendChild(new Label("Cash Book :").rightAlign());
		hbox6.appendChild(cashBook);
		//20112770324
		
		String sqlCashBook = "SELECT name,C_Cashbook_ID,isDefault FROM C_Cashbook ";
		if( Env.getContextAsInt(Env.getCtx(), "AD_Org_ID") > 0)
        {
			sqlCashBook=sqlCashBook+" WHERE AD_Org_ID = "+Env.getAD_Org_ID(getCtx()); 
        }
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlCashBook,null);
			rs = pstmt.executeQuery();
			Comboitem ciDefault = null;
			while(rs.next()) {
				Comboitem comboitem = new Comboitem(rs.getString(1),rs.getString(2));
				cashBook.appendChild(comboitem);
				if( rs.getString(3).equalsIgnoreCase("Y")) {
					ciDefault = comboitem;
				}
			}
			cashBook.setSelectedItem(ciDefault);

		} 
		catch (SQLException e) 
		{
			log.log(Level.SEVERE, "Can't Fill The Cash Book:");
		}	

		//20112770324
		String sqlBankAcct = "SELECT accountno,c_bankaccount_id,isDefault FROM c_bankaccount ";
        if( Env.getContextAsInt(Env.getCtx(), "AD_Org_ID") > 0)
        {
        	sqlBankAcct=sqlBankAcct+" WHERE AD_Org_ID = "+Env.getAD_Org_ID(getCtx()); 
        }
		try {
			pstmt = DB.prepareStatement(sqlBankAcct,null);
			rs = pstmt.executeQuery();
			Comboitem ciDefault = null;
			while(rs.next()) {
				Comboitem comboitem = new Comboitem(rs.getString(1),rs.getString(2));

				bankAccount.appendItem(rs.getString(1),rs.getString(2));
				if( rs.getString(3).equalsIgnoreCase("Y")) {
					ciDefault = comboitem;
				}
			}
			bankAccount.setSelectedItem(ciDefault);
			if(bankAccount.getValue() != null && bankAccount.getValue() !="")
			{
				bankAccoountID = new Integer((String)bankAccount.getValue());
			}
		} 
		catch (SQLException e) 
		{
			log.log(Level.SEVERE, "Can't Fill The Cash Book:");
		}


		Hbox hbox3 = new Hbox();
		hbox3.setParent(row3); 

		hbox3.setStyle("border-style:none none none ; background-color:white;");
		Div div5 = new Div();
		div5.setWidth("25px");
		hbox3.appendChild(div5);
		Label fourth = new Label("Bank Account :");
		hbox3.appendChild(fourth.rightAlign());
		hbox3.appendChild(bankAccount);
		bankAccount.setWidth("148px");
		Div div9 = new Div();
		div9.setWidth("10px");
		hbox3.appendChild(div9);


		Hbox hbox8 = new Hbox();
		hbox8.setParent(row3); 

		hbox8.setStyle("border-style:none none none ; background-color:white;");

		Label third = new Label("Cheque Number :");
		hbox3.appendChild(third.rightAlign());		
		hbox3.appendChild(checkNumber);
		checkNumber.setWidth("110px");
		
		Hbox hbox4 = new Hbox();
		Div div7 = new Div();
		div7.setWidth("2px");
		hbox4.appendChild(div7);
		hbox4.setParent(row4);
		row4.setWidth("100%");
		row4.setHeight("50px");
		hbox4.setWidth("500px");
		hbox4.setStyle("cellspacing:8px");
		hbox4.setStyle("border-style:none none none ; background-color:white;");
	
		hbox4.appendChild(new Label("Routing Number :").rightAlign());
		hbox4.appendChild(routingNumber);
		routingNumber.setWidth("146px");
		
	
		hbox4.appendChild(new Label("MICR Number :").rightAlign());
		hbox4.appendChild(micrNumber);
		micrNumber.setWidth("110px");

		Hbox hbox7 = new Hbox();
		hbox7.setParent(row6);
		row6.setStyle("border-style:none none none ; background-color:white;");
		hbox7.setWidth("150px");
		hbox7.setStyle("cellspacing:3px");
		hbox7.setStyle("border-style:none none none ; background-color:white;");
		Div div3 = new Div();	
		hbox7.appendChild(div3);
		div3.setWidth("105px");
		hbox7.appendChild(ok);	
		Div div4 = new Div();	
		hbox7.appendChild(div4);
		div4.setWidth("20px");
		hbox7.appendChild(cancel);
	}

	public void onEvent(Event event) throws Exception
	{
		if(event.getTarget()== paymentMode)
		{
			row3.setParent(null);
			row4.setParent(null);
			row5.setParent(null);
			row6.setParent(null);

			if(event.getName().equalsIgnoreCase("ONSELECT")) {
				Comboitem comboItem = paymentMode.getSelectedItem();
				mode = (String)comboItem.getValue();
				if(mode.equalsIgnoreCase("Cash")) {
					micrNumber.setText( "" );
					routingNumber.setText(""); 
					checkNumber.setText("" );
					row5.setParent(rows);						
					row6.setParent(rows);
				}else if(mode.equalsIgnoreCase("Cheque")) {
					cashBook.setSelectedItem( null );
					row3.setParent(rows);					
					row4.setParent(rows);					
					row6.setParent(rows);
				}
			}
		}
		
		if(event.getTarget() == paymentAmount)
		{
			BigDecimal paymentVal=Env.ZERO;
			if(paymentAmount.getValue() != null) 
			{
				paymentVal=paymentAmount.getValue();
			}
			else
			{
				paymentAmount.setValue(Env.ZERO);
			}
			
		}
		
		
		if(event.getTarget() == ok) 
		{
			if(event.getName().equalsIgnoreCase("ONCLICK"))	{
				
				Comboitem comboItem = paymentMode.getSelectedItem();
				if( comboItem != null ){
					mode = (String)comboItem.getValue();
					
					
					
					
					if(mode.equalsIgnoreCase("Cash")) {
						
						Comboitem cashbook = cashBook.getSelectedItem();
						if( cashbook == null ){
							FDialog.error( getWindowNo() , " Please select the cash book " );
							return;
						} 
						if( bankAccount.getValue() == null ){
							FDialog.error(getWindowNo(), "No Default Bank Account Exists for Current Organization"); 
							return;
						}
					}else if(mode.equalsIgnoreCase("Cheque")) {
						
						Comboitem account = bankAccount.getSelectedItem();
						 
						if( account == null ){
							FDialog.error( getWindowNo() , "Please select the bank account " ); 
							return;
						}
					}
				} else {
					FDialog.error( getWindowNo() , " Payment Mode Not Selected." );
					return;
				}
				
				if( makePayment() ) {
					
					log.log( Level.FINE, 
							 " Payment maid successfully for the tax return form " 
							 + recordId 
							 + " with  amount = " 
							 + paymentAmount.getValue());
				}
				
			}

			this.setVisible(false);

		}
		if(event.getTarget() == cancel) {
			if(event.getName().equalsIgnoreCase("ONCLICK")) {
				paymentAmount.setValue(null);
				discountAmount.setValue(null);
				checkNumber.setValue(null);
				routingNumber.setValue(null);
				micrNumber.setValue(null);
				bankAccount.setValue(null);
				paymentMode.setValue(null);
				cashBook.setValue(null);
				this.setVisible(false);
			}
		}
	}

	/**
	 *  Used to make the payment
	 */
	private boolean makePayment() {
		
		Trx trx = Trx.get("ok", true);
		
		boolean paymentSuccessful = Boolean.TRUE;
		MPaymentAllocate paymentAllocate = null;
		try {
			MPayment payment = new MPayment(Env.getCtx(),0,trx.getTrxName());
			
			if (null != businessPartnerId) {
				
				payment.setC_BPartner_ID(businessPartnerId);
				payment.setC_DocType_ID( EagleConstants.AP_PAYMENT_ID );
				payment.setpayment_ref_id( recordId );
				
				//Bug-1790
				//[20111223:5:00]
				//WE Have Decided That We have To Use Table ID ,
				// We Can Make Payment To Any Charge Or Invoice .
				// For Tracking Which Payment is Given on What Cause We Have TO Maitain Table ID 
				int tableID = pi.getTable_ID();
				payment.setAD_Table_ID(tableID);
				
				if( null != mode )	{

					if( mode.equalsIgnoreCase("Cash") )	{
						
						payment.setTenderType(MPayment.TENDERTYPE_Cash);
						payment.setC_Currency_ID(Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID"));

						if(bankAccoountID != null && bankAccoountID != 0) {
							payment.setC_BankAccount_ID(bankAccoountID);
						}
						
						if( cashBook.getValue() != null){
							
							Integer cashBookID=new Integer(cashBook.getSelectedItem().getImage());
							payment.setC_CashBook_ID(cashBookID);
						}
						
						
					}else if (mode.equalsIgnoreCase("Cheque"))	{
						
						payment.setTenderType(MPayment.TENDERTYPE_Check);
						Comboitem baccount = bankAccount.getSelectedItem();
						
						if(bankAccount.getValue() != null && bankAccount.getValue() !="")	{
							
							Integer accountID = new Integer((String)baccount.getValue());
							payment.setC_BankAccount_ID(accountID);
						} 
						
						if(checkNumber.getValue() != null)
							payment.setCheckNo(checkNumber.getValue());
						
						if(micrNumber.getValue() != null)
							payment.setMicr(micrNumber.getValue());
						payment.setC_Currency_ID(Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID"));
						
						
						if(routingNumber.getValue() != null) {
							 
							payment.setRoutingNo(routingNumber.getValue());
						}
					}

				}
			
				payment.setPayAmt( paymentAmount.getValue()  ); 
				if(!payment.save( trx.getTrxName() )) {
				
					paymentSuccessful = Boolean.FALSE;
					log.log( Level.SEVERE, 
							" Faild to save the payment for the tax return calculation form ");
				}else {
				
					payment.setFormCreatePayment( Boolean.TRUE );
					 paymentAllocate = new MPaymentAllocate( Env.getCtx(), 0, trx.getTrxName() );
					paymentAllocate.setC_Payment_ID(payment.getC_Payment_ID());
					paymentAllocate.setC_Charge_ID( chargeId );
					paymentAllocate.setAmount( null != paymentAmount.getValue() ? paymentAmount.getValue() : Env.ZERO );
					
					if(!paymentAllocate.save()) {
					
						paymentSuccessful = Boolean.FALSE;
						
						log.log(Level.SEVERE, " Failed to create an payment allocation for taz return form - " + recordId );  
					}else {
						
						// 
						// Complete the payment document
						//
						
						String docStatus = payment.prepareIt();
						docStatus = payment.completeIt(); 
						
						payment.setDocStatus(docStatus);
						payment.save();
						
						if( !docStatus.equalsIgnoreCase( DocAction.STATUS_Completed ) ){ 
							paymentSuccessful = Boolean.FALSE;
						}
						
					}
				}	
			}
				
		} catch (Exception e) {
			
			paymentSuccessful = Boolean.FALSE;
			log.log( Level.SEVERE, 
					 "Failed to make the payment for the tax return form - " 
					 + recordId 
					 + e.getMessage());
			
		} finally {
		
			if( paymentSuccessful ) {
				
				trx.commit();
				
				//
				// Update the balance amount
				//
				
				
				
				 MWTCTaxReturnForm taxReturnForm = new MWTCTaxReturnForm(Env.getCtx(), recordId,  trx.getTrxName() );
				 boolean createdAllocation =  MPayment.createMWTCTaxretrunPaymentAlllocation( paymentAmount.getValue() , 
																							 paymentAllocate.get_ID(), 
																							 taxReturnForm.getWTC_TaxIndicator_ID(), 
																							 taxReturnForm.get_ID(),
																							 Env.getCtx(),
																							 trx.getTrxName()  );
				 
				 BigDecimal openAmount = taxReturnForm.getOpenAmt().subtract(paymentAmount.getValue());
				 taxReturnForm.setOpenAmt( openAmount);
				 
				 if( ! createdAllocation || !taxReturnForm.save() ) {
					 
					 log.log( Level.SEVERE, 
							 " Failed to update the open amount for the tax return calculation - " 
							 + recordId );
					 if( !createdAllocation ){
						 log.log( Level.SEVERE, 
								 " Failed to create tax compuation allocation - " 
								 + recordId );
					}
					 
				 }else {
					 
					 log.log( Level.FINE, 
							 " Open amount for the tax return calculation - " 
							 + recordId + " updated succesffully " );
				 }
				 
				 if( null != trx && !trx.close()) {
				
					 trx.close();
					 trx = null;
				 }
			}
		}
		
		return paymentSuccessful;
	}

	@Override
	protected void initButtonDailog() 
	{
		pi = getProcessInfo(); 
		
		init();
	}
 
	
	protected void prepare() {
		
		if( pi != null )	{

			recordId = pi.getRecord_ID();
						
			int tableID = pi.getTable_ID();
			
			// From table ID, get the table name
			
			
			tableName = MTable.getTableName(Env.getCtx(), tableID);

			keyColumnName = tableName + "_ID";
		  whereClause = keyColumnName + " = " + recordId;

		  PreparedStatement ps=null;
		  ResultSet rs=null;
		  int taxIndicatorId = 0;

		  String commSql=" SELECT WTC_TaxIndicator_ID,grandtotal,openamt FROM " + tableName+" WHERE "+ whereClause;

		   ps= DB.prepareStatement(commSql, pi.getTransactionName());
			
		   try  {
			   
				 rs=ps.executeQuery();
				 
				 if(rs.next())	 {
				 
					 taxIndicatorId = rs.getInt(1);
					 openAmount = rs.getBigDecimal(3);
					 grandTotal = rs.getBigDecimal(2);
				 }
			 }
			 catch (Exception e) {
				
				 log.log(Level.SEVERE, " Failed to get the taxindicator id for the return form");
			}finally {
				
				DB.close( rs, ps );
				ps= null;
				rs = null;
				
			}
			
			//
			// Get the charge id of the tax indicator
			//
			
			String whereClause = I_WTC_TaxIndicator.COLUMNNAME_WTC_TaxIndicator_ID + " = " + taxIndicatorId;
			
			X_WTC_TaxIndicator taxIndicator = new Query( Env.getCtx() , I_WTC_TaxIndicator.Table_Name, whereClause, null)
											  .setOnlyActiveRecords(true)
											  .first();
			
			chargeId = taxIndicator.getC_Charge_ID();
			
			int clientSatutoryInfoId = taxIndicator.getWTC_ClientStatutory_ID();
				
			//
			// Get the Business Partner id for the associate client satutory information of the tax indicator
			//
			
			String bpWhereClause = I_WTC_ClientStatutory.COLUMNNAME_WTC_ClientStatutory_ID  + " = " + clientSatutoryInfoId ;
				
			X_WTC_ClientStatutory  clientClientStatutory = new Query( Env.getCtx() , I_WTC_ClientStatutory.Table_Name, bpWhereClause, null)
														   .setOnlyActiveRecords(true)
														   .first();
			
				businessPartnerId = clientClientStatutory.getC_BPartner_ID();
			 
		}
	}
}
