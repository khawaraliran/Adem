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

/** Generated Model for WTC_Leave_CreditHistory
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_WTC_Leave_CreditHistory.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_WTC_Leave_CreditHistory extends PO implements I_WTC_Leave_CreditHistory, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_WTC_Leave_CreditHistory.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111122L;

    /** Standard Constructor */
    public X_WTC_Leave_CreditHistory (Properties ctx, int WTC_Leave_CreditHistory_ID, String trxName)
    {
      super (ctx, WTC_Leave_CreditHistory_ID, trxName);
      /** if (WTC_Leave_CreditHistory_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_Year_ID (0);
			setHR_Employee_ID (0);
			setHR_Leave_Assign_ID (0);
			setHR_LeaveType_ID (0);
			setnoofleavescredited (Env.ZERO);
			setWTC_Leave_CreditHistory_ID (0);
        } */
    }

    /** Load Constructor */
    public X_WTC_Leave_CreditHistory (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_WTC_Leave_CreditHistory[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public I_HR_Leave_Assign getHR_Leave_Assign() throws RuntimeException
    {
		return (I_HR_Leave_Assign)MTable.get(getCtx(), I_HR_Leave_Assign.Table_Name)
			.getPO(getHR_Leave_Assign_ID(), get_TrxName());	}

	/** Set HR_Leave_Assign.
		@param HR_Leave_Assign_ID HR_Leave_Assign	  */
	public void setHR_Leave_Assign_ID (int HR_Leave_Assign_ID)
	{
		if (HR_Leave_Assign_ID < 1) 
			set_Value (COLUMNNAME_HR_Leave_Assign_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Leave_Assign_ID, Integer.valueOf(HR_Leave_Assign_ID));
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

	/** Set No Of Leaves Credited.
		@param noofleavescredited No Of Leaves Credited	  */
	public void setnoofleavescredited (BigDecimal noofleavescredited)
	{
		set_Value (COLUMNNAME_noofleavescredited, noofleavescredited);
	}

	/** Get No Of Leaves Credited.
		@return No Of Leaves Credited	  */
	public BigDecimal getnoofleavescredited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_noofleavescredited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Reason.
		@param reasonforcredit Reason	  */
	public void setreasonforcredit (String reasonforcredit)
	{
		set_Value (COLUMNNAME_reasonforcredit, reasonforcredit);
	}

	/** Get Reason.
		@return Reason	  */
	public String getreasonforcredit () 
	{
		return (String)get_Value(COLUMNNAME_reasonforcredit);
	}

	/** Set WTC_Leave_CreditHistory.
		@param WTC_Leave_CreditHistory_ID WTC_Leave_CreditHistory	  */
	public void setWTC_Leave_CreditHistory_ID (int WTC_Leave_CreditHistory_ID)
	{
		if (WTC_Leave_CreditHistory_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WTC_Leave_CreditHistory_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WTC_Leave_CreditHistory_ID, Integer.valueOf(WTC_Leave_CreditHistory_ID));
	}

	/** Get WTC_Leave_CreditHistory.
		@return WTC_Leave_CreditHistory	  */
	public int getWTC_Leave_CreditHistory_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_Leave_CreditHistory_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}