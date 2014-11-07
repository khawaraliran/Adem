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
 
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
 
/**
 *	Bank Deposit Line Model
 *
 *	@author Michael McKay
 */
 public class MBankDepositLine extends X_C_BankDepositLine
 {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6478916594928339137L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementLine_ID id
	 *	@param trxName transaction
	 */
	public MBankDepositLine (Properties ctx, int C_BankStatementLine_ID, String trxName)
	{
		super (ctx, C_BankStatementLine_ID, trxName);
		if (C_BankStatementLine_ID == 0)
		{
			setDepositAmt(Env.ZERO);
			setTrxAmt(Env.ZERO);
			setChargeAmt(Env.ZERO);
			setIsReversal (false);  //  TODO: is this required?
		}
	}	//	MBankStatementLine
	
	/**
	 *	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MBankDepositLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatementLine
	
	/**
	 * 	Parent Constructor
	 * 	@param deposit Bank Deposit that the line is part of
	 */
	public MBankDepositLine(MBankDeposit deposit)
	{
		this (deposit.getCtx(), 0, deposit.get_TrxName());
		setClientOrg(deposit);
		setC_BankDeposit_ID(deposit.getC_BankDeposit_ID());
	}	//	MBankStatementLine

	/**
	 * 	Parent Constructor
	 * 	@param deposit Bank Deposit that the line is part of
	 * 	@param lineNo position of the line within the statement
	 */
	public MBankDepositLine(MBankDeposit deposit, int lineNo)
	{
		this (deposit);
		setLine(lineNo);
	}	//	MBankDepositLine

	/**
	 * 	Set Payment
	 *	@param payment payment
	 */
	public void setPayment (MPayment payment)
	{
		setC_Payment_ID (payment.getC_Payment_ID());
		setC_Currency_ID (payment.getC_Currency_ID());
		//
		BigDecimal chargeAmt = getChargeAmt();  // In bank currency
		setTrxAmt(payment.getPayAmt(false));  // In source currency
		// Convert to bank currency on the day (DateAcct) of deposit
		BigDecimal amt = MConversionRate.convert (getCtx(), payment.getPayAmt(), 
				payment.getC_Currency_ID(), getParent().getC_Currency_ID(),
				getParent().getDateAcct(), payment.getC_ConversionType_ID(), 
				payment.getAD_Client_ID(), payment.getAD_Org_ID());
		setDepositAmt(amt.add(chargeAmt));  // In bank currency
		//
		setDescription(payment.getDescription());
	}	//	setPayment

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
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord && getParent().isComplete()) {
			log.saveError("ParentComplete", Msg.translate(getCtx(), "C_BankDepositLine"));
			return false;
		}
		if (getChargeAmt().signum() != 0 && getC_Charge_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Charge_ID"));
			return false;
		}
		// Un-link Payment if TrxAmt is zero  TODO - check that this is correct 
		if (getTrxAmt().signum() == 0 && getC_Payment_ID() > 0)
		{
			setC_Payment_ID(I_ZERO);
		}
		//	Set Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_BankDepositLine WHERE C_BankDeposit_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getC_BankDeposit_ID());
			setLine (ii);
		}
		
		//	Set References
		if (getC_Payment_ID() != 0 && getC_BPartner_ID() == 0)
		{
			MPayment payment = new MPayment (getCtx(), getC_Payment_ID(), get_TrxName());
			setC_BPartner_ID(payment.getC_BPartner_ID());
			if (payment.getC_Invoice_ID() != 0)
				setC_Invoice_ID(payment.getC_Invoice_ID());
		}
		if (getC_Invoice_ID() != 0 && getC_BPartner_ID() == 0)
		{
			MInvoice invoice = new MInvoice (getCtx(), getC_Invoice_ID(), get_TrxName());
			setC_BPartner_ID(invoice.getC_BPartner_ID());
		}
		
		//	Calculate Charge = Deposit - trx - Interest  
		//BigDecimal amt = getDepositAmt();
		//amt = amt.subtract(getTrxAmt());
		//if (amt.compareTo(getChargeAmt()) != 0)
		//	setChargeAmt (amt);
		//
		
		return true;
	}	//	beforeSave
	
	/** Parent					*/
	private MBankDeposit			m_parent = null;
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MBankDeposit getParent()
	{
		if (m_parent == null)
			m_parent = new MBankDeposit (getCtx(), getC_BankDeposit_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return (updateHeader());
	}	//	afterSave

	/**
	 * 	Update Header
	 */
	private boolean updateHeader()
	{
		//  Update the total deposit amount on the header in the bank currency
		MBankDeposit depositHdr = getParent();
		depositHdr.update();
		return (depositHdr.save());
	}	//	updateHeader

	public void update() {
		//  Get charge & payment
		BigDecimal convAmt = Env.ZERO;
		BigDecimal deposit;
		BigDecimal charge = this.getChargeAmt();

		if (charge == null)
			charge = Env.ZERO;
		
		// Check the bank currency - the payment could be in a different currency
		MBankDeposit parent = getParent();
		int currencyID = parent.getC_Currency_ID();
		MPayment pmt = new MPayment(getCtx(),this.getC_Payment_ID(),null);
		if (currencyID > 0)
		{
			if (currencyID == pmt.getC_Currency_ID())
			{
				// Same currencies - use the payment amt
				convAmt = pmt.getPayAmt(true);
			}
			else
			{
				// Convert the payment to the line currency on the line deposit date.
				Timestamp convDate = (Timestamp) parent.getDepositDate();  
				if (convDate != null)
				{
					convAmt = MConversionRate.convert(getCtx(), pmt.getPayAmt(true), pmt.getC_Currency_ID(), currencyID, convDate, pmt.getC_ConversionType_ID(), pmt.getAD_Client_ID(), pmt.getAD_Org_ID());
				}
			}
		}
		else
		{
			log.severe("Bank currency ID is null. Can't add payment to deposit line.");			
		}

		deposit = charge.add(convAmt);
		if (!this.getDepositAmt().equals(deposit)){
			this.setDepositAmt(deposit);
			this.saveEx();
		}
	}
	
 }	//	MBankDepositLine
