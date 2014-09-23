package org.wtc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.compiere.model.GridTab;
import org.compiere.model.I_WTC_LeaveType_Combination;
import org.compiere.model.MLeaveRequest;
import org.compiere.model.MLeaveType;
import org.compiere.model.MNonBusinessDay;
import org.compiere.model.MWTCLeaveTypeCombination;
import org.compiere.model.Query;
import org.compiere.model.X_HR_Leave_Request;
import org.compiere.model.X_WTC_LeaveType_Combination;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;


/**
 * <P>
 * 	Contains all the rules for leave requests & validation <BR> 
 *  against the leave request <BR>
 *  
 *  Task No			Date			Identification			Author 				Change
 *  
 *  1631			22/12/2011		201112220323 			Ranjit				By default requested leave type is included in leave combination
 *  1631			22/12/2011		201112220324			Ranjit				New method for date overlapping is added
 *  1631 			24/12/2011      201112241058			Ranjit				Changed the method to get next day from role to add
 *  
 * @author Ranjit
 *
 */

public class LeaveRequestManager {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LeaveRequestManager.java 1009 2012-02-09 09:16:13Z suman $";
	
	public static final BigDecimal NEG = new BigDecimal(-1);
	
	
	
	/**
	 * <P>
	 *  Checks for the non working day. <BR>
	 * <LI> Check from the weekend from Compnay work days window</LI>
	 * <LI> Check from the non business day i.e. Holidays window</LI>
	 * 
	 * @param date	: Timestamp
	 * @return		: TRUE if non working day else FALSE
	 */
	public static boolean isNonWorkingDay(Timestamp date) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());

		int weekNoInMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		StringBuffer sql = new StringBuffer("");
					sql.append(" SELECT count(*) FROM wtc_companyworkday WHERE weekday = ?  AND ");

		switch (weekNoInMonth) {

		case 1:

			sql.append(" firstweek = 'N'");
			break;

		case 2:

			sql.append(" secondweek = 'N'");
			break;

		case 3:

			sql.append(" thirdweek = 'N' ");
			break;

		case 4:

			sql.append(" fourthweek = 'N'");
			break;

		case 5:

			sql.append(" fifthweek = 'N'");
			break;

		}

		int count = DB.getSQLValue( null, 
									sql.toString(), 
									new Object[] { Integer.toString(dayOfWeek) });

		if (count > 0) {

			return Boolean.TRUE;
		} else {

			if ( MNonBusinessDay.isCompanyHoliday(date)) {
				
				return Boolean.TRUE;
			}else {
				
				return Boolean.FALSE;
			}
		}
	}


	/**
	 * Checks for the continuous leave request i.e. leave type combination 
	 * at the leave type tab.
	 * 
	 * If leave type combination not allowed returns false else true
	 * 
	 * @param mLeaveRequest
	 * @param get_TrxName
	 * @return
	 */
	public static boolean checkForContinuousRequestOfSameLeaveLeaveType(
			MLeaveRequest leaveRequest, String trxName) {
		
		
		boolean previousRequest = Boolean.FALSE;
		boolean futureRequest = Boolean.FALSE;

		MLeaveType leaveType = (MLeaveType) leaveRequest.getHR_LeaveType();
		
		
		
		if(leaveRequest.getnumber_of_workingdays().compareTo(leaveType.getmaxcontinousleaves()) > 0){
			
			return Boolean.FALSE;
		}
		
		//
		// check for previous leave requests
		//
		

		List<MLeaveRequest> previousLeaveRequestForEmployee = getImmediatePreviousLeaveRequest(
																								leaveRequest.getFromDate(), 
																								leaveRequest.getC_BPartner_ID(),
																								leaveRequest.getHR_LeaveType_ID(), 
																								trxName);
		
		if(null != previousLeaveRequestForEmployee && !previousLeaveRequestForEmployee.isEmpty()) {
			
			BigDecimal oldRequestedLeaveDays = Env.ZERO;
			
			for(MLeaveRequest mLeaveRequest : previousLeaveRequestForEmployee ) {
				
				oldRequestedLeaveDays = oldRequestedLeaveDays.add(mLeaveRequest.getnumber_of_workingdays());
			}
			
			BigDecimal maxContinuousLeavesAllowed = leaveType.getmaxcontinousleaves();
			
			if(oldRequestedLeaveDays.add(leaveRequest.getnumber_of_workingdays()).compareTo(maxContinuousLeavesAllowed) > 0) {
				
				previousRequest =  Boolean.FALSE;
			}else {
				
				previousRequest =  Boolean.TRUE;
			}
			
		}else {
			
			previousRequest =  Boolean.TRUE;
		}
		
		
		
		//
		// check for future leave requests
		//
		
		List<MLeaveRequest> futureLeaveRequestForEmployee = getImmediateFutureLeaveRequest(
																							leaveRequest.getToDate(), 
																							leaveRequest.getC_BPartner_ID(),
																							leaveRequest.getHR_LeaveType_ID(),
																							trxName);

		if (null != futureLeaveRequestForEmployee
				&& !futureLeaveRequestForEmployee.isEmpty()) {

			BigDecimal oldRequestedLeaveDays = Env.ZERO;

			for (MLeaveRequest mLeaveRequest : futureLeaveRequestForEmployee) {

				oldRequestedLeaveDays.add(mLeaveRequest.getnumber_of_workingdays());
			}

			BigDecimal maxContinuousLeavesAllowed = leaveType.getmaxcontinousleaves();

			if (oldRequestedLeaveDays.add(leaveRequest.getnumber_of_workingdays()).compareTo(maxContinuousLeavesAllowed) > 0) {

				futureRequest = Boolean.FALSE;
			} else {

				futureRequest = Boolean.TRUE;
			}

		} else {

			futureRequest = Boolean.TRUE;
		}

		
		if( previousRequest == false || futureRequest == false ){
			
			return Boolean.FALSE;
		}else {
			return Boolean.TRUE;
		}
		
		
		
	}

	/**
	 * <P>
	 *  Gets the immediate future i.e. prefix leave <BR> 
	 *  request for the end date i.e. to date of the leave request
	 * 
	 * @param todate		: To date
	 * @param employeeId	: C_BPartner_ID
	 * @param leaveTypeId	: HR_LeaveType_ID
	 * @param trxName
	 * @return				: List of the leave request
	 */
	private static List<MLeaveRequest> getImmediateFutureLeaveRequest(
			Timestamp todate, int employeeId, int leaveTypeId,
			String trxName) {
		
		List<MLeaveRequest> leaveRequestList = new ArrayList<MLeaveRequest>(); 
		
		Timestamp nextDate = WTCTimeUtil.getImmediateNextTimestamp(todate);
		
		if(null != nextDate) {
			
			StringBuffer whereClause = new StringBuffer(" ? >= " + X_HR_Leave_Request.COLUMNNAME_FromDate);
									  whereClause.append("  AND ? <= " +  X_HR_Leave_Request.COLUMNNAME_ToDate);
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_C_BPartner_ID + " = ?");
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_HR_LeaveType_ID + " = ?");
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_DocStatus + " <> ?");
									  
			leaveRequestList = new Query( Env.getCtx(), X_HR_Leave_Request.Table_Name, whereClause.toString(), trxName).
							   setParameters(nextDate,nextDate,employeeId,leaveTypeId,X_HR_Leave_Request.DOCSTATUS_Voided).
							   setOnlyActiveRecords(Boolean.TRUE).
							   list();
		}
		
		return leaveRequestList;
	}


	/**
	 * <P>
	 * 	Gets the immediate previous leave request i.e.<BR>
	 *  leaves for the start date of the leave request
	 * </P>
	 * @param fromDate			: Start date
	 * @param employeeId		: C_BPartner_ID
	 * @param leaveTypeId		:HR_LeaveType_ID
	 * @param trxName		
	 * @return					: List of leave request
	 */
	private static List<MLeaveRequest> getImmediatePreviousLeaveRequest( Timestamp fromDate, 
																		 int employeeId, 
																		 int leaveTypeId, 
																		 String trxName) {
		
		List<MLeaveRequest> leaveRequestList = new ArrayList<MLeaveRequest>(); 
		
		Timestamp previousDate = WTCTimeUtil.getPreviousDay(fromDate);
		
		if(null != previousDate) {
			
			StringBuffer whereClause = new StringBuffer(" ? >= " + X_HR_Leave_Request.COLUMNNAME_FromDate);
									  whereClause.append("  AND ? <= " +  X_HR_Leave_Request.COLUMNNAME_ToDate);
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_C_BPartner_ID + " = ?");
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_HR_LeaveType_ID + " = ?");
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_WFState + " NOT IN ( ?, ?)");
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_DocStatus + " <> ? ");
									  
			leaveRequestList = new Query( Env.getCtx(), X_HR_Leave_Request.Table_Name, whereClause.toString(), trxName).
							   setParameters(previousDate,previousDate,employeeId,leaveTypeId,EagleConstants.LEAVEREQUEST_DISAPPROVED_STATUS,
									   EagleConstants.LEAVEREQUEST_STATUS_CANCEL,X_HR_Leave_Request.DOCSTATUS_Voided).
							   setOnlyActiveRecords(Boolean.TRUE).
							   list();
		}
		
		return leaveRequestList;
	}
	
	/**
	 * Gets the immediate previous leave request for the provided date
	 *  
	 * @param fromDate			: From date
	 * @param employeeId		: C_BPartner_ID
	 * @param trxName			: 
	 * @return					: List of leave request
	 */
	private static List<MLeaveRequest> getImmediatePreviousLeaveRequest( Timestamp fromDate, 
																		 int employeeId, 
																		 String trxName) {

			List<MLeaveRequest> leaveRequestList = new ArrayList<MLeaveRequest>(); 

			Timestamp previousDate = WTCTimeUtil.getPreviousDay(fromDate);

			if(null != previousDate) {

				StringBuffer whereClause = new StringBuffer(" ? >= " + X_HR_Leave_Request.COLUMNNAME_FromDate);
				whereClause.append("  AND ? <= " +  X_HR_Leave_Request.COLUMNNAME_ToDate);
				whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_C_BPartner_ID + " = ?");
				whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_DocStatus + " <> ?");
				whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_WFState + " NOT IN ( ?, ?)");

				leaveRequestList = new Query( Env.getCtx(), X_HR_Leave_Request.Table_Name, whereClause.toString(), trxName).
				setParameters(previousDate,previousDate,employeeId,X_HR_Leave_Request.DOCSTATUS_Voided,
						EagleConstants.LEAVEREQUEST_DISAPPROVED_STATUS,
						EagleConstants.LEAVEREQUEST_STATUS_CANCEL).
				setOnlyActiveRecords(Boolean.TRUE).
				list();
			}

			return leaveRequestList;
	}


	/**
	 * <P>
	 * 	Checks for the current leave request is allowed <BR>
	 *  with the combination of leaves i.e. immeditate future & previous <BR>
	 *  leave requests, if not allows then returns string else null 
	 * </P>
	 * 
	 * @param mLeaveRequest	: MLeaveRequest
	 * @param trxName		
	 * @return				: String
	 */
	public static String checkLeaveCombinationAllowed( MLeaveRequest mLeaveRequest, String trxName ) {
		
		
		
		List<Integer> leaveTypeCombinationList = new ArrayList<Integer>();
		
		leaveTypeCombinationList = getLeaveTypeCombination(mLeaveRequest.getHR_LeaveType_ID(),trxName);
		
		
		if(null != leaveTypeCombinationList && !leaveTypeCombinationList.isEmpty()) {
				
			List<MLeaveRequest> previousLeaveRequestForEmployee = getImmediatePreviousLeaveRequest(
																									mLeaveRequest.getFromDate(), 
																									mLeaveRequest.getC_BPartner_ID(),
																									trxName);
			
			
			if( null != previousLeaveRequestForEmployee && !previousLeaveRequestForEmployee.isEmpty()) {
				
				for(MLeaveRequest previousLeaveRequest : previousLeaveRequestForEmployee) {
					
					if(!leaveTypeCombinationList.contains(previousLeaveRequest.getHR_LeaveType_ID())) {
						
						return previousLeaveRequest.getHR_LeaveType().getleavetype();
					}
				}
			}
				
			
			// check for the future leave requests
			
			
			List<MLeaveRequest> futureLeaveRequestForEmployee = getImmediateFutureLeaveRequest(
																								 mLeaveRequest.getToDate(), 
																								 mLeaveRequest.getC_BPartner_ID(),
																								 trxName);
			

			if( null != futureLeaveRequestForEmployee && !futureLeaveRequestForEmployee.isEmpty()) {
				
				for(MLeaveRequest futureLeaveRequest : futureLeaveRequestForEmployee) {
					
					if(!leaveTypeCombinationList.contains(futureLeaveRequest.getHR_LeaveType_ID())) {
						
						return futureLeaveRequest.getHR_LeaveType().getleavetype();
					}
				}
			}
			
		}else if(null != leaveTypeCombinationList && leaveTypeCombinationList.isEmpty()){
			
			return null;
			
		}
		
		
		
		return null;
	}


	/**
	 * <P>
	 * Get all the leave type combinations for the provided <BR> 
	 * leave type id <BR>
	 * </P>
	 * 
	 * @param leaveTypeId	: HR_LeaveType-ID
	 * @param trxName
	 * @return				: List of the id for the allowed leve types
	 */
	private static List<Integer> getLeaveTypeCombination(int leaveTypeId,String trxName) {
	
		List<Integer> listOfCombinationLeaveTypes = new ArrayList<Integer>();
		
		StringBuffer whereClause  = new StringBuffer(X_WTC_LeaveType_Combination.COLUMNNAME_HR_LeaveType_ID + " = ?");
		
		List<MWTCLeaveTypeCombination> leaveTypeCombinationList = new Query( Env.getCtx(), 
																  I_WTC_LeaveType_Combination.Table_Name, 
																  whereClause.toString(), trxName).
																  setParameters(leaveTypeId).
																  setOnlyActiveRecords(Boolean.TRUE).
																  list();
		
		if(null != leaveTypeCombinationList && !leaveTypeCombinationList.isEmpty()) {
			
				for(MWTCLeaveTypeCombination combination : leaveTypeCombinationList) {
					
					listOfCombinationLeaveTypes.add(combination.getallowedLeaveType_ID());
				}
				
				// 201112220323
				listOfCombinationLeaveTypes.add(leaveTypeId);
		}
		
		return listOfCombinationLeaveTypes;
	}
	
	/**
	 * <P>
	 * Get the immeditate future leave request for the employee <BR>
	 * </P>
	 * 
	 * @param toDate		: To Date
	 * @param employeeId	: C_BPartner_ID
	 * @param trxName
	 * @return				: List of the leave requests
	 */
	private static List<MLeaveRequest> getImmediateFutureLeaveRequest(Timestamp toDate, int employeeId, String trxName) {
		
		List<MLeaveRequest> leaveRequestList = new ArrayList<MLeaveRequest>(); 
		
		Timestamp nextDate = WTCTimeUtil.getImmediateNextTimestamp(toDate);
		
		if(null != nextDate) {
			
			StringBuffer whereClause = new StringBuffer(" ? >= " + X_HR_Leave_Request.COLUMNNAME_FromDate);
									  whereClause.append("  AND ? <= " +  X_HR_Leave_Request.COLUMNNAME_ToDate);
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_C_BPartner_ID + " = ?");
									  whereClause.append(" AND " + X_HR_Leave_Request.COLUMNNAME_DocStatus + " <> ? ");
									  
			leaveRequestList = new Query( Env.getCtx(), X_HR_Leave_Request.Table_Name, whereClause.toString(), trxName).
							   setParameters(nextDate,nextDate,employeeId,X_HR_Leave_Request.DOCSTATUS_Voided).
							   setOnlyActiveRecords(Boolean.TRUE).
							   list();
		}
		
		return leaveRequestList;
	}
	
	/**
	 * 
	 * @param toDate
	 * @return
	 */
	public static BigDecimal getNumberOfDaysFromLeaveEndDate(Date toDate) {

		Timestamp nextDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(toDate.getTime());
//		cal.roll(Calendar.DAY_OF_YEAR, true);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		nextDate = new Timestamp(cal.getTimeInMillis());
		BigDecimal days = Env.ZERO;

		while(LeaveRequestManager.isNonWorkingDay(nextDate)) {
			
			days = days.add(Env.ONE);
			cal.setTimeInMillis(nextDate.getTime());
			cal.add(Calendar.DAY_OF_YEAR, 1); // 201112241058
			nextDate = new Timestamp(cal.getTimeInMillis());
			
		}
		
		return days;
	}

	/**
	 * 
	 * @param fromDate
	 * @return
	 */
	public static BigDecimal getNumberOfDaysFromLeaveStartDate(Date fromDate) {
		
		
		Timestamp prevDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(fromDate.getTime());
		cal.add(Calendar.DAY_OF_YEAR, -1);	// 201112241058
		prevDate = new Timestamp(cal.getTimeInMillis());
		BigDecimal days = Env.ZERO;

		while(isNonWorkingDay(prevDate)) {
			
			days = days.add(Env.ONE);
			cal.setTimeInMillis(prevDate.getTime());
			cal.add(Calendar.DAY_OF_YEAR, -1);	// 201112241058
			prevDate = new Timestamp(cal.getTimeInMillis());
			
		}
		
		return days;
	}

	/**
	 * <P>
	 * Get the number of days before the start date of the of the leave request<BR>
	 * which can be considered as holiday.<BR>
	 * 
	 * This is applicable only if at leave type setup configuartion to consider<BR>
	 * as adjacent leaves <BR>
	 * 
	 * </P>
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return	   : Number of holidays
	 */
	public static  BigDecimal getNumberOfDaysForConsiderationForLeaveRequestBeforeStartDate(Timestamp fromDate,Timestamp toDate) {
		
		if(null == fromDate || null == toDate )
			
			return NEG;
		
		BigDecimal prevDays =  getNumberOfDaysFromLeaveStartDate(fromDate);
		
		if(null != prevDays)
			return prevDays;
		else
			return Env.ZERO;
	}
	
	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static BigDecimal getNumberOfConsiderationDaysForLeaveRequestAfterEndDate(Timestamp fromDate,Timestamp toDate) {
		
		
		if(null == fromDate || null == toDate )
			
			return NEG;
		
		BigDecimal afterDays = getNumberOfDaysFromLeaveEndDate(toDate);
		
		if( null != afterDays)
			
			return afterDays;
		else
			
		return Env.ZERO;
	}



	public static BigDecimal getNumberOfDaysHolidaysBetweenLeaveRequestDates(
			Properties ctx, GridTab mTab) {
		
		Date fromDate = (Date)mTab.getValue("fromdate");
		Date toDate=(Date)mTab.getValue("todate");
		
		if(null == fromDate || null == toDate )
			
			return NEG;
		
		if( fromDate.equals(toDate))
			
			return Env.ZERO;
		
		int  days = 0;
		
		Timestamp timeStamp = null;
		
		while(!fromDate.after(toDate)) {
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(fromDate.getTime());
			
			timeStamp  = new Timestamp(cal.getTimeInMillis());
			
			if(isNonWorkingDay(timeStamp)) {
				
				days = days + 1;
				
			}
			
			//cal.roll(Calendar.DAY_OF_YEAR, true);
			//201112241058
			cal.add(Calendar.DAY_OF_YEAR, 1);
			fromDate  = new Timestamp(cal.getTimeInMillis());
		}
		return new BigDecimal(days);
	}



	public static Timestamp getNewStartDateForLeaveRequest(Timestamp startDate,	BigDecimal beforeDays,boolean before) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startDate.getTime());
		
		int count = 1;
		
		// 201112241058
		
		if(false == before) {
			
			count = -1;
		}
		
		while(beforeDays.intValue() > 0) {
			
			cal.add(Calendar.DAY_OF_YEAR, count);
			beforeDays = beforeDays.subtract(Env.ONE);
		}
		
		return new Timestamp(cal.getTimeInMillis());
		
	}
	
	/**
	 * 
	 * CASE 1 : <BR><BR>
	 *
	 * 		|-----------------------------------------|<BR>
	 * |---------------|<BR>
	 * 
	 * CASE 2:<BR><BR>
	 * 
	 * 		|-----------------------------------------|<BR>
	 * 	    |<BR><BR>
	 * 
	 * CASE 3:<BR><BR>
	 * 
	 * 	    |-----------------------------------------|<BR>
	 *          |---------------------|<BR><BR>
	 *          
	 * CASE 4:<BR><BR>
	 * 
	 * 	    |-----------------------------------------|<BR>
	 * 		|-------------------|<BR><BR>
	 * 
	 * CASE 5:<BR><BR>
	 * 
	 *      |-----------------------------------------|<BR>
	 *      						|-----------------|<BR><BR>
	 *      
	 * CASE 6:<BR><BR>
	 * 
	 * 		|-----------------------------------------|<BR>
	 *                                                |<BR><BR>
	 *                                                
	 * CASE 7:<BR><BR>
	 * 
	 * 		|-----------------------------------------|<BR>
	 * 												  |-------------|<BR><BR>
	 * 
	 * CASE 8:<BR><BR>
	 * 
	 * 		|-----------------------------------------|<BR>
	 *                              |----------------------|<BR><BR>
	 *                              
	 *                              
	 *                              
	 *                              
	 * start1 <= end2 && start2 <= end1
	 * 
	 * 201112220324
	 * 
	 * Assumption for this method is that it did not removed the time part of the timestamp
	 * 
	 * @param start1
	 * @param end1
	 * @param start2
	 * @param end2
	 * @return
	 */
	public static boolean isDatesOverlapping(Timestamp start1, Timestamp end1,Timestamp start2, Timestamp end2) {
		
		if(null == start1 || null == end1 || null == start2 || null == end2) {
			
			return Boolean.TRUE; // Assumption is that something went wrong
		}
		
		 
		if( (start1.before(end2) || start1.equals(end2)) && (start2.before(end1) || start2.equals(end1))) {
			
			return Boolean.TRUE;
		}else {
			
			return Boolean.FALSE;
		}
		
	}

}
