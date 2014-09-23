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

/** Generated Interface for C_BPartner
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_C_BPartner 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_C_BPartner.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=C_BPartner */
    public static final String Table_Name = "C_BPartner";

    /** AD_Table_ID=291 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AcqusitionCost */
    public static final String COLUMNNAME_AcqusitionCost = "AcqusitionCost";

	/** Set Acquisition Cost.
	  * The cost of gaining the prospect as a customer
	  */
	public void setAcqusitionCost (BigDecimal AcqusitionCost);

	/** Get Acquisition Cost.
	  * The cost of gaining the prospect as a customer
	  */
	public BigDecimal getAcqusitionCost();

    /** Column name ActualLifeTimeValue */
    public static final String COLUMNNAME_ActualLifeTimeValue = "ActualLifeTimeValue";

	/** Set Actual Life Time Value.
	  * Actual Life Time Revenue
	  */
	public void setActualLifeTimeValue (BigDecimal ActualLifeTimeValue);

	/** Get Actual Life Time Value.
	  * Actual Life Time Revenue
	  */
	public BigDecimal getActualLifeTimeValue();

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Language */
    public static final String COLUMNNAME_AD_Language = "AD_Language";

	/** Set Language.
	  * Language for this entity
	  */
	public void setAD_Language (String AD_Language);

	/** Get Language.
	  * Language for this entity
	  */
	public String getAD_Language();

    /** Column name AD_OrgBP_ID */
    public static final String COLUMNNAME_AD_OrgBP_ID = "AD_OrgBP_ID";

	/** Set Linked Organization.
	  * The Business Partner is another Organization for explicit Inter-Org transactions
	  */
	public void setAD_OrgBP_ID (String AD_OrgBP_ID);

	/** Get Linked Organization.
	  * The Business Partner is another Organization for explicit Inter-Org transactions
	  */
	public String getAD_OrgBP_ID();

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

    /** Column name agent_ID */
    public static final String COLUMNNAME_agent_ID = "agent_ID";

	/** Set Agent Name.
	  * Agent Name
	  */
	public void setagent_ID (int agent_ID);

	/** Get Agent Name.
	  * Agent Name
	  */
	public int getagent_ID();

	public I_C_BPartner getagent() throws RuntimeException;

    /** Column name BPartner_Parent_ID */
    public static final String COLUMNNAME_BPartner_Parent_ID = "BPartner_Parent_ID";

	/** Set Partner Parent.
	  * Business Partner Parent
	  */
	public void setBPartner_Parent_ID (int BPartner_Parent_ID);

	/** Get Partner Parent.
	  * Business Partner Parent
	  */
	public int getBPartner_Parent_ID();

	public I_C_BPartner getBPartner_Parent() throws RuntimeException;

    /** Column name buyercode */
    public static final String COLUMNNAME_buyercode = "buyercode";

	/** Set buyercode	  */
	public void setbuyercode (String buyercode);

	/** Get buyercode	  */
	public String getbuyercode();

    /** Column name buyertype */
    public static final String COLUMNNAME_buyertype = "buyertype";

	/** Set buyertype	  */
	public void setbuyertype (String buyertype);

	/** Get buyertype	  */
	public String getbuyertype();

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner.
	  * Identifies a Employee
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner.
	  * Identifies a Employee
	  */
	public int getC_BPartner_ID();

    /** Column name C_BP_Group_ID */
    public static final String COLUMNNAME_C_BP_Group_ID = "C_BP_Group_ID";

	/** Set Business Partner Group.
	  * Business Partner Group
	  */
	public void setC_BP_Group_ID (int C_BP_Group_ID);

	/** Get Business Partner Group.
	  * Business Partner Group
	  */
	public int getC_BP_Group_ID();

	public I_C_BP_Group getC_BP_Group() throws RuntimeException;

    /** Column name C_Dunning_ID */
    public static final String COLUMNNAME_C_Dunning_ID = "C_Dunning_ID";

	/** Set Dunning.
	  * Dunning Rules for overdue invoices
	  */
	public void setC_Dunning_ID (int C_Dunning_ID);

	/** Get Dunning.
	  * Dunning Rules for overdue invoices
	  */
	public int getC_Dunning_ID();

	public I_C_Dunning getC_Dunning() throws RuntimeException;

    /** Column name C_Greeting_ID */
    public static final String COLUMNNAME_C_Greeting_ID = "C_Greeting_ID";

	/** Set Greeting.
	  * Greeting to print on correspondence
	  */
	public void setC_Greeting_ID (int C_Greeting_ID);

	/** Get Greeting.
	  * Greeting to print on correspondence
	  */
	public int getC_Greeting_ID();

	public I_C_Greeting getC_Greeting() throws RuntimeException;

    /** Column name C_InvoiceSchedule_ID */
    public static final String COLUMNNAME_C_InvoiceSchedule_ID = "C_InvoiceSchedule_ID";

	/** Set Invoice Schedule.
	  * Schedule for generating Invoices
	  */
	public void setC_InvoiceSchedule_ID (int C_InvoiceSchedule_ID);

	/** Get Invoice Schedule.
	  * Schedule for generating Invoices
	  */
	public int getC_InvoiceSchedule_ID();

	public I_C_InvoiceSchedule getC_InvoiceSchedule() throws RuntimeException;

    /** Column name CommissionAmt */
    public static final String COLUMNNAME_CommissionAmt = "CommissionAmt";

	/** Set Commission Amount.
	  * Commission Amount
	  */
	public void setCommissionAmt (String CommissionAmt);

	/** Get Commission Amount.
	  * Commission Amount
	  */
	public String getCommissionAmt();

    /** Column name C_PaymentTerm_ID */
    public static final String COLUMNNAME_C_PaymentTerm_ID = "C_PaymentTerm_ID";

	/** Set Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID (int C_PaymentTerm_ID);

	/** Get Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID();

	public I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException;

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

    /** Column name createuser */
    public static final String COLUMNNAME_createuser = "createuser";

	/** Set Create / Update User Details.
	  * create or udpate user details of the employee
	  */
	public void setcreateuser (String createuser);

	/** Get Create / Update User Details.
	  * create or udpate user details of the employee
	  */
	public String getcreateuser();

    /** Column name C_TaxGroup_ID */
    public static final String COLUMNNAME_C_TaxGroup_ID = "C_TaxGroup_ID";

	/** Set Tax Group	  */
	public void setC_TaxGroup_ID (int C_TaxGroup_ID);

	/** Get Tax Group	  */
	public int getC_TaxGroup_ID();

	public org.eevolution.model.I_C_TaxGroup getC_TaxGroup() throws RuntimeException;

    /** Column name DeliveryRule */
    public static final String COLUMNNAME_DeliveryRule = "DeliveryRule";

	/** Set Delivery Rule.
	  * Defines the timing of Delivery
	  */
	public void setDeliveryRule (String DeliveryRule);

	/** Get Delivery Rule.
	  * Defines the timing of Delivery
	  */
	public String getDeliveryRule();

    /** Column name DeliveryViaRule */
    public static final String COLUMNNAME_DeliveryViaRule = "DeliveryViaRule";

	/** Set Delivery Via.
	  * How the order will be delivered
	  */
	public void setDeliveryViaRule (String DeliveryViaRule);

	/** Get Delivery Via.
	  * How the order will be delivered
	  */
	public String getDeliveryViaRule();

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

    /** Column name division_ID */
    public static final String COLUMNNAME_division_ID = "division_ID";

	/** Set Division / Branch.
	  * Division / Branch
	  */
	public void setdivision_ID (int division_ID);

	/** Get Division / Branch.
	  * Division / Branch
	  */
	public int getdivision_ID();

    /** Column name DocumentCopies */
    public static final String COLUMNNAME_DocumentCopies = "DocumentCopies";

	/** Set Document Copies.
	  * Number of copies to be printed
	  */
	public void setDocumentCopies (int DocumentCopies);

	/** Get Document Copies.
	  * Number of copies to be printed
	  */
	public int getDocumentCopies();

    /** Column name DunningGrace */
    public static final String COLUMNNAME_DunningGrace = "DunningGrace";

	/** Set Dunning Grace Date	  */
	public void setDunningGrace (Timestamp DunningGrace);

	/** Get Dunning Grace Date	  */
	public Timestamp getDunningGrace();

    /** Column name DUNS */
    public static final String COLUMNNAME_DUNS = "DUNS";

	/** Set D-U-N-S.
	  * Dun & Bradstreet Number
	  */
	public void setDUNS (String DUNS);

	/** Get D-U-N-S.
	  * Dun & Bradstreet Number
	  */
	public String getDUNS();

    /** Column name ecccode */
    public static final String COLUMNNAME_ecccode = "ecccode";

	/** Set ECC Code	  */
	public void setecccode (String ecccode);

	/** Get ECC Code	  */
	public String getecccode();

    /** Column name eccdivision */
    public static final String COLUMNNAME_eccdivision = "eccdivision";

	/** Set eccdivision	  */
	public void seteccdivision (String eccdivision);

	/** Get eccdivision	  */
	public String geteccdivision();

    /** Column name eccrange */
    public static final String COLUMNNAME_eccrange = "eccrange";

	/** Set eccrange	  */
	public void seteccrange (String eccrange);

	/** Get eccrange	  */
	public String geteccrange();

    /** Column name EMail */
    public static final String COLUMNNAME_EMail = "EMail";

	/** Set EMail Address.
	  * Electronic Mail Address
	  */
	public void setEMail (String EMail);

	/** Get EMail Address.
	  * Electronic Mail Address
	  */
	public String getEMail();

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

    /** Column name employeestatus */
    public static final String COLUMNNAME_employeestatus = "employeestatus";

	/** Set Employee Status.
	  * Status of the Employee
	  */
	public void setemployeestatus (String employeestatus);

	/** Get Employee Status.
	  * Status of the Employee
	  */
	public String getemployeestatus();

    /** Column name FirstName1 */
    public static final String COLUMNNAME_FirstName1 = "FirstName1";

	/** Set First Name 1	  */
	public void setFirstName1 (String FirstName1);

	/** Get First Name 1	  */
	public String getFirstName1();

    /** Column name FirstName2 */
    public static final String COLUMNNAME_FirstName2 = "FirstName2";

	/** Set First Name 2	  */
	public void setFirstName2 (String FirstName2);

	/** Get First Name 2	  */
	public String getFirstName2();

    /** Column name FirstSale */
    public static final String COLUMNNAME_FirstSale = "FirstSale";

	/** Set First Sale.
	  * Date of First Sale
	  */
	public void setFirstSale (Timestamp FirstSale);

	/** Get First Sale.
	  * Date of First Sale
	  */
	public Timestamp getFirstSale();

    /** Column name FlatDiscount */
    public static final String COLUMNNAME_FlatDiscount = "FlatDiscount";

	/** Set Flat Discount %.
	  * Flat discount percentage 
	  */
	public void setFlatDiscount (BigDecimal FlatDiscount);

	/** Get Flat Discount %.
	  * Flat discount percentage 
	  */
	public BigDecimal getFlatDiscount();

    /** Column name FreightCostRule */
    public static final String COLUMNNAME_FreightCostRule = "FreightCostRule";

	/** Set Freight Cost Rule.
	  * Method for charging Freight
	  */
	public void setFreightCostRule (String FreightCostRule);

	/** Get Freight Cost Rule.
	  * Method for charging Freight
	  */
	public String getFreightCostRule();

    /** Column name HR_Block_ID */
    public static final String COLUMNNAME_HR_Block_ID = "HR_Block_ID";

	/** Set Block Name.
	  * Block Name
	  */
	public void setHR_Block_ID (int HR_Block_ID);

	/** Get Block Name.
	  * Block Name
	  */
	public int getHR_Block_ID();

    /** Column name hrcommamt */
    public static final String COLUMNNAME_hrcommamt = "hrcommamt";

	/** Set Commission Amount	  */
	public void sethrcommamt (BigDecimal hrcommamt);

	/** Get Commission Amount	  */
	public BigDecimal gethrcommamt();

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

    /** Column name HR_Designation_ID */
    public static final String COLUMNNAME_HR_Designation_ID = "HR_Designation_ID";

	/** Set Designation.
	  * Designation
	  */
	public void setHR_Designation_ID (int HR_Designation_ID);

	/** Get Designation.
	  * Designation
	  */
	public int getHR_Designation_ID();

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Employee Name	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Employee Name	  */
	public int getHR_Employee_ID();

	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException;

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

    /** Column name HR_Grade_ID */
    public static final String COLUMNNAME_HR_Grade_ID = "HR_Grade_ID";

	/** Set Grade.
	  * Grade
	  */
	public void setHR_Grade_ID (int HR_Grade_ID);

	/** Get Grade.
	  * Grade
	  */
	public int getHR_Grade_ID();

    /** Column name HR_Quarter_ID */
    public static final String COLUMNNAME_HR_Quarter_ID = "HR_Quarter_ID";

	/** Set Quarter.
	  * Quarter
	  */
	public void setHR_Quarter_ID (int HR_Quarter_ID);

	/** Get Quarter.
	  * Quarter
	  */
	public int getHR_Quarter_ID();

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

    /** Column name iecnumber */
    public static final String COLUMNNAME_iecnumber = "iecnumber";

	/** Set iecnumber	  */
	public void setiecnumber (String iecnumber);

	/** Get iecnumber	  */
	public String getiecnumber();

    /** Column name Invoice_PrintFormat_ID */
    public static final String COLUMNNAME_Invoice_PrintFormat_ID = "Invoice_PrintFormat_ID";

	/** Set Invoice Print Format.
	  * Print Format for printing Invoices
	  */
	public void setInvoice_PrintFormat_ID (int Invoice_PrintFormat_ID);

	/** Get Invoice Print Format.
	  * Print Format for printing Invoices
	  */
	public int getInvoice_PrintFormat_ID();

	public I_AD_PrintFormat getInvoice_PrintFormat() throws RuntimeException;

    /** Column name InvoiceRule */
    public static final String COLUMNNAME_InvoiceRule = "InvoiceRule";

	/** Set Invoice Rule.
	  * Frequency and method of invoicing 
	  */
	public void setInvoiceRule (String InvoiceRule);

	/** Get Invoice Rule.
	  * Frequency and method of invoicing 
	  */
	public String getInvoiceRule();

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

    /** Column name iscollectionagent */
    public static final String COLUMNNAME_iscollectionagent = "iscollectionagent";

	/** Set iscollectionagent	  */
	public void setiscollectionagent (boolean iscollectionagent);

	/** Get iscollectionagent	  */
	public boolean iscollectionagent();

    /** Column name iscompanyweekpolicy */
    public static final String COLUMNNAME_iscompanyweekpolicy = "iscompanyweekpolicy";

	/** Set Company week policy	  */
	public void setiscompanyweekpolicy (boolean iscompanyweekpolicy);

	/** Get Company week policy	  */
	public boolean iscompanyweekpolicy();

    /** Column name isconsginmentagent */
    public static final String COLUMNNAME_isconsginmentagent = "isconsginmentagent";

	/** Set isconsginmentagent	  */
	public void setisconsginmentagent (boolean isconsginmentagent);

	/** Get isconsginmentagent	  */
	public boolean isconsginmentagent();

    /** Column name IsCustomer */
    public static final String COLUMNNAME_IsCustomer = "IsCustomer";

	/** Set Customer.
	  * Indicates if this Business Partner is a Customer
	  */
	public void setIsCustomer (boolean IsCustomer);

	/** Get Customer.
	  * Indicates if this Business Partner is a Customer
	  */
	public boolean isCustomer();

    /** Column name IsDetailedNames */
    public static final String COLUMNNAME_IsDetailedNames = "IsDetailedNames";

	/** Set Detailed Names	  */
	public void setIsDetailedNames (boolean IsDetailedNames);

	/** Get Detailed Names	  */
	public boolean isDetailedNames();

    /** Column name IsDiscountPrinted */
    public static final String COLUMNNAME_IsDiscountPrinted = "IsDiscountPrinted";

	/** Set Discount Printed.
	  * Print Discount on Invoice and Order
	  */
	public void setIsDiscountPrinted (boolean IsDiscountPrinted);

	/** Get Discount Printed.
	  * Print Discount on Invoice and Order
	  */
	public boolean isDiscountPrinted();

    /** Column name IsEmployee */
    public static final String COLUMNNAME_IsEmployee = "IsEmployee";

	/** Set Employee.
	  * Indicates if  this Business Partner is an employee
	  */
	public void setIsEmployee (boolean IsEmployee);

	/** Get Employee.
	  * Indicates if  this Business Partner is an employee
	  */
	public boolean isEmployee();

    /** Column name isgeneralsupplier */
    public static final String COLUMNNAME_isgeneralsupplier = "isgeneralsupplier";

	/** Set isgeneralsupplier	  */
	public void setisgeneralsupplier (boolean isgeneralsupplier);

	/** Get isgeneralsupplier	  */
	public boolean isgeneralsupplier();

    /** Column name IsManufacturer */
    public static final String COLUMNNAME_IsManufacturer = "IsManufacturer";

	/** Set Is Manufacturer.
	  * Indicate role of this Business partner as Manufacturer
	  */
	public void setIsManufacturer (boolean IsManufacturer);

	/** Get Is Manufacturer.
	  * Indicate role of this Business partner as Manufacturer
	  */
	public boolean isManufacturer();

    /** Column name IsOneTime */
    public static final String COLUMNNAME_IsOneTime = "IsOneTime";

	/** Set One time transaction	  */
	public void setIsOneTime (boolean IsOneTime);

	/** Get One time transaction	  */
	public boolean isOneTime();

    /** Column name IsPOTaxExempt */
    public static final String COLUMNNAME_IsPOTaxExempt = "IsPOTaxExempt";

	/** Set PO Tax exempt.
	  * Business partner is exempt from tax on purchases
	  */
	public void setIsPOTaxExempt (boolean IsPOTaxExempt);

	/** Get PO Tax exempt.
	  * Business partner is exempt from tax on purchases
	  */
	public boolean isPOTaxExempt();

    /** Column name IsProspect */
    public static final String COLUMNNAME_IsProspect = "IsProspect";

	/** Set Prospect.
	  * Indicates this is a Prospect
	  */
	public void setIsProspect (boolean IsProspect);

	/** Get Prospect.
	  * Indicates this is a Prospect
	  */
	public boolean isProspect();

    /** Column name ispurchaseagent */
    public static final String COLUMNNAME_ispurchaseagent = "ispurchaseagent";

	/** Set Purchase Agent ?.
	  * Purchase Agent
	  */
	public void setispurchaseagent (boolean ispurchaseagent);

	/** Get Purchase Agent ?.
	  * Purchase Agent
	  */
	public boolean ispurchaseagent();

    /** Column name israwmaterialsupplier */
    public static final String COLUMNNAME_israwmaterialsupplier = "israwmaterialsupplier";

	/** Set israwmaterialsupplier	  */
	public void setisrawmaterialsupplier (boolean israwmaterialsupplier);

	/** Get israwmaterialsupplier	  */
	public boolean israwmaterialsupplier();

    /** Column name isresourceagent */
    public static final String COLUMNNAME_isresourceagent = "isresourceagent";

	/** Set Resource Agent ?.
	  * Resource Agent
	  */
	public void setisresourceagent (boolean isresourceagent);

	/** Get Resource Agent ?.
	  * Resource Agent
	  */
	public boolean isresourceagent();

    /** Column name issaleagent */
    public static final String COLUMNNAME_issaleagent = "issaleagent";

	/** Set Sale Agent ?.
	  * Sale Agent
	  */
	public void setissaleagent (boolean issaleagent);

	/** Get Sale Agent ?.
	  * Sale Agent
	  */
	public boolean issaleagent();

    /** Column name IsSalesRep */
    public static final String COLUMNNAME_IsSalesRep = "IsSalesRep";

	/** Set Sales Representative.
	  * Indicates if  the business partner is a sales representative or company agent
	  */
	public void setIsSalesRep (boolean IsSalesRep);

	/** Get Sales Representative.
	  * Indicates if  the business partner is a sales representative or company agent
	  */
	public boolean isSalesRep();

    /** Column name isstoresupplier */
    public static final String COLUMNNAME_isstoresupplier = "isstoresupplier";

	/** Set isstoresupplier	  */
	public void setisstoresupplier (boolean isstoresupplier);

	/** Get isstoresupplier	  */
	public boolean isstoresupplier();

    /** Column name IsSummary */
    public static final String COLUMNNAME_IsSummary = "IsSummary";

	/** Set Summary Level.
	  * This is a summary entity
	  */
	public void setIsSummary (boolean IsSummary);

	/** Get Summary Level.
	  * This is a summary entity
	  */
	public boolean isSummary();

    /** Column name IsTaxExempt */
    public static final String COLUMNNAME_IsTaxExempt = "IsTaxExempt";

	/** Set SO Tax exempt.
	  * Business partner is exempt from tax on sales
	  */
	public void setIsTaxExempt (boolean IsTaxExempt);

	/** Get SO Tax exempt.
	  * Business partner is exempt from tax on sales
	  */
	public boolean isTaxExempt();

    /** Column name istdsapplicable */
    public static final String COLUMNNAME_istdsapplicable = "istdsapplicable";

	/** Set IS TDS Applicable.
	  * Is TDS applicable for the Business Partner
	  */
	public void setistdsapplicable (boolean istdsapplicable);

	/** Get IS TDS Applicable.
	  * Is TDS applicable for the Business Partner
	  */
	public boolean istdsapplicable();

    /** Column name istransporter */
    public static final String COLUMNNAME_istransporter = "istransporter";

	/** Set istransporter	  */
	public void setistransporter (boolean istransporter);

	/** Get istransporter	  */
	public boolean istransporter();

    /** Column name IsUseTaxIdDigit */
    public static final String COLUMNNAME_IsUseTaxIdDigit = "IsUseTaxIdDigit";

	/** Set Use Tax Id Digit	  */
	public void setIsUseTaxIdDigit (boolean IsUseTaxIdDigit);

	/** Get Use Tax Id Digit	  */
	public boolean isUseTaxIdDigit();

    /** Column name IsVendor */
    public static final String COLUMNNAME_IsVendor = "IsVendor";

	/** Set Vendor.
	  * Indicates if this Business Partner is a Vendor
	  */
	public void setIsVendor (boolean IsVendor);

	/** Get Vendor.
	  * Indicates if this Business Partner is a Vendor
	  */
	public boolean isVendor();

    /** Column name joiningdate */
    public static final String COLUMNNAME_joiningdate = "joiningdate";

	/** Set Joining Date.
	  * Joining Date
	  */
	public void setjoiningdate (Timestamp joiningdate);

	/** Get Joining Date.
	  * Joining Date
	  */
	public Timestamp getjoiningdate();

    /** Column name LastName1 */
    public static final String COLUMNNAME_LastName1 = "LastName1";

	/** Set Last Name 1	  */
	public void setLastName1 (String LastName1);

	/** Get Last Name 1	  */
	public String getLastName1();

    /** Column name LastName2 */
    public static final String COLUMNNAME_LastName2 = "LastName2";

	/** Set Last Name 2	  */
	public void setLastName2 (String LastName2);

	/** Get Last Name 2	  */
	public String getLastName2();

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

    /** Column name LCO_ISIC_ID */
    public static final String COLUMNNAME_LCO_ISIC_ID = "LCO_ISIC_ID";

	/** Set ISIC	  */
	public void setLCO_ISIC_ID (int LCO_ISIC_ID);

	/** Get ISIC	  */
	public int getLCO_ISIC_ID();

    /** Column name LCO_TaxIdType_ID */
    public static final String COLUMNNAME_LCO_TaxIdType_ID = "LCO_TaxIdType_ID";

	/** Set Tax ID Type	  */
	public void setLCO_TaxIdType_ID (int LCO_TaxIdType_ID);

	/** Get Tax ID Type	  */
	public int getLCO_TaxIdType_ID();

    /** Column name LCO_TaxPayerType_ID */
    public static final String COLUMNNAME_LCO_TaxPayerType_ID = "LCO_TaxPayerType_ID";

	/** Set Deductee Type.
	  * TDS deduction type for the Business Partner
	  */
	public void setLCO_TaxPayerType_ID (int LCO_TaxPayerType_ID);

	/** Get Deductee Type.
	  * TDS deduction type for the Business Partner
	  */
	public int getLCO_TaxPayerType_ID();

    /** Column name Logo_ID */
    public static final String COLUMNNAME_Logo_ID = "Logo_ID";

	/** Set Logo	  */
	public void setLogo_ID (int Logo_ID);

	/** Get Logo	  */
	public int getLogo_ID();

    /** Column name M_DiscountSchema_ID */
    public static final String COLUMNNAME_M_DiscountSchema_ID = "M_DiscountSchema_ID";

	/** Set Discount Schema.
	  * Schema to calculate the trade discount percentage
	  */
	public void setM_DiscountSchema_ID (int M_DiscountSchema_ID);

	/** Get Discount Schema.
	  * Schema to calculate the trade discount percentage
	  */
	public int getM_DiscountSchema_ID();

	public I_M_DiscountSchema getM_DiscountSchema() throws RuntimeException;

    /** Column name mercantilebuyer */
    public static final String COLUMNNAME_mercantilebuyer = "mercantilebuyer";

	/** Set mercantilebuyer	  */
	public void setmercantilebuyer (boolean mercantilebuyer);

	/** Get mercantilebuyer	  */
	public boolean ismercantilebuyer();

    /** Column name M_PriceList_ID */
    public static final String COLUMNNAME_M_PriceList_ID = "M_PriceList_ID";

	/** Set Price List.
	  * Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID);

	/** Get Price List.
	  * Unique identifier of a Price List
	  */
	public int getM_PriceList_ID();

	public I_M_PriceList getM_PriceList() throws RuntimeException;

    /** Column name NAICS */
    public static final String COLUMNNAME_NAICS = "NAICS";

	/** Set NAICS/SIC.
	  * Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public void setNAICS (String NAICS);

	/** Get NAICS/SIC.
	  * Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public String getNAICS();

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

    /** Column name NumberEmployees */
    public static final String COLUMNNAME_NumberEmployees = "NumberEmployees";

	/** Set Number of employees.
	  * Number of employees
	  */
	public void setNumberEmployees (int NumberEmployees);

	/** Get Number of employees.
	  * Number of employees
	  */
	public int getNumberEmployees();

    /** Column name pannumber */
    public static final String COLUMNNAME_pannumber = "pannumber";

	/** Set PAN Number .
	  * PAN Number 
	  */
	public void setpannumber (String pannumber);

	/** Get PAN Number .
	  * PAN Number 
	  */
	public String getpannumber();

    /** Column name PaymentRule */
    public static final String COLUMNNAME_PaymentRule = "PaymentRule";

	/** Set Payment Rule.
	  * How you pay the invoice
	  */
	public void setPaymentRule (String PaymentRule);

	/** Get Payment Rule.
	  * How you pay the invoice
	  */
	public String getPaymentRule();

    /** Column name PaymentRulePO */
    public static final String COLUMNNAME_PaymentRulePO = "PaymentRulePO";

	/** Set Payment Rule.
	  * Purchase payment option
	  */
	public void setPaymentRulePO (String PaymentRulePO);

	/** Get Payment Rule.
	  * Purchase payment option
	  */
	public String getPaymentRulePO();

    /** Column name PO_DiscountSchema_ID */
    public static final String COLUMNNAME_PO_DiscountSchema_ID = "PO_DiscountSchema_ID";

	/** Set PO Discount Schema.
	  * Schema to calculate the purchase trade discount percentage
	  */
	public void setPO_DiscountSchema_ID (int PO_DiscountSchema_ID);

	/** Get PO Discount Schema.
	  * Schema to calculate the purchase trade discount percentage
	  */
	public int getPO_DiscountSchema_ID();

	public I_M_DiscountSchema getPO_DiscountSchema() throws RuntimeException;

    /** Column name PO_PaymentTerm_ID */
    public static final String COLUMNNAME_PO_PaymentTerm_ID = "PO_PaymentTerm_ID";

	/** Set PO Payment Term.
	  * Payment rules for a purchase order
	  */
	public void setPO_PaymentTerm_ID (int PO_PaymentTerm_ID);

	/** Get PO Payment Term.
	  * Payment rules for a purchase order
	  */
	public int getPO_PaymentTerm_ID();

	public I_C_PaymentTerm getPO_PaymentTerm() throws RuntimeException;

    /** Column name PO_PriceList_ID */
    public static final String COLUMNNAME_PO_PriceList_ID = "PO_PriceList_ID";

	/** Set Purchase Pricelist.
	  * Price List used by this Business Partner
	  */
	public void setPO_PriceList_ID (int PO_PriceList_ID);

	/** Get Purchase Pricelist.
	  * Price List used by this Business Partner
	  */
	public int getPO_PriceList_ID();

	public I_M_PriceList getPO_PriceList() throws RuntimeException;

    /** Column name POReference */
    public static final String COLUMNNAME_POReference = "POReference";

	/** Set Order Reference.
	  * Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	public void setPOReference (String POReference);

	/** Get Order Reference.
	  * Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	public String getPOReference();

    /** Column name PotentialLifeTimeValue */
    public static final String COLUMNNAME_PotentialLifeTimeValue = "PotentialLifeTimeValue";

	/** Set Potential Life Time Value.
	  * Total Revenue expected
	  */
	public void setPotentialLifeTimeValue (BigDecimal PotentialLifeTimeValue);

	/** Get Potential Life Time Value.
	  * Total Revenue expected
	  */
	public BigDecimal getPotentialLifeTimeValue();

    /** Column name purchasecomm */
    public static final String COLUMNNAME_purchasecomm = "purchasecomm";

	/** Set Purchase Commission.
	  * Purchase Commission
	  */
	public void setpurchasecomm (BigDecimal purchasecomm);

	/** Get Purchase Commission.
	  * Purchase Commission
	  */
	public BigDecimal getpurchasecomm();

    /** Column name Rating */
    public static final String COLUMNNAME_Rating = "Rating";

	/** Set Rating.
	  * Classification or Importance
	  */
	public void setRating (String Rating);

	/** Get Rating.
	  * Classification or Importance
	  */
	public String getRating();

    /** Column name rcnumber */
    public static final String COLUMNNAME_rcnumber = "rcnumber";

	/** Set Rc Number	  */
	public void setrcnumber (String rcnumber);

	/** Get Rc Number	  */
	public String getrcnumber();

    /** Column name ReferenceNo */
    public static final String COLUMNNAME_ReferenceNo = "ReferenceNo";

	/** Set Reference No.
	  * Your customer or vendor number at the Business Partner's site
	  */
	public void setReferenceNo (String ReferenceNo);

	/** Get Reference No.
	  * Your customer or vendor number at the Business Partner's site
	  */
	public String getReferenceNo();

    /** Column name relieveemployee */
    public static final String COLUMNNAME_relieveemployee = "relieveemployee";

	/** Set Relieve Employee.
	  * Relieve Employee
	  */
	public void setrelieveemployee (String relieveemployee);

	/** Get Relieve Employee.
	  * Relieve Employee
	  */
	public String getrelieveemployee();

    /** Column name relievingdate */
    public static final String COLUMNNAME_relievingdate = "relievingdate";

	/** Set Relieving Date.
	  * Relieving Date
	  */
	public void setrelievingdate (Timestamp relievingdate);

	/** Get Relieving Date.
	  * Relieving Date
	  */
	public Timestamp getrelievingdate();

    /** Column name removedfromprobationdate */
    public static final String COLUMNNAME_removedfromprobationdate = "removedfromprobationdate";

	/** Set removedfromprobationdate	  */
	public void setremovedfromprobationdate (Timestamp removedfromprobationdate);

	/** Get removedfromprobationdate	  */
	public Timestamp getremovedfromprobationdate();

    /** Column name salecomm */
    public static final String COLUMNNAME_salecomm = "salecomm";

	/** Set Sale Commission.
	  * Sale Commission
	  */
	public void setsalecomm (BigDecimal salecomm);

	/** Get Sale Commission.
	  * Sale Commission
	  */
	public BigDecimal getsalecomm();

    /** Column name SalesRep_ID */
    public static final String COLUMNNAME_SalesRep_ID = "SalesRep_ID";

	/** Set Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID);

	/** Get Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public int getSalesRep_ID();

	public I_AD_User getSalesRep() throws RuntimeException;

    /** Column name SalesVolume */
    public static final String COLUMNNAME_SalesVolume = "SalesVolume";

	/** Set Sales Volume in 1.000.
	  * Total Volume of Sales in Thousands of Currency
	  */
	public void setSalesVolume (int SalesVolume);

	/** Get Sales Volume in 1.000.
	  * Total Volume of Sales in Thousands of Currency
	  */
	public int getSalesVolume();

    /** Column name SendEMail */
    public static final String COLUMNNAME_SendEMail = "SendEMail";

	/** Set Send EMail.
	  * Enable sending Document EMail
	  */
	public void setSendEMail (boolean SendEMail);

	/** Get Send EMail.
	  * Enable sending Document EMail
	  */
	public boolean isSendEMail();

    /** Column name ShareOfCustomer */
    public static final String COLUMNNAME_ShareOfCustomer = "ShareOfCustomer";

	/** Set Share.
	  * Share of Customer's business as a percentage
	  */
	public void setShareOfCustomer (int ShareOfCustomer);

	/** Get Share.
	  * Share of Customer's business as a percentage
	  */
	public int getShareOfCustomer();

    /** Column name ShelfLifeMinPct */
    public static final String COLUMNNAME_ShelfLifeMinPct = "ShelfLifeMinPct";

	/** Set Min Shelf Life %.
	  * Minimum Shelf Life in percent based on Product Instance Guarantee Date
	  */
	public void setShelfLifeMinPct (int ShelfLifeMinPct);

	/** Get Min Shelf Life %.
	  * Minimum Shelf Life in percent based on Product Instance Guarantee Date
	  */
	public int getShelfLifeMinPct();

    /** Column name SO_CreditLimit */
    public static final String COLUMNNAME_SO_CreditLimit = "SO_CreditLimit";

	/** Set Credit Limit.
	  * Total outstanding invoice amounts allowed
	  */
	public void setSO_CreditLimit (BigDecimal SO_CreditLimit);

	/** Get Credit Limit.
	  * Total outstanding invoice amounts allowed
	  */
	public BigDecimal getSO_CreditLimit();

    /** Column name SOCreditStatus */
    public static final String COLUMNNAME_SOCreditStatus = "SOCreditStatus";

	/** Set Credit Status.
	  * Business Partner Credit Status
	  */
	public void setSOCreditStatus (String SOCreditStatus);

	/** Get Credit Status.
	  * Business Partner Credit Status
	  */
	public String getSOCreditStatus();

    /** Column name SO_CreditUsed */
    public static final String COLUMNNAME_SO_CreditUsed = "SO_CreditUsed";

	/** Set Credit Used.
	  * Current open balance
	  */
	public void setSO_CreditUsed (BigDecimal SO_CreditUsed);

	/** Get Credit Used.
	  * Current open balance
	  */
	public BigDecimal getSO_CreditUsed();

    /** Column name SO_Description */
    public static final String COLUMNNAME_SO_Description = "SO_Description";

	/** Set Order Description.
	  * Description to be used on orders
	  */
	public void setSO_Description (String SO_Description);

	/** Get Order Description.
	  * Description to be used on orders
	  */
	public String getSO_Description();

    /** Column name TaxID */
    public static final String COLUMNNAME_TaxID = "TaxID";

	/** Set Tax ID.
	  * Tax Identification
	  */
	public void setTaxID (String TaxID);

	/** Get Tax ID.
	  * Tax Identification
	  */
	public String getTaxID();

    /** Column name TaxIdDigit */
    public static final String COLUMNNAME_TaxIdDigit = "TaxIdDigit";

	/** Set Tax ID Digit	  */
	public void setTaxIdDigit (String TaxIdDigit);

	/** Get Tax ID Digit	  */
	public String getTaxIdDigit();

    /** Column name tinnumber */
    public static final String COLUMNNAME_tinnumber = "tinnumber";

	/** Set Tin Number.
	  * Tin Number
	  */
	public void settinnumber (String tinnumber);

	/** Get Tin Number.
	  * Tin Number
	  */
	public String gettinnumber();

    /** Column name TotalOpenBalance */
    public static final String COLUMNNAME_TotalOpenBalance = "TotalOpenBalance";

	/** Set Open Balance.
	  * Total Open Balance Amount in primary Accounting Currency
	  */
	public void setTotalOpenBalance (BigDecimal TotalOpenBalance);

	/** Get Open Balance.
	  * Total Open Balance Amount in primary Accounting Currency
	  */
	public BigDecimal getTotalOpenBalance();

    /** Column name totnumber */
    public static final String COLUMNNAME_totnumber = "totnumber";

	/** Set totnumber	  */
	public void settotnumber (String totnumber);

	/** Get totnumber	  */
	public String gettotnumber();

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

    /** Column name URL */
    public static final String COLUMNNAME_URL = "URL";

	/** Set URL.
	  * Full URL address - e.g. http://www.adempiere.org
	  */
	public void setURL (String URL);

	/** Get URL.
	  * Full URL address - e.g. http://www.adempiere.org
	  */
	public String getURL();

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();

    /** Column name weeklyoff */
    public static final String COLUMNNAME_weeklyoff = "weeklyoff";

	/** Set Weekly Off.
	  * Weekly Off
	  */
	public void setweeklyoff (String weeklyoff);

	/** Get Weekly Off.
	  * Weekly Off
	  */
	public String getweeklyoff();

    /** Column name wtc_tds_deducteetype_ID */
    public static final String COLUMNNAME_wtc_tds_deducteetype_ID = "wtc_tds_deducteetype_ID";

	/** Set TDS_DeducteeType	  */
	public void setwtc_tds_deducteetype_ID (int wtc_tds_deducteetype_ID);

	/** Get TDS_DeducteeType	  */
	public int getwtc_tds_deducteetype_ID();
}
