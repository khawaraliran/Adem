/**
 * Class creates a form with two boxes contains their own data and allows to move the data from one box to another
 * 
 * -----------------------------------------------------------------------------
 * |	data availbale			|				|	data added	|	amount	   |
 * -----------------------------------------------------------------------------
 * |	data1					|	/---		|		data5	|	2		   |
 * |	data2					|	\---		|		data6	|	3		   |
 * |	data3					|				|		data7	|	1		   |
 * |							|	----\		|				|			   |
 * |							|	----/		|				|			   |
 * |							|				|				|			   |
 * |							|				|				|			   |
 * |							|				|				|		 	   |
 * -----------------------------------------------------------------------------
 * 								OK			Cancel
 * -----------------------------------------------------------------------------
 */
package org.adempiere.webui.apps.dailog;


import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.ListCell;
import org.adempiere.webui.component.ListHead;
import org.adempiere.webui.component.ListHeader;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.panel.ADButtonDailog;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.X_HR_Quarter_Appliances;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.East;
import org.zkoss.zkex.zul.South;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Vbox;

/**
  * @Bug   @author       @Change ID           @Description
 * ************************************************************************************************************
 *  1639    Arunkumar    				      This Class Is Used To Add Appliances To A Quarter.
 *  1639    Arunkumar    [20120102:9:00PM]    Appliances are appearing many times  :  To Solve This Problem 
 *                                            we made code Change in A Way that We Making hat List Item As available to Garbage collector
 *                                            
 *                       [20120102:9:15PM]    Before Adding An Applience We Have To Delete With the Same ProductID
 * 1639      Arunkumar   [20120109:01:00PM]   Mofied deleteAppliances(int)  method in such a way that if we pass product Id 
 * 											  It will delete the That specific product from Quarter Appliance Other Wise It Will Delete all the products.
 * 1639      Arunkumar   [20120109:01:00PM]   Modified : onEvent method : First We are Going to Select The Required Appliences Then After Click on Ok Buuton 
 * 											  we are going to delete all the applieances in that quarter   And Create the Selectd Appliances With Given Qunatities.                 
 */
public class WAddApplianceDialog extends  ADButtonDailog {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: WAddApplianceDialog.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1353525L;
	
	ProcessInfo pi = null;
	
	int quarterId = 0;
	Borderlayout mainPanel = new Borderlayout();
	West west = new West();
	East east = new East();
	Center center = new Center();
	South south = new South();
	Listbox avaialable = new Listbox();
	Listbox selected = new Listbox();
	Vbox buttons = new Vbox();
	Button add = new Button();
	Button remove = new Button();
	
	Rows availablerows = new Rows();
	ListHead availablelistHead = new ListHead();
	ListHeader availablehead = new ListHeader(AVAILABLE_HEADER);
	
	Rows selectedRows = new Rows();
	ListHead selectedListHead = new ListHead();
	ListHeader selectedHead = new ListHeader(SELECTED_HEADER);
	ListHeader selectedHeadQuantity= new ListHeader("Quantity");
	
	ConfirmPanel confirm = new ConfirmPanel(true,false,false,false,false,false,false);
	static String AVAILABLE_HEADER = "Available Appliances";
	static String SELECTED_HEADER = "Selected Appliances";
	
	ListItem selectedQuantity = null;
	ListCell quantity = null;
	Textbox quantityTb = null;
	ListCell lab = null;
	
	ListItem availableitem = null;
	
	Vbox vb = new Vbox();
	// create the actual component
	private void createComponent()
	{
		mainPanel.appendChild(east);
		mainPanel.appendChild(west);
		mainPanel.appendChild(south);
		mainPanel.appendChild(center);
		east.setWidth("48%");
		west.setWidth("48%");
		
		availablelistHead.setParent(avaialable);		
		availablehead.setParent(availablelistHead);
		avaialable.setSeltype("multiple");
		
		
		selectedListHead.setParent(selected);		
		selectedHead.setParent(selectedListHead);		
		selectedHeadQuantity.setParent(selectedListHead);
		selected.setSeltype("multiple");
		
		vb.setHeight("150px");
		buttons.appendChild(vb);
		add.setImage("images/Detail24.png");
		add.addEventListener(Events.ON_CLICK, this);
		remove.setImage("images/Parent24.png");
		remove.addEventListener(Events.ON_CLICK, this);
		buttons.appendChild(add);
		buttons.appendChild(remove);
		buttons.setParent(center);
		selected.setParent(east);
		avaialable.setParent(west);
		confirm.setParent(south);
		mainPanel.setParent(this);
		confirm.addActionListener(Events.ON_CLICK, this);
		
		prepareAvailableListbox();
		prepareSelectListbox();
	}
	private void prepareSelectListbox(){
		
		List<X_HR_Quarter_Appliances> appliances = loadSelectData();
		MProduct product = null;
		if(appliances != null){
			for(X_HR_Quarter_Appliances app : appliances){
				product = new MProduct(Env.getCtx(), app.getM_Product_ID(), null);
				selectedQuantity = new ListItem();
				quantity = new ListCell();
				quantityTb = new Textbox(String.valueOf(app.getquantity()));
				quantity.appendChild(quantityTb);
				lab = new ListCell(product.getName());
				lab.setValue(product.getM_Product_ID());
				lab.setParent(selectedQuantity);
				quantity.setParent(selectedQuantity);
				selected.appendChild(selectedQuantity);
			}
		}
	}
	
	
	private List<X_HR_Quarter_Appliances> loadSelectData(){
		Query query = new Query(Env.getCtx(),X_HR_Quarter_Appliances.Table_Name,X_HR_Quarter_Appliances.COLUMNNAME_HR_Quarter_ID+" = "+quarterId,null);
		List<X_HR_Quarter_Appliances> appliances = query.list();
		return appliances;
	}
	
	private void prepareAvailableListbox(){
		List<MProduct> appliances = loadAvailableData();
		if(appliances != null){
			for(MProduct prod : appliances)	{
				
				avaialable.appendItem(prod.getName(), prod.getM_Product_ID());
			}
		}
	}
	
	private List<MProduct> loadAvailableData(){
		StringBuffer whereClause = new StringBuffer();
		whereClause.append(MProductCategory.COLUMNNAME_M_Product_Category_ID);
		whereClause.append("=" +EagleConstants.APPLIANCES_PRODUCT_CATEGORY+" AND " +
				MProduct.COLUMNNAME_M_Product_ID+" NOT IN (" +
						"select "+X_HR_Quarter_Appliances.COLUMNNAME_M_Product_ID +" FROM "+X_HR_Quarter_Appliances.Table_Name+" WHERE "+X_HR_Quarter_Appliances.COLUMNNAME_HR_Quarter_ID+" = "+quarterId+
						")");
		Query query = new Query(Env.getCtx(),MProduct.Table_Name,whereClause.toString(),null);
		List<MProduct> appliances = query.list();
		return appliances;
		
	}
	
	public void onEvent(Event event){
		if(event.getTarget() == add){
			Set<ListItem> selectedIdices = avaialable.getSelectedItems();
			//[20120102:9:00PM]	
		if(selectedIdices != null) {
			ListItem[] removableItems = new ListItem[selectedIdices.size()];
			int arrindex=0;
			for(ListItem idex : selectedIdices)
			{
				removableItems[arrindex]=idex;
				arrindex++;
			}
			
			for(ListItem idex : selectedIdices){

				idex.setVisible(false);
				
					selectedQuantity = new ListItem();
					quantity = new ListCell();
					quantityTb = new Textbox("1");
					quantity.appendChild(quantityTb);
					lab = new ListCell(idex.getLabel());
					lab.setValue(idex.getValue());
					lab.setParent(selectedQuantity);
					quantity.setParent(selectedQuantity);
					selected.appendChild(selectedQuantity);		
				}
			
			avaialable.renderAll();
			
			
			for(int i=0;i<removableItems.length;i++)
			{
				ListItem ite=removableItems[i];
				ite.setParent(null);
			}
		  }
		}
		if(event.getTarget() ==remove){
			
			Set<ListItem> selectedIndices =(Set<ListItem>)selected.getSelectedItems();
			
			if(selectedIndices != null) {
			
				ListItem[] removableItems = new ListItem[selectedIndices.size()];
			        
		        int arrindex=0;
				for(ListItem index : selectedIndices)
				{
					removableItems[arrindex]=index;
					arrindex++;
				}
				
				
				for(ListItem item : selectedIndices){
				
					item.setVisible(false);
					List<ListCell> childs =  item.getChildren();
					availableitem = new ListItem(childs.get(0).getLabel(),childs.get(0).getValue());
					avaialable.appendChild(availableitem);
				}
				avaialable.renderAll();
				for(int i=0;i<removableItems.length;i++)
				{
					ListItem ite=removableItems[i];
					ite.setParent(null);
				}
			}
		}
		if(event.getTarget().getId().equalsIgnoreCase("OK")){
			List<ListItem> items = selected.getItems();
			int productid =0,quantity =0;
			
			
			if(items != null && items.size() > 0) {
				//[20120109:01:00PM]
				//Before Adding An Applience We Have To Delete All The Appliances of that quarter
				deleteAppliances(0);
				for(ListItem item : items)
				{
						List<ListCell> childs =  item.getChildren();
						ListCell cel = childs.get(0);
						for(ListCell cell : childs)
						{
							if(cell.getChildren().size() != 0)
							{
								quantity = Integer.parseInt(((Textbox)cell.getFirstChild()).getValue());
							}
							else
							{
								productid = (Integer) cell.getValue();
							}
							
						}
					if(item.isVisible()) {
						addAppliance(productid, quantity);
					}
					else {
						deleteAppliances(productid);
					}
				}
			}
			this.detach();
			
		}
		if(event.getTarget().getId().equalsIgnoreCase("cancel")){
			this.detach();
			
		}
		
	}
	
	
	/**
	 * This Method is used to add the appliances
	 * @param productid
	 * @param quantity
	 */
	private void addAppliance(int productid,int quantity){
		
		X_HR_Quarter_Appliances quarterapp  = new X_HR_Quarter_Appliances(Env.getCtx(), 0, null);
		quarterapp.setHR_Quarter_ID(quarterId);
		quarterapp.setM_Product_ID(productid);
		quarterapp.setquantity(quantity);
		Boolean success =  quarterapp.save();
		if(! success) {
			logger.log(Level.SEVERE, "Can't save Quarter Appliance For ProductID "+productid);
		}
	}
	
	
	/**
	 * This Method Is used To Delete The Appliances
	 * @param productid
	 * @param quantity
	 */
	private void deleteAppliances(int productid ){
	
		String whereClause = X_HR_Quarter_Appliances.COLUMNNAME_HR_Quarter_ID+"= "+quarterId;
		if(productid > 0) {
			whereClause = whereClause+" AND " +X_HR_Quarter_Appliances.COLUMNNAME_M_Product_ID +"=" +productid;
		}

		Query query = new Query(Env.getCtx(),X_HR_Quarter_Appliances.Table_Name,whereClause,null);
		List<X_HR_Quarter_Appliances> apps = query.list();
		for(X_HR_Quarter_Appliances app : apps){
			app.delete(true);
		}
	}

	@Override
	protected void initButtonDailog() {
		pi = getProcessInfo(); 
		
		//Prepares The Record Id
		prepare(); 
		
		createComponent();
		this.setHeight("600px");
		this.setWidth("1000px");
		this.setTitle("Add/Update Appliances");
		this.setBorder("1");
	}
	
	protected void prepare() {
		
		if( pi != null )	{

			quarterId = pi.getRecord_ID();
		}
	}
}
