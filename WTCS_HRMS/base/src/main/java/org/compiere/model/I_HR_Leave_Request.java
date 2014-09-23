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

/** Generated Interface for HR_Leave_Request
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HR_Leave_Request 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_HR_Leave_Request.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=HR_Leave_Request */
    public static final String Table_Name = "HR_Leave_Request";

    /** AD_Table_ID=1000074 */
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

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Employee.
	  * Identifies a Employee
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Employee.
	  * Identifies a Employee
	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name Comments */
    public static final String COLUMNNAME_Comments = "Comments";

	/** Set Comments.
	  * Comments or additional information
	  */
	public void setComments (String Comments);

	/** Get Comments.
	  * Comments or additional information
	  */
	public String getComments();

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

    /** Column name FromDate */
    public static final String COLUMNNAME_FromDate = "FromDate";

	/** Set From Date	  */
	public void setFromDate (Timestamp FromDate);

	/** Get From Date	  */
	public Timestamp getFromDate();

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee Name	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee Name	  */
	public int getHR_Employee_ID();

	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_Leave_Request_ID */
    public static final String COLUMNNAME_HR_Leave_Request_ID = "HR_Leave_Request_ID";

	/** Set HR_Leave_Request	  */
	public void setHR_Leave_Request_ID (int HR_Leave_Request_ID);

	/** Get HR_Leave_Request	  */
	public int getHR_Leave_Request_ID();

    /** Column name HR_LeaveType_ID */
    public static final String COLUMNNAME_HR_LeaveType_ID = "HR_LeaveType_ID";

	/** Set Leave Type.
	  * Leave Type
	  */
	public void setHR_LeaveType_ID (int HR_LeaveType_ID);

	/** Get Leave Type.
	  * Leave Type
	  */
	public int getHR_LeaveType_ID();

	public I_HR_LeaveType getHR_LeaveType() throws RuntimeException;

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

    /** Column name isavailed */
    public static final String COLUMNNAME_isavailed = "isavailed";

	/** Set isavailed	  */
	public void setisavailed (boolean isavailed);

	/** Get isavailed	  */
	public boolean isavailed();

    /** Column name leave_status */
    public static final String COLUMNNAME_leave_status = "leave_status";

	/** Set leave_status	  */
	public void setleave_status (String leave_status);

	/** Get leave_status	  */
	public String getleave_status();

    /** Column name leavesummary */
    public static final String COLUMNNAME_leavesummary = "leavesummary";

	/** Set Leave Summary.
	  * Leave Summary
	  */
	public void setleavesummary (String leavesummary);

	/** Get Leave Summary.
	  * Leave Summary
	  */
	public String getleavesummary();

    /** Column name number_of_workingdays */
    public static final String COLUMNNAME_number_of_workingdays = "number_of_workingdays";

	/** Set number_of_workingdays	  */
	public void setnumber_of_workingdays (BigDecimal number_of_workingdays);

	/** Get number_of_workingdays	  */
	public BigDecimal getnumber_of_workingdays();

    /** Column name otherdisapprovalreason */
    public static final String COLUMNNAME_otherdisapprovalreason = "otherdisapprovalreason";

	/** Set otherdisapprovalreason	  */
	public void setotherdisapprovalreason (String otherdisapprovalreason);

	/** Get otherdisapprovalreason	  */
	public String getotherdisapprovalreason();

    /** Column name otherreason */
    public static final String COLUMNNAME_otherreason = "otherreason";

	/** Set otherreason	  */
	public void setotherreason (String otherreason);

	/** Get otherreason	  */
	public String getotherreason();

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

    /** Column name requestdisapprovalreason_ID */
    public static final String COLUMNNAME_requestdisapprovalreason_ID = "requestdisapprovalreason_ID";

	/** Set Disapproval Reason ID	  */
	public void setrequestdisapprovalreason_ID (int requestdisapprovalreason_ID);

	/** Get Disapproval Reason ID	  */
	public int getrequestdisapprovalreason_ID();

	public I_WTC_Reasons getrequestdisapprovalreason() throws RuntimeException;

    /** Column name ToDate */
    public static final String COLUMNNAME_ToDate = "ToDate";

	/** Set To Date	  */
	public void setToDate (Timestamp ToDate);

	/** Get To Date	  */
	public Timestamp getToDate();

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

    /** Column name workinghours */
    public static final String COLUMNNAME_workinghours = "workinghours";

	/** Set workinghours	  */
	public void setworkinghours (BigDecimal workinghours);

	/** Get workinghours	  */
	public BigDecimal getworkinghours();

    /** Column name WTC_Reasons_ID */
    public static final String COLUMNNAME_WTC_Reasons_ID = "WTC_Reasons_ID";

	/** Set Reason.
	  * Predefiend reasons
	  */
	public void setWTC_Reasons_ID (int WTC_Reasons_ID);

	/** Get Reason.
	  * Predefiend reasons
	  */
	public int getWTC_Reasons_ID();

	public I_WTC_Reasons getWTC_Reasons() throws RuntimeException;
}
