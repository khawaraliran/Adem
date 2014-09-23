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

/** Generated Model for HR_Employee_Insurance
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Employee_Insurance.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Employee_Insurance extends PO implements I_HR_Employee_Insurance, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Employee_Insurance.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111123L;

    /** Standard Constructor */
    public X_HR_Employee_Insurance (Properties ctx, int HR_Employee_Insurance_ID, String trxName)
    {
      super (ctx, HR_Employee_Insurance_ID, trxName);
      /** if (HR_Employee_Insurance_ID == 0)
        {
			setC_BPartner_ID (0);
			setcoverage_amount (Env.ZERO);
			setHR_Employee_Insurance_ID (0);
			setHR_Insurance_Type_ID (0);
			setinsurance_reference (null);
			setpaymentdate (new Timestamp( System.currentTimeMillis() ));
			setpaymentfrequency (null);
			setpremium_amount (Env.ZERO);
			setsponsor_name (null);
        } */
    }

    /** Load Constructor */
    public X_HR_Employee_Insurance (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Employee_Insurance[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Balance Amount.
		@param balance_amount Balance Amount	  */
	public void setbalance_amount (BigDecimal balance_amount)
	{
		set_Value (COLUMNNAME_balance_amount, balance_amount);
	}

	/** Get Balance Amount.
		@return Balance Amount	  */
	public BigDecimal getbalance_amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_balance_amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Employee.
		@param C_BPartner_ID 
		Identifies a Employee
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Employee.
		@return Identifies a Employee
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Claimed Amount.
		@param claimed_amount Claimed Amount	  */
	public void setclaimed_amount (BigDecimal claimed_amount)
	{
		set_Value (COLUMNNAME_claimed_amount, claimed_amount);
	}

	/** Get Claimed Amount.
		@return Claimed Amount	  */
	public BigDecimal getclaimed_amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_claimed_amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Coverage Amount.
		@param coverage_amount Coverage Amount	  */
	public void setcoverage_amount (BigDecimal coverage_amount)
	{
		set_Value (COLUMNNAME_coverage_amount, coverage_amount);
	}

	/** Get Coverage Amount.
		@return Coverage Amount	  */
	public BigDecimal getcoverage_amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_coverage_amount);
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

	/** Set HR_Employee_Insurance_ID.
		@param HR_Employee_Insurance_ID HR_Employee_Insurance_ID	  */
	public void setHR_Employee_Insurance_ID (int HR_Employee_Insurance_ID)
	{
		if (HR_Employee_Insurance_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_Insurance_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_Insurance_ID, Integer.valueOf(HR_Employee_Insurance_ID));
	}

	/** Get HR_Employee_Insurance_ID.
		@return HR_Employee_Insurance_ID	  */
	public int getHR_Employee_Insurance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_Insurance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Insurance_Type getHR_Insurance_Type() throws RuntimeException
    {
		return (I_HR_Insurance_Type)MTable.get(getCtx(), I_HR_Insurance_Type.Table_Name)
			.getPO(getHR_Insurance_Type_ID(), get_TrxName());	}

	/** Set HR_Insurance_Type_ID.
		@param HR_Insurance_Type_ID HR_Insurance_Type_ID	  */
	public void setHR_Insurance_Type_ID (int HR_Insurance_Type_ID)
	{
		if (HR_Insurance_Type_ID < 1) 
			set_Value (COLUMNNAME_HR_Insurance_Type_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Insurance_Type_ID, Integer.valueOf(HR_Insurance_Type_ID));
	}

	/** Get HR_Insurance_Type_ID.
		@return HR_Insurance_Type_ID	  */
	public int getHR_Insurance_Type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Insurance_Type_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Insurance Plan.
		@param insurance_plan Insurance Plan	  */
	public void setinsurance_plan (String insurance_plan)
	{
		set_Value (COLUMNNAME_insurance_plan, insurance_plan);
	}

	/** Get Insurance Plan.
		@return Insurance Plan	  */
	public String getinsurance_plan () 
	{
		return (String)get_Value(COLUMNNAME_insurance_plan);
	}

	/** Set Insurance Reference Number.
		@param insurance_reference Insurance Reference Number	  */
	public void setinsurance_reference (String insurance_reference)
	{
		set_Value (COLUMNNAME_insurance_reference, insurance_reference);
	}

	/** Get Insurance Reference Number.
		@return Insurance Reference Number	  */
	public String getinsurance_reference () 
	{
		return (String)get_Value(COLUMNNAME_insurance_reference);
	}

	/** Set Last Paid Date.
		@param lastpaiddate 
		Last Commission Paid Date
	  */
	public void setlastpaiddate (Timestamp lastpaiddate)
	{
		set_ValueNoCheck (COLUMNNAME_lastpaiddate, lastpaiddate);
	}

	/** Get Last Paid Date.
		@return Last Commission Paid Date
	  */
	public Timestamp getlastpaiddate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_lastpaiddate);
	}

	/** Set lastpremiumdate.
		@param lastpremiumdate lastpremiumdate	  */
	public void setlastpremiumdate (Timestamp lastpremiumdate)
	{
		set_Value (COLUMNNAME_lastpremiumdate, lastpremiumdate);
	}

	/** Get lastpremiumdate.
		@return lastpremiumdate	  */
	public Timestamp getlastpremiumdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_lastpremiumdate);
	}

	/** Set Payment Date.
		@param paymentdate 
		Date of the Payment
	  */
	public void setpaymentdate (Timestamp paymentdate)
	{
		set_ValueNoCheck (COLUMNNAME_paymentdate, paymentdate);
	}

	/** Get Payment Date.
		@return Date of the Payment
	  */
	public Timestamp getpaymentdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_paymentdate);
	}

	/** paymentfrequency AD_Reference_ID=1000024 */
	public static final int PAYMENTFREQUENCY_AD_Reference_ID=1000024;
	/** Monthly = M */
	public static final String PAYMENTFREQUENCY_Monthly = "M";
	/** Quarterly = Q */
	public static final String PAYMENTFREQUENCY_Quarterly = "Q";
	/** Semi-yearly = S */
	public static final String PAYMENTFREQUENCY_Semi_Yearly = "S";
	/** Yearly = Y */
	public static final String PAYMENTFREQUENCY_Yearly = "Y";
	/** Set Payment Frequency.
		@param paymentfrequency 
		Payment Frequency
	  */
	public void setpaymentfrequency (String paymentfrequency)
	{

		set_Value (COLUMNNAME_paymentfrequency, paymentfrequency);
	}

	/** Get Payment Frequency.
		@return Payment Frequency
	  */
	public String getpaymentfrequency () 
	{
		return (String)get_Value(COLUMNNAME_paymentfrequency);
	}

	/** Set Premium Amount.
		@param premium_amount Premium Amount	  */
	public void setpremium_amount (BigDecimal premium_amount)
	{
		set_Value (COLUMNNAME_premium_amount, premium_amount);
	}

	/** Get Premium Amount.
		@return Premium Amount	  */
	public BigDecimal getpremium_amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_premium_amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sponsor Name.
		@param sponsor_name Sponsor Name	  */
	public void setsponsor_name (String sponsor_name)
	{
		set_Value (COLUMNNAME_sponsor_name, sponsor_name);
	}

	/** Get Sponsor Name.
		@return Sponsor Name	  */
	public String getsponsor_name () 
	{
		return (String)get_Value(COLUMNNAME_sponsor_name);
	}
}