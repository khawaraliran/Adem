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

/** Generated Model for WTC_Inward_Gate_Pass
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_WTC_Inward_Gate_Pass.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_WTC_Inward_Gate_Pass extends PO implements I_WTC_Inward_Gate_Pass, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_WTC_Inward_Gate_Pass.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111215L;

    /** Standard Constructor */
    public X_WTC_Inward_Gate_Pass (Properties ctx, int WTC_Inward_Gate_Pass_ID, String trxName)
    {
      super (ctx, WTC_Inward_Gate_Pass_ID, trxName);
      /** if (WTC_Inward_Gate_Pass_ID == 0)
        {
			setdateandtime (new Timestamp( System.currentTimeMillis() ));
			setinward_type (null);
// m
			setWTC_Inward_Gate_Pass_ID (0);
        } */
    }

    /** Load Constructor */
    public X_WTC_Inward_Gate_Pass (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_WTC_Inward_Gate_Pass[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Actual Weight.
		@param actual_weight 
		Actual Weight
	  */
	public void setactual_weight (BigDecimal actual_weight)
	{
		set_Value (COLUMNNAME_actual_weight, actual_weight);
	}

	/** Get Actual Weight.
		@return Actual Weight
	  */
	public BigDecimal getactual_weight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_actual_weight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set buttoninward.
		@param buttoninward buttoninward	  */
	public void setbuttoninward (String buttoninward)
	{
		set_Value (COLUMNNAME_buttoninward, buttoninward);
	}

	/** Get buttoninward.
		@return buttoninward	  */
	public String getbuttoninward () 
	{
		return (String)get_Value(COLUMNNAME_buttoninward);
	}

	public I_C_Location getC_Location() throws RuntimeException
    {
		return (I_C_Location)MTable.get(getCtx(), I_C_Location.Table_Name)
			.getPO(getC_Location_ID(), get_TrxName());	}

	/** Set Address of The Visitor.
		@param C_Location_ID 
		Address of The Visitor
	  */
	public void setC_Location_ID (int C_Location_ID)
	{
		if (C_Location_ID < 1) 
			set_Value (COLUMNNAME_C_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_Location_ID, Integer.valueOf(C_Location_ID));
	}

	/** Get Address of The Visitor.
		@return Address of The Visitor
	  */
	public int getC_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Company.
		@param company 
		Previous working Company Name(Organization)
	  */
	public void setcompany (String company)
	{
		set_Value (COLUMNNAME_company, company);
	}

	/** Get Company.
		@return Previous working Company Name(Organization)
	  */
	public String getcompany () 
	{
		return (String)get_Value(COLUMNNAME_company);
	}

	public I_C_Order getC_Order() throws RuntimeException
    {
		return (I_C_Order)MTable.get(getCtx(), I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_Value (COLUMNNAME_C_Order_ID, null);
		else 
			set_Value (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date And Time.
		@param dateandtime 
		Date and Time on whicn in inward gate pass is being prepared
	  */
	public void setdateandtime (Timestamp dateandtime)
	{
		set_Value (COLUMNNAME_dateandtime, dateandtime);
	}

	/** Get Date And Time.
		@return Date and Time on whicn in inward gate pass is being prepared
	  */
	public Timestamp getdateandtime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_dateandtime);
	}

	/** Set Exit Date And Time.
		@param exitdateandtime 
		Date and Time on which vehicle or Person is going out of office premises
	  */
	public void setexitdateandtime (Timestamp exitdateandtime)
	{
		set_Value (COLUMNNAME_exitdateandtime, exitdateandtime);
	}

	/** Get Exit Date And Time.
		@return Date and Time on which vehicle or Person is going out of office premises
	  */
	public Timestamp getexitdateandtime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_exitdateandtime);
	}

	/** Set Gross Weight.
		@param gross_weight 
		Total Weight of the Vehicle
	  */
	public void setgross_weight (BigDecimal gross_weight)
	{
		set_Value (COLUMNNAME_gross_weight, gross_weight);
	}

	/** Get Gross Weight.
		@return Total Weight of the Vehicle
	  */
	public BigDecimal getgross_weight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_gross_weight);
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

	public I_HR_Designation getHR_Designation() throws RuntimeException
    {
		return (I_HR_Designation)MTable.get(getCtx(), I_HR_Designation.Table_Name)
			.getPO(getHR_Designation_ID(), get_TrxName());	}

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

	/** Set in_ward_reference_number.
		@param in_ward_reference_number in_ward_reference_number	  */
	public void setin_ward_reference_number (String in_ward_reference_number)
	{
		set_Value (COLUMNNAME_in_ward_reference_number, in_ward_reference_number);
	}

	/** Get in_ward_reference_number.
		@return in_ward_reference_number	  */
	public String getin_ward_reference_number () 
	{
		return (String)get_Value(COLUMNNAME_in_ward_reference_number);
	}

	/** Set inward_serial_number.
		@param inward_serial_number inward_serial_number	  */
	public void setinward_serial_number (int inward_serial_number)
	{
		set_Value (COLUMNNAME_inward_serial_number, Integer.valueOf(inward_serial_number));
	}

	/** Get inward_serial_number.
		@return inward_serial_number	  */
	public int getinward_serial_number () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_inward_serial_number);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** inward_type AD_Reference_ID=6000005 */
	public static final int INWARD_TYPE_AD_Reference_ID=6000005;
	/** Material = m */
	public static final String INWARD_TYPE_Material = "m";
	/** Person = p */
	public static final String INWARD_TYPE_Person = "p";
	/** Set Inward Type.
		@param inward_type 
		Type of the Inward i.e. whether people are coming in and material is being received
	  */
	public void setinward_type (String inward_type)
	{

		set_Value (COLUMNNAME_inward_type, inward_type);
	}

	/** Get Inward Type.
		@return Type of the Inward i.e. whether people are coming in and material is being received
	  */
	public String getinward_type () 
	{
		return (String)get_Value(COLUMNNAME_inward_type);
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

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Need Weight Check?.
		@param need_weight_check 
		Need Weight Check
	  */
	public void setneed_weight_check (boolean need_weight_check)
	{
		set_Value (COLUMNNAME_need_weight_check, Boolean.valueOf(need_weight_check));
	}

	/** Get Need Weight Check?.
		@return Need Weight Check
	  */
	public boolean isneed_weight_check () 
	{
		Object oo = get_Value(COLUMNNAME_need_weight_check);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set No Of Persons.
		@param no_of_persons 
		Number of Persons that are visiting 
	  */
	public void setno_of_persons (int no_of_persons)
	{
		set_Value (COLUMNNAME_no_of_persons, Integer.valueOf(no_of_persons));
	}

	/** Get No Of Persons.
		@return Number of Persons that are visiting 
	  */
	public int getno_of_persons () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_no_of_persons);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Phone Number.
		@param phone_number 
		Phone Number of the Visitor
	  */
	public void setphone_number (String phone_number)
	{
		set_Value (COLUMNNAME_phone_number, phone_number);
	}

	/** Get Phone Number.
		@return Phone Number of the Visitor
	  */
	public String getphone_number () 
	{
		return (String)get_Value(COLUMNNAME_phone_number);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Purpose Of  The Visit.
		@param purpose_of_the_visit 
		Purpose Of  The Visit
	  */
	public void setpurpose_of_the_visit (String purpose_of_the_visit)
	{
		set_Value (COLUMNNAME_purpose_of_the_visit, purpose_of_the_visit);
	}

	/** Get Purpose Of  The Visit.
		@return Purpose Of  The Visit
	  */
	public String getpurpose_of_the_visit () 
	{
		return (String)get_Value(COLUMNNAME_purpose_of_the_visit);
	}

	public I_C_BPartner getsupplier_name() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getsupplier_name_id(), get_TrxName());	}

	/** Set Supplier Name.
		@param supplier_name_id 
		Name of the Supplier
	  */
	public void setsupplier_name_id (int supplier_name_id)
	{
		set_Value (COLUMNNAME_supplier_name_id, Integer.valueOf(supplier_name_id));
	}

	/** Get Supplier Name.
		@return Name of the Supplier
	  */
	public int getsupplier_name_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_supplier_name_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Tare Weight.
		@param tare_weight 
		Empty Vehicles Weight
	  */
	public void settare_weight (BigDecimal tare_weight)
	{
		set_Value (COLUMNNAME_tare_weight, tare_weight);
	}

	/** Get Tare Weight.
		@return Empty Vehicles Weight
	  */
	public BigDecimal gettare_weight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_tare_weight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Vehicle Number.
		@param vehicle_number 
		Vehicle Number
	  */
	public void setvehicle_number (String vehicle_number)
	{
		set_Value (COLUMNNAME_vehicle_number, vehicle_number);
	}

	/** Get Vehicle Number.
		@return Vehicle Number
	  */
	public String getvehicle_number () 
	{
		return (String)get_Value(COLUMNNAME_vehicle_number);
	}

	/** Set Visitor Name.
		@param visitor_name 
		Name of the Visitor
	  */
	public void setvisitor_name (String visitor_name)
	{
		set_Value (COLUMNNAME_visitor_name, visitor_name);
	}

	/** Get Visitor Name.
		@return Name of the Visitor
	  */
	public String getvisitor_name () 
	{
		return (String)get_Value(COLUMNNAME_visitor_name);
	}

	public I_C_BPartner getwho_to_meet() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getwho_to_meet_id(), get_TrxName());	}

	/** Set Who_To_Meet.
		@param who_to_meet_id 
		Employee in the company with whom e visitor want to meet
	  */
	public void setwho_to_meet_id (int who_to_meet_id)
	{
		set_Value (COLUMNNAME_who_to_meet_id, Integer.valueOf(who_to_meet_id));
	}

	/** Get Who_To_Meet.
		@return Employee in the company with whom e visitor want to meet
	  */
	public int getwho_to_meet_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_who_to_meet_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_WTC_Gate_Pass_Status getWTC_Gate_Pass_Status() throws RuntimeException
    {
		return (I_WTC_Gate_Pass_Status)MTable.get(getCtx(), I_WTC_Gate_Pass_Status.Table_Name)
			.getPO(getWTC_Gate_Pass_Status_ID(), get_TrxName());	}

	/** Set GatePass Status.
		@param WTC_Gate_Pass_Status_ID GatePass Status	  */
	public void setWTC_Gate_Pass_Status_ID (int WTC_Gate_Pass_Status_ID)
	{
		if (WTC_Gate_Pass_Status_ID < 1) 
			set_Value (COLUMNNAME_WTC_Gate_Pass_Status_ID, null);
		else 
			set_Value (COLUMNNAME_WTC_Gate_Pass_Status_ID, Integer.valueOf(WTC_Gate_Pass_Status_ID));
	}

	/** Get GatePass Status.
		@return GatePass Status	  */
	public int getWTC_Gate_Pass_Status_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_Gate_Pass_Status_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Inward Gate Pass.
		@param WTC_Inward_Gate_Pass_ID Inward Gate Pass	  */
	public void setWTC_Inward_Gate_Pass_ID (int WTC_Inward_Gate_Pass_ID)
	{
		if (WTC_Inward_Gate_Pass_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WTC_Inward_Gate_Pass_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WTC_Inward_Gate_Pass_ID, Integer.valueOf(WTC_Inward_Gate_Pass_ID));
	}

	/** Get Inward Gate Pass.
		@return Inward Gate Pass	  */
	public int getWTC_Inward_Gate_Pass_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_Inward_Gate_Pass_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_WTC_Material_Inward_Type getWTC_Material_Inward_Type() throws RuntimeException
    {
		return (I_WTC_Material_Inward_Type)MTable.get(getCtx(), I_WTC_Material_Inward_Type.Table_Name)
			.getPO(getWTC_Material_Inward_Type_ID(), get_TrxName());	}

	/** Set Material Inward Type.
		@param WTC_Material_Inward_Type_ID Material Inward Type	  */
	public void setWTC_Material_Inward_Type_ID (int WTC_Material_Inward_Type_ID)
	{
		if (WTC_Material_Inward_Type_ID < 1) 
			set_Value (COLUMNNAME_WTC_Material_Inward_Type_ID, null);
		else 
			set_Value (COLUMNNAME_WTC_Material_Inward_Type_ID, Integer.valueOf(WTC_Material_Inward_Type_ID));
	}

	/** Get Material Inward Type.
		@return Material Inward Type	  */
	public int getWTC_Material_Inward_Type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_Material_Inward_Type_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}