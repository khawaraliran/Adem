package org.compiere.grid.tree;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.tree.TreePath;

import org.compiere.apps.SwingWorker;
import org.compiere.model.MRole;
import org.compiere.model.MTreeNode;
import org.compiere.model.X_AD_Menu;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 * Tree Search Worker. Searches for a String in a VTreePanels tree and selects/expands a found node
 * 
 * @author Jan Thielemann - jan.thielemann@evenos.de - evenos GmbH - www.evenos.de
 * @version $Id: VSearchTreeNodeWorker.java,v 1.0 2012/12/10 10:13:05 jant Exp $
 */
public class VSearchTreeNodeWorker extends SwingWorker {

	/** The VTreePanel in which we search */
	private VTreePanel vTreePanel;
	
	/** The search text for which we search */
	private String searchText;
	
	/** copy of current search string to determine if search string has changed since last search */
	private static String currentSearchText;
	
	/** List of current results so we can go through them each time we press enter (without changing the search term) */
	private static List<HashMap<String, Object>> currentSearchResults;
	
	/** The current selected result */
	private static HashMap<String, Object> currentSelectedSearchresult;
		
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VSearchTreeNodeWorker.class);
	
	
	
	/** Constructor */
	public VSearchTreeNodeWorker(VTreePanel panel, String searchText){
		this.vTreePanel = panel;
		this.searchText = searchText;
	}

	
	
	/** Method which automatically get called if we start the SwingWorker*/
	@Override
	public Object construct() {
		log.config("Start search");
		findNodeInSearchList(searchText);
		return null;
	}

	
	
	/**
	 * Search for a node in the searchList depending on text which can be either name or description of the node
	 * @param searchText
	 */
	private void findNodeInSearchList(String searchText){
		log.fine("Searching for \""+searchText+"\"");
		
		//Check if searching a new searchterm
		if(!searchText.equals(currentSearchText)){
			
			//Init search list for new search term
			initSearchList(searchText);
			
			//Set current search text to new search term
			currentSearchText = searchText;
		
		}
		
		
		//If the search has results...
		if(currentSearchResults.size()>0){
			
			//...check if we already selected a result
			int index = currentSearchResults.indexOf(currentSelectedSearchresult);
			
			//Check if the currentSelectedSearchResult is in the currentSearchResults, if not, set it to results 0 item
			if(index == -1 || !(index < currentSearchResults.size()-1)){
				index = 0;
			}else{
				index++;
			}		
			
			//Get map to result
			currentSelectedSearchresult = currentSearchResults.get(index);
			
			
			//Search for node in tree by its map (which 
			searchNodeIdInTree(currentSelectedSearchresult);
			
			
		}else{
			log.config("No entry found for \""+searchText+"\"");
			vTreePanel.getSearchField().setEnabled(true);
			vTreePanel.getSearchField().requestFocus();	
		}
		
	}
	
	
	/**
	 * Initialize the SearchList. We use the Search List to find nodes in the tree 
	 * even if they are not loaded because of the lazy loading feature.
	 */
	private void initSearchList(String searchText){
		
		//Reset Search Results
		currentSearchResults = new ArrayList<HashMap<String, Object>>();
		
		//Get SQL for loading all possible nodes for the trees root node
		String sql = vTreePanel.getRoot().buildSQLForLoadingAllNodesWithNameOrDescription(searchText);
		
		//PreparedStatement & ResultSet
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try{
			//Prepare the statement
			pstmt = DB.prepareStatement(sql, null);
			
			//Execture the Query
			rs = pstmt.executeQuery();
			
			
			//While we have lines in the ResultSet...
			while(rs.next()){
				//...create a HashMap from the ResultSet...
				HashMap<String, Object> rsMap = new HashMap<String, Object>();
				rsMap.put("name", (rs.getString("name") == null ? "" : rs.getString("name")));
				rsMap.put("description", (rs.getString("description") == null ? "" : rs.getString("description")));
				rsMap.put("parent_id", rs.getInt("parent_id"));
				rsMap.put("node_id", rs.getInt("node_id"));
				
						
				//If Tree is a menu tree, we only search for Windows, Processes, Forms, Workflows or Tasks - Summary Folder or Workbench is not supported
				if(vTreePanel.getRoot().getMTree().isMenu()){
					MRole role = MRole.getDefault(Env.getCtx(), false);
					Boolean access = null;
					
					String action = rs.getString("action");
					int AD_Window_ID = rs.getInt("ad_window_id");
					int AD_Process_ID = rs.getInt("ad_process_id");
					int AD_Form_ID = rs.getInt("ad_form_id");
					int AD_Workflow_ID = rs.getInt("ad_workflow_id");
					int AD_Task_ID = rs.getInt("ad_task_id");
					
					if (X_AD_Menu.ACTION_Window.equals(action))
						access = role.getWindowAccess(AD_Window_ID);
					else if (X_AD_Menu.ACTION_Process.equals(action) 
						|| X_AD_Menu.ACTION_Report.equals(action))
						access = role.getProcessAccess(AD_Process_ID);
					else if (X_AD_Menu.ACTION_Form.equals(action))
						access = role.getFormAccess(AD_Form_ID);
					else if (X_AD_Menu.ACTION_WorkFlow.equals(action))
						access = role.getWorkflowAccess(AD_Workflow_ID);
					else if (X_AD_Menu.ACTION_Task.equals(action))
						access = role.getTaskAccess(AD_Task_ID);
					if (access != null){
						currentSearchResults.add(rsMap);
					}
					
				}
				//If not menu, always add
				else{
					//...and put the Map into the searchList 
					currentSearchResults.add(rsMap);					
				}
				
				log.fine("add to searchList: name="+rs.getString("name")+" desc="+rs.getString("description")
						+" parent="+rs.getInt("parent_id")+" node_id="+rs.getInt("node_id"));
				
			}
			
			
		}catch (Exception e){
			log.severe("Error during initialization of Search Map");
		}
		
		//Finally close ResultSet & PreparedStatement
		DB.close(rs, pstmt);
	}
	

	

	private void searchNodeIdInTree(HashMap<String, Object> nodeMap){
		//This list is used to determine the treepath
		List<Integer> nodeList = new ArrayList<Integer>();
		
		//Get/Build the reverse path to the searched node
		String sql = 
				"with recursive children as " +
				"( " +
				"select * from " + vTreePanel.getRoot().getMTree().getNodeTableName() + 
				" where node_id = " + nodeMap.get("node_id") +
				" and ad_tree_id = " + vTreePanel.getRoot().getMTree().get_ID() +
				" union all " +
				" select child.* "+
				" from " +  vTreePanel.getRoot().getMTree().getNodeTableName() + " as child " +
				" join children as childs on (child.node_id = childs.parent_id) " +
				" ) " +
				" select * from children where ad_tree_id = " + vTreePanel.getRoot().getMTree().get_ID();
				
		log.fine(sql);		
		
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
		
		
		
		
		try{
			//If there is a path, try to expand it
			if(nodeList.size()>0){
			MTreeNode node = null;
			for(int i = nodeList.size()-1; i>=0; i--){
				//Try to find the node which we want to expand/show
				node = vTreePanel.getRoot().findNode((Integer)nodeList.get(i));
				
				//Get the TreePath to the node
				TreePath treePath = new TreePath(node.getPath());
				
				//If the node isn't the last node in the TreePath, expand it
				if(i>0)	vTreePanel.getTree().expandPath(treePath);
				
				//Select the node, make it visible and scroll to the node so the user sees it
				vTreePanel.getTree().setSelectionPath(treePath);
				vTreePanel.getTree().makeVisible(treePath);			
				vTreePanel.getTree().scrollPathToVisible(treePath);
			}
			
			//Select node only if not menu (in menu this would open e.g. a window instead of only selecting it
			if(!vTreePanel.getRoot().getMTree().isMenu())
				vTreePanel.setSelectedNode(node);
			
		}
		}catch(NullPointerException e){
			log.fine("Could not find and select node " + e);
		}finally{
			vTreePanel.getSearchField().setEnabled(true);
			vTreePanel.getSearchField().requestFocus();	
		}
		log.config("Search done");
	}
	

	
	
}
