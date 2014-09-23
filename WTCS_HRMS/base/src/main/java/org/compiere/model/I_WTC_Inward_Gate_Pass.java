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

/** Generated Interface for WTC_Inward_Gate_Pass
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_WTC_Inward_Gate_Pass 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_WTC_Inward_Gate_Pass.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=WTC_Inward_Gate_Pass */
    public static final String Table_Name = "WTC_Inward_Gate_Pass";

    /** AD_Table_ID=6000011 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name actual_weight */
    public static final String COLUMNNAME_actual_weight = "actual_weight";

	/** Set Actual Weight.
	  * Actual Weight
	  */
	public void setactual_weight (BigDecimal actual_weight);

	/** Get Actual Weight.
	  * Actual Weight
	  */
	public BigDecimal getactual_weight();

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

    /** Column name buttoninward */
    public static final String COLUMNNAME_buttoninward = "buttoninward";

	/** Set buttoninward	  */
	public void setbuttoninward (String buttoninward);

	/** Get buttoninward	  */
	public String getbuttoninward();

    /** Column name C_Location_ID */
    public static final String COLUMNNAME_C_Location_ID = "C_Location_ID";

	/** Set Address of The Visitor.
	  * Address of The Visitor
	  */
	public void setC_Location_ID (int C_Location_ID);

	/** Get Address of The Visitor.
	  * Address of The Visitor
	  */
	public int getC_Location_ID();

	public I_C_Location getC_Location() throws RuntimeException;

    /** Column name company */
    public static final String COLUMNNAME_company = "company";

	/** Set Company.
	  * Previous working Company Name(Organization)
	  */
	public void setcompany (String company);

	/** Get Company.
	  * Previous working Company Name(Organization)
	  */
	public String getcompany();

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	public I_C_Order getC_Order() throws RuntimeException;

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

    /** Column name dateandtime */
    public static final String COLUMNNAME_dateandtime = "dateandtime";

	/** Set Date And Time.
	  * Date and Time on whicn in inward gate pass is being prepared
	  */
	public void setdateandtime (Timestamp dateandtime);

	/** Get Date And Time.
	  * Date and Time on whicn in inward gate pass is being prepared
	  */
	public Timestamp getdateandtime();

    /** Column name exitdateandtime */
    public static final String COLUMNNAME_exitdateandtime = "exitdateandtime";

	/** Set Exit Date And Time.
	  * Date and Time on which vehicle or Person is going out of office premises
	  */
	public void setexitdateandtime (Timestamp exitdateandtime);

	/** Get Exit Date And Time.
	  * Date and Time on which vehicle or Person is going out of office premises
	  */
	public Timestamp getexitdateandtime();

    /** Column name gross_weight */
    public static final String COLUMNNAME_gross_weight = "gross_weight";

	/** Set Gross Weight.
	  * Total Weight of the Vehicle
	  */
	public void setgross_weight (BigDecimal gross_weight);

	/** Get Gross Weight.
	  * Total Weight of the Vehicle
	  */
	public BigDecimal getgross_weight();

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

	public I_HR_Designation getHR_Designation() throws RuntimeException;

    /** Column name in_ward_reference_number */
    public static final String COLUMNNAME_in_ward_reference_number = "in_ward_reference_number";

	/** Set in_ward_reference_number	  */
	public void setin_ward_reference_number (String in_ward_reference_number);

	/** Get in_ward_reference_number	  */
	public String getin_ward_reference_number();

    /** Column name inward_serial_number */
    public static final String COLUMNNAME_inward_serial_number = "inward_serial_number";

	/** Set inward_serial_number	  */
	public void setinward_serial_number (int inward_serial_number);

	/** Get inward_serial_number	  */
	public int getinward_serial_number();

    /** Column name inward_type */
    public static final String COLUMNNAME_inward_type = "inward_type";

	/** Set Inward Type.
	  * Type of the Inward i.e. whether people are coming in and material is being received
	  */
	public void setinward_type (String inward_type);

	/** Get Inward Type.
	  * Type of the Inward i.e. whether people are coming in and material is being received
	  */
	public String getinward_type();

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

    /** Column name Logo_ID */
    public static final String COLUMNNAME_Logo_ID = "Logo_ID";

	/** Set Logo	  */
	public void setLogo_ID (int Logo_ID);

	/** Get Logo	  */
	public int getLogo_ID();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws RuntimeException;

    /** Column name need_weight_check */
    public static final String COLUMNNAME_need_weight_check = "need_weight_check";

	/** Set Need Weight Check?.
	  * Need Weight Check
	  */
	public void setneed_weight_check (boolean need_weight_check);

	/** Get Need Weight Check?.
	  * Need Weight Check
	  */
	public boolean isneed_weight_check();

    /** Column name no_of_persons */
    public static final String COLUMNNAME_no_of_persons = "no_of_persons";

	/** Set No Of Persons.
	  * Number of Persons that are visiting 
	  */
	public void setno_of_persons (int no_of_persons);

	/** Get No Of Persons.
	  * Number of Persons that are visiting 
	  */
	public int getno_of_persons();

    /** Column name phone_number */
    public static final String COLUMNNAME_phone_number = "phone_number";

	/** Set Phone Number.
	  * Phone Number of the Visitor
	  */
	public void setphone_number (String phone_number);

	/** Get Phone Number.
	  * Phone Number of the Visitor
	  */
	public String getphone_number();

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

    /** Column name purpose_of_the_visit */
    public static final String COLUMNNAME_purpose_of_the_visit = "purpose_of_the_visit";

	/** Set Purpose Of  The Visit.
	  * Purpose Of  The Visit
	  */
	public void setpurpose_of_the_visit (String purpose_of_the_visit);

	/** Get Purpose Of  The Visit.
	  * Purpose Of  The Visit
	  */
	public String getpurpose_of_the_visit();

    /** Column name supplier_name_id */
    public static final String COLUMNNAME_supplier_name_id = "supplier_name_id";

	/** Set Supplier Name.
	  * Name of the Supplier
	  */
	public void setsupplier_name_id (int supplier_name_id);

	/** Get Supplier Name.
	  * Name of the Supplier
	  */
	public int getsupplier_name_id();

	public I_C_BPartner getsupplier_name() throws RuntimeException;

    /** Column name tare_weight */
    public static final String COLUMNNAME_tare_weight = "tare_weight";

	/** Set Tare Weight.
	  * Empty Vehicles Weight
	  */
	public void settare_weight (BigDecimal tare_weight);

	/** Get Tare Weight.
	  * Empty Vehicles Weight
	  */
	public BigDecimal gettare_weight();

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

    /** Column name vehicle_number */
    public static final String COLUMNNAME_vehicle_number = "vehicle_number";

	/** Set Vehicle Number.
	  * Vehicle Number
	  */
	public void setvehicle_number (String vehicle_number);

	/** Get Vehicle Number.
	  * Vehicle Number
	  */
	public String getvehicle_number();

    /** Column name visitor_name */
    public static final String COLUMNNAME_visitor_name = "visitor_name";

	/** Set Visitor Name.
	  * Name of the Visitor
	  */
	public void setvisitor_name (String visitor_name);

	/** Get Visitor Name.
	  * Name of the Visitor
	  */
	public String getvisitor_name();

    /** Column name who_to_meet_id */
    public static final String COLUMNNAME_who_to_meet_id = "who_to_meet_id";

	/** Set Who_To_Meet.
	  * Employee in the company with whom e visitor want to meet
	  */
	public void setwho_to_meet_id (int who_to_meet_id);

	/** Get Who_To_Meet.
	  * Employee in the company with whom e visitor want to meet
	  */
	public int getwho_to_meet_id();

	public I_C_BPartner getwho_to_meet() throws RuntimeException;

    /** Column name WTC_Gate_Pass_Status_ID */
    public static final String COLUMNNAME_WTC_Gate_Pass_Status_ID = "WTC_Gate_Pass_Status_ID";

	/** Set GatePass Status	  */
	public void setWTC_Gate_Pass_Status_ID (int WTC_Gate_Pass_Status_ID);

	/** Get GatePass Status	  */
	public int getWTC_Gate_Pass_Status_ID();

	public I_WTC_Gate_Pass_Status getWTC_Gate_Pass_Status() throws RuntimeException;

    /** Column name WTC_Inward_Gate_Pass_ID */
    public static final String COLUMNNAME_WTC_Inward_Gate_Pass_ID = "WTC_Inward_Gate_Pass_ID";

	/** Set Inward Gate Pass	  */
	public void setWTC_Inward_Gate_Pass_ID (int WTC_Inward_Gate_Pass_ID);

	/** Get Inward Gate Pass	  */
	public int getWTC_Inward_Gate_Pass_ID();

    /** Column name WTC_Material_Inward_Type_ID */
    public static final String COLUMNNAME_WTC_Material_Inward_Type_ID = "WTC_Material_Inward_Type_ID";

	/** Set Material Inward Type	  */
	public void setWTC_Material_Inward_Type_ID (int WTC_Material_Inward_Type_ID);

	/** Get Material Inward Type	  */
	public int getWTC_Material_Inward_Type_ID();

	public I_WTC_Material_Inward_Type getWTC_Material_Inward_Type() throws RuntimeException;
}
