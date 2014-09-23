/**
 * 
 */
package org.adempiere.webui.apps.form;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.panel.ADForm;
import org.compiere.model.MBPartner;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.eevolution.model.MHREmployee;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Span;
import org.zkoss.zul.Textbox;


/**
 * 
 * @author PhaniKiran.Gutha
 *
 */
public class WSalaryCalculator extends ADForm implements EventListener{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: WSalaryCalculator.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2806573408787157127L;

	/**
	 * 
	 */
	public WSalaryCalculator() {
	}

	/* (non-Javadoc)
	 * @see org.adempiere.webui.panel.ADForm#initForm()
	 */
	@Override
	protected void initForm() {

		initComponents();
		dynInit(null);
		
	}

	private void initComponents()
	{
		
		
		h1 		= new Hbox();
		h2		= new Hbox();
		h3 		= new Hbox();
		h4 		= new Hbox();
		
		curSalVal	= new Textbox();
		newSalVal	= new Textbox();
		diffAmtVal	= new Textbox();
		perYrVal	= new Textbox();
		hikeVal		= new Textbox();
		
		newSalVal.setReadonly(true);
		curSalVal.setReadonly(true);
		diffAmtVal.setReadonly(true);
		perYrVal.setReadonly(true);
		hikeVal.setReadonly(true);
		
		Panel p = new Panel();
		Div d =new Div();
		d.setWidth("400px");
		h1.setStyle("cellspacing:10px;");
		h1.appendChild(d);
		h1.appendChild(curSal);
		h1.appendChild(curSalVal);
		h1.setAlign("Center");
		h1.setParent(p);
		
		type.setRows(0);
		type.setMultiple(false);
		type.setMold("select");
//		type.setWidth("150px");    
		type.addEventListener(Events.ON_SELECT, this);
		h2.appendChild(type);
		fixed.setWidth("100px");
		radioGp.appendChild(fixed);
		
		Span d2 =new Span();
		d2.setWidth("5px");
		radioGp.appendChild(d2);
		radioGp.appendChild(percentage);
		radioGp.setWidth("600px");
		radioGp.setSelectedIndex(1);
		Div d1 =new Div();
		d1.setWidth("250px");
		h2.appendChild(d1);
		h2.appendChild(radioGp);
		h2.setParent(p);
		
		p.setParent(north);
		
		Panel p1 = new Panel();
		
		h3.appendChild(calcSal);
		calcSal.addEventListener(Events.ON_CLICK, this);
		h3.setParent(p1);
		h3.setAlign("Center");
		
		h4.appendChild(newSal);
		h4.appendChild(newSalVal);
		h4.appendChild(diffAmt);
		h4.appendChild(diffAmtVal);
		h4.appendChild(perYr);
		h4.appendChild(perYrVal);
		h4.setParent(p1);
		
		p1.setParent(south);
		
		
		column = new Column("Type Name");
		column.setParent(columns);
		
		column = new Column("Head Count"); 
		column.setParent(columns);
		
		column = new Column("Hike Per Month");
		column.setParent(columns);
		
		columns.setParent(centerGrid);		
		columns.setSizable( Boolean.TRUE );  
		rows.setParent(centerGrid);
		rows.setWidth("100%");  
		centerGrid.setParent(center);
		centerGrid.setFixedLayout( Boolean.TRUE );   
		type.setVflex( Boolean.TRUE );
		mainLayout.appendChild(center);
		mainLayout.appendChild(north);
		mainLayout.appendChild(south);
		mainLayout.setParent(this);
	}
	
	
	Borderlayout	mainLayout		= new Borderlayout();
	Center			center	     	= new Center();
	North			north	      	= new North();
	South			south		  	= new South();
	Grid			centerGrid    	= new Grid();
	Listbox			type	       	= new Listbox();
	Columns			columns			= new Columns();
	Rows			rows			= new Rows();
	Button			calcSal		  	= new Button("Calculate Salary");
	Radiogroup		radioGp			= new Radiogroup();
	Radio			fixed		  	= new Radio("Fixed Amount    ");
	Radio			percentage	 	= new Radio("Percentage      ");
	Label 			newSal			= new Label("New Salary");
	Label			diffAmt			= new Label("Difference Amount");
	Label			perYr			= new Label("Per Year");
	Label			curSal			= new Label("Current Salary");
	
	HashMap<String,HashMap<Integer,BigDecimal>> typelist = new HashMap<String,HashMap<Integer,BigDecimal>>();
	
	Column			column;
	Row				row,changedRow;
	Hbox			h1,h2,h3,h4;
	Label			empTye , headCount;
    Textbox			curSalVal,newSalVal,diffAmtVal,perYrVal,hikeVal;
    ListItem 		Listitem;
    BigDecimal      currentSal= Env.ZERO,gradeSal= Env.ZERO,empTypeSal= Env.ZERO,designationSal= Env.ZERO;
    
	
	
	@SuppressWarnings("unchecked")
	private void dynInit(Listitem item)
	{
		if(item == null)
		{
			Listitem = new ListItem("Select Type",0);
			Listitem.setParent(type);
			
			Listitem = new ListItem("Grade",1);
			Listitem.setParent(type);
			
			Listitem = new ListItem("Employee Type",2);
			Listitem.setParent(type);
			
			Listitem = new ListItem("Designation",3);
			Listitem.setParent(type);
			
			curSalVal.setValue(String.valueOf(getCurrentSalary()));
			
			calcSal.setDisabled(true);
			
			return;
		}
		
		centerGrid.removeChild(rows);
		rows.setParent(null);
		rows = new Rows();
		centerGrid.appendChild(rows);
		int value =(Integer) item.getValue();
				
		typelist.clear();
		getTypes(value);
		
		Set set =  typelist.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			
			Map.Entry<String, HashMap<Integer,BigDecimal>> entry =(Entry<String, HashMap<Integer,BigDecimal>>) it.next();
			
			row = new Row();
			empTye = new Label( entry.getKey() );
			Map<Integer,BigDecimal> entry1 =(HashMap<Integer,BigDecimal>) entry.getValue(); 
			Set set1 =  entry1.entrySet();; 
			Iterator it1 = set1.iterator();
			Map.Entry<Integer,BigDecimal> countentry =(Entry<Integer,BigDecimal>)it1.next();
			
			headCount = new Label( countentry.getKey().toString() ); 
			hikeVal = new Textbox();
			hikeVal.setValue("0");
			row.appendChild(empTye);
			row.appendChild( headCount );
			row.appendChild(hikeVal);
			hikeVal.addEventListener(Events.ON_CHANGE, this);
			hikeVal.addEventListener(Events.ON_CHANGING, this);
			row.setParent(rows);
		}
		newSalVal.setValue("");
		diffAmtVal.setValue("");
		perYrVal.setValue("");
		
	}
	
	public void onEvent(Event event)
	{
		if(event.getName().equals(Events.ON_SELECT))
		{
			dynInit(type.getSelectedItem());
		}
		else if(event.getName().equals(Events.ON_CLICK))
		{
			calculateSalary();
		}
		else if(event.getName().equals(Events.ON_CHANGE) || event.getName().equals(Events.ON_CHANGING)){
			calcSal.setDisabled(false);
		}
	}
	
	private BigDecimal getCurrentSalary()
	{
		BigDecimal msal = Env.ZERO;
		BigDecimal staffSal = Env.ZERO;
		BigDecimal workerSal = Env.ZERO;
		String where = MHREmployee.COLUMNNAME_C_BPartner_ID+" IN "+" (SELECT C_BPartner_ID FROM C_BPartner WHERE isEmployee='Y' AND HR_Employee_Type_ID = "+EagleConstants.EMPLOYEE_TYPE_STAFF+" AND (CASE WHEN " + MBPartner.COLUMNNAME_relievingdate+" IS  NULL THEN 1=1 ELSE C_BPartner.relievingdate >= (select current_date) END))";
		List<MHREmployee> employees = new Query(Env.getCtx(),MHREmployee.Table_Name,where,null).setOnlyActiveRecords( Boolean.TRUE ).list();
		
		for(MHREmployee employee : employees)
		{
			staffSal  = staffSal.add(employee.getmonthly_salary());
		}
		
		where = MHREmployee.COLUMNNAME_C_BPartner_ID+" IN "+" (SELECT C_BPartner_ID FROM C_BPartner WHERE isEmployee='Y' AND HR_Employee_Type_ID = "+EagleConstants.EMPLOYEE_TYPE_WORKER+" AND (CASE WHEN " + MBPartner.COLUMNNAME_relievingdate+" IS  NULL THEN 1=1 ELSE C_BPartner.relievingdate >= (select current_date) END))";
		
		employees = new Query(Env.getCtx(),MHREmployee.Table_Name,where,null).setOnlyActiveRecords( Boolean.TRUE ).list();
		
		for(MHREmployee employee : employees)
		{
			msal = msal.add(employee.getdaily_salary().multiply(BigDecimal.valueOf(30)));
			workerSal = workerSal.add(msal);
			msal = Env.ZERO;
		}
		currentSal = staffSal.add(workerSal);
		
		currentSal = currentSal.setScale(4, BigDecimal.ROUND_HALF_UP );
		return currentSal;
	}
	
	private void getTypes(int seletionType)
	{
		switch (seletionType)
		{
			case 1:
				loadGradeList();			
				break;
			
			case 2:
				loadEmployeeTypeList();			
				break;
			
			case 3:
				loadDesignationList();			
				break;
			
		}
		return ;
	}
	
	private void loadGradeList()
	{
		gradeSal = Env.ZERO;
		PreparedStatement pstmt  = DB.prepareStatement("SELECT HR_Grade_ID,name FROM HR_Grade WHERE IsActive='Y'" , null);
		String empwhere = null;
		ResultSet rs =null;
	
		try {
			 rs = pstmt.executeQuery();
			 while(rs.next())
			 {
				 empwhere = MBPartner.COLUMNNAME_HR_Grade_ID+" = "+rs.getInt(1) +" AND "+MBPartner.COLUMNNAME_IsEmployee +" = 'Y' AND (CASE WHEN " + MBPartner.COLUMNNAME_relievingdate+" IS  NULL THEN 1=1 ELSE C_BPartner.relievingdate >= (select current_date) END)";
				 List<MBPartner> partners = new Query(Env.getCtx(),MBPartner.Table_Name,empwhere,null).setOnlyActiveRecords( Boolean.TRUE ).list();
				 BigDecimal sal = Env.ZERO;
				 int count =0;
				 for(MBPartner partner : partners){
					 
					 MHREmployee emp = new Query( Env.getCtx(),
													 MHREmployee.Table_Name,
													 MHREmployee.COLUMNNAME_C_BPartner_ID+" = "+partner.getC_BPartner_ID(),
													 null)
											 .setOnlyActiveRecords( Boolean.TRUE )
											 .first();
					 
					 BigDecimal empsal = Env.ZERO;
					 
					if(partner.getHR_Employee_Type_ID()==EagleConstants.EMPLOYEE_TYPE_WORKER){
						 
						empsal = emp.getdaily_salary().multiply(BigDecimal.valueOf(30)); 
						
					 } else {
						 
						 empsal = emp.getmonthly_salary();
					 }
					 sal = sal.add(empsal);
					 count++;
				 }
				 HashMap<Integer, BigDecimal> hm = new HashMap<Integer, BigDecimal>();
				 hm.put(count, sal);
				 gradeSal = gradeSal.add(sal);
				 typelist.put(rs.getString(2),hm );
			 }
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DB.close(rs, pstmt);
		}
		
	}
	
	private void loadEmployeeTypeList()
	{
		empTypeSal = Env.ZERO;
		PreparedStatement pstmt  = DB.prepareStatement("SELECT HR_Employee_Type_ID,name FROM HR_Employee_Type WHERE IsActive='Y'  " , null);
		String empwhere = null;
		ResultSet rs =null;
	
		try {
			 rs = pstmt.executeQuery();
			 while(rs.next())
			 {
				 empwhere = MBPartner.COLUMNNAME_HR_Employee_Type_ID+" = ?  AND "+MBPartner.COLUMNNAME_IsEmployee +" = 'Y' AND (CASE WHEN " + MBPartner.COLUMNNAME_relievingdate+" IS  NULL THEN 1=1 ELSE C_BPartner.relievingdate >= (select current_date) END) ";
				 List<MBPartner> partners = new Query( Env.getCtx(),
														 MBPartner.Table_Name,
														 empwhere,
														 null)
												 .setOnlyActiveRecords( Boolean.TRUE )
												 .setParameters( rs.getInt(1) )
												 .list();
				 BigDecimal sal = Env.ZERO;
				 int count =0;
				 for(MBPartner partner : partners){

					 MHREmployee emp = new Query( Env.getCtx(),
													 MHREmployee.Table_Name,
													 MHREmployee.COLUMNNAME_C_BPartner_ID + " = ? ",
															 null)
											 .setParameters( +partner.getC_BPartner_ID() )
											 .setOnlyActiveRecords( Boolean.TRUE )
											 .first();

					 BigDecimal empsal = Env.ZERO;

					 if( partner.getHR_Employee_Type_ID()==EagleConstants.EMPLOYEE_TYPE_WORKER ) {

						 empsal = emp.getdaily_salary().multiply(BigDecimal.valueOf(30));

					 } else {

						 empsal = emp.getmonthly_salary();
					 }

					 sal = sal.add(empsal);
					 count++;
				 }
				 HashMap<Integer, BigDecimal> hm = new HashMap<Integer, BigDecimal>();
				 hm.put( count, sal );
				 empTypeSal = empTypeSal.add( sal );
				 typelist.put( rs.getString(2), hm );
			 }
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DB.close(rs, pstmt);
		}
		
	}
	
	private void loadDesignationList()
	{
		designationSal = Env.ZERO;
		PreparedStatement pstmt  = DB.prepareStatement("SELECT HR_Designation_ID,name FROM HR_Designation WHERE IsActive='Y' " , null );
		String empwhere = null;
		ResultSet rs =null;
		try {
			 rs = pstmt.executeQuery();
			 while(rs.next())
			 {
				 empwhere = MBPartner.COLUMNNAME_HR_Designation_ID+" = ?  AND "+MBPartner.COLUMNNAME_IsEmployee +" = 'Y' AND (CASE WHEN " + MBPartner.COLUMNNAME_relievingdate+" IS  NULL THEN 1=1 ELSE C_BPartner.relievingdate >= (select current_date) END)";
				 List<MBPartner> partners = new Query( Env.getCtx(),
														 MBPartner.Table_Name,
														 empwhere,
														 null)
												 .setOnlyActiveRecords( Boolean.TRUE )
												 .setParameters( rs.getInt(1) )
												 .list();
				 BigDecimal sal = Env.ZERO;
				 int count =0;
				 for( MBPartner partner : partners ){
					 
					 MHREmployee emp = new Query( Env.getCtx(),
													 MHREmployee.Table_Name,
													 MHREmployee.COLUMNNAME_C_BPartner_ID + " = ? ",
													 null)
											 .setOnlyActiveRecords( Boolean.TRUE )
											 .setParameters( partner.getC_BPartner_ID() )
											 .first();
					 BigDecimal empsal = Env.ZERO;
					 
					 if(partner.getHR_Employee_Type_ID()==EagleConstants.EMPLOYEE_TYPE_WORKER){
						 
						empsal = emp.getdaily_salary().multiply(BigDecimal.valueOf(30));
						
					 } else {
						 
						 empsal = emp.getmonthly_salary();
					 }
					 sal = sal.add(empsal);
					 count++;
				 }
				 HashMap<Integer, BigDecimal> hm = new HashMap<Integer, BigDecimal>();
				 hm.put(count, sal);
				 designationSal = designationSal.add(sal);
				 typelist.put(rs.getString(2),hm );
			 }
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DB.close(rs, pstmt);
		}
		
	
	}
	
	private void calculateSalary()
	{
		BigDecimal newsal = Env.ZERO;
		List<Row> childrens = rows.getChildren();
		for(Row child : childrens)
		{
			Label label =((Label)child.getFirstChild());
			HashMap hm = typelist.get(label.getValue());
			Textbox box = (Textbox)child.getLastChild();
			
			Set set =  hm.entrySet();;
			Iterator it = set.iterator();
			while(it.hasNext()){
				
				Map.Entry<Integer,BigDecimal> entry =(Entry<Integer,BigDecimal>) it.next();
				String val = box.getValue();
				
				if( val.isEmpty() || val == null ){
					val = "0";
				}
				if(radioGp.getSelectedItem() == fixed){
					
					newsal=newsal.add(BigDecimal.valueOf(entry.getKey()).multiply(BigDecimal.valueOf(Integer.parseInt( val ))));
					newsal = newsal.add(entry.getValue());
					
				} else {
					
					BigDecimal actsal = entry.getValue();
					actsal = actsal.multiply(BigDecimal.valueOf(Integer.parseInt( val ))); 
					newsal=newsal.add(actsal.divide(BigDecimal.valueOf(100)));
					newsal = newsal.add(entry.getValue());
				}
			}
		}
		
		String selType = type.getSelectedItem().getValue().toString();
		newsal = newsal.setScale(4, BigDecimal.ROUND_HALF_UP );
		
		if(!selType.equalsIgnoreCase("0") && selType.equalsIgnoreCase("1")){
			
			newsal = (currentSal.subtract(gradeSal)).add(newsal);
			
		} else if(!selType.equalsIgnoreCase("0") && selType.equalsIgnoreCase("2")){
			
			newsal = (currentSal.subtract(empTypeSal)).add(newsal);
			
		} else if(!selType.equalsIgnoreCase("0") && selType.equalsIgnoreCase("3")){
			
			newsal = (currentSal.subtract(designationSal)).add(newsal);
			
		}
		
		newsal = newsal.setScale(4, BigDecimal.ROUND_HALF_UP );		
		newSalVal.setValue(newsal.toString());
		
		perYrVal.setValue(newsal.multiply(BigDecimal.valueOf(12)).toString());
		diffAmtVal.setValue(newsal.subtract(currentSal).toString()); 
	}	

	
}