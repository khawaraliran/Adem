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

/** Generated Model for HR_Leave_Request
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Leave_Request.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Leave_Request extends PO implements I_HR_Leave_Request, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Leave_Request.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111210L;

    /** Standard Constructor */
    public X_HR_Leave_Request (Properties ctx, int HR_Leave_Request_ID, String trxName)
    {
      super (ctx, HR_Leave_Request_ID, trxName);
      /** if (HR_Leave_Request_ID == 0)
        {
			setC_BPartner_ID (0);
			setFromDate (new Timestamp( System.currentTimeMillis() ));
			setHR_Leave_Request_ID (0);
			setHR_LeaveType_ID (0);
			setisavailed (false);
			setnumber_of_workingdays (Env.ZERO);
			setProcessed (false);
			setToDate (new Timestamp( System.currentTimeMillis() ));
			setworkinghours (Env.ZERO);
			setWTC_Reasons_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HR_Leave_Request (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Leave_Request[")
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

	/** Set Comments.
		@param Comments 
		Comments or additional information
	  */
	public void setComments (String Comments)
	{
		set_Value (COLUMNNAME_Comments, Comments);
	}

	/** Get Comments.
		@return Comments or additional information
	  */
	public String getComments () 
	{
		return (String)get_Value(COLUMNNAME_Comments);
	}

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set From Date.
		@param FromDate From Date	  */
	public void setFromDate (Timestamp FromDate)
	{
		set_Value (COLUMNNAME_FromDate, FromDate);
	}

	/** Get From Date.
		@return From Date	  */
	public Timestamp getFromDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FromDate);
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

	/** Set HR_Leave_Request.
		@param HR_Leave_Request_ID HR_Leave_Request	  */
	public void setHR_Leave_Request_ID (int HR_Leave_Request_ID)
	{
		if (HR_Leave_Request_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Leave_Request_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Leave_Request_ID, Integer.valueOf(HR_Leave_Request_ID));
	}

	/** Get HR_Leave_Request.
		@return HR_Leave_Request	  */
	public int getHR_Leave_Request_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Leave_Request_ID);
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

	/** Set Is Management Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Is Management Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isavailed.
		@param isavailed isavailed	  */
	public void setisavailed (boolean isavailed)
	{
		set_Value (COLUMNNAME_isavailed, Boolean.valueOf(isavailed));
	}

	/** Get isavailed.
		@return isavailed	  */
	public boolean isavailed () 
	{
		Object oo = get_Value(COLUMNNAME_isavailed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set leave_status.
		@param leave_status leave_status	  */
	public void setleave_status (String leave_status)
	{
		set_Value (COLUMNNAME_leave_status, leave_status);
	}

	/** Get leave_status.
		@return leave_status	  */
	public String getleave_status () 
	{
		return (String)get_Value(COLUMNNAME_leave_status);
	}

	/** Set Leave Summary.
		@param leavesummary 
		Leave Summary
	  */
	public void setleavesummary (String leavesummary)
	{
		set_Value (COLUMNNAME_leavesummary, leavesummary);
	}

	/** Get Leave Summary.
		@return Leave Summary
	  */
	public String getleavesummary () 
	{
		return (String)get_Value(COLUMNNAME_leavesummary);
	}

	/** Set number_of_workingdays.
		@param number_of_workingdays number_of_workingdays	  */
	public void setnumber_of_workingdays (BigDecimal number_of_workingdays)
	{
		set_Value (COLUMNNAME_number_of_workingdays, number_of_workingdays);
	}

	/** Get number_of_workingdays.
		@return number_of_workingdays	  */
	public BigDecimal getnumber_of_workingdays () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_number_of_workingdays);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set otherdisapprovalreason.
		@param otherdisapprovalreason otherdisapprovalreason	  */
	public void setotherdisapprovalreason (String otherdisapprovalreason)
	{
		set_Value (COLUMNNAME_otherdisapprovalreason, otherdisapprovalreason);
	}

	/** Get otherdisapprovalreason.
		@return otherdisapprovalreason	  */
	public String getotherdisapprovalreason () 
	{
		return (String)get_Value(COLUMNNAME_otherdisapprovalreason);
	}

	/** Set otherreason.
		@param otherreason otherreason	  */
	public void setotherreason (String otherreason)
	{
		set_Value (COLUMNNAME_otherreason, otherreason);
	}

	/** Get otherreason.
		@return otherreason	  */
	public String getotherreason () 
	{
		return (String)get_Value(COLUMNNAME_otherreason);
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

	public I_WTC_Reasons getrequestdisapprovalreason() throws RuntimeException
    {
		return (I_WTC_Reasons)MTable.get(getCtx(), I_WTC_Reasons.Table_Name)
			.getPO(getrequestdisapprovalreason_ID(), get_TrxName());	}

	/** Set Disapproval Reason ID.
		@param requestdisapprovalreason_ID Disapproval Reason ID	  */
	public void setrequestdisapprovalreason_ID (int requestdisapprovalreason_ID)
	{
		if (requestdisapprovalreason_ID < 1) 
			set_Value (COLUMNNAME_requestdisapprovalreason_ID, null);
		else 
			set_Value (COLUMNNAME_requestdisapprovalreason_ID, Integer.valueOf(requestdisapprovalreason_ID));
	}

	/** Get Disapproval Reason ID.
		@return Disapproval Reason ID	  */
	public int getrequestdisapprovalreason_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_requestdisapprovalreason_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set To Date.
		@param ToDate To Date	  */
	public void setToDate (Timestamp ToDate)
	{
		set_Value (COLUMNNAME_ToDate, ToDate);
	}

	/** Get To Date.
		@return To Date	  */
	public Timestamp getToDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ToDate);
	}

	/** Set Workflow State.
		@param WFState 
		State of the execution of the workflow
	  */
	public void setWFState (String WFState)
	{
		set_Value (COLUMNNAME_WFState, WFState);
	}

	/** Get Workflow State.
		@return State of the execution of the workflow
	  */
	public String getWFState () 
	{
		return (String)get_Value(COLUMNNAME_WFState);
	}

	/** Set workinghours.
		@param workinghours workinghours	  */
	public void setworkinghours (BigDecimal workinghours)
	{
		set_Value (COLUMNNAME_workinghours, workinghours);
	}

	/** Get workinghours.
		@return workinghours	  */
	public BigDecimal getworkinghours () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_workinghours);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_WTC_Reasons getWTC_Reasons() throws RuntimeException
    {
		return (I_WTC_Reasons)MTable.get(getCtx(), I_WTC_Reasons.Table_Name)
			.getPO(getWTC_Reasons_ID(), get_TrxName());	}

	/** Set Reason.
		@param WTC_Reasons_ID 
		Predefiend reasons
	  */
	public void setWTC_Reasons_ID (int WTC_Reasons_ID)
	{
		if (WTC_Reasons_ID < 1) 
			set_Value (COLUMNNAME_WTC_Reasons_ID, null);
		else 
			set_Value (COLUMNNAME_WTC_Reasons_ID, Integer.valueOf(WTC_Reasons_ID));
	}

	/** Get Reason.
		@return Predefiend reasons
	  */
	public int getWTC_Reasons_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_Reasons_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}