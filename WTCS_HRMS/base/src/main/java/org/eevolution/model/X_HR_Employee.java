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
/** Generated Model - DO NOT CHANGE */
package org.eevolution.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for HR_Employee
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Employee.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Employee extends PO implements I_HR_Employee, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Employee.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111122L;

    /** Standard Constructor */
    public X_HR_Employee (Properties ctx, int HR_Employee_ID, String trxName)
    {
      super (ctx, HR_Employee_ID, trxName);
      /** if (HR_Employee_ID == 0)
        {
			setC_BPartner_ID (0);
			setHR_Department_ID (0);
			setHR_Employee_ID (0);
			setHR_Job_ID (0);
			setissalcreditedinbank (false);
			setStartDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_HR_Employee (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_HR_Employee[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_User getAD_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** blood_group AD_Reference_ID=1000018 */
	public static final int BLOOD_GROUP_AD_Reference_ID=1000018;
	/** A - = A - */
	public static final String BLOOD_GROUP_A_ = "A -";
	/** A + = A + */
	public static final String BLOOD_GROUP_APlus = "A +";
	/** AB - = AB - */
	public static final String BLOOD_GROUP_AB_ = "AB -";
	/** AB + = AB + */
	public static final String BLOOD_GROUP_ABPlus = "AB +";
	/** B + = B + */
	public static final String BLOOD_GROUP_BPlus = "B +";
	/** O - = O - */
	public static final String BLOOD_GROUP_O_ = "O -";
	/** O + = O + */
	public static final String BLOOD_GROUP_OPlus = "O +";
	/** Set Blood Group.
		@param blood_group Blood Group	  */
	public void setblood_group (String blood_group)
	{

		set_Value (COLUMNNAME_blood_group, blood_group);
	}

	/** Get Blood Group.
		@return Blood Group	  */
	public String getblood_group () 
	{
		return (String)get_Value(COLUMNNAME_blood_group);
	}

	public I_C_Activity getC_Activity() throws RuntimeException
    {
		return (I_C_Activity)MTable.get(getCtx(), I_C_Activity.Table_Name)
			.getPO(getC_Activity_ID(), get_TrxName());	}

	/** Set Activity.
		@param C_Activity_ID 
		Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID)
	{
		if (C_Activity_ID < 1) 
			set_Value (COLUMNNAME_C_Activity_ID, null);
		else 
			set_Value (COLUMNNAME_C_Activity_ID, Integer.valueOf(C_Activity_ID));
	}

	/** Get Activity.
		@return Business Activity
	  */
	public int getC_Activity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Activity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Employee.
		@param C_BPartner_ID 
		Identifies a Employee
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Employee.
		@return Identifies a Employee
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Validation code.
		@param Code 
		Validation Code
	  */
	public void setCode (String Code)
	{
		set_Value (COLUMNNAME_Code, Code);
	}

	/** Get Validation code.
		@return Validation Code
	  */
	public String getCode () 
	{
		return (String)get_Value(COLUMNNAME_Code);
	}

	/** Set daily_salary.
		@param daily_salary daily_salary	  */
	public void setdaily_salary (BigDecimal daily_salary)
	{
		set_Value (COLUMNNAME_daily_salary, daily_salary);
	}

	/** Get daily_salary.
		@return daily_salary	  */
	public BigDecimal getdaily_salary () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_daily_salary);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Date of Birth.
		@param date_of_birth Date of Birth	  */
	public void setdate_of_birth (Timestamp date_of_birth)
	{
		set_Value (COLUMNNAME_date_of_birth, date_of_birth);
	}

	/** Get Date of Birth.
		@return Date of Birth	  */
	public Timestamp getdate_of_birth () 
	{
		return (Timestamp)get_Value(COLUMNNAME_date_of_birth);
	}

	/** EmpStatus AD_Reference_ID=1000019 */
	public static final int EMPSTATUS_AD_Reference_ID=1000019;
	/** Without Reason = 00 */
	public static final String EMPSTATUS_WithoutReason = "00";
	/** On Leave = 01 */
	public static final String EMPSTATUS_OnLeave = "01";
	/** Left Service = 02 */
	public static final String EMPSTATUS_LeftService = "02";
	/** Retired = 03 */
	public static final String EMPSTATUS_Retired = "03";
	/** Out of Coverage = 04 */
	public static final String EMPSTATUS_OutOfCoverage = "04";
	/** Expired = 05 */
	public static final String EMPSTATUS_Expired = "05";
	/** Non Implemented Area = 06 */
	public static final String EMPSTATUS_NonImplementedArea = "06";
	/** Compliance by Immediate Ex = 07 */
	public static final String EMPSTATUS_ComplianceByImmediateEx = "07";
	/** Suspension of work = 08 */
	public static final String EMPSTATUS_SuspensionOfWork = "08";
	/** Strike/Lockout = 09 */
	public static final String EMPSTATUS_StrikeLockout = "09";
	/** Retrenchment = 10 */
	public static final String EMPSTATUS_Retrenchment = "10";
	/** No Work = 11 */
	public static final String EMPSTATUS_NoWork = "11";
	/** Doesnt Belong To This Employee = 12 */
	public static final String EMPSTATUS_DoesntBelongToThisEmployee = "12";
	/** Active = 13 */
	public static final String EMPSTATUS_Active = "13";
	/** Set Employee Status.
		@param EmpStatus Employee Status	  */
	public void setEmpStatus (String EmpStatus)
	{

		set_Value (COLUMNNAME_EmpStatus, EmpStatus);
	}

	/** Get Employee Status.
		@return Employee Status	  */
	public String getEmpStatus () 
	{
		return (String)get_Value(COLUMNNAME_EmpStatus);
	}

	/** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate)
	{
		set_Value (COLUMNNAME_EndDate, EndDate);
	}

	/** Get End Date.
		@return Last effective date (inclusive)
	  */
	public Timestamp getEndDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDate);
	}

	/** Set esiamt.
		@param esiamt esiamt	  */
	public void setesiamt (BigDecimal esiamt)
	{
		set_Value (COLUMNNAME_esiamt, esiamt);
	}

	/** Get esiamt.
		@return esiamt	  */
	public BigDecimal getesiamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_esiamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set esinumber.
		@param esinumber esinumber	  */
	public void setesinumber (String esinumber)
	{
		set_Value (COLUMNNAME_esinumber, esinumber);
	}

	/** Get esinumber.
		@return esinumber	  */
	public String getesinumber () 
	{
		return (String)get_Value(COLUMNNAME_esinumber);
	}

	/** Set Father's Name.
		@param fathers_name Father's Name	  */
	public void setfathers_name (String fathers_name)
	{
		set_Value (COLUMNNAME_fathers_name, fathers_name);
	}

	/** Get Father's Name.
		@return Father's Name	  */
	public String getfathers_name () 
	{
		return (String)get_Value(COLUMNNAME_fathers_name);
	}

	/** gender AD_Reference_ID=1000014 */
	public static final int GENDER_AD_Reference_ID=1000014;
	/** Female = Female */
	public static final String GENDER_Female = "Female";
	/** Male = Male */
	public static final String GENDER_Male = "Male";
	/** Set Gender.
		@param gender Gender	  */
	public void setgender (String gender)
	{

		set_Value (COLUMNNAME_gender, gender);
	}

	/** Get Gender.
		@return Gender	  */
	public String getgender () 
	{
		return (String)get_Value(COLUMNNAME_gender);
	}

	/** Set hasoptedesi.
		@param hasoptedesi hasoptedesi	  */
	public void sethasoptedesi (boolean hasoptedesi)
	{
		set_Value (COLUMNNAME_hasoptedesi, Boolean.valueOf(hasoptedesi));
	}

	/** Get hasoptedesi.
		@return hasoptedesi	  */
	public boolean ishasoptedesi () 
	{
		Object oo = get_Value(COLUMNNAME_hasoptedesi);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set hasoptedpf.
		@param hasoptedpf hasoptedpf	  */
	public void sethasoptedpf (boolean hasoptedpf)
	{
		set_Value (COLUMNNAME_hasoptedpf, Boolean.valueOf(hasoptedpf));
	}

	/** Get hasoptedpf.
		@return hasoptedpf	  */
	public boolean ishasoptedpf () 
	{
		Object oo = get_Value(COLUMNNAME_hasoptedpf);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set hasoptedtds.
		@param hasoptedtds hasoptedtds	  */
	public void sethasoptedtds (boolean hasoptedtds)
	{
		set_Value (COLUMNNAME_hasoptedtds, Boolean.valueOf(hasoptedtds));
	}

	/** Get hasoptedtds.
		@return hasoptedtds	  */
	public boolean ishasoptedtds () 
	{
		Object oo = get_Value(COLUMNNAME_hasoptedtds);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.eevolution.model.I_HR_Department getHR_Department() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Department)MTable.get(getCtx(), org.eevolution.model.I_HR_Department.Table_Name)
			.getPO(getHR_Department_ID(), get_TrxName());	}

	/** Set Department.
		@param HR_Department_ID 
		Department Name
	  */
	public void setHR_Department_ID (int HR_Department_ID)
	{
		if (HR_Department_ID < 1) 
			set_Value (COLUMNNAME_HR_Department_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Department_ID, Integer.valueOf(HR_Department_ID));
	}

	/** Get Department.
		@return Department Name
	  */
	public int getHR_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Employee ID.
		@param HR_Employee_ID Employee ID	  */
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
	}

	/** Get Employee ID.
		@return Employee ID	  */
	public int getHR_Employee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Employee Type.
		@param HR_Employee_Type_ID 
		Employee Type
	  */
	public void setHR_Employee_Type_ID (int HR_Employee_Type_ID)
	{
		if (HR_Employee_Type_ID < 1) 
			set_Value (COLUMNNAME_HR_Employee_Type_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Employee_Type_ID, Integer.valueOf(HR_Employee_Type_ID));
	}

	/** Get Employee Type.
		@return Employee Type
	  */
	public int getHR_Employee_Type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_Type_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_HR_Job getHR_Job() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Job)MTable.get(getCtx(), org.eevolution.model.I_HR_Job.Table_Name)
			.getPO(getHR_Job_ID(), get_TrxName());	}

	/** Set Payroll Job.
		@param HR_Job_ID Payroll Job	  */
	public void setHR_Job_ID (int HR_Job_ID)
	{
		if (HR_Job_ID < 1) 
			set_Value (COLUMNNAME_HR_Job_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Job_ID, Integer.valueOf(HR_Job_ID));
	}

	/** Get Payroll Job.
		@return Payroll Job	  */
	public int getHR_Job_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Job_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_HR_Payroll getHR_Payroll() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Payroll)MTable.get(getCtx(), org.eevolution.model.I_HR_Payroll.Table_Name)
			.getPO(getHR_Payroll_ID(), get_TrxName());	}

	/** Set Payroll.
		@param HR_Payroll_ID Payroll	  */
	public void setHR_Payroll_ID (int HR_Payroll_ID)
	{
		if (HR_Payroll_ID < 1) 
			set_Value (COLUMNNAME_HR_Payroll_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Payroll_ID, Integer.valueOf(HR_Payroll_ID));
	}

	/** Get Payroll.
		@return Payroll	  */
	public int getHR_Payroll_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Payroll_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set hr_race_ID.
		@param hr_race_ID hr_race_ID	  */
	public void sethr_race_ID (int hr_race_ID)
	{
		if (hr_race_ID < 1) 
			set_Value (COLUMNNAME_hr_race_ID, null);
		else 
			set_Value (COLUMNNAME_hr_race_ID, Integer.valueOf(hr_race_ID));
	}

	/** Get hr_race_ID.
		@return hr_race_ID	  */
	public int gethr_race_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_hr_race_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Skill Type.
		@param HR_SkillType_ID 
		Skill Type
	  */
	public void setHR_SkillType_ID (int HR_SkillType_ID)
	{
		if (HR_SkillType_ID < 1) 
			set_Value (COLUMNNAME_HR_SkillType_ID, null);
		else 
			set_Value (COLUMNNAME_HR_SkillType_ID, Integer.valueOf(HR_SkillType_ID));
	}

	/** Get Skill Type.
		@return Skill Type
	  */
	public int getHR_SkillType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_SkillType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set identification_mark.
		@param identification_mark identification_mark	  */
	public void setidentification_mark (String identification_mark)
	{
		set_Value (COLUMNNAME_identification_mark, identification_mark);
	}

	/** Get identification_mark.
		@return identification_mark	  */
	public String getidentification_mark () 
	{
		return (String)get_Value(COLUMNNAME_identification_mark);
	}

	/** Set Image URL.
		@param ImageURL 
		URL of  image
	  */
	public void setImageURL (int ImageURL)
	{
		set_Value (COLUMNNAME_ImageURL, Integer.valueOf(ImageURL));
	}

	/** Get Image URL.
		@return URL of  image
	  */
	public int getImageURL () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ImageURL);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set issalcreditedinbank.
		@param issalcreditedinbank issalcreditedinbank	  */
	public void setissalcreditedinbank (boolean issalcreditedinbank)
	{
		set_Value (COLUMNNAME_issalcreditedinbank, Boolean.valueOf(issalcreditedinbank));
	}

	/** Get issalcreditedinbank.
		@return issalcreditedinbank	  */
	public boolean issalcreditedinbank () 
	{
		Object oo = get_Value(COLUMNNAME_issalcreditedinbank);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Logo.
		@param Logo_ID Logo	  */
	public void setLogo_ID (int Logo_ID)
	{
		if (Logo_ID < 1) 
			set_Value (COLUMNNAME_Logo_ID, null);
		else 
			set_Value (COLUMNNAME_Logo_ID, Integer.valueOf(Logo_ID));
	}

	/** Get Logo.
		@return Logo	  */
	public int getLogo_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Logo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** marital_status AD_Reference_ID=1000016 */
	public static final int MARITAL_STATUS_AD_Reference_ID=1000016;
	/** Divorced = Divorced */
	public static final String MARITAL_STATUS_Divorced = "Divorced";
	/** Live-in = Live-in */
	public static final String MARITAL_STATUS_Live_In = "Live-in";
	/** Married = Married */
	public static final String MARITAL_STATUS_Married = "Married";
	/** Single = Single */
	public static final String MARITAL_STATUS_Single = "Single";
	/** Widow = Widow */
	public static final String MARITAL_STATUS_Widow = "Widow";
	/** Windower = Windower */
	public static final String MARITAL_STATUS_Windower = "Windower";
	/** Set Marital Status.
		@param marital_status Marital Status	  */
	public void setmarital_status (String marital_status)
	{

		set_Value (COLUMNNAME_marital_status, marital_status);
	}

	/** Get Marital Status.
		@return Marital Status	  */
	public String getmarital_status () 
	{
		return (String)get_Value(COLUMNNAME_marital_status);
	}

	/** Set marriage_anniversary_date.
		@param marriage_anniversary_date marriage_anniversary_date	  */
	public void setmarriage_anniversary_date (Timestamp marriage_anniversary_date)
	{
		set_Value (COLUMNNAME_marriage_anniversary_date, marriage_anniversary_date);
	}

	/** Get marriage_anniversary_date.
		@return marriage_anniversary_date	  */
	public Timestamp getmarriage_anniversary_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_marriage_anniversary_date);
	}

	/** Set monthly_salary.
		@param monthly_salary monthly_salary	  */
	public void setmonthly_salary (BigDecimal monthly_salary)
	{
		set_Value (COLUMNNAME_monthly_salary, monthly_salary);
	}

	/** Get monthly_salary.
		@return monthly_salary	  */
	public BigDecimal getmonthly_salary () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_monthly_salary);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Name 2.
		@param Name2 
		Additional Name
	  */
	public void setName2 (String Name2)
	{
		set_Value (COLUMNNAME_Name2, Name2);
	}

	/** Get Name 2.
		@return Additional Name
	  */
	public String getName2 () 
	{
		return (String)get_Value(COLUMNNAME_Name2);
	}

	/** Set National Code.
		@param NationalCode National Code	  */
	public void setNationalCode (String NationalCode)
	{
		set_Value (COLUMNNAME_NationalCode, NationalCode);
	}

	/** Get National Code.
		@return National Code	  */
	public String getNationalCode () 
	{
		return (String)get_Value(COLUMNNAME_NationalCode);
	}

	/** Set Nationality.
		@param nationality Nationality	  */
	public void setnationality (int nationality)
	{
		set_Value (COLUMNNAME_nationality, Integer.valueOf(nationality));
	}

	/** Get Nationality.
		@return Nationality	  */
	public int getnationality () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_nationality);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Partners Birth Date.
		@param partners_birth_date Partners Birth Date	  */
	public void setpartners_birth_date (Timestamp partners_birth_date)
	{
		set_Value (COLUMNNAME_partners_birth_date, partners_birth_date);
	}

	/** Get Partners Birth Date.
		@return Partners Birth Date	  */
	public Timestamp getpartners_birth_date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_partners_birth_date);
	}

	/** Set Partner's Name.
		@param partners_name Partner's Name	  */
	public void setpartners_name (String partners_name)
	{
		set_Value (COLUMNNAME_partners_name, partners_name);
	}

	/** Get Partner's Name.
		@return Partner's Name	  */
	public String getpartners_name () 
	{
		return (String)get_Value(COLUMNNAME_partners_name);
	}

	/** Set pfamt.
		@param pfamt pfamt	  */
	public void setpfamt (BigDecimal pfamt)
	{
		set_Value (COLUMNNAME_pfamt, pfamt);
	}

	/** Get pfamt.
		@return pfamt	  */
	public BigDecimal getpfamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_pfamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set pfnumber.
		@param pfnumber pfnumber	  */
	public void setpfnumber (String pfnumber)
	{
		set_Value (COLUMNNAME_pfnumber, pfnumber);
	}

	/** Get pfnumber.
		@return pfnumber	  */
	public String getpfnumber () 
	{
		return (String)get_Value(COLUMNNAME_pfnumber);
	}

	/** Set Place of Birth.
		@param place_of_birth Place of Birth	  */
	public void setplace_of_birth (String place_of_birth)
	{
		set_Value (COLUMNNAME_place_of_birth, place_of_birth);
	}

	/** Get Place of Birth.
		@return Place of Birth	  */
	public String getplace_of_birth () 
	{
		return (String)get_Value(COLUMNNAME_place_of_birth);
	}

	/** Set professionaltaxrequired.
		@param professionaltaxrequired professionaltaxrequired	  */
	public void setprofessionaltaxrequired (boolean professionaltaxrequired)
	{
		set_Value (COLUMNNAME_professionaltaxrequired, Boolean.valueOf(professionaltaxrequired));
	}

	/** Get professionaltaxrequired.
		@return professionaltaxrequired	  */
	public boolean isprofessionaltaxrequired () 
	{
		Object oo = get_Value(COLUMNNAME_professionaltaxrequired);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Social Security Code.
		@param SSCode Social Security Code	  */
	public void setSSCode (String SSCode)
	{
		set_Value (COLUMNNAME_SSCode, SSCode);
	}

	/** Get Social Security Code.
		@return Social Security Code	  */
	public String getSSCode () 
	{
		return (String)get_Value(COLUMNNAME_SSCode);
	}

	/** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
	}

	/** Set tdsamt.
		@param tdsamt tdsamt	  */
	public void settdsamt (BigDecimal tdsamt)
	{
		set_Value (COLUMNNAME_tdsamt, tdsamt);
	}

	/** Get tdsamt.
		@return tdsamt	  */
	public BigDecimal gettdsamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_tdsamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Thumb_Image_ID.
		@param Thumb_Image_ID Thumb_Image_ID	  */
	public void setThumb_Image_ID (int Thumb_Image_ID)
	{
		if (Thumb_Image_ID < 1) 
			set_Value (COLUMNNAME_Thumb_Image_ID, null);
		else 
			set_Value (COLUMNNAME_Thumb_Image_ID, Integer.valueOf(Thumb_Image_ID));
	}

	/** Get Thumb_Image_ID.
		@return Thumb_Image_ID	  */
	public int getThumb_Image_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Thumb_Image_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}