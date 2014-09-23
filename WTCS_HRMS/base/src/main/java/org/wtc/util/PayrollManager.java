package org.wtc.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_HR_Emp_Token;
import org.compiere.model.I_HR_Employee_WeeklyOff;
import org.compiere.model.I_HR_Leave_Request;
import org.compiere.model.I_HR_Leave_RequestLine;
import org.compiere.model.I_HR_TDS;
import org.compiere.model.I_WTC_LeaveType_EmployeeType;
import org.compiere.model.MBPartner;
import org.compiere.model.MEmpSupAttendence;
import org.compiere.model.MEmpToken;
import org.compiere.model.MNonBusinessDay;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTDS;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.X_HR_Emp_Token;
import org.compiere.model.X_HR_Employee_WeeklyOff;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.eevolution.model.MHRPeriod;
import org.eevolution.model.MHRProcess;
import org.eevolution.model.X_HR_Period;
import org.eevolution.model.X_HR_Process;


/**
 * 
 * @author Ranjit
 * 
 * Change History
 * 
 * 
 * Task		Date		Identification			Author			Change
 * 
 * 1655		5/1/2012	201201050813			Ranjit			deletemovements method modified
 *
 */
public class PayrollManager {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: PayrollManager.java 1009 2012-02-09 09:16:13Z suman $";
	
	private static final CLogger logger = CLogger.getCLogger(PayrollManager.class);

	
	/**
	 * RE-Process, delete movement except concept type Incidence
	 * 
	 * @param periodId : PeriodId
	 * @param manual   : IsManual
	 * @param trxName
	 */
	public static void deletemovements(int periodId, boolean manual,String trxName){
		
		String sql = "DELETE FROM HR_Movement m WHERE HR_Process_ID IN (SELECT HR_Process_ID FROM HR_Process WHERE HR_Period_ID = ? AND payrollprocesstype = ? ) AND IsManual<>?";
		
		int no = DB.executeUpdateEx(sql,new Object[] { periodId,X_HR_Process.PAYROLLPROCESSTYPE_Mock, manual },trxName);
		
		logger.info("HR_Movement deleted #" + no);
	}
	
	/**
	 * Deletes the pay slip entries
	 * 
	 * @param periodId	: PeriodId
	 * @param trxName	:
	 */
	public static void deletePaySlipEntriesForPeriod(int periodId, String trxName) {
		
		String sql = "DELETE FROM HR_Payslip m WHERE HR_Period_ID=? AND ismock = 'Y' ";
		
		int no = DB.executeUpdateEx(sql,new Object[] { periodId },trxName);
			
		logger.info("Pay slip entries deleted #" + no);
	}
	
	/**
	 * Get all the active employees for the payroll process
	 * 
	 * @param hrProcess	: MHRProcess
	 * @return			: Array of business partners
	 */
	public static MBPartner[] getEmployees (MHRProcess hrProcess) {
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		
		whereClause.append(" C_BPartner.C_BPartner_ID IN (SELECT e.C_BPartner_ID FROM HR_Employee e WHERE e.IsActive=?");
		
		// Just active employee
		params.add(true);

		// This payroll not content periods, NOT IS a Regular Payroll > ogi-cd 28Nov2007
		if(hrProcess.getHR_Payroll_ID() != 0 && hrProcess.getHR_Period_ID() != 0){
			
			whereClause.append(" AND (e.HR_Payroll_ID IS NULL OR e.HR_Payroll_ID=?) " );
			params.add(hrProcess.getHR_Payroll_ID());
		}
		
		// HR Period
		if(hrProcess.getHR_Period_ID() == 0){
			
			whereClause.append(" AND e.StartDate <=? ");
			params.add(hrProcess.getDateAcct());	
		}else{
			
			whereClause.append(" AND e.StartDate <=? ");
			params.add(new X_HR_Period(hrProcess.getCtx(), hrProcess.getHR_Period_ID(), null).getEndDate());
		}
		
		// Selected Department
		if (hrProcess.getHR_Department_ID() != 0){
			
			whereClause.append(" AND e.HR_Department_ID =? ");
			params.add(hrProcess.getHR_Department_ID());
		}
		
		whereClause.append(" ) "); // end select from HR_Employee
		
		// Selected Employee
		if (hrProcess.getC_BPartner_ID() != 0){
			
			whereClause.append(" AND C_BPartner_ID =? ");
			params.add(hrProcess.getC_BPartner_ID());
		}
		
		
		String sql = " AND ( hr_designation_id in ( select hr_designation_id from HR_Designation where hr_payroll_id = " +  hrProcess.getHR_Payroll_ID() + ")" +
		" OR hr_employee_type_id IN ( select hr_employee_type_id from HR_Employee_Type where hr_payroll_id =" + hrProcess.getHR_Payroll_ID() + "))";
		
		
		whereClause.append(sql);
		
		//client
		whereClause.append(" AND AD_Client_ID =? ");
		params.add(hrProcess.getAD_Client_ID());
		
		whereClause.append(" AND " + X_C_BPartner.COLUMNNAME_relievingdate + " IS NULL OR " + " ? <  " + X_C_BPartner.COLUMNNAME_relievingdate);
		params.add(hrProcess.getHR_Period().getStartDate());
		
		
		List<MBPartner> list = new Query(hrProcess.getCtx(), I_C_BPartner.Table_Name, whereClause.toString(), hrProcess.get_TrxName())
								.setParameters(params)
								.setOnlyActiveRecords(true)
								.setOrderBy(I_C_BPartner.COLUMNNAME_Name)
								.list();
		

		return list.toArray(new MBPartner[list.size()]);
		
	}	//	getEmployees
	
	
	/**
	 * Returns the payroll id for the provided employee id
	 * 
	 * @param employeeId		: C_BPartner_ID
	 * @return					: HR_Payroll_ID
	 */
	 public static int getPayrollId(int employeeId) {
		 
		 int payrollId = 0;
		 if(employeeId < 0 )
			 
			 return payrollId;

		 // Get the business partner for the employee 
		 
		 MBPartner employee = new MBPartner(Env.getCtx(), employeeId, null);
		 
		 if(null != employee) {
		 
			 //check HR_Designation for payroll id
		
			 String designationSql = "SELECT HR_Payroll_ID FROM HR_Designation WHERE HR_Designation_ID = ?";
		
			 payrollId = DB.getSQLValue(null, designationSql, employee.getHR_Designation_ID());
				
			 // If payroll id doesnt exist for Designation then get payrollid from EmployeeType
				
			 if(payrollId == 0){
			
				 String sql = "SELECT HR_Payroll_ID FROM HR_Employee_Type WHERE HR_Employee_Type_ID = ?";
				 payrollId = DB.getSQLValue(null, sql, employee.getHR_Employee_Type_ID());
			 }
		 }
		 
		return payrollId;
	 }
	 /**
	  * 
	  * @param mbEmployee
	  * @param period
	  * @return
	  */
	 public static int getEmployeeNumberOfLeaveDaysForPeriod(int employeeBPPartnerId, MHRPeriod period) {
		 
		 
		StringBuffer whereClause = new StringBuffer("");
					 whereClause.append(I_HR_Leave_Request.COLUMNNAME_IsApproved + " = 'Y'");
					 whereClause.append(" AND " + I_HR_Leave_Request.COLUMNNAME_isavailed + " = 'Y'");
					 whereClause.append(" AND " + I_HR_Leave_Request.COLUMNNAME_FromDate + " <= ? ");
					 whereClause.append(" AND ? <= " + I_HR_Leave_Request.COLUMNNAME_ToDate);
					 whereClause.append(" AND " + I_HR_Leave_Request.COLUMNNAME_C_BPartner_ID +  " = ?");
					 
			
		int leaveCount = new Query( Env.getCtx(), I_HR_Leave_Request.Table_Name, whereClause.toString(), null)
						.setParameters( period.getEndDate(), 
										period.getStartDate(), 
										employeeBPPartnerId)
						.setOnlyActiveRecords(Boolean.TRUE)
						.count();
					
		 return leaveCount;
	 }
	 
	public static BigDecimal getCanteenAmountForEmployee(int employeeBusinessPartnerId, int periodId, boolean processIt,String trxName) {
		
		
		BigDecimal totalCanteenAmt = Env.ZERO;
		
		StringBuffer whereClause = new StringBuffer();
					 whereClause.append(I_HR_Emp_Token.COLUMNNAME_C_BPartner_ID + " = ?");
					 whereClause.append(" AND " + I_HR_Emp_Token.COLUMNNAME_HR_Period_ID + " = ?");
					 whereClause.append(" AND " + I_HR_Emp_Token.COLUMNNAME_tokenlevel +  "=  ? ");
					 
		List<MEmpToken> tokens = new Query(Env.getCtx(),I_HR_Emp_Token.Table_Name,whereClause.toString(),trxName)
								 .setParameters(employeeBusinessPartnerId,periodId,X_HR_Emp_Token.TOKENLEVEL_Canteen)
								 .setOnlyActiveRecords(Boolean.TRUE)
								 .list();
		
		if (null != tokens) {

			for (MEmpToken token : tokens) {

				totalCanteenAmt = totalCanteenAmt.add(token.gettotalamount());

				// set processed value of the record to true and save the record

				if (processIt) {

					token.setProcessed(true);
					token.save(trxName);
				}

			}
		}
		
		return totalCanteenAmt;
	}
	
	public static BigDecimal getStoreAmmountForEmployee(int employeeBusinessPartnerId, int periodId, boolean processIt,String trxName) {
		
		BigDecimal totalStoreAmt = Env.ZERO;
		
		StringBuffer whereClause = new StringBuffer();
								   whereClause.append(I_HR_Emp_Token.COLUMNNAME_C_BPartner_ID + " = ?");
								   whereClause.append(" AND " + I_HR_Emp_Token.COLUMNNAME_HR_Period_ID + " = ?");
								   whereClause.append(" AND " + I_HR_Emp_Token.COLUMNNAME_tokenlevel +  "=  ? ");
								   
	   List<MEmpToken> tokens = new Query(Env.getCtx(),I_HR_Emp_Token.Table_Name,whereClause.toString(),trxName)
	   							.setParameters(employeeBusinessPartnerId,periodId,X_HR_Emp_Token.TOKENLEVEL_Stores)
	   							.setOnlyActiveRecords(Boolean.TRUE)
	   							.list();
								   
		if (null != tokens) {	
		
			for(MEmpToken token : tokens){
				
			totalStoreAmt = totalStoreAmt.add(token.gettotalamount());
			
			// set processed value of the record to true and save the record

			if(processIt) {
				
				token.setProcessed(true);
				token.save(trxName);
			}
		}
	}
		
		return totalStoreAmt;
	}
	
	/**
	 * 
	 * @param m_C_BPartner_ID	: Business partner id for employee
	 * @param m_HR_Period_ID	: HR period id
	 * @param processType		: Type of process (Like - Mock, General)
	 * @param trxName			
	 * @return					: Employee TDS amount for the period
	 */
	public static double getEmployeeTDSAmount(int m_C_BPartner_ID, int m_HR_Period_ID,String processType,String trxName) {
		
		StringBuffer whereClause = new StringBuffer();
		
    	whereClause.append(I_HR_TDS.COLUMNNAME_C_BPartner_ID + " = ? ");
    	whereClause.append(" AND " + I_HR_TDS.COLUMNNAME_HR_Period_ID + " = ?");
    	
    	MTDS tds = new Query(Env.getCtx(),I_HR_TDS.Table_Name,whereClause.toString(),trxName)
    				   .setParameters( m_C_BPartner_ID , m_HR_Period_ID)
    				   .setOnlyActiveRecords(Boolean.TRUE)
    				   .first();
    	
    	BigDecimal tdsamount = Env.ZERO;
   	
    	if(tds != null){
    		
    		tdsamount = tdsamount.add(tds.gettdsamount());
    	
    		if(processType.equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)) {
    		
    			tds.setProcessed(true);
    			tds.save();
    		}
    	}
    	
    	return tdsamount.doubleValue();
	}
	
	
	
	public static List<Timestamp> getNonWorkingDayDatesForEmployee(MBPartner emplMbPartner, Timestamp startDate , Timestamp endDate){

		List<Timestamp> employeeWeeklyOffDateList = new ArrayList<Timestamp>();
		
		if(null == emplMbPartner)
			
			return employeeWeeklyOffDateList;
		
		if(emplMbPartner.iscompanyweekpolicy()) {
			
			// Employee weekly off policy is similar to the company weekly off policy & holidays
			
			employeeWeeklyOffDateList = getCompanyNonWorkingDayForPeriod(startDate,endDate);
			
		}else {
			
			// Employee has individual weekly off policy
			
			employeeWeeklyOffDateList = getEmployeeNonWorkingDaysForPeriod(emplMbPartner,startDate,endDate);
			
			
		}
		
		return employeeWeeklyOffDateList;
	}

	private static List<Timestamp> getEmployeeNonWorkingDaysForPeriod( MBPartner emplMbPartner, 
																	   Timestamp startDate, 
																	   Timestamp endDate) {
		
		
		List<Timestamp> employeeWeeklyOffDateList = new ArrayList<Timestamp>();
		
		
		if(null == startDate || null == endDate) 
			
			return employeeWeeklyOffDateList;
		
		Timestamp periodStartDate = TimeUtil.getDay(startDate);
		Timestamp periodEndDate = TimeUtil.getDay(endDate);
		
		StringBuffer whereClause = new StringBuffer( I_HR_Employee_WeeklyOff.COLUMNNAME_C_BPartner_ID + " = ?");
		
		
		X_HR_Employee_WeeklyOff employee_WeeklyOff = new Query(Env.getCtx(), I_HR_Employee_WeeklyOff.Table_Name, whereClause.toString(), null)
													 .setParameters(emplMbPartner.getC_BPartner_ID())
													 .setOnlyActiveRecords(Boolean.TRUE)
													 .first();
		
		
		List<Integer> weekDayNoList = getWeekDayNoList(employee_WeeklyOff);
		
			
			while(!periodStartDate.after(periodEndDate)) {

				// Company non business day 
				
				if(MNonBusinessDay.isCompanyHoliday(periodStartDate)) {
				
					employeeWeeklyOffDateList.add(periodStartDate);
				}else {
					
					// date day is week off for employee 
					
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(periodStartDate.getTime());
					
					if(weekDayNoList.contains(cal.get(Calendar.DAY_OF_WEEK))) {
						
						employeeWeeklyOffDateList.add(periodStartDate);
					}
					
				}
				
			
				
			
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(periodStartDate.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
			
				periodStartDate  = new Timestamp(cal.getTimeInMillis());
			
			
			}
			
		return employeeWeeklyOffDateList;
	}

	private static List<Integer> getWeekDayNoList( X_HR_Employee_WeeklyOff employee_WeeklyOff) {
		
		List<Integer> weekDayNoList = new ArrayList<Integer>();
		
		if(null == employee_WeeklyOff)
			
			return weekDayNoList;
		
		if(employee_WeeklyOff.issunday()) {
			
			weekDayNoList.add(Calendar.SUNDAY);
		}
		if(employee_WeeklyOff.ismonday()) {
			
			weekDayNoList.add(Calendar.MONDAY);
		}
		if(employee_WeeklyOff.istuesday()) {
		
			weekDayNoList.add(Calendar.TUESDAY);
		}
		if(employee_WeeklyOff.iswednesday()) {
		
			weekDayNoList.add(Calendar.WEDNESDAY);
		}
		if(employee_WeeklyOff.isthursday()) {
		
			weekDayNoList.add(Calendar.THURSDAY);
		}
		if(employee_WeeklyOff.isfriday()) {
			
			weekDayNoList.add(Calendar.FRIDAY);
		}
		if(employee_WeeklyOff.issaturday()) {
			
			weekDayNoList.add(Calendar.SATURDAY);
		}
		
		return weekDayNoList;
	}

	private static List<Timestamp> getCompanyNonWorkingDayForPeriod( Timestamp startDate, Timestamp endDate) {
		
		List<Timestamp> employeeWeeklyOffDateList = new ArrayList<Timestamp>();
		
		if(null == startDate || null == endDate) 
			
			return employeeWeeklyOffDateList;
		
		Timestamp periodStartDate = TimeUtil.getDay(startDate);
		Timestamp periodEndDate = TimeUtil.getDay(endDate);
		
		while(!periodStartDate.after(periodEndDate)) {

			// Checks for both weekend & non business day i.e. holiday
			
			if(LeaveRequestManager.isNonWorkingDay(periodStartDate)) {
			
				employeeWeeklyOffDateList.add(periodStartDate);
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(periodStartDate.getTime());
			cal.add(Calendar.DAY_OF_YEAR, 1);
			
			periodStartDate  = new Timestamp(cal.getTimeInMillis());
			
		}
		
		return employeeWeeklyOffDateList;
	}

	public static int getWorkingDaysForPeriod(MBPartner bpEmployee, Timestamp startDate,Timestamp endDate) {
		
		int days = TimeUtil.getDaysBetween(startDate, endDate) + 1;
		int holidayCountForPeriod = 0;
		
		List<Timestamp> employeeHolidayListForPeriod = getNonWorkingDayDatesForEmployee(bpEmployee,startDate,endDate);
		
		if(null != employeeHolidayListForPeriod && !employeeHolidayListForPeriod.isEmpty()) {
			
			holidayCountForPeriod = employeeHolidayListForPeriod.size();
		}
		
		return days - holidayCountForPeriod;
		
	}

	public static boolean isEmployeeApplicableForLossOfPay(MBPartner bpEmployee) {
		
		StringBuffer whereClause = new StringBuffer(I_WTC_LeaveType_EmployeeType.COLUMNNAME_HR_Employee_Type_ID + " = ?  AND "); 
									whereClause.append(I_WTC_LeaveType_EmployeeType.COLUMNNAME_HR_LeaveType_ID + " = ? ");
									
		
		
		
		
		int count = new Query(Env.getCtx(), I_WTC_LeaveType_EmployeeType.Table_Name, whereClause.toString(), null)
					.setParameters(bpEmployee.getHR_Employee_Type_ID(),EagleConstants.LOSS_OF_PAY_LEAVE_TYPE)
					.setOnlyActiveRecords(Boolean.TRUE)
					.count();
		
		if(count > 0) {
		
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	 /**
	 * @author Ranjit
	 *             
	 * <P>              
	 *  SQL statment modified as it is not considering the half day <BR>
	 *  & holidays in leave request period. <BR>
	 * </p>         
	 * @return
	 */
   public static double getNoOfEarnedleavesForLeaveType(int  leaveType, Timestamp periodStartDate, Timestamp periodEndDate, int businessPartnerId) {
	   
		double noOfDays = EagleConstants.DOUBLE_ZERO;
		
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT count(lrl.*), lrl." + I_HR_Leave_RequestLine.COLUMNNAME_leavesize );
		sql.append( " FROM " + I_HR_Leave_Request.Table_Name + " lr ");
		sql.append(" JOIN " + I_HR_Leave_RequestLine.Table_Name + " lrl ");
		sql.append(" ON lrl." + I_HR_Leave_RequestLine.COLUMNNAME_HR_Leave_Request_ID + " = " );
		sql.append( " lr." + I_HR_Leave_Request.COLUMNNAME_HR_Leave_Request_ID );
		sql.append(" WHERE lrl." + I_HR_Leave_RequestLine.COLUMNNAME_leavedate);
		sql.append(" >= ? ");																			// 1
		sql.append(" AND lrl." + I_HR_Leave_RequestLine.COLUMNNAME_leavedate);
		sql.append(" <= ? ");																			// 2
		sql.append(" AND lr." + I_HR_Leave_Request.COLUMNNAME_HR_LeaveType_ID + " = ? ");				// 3
		sql.append(" AND lr." + I_HR_Leave_Request.COLUMNNAME_IsApproved + " = 'Y' ");
		sql.append(" AND lr." + I_HR_Leave_Request.COLUMNNAME_isavailed + " = 'Y' ");
		sql.append(" AND lr." + I_HR_Leave_Request.COLUMNNAME_IsActive + " = 'Y' ");
		sql.append(" AND lrl." + I_HR_Leave_RequestLine.COLUMNNAME_IsActive + " = 'Y' ");
		sql.append(" AND lr." + I_HR_Leave_Request.COLUMNNAME_C_BPartner_ID + " = ? ");					// 4
		
		sql.append(" GROUP BY lrl." + I_HR_Leave_RequestLine.COLUMNNAME_leavesize );
		sql.append(" ORDER BY lrl." + I_HR_Leave_RequestLine.COLUMNNAME_leavesize);
		sql.append(" DESC ");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try	{
			
			pstmt = DB.prepareStatement(sql.toString(), null);
			
			DB.setParameters(pstmt, new Object[] { periodStartDate, periodEndDate , leaveType , businessPartnerId });
			
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				int days =  rs.getInt(1);
				double leaveSize = Double.parseDouble(rs.getString(2));
				
				double totalDays = days * leaveSize;
				
				noOfDays = noOfDays + totalDays;
			}
		}
		catch (Exception e)	{
			
			logger.log( Level.SEVERE, 
					   "Failed to get the availed leaves of leave type id - " 
					   + leaveType 
					   + " for employee " 
					   + businessPartnerId 
					   + e.getMessage());
		}
		finally	{
			
			
			try {
				if(rs != null) {
					
					rs.close();
				}
				
				if(pstmt != null) {
					
					pstmt.close();
				}
			} catch (SQLException e) {
				
				logger.log( Level.SEVERE,e.getMessage());
			}
			
			rs = null;
			pstmt = null;
			
		}
		
		return noOfDays;
	}

	
   /**
    * This method includes the attendance on weekly off as well
    * 
    * @param ctx
    * @param businesspartnerEmployee
    * @param period
    * @param generalPayroll
    * @param trxName
    * @return
    */
   public static double getPresentDaysForEmployee( Properties ctx , 
		   										   MBPartner businesspartnerEmployee, 
		   										   MHRPeriod period,
		   										   Timestamp startDate,
		   										   Timestamp endDate,
		   										   boolean generalPayroll, 
		   										   String trxName ) {
	   
	   
	   double presentDays = EagleConstants.DOUBLE_ZERO;
	   
		boolean supervisorAttendance = MSysConfig.getBooleanValue( EagleConstants.USE_ATTENDANCE_BY_SUPERVISOR,Boolean.FALSE);
		
		List<MEmpSupAttendence> employeeAttendanceList = null;

		if (supervisorAttendance) {

			employeeAttendanceList = WTCEmployeeUtil.getEmployeeSupervisorAttendanceList( ctx, 
																						  businesspartnerEmployee.getC_BPartner_ID(), 
																						  period.getHR_Period_ID(), 
																						  trxName);

		} else {

			BigDecimal employeeGateDays = LeaveTypeManager.getEmployeeAttendanceFromGate( businesspartnerEmployee.getC_BPartner_ID(), 
																						  period.getStartDate(), 
																						  period.getEndDate(), 
																						  generalPayroll);
		
			presentDays = employeeGateDays.doubleValue();
			
			return presentDays;
		}

		double days = EagleConstants.DOUBLE_ZERO;
		double halfDays = EagleConstants.DOUBLE_ZERO;
		double halfDayHours = EagleConstants.DOUBLE_ZERO;

		if ( null != employeeAttendanceList ) {

			for (MEmpSupAttendence attendence : employeeAttendanceList) {

				if (!attendence.isadjusthours()) {

					days = days + EagleConstants.DOUBLE_ONE;
				} else {
					
						if (WTCEmployeeUtil.employeeIsStaff(businesspartnerEmployee)) {
							
							days = days + EagleConstants.DOUBLE_ONE;
							
					} else {

						halfDayHours = halfDayHours + attendence.getadjusthours().intValue();
					}
				}
				
				//
				// set processed value of the record to true and save the
				// record
				//
				if (generalPayroll) {

					attendence.setProcessed(Boolean.TRUE);
					attendence.save(trxName);
				}
				
				/* get the employee daily working hours from shift group */

				int dailyWorkingHours = WTCEmployeeUtil.getHoursInShiftGroup(businesspartnerEmployee).intValue();

				if (dailyWorkingHours > 0) {

					halfDays = halfDayHours / dailyWorkingHours;
					
					// presentHours = halfDayHours % dailyWorkingHours;
					
				}
				
				presentDays = days + halfDays;
			}

			
		}else {
			
			presentDays = EagleConstants.DOUBLE_ZERO;
		}
		
		return presentDays;
   }
   
	
}
