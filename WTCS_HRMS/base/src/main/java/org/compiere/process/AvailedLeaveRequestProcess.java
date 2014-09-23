package org.compiere.process;

import java.util.List;
import java.util.logging.Level;

import org.compiere.model.I_HR_Leave_Request;
import org.compiere.model.MLeaveRequest;
import org.compiere.model.Query;


/**
 * <P>
 * 
 * Process is used to availed the leave requests which <BR>
 * are approved and "FROM DATE" of the leave request is less <BR>
 * than the current date
 * 
 * </P>
 * 
 * @author Ranjit
 *
 */

public class AvailedLeaveRequestProcess extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: AvailedLeaveRequestProcess.java 1009 2012-02-09 09:16:13Z suman $";

	
	List<MLeaveRequest> leaveRequestList = null;
	
	@Override
	protected void prepare() {
		
		//
		// Get the leave request list to availe
		//
		
		leaveRequestList = getTobeAvailedLeaveRequests();
	}

	/**
	 * <P>
	 *  Get the leave requests which satisfy following condition <BR>
	 *  
	 *    <LI> 1 : Leave request start date should be less than current date </LI>
	 *    <LI> 2 : Leave request should be approved </LI>
	 *    <LI> 3 : Leave request should not be availed </LI>  
	 *    
	 * @return : MLeaveRequest list
	 */
	private List<MLeaveRequest> getTobeAvailedLeaveRequests() {

		StringBuffer whereClause = new StringBuffer( I_HR_Leave_Request.COLUMNNAME_FromDate + " < CURRENT_DATE ");
								   whereClause.append( " AND " + I_HR_Leave_Request.COLUMNNAME_IsApproved + " = 'Y' AND ");
								   whereClause.append(I_HR_Leave_Request.COLUMNNAME_isavailed + " = 'N' ");
								   
		List <MLeaveRequest> leaveRequestsList = new Query(getCtx(), I_HR_Leave_Request.Table_Name, whereClause.toString(), get_TrxName()).
												 setOnlyActiveRecords(Boolean.TRUE).
												 list();
		
		return leaveRequestsList;
	}

	@Override
	protected String doIt() throws Exception {
		
		if(null != leaveRequestList && leaveRequestList.isEmpty()) {
			
			return "";
		}
		
		int totalLeaveRequestCount = leaveRequestList.size();
		int leaveRequestCount = 0;
		
		for( MLeaveRequest leaveRequest : leaveRequestList ) {
			
			leaveRequest.setisavailed(Boolean.TRUE);				// Availed the request
			
			if( leaveRequest.save() ) {
				
				leaveRequest.processIt(DocAction.ACTION_Complete);	// Complete the leave request
				leaveRequest.saveEx(get_TrxName());
					
				leaveRequestCount++;
				
			}else {
				
				log.log ( Level.SEVERE, 
						  "Failed to availed the leave request - " 
						  + leaveRequest.get_ID() 
						  + " for " 
						  + leaveRequest.getC_BPartner_ID());
			}
		}
		
		String msg = leaveRequestCount + " leave requests are availed out of " + totalLeaveRequestCount;
		addLog(msg );
		log.log(Level.FINE,msg);
		
		return "";
	}
}
