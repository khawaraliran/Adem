/******************************************************************************
 * Copyright (C) 2008 Elaine Tan                                              *
 * Copyright (C) 2008 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.webui.dashboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.util.ServerPushTemplate;
import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Box;
import org.zkoss.zul.Vbox;

/**
 * Dashboard item: Workflow activities, notices and requests
 * @author Elaine
 * @date November 20, 2008
 * 
 * @ChangeComment:-
 * <li> If any Workflow'(s) is hide then we don't show <BR>
 * respective worklfow'(s) count on Workflow Activities button  
 * @author Giri 
 * @date	December 27 2011 
 * @bug 1633
 *
 * @Bug   @author     @ChangeID           @Description
 * *****************************************************************************************************************************
 * 1989   Arunkumar   [20120127:5:58PM]    Modified: getWorkflowCount() which will give the count of 
 *                                                   This Query will Return The Count of the Activities Waiting for approval, Belongs To Login User AND 
 *											         Activities which don't have user id , Role ID .
 * 1989    Arunkumar  [20120203:12:00]     Modified: updateUI()
 *                                                    removed code that is checks for any aprent is not visible of this commponent then it will not update the DPDashboard.
 *                                                    But on the Present Context We have to Update The DPActivities Dash Boards Even if that is not Visible 
 *                                                    So Removed code 
 * 1989    Arunkumar  [20120203:12:00]     Modified: updateUI()
 * 													   added Code That is Reverted  In the Above comment.
 * 													   As instant Workflow count refresh functionality is reverting...                                                   
 *                                                    
 */
public class DPActivities extends DashboardPanel implements EventListener {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: DPActivities.java 1042 2012-02-16 12:33:58Z arun $";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8123912981765687655L;

	private static final CLogger logger = CLogger.getCLogger(DPActivities.class);

	private Button btnNotice, btnRequest, btnWorkflow;

	private int noOfNotice;

	private int noOfRequest;

	private int noOfWorkflow;

	public DPActivities()
	{
		super();
        this.appendChild(createActivitiesPanel());
	}

	private Box createActivitiesPanel()
	{
		Vbox vbox = new Vbox();

        btnNotice = new Button();
        vbox.appendChild(btnNotice);
        btnNotice.setLabel(Msg.translate(Env.getCtx(), "AD_Note_ID") + " : 0");
        btnNotice.setTooltiptext(Msg.translate(Env.getCtx(), "AD_Note_ID"));
        btnNotice.setImage("/images/GetMail16.png");
        int AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'Notice' AND IsSummary = 'N'");
        btnNotice.setName(String.valueOf(AD_Menu_ID));
        btnNotice.addEventListener(Events.ON_CLICK, this);

        btnRequest = new Button();
        vbox.appendChild(btnRequest);
        btnRequest.setLabel(Msg.translate(Env.getCtx(), "R_Request_ID") + " : 0");
        btnRequest.setTooltiptext(Msg.translate(Env.getCtx(), "R_Request_ID"));
        btnRequest.setImage("/images/Request16.png");
        AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'Request' AND IsSummary = 'N'");
        btnRequest.setName(String.valueOf(AD_Menu_ID));
        btnRequest.addEventListener(Events.ON_CLICK, this);

        btnWorkflow = new Button();
        vbox.appendChild(btnWorkflow);
        btnWorkflow.setLabel(Msg.getMsg (Env.getCtx(), "WorkflowActivities") + " : 0");
        btnWorkflow.setTooltiptext(Msg.getMsg (Env.getCtx(), "WorkflowActivities"));
        btnWorkflow.setImage("/images/Assignment16.png");
        AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'Workflow Activities' AND IsSummary = 'N'");
        btnWorkflow.setName(String.valueOf(AD_Menu_ID));
        btnWorkflow.addEventListener(Events.ON_CLICK, this);

        return vbox;
	}

	/**
	 * Get notice count
	 * @return number of notice
	 */
	public static int getNoticeCount()
	{
		String sql = "SELECT COUNT(1) FROM AD_Note "
			+ "WHERE AD_Client_ID=? AND AD_User_ID IN (0,?)"
			+ " AND Processed='N'";

		int retValue = DB.getSQLValue(null, sql, Env.getAD_Client_ID(Env.getCtx()), Env.getAD_User_ID(Env.getCtx()));
		return retValue;
	}

	/**
	 * Get request count
	 * @return number of request
	 */
	public static int getRequestCount()
	{
		String sql = MRole.getDefault().addAccessSQL ("SELECT COUNT(1) FROM R_Request "
				+ "WHERE (SalesRep_ID=? OR AD_Role_ID=?) AND Processed='N'"
				+ " AND (DateNextAction IS NULL OR TRUNC(DateNextAction) <= TRUNC(SysDate))"
				+ " AND (R_Status_ID IS NULL OR R_Status_ID IN (SELECT R_Status_ID FROM R_Status WHERE IsClosed='N'))",
					"R_Request", false, true);	//	not qualified - RW
		int retValue = DB.getSQLValue(null, sql, Env.getAD_User_ID(Env.getCtx()), Env.getAD_Role_ID(Env.getCtx()));
		return retValue;
	}

	/**
	 * Get workflow activity count
	 * @return number of workflow activity
	 */
	public static int getWorkflowCount()
	{
		int count = 0;

		//Bug-1989 : [20120127:5:58PM]
		//This Query will Return The Count of the Activities Waiting for approval, Belongs To Login User AND 
		//Activities which don't have user id , Role ID .
		String  sql = "SELECT count(*) FROM AD_WF_Activity a "
					+ " WHERE a.Processed='N' AND a.WFState='OS' " 
					// Hide Time should be  
					+ " AND ( CASE WHEN a.hidetime IS NULL THEN 1=1 ELSE a.hideTime < current_date END ) "	
					+ " AND ("
					//	Owner of Activity
					+ " a.AD_User_ID= ?"	//	#1
		            // role of the user in the activity
					+ " OR ( a.ad_Role_ID IN ( SELECT ur.AD_Role_ID FROM AD_User_Roles ur WHERE ur.AD_User_ID= ? AND ur.isActive = 'Y' ))"
					// if the activity doesnot have the user and the role 
					+ " OR ( a.AD_User_ID IS NULL AND a.AD_Role_ID IS NULL ) " 	//	#3
					
					+ ") ";
		
		int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_User_ID);
			pstmt.setInt (2, AD_User_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ()) {
				count = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return count;
	}

	@Override
    public void refresh(ServerPushTemplate template)
	{
    	noOfNotice = getNoticeCount();
    	noOfRequest = getRequestCount();
    	noOfWorkflow = getWorkflowCount();

    	template.execute(this);
	}

    @Override
	public void updateUI() {

    	//don't update if not visible
    	Component c = this.getParent();
    	while (c != null) {
    		if (!c.isVisible())
    			return;
    		c = c.getParent();
    	}
    	
    	btnNotice.setLabel(Msg.translate(Env.getCtx(), "AD_Note_ID") + " : " + noOfNotice);
		btnRequest.setLabel(Msg.translate(Env.getCtx(), "R_Request_ID") + " : " + noOfRequest);
		btnWorkflow.setLabel(Msg.getMsg (Env.getCtx(), "WorkflowActivities") + " : " + noOfWorkflow);
	}

	public void onEvent(Event event)
    {
        Component comp = event.getTarget();
        String eventName = event.getName();

        if(eventName.equals(Events.ON_CLICK))
        {
            if(comp instanceof Button)
            {
            	Button btn = (Button) comp;

            	int menuId = 0;
            	try
            	{
            		menuId = Integer.valueOf(btn.getName());
            	}
            	catch (Exception e) {

				}

            	if(menuId > 0) SessionManager.getAppDesktop().onMenuSelected(menuId);
            }
        }
	}
}
