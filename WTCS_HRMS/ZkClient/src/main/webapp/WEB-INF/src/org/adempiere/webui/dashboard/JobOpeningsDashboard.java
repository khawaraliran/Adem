/**
 * 
 */
package org.adempiere.webui.dashboard;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.util.ServerPushTemplate;
import org.compiere.model.MSysConfig;
import org.compiere.model.MWTCJobOpenings;
import org.compiere.model.Query;
import org.compiere.model.X_WTC_JobOpenings;
import org.compiere.util.CLogger;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.wtc.util.WTCTimeUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Vbox;

/**
 * @author Arunkumar
 * @Date   21st Nov 2011
 * 
 * This Dash Board Is Used To Show The Active Job Openings Which Are Approved By Department Manager OR HR Manager 
 * 
 * @author Arunkumar
 * Bug-1625
 * Date Should Not Include TIme Like 00:00:00  So Modified To A Format day-MON-YYYY
 * 
 * 
 * @author Arunkumar
 * Bug- 1625
 * Date Should be in a Format Of Language That Is Selected When Log in.
 * 
 * 
 * As Per Requirement   We have TO Show The  Fields With out Cuts.
 * 
 * When THere is No Job Openings Just Show No Job Openings.
 * 
 * Bug -1625
 * 
 * We have to show job openings on the dash board if numberofopenpositions > 0 
 * 
 * 
 * Bug- 1625 
 * 
 * Change ID :  [20111205:03:00]
 * According Review Comment :
 * 
 * Given  Pagination To The Grid
 * 
 * Give  Sorting To The Columns
 * 
 * Bug-1625 [20111206:3:30PM]
 * Paging should use system configuration( DASHBOARD_PAGE_SIZE )
 * 
 * [20111206:3:45PM]  
 *  Getting The Current System Time.
 *  
 * [20111206:7:30PM]
 *  we Need To Remove Multiple Printing Of JOb Opening Not Available Message
 */
public class JobOpeningsDashboard extends DashboardPanel  implements EventListener{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: JobOpeningsDashboard.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	private static final long serialVersionUID = 11234141L;

	/**	Logger							*/
	protected transient CLogger	log = CLogger.getCLogger (getClass());
	/**
	 * 
	 */
	public JobOpeningsDashboard() {
		// TODO Auto-generated constructor stub
		super();
		createView();
	}
	
	
	Grid grid = new Grid();
	
	Vbox vbox = new Vbox();
	Row row = null;
	
	Columns columns = new Columns();
	Column careerLevelCol = null;
	Column jobcodeCol = null;
	Column positionsCol = null;
	Column effectiveFromDateCol = null;
	Column expirationDateCol = null;
	
	Label rowItem = null;
	Label careerLevel  = null;
	Label jobcode = null;
	Label positions = null;
	Label effectiveFromDate = null;
	Label expirationDate = null;
	
	Vbox msgbox = new Vbox();
	
	List<X_WTC_JobOpenings> jobOpenings = null;
	
	
	private void createView() {
		
		//Bug : 1625
		//[20111205:03:00]
		careerLevelCol = new org.zkoss.zul.Column(" Career Level",null,"20%");
		careerLevelCol.setSort("auto");
		careerLevelCol.setStyle("white-space:normal");
		columns.appendChild(careerLevelCol);

		jobcodeCol = new org.zkoss.zul.Column(" Job Code ",null,"18%");
		jobcodeCol.setSort("auto");
		jobcodeCol.setStyle("white-space:normal");
		columns.appendChild(jobcodeCol);
		
		positionsCol = new org.zkoss.zul.Column(" Open Positions ",null,"15%");
		positionsCol.setSort("auto");
		positionsCol.setStyle("white-space:normal");
		columns.appendChild(positionsCol);
		
		effectiveFromDateCol = new org.zkoss.zul.Column(" Effective From Date ",null,"22%");
		effectiveFromDateCol.setSort("auto");
		effectiveFromDateCol.setStyle("white-space:normal");
		columns.appendChild(effectiveFromDateCol);
		
		expirationDateCol = new org.zkoss.zul.Column(" Expiration Date ",null,"25%");
		expirationDateCol.setSort("auto");
		expirationDateCol.setStyle("white-space:normal");
		columns.appendChild(expirationDateCol);
		
		grid.appendChild(columns);
		columns.setSizable(true);
		vbox.appendChild(grid);
		grid.appendChild(createRows());
		grid.setMold("paging");
	
		//Bug-1625 [20111206:3:30PM]
		// Paging should use system configuration ( DASHBOARD_PAGE_SIZE )
		int pageSize = MSysConfig.getIntValue(EagleConstants.DASHBOARD_PAGE_SIZE, 5);
		grid.setPageSize(pageSize);
		
		this.appendChild(vbox);
		grid.renderAll();
	}

	public void onEvent(Event event) throws Exception {
		String  jobOpenig = event.getTarget().getId();
		int jobOpenigId = (Integer.parseInt(jobOpenig));
		AEnv.zoom(MWTCJobOpenings.Table_ID, jobOpenigId);
	}
	
	/**
	 * This Method Is Responsible For Getting The List Of Job Openings Those Are Approved By Manager And Active.
	 * @return
	 */
	private List<X_WTC_JobOpenings> getJobOpeningsList() {
		//[20111206:3:45PM]  Getting The Current System Time. 
		Timestamp cureentTime = WTCTimeUtil.getSystemCurrentTimestamp();
		
		String where = X_WTC_JobOpenings.COLUMNNAME_Processed+" = "+" 'Y' AND "+
		               X_WTC_JobOpenings.COLUMNNAME_IsActive+" = 'Y' AND "+
		               X_WTC_JobOpenings.COLUMNNAME_DocStatus+" = 'CO' AND " +
		               X_WTC_JobOpenings.COLUMNNAME_numberofopenpositions+" > "+Env.ZERO+" AND " +
		               X_WTC_JobOpenings.COLUMNNAME_WFState+"= '"+EagleConstants.PUBLISHED+"' AND "+
		               X_WTC_JobOpenings.COLUMNNAME_effective_fromdate +"<='"+ cureentTime +"' AND "+
		               X_WTC_JobOpenings.COLUMNNAME_expiration_date +">='"+cureentTime+"'"  ;
		Query query = new Query(Env.getCtx(), X_WTC_JobOpenings.Table_Name, where, null);	
		jobOpenings =  query.list();
		return jobOpenings;
	}

	
	
	/**
	 * This Method Will Refresh  The Screen In The Seconds Given In Server Push
	 */
	public void refresh(ServerPushTemplate template) {
		List<X_WTC_JobOpenings> existing = jobOpenings;
		getJobOpeningsList();
		if(existing != jobOpenings){
			template.execute(this);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void updateUI(){
		Component c = this.getParent();
			if (!c.isVisible())
				return;
			while (c != null) {
			c = c.getParent();
		}
		updateView(); 
		
	}
	
	
	private Component createRows(){
		
		Rows rows = new Rows();
		
		if(rows.getChildren() != null){
			List<Component> childs = rows.getChildren();
			for(Component cmp : childs){
				rows.removeChild(cmp);
			}
		}

		jobOpenings = (ArrayList<X_WTC_JobOpenings>) getJobOpeningsList();
		
		if(jobOpenings.size() == 0)
		{
			vbox.removeChild(msgbox);
			grid.setVisible(false);
			msgbox.setVisible(true);
			//[20111206:7:30PM]
			//we Need To Remove Multiple Printing Of JOb Opening Not Available Message
			if(msgbox != null && msgbox.getChildren().size() > 0) {
				msgbox.removeChild(rowItem);
			}
			String msg = Msg.getMsg(Env.getCtx(), EagleConstants.NO_JOB_OPENINGS);
			rowItem = new Label(msg);
			rowItem.setParent(msgbox);
			msgbox.setParent(vbox);
		}
		else{
					
			grid.setVisible(true);
			msgbox.setVisible(false);
			
			for(X_WTC_JobOpenings jobOpening : jobOpenings){
				
				row = new Row();
				row.addEventListener(Events.ON_CLICK, this);
				row.setParent(rows);
				
				careerLevel  = new Label(jobOpening.getWTC_CareerLevel().getName());
				jobcode = new Label(jobOpening.getjobcode());
				positions = new Label(new Integer(jobOpening.getnumberofopenpositions()).toString());
				
				//Bug-1625
				// Date Should Not Include TIme Like 00:00:00  So Modified To A Format day-MON-YYYY
				Timestamp fromTime = (jobOpening.geteffective_fromdate());
				Timestamp toTime   = jobOpening.getexpiration_date();
				
				//Bug-1625
				//Date Should be in a Format Of Language That Is Selected When Log in.
				Language lan=Env.getLanguage(Env.getCtx());
				
				SimpleDateFormat da=lan.getDateFormat();  
				if(da == null) {
					String dateFormat = Msg.getMsg(Env.getCtx(), EagleConstants.DATE_FORMAT);
					da = new SimpleDateFormat(dateFormat);
				}
				effectiveFromDate = new Label((da.format(fromTime)).toString());
				expirationDate = new Label((da.format(toTime)).toString());
				
				row.appendChild(careerLevel);
				row.appendChild(jobcode);
				row.appendChild(positions);
				row.appendChild(effectiveFromDate);
				row.appendChild(expirationDate);
				
				row.setId(String.valueOf(jobOpening.getWTC_JobOpenings_ID()));
			}
		}
		return rows;
	}
	
	private void updateView(){
		grid.getRows().setParent(null);
		grid.appendChild(createRows());
	}

}
