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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for HR_Sal_Structure
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Sal_Structure.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Sal_Structure extends PO implements I_HR_Sal_Structure, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Sal_Structure.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111215L;

    /** Standard Constructor */
    public X_HR_Sal_Structure (Properties ctx, int HR_Sal_Structure_ID, String trxName)
    {
      super (ctx, HR_Sal_Structure_ID, trxName);
      /** if (HR_Sal_Structure_ID == 0)
        {
			setAmount (Env.ZERO);
			setHR_Sal_Earn_Comp_ID (0);
			setHR_Sal_Struct_Header_ID (0);
			setHR_Sal_Structure_ID (0);
			setPercentage (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_HR_Sal_Structure (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Sal_Structure[")
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

	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Employee)MTable.get(getCtx(), org.eevolution.model.I_HR_Employee.Table_Name)
			.getPO(getHR_Employee_ID(), get_TrxName());	}

	/** Set Employee Name.
		@param HR_Employee_ID Employee Name	  */
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1) 
			set_Value (COLUMNNAME_HR_Employee_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
	}

	/** Get Employee Name.
		@return Employee Name	  */
	public int getHR_Employee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Sal_Earn_Comp getHR_Sal_Earn_Comp() throws RuntimeException
    {
		return (I_HR_Sal_Earn_Comp)MTable.get(getCtx(), I_HR_Sal_Earn_Comp.Table_Name)
			.getPO(getHR_Sal_Earn_Comp_ID(), get_TrxName());	}

	/** Set Salary earning component.
		@param HR_Sal_Earn_Comp_ID Salary earning component	  */
	public void setHR_Sal_Earn_Comp_ID (int HR_Sal_Earn_Comp_ID)
	{
		if (HR_Sal_Earn_Comp_ID < 1) 
			set_Value (COLUMNNAME_HR_Sal_Earn_Comp_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Sal_Earn_Comp_ID, Integer.valueOf(HR_Sal_Earn_Comp_ID));
	}

	/** Get Salary earning component.
		@return Salary earning component	  */
	public int getHR_Sal_Earn_Comp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Sal_Earn_Comp_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Sal_Struct_Header getHR_Sal_Struct_Header() throws RuntimeException
    {
		return (I_HR_Sal_Struct_Header)MTable.get(getCtx(), I_HR_Sal_Struct_Header.Table_Name)
			.getPO(getHR_Sal_Struct_Header_ID(), get_TrxName());	}

	/** Set Salary Structure.
		@param HR_Sal_Struct_Header_ID Salary Structure	  */
	public void setHR_Sal_Struct_Header_ID (int HR_Sal_Struct_Header_ID)
	{
		if (HR_Sal_Struct_Header_ID < 1) 
			set_Value (COLUMNNAME_HR_Sal_Struct_Header_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Sal_Struct_Header_ID, Integer.valueOf(HR_Sal_Struct_Header_ID));
	}

	/** Get Salary Structure.
		@return Salary Structure	  */
	public int getHR_Sal_Struct_Header_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Sal_Struct_Header_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Salary structure.
		@param HR_Sal_Structure_ID Salary structure	  */
	public void setHR_Sal_Structure_ID (int HR_Sal_Structure_ID)
	{
		if (HR_Sal_Structure_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Sal_Structure_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Sal_Structure_ID, Integer.valueOf(HR_Sal_Structure_ID));
	}

	/** Get Salary structure.
		@return Salary structure	  */
	public int getHR_Sal_Structure_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Sal_Structure_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Percentage.
		@param Percentage 
		Percent of the entire amount
	  */
	public void setPercentage (BigDecimal Percentage)
	{
		set_Value (COLUMNNAME_Percentage, Percentage);
	}

	/** Get Percentage.
		@return Percent of the entire amount
	  */
	public BigDecimal getPercentage () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}