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

/** Generated Interface for HR_Employee_Insurance
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HR_Employee_Insurance 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_HR_Employee_Insurance.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=HR_Employee_Insurance */
    public static final String Table_Name = "HR_Employee_Insurance";

    /** AD_Table_ID=1000028 */
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

    /** Column name balance_amount */
    public static final String COLUMNNAME_balance_amount = "balance_amount";

	/** Set Balance Amount	  */
	public void setbalance_amount (BigDecimal balance_amount);

	/** Get Balance Amount	  */
	public BigDecimal getbalance_amount();

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

    /** Column name claimed_amount */
    public static final String COLUMNNAME_claimed_amount = "claimed_amount";

	/** Set Claimed Amount	  */
	public void setclaimed_amount (BigDecimal claimed_amount);

	/** Get Claimed Amount	  */
	public BigDecimal getclaimed_amount();

    /** Column name coverage_amount */
    public static final String COLUMNNAME_coverage_amount = "coverage_amount";

	/** Set Coverage Amount	  */
	public void setcoverage_amount (BigDecimal coverage_amount);

	/** Get Coverage Amount	  */
	public BigDecimal getcoverage_amount();

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

    /** Column name HR_Employee_Insurance_ID */
    public static final String COLUMNNAME_HR_Employee_Insurance_ID = "HR_Employee_Insurance_ID";

	/** Set HR_Employee_Insurance_ID	  */
	public void setHR_Employee_Insurance_ID (int HR_Employee_Insurance_ID);

	/** Get HR_Employee_Insurance_ID	  */
	public int getHR_Employee_Insurance_ID();

    /** Column name HR_Insurance_Type_ID */
    public static final String COLUMNNAME_HR_Insurance_Type_ID = "HR_Insurance_Type_ID";

	/** Set HR_Insurance_Type_ID	  */
	public void setHR_Insurance_Type_ID (int HR_Insurance_Type_ID);

	/** Get HR_Insurance_Type_ID	  */
	public int getHR_Insurance_Type_ID();

	public I_HR_Insurance_Type getHR_Insurance_Type() throws RuntimeException;

    /** Column name HR_Period_ID */
    public static final String COLUMNNAME_HR_Period_ID = "HR_Period_ID";

	/** Set Payroll Period	  */
	public void setHR_Period_ID (int HR_Period_ID);

	/** Get Payroll Period	  */
	public int getHR_Period_ID();

	public org.eevolution.model.I_HR_Period getHR_Period() throws RuntimeException;

    /** Column name insurance_plan */
    public static final String COLUMNNAME_insurance_plan = "insurance_plan";

	/** Set Insurance Plan	  */
	public void setinsurance_plan (String insurance_plan);

	/** Get Insurance Plan	  */
	public String getinsurance_plan();

    /** Column name insurance_reference */
    public static final String COLUMNNAME_insurance_reference = "insurance_reference";

	/** Set Insurance Reference Number	  */
	public void setinsurance_reference (String insurance_reference);

	/** Get Insurance Reference Number	  */
	public String getinsurance_reference();

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

    /** Column name lastpaiddate */
    public static final String COLUMNNAME_lastpaiddate = "lastpaiddate";

	/** Set Last Paid Date.
	  * Last Commission Paid Date
	  */
	public void setlastpaiddate (Timestamp lastpaiddate);

	/** Get Last Paid Date.
	  * Last Commission Paid Date
	  */
	public Timestamp getlastpaiddate();

    /** Column name lastpremiumdate */
    public static final String COLUMNNAME_lastpremiumdate = "lastpremiumdate";

	/** Set lastpremiumdate	  */
	public void setlastpremiumdate (Timestamp lastpremiumdate);

	/** Get lastpremiumdate	  */
	public Timestamp getlastpremiumdate();

    /** Column name paymentdate */
    public static final String COLUMNNAME_paymentdate = "paymentdate";

	/** Set Payment Date.
	  * Date of the Payment
	  */
	public void setpaymentdate (Timestamp paymentdate);

	/** Get Payment Date.
	  * Date of the Payment
	  */
	public Timestamp getpaymentdate();

    /** Column name paymentfrequency */
    public static final String COLUMNNAME_paymentfrequency = "paymentfrequency";

	/** Set Payment Frequency.
	  * Payment Frequency
	  */
	public void setpaymentfrequency (String paymentfrequency);

	/** Get Payment Frequency.
	  * Payment Frequency
	  */
	public String getpaymentfrequency();

    /** Column name premium_amount */
    public static final String COLUMNNAME_premium_amount = "premium_amount";

	/** Set Premium Amount	  */
	public void setpremium_amount (BigDecimal premium_amount);

	/** Get Premium Amount	  */
	public BigDecimal getpremium_amount();

    /** Column name sponsor_name */
    public static final String COLUMNNAME_sponsor_name = "sponsor_name";

	/** Set Sponsor Name	  */
	public void setsponsor_name (String sponsor_name);

	/** Get Sponsor Name	  */
	public String getsponsor_name();

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
}
