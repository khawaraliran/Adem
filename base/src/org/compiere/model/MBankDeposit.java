/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2014 Michael McKay All Rights Reserved.                      *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.compiere.acct.Doc;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
 
/**
*	Bank Statement Model
*
*  @author Michael McKay
*  
*/
public class MBankDeposit extends X_C_BankDeposit implements DocAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4006787077927772553L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankDeposit_ID id
	 *	@param trxName transaction
	 */	
	public MBankDeposit (Properties ctx, int C_BankDeposit_ID, String trxName)
	{
		super (ctx, C_BankDeposit_ID, trxName);
		if (C_BankDeposit_ID == 0)
		{ 
		//	setC_BankAccount_ID (0);	//	parent
			setDepositDate (new Timestamp(System.currentTimeMillis()));	// @Date@
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setTotalDeposit(Env.ZERO);
			setIsApproved (false);	// N
			setIsManual (true);	// Y
			setPosted (false);	// N
			super.setProcessed (false);
		}
	}	//	MBankStatement

	/**
	 * 	Load Constructor
	 * 	@param ctx Current context
	 * 	@param rs result set
	 *	@param trxName transaction
	 */
	public MBankDeposit(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatement

 	/**
 	 * 	Parent Constructor
	 *	@param account Bank Account
 	 * 	@param isManual Manual deposit
 	 **/
	public MBankDeposit (MBankAccount account, boolean isManual)
	{
		this (account.getCtx(), 0, account.get_TrxName());
		setClientOrg(account);
		setC_BankAccount_ID(account.getC_BankAccount_ID());
		setDepositDate(new Timestamp(System.currentTimeMillis()));
		setName(Msg.translate(account.getCtx(), "C_BankDeposit_ID") + getDepositDate().toString());
		setIsManual(isManual);
	}	//	MBankStatement
	
	/**
	 * 	Create a new Bank Statement
	 *	@param account Bank Account
	 */
	public MBankDeposit(MBankAccount account)
	{
		this(account, false);
	}	//	MBankStatement
 
	/**	Lines							*/
	private MBankDepositLine[] 	m_lines = null;
	
 	/**
 	 * 	Get Bank Statement Lines
 	 * 	@param requery requery
 	 *	@return line array
 	 */
 	public MBankDepositLine[] getLines (boolean requery)
 	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		//
		final String whereClause = I_C_BankDepositLine.COLUMNNAME_C_BankDeposit_ID+"=?";
		List<MBankDepositLine> list = new Query(getCtx(),I_C_BankDepositLine.Table_Name,whereClause,get_TrxName())
		.setParameters(getC_BankDeposit_ID())
		.setOrderBy("Line")
		.list();
		MBankDepositLine[] retValue = new MBankDepositLine[list.size()];
		list.toArray(retValue);
		return retValue;
 	}	//	getLines

 	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Set Processed.
	 * 	Propagate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String sql = "UPDATE C_BankDepositLine SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_BankDeposit_ID=" + getC_BankDeposit_ID();
		int noLine = DB.executeUpdate(sql, get_TrxName());
		m_lines = null;
		log.fine("setProcessed - " + processed + " - Lines=" + noLine);
	}	//	setProcessed

	/**
	 * 	Get Bank Account
	 *	@return bank Account
	 */
	public MBankAccount getBankAccount()
	{
		return MBankAccount.get(getCtx(), getC_BankAccount_ID());
	}	//	getBankAccount
	
	/**
	 * 	Get Document No 
	 *	@return name
	 */
	public String getDocumentNo()
	{
		return getName();
	}	//	getDocumentNo
	
	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		return Msg.getMsg(getCtx(), "Bank Deposit") + " " + getBankAccount().getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//  No special actions
		return true;
	}	//	beforeSave
	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Std Period open?
		MPeriod.testPeriodOpen(getCtx(), getDepositDate(), MDocType.DOCBASETYPE_BankStatement, getAD_Org_ID());
		if (update() == 0) // Sets the total deposit amount
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info("completeIt - " + toString());
		
		//	Set Payment reconciled
		MBankDepositLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankDepositLine line = lines[i];
			if (line.getC_Payment_ID() != 0)
			{
				MPayment payment = new MPayment (getCtx(), line.getC_Payment_ID(), get_TrxName());
				payment.setIsReconciled(true);
				payment.save(get_TrxName());
			}
		}
		
		//	Create the summary payment representing the deposit
		MPayment pay = new MPayment (getCtx(), 0, get_TrxName());
		pay.setAD_Org_ID(getAD_Org_ID());
		String documentNo = getName();
		pay.setDocumentNo(documentNo);
		pay.setR_PnRef(documentNo);
		pay.set_ValueNoCheck("TrxType", "X");		//	Transfer
		pay.set_ValueNoCheck("TenderType", "G");	//  Manual deposit
		pay.setC_BankAccount_ID(getC_BankAccount_ID());
		// pay.setC_CashBook_ID(getC_CashBook_ID()); 
		pay.setC_DocType_ID(true);	//	Receipt
		pay.setDateTrx(getDepositDate());
		pay.setDateAcct(getDateAcct());
		pay.setAmount(getC_Currency_ID(), getTotalDeposit());	//	Transfer
		pay.setDescription(getDocumentInfo());
		pay.setDocStatus(MPayment.DOCSTATUS_Closed);
		pay.setDocAction(MPayment.DOCACTION_None);
		pay.setPosted(true);
		pay.setIsAllocated(true);	//	Has No Allocation!
		pay.setProcessed(true);
		pay.setIsReconciled(getTotalDeposit().equals(Env.ZERO));  // Reconcile if zero amount.
		if (!pay.save())
		{
			m_processMsg = CLogger.retrieveErrorString("Could not create Payment");
			return DocAction.STATUS_Invalid;
		}
		
		setC_Payment_ID(pay.getC_Payment_ID());
		if (!save())
		{
			m_processMsg = "Could not update Deposit with transfer payment";
			return DocAction.STATUS_Invalid;
		}

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Void Document.
	 * 	@return false 
	 */
	public boolean voidIt()
	{
		log.info(toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
			;
		//	Std Period open?
		else
		{
			MPeriod.testPeriodOpen(getCtx(), getDepositDate(), MDocType.DOCBASETYPE_BankStatement, getAD_Org_ID());
			MFactAcct.deleteEx(Table_ID, getC_BankDeposit_ID(), get_TrxName());
		}
		
		//	Set lines to 0
		MBankDepositLine[] lines = getLines(true);
		for (int i = 0; i < lines.length; i++)
		{
			MBankDepositLine line = lines[i];
			if (line.getDepositAmt().compareTo(Env.ZERO) != 0)
			{
				String description = Msg.getMsg(getCtx(), "Voided") + " ("
					+ Msg.translate(getCtx(), "DepositAmt") + "=" + line.getDepositAmt();
				description += ")";
				line.addDescription(description);
				//
				line.setDepositAmt(Env.ZERO);
				line.setTrxAmt(Env.ZERO);
				line.setChargeAmt(Env.ZERO);
				//
				if (line.getC_Payment_ID() != 0)
				{
					MPayment payment = new MPayment (getCtx(), line.getC_Payment_ID(), get_TrxName());
					payment.setIsReconciled(false);
					payment.saveEx();
					line.setC_Payment_ID(0);
				}
				line.saveEx();
			}
		}
		
		if (getC_Payment_ID() == 0)
			throw new IllegalStateException("Cannot reverse payment");
			
		MPayment payment = new MPayment(getCtx(), getC_Payment_ID(), get_TrxName());
		payment.reverseCorrectIt();
		payment.saveEx();

		addDescription(Msg.getMsg(getCtx(), "Voided"));
		setTotalDeposit(Env.ZERO);
		
		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;		
		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info("closeIt - " + toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;		

		setDocAction(DOCACTION_None);

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return false 
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		
		return false;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual
	 * 	@return false 
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return false 
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		if (this.isReconciled()) {
			m_processMsg = Msg.translate(getCtx(), "This record has been reconciled in a Bank Statement and can't be reactivated.");
			return false;
		}
		
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), Doc.DOCTYPE_BankDeposit, getAD_Org_ID());
		MFactAcct.deleteEx(MBankDeposit.Table_ID, get_ID(), get_TrxName());
		setPosted(false);
		setProcessed(false);
		setDocAction(DOCACTION_Complete);
		
		//	Set Payment reconciled
		MBankDepositLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankDepositLine line = lines[i];
			if (line.getC_Payment_ID() != 0)
			{
				MPayment payment = new MPayment (getCtx(), line.getC_Payment_ID(), get_TrxName());
				payment.setIsReconciled(false);
				payment.save(get_TrxName());
			}
		}

		
		if (getC_Payment_ID() == 0)
			throw new IllegalStateException("Cannot reverse payment");
			
		MPayment payment = new MPayment(getCtx(), getC_Payment_ID(), get_TrxName());
		payment.reverseCorrectIt();
		payment.saveEx();
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;		
		return true;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"TotalDeposit")).append("=").append(getTotalDeposit())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary
	
	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getUpdatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount.
	 * 	Total Deposit
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getTotalDeposit();
	}	//	getApprovalAmt

	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
		MBankAccount ba = getBankAccount();
		return ba.getC_Currency_ID();
	}	//	getC_Currency_ID

	/**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds) 
			|| DOCSTATUS_Closed.equals(ds)
			|| DOCSTATUS_Reversed.equals(ds);
	}	//	isComplete
	
	/**
	 * Update the total deposit amount.
	 */
	public int update()
	{
		MBankDepositLine[] lines = getLines(true);

		//	Lines - in deposit currency
		BigDecimal total = Env.ZERO;
		for (int i = 0; i < lines.length; i++)
		{
			MBankDepositLine line = lines[i];
			line.update();
			total = total.add(line.getDepositAmt()); // In deposit currency
		}
		setTotalDeposit(total);
		return lines.length;
	}
	
}	//	MBankStatement
