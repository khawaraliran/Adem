package org.adempiere.webui.trees;

import java.awt.image.RenderedImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MClient;
import org.compiere.model.MRole;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.event.TreeDataEvent;

public class WTreeModel extends AbstractTreeModel implements TreeitemRenderer, EventListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * General constructor
	 * @param root
	 */
	public WTreeModel(Object root) {
		super(root);
	}

	/**
	 * Stores the WTree used in this model
	 */
	private WTree m_WTree;
	
	/**
	 * Set the WTree for this model
	 * @param tree
	 */
	public void setWTree(WTree tree){
		m_WTree = tree;
	}
	
	/**
	 * Get the WTree used in this model
	 * @return WTree
	 */
	public WTree getWTree(){
		return m_WTree;
	}
	
	/********************************************************************** 
	 * AbstractTableModel implementation								  *
	 **********************************************************************/
	
	@Override
	public boolean isLeaf(Object node) {
		WTreeNode tn = (WTreeNode)node;
		return !tn.isSummary();
	}

	@Override
	public Object getChild(Object parent, int index) {
		WTreeNode tn = (WTreeNode)parent;
		return tn.getChildAt(index);
	}

	
	@Override
	public int getChildCount(Object parent) {
		WTreeNode tn = (WTreeNode)parent;
		return tn.getChildCount();
	}


	
	/**********************************************************************
	 * TreeitemRenderer implementation									  *
	 **********************************************************************/
	
	/**
	 * Render a TreeItem with the date from a WTreeNode. Here we decide if a node in the tree is draggable, what its name is,
	 * how it looks like, which icon it has and so on
	 */
	@Override
	public void render(Treeitem item, Object data) throws Exception {
		
		//toString of WTreeNode, its the node name
		Treecell tc = new Treecell(Objects.toString(data));
		Treerow tr = null;
		
		//Reuse Treerows if possible 
		if(item.getTreerow()!=null){
			tr = item.getTreerow(); 
			tr.getChildren().clear();
		}else{
			tr = new Treerow();			
			tr.setParent(item);
			if (isItemDraggable()) {
				tr.setDraggable("true");
			}
			if (!onDropListners.isEmpty()) {
				tr.setDroppable("true");
				tr.addEventListener(Events.ON_DROP, this);
			}
			if (!onClickListners.isEmpty()){
				tr.addEventListener(Events.ON_CLICK, this);
			}
		}
		tc.setParent(tr);
		item.setValue(data);
		
		
		//Make sure data is really a WTreeNode
		if(data.getClass().equals(WTreeNode.class)){
			WTreeNode node = (WTreeNode)data;
			
			//If node is not a summary node, set the node icon
			if(!node.isSummary()){
				//Icons if tree is menu tree
				if(node.getWTree().isMenu()){
					if (node.isReport())
						item.setImage("/images/mReport.png");
	                else if (node.isProcess() || node.isTask())
	                	item.setImage("/images/mProcess.png");
	                else if (node.isWorkFlow())
	                	item.setImage("/images/mWorkFlow.png");
	                else
	                	item.setImage("/images/mWindow.png");
				}
				//Icons if tree is not a menu tree (e.g. organization)
				else{
					item.setImage("/images/mReport.png"); //FIXME: This is just a suggestion because no icons in the tree looks a little bit ugly
				}
					
				
				//If dragging leafs to a destination is allowed, set the draggable destination
				if(leafsDraggableDestination!=null && leafsDraggableDestination.length()>0)
					tr.setDraggable(leafsDraggableDestination);
			}
		}
	}

	
	
	/********************************************************************** 
	 * EventListener implementation										  *
	 **********************************************************************/
	
	
	/** List of ON_DROP EventListeners*/
	private List<EventListener> onDropListners = new ArrayList<EventListener>();
	/** List of ON_CLICK EventListeners*/
	private List<EventListener> onClickListners = new ArrayList<EventListener>();
	
	/**
	 * If an Event occurs in the model, we give the event to the registered EventListeners 
	 */
	@Override
	public void onEvent(Event event) throws Exception {
		if (Events.ON_DROP.equals(event.getName())) {
			for (EventListener listener : onDropListners) {
				listener.onEvent(event);
			}
		}
		
		if (Events.ON_CLICK.equals(event.getName())) {
			for (EventListener listener : onClickListners) {
				listener.onEvent(event);
			}
		}
	}
	
	/**
	 * Add an ON_DROP EventListener
	 * @param listener
	 */
	public void addOnDropEventListener(EventListener listener) {
		onDropListners.add(listener);
	}

	/**
	 * Add an ON_CLICK EventListener
	 * @param listener
	 */
	public void addOnClickEventListener(EventListener listener) {
		onClickListners.add(listener);
	}

	/**********************************************************************
	 * Others 				 											  *
	 **********************************************************************/

	
	/**
	 * Remove a node from the model and update the Tree
	 * @param node
	 */
	public void removeNode(WTreeNode node) {
		
		//Get Path from root node to the node we want to remove
		int path[] = this.getPathFromRootToNode(node);
		
		
		//If we got a path and the node is not the root we go through the path
		if (path != null && path.length > 0) {
			
			//We start at the root node
			WTreeNode parentNode = (WTreeNode) getRoot();
			
			//And go all the way till we find the last parent where we remove the node
			int index = path.length - 1;
			for (int i = 0; i < index; i++) {
				parentNode = (WTreeNode) getChild(parentNode, path[i]);
			}
			parentNode.getChildren().remove(path[index]);
			
			//Fire change event so the tree gets reloaded
			fireEvent(parentNode, path[index], path[index], TreeDataEvent.INTERVAL_REMOVED);
		}
		
	}

	/**
	 * Adds a WTreeNode to the models root node and update the Tree
	 * @param node
	 */
	public void addNode(WTreeNode node) {
		
		//Get root node
		WTreeNode root = ((WTreeNode)getRoot());
		
		//Add new node to root
		root.add(node);
		
		//Fire added event to reload tree
		fireEvent(root, root.getChildCount() - 1, root.getChildCount() - 1, TreeDataEvent.INTERVAL_ADDED);
	}
	
	
	/**
	 * Update the Tree for given WTreeNodes 
	 * @param newParent
	 * @param newNode
	 * @param index
	 */
	public void addNode(WTreeNode newParent, WTreeNode newNode,
			int index) {
		newParent.getChildren().add(index, newNode);
		fireEvent(newParent, index, index, TreeDataEvent.INTERVAL_ADDED);
	}
	
	/**
	 * Update the Tree for a given WTreeNode
	 * @param node
	 */
	public void nodeUpdated(WTreeNode node) {
		
		WTreeNode parent = getParent(node);
		if (parent != null) {
			int i = parent.getChildren().indexOf(node);
			fireEvent(parent, i, i, TreeDataEvent.CONTENTS_CHANGED);
		}
	}
	
	
	
	/**
	 * Try to find the Parent WTreeNode of a given WTreeNode 
	 * @param treeNode
	 * @return WTreeNode or null
	 */
	public WTreeNode getParent(WTreeNode treeNode) {
		int path[] = this.getPathFromRootToNode(treeNode);//this.getPath(getRoot(), treeNode);
			
		if (path != null && path.length > 0) {
			WTreeNode parentNode = (WTreeNode) getRoot();
			int index = path.length - 1;
			for (int i = 0; i < index; i++) {
				parentNode = (WTreeNode) getChild(parentNode, path[i]);
			}
						
			return parentNode;
		}
		
		return null;
	}
	
		
	/** Is Drag & Drop allowd*/
	private boolean itemDraggable;
	
	/**
	 * Enable/Disable dragging of nodes (e.g. for move a window from one summary node to another)
	 * @param b
	 */
	public void setItemDraggable(boolean b) {
		itemDraggable = b;
	}
	
	/**
	 * Tells if dragging nodes is allowed (e.g. for move a window from one summary node to another)
	 * @return boolean
	 */
	public boolean isItemDraggable() {
		return itemDraggable;
	}
	
	/**
	 * Possible destination where leafs could be dragged on
	 */
	private String leafsDraggableDestination;	
	
	/**
	 * This is for use e.g. in the MenuPanel. With this setter we can make the nodes in the MenuPanel
	 * draggable to be dragged into the DPFavourites - the favourites bar on the main tab
	 * see also render(Treeitem item, Object data)
	 */
	public void setLeafsDraggable(String destination){
		if(destination != null && destination.length()>0){
			leafsDraggableDestination = destination;
		}else{
			leafsDraggableDestination = null;
		}
	}
		
	
	/**
	 * Gets a TreePath from the root node to the given node
	 * @param lastNode
	 * @return int[] or null
	 */
	public int[] getPathFromRootToNode(WTreeNode lastNode) {
	
		//This list is used to determine the treepath
		List<Integer> nodeList = new ArrayList<Integer>();
				
		//Get/Build the reverse path to the searched node by their IDs in the database
		String sql = 
				"with recursive children as " +
				"( " + 
				"select * from " + getWTree().getNodeTableName() + 
				" where node_id = " + lastNode.getNode_ID() +
				" and ad_tree_id = " + getWTree().get_ID() +
				" union all " +
				" select child.* "+
				" from " +  getWTree().getNodeTableName() + " as child " +
				" join children as childs on (child.node_id = childs.parent_id) " +
				" ) " +
				" select * from children where ad_tree_id = " + getWTree().get_ID();
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
				
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				nodeList.add(rs.getInt(2));				
			}
		}catch(Exception e){}		
		DB.close(rs, pstmt);
		
		
		
		//Go down the list of IDs
		if(nodeList.size()>0){
			
			//List used to store the path
			List<Integer> pathList = new ArrayList<Integer>();
						
			//Start node
			WTreeNode parent = getWTree().getRoot();
			
			//Child node
			WTreeNode child = null;
			
			for(int i = nodeList.size()-1; i>=0; i--){
				
				if((Integer)nodeList.get(i) == 0)
					continue;
				
				//Try to find the next child
				child = parent.getChildWithNodeID((Integer)nodeList.get(i));
				System.out.println("Geladen Child: " + child);
				//Add the index of the child in its parent to the path
				pathList.add(parent.getIndexOfChild(child));
				
				//Set parent = child for the next iteration
				parent = child;
			}
			
			
			//Build int array from pathList
			int[] path = new int[pathList.size()];
			for(int i = 0; i < pathList.size(); i++)
				path[i] = pathList.get(i).intValue();
			return path;
		}else
			return null;
	}
	

	@Override
	public int[] getPath(Object parent, Object lastNode){
		System.out.println("Kein Path für dich heute! "+parent.getClass());
		WTreeNode parentNode = (WTreeNode)parent;
		WTreeNode searchedNode = (WTreeNode)lastNode;
		
		if(parentNode.getNode_ID() == ((WTreeNode)getRoot()).getNode_ID()){
			System.out.println("Nagut, ein path aber nur von der root node bis zu " + searchedNode);
			return getPathFromRootToNode(searchedNode);
		}
		else{
			return super.getPath(parent, lastNode);
		}
	}
	
	/**
	 * Gets a TreePath from the root node to a node with the given id
	 * @param node_id
	 * @return int[] or null
	 */
	public int[] getPathFromRootToNode(int node_id) {
		
		//This list is used to determine the treepath
		List<Integer> nodeList = new ArrayList<Integer>();
				
		//Get/Build the reverse path to the searched node by their IDs in the database
		String sql = 
				"with recursive children as " +
				"( " + 
				"select * from " + getWTree().getNodeTableName() + 
				" where node_id = " + node_id +
				" and ad_tree_id = " + getWTree().get_ID() +
				" union all " +
				" select child.* "+
				" from " +  getWTree().getNodeTableName() + " as child " +
				" join children as childs on (child.node_id = childs.parent_id) " +
				" ) " +
				" select * from children where ad_tree_id = " + getWTree().get_ID();
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
				
		try{			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				nodeList.add(rs.getInt(2));				
			}
		}catch(Exception e){}		
		DB.close(rs, pstmt);
		
		
		
		//Go down the list of IDs
		if(nodeList.size()>0){
			
			//List used to store the path
			List<Integer> pathList = new ArrayList<Integer>();
						
			//Start node
			WTreeNode parent = getWTree().getRoot();
			
			//Child node
			WTreeNode child = null;
			
			for(int i = nodeList.size()-1; i>=0; i--){
				
				//Try to find the next child
				child = parent.getChildWithNodeID((Integer)nodeList.get(i));
				
				//Add the index of the child in its parent to the path
				pathList.add(parent.getIndexOfChild(child));
				
				//Set parent = child for the next iteration
				parent = child;
			}
			
			
			//Build int array from pathList
			int[] path = new int[pathList.size()];
			for(int i = 0; i < pathList.size(); i++)
				path[i] = pathList.get(i).intValue();
			
			//Return the path
			return path;
			
		}else
			//if nodeList has no entries, we won't be able to build a path so just return null;
			return null;
	}
	
	
	/**
	 * Find a WTreeNode by its node_id in a given Tree
	 * @param node_id
	 * @param inTree
	 * @return WTreeNode or null
	 */
	public WTreeNode findNode(int node_id, Tree inTree){
		int[] path = getPathFromRootToNode(node_id);
		Treeitem item = inTree.renderItemByPath(path);
		
		if(item.getValue().getClass().equals(WTreeNode.class))
			return (WTreeNode)item.getValue();
		
		return null;
	}
	
	
	/**
	 * Returns a PreparedStatement for loading all leafs where name or description contains the search text. Also limits the resultset
	 * @param searchText
	 * @param limit
	 * @return PreparedStatement
	 */
	public PreparedStatement pstmtForLoadingLeafWithNameOrDescription(String searchText, int limit) throws SQLException{
		
		//This models MTree
		MTree m_tree = ((MTreeNode)getRoot()).getMTree();
		
		//Strings for building SQLs
		String sql = null;
		StringBuilder sqlNode = new StringBuilder();
		String nodeTable = m_WTree.getNodeTableName();
		String sourceTable = "t";
		String fromClause = m_WTree.getSourceTableName(false);	//	fully qualified
		String columnNameX = m_WTree.getSourceTableName(true); //--> AD_Menu, C_BPartner, AD_Org...
		String color = m_WTree.getActionColorName();
		
		
		//Used for checking if we use base language or other
		boolean base = Env.isBaseLanguage(Env.getCtx(), "AD_Menu");
		
		
		if (m_WTree.getTreeType().equals(MTree.TREETYPE_Menu))
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
				sqlNode.append("WHERE m.AD_Menu_ID=t.AD_Menu_ID AND t.AD_Language=? ");
			}
			
			if (!m_WTree.isEditable())
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
			if (!m_WTree.isEditable())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND ");
				if (m_WTree.isClientTree())
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
			
		}else{
			if (columnNameX == null)
				throw new IllegalArgumentException("Unknown TreeType=" + m_WTree.getTreeType());
					
			sqlNode.append("SELECT tn.Node_ID, tn.Parent_ID, tn.SeqNo, t.Name,t.Description,t.IsSummary, ");
			sqlNode.append(color);
			sqlNode.append(" FROM "+fromClause+" JOIN " + nodeTable + " tn ON t."+columnNameX+"_ID = tn.Node_ID "); 
			if (!m_WTree.isEditable())
				sqlNode.append(" WHERE t.IsActive='Y'");
			
			//Show only for current tree
			boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
			sqlNode.append(hasWhere ? " AND " : " WHERE ");
			sqlNode.append(" tn.AD_Tree_ID=? ");
			sqlNode.append(" AND (upper(t.name) like upper(?) or upper(t.description) like upper(?)) ");
			
		}
		
	
		
		//Add the Access SQL
		sql = sqlNode.toString();
		if (!m_WTree.isEditable())	//	editable = menu/etc. window
			sql = MRole.getDefault(Env.getCtx(), false).addAccessSQL(sql, 
				sourceTable, MRole.SQL_FULLYQUALIFIED, m_WTree.isEditable());
		
		if(limit>0)
			sql += " SQLLIMIT " + limit;
		
		
		//Prepare the statement which we will return
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
				
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
