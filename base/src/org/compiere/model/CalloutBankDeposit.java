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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.Env;


/**
 *	Bank Deposit Callout	
 *	
 *  @author Michael McKay
 *  
 *  Copied largely from CalloutBankStatement.java
 *  @author Jorg Janke
 *  @version $Id: CalloutBankStatement.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class CalloutBankDeposit extends CalloutEngine
{
	/**
	 * 	Bank Account Changed.
	 * 	Update Beginning Balance
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String bankAccount (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value == null)
			return "";
		int C_BankAccount_ID = ((Integer)value).intValue();
		MBankAccount ba = MBankAccount.get(ctx, C_BankAccount_ID);
		ba.load(ba.get_TrxName());
		mTab.setValue("BeginningBalance", ba.getCurrentBalance());
		return "";
	}	//	bankAccount
	
	/**
	 *	BankDeposit - Amount.
	 *  Calculate ChargeAmt = StmtAmt - TrxAmt - InterestAmt
	 *    or id Charge is entered - InterestAmt = StmtAmt - TrxAmt - ChargeAmt
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String amount (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";

		//  Get Stmt & Trx
		BigDecimal trx = (BigDecimal)mTab.getValue("ConvertedAmt");
		BigDecimal charge = (BigDecimal)mTab.getValue("ChargeAmt");
		BigDecimal deposit = (BigDecimal)mTab.getValue("DepositAmt");
		if (trx == null)
			trx = Env.ZERO;
		if (charge == null)
			charge = Env.ZERO;
		if (deposit == null)
			deposit = Env.ZERO;

		deposit = trx.add(charge);
		mTab.setValue("DepositAmt", deposit);

		return "";
	}   //  amount


	/**
	 *	BankStmt - Payment.
	 *  Update Transaction Amount when payment is selected
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String payment (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		Integer C_Payment_ID = (Integer)value;
		if (C_Payment_ID == null || C_Payment_ID.intValue() == 0)
			return "";
		//
		Integer AD_Client_ID = (Integer) mTab.getField("AD_Client_ID").getValue();
		if (AD_Client_ID == null || AD_Client_ID.intValue() == 0)
			return "";
		//
		Integer AD_Org_ID = (Integer) mTab.getField("AD_Org_ID").getValue();
		if (AD_Org_ID == null || AD_Org_ID.intValue() == 0)
			return "";
		//
		BigDecimal deposit = (BigDecimal)mTab.getValue("DepositAmt");
		BigDecimal convertedAmt = (BigDecimal)mTab.getValue("ConvertedAmt");
		if (deposit == null)
			deposit = Env.ZERO;
		if (convertedAmt == null)
			convertedAmt = Env.ZERO;
		deposit = deposit.subtract(convertedAmt);  // Remove the previous trxAmt
		//
		MPayment pmt = new MPayment(ctx,C_Payment_ID,null);

		// Check the bank currency - the payment could be in a different currency
		Object currencyID = mTab.getField("C_Currency_ID").getValue();
		if (currencyID != null)
		{
			if (((Integer) currencyID).intValue() == pmt.getC_Currency_ID())
			{
				// Same currencies - use the payment amt
				convertedAmt = pmt.getPayAmt(true);
			}
			else
			{
				// Convert the payment to the line currency on the line deposit date.
				GridTab parent = mTab.getParentTab();
				if (parent.getValue("DateAcct") != null)
				{
					Timestamp convDate = (Timestamp) parent.getValue("DateAcct");  
					convertedAmt = MConversionRate.convert(ctx, pmt.getPayAmt(true), pmt.getC_Currency_ID(), ((Integer) currencyID).intValue(), convDate, pmt.getC_ConversionType_ID(), AD_Client_ID.intValue(), AD_Org_ID.intValue());
				}
			}
			mTab.setValue("TrxAmt", pmt.getPayAmt());
			deposit = deposit.add(convertedAmt);
			mTab.setValue("DepositAmt", deposit);
		}
		else
		{
			log.severe("Line currency ID is null. Can't add payment.");			
		}
		
		//  Recalculate Amounts
		amount (ctx, WindowNo, mTab, mField, value);
		return "";

	}	//	payment

}	//	CalloutBankDeposit



