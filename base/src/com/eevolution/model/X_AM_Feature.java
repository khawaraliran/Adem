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

/** Generated Model for AM_Feature
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_AM_Feature extends PO implements I_AM_Feature, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120502L;

    /** Standard Constructor */
    public X_AM_Feature (Properties ctx, int AM_Feature_ID, String trxName)
    {
      super (ctx, AM_Feature_ID, trxName);
      /** if (AM_Feature_ID == 0)
        {
			setAM_Feature_ID (0);
			setAM_Season_ID (0);
// @AM_Season_ID@
			setPP_Product_BOMFeature_ID (0);
			setSeqNo (0);
        } */
    }

    /** Load Constructor */
    public X_AM_Feature (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AM_Feature[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

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

	public com.eevolution.model.I_AM_Season getAM_Season() throws RuntimeException
    {
		return (com.eevolution.model.I_AM_Season)MTable.get(getCtx(), com.eevolution.model.I_AM_Season.Table_Name)
			.getPO(getAM_Season_ID(), get_TrxName());	}

	/** Set Season ID.
		@param AM_Season_ID Season ID	  */
	public void setAM_Season_ID (int AM_Season_ID)
	{
		if (AM_Season_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AM_Season_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AM_Season_ID, Integer.valueOf(AM_Season_ID));
	}

	/** Get Season ID.
		@return Season ID	  */
	public int getAM_Season_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AM_Season_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_PP_Product_BOMFeature getPP_Product_BOMFeature() throws RuntimeException
    {
		return (org.eevolution.model.I_PP_Product_BOMFeature)MTable.get(getCtx(), org.eevolution.model.I_PP_Product_BOMFeature.Table_Name)
			.getPO(getPP_Product_BOMFeature_ID(), get_TrxName());	}

	/** Set Feature.
		@param PP_Product_BOMFeature_ID Feature	  */
	public void setPP_Product_BOMFeature_ID (int PP_Product_BOMFeature_ID)
	{
		if (PP_Product_BOMFeature_ID < 1) 
			set_Value (COLUMNNAME_PP_Product_BOMFeature_ID, null);
		else 
			set_Value (COLUMNNAME_PP_Product_BOMFeature_ID, Integer.valueOf(PP_Product_BOMFeature_ID));
	}

	/** Get Feature.
		@return Feature	  */
	public int getPP_Product_BOMFeature_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PP_Product_BOMFeature_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getPP_Product_BOMFeature_ID()));
    }

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}