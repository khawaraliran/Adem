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

/** Generated Model for HR_Insurance_History
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Insurance_History.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Insurance_History extends PO implements I_HR_Insurance_History, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Insurance_History.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111221L;

    /** Standard Constructor */
    public X_HR_Insurance_History (Properties ctx, int HR_Insurance_History_ID, String trxName)
    {
      super (ctx, HR_Insurance_History_ID, trxName);
      /** if (HR_Insurance_History_ID == 0)
        {
			setHR_Insurance_History_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HR_Insurance_History (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Insurance_History[")
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

	public I_HR_Employee_Insurance getHR_Employee_Insurance() throws RuntimeException
    {
		return (I_HR_Employee_Insurance)MTable.get(getCtx(), I_HR_Employee_Insurance.Table_Name)
			.getPO(getHR_Employee_Insurance_ID(), get_TrxName());	}

	/** Set HR_Employee_Insurance_ID.
		@param HR_Employee_Insurance_ID HR_Employee_Insurance_ID	  */
	public void setHR_Employee_Insurance_ID (int HR_Employee_Insurance_ID)
	{
		if (HR_Employee_Insurance_ID < 1) 
			set_Value (COLUMNNAME_HR_Employee_Insurance_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Employee_Insurance_ID, Integer.valueOf(HR_Employee_Insurance_ID));
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

	/** Set Insurance history.
		@param HR_Insurance_History_ID Insurance history	  */
	public void setHR_Insurance_History_ID (int HR_Insurance_History_ID)
	{
		if (HR_Insurance_History_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Insurance_History_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Insurance_History_ID, Integer.valueOf(HR_Insurance_History_ID));
	}

	/** Get Insurance history.
		@return Insurance history	  */
	public int getHR_Insurance_History_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Insurance_History_ID);
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set rundate.
		@param rundate rundate	  */
	public void setrundate (Timestamp rundate)
	{
		set_Value (COLUMNNAME_rundate, rundate);
	}

	/** Get rundate.
		@return rundate	  */
	public Timestamp getrundate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_rundate);
	}
}