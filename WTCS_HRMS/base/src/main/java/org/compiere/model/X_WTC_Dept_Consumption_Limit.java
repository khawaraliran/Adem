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

/** Generated Model for WTC_Dept_Consumption_Limit
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_WTC_Dept_Consumption_Limit.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_WTC_Dept_Consumption_Limit extends PO implements I_WTC_Dept_Consumption_Limit, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_WTC_Dept_Consumption_Limit.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111224L;

    /** Standard Constructor */
    public X_WTC_Dept_Consumption_Limit (Properties ctx, int WTC_Dept_Consumption_Limit_ID, String trxName)
    {
      super (ctx, WTC_Dept_Consumption_Limit_ID, trxName);
      /** if (WTC_Dept_Consumption_Limit_ID == 0)
        {
			setAct_Consumption (Env.ZERO);
			setExp_Consumption (Env.ZERO);
			setFromDate (new Timestamp( System.currentTimeMillis() ));
			setHR_Department_ID (0);
			setM_Product_ID (0);
			setToDate (new Timestamp( System.currentTimeMillis() ));
			setWTC_Dept_Consumption_Limit_ID (0);
        } */
    }

    /** Load Constructor */
    public X_WTC_Dept_Consumption_Limit (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_WTC_Dept_Consumption_Limit[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Actual Consumption.
		@param Act_Consumption 
		Actual Consumption
	  */
	public void setAct_Consumption (BigDecimal Act_Consumption)
	{
		set_Value (COLUMNNAME_Act_Consumption, Act_Consumption);
	}

	/** Get Actual Consumption.
		@return Actual Consumption
	  */
	public BigDecimal getAct_Consumption () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Act_Consumption);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Expected Consumption.
		@param Exp_Consumption 
		Expected Consumption
	  */
	public void setExp_Consumption (BigDecimal Exp_Consumption)
	{
		set_Value (COLUMNNAME_Exp_Consumption, Exp_Consumption);
	}

	/** Get Expected Consumption.
		@return Expected Consumption
	  */
	public BigDecimal getExp_Consumption () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Exp_Consumption);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set From Date.
		@param FromDate From Date	  */
	public void setFromDate (Timestamp FromDate)
	{
		set_Value (COLUMNNAME_FromDate, FromDate);
	}

	/** Get From Date.
		@return From Date	  */
	public Timestamp getFromDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FromDate);
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

	/** Set Remarks.
		@param Remarks 
		Remarks
	  */
	public void setRemarks (String Remarks)
	{
		set_Value (COLUMNNAME_Remarks, Remarks);
	}

	/** Get Remarks.
		@return Remarks
	  */
	public String getRemarks () 
	{
		return (String)get_Value(COLUMNNAME_Remarks);
	}

	/** Set To Date.
		@param ToDate To Date	  */
	public void setToDate (Timestamp ToDate)
	{
		set_Value (COLUMNNAME_ToDate, ToDate);
	}

	/** Get To Date.
		@return To Date	  */
	public Timestamp getToDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ToDate);
	}

	/** Set Department Consumption Limit.
		@param WTC_Dept_Consumption_Limit_ID Department Consumption Limit	  */
	public void setWTC_Dept_Consumption_Limit_ID (int WTC_Dept_Consumption_Limit_ID)
	{
		if (WTC_Dept_Consumption_Limit_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WTC_Dept_Consumption_Limit_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WTC_Dept_Consumption_Limit_ID, Integer.valueOf(WTC_Dept_Consumption_Limit_ID));
	}

	/** Get Department Consumption Limit.
		@return Department Consumption Limit	  */
	public int getWTC_Dept_Consumption_Limit_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WTC_Dept_Consumption_Limit_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}