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

/** Generated Model for HR_Leave_Assign
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Leave_Assign.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Leave_Assign extends PO implements I_HR_Leave_Assign, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Leave_Assign.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111130L;

    /** Standard Constructor */
    public X_HR_Leave_Assign (Properties ctx, int HR_Leave_Assign_ID, String trxName)
    {
      super (ctx, HR_Leave_Assign_ID, trxName);
      /** if (HR_Leave_Assign_ID == 0)
        {
			setadd_leaves (Env.ZERO);
			setC_Year_ID (0);
			setHR_LeaveType_ID (0);
			settotal_leaves (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_HR_Leave_Assign (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Leave_Assign[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Add Leaves.
		@param add_leaves Add Leaves	  */
	public void setadd_leaves (BigDecimal add_leaves)
	{
		set_Value (COLUMNNAME_add_leaves, add_leaves);
	}

	/** Get Add Leaves.
		@return Add Leaves	  */
	public BigDecimal getadd_leaves () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_add_leaves);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Balance Leaves.
		@param balance_leaves Balance Leaves	  */
	public void setbalance_leaves (BigDecimal balance_leaves)
	{
		set_Value (COLUMNNAME_balance_leaves, balance_leaves);
	}

	/** Get Balance Leaves.
		@return Balance Leaves	  */
	public BigDecimal getbalance_leaves () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_balance_leaves);
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

	/** Set creditleavesbutton.
		@param creditleavesbutton creditleavesbutton	  */
	public void setcreditleavesbutton (String creditleavesbutton)
	{
		set_Value (COLUMNNAME_creditleavesbutton, creditleavesbutton);
	}

	/** Get creditleavesbutton.
		@return creditleavesbutton	  */
	public String getcreditleavesbutton () 
	{
		return (String)get_Value(COLUMNNAME_creditleavesbutton);
	}

	public I_C_Year getC_Year() throws RuntimeException
    {
		return (I_C_Year)MTable.get(getCtx(), I_C_Year.Table_Name)
			.getPO(getC_Year_ID(), get_TrxName());	}

	/** Set Year.
		@param C_Year_ID 
		Calendar Year
	  */
	public void setC_Year_ID (int C_Year_ID)
	{
		if (C_Year_ID < 1) 
			set_Value (COLUMNNAME_C_Year_ID, null);
		else 
			set_Value (COLUMNNAME_C_Year_ID, Integer.valueOf(C_Year_ID));
	}

	/** Get Year.
		@return Calendar Year
	  */
	public int getC_Year_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Year_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Employee Code.
		@param employee_code 
		Employee Code - Unique
	  */
	public void setemployee_code (String employee_code)
	{
		set_Value (COLUMNNAME_employee_code, employee_code);
	}

	/** Get Employee Code.
		@return Employee Code - Unique
	  */
	public String getemployee_code () 
	{
		return (String)get_Value(COLUMNNAME_employee_code);
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

	/** Set HR_Leave_Assign.
		@param HR_Leave_Assign_ID HR_Leave_Assign	  */
	public void setHR_Leave_Assign_ID (int HR_Leave_Assign_ID)
	{
		if (HR_Leave_Assign_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Leave_Assign_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Leave_Assign_ID, Integer.valueOf(HR_Leave_Assign_ID));
	}

	/** Get HR_Leave_Assign.
		@return HR_Leave_Assign	  */
	public int getHR_Leave_Assign_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Leave_Assign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_LeaveType getHR_LeaveType() throws RuntimeException
    {
		return (I_HR_LeaveType)MTable.get(getCtx(), I_HR_LeaveType.Table_Name)
			.getPO(getHR_LeaveType_ID(), get_TrxName());	}

	/** Set Leave Type.
		@param HR_LeaveType_ID 
		Leave Type
	  */
	public void setHR_LeaveType_ID (int HR_LeaveType_ID)
	{
		if (HR_LeaveType_ID < 1) 
			set_Value (COLUMNNAME_HR_LeaveType_ID, null);
		else 
			set_Value (COLUMNNAME_HR_LeaveType_ID, Integer.valueOf(HR_LeaveType_ID));
	}

	/** Get Leave Type.
		@return Leave Type
	  */
	public int getHR_LeaveType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_LeaveType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set leave_hist.
		@param leave_hist leave_hist	  */
	public void setleave_hist (boolean leave_hist)
	{
		set_Value (COLUMNNAME_leave_hist, Boolean.valueOf(leave_hist));
	}

	/** Get leave_hist.
		@return leave_hist	  */
	public boolean isleave_hist () 
	{
		Object oo = get_Value(COLUMNNAME_leave_hist);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set prvyearcfleaves.
		@param prvyearcfleaves prvyearcfleaves	  */
	public void setprvyearcfleaves (BigDecimal prvyearcfleaves)
	{
		set_Value (COLUMNNAME_prvyearcfleaves, prvyearcfleaves);
	}

	/** Get prvyearcfleaves.
		@return prvyearcfleaves	  */
	public BigDecimal getprvyearcfleaves () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_prvyearcfleaves);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Number of Leaves Allocated.
		@param total_leaves Number of Leaves Allocated	  */
	public void settotal_leaves (BigDecimal total_leaves)
	{
		set_Value (COLUMNNAME_total_leaves, total_leaves);
	}

	/** Get Number of Leaves Allocated.
		@return Number of Leaves Allocated	  */
	public BigDecimal gettotal_leaves () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_total_leaves);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Used Leaves.
		@param used_leaves Used Leaves	  */
	public void setused_leaves (BigDecimal used_leaves)
	{
		set_Value (COLUMNNAME_used_leaves, used_leaves);
	}

	/** Get Used Leaves.
		@return Used Leaves	  */
	public BigDecimal getused_leaves () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_used_leaves);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}