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

import java.util.logging.Level;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
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



}
