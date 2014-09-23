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

/** Generated Model for HR_Sal_Adv_Req
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Sal_Adv_Req.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Sal_Adv_Req extends PO implements I_HR_Sal_Adv_Req, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Sal_Adv_Req.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20120106L;

    /** Standard Constructor */
    public X_HR_Sal_Adv_Req (Properties ctx, int HR_Sal_Adv_Req_ID, String trxName)
    {
      super (ctx, HR_Sal_Adv_Req_ID, trxName);
      /** if (HR_Sal_Adv_Req_ID == 0)
        {
			setC_BPartner_ID (0);
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setHR_Sal_Adv_Req_ID (0);
			setisacctapproved (false);
			setIsApproved (false);
			setisdisbursed (false);
			setishrapproved (false);
			setispaidoff (false);
			setispresidentapproved (false);
			setneedpresidentapproval (false);
			setref_partner_id (0);
			setrequestdate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_HR_Sal_Adv_Req (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Sal_Adv_Req[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_HR_Dependents getadvance_collected_by() throws RuntimeException
    {
		return (I_HR_Dependents)MTable.get(getCtx(), I_HR_Dependents.Table_Name)
			.getPO(getadvance_collected_by_id(), get_TrxName());	}

	/** Set Advance_Collected_BY_ID.
		@param advance_collected_by_id Advance_Collected_BY_ID	  */
	public void setadvance_collected_by_id (int advance_collected_by_id)
	{
		set_Value (COLUMNNAME_advance_collected_by_id, Integer.valueOf(advance_collected_by_id));
	}

	/** Get Advance_Collected_BY_ID.
		@return Advance_Collected_BY_ID	  */
	public int getadvance_collected_by_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_advance_collected_by_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set advancegivenamt.
		@param advancegivenamt advancegivenamt	  */
	public void setadvancegivenamt (BigDecimal advancegivenamt)
	{
		set_Value (COLUMNNAME_advancegivenamt, advancegivenamt);
	}

	/** Get advancegivenamt.
		@return advancegivenamt	  */
	public BigDecimal getadvancegivenamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_advancegivenamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set advancegivendate.
		@param advancegivendate advancegivendate	  */
	public void setadvancegivendate (Timestamp advancegivendate)
	{
		set_Value (COLUMNNAME_advancegivendate, advancegivendate);
	}

	/** Get advancegivendate.
		@return advancegivendate	  */
	public Timestamp getadvancegivendate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_advancegivendate);
	}

	/** Set advancereturnamt.
		@param advancereturnamt advancereturnamt	  */
	public void setadvancereturnamt (BigDecimal advancereturnamt)
	{
		set_Value (COLUMNNAME_advancereturnamt, advancereturnamt);
	}

	/** Get advancereturnamt.
		@return advancereturnamt	  */
	public BigDecimal getadvancereturnamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_advancereturnamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set approvedamt.
		@param approvedamt approvedamt	  */
	public void setapprovedamt (BigDecimal approvedamt)
	{
		set_Value (COLUMNNAME_approvedamt, approvedamt);
	}

	/** Get approvedamt.
		@return approvedamt	  */
	public BigDecimal getapprovedamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_approvedamt);
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

	/** Set currentmonthsalary.
		@param currentmonthsalary currentmonthsalary	  */
	public void setcurrentmonthsalary (BigDecimal currentmonthsalary)
	{
		set_Value (COLUMNNAME_currentmonthsalary, currentmonthsalary);
	}

	/** Get currentmonthsalary.
		@return currentmonthsalary	  */
	public BigDecimal getcurrentmonthsalary () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currentmonthsalary);
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

	public org.eevolution.model.I_HR_Department getHR_Department() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Department)MTable.get(getCtx(), org.eevolution.model.I_HR_Department.Table_Name)
			.getPO(getHR_Department_ID(), get_TrxName());	}

	/** Set Department.
		@param HR_Department_ID 
		Department Name
	  */
	public void setHR_Department_ID (int HR_Department_ID)
	{
		if (HR_Department_ID < 1) 
			set_Value (COLUMNNAME_HR_Department_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Department_ID, Integer.valueOf(HR_Department_ID));
	}

	/** Get Department.
		@return Department Name
	  */
	public int getHR_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Designation getHR_Designation() throws RuntimeException
    {
		return (I_HR_Designation)MTable.get(getCtx(), I_HR_Designation.Table_Name)
			.getPO(getHR_Designation_ID(), get_TrxName());	}

	/** Set Designation.
		@param HR_Designation_ID 
		Designation
	  */
	public void setHR_Designation_ID (int HR_Designation_ID)
	{
		if (HR_Designation_ID < 1) 
			set_Value (COLUMNNAME_HR_Designation_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Designation_ID, Integer.valueOf(HR_Designation_ID));
	}

	/** Get Designation.
		@return Designation
	  */
	public int getHR_Designation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Designation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Advance Salary Request / Loan.
		@param HR_Sal_Adv_Req_ID Advance Salary Request / Loan	  */
	public void setHR_Sal_Adv_Req_ID (int HR_Sal_Adv_Req_ID)
	{
		if (HR_Sal_Adv_Req_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Sal_Adv_Req_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Sal_Adv_Req_ID, Integer.valueOf(HR_Sal_Adv_Req_ID));
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

	/** Set isacctapproved.
		@param isacctapproved isacctapproved	  */
	public void setisacctapproved (boolean isacctapproved)
	{
		set_Value (COLUMNNAME_isacctapproved, Boolean.valueOf(isacctapproved));
	}

	/** Get isacctapproved.
		@return isacctapproved	  */
	public boolean isacctapproved () 
	{
		Object oo = get_Value(COLUMNNAME_isacctapproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set isdisbursed.
		@param isdisbursed isdisbursed	  */
	public void setisdisbursed (boolean isdisbursed)
	{
		set_Value (COLUMNNAME_isdisbursed, Boolean.valueOf(isdisbursed));
	}

	/** Get isdisbursed.
		@return isdisbursed	  */
	public boolean isdisbursed () 
	{
		Object oo = get_Value(COLUMNNAME_isdisbursed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ishrapproved.
		@param ishrapproved ishrapproved	  */
	public void setishrapproved (boolean ishrapproved)
	{
		set_Value (COLUMNNAME_ishrapproved, Boolean.valueOf(ishrapproved));
	}

	/** Get ishrapproved.
		@return ishrapproved	  */
	public boolean ishrapproved () 
	{
		Object oo = get_Value(COLUMNNAME_ishrapproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set ispresidentapproved.
		@param ispresidentapproved ispresidentapproved	  */
	public void setispresidentapproved (boolean ispresidentapproved)
	{
		set_Value (COLUMNNAME_ispresidentapproved, Boolean.valueOf(ispresidentapproved));
	}

	/** Get ispresidentapproved.
		@return ispresidentapproved	  */
	public boolean ispresidentapproved () 
	{
		Object oo = get_Value(COLUMNNAME_ispresidentapproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set makepayment.
		@param makepayment makepayment	  */
	public void setmakepayment (String makepayment)
	{
		set_Value (COLUMNNAME_makepayment, makepayment);
	}

	/** Get makepayment.
		@return makepayment	  */
	public String getmakepayment () 
	{
		return (String)get_Value(COLUMNNAME_makepayment);
	}

	/** Set needpresidentapproval.
		@param needpresidentapproval needpresidentapproval	  */
	public void setneedpresidentapproval (boolean needpresidentapproval)
	{
		set_Value (COLUMNNAME_needpresidentapproval, Boolean.valueOf(needpresidentapproval));
	}

	/** Get needpresidentapproval.
		@return needpresidentapproval	  */
	public boolean isneedpresidentapproval () 
	{
		Object oo = get_Value(COLUMNNAME_needpresidentapproval);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set noofinstalments.
		@param noofinstalments noofinstalments	  */
	public void setnoofinstalments (int noofinstalments)
	{
		set_Value (COLUMNNAME_noofinstalments, Integer.valueOf(noofinstalments));
	}

	/** Get noofinstalments.
		@return noofinstalments	  */
	public int getnoofinstalments () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_noofinstalments);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_C_BPartner getpaid_partner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getpaid_partner_id(), get_TrxName());	}

	/** Set paid_partner_id.
		@param paid_partner_id paid_partner_id	  */
	public void setpaid_partner_id (int paid_partner_id)
	{
		set_Value (COLUMNNAME_paid_partner_id, Integer.valueOf(paid_partner_id));
	}

	/** Get paid_partner_id.
		@return paid_partner_id	  */
	public int getpaid_partner_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_paid_partner_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set previousmonthsalary.
		@param previousmonthsalary previousmonthsalary	  */
	public void setpreviousmonthsalary (BigDecimal previousmonthsalary)
	{
		set_Value (COLUMNNAME_previousmonthsalary, previousmonthsalary);
	}

	/** Get previousmonthsalary.
		@return previousmonthsalary	  */
	public BigDecimal getpreviousmonthsalary () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_previousmonthsalary);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public I_C_BPartner getref_partner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getref_partner_id(), get_TrxName());	}

	/** Set ref_partner_id.
		@param ref_partner_id ref_partner_id	  */
	public void setref_partner_id (int ref_partner_id)
	{
		set_Value (COLUMNNAME_ref_partner_id, Integer.valueOf(ref_partner_id));
	}

	/** Get ref_partner_id.
		@return ref_partner_id	  */
	public int getref_partner_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ref_partner_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set reqnoofinstalments.
		@param reqnoofinstalments reqnoofinstalments	  */
	public void setreqnoofinstalments (int reqnoofinstalments)
	{
		set_Value (COLUMNNAME_reqnoofinstalments, Integer.valueOf(reqnoofinstalments));
	}

	/** Get reqnoofinstalments.
		@return reqnoofinstalments	  */
	public int getreqnoofinstalments () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_reqnoofinstalments);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set requestdate.
		@param requestdate requestdate	  */
	public void setrequestdate (Timestamp requestdate)
	{
		set_Value (COLUMNNAME_requestdate, requestdate);
	}

	/** Get requestdate.
		@return requestdate	  */
	public Timestamp getrequestdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_requestdate);
	}

	/** Set requestedamt.
		@param requestedamt requestedamt	  */
	public void setrequestedamt (BigDecimal requestedamt)
	{
		set_Value (COLUMNNAME_requestedamt, requestedamt);
	}

	/** Get requestedamt.
		@return requestedamt	  */
	public BigDecimal getrequestedamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_requestedamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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