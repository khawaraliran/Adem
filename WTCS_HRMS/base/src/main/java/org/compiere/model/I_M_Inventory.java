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

/** Generated Interface for M_Inventory
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_M_Inventory 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_M_Inventory.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=M_Inventory */
    public static final String Table_Name = "M_Inventory";

    /** AD_Table_ID=321 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 1 - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(1);

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

    /** Column name AD_OrgTrx_ID */
    public static final String COLUMNNAME_AD_OrgTrx_ID = "AD_OrgTrx_ID";

	/** Set Trx Organization.
	  * Performing or initiating organization
	  */
	public void setAD_OrgTrx_ID (int AD_OrgTrx_ID);

	/** Get Trx Organization.
	  * Performing or initiating organization
	  */
	public int getAD_OrgTrx_ID();

    /** Column name ApprovalAmt */
    public static final String COLUMNNAME_ApprovalAmt = "ApprovalAmt";

	/** Set Approval Amount.
	  * Document Approval Amount
	  */
	public void setApprovalAmt (BigDecimal ApprovalAmt);

	/** Get Approval Amount.
	  * Document Approval Amount
	  */
	public BigDecimal getApprovalAmt();

    /** Column name C_Activity_ID */
    public static final String COLUMNNAME_C_Activity_ID = "C_Activity_ID";

	/** Set Activity.
	  * Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID);

	/** Get Activity.
	  * Business Activity
	  */
	public int getC_Activity_ID();

	public I_C_Activity getC_Activity() throws RuntimeException;

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

    /** Column name C_Campaign_ID */
    public static final String COLUMNNAME_C_Campaign_ID = "C_Campaign_ID";

	/** Set Campaign.
	  * Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID);

	/** Get Campaign.
	  * Marketing Campaign
	  */
	public int getC_Campaign_ID();

	public I_C_Campaign getC_Campaign() throws RuntimeException;

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

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public I_C_Project getC_Project() throws RuntimeException;

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

    /** Column name CreateRequisition */
    public static final String COLUMNNAME_CreateRequisition = "CreateRequisition";

	/** Set Create Requisition.
	  * Create Requisition
	  */
	public void setCreateRequisition (String CreateRequisition);

	/** Get Create Requisition.
	  * Create Requisition
	  */
	public String getCreateRequisition();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

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

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name GenerateList */
    public static final String COLUMNNAME_GenerateList = "GenerateList";

	/** Set Generate List.
	  * Generate List
	  */
	public void setGenerateList (String GenerateList);

	/** Get Generate List.
	  * Generate List
	  */
	public String getGenerateList();

    /** Column name HR_Department_ID */
    public static final String COLUMNNAME_HR_Department_ID = "HR_Department_ID";

	/** Set Department.
	  * Department Name
	  */
	public void setHR_Department_ID (int HR_Department_ID);

	/** Get Department.
	  * Department Name
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

	/** Set Approved.
	  * Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved);

	/** Get Approved.
	  * Indicates if this document requires approval
	  */
	public boolean isApproved();

    /** Column name IsHODApproved */
    public static final String COLUMNNAME_IsHODApproved = "IsHODApproved";

	/** Set Is HOD Approved ?.
	  * Is Approved by HOD 
	  */
	public void setIsHODApproved (boolean IsHODApproved);

	/** Get Is HOD Approved ?.
	  * Is Approved by HOD 
	  */
	public boolean isHODApproved();

    /** Column name IsIssued */
    public static final String COLUMNNAME_IsIssued = "IsIssued";

	/** Set Is Issued ?.
	  * Is Issued ?
	  */
	public void setIsIssued (boolean IsIssued);

	/** Get Is Issued ?.
	  * Is Issued ?
	  */
	public boolean isIssued();

    /** Column name IsRedIndent */
    public static final String COLUMNNAME_IsRedIndent = "IsRedIndent";

	/** Set Is Red Indent ?.
	  * Is Red Indent ?
	  */
	public void setIsRedIndent (boolean IsRedIndent);

	/** Get Is Red Indent ?.
	  * Is Red Indent ?
	  */
	public boolean isRedIndent();

    /** Column name IsReturn */
    public static final String COLUMNNAME_IsReturn = "IsReturn";

	/** Set Is Return ?.
	  * Is Return ?
	  */
	public void setIsReturn (boolean IsReturn);

	/** Get Is Return ?.
	  * Is Return ?
	  */
	public boolean isReturn();

    /** Column name IsUrgent */
    public static final String COLUMNNAME_IsUrgent = "IsUrgent";

	/** Set Is Urgent ?.
	  * Is Urgent ?
	  */
	public void setIsUrgent (boolean IsUrgent);

	/** Get Is Urgent ?.
	  * Is Urgent ?
	  */
	public boolean isUrgent();

    /** Column name M_Inventory_ID */
    public static final String COLUMNNAME_M_Inventory_ID = "M_Inventory_ID";

	/** Set Phys.Inventory.
	  * Parameters for a Physical Inventory
	  */
	public void setM_Inventory_ID (int M_Inventory_ID);

	/** Get Phys.Inventory.
	  * Parameters for a Physical Inventory
	  */
	public int getM_Inventory_ID();

    /** Column name MovementDate */
    public static final String COLUMNNAME_MovementDate = "MovementDate";

	/** Set Movement Date.
	  * Date a product was moved in or out of inventory
	  */
	public void setMovementDate (Timestamp MovementDate);

	/** Get Movement Date.
	  * Date a product was moved in or out of inventory
	  */
	public Timestamp getMovementDate();

    /** Column name MovementType */
    public static final String COLUMNNAME_MovementType = "MovementType";

	/** Set Movement Type.
	  * Method of moving the inventory
	  */
	public void setMovementType (String MovementType);

	/** Get Movement Type.
	  * Method of moving the inventory
	  */
	public String getMovementType();

    /** Column name M_PerpetualInv_ID */
    public static final String COLUMNNAME_M_PerpetualInv_ID = "M_PerpetualInv_ID";

	/** Set Perpetual Inventory.
	  * Rules for generating physical inventory
	  */
	public void setM_PerpetualInv_ID (int M_PerpetualInv_ID);

	/** Get Perpetual Inventory.
	  * Rules for generating physical inventory
	  */
	public int getM_PerpetualInv_ID();

	public I_M_PerpetualInv getM_PerpetualInv() throws RuntimeException;

    /** Column name M_Warehouse_ID */
    public static final String COLUMNNAME_M_Warehouse_ID = "M_Warehouse_ID";

	/** Set Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID);

	/** Get Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID();

	public I_M_Warehouse getM_Warehouse() throws RuntimeException;

    /** Column name OrginialIdent_ID */
    public static final String COLUMNNAME_OrginialIdent_ID = "OrginialIdent_ID";

	/** Set Orginial Ident	  */
	public void setOrginialIdent_ID (int OrginialIdent_ID);

	/** Get Orginial Ident	  */
	public int getOrginialIdent_ID();

	public I_M_Inventory getOrginialIdent() throws RuntimeException;

    /** Column name Posted */
    public static final String COLUMNNAME_Posted = "Posted";

	/** Set Posted.
	  * Posting status
	  */
	public void setPosted (boolean Posted);

	/** Get Posted.
	  * Posting status
	  */
	public boolean isPosted();

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

    /** Column name ProcessedOn */
    public static final String COLUMNNAME_ProcessedOn = "ProcessedOn";

	/** Set Processed On.
	  * The date+time (expressed in decimal format) when the document has been processed
	  */
	public void setProcessedOn (BigDecimal ProcessedOn);

	/** Get Processed On.
	  * The date+time (expressed in decimal format) when the document has been processed
	  */
	public BigDecimal getProcessedOn();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name RequestDate */
    public static final String COLUMNNAME_RequestDate = "RequestDate";

	/** Set Request Date.
	  * Request Date
	  */
	public void setRequestDate (Timestamp RequestDate);

	/** Get Request Date.
	  * Request Date
	  */
	public Timestamp getRequestDate();

    /** Column name Reversal_ID */
    public static final String COLUMNNAME_Reversal_ID = "Reversal_ID";

	/** Set Reversal ID.
	  * ID of document reversal
	  */
	public void setReversal_ID (int Reversal_ID);

	/** Get Reversal ID.
	  * ID of document reversal
	  */
	public int getReversal_ID();

	public I_M_Inventory getReversal() throws RuntimeException;

    /** Column name S_Resource_ID */
    public static final String COLUMNNAME_S_Resource_ID = "S_Resource_ID";

	/** Set Resource.
	  * Resource
	  */
	public void setS_Resource_ID (int S_Resource_ID);

	/** Get Resource.
	  * Resource
	  */
	public int getS_Resource_ID();

	public I_S_Resource getS_Resource() throws RuntimeException;

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

    /** Column name UpdateQty */
    public static final String COLUMNNAME_UpdateQty = "UpdateQty";

	/** Set Update Quantities	  */
	public void setUpdateQty (String UpdateQty);

	/** Get Update Quantities	  */
	public String getUpdateQty();

    /** Column name UrgencyReason */
    public static final String COLUMNNAME_UrgencyReason = "UrgencyReason";

	/** Set Urgency Reason.
	  * Reason for Urgency
	  */
	public void setUrgencyReason (String UrgencyReason);

	/** Get Urgency Reason.
	  * Reason for Urgency
	  */
	public String getUrgencyReason();

    /** Column name User1_ID */
    public static final String COLUMNNAME_User1_ID = "User1_ID";

	/** Set User List 1.
	  * User defined list element #1
	  */
	public void setUser1_ID (int User1_ID);

	/** Get User List 1.
	  * User defined list element #1
	  */
	public int getUser1_ID();

	public I_C_ElementValue getUser1() throws RuntimeException;

    /** Column name User2_ID */
    public static final String COLUMNNAME_User2_ID = "User2_ID";

	/** Set User List 2.
	  * User defined list element #2
	  */
	public void setUser2_ID (int User2_ID);

	/** Get User List 2.
	  * User defined list element #2
	  */
	public int getUser2_ID();

	public I_C_ElementValue getUser2() throws RuntimeException;

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

    /** Column name WTC_Defect_ID */
    public static final String COLUMNNAME_WTC_Defect_ID = "WTC_Defect_ID";

	/** Set Defect.
	  * Defect
	  */
	public void setWTC_Defect_ID (int WTC_Defect_ID);

	/** Get Defect.
	  * Defect
	  */
	public int getWTC_Defect_ID();

    /** Column name WTC_Maintenance_ID */
    public static final String COLUMNNAME_WTC_Maintenance_ID = "WTC_Maintenance_ID";

	/** Set Maintenance.
	  * Maintenance
	  */
	public void setWTC_Maintenance_ID (int WTC_Maintenance_ID);

	/** Get Maintenance.
	  * Maintenance
	  */
	public int getWTC_Maintenance_ID();
}
