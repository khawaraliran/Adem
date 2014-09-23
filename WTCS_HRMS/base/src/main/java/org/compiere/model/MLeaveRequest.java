package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.eevolution.model.I_HR_Department;
import org.eevolution.model.X_HR_Department;
import org.wtc.util.EMailUtil;
import org.wtc.util.LeaveRequestManager;
import org.wtc.util.WTCUtil;


/**
 * 
 * @author Ranjit
 * 
 * Modifications
 * 
 * Task No 			Date 			Identifier				Author				Change
 * ***********************************************************************************************************
 * 
 * 1631				13/12/2011     13122011944_1			Ranjit				1. Checking null condition for the other reasons
 * 								   13122011944_2								2. Changed from reason id to dispproval reason id
 * 								   13122011944_3								3. Skipping the creating date entry for holiday when leave type
 * 																				   consideration is "None"
 * 								   13122011944_4								4. SQL changes in getOtherLeaves method											 
 * 
 * 1631			22/12/2011		   221220110906_1			Ranjit				Message configured in system instread of hard coding
 *								
 *				22/12/2011		   221220110906_2			Ranjit				If leave type is loss of pay then we wont check for the balance leaves 								
 *								   
 *			    22/12/2011		   221220110906_3			Ranjit				Removed the code for checking the leave balance while approving as, this
 *																				is handled while saving the leave request itself
 * 1869			4/1/2012			040120121028			Ranjit				Leave combination is checked whenever a record is new / leave type modified/
 * 																				from date / to date changed
 * 																				 
 *
 *						   
 */


public class MLeaveRequest extends X_HR_Leave_Request implements DocAction {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: MLeaveRequest.java 1009 2012-02-09 09:16:13Z suman $";

	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		if(!newRecord && getWFState().equalsIgnoreCase("Approved")) {
			
			if( is_ValueChanged(COLUMNNAME_FromDate) || 
				is_ValueChanged(COLUMNNAME_ToDate) || 
				is_ValueChanged(COLUMNNAME_HR_LeaveType_ID) || 
				is_ValueChanged(COLUMNNAME_number_of_workingdays) ||
				is_ValueChanged(COLUMNNAME_WTC_Reasons_ID)) {
				
				// [ Change identification] -  221220110906_1
				
				String msg = Msg.getMsg(getCtx(), EagleConstants.LEAVEREQUEST_MODIFICATION_MSG);
				log.log(Level.WARNING,msg);
				log.saveError("",msg);
			
				return Boolean.FALSE;
			}
		}
		
		
		if( newRecord || is_ValueChanged(COLUMNNAME_number_of_workingdays)) {
			
			//
			// If leave type is loss of pay we dont check for the balance leaves 
			// [Change identification] - 221220110906_2 
			// 
			
			if(getHR_LeaveType_ID()  != EagleConstants.LOSS_OF_PAY_LEAVE_TYPE) {
			
					int balanceLeaves = checkForBalanceLeavesForEmployee(this);
			
					if( balanceLeaves < 0) {
				
							String msg = Msg.getMsg(getCtx(), EagleConstants.BALANCED_LEAVES_ERROR_MSG);
							log.log(Level.WARNING,msg);
							log.saveError("",msg);
				
							return Boolean.FALSE;
					}
			}
		}
		
		
		if( newRecord || is_ValueChanged(COLUMNNAME_FromDate) || 
			is_ValueChanged(COLUMNNAME_ToDate) || 
			is_ValueChanged(COLUMNNAME_HR_LeaveType_ID) || 
			is_ValueChanged(COLUMNNAME_number_of_workingdays) ) {
				
				//
				// Check whether request leave type allows for applied no of days leaves as continues
				//
		
				boolean allowed = LeaveRequestManager.checkForContinuousRequestOfSameLeaveLeaveType(this,get_TrxName());
		
				if(Boolean.FALSE == allowed ) {
					
					String msg = Msg.getMsg( Env.getCtx(), 
							EagleConstants.MAX_CONTINUOUS_LEAVES_FORLEAVETYPE_EXCEED_MSG, 
							new Object[] { this.getHR_LeaveType().getleavetype()});
					
					log.log(Level.WARNING,msg);
					log.saveError("",msg);
					
					return Boolean.FALSE;
				}
		
		}
		
		// 040120121028
		
		if( newRecord || is_ValueChanged(COLUMNNAME_FromDate) || 
				is_ValueChanged(COLUMNNAME_ToDate) || 
				is_ValueChanged(COLUMNNAME_HR_LeaveType_ID) || 
				is_ValueChanged(COLUMNNAME_number_of_workingdays) ) {

			//
			// Check for leave combination i.e. leave allowed with other leave
			// type
			//

			String notAllowedLeaveType = LeaveRequestManager.checkLeaveCombinationAllowed(this, get_TrxName());

			if (null != notAllowedLeaveType) {

				String msg = Msg.getMsg( Env.getCtx(),EagleConstants.COMBINED_LEAVES_NOTPERMITTED,
										 new Object[] { this.getHR_LeaveType().getleavetype(),notAllowedLeaveType });

				log.log(Level.WARNING, msg);
				log.saveError("", msg);

				return Boolean.FALSE;
			}
		}
	
		return Boolean.TRUE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**	Process Message 			*/
	private String		m_processMsg = null;

	public MLeaveRequest(Properties ctx, int HR_Leave_Request_ID, String trxName) {
		super(ctx, HR_Leave_Request_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLeaveRequest(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public boolean processIt(String action) throws Exception {
		
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (action, getDocAction());
	}

	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	public String prepareIt() {
		

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE );
		
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE );
		
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		return DocAction.STATUS_InProgress;
	}

	public boolean approveIt() {

		// [Change identification] - 221220110906_3
		
		setIsApproved(Boolean.TRUE);
		setWFState(EagleConstants.LEAVEREQUEST_APPROVED_STATUS);

		return Boolean.TRUE;
	}

	public boolean rejectIt() {
				
		setWFState(EagleConstants.LEAVEREQUEST_DISAPPROVED_STATUS);
		setIsApproved(false);
		setisavailed(false);
		set_ValueOfColumn("Processed", Boolean.TRUE);
		
		return true;
		
	}

	public String completeIt() {
		
		//
		// Update the employee leave assign record
		//
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
			
		if(isApproved()) {
			
			if ( updateEmployeeLeaveAssignRecord(this) ) {
			
				setWFState("Availed");
				this.setisavailed(Boolean.TRUE);
				set_ValueOfColumn("Processed", Boolean.TRUE);
			
			return DocAction.STATUS_Completed;
			
			}
		}else {
			
			return DocAction.STATUS_Invalid;
		}
		
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		return DocAction.STATUS_Completed;
	}

	public boolean voidIt() {
		
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_VOID );
		
		if (m_processMsg != null)
			return Boolean.FALSE;
		
		setWFState("Cancelled");
		setIsApproved(false);
		setisavailed(false);
		set_ValueOfColumn("Processed", Boolean.TRUE);
		
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID );
		
		if (m_processMsg != null)
			return Boolean.FALSE;
		
		return true;
	}

	public boolean closeIt() {
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_CLOSE );
		
		if (m_processMsg != null)
			return Boolean.FALSE;
		
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_CLOSE );
		
		if (m_processMsg != null)
			return Boolean.FALSE;
		
		
		return true;
	}

	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean reActivateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDocumentNo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProcessMsg() {
		
		return m_processMsg;
	}

	public int getDoc_User_ID() {
		
		MBPartner mbPartner = new MBPartner(getCtx(), this.getC_BPartner_ID(), get_TrxName());
		return mbPartner.getPrimaryAD_User_ID();
	}

	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return Env.ONE;
	}
	
	
	public void addUsedLeaves(){
		
		
		int leaveTypeId = this.getHR_LeaveType_ID();
		int leaveRequestId = this.getHR_Leave_Request_ID();
		BigDecimal noOfWorkingDaysForLeaveRequest = this.getnumber_of_workingdays();
		
		
		//
		// Get the Employee Leave Assign record
		//
		
		 
		
		
		
		
            
              
	}
	/**
	 * 
	 * @param mLeaveRequest
	 * @return
	 */
	private boolean updateEmployeeLeaveAssignRecord( MLeaveRequest mLeaveRequest ) {
		
		int businessPartnerId = this.getC_BPartner_ID();
		int leaveTypeId = this.getHR_LeaveType_ID();
		
		BigDecimal noOfLeaveRequest = mLeaveRequest.getnumber_of_workingdays();
		
		StringBuffer whereClause = new StringBuffer( I_HR_Leave_Assign.COLUMNNAME_C_BPartner_ID + " = ?");
								   whereClause.append(" AND ");
								   whereClause.append( I_HR_Leave_Assign.COLUMNNAME_HR_LeaveType_ID + " = ? ");
		
		MLeaveAssign employeeLeaveAssign = new Query(getCtx(), I_HR_Leave_Assign.Table_Name, whereClause.toString(), get_TrxName()).
											setParameters(businessPartnerId,leaveTypeId).
											setOnlyActiveRecords(Boolean.TRUE).
											first();
		
		if (null != employeeLeaveAssign ) {
			
			
			BigDecimal usedLeaves = employeeLeaveAssign.getused_leaves();
			BigDecimal balancedLeaves = employeeLeaveAssign.getbalance_leaves();
			BigDecimal totalLeaves = employeeLeaveAssign.gettotal_leaves();
			
			
			employeeLeaveAssign.setused_leaves(usedLeaves.add(noOfLeaveRequest));
			employeeLeaveAssign.setbalance_leaves(balancedLeaves.subtract(noOfLeaveRequest));
			employeeLeaveAssign.settotal_leaves(totalLeaves.subtract(noOfLeaveRequest));
			
			if (!employeeLeaveAssign.save() ) {
				
				log.log( Level.SEVERE, 
						"Failed to update the leave assign record for business partner - " 
						+ businessPartnerId 
						+ " of leave type id " 
						+ leaveTypeId);	
				
				return Boolean.FALSE;
				
			}
		}
		
		return Boolean.TRUE;
	}

	public boolean afterSave(boolean newRecord, boolean success)
	{
		
		//
		// If "Other Reason" option selected at the reason field then create an entry
		// in the WTC_Reasons table with this reason such that, it will appear as new reason
		//
		
		
		if( newRecord || ( success && is_ValueChanged(COLUMNNAME_WTC_Reasons_ID)) ) {
			
			if( this.getWTC_Reasons_ID() == EagleConstants.OTHERS_REASON_ID) {
				
				String otherReason = this.getotherreason();
				
				if(null == otherReason) { // 13122011944_1
					
					log.log(Level.SEVERE,EagleConstants.MANDATORY_LEAVEREQUEST_REASON);
					return Boolean.FALSE;
					
				}else {
					
					MWTCReasons.createWTCReasonEntry( otherReason, X_WTC_Reasons.REASONTYPE_LeaveRequest,get_TrxName());
				}
				
			}
		}
		
		if( !newRecord || ( success && is_ValueChanged(COLUMNNAME_requestdisapprovalreason_ID)) ) {
			
			// 13122011944_2
			if( this.getrequestdisapprovalreason_ID() == EagleConstants.OTHERS_REASON_ID) {
				
				String otherReason = this.getotherdisapprovalreason();
				
				if(null == otherReason) { // 13122011944_1
					
					log.log(Level.SEVERE,EagleConstants.MANDATORY_LEAVEREQUEST_REASON);
					return Boolean.FALSE;
					
				}else {
					
					MWTCReasons.createWTCReasonEntry( otherReason, X_WTC_Reasons.REASONTYPE_LeaveRequestDisapproval,get_TrxName());
				}
				
			}
		}
		
		
		//
		// Send the mail for leave request for following cases
		//
		// Case 1: When employee applies for leave i.e. when status is - "Waiting for HOD" to the HOD of employee
		// Case 2: When the leave request gets approved i.e. Status is - "Approved" to the Employee
		// Case 3: When the leave request gets disapproved i.e. status is - "DisApproved" to the Employee
		//
		
		
		if( is_ValueChanged(COLUMNNAME_WFState)) {
			
			
			MMailText mailTemplate = null;
			
			if( this.getWFState().equalsIgnoreCase("Request Sent to HOD")) {
				
				// Case 1
				
				mailTemplate = WTCUtil.getMailTemplate( getAD_Client_ID() , EagleConstants.LEAVEREQUEST_MAILTEMPLATE_FORHOD, getCtx(), get_TrxName() );
				int hodId = getHeadOfDepartmentId(this);
				MUser toUser = MBPartner.getUserForBusinessPartner(hodId,get_TrxName());
				
				if(null != toUser)
					
				sendLeaveRequestMails(mailTemplate,toUser,get_TrxName());
				
				
			}else if(this.getWFState().equalsIgnoreCase("Approved")) {
				
				// Case 2
				mailTemplate = WTCUtil.getMailTemplate( getAD_Client_ID() , EagleConstants.LEAVEREQUEST_MAILTEMPLATE_FOR_APPROVAL, getCtx(), get_TrxName() );
				MUser toUser = MBPartner.getUserForBusinessPartner(this.getC_BPartner_ID(),get_TrxName());
				
				if(null != toUser)
					
				sendLeaveRequestMails(mailTemplate,toUser,get_TrxName());
				
			}else if(this.getWFState().equalsIgnoreCase("DisApproved")) {
				
				// Case 3
				
				mailTemplate = WTCUtil.getMailTemplate( getAD_Client_ID() , EagleConstants.LEAVEREQUEST_MAILTEMPLATE_FOR_DISAPPROVAL, getCtx(), get_TrxName() );
				MUser toUser = MBPartner.getUserForBusinessPartner(this.getC_BPartner_ID(),get_TrxName());
				
				if(null != toUser)
				sendLeaveRequestMails(mailTemplate,toUser,get_TrxName());
				
			}
		}
		
		
		//
		// If the number of balanced leaves became -ve then should not allow to create the leave request
		//
		
		
		/*if(success && is_ValueChanged(COLUMNNAME_number_of_workingdays)) {
			
			int balanceLeaves = checkForBalanceLeavesForEmployee(this);
			
			if( balanceLeaves < 0) {
			
				String msg = Msg.getMsg(getCtx(), EagleConstants.BALANCED_LEAVES_ERROR_MSG);
				
				log.saveError("Error", msg);
				
				return Boolean.FALSE;
			}
		}*/
		
		
		
		
		//
		// when new record is going to save create entries in leave request lines for each date in the leave request, or if 
		// columns leave type of the leave request or fromdate or todate of the leave request changes delete the request lines 
		// existing for the leave request and create new ones.
		//
		
		
		if( newRecord || 
			is_ValueChanged(COLUMNNAME_HR_LeaveType_ID) || 
			is_ValueChanged(COLUMNNAME_FromDate) || 
			is_ValueChanged(COLUMNNAME_ToDate)) {
			
			int no  = deleteOldRequestLines(this,get_TrxName());
			
			if(no < 0) {
				
				log.saveError("", "Failed to remove the old leave request lines");
				return Boolean.FALSE;
			}
			
			success = createLeaveRequestLines(getCtx(),this,get_TrxName());
			
			if(success) {
				
				log.log(Level.FINE,"Leave request lines are created successfully - " + get_ID());
			}else {
				
				log.log(Level.FINE,"Failed to create leave request lines for leave request - " + get_ID());
				log.saveError("", "Failed to create leave request lines");
				return Boolean.FALSE;
			}
			
			
		}
		
		//----------------.
		
		if(success != true)
			return success;
		MLeaveRequest [] leaves = getOtherLeaves();
		
		for(int i=0; i<leaves.length; i++)	{
			
//			boolean inBetween = TimeUtil.inRange(this.getFromDate(), this.getToDate(), leaves[i].getFromDate(), leaves[i].getToDate());
			boolean inBetween = LeaveRequestManager.isDatesOverlapping(leaves[i].getFromDate(), leaves[i].getToDate(),this.getFromDate(), this.getToDate());
			if(inBetween == true)
				throw new AdempiereException("There is Overlapping Leaves for the same Date entered here. So can not Save this Leave");
		}
		return success;
	}

	

	private boolean createLeaveRequestLines(Properties ctx,	MLeaveRequest mLeaveRequest, String get_TrxName) {
		
		
		String considerationType = mLeaveRequest.getHR_LeaveType().getadjacentholidayasleave();
		
		MLeaveRequestLine line = null;
		Timestamp fromdate = getFromDate();
		Timestamp todate = getToDate();
		int employeeId  = mLeaveRequest.getC_BPartner_ID();
		
		String leaveRequestDates = Env.getContext(this.getCtx(), "LeaveRequestDates_"+ employeeId);
		Timestamp originalStartDate = null;
		Timestamp originalEndDate  = null;
		
		if(null != leaveRequestDates && !leaveRequestDates.isEmpty()) {
			
			 originalStartDate = getOriginalDate(leaveRequestDates,true);
			 originalEndDate   = getOriginalDate(leaveRequestDates,false);
		}
		

		while(!fromdate.after(todate)){
			
			line = new MLeaveRequestLine(getCtx(),0, get_TrxName());

			line.setHR_Leave_Request_ID(getHR_Leave_Request_ID());
			
			

			if(considerationType.equalsIgnoreCase(X_HR_LeaveType.ADJACENTHOLIDAYASLEAVE_Adjacent)) {
				
				if(null != originalStartDate && null != originalEndDate) {
					if( fromdate.equals(originalStartDate) || 
						(fromdate.equals(originalStartDate) || fromdate.after(originalStartDate) || fromdate.equals(originalEndDate)) 
						&& !fromdate.after(originalEndDate)) {
					
						if(LeaveRequestManager.isNonWorkingDay(fromdate)) {
						
							fromdate = TimeUtil.getNextDay(fromdate);
							continue;
						}else {
						
							line.setleavedate(fromdate);
						}
					}else {
						line.setleavedate(fromdate);
					}
				}else {
					
					line.setleavedate(fromdate);
				}
				
			}else if(considerationType.equalsIgnoreCase(X_HR_LeaveType.ADJACENTHOLIDAYASLEAVE_None)) {
				
				if(null != originalStartDate && null != originalEndDate) {
					
					// 13122011944_3
					if( fromdate.equals(originalStartDate) || 
							(fromdate.equals(originalStartDate) || fromdate.after(originalStartDate) || fromdate.equals(originalEndDate)) 
							&& !fromdate.after(originalEndDate)) {
					
						if(LeaveRequestManager.isNonWorkingDay(fromdate)) {
						
							fromdate = TimeUtil.getNextDay(fromdate);
							continue;
						}else {
						
							line.setleavedate(fromdate);
						}
					}
				}else {
					
					line.setleavedate(fromdate);
				}
			}else {
				
				line.setleavedate(fromdate);
			}
			
			line.setleavesize(MLeaveRequestLine.LEAVESIZE_FullDay);
			
			if(!line.save()){
				
				log.saveError("", "Unable to create Leave Request Line's for the Leave Request of employee"+getHR_Employee().getName()+" on "+System.currentTimeMillis());
				return false;
			}
			
			log.fine("Leave Request Line created for Leave Request : LeaveRequestLineId as"+line.getHR_Leave_RequestLine_ID());
			fromdate = TimeUtil.getNextDay(fromdate);
		}
		
		Env.setContext(ctx, "LeaveRequestDates_"+ employeeId , "" );
		return true;
	}

	private Timestamp getOriginalDate(String leaveRequestDates, boolean startDate) {
		
		//
		// {LeaveRequestDates_9000014_startDate=2011-12-17 00:00:00.0, LeaveRequestDates_9000014_endDate=2011-12-19 00:00:00.0}
		//
		
		String dateValues = leaveRequestDates.substring(leaveRequestDates.indexOf("{") + 1, 
							leaveRequestDates.lastIndexOf("}"));
		
		
		Timestamp timeStamp = null;

		StringTokenizer st = new StringTokenizer(dateValues, ",", false);

		while (st.hasMoreTokens()) {

			String token = st.nextToken().trim();
			int index = token.indexOf("=");

			String key = token.substring(0, index);
			
			if( startDate && key.endsWith("_startDate")) {
				
				String value = token.substring(index + 1);
				timeStamp = getTimestampForString(value);
				
				return timeStamp;
			}else if(startDate == false && key.endsWith("_endDate")){
				
				String value = token.substring(index + 1);
				timeStamp = getTimestampForString(value);
				
				return timeStamp;
			}
		}
		
		return null;
	}
	
	private Timestamp getTimestampForString(String value) {
		
		try {
			
			SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date dt = datetimeFormatter1.parse(value);
			return new Timestamp(dt.getTime());
			
		} catch (ParseException e) {
			
			log.log(Level.SEVERE,"Failed to parse the string - " + value + "to timestamp" + e.getMessage());
			return null;
			
		}

	}

	private int deleteOldRequestLines(MLeaveRequest mLeaveRequest,
			String get_TrxName) {
		
		int nodel = 0;

		try {
			
			String sqldel = "DELETE FROM HR_Leave_RequestLine WHERE HR_Leave_Request_ID = ?";
			
			PreparedStatement pstmtdel = DB.prepareStatement( sqldel,get_TrxName());
			pstmtdel.setInt(1, mLeaveRequest.get_ID());
			
			 nodel = pstmtdel.executeUpdate();
			
			log.config("Leave request lines are deleted =" + nodel);
			pstmtdel.close();
			
		} catch (SQLException e) {
			
			log.log(Level.SEVERE,"Failed to delete the leave request lines for the request - " + mLeaveRequest.get_ID()  + e.getMessage());
			nodel = -1;
			
		}
		
		return nodel;
	}

	private int checkForBalanceLeavesForEmployee(MLeaveRequest mLeaveRequest) {
		
		BigDecimal balancedLeaves = Env.ZERO;
		
		int leaveTypeId = mLeaveRequest.getHR_LeaveType_ID();
		int businessPartnerId = mLeaveRequest.getC_BPartner_ID();
		
		StringBuffer whereClause = new StringBuffer( I_HR_Leave_Assign.COLUMNNAME_HR_LeaveType_ID + " = ?  AND " );
									whereClause.append( I_HR_Leave_Assign.COLUMNNAME_C_BPartner_ID + " = ? ");
		
		MLeaveAssign leaveAssign = new Query(Env.getCtx(), I_HR_Leave_Assign.Table_Name, whereClause.toString(), get_TrxName()).
									setParameters(leaveTypeId,businessPartnerId).
									setOnlyActiveRecords(Boolean.TRUE).
									first();
		
		if(null != leaveAssign) {
		
				balancedLeaves = leaveAssign.getbalance_leaves();
		
				BigDecimal numberOfLeavesForRequest = mLeaveRequest.getnumber_of_workingdays();
		
				balancedLeaves = balancedLeaves.subtract(numberOfLeavesForRequest);
		
		}
		
		return balancedLeaves.signum();
	}

	private int getHeadOfDepartmentId(MLeaveRequest mLeaveRequest) {

		int employeeDepartmentId = mLeaveRequest.getC_BPartner().getHR_Department_ID();
		
		StringBuffer whereClause = new StringBuffer( I_HR_Department.COLUMNNAME_HR_Department_ID + " = ?");
		
		X_HR_Department department = new Query( getCtx(), I_HR_Department.Table_Name, whereClause.toString(), get_TrxName()).
								   setParameters( employeeDepartmentId ).
								   setOnlyActiveRecords(Boolean.TRUE).
								   first();
	
		int hodId = department.getC_BPartner_ID();
		
		return hodId;
	}


	private void sendLeaveRequestMails( MMailText mailTemplate,	MUser toUser, String getTrxName) {
		
		
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
					   									   X_HR_Leave_Request.Table_ID, 
					   									   this.get_ID(), 
					   									   null, 
					   									   getTrxName);
			
			if(mailEnqueued) {
				
				log.log(Level.FINE,"Mail enqueued successfully for leave request");
			}else {
				
				log.log(Level.SEVERE,"Failed to enqueue mail for leave request");
			}
		} catch (Exception ex) {
			
			log.log( Level.SEVERE, 
					"Failed to enqueue the mail for leave request -" 
					+ this.get_ID() + ex.getMessage());
			
		}
		
		
		
		
	}

	private MLeaveRequest[] getOtherLeaves() {
		
		//
		// bug: here leave request for the same employee should be retrived
		//
		
		// 13122011944_4
		
		String whereClause = "HR_Leave_Request_ID <> ? AND C_BPartner_ID = ? AND wfstate NOT IN (?,?)";
		List <MLeaveRequest> list = new Query(getCtx(), MLeaveRequest.Table_Name, whereClause, get_TrxName())
			.setParameters(this.get_ID(),this.getC_BPartner_ID(),EagleConstants.LEAVEREQUEST_STATUS_CANCEL,EagleConstants.LEAVEREQUEST_DISAPPROVED_STATUS)
			.setOnlyActiveRecords(Boolean.TRUE)
			.setOrderBy("number_of_workingdays")
			.list();

		MLeaveRequest[] retValue = new MLeaveRequest[list.size()];
		list.toArray(retValue);
		return retValue;
	}


}
