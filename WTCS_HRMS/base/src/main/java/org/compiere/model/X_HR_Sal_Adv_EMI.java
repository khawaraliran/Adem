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

/** Generated Model for HR_Sal_Adv_EMI
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Sal_Adv_EMI.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Sal_Adv_EMI extends PO implements I_HR_Sal_Adv_EMI, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Sal_Adv_EMI.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111221L;

    /** Standard Constructor */
    public X_HR_Sal_Adv_EMI (Properties ctx, int HR_Sal_Adv_EMI_ID, String trxName)
    {
      super (ctx, HR_Sal_Adv_EMI_ID, trxName);
      /** if (HR_Sal_Adv_EMI_ID == 0)
        {
			setHR_Sal_Adv_EMI_ID (0);
			setHR_Sal_Adv_Req_ID (0);
			setispaidoff (false);
			setispartial (false);
			setLineNo (0);
			setProcessed (false);
			setskipinpayslip (false);
        } */
    }

    /** Load Constructor */
    public X_HR_Sal_Adv_EMI (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
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
      StringBuffer sb = new StringBuffer ("X_HR_Sal_Adv_EMI[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set actualemiamount.
		@param actualemiamount actualemiamount	  */
	public void setactualemiamount (BigDecimal actualemiamount)
	{
		set_Value (COLUMNNAME_actualemiamount, actualemiamount);
	}

	/** Get actualemiamount.
		@return actualemiamount	  */
	public BigDecimal getactualemiamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_actualemiamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_Payment getC_Payment() throws RuntimeException
    {
		return (I_C_Payment)MTable.get(getCtx(), I_C_Payment.Table_Name)
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

	/** Set emiamount.
		@param emiamount emiamount	  */
	public void setemiamount (BigDecimal emiamount)
	{
		set_Value (COLUMNNAME_emiamount, emiamount);
	}

	/** Get emiamount.
		@return emiamount	  */
	public BigDecimal getemiamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_emiamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.eevolution.model.I_HR_Period getHR_Period() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Period)MTable.get(getCtx(), org.eevolution.model.I_HR_Period.Table_Name)
			.getPO(getHR_Period_ID(), get_TrxName());	}

	/** Set Payroll Period.
		@param HR_Period_ID Payroll Period	  */
	public void setHR_Period_ID (int HR_Period_ID)
	{
		if (HR_Period_ID < 1) 
			set_Value (COLUMNNAME_HR_Period_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Period_ID, Integer.valueOf(HR_Period_ID));
	}

	/** Get Payroll Period.
		@return Payroll Period	  */
	public int getHR_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Sal_Adv_EMI gethr_ref_sal_adv_emi() throws RuntimeException
    {
		return (I_HR_Sal_Adv_EMI)MTable.get(getCtx(), I_HR_Sal_Adv_EMI.Table_Name)
			.getPO(gethr_ref_sal_adv_emi_id(), get_TrxName());	}

	/** Set HR_Ref_Sal_Adv_Emi_ID.
		@param hr_ref_sal_adv_emi_id HR_Ref_Sal_Adv_Emi_ID	  */
	public void sethr_ref_sal_adv_emi_id (int hr_ref_sal_adv_emi_id)
	{
		set_Value (COLUMNNAME_hr_ref_sal_adv_emi_id, Integer.valueOf(hr_ref_sal_adv_emi_id));
	}

	/** Get HR_Ref_Sal_Adv_Emi_ID.
		@return HR_Ref_Sal_Adv_Emi_ID	  */
	public int gethr_ref_sal_adv_emi_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_hr_ref_sal_adv_emi_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HR_Sal_Adv_EMI.
		@param HR_Sal_Adv_EMI_ID HR_Sal_Adv_EMI	  */
	public void setHR_Sal_Adv_EMI_ID (int HR_Sal_Adv_EMI_ID)
	{
		if (HR_Sal_Adv_EMI_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Sal_Adv_EMI_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Sal_Adv_EMI_ID, Integer.valueOf(HR_Sal_Adv_EMI_ID));
	}

	/** Get HR_Sal_Adv_EMI.
		@return HR_Sal_Adv_EMI	  */
	public int getHR_Sal_Adv_EMI_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Sal_Adv_EMI_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Sal_Adv_Req getHR_Sal_Adv_Req() throws RuntimeException
    {
		return (I_HR_Sal_Adv_Req)MTable.get(getCtx(), I_HR_Sal_Adv_Req.Table_Name)
			.getPO(getHR_Sal_Adv_Req_ID(), get_TrxName());	}

	/** Set Advance Salary Request / Loan.
		@param HR_Sal_Adv_Req_ID Advance Salary Request / Loan	  */
	public void setHR_Sal_Adv_Req_ID (int HR_Sal_Adv_Req_ID)
	{
		if (HR_Sal_Adv_Req_ID < 1) 
			set_Value (COLUMNNAME_HR_Sal_Adv_Req_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Sal_Adv_Req_ID, Integer.valueOf(HR_Sal_Adv_Req_ID));
	}

	/** Get Advance Salary Request / Loan.
		@return Advance Salary Request / Loan	  */
	public int getHR_Sal_Adv_Req_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Sal_Adv_Req_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set is Already Paid Back.
		@param ispaidoff is Already Paid Back	  */
	public void setispaidoff (boolean ispaidoff)
	{
		set_Value (COLUMNNAME_ispaidoff, Boolean.valueOf(ispaidoff));
	}

	/** Get is Already Paid Back.
		@return is Already Paid Back	  */
	public boolean ispaidoff () 
	{
		Object oo = get_Value(COLUMNNAME_ispaidoff);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Partial?.
		@param ispartial Is Partial?	  */
	public void setispartial (boolean ispartial)
	{
		set_Value (COLUMNNAME_ispartial, Boolean.valueOf(ispartial));
	}

	/** Get Is Partial?.
		@return Is Partial?	  */
	public boolean ispartial () 
	{
		Object oo = get_Value(COLUMNNAME_ispartial);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Line.
		@param LineNo 
		Line No
	  */
	public void setLineNo (int LineNo)
	{
		set_Value (COLUMNNAME_LineNo, Integer.valueOf(LineNo));
	}

	/** Get Line.
		@return Line No
	  */
	public int getLineNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set paiddate.
		@param paiddate paiddate	  */
	public void setpaiddate (Timestamp paiddate)
	{
		set_Value (COLUMNNAME_paiddate, paiddate);
	}

	/** Get paiddate.
		@return paiddate	  */
	public Timestamp getpaiddate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_paiddate);
	}

	/** Set Paid Off Date.
		@param paidoffdate Paid Off Date	  */
	public void setpaidoffdate (Timestamp paidoffdate)
	{
		set_Value (COLUMNNAME_paidoffdate, paidoffdate);
	}

	/** Get Paid Off Date.
		@return Paid Off Date	  */
	public Timestamp getpaidoffdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_paidoffdate);
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

	/** Set Skip In Payslip.
		@param skipinpayslip Skip In Payslip	  */
	public void setskipinpayslip (boolean skipinpayslip)
	{
		set_Value (COLUMNNAME_skipinpayslip, Boolean.valueOf(skipinpayslip));
	}

	/** Get Skip In Payslip.
		@return Skip In Payslip	  */
	public boolean isskipinpayslip () 
	{
		Object oo = get_Value(COLUMNNAME_skipinpayslip);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}