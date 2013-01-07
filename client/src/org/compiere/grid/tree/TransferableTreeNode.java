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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.compiere.model.MTreeNode;

/**
 * TransferableTreeNode wraps an MTreeNode into a Transferable for use by drag
 * and drop type actions.
 * 
 * 
 * @author phib 2008/07/30 FR [ 2032092 ] Java 6 improvements to tree drag and
 *         drop
 */
class TransferableTreeNode implements Transferable {

	public static DataFlavor TREE_NODE_FLAVOR = new DataFlavor(MTreeNode.class,
			"Tree Path");

	DataFlavor flavors[] = { TREE_NODE_FLAVOR };


	MTreeNodeContainer nodecontainer;



	public TransferableTreeNode(MTreeNode node, int windowno) {
		this.nodecontainer = new MTreeNodeContainer(node, windowno);
	}

	public synchronized DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor.getRepresentationClass() == MTreeNode.class);
	}

	public synchronized Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor)) {
			return (Object) nodecontainer;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

}

class MTreeNodeContainer implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6548046451015886622L;
	MTreeNode node;
	int windowno;
	public MTreeNodeContainer(MTreeNode node, int windowNo){
		this.node = node;
		this.windowno = windowNo;
	}
}