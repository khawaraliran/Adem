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
package org.eevolution.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.I_AD_User;
import org.compiere.model.I_C_Activity;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

/** Generated Interface for HR_Employee
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HR_Employee 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_HR_Employee.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=HR_Employee */
    public static final String Table_Name = "HR_Employee";

    /** AD_Table_ID=53086 */
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

	/** Set Division.
	  * Division entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Division.
	  * Division entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public I_AD_User getAD_User() throws RuntimeException;

    /** Column name blood_group */
    public static final String COLUMNNAME_blood_group = "blood_group";

	/** Set Blood Group	  */
	public void setblood_group (String blood_group);

	/** Get Blood Group	  */
	public String getblood_group();

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

    /** Column name Code */
    public static final String COLUMNNAME_Code = "Code";

	/** Set Validation code.
	  * Validation Code
	  */
	public void setCode (String Code);

	/** Get Validation code.
	  * Validation Code
	  */
	public String getCode();

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

    /** Column name daily_salary */
    public static final String COLUMNNAME_daily_salary = "daily_salary";

	/** Set daily_salary	  */
	public void setdaily_salary (BigDecimal daily_salary);

	/** Get daily_salary	  */
	public BigDecimal getdaily_salary();

    /** Column name date_of_birth */
    public static final String COLUMNNAME_date_of_birth = "date_of_birth";

	/** Set Date of Birth	  */
	public void setdate_of_birth (Timestamp date_of_birth);

	/** Get Date of Birth	  */
	public Timestamp getdate_of_birth();

    /** Column name EmpStatus */
    public static final String COLUMNNAME_EmpStatus = "EmpStatus";

	/** Set Employee Status	  */
	public void setEmpStatus (String EmpStatus);

	/** Get Employee Status	  */
	public String getEmpStatus();

    /** Column name EndDate */
    public static final String COLUMNNAME_EndDate = "EndDate";

	/** Set End Date.
	  * Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate);

	/** Get End Date.
	  * Last effective date (inclusive)
	  */
	public Timestamp getEndDate();

    /** Column name esiamt */
    public static final String COLUMNNAME_esiamt = "esiamt";

	/** Set esiamt	  */
	public void setesiamt (BigDecimal esiamt);

	/** Get esiamt	  */
	public BigDecimal getesiamt();

    /** Column name esinumber */
    public static final String COLUMNNAME_esinumber = "esinumber";

	/** Set esinumber	  */
	public void setesinumber (String esinumber);

	/** Get esinumber	  */
	public String getesinumber();

    /** Column name fathers_name */
    public static final String COLUMNNAME_fathers_name = "fathers_name";

	/** Set Father's Name	  */
	public void setfathers_name (String fathers_name);

	/** Get Father's Name	  */
	public String getfathers_name();

    /** Column name gender */
    public static final String COLUMNNAME_gender = "gender";

	/** Set Gender	  */
	public void setgender (String gender);

	/** Get Gender	  */
	public String getgender();

    /** Column name hasoptedesi */
    public static final String COLUMNNAME_hasoptedesi = "hasoptedesi";

	/** Set hasoptedesi	  */
	public void sethasoptedesi (boolean hasoptedesi);

	/** Get hasoptedesi	  */
	public boolean ishasoptedesi();

    /** Column name hasoptedpf */
    public static final String COLUMNNAME_hasoptedpf = "hasoptedpf";

	/** Set hasoptedpf	  */
	public void sethasoptedpf (boolean hasoptedpf);

	/** Get hasoptedpf	  */
	public boolean ishasoptedpf();

    /** Column name hasoptedtds */
    public static final String COLUMNNAME_hasoptedtds = "hasoptedtds";

	/** Set hasoptedtds	  */
	public void sethasoptedtds (boolean hasoptedtds);

	/** Get hasoptedtds	  */
	public boolean ishasoptedtds();

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

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee ID	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee ID	  */
	public int getHR_Employee_ID();

    /** Column name HR_Employee_Type_ID */
    public static final String COLUMNNAME_HR_Employee_Type_ID = "HR_Employee_Type_ID";

	/** Set Employee Type.
	  * Employee Type
	  */
	public void setHR_Employee_Type_ID (int HR_Employee_Type_ID);

	/** Get Employee Type.
	  * Employee Type
	  */
	public int getHR_Employee_Type_ID();

    /** Column name HR_Job_ID */
    public static final String COLUMNNAME_HR_Job_ID = "HR_Job_ID";

	/** Set Payroll Job	  */
	public void setHR_Job_ID (int HR_Job_ID);

	/** Get Payroll Job	  */
	public int getHR_Job_ID();

	public org.eevolution.model.I_HR_Job getHR_Job() throws RuntimeException;

    /** Column name HR_Payroll_ID */
    public static final String COLUMNNAME_HR_Payroll_ID = "HR_Payroll_ID";

	/** Set Payroll	  */
	public void setHR_Payroll_ID (int HR_Payroll_ID);

	/** Get Payroll	  */
	public int getHR_Payroll_ID();

	public org.eevolution.model.I_HR_Payroll getHR_Payroll() throws RuntimeException;

    /** Column name hr_race_ID */
    public static final String COLUMNNAME_hr_race_ID = "hr_race_ID";

	/** Set hr_race_ID	  */
	public void sethr_race_ID (int hr_race_ID);

	/** Get hr_race_ID	  */
	public int gethr_race_ID();

    /** Column name HR_SkillType_ID */
    public static final String COLUMNNAME_HR_SkillType_ID = "HR_SkillType_ID";

	/** Set Skill Type.
	  * Skill Type
	  */
	public void setHR_SkillType_ID (int HR_SkillType_ID);

	/** Get Skill Type.
	  * Skill Type
	  */
	public int getHR_SkillType_ID();

    /** Column name identification_mark */
    public static final String COLUMNNAME_identification_mark = "identification_mark";

	/** Set identification_mark	  */
	public void setidentification_mark (String identification_mark);

	/** Get identification_mark	  */
	public String getidentification_mark();

    /** Column name ImageURL */
    public static final String COLUMNNAME_ImageURL = "ImageURL";

	/** Set Image URL.
	  * URL of  image
	  */
	public void setImageURL (int ImageURL);

	/** Get Image URL.
	  * URL of  image
	  */
	public int getImageURL();

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

    /** Column name issalcreditedinbank */
    public static final String COLUMNNAME_issalcreditedinbank = "issalcreditedinbank";

	/** Set issalcreditedinbank	  */
	public void setissalcreditedinbank (boolean issalcreditedinbank);

	/** Get issalcreditedinbank	  */
	public boolean issalcreditedinbank();

    /** Column name Logo_ID */
    public static final String COLUMNNAME_Logo_ID = "Logo_ID";

	/** Set Logo	  */
	public void setLogo_ID (int Logo_ID);

	/** Get Logo	  */
	public int getLogo_ID();

    /** Column name marital_status */
    public static final String COLUMNNAME_marital_status = "marital_status";

	/** Set Marital Status	  */
	public void setmarital_status (String marital_status);

	/** Get Marital Status	  */
	public String getmarital_status();

    /** Column name marriage_anniversary_date */
    public static final String COLUMNNAME_marriage_anniversary_date = "marriage_anniversary_date";

	/** Set marriage_anniversary_date	  */
	public void setmarriage_anniversary_date (Timestamp marriage_anniversary_date);

	/** Get marriage_anniversary_date	  */
	public Timestamp getmarriage_anniversary_date();

    /** Column name monthly_salary */
    public static final String COLUMNNAME_monthly_salary = "monthly_salary";

	/** Set monthly_salary	  */
	public void setmonthly_salary (BigDecimal monthly_salary);

	/** Get monthly_salary	  */
	public BigDecimal getmonthly_salary();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Name2 */
    public static final String COLUMNNAME_Name2 = "Name2";

	/** Set Name 2.
	  * Additional Name
	  */
	public void setName2 (String Name2);

	/** Get Name 2.
	  * Additional Name
	  */
	public String getName2();

    /** Column name NationalCode */
    public static final String COLUMNNAME_NationalCode = "NationalCode";

	/** Set National Code	  */
	public void setNationalCode (String NationalCode);

	/** Get National Code	  */
	public String getNationalCode();

    /** Column name nationality */
    public static final String COLUMNNAME_nationality = "nationality";

	/** Set Nationality	  */
	public void setnationality (int nationality);

	/** Get Nationality	  */
	public int getnationality();

    /** Column name partners_birth_date */
    public static final String COLUMNNAME_partners_birth_date = "partners_birth_date";

	/** Set Partners Birth Date	  */
	public void setpartners_birth_date (Timestamp partners_birth_date);

	/** Get Partners Birth Date	  */
	public Timestamp getpartners_birth_date();

    /** Column name partners_name */
    public static final String COLUMNNAME_partners_name = "partners_name";

	/** Set Partner's Name	  */
	public void setpartners_name (String partners_name);

	/** Get Partner's Name	  */
	public String getpartners_name();

    /** Column name pfamt */
    public static final String COLUMNNAME_pfamt = "pfamt";

	/** Set pfamt	  */
	public void setpfamt (BigDecimal pfamt);

	/** Get pfamt	  */
	public BigDecimal getpfamt();

    /** Column name pfnumber */
    public static final String COLUMNNAME_pfnumber = "pfnumber";

	/** Set pfnumber	  */
	public void setpfnumber (String pfnumber);

	/** Get pfnumber	  */
	public String getpfnumber();

    /** Column name place_of_birth */
    public static final String COLUMNNAME_place_of_birth = "place_of_birth";

	/** Set Place of Birth	  */
	public void setplace_of_birth (String place_of_birth);

	/** Get Place of Birth	  */
	public String getplace_of_birth();

    /** Column name professionaltaxrequired */
    public static final String COLUMNNAME_professionaltaxrequired = "professionaltaxrequired";

	/** Set professionaltaxrequired	  */
	public void setprofessionaltaxrequired (boolean professionaltaxrequired);

	/** Get professionaltaxrequired	  */
	public boolean isprofessionaltaxrequired();

    /** Column name SSCode */
    public static final String COLUMNNAME_SSCode = "SSCode";

	/** Set Social Security Code	  */
	public void setSSCode (String SSCode);

	/** Get Social Security Code	  */
	public String getSSCode();

    /** Column name StartDate */
    public static final String COLUMNNAME_StartDate = "StartDate";

	/** Set Start Date.
	  * First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate);

	/** Get Start Date.
	  * First effective day (inclusive)
	  */
	public Timestamp getStartDate();

    /** Column name tdsamt */
    public static final String COLUMNNAME_tdsamt = "tdsamt";

	/** Set tdsamt	  */
	public void settdsamt (BigDecimal tdsamt);

	/** Get tdsamt	  */
	public BigDecimal gettdsamt();

    /** Column name Thumb_Image_ID */
    public static final String COLUMNNAME_Thumb_Image_ID = "Thumb_Image_ID";

	/** Set Thumb_Image_ID	  */
	public void setThumb_Image_ID (int Thumb_Image_ID);

	/** Get Thumb_Image_ID	  */
	public int getThumb_Image_ID();

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
