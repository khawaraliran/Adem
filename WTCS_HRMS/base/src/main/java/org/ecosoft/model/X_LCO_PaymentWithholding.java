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
package org.ecosoft.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for LCO_PaymentWithholding
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_LCO_PaymentWithholding.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_LCO_PaymentWithholding extends PO implements I_LCO_PaymentWithholding, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_LCO_PaymentWithholding.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111117L;

    /** Standard Constructor */
    public X_LCO_PaymentWithholding (Properties ctx, int LCO_PaymentWithholding_ID, String trxName)
    {
      super (ctx, LCO_PaymentWithholding_ID, trxName);
      /** if (LCO_PaymentWithholding_ID == 0)
        {
			setC_Invoice_ID (0);
			setInvoiceAmt (Env.ZERO);
// 0
			setIsTaxIncluded (false);
// N
			setLCO_PaymentWithholding_ID (0);
			setLCO_WithholdingType_ID (0);
			setProcessed (false);
// N
			setTaxAmt (Env.ZERO);
			setTaxBaseAmt (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_LCO_PaymentWithholding (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 1 - Org 
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
      StringBuffer sb = new StringBuffer ("X_LCO_PaymentWithholding[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_AllocationLine getC_AllocationLine() throws RuntimeException
    {
		return (I_C_AllocationLine)MTable.get(getCtx(), I_C_AllocationLine.Table_Name)
			.getPO(getC_AllocationLine_ID(), get_TrxName());	}

	/** Set Allocation Line.
		@param C_AllocationLine_ID 
		Allocation Line
	  */
	public void setC_AllocationLine_ID (int C_AllocationLine_ID)
	{
		if (C_AllocationLine_ID < 1) 
			set_Value (COLUMNNAME_C_AllocationLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_AllocationLine_ID, Integer.valueOf(C_AllocationLine_ID));
	}

	/** Get Allocation Line.
		@return Allocation Line
	  */
	public int getC_AllocationLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_AllocationLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Charge getC_Charge() throws RuntimeException
    {
		return (I_C_Charge)MTable.get(getCtx(), I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		throw new IllegalArgumentException ("C_Charge_ID is virtual column");	}

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

	public I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
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

	public I_C_InvoiceLine getC_InvoiceLine() throws RuntimeException
    {
		return (I_C_InvoiceLine)MTable.get(getCtx(), I_C_InvoiceLine.Table_Name)
			.getPO(getC_InvoiceLine_ID(), get_TrxName());	}

	/** Set Invoice Line.
		@param C_InvoiceLine_ID 
		Invoice Detail Line
	  */
	public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
	{
		if (C_InvoiceLine_ID < 1) 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, Integer.valueOf(C_InvoiceLine_ID));
	}

	/** Get Invoice Line.
		@return Invoice Detail Line
	  */
	public int getC_InvoiceLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_InvoiceLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_PaymentAllocate getC_PaymentAllocate() throws RuntimeException
    {
		return (I_C_PaymentAllocate)MTable.get(getCtx(), I_C_PaymentAllocate.Table_Name)
			.getPO(getC_PaymentAllocate_ID(), get_TrxName());	}

	/** Set Allocate Payment.
		@param C_PaymentAllocate_ID 
		Allocate Payment to Invoices
	  */
	public void setC_PaymentAllocate_ID (int C_PaymentAllocate_ID)
	{
		if (C_PaymentAllocate_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_PaymentAllocate_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_PaymentAllocate_ID, Integer.valueOf(C_PaymentAllocate_ID));
	}

	/** Get Allocate Payment.
		@return Allocate Payment to Invoices
	  */
	public int getC_PaymentAllocate_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentAllocate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Tax getC_Tax() throws RuntimeException
    {
		return (I_C_Tax)MTable.get(getCtx(), I_C_Tax.Table_Name)
			.getPO(getC_Tax_ID(), get_TrxName());	}

	/** Set Tax.
		@param C_Tax_ID 
		Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID)
	{
		if (C_Tax_ID < 1) 
			set_Value (COLUMNNAME_C_Tax_ID, null);
		else 
			set_Value (COLUMNNAME_C_Tax_ID, Integer.valueOf(C_Tax_ID));
	}

	/** Get Tax.
		@return Tax identifier
	  */
	public int getC_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set Invoice Amt.
		@param InvoiceAmt Invoice Amt	  */
	public void setInvoiceAmt (BigDecimal InvoiceAmt)
	{
		set_Value (COLUMNNAME_InvoiceAmt, InvoiceAmt);
	}

	/** Get Invoice Amt.
		@return Invoice Amt	  */
	public BigDecimal getInvoiceAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_InvoiceAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Is Calc On Payment.
		@param IsCalcOnPayment Is Calc On Payment	  */
	public void setIsCalcOnPayment (boolean IsCalcOnPayment)
	{
		set_ValueNoCheck (COLUMNNAME_IsCalcOnPayment, Boolean.valueOf(IsCalcOnPayment));
	}

	/** Get Is Calc On Payment.
		@return Is Calc On Payment	  */
	public boolean isCalcOnPayment () 
	{
		Object oo = get_Value(COLUMNNAME_IsCalcOnPayment);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Price includes Tax.
		@param IsTaxIncluded 
		Tax is included in the price 
	  */
	public void setIsTaxIncluded (boolean IsTaxIncluded)
	{
		set_Value (COLUMNNAME_IsTaxIncluded, Boolean.valueOf(IsTaxIncluded));
	}

	/** Get Price includes Tax.
		@return Tax is included in the price 
	  */
	public boolean isTaxIncluded () 
	{
		Object oo = get_Value(COLUMNNAME_IsTaxIncluded);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Payment TDS.
		@param LCO_PaymentWithholding_ID Payment TDS	  */
	public void setLCO_PaymentWithholding_ID (int LCO_PaymentWithholding_ID)
	{
		if (LCO_PaymentWithholding_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LCO_PaymentWithholding_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LCO_PaymentWithholding_ID, Integer.valueOf(LCO_PaymentWithholding_ID));
	}

	/** Get Payment TDS.
		@return Payment TDS	  */
	public int getLCO_PaymentWithholding_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_PaymentWithholding_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TDS Rule.
		@param LCO_WithholdingRule_ID TDS Rule	  */
	public void setLCO_WithholdingRule_ID (int LCO_WithholdingRule_ID)
	{
		if (LCO_WithholdingRule_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LCO_WithholdingRule_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LCO_WithholdingRule_ID, Integer.valueOf(LCO_WithholdingRule_ID));
	}

	/** Get TDS Rule.
		@return TDS Rule	  */
	public int getLCO_WithholdingRule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_WithholdingRule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payment Nature.
		@param LCO_WithholdingType_ID Payment Nature	  */
	public void setLCO_WithholdingType_ID (int LCO_WithholdingType_ID)
	{
		if (LCO_WithholdingType_ID < 1) 
			set_Value (COLUMNNAME_LCO_WithholdingType_ID, null);
		else 
			set_Value (COLUMNNAME_LCO_WithholdingType_ID, Integer.valueOf(LCO_WithholdingType_ID));
	}

	/** Get Payment Nature.
		@return Payment Nature	  */
	public int getLCO_WithholdingType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_WithholdingType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		throw new IllegalArgumentException ("M_Product_ID is virtual column");	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Percent.
		@param Percent 
		Percentage
	  */
	public void setPercent (BigDecimal Percent)
	{
		set_Value (COLUMNNAME_Percent, Percent);
	}

	/** Get Percent.
		@return Percentage
	  */
	public BigDecimal getPercent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Tax Amount.
		@param TaxAmt 
		Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		set_Value (COLUMNNAME_TaxAmt, TaxAmt);
	}

	/** Get Tax Amount.
		@return Tax Amount for a document
	  */
	public BigDecimal getTaxAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Tax base Amount.
		@param TaxBaseAmt 
		Base for calculating the tax amount
	  */
	public void setTaxBaseAmt (BigDecimal TaxBaseAmt)
	{
		set_Value (COLUMNNAME_TaxBaseAmt, TaxBaseAmt);
	}

	/** Get Tax base Amount.
		@return Base for calculating the tax amount
	  */
	public BigDecimal getTaxBaseAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxBaseAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Tax Return Form.
		@param WTC_TaxReturnForm_ID Tax Return Form	  */
	public void setWTC_TaxReturnForm_ID (int WTC_TaxReturnForm_ID)
	{
		if (WTC_TaxReturnForm_ID < 1) 
			set_Value (COLUMNNAME_WTC_TaxReturnForm_ID, null);
		else 
			set_Value (COLUMNNAME_WTC_TaxReturnForm_ID, Integer.valueOf(WTC_TaxReturnForm_ID));
	}

	/** Get Tax Return Form.
		@return Tax Return Form	  */
	public int getWTC_TaxReturnForm_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_TaxReturnForm_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}