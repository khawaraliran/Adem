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

/** Generated Model for HR_Emp_Sup_Attendence
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Emp_Sup_Attendence.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Emp_Sup_Attendence extends PO implements I_HR_Emp_Sup_Attendence, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Emp_Sup_Attendence.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111210L;

    /** Standard Constructor */
    public X_HR_Emp_Sup_Attendence (Properties ctx, int HR_Emp_Sup_Attendence_ID, String trxName)
    {
      super (ctx, HR_Emp_Sup_Attendence_ID, trxName);
      /** if (HR_Emp_Sup_Attendence_ID == 0)
        {
			setC_BPartner_ID (0);
			setHR_Emp_Sup_Attendence_ID (0);
			setHR_Work_Group_ID (0);
			setincomingtime (new Timestamp( System.currentTimeMillis() ));
			setissatisfactory (false);
			setProcessed (false);
			setSupervisor_ID (0);
			setworkdate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_HR_Emp_Sup_Attendence (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Emp_Sup_Attendence[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_HR_Work_Shift getActual_Work_Shift() throws RuntimeException
    {
		return (I_HR_Work_Shift)MTable.get(getCtx(), I_HR_Work_Shift.Table_Name)
			.getPO(getActual_Work_Shift_ID(), get_TrxName());	}

	/** Set Actual_Work_Shift_ID.
		@param Actual_Work_Shift_ID Actual_Work_Shift_ID	  */
	public void setActual_Work_Shift_ID (int Actual_Work_Shift_ID)
	{
		if (Actual_Work_Shift_ID < 1) 
			set_Value (COLUMNNAME_Actual_Work_Shift_ID, null);
		else 
			set_Value (COLUMNNAME_Actual_Work_Shift_ID, Integer.valueOf(Actual_Work_Shift_ID));
	}

	/** Get Actual_Work_Shift_ID.
		@return Actual_Work_Shift_ID	  */
	public int getActual_Work_Shift_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Actual_Work_Shift_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set adjusthours.
		@param adjusthours adjusthours	  */
	public void setadjusthours (BigDecimal adjusthours)
	{
		set_Value (COLUMNNAME_adjusthours, adjusthours);
	}

	/** Get adjusthours.
		@return adjusthours	  */
	public BigDecimal getadjusthours () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_adjusthours);
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

	/** Set HR_Emp_Sup_Attendence.
		@param HR_Emp_Sup_Attendence_ID HR_Emp_Sup_Attendence	  */
	public void setHR_Emp_Sup_Attendence_ID (int HR_Emp_Sup_Attendence_ID)
	{
		if (HR_Emp_Sup_Attendence_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Emp_Sup_Attendence_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Emp_Sup_Attendence_ID, Integer.valueOf(HR_Emp_Sup_Attendence_ID));
	}

	/** Get HR_Emp_Sup_Attendence.
		@return HR_Emp_Sup_Attendence	  */
	public int getHR_Emp_Sup_Attendence_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Emp_Sup_Attendence_ID);
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

	public I_HR_Work_Group getHR_Work_Group() throws RuntimeException
    {
		return (I_HR_Work_Group)MTable.get(getCtx(), I_HR_Work_Group.Table_Name)
			.getPO(getHR_Work_Group_ID(), get_TrxName());	}

	/** Set Work Group Name.
		@param HR_Work_Group_ID 
		Name of the Work Group
	  */
	public void setHR_Work_Group_ID (int HR_Work_Group_ID)
	{
		if (HR_Work_Group_ID < 1) 
			set_Value (COLUMNNAME_HR_Work_Group_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Work_Group_ID, Integer.valueOf(HR_Work_Group_ID));
	}

	/** Get Work Group Name.
		@return Name of the Work Group
	  */
	public int getHR_Work_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Work_Group_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set incomingtime.
		@param incomingtime incomingtime	  */
	public void setincomingtime (Timestamp incomingtime)
	{
		set_Value (COLUMNNAME_incomingtime, incomingtime);
	}

	/** Get incomingtime.
		@return incomingtime	  */
	public Timestamp getincomingtime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_incomingtime);
	}

	/** Set isadjusthours.
		@param isadjusthours isadjusthours	  */
	public void setisadjusthours (boolean isadjusthours)
	{
		set_Value (COLUMNNAME_isadjusthours, Boolean.valueOf(isadjusthours));
	}

	/** Get isadjusthours.
		@return isadjusthours	  */
	public boolean isadjusthours () 
	{
		Object oo = get_Value(COLUMNNAME_isadjusthours);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set issatisfactory.
		@param issatisfactory issatisfactory	  */
	public void setissatisfactory (boolean issatisfactory)
	{
		set_Value (COLUMNNAME_issatisfactory, Boolean.valueOf(issatisfactory));
	}

	/** Get issatisfactory.
		@return issatisfactory	  */
	public boolean issatisfactory () 
	{
		Object oo = get_Value(COLUMNNAME_issatisfactory);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set numberofhours.
		@param numberofhours numberofhours	  */
	public void setnumberofhours (BigDecimal numberofhours)
	{
		set_Value (COLUMNNAME_numberofhours, numberofhours);
	}

	/** Get numberofhours.
		@return numberofhours	  */
	public BigDecimal getnumberofhours () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_numberofhours);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set outgoingtime.
		@param outgoingtime outgoingtime	  */
	public void setoutgoingtime (Timestamp outgoingtime)
	{
		set_Value (COLUMNNAME_outgoingtime, outgoingtime);
	}

	/** Get outgoingtime.
		@return outgoingtime	  */
	public Timestamp getoutgoingtime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_outgoingtime);
	}

	public I_HR_Work_Shift getPlan_Work_Shift() throws RuntimeException
    {
		return (I_HR_Work_Shift)MTable.get(getCtx(), I_HR_Work_Shift.Table_Name)
			.getPO(getPlan_Work_Shift_ID(), get_TrxName());	}

	/** Set Plan_Work_Shift_ID.
		@param Plan_Work_Shift_ID Plan_Work_Shift_ID	  */
	public void setPlan_Work_Shift_ID (int Plan_Work_Shift_ID)
	{
		if (Plan_Work_Shift_ID < 1) 
			set_Value (COLUMNNAME_Plan_Work_Shift_ID, null);
		else 
			set_Value (COLUMNNAME_Plan_Work_Shift_ID, Integer.valueOf(Plan_Work_Shift_ID));
	}

	/** Get Plan_Work_Shift_ID.
		@return Plan_Work_Shift_ID	  */
	public int getPlan_Work_Shift_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Plan_Work_Shift_ID);
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

	/** Set remarks.
		@param remarks remarks	  */
	public void setremarks (String remarks)
	{
		set_Value (COLUMNNAME_remarks, remarks);
	}

	/** Get remarks.
		@return remarks	  */
	public String getremarks () 
	{
		return (String)get_Value(COLUMNNAME_remarks);
	}

	public I_C_BPartner getSupervisor() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getSupervisor_ID(), get_TrxName());	}

	/** Set Supervisor.
		@param Supervisor_ID 
		Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID)
	{
		if (Supervisor_ID < 1) 
			set_Value (COLUMNNAME_Supervisor_ID, null);
		else 
			set_Value (COLUMNNAME_Supervisor_ID, Integer.valueOf(Supervisor_ID));
	}

	/** Get Supervisor.
		@return Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Supervisor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set workdate.
		@param workdate workdate	  */
	public void setworkdate (Timestamp workdate)
	{
		set_Value (COLUMNNAME_workdate, workdate);
	}

	/** Get workdate.
		@return workdate	  */
	public Timestamp getworkdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_workdate);
	}
}