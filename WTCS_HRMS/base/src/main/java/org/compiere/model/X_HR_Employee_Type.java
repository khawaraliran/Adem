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

/** Generated Model for HR_Employee_Type
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Employee_Type.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Employee_Type extends PO implements I_HR_Employee_Type, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Employee_Type.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20120107L;

    /** Standard Constructor */
    public X_HR_Employee_Type (Properties ctx, int HR_Employee_Type_ID, String trxName)
    {
      super (ctx, HR_Employee_Type_ID, trxName);
      /** if (HR_Employee_Type_ID == 0)
        {
			setHR_Employee_Type_ID (0);
			setHR_Payroll_ID (0);
			setissystemgenerated (false);
			setName (null);
			setwagelevel (null);
        } */
    }

    /** Load Constructor */
    public X_HR_Employee_Type (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Employee_Type[")
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

	/** Set Employee Type.
		@param HR_Employee_Type_ID 
		Employee Type
	  */
	public void setHR_Employee_Type_ID (int HR_Employee_Type_ID)
	{
		if (HR_Employee_Type_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_Type_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Employee_Type_ID, Integer.valueOf(HR_Employee_Type_ID));
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

	/** Set issystemgenerated.
		@param issystemgenerated issystemgenerated	  */
	public void setissystemgenerated (boolean issystemgenerated)
	{
		set_Value (COLUMNNAME_issystemgenerated, Boolean.valueOf(issystemgenerated));
	}

	/** Get issystemgenerated.
		@return issystemgenerated	  */
	public boolean issystemgenerated () 
	{
		Object oo = get_Value(COLUMNNAME_issystemgenerated);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** wagelevel AD_Reference_ID=9000008 */
	public static final int WAGELEVEL_AD_Reference_ID=9000008;
	/** Daily = DA */
	public static final String WAGELEVEL_Daily = "DA";
	/** Monthly = MO */
	public static final String WAGELEVEL_Monthly = "MO";
	/** Set Wage Level.
		@param wagelevel 
		Wage Level
	  */
	public void setwagelevel (String wagelevel)
	{

		set_Value (COLUMNNAME_wagelevel, wagelevel);
	}

	/** Get Wage Level.
		@return Wage Level
	  */
	public String getwagelevel () 
	{
		return (String)get_Value(COLUMNNAME_wagelevel);
	}
}