/**
 * This Class IS Responsible For :
 * Distribution OF Amount To All The Employees Who Are Staying IN Same Quarter In a Particular Period
 * i.e,  We Have Power Consumption Window In That We are Giving Meter Reading For a Particular Quarter
 * IN Some Day.
 * And It Has Power Consumption tab &  Dish Tab.
 * When ever User Click on Power Consumption Window=> Distribute Amount Button 
 * We have to Get the Users (FROM HR_Accomodation_History table) That are Living In this QUarter 
 * in this period (meter reading date)
 * and Equally Distribute Amount .
 * User Can Change The Amount Distribution
 * 
 * @author ArunKumar   Bug-975
 * @Bug      @author     @ChangeID       @Discription
 * ****************************************************************************************************************************
 * 1639      Arunkumar                   Initial Check In 
 * 1639      Arunkumar    20111226]      We HAve To Use SysconfigurationPerameter To Calculate Attendance
 * 
 */
package org.adempiere.webui.apps.dailog;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;

import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListCell;
import org.adempiere.webui.component.ListHead;
import org.adempiere.webui.component.ListHeader;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.panel.ADButtonDailog;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MBPartner;
import org.compiere.model.MQuartEmpCharges;
import org.compiere.model.MSysConfig;
import org.compiere.model.Query;
import org.compiere.model.X_HR_Emp_Acomm_History;
import org.compiere.model.X_HR_Quarter;
import org.compiere.model.X_HR_Quarter_Power_Cons;
import org.compiere.process.ProcessInfo;
import org.compiere.util.Env;
import org.eevolution.model.MHRPeriod;
import org.eevolution.model.MHRProcess;
import org.wtc.util.WTCEmployeeUtil;
import org.wtc.util.WTCUtil;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;

import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;

import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

/**
 * @Bug   @author      @Change ID         @Description
 * ************************************************************************************************************
 *  1639   Arunkumar
 *  1639   Arunkumar  [20111227:8:00]     prepareEmployeeDetails method check for the list empty or not for accomdation history
 *  1639   Arunkumar  [20111230:8:00]     Getting The Salary And Number Of Present Days
 *  1639   Arunkumar  [20120102:9:30]     According Code Review Comments , Lable Name And Calss Name Is Changed According to Coding Guid Lines
 *  1903   Arunkumar  [20120109:5:00]     Modified : setPresentDays(int, MBPartner, WTCEmployeeUtil)  
 *                                        in a way that it will get the actual present days with out weekly offs.
 *                                        
 *  1903   Arunkumar  [20120109:6:00]     Modified :  current month salary functionality to get 2 decimal precision                                
 *   
 */
public class WPowerChargeDistribution extends ADButtonDailog {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: WPowerChargeDistribution.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	private static final long serialVersionUID = -693980434826L;

	/**
	 * 
	 */
	public WPowerChargeDistribution() {
		// TODO Auto-generated constructor stub
	}

	ProcessInfo pi=null;
	
	X_HR_Quarter_Power_Cons powerCons=null;
	/* (non-Javadoc)
	 * @see org.adempiere.webui.panel.ADButtonDailog#initButtonDailog()
	 */
	int recordID=0;
	@Override
	protected void initButtonDailog()
	{
		pi =getProcessInfo();
		if(pi != null)
		{
		  recordID=pi.getRecord_ID();
		}
		
		
		
		try 
		{
			// initialize the window 
			init();		
			// initialize the components
			initComponents();
		}
		catch(Exception ex)
		{
		    logger.log(Level.SEVERE,"Power Charge Distribution");
		}
		
	}
	

	
	// border layout
	Borderlayout mainlayout = new Borderlayout();
	
	North north = new North();
	Center center = new Center();
	South south = new South();
	
	
	// PowerBillAmount label
	Label 		powerBillAmount = new Label("Power Bill Amount :");
	
	Label 		dishBillAmount = new Label("Dish Amount :");
	
	Boolean hasCableConnection=null;
	int dishChargeAmount=0;
	
	//NumberBox  billvalue        = new NumberBox(true);
	Label 		powervalue 		= new Label("0");
	Label 		dishvalue 		= new Label("0");
	
	Listbox employees = new Listbox();
	
	Rows employeesRows = new Rows();
	ListHead employeesHead = new ListHead();
	ListHeader employeesName = new ListHeader("Employee Name");
	ListHeader employeeCode= new ListHeader("Employee Code");
	ListHeader presentDays= new ListHeader("Present Days");
	ListHeader netSalary= new ListHeader("Salary");
	ListHeader powerAmount= new ListHeader("Power Amount");
	ListHeader dishAmount= new ListHeader("Dish Amount");
	
    ListItem employeeDetails = null;
	
	ListCell bpName=null; 
	ListCell bpCode=null;
	ListCell bpPresentdays=null;
	ListCell bpNetSalary=null;
	ListCell bpPowerAmount=null;
	ListCell bpDishAmount=null;
	
	NumberBox powerValueDecimal=null;
	NumberBox dishValueDecimal=null;
	
	Button ok=null;
	Button cancel=null;
	
	// grid to add rows and components to the rows
	Grid		grid		= new Grid();
	Rows		rows		= new Rows();
	Row			row;
	Hbox		hbox;
	Div 		div;

	
	private void init() throws Exception{
		this.setMaximizable(false);
		this.setTitle("Power & Dish Amount Distribution");
		this.setWidth("1000px");
		this.setHeight("450px");
		this.setClosable(true);
		this.setSizable(false);
		this.setBorder("normal");
	}
	
	
	
	//
	// Initialize the Components
	//
	
	public void initComponents()
	{
		mainlayout.appendChild(north);
		mainlayout.appendChild(south);
		mainlayout.appendChild(center);
		
		row = new Row();
		row.setStyle("border-style:none none none; background-color:white;");
		row.setParent(rows);
		
		hbox = new Hbox();
		
		hbox.appendChild(powerBillAmount);
		powerBillAmount.setStyle("font-weight:bold;text-align:right;border-style:none none none none");
		
		div = new Div();
		div.setWidth("40px");
		hbox.appendChild(div);
		hbox.appendChild(powervalue);
		powervalue.setStyle("font-weight:bold;text-align:right;border-style:none none none none");
		
		div = new Div();
		div.setWidth("200px");
		hbox.appendChild(div);
		
		
		hbox.appendChild(dishBillAmount);
		dishBillAmount.setStyle("font-weight:bold;text-align:right;border-style:none none none none");
		
		div = new Div();
		div.setWidth("40px");
		
		hbox.appendChild(div);
		hbox.appendChild(dishvalue);
		dishvalue.setStyle("font-weight:bold;text-align:right;border-style:none none none none");
		
		
		hbox.setParent(row);
		row.appendChild(hbox);
		rows.setParent(grid);
		grid.setParent(north);
		grid.setHeight("50px");
		
		
		if(pi !=null)
		{
			if(recordID > 0)
			{
				powerCons=new X_HR_Quarter_Power_Cons(Env.getCtx(), recordID,null);
				if(powerCons != null)
				{
					
					//Power Bill Amount 
					BigDecimal amount=powerCons.getAmount();
					if(amount.compareTo(Env.ZERO) == 1)
					{
						powervalue.setText(amount.toString());
					}
					
					
					//Dish Bill Amount
					X_HR_Quarter quarterID=(X_HR_Quarter)powerCons.getHR_Quarter();
					
					if(quarterID != null)
					{
						hasCableConnection=quarterID.ishascableconnection();
						
						if(hasCableConnection != null)
						{
							if(hasCableConnection)
							{
								dishChargeAmount=MSysConfig.getIntValue("QUARTER_DISH_AMOUNT", 100);
								
								dishvalue.setText((new Integer(dishChargeAmount)).toString());
							}
							else
							{
								dishChargeAmount=0;
								dishvalue.setText((new Integer(0)).toString());
							}
						}
					}
				}
			}
		}
		
		employeesHead.setParent(employees);
		employeesHead.setSizable(true);
		
		employeesName.setParent(employeesHead);
		employeesName.setWidth("5%");
		
		employeeCode.setParent(employeesHead);
		employeeCode.setWidth("70px");
		
		presentDays.setParent(employeesHead);
		presentDays.setWidth("70px");
	
		
		netSalary.setParent(employeesHead);
		netSalary.setWidth("70px");
		
		powerAmount.setParent(employeesHead);
		powerAmount.setWidth("70px");
		
		
		dishAmount.setParent(employeesHead);
		dishAmount.setWidth("70px");
		
		employees.setHeight("1000px");
		
		employees.setParent(center);
		
		
		//Gives List OF Employees Belonging To This Quarter And In this Period
		prepareEmployeeDetails();
		
		 ok=new Button();
		 ok.setId("ok");
		 ok.setLabel("OK");
		 ok.setWidth("60px");
		 ok.addEventListener(Events.ON_CLICK, this);
      
		 
		 cancel=new Button();
	     cancel.setLabel("Cancel");
	     cancel.setWidth("60px");
	     cancel.setId("cancel");
	     cancel.addEventListener(Events.ON_CLICK, this);
	     
      Panel pnlButtonRight = new Panel();
      pnlButtonRight.appendChild(cancel);
     
      pnlButtonRight.setWidth("100%");

      Panel pnlButtonLeft = new Panel();
      pnlButtonLeft.appendChild(ok);
      pnlButtonLeft.setAlign("right");
      
      Hbox hboxButton = new Hbox();
      hboxButton.appendChild(pnlButtonLeft);
      hboxButton.appendChild(pnlButtonRight);
      hboxButton.setWidth("100%");
      hboxButton.setHeight("20%");
      hboxButton.setValign("Center");
      hboxButton.setStyle("background-color: white;border-style:none none none none"); 
	   
      hboxButton.setParent(south);
	
		north.setParent(mainlayout);
		center.setParent(mainlayout);
		south.setParent(mainlayout);
		mainlayout.setParent(this);
		
	}

	public void prepareEmployeeDetails()
	{
		if(powerCons != null)
		{
			Timestamp readingTime=powerCons.getreadingdate();
			int QuarterID=powerCons.getHR_Quarter_ID();
			String whereClause="('"+readingTime +"' Between trunc(" + X_HR_Emp_Acomm_History.COLUMNNAME_FromDate + 
			                   ") AND trunc(COALESCE(" +X_HR_Emp_Acomm_History.COLUMNNAME_ToDate +",'"+readingTime+"') ) " +
			                   ") AND "+X_HR_Emp_Acomm_History.COLUMNNAME_HR_Quarter_ID +"="+QuarterID +
			                   " AND "+X_HR_Emp_Acomm_History.COLUMNNAME_C_BPartner_ID +"" +
			                   		" IN (SELECT C_Bpartner_ID FROM C_Bpartner WHERE IsActive = 'Y' AND (CASE WHEN (relievingdate IS NULL ) THEN 1=1 ELSE (relievingdate >='"+readingTime+"') END))";
			List <X_HR_Emp_Acomm_History> histo=new Query(Env.getCtx(), X_HR_Emp_Acomm_History.Table_Name, whereClause, null).setOnlyActiveRecords(Boolean.TRUE).list();
			
			//[20111227:8:00]
			if(histo != null && ! histo.isEmpty())
			{
				//Distributing Charges Equally
				BigDecimal powerPartAmount=Env.ZERO;
				BigDecimal dishPartAmount=Env.ZERO;
				BigDecimal amount=powerCons.getAmount();
				int numberOFEntries=0;
				numberOFEntries=histo.size();
				if(( amount.compareTo(Env.ZERO) == 1 ) && numberOFEntries>0)
				{
					powerPartAmount=(amount).divide(new BigDecimal(numberOFEntries),2, BigDecimal.ROUND_HALF_UP);
				}
				
				if(dishChargeAmount >0 && numberOFEntries >0)
				{
					dishPartAmount=(new BigDecimal(dishChargeAmount)).divide(new BigDecimal(numberOFEntries), 2, BigDecimal.ROUND_HALF_UP);
				}
				
				
				
				//Get Each Employee Record And Create An Entry In the Window
				
				for(X_HR_Emp_Acomm_History  history: histo)
				{
					if(history != null)
					{
						MBPartner bpartner=(MBPartner)history.getC_BPartner();
						if(bpartner != null)
						{
							int bpartnerID=bpartner.getC_BPartner_ID();
							
							
							employeeDetails = new ListItem();
							
							//Employee Name
							bpName =new ListCell();
							bpName.appendChild(new Label(bpartner.getName()));
							bpName.setParent(employeeDetails);
							
							//Employee Code
							bpCode=new ListCell();
							bpCode.appendChild(new Label(bpartner.getemployee_code()));
							bpCode.setParent(employeeDetails);
							
							
							//Present Days
							bpPresentdays=new ListCell();
							bpPresentdays.setParent(employeeDetails);
							
							int periodID=0;
							if(readingTime != null)	{
								periodID=WTCEmployeeUtil.getPeriodId(bpartnerID, readingTime);
							}
							
							
							int mHRPeriodID = WTCEmployeeUtil.getPeriodId(bpartnerID, readingTime);
							String processType = MHRProcess.PAYROLLPROCESSTYPE_Mock;
							WTCEmployeeUtil    employeeUtil = new WTCEmployeeUtil(getCtx(), 
																				  bpartnerID, 
																				  mHRPeriodID, 
																				  processType, 
																				  null);
							
							//[20111230:8:00]
							//Set The Number Of Days Present
							setPresentDays(periodID , bpartner ,employeeUtil);
							
							//Net Salary
							bpNetSalary=new ListCell();
							bpNetSalary.setParent(employeeDetails);
							
						
							BigDecimal currMonthSal= Env.ZERO;
							if(employeeUtil != null) {
							    currMonthSal  =   employeeUtil.getCurrentMonthSalary(bpartnerID,readingTime);
							    //[20120109:6:00]
							    currMonthSal  =   currMonthSal.setScale(2,BigDecimal.ROUND_HALF_UP);
							}
							
							if(currMonthSal !=null)
							{
							   bpNetSalary.appendChild(new Label(currMonthSal.toString()));
							   bpNetSalary.setValue(currMonthSal);
							}
							else
							{
							   bpNetSalary.appendChild(new Label("0"));
							   bpNetSalary.setValue(new BigDecimal(0));
							}
							
							
							//Charge Amount
							bpPowerAmount=new ListCell();
							bpPowerAmount.setParent(employeeDetails);
							
							
							bpDishAmount=new ListCell();
							bpDishAmount.setParent(employeeDetails);
							
							//Query For Power Bill Charge
							String empChargeWhereClause=MQuartEmpCharges.COLUMNNAME_C_BPartner_ID+"=" +bpartnerID +" AND " + 
							                            MQuartEmpCharges.COLUMNNAME_HR_Quarter_ID+"=" +QuarterID + " AND " +
							                            MQuartEmpCharges.COLUMNNAME_HR_Quarter_Power_Cons_ID+"="+recordID +" AND "+
							                            MQuartEmpCharges.COLUMNNAME_HR_Period_ID + "=" + periodID +" AND "+ 
							                            MQuartEmpCharges.COLUMNNAME_chargetype +"='"+MQuartEmpCharges.CHARGETYPE_Power+"'";
							MQuartEmpCharges empCharges=new Query(Env.getCtx(), MQuartEmpCharges.Table_Name, empChargeWhereClause, null).first();
							
							//Query For Dish Bill Charge
							String empDishWhereClause=MQuartEmpCharges.COLUMNNAME_C_BPartner_ID+"=" +bpartnerID +" AND " + 
                                                        MQuartEmpCharges.COLUMNNAME_HR_Quarter_ID+"=" +QuarterID + " AND " +
                                                        MQuartEmpCharges.COLUMNNAME_HR_Quarter_Power_Cons_ID+"="+recordID +" AND "+
                                                        MQuartEmpCharges.COLUMNNAME_HR_Period_ID + "=" + periodID +" AND "+ 
                                                        MQuartEmpCharges.COLUMNNAME_chargetype +"='"+MQuartEmpCharges.CHARGETYPE_Dish+"'";
                            MQuartEmpCharges empDishCharges=new Query(Env.getCtx(), MQuartEmpCharges.Table_Name, empDishWhereClause, null).first();

							
							//Number Box initialization And Event Listener For this Number Box
							powerValueDecimal=new NumberBox(true);
							
							dishValueDecimal=new NumberBox(true);
							//chargeValue.addEventListener(Events.ON_BLUR, this);
							
							if(empCharges != null)
							{
								BigDecimal powerAmount=empCharges.getAmount();
								if(powerAmount != null)
								{
									bpName.setValue(empCharges);
									powerValueDecimal.setValue(powerAmount);
								}
								else
								{
									bpName.setValue(empCharges);
									powerValueDecimal.setValue(powerPartAmount);
								}
							}
							else
							{
								bpName.setValue(bpartner);
								powerValueDecimal.setValue(powerPartAmount);
							}
							
							
							
							if(empDishCharges != null)
							{
								BigDecimal dishAmount=empDishCharges.getAmount();
								if(dishAmount !=null)
								{
									bpDishAmount.setValue(empDishCharges);
									if(hasCableConnection)
									{
									   dishValueDecimal.setValue(dishAmount);
									}
									else
									{
									   dishValueDecimal.setValue(dishPartAmount);
									}
								}
								else
								{
									bpDishAmount.setValue(empDishCharges);
									dishValueDecimal.setValue(dishPartAmount);
								}
							}
							else
							{
								bpDishAmount.setValue(bpartner);
								dishValueDecimal.setValue(dishPartAmount);
							}
							
							if(!hasCableConnection)
							{
								dishValueDecimal.setEnabled(false);
							}
							else
							{
								dishValueDecimal.setEnabled(true);
							}
							
							bpPowerAmount.appendChild(powerValueDecimal);
							
							bpDishAmount.appendChild(dishValueDecimal);
							
							employees.appendChild(employeeDetails);
							
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param periodID
	 * @param bpartner
	 * @param employeeUtil 
	 */
	private void setPresentDays(int periodID, MBPartner bpartner, WTCEmployeeUtil employeeUtil) {
		
		if(periodID > 0) {
			MHRPeriod period =   MHRPeriod.get(getCtx(), periodID);
			int attendence = 0;
			if(period != null) {
			    double actualPrest = employeeUtil.getActualPresentedDays(Boolean.FALSE);

			    BigDecimal actualPresent = new BigDecimal(actualPrest);
			    if(actualPresent != null) {
					if(actualPresent.compareTo(Env.ZERO) == 1 ) {
						attendence = actualPresent.intValue();
						bpPresentdays.appendChild(new Label((new Integer(attendence)).toString()));
					}else {
						bpPresentdays.appendChild(new Label("0"));
					}
			    }
		    }else {
		    	logger.log(Level.SEVERE, "Period Not Found");
				bpPresentdays.appendChild(new Label("0"));
			}
		}else {
			logger.log(Level.SEVERE, "Period Not Found");
			bpPresentdays.appendChild(new Label("0"));
		}
		
	}



	public void onEvent(Event event)
	{
		if(event.getTarget().equals(ok))
		{
			List<ListItem> sd= employees.getItems();
			
			Boolean detach=true;
			Boolean isvalid=true;
			//Validate that Amount IS Dispersed Correctly
			BigDecimal dishTotalVal=Env.ZERO;
			BigDecimal powerTotalVal=Env.ZERO;
			
			for(int i=0;i<sd.size();i++)
			{
				ListItem listitem=(ListItem)sd.get(i);
				
				if(listitem != null)
				{
					ListCell dishCell=(ListCell)listitem.getLastChild();
					if(dishCell != null)
					{
						NumberBox currDishVal=(NumberBox)dishCell.getFirstChild();
						
						ListCell powerCell=(ListCell)dishCell.getPreviousSibling();
						
						NumberBox currPowerVal=(NumberBox)powerCell.getFirstChild();
						
						
						//This Method is Responsible For Validating Distributed Power & Dish 
						//Values With Employees Current Month salary
						isvalid=validatePowerAndDishAmountWithEmployeeSalary(dishCell);
						
						if(!isvalid)
						{
							detach=false;
							break;
						}
						
						if(currDishVal != null)
						{
							dishTotalVal=dishTotalVal.add(currDishVal.getValue());
						}
						
						if(currPowerVal != null)
						{
							powerTotalVal=powerTotalVal.add(currPowerVal.getValue());
						}
			        }
			    }
			}
			
			
			if(isvalid)
			{
					BigDecimal actual=powerCons.getAmount();
					
					if((actual.compareTo(powerTotalVal) < 0) || ( ((new BigDecimal(dishChargeAmount)).compareTo(dishTotalVal)) < 0))
					{
						FDialog.error(this.getWindowNo(), "POWER_CONSUPTION_AMOUNT_DISTRIBUTION");
						detach=false;
					}
					
					
					if(detach)
					{
						//Change The Values
						for(int i=0;i<sd.size();i++)
						{
							ListItem listitem=(ListItem)sd.get(i);
							
							
							//Creating Power Distribution amount Entry In to PowerConsution For Each Employee
							cretePowerBillAndDishBillDistributionEntry(listitem);
						}
					}
		    }
			
			if(detach){
				//In Dynamic Validation We Are using This For Make Quarter And Reading Date Widgets Read Only
				powerCons.setdistributecharge("Y");
				if(!powerCons.save()){
					logger.log(Level.SEVERE, "Distribute Charge Widget IS Not Updated With 'Y'");
				}

				this.detach();
			}
		}
		else if(event.getTarget().equals(cancel)) {
			this.detach();
		}
	}
	
	
	public void cretePowerBillAndDishBillDistributionEntry(ListItem listitem)
	{
		if(listitem != null)
		{
			//Dish amount Cell
			ListCell listcell=(ListCell)listitem.getLastChild();
			
			if(listcell != null)
			{
				NumberBox dishNewval=(NumberBox)listcell.getFirstChild();
				
				ListCell powerCell=(ListCell)listcell.getPreviousSibling();
				
				NumberBox powerNewVal=(NumberBox)powerCell.getFirstChild();
				
				
				//Power Bill Entries Creation
				//If Entry is already Exists Then It Will modify
				//Other Wise It will Create New Entry.
				
				if(powerNewVal != null)	{
					
					ListCell listcellname=(ListCell)listitem.getFirstChild();
					if(listcellname != null)
					{
						Object resul=(Object)listcellname.getValue();
						
						if(resul != null && resul instanceof  MQuartEmpCharges)
						{
						    MQuartEmpCharges charges=(MQuartEmpCharges)resul;
						    if(charges != null)
							{
								charges.setAmount(powerNewVal.getValue());
							}
						    if(!charges.save())
						    {
						    	logger.log(Level.SEVERE, "MQuartEmpCharges Entry IS Not Saved:");
						    }
						}
						else if(resul != null && resul instanceof  MBPartner )
						{
							MBPartner bpart=(MBPartner)resul;
							if(bpart != null)
							{
								MQuartEmpCharges charge=new MQuartEmpCharges(Env.getCtx(), 0, null);
								
								int bpartID=bpart.getC_BPartner_ID();
								charge.setC_BPartner_ID(bpartID);
								int periID=0;
								if(powerCons != null)
								{
								  charge.setHR_Quarter_ID(powerCons.getHR_Quarter_ID());
								  charge.setchargedate(powerCons.getreadingdate());
								  periID=WTCEmployeeUtil.getPeriodId(bpartID, powerCons.getreadingdate());
								  charge.setHR_Period_ID(periID);
								  
								  charge.setHR_Quarter_Power_Cons_ID(powerCons.getHR_Quarter_Power_Cons_ID());
								}
								charge.setAmount(powerNewVal.getValue());
								charge.setchargetype(MQuartEmpCharges.CHARGETYPE_Power);
								
								if(!charge.save())
							    {
							    	logger.log(Level.SEVERE, "MQuartEmpCharges Entry IS Not Saved:");
							    }
							}
						}
					}
				}
				
				if(dishNewval != null){
					
					Object resultObj=(Object) listcell.getValue();
					
					if(resultObj != null && resultObj instanceof  MQuartEmpCharges)
					{
					    MQuartEmpCharges charges=(MQuartEmpCharges)resultObj;
					    if(charges != null)
						{
							charges.setAmount(dishNewval.getValue());
						}
					    if(!charges.save())
					    {
					    	logger.log(Level.SEVERE, "MQuartEmpCharges Entry IS Not Saved:");
					    }
					}
					else if(resultObj != null && resultObj instanceof  MBPartner )
					{
						MBPartner bpart=(MBPartner)resultObj;
						if(bpart != null)
						{
							MQuartEmpCharges charge=new MQuartEmpCharges(Env.getCtx(), 0, null);
							
							int bpartID=bpart.getC_BPartner_ID();
							charge.setC_BPartner_ID(bpartID);
							int periID=0;
							if(powerCons != null)
							{
							  charge.setHR_Quarter_ID(powerCons.getHR_Quarter_ID());
							  charge.setchargedate(powerCons.getreadingdate());
							  periID=WTCUtil.getPeriodId(bpartID, powerCons.getreadingdate());
							  charge.setHR_Period_ID(periID);
							  
							  charge.setHR_Quarter_Power_Cons_ID(powerCons.getHR_Quarter_Power_Cons_ID());
							}
							charge.setAmount(dishNewval.getValue());
							charge.setchargetype(MQuartEmpCharges.CHARGETYPE_Dish);
							
							if(!charge.save())
						    {
						    	logger.log(Level.SEVERE, "MQuartEmpCharges Entry IS Not Saved:");
						    }
						}
					}
					
				}
			}
		}
	}
	
	
	
	
	public boolean validatePowerAndDishAmountWithEmployeeSalary(ListCell dishCell)
	{
		
		ListCell powerBill=(ListCell)dishCell.getPreviousSibling();
		
		ListCell netSal=(ListCell)powerBill.getPreviousSibling();
		
		
		BigDecimal curreMonSal=(BigDecimal)netSal.getValue();
		
		NumberBox dishVal=(NumberBox)dishCell.getFirstChild();
		NumberBox powerVal=(NumberBox)powerBill.getFirstChild();
		
		
		BigDecimal powerAmountDistributed=powerVal.getValue();
		BigDecimal dishAmountDistributed=dishVal.getValue();
	
		BigDecimal result=powerAmountDistributed.add(dishAmountDistributed);
		
		if(result.compareTo(curreMonSal) == 1)
		{
			FDialog.error(this.getWindowNo(), "POWER_DISTRIBUTION_AMOUN_NOT_MATCH_WITH_CURRENT_MONTH_SALARY");
			
			powerVal.setFocus(true);
			
			
			return false;
		}
		
		return true;
	}
	
	
	
	
}
