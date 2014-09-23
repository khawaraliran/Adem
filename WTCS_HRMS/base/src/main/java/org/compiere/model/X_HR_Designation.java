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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for HR_Designation
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Designation.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Designation extends PO implements I_HR_Designation, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Designation.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111126L;

    /** Standard Constructor */
    public X_HR_Designation (Properties ctx, int HR_Designation_ID, String trxName)
    {
      super (ctx, HR_Designation_ID, trxName);
      /** if (HR_Designation_ID == 0)
        {
			setHR_Designation_ID (0);
			setHR_Sal_Struct_Header_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_HR_Designation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Designation[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Designation.
		@param HR_Designation_ID 
		Designation
	  */
	public void setHR_Designation_ID (int HR_Designation_ID)
	{
		if (HR_Designation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Designation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Designation_ID, Integer.valueOf(HR_Designation_ID));
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

	public I_HR_Employee_Type getHR_Employee_Type() throws RuntimeException
    {
		return (I_HR_Employee_Type)MTable.get(getCtx(), I_HR_Employee_Type.Table_Name)
			.getPO(getHR_Employee_Type_ID(), get_TrxName());	}

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

	/*public I_HR_Sal_Struct_Header getHR_Sal_Struct_Header() throws RuntimeException
    {
		return (I_HR_Sal_Struct_Header)MTable.get(getCtx(), I_HR_Sal_Struct_Header.Table_Name)
			.getPO(getHR_Sal_Struct_Header_ID(), get_TrxName());	}
*/
	/** Set Salary Structure.
		@param HR_Sal_Struct_Header_ID Salary Structure	  */
	public void setHR_Sal_Struct_Header_ID (int HR_Sal_Struct_Header_ID)
	{
		if (HR_Sal_Struct_Header_ID < 1) 
			set_Value (COLUMNNAME_HR_Sal_Struct_Header_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Sal_Struct_Header_ID, Integer.valueOf(HR_Sal_Struct_Header_ID));
	}

	/** Get Salary Structure.
		@return Salary Structure	  */
	public int getHR_Sal_Struct_Header_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Sal_Struct_Header_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}