/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_BankDepositLine
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_C_BankDepositLine extends PO implements I_C_BankDepositLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140112L;

    /** Standard Constructor */
    public X_C_BankDepositLine (Properties ctx, int C_BankDepositLine_ID, String trxName)
    {
      super (ctx, C_BankDepositLine_ID, trxName);
      /** if (C_BankDepositLine_ID == 0)
        {
			setC_BankDeposit_ID (0);
			setC_BankDepositLine_ID (0);
			setC_Currency_ID (0);
// @SQL=SELECT C_Currency_ID FROM C_BankAccount WHERE C_BankAccount_ID=@C_BankAccount_ID@
			setChargeAmt (Env.ZERO);
			setDepositAmt (Env.ZERO);
			setIsManual (true);
// Y
			setIsReversal (false);
			setLine (0);
// @SQL=SELECT COALESCE(MAX(Line),0)+10 FROM C_BankDepositLine WHERE C_BankDeposit_ID=@C_BankDeposit_ID@
			setProcessed (false);
			setTrxAmt (Env.ZERO);
			setValutaDate (new Timestamp( System.currentTimeMillis() ));
// @DepositDate@
        } */
    }

    /** Load Constructor */
    public X_C_BankDepositLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_C_BankDepositLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Bank Currency.
		@param BankCurrency 
		The currency of the bank
	  */
	public void setBankCurrency (String BankCurrency)
	{
		throw new IllegalArgumentException ("BankCurrency is virtual column");	}

	/** Get Bank Currency.
		@return The currency of the bank
	  */
	public String getBankCurrency () 
	{
		return (String)get_Value(COLUMNNAME_BankCurrency);
	}

	public I_C_BankDeposit getC_BankDeposit() throws RuntimeException
    {
		return (I_C_BankDeposit)MTable.get(getCtx(), I_C_BankDeposit.Table_Name)
			.getPO(getC_BankDeposit_ID(), get_TrxName());	}

	/** Set Bank Deposit.
		@param C_BankDeposit_ID 
		A bank deposit is a collection of items that are provided to the bank for deposit into the bank account
	  */
	public void setC_BankDeposit_ID (int C_BankDeposit_ID)
	{
		if (C_BankDeposit_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankDeposit_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankDeposit_ID, Integer.valueOf(C_BankDeposit_ID));
	}

	/** Get Bank Deposit.
		@return A bank deposit is a collection of items that are provided to the bank for deposit into the bank account
	  */
	public int getC_BankDeposit_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankDeposit_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Bank Deposit Line.
		@param C_BankDepositLine_ID Bank Deposit Line	  */
	public void setC_BankDepositLine_ID (int C_BankDepositLine_ID)
	{
		if (C_BankDepositLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankDepositLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankDepositLine_ID, Integer.valueOf(C_BankDepositLine_ID));
	}

	/** Get Bank Deposit Line.
		@return Bank Deposit Line	  */
	public int getC_BankDepositLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankDepositLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Charge amount.
		@param ChargeAmt 
		Charge Amount
	  */
	public void setChargeAmt (BigDecimal ChargeAmt)
	{
		set_Value (COLUMNNAME_ChargeAmt, ChargeAmt);
	}

	/** Get Charge amount.
		@return Charge Amount
	  */
	public BigDecimal getChargeAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ChargeAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Converted Amount.
		@param ConvertedAmt 
		Converted Amount
	  */
	public void setConvertedAmt (BigDecimal ConvertedAmt)
	{
		throw new IllegalArgumentException ("ConvertedAmt is virtual column");	}

	/** Get Converted Amount.
		@return Converted Amount
	  */
	public BigDecimal getConvertedAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ConvertedAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Deposit amount.
		@param DepositAmt 
		Deposit Amount
	  */
	public void setDepositAmt (BigDecimal DepositAmt)
	{
		set_Value (COLUMNNAME_DepositAmt, DepositAmt);
	}

	/** Get Deposit amount.
		@return Deposit Amount
	  */
	public BigDecimal getDepositAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DepositAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Manual.
		@param IsManual 
		This is a manual process
	  */
	public void setIsManual (boolean IsManual)
	{
		set_Value (COLUMNNAME_IsManual, Boolean.valueOf(IsManual));
	}

	/** Get Manual.
		@return This is a manual process
	  */
	public boolean isManual () 
	{
		Object oo = get_Value(COLUMNNAME_IsManual);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Reversal.
		@param IsReversal 
		This is a reversing transaction
	  */
	public void setIsReversal (boolean IsReversal)
	{
		set_Value (COLUMNNAME_IsReversal, Boolean.valueOf(IsReversal));
	}

	/** Get Reversal.
		@return This is a reversing transaction
	  */
	public boolean isReversal () 
	{
		Object oo = get_Value(COLUMNNAME_IsReversal);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
    }

	/** Set Memo.
		@param Memo 
		Memo Text
	  */
	public void setMemo (String Memo)
	{
		set_Value (COLUMNNAME_Memo, Memo);
	}

	/** Get Memo.
		@return Memo Text
	  */
	public String getMemo () 
	{
		return (String)get_Value(COLUMNNAME_Memo);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Reference No.
		@param ReferenceNo 
		Your customer or vendor number at the Business Partner's site
	  */
	public void setReferenceNo (String ReferenceNo)
	{
		set_Value (COLUMNNAME_ReferenceNo, ReferenceNo);
	}

	/** Get Reference No.
		@return Your customer or vendor number at the Business Partner's site
	  */
	public String getReferenceNo () 
	{
		return (String)get_Value(COLUMNNAME_ReferenceNo);
	}

	/** Set Transaction Amount.
		@param TrxAmt 
		Amount of a transaction
	  */
	public void setTrxAmt (BigDecimal TrxAmt)
	{
		set_Value (COLUMNNAME_TrxAmt, TrxAmt);
	}

	/** Get Transaction Amount.
		@return Amount of a transaction
	  */
	public BigDecimal getTrxAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TrxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Effective date.
		@param ValutaDate 
		Date when money is available
	  */
	public void setValutaDate (Timestamp ValutaDate)
	{
		set_Value (COLUMNNAME_ValutaDate, ValutaDate);
	}

	/** Get Effective date.
		@return Date when money is available
	  */
	public Timestamp getValutaDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValutaDate);
	}
}