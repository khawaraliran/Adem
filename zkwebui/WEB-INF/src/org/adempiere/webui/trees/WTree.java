/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/

package org.adempiere.webui.trees;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MTree_Base;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * Builds Tree / Trees Root Node - maintained in ADTreePanel (webui)
 * 
 * @author Jan Thielemann - evenos GmbH - jan.thielemann@evenos.de - www.evenos.de
 * @version $Id: WTree.java,v 1.0 2012/12/4 08:22:02 jan.thielemann $
 */

public class WTree extends MTree_Base{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6500196510733402235L;

	/** Is Tree editable */
	private boolean m_editable = false;

	/** The tree is displayed on the Java Client (i.e. not web) */
	private boolean m_clientTree = false;

	/**
	 * The Tree should show all nodes or only nodes which belong to the
	 * user/role
	 */
	private boolean m_allNodes = false;

	/** Root Node */
	private WTreeNode m_root = null;

	
	/** Logger */
	private static CLogger s_log = CLogger.getCLogger(WTree.class);

		
	
	/**
	 * Default Constructor. Need to call loadNodes explicitly
	 * 
	 * @param ctx
	 *            context for security
	 * @param AD_Tree_ID
	 *            The tree to build
	 * @param trxName
	 *            transaction
	 */
	public WTree(Properties ctx, int AD_Tree_ID, String trxName) {
		super(ctx, AD_Tree_ID, trxName);
	} //WTree

	/**
	 * Construct & Load Tree
	 * 
	 * @param ctx
	 *			  context for security
	 * @param AD_Tree_ID
	 *            The tree to build
	 * @param editable
	 *            True, if tree can be modified - includes inactive and empty
	 *            summary nodes
	 * @param clientTree
	 *            the tree is displayed on the java client (not on web)
	 * @param trxName
	 *            transaction
	 */
	public WTree(Properties ctx, int AD_Tree_ID, boolean editable, boolean clientTree, String trxName) {
		this(ctx, AD_Tree_ID, editable, clientTree, false, trxName);
	} //WTree

	
	/**
	 * Construct & Load Tree
	 * 
	 * @param ctx
	 *            context for security
	 * @param AD_Tree_ID
	 *            The tree to build
	 * @param editable
	 *            True, if tree can be modified - includes inactive and empty
	 *            summary nodes
	 * @param clientTree
	 *            the tree is displayed on the java client (not on web)
	 * @param allNodes
	 * 			  the tree should show all nodes
	 * @param trxName
	 *            transaction
	 */
	public WTree(Properties ctx, int AD_Tree_ID, boolean editable, boolean clientTree, boolean allNodes, String trxName) {
		this(ctx, AD_Tree_ID, trxName);

		m_editable = editable;
		m_clientTree = clientTree;
		m_allNodes = allNodes;

		s_log.config("AD_Tree_ID=" + AD_Tree_ID + ", Editable=" + editable
				+ ", SwingClient=" + clientTree + ", AllNodes=" + allNodes);

		loadRootNode();

	}//WTree
	
	private void loadRootNode() {
		m_root = new WTreeNode(this, 0, 0, Msg.getMsg(Env.getCtx(), getName()), getDescription(), 0, true, null, false, null);
	}
	
	
	/**************************************************************************
	 * Get default (oldest) complete AD_Tree_ID for KeyColumn. Called from
	 * GridController
	 * 
	 * @param keyColumnName
	 *            key column name, eg. C_Project_ID
	 * @param AD_Client_ID
	 *            client
	 * @return AD_Tree_ID
	 */
	public static int getDefaultAD_Tree_ID(int AD_Client_ID, String keyColumnName) {
		s_log.config(keyColumnName);
		if (keyColumnName == null || keyColumnName.length() == 0)
			return 0;

		String TreeType = null;
		if (keyColumnName.equals("AD_Menu_ID"))
			TreeType = TREETYPE_Menu;
		else if (keyColumnName.equals("C_ElementValue_ID"))
			TreeType = TREETYPE_ElementValue;
		else if (keyColumnName.equals("M_Product_ID"))
			TreeType = TREETYPE_Product;
		else if (keyColumnName.equals("C_BPartner_ID"))
			TreeType = TREETYPE_BPartner;
		else if (keyColumnName.equals("AD_Org_ID"))
			TreeType = TREETYPE_Organization;
		else if (keyColumnName.equals("C_Project_ID"))
			TreeType = TREETYPE_Project;
		else if (keyColumnName.equals("M_ProductCategory_ID"))
			TreeType = TREETYPE_ProductCategory;
		else if (keyColumnName.equals("M_BOM_ID"))
			TreeType = TREETYPE_BoM;
		else if (keyColumnName.equals("C_SalesRegion_ID"))
			TreeType = TREETYPE_SalesRegion;
		else if (keyColumnName.equals("C_Campaign_ID"))
			TreeType = TREETYPE_Campaign;
		else if (keyColumnName.equals("C_Activity_ID"))
			TreeType = TREETYPE_Activity;
		//
		else if (keyColumnName.equals("CM_CStage_ID"))
			TreeType = TREETYPE_CMContainerStage;
		else if (keyColumnName.equals("CM_Container_ID"))
			TreeType = TREETYPE_CMContainer;
		else if (keyColumnName.equals("CM_Media_ID"))
			TreeType = TREETYPE_CMMedia;
		else if (keyColumnName.equals("CM_Template_ID"))
			TreeType = TREETYPE_CMTemplate;
		else {
			s_log.severe("Could not map " + keyColumnName);
			return 0;
		}

		int AD_Tree_ID = 0;
		String sql = "SELECT AD_Tree_ID, Name FROM AD_Tree "
				+ "WHERE AD_Client_ID=? AND TreeType=? AND IsActive='Y' AND IsAllNodes='Y' "
				+ "ORDER BY IsDefault DESC, AD_Tree_ID";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setString(2, TreeType);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				AD_Tree_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			s_log.severe(sql + " - " + e);
		}

		return AD_Tree_ID;
	} // getDefaultAD_Tree_ID
	
	
	
	/**
	 * Get Root node
	 * 
	 * @return root
	 */
	public WTreeNode getRoot() {
		return m_root;
	} // getRoot

	/**
	 * Is Menu Tree
	 * 
	 * @return true if menu
	 */
	public boolean isMenu() {
		return TREETYPE_Menu.equals(getTreeType());
	} // isMenu

	/**
	 * Is Product Tree
	 * 
	 * @return true if product
	 */
	public boolean isProduct() {
		return TREETYPE_Product.equals(getTreeType());
	} // isProduct

	/**
	 * Is Business Partner Tree
	 * 
	 * @return true if partner
	 */
	public boolean isBPartner() {
		return TREETYPE_BPartner.equals(getTreeType());
	} // isBPartner

	
	public boolean isEditable() {
		return m_editable;
	}

	public boolean isClientTree() {
		return m_clientTree;
	}

	public boolean isAllNodes() {
		return m_allNodes;
	}
	
	
	
	
	/**
	 * String representation
	 * 
	 * @return info
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("MTree[");
		sb.append("AD_Tree_ID=").append(getAD_Tree_ID()).append(", Name=")
				.append(getName());
		sb.append("]");
		return sb.toString();
	}
	
	
	/**************************************************************************
	 *  Trim tree of empty summary nodes
	 */
	public void trimTree()
	{
		log.fine("Remove empty summary nodes from tree " + this);
		trimTreeNode(m_root, 0);
	}   //  trimTree

	private void trimTreeNode(WTreeNode node, int level){
		//Prefix for debug output
		String prefix = "";
		for(int i = 0; i<level; i++){
			prefix+="-";
		}
		
		//if we remove a node, the enumerations nextElement() skips a node so we use this 
		//to make sure we trim the tree as long as we have nodes to remove
		boolean needToTrimAgain = node!=null;
		while(needToTrimAgain){
			
			//set to false so that if we don't remove a node, we will finish this method
			needToTrimAgain=false;			
			
			//get enumeration from node (these are the nodes childs)
//			node.getChildCount();
			Enumeration en = node.preorderEnumeration();
			
			List<WTreeNode> nodesToDelete = new ArrayList<WTreeNode>();
			
			//iterate over all childs + the node itself
			while(en.hasMoreElements()){
				
				//parse the node
				WTreeNode nd = (WTreeNode)en.nextElement();

				//if the node is itself skip this one
				if(nd.equals(node))
					continue;
				
				//if node is summary but has childs itself, recursive call to this method
				if (nd.isSummary() && nd.getChildCount() > 0){
					trimTreeNode(nd, (level+1));
				}

				//if node is summary but has no childs, remove the child and make sure we iterate over its parent again
				if (nd.isSummary() && nd.getChildCount() == 0){
					log.finer(prefix+nd);
					nodesToDelete.add(nd);
//					node.getChildren().remove(nd);
//					nd.removeFromParent();
					needToTrimAgain = true;
				}
			}
			
			node.getChildren().removeAll(nodesToDelete);
		}
	}

}




