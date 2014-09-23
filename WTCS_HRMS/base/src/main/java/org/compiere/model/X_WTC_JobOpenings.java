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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for WTC_JobOpenings
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_WTC_JobOpenings.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_WTC_JobOpenings extends PO implements I_WTC_JobOpenings, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_WTC_JobOpenings.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111202L;

    /** Standard Constructor */
    public X_WTC_JobOpenings (Properties ctx, int WTC_JobOpenings_ID, String trxName)
    {
      super (ctx, WTC_JobOpenings_ID, trxName);
      /** if (WTC_JobOpenings_ID == 0)
        {
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			seteffective_fromdate (new Timestamp( System.currentTimeMillis() ));
			setexpiration_date (new Timestamp( System.currentTimeMillis() ));
			setIsApproved (false);
			setishodapproval (false);
			setishrapproval (false);
			setProcessed (false);
			setWTC_JobOpenings_ID (0);
        } */
    }

    /** Load Constructor */
    public X_WTC_JobOpenings (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_WTC_JobOpenings[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_DocType getC_DocType() throws RuntimeException
    {
		return (I_C_DocType)MTable.get(getCtx(), I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Location getC_Location() throws RuntimeException
    {
		return (I_C_Location)MTable.get(getCtx(), I_C_Location.Table_Name)
			.getPO(getC_Location_ID(), get_TrxName());	}

	/** Set Address.
		@param C_Location_ID 
		Location or Address
	  */
	public void setC_Location_ID (int C_Location_ID)
	{
		if (C_Location_ID < 1) 
			set_Value (COLUMNNAME_C_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_Location_ID, Integer.valueOf(C_Location_ID));
	}

	/** Get Address.
		@return Location or Address
	  */
	public int getC_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set compensation.
		@param compensation compensation	  */
	public void setcompensation (String compensation)
	{
		set_Value (COLUMNNAME_compensation, compensation);
	}

	/** Get compensation.
		@return compensation	  */
	public String getcompensation () 
	{
		return (String)get_Value(COLUMNNAME_compensation);
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

	/** Set effective_fromdate.
		@param effective_fromdate effective_fromdate	  */
	public void seteffective_fromdate (Timestamp effective_fromdate)
	{
		set_Value (COLUMNNAME_effective_fromdate, effective_fromdate);
	}

	/** Get effective_fromdate.
		@return effective_fromdate	  */
	public Timestamp geteffective_fromdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_effective_fromdate);
	}

	/** Set expiration_date.
		@param expiration_date expiration_date	  */
	public void setexpiration_date (Timestamp expiration_date)
	{
		set_Value (COLUMNNAME_expiration_date, expiration_date);
	}

	/** Get expiration_date.
		@return expiration_date	  */
	public Timestamp getexpiration_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_expiration_date);
	}

	public org.eevolution.model.I_HR_Department getHR_Department() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Department)MTable.get(getCtx(), org.eevolution.model.I_HR_Department.Table_Name)
			.getPO(getHR_Department_ID(), get_TrxName());	}

	/** Set Department.
		@param HR_Department_ID 
		This Department Will Be Shown TO Users Who Are Having Department Manager Role 
	  */
	public void setHR_Department_ID (int HR_Department_ID)
	{
		if (HR_Department_ID < 1) 
			set_Value (COLUMNNAME_HR_Department_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Department_ID, Integer.valueOf(HR_Department_ID));
	}

	/** Get Department.
		@return This Department Will Be Shown TO Users Who Are Having Department Manager Role 
	  */
	public int getHR_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Department_ID);
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

	/** Set ishodapproval.
		@param ishodapproval ishodapproval	  */
	public void setishodapproval (boolean ishodapproval)
	{
		set_Value (COLUMNNAME_ishodapproval, Boolean.valueOf(ishodapproval));
	}

	/** Get ishodapproval.
		@return ishodapproval	  */
	public boolean ishodapproval () 
	{
		Object oo = get_Value(COLUMNNAME_ishodapproval);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ishrapproval.
		@param ishrapproval ishrapproval	  */
	public void setishrapproval (boolean ishrapproval)
	{
		set_Value (COLUMNNAME_ishrapproval, Boolean.valueOf(ishrapproval));
	}

	/** Get ishrapproval.
		@return ishrapproval	  */
	public boolean ishrapproval () 
	{
		Object oo = get_Value(COLUMNNAME_ishrapproval);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set jobcode.
		@param jobcode jobcode	  */
	public void setjobcode (String jobcode)
	{
		set_ValueNoCheck (COLUMNNAME_jobcode, jobcode);
	}

	/** Get jobcode.
		@return jobcode	  */
	public String getjobcode () 
	{
		return (String)get_Value(COLUMNNAME_jobcode);
	}

	/** Set numberofopenpositions.
		@param numberofopenpositions numberofopenpositions	  */
	public void setnumberofopenpositions (int numberofopenpositions)
	{
		set_Value (COLUMNNAME_numberofopenpositions, Integer.valueOf(numberofopenpositions));
	}

	/** Get numberofopenpositions.
		@return numberofopenpositions	  */
	public int getnumberofopenpositions () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_numberofopenpositions);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set primary_skills.
		@param primary_skills primary_skills	  */
	public void setprimary_skills (String primary_skills)
	{
		set_Value (COLUMNNAME_primary_skills, primary_skills);
	}

	/** Get primary_skills.
		@return primary_skills	  */
	public String getprimary_skills () 
	{
		return (String)get_Value(COLUMNNAME_primary_skills);
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

	/** Set responsibilities.
		@param responsibilities responsibilities	  */
	public void setresponsibilities (String responsibilities)
	{
		set_Value (COLUMNNAME_responsibilities, responsibilities);
	}

	/** Get responsibilities.
		@return responsibilities	  */
	public String getresponsibilities () 
	{
		return (String)get_Value(COLUMNNAME_responsibilities);
	}

	/** Set secondary_skills.
		@param secondary_skills secondary_skills	  */
	public void setsecondary_skills (String secondary_skills)
	{
		set_Value (COLUMNNAME_secondary_skills, secondary_skills);
	}

	/** Get secondary_skills.
		@return secondary_skills	  */
	public String getsecondary_skills () 
	{
		return (String)get_Value(COLUMNNAME_secondary_skills);
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

	public I_WTC_CareerLevel getWTC_CareerLevel() throws RuntimeException
    {
		return (I_WTC_CareerLevel)MTable.get(getCtx(), I_WTC_CareerLevel.Table_Name)
			.getPO(getWTC_CareerLevel_ID(), get_TrxName());	}

	/** Set WTC_CareerLevel.
		@param WTC_CareerLevel_ID WTC_CareerLevel	  */
	public void setWTC_CareerLevel_ID (int WTC_CareerLevel_ID)
	{
		if (WTC_CareerLevel_ID < 1) 
			set_Value (COLUMNNAME_WTC_CareerLevel_ID, null);
		else 
			set_Value (COLUMNNAME_WTC_CareerLevel_ID, Integer.valueOf(WTC_CareerLevel_ID));
	}

	/** Get WTC_CareerLevel.
		@return WTC_CareerLevel	  */
	public int getWTC_CareerLevel_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_CareerLevel_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_WTC_JobEducation getWTC_JobEducation() throws RuntimeException
    {
		return (I_WTC_JobEducation)MTable.get(getCtx(), I_WTC_JobEducation.Table_Name)
			.getPO(getWTC_JobEducation_ID(), get_TrxName());	}

	/** Set WTC_JobEducation.
		@param WTC_JobEducation_ID WTC_JobEducation	  */
	public void setWTC_JobEducation_ID (int WTC_JobEducation_ID)
	{
		if (WTC_JobEducation_ID < 1) 
			set_Value (COLUMNNAME_WTC_JobEducation_ID, null);
		else 
			set_Value (COLUMNNAME_WTC_JobEducation_ID, Integer.valueOf(WTC_JobEducation_ID));
	}

	/** Get WTC_JobEducation.
		@return WTC_JobEducation	  */
	public int getWTC_JobEducation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_JobEducation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set WTC_JobOpenings.
		@param WTC_JobOpenings_ID WTC_JobOpenings	  */
	public void setWTC_JobOpenings_ID (int WTC_JobOpenings_ID)
	{
		if (WTC_JobOpenings_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WTC_JobOpenings_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WTC_JobOpenings_ID, Integer.valueOf(WTC_JobOpenings_ID));
	}

	/** Get WTC_JobOpenings.
		@return WTC_JobOpenings	  */
	public int getWTC_JobOpenings_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_JobOpenings_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_WTC_JobType getWTC_JobType() throws RuntimeException
    {
		return (I_WTC_JobType)MTable.get(getCtx(), I_WTC_JobType.Table_Name)
			.getPO(getWTC_JobType_ID(), get_TrxName());	}

	/** Set WTC_JobType.
		@param WTC_JobType_ID WTC_JobType	  */
	public void setWTC_JobType_ID (int WTC_JobType_ID)
	{
		if (WTC_JobType_ID < 1) 
			set_Value (COLUMNNAME_WTC_JobType_ID, null);
		else 
			set_Value (COLUMNNAME_WTC_JobType_ID, Integer.valueOf(WTC_JobType_ID));
	}

	/** Get WTC_JobType.
		@return WTC_JobType	  */
	public int getWTC_JobType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_JobType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}