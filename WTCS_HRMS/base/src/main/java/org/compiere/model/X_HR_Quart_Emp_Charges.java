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

/** Generated Model for HR_Quart_Emp_Charges
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Quart_Emp_Charges.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Quart_Emp_Charges extends PO implements I_HR_Quart_Emp_Charges, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Quart_Emp_Charges.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111216L;

    /** Standard Constructor */
    public X_HR_Quart_Emp_Charges (Properties ctx, int HR_Quart_Emp_Charges_ID, String trxName)
    {
      super (ctx, HR_Quart_Emp_Charges_ID, trxName);
      /** if (HR_Quart_Emp_Charges_ID == 0)
        {
			setC_BPartner_ID (0);
			setchargedate (new Timestamp( System.currentTimeMillis() ));
			setHR_Period_ID (0);
			setHR_Quart_Emp_Charges_ID (0);
			setHR_Quarter_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HR_Quart_Emp_Charges (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Quart_Emp_Charges[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amount 
		Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
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

	/** Set chargedate.
		@param chargedate chargedate	  */
	public void setchargedate (Timestamp chargedate)
	{
		set_Value (COLUMNNAME_chargedate, chargedate);
	}

	/** Get chargedate.
		@return chargedate	  */
	public Timestamp getchargedate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_chargedate);
	}

	/** chargetype AD_Reference_ID=7000001 */
	public static final int CHARGETYPE_AD_Reference_ID=7000001;
	/** Dish = Dish */
	public static final String CHARGETYPE_Dish = "Dish";
	/** Power = Power */
	public static final String CHARGETYPE_Power = "Power";
	/** Set chargetype.
		@param chargetype chargetype	  */
	public void setchargetype (String chargetype)
	{

		set_Value (COLUMNNAME_chargetype, chargetype);
	}

	/** Get chargetype.
		@return chargetype	  */
	public String getchargetype () 
	{
		return (String)get_Value(COLUMNNAME_chargetype);
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

	/** Set Quarter employee charges.
		@param HR_Quart_Emp_Charges_ID Quarter employee charges	  */
	public void setHR_Quart_Emp_Charges_ID (int HR_Quart_Emp_Charges_ID)
	{
		if (HR_Quart_Emp_Charges_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Quart_Emp_Charges_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Quart_Emp_Charges_ID, Integer.valueOf(HR_Quart_Emp_Charges_ID));
	}

	/** Get Quarter employee charges.
		@return Quarter employee charges	  */
	public int getHR_Quart_Emp_Charges_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Quart_Emp_Charges_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Quarter getHR_Quarter() throws RuntimeException
    {
		return (I_HR_Quarter)MTable.get(getCtx(), I_HR_Quarter.Table_Name)
			.getPO(getHR_Quarter_ID(), get_TrxName());	}

	/** Set Quarter.
		@param HR_Quarter_ID 
		Quarter
	  */
	public void setHR_Quarter_ID (int HR_Quarter_ID)
	{
		if (HR_Quarter_ID < 1) 
			set_Value (COLUMNNAME_HR_Quarter_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Quarter_ID, Integer.valueOf(HR_Quarter_ID));
	}

	/** Get Quarter.
		@return Quarter
	  */
	public int getHR_Quarter_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Quarter_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Quarter_Power_Cons getHR_Quarter_Power_Cons() throws RuntimeException
    {
		return (I_HR_Quarter_Power_Cons)MTable.get(getCtx(), I_HR_Quarter_Power_Cons.Table_Name)
			.getPO(getHR_Quarter_Power_Cons_ID(), get_TrxName());	}

	/** Set Quarter power consumption.
		@param HR_Quarter_Power_Cons_ID Quarter power consumption	  */
	public void setHR_Quarter_Power_Cons_ID (int HR_Quarter_Power_Cons_ID)
	{
		if (HR_Quarter_Power_Cons_ID < 1) 
			set_Value (COLUMNNAME_HR_Quarter_Power_Cons_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Quarter_Power_Cons_ID, Integer.valueOf(HR_Quarter_Power_Cons_ID));
	}

	/** Get Quarter power consumption.
		@return Quarter power consumption	  */
	public int getHR_Quarter_Power_Cons_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Quarter_Power_Cons_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}