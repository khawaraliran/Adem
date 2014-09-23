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

/** Generated Interface for HR_Leave_Assign
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HR_Leave_Assign 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_HR_Leave_Assign.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=HR_Leave_Assign */
    public static final String Table_Name = "HR_Leave_Assign";

    /** AD_Table_ID=1000040 */
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

    /** Column name add_leaves */
    public static final String COLUMNNAME_add_leaves = "add_leaves";

	/** Set Add Leaves	  */
	public void setadd_leaves (BigDecimal add_leaves);

	/** Get Add Leaves	  */
	public BigDecimal getadd_leaves();

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

    /** Column name balance_leaves */
    public static final String COLUMNNAME_balance_leaves = "balance_leaves";

	/** Set Balance Leaves	  */
	public void setbalance_leaves (BigDecimal balance_leaves);

	/** Get Balance Leaves	  */
	public BigDecimal getbalance_leaves();

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

    /** Column name creditleavesbutton */
    public static final String COLUMNNAME_creditleavesbutton = "creditleavesbutton";

	/** Set creditleavesbutton	  */
	public void setcreditleavesbutton (String creditleavesbutton);

	/** Get creditleavesbutton	  */
	public String getcreditleavesbutton();

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

    /** Column name employee_code */
    public static final String COLUMNNAME_employee_code = "employee_code";

	/** Set Employee Code.
	  * Employee Code - Unique
	  */
	public void setemployee_code (String employee_code);

	/** Get Employee Code.
	  * Employee Code - Unique
	  */
	public String getemployee_code();

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

    /** Column name leave_hist */
    public static final String COLUMNNAME_leave_hist = "leave_hist";

	/** Set leave_hist	  */
	public void setleave_hist (boolean leave_hist);

	/** Get leave_hist	  */
	public boolean isleave_hist();

    /** Column name prvyearcfleaves */
    public static final String COLUMNNAME_prvyearcfleaves = "prvyearcfleaves";

	/** Set prvyearcfleaves	  */
	public void setprvyearcfleaves (BigDecimal prvyearcfleaves);

	/** Get prvyearcfleaves	  */
	public BigDecimal getprvyearcfleaves();

    /** Column name total_leaves */
    public static final String COLUMNNAME_total_leaves = "total_leaves";

	/** Set Number of Leaves Allocated	  */
	public void settotal_leaves (BigDecimal total_leaves);

	/** Get Number of Leaves Allocated	  */
	public BigDecimal gettotal_leaves();

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

    /** Column name used_leaves */
    public static final String COLUMNNAME_used_leaves = "used_leaves";

	/** Set Used Leaves	  */
	public void setused_leaves (BigDecimal used_leaves);

	/** Get Used Leaves	  */
	public BigDecimal getused_leaves();
}
