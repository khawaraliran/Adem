/**
 * 
 */
package org.compiere.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBankAccount;
import org.compiere.model.MDocType;
import org.compiere.model.MOrg;
import org.compiere.model.MPayment;
import org.compiere.model.MSalAdvEMI;
import org.compiere.model.Query;
import org.compiere.model.X_HR_Sal_Adv_EMI;
import org.compiere.model.X_HR_Sal_Adv_Req;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.eevolution.model.MHRPeriod;
import org.wtc.util.EagleMessageConstants;
import org.wtc.util.WTCEmployeeUtil;
import org.wtc.util.WTCTimeUtil;
import org.wtc.util.WTCUtil;


/**
 *  @Bug     @author       @ChangeID            @Description    
 *  ------   -------       ---------            ------------
 *  1638     Arunkumar                          Initial Check in
 *  1638     Arunkumar     [20111223:5:00]      Setting the Document Status
 *  1638     Arunkumar     [20120104:5:00]      Removed Column HR_sal_adv_req From C_Payment According to That Made Code Change.
 */
public class SalaryAdvanceProcess extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: SalaryAdvanceProcess.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	public SalaryAdvanceProcess() {
		
	}
	
	
	ProcessInfo pi						= null;
	int tableID                         = 0;

	private String msg = " ";
	private String collectedBy = EagleConstants.SELF;
	private BigDecimal collectedByDependentID = null;
	private Timestamp paymentDate = null;
	private Timestamp nextEmi = null;

	private boolean success = true;

	private X_HR_Sal_Adv_Req salAdvReq = null;

	/*
	 * (non-Javadoc)
	 * [20120104:5:00] 
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		msg = Msg.getMsg(Env.getCtx(), EagleConstants.PAYMENT_ISSUE_CONFORMATION);
		Timestamp currentTimeStamp = WTCTimeUtil.getSystemCurrentTimestamp();
		

		if (salAdvReq == null) 
		{
			this.msg = Msg.getMsg(Env.getCtx(),EagleConstants.ADVANCE_SALARY_REQUEST_COULD_NOT_RETREIVE);
					
		} else 
		  {
			Timestamp advRequestDate = salAdvReq.getrequestdate();
			int periodId = WTCEmployeeUtil.getPeriodId(salAdvReq.getC_BPartner_ID(),paymentDate);
					
			if (periodId > 0) 
			{
				if (advRequestDate != null && paymentDate != null) 
				{

					if (advRequestDate.compareTo(paymentDate) <= 0) 
					{
						BigDecimal approvedAmt = salAdvReq.getapprovedamt();
						int numberOfInstallments = salAdvReq.getnoofinstalments();

						salAdvReq.setadvancegivenamt(approvedAmt);
						salAdvReq.setadvancegivendate(paymentDate);
						salAdvReq.setisdisbursed(true);

						if (collectedBy.equalsIgnoreCase(EagleConstants.OTHERS)) 
						{
							salAdvReq.setadvance_collected_by_id(collectedByDependentID.intValue());
						}

						this.success = salAdvReq.save();

						MPayment mPayment = null;
						if (this.success) 
						{
							//
							// Create payment record in the system
							//
							mPayment = new MPayment(this.getCtx(), 0,this.get_TrxName());
							int docTypeId = WTCUtil.getDocTypeID(MDocType.DOCBASETYPE_APPayment);
							mPayment.setAD_Org_ID(Env.getAD_Org_ID(this.getCtx()));
							int currencyID = Env.getContextAsInt(getCtx(),"$C_Currency_ID");
							if(currencyID < 0) {
								currencyID = EagleConstants.DEFAULT_CURRENCY_ID;
								
								log.log(Level.SEVERE, "Currency "+currencyID+"Not Found");
							}else {
								log.log(Level.SEVERE, "Currency "+currencyID+"Found");
							}
							mPayment.setAmount(currencyID,approvedAmt);
							mPayment.setIsReceipt(false);
							mPayment.setC_BPartner_ID(salAdvReq.getC_BPartner_ID());
							mPayment.setC_DocType_ID(docTypeId);
							mPayment.setAD_Table_ID(tableID);
							int reqID = salAdvReq.getHR_Sal_Adv_Req_ID();
							mPayment.setpayment_ref_id(reqID);
							mPayment.setDateTrx(currentTimeStamp);
							mPayment.setTrxType(MPayment.TRXTYPE_CreditPayment);

							int orgId = Env.getAD_Org_ID(Env.getCtx());
							int  bankAcctNumber = WTCUtil.organizationalBankAccount(orgId);
							if (bankAcctNumber <=  0 ) {
								log.log(Level.SEVERE, "Can't find Bank Account");
								String msg = Msg.getMsg(getCtx(), EagleMessageConstants.ORGANIZATIONA_BANK_ACCOUNT_MANDATORY);
								throw new AdempiereException(msg);
							}else {
								mPayment.setC_BankAccount_ID(bankAcctNumber);
							}
														
							mPayment.setTenderType(MPayment.TENDERTYPE_Account);
							mPayment.setPayAmt(approvedAmt);
							mPayment.setDateAcct(paymentDate);
							mPayment.setC_Charge_ID(EagleConstants.ADVANCE_PAYMENT_CHARGE_ID);

							this.success = mPayment.save();
							if (!this.success) {
								this.msg = Msg.getMsg(Env.getCtx(),EagleConstants.FAILED_TO_POST_PAYMENT);
							}else {
						          	//Bug-1638
								//[20111223:5:00]
								String docStatus = mPayment.completeIt();
								
								if(docStatus != null ) {
									if (! docStatus.equalsIgnoreCase(DocAction.STATUS_Completed)) {
										log.log(Level.SEVERE, "Payment Not Posted, Doc Status :"+docStatus);
									}else {
										mPayment.setDocStatus(MPayment.DOCSTATUS_Completed);
										mPayment.setDocAction(MPayment.DOCACTION_Close);
										mPayment.setProcessed(true);
										this.success =  mPayment.save();
										if (!this.success) {
											log.log(Level.SEVERE, "Failed To Update the Payment Objects");
										}
									}
								}
							}

						} else {
							this.msg = Msg.getMsg(Env.getCtx(),EagleConstants.FAILED_TO_UPDATE_ADVANCE_REQUEST);
						}

						if (this.success) 
						{
							//
							// Create EMI entries
							//
							BigDecimal emiAmount = approvedAmt.divide(new BigDecimal(numberOfInstallments), 2,RoundingMode.HALF_UP);

							for (int i = 0; i < numberOfInstallments; i++) 
							{
								X_HR_Sal_Adv_EMI salAdvEmi = new X_HR_Sal_Adv_EMI(this.getCtx(), 0, this.get_TrxName());
								salAdvEmi.setLineNo(i + 1);
								salAdvEmi.setemiamount(emiAmount);
								salAdvEmi.setHR_Sal_Adv_Req_ID(this.getRecord_ID());
								salAdvEmi.setispaidoff(false);
								salAdvEmi.setispartial(false);
								salAdvEmi.setskipinpayslip(false);
								salAdvEmi.setactualemiamount(emiAmount);
								salAdvEmi.setHR_Period_ID(periodId);
								
								if (mPayment != null ) {
									int C_Payment_ID  =   mPayment.getC_Payment_ID();
									salAdvEmi.setC_Payment_ID(C_Payment_ID);
								}
								salAdvEmi.setpaiddate(getNextEmiDate());
								this.success = salAdvEmi.save(this.get_TrxName());
								periodId = MHRPeriod.getNextPeriodId(this.getCtx(), periodId,this.get_TrxName());

								// IF the Period ID Not There Then It Will Roll
								// Back This Transaction.
								if (periodId <= 0) 
								{
									Trx localTrx = null;
									localTrx = Trx.get(this.get_TrxName(), true);
									if (localTrx != null) 
									{
										localTrx.rollback(true);
									}

									this.msg = Msg.getMsg(Env.getCtx(),EagleConstants.PeriodNextNotFound);
									break;
								}

								if (!this.success) 
								{
									this.msg = Msg.getMsg(Env.getCtx(), EagleConstants.FAILED_TO_CREATE_EMI_FOR_ADVANCE_PAYMENT);
									break;
								}
							}
						}
					} 
					else {
						this.msg = Msg.getMsg(Env.getCtx(),EagleConstants.PAYMENT_DATE_NOT_LESS_THAN_REQUEST_DATE);
					}
				}
			} 
			else {
				this.msg = Msg.getMsg(Env.getCtx(),EagleConstants.PERIOD_NOT_AVAILABLE_FOR_THE_CURRENT_MONTH);
						
			}
		}

		return this.msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() 
	{
		pi = getProcessInfo(); 
		
		
		if( pi != null )	{

			tableID = pi.getTable_ID();
		}	
			// From table ID, get the table name
		
		ProcessInfoParameter[] para = getParameter();
		String parameterName = null;

		for (int i = 0; i < para.length; i++) 
		{
			parameterName = para[i].getParameterName();

			if (parameterName.equalsIgnoreCase("HR_Dependents_ID"))
			{

				collectedByDependentID = ((BigDecimal) para[i].getParameter());

				if (collectedByDependentID != null) 
				{
					this.collectedBy = EagleConstants.OTHERS;

				} else {
					this.collectedBy = EagleConstants.SELF;
				}
			} else if (parameterName.equalsIgnoreCase("PaymentDate")) 
			{
				if ((para[i].getParameter()) != null) 
				{
					paymentDate = (Timestamp) (para[i].getParameter());
				} else 
				{
					paymentDate = TimeUtil.getDay(0);
				}
			}
		}

		salAdvReq = new X_HR_Sal_Adv_Req(this.getCtx(), this.getRecord_ID(),this.get_TrxName());
    }

	/**
	 *  It Will Get The Next EMI Date 
	 */
	private Timestamp getNextEmiDate() 
	{
		  if(null == nextEmi)
		  {
		    nextEmi = paymentDate;
		  }
		  GregorianCalendar gre=(GregorianCalendar) Calendar.getInstance();
		  gre.setTimeInMillis(nextEmi.getTime());
		  gre.add(Calendar.MONTH, 1);
		  nextEmi = new Timestamp(gre.getTimeInMillis());
		  return nextEmi;
	}
}
