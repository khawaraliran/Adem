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

/** Generated Model for HR_Emp_Token
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id: X_HR_Emp_Token.java 1009 2012-02-09 09:16:13Z suman $ */
public class X_HR_Emp_Token extends PO implements I_HR_Emp_Token, I_Persistent 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: X_HR_Emp_Token.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 *
	 */
	private static final long serialVersionUID = 20111212L;

    /** Standard Constructor */
    public X_HR_Emp_Token (Properties ctx, int HR_Emp_Token_ID, String trxName)
    {
      super (ctx, HR_Emp_Token_ID, trxName);
      /** if (HR_Emp_Token_ID == 0)
        {
			setC_BPartner_ID (0);
			setHR_Emp_Token_ID (0);
			setHR_Token_ID (0);
			setissuedate (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_HR_Emp_Token (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Emp_Token[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Employee.
		@param C_BPartner_ID 
		Identifies a Employee
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Employee.
		@return Identifies a Employee
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set currmonthsal.
		@param currmonthsal currmonthsal	  */
	public void setcurrmonthsal (BigDecimal currmonthsal)
	{
		set_Value (COLUMNNAME_currmonthsal, currmonthsal);
	}

	/** Get currmonthsal.
		@return currmonthsal	  */
	public BigDecimal getcurrmonthsal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currmonthsal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HR_Emp_Token.
		@param HR_Emp_Token_ID HR_Emp_Token	  */
	public void setHR_Emp_Token_ID (int HR_Emp_Token_ID)
	{
		if (HR_Emp_Token_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Emp_Token_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Emp_Token_ID, Integer.valueOf(HR_Emp_Token_ID));
	}

	/** Get HR_Emp_Token.
		@return HR_Emp_Token	  */
	public int getHR_Emp_Token_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Emp_Token_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_HR_Period getHR_Period() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Period)MTable.get(getCtx(), org.eevolution.model.I_HR_Period.Table_Name)
			.getPO(getHR_Period_ID(), get_TrxName());	}

	/** Set Payroll Period.
		@param HR_Period_ID Payroll Period	  */
	public void setHR_Period_ID (int HR_Period_ID)
	{
		if (HR_Period_ID < 1) 
			set_Value (COLUMNNAME_HR_Period_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Period_ID, Integer.valueOf(HR_Period_ID));
	}

	/** Get Payroll Period.
		@return Payroll Period	  */
	public int getHR_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Token getHR_Token() throws RuntimeException
    {
		return (I_HR_Token)MTable.get(getCtx(), I_HR_Token.Table_Name)
			.getPO(getHR_Token_ID(), get_TrxName());	}

	/** Set Token.
		@param HR_Token_ID Token	  */
	public void setHR_Token_ID (int HR_Token_ID)
	{
		if (HR_Token_ID < 1) 
			set_Value (COLUMNNAME_HR_Token_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Token_ID, Integer.valueOf(HR_Token_ID));
	}

	/** Get Token.
		@return Token	  */
	public int getHR_Token_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Token_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set issuedate.
		@param issuedate issuedate	  */
	public void setissuedate (Timestamp issuedate)
	{
		set_Value (COLUMNNAME_issuedate, issuedate);
	}

	/** Get issuedate.
		@return issuedate	  */
	public Timestamp getissuedate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_issuedate);
	}

	/** Set nooftokens.
		@param nooftokens nooftokens	  */
	public void setnooftokens (int nooftokens)
	{
		set_Value (COLUMNNAME_nooftokens, Integer.valueOf(nooftokens));
	}

	/** Get nooftokens.
		@return nooftokens	  */
	public int getnooftokens () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_nooftokens);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set prevmonthsal.
		@param prevmonthsal prevmonthsal	  */
	public void setprevmonthsal (BigDecimal prevmonthsal)
	{
		set_Value (COLUMNNAME_prevmonthsal, prevmonthsal);
	}

	/** Get prevmonthsal.
		@return prevmonthsal	  */
	public BigDecimal getprevmonthsal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_prevmonthsal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set tokenamount.
		@param tokenamount tokenamount	  */
	public void settokenamount (BigDecimal tokenamount)
	{
		set_Value (COLUMNNAME_tokenamount, tokenamount);
	}

	/** Get tokenamount.
		@return tokenamount	  */
	public BigDecimal gettokenamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_tokenamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** tokenlevel AD_Reference_ID=7000000 */
	public static final int TOKENLEVEL_AD_Reference_ID=7000000;
	/** Canteen = CA */
	public static final String TOKENLEVEL_Canteen = "CA";
	/** Stores = ST */
	public static final String TOKENLEVEL_Stores = "ST";
	/** Set tokenlevel.
		@param tokenlevel tokenlevel	  */
	public void settokenlevel (String tokenlevel)
	{

		set_Value (COLUMNNAME_tokenlevel, tokenlevel);
	}

	/** Get tokenlevel.
		@return tokenlevel	  */
	public String gettokenlevel () 
	{
		return (String)get_Value(COLUMNNAME_tokenlevel);
	}

	/** Set totalamount.
		@param totalamount totalamount	  */
	public void settotalamount (BigDecimal totalamount)
	{
		set_Value (COLUMNNAME_totalamount, totalamount);
	}

	/** Get totalamount.
		@return totalamount	  */
	public BigDecimal gettotalamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_totalamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}