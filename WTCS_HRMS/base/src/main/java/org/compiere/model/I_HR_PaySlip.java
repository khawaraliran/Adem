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

/** Generated Interface for HR_PaySlip
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HR_PaySlip 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_HR_PaySlip.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=HR_PaySlip */
    public static final String Table_Name = "HR_PaySlip";

    /** AD_Table_ID=9000002 */
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

    /** Column name empattendance */
    public static final String COLUMNNAME_empattendance = "empattendance";

	/** Set empattendance	  */
	public void setempattendance (int empattendance);

	/** Get empattendance	  */
	public int getempattendance();

    /** Column name FromDate */
    public static final String COLUMNNAME_FromDate = "FromDate";

	/** Set From Date	  */
	public void setFromDate (Timestamp FromDate);

	/** Get From Date	  */
	public Timestamp getFromDate();

    /** Column name grossdeductions */
    public static final String COLUMNNAME_grossdeductions = "grossdeductions";

	/** Set grossdeductions	  */
	public void setgrossdeductions (BigDecimal grossdeductions);

	/** Get grossdeductions	  */
	public BigDecimal getgrossdeductions();

    /** Column name grossearnings */
    public static final String COLUMNNAME_grossearnings = "grossearnings";

	/** Set grossearnings	  */
	public void setgrossearnings (BigDecimal grossearnings);

	/** Get grossearnings	  */
	public BigDecimal getgrossearnings();

    /** Column name grosssalary */
    public static final String COLUMNNAME_grosssalary = "grosssalary";

	/** Set grosssalary	  */
	public void setgrosssalary (BigDecimal grosssalary);

	/** Get grosssalary	  */
	public BigDecimal getgrosssalary();

    /** Column name HR_PaySlip_ID */
    public static final String COLUMNNAME_HR_PaySlip_ID = "HR_PaySlip_ID";

	/** Set Pay slip	  */
	public void setHR_PaySlip_ID (int HR_PaySlip_ID);

	/** Get Pay slip	  */
	public int getHR_PaySlip_ID();

    /** Column name HR_Period_ID */
    public static final String COLUMNNAME_HR_Period_ID = "HR_Period_ID";

	/** Set Payroll Period	  */
	public void setHR_Period_ID (int HR_Period_ID);

	/** Get Payroll Period	  */
	public int getHR_Period_ID();

	public org.eevolution.model.I_HR_Period getHR_Period() throws RuntimeException;

    /** Column name HR_Process_ID */
    public static final String COLUMNNAME_HR_Process_ID = "HR_Process_ID";

	/** Set Payroll Process	  */
	public void setHR_Process_ID (int HR_Process_ID);

	/** Get Payroll Process	  */
	public int getHR_Process_ID();

	public org.eevolution.model.I_HR_Process getHR_Process() throws RuntimeException;

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

    /** Column name ismock */
    public static final String COLUMNNAME_ismock = "ismock";

	/** Set ismock	  */
	public void setismock (boolean ismock);

	/** Get ismock	  */
	public boolean ismock();

    /** Column name netsalary */
    public static final String COLUMNNAME_netsalary = "netsalary";

	/** Set netsalary	  */
	public void setnetsalary (BigDecimal netsalary);

	/** Get netsalary	  */
	public BigDecimal getnetsalary();

    /** Column name otherdeductions */
    public static final String COLUMNNAME_otherdeductions = "otherdeductions";

	/** Set otherdeductions	  */
	public void setotherdeductions (BigDecimal otherdeductions);

	/** Get otherdeductions	  */
	public BigDecimal getotherdeductions();

    /** Column name otherdeductionsreason */
    public static final String COLUMNNAME_otherdeductionsreason = "otherdeductionsreason";

	/** Set otherdeductionsreason	  */
	public void setotherdeductionsreason (String otherdeductionsreason);

	/** Get otherdeductionsreason	  */
	public String getotherdeductionsreason();

    /** Column name otherearnings */
    public static final String COLUMNNAME_otherearnings = "otherearnings";

	/** Set otherearnings	  */
	public void setotherearnings (BigDecimal otherearnings);

	/** Get otherearnings	  */
	public BigDecimal getotherearnings();

    /** Column name otherearningsreason */
    public static final String COLUMNNAME_otherearningsreason = "otherearningsreason";

	/** Set otherearningsreason	  */
	public void setotherearningsreason (String otherearningsreason);

	/** Get otherearningsreason	  */
	public String getotherearningsreason();

    /** Column name showpayslip */
    public static final String COLUMNNAME_showpayslip = "showpayslip";

	/** Set showpayslip	  */
	public void setshowpayslip (BigDecimal showpayslip);

	/** Get showpayslip	  */
	public BigDecimal getshowpayslip();

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

    /** Column name workingdays */
    public static final String COLUMNNAME_workingdays = "workingdays";

	/** Set workingdays	  */
	public void setworkingdays (int workingdays);

	/** Get workingdays	  */
	public int getworkingdays();
}
