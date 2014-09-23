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

/** Generated Model for ES_Leave_Compensation
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_ES_Leave_Compensation.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_ES_Leave_Compensation extends PO implements I_ES_Leave_Compensation, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_ES_Leave_Compensation.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111202L;

    /** Standard Constructor */
    public X_ES_Leave_Compensation (Properties ctx, int ES_Leave_Compensation_ID, String trxName)
    {
      super (ctx, ES_Leave_Compensation_ID, trxName);
      /** if (ES_Leave_Compensation_ID == 0)
        {
			setC_BPartner_ID (0);
			setcompensationamt (Env.ZERO);
			setencashedleaves (Env.ZERO);
			setES_Leave_Compensation_ID (0);
			setHR_LeaveType_ID (0);
			setIsApproved (false);
			setProcessed (false);
			setreqdate (new Timestamp( System.currentTimeMillis() ));
			setshowpayslip (false);
        } */
    }

    /** Load Constructor */
    public X_ES_Leave_Compensation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_ES_Leave_Compensation[")
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

	/** Set compensationamt.
		@param compensationamt compensationamt	  */
	public void setcompensationamt (BigDecimal compensationamt)
	{
		set_Value (COLUMNNAME_compensationamt, compensationamt);
	}

	/** Get compensationamt.
		@return compensationamt	  */
	public BigDecimal getcompensationamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_compensationamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set encashedleaves.
		@param encashedleaves encashedleaves	  */
	public void setencashedleaves (BigDecimal encashedleaves)
	{
		set_Value (COLUMNNAME_encashedleaves, encashedleaves);
	}

	/** Get encashedleaves.
		@return encashedleaves	  */
	public BigDecimal getencashedleaves () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_encashedleaves);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ES_Leave_Compensation.
		@param ES_Leave_Compensation_ID ES_Leave_Compensation	  */
	public void setES_Leave_Compensation_ID (int ES_Leave_Compensation_ID)
	{
		if (ES_Leave_Compensation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_ES_Leave_Compensation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_ES_Leave_Compensation_ID, Integer.valueOf(ES_Leave_Compensation_ID));
	}

	/** Get ES_Leave_Compensation.
		@return ES_Leave_Compensation	  */
	public int getES_Leave_Compensation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ES_Leave_Compensation_ID);
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

	/** Set processeddate.
		@param processeddate processeddate	  */
	public void setprocesseddate (Timestamp processeddate)
	{
		set_Value (COLUMNNAME_processeddate, processeddate);
	}

	/** Get processeddate.
		@return processeddate	  */
	public Timestamp getprocesseddate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_processeddate);
	}

	/** Set reqdate.
		@param reqdate reqdate	  */
	public void setreqdate (Timestamp reqdate)
	{
		set_Value (COLUMNNAME_reqdate, reqdate);
	}

	/** Get reqdate.
		@return reqdate	  */
	public Timestamp getreqdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_reqdate);
	}

	/** Set showpayslip.
		@param showpayslip showpayslip	  */
	public void setshowpayslip (boolean showpayslip)
	{
		set_Value (COLUMNNAME_showpayslip, Boolean.valueOf(showpayslip));
	}

	/** Get showpayslip.
		@return showpayslip	  */
	public boolean isshowpayslip () 
	{
		Object oo = get_Value(COLUMNNAME_showpayslip);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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
}