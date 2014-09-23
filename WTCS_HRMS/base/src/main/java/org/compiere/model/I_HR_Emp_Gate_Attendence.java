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

/** Generated Interface for HR_Emp_Gate_Attendence
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HR_Emp_Gate_Attendence 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_HR_Emp_Gate_Attendence.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=HR_Emp_Gate_Attendence */
    public static final String Table_Name = "HR_Emp_Gate_Attendence";

    /** AD_Table_ID=1000059 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name Actual_Work_Shift_ID */
    public static final String COLUMNNAME_Actual_Work_Shift_ID = "Actual_Work_Shift_ID";

	/** Set Actual_Work_Shift_ID	  */
	public void setActual_Work_Shift_ID (int Actual_Work_Shift_ID);

	/** Get Actual_Work_Shift_ID	  */
	public int getActual_Work_Shift_ID();

	public I_HR_Work_Shift getActual_Work_Shift() throws RuntimeException;

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

    /** Column name HR_Emp_Gate_Attendence_ID */
    public static final String COLUMNNAME_HR_Emp_Gate_Attendence_ID = "HR_Emp_Gate_Attendence_ID";

	/** Set HR_Emp_Gate_Attendence	  */
	public void setHR_Emp_Gate_Attendence_ID (int HR_Emp_Gate_Attendence_ID);

	/** Get HR_Emp_Gate_Attendence	  */
	public int getHR_Emp_Gate_Attendence_ID();

    /** Column name HR_Period_ID */
    public static final String COLUMNNAME_HR_Period_ID = "HR_Period_ID";

	/** Set Payroll Period	  */
	public void setHR_Period_ID (int HR_Period_ID);

	/** Get Payroll Period	  */
	public int getHR_Period_ID();

	public org.eevolution.model.I_HR_Period getHR_Period() throws RuntimeException;

    /** Column name HR_Work_Group_ID */
    public static final String COLUMNNAME_HR_Work_Group_ID = "HR_Work_Group_ID";

	/** Set Work Group Name.
	  * Name of the Work Group
	  */
	public void setHR_Work_Group_ID (int HR_Work_Group_ID);

	/** Get Work Group Name.
	  * Name of the Work Group
	  */
	public int getHR_Work_Group_ID();

	public I_HR_Work_Group getHR_Work_Group() throws RuntimeException;

    /** Column name incomingtime */
    public static final String COLUMNNAME_incomingtime = "incomingtime";

	/** Set incomingtime	  */
	public void setincomingtime (Timestamp incomingtime);

	/** Get incomingtime	  */
	public Timestamp getincomingtime();

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

    /** Column name islatecoming */
    public static final String COLUMNNAME_islatecoming = "islatecoming";

	/** Set islatecoming	  */
	public void setislatecoming (boolean islatecoming);

	/** Get islatecoming	  */
	public boolean islatecoming();

    /** Column name loginpunch */
    public static final String COLUMNNAME_loginpunch = "loginpunch";

	/** Set loginpunch	  */
	public void setloginpunch (String loginpunch);

	/** Get loginpunch	  */
	public String getloginpunch();

    /** Column name logoutpunch */
    public static final String COLUMNNAME_logoutpunch = "logoutpunch";

	/** Set logoutpunch	  */
	public void setlogoutpunch (String logoutpunch);

	/** Get logoutpunch	  */
	public String getlogoutpunch();

    /** Column name numberofhours */
    public static final String COLUMNNAME_numberofhours = "numberofhours";

	/** Set numberofhours	  */
	public void setnumberofhours (BigDecimal numberofhours);

	/** Get numberofhours	  */
	public BigDecimal getnumberofhours();

    /** Column name outgoingtime */
    public static final String COLUMNNAME_outgoingtime = "outgoingtime";

	/** Set outgoingtime	  */
	public void setoutgoingtime (Timestamp outgoingtime);

	/** Get outgoingtime	  */
	public Timestamp getoutgoingtime();

    /** Column name Plan_Work_Shift_ID */
    public static final String COLUMNNAME_Plan_Work_Shift_ID = "Plan_Work_Shift_ID";

	/** Set Plan_Work_Shift_ID	  */
	public void setPlan_Work_Shift_ID (int Plan_Work_Shift_ID);

	/** Get Plan_Work_Shift_ID	  */
	public int getPlan_Work_Shift_ID();

	public I_HR_Work_Shift getPlan_Work_Shift() throws RuntimeException;

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

    /** Column name workdate */
    public static final String COLUMNNAME_workdate = "workdate";

	/** Set workdate	  */
	public void setworkdate (Timestamp workdate);

	/** Get workdate	  */
	public Timestamp getworkdate();
}
