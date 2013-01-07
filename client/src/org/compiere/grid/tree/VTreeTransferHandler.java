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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.compiere.model.MTreeNode;
import org.compiere.util.CLogger;

/**
 *  VTreeTransferHandler provides the TransferHandler for dragging and dropping
 *  within a tree.  See VTreePanel.
 *  
 *  
 *  @author 	phib  2008/07/30
 *  FR [ 2032092 ] Java 6 improvements to tree drag and drop
 */
public class VTreeTransferHandler extends TransferHandler {

	private static CLogger log = CLogger.getCLogger(VTreeTransferHandler.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int m_windowno;
	
	public VTreeTransferHandler(int windowNo){
		super();
		m_windowno = windowNo;
	}
	
	
	/**
	 * Used to determine if we have a drag or cut action. We don't need copy for our handler 
	 */
	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE;
	}
	
	

	/**
	 * Creates a TransferableTreeNode so we can drag & drop or paste this node
	 */
	protected Transferable createTransferable(JComponent c) {
		
		//Get selected node from tree
		JTree tree = (JTree) c;
		MTreeNode node = (MTreeNode) tree.getSelectionPath().getLastPathComponent();
		
		//If the node is the root node, it's not a good idea to drag&drop it. Just return nothing
		if(node.getNode_ID()==0){
			return null;
		}
		
		//Create new Transferable
		return new TransferableTreeNode(node, m_windowno);
	}

	
	/**
	 * Invoked after the drop/cut action. In our case, we need to remove the node 
	 * from its previous parent
	 */
	protected void exportDone(JComponent c, Transferable t, int action) {
		//Check if we have a move action here (from drag&drop or cut&paste)
		if (action == MOVE) {
			try {
				
				//Check if Transferable is a TransferableTreeNode
				if(t.getClass().equals(TransferableTreeNode.class)){
					
					//Check if TransferableTreeNode is from the correct window (so we can't e. g. insert Organization Nodes in Menu Trees)
					if(((TransferableTreeNode)t).nodecontainer.windowno == m_windowno){
						JTree tree = (JTree) c;
						MTreeNode node = ((MTreeNodeContainer)t.getTransferData(TransferableTreeNode.TREE_NODE_FLAVOR)).node;
						((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
						tree.updateUI();		
					}
				}		
				
			} catch (Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Invoked during dragging or paste action. Tells if the object is allowed to get dropped/pasted at this place.
	 */
	public boolean canImport(TransferSupport info) {
		// Check for flavor
		if (!info.isDataFlavorSupported(TransferableTreeNode.TREE_NODE_FLAVOR)) {
			return false;
		}
				
		//Check if we are in the correct window (e. g. we don't want to allow drag&drop organization nodes to menu window)
		try {
			Transferable t = info.getTransferable();
			MTreeNodeContainer c = (MTreeNodeContainer)t.getTransferData(TransferableTreeNode.TREE_NODE_FLAVOR);
			if(c.windowno != m_windowno){
				return false;
			}
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			e.printStackTrace();
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
		
		//Index in which the node should get inserted in its new parent
		int index;
		
		
		/* 
		 * First we need to know the from and to node. Then we decide if from node is one 
		 * of to nodes parent or itself. In this case we will not allow drop/paste 
		 */		
		try {
			/* 
			 * Try to load the Node which is moved. If node cannot be loaded (throws exception)
			 * return with false so nothing is changed
			 */
			from = ((MTreeNodeContainer)t.getTransferData(TransferableTreeNode.TREE_NODE_FLAVOR)).node;
		} 
		catch (Exception e) { 
			return false; 
		}
		

		if (info.isDrop()) {//If the from-node war drag&dropped

			//Get new parent - the node or place where the from-node was dropped 
			JTree.DropLocation dl = (JTree.DropLocation)info.getDropLocation();
			to = (MTreeNode) dl.getPath().getLastPathComponent();

			//Make sure we get the right index for inserting the node
			index = dl.getChildIndex() == -1 ? 0 : dl.getChildIndex();
			
		}else {   //the node was inserted by cut/paste           
			
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

				
		/* 
		 * If from node is in to nodes treePath, we don't want to drop/paste it. 
		 * A node, dropped to one of its children or itself would disappear!
		 */
		for(TreeNode treeNode : to.getPath()){
			MTreeNode node = (MTreeNode)treeNode;
			if(node.getNode_ID() == from.getNode_ID()){
				log.fine("Cannot drop node " + from + " on node " + to);
				return false;
			}
		}
		
		
		/*
		 * Because we want a node only to be pasted once, we set a new clipboard content
		 * after we pasted the node. This is the case if we don't have a (drag and) drop
		 */		
		if (!info.isDrop()){
			Clipboard c = tree.getToolkit().getSystemClipboard();
			c.setContents(new StringSelection(""), null);
		}
		
		
		/* 
		 * Now that we know in which node (to) we want to insert the from-node and at what index we want to insert, 
		 * we tell the model to insert the node. If the new parent is equal to the old parent, we first need to 
		 * to remove the node before inserting it again. Otherwise the from-node would be two times in the to-nodes
		 * children and we would probably save the wrong seqno for the from-node
		 */
		if(from.getParent()!=null && ((MTreeNode)from.getParent()).getNode_ID() == to.getNode_ID()){
			for(int i = 0; i < to.getChildCount(); i++)	{
				if(((MTreeNode)to.getChildAt(i)).getNode_ID() == from.getNode_ID()){
					to.remove(i);
					break;
				}
			}
		}
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
