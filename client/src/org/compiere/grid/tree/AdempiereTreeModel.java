///******************************************************************************
// * Product: Adempiere ERP & CRM Smart Business Solution                        *
// * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
// * This program is free software; you can redistribute it and/or modify it    *
// * under the terms version 2 of the GNU General Public License as published   *
// * by the Free Software Foundation. This program is distributed in the hope   *
// * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
// * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
// * See the GNU General Public License for more details.                       *
// * You should have received a copy of the GNU General Public License along    *
// * with this program; if not, write to the Free Software Foundation, Inc.,    *
// * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
// * For the text or an alternative of this public license, you may reach us    *
// * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
// * or via info@compiere.org or http://www.compiere.org/license.html           *
// *****************************************************************************/
//package org.compiere.grid.tree;
//
//import java.util.logging.Level;
//
//import javax.swing.tree.DefaultTreeModel;
//import javax.swing.tree.TreeNode;
//
//import org.compiere.model.MTable;
//import org.compiere.model.MTree;
//import org.compiere.model.MTreeNode;
//import org.compiere.model.PO;
//import org.compiere.util.CLogger;
//import org.compiere.util.Env;
//import org.compiere.util.Trx;
//
///**
// *  AdempiereTreeModel provides a persistable tree model based on an MTree.
// *
// *  @author 	phib  2008/07/30
// *  FR [ 2032092 ] Java 6 improvements to tree drag and drop
// */
//public class AdempiereTreeModel extends DefaultTreeModel {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 8503954687681402088L;
//
//	/**	Logger			*/
//	private static CLogger log = CLogger.getCLogger(AdempiereTreeModel.class);
//
//	private MTree m_MTree;
//	public AdempiereTreeModel(TreeNode root) {
//		super(root);
//	}
//
//	public AdempiereTreeModel(MTreeNode root, boolean b) {
//		super(root, b);
//	}
//
//	public void setMTree(MTree tree) {
//		
//		m_MTree = tree;
//		
//	}
//	
//	public void saveChangedNodes(MTreeNode from, MTreeNode to) {
//		int AD_Tree_ID = m_MTree.getAD_Tree_ID();
//		Trx trx = Trx.get (Trx.createTrxName("AdempiereTreeModel"), true);
//		try
//		{
//			int no = 0;
//			for (int i = 0; i < from.getChildCount(); i++)
//			{
//				MTreeNode nd = (MTreeNode)from.getChildAt(i);
//				String whereClause = "AD_Tree_ID="+AD_Tree_ID+ " AND Node_ID=" + nd.getNode_ID();
//				PO tree = MTable.get(Env.getCtx(), m_MTree.getNodeTableName()).getPO(whereClause, trx.getTrxName());
//				tree.set_CustomColumn("Parent_ID", from.getNode_ID());
//				tree.set_CustomColumn("SeqNo", i);
//				tree.saveEx();
//			}
//			if (from != to)
//				for (int i = 0; i < to.getChildCount(); i++)
//				{
//					MTreeNode nd = (MTreeNode)to.getChildAt(i);
//					String whereClause = "AD_Tree_ID="+AD_Tree_ID+ " AND Node_ID=" + nd.getNode_ID();
//					PO tree = MTable.get(Env.getCtx(), m_MTree.getNodeTableName()).getPO(whereClause, trx.getTrxName());
//					tree.set_CustomColumn("Parent_ID", to.getNode_ID());
//					tree.set_CustomColumn("SeqNo", i);
//					tree.saveEx();
//				}
//			trx.commit(true);
//		}
//		catch (Exception e)
//		{
//			trx.rollback();
//			log.log(Level.SEVERE, "move", e);
//		}
//		trx.close();
//		trx = null;
//		log.config("complete");
//		
//	}
//
//	
//}
/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.compiere.grid.tree;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.compiere.model.MClient;
import org.compiere.model.MRole;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 *  AdempiereTreeModel provides a persistable tree model based on an MTree.
 *
 *  @author 	phib  2008/07/30
 *  FR [ 2032092 ] Java 6 improvements to tree drag and drop
 */
public class AdempiereTreeModel extends DefaultTreeModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8503954687681402088L;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AdempiereTreeModel.class);

	
	/**
     * Creates a tree in which any node can have children.
     *
     * @param root a TreeNode object that is the root of the tree
     */
	public AdempiereTreeModel(TreeNode root) {
		super(root);
	}

	/** 
	 * Creates a tree specifying whether any node can have children,
     * or whether only certain nodes can have children.
	 * @param root MTreeNode object that is the root of the tree
	 * @param askAllowsChildren a boolean, false if any node can
     *        have children, true if each node is asked to see if
     *        it can have children
	 */
	public AdempiereTreeModel(MTreeNode root, boolean b) {
		super(root, b);
	}

	
	public void saveChangedNodes(MTreeNode from, MTreeNode to) {
		
		log.info("Saving from="+from + " to="+to);
		
		//Get the Trees from Nodes
		MTree toTree = to.getMTree();
		MTree fromTree = from.getMTree();
		
		//Get Tree_IDs from Trees
		int AD_Tree_ID_To = toTree.getAD_Tree_ID();
		int AD_Tree_ID_From = fromTree.getAD_Tree_ID();
				
		//Create trx
		Trx trx = Trx.get (Trx.createTrxName("AdempiereTreeModel"), true);
		
		try
		{
			//Update from-nodes childs
			int no = 0;
			for (int i = 0; i < from.getChildCount(); i++)
			{
				MTreeNode nd = (MTreeNode)from.getChildAt(i);
				StringBuffer sql = new StringBuffer("UPDATE ");
				sql.append(fromTree.getNodeTableName())
					.append(" SET Parent_ID=").append(from.getNode_ID())
					.append(", SeqNo=").append(i)
					.append(", Updated=SysDate")
					.append(" WHERE AD_Tree_ID=").append(AD_Tree_ID_From)
					.append(" AND Node_ID=").append(nd.getNode_ID());
				no = DB.executeUpdate(sql.toString(),trx.getTrxName());
			}
			
			//If from-node is different to to-node, update to-nodes childs as well
			if (from != to)
				for (int i = 0; i < to.getChildCount(); i++)
				{
					MTreeNode nd = (MTreeNode)to.getChildAt(i);
					StringBuffer sql = new StringBuffer("UPDATE ");
					sql.append(toTree.getNodeTableName())
						.append(" SET Parent_ID=").append(to.getNode_ID())
						.append(", SeqNo=").append(i)
						.append(", Updated=SysDate")
						.append(" WHERE AD_Tree_ID=").append(AD_Tree_ID_To)
						.append(" AND Node_ID=").append(nd.getNode_ID());
					log.fine(sql.toString());
					no = DB.executeUpdate(sql.toString(),trx.getTrxName());
				}
			
			//Commit changes
			trx.commit(true);
		}
		catch (Exception e)
		{
			//If error, rollback
			trx.rollback();
			log.log(Level.SEVERE, "move", e);
		}
		
		//Done
		trx.close();
		trx = null;
		
		log.config("complete");
		
	}

	
	/** 
	 * This method is invoked by VTreePanel.nodeChanged() after a row in the grid was deleted and if
	 * there was noticed a delete action. We now want to do two things: remove the node from the tree/model
	 * and if the node is a summary and has children, we want to update all these children so we don't lose them.
	 */
	public void deleteNode(MTreeNode deletedNode){
		
		//Parent from the deleted node
		MTreeNode parent = (MTreeNode)deletedNode.getParent();
		
		//Child count from the deleted node
		int childCount = deletedNode.getChildCount();
		
		//Child count from parent before inserting new nodes
		int parentChildCount = parent.getChildCount();
		
		
		
		
		//Remove each child from the delted node and add it to the deleted nodes parent
		List<MTreeNode> childList = new ArrayList<MTreeNode>();
		for(int i = 0; i < childCount; i++){
			MTreeNode child = (MTreeNode)deletedNode.getChildAt(i);
			childList.add(child);
		}
		for(MTreeNode child : childList){
			child.removeFromParent();
			parent.add(child);
		}
		
		
		
		//Remove the delted node from its parent
		deletedNode.removeFromParent();
		
		//Update children in the database
		try{
			for (int i = (parentChildCount - 1); i < parent.getChildCount(); i++)
			{
				MTreeNode nd = (MTreeNode)parent.getChildAt(i);
				StringBuffer sql = new StringBuffer("UPDATE ");
				sql.append(parent.getMTree().getNodeTableName())
					.append(" SET Parent_ID=").append(parent.getNode_ID())
					.append(", SeqNo=").append(i)
					.append(", Updated=SysDate")
					.append(" WHERE AD_Tree_ID=").append(parent.getMTree().get_ID())
					.append(" AND Node_ID=").append(nd.getNode_ID());
				log.fine(sql.toString());
				int no = DB.executeUpdate(sql.toString(),null);
			}
		}catch(Exception e){
			
		}
		
		/* FIXME-evenos: die richtige sequenz nummer benutzen */
	}
	
	public PreparedStatement pstmtForLoadingLeafWithNameOrDescription(String searchText) throws SQLException {
		
		//This models MTree
		MTree m_tree = ((MTreeNode)getRoot()).getMTree();
		
		//Strings for building SQLs
		String sql = null;
		StringBuilder sqlNode = new StringBuilder();
		String nodeTable = m_tree.getNodeTableName();
		String sourceTable = "t";
		String fromClause = m_tree.getSourceTableName(false);	//	fully qualified
		String columnNameX = m_tree.getSourceTableName(true); //--> AD_Menu, C_BPartner, AD_Org...
		String color = m_tree.getActionColorName();
		
		//PreparedStatement which we will return
		PreparedStatement pstmt = null;
		
		
		//Used for checking if we use base language or other
		boolean base = Env.isBaseLanguage(Env.getCtx(), "AD_Menu");
		
		//Menu specific
		if (m_tree.getTreeType().equals(MTree.TREETYPE_Menu))
		{
			
			sourceTable = "m";
			if (base){
				sqlNode.append("SELECT tn.Node_ID, tn.Parent_ID, tn.SeqNo, m.Name,m.Description, m.IsSummary, ");
				sqlNode.append("m.Action, m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Workflow_ID, m.AD_Task_ID, m.AD_Workbench_ID ");
				sqlNode.append("FROM AD_Menu m JOIN AD_TreeNodeMM tn ON m.ad_menu_id=tn.node_id ");
			}
			else{
				sqlNode.append("SELECT tn.Node_ID, tn.Parent_ID, tn.SeqNo, t.Name,t.Description, m.IsSummary, ");
				sqlNode.append("m.Action, m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Workflow_ID, m.AD_Task_ID, m.AD_Workbench_ID ");
				sqlNode.append("FROM AD_Menu m JOIN AD_TreeNodeMM tn ON m.ad_menu_id=tn.node_id ");
				sqlNode.append("JOIN AD_Menu_Trl t on m.ad_menu_id = t.ad_menu_id ");
			}
			
			if (!base)
				sqlNode.append(" WHERE m.AD_Menu_ID=t.AD_Menu_ID AND t.AD_Language=? ");
			if (!m_tree.isEditable())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ").append("m.IsActive='Y' ");
			}
			//	Do not show Beta
			if (!MClient.get(Env.getCtx()).isUseBetaFunctions())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Window_ID IS NULL OR EXISTS (SELECT * FROM AD_Window w WHERE m.AD_Window_ID=w.AD_Window_ID AND w.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Process_ID IS NULL OR EXISTS (SELECT * FROM AD_Process p WHERE m.AD_Process_ID=p.AD_Process_ID AND p.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Workflow_ID IS NULL OR EXISTS (SELECT * FROM AD_Workflow wf WHERE m.AD_Workflow_ID=wf.AD_Workflow_ID AND wf.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND f.IsBetaFunctionality='N'))");
			}
			//	In R/O Menu - Show only defined Forms
			if (!m_tree.isEditable())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND ");
				if (m_tree.isClientTree())
					sqlNode.append("f.Classname");
				else
					sqlNode.append("f.JSPURL");
				sqlNode.append(" IS NOT NULL))");
			}
			
			//Show only  for current tree
			boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
			sqlNode.append(hasWhere ? " AND " : " WHERE ");
			sqlNode.append(" tn.AD_Tree_ID=? ");
			
			
			//Show only matching the search term
			sqlNode.append(" AND (upper(m.name) like upper(?) or upper(m.description) like upper(?)) ");
			
			
			//Show only active nodes
			sqlNode.append(" AND tn.IsActive='Y' ");
			
			
			//In Menu, we want only leaf nodes - not summary nodes
			sqlNode.append(" AND m.issummary='N' ");
				
		}else{//Not menu specific but general
			if (columnNameX == null)
				throw new IllegalArgumentException("Unknown TreeType=" + m_tree.getTreeType());
					
			sqlNode.append("SELECT tn.Node_ID, tn.Parent_ID, tn.SeqNo, t.Name,t.Description,t.IsSummary, ");
			sqlNode.append(color);
			sqlNode.append(" FROM "+fromClause+" JOIN " + nodeTable + " tn ON t."+columnNameX+"_ID = tn.Node_ID "); 
			if (!m_tree.isEditable())
				sqlNode.append(" WHERE t.IsActive='Y'");
			
			//Show only for current tree
			boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
			sqlNode.append(hasWhere ? " AND " : " WHERE ");
			sqlNode.append(" tn.AD_Tree_ID=? ");
			sqlNode.append(" AND (upper(t.name) like upper(?) or upper(t.description) like upper(?)) ");
		}
		
		//Add the Access SQL
		sql = sqlNode.toString();
		if (!m_tree.isEditable())	
			sql = MRole.getDefault(Env.getCtx(), false).addAccessSQL(sql, 
				sourceTable, MRole.SQL_FULLYQUALIFIED, m_tree.isEditable());
		
		
		
		//Prepare the statement
		pstmt = DB.prepareStatement(sql, null);
		
		//Set statement parameter
		int i = 1;
		if (m_tree.getTreeType().equals(MTree.TREETYPE_Menu)){	//In menu
			
			//Set language if needed
			if (!base)
				pstmt.setString(i++, Env.getAD_Language(Env.getCtx()));
			
			//Set tree id
			pstmt.setInt(i++, m_tree.get_ID());
			
			//Set search term
			pstmt.setString(i++, "%"+searchText+"%");
			pstmt.setString(i++, "%"+searchText+"%");
						
		}else{	//not in menu
			
			//Set tree id
			pstmt.setInt(i++, m_tree.get_ID());
			
			//Set search term
			pstmt.setString(i++, "%"+searchText+"%");
			pstmt.setString(i++, "%"+searchText+"%");
		}
		
		
		return pstmt;	
		
	}



}
