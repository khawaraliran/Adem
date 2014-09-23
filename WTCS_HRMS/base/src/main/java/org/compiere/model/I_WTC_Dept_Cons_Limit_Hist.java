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

/** Generated Interface for WTC_Dept_Cons_Limit_Hist
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_WTC_Dept_Cons_Limit_Hist 
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: I_WTC_Dept_Cons_Limit_Hist.java 1009 2012-02-09 09:16:13Z suman $";

    /** TableName=WTC_Dept_Cons_Limit_Hist */
    public static final String Table_Name = "WTC_Dept_Cons_Limit_Hist";

    /** AD_Table_ID=8000006 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name Act_Consumption */
    public static final String COLUMNNAME_Act_Consumption = "Act_Consumption";

	/** Set Actual Consumption.
	  * Actual Consumption
	  */
	public void setAct_Consumption (int Act_Consumption);

	/** Get Actual Consumption.
	  * Actual Consumption
	  */
	public int getAct_Consumption();

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

    /** Column name Exp_Consumption */
    public static final String COLUMNNAME_Exp_Consumption = "Exp_Consumption";

	/** Set Expected Consumption.
	  * Expected Consumption
	  */
	public void setExp_Consumption (int Exp_Consumption);

	/** Get Expected Consumption.
	  * Expected Consumption
	  */
	public int getExp_Consumption();

    /** Column name FromDate */
    public static final String COLUMNNAME_FromDate = "FromDate";

	/** Set From Date	  */
	public void setFromDate (Timestamp FromDate);

	/** Get From Date	  */
	public Timestamp getFromDate();

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

    /** Column name Remarks */
    public static final String COLUMNNAME_Remarks = "Remarks";

	/** Set Remarks.
	  * Remarks
	  */
	public void setRemarks (String Remarks);

	/** Get Remarks.
	  * Remarks
	  */
	public String getRemarks();

    /** Column name ToDate */
    public static final String COLUMNNAME_ToDate = "ToDate";

	/** Set To Date	  */
	public void setToDate (Timestamp ToDate);

	/** Get To Date	  */
	public Timestamp getToDate();

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

    /** Column name WTC_Dept_Cons_Limit_Hist_ID */
    public static final String COLUMNNAME_WTC_Dept_Cons_Limit_Hist_ID = "WTC_Dept_Cons_Limit_Hist_ID";

	/** Set Department Consumption Limit History	  */
	public void setWTC_Dept_Cons_Limit_Hist_ID (int WTC_Dept_Cons_Limit_Hist_ID);

	/** Get Department Consumption Limit History	  */
	public int getWTC_Dept_Cons_Limit_Hist_ID();
}
