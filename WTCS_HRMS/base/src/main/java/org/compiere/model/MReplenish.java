/**********************************************************************
 * This file is part of Adempiere ERP Bazaar                          *
 * http://www.adempiere.org                                           *
 *                                                                    *
 * Copyright (C) Daniel Tamm                                          *
 * Copyright (C) Contributors                                         *
 *                                                                    *
 * This program is free software, you can redistribute it and/or      *
 * modify it under the terms of the GNU General Public License        *
 * as published by the Free Software Foundation, either version 2     *
 * of the License, or (at your option) any later version.             *
 *                                                                    *
 * This program is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY, without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the       *
 * GNU General Public License for more details.                       *
 *                                                                    *
 * You should have received a copy of the GNU General Public License  *
 * along with this program, if not, write to the Free Software        *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,         *
 * MA 02110-1301, USA.                                                *
 *                                                                    *
 * Contributors:                                                      *
 * - Daniel Tamm     (usrdno@users.sourceforge.net)                   *
 * - Victor Perez    (victor.perez@e-evolution.com)					  *
 *                                                                    *
 * Sponsors:                                                          *
 * - Company (http://www.notima.se)                                   *
 * - Company (http://www.cyberphoto.se)                               *
 *********************************************************************/

package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.wtc.util.WTCTimeUtil;

/**
 *
 * @author Daniel Tamm
 * 
 * @author PhaniKiran.Gutha   
 * 
 * <pre>
 * 
 *   @Author                            @Date        @BugNo     @ChangeID                           @Description
 * PhaniKiran.Gutha                Dec 26, 2011  1673        20111226       Creating initial Inventory For the Product in the Replenish 
 * PhaniKiran Guta                 Jan 13, 2012  1673                       save the inventory after completion
 * D. Yadagiri Rao                 Feb 02, 2012  2028        20120202       Create a new Attribute_set_instance id And set to InventoryLine
 * D. Yadagiri Rao                 Feb 08, 2012  2028        201202081155   If Product have attributesetinstance Then we no need to create attribute set instance
 *                                                                          Rather than creating new attributesetinstance, use product attributesetinstance  	
 * PhaniKirna.Gutha                Feb 10, 2012  2038        20120210       Donot create attribute set instance for inventory line while creating inventory for the opening stock if product attribute set instance is not available in mReplenish.
 * Yadagiri Rao.Dontineni          Mar 16, 2012  2591        201203161015   Setting AD_Org_ID to respective inventory and line		
 * <Pre>
 */
public class MReplenish extends X_M_Replenish {
/**
 * Kindly do not delete below line as it is being used for svn version maintenance
 */
public static final String svnRevision =  "$Id: MReplenish.java 1157 2012-03-16 04:43:37Z yadagiri $";

    /**
	 * 
	 */
	private static final long serialVersionUID = -76806183034687720L;

	/**
     * Standard constructor
     * 
     * @param ctx
     * @param M_Replenish_ID
     * @param trxName
     */
    public MReplenish(Properties ctx, int M_Replenish_ID, String trxName) {
        super(ctx, M_Replenish_ID, trxName);
    }
    
    /**
     * Standard constructor to create a PO from a resultset.
     * 
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MReplenish(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    
    /**
     * 
     * @param ctx
     * @param M_ProductID
     * @param trxName
     * @return  A list of active replenish lines for given product.
     */
    public static List<MReplenish> getForProduct(Properties ctx, int M_ProductID, String trxName) {
    	final String whereClause= "M_Product_ID=? AND AD_Org_ID IN (0, ?) ";         
    	return new Query(ctx, I_M_Replenish.Table_Name, whereClause, trxName)
    	.setParameters(M_ProductID, Env.getAD_Org_ID(ctx))
    	.setClient_ID()
    	.setOrderBy("AD_Org_ID")
    	.setOnlyActiveRecords(true)
    	.list();
    }
    
    
    
    protected boolean beforeSave (boolean newRecord)
	{
    	//20111226
    	if( newRecord )
    	{
    		String whereClause=MReplenish.COLUMNNAME_M_Warehouse_ID+"="+this.getM_Warehouse_ID()+" AND " +
    				           MReplenish.COLUMNNAME_M_Product_ID+"="+this.getM_Product_ID();
    		MReplenish repl=new Query(this.getCtx(), MReplenish.Table_Name, whereClause, this.get_TrxName()).first();
    		
    		if(repl == null)
    		{
	    		if((this.getOpeningStock()).compareTo(Env.ZERO) >0  && (this.getOpeningValue()).compareTo(Env.ZERO) >0)
	    		{
		    		MInventory inventory=new MInventory(Env.getCtx(), 0, this.get_TrxName());
		    		int warehouseID=this.getM_Warehouse_ID();
		    		if(warehouseID>0)
		    		{
		    		    inventory.setM_Warehouse_ID(warehouseID);
		    		}
		    	    //Material Physical Inventory Document Type.
		    		inventory.setC_DocType_ID(DB.getSQLValue(this.get_TrxName(), "SELECT C_DocType_ID FROM C_DocType WHERE C_DocType.DocBaseType='MMI'"));
		    		
		    		inventory.setMovementType( X_M_Inventory.MOVEMENTTYPE_InventoryIn );    // Inventory In 'I+'
		    		
		    		inventory.setMovementDate( WTCTimeUtil.getSystemCurrentTimestamp() );
		    		
		    		inventory.setRequestDate(  WTCTimeUtil.getSystemCurrentTimestamp() );
		    		
		    		//
	    			//	201203161015
	    			//	As part of Junit test case execution purpose i added below statement 
	    			//
		    		inventory.setAD_Org_ID( this.getAD_Org_ID() );
		    		
		    		if( inventory.save() )
		    		{
		    			MInventoryLine invenLine=new MInventoryLine(Env.getCtx(), 0, this.get_TrxName());
		    			invenLine.setM_Inventory_ID(inventory.get_ID());
		    			invenLine.setQtyInternalUse(this.getOpeningStock());
		    			
		  
		    			invenLine.setCurrentCostPrice(this.getOpeningValue());
		    			
		    			if(this.getM_Locator_ID() >0)
		    			{
		    			   invenLine.setM_Locator_ID(this.getM_Locator_ID());
		    			}
		    			else
		    			{
		    				MLocator loc=new Query(Env.getCtx(), MLocator.Table_Name, MLocator.COLUMNNAME_M_Warehouse_ID+"="+this.getM_Warehouse_ID(), this.get_TrxName()).first();
		    				invenLine.setM_Locator_ID(loc.getM_Locator_ID());
		    				
		    			}
		    			invenLine.setM_Product_ID(this.getM_Product_ID());
		    			
		    			//
		    			//20120210
		    			//first pick the attribute set instance id given in replenish. if not exits then get the attribute set instance of the product.
		    			//
		    			
		    			int m_AttributeSetInstance_ID = getM_AttributeSetInstance_ID();
		    			
		    			if( m_AttributeSetInstance_ID <= 0 && null != getM_Product() && getM_Product().getM_AttributeSetInstance_ID() > 0 ) {
		    				
		    				m_AttributeSetInstance_ID	=	getM_Product().getM_AttributeSetInstance_ID();
		    				
		    			}
		    					    			
		    			if( m_AttributeSetInstance_ID > 0 ) {
		    				
		    				invenLine.setM_AttributeSetInstance_ID( m_AttributeSetInstance_ID );
		    				
		    			}
		    			
		    			invenLine.setInventoryType("D");    //Charge Account
		    			invenLine.setC_Charge_ID(EagleConstants.INITALINVENTORYCHARGE);
		    			
		    			//
		    			//	201203161015
		    			//	As part of Junit test case execution purpose i added below statement 
		    			//
		    			invenLine.setAD_Org_ID( this.getAD_Org_ID() );
		    			
		    			if(!invenLine.save())
		    			{
		    				log.log(Level.SEVERE,"Can't Save Inventory Line: " );
		    			}
		    			else
		    			{
		    				String completeStaus=inventory.completeIt();
		    				
		    				if( !completeStaus.equals( DocAction.STATUS_Completed ) ){
		    					log.saveError("",inventory.getProcessMsg());
		    					return false;
		    				} else { //20120113
		    					
		    					inventory.setDocStatus( completeStaus );
		    					inventory.save();
		    				}
		    				
		    				log.log(Level.INFO, " Status OF Inventory Completion IS :"+completeStaus);
		    			}
		    		}
	    		}
	    		else if((this.getOpeningStock()).compareTo(Env.ZERO) >0)
	    		{
	    			if((this.getOpeningValue()).compareTo(Env.ZERO) <= 0 )
	    			{
	                   String msg=Msg.getMsg(Env.getCtx(), "FILLMANDATORY");    				
	                   throw new AdempiereException(msg);
	    			}
	    		}
    		}
    	}
    	
    	return true;
	}
    
    //20120210 removed createAttributesetinstance method. as it is not required any more.
}
