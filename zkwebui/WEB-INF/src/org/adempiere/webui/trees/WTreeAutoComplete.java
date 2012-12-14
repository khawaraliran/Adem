package org.adempiere.webui.trees;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.adempiere.webui.component.Combobox;
import org.compiere.model.MRole;
import org.compiere.model.X_AD_Menu;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Comboitem;

public class WTreeAutoComplete  extends Combobox 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2642639623099513816L;

	/** comboItems	All menu labels	 */
//	private String[] comboItems;
	
	/** strDescription	Description of menu items	 */
//	private String[] strDescription;

	/** Replacement of comboItems and strDescription	*/
	
	private static List<HashMap<String, Object>> searchResults;
	
	
	private WTreeModel m_wmodel;
	
	/**
	 * Set menu labels
	 * 
	 * @param vals	Menu labels
	 */
	
//	public void setDict(String[] vals)
//	{
//		comboItems = vals;
//		
//		if (comboItems != null)
//		{
//			Arrays.sort(comboItems);
//		}
//	}
	
	/**
	 * Set description of menu items
	 * 
	 * @param vals	Description of menu items
	 */
	
//	public void setDescription(String[] vals)
//	{
//		strDescription = vals;
//	}
	
	/**
	 * 	Constructor
	 */
	
	public WTreeAutoComplete() 
	{
//		if (comboItems != null)
//			refresh("");
	}
	
	public WTreeAutoComplete(String value) 
	{
		super.setValue(value);
	}

	public void setValue(String value) 
	{
		super.setValue(value);
		refresh(value);
	}
	
	
	public void setWTreeModel(WTreeModel model){
		m_wmodel = model;
	}
	
	
	/**
	 * Event handler responsible to reducing number of items
	 * Method is invoked each time something is typed in the combobox
	 * 
	 * @param evt	The event
	 */
	public void onChanging(InputEvent evt) 
	{
		//System.out.println("AutoComplete onChange Event = "+evt);
		if (!evt.isChangingBySelectBack())
		{
			refresh(evt.getValue());
		}
	}
	
	
	
	
	
	/*TODO: This methods looks for 20 entries for the auto complete list 
	 * BUT it checks later if the current role is allowed to see the entries
	 * so it is possible that from 20 entries only 5 are visible to the user.
	 * this is bad, buildSQLForLoadingAllLeafsWithNameOrDescription() should be 
	 * refactored to include all the access logic directly!
	 */
	/** 
	 * Refresh comboitem based on the specified value.
	*/	
	private void refresh(String val) 
	{
	
		//Reset search results
		searchResults = new ArrayList<HashMap<String, Object>>(); 
		
		
		//Only search for search terms greater than 3 characters
		if(val.trim().length()>0){
			//System.out.println("Value is greater than 3 characters. Start searching");
			
			
			
			//Build SQL for loading results for the auto complete list
			String sql = m_wmodel.buildSQLForLoadingAllLeafsWithNameOrDescription(val.trim(), 20);
				
			
			
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
				
					//New temporary map for each of the result sets lines
					HashMap<String, Object> nodeMap = new HashMap<String, Object>(); 
					
					int id = rs.getInt("node_id");
					String name = rs.getString("name");
					String desc = rs.getString("description");
					
					if(name == null)
						name = "";
					if(desc == null)
						desc = "";
					
					
					//If Tree is a menu tree, we only search for Windows, Processes, Forms, Workflows or Tasks - Summary Folder or Workbench is not supported
					if(m_wmodel.getWTree().isMenu()){
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
							nodeMap.put("name", name);
							nodeMap.put("description", desc);
							nodeMap.put("node_id", id);		
							searchResults.add(nodeMap);
						}
						
					}
					//If not menu, always add
					else{
						nodeMap.put("name", name);
						nodeMap.put("description", desc);
						nodeMap.put("node_id", id);
						searchResults.add(nodeMap);
					}
				}				
			}catch (Exception e){
				System.out.println("Error during initialization of Search Results");
				System.out.println(e);
			}
			
			//Finally close ResultSet & PreparedStatement
			DB.close(rs, pstmt);
		}
		
		//Clear the AutoComplete list if no search results are found
		if (searchResults == null || searchResults.size() == 0) {
			super.getChildren().clear();
			return;
		}
		

		Iterator<?> it = getItems().iterator();
		for(int i = 0; i < searchResults.size(); i++){
			Comboitem comboitem = null;
			if (it != null && it.hasNext()) {
				comboitem = ((Comboitem)it.next());
		    } else {
		        it = null;
		        comboitem = new Comboitem();
		        super.appendChild(comboitem);
		    }
			HashMap<String, Object> nodeMap = searchResults.get(i);
			comboitem.setLabel((String)nodeMap.get("name"));
			comboitem.setDescription((String)nodeMap.get("description"));
			comboitem.setValue(nodeMap);
		}

		while (it != null && it.hasNext()) {
	      it.next();
	      it.remove();
	    }		
	}
}
