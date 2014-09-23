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

/** Generated Interface for WTC_Leave_CreditHistory
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_WTC_Leave_CreditHistory 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_WTC_Leave_CreditHistory.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=WTC_Leave_CreditHistory */
    public static final String Table_Name = "WTC_Leave_CreditHistory";

    /** AD_Table_ID=1000037 */
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

    /** Column name C_Year_ID */
    public static final String COLUMNNAME_C_Year_ID = "C_Year_ID";

	/** Set Year.
	  * Calendar Year
	  */
	public void setC_Year_ID (int C_Year_ID);

	/** Get Year.
	  * Calendar Year
	  */
	public int getC_Year_ID();

	public I_C_Year getC_Year() throws RuntimeException;

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee Name	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee Name	  */
	public int getHR_Employee_ID();

	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_Leave_Assign_ID */
    public static final String COLUMNNAME_HR_Leave_Assign_ID = "HR_Leave_Assign_ID";

	/** Set HR_Leave_Assign	  */
	public void setHR_Leave_Assign_ID (int HR_Leave_Assign_ID);

	/** Get HR_Leave_Assign	  */
	public int getHR_Leave_Assign_ID();

	public I_HR_Leave_Assign getHR_Leave_Assign() throws RuntimeException;

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

    /** Column name noofleavescredited */
    public static final String COLUMNNAME_noofleavescredited = "noofleavescredited";

	/** Set No Of Leaves Credited	  */
	public void setnoofleavescredited (BigDecimal noofleavescredited);

	/** Get No Of Leaves Credited	  */
	public BigDecimal getnoofleavescredited();

    /** Column name reasonforcredit */
    public static final String COLUMNNAME_reasonforcredit = "reasonforcredit";

	/** Set Reason	  */
	public void setreasonforcredit (String reasonforcredit);

	/** Get Reason	  */
	public String getreasonforcredit();

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

    /** Column name WTC_Leave_CreditHistory_ID */
    public static final String COLUMNNAME_WTC_Leave_CreditHistory_ID = "WTC_Leave_CreditHistory_ID";

	/** Set WTC_Leave_CreditHistory	  */
	public void setWTC_Leave_CreditHistory_ID (int WTC_Leave_CreditHistory_ID);

	/** Get WTC_Leave_CreditHistory	  */
	public int getWTC_Leave_CreditHistory_ID();
}
