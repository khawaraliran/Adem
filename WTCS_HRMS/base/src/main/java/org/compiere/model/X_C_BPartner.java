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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for C_BPartner
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_C_BPartner.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_C_BPartner extends PO implements I_C_BPartner, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_C_BPartner.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111225L;

    /** Standard Constructor */
    public X_C_BPartner (Properties ctx, int C_BPartner_ID, String trxName)
    {
      super (ctx, C_BPartner_ID, trxName);
      /** if (C_BPartner_ID == 0)
        {
			setBPartner_Parent_ID (0);
			setC_BPartner_ID (0);
			setC_BP_Group_ID (0);
			setdivision_ID (0);
			setEMail (null);
			setemployee_code (null);
			setHR_Department_ID (0);
			setHR_Designation_ID (0);
			setHR_Employee_Type_ID (0);
			setHR_Work_Group_ID (0);
			setIsCustomer (false);
			setIsDetailedNames (false);
// N
			setIsEmployee (false);
			setIsOneTime (false);
			setIsPOTaxExempt (false);
// N
			setIsProspect (false);
			setIsSalesRep (false);
			setIsSummary (false);
			setIsUseTaxIdDigit (false);
// N
			setIsVendor (false);
			setjoiningdate (new Timestamp( System.currentTimeMillis() ));
			setName (null);
			setSendEMail (false);
			setSO_CreditLimit (Env.ZERO);
			setSO_CreditUsed (Env.ZERO);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_C_BPartner (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_BPartner[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Acquisition Cost.
		@param AcqusitionCost 
		The cost of gaining the prospect as a customer
	  */
	public void setAcqusitionCost (BigDecimal AcqusitionCost)
	{
		set_Value (COLUMNNAME_AcqusitionCost, AcqusitionCost);
	}

	/** Get Acquisition Cost.
		@return The cost of gaining the prospect as a customer
	  */
	public BigDecimal getAcqusitionCost () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AcqusitionCost);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Actual Life Time Value.
		@param ActualLifeTimeValue 
		Actual Life Time Revenue
	  */
	public void setActualLifeTimeValue (BigDecimal ActualLifeTimeValue)
	{
		set_Value (COLUMNNAME_ActualLifeTimeValue, ActualLifeTimeValue);
	}

	/** Get Actual Life Time Value.
		@return Actual Life Time Revenue
	  */
	public BigDecimal getActualLifeTimeValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ActualLifeTimeValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** AD_Language AD_Reference_ID=327 */
	public static final int AD_LANGUAGE_AD_Reference_ID=327;
	/** Set Language.
		@param AD_Language 
		Language for this entity
	  */
	public void setAD_Language (String AD_Language)
	{

		set_Value (COLUMNNAME_AD_Language, AD_Language);
	}

	/** Get Language.
		@return Language for this entity
	  */
	public String getAD_Language () 
	{
		return (String)get_Value(COLUMNNAME_AD_Language);
	}

	/** Set Linked Organization.
		@param AD_OrgBP_ID 
		The Business Partner is another Organization for explicit Inter-Org transactions
	  */
	public void setAD_OrgBP_ID (String AD_OrgBP_ID)
	{
		set_Value (COLUMNNAME_AD_OrgBP_ID, AD_OrgBP_ID);
	}

	/** Get Linked Organization.
		@return The Business Partner is another Organization for explicit Inter-Org transactions
	  */
	public String getAD_OrgBP_ID () 
	{
		return (String)get_Value(COLUMNNAME_AD_OrgBP_ID);
	}

	public I_C_BPartner getagent() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getagent_ID(), get_TrxName());	}

	/** Set Agent Name.
		@param agent_ID 
		Agent Name
	  */
	public void setagent_ID (int agent_ID)
	{
		if (agent_ID < 1) 
			set_Value (COLUMNNAME_agent_ID, null);
		else 
			set_Value (COLUMNNAME_agent_ID, Integer.valueOf(agent_ID));
	}

	/** Get Agent Name.
		@return Agent Name
	  */
	public int getagent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_agent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getBPartner_Parent() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getBPartner_Parent_ID(), get_TrxName());	}

	/** Set Partner Parent.
		@param BPartner_Parent_ID 
		Business Partner Parent
	  */
	public void setBPartner_Parent_ID (int BPartner_Parent_ID)
	{
		if (BPartner_Parent_ID < 1) 
			set_Value (COLUMNNAME_BPartner_Parent_ID, null);
		else 
			set_Value (COLUMNNAME_BPartner_Parent_ID, Integer.valueOf(BPartner_Parent_ID));
	}

	/** Get Partner Parent.
		@return Business Partner Parent
	  */
	public int getBPartner_Parent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BPartner_Parent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set buyercode.
		@param buyercode buyercode	  */
	public void setbuyercode (String buyercode)
	{
		set_ValueNoCheck (COLUMNNAME_buyercode, buyercode);
	}

	/** Get buyercode.
		@return buyercode	  */
	public String getbuyercode () 
	{
		return (String)get_Value(COLUMNNAME_buyercode);
	}

	/** buyertype AD_Reference_ID=1000022 */
	public static final int BUYERTYPE_AD_Reference_ID=1000022;
	/** Domestic = D */
	public static final String BUYERTYPE_Domestic = "D";
	/** Export = E */
	public static final String BUYERTYPE_Export = "E";
	/** Set buyertype.
		@param buyertype buyertype	  */
	public void setbuyertype (String buyertype)
	{

		set_ValueNoCheck (COLUMNNAME_buyertype, buyertype);
	}

	/** Get buyertype.
		@return buyertype	  */
	public String getbuyertype () 
	{
		return (String)get_Value(COLUMNNAME_buyertype);
	}

	/** Set Business Partner.
		@param C_BPartner_ID 
		Identifies a Employee
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner.
		@return Identifies a Employee
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BP_Group getC_BP_Group() throws RuntimeException
    {
		return (I_C_BP_Group)MTable.get(getCtx(), I_C_BP_Group.Table_Name)
			.getPO(getC_BP_Group_ID(), get_TrxName());	}

	/** Set Business Partner Group.
		@param C_BP_Group_ID 
		Business Partner Group
	  */
	public void setC_BP_Group_ID (int C_BP_Group_ID)
	{
		if (C_BP_Group_ID < 1) 
			set_Value (COLUMNNAME_C_BP_Group_ID, null);
		else 
			set_Value (COLUMNNAME_C_BP_Group_ID, Integer.valueOf(C_BP_Group_ID));
	}

	/** Get Business Partner Group.
		@return Business Partner Group
	  */
	public int getC_BP_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BP_Group_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Dunning getC_Dunning() throws RuntimeException
    {
		return (I_C_Dunning)MTable.get(getCtx(), I_C_Dunning.Table_Name)
			.getPO(getC_Dunning_ID(), get_TrxName());	}

	/** Set Dunning.
		@param C_Dunning_ID 
		Dunning Rules for overdue invoices
	  */
	public void setC_Dunning_ID (int C_Dunning_ID)
	{
		if (C_Dunning_ID < 1) 
			set_Value (COLUMNNAME_C_Dunning_ID, null);
		else 
			set_Value (COLUMNNAME_C_Dunning_ID, Integer.valueOf(C_Dunning_ID));
	}

	/** Get Dunning.
		@return Dunning Rules for overdue invoices
	  */
	public int getC_Dunning_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Dunning_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Greeting getC_Greeting() throws RuntimeException
    {
		return (I_C_Greeting)MTable.get(getCtx(), I_C_Greeting.Table_Name)
			.getPO(getC_Greeting_ID(), get_TrxName());	}

	/** Set Greeting.
		@param C_Greeting_ID 
		Greeting to print on correspondence
	  */
	public void setC_Greeting_ID (int C_Greeting_ID)
	{
		if (C_Greeting_ID < 1) 
			set_Value (COLUMNNAME_C_Greeting_ID, null);
		else 
			set_Value (COLUMNNAME_C_Greeting_ID, Integer.valueOf(C_Greeting_ID));
	}

	/** Get Greeting.
		@return Greeting to print on correspondence
	  */
	public int getC_Greeting_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Greeting_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_InvoiceSchedule getC_InvoiceSchedule() throws RuntimeException
    {
		return (I_C_InvoiceSchedule)MTable.get(getCtx(), I_C_InvoiceSchedule.Table_Name)
			.getPO(getC_InvoiceSchedule_ID(), get_TrxName());	}

	/** Set Invoice Schedule.
		@param C_InvoiceSchedule_ID 
		Schedule for generating Invoices
	  */
	public void setC_InvoiceSchedule_ID (int C_InvoiceSchedule_ID)
	{
		if (C_InvoiceSchedule_ID < 1) 
			set_Value (COLUMNNAME_C_InvoiceSchedule_ID, null);
		else 
			set_Value (COLUMNNAME_C_InvoiceSchedule_ID, Integer.valueOf(C_InvoiceSchedule_ID));
	}

	/** Get Invoice Schedule.
		@return Schedule for generating Invoices
	  */
	public int getC_InvoiceSchedule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_InvoiceSchedule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Commission Amount.
		@param CommissionAmt 
		Commission Amount
	  */
	public void setCommissionAmt (String CommissionAmt)
	{
		throw new IllegalArgumentException ("CommissionAmt is virtual column");	}

	/** Get Commission Amount.
		@return Commission Amount
	  */
	public String getCommissionAmt () 
	{
		return (String)get_Value(COLUMNNAME_CommissionAmt);
	}

	public I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException
    {
		return (I_C_PaymentTerm)MTable.get(getCtx(), I_C_PaymentTerm.Table_Name)
			.getPO(getC_PaymentTerm_ID(), get_TrxName());	}

	/** Set Payment Term.
		@param C_PaymentTerm_ID 
		The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
	{
		if (C_PaymentTerm_ID < 1) 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, null);
		else 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, Integer.valueOf(C_PaymentTerm_ID));
	}

	/** Get Payment Term.
		@return The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentTerm_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Create / Update User Details.
		@param createuser 
		create or udpate user details of the employee
	  */
	public void setcreateuser (String createuser)
	{
		set_Value (COLUMNNAME_createuser, createuser);
	}

	/** Get Create / Update User Details.
		@return create or udpate user details of the employee
	  */
	public String getcreateuser () 
	{
		return (String)get_Value(COLUMNNAME_createuser);
	}

	public org.eevolution.model.I_C_TaxGroup getC_TaxGroup() throws RuntimeException
    {
		return (org.eevolution.model.I_C_TaxGroup)MTable.get(getCtx(), org.eevolution.model.I_C_TaxGroup.Table_Name)
			.getPO(getC_TaxGroup_ID(), get_TrxName());	}

	/** Set Tax Group.
		@param C_TaxGroup_ID Tax Group	  */
	public void setC_TaxGroup_ID (int C_TaxGroup_ID)
	{
		if (C_TaxGroup_ID < 1) 
			set_Value (COLUMNNAME_C_TaxGroup_ID, null);
		else 
			set_Value (COLUMNNAME_C_TaxGroup_ID, Integer.valueOf(C_TaxGroup_ID));
	}

	/** Get Tax Group.
		@return Tax Group	  */
	public int getC_TaxGroup_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TaxGroup_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** DeliveryRule AD_Reference_ID=151 */
	public static final int DELIVERYRULE_AD_Reference_ID=151;
	/** After Receipt = R */
	public static final String DELIVERYRULE_AfterReceipt = "R";
	/** Availability = A */
	public static final String DELIVERYRULE_Availability = "A";
	/** Complete Line = L */
	public static final String DELIVERYRULE_CompleteLine = "L";
	/** Complete Order = O */
	public static final String DELIVERYRULE_CompleteOrder = "O";
	/** Force = F */
	public static final String DELIVERYRULE_Force = "F";
	/** Manual = M */
	public static final String DELIVERYRULE_Manual = "M";
	/** Set Delivery Rule.
		@param DeliveryRule 
		Defines the timing of Delivery
	  */
	public void setDeliveryRule (String DeliveryRule)
	{

		set_Value (COLUMNNAME_DeliveryRule, DeliveryRule);
	}

	/** Get Delivery Rule.
		@return Defines the timing of Delivery
	  */
	public String getDeliveryRule () 
	{
		return (String)get_Value(COLUMNNAME_DeliveryRule);
	}

	/** DeliveryViaRule AD_Reference_ID=152 */
	public static final int DELIVERYVIARULE_AD_Reference_ID=152;
	/** Pickup = P */
	public static final String DELIVERYVIARULE_Pickup = "P";
	/** Delivery = D */
	public static final String DELIVERYVIARULE_Delivery = "D";
	/** Shipper = S */
	public static final String DELIVERYVIARULE_Shipper = "S";
	/** Set Delivery Via.
		@param DeliveryViaRule 
		How the order will be delivered
	  */
	public void setDeliveryViaRule (String DeliveryViaRule)
	{

		set_Value (COLUMNNAME_DeliveryViaRule, DeliveryViaRule);
	}

	/** Get Delivery Via.
		@return How the order will be delivered
	  */
	public String getDeliveryViaRule () 
	{
		return (String)get_Value(COLUMNNAME_DeliveryViaRule);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Division / Branch.
		@param division_ID 
		Division / Branch
	  */
	public void setdivision_ID (int division_ID)
	{
		if (division_ID < 1) 
			set_Value (COLUMNNAME_division_ID, null);
		else 
			set_Value (COLUMNNAME_division_ID, Integer.valueOf(division_ID));
	}

	/** Get Division / Branch.
		@return Division / Branch
	  */
	public int getdivision_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_division_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Copies.
		@param DocumentCopies 
		Number of copies to be printed
	  */
	public void setDocumentCopies (int DocumentCopies)
	{
		set_Value (COLUMNNAME_DocumentCopies, Integer.valueOf(DocumentCopies));
	}

	/** Get Document Copies.
		@return Number of copies to be printed
	  */
	public int getDocumentCopies () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DocumentCopies);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Dunning Grace Date.
		@param DunningGrace Dunning Grace Date	  */
	public void setDunningGrace (Timestamp DunningGrace)
	{
		set_Value (COLUMNNAME_DunningGrace, DunningGrace);
	}

	/** Get Dunning Grace Date.
		@return Dunning Grace Date	  */
	public Timestamp getDunningGrace () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DunningGrace);
	}

	/** Set D-U-N-S.
		@param DUNS 
		Dun & Bradstreet Number
	  */
	public void setDUNS (String DUNS)
	{
		set_Value (COLUMNNAME_DUNS, DUNS);
	}

	/** Get D-U-N-S.
		@return Dun & Bradstreet Number
	  */
	public String getDUNS () 
	{
		return (String)get_Value(COLUMNNAME_DUNS);
	}

	/** Set ECC Code.
		@param ecccode ECC Code	  */
	public void setecccode (String ecccode)
	{
		set_Value (COLUMNNAME_ecccode, ecccode);
	}

	/** Get ECC Code.
		@return ECC Code	  */
	public String getecccode () 
	{
		return (String)get_Value(COLUMNNAME_ecccode);
	}

	/** Set eccdivision.
		@param eccdivision eccdivision	  */
	public void seteccdivision (String eccdivision)
	{
		set_Value (COLUMNNAME_eccdivision, eccdivision);
	}

	/** Get eccdivision.
		@return eccdivision	  */
	public String geteccdivision () 
	{
		return (String)get_Value(COLUMNNAME_eccdivision);
	}

	/** Set eccrange.
		@param eccrange eccrange	  */
	public void seteccrange (String eccrange)
	{
		set_Value (COLUMNNAME_eccrange, eccrange);
	}

	/** Get eccrange.
		@return eccrange	  */
	public String geteccrange () 
	{
		return (String)get_Value(COLUMNNAME_eccrange);
	}

	/** Set EMail Address.
		@param EMail 
		Electronic Mail Address
	  */
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail () 
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set Employee Code.
		@param employee_code 
		Employee Code - Unique
	  */
	public void setemployee_code (String employee_code)
	{
		set_Value (COLUMNNAME_employee_code, employee_code);
	}

	/** Get Employee Code.
		@return Employee Code - Unique
	  */
	public String getemployee_code () 
	{
		return (String)get_Value(COLUMNNAME_employee_code);
	}

	/** employeestatus AD_Reference_ID=1000042 */
	public static final int EMPLOYEESTATUS_AD_Reference_ID=1000042;
	/** Probation = PRB */
	public static final String EMPLOYEESTATUS_Probation = "PRB";
	/** Confirmed = CON */
	public static final String EMPLOYEESTATUS_Confirmed = "CON";
	/** Performance Improvement Plan = PIP */
	public static final String EMPLOYEESTATUS_PerformanceImprovementPlan = "PIP";
	/** Serving Notice Period = SNP */
	public static final String EMPLOYEESTATUS_ServingNoticePeriod = "SNP";
	/** Separated = SEP */
	public static final String EMPLOYEESTATUS_Separated = "SEP";
	/** Set Employee Status.
		@param employeestatus 
		Status of the Employee
	  */
	public void setemployeestatus (String employeestatus)
	{

		set_Value (COLUMNNAME_employeestatus, employeestatus);
	}

	/** Get Employee Status.
		@return Status of the Employee
	  */
	public String getemployeestatus () 
	{
		return (String)get_Value(COLUMNNAME_employeestatus);
	}

	/** Set First Name 1.
		@param FirstName1 First Name 1	  */
	public void setFirstName1 (String FirstName1)
	{
		set_Value (COLUMNNAME_FirstName1, FirstName1);
	}

	/** Get First Name 1.
		@return First Name 1	  */
	public String getFirstName1 () 
	{
		return (String)get_Value(COLUMNNAME_FirstName1);
	}

	/** Set First Name 2.
		@param FirstName2 First Name 2	  */
	public void setFirstName2 (String FirstName2)
	{
		set_Value (COLUMNNAME_FirstName2, FirstName2);
	}

	/** Get First Name 2.
		@return First Name 2	  */
	public String getFirstName2 () 
	{
		return (String)get_Value(COLUMNNAME_FirstName2);
	}

	/** Set First Sale.
		@param FirstSale 
		Date of First Sale
	  */
	public void setFirstSale (Timestamp FirstSale)
	{
		set_Value (COLUMNNAME_FirstSale, FirstSale);
	}

	/** Get First Sale.
		@return Date of First Sale
	  */
	public Timestamp getFirstSale () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FirstSale);
	}

	/** Set Flat Discount %.
		@param FlatDiscount 
		Flat discount percentage 
	  */
	public void setFlatDiscount (BigDecimal FlatDiscount)
	{
		set_Value (COLUMNNAME_FlatDiscount, FlatDiscount);
	}

	/** Get Flat Discount %.
		@return Flat discount percentage 
	  */
	public BigDecimal getFlatDiscount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_FlatDiscount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** FreightCostRule AD_Reference_ID=153 */
	public static final int FREIGHTCOSTRULE_AD_Reference_ID=153;
	/** Freight included = I */
	public static final String FREIGHTCOSTRULE_FreightIncluded = "I";
	/** Fix price = F */
	public static final String FREIGHTCOSTRULE_FixPrice = "F";
	/** Calculated = C */
	public static final String FREIGHTCOSTRULE_Calculated = "C";
	/** Line = L */
	public static final String FREIGHTCOSTRULE_Line = "L";
	/** Set Freight Cost Rule.
		@param FreightCostRule 
		Method for charging Freight
	  */
	public void setFreightCostRule (String FreightCostRule)
	{

		set_Value (COLUMNNAME_FreightCostRule, FreightCostRule);
	}

	/** Get Freight Cost Rule.
		@return Method for charging Freight
	  */
	public String getFreightCostRule () 
	{
		return (String)get_Value(COLUMNNAME_FreightCostRule);
	}

	/** Set Block Name.
		@param HR_Block_ID 
		Block Name
	  */
	public void setHR_Block_ID (int HR_Block_ID)
	{
		if (HR_Block_ID < 1) 
			set_Value (COLUMNNAME_HR_Block_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Block_ID, Integer.valueOf(HR_Block_ID));
	}

	/** Get Block Name.
		@return Block Name
	  */
	public int getHR_Block_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Block_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Commission Amount.
		@param hrcommamt Commission Amount	  */
	public void sethrcommamt (BigDecimal hrcommamt)
	{
		set_Value (COLUMNNAME_hrcommamt, hrcommamt);
	}

	/** Get Commission Amount.
		@return Commission Amount	  */
	public BigDecimal gethrcommamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_hrcommamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Designation.
		@param HR_Designation_ID 
		Designation
	  */
	public void setHR_Designation_ID (int HR_Designation_ID)
	{
		if (HR_Designation_ID < 1) 
			set_Value (COLUMNNAME_HR_Designation_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Designation_ID, Integer.valueOf(HR_Designation_ID));
	}

	/** Get Designation.
		@return Designation
	  */
	public int getHR_Designation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Designation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Employee)MTable.get(getCtx(), org.eevolution.model.I_HR_Employee.Table_Name)
			.getPO(getHR_Employee_ID(), get_TrxName());	}

	/** Set Employee Name.
		@param HR_Employee_ID Employee Name	  */
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1) 
			set_Value (COLUMNNAME_HR_Employee_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
	}

	/** Get Employee Name.
		@return Employee Name	  */
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

	/** Set Grade.
		@param HR_Grade_ID 
		Grade
	  */
	public void setHR_Grade_ID (int HR_Grade_ID)
	{
		if (HR_Grade_ID < 1) 
			set_Value (COLUMNNAME_HR_Grade_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Grade_ID, Integer.valueOf(HR_Grade_ID));
	}

	/** Get Grade.
		@return Grade
	  */
	public int getHR_Grade_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Grade_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Quarter.
		@param HR_Quarter_ID 
		Quarter
	  */
	public void setHR_Quarter_ID (int HR_Quarter_ID)
	{
		if (HR_Quarter_ID < 1) 
			set_Value (COLUMNNAME_HR_Quarter_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Quarter_ID, Integer.valueOf(HR_Quarter_ID));
	}

	/** Get Quarter.
		@return Quarter
	  */
	public int getHR_Quarter_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Quarter_ID);
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

	/** Set Work Group Name.
		@param HR_Work_Group_ID 
		Name of the Work Group
	  */
	public void setHR_Work_Group_ID (int HR_Work_Group_ID)
	{
		if (HR_Work_Group_ID < 1) 
			set_Value (COLUMNNAME_HR_Work_Group_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Work_Group_ID, Integer.valueOf(HR_Work_Group_ID));
	}

	/** Get Work Group Name.
		@return Name of the Work Group
	  */
	public int getHR_Work_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Work_Group_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set iecnumber.
		@param iecnumber iecnumber	  */
	public void setiecnumber (String iecnumber)
	{
		set_Value (COLUMNNAME_iecnumber, iecnumber);
	}

	/** Get iecnumber.
		@return iecnumber	  */
	public String getiecnumber () 
	{
		return (String)get_Value(COLUMNNAME_iecnumber);
	}

	public I_AD_PrintFormat getInvoice_PrintFormat() throws RuntimeException
    {
		return (I_AD_PrintFormat)MTable.get(getCtx(), I_AD_PrintFormat.Table_Name)
			.getPO(getInvoice_PrintFormat_ID(), get_TrxName());	}

	/** Set Invoice Print Format.
		@param Invoice_PrintFormat_ID 
		Print Format for printing Invoices
	  */
	public void setInvoice_PrintFormat_ID (int Invoice_PrintFormat_ID)
	{
		if (Invoice_PrintFormat_ID < 1) 
			set_Value (COLUMNNAME_Invoice_PrintFormat_ID, null);
		else 
			set_Value (COLUMNNAME_Invoice_PrintFormat_ID, Integer.valueOf(Invoice_PrintFormat_ID));
	}

	/** Get Invoice Print Format.
		@return Print Format for printing Invoices
	  */
	public int getInvoice_PrintFormat_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Invoice_PrintFormat_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** InvoiceRule AD_Reference_ID=150 */
	public static final int INVOICERULE_AD_Reference_ID=150;
	/** After Order delivered = O */
	public static final String INVOICERULE_AfterOrderDelivered = "O";
	/** After Delivery = D */
	public static final String INVOICERULE_AfterDelivery = "D";
	/** Customer Schedule after Delivery = S */
	public static final String INVOICERULE_CustomerScheduleAfterDelivery = "S";
	/** Immediate = I */
	public static final String INVOICERULE_Immediate = "I";
	/** Set Invoice Rule.
		@param InvoiceRule 
		Frequency and method of invoicing 
	  */
	public void setInvoiceRule (String InvoiceRule)
	{

		set_Value (COLUMNNAME_InvoiceRule, InvoiceRule);
	}

	/** Get Invoice Rule.
		@return Frequency and method of invoicing 
	  */
	public String getInvoiceRule () 
	{
		return (String)get_Value(COLUMNNAME_InvoiceRule);
	}

	/** Set Is Management Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Is Management Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set iscollectionagent.
		@param iscollectionagent iscollectionagent	  */
	public void setiscollectionagent (boolean iscollectionagent)
	{
		set_Value (COLUMNNAME_iscollectionagent, Boolean.valueOf(iscollectionagent));
	}

	/** Get iscollectionagent.
		@return iscollectionagent	  */
	public boolean iscollectionagent () 
	{
		Object oo = get_Value(COLUMNNAME_iscollectionagent);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Company week policy.
		@param iscompanyweekpolicy Company week policy	  */
	public void setiscompanyweekpolicy (boolean iscompanyweekpolicy)
	{
		set_Value (COLUMNNAME_iscompanyweekpolicy, Boolean.valueOf(iscompanyweekpolicy));
	}

	/** Get Company week policy.
		@return Company week policy	  */
	public boolean iscompanyweekpolicy () 
	{
		Object oo = get_Value(COLUMNNAME_iscompanyweekpolicy);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isconsginmentagent.
		@param isconsginmentagent isconsginmentagent	  */
	public void setisconsginmentagent (boolean isconsginmentagent)
	{
		set_Value (COLUMNNAME_isconsginmentagent, Boolean.valueOf(isconsginmentagent));
	}

	/** Get isconsginmentagent.
		@return isconsginmentagent	  */
	public boolean isconsginmentagent () 
	{
		Object oo = get_Value(COLUMNNAME_isconsginmentagent);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Customer.
		@param IsCustomer 
		Indicates if this Business Partner is a Customer
	  */
	public void setIsCustomer (boolean IsCustomer)
	{
		set_Value (COLUMNNAME_IsCustomer, Boolean.valueOf(IsCustomer));
	}

	/** Get Customer.
		@return Indicates if this Business Partner is a Customer
	  */
	public boolean isCustomer () 
	{
		Object oo = get_Value(COLUMNNAME_IsCustomer);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Detailed Names.
		@param IsDetailedNames Detailed Names	  */
	public void setIsDetailedNames (boolean IsDetailedNames)
	{
		set_Value (COLUMNNAME_IsDetailedNames, Boolean.valueOf(IsDetailedNames));
	}

	/** Get Detailed Names.
		@return Detailed Names	  */
	public boolean isDetailedNames () 
	{
		Object oo = get_Value(COLUMNNAME_IsDetailedNames);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Discount Printed.
		@param IsDiscountPrinted 
		Print Discount on Invoice and Order
	  */
	public void setIsDiscountPrinted (boolean IsDiscountPrinted)
	{
		set_Value (COLUMNNAME_IsDiscountPrinted, Boolean.valueOf(IsDiscountPrinted));
	}

	/** Get Discount Printed.
		@return Print Discount on Invoice and Order
	  */
	public boolean isDiscountPrinted () 
	{
		Object oo = get_Value(COLUMNNAME_IsDiscountPrinted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Employee.
		@param IsEmployee 
		Indicates if  this Business Partner is an employee
	  */
	public void setIsEmployee (boolean IsEmployee)
	{
		set_Value (COLUMNNAME_IsEmployee, Boolean.valueOf(IsEmployee));
	}

	/** Get Employee.
		@return Indicates if  this Business Partner is an employee
	  */
	public boolean isEmployee () 
	{
		Object oo = get_Value(COLUMNNAME_IsEmployee);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isgeneralsupplier.
		@param isgeneralsupplier isgeneralsupplier	  */
	public void setisgeneralsupplier (boolean isgeneralsupplier)
	{
		set_Value (COLUMNNAME_isgeneralsupplier, Boolean.valueOf(isgeneralsupplier));
	}

	/** Get isgeneralsupplier.
		@return isgeneralsupplier	  */
	public boolean isgeneralsupplier () 
	{
		Object oo = get_Value(COLUMNNAME_isgeneralsupplier);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Manufacturer.
		@param IsManufacturer 
		Indicate role of this Business partner as Manufacturer
	  */
	public void setIsManufacturer (boolean IsManufacturer)
	{
		set_Value (COLUMNNAME_IsManufacturer, Boolean.valueOf(IsManufacturer));
	}

	/** Get Is Manufacturer.
		@return Indicate role of this Business partner as Manufacturer
	  */
	public boolean isManufacturer () 
	{
		Object oo = get_Value(COLUMNNAME_IsManufacturer);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set One time transaction.
		@param IsOneTime One time transaction	  */
	public void setIsOneTime (boolean IsOneTime)
	{
		set_Value (COLUMNNAME_IsOneTime, Boolean.valueOf(IsOneTime));
	}

	/** Get One time transaction.
		@return One time transaction	  */
	public boolean isOneTime () 
	{
		Object oo = get_Value(COLUMNNAME_IsOneTime);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set PO Tax exempt.
		@param IsPOTaxExempt 
		Business partner is exempt from tax on purchases
	  */
	public void setIsPOTaxExempt (boolean IsPOTaxExempt)
	{
		set_Value (COLUMNNAME_IsPOTaxExempt, Boolean.valueOf(IsPOTaxExempt));
	}

	/** Get PO Tax exempt.
		@return Business partner is exempt from tax on purchases
	  */
	public boolean isPOTaxExempt () 
	{
		Object oo = get_Value(COLUMNNAME_IsPOTaxExempt);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Prospect.
		@param IsProspect 
		Indicates this is a Prospect
	  */
	public void setIsProspect (boolean IsProspect)
	{
		set_Value (COLUMNNAME_IsProspect, Boolean.valueOf(IsProspect));
	}

	/** Get Prospect.
		@return Indicates this is a Prospect
	  */
	public boolean isProspect () 
	{
		Object oo = get_Value(COLUMNNAME_IsProspect);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Purchase Agent ?.
		@param ispurchaseagent 
		Purchase Agent
	  */
	public void setispurchaseagent (boolean ispurchaseagent)
	{
		set_Value (COLUMNNAME_ispurchaseagent, Boolean.valueOf(ispurchaseagent));
	}

	/** Get Purchase Agent ?.
		@return Purchase Agent
	  */
	public boolean ispurchaseagent () 
	{
		Object oo = get_Value(COLUMNNAME_ispurchaseagent);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set israwmaterialsupplier.
		@param israwmaterialsupplier israwmaterialsupplier	  */
	public void setisrawmaterialsupplier (boolean israwmaterialsupplier)
	{
		set_Value (COLUMNNAME_israwmaterialsupplier, Boolean.valueOf(israwmaterialsupplier));
	}

	/** Get israwmaterialsupplier.
		@return israwmaterialsupplier	  */
	public boolean israwmaterialsupplier () 
	{
		Object oo = get_Value(COLUMNNAME_israwmaterialsupplier);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Resource Agent ?.
		@param isresourceagent 
		Resource Agent
	  */
	public void setisresourceagent (boolean isresourceagent)
	{
		set_Value (COLUMNNAME_isresourceagent, Boolean.valueOf(isresourceagent));
	}

	/** Get Resource Agent ?.
		@return Resource Agent
	  */
	public boolean isresourceagent () 
	{
		Object oo = get_Value(COLUMNNAME_isresourceagent);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Sale Agent ?.
		@param issaleagent 
		Sale Agent
	  */
	public void setissaleagent (boolean issaleagent)
	{
		set_Value (COLUMNNAME_issaleagent, Boolean.valueOf(issaleagent));
	}

	/** Get Sale Agent ?.
		@return Sale Agent
	  */
	public boolean issaleagent () 
	{
		Object oo = get_Value(COLUMNNAME_issaleagent);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Sales Representative.
		@param IsSalesRep 
		Indicates if  the business partner is a sales representative or company agent
	  */
	public void setIsSalesRep (boolean IsSalesRep)
	{
		set_Value (COLUMNNAME_IsSalesRep, Boolean.valueOf(IsSalesRep));
	}

	/** Get Sales Representative.
		@return Indicates if  the business partner is a sales representative or company agent
	  */
	public boolean isSalesRep () 
	{
		Object oo = get_Value(COLUMNNAME_IsSalesRep);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isstoresupplier.
		@param isstoresupplier isstoresupplier	  */
	public void setisstoresupplier (boolean isstoresupplier)
	{
		set_Value (COLUMNNAME_isstoresupplier, Boolean.valueOf(isstoresupplier));
	}

	/** Get isstoresupplier.
		@return isstoresupplier	  */
	public boolean isstoresupplier () 
	{
		Object oo = get_Value(COLUMNNAME_isstoresupplier);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Summary Level.
		@param IsSummary 
		This is a summary entity
	  */
	public void setIsSummary (boolean IsSummary)
	{
		set_Value (COLUMNNAME_IsSummary, Boolean.valueOf(IsSummary));
	}

	/** Get Summary Level.
		@return This is a summary entity
	  */
	public boolean isSummary () 
	{
		Object oo = get_Value(COLUMNNAME_IsSummary);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SO Tax exempt.
		@param IsTaxExempt 
		Business partner is exempt from tax on sales
	  */
	public void setIsTaxExempt (boolean IsTaxExempt)
	{
		set_Value (COLUMNNAME_IsTaxExempt, Boolean.valueOf(IsTaxExempt));
	}

	/** Get SO Tax exempt.
		@return Business partner is exempt from tax on sales
	  */
	public boolean isTaxExempt () 
	{
		Object oo = get_Value(COLUMNNAME_IsTaxExempt);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IS TDS Applicable.
		@param istdsapplicable 
		Is TDS applicable for the Business Partner
	  */
	public void setistdsapplicable (boolean istdsapplicable)
	{
		set_Value (COLUMNNAME_istdsapplicable, Boolean.valueOf(istdsapplicable));
	}

	/** Get IS TDS Applicable.
		@return Is TDS applicable for the Business Partner
	  */
	public boolean istdsapplicable () 
	{
		Object oo = get_Value(COLUMNNAME_istdsapplicable);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set istransporter.
		@param istransporter istransporter	  */
	public void setistransporter (boolean istransporter)
	{
		set_Value (COLUMNNAME_istransporter, Boolean.valueOf(istransporter));
	}

	/** Get istransporter.
		@return istransporter	  */
	public boolean istransporter () 
	{
		Object oo = get_Value(COLUMNNAME_istransporter);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Use Tax Id Digit.
		@param IsUseTaxIdDigit Use Tax Id Digit	  */
	public void setIsUseTaxIdDigit (boolean IsUseTaxIdDigit)
	{
		set_Value (COLUMNNAME_IsUseTaxIdDigit, Boolean.valueOf(IsUseTaxIdDigit));
	}

	/** Get Use Tax Id Digit.
		@return Use Tax Id Digit	  */
	public boolean isUseTaxIdDigit () 
	{
		Object oo = get_Value(COLUMNNAME_IsUseTaxIdDigit);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Vendor.
		@param IsVendor 
		Indicates if this Business Partner is a Vendor
	  */
	public void setIsVendor (boolean IsVendor)
	{
		set_Value (COLUMNNAME_IsVendor, Boolean.valueOf(IsVendor));
	}

	/** Get Vendor.
		@return Indicates if this Business Partner is a Vendor
	  */
	public boolean isVendor () 
	{
		Object oo = get_Value(COLUMNNAME_IsVendor);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Joining Date.
		@param joiningdate 
		Joining Date
	  */
	public void setjoiningdate (Timestamp joiningdate)
	{
		set_Value (COLUMNNAME_joiningdate, joiningdate);
	}

	/** Get Joining Date.
		@return Joining Date
	  */
	public Timestamp getjoiningdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_joiningdate);
	}

	/** Set Last Name 1.
		@param LastName1 Last Name 1	  */
	public void setLastName1 (String LastName1)
	{
		set_Value (COLUMNNAME_LastName1, LastName1);
	}

	/** Get Last Name 1.
		@return Last Name 1	  */
	public String getLastName1 () 
	{
		return (String)get_Value(COLUMNNAME_LastName1);
	}

	/** Set Last Name 2.
		@param LastName2 Last Name 2	  */
	public void setLastName2 (String LastName2)
	{
		set_Value (COLUMNNAME_LastName2, LastName2);
	}

	/** Get Last Name 2.
		@return Last Name 2	  */
	public String getLastName2 () 
	{
		return (String)get_Value(COLUMNNAME_LastName2);
	}

	/** Set Last Paid Date.
		@param lastpaiddate 
		Last Commission Paid Date
	  */
	public void setlastpaiddate (Timestamp lastpaiddate)
	{
		set_Value (COLUMNNAME_lastpaiddate, lastpaiddate);
	}

	/** Get Last Paid Date.
		@return Last Commission Paid Date
	  */
	public Timestamp getlastpaiddate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_lastpaiddate);
	}

	/** Set ISIC.
		@param LCO_ISIC_ID ISIC	  */
	public void setLCO_ISIC_ID (int LCO_ISIC_ID)
	{
		if (LCO_ISIC_ID < 1) 
			set_Value (COLUMNNAME_LCO_ISIC_ID, null);
		else 
			set_Value (COLUMNNAME_LCO_ISIC_ID, Integer.valueOf(LCO_ISIC_ID));
	}

	/** Get ISIC.
		@return ISIC	  */
	public int getLCO_ISIC_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_ISIC_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Tax ID Type.
		@param LCO_TaxIdType_ID Tax ID Type	  */
	public void setLCO_TaxIdType_ID (int LCO_TaxIdType_ID)
	{
		if (LCO_TaxIdType_ID < 1) 
			set_Value (COLUMNNAME_LCO_TaxIdType_ID, null);
		else 
			set_Value (COLUMNNAME_LCO_TaxIdType_ID, Integer.valueOf(LCO_TaxIdType_ID));
	}

	/** Get Tax ID Type.
		@return Tax ID Type	  */
	public int getLCO_TaxIdType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_TaxIdType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Deductee Type.
		@param LCO_TaxPayerType_ID 
		TDS deduction type for the Business Partner
	  */
	public void setLCO_TaxPayerType_ID (int LCO_TaxPayerType_ID)
	{
		if (LCO_TaxPayerType_ID < 1) 
			set_Value (COLUMNNAME_LCO_TaxPayerType_ID, null);
		else 
			set_Value (COLUMNNAME_LCO_TaxPayerType_ID, Integer.valueOf(LCO_TaxPayerType_ID));
	}

	/** Get Deductee Type.
		@return TDS deduction type for the Business Partner
	  */
	public int getLCO_TaxPayerType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_TaxPayerType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_M_DiscountSchema getM_DiscountSchema() throws RuntimeException
    {
		return (I_M_DiscountSchema)MTable.get(getCtx(), I_M_DiscountSchema.Table_Name)
			.getPO(getM_DiscountSchema_ID(), get_TrxName());	}

	/** Set Discount Schema.
		@param M_DiscountSchema_ID 
		Schema to calculate the trade discount percentage
	  */
	public void setM_DiscountSchema_ID (int M_DiscountSchema_ID)
	{
		if (M_DiscountSchema_ID < 1) 
			set_Value (COLUMNNAME_M_DiscountSchema_ID, null);
		else 
			set_Value (COLUMNNAME_M_DiscountSchema_ID, Integer.valueOf(M_DiscountSchema_ID));
	}

	/** Get Discount Schema.
		@return Schema to calculate the trade discount percentage
	  */
	public int getM_DiscountSchema_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_DiscountSchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set mercantilebuyer.
		@param mercantilebuyer mercantilebuyer	  */
	public void setmercantilebuyer (boolean mercantilebuyer)
	{
		set_Value (COLUMNNAME_mercantilebuyer, Boolean.valueOf(mercantilebuyer));
	}

	/** Get mercantilebuyer.
		@return mercantilebuyer	  */
	public boolean ismercantilebuyer () 
	{
		Object oo = get_Value(COLUMNNAME_mercantilebuyer);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_M_PriceList getM_PriceList() throws RuntimeException
    {
		return (I_M_PriceList)MTable.get(getCtx(), I_M_PriceList.Table_Name)
			.getPO(getM_PriceList_ID(), get_TrxName());	}

	/** Set Price List.
		@param M_PriceList_ID 
		Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		if (M_PriceList_ID < 1) 
			set_Value (COLUMNNAME_M_PriceList_ID, null);
		else 
			set_Value (COLUMNNAME_M_PriceList_ID, Integer.valueOf(M_PriceList_ID));
	}

	/** Get Price List.
		@return Unique identifier of a Price List
	  */
	public int getM_PriceList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PriceList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set NAICS/SIC.
		@param NAICS 
		Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public void setNAICS (String NAICS)
	{
		set_Value (COLUMNNAME_NAICS, NAICS);
	}

	/** Get NAICS/SIC.
		@return Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public String getNAICS () 
	{
		return (String)get_Value(COLUMNNAME_NAICS);
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

	/** Set Number of employees.
		@param NumberEmployees 
		Number of employees
	  */
	public void setNumberEmployees (int NumberEmployees)
	{
		set_Value (COLUMNNAME_NumberEmployees, Integer.valueOf(NumberEmployees));
	}

	/** Get Number of employees.
		@return Number of employees
	  */
	public int getNumberEmployees () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NumberEmployees);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PAN Number .
		@param pannumber 
		PAN Number 
	  */
	public void setpannumber (String pannumber)
	{
		set_Value (COLUMNNAME_pannumber, pannumber);
	}

	/** Get PAN Number .
		@return PAN Number 
	  */
	public String getpannumber () 
	{
		return (String)get_Value(COLUMNNAME_pannumber);
	}

	/** PaymentRule AD_Reference_ID=195 */
	public static final int PAYMENTRULE_AD_Reference_ID=195;
	/** Cash = B */
	public static final String PAYMENTRULE_Cash = "B";
	/** Credit Card = K */
	public static final String PAYMENTRULE_CreditCard = "K";
	/** Direct Deposit = T */
	public static final String PAYMENTRULE_DirectDeposit = "T";
	/** Check = S */
	public static final String PAYMENTRULE_Check = "S";
	/** On Credit = P */
	public static final String PAYMENTRULE_OnCredit = "P";
	/** Direct Debit = D */
	public static final String PAYMENTRULE_DirectDebit = "D";
	/** Mixed = M */
	public static final String PAYMENTRULE_Mixed = "M";
	/** Set Payment Rule.
		@param PaymentRule 
		How you pay the invoice
	  */
	public void setPaymentRule (String PaymentRule)
	{

		set_Value (COLUMNNAME_PaymentRule, PaymentRule);
	}

	/** Get Payment Rule.
		@return How you pay the invoice
	  */
	public String getPaymentRule () 
	{
		return (String)get_Value(COLUMNNAME_PaymentRule);
	}

	/** PaymentRulePO AD_Reference_ID=195 */
	public static final int PAYMENTRULEPO_AD_Reference_ID=195;
	/** Cash = B */
	public static final String PAYMENTRULEPO_Cash = "B";
	/** Credit Card = K */
	public static final String PAYMENTRULEPO_CreditCard = "K";
	/** Direct Deposit = T */
	public static final String PAYMENTRULEPO_DirectDeposit = "T";
	/** Check = S */
	public static final String PAYMENTRULEPO_Check = "S";
	/** On Credit = P */
	public static final String PAYMENTRULEPO_OnCredit = "P";
	/** Direct Debit = D */
	public static final String PAYMENTRULEPO_DirectDebit = "D";
	/** Mixed = M */
	public static final String PAYMENTRULEPO_Mixed = "M";
	/** Set Payment Rule.
		@param PaymentRulePO 
		Purchase payment option
	  */
	public void setPaymentRulePO (String PaymentRulePO)
	{

		set_Value (COLUMNNAME_PaymentRulePO, PaymentRulePO);
	}

	/** Get Payment Rule.
		@return Purchase payment option
	  */
	public String getPaymentRulePO () 
	{
		return (String)get_Value(COLUMNNAME_PaymentRulePO);
	}

	public I_M_DiscountSchema getPO_DiscountSchema() throws RuntimeException
    {
		return (I_M_DiscountSchema)MTable.get(getCtx(), I_M_DiscountSchema.Table_Name)
			.getPO(getPO_DiscountSchema_ID(), get_TrxName());	}

	/** Set PO Discount Schema.
		@param PO_DiscountSchema_ID 
		Schema to calculate the purchase trade discount percentage
	  */
	public void setPO_DiscountSchema_ID (int PO_DiscountSchema_ID)
	{
		if (PO_DiscountSchema_ID < 1) 
			set_Value (COLUMNNAME_PO_DiscountSchema_ID, null);
		else 
			set_Value (COLUMNNAME_PO_DiscountSchema_ID, Integer.valueOf(PO_DiscountSchema_ID));
	}

	/** Get PO Discount Schema.
		@return Schema to calculate the purchase trade discount percentage
	  */
	public int getPO_DiscountSchema_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PO_DiscountSchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_PaymentTerm getPO_PaymentTerm() throws RuntimeException
    {
		return (I_C_PaymentTerm)MTable.get(getCtx(), I_C_PaymentTerm.Table_Name)
			.getPO(getPO_PaymentTerm_ID(), get_TrxName());	}

	/** Set PO Payment Term.
		@param PO_PaymentTerm_ID 
		Payment rules for a purchase order
	  */
	public void setPO_PaymentTerm_ID (int PO_PaymentTerm_ID)
	{
		if (PO_PaymentTerm_ID < 1) 
			set_Value (COLUMNNAME_PO_PaymentTerm_ID, null);
		else 
			set_Value (COLUMNNAME_PO_PaymentTerm_ID, Integer.valueOf(PO_PaymentTerm_ID));
	}

	/** Get PO Payment Term.
		@return Payment rules for a purchase order
	  */
	public int getPO_PaymentTerm_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PO_PaymentTerm_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_PriceList getPO_PriceList() throws RuntimeException
    {
		return (I_M_PriceList)MTable.get(getCtx(), I_M_PriceList.Table_Name)
			.getPO(getPO_PriceList_ID(), get_TrxName());	}

	/** Set Purchase Pricelist.
		@param PO_PriceList_ID 
		Price List used by this Business Partner
	  */
	public void setPO_PriceList_ID (int PO_PriceList_ID)
	{
		if (PO_PriceList_ID < 1) 
			set_Value (COLUMNNAME_PO_PriceList_ID, null);
		else 
			set_Value (COLUMNNAME_PO_PriceList_ID, Integer.valueOf(PO_PriceList_ID));
	}

	/** Get Purchase Pricelist.
		@return Price List used by this Business Partner
	  */
	public int getPO_PriceList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PO_PriceList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Order Reference.
		@param POReference 
		Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	public void setPOReference (String POReference)
	{
		set_Value (COLUMNNAME_POReference, POReference);
	}

	/** Get Order Reference.
		@return Transaction Reference Number (Sales Order, Purchase Order) of your Business Partner
	  */
	public String getPOReference () 
	{
		return (String)get_Value(COLUMNNAME_POReference);
	}

	/** Set Potential Life Time Value.
		@param PotentialLifeTimeValue 
		Total Revenue expected
	  */
	public void setPotentialLifeTimeValue (BigDecimal PotentialLifeTimeValue)
	{
		set_Value (COLUMNNAME_PotentialLifeTimeValue, PotentialLifeTimeValue);
	}

	/** Get Potential Life Time Value.
		@return Total Revenue expected
	  */
	public BigDecimal getPotentialLifeTimeValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PotentialLifeTimeValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Purchase Commission.
		@param purchasecomm 
		Purchase Commission
	  */
	public void setpurchasecomm (BigDecimal purchasecomm)
	{
		set_Value (COLUMNNAME_purchasecomm, purchasecomm);
	}

	/** Get Purchase Commission.
		@return Purchase Commission
	  */
	public BigDecimal getpurchasecomm () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_purchasecomm);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rating.
		@param Rating 
		Classification or Importance
	  */
	public void setRating (String Rating)
	{
		set_Value (COLUMNNAME_Rating, Rating);
	}

	/** Get Rating.
		@return Classification or Importance
	  */
	public String getRating () 
	{
		return (String)get_Value(COLUMNNAME_Rating);
	}

	/** Set Rc Number.
		@param rcnumber Rc Number	  */
	public void setrcnumber (String rcnumber)
	{
		set_Value (COLUMNNAME_rcnumber, rcnumber);
	}

	/** Get Rc Number.
		@return Rc Number	  */
	public String getrcnumber () 
	{
		return (String)get_Value(COLUMNNAME_rcnumber);
	}

	/** Set Reference No.
		@param ReferenceNo 
		Your customer or vendor number at the Business Partner's site
	  */
	public void setReferenceNo (String ReferenceNo)
	{
		set_Value (COLUMNNAME_ReferenceNo, ReferenceNo);
	}

	/** Get Reference No.
		@return Your customer or vendor number at the Business Partner's site
	  */
	public String getReferenceNo () 
	{
		return (String)get_Value(COLUMNNAME_ReferenceNo);
	}

	/** Set Relieve Employee.
		@param relieveemployee 
		Relieve Employee
	  */
	public void setrelieveemployee (String relieveemployee)
	{
		set_Value (COLUMNNAME_relieveemployee, relieveemployee);
	}

	/** Get Relieve Employee.
		@return Relieve Employee
	  */
	public String getrelieveemployee () 
	{
		return (String)get_Value(COLUMNNAME_relieveemployee);
	}

	/** Set Relieving Date.
		@param relievingdate 
		Relieving Date
	  */
	public void setrelievingdate (Timestamp relievingdate)
	{
		set_Value (COLUMNNAME_relievingdate, relievingdate);
	}

	/** Get Relieving Date.
		@return Relieving Date
	  */
	public Timestamp getrelievingdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_relievingdate);
	}

	/** Set removedfromprobationdate.
		@param removedfromprobationdate removedfromprobationdate	  */
	public void setremovedfromprobationdate (Timestamp removedfromprobationdate)
	{
		set_Value (COLUMNNAME_removedfromprobationdate, removedfromprobationdate);
	}

	/** Get removedfromprobationdate.
		@return removedfromprobationdate	  */
	public Timestamp getremovedfromprobationdate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_removedfromprobationdate);
	}

	/** Set Sale Commission.
		@param salecomm 
		Sale Commission
	  */
	public void setsalecomm (BigDecimal salecomm)
	{
		set_Value (COLUMNNAME_salecomm, salecomm);
	}

	/** Get Sale Commission.
		@return Sale Commission
	  */
	public BigDecimal getsalecomm () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_salecomm);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_AD_User getSalesRep() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_Value (COLUMNNAME_SalesRep_ID, null);
		else 
			set_Value (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sales Volume in 1.000.
		@param SalesVolume 
		Total Volume of Sales in Thousands of Currency
	  */
	public void setSalesVolume (int SalesVolume)
	{
		set_Value (COLUMNNAME_SalesVolume, Integer.valueOf(SalesVolume));
	}

	/** Get Sales Volume in 1.000.
		@return Total Volume of Sales in Thousands of Currency
	  */
	public int getSalesVolume () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesVolume);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Send EMail.
		@param SendEMail 
		Enable sending Document EMail
	  */
	public void setSendEMail (boolean SendEMail)
	{
		set_Value (COLUMNNAME_SendEMail, Boolean.valueOf(SendEMail));
	}

	/** Get Send EMail.
		@return Enable sending Document EMail
	  */
	public boolean isSendEMail () 
	{
		Object oo = get_Value(COLUMNNAME_SendEMail);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Share.
		@param ShareOfCustomer 
		Share of Customer's business as a percentage
	  */
	public void setShareOfCustomer (int ShareOfCustomer)
	{
		set_Value (COLUMNNAME_ShareOfCustomer, Integer.valueOf(ShareOfCustomer));
	}

	/** Get Share.
		@return Share of Customer's business as a percentage
	  */
	public int getShareOfCustomer () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ShareOfCustomer);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Min Shelf Life %.
		@param ShelfLifeMinPct 
		Minimum Shelf Life in percent based on Product Instance Guarantee Date
	  */
	public void setShelfLifeMinPct (int ShelfLifeMinPct)
	{
		set_Value (COLUMNNAME_ShelfLifeMinPct, Integer.valueOf(ShelfLifeMinPct));
	}

	/** Get Min Shelf Life %.
		@return Minimum Shelf Life in percent based on Product Instance Guarantee Date
	  */
	public int getShelfLifeMinPct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ShelfLifeMinPct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Credit Limit.
		@param SO_CreditLimit 
		Total outstanding invoice amounts allowed
	  */
	public void setSO_CreditLimit (BigDecimal SO_CreditLimit)
	{
		set_Value (COLUMNNAME_SO_CreditLimit, SO_CreditLimit);
	}

	/** Get Credit Limit.
		@return Total outstanding invoice amounts allowed
	  */
	public BigDecimal getSO_CreditLimit () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SO_CreditLimit);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** SOCreditStatus AD_Reference_ID=289 */
	public static final int SOCREDITSTATUS_AD_Reference_ID=289;
	/** Credit Stop = S */
	public static final String SOCREDITSTATUS_CreditStop = "S";
	/** Credit Hold = H */
	public static final String SOCREDITSTATUS_CreditHold = "H";
	/** Credit Watch = W */
	public static final String SOCREDITSTATUS_CreditWatch = "W";
	/** No Credit Check = X */
	public static final String SOCREDITSTATUS_NoCreditCheck = "X";
	/** Credit OK = O */
	public static final String SOCREDITSTATUS_CreditOK = "O";
	/** Set Credit Status.
		@param SOCreditStatus 
		Business Partner Credit Status
	  */
	public void setSOCreditStatus (String SOCreditStatus)
	{

		set_Value (COLUMNNAME_SOCreditStatus, SOCreditStatus);
	}

	/** Get Credit Status.
		@return Business Partner Credit Status
	  */
	public String getSOCreditStatus () 
	{
		return (String)get_Value(COLUMNNAME_SOCreditStatus);
	}

	/** Set Credit Used.
		@param SO_CreditUsed 
		Current open balance
	  */
	public void setSO_CreditUsed (BigDecimal SO_CreditUsed)
	{
		set_ValueNoCheck (COLUMNNAME_SO_CreditUsed, SO_CreditUsed);
	}

	/** Get Credit Used.
		@return Current open balance
	  */
	public BigDecimal getSO_CreditUsed () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SO_CreditUsed);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Order Description.
		@param SO_Description 
		Description to be used on orders
	  */
	public void setSO_Description (String SO_Description)
	{
		set_Value (COLUMNNAME_SO_Description, SO_Description);
	}

	/** Get Order Description.
		@return Description to be used on orders
	  */
	public String getSO_Description () 
	{
		return (String)get_Value(COLUMNNAME_SO_Description);
	}

	/** Set Tax ID.
		@param TaxID 
		Tax Identification
	  */
	public void setTaxID (String TaxID)
	{
		set_Value (COLUMNNAME_TaxID, TaxID);
	}

	/** Get Tax ID.
		@return Tax Identification
	  */
	public String getTaxID () 
	{
		return (String)get_Value(COLUMNNAME_TaxID);
	}

	/** Set Tax ID Digit.
		@param TaxIdDigit Tax ID Digit	  */
	public void setTaxIdDigit (String TaxIdDigit)
	{
		set_Value (COLUMNNAME_TaxIdDigit, TaxIdDigit);
	}

	/** Get Tax ID Digit.
		@return Tax ID Digit	  */
	public String getTaxIdDigit () 
	{
		return (String)get_Value(COLUMNNAME_TaxIdDigit);
	}

	/** Set Tin Number.
		@param tinnumber 
		Tin Number
	  */
	public void settinnumber (String tinnumber)
	{
		set_Value (COLUMNNAME_tinnumber, tinnumber);
	}

	/** Get Tin Number.
		@return Tin Number
	  */
	public String gettinnumber () 
	{
		return (String)get_Value(COLUMNNAME_tinnumber);
	}

	/** Set Open Balance.
		@param TotalOpenBalance 
		Total Open Balance Amount in primary Accounting Currency
	  */
	public void setTotalOpenBalance (BigDecimal TotalOpenBalance)
	{
		set_Value (COLUMNNAME_TotalOpenBalance, TotalOpenBalance);
	}

	/** Get Open Balance.
		@return Total Open Balance Amount in primary Accounting Currency
	  */
	public BigDecimal getTotalOpenBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalOpenBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set totnumber.
		@param totnumber totnumber	  */
	public void settotnumber (String totnumber)
	{
		set_Value (COLUMNNAME_totnumber, totnumber);
	}

	/** Get totnumber.
		@return totnumber	  */
	public String gettotnumber () 
	{
		return (String)get_Value(COLUMNNAME_totnumber);
	}

	/** Set URL.
		@param URL 
		Full URL address - e.g. http://www.adempiere.org
	  */
	public void setURL (String URL)
	{
		set_Value (COLUMNNAME_URL, URL);
	}

	/** Get URL.
		@return Full URL address - e.g. http://www.adempiere.org
	  */
	public String getURL () 
	{
		return (String)get_Value(COLUMNNAME_URL);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** weeklyoff AD_Reference_ID=167 */
	public static final int WEEKLYOFF_AD_Reference_ID=167;
	/** Sunday = 1 */
	public static final String WEEKLYOFF_Sunday = "1";
	/** Monday = 2 */
	public static final String WEEKLYOFF_Monday = "2";
	/** Tuesday = 3 */
	public static final String WEEKLYOFF_Tuesday = "3";
	/** Wednesday = 4 */
	public static final String WEEKLYOFF_Wednesday = "4";
	/** Thursday = 5 */
	public static final String WEEKLYOFF_Thursday = "5";
	/** Friday = 6 */
	public static final String WEEKLYOFF_Friday = "6";
	/** Saturday = 7 */
	public static final String WEEKLYOFF_Saturday = "7";
	/** Set Weekly Off.
		@param weeklyoff 
		Weekly Off
	  */
	public void setweeklyoff (String weeklyoff)
	{

		set_Value (COLUMNNAME_weeklyoff, weeklyoff);
	}

	/** Get Weekly Off.
		@return Weekly Off
	  */
	public String getweeklyoff () 
	{
		return (String)get_Value(COLUMNNAME_weeklyoff);
	}

	/** Set TDS_DeducteeType.
		@param wtc_tds_deducteetype_ID TDS_DeducteeType	  */
	public void setwtc_tds_deducteetype_ID (int wtc_tds_deducteetype_ID)
	{
		if (wtc_tds_deducteetype_ID < 1) 
			set_Value (COLUMNNAME_wtc_tds_deducteetype_ID, null);
		else 
			set_Value (COLUMNNAME_wtc_tds_deducteetype_ID, Integer.valueOf(wtc_tds_deducteetype_ID));
	}

	/** Get TDS_DeducteeType.
		@return TDS_DeducteeType	  */
	public int getwtc_tds_deducteetype_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_wtc_tds_deducteetype_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}