package org.adempiere.webui.process;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.I_WTC_Maintenance;
import org.compiere.model.I_WTC_PlantInspectionSchedule;
import org.compiere.model.MMailText;
import org.compiere.model.MUser;
import org.compiere.model.MWTCMaintenance;
import org.compiere.model.X_WTC_PlantInspectionSchedule;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.wtc.util.EMailUtil;
import org.wtc.util.WTCUtil;

public class ReminderProcess extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: ReminderProcess.java 1009 2012-02-09 09:16:13Z suman $";

	public ReminderProcess() {
		// TODO Auto-generated constructor stub
	}

	
	protected void prepare() {
		

	}

	
	protected String doIt() throws Exception {
		
		MMailText mailTemplate = null;

		// TODO Auto-generated method stub
		Calendar currentDate =Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		for(int i=1;i<=2;i++)
		{
			if(currentDate.getMaximum(Calendar.DAY_OF_YEAR)==currentDate.get(Calendar.DAY_OF_YEAR))
			{
				currentDate.roll(Calendar.YEAR, true);
			}
			currentDate.roll(Calendar.DAY_OF_YEAR, 1);
		}
		
		
		String maintainanceDateQ = "select * from WTC_maintenance where cast(starttime as date)= ?";		
		
		String maintainanceCompleteQ = "select * from WTC_maintenance where maintenancestatus NOT LIKE 'CO'";
		
		String inspectionDateQ ="select * from WTC_plantinspection_schedule where cast(fromtime as date)= ?";
		

		
		PreparedStatement maintainanceDatePS=DB.prepareStatement(maintainanceDateQ, get_TrxName());
		maintainanceDatePS.setDate(1, new Date(currentDate.getTimeInMillis()));
		ResultSet maintainanceDateRS = maintainanceDatePS.executeQuery();
		while(maintainanceDateRS.next())
		{
			MWTCMaintenance mm = new MWTCMaintenance(getCtx(),maintainanceDateRS.getInt("WTC_maintenance_id"),get_TrxName());
			mailTemplate = WTCUtil.getMailTemplate( getAD_Client_ID() , EagleConstants.MAINTENANCE_MAILTEMPLATE_MAINTENANCE_SCHDL, getCtx(), get_TrxName() );
			sendMaintananceMails(mailTemplate, null, I_WTC_Maintenance.Table_ID, mm.get_ID(), get_TrxName());
		}
		DB.close(maintainanceDateRS, maintainanceDatePS);
		
		
		PreparedStatement inspectionDatePS=DB.prepareStatement(inspectionDateQ, get_TrxName());
		inspectionDatePS.setDate(1, new Date(currentDate.getTimeInMillis()));
		ResultSet inspectionDateRS = inspectionDatePS.executeQuery();
		while(inspectionDateRS.next())
		{
			X_WTC_PlantInspectionSchedule mpis = new X_WTC_PlantInspectionSchedule(getCtx(),inspectionDateRS.getInt("WTC_plantinspection_schedule_id "),get_TrxName());
			mailTemplate = WTCUtil.getMailTemplate( getAD_Client_ID() , EagleConstants.MAINTENANCE_MAILTEMPLATE_INSPECTION_SCHDL, getCtx(), get_TrxName() );
			sendMaintananceMails(mailTemplate, null, I_WTC_PlantInspectionSchedule.Table_ID, mpis.get_ID(), get_TrxName());
		}
		DB.close(inspectionDateRS, inspectionDatePS);
		
		
		PreparedStatement maintainanceCompletePS=DB.prepareStatement(maintainanceCompleteQ, get_TrxName());
		ResultSet maintainanceCompleteRS = maintainanceCompletePS.executeQuery();
		Calendar new_Current_Date = Calendar.getInstance();
		Timestamp maintenance_End_Time,new_Current_Time;
		while(maintainanceCompleteRS.next())
		{
			new_Current_Time = new Timestamp(new_Current_Date.getTimeInMillis());
			maintenance_End_Time = maintainanceCompleteRS.getTimestamp("endtime");
			if(maintenance_End_Time.before(new_Current_Time))
			{
				MWTCMaintenance mic = new MWTCMaintenance(getCtx(),maintainanceCompleteRS.getInt("WTC_maintenance_id"),get_TrxName());
				mailTemplate = WTCUtil.getMailTemplate( getAD_Client_ID() , EagleConstants.MAINTENANCE_MAILTEMPLATE_MAINTENANCE_SCHDL_DEV, getCtx(), get_TrxName() );
				sendMaintananceMails(mailTemplate, null,I_WTC_Maintenance.Table_ID,mic.get_ID(),get_TrxName());
			}
					
		}
		DB.close(maintainanceCompleteRS, maintainanceCompletePS);
		
		
		return " Reminders has been Sent Successfully";
	
	}
	
	private void sendMaintananceMails( MMailText mailTemplate,	MUser toUser,  int tableID, int recordId, String trxName ) {
		
		
		  ArrayList<Object> toList = new ArrayList<Object>();
		  toList.add(toUser);
		
		  MUser fromUser = null;
		  
		  try {
			  
			mailTemplate.setUser(toUser);
			
			boolean mailEnqueued = EMailUtil.enqueueEmail( fromUser,
					   									   toList, 
					   									   null, 
					   									   null, 
					   									   mailTemplate.getMailHeader(), 
					   									   mailTemplate.getMailText(),
					   									   tableID, 
					   									   recordId, 
					   									   null, 
					   									   trxName );
			
			if(mailEnqueued) {
				
				log.log(Level.FINE,"Mail enqueued successfully for Maintance");
			}else {
				
				log.log(Level.SEVERE,"Failed to enqueue mail for Maintance");
			}
		} catch (Exception ex) {
			
			log.log( Level.SEVERE, 
					"Failed to enqueue the mail for Maintance -" 
					+ this.getRecord_ID() + ex.getMessage());
			
		}
	}

}
