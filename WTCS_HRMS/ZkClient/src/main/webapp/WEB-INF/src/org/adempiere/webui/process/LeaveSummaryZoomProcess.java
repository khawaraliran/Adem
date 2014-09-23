package org.adempiere.webui.process;



import org.adempiere.webui.apps.AEnv;
import org.compiere.model.I_HR_Leave_Request;
import org.compiere.model.MLeaveRequest;
import org.compiere.model.MQuery;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;


public class LeaveSummaryZoomProcess extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LeaveSummaryZoomProcess.java 1009 2012-02-09 09:16:13Z suman $";

	
	
	private int recordId = 0;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] params = getParameter();
		
		for(ProcessInfoParameter param : params){
//			
//			if(param.getParameterName().equalsIgnoreCase("manualleavescredit")){
//				
//				noOfLeaves = GeneralUtil.getBigDecimalValue(param.getParameter());
//			}
//			if(param.getParameterName().equalsIgnoreCase("creditLeaveReason")){
//				
//				leaveCreditReason = (String)param.getParameter();
//				
//			}
		}
		
		recordId = getRecord_ID();

	}

	@Override
	protected String doIt() throws Exception {
		
		
		if( recordId < 0) {
			
			return "";
		}
		
		
		MLeaveRequest leaveRequest = getLeaveRequest();
		
		if( null != leaveRequest ) {
			
			MQuery zoomQuery = new MQuery();
			zoomQuery.addRestriction(" c_bpartner_id = " + leaveRequest.getC_BPartner_ID());
			
			AEnv.zoom(1000049,zoomQuery);
			
			
		}
		
		return "";
	}

	private MLeaveRequest getLeaveRequest() {
		
		StringBuffer whereClause = new StringBuffer( I_HR_Leave_Request.COLUMNNAME_HR_Leave_Request_ID + " = ?");
		
		MLeaveRequest leaveRequest = new Query( getCtx(), I_HR_Leave_Request.Table_Name, whereClause.toString(), get_TrxName()).
									setParameters(recordId).
									setOnlyActiveRecords(Boolean.TRUE).
									first();
		
		return leaveRequest;
	}

}
