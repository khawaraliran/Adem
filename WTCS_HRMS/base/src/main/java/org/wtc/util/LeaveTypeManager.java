package org.wtc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_HR_Leave_Assign;
import org.compiere.model.MBPartner;
import org.compiere.model.MEmpGateAttendence;
import org.compiere.model.MEmployeeType;
import org.compiere.model.MLeaveAssign;
import org.compiere.model.MLeaveType;
import org.compiere.model.MPeriod;
import org.compiere.model.MSysConfig;
import org.compiere.model.MWTCLeaveCreditHistory;
import org.compiere.model.MWTCLeaveTypeEmployeeType;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.X_HR_Emp_Gate_Attendence;
import org.compiere.model.X_HR_Emp_Sup_Attendence;
import org.compiere.model.X_HR_LeaveType;
import org.compiere.util.CLogger;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;


/**
 * <P>
 * 	Leave type manager which takes care of deriving all the rules <BR>
 * 	and assigning the leaves to the employee <BR>
 * <P>
 * 
 * @author Ranjit
 * 
 * Change history - 
 * 
 * Task 		Date		Identifier		Author			Change
 * 
 * 1869			2/1/2012	212012			Ranjit			Added the values to the add_leaves field
 * 
 *
 */

public class LeaveTypeManager {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LeaveTypeManager.java 1009 2012-02-09 09:16:13Z suman $";
	
	private static final CLogger logger = CLogger.getCLogger(LeaveTypeManager.class);
	
	
	private static final int REPEATTYPE_MONTH 	= 1;
	private static final int REPEATTYPE_QUARTER = 4;
	private static final int REPEATTYPE_YEAR 	= 12;
	
	/**
	 * 
	 * @param employee
	 * @return
	 */
	public static boolean allocateLeavesForEmployee(MBPartner employee,String trxName ) {
		
		if(null == employee) 
			
			return Boolean.FALSE;
		
		List<MLeaveType> employeeLeaveTypeList = getEmployeeLeaveTypes(employee.getHR_Employee_Type_ID(),trxName);
		
		if(null != employeeLeaveTypeList && employeeLeaveTypeList.isEmpty()) {
			
			logger.log(Level.INFO," NO Leaves configured in the system for employee type - " + employee.getHR_Employee_Type_ID() );
		
			return Boolean.TRUE;
		}
		
		for ( MLeaveType type : employeeLeaveTypeList ) {
			
			createEmployeeLeaveAssignDetails(employee , type , trxName);
		}

		return Boolean.TRUE;
	}

	/**
	 * 
	 * @param hr_Employee_Type_ID
	 * @param trxName
	 * @return
	 */
	private static List<MLeaveType> getEmployeeLeaveTypes( int employeeTypeID, String trxName) {

		
		List <MLeaveType> employeeLeaveTypeList = new ArrayList<MLeaveType>();
		
		List <MWTCLeaveTypeEmployeeType> leaveTypeEmployeeTypeList = MWTCLeaveTypeEmployeeType.getLeaveTypeForEmployee(employeeTypeID);
		
		if(null != leaveTypeEmployeeTypeList && !leaveTypeEmployeeTypeList.isEmpty()) {
			
			for( MWTCLeaveTypeEmployeeType emplLeaveTypeEmployeeType : leaveTypeEmployeeTypeList ) {
				
					if(null != emplLeaveTypeEmployeeType.getHR_LeaveType()) {
						
						employeeLeaveTypeList.add( (MLeaveType)emplLeaveTypeEmployeeType.getHR_LeaveType());
					}
			}
		}
		
		return employeeLeaveTypeList;
	}
	
	/**
	 * Returns the C_Year_ID for the provided timestamp
	 * 
	 * @param timestamp	: Timestamp
	 * @return			: YearID
	 */
	private static int getYearIdForTimestamp(Timestamp timestamp) {
		
		if( null == timestamp)
			return 0;
		
		int yearId = -1;
		int periodID = WTCTimeUtil.getCPeriodId(timestamp);
		
		if(periodID > 0) {
			
			MPeriod period = MPeriod.get(Env.getCtx(), periodID);
			
			if(null != period)
			yearId = period.getC_Year_ID();
		}
		
		return yearId;
	}
	
	/**
	 * <P>
	 * 	Creates the employee leave assign details. <BR>
	 *  This method is used to create leave assign detail while employee creation <BR>
	 * 
	 * @param employee		: MBPartner
	 * @param leaveType		: MLeaveType
	 * @param trxName		
	 * @return				: TRUE if leave assign detail created successfully else FALSE
	 */
	private static boolean createEmployeeLeaveAssignDetails( MBPartner employee, 
															 MLeaveType leaveType, 
															 String trxName){
		
		int yearId = getYearIdForTimestamp(new Timestamp(System.currentTimeMillis()));
		
		
		if( yearId <= 0) {

			logger.log(Level.SEVERE, "Failed to get the year id for the employee joining date");
			return Boolean.FALSE;
		}
			
		MLeaveAssign leaveAssign =  new MLeaveAssign(Env.getCtx(), 0, trxName);

		leaveAssign.setC_BPartner_ID(employee.getC_BPartner_ID());
		leaveAssign.setemployee_code(employee.getemployee_code());
		leaveAssign.setHR_LeaveType_ID(leaveType.getHR_LeaveType_ID());
		leaveAssign.setadd_leaves(Env.ZERO);							// Initial no leaves credited
		leaveAssign.setbalance_leaves(Env.ZERO);
		leaveAssign.settotal_leaves(Env.ZERO);
		
		leaveAssign.setC_Year_ID(yearId);
		leaveAssign.setemployee_code(employee.getemployee_code());

		if (!leaveAssign.save()) {

			logger.log( Level.SEVERE,
						"Failed to associate leave type - " 
						+ leaveType.getHR_LeaveType_ID() 
						+ " to employee - " 
						+ employee.getC_BPartner_ID() );
			
			return Boolean.FALSE;
		} else {

			//
			// Create the Leave credit history
			//

			boolean hisotryCreaded = MWTCLeaveCreditHistory.createLeaveCreditHistory(leaveAssign,
															  EagleConstants.INITIAL_LEAVE_CREDIT_REASON,
															  null, 
															  trxName);

			if (Boolean.TRUE == hisotryCreaded) {

				logger.log( Level.FINE,
							"Leave credit history created successfully as part of employee setup");
				
				return Boolean.TRUE;
			} else {

				logger.log( Level.SEVERE,
							"Failed to create the leave credit history as part of employee setup");
				return Boolean.FALSE;
			}
		}
	}
	
	/**
	 * <P>
	 *  Returns the reference of the respective leave type for <BR>
	 *  provided leave type code <BR>
	 *  </P>
	 *  
	 * @param leaveTypeCode		: Leave type code 
	 * @return					: 
	 */
	public static LeaveType getLeaveTypeInstance(String leaveTypeCode) {
		
		return LeaveTypeFactory.getLeaveType(leaveTypeCode);
	}
	
	/**
	 * <P>
	 * Retrieves all the employee types for provided leave type id <BR>
	 * </P>
	 * 
	 * @param leaveTypeId   : Leave type id
	 * @return 				: Array of employee types
	 */
	public static MEmployeeType[] getEmployeeTypeForLeaveType(int leaveTypeId) {

		List<MEmployeeType> employeeTypeList = new ArrayList<MEmployeeType>();

		List<MWTCLeaveTypeEmployeeType> leaveTypeEmployeeTypeList = MWTCLeaveTypeEmployeeType.getEmployeeTypeForLeaveType(leaveTypeId);

		if ( null != leaveTypeEmployeeTypeList && 
			 !leaveTypeEmployeeTypeList.isEmpty()) {

			for ( MWTCLeaveTypeEmployeeType emplLeaveTypeEmployeeType : leaveTypeEmployeeTypeList ) {

				if (null != emplLeaveTypeEmployeeType.getHR_Employee_Type()) {

					employeeTypeList.add((MEmployeeType) emplLeaveTypeEmployeeType.getHR_Employee_Type());
				}
			}
		}

		MEmployeeType[] typeArray = new MEmployeeType[employeeTypeList.size()];
		employeeTypeList.toArray(typeArray);
		
		return typeArray;
	}

	/**
	 * <P>
	 * 	Get all the employees with provided employee type & who are not in <BR>
	 * 	following status is not in probation period <BR>
	 * </P>
	 * 
	 * 
	 * @param employeeTypeId   : Employee type id
	 * @return
	 */
	public static List<MBPartner> getEmployeeListForEmployeeType( int employeeTypeId ) {

		List<MBPartner> employeeList = new ArrayList<MBPartner>();

		StringBuffer whereClause = new StringBuffer(I_C_BPartner.COLUMNNAME_HR_Employee_Type_ID + " = ?");
								   whereClause.append(" AND " + I_C_BPartner.COLUMNNAME_IsEmployee + " = 'Y'");
								   whereClause.append(" AND " + I_C_BPartner.COLUMNNAME_employeestatus);
								   whereClause.append(" NOT IN ( '"	+ X_C_BPartner.EMPLOYEESTATUS_Probation + "'");
								   whereClause.append(" , '" + X_C_BPartner.EMPLOYEESTATUS_Separated + "'");
								   whereClause.append(")");

		employeeList = new Query(Env.getCtx(), I_C_BPartner.Table_Name,whereClause.toString(), null).
					   setParameters(employeeTypeId)
					   .setOnlyActiveRecords(Boolean.TRUE).
					   list();

		return employeeList;
	}

	/**
	 * <P>
	 * 	Get the employee leave detail for the provided leave type <BR>
	 *  Unique record for employee with leave type <BR>
	 * </P>
	 * 
	 * @param bpartnerId		: Business partner id
	 * @param leaveTypeId		: Leave type id
	 * @return					: MLeaveAssign
	 */
	public static MLeaveAssign getEmployeeLeaveAssignDetailForLeaveType( int bpartnerId, int leaveTypeId) {
		
		StringBuffer whereClause = new StringBuffer( I_HR_Leave_Assign.COLUMNNAME_C_BPartner_ID + " = ? ");
								   whereClause.append(" AND " + I_HR_Leave_Assign.COLUMNNAME_HR_LeaveType_ID + " = ? ");
		
		MLeaveAssign leaveAssign = new Query( Env.getCtx(), I_HR_Leave_Assign.Table_Name, whereClause.toString(), null).
								   setParameters( bpartnerId,leaveTypeId).
								   setOnlyActiveRecords(Boolean.TRUE).
								   first();
		return leaveAssign;
	}
	
	public static BigDecimal getCreditLeavesForMonth(MLeaveType leaveType, MBPartner employee,Timestamp date){
		
		
		BigDecimal noOfLeavesForCredit = Env.ZERO;
		
		Calendar calendar = Calendar.getInstance();
				 			calendar.setTimeInMillis(date.getTime());
		
		Timestamp fromdate = null;
		Timestamp toDate = null;
		
		//
		// based on  the leave type credit time  give fromDate and toDate proper value
		//
		
		if( leaveType.getmontlycredittime().equalsIgnoreCase(MLeaveType.MONTLYCREDITTIME_EndOfThePeriod)){
			
			toDate = WTCTimeUtil.getPreviousMonthLastDay(date);
			
			calendar.setTimeInMillis(toDate.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			fromdate = new Timestamp(calendar.getTimeInMillis());;
			
			// fromdate = checkIfEmployeeInProbationForPreaviousMonth(employee,fromdate,toDate);
			
			BigDecimal employeeAttendance = getEmployeeAttendanceForProeviousPeriod(employee.getC_BPartner_ID(),fromdate,toDate);
			
			BigDecimal minAttendanceRequire = leaveType.getminimumattendancerequire();
			
			if(employeeAttendance.compareTo(minAttendanceRequire) >= 0) {
				
				//
				// Employee is eligible for leave as minimum attendance is satisfied
				//
				
				noOfLeavesForCredit =  leaveType.getnumber_of_leaves();
			}
			
		}
		else{
			
			// 
			// Credit time is start of the month so no check for the attendance,
			// directly returning the number of leaves to be credited for the leave type
			//
			
			noOfLeavesForCredit =  leaveType.getnumber_of_leaves();
		}
		
		return noOfLeavesForCredit;
	}

	private static BigDecimal getEmployeeAttendanceForProeviousPeriod( int bpartnerID, Timestamp fromdate, Timestamp toDate) {
		
		
		boolean supervisorAttendance = MSysConfig.getBooleanValue( EagleConstants.USE_ATTENDANCE_BY_SUPERVISOR, 
																   Boolean.FALSE);
		
		BigDecimal employeeAttendance = Env.ZERO;
		
		if(supervisorAttendance) {
			
			employeeAttendance = getEmployeeAttendanceFromSupervisor(bpartnerID,fromdate,toDate);
			
		}else {
			
			employeeAttendance = getEmployeeAttendanceFromGate(bpartnerID,fromdate,toDate,false);
		}
		
		return employeeAttendance;
	}

	public static BigDecimal getEmployeeAttendanceFromGate(int bpartnerID,
			Timestamp fromdate, Timestamp toDate, boolean generalPayroll) {
		
		int attended = 0;
		
		StringBuffer whereClause = new StringBuffer(X_HR_Emp_Gate_Attendence.COLUMNNAME_C_BPartner_ID + " = ?" );
		whereClause.append(" AND " + X_HR_Emp_Gate_Attendence.COLUMNNAME_workdate + " >= ? "  );
		
		whereClause.append( " AND " + X_HR_Emp_Gate_Attendence.COLUMNNAME_workdate +  " <= ?");
		
		
		List<MEmpGateAttendence> empGateAttendanceList = new Query( Env.getCtx(), X_HR_Emp_Gate_Attendence.Table_Name, whereClause.toString(),null).
															   setParameters(bpartnerID,fromdate,toDate).
															   setOnlyActiveRecords(Boolean.TRUE).
															   list();
		
		
		if(null != empGateAttendanceList) {
			
			attended =  empGateAttendanceList.size();
		
			if(generalPayroll) {
				
				for(MEmpGateAttendence empGateAttendence : empGateAttendanceList) {
					
					empGateAttendence.setProcessed(Boolean.TRUE);
					empGateAttendence.save();
				}
			}
		}
		
		return new BigDecimal(attended);
	}

	public static BigDecimal getEmployeeAttendanceFromSupervisor(
			int bpartnerID, Timestamp fromdate, Timestamp toDate) {
		
		StringBuffer whereClause = new StringBuffer(X_HR_Emp_Sup_Attendence.COLUMNNAME_C_BPartner_ID + " = ?" );
		whereClause.append(" AND " + X_HR_Emp_Sup_Attendence.COLUMNNAME_workdate + " >= ? "  );
		whereClause.append( " AND " + X_HR_Emp_Gate_Attendence.COLUMNNAME_workdate +  " <= ? ");
		
		
		int attended = new Query( Env.getCtx(), X_HR_Emp_Sup_Attendence.Table_Name, whereClause.toString(),null).
					   setParameters(bpartnerID,fromdate,toDate).
					   setOnlyActiveRecords(Boolean.TRUE).
					   count();
		
		return new BigDecimal(attended);
	}

	private static Timestamp checkIfEmployeeInProbationForPreaviousMonth( MBPartner employee, 
																		  Timestamp fromdate,
																		  Timestamp toDate) {

		//
		// 1. Current status of the employee is not in probation (Assumption)
		// 2. Get the date of confirmation for the employee i.e. removed from probation status
		// 3. If yes then return the conformed date as from date else original from date
		//
		
		Timestamp conformedDate = employee.getremovedfromprobationdate();

		boolean status = isConformedDateWithinRange(conformedDate,fromdate,toDate);
		
		if(status) {
			
			return conformedDate;
		}else {
		
			return fromdate;
		}
	}

	private static int getDaysBetween(Timestamp fromDate, Timestamp toDate) {
		
		return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
	}

	private static boolean isConformedDateWithinRange(
			Timestamp conformedDate, Timestamp fromdate, Timestamp toDate) {
		
		return conformedDate.getTime() >= fromdate.getTime() && conformedDate.getTime() <= toDate.getTime();
	}
	
	
	public static BigDecimal creditLeavesForYear(MBPartner employee,MLeaveType leaveType, int year,Timestamp date){
		
		Timestamp timeStamp  = null;
		
		if( year == 0){

			timeStamp = new Timestamp(System.currentTimeMillis());
			timeStamp = WTCTimeUtil.getPreviousMonthLastDay(timeStamp);
			year = timeStamp.getYear();
			
		}
		
		else{
		
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, cal.getActualMinimum(Calendar.MONTH));
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			timeStamp = new Timestamp(cal.getTimeInMillis());
			timeStamp = WTCTimeUtil.getPreviousMonthLastDay(timeStamp);
		}
		
		BigDecimal monthCredit = getCreditLeavesForMonth(leaveType,employee, date);
		
		MLeaveAssign assign = new Query(Env.getCtx(),
				MLeaveAssign.Table_Name,MLeaveAssign.COLUMNNAME_C_BPartner_ID+" = "
				+employee.getC_BPartner_ID()+" AND "+MLeaveAssign.COLUMNNAME_HR_LeaveType_ID
				+" = "+leaveType.getHR_LeaveType_ID(),null)
		.setOnlyActiveRecords(true)
		.first();
		
		assign.setbalance_leaves(( assign.getbalance_leaves().add(monthCredit)));
		
		
		String where = MLeaveAssign.COLUMNNAME_C_Year_ID+ " =( SELECT C_Year_ID FROM C_Year WHERE 	fiscalyear = "+(year-1)+" ) AND "+MLeaveAssign.COLUMNNAME_HR_Employee_ID+" = " +
				employee.getHR_Employee_ID()+" AND "+MLeaveAssign.COLUMNNAME_HR_LeaveType_ID+" = "+leaveType.getHR_LeaveType_ID();
		
		
		MLeaveAssign assign1 = new Query(Env.getCtx(),MLeaveAssign.Table_Name,where,null).setOnlyActiveRecords(true).first();
		
		
		
		return assign1.getbalance_leaves();
		
		
		
	}

	
	public static boolean checDatekForNewYear( Timestamp currentMonthDate,String repeatedType ) {
		
		
		if(repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Monthly)) {
				
			Timestamp lastmonthDate  = WTCTimeUtil.getPreviousMonthLastDay(currentMonthDate);
		
			int previousMonthYear   =  WTCTimeUtil.getYear(lastmonthDate);
			int currentMonthYear 	=  WTCTimeUtil.getYear(currentMonthDate);
		
			if(previousMonthYear == currentMonthYear) {
			
				return Boolean.FALSE;
			}else {
			
				return Boolean.TRUE;
			}
		}else if(repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Quarterly)) {
			
			Timestamp lastQuarterLastDate  = WTCTimeUtil.getLastDayOfPreviousQuarter(currentMonthDate);
			
			int previousMonthYear   =  WTCTimeUtil.getYear(lastQuarterLastDate);
			int currentMonthYear 	=  WTCTimeUtil.getYear(currentMonthDate);
		
			if(previousMonthYear == currentMonthYear) {
			
				return Boolean.FALSE;
			}else {
			
				return Boolean.TRUE;
			}
			
			
		}else {
			
			return Boolean.TRUE;
		}
	}

	public static MLeaveAssign createNewLeaveAssignDetail( MBPartner employee,
														   MLeaveAssign empLeaveAssign, 
														   BigDecimal noOfLeavesForCredit,
														   Timestamp date, 
														   String trxName) {
		
		
		int yearId = WTCTimeUtil.getYearIdForDate(date); 
		
		MLeaveAssign leaveAssign = new MLeaveAssign(Env.getCtx(), 0, trxName);
		
		leaveAssign.setC_BPartner_ID(empLeaveAssign.getC_BPartner_ID());
		leaveAssign.setC_Year_ID(yearId);
		leaveAssign.setHR_Employee_ID(employee.getHR_Employee_ID());
		leaveAssign.setHR_LeaveType_ID(empLeaveAssign.getHR_LeaveType_ID());
		leaveAssign.setadd_leaves(noOfLeavesForCredit);
		leaveAssign.setbalance_leaves(noOfLeavesForCredit);
		leaveAssign.settotal_leaves(noOfLeavesForCredit);
		leaveAssign.setemployee_code(employee.getemployee_code());
		
		if(leaveAssign.save()) {
			
			//
			// Creates the history for the employee leave
			// assign entry
			//

			boolean createdHistory = MWTCLeaveCreditHistory.createLeaveCreditHistory( empLeaveAssign,
																					  EagleConstants.LEAVE_CREDIT_PROCESS_REASON,null,
																					 trxName);

			if (createdHistory) {

				logger.log( Level.FINE,
						   " Leave Credit History created for the leave assign id - "
							+ empLeaveAssign.get_ID());
			}
			
			
			return leaveAssign;
		}
		
		return null;
	}

	public static BigDecimal getCarryForwardedLeavesForLeaveType( LeaveType leavetype,MLeaveAssign empLeaveAssign, String trxName) {

		
		
		if (leavetype.canLeavesForwardedToNextYear()) {

			BigDecimal balanceLeaves = empLeaveAssign.getbalance_leaves();
			BigDecimal leavesCanForward = leavetype.maximumLeavesForwardedToNextYear();

			if (balanceLeaves.compareTo(leavesCanForward) > 0) {

				return leavesCanForward;
			} else {
				
				return balanceLeaves;
			}

		}
		return BigDecimal.ZERO;
	}

	public static boolean updateLeaveAssignDetailWithNoOfLeavesCarryForward(
			BigDecimal noOfLeavesForCarryForward, MLeaveAssign newLeaveAssign,
			String trxName) {
		
		newLeaveAssign.setprvyearcfleaves(noOfLeavesForCarryForward);
		newLeaveAssign.setadd_leaves(newLeaveAssign.getadd_leaves().add(noOfLeavesForCarryForward));
		newLeaveAssign.setbalance_leaves(newLeaveAssign.getbalance_leaves().add(noOfLeavesForCarryForward));
		newLeaveAssign.settotal_leaves(newLeaveAssign.gettotal_leaves().add(noOfLeavesForCarryForward));
		
		if(newLeaveAssign.save(trxName)) {
			
			
			//
			// Creates the history for the employee leave
			// assign entry
			//

			boolean createdHistory = MWTCLeaveCreditHistory.createLeaveCreditHistory( newLeaveAssign,
																					  EagleConstants.LEAVE_CARRYFORWARD_REASON,
																					  noOfLeavesForCarryForward,
																					 trxName);

			if (createdHistory) {

				logger.log( Level.FINE,
						   " Leave Credit History created for the leave assign id - "
							+ newLeaveAssign.get_ID());
			}
			
			
			return Boolean.TRUE;
			
		}
		
		return Boolean.FALSE;
	}

	/**
	 * <P>
	 * 	Updates the next run date for the leave credit process, <BR>
	 *  so that process will execute after the next run date only <BR>
	 *  for the provided type.<BR>
	 *  Next run date depends on the repetition type : <BR>
	 *  
	 *  <LI>Month</LI>
	 *  <LI>Quarter</LI>
	 *  <LI>Year</LI>
	 *  
	 * </P>
	 * @param date			: Credit leave process run date
	 * @param repeatedType	: Repetition date
	 * @param mleaveType	: MLeaveType
	 */
	public static void updateNextRunDateForLeaveType( Timestamp date,
													  String repeatedType, 
													  MLeaveType mleaveType) {
		
		Timestamp newDate = null;
		
		if(repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Monthly)){
			
			 newDate = WTCTimeUtil.firstDayOfProvidedType(date,REPEATTYPE_MONTH);
		}else if(repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Quarterly))	{
			
			 newDate = WTCTimeUtil.firstDayOfProvidedType(date,REPEATTYPE_QUARTER);
		}else if(repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Yearly))	{
			
			 newDate = WTCTimeUtil.firstDayOfProvidedType(date,REPEATTYPE_YEAR);
		}
		
		if (null != newDate) {
			
			//
			// Updates the next run date for the leave type
			//
			
			mleaveType.setnextrundate(newDate);
			
			if(mleaveType.save()){
			
				logger.log(Level.FINE," Next run date updated successfully for the leave type - " + mleaveType.get_ID());
			}else {

				logger.log( Level.SEVERE, 
							"Failed to update the next run date for the leave type - [ " 
							+ mleaveType.getleavetype() 
							+ " - " 
							+ mleaveType.get_ID() 
							+ "]. Leave credit process run date - " 
							+ date);
			}
		}
	}

	public static BigDecimal getCreditLeavesForQuarter(MLeaveType leaveType,MBPartner employee, Timestamp date) {
		
			BigDecimal noOfLeavesForCredit = Env.ZERO;
		
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date.getTime());
		
		Timestamp fromdate = null;
		Timestamp toDate = null;
		
		
		if( leaveType.getmontlycredittime().equalsIgnoreCase(MLeaveType.MONTLYCREDITTIME_EndOfThePeriod)){
			
			toDate = WTCTimeUtil.getLastDayOfPreviousQuarter(date);
			fromdate = WTCTimeUtil.getFirstayOfPreviousQuarter(toDate);
			
			BigDecimal employeeAttendance = getEmployeeAttendanceForProeviousPeriod(employee.getC_BPartner_ID(),fromdate,toDate);
			
			BigDecimal minAttendanceRequire = leaveType.getminimumattendancerequire();
			
			if(employeeAttendance.compareTo(minAttendanceRequire) >= 0) {
				
				//
				// Employee is eligible for leave as minimum attendance is satisfied
				//
				
				noOfLeavesForCredit =  leaveType.getnumber_of_leaves();
			}
			
		}
		else{
			
			// 
			// Credit time is start of the month so no check for the attendance,
			// directly returning the number of leaves to be credited for the leave type
			//
			
			noOfLeavesForCredit =  leaveType.getnumber_of_leaves();
		}
		
		return noOfLeavesForCredit;
	}

	/**
	 * <P>
	 * 	Calculates the number of leaves to credit towards employee for the <BR>
	 *  provided year. <BR>
	 * 
	 * 
	 * @param leaveType
	 * @param employee
	 * @param date
	 * @return
	 */
	public static BigDecimal getCreditLeavesForYear(MLeaveType leaveType,
			MBPartner employee, Timestamp date) {

		
		/*
		 * 1. Check for the credit time of the leave type
		 * 
		 * 2. If credit time end of period then get first & last day of the previous year 
		 * 
		 * 3. Get the employee attendance for the last yearusing first day & last day from step 2 
		 * 
		 * 4. Get the minimum attendance require value to eligible the leave credit
		 * 
		 * 5. If the attendance is sufficient to credit leave then return the no of
		 *    leaves for credit.
		 *    
		 * 6. Else zero
		 * 
		 * 7. If the leave credit time is start of the provided leave type period
		 *    then  
		 */
		
		
		
		
		BigDecimal noOfLeavesForCredit = Env.ZERO;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());

		Timestamp fromdate = null;
		Timestamp toDate = null;

		if (leaveType.getmontlycredittime().equalsIgnoreCase(
				MLeaveType.MONTLYCREDITTIME_EndOfThePeriod)) {

			toDate = WTCTimeUtil.lastDayOfPreviousYear(date);
			fromdate = WTCTimeUtil.firstDayOfPreviousYear(toDate);

			BigDecimal employeeAttendance = getEmployeeAttendanceForProeviousPeriod(
					employee.getC_BPartner_ID(), fromdate, toDate);

			BigDecimal minAttendanceRequire = leaveType
					.getminimumattendancerequire();

			if (employeeAttendance.compareTo(minAttendanceRequire) >= 0) {

				//
				// Employee is eligible for leave as minimum attendance is
				// satisfied
				//

				noOfLeavesForCredit = leaveType.getnumber_of_leaves();
			}

		} else {

			//
			// Credit time is start of the month so no check for the attendance,
			// directly returning the number of leaves to be credited for the
			// leave type
			//

			noOfLeavesForCredit = leaveType.getnumber_of_leaves();
		}

		return noOfLeavesForCredit;
	}

}
