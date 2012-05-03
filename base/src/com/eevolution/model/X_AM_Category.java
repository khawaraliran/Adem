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
package com.eevolution.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for AM_Category
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_AM_Category extends PO implements I_AM_Category, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120502L;

    /** Standard Constructor */
    public X_AM_Category (Properties ctx, int AM_Category_ID, String trxName)
    {
      super (ctx, AM_Category_ID, trxName);
      /** if (AM_Category_ID == 0)
        {
			setAM_Category_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AM_Category (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AM_Category[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_PrintColor getAD_PrintColor() throws RuntimeException
    {
		return (org.compiere.model.I_AD_PrintColor)MTable.get(getCtx(), org.compiere.model.I_AD_PrintColor.Table_Name)
			.getPO(getAD_PrintColor_ID(), get_TrxName());	}

	/** Set Print Color.
		@param AD_PrintColor_ID 
		Color used for printing and display
	  */
	public void setAD_PrintColor_ID (int AD_PrintColor_ID)
	{
		if (AD_PrintColor_ID < 1) 
			set_Value (COLUMNNAME_AD_PrintColor_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PrintColor_ID, Integer.valueOf(AD_PrintColor_ID));
	}

	/** Get Print Color.
		@return Color used for printing and display
	  */
	public int getAD_PrintColor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PrintColor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Collection Category.
		@param AM_Category_ID Collection Category	  */
	public void setAM_Category_ID (int AM_Category_ID)
	{
		if (AM_Category_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AM_Category_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AM_Category_ID, Integer.valueOf(AM_Category_ID));
	}

	/** Get Collection Category.
		@return Collection Category	  */
	public int getAM_Category_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AM_Category_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public com.eevolution.model.I_AM_Category getAM_Category_Parent() throws RuntimeException
    {
		return (com.eevolution.model.I_AM_Category)MTable.get(getCtx(), com.eevolution.model.I_AM_Category.Table_Name)
			.getPO(getAM_Category_Parent_ID(), get_TrxName());	}

	/** Set Parent Collection Category ID.
		@param AM_Category_Parent_ID Parent Collection Category ID	  */
	public void setAM_Category_Parent_ID (int AM_Category_Parent_ID)
	{
		if (AM_Category_Parent_ID < 1) 
			set_Value (COLUMNNAME_AM_Category_Parent_ID, null);
		else 
			set_Value (COLUMNNAME_AM_Category_Parent_ID, Integer.valueOf(AM_Category_Parent_ID));
	}

	/** Get Parent Collection Category ID.
		@return Parent Collection Category ID	  */
	public int getAM_Category_Parent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AM_Category_Parent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Default.
		@param IsDefault 
		Default value
	  */
	public void setIsDefault (boolean IsDefault)
	{
		set_Value (COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
	}

	/** Get Default.
		@return Default value
	  */
	public boolean isDefault () 
	{
		Object oo = get_Value(COLUMNNAME_IsDefault);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
    }
}