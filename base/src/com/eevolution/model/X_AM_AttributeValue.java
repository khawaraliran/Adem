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

/** Generated Model for AM_AttributeValue
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_AM_AttributeValue extends PO implements I_AM_AttributeValue, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120502L;

    /** Standard Constructor */
    public X_AM_AttributeValue (Properties ctx, int AM_AttributeValue_ID, String trxName)
    {
      super (ctx, AM_AttributeValue_ID, trxName);
      /** if (AM_AttributeValue_ID == 0)
        {
			setAM_AttributeValue_ID (0);
			setAM_Feature_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AM_AttributeValue (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AM_AttributeValue[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Collection Attribute Value ID.
		@param AM_AttributeValue_ID Collection Attribute Value ID	  */
	public void setAM_AttributeValue_ID (int AM_AttributeValue_ID)
	{
		if (AM_AttributeValue_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AM_AttributeValue_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AM_AttributeValue_ID, Integer.valueOf(AM_AttributeValue_ID));
	}

	/** Get Collection Attribute Value ID.
		@return Collection Attribute Value ID	  */
	public int getAM_AttributeValue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AM_AttributeValue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public com.eevolution.model.I_AM_Feature getAM_Feature() throws RuntimeException
    {
		return (com.eevolution.model.I_AM_Feature)MTable.get(getCtx(), com.eevolution.model.I_AM_Feature.Table_Name)
			.getPO(getAM_Feature_ID(), get_TrxName());	}

	/** Set Feature.
		@param AM_Feature_ID Feature	  */
	public void setAM_Feature_ID (int AM_Feature_ID)
	{
		if (AM_Feature_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AM_Feature_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AM_Feature_ID, Integer.valueOf(AM_Feature_ID));
	}

	/** Get Feature.
		@return Feature	  */
	public int getAM_Feature_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AM_Feature_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_AttributeValue getM_AttributeValue() throws RuntimeException
    {
		return (org.compiere.model.I_M_AttributeValue)MTable.get(getCtx(), org.compiere.model.I_M_AttributeValue.Table_Name)
			.getPO(getM_AttributeValue_ID(), get_TrxName());	}

	/** Set Attribute Value.
		@param M_AttributeValue_ID 
		Product Attribute Value
	  */
	public void setM_AttributeValue_ID (int M_AttributeValue_ID)
	{
		if (M_AttributeValue_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_AttributeValue_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_AttributeValue_ID, Integer.valueOf(M_AttributeValue_ID));
	}

	/** Get Attribute Value.
		@return Product Attribute Value
	  */
	public int getM_AttributeValue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_AttributeValue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_AttributeValue_ID()));
    }

	public org.compiere.model.I_M_Attribute getM_Attribute() throws RuntimeException
    {
		return (org.compiere.model.I_M_Attribute)MTable.get(getCtx(), org.compiere.model.I_M_Attribute.Table_Name)
			.getPO(getM_Attribute_ID(), get_TrxName());	}

	/** Set Attribute.
		@param M_Attribute_ID 
		Product Attribute
	  */
	public void setM_Attribute_ID (int M_Attribute_ID)
	{
		if (M_Attribute_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Attribute_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Attribute_ID, Integer.valueOf(M_Attribute_ID));
	}

	/** Get Attribute.
		@return Product Attribute
	  */
	public int getM_Attribute_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Attribute_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}