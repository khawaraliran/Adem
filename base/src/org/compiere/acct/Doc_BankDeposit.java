/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2014 Michael McKay All Rights Reserved.                      *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBankAccount;
import org.compiere.model.MBankDeposit;
import org.compiere.model.MBankDepositLine;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCurrency;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *  Post Bank Deposits.
 *  <pre>
 *  Table:              C_BankDeposit
 *  Document Types:     CMD
 *  </pre>
 *  @author Michael McKay
 *  
 *  Based on Doc_BankStatement
 */
public class Doc_BankDeposit extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_BankDeposit (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MBankDeposit.class, rs, DOCTYPE_BankDeposit, trxName);
	}	//	Doc_BankDeposit
	
	/** Bank Account			*/
	private int	m_C_BankAccount_ID = 0;
	private int m_AD_Org_ID;
	private int m_C_BPartner_ID;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MBankDeposit bd = (MBankDeposit)getPO();
		setDateDoc(bd.getDepositDate());
		setDateAcct(bd.getDateAcct());
		
		m_C_BankAccount_ID = bd.getC_BankAccount_ID();
		//	Amounts
		setAmount(AMTTYPE_Gross, bd.getTotalDeposit());

		//  Set Bank Account Info (Currency)
		MBankAccount ba = MBankAccount.get (getCtx(), m_C_BankAccount_ID);
		setC_Currency_ID (ba.getC_Currency_ID());

		//	Contained Objects
		p_lines = loadLines(bd);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Deposit Line.
	 *	@param bd bank deposit
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MBankDeposit bd)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MBankDepositLine[] lines = bd.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankDepositLine line = lines[i];
			DocLine_Deposit docLine = new DocLine_Deposit(line, this);
			MPeriod period = MPeriod.get(getCtx(), bd.getDateAcct(), line.getAD_Org_ID());
			if (period != null && period.isOpen(DOCTYPE_BankDeposit, bd.getDateAcct()))
				docLine.setC_Period_ID(period.getC_Period_ID());
			//
			list.add(docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line amounts from total - no rounding
	 *  @return positive amount, if total deposit is bigger than line deposit amounts
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			BigDecimal lineBalance = ((DocLine_Deposit)p_lines[i]).getDepositAmt();
			retValue = retValue.subtract(lineBalance);
			sb.append("-").append(lineBalance);
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  CMB.
	 *  <pre>
	 *      BankInTransit   DR      CR  (Deposit)
	 *      BankInTransit   DR      CR              (Payment)
	 *      Charge          DR          (Charge)
	 *      Interest        DR      CR  (Interest)
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		// boolean isInterOrg = isInterOrg(as);

		//  Header -- there may be different currency amounts

		//  The goal of the FACTS create for a deposit is to generate a trail from payment to bank account via the 
		//  bank deposit document and the bank statement.  Deposits combine a number of payments, so the trail for
		//  for a single payment is Payment->Deposit->Statement->Bank asset
		
		//  Bank Deposit FACTS create:
		//    * currency conversion to bank currency and an entry for realized gain, if required
		//    * account entries for any payment deposited - transit account balance should be zero in source and accounted currency
		//    * charge and interest amounts, if any
		//	  * a summary transit account entry for the deposit which will balance the "fake" payment
		//      created in MBankDeposit
		//
		//  Assumptions in creating the FACTS:
		//  1. The currency of the bank account and the payment may be different
		//  2. The trxAmt is the value of the payment in the payment currency.
		//
		//  The fact creation matches that in Doc_BankStatement.java
		
		FactLine fl = null;
		m_AD_Org_ID = getBank_Org_ID();	//	Bank Account Org
		BigDecimal totalDeposit = Env.ZERO;
		
		MAccount acct_bank_asset =  getAccount(Doc.ACCTTYPE_BankAsset, as);
		MAccount acct_bank_in_transit = getAccount(Doc.ACCTTYPE_BankInTransit, as);

		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine_Deposit line = (DocLine_Deposit)p_lines[i];
			m_C_BPartner_ID = line.getC_BPartner_ID();
			int C_Payment_ID = line.getC_Payment_ID();
			//
			//  Deposit amount is in bank currency
			BigDecimal depositAmt = Env.ZERO.add(line.getDepositAmt());
			totalDeposit = totalDeposit.add(depositAmt);
			//
			// Get payment details
			MPayment pmt = null;
			if (C_Payment_ID != 0)  // Don't create a payment
				pmt = new MPayment(getCtx(), C_Payment_ID, getTrxName());
										
			// Test for currency differences and add realized gains/losses, if any
			if (pmt != null)
			{
				if (getC_Currency_ID() != pmt.getC_Currency_ID())
				{
					this.setIsMultiCurrency(true);  // Don't balance source currency - a conversion is going to happen.
					
					// Test the accounted amounts 
					// The original payment amount would have been posted in the in-transit account if used.
					BigDecimal pmtOrigAcctAmt = MConversionRate.convert(getCtx(), pmt.getPayAmt(), pmt.getC_Currency_ID(), 
							as.getC_Currency_ID(), pmt.getDateAcct(), pmt.getC_ConversionType_ID(), pmt.getAD_Client_ID(), pmt.getAD_Org_ID());
					BigDecimal pmtNewAcctAmt = MConversionRate.convert(getCtx(), pmt.getPayAmt(), pmt.getC_Currency_ID(), 
							as.getC_Currency_ID(), getDateAcct(), pmt.getC_ConversionType_ID(), pmt.getAD_Client_ID(), pmt.getAD_Org_ID());
					BigDecimal realizedGain = pmtNewAcctAmt.subtract(pmtOrigAcctAmt); // In system currency
					//
					//  Find the new source currency amount - payment converted to bank currency on deposit DateAcct
					BigDecimal pmtNewSourceAmt = MConversionRate.convert(getCtx(), pmt.getPayAmt(), pmt.getC_Currency_ID(), 
							getC_Currency_ID(), getDateAcct(), 0, pmt.getAD_Client_ID(), pmt.getAD_Org_ID());
					
					String description = "Payment=(" + MCurrency.getISO_Code(getCtx(), pmt.getC_Currency_ID()) + ")" + 
								pmt.getPayAmt() + "/" + pmtOrigAcctAmt
								+ " - Bank=(" + MCurrency.getISO_Code(getCtx(),getC_Currency_ID()) + ")" + pmt.getPayAmt() + "/" + pmtNewAcctAmt;

					// Convert the payment from the transit account in the payment currency 
					// to the bank currency, evaluated on the dateacct of the payment.
					Timestamp lineDateAcct = line.getDateAcct();
					line.setDateAcct(pmt.getDateAcct());  // Fake the date of the original posting
					if (pmt.isReceipt())
					{
						//	Asset - the acct amount should match pmtOrigAcctAmt
						//  This cancels the entry when the payment was posted
						fl = fact.createLine(line, acct_bank_in_transit,
							pmt.getC_Currency_ID(), null, pmt.getPayAmt());
						addBPandOrg(fl, line);
						//
						//Reset the line date
						line.setDateAcct(lineDateAcct); // Reset the line
						//
						//  Now add the payment back in the bank currency in the transit account
						//  BankAsset       DR      CR  (Statement)
						fl = fact.createLine(line, acct_bank_in_transit,
							getC_Currency_ID(), pmtNewSourceAmt, null);
						addBPandOrg(fl, line);
						//
						totalDeposit = totalDeposit.subtract(pmtNewSourceAmt);
					}
					else 
					{
						//  Shouldn't happen generally (deposit a AP payment) - may be a credit return.
						//	Asset - the acct amount should match pmtOrigAcctAmt
						//  This cancels the entry when the payment was posted
						fl = fact.createLine(line, acct_bank_in_transit,
							pmt.getC_Currency_ID(), pmt.getPayAmt(), null);
						addBPandOrg(fl, line);
						//
						//Reset the line date
						line.setDateAcct(lineDateAcct); // Reset the line
						//
						//  Now add the payment back in the bank currency in the transit account
						//  BankAsset       DR      CR  (Statement)
						fl = fact.createLine(line, acct_bank_in_transit,
							getC_Currency_ID(), null, pmtNewSourceAmt);
						addBPandOrg(fl, line);
						//
						totalDeposit = totalDeposit.add(pmtNewSourceAmt);
					}

					if (!(realizedGain.compareTo(Env.ZERO) == 0))
					{
						// Add the realized gain on the transaction so the total matches pmtNewAcctAmt
						MAccount gain = MAccount.get (as.getCtx(), as.getAcctSchemaDefault().getRealizedGain_Acct());
						MAccount loss = MAccount.get (as.getCtx(), as.getAcctSchemaDefault().getRealizedLoss_Acct());
						fl = fact.createLine (line, loss, gain, 
								as.getC_Currency_ID(), realizedGain.negate());
						fl.setDescription(description);
						addBPandOrg(fl, line);
					}
				}
				else // all in same currency
				{
					// If there is no realized gain, or currency conversion, the payment fact is already in the transit
					// account.  The sum of all payments in the deposit will be balanced by the bank statement when the
					// deposit is reconciled.  No fact needs to be added in this case here.
					//
					totalDeposit = totalDeposit.subtract(line.getTrxAmt());  // Both in bank currency
				}
			}
			else // pmt is null - trxAmt should be zero
			{
				//  This shouldn't happen - see MBankDepositLine.beforeSave() Un-link Payment if TrxAmt is zero 
				if (line.getTrxAmt().compareTo(Env.ZERO) != 0)
					log.warning("No payment but transaction amount is not zero. Ignoring.");
			}

			if ((!as.isPostIfClearingEqual()) && acct_bank_asset.equals(acct_bank_in_transit)) {
				// Not using clearing accounts
				// just post the difference (if any) - should equal the charge amount

				BigDecimal amt_deposit_minus_trx = line.getDepositAmt().subtract(line.getConvertedAmt());
				if (amt_deposit_minus_trx.compareTo(Env.ZERO) != 0) {
	
					//  BankAsset       DR      CR  (Statement minus Payment)
					fl = fact.createLine(line,
						getAccount(Doc.ACCTTYPE_BankAsset, as),
						getC_Currency_ID(), amt_deposit_minus_trx);
					addBPandOrg(fl, line);					
				}
				
			} else {	
				//  Normal Adempiere behavior
				//  We don't need to do anything here.
			}
				
			//  Add the charge expenses
			if (line.getChargeAmt().compareTo(Env.ZERO) != 0)
			{
				if (line.getChargeAmt().compareTo(Env.ZERO) > 0) {
					fl = fact.createLine(line,
							line.getChargeAccount(as, line.getChargeAmt().negate()),
							getC_Currency_ID(), null, line.getChargeAmt());
				} else {
					fl = fact.createLine(line,
							line.getChargeAccount(as, line.getChargeAmt().negate()),
							getC_Currency_ID(), line.getChargeAmt().negate(), null);
				}
				addBPandOrg(fl, line);
			}

		} //  Next line
		
		//  Finally, add the new transit amount that will reflect the "fake" payment
		if (totalDeposit.compareTo(Env.ZERO) != 0) {	

			MBankDepositLine bdLine = new MBankDepositLine(Env.getCtx(), 0, getTrxName());
			bdLine.setAD_Org_ID(getAD_Org_ID());
			bdLine.setC_Currency_ID(getC_Currency_ID());
			bdLine.setValutaDate(getDateAcct());
			bdLine.setIsReversal(false);
			bdLine.setC_Payment_ID(0);
			bdLine.setTrxAmt(totalDeposit);
			bdLine.setDepositAmt(totalDeposit);
			m_C_BPartner_ID = 0;  // Don't assign a business partner to the deposit
			DocLine_Deposit line = new DocLine_Deposit(bdLine, this);
			
			if ((!as.isPostIfClearingEqual()) && acct_bank_asset.equals(acct_bank_in_transit)) {
				// Not using clearing accounts
				// just post the difference (if any)
				
				//  BankAsset       DR      CR  (Statement minus Payment)
				fl = fact.createLine(line, acct_bank_asset,
					getC_Currency_ID(), totalDeposit);
				addBPandOrg(fl, line);					
			} else {
				fl = fact.createLine(line, acct_bank_in_transit,
						getC_Currency_ID(), totalDeposit);
				addBPandOrg(fl, line);				
			}
			if (fl != null)
			{
				fl.setDescription(fl.getDescription() + " " + Msg.translate(getCtx(), "Bank Deposit"));
			}
		}		
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	/**
	 * 	Get AD_Org_ID from Bank Account
	 * 	@return AD_Org_ID or 0
	 */
	private int getBank_Org_ID ()
	{
		if (m_C_BankAccount_ID == 0)
			return 0;
		//
		MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
		return ba.getAD_Org_ID();
	}	//	getBank_Org_ID
	
	private void addBPandOrg(FactLine fl, DocLine_Deposit line)
	{
		if (fl != null)
		{
			if (m_C_BPartner_ID != 0)
				fl.setC_BPartner_ID(m_C_BPartner_ID);
			if (m_AD_Org_ID != 0)
				fl.setAD_Org_ID(m_AD_Org_ID);
			else
				fl.setAD_Org_ID(line.getAD_Org_ID(true)); // from payment
		}
	}
}   //  Doc_Bank
