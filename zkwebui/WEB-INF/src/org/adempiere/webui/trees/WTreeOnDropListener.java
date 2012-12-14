package org.adempiere.webui.trees;

import org.adempiere.webui.component.ADTreeOnDropListener;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MTreeNode;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.SimpleTreeNode;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class WTreeOnDropListener implements EventListener {
	private WTreeModel wTreeModel;
	private WTree wTree;
	private int windowNo;
	private Tree tree;
	
	private static final CLogger log = CLogger.getCLogger(WTreeOnDropListener.class);
	
	public WTreeOnDropListener(Tree tree, WTree wTree, WTreeModel wTreeModel, int windowNo){
		this.tree = tree;
		this.wTree = wTree;
		this.wTreeModel = wTreeModel;
		this.windowNo = windowNo;
	}
	
	
	/**
	 * @param event
	 */
	public void onEvent(Event event) throws Exception {
		if (event instanceof DropEvent) {
			DropEvent de = (DropEvent) event;
			log.fine("Source=" + de.getDragged() + " Target=" + de.getTarget());
			if (de.getDragged() != de.getTarget()) {
				Treeitem src = (Treeitem) ((Treerow) de.getDragged()).getParent();
				Treeitem target = (Treeitem) ((Treerow) de.getTarget()).getParent();
				moveNode((WTreeNode)src.getValue(), (WTreeNode)target.getValue());
			}
		} 
	}
	
	/**
	 *	Move TreeNode
	 *	@param	movingNode	The node to be moved
	 *	@param	toNode		The target node
	 */
	private void moveNode(WTreeNode movingNode, WTreeNode toNode)
	{
		log.info(movingNode.toString() + " to " + toNode.toString());

		if (movingNode == toNode)
			return;
		
					
		WTreeNode newParent;
		int index;
		if (!toNode.isSummary())	//	drop on a child node
		{
			moveNode(movingNode, toNode, false);
		}
		else						//	drop on a summary node
		{
			//TODO: Check if node to insert is a child of self - jan.thielemann@evenos.de
			//prompt user to select insert after or drop into the summary node
			int path[] = wTreeModel.getPathFromRootToNode(toNode);
			Treeitem toItem = tree.renderItemByPath(path);
			
			tree.setSelectedItem(toItem);
			Events.sendEvent(tree, new Event(Events.ON_SELECT, tree));
			
			WMenuListener listener = new WMenuListener(movingNode, toNode);
			
			//TODO: translation
			Menupopup popup = new Menupopup();
			Menuitem menuItem = new Menuitem("Insert After");
			menuItem.setValue("InsertAfter");
			menuItem.setParent(popup);
			menuItem.addEventListener(Events.ON_CLICK, listener);
			
			menuItem = new Menuitem("Move Into");
			menuItem.setValue("MoveInto");
			menuItem.setParent(popup);
			menuItem.addEventListener(Events.ON_CLICK, listener);
			
		
			
			popup.setPage(tree.getPage());
			popup.open(toItem.getTreerow());
			
		}
		
	}	//	moveNode
	
	private void moveNode(WTreeNode movingNode, WTreeNode toNode, boolean moveInto)
	{
		WTreeNode newParent;
		int index;		
		
		//  remove
		WTreeNode oldParent = wTreeModel.getParent(movingNode);
		wTreeModel.removeNode(movingNode);
		
		//get new index
		if (!moveInto)
		{
			newParent = wTreeModel.getParent(toNode);
			index = newParent.getChildren().indexOf(toNode) + 1;	//	the next node
		}
		else									//	drop on a summary node
		{
			newParent = toNode;
			index = 0;                   			//	the first node
		}
		
		//  insert
		wTreeModel.addNode(newParent, movingNode, index);
		


		//	***	Save changes to disk
		Trx trx = Trx.get (Trx.createTrxName("ADTree"), true);
		try
		{
			int no = 0;
			for (int i = 0; i < oldParent.getChildCount(); i++)
			{
				WTreeNode nd = (WTreeNode)oldParent.getChildAt(i);
				StringBuffer sql = new StringBuffer("UPDATE ");
				sql.append(wTree.getNodeTableName())
					.append(" SET Parent_ID=").append(oldParent.getNode_ID())
					.append(", SeqNo=").append(i)
					.append(", Updated=SysDate")
					.append(" WHERE AD_Tree_ID=").append(wTree.getAD_Tree_ID())
					.append(" AND Node_ID=").append(nd.getNode_ID());
				log.fine(sql.toString());
				no = DB.executeUpdate(sql.toString(),trx.getTrxName());
			}
			if (oldParent != newParent) 
			{
				for (int i = 0; i < newParent.getChildCount(); i++)
				{
					WTreeNode nd = (WTreeNode)newParent.getChildAt(i);
					StringBuffer sql = new StringBuffer("UPDATE ");
					sql.append(wTree.getNodeTableName())
						.append(" SET Parent_ID=").append(newParent.getNode_ID())
						.append(", SeqNo=").append(i)
						.append(", Updated=SysDate")
						.append(" WHERE AD_Tree_ID=").append(wTree.getAD_Tree_ID())
						.append(" AND Node_ID=").append(nd.getNode_ID());
					log.fine(sql.toString());
					no = DB.executeUpdate(sql.toString(),trx.getTrxName());
				}
			}
			//	COMMIT          *********************
			trx.commit(true);
		}
        catch (Exception e)
		{
			trx.rollback();
			FDialog.error(windowNo, tree, "TreeUpdateError", e.getLocalizedMessage());
		}
		trx.close();
		trx = null;
		
		
		int path[] = wTreeModel.getPathFromRootToNode(movingNode);
		Treeitem movingItem = tree.renderItemByPath(path);		
		tree.setSelectedItem(movingItem);
		Events.sendEvent(tree, new Event(Events.ON_SELECT, tree));
	}
	
	
	class WMenuListener implements EventListener {
		private WTreeNode movingNode;
		private WTreeNode toNode;
		WMenuListener(WTreeNode movingNode, WTreeNode toNode) {
			this.movingNode = movingNode;
			this.toNode = toNode;
		}
		public void onEvent(Event event) throws Exception {
			if (Events.ON_CLICK.equals(event.getName()) && event.getTarget() instanceof Menuitem) {
				Menuitem menuItem = (Menuitem) event.getTarget();
				if ("InsertAfter".equals(menuItem.getValue())) {
					moveNode(movingNode, toNode, false);
				} else if ("MoveInto".equals(menuItem.getValue())) {
					moveNode(movingNode, toNode, true);
				}
			}
		}
		
	}

}
