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
//import java.awt.datatransfer.Transferable;
//
//import javax.swing.JComponent;
//import javax.swing.JTree;
//import javax.swing.TransferHandler;
//import javax.swing.tree.DefaultTreeModel;
//import javax.swing.tree.TreePath;
//
//import org.compiere.model.MTreeNode;
//
///**
// *  VTreeTransferHandler provides the TransferHandler for dragging and dropping
// *  within a tree.  See VTreePanel.
// *  
// *  
// *  @author 	phib  2008/07/30
// *  FR [ 2032092 ] Java 6 improvements to tree drag and drop
// */
//public class VTreeTransferHandler extends TransferHandler {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public int getSourceActions(JComponent c) {
//		return TransferHandler.MOVE;
//	}
//
//	protected Transferable createTransferable(JComponent c) {
//		JTree tree = (JTree) c;
//		MTreeNode node = (MTreeNode) tree.getSelectionPath().getLastPathComponent();
//		return new TransferableTreeNode(node);
//	}
//
//	protected void exportDone(JComponent c, Transferable t, int action) {
//		if (action == MOVE) {
//			JTree tree = (JTree) c;
//			MTreeNode node = null;
//			try {
//				node = (MTreeNode) t.getTransferData(TransferableTreeNode.TREE_NODE_FLAVOR);
//			} catch (Exception e) {
//				// ignore
//			}
//			
//			if ( node != null )
//				((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
//		}
//	}
//
//	public boolean canImport(TransferSupport info) {
//		// Check for flavor
//		if (!info.isDataFlavorSupported(TransferableTreeNode.TREE_NODE_FLAVOR)) {
//			return false;
//		}
//		return true;
//	}
//	
//	public boolean importData(TransferHandler.TransferSupport info) {
//		if (!canImport(info))
//			return false;
//
//		JTree tree = (JTree) info.getComponent();
//		AdempiereTreeModel model = (AdempiereTreeModel) tree.getModel();
//		Transferable t = info.getTransferable();
//		MTreeNode to = null;
//		MTreeNode from = null;
//		int index;
//		try {
//			from = (MTreeNode)t.getTransferData(TransferableTreeNode.TREE_NODE_FLAVOR);
//		} 
//		catch (Exception e) { return false; }
//
//		if (info.isDrop()) {
//			JTree.DropLocation dl = (JTree.DropLocation)info.getDropLocation();
//			to = (MTreeNode) dl.getPath().getLastPathComponent();
//
//			if (from == to)
//				return false;
//
//			index = dl.getChildIndex();
//			if ( index == -1 )
//				index = 0;  // insert as first child
//
//		}
//		else {              // it's a paste
//			MTreeNode selected = (MTreeNode) tree.getSelectionPath().getLastPathComponent();
//			if ( selected.isLeaf() ) {
//				to = (MTreeNode) selected.getParent();
//				index = to.getIndex(selected) + 1;  // insert after selected
//			}
//			else {
//				to = selected;
//				index = 0;
//			}
//		}
//
//		model.insertNodeInto(from, to, index);
//		tree.scrollPathToVisible(new TreePath(from.getPath()));   // display from's new location
//		model.saveChangedNodes(from, to);
//
//		return true;
//	}
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

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.compiere.model.MTreeNode;
import org.jfree.util.Log;

/**
 *  VTreeTransferHandler provides the TransferHandler for dragging and dropping
 *  within a tree.  See VTreePanel.
 *  
 *  
 *  @author 	phib  2008/07/30
 *  FR [ 2032092 ] Java 6 improvements to tree drag and drop
 */
public class VTreeTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE;
	}

	/**
	 * Creates a Transferable so we can drag & drop a TreeNode
	 */
	protected Transferable createTransferable(JComponent c) {
		JTree tree = (JTree) c;
		MTreeNode node = (MTreeNode) tree.getSelectionPath().getLastPathComponent();
		
		//If the node is the root node, it's not a good idea to drag&drop it. Just return
		if(node.getNode_ID()==0){
			return null;
		}
		return new TransferableTreeNode(node);
	}

	protected void exportDone(JComponent c, Transferable t, int action) {
		if (action == MOVE) {
			JTree tree = (JTree) c;
			MTreeNode node = null;
			try {
				node = (MTreeNode) t.getTransferData(TransferableTreeNode.TREE_NODE_FLAVOR);
			} catch (Exception e) {
				// ignore
			}
			
			if ( node != null ){
				((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
				tree.updateUI();
			}
		}
	}

	public boolean canImport(TransferSupport info) {
		// Check for flavor
		if (!info.isDataFlavorSupported(TransferableTreeNode.TREE_NODE_FLAVOR)) {
			return false;
		}
		return true;
	}
	
	public boolean importData(TransferHandler.TransferSupport info) {
		
		//First check if importing the data is supported - means: is it a MTreeNode we want to handle?
		if (!canImport(info))
			return false;

		//Get the Tree we are currently in
		JTree tree = (JTree) info.getComponent();
		
		//Get the TreeModel from the Tree
		AdempiereTreeModel model = (AdempiereTreeModel) tree.getModel();
		
		//Get the node we are currently handling
		Transferable t = info.getTransferable();
		
		//Node to which the "from" node is moved to
		MTreeNode to = null;
		
		//Node which is moved
		MTreeNode from = null;
		
		
		try {
			//Try to load the Node which is moved. If node cannot be loaded (throws exception) return with false so nothing is changed
			from = (MTreeNode)t.getTransferData(TransferableTreeNode.TREE_NODE_FLAVOR);
		} 
		catch (Exception e) { return false; }

		
		//Index in which the node should get inserted in its new parent
		int index;
		
		//If the from-node war drag&dropped
		if (info.isDrop()) {
			
			//Get new parent - the node or place where the from-node was dropped 
			JTree.DropLocation dl = (JTree.DropLocation)info.getDropLocation();
			to = (MTreeNode) dl.getPath().getLastPathComponent();

			//If node was dropped on itself, return with false and do nothing
			if (from.equals(to))
				return false;

			//Make sure we get the right index for inserting the node
			index = dl.getChildIndex() == -1 ? 0 : dl.getChildIndex();
			
		}
		
		//the node was inserted by cut/paste
		else {              
			
			//Get the selected node in which we want to insert our from-node
			MTreeNode selected = (MTreeNode) tree.getSelectionPath().getLastPathComponent();
			
			//If selected node is a leaf, we insert our from-node after the leaf in the leafs parent folder
			if ( selected.isLeaf() && !selected.isSummary()) {
				to = (MTreeNode) selected.getParent();
				index = to.getIndex(selected) + 1;  
			}
			//If selected node is a folder, we insert our from-node at index 0 of this folder
			else {
				to = selected;
				index = 0;
			}
		}

		//Now that we know in which node (to) we want to insert the from-node and at what index we want to insert, 
		//we tell the model to insert the node
		model.insertNodeInto(from, to, index);
		
		//After inserting the node, the tree should update its UI 
		tree.updateUI();
				
		//Also the inserted node should be visible so tell the tree to scroll to the inserted node
		tree.scrollPathToVisible(new TreePath(from.getPath()));   
		
		//Tell the model to persist the changed nodes to the database 
		model.saveChangedNodes(from, to);

		//Return true because data was imported without errors
		return true;
	}

}
