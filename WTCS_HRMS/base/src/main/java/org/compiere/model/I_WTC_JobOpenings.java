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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for WTC_JobOpenings
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_WTC_JobOpenings 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_WTC_JobOpenings.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=WTC_JobOpenings */
    public static final String Table_Name = "WTC_JobOpenings";

    /** AD_Table_ID=1000065 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_Location_ID */
    public static final String COLUMNNAME_C_Location_ID = "C_Location_ID";

	/** Set Address.
	  * Location or Address
	  */
	public void setC_Location_ID (int C_Location_ID);

	/** Get Address.
	  * Location or Address
	  */
	public int getC_Location_ID();

	public I_C_Location getC_Location() throws RuntimeException;

    /** Column name compensation */
    public static final String COLUMNNAME_compensation = "compensation";

	/** Set compensation	  */
	public void setcompensation (String compensation);

	/** Get compensation	  */
	public String getcompensation();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DocAction */
    public static final String COLUMNNAME_DocAction = "DocAction";

	/** Set Document Action.
	  * The targeted status of the document
	  */
	public void setDocAction (String DocAction);

	/** Get Document Action.
	  * The targeted status of the document
	  */
	public String getDocAction();

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name effective_fromdate */
    public static final String COLUMNNAME_effective_fromdate = "effective_fromdate";

	/** Set effective_fromdate	  */
	public void seteffective_fromdate (Timestamp effective_fromdate);

	/** Get effective_fromdate	  */
	public Timestamp geteffective_fromdate();

    /** Column name expiration_date */
    public static final String COLUMNNAME_expiration_date = "expiration_date";

	/** Set expiration_date	  */
	public void setexpiration_date (Timestamp expiration_date);

	/** Get expiration_date	  */
	public Timestamp getexpiration_date();

    /** Column name HR_Department_ID */
    public static final String COLUMNNAME_HR_Department_ID = "HR_Department_ID";

	/** Set Department.
	  * This Department Will Be Shown TO Users Who Are Having Department Manager Role 
	  */
	public void setHR_Department_ID (int HR_Department_ID);

	/** Get Department.
	  * This Department Will Be Shown TO Users Who Are Having Department Manager Role 
	  */
	public int getHR_Department_ID();

	public org.eevolution.model.I_HR_Department getHR_Department() throws RuntimeException;

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsApproved */
    public static final String COLUMNNAME_IsApproved = "IsApproved";

	/** Set Is Management Approved.
	  * Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved);

	/** Get Is Management Approved.
	  * Indicates if this document requires approval
	  */
	public boolean isApproved();

    /** Column name ishodapproval */
    public static final String COLUMNNAME_ishodapproval = "ishodapproval";

	/** Set ishodapproval	  */
	public void setishodapproval (boolean ishodapproval);

	/** Get ishodapproval	  */
	public boolean ishodapproval();

    /** Column name ishrapproval */
    public static final String COLUMNNAME_ishrapproval = "ishrapproval";

	/** Set ishrapproval	  */
	public void setishrapproval (boolean ishrapproval);

	/** Get ishrapproval	  */
	public boolean ishrapproval();

    /** Column name jobcode */
    public static final String COLUMNNAME_jobcode = "jobcode";

	/** Set jobcode	  */
	public void setjobcode (String jobcode);

	/** Get jobcode	  */
	public String getjobcode();

    /** Column name numberofopenpositions */
    public static final String COLUMNNAME_numberofopenpositions = "numberofopenpositions";

	/** Set numberofopenpositions	  */
	public void setnumberofopenpositions (int numberofopenpositions);

	/** Get numberofopenpositions	  */
	public int getnumberofopenpositions();

    /** Column name primary_skills */
    public static final String COLUMNNAME_primary_skills = "primary_skills";

	/** Set primary_skills	  */
	public void setprimary_skills (String primary_skills);

	/** Get primary_skills	  */
	public String getprimary_skills();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name responsibilities */
    public static final String COLUMNNAME_responsibilities = "responsibilities";

	/** Set responsibilities	  */
	public void setresponsibilities (String responsibilities);

	/** Get responsibilities	  */
	public String getresponsibilities();

    /** Column name secondary_skills */
    public static final String COLUMNNAME_secondary_skills = "secondary_skills";

	/** Set secondary_skills	  */
	public void setsecondary_skills (String secondary_skills);

	/** Get secondary_skills	  */
	public String getsecondary_skills();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name WFState */
    public static final String COLUMNNAME_WFState = "WFState";

	/** Set Workflow State.
	  * State of the execution of the workflow
	  */
	public void setWFState (String WFState);

	/** Get Workflow State.
	  * State of the execution of the workflow
	  */
	public String getWFState();

    /** Column name WTC_CareerLevel_ID */
    public static final String COLUMNNAME_WTC_CareerLevel_ID = "WTC_CareerLevel_ID";

	/** Set WTC_CareerLevel	  */
	public void setWTC_CareerLevel_ID (int WTC_CareerLevel_ID);

	/** Get WTC_CareerLevel	  */
	public int getWTC_CareerLevel_ID();

	public I_WTC_CareerLevel getWTC_CareerLevel() throws RuntimeException;

    /** Column name WTC_JobEducation_ID */
    public static final String COLUMNNAME_WTC_JobEducation_ID = "WTC_JobEducation_ID";

	/** Set WTC_JobEducation	  */
	public void setWTC_JobEducation_ID (int WTC_JobEducation_ID);

	/** Get WTC_JobEducation	  */
	public int getWTC_JobEducation_ID();

	public I_WTC_JobEducation getWTC_JobEducation() throws RuntimeException;

    /** Column name WTC_JobOpenings_ID */
    public static final String COLUMNNAME_WTC_JobOpenings_ID = "WTC_JobOpenings_ID";

	/** Set WTC_JobOpenings	  */
	public void setWTC_JobOpenings_ID (int WTC_JobOpenings_ID);

	/** Get WTC_JobOpenings	  */
	public int getWTC_JobOpenings_ID();

    /** Column name WTC_JobType_ID */
    public static final String COLUMNNAME_WTC_JobType_ID = "WTC_JobType_ID";

	/** Set WTC_JobType	  */
	public void setWTC_JobType_ID (int WTC_JobType_ID);

	/** Get WTC_JobType	  */
	public int getWTC_JobType_ID();

	public I_WTC_JobType getWTC_JobType() throws RuntimeException;
}
