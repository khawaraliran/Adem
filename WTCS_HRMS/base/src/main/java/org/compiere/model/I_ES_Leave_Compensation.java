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

/** Generated Interface for ES_Leave_Compensation
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_ES_Leave_Compensation 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_ES_Leave_Compensation.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=ES_Leave_Compensation */
    public static final String Table_Name = "ES_Leave_Compensation";

    /** AD_Table_ID=1000079 */
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

    /** Column name compensationamt */
    public static final String COLUMNNAME_compensationamt = "compensationamt";

	/** Set compensationamt	  */
	public void setcompensationamt (BigDecimal compensationamt);

	/** Get compensationamt	  */
	public BigDecimal getcompensationamt();

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

    /** Column name encashedleaves */
    public static final String COLUMNNAME_encashedleaves = "encashedleaves";

	/** Set encashedleaves	  */
	public void setencashedleaves (BigDecimal encashedleaves);

	/** Get encashedleaves	  */
	public BigDecimal getencashedleaves();

    /** Column name ES_Leave_Compensation_ID */
    public static final String COLUMNNAME_ES_Leave_Compensation_ID = "ES_Leave_Compensation_ID";

	/** Set ES_Leave_Compensation	  */
	public void setES_Leave_Compensation_ID (int ES_Leave_Compensation_ID);

	/** Get ES_Leave_Compensation	  */
	public int getES_Leave_Compensation_ID();

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

    /** Column name HR_Period_ID */
    public static final String COLUMNNAME_HR_Period_ID = "HR_Period_ID";

	/** Set Payroll Period	  */
	public void setHR_Period_ID (int HR_Period_ID);

	/** Get Payroll Period	  */
	public int getHR_Period_ID();

	public org.eevolution.model.I_HR_Period getHR_Period() throws RuntimeException;

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

    /** Column name processeddate */
    public static final String COLUMNNAME_processeddate = "processeddate";

	/** Set processeddate	  */
	public void setprocesseddate (Timestamp processeddate);

	/** Get processeddate	  */
	public Timestamp getprocesseddate();

    /** Column name reqdate */
    public static final String COLUMNNAME_reqdate = "reqdate";

	/** Set reqdate	  */
	public void setreqdate (Timestamp reqdate);

	/** Get reqdate	  */
	public Timestamp getreqdate();

    /** Column name showpayslip */
    public static final String COLUMNNAME_showpayslip = "showpayslip";

	/** Set showpayslip	  */
	public void setshowpayslip (boolean showpayslip);

	/** Get showpayslip	  */
	public boolean isshowpayslip();

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
}
