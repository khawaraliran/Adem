package org.wtc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.I_HR_Emp_Gate_Attendence;
import org.compiere.model.I_HR_Emp_Sup_Attendence;
import org.compiere.model.MBPartner;
import org.compiere.model.MEmpGateAttendence;
import org.compiere.model.MEmpSupAttendence;
import org.compiere.model.MEmployeeType;
import org.compiere.model.MLeaveCompensation;
import org.compiere.model.MLeaveRequest;
import org.compiere.model.MOtherDeductions;
import org.compiere.model.MOtherEarnings;
import org.compiere.model.MSalaryChange;
import org.compiere.model.MSysConfig;
import org.compiere.model.Query;
import org.compiere.model.X_HR_Employee_Type;
import org.compiere.model.X_HR_SalaryChange;
import org.compiere.model.X_HR_Shift_Group;
import org.compiere.model.X_HR_Work_Group;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.eevolution.model.MHREmployee;
import org.eevolution.model.MHRMovement;
import org.eevolution.model.MHRPeriod;
import org.eevolution.model.MHRProcess;

/**
 * 
 *@author Giri   @Date Tue July 05 16:40:18 2011    Description :- Added a new method is incentiveCheck() and used 
 * 																	at All incentive and deduction related methods 
 *               @Date Tue July 25 17:31:25 2011    Description : - 
 *                				   1. Modified PresentDays() function	
 *                                 2. Calculating offDay present also
 *                                 3. In case staff come and work on weekly for half day, he will get full day Attendance.
 *                                 4. hours calculated and converted into days based on the  daily Working Hours
 *                                 5. introducing a new function called isStaff() instead of comparing multiple places with employee type
 *                                 6. Changed the Absent Days, Present days, Actual Present Days definitions for half day attendance introducing purpose
 *                                 7. Earned salary calculating based on the present Days and hours
 *                                 8. getHoursInShiftGroup() return type changed to primitive int type
 *         
 *@author  ArunKumar   @Date Tue July 07 16:nextRenewalDate40:18 2011    Description :-GetCurrebntMonthSalary Method   Staff  salary is not Coming Properly
 *                                                          i.e, Staff Salary IS Not Calculate For Present Days
 *                                                          
 *@author Giri		@Date Wed Aug 24 13:21:50 2011		
 *@BugNo: 1059	@Identifier 201108241321  @Description :-
 *											1. Modified at getApprovedCasualLeaves() Method For Calculating Leaves of respective employee
 *				 						2. Existing Sql Query is wrong one scenario is missed i.e
 *										For example payroll ran for April period, one employee can took leaves form march 15th to May 2nd
 *										3. So modified sql Query Now all scenario are covered by using OVERLAPS operator, 
 *										4. And Added one more if condition for overlapping leaves  
 *                                                    
 *
 *                                               
 * @author Giri		@Date Wed Aug 29 17:21:50 2011		
 * @BugNo: 1214	@Identifier 201108291721 @Description :-
 * 										 1. Added one method getPlanWorkShift(employeeId, timestamp)
 * 										 2. get the workshift for the particulart employee in partcular date
 * 
 * @Bug  @author    @ChangeID    	  @Description
 * 1636  Arunkumar  [20111219:10:00]   when you call getPeriodId(), Check whether return value is greater than zero or not.
 * 									   if not return simply return zero,otherwise it will lead to null pointer exception.
 * 
 * Modification history
 * 
 * Task no 		Date				Identification			Author			Change
 * -----------------------------------------------------------------------------------------------
 * 1655			25/12/2011			251220110251			Ranjit			Changed the complete logic to get the weekly off,
 * 																			it includes the non business day of the company.
 * 
 * 1655			25/12/2011			251220110617			Ranjit			getAbsentDays method changed
 * 
 * 1636         30/12/2011          20111230:6:30           Arunkumar       This Method Will Return The Number Of Days Need To Pay 
 * 																			Total Number Of Days - (Number Of Loss Of Pay Leaves)
 * 																			Ex:  For A month 31 days , Company Will Not Pay Only For Loss Of Pay Leaves 
 *      																	And Our Assumption Is To Every Employee Should Create Loss Of Pay Leave If He Absent
 *      																	For the day.
 *      
 * 1854         05/01/2012         [20110105:2:00]          Arunkumar        ADDED A Method : getSalaryOfEmployee   This Method will return the salary Of the Employee
 * 1854         05/01/2012         [20110105:2:00]          Arunkumar        DELETED getSalaryOfEmployee , Because this Functionality is Achived Through The Get Earned salary.
 * 
 * 1655				6/1/2012		201201061252			Ranjit			1. Created method for the net salary 
 * 																			2. Created method for the leave encashment	
 * 																			3. Added the encasement as well in the gross salary
 * 																			4. After arriers calculated setting the variable true foe arrierscalculated field
 * 																			5. getAbsentdays method modified
 * 
 * 1655			7/1/2012		 20120107					Ranjit			When payrollprocess type is general then only setting the arriescalculated = true
 * 
 * 
 * 1636         20120107         20120107                   Arunkumar       Modified getCurrentMonthSalary() To Return Exact salary
 * 1636         20120107         20120107                   Arunkumar       Added A Method Which will get Effective Salary days 
 * 																			getEffectiveSalaryDays(MHRPeriod, boolean, BigDecimal, int)
 */

public class WTCEmployeeUtil {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: WTCEmployeeUtil.java 1009 2012-02-09 09:16:13Z suman $";

	private int mCBPartnerID;
	private int mHRPeriodID;
	private String mHRProcessType;
	private int workingdays;
	private double presenteedWeeklyOffs;
	private double presentdays;
	private double effectiveSalaryDays;
	private int lateComingDays;
	private MBPartner bpEmployee;
	private MHREmployee employeeDetails;
	private MHRPeriod period;
	private Properties ctx;
	private String trxName;
	
	private static final CLogger logger = CLogger.getCLogger(WTCEmployeeUtil.class);

	public WTCEmployeeUtil( Properties ctx, 
							int mCBPartnerID, 
							int mHRPeriodID, 
							String processType ,
							MHREmployee employee,
							String trxName) {
		
		this(ctx,mCBPartnerID,mHRPeriodID,processType,trxName);
		
		if(null == employee) {
			
			employeeDetails = MHREmployee.getActiveEmployee(ctx, mCBPartnerID,trxName);
		}else {
		
			employeeDetails = employee;
		}
	}
	
	public WTCEmployeeUtil( Properties ctx, 
							int mCBPartnerID, 
							int mHRPeriodID, 
							String processType ,
							String trxName) {

		this.mCBPartnerID = mCBPartnerID;
		this.mHRPeriodID  = mHRPeriodID;
		this.mHRProcessType =processType;
		this.ctx = ctx;
		this.trxName = trxName;
		
		bpEmployee = new MBPartner(ctx, mCBPartnerID, trxName);
		period = MHRPeriod.get(ctx, mHRPeriodID);
		workingdays = -1;
		lateComingDays = -1;
		presentdays = -1.0;
		presenteedWeeklyOffs = -1.0;
		effectiveSalaryDays = -1.0;
	}
	
	public boolean checkForThreeMonthEligibility() {
		Timestamp fromdate = WTCTimeUtil.getPreviousMonthLastDay(period.getEndDate());
		fromdate = WTCTimeUtil.getPreviousMonthLastDay(fromdate);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(fromdate.getTime());
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		fromdate.setTime(cal.getTimeInMillis());
		int workdays = TimeUtil.getDaysBetween(fromdate, period.getEndDate());
		int presdays = new Query(Env.getCtx(),I_HR_Emp_Sup_Attendence.Table_Name," "+I_HR_Emp_Sup_Attendence.COLUMNNAME_workdate+"  BETWEEN '"+fromdate +"' AND '"+period.getEndDate()
				+"' AND "+I_HR_Emp_Sup_Attendence.COLUMNNAME_C_BPartner_ID+" = "+mCBPartnerID,trxName).setOnlyActiveRecords(true).count();
		int weeklyOff = getWeeklyOffs();
		
		return ((workdays - weeklyOff) <= presdays);
	}

	/**
	 * 
	 * 251220110251
	 * 
	 * Get the number of weekly off days.
	 * 
	 * This includes the non business day of the company as well
	 * 
	 * @return : Number of weekly off days
	 */
	public int getWeeklyOffs() {

		int offdays = 0;

		if (mHRProcessType.equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_Worksheet)) {

			List<Timestamp> employeeWeeklyOffDateList = PayrollManager.getNonWorkingDayDatesForEmployee( bpEmployee, 
																										 period.getStartDate(), 
																										 TimeUtil.trunc(
																										 WTCTimeUtil.getSystemCurrentTimestamp(), null));
			if (null != employeeWeeklyOffDateList) {

				return employeeWeeklyOffDateList.size();
			}

			return offdays;
		}

		List<Timestamp> employeeWeeklyOffDateList = PayrollManager.getNonWorkingDayDatesForEmployee( bpEmployee,
																									 period.getStartDate(), 
																									 period.getEndDate());
		if (null != employeeWeeklyOffDateList) {

			return employeeWeeklyOffDateList.size();
		}

		return offdays;
	}

	public boolean absentInworkingDay() {
		int workigdays = getWorkingDays();
		double presentdays = getPresentDays(false);
		int weeklyOff = getWeeklyOffs();
		return (workigdays - weeklyOff) > presentdays;
	}

	/**
	 * 
	 * Retrieves the present days of the employee
	 * 
	 * This will return the days which includes weekly offs
	 * and non businessday attendance as well
	 * 
	 * @param generalPayroll
	 *            : TRUE if generalPayroll to set as processed else FALSE
	 * @return : present days of the respective employee
	 */
	public Double getPresentDays(boolean generalPayroll) {

		if (presentdays == -1.0) {
			
			presentdays = PayrollManager.getPresentDaysForEmployee( ctx, 
																	bpEmployee, 
																	period,period.getStartDate(), 
																	period.getEndDate(), 
																	generalPayroll, 
																	trxName);
		}

		return presentdays;
	}
	
	
	/**
	 * <P>
	 * Gets the employee attendance from the supervisor attendance for provided<BR>
	 * employee and period<BR>
	 * </P>
	 *  
	 * @param ctx					: Context
	 * @param businessPartnerId		: c_bpartner_id
	 * @param hrPeriodId			: hr_period_id
	 * @param trxName
	 * @return						: List of the employee attendance list
	 */
	public static  List<MEmpSupAttendence> getEmployeeSupervisorAttendanceList( Properties ctx, 
																		 int businessPartnerId, 
																		 int hrPeriodId, 
																		 String trxName) {

		StringBuffer whereClause = new StringBuffer(I_HR_Emp_Sup_Attendence.COLUMNNAME_C_BPartner_ID + " = ? ");
							 	   whereClause.append(" AND " + I_HR_Emp_Sup_Attendence.COLUMNNAME_HR_Period_ID + "= ?");

		List<MEmpSupAttendence> employeeAttendanceList = new Query( ctx, I_HR_Emp_Sup_Attendence.Table_Name,whereClause.toString(), trxName)
					 									 .setParameters(businessPartnerId,hrPeriodId)
					 									 .setOnlyActiveRecords(Boolean.TRUE)
					 									 .setOrderBy(I_HR_Emp_Sup_Attendence.COLUMNNAME_isadjusthours)
					 									 .list();

		return employeeAttendanceList;
	}

	/**
	 * Get the working days for the period
	 * 
	 * @return	: Number of working days in an period
	 */
	public int getWorkingDays() {

		if (workingdays == -1) {

			 workingdays = TimeUtil.getDaysBetween(period.getStartDate(), period.getEndDate()) + 1;
		}

		return workingdays;
	}

	
	/**
	 * Retrives the late comeing days for the employee
	 * 
	 * @param processIt
	 * 
	 * @return
	 */
    public int getLateCommingDays(boolean processIt){
    	
    	if( lateComingDays == -1 ){
		
    		StringBuffer whereClause = new StringBuffer();
    		
    					 whereClause.append( I_HR_Emp_Gate_Attendence.COLUMNNAME_C_BPartner_ID + " = ? AND "); 
    					 whereClause.append( I_HR_Emp_Gate_Attendence.COLUMNNAME_islatecoming + " = 'Y' ");
    					 whereClause.append(" AND HR_Period_ID = ? ");
		
    		int days =0;
    		List<MEmpGateAttendence> attendences = new Query(ctx,I_HR_Emp_Gate_Attendence.Table_Name,whereClause.toString(),trxName)
    											   .setParameters(mCBPartnerID,mHRPeriodID)
    											   .setOnlyActiveRecords(Boolean.TRUE)
    											   .list();
    	
    		if(null != attendences && !attendences.isEmpty()) {
    				
    			for(MEmpGateAttendence attendence : attendences) {
    				
    				days++;
			
					//
					// set processed value of the record to true and save the record
					//
					if( processIt){
					
						attendence.setProcessed(true);
						attendence.save(trxName);
					}
    			}
    		}
    		
    		lateComingDays = days;
    	}
		
		return lateComingDays;
	}
    public Double getActualPresentedDays(boolean processIt ) {
    	
    	double presentedDays = getPresentDays(processIt) - getPresentedWeeklyOffs(); 
    	return presentedDays;
    }
    
    /**
     * 
     * 201201061252
     * 
     * Get the number of days for which employee was absent for the provided period
     * 
     * @return :Number of absent days
     */
	public Double getAbsentDays(boolean processIt,int businessPartnerId,Timestamp startDate, Timestamp endDate, MHRPeriod period) {
		
		if( businessPartnerId <= 0) {
			
			return EagleConstants.DOUBLE_ZERO;
		}
		
		MBPartner employeeBusinessPartner = new MBPartner(Env.getCtx(),businessPartnerId,null);
		
		// Number of days for the period
		
		double  periodDays = getDaysForPeriod(startDate,endDate);
		
		//
		// If employee wage level is not daily then only fetch the number of 
		// weekly off days else zero
		//
		
		double absentDays 						= EagleConstants.DOUBLE_ZERO;
		double nonBusinnessDaysForEmployee  	= EagleConstants.DOUBLE_ZERO;
		double daysForWeeklyOffsPresent 		= EagleConstants.DOUBLE_ZERO;
		double presentDaysIncludingWeeklyOffs 	= EagleConstants.DOUBLE_ZERO;
		
		presentDaysIncludingWeeklyOffs = PayrollManager.getPresentDaysForEmployee( ctx, 
																				   employeeBusinessPartner, 
																				   period, 
																				   startDate, 
																				   endDate , 
																				   false, 
																				   trxName);
		
		String wageLevel = getEmployeeEmployeeWageLevel(employeeBusinessPartner);
		
		if(null != wageLevel && !wageLevel.trim().isEmpty() && !wageLevel.equalsIgnoreCase(X_HR_Employee_Type.WAGELEVEL_Daily)) {
			
			// 
			// This method will return the list of weekly off dates & company 
			// holidays based on the weekly off configuration
			//
			
			List<Timestamp> nonWorkingDaysDateList = PayrollManager.getNonWorkingDayDatesForEmployee( employeeBusinessPartner, 
																									  startDate, 
																									  endDate);
			
			if(null != nonWorkingDaysDateList) {
				
				nonBusinnessDaysForEmployee = nonWorkingDaysDateList.size();
			}
			
			
			//
			// Get the working days of the non daily wise employee
			//
			
			daysForWeeklyOffsPresent = getPresentedWeeklyOffs();
			
			absentDays = periodDays - ( nonBusinnessDaysForEmployee + (presentDaysIncludingWeeklyOffs - daysForWeeklyOffsPresent));
			
		}else {
			
			absentDays = periodDays - presentDaysIncludingWeeklyOffs;
		}
		
		return absentDays;
	}

//    private MIncentiveConf getIncentiveInfo(){
//    	
//    	if( conf == null ){
//        	int workingdays = getWorkingDays();
//    		double presentdays = getPresentDays(false);
//    		int pDays = (int) Math.ceil(presentdays);
//    		int weeklyOff =Integer.parseInt(employee.getweeklyoff());
//    		int offdays = ESUtil.getNoDaysOfDay(period.getStartDate(),period.getEndDate(), weeklyOff);
//    				
//    		conf = MIncentiveConf.getIncentiveInfo(employee.getHR_Work_Group_ID(), workingdays, offdays, pDays);
//        	
//        	}
//
//		return conf;
//    }
    
	/**
	 * 
	 * Gets the wage leavel of the employee for the provided
	 * business partner
	 * 
	 * 
	 */
	private String getEmployeeEmployeeWageLevel(MBPartner employeeBusinessPartner) {
		
		String wageLevel = "";
		
		int employeeTypeId = employeeBusinessPartner.getHR_Employee_Type_ID();
		
		MEmployeeType employeeType = new MEmployeeType(Env.getCtx(), employeeTypeId, null);
		
		if(null != employeeType) {
			
			wageLevel = employeeType.getwagelevel();
			
			wageLevel = null != wageLevel ? wageLevel : "";
		}
		
		return wageLevel;
	}

	public Double getAttendanceIncentive() {

//		if (incentiveCheck()) {
//			MIncentiveConf conf = getIncentiveInfo();
//
//			if (conf == null) {
//				return new Double(0.0);
//			} else {
//				BigDecimal incentiveamount = conf.getincentiveamt();
//				incentiveamount = incentiveamount.multiply(new BigDecimal(
//						getPresentDays(false)));
//				return incentiveamount.doubleValue();
//			}
//		} else {
//			return ESConstants.DOUBLE_ZERO;
//		}
		
		// TODO - This will be calculated when incentive module done
		return EagleConstants.DOUBLE_ZERO;
	}
    
	public Double getOnTimeIncentive() {

//		if (incentiveCheck()) {
//			MIncentiveConf conf = getIncentiveInfo();
//
//			if (conf == null) {
//				return new Double(0.0);
//			} 
//			else {
//				BigDecimal incentiveamount = conf.getontimeincentive();
//				int pDays = (int) Math.ceil(getPresentDays(false));
//				int onTimeDays = pDays - getLateCommingDays(false);
//				incentiveamount = incentiveamount.multiply(new BigDecimal(onTimeDays));
//				return incentiveamount.doubleValue();
//			}
//		} 
//		else {
//			return ESConstants.DOUBLE_ZERO;
//		}
		
		// TODO - This will be done when the incentive module done
		
		return EagleConstants.DOUBLE_ZERO;

	}

		public Double getOverTimeIncentive() {
	
//			if (incentiveCheck()) {
//				StringBuffer where = new StringBuffer();
//				where.append(MEmpOTSlip.COLUMNNAME_C_BPartner_ID).append(" = ")
//						.append(mCBPartnerID).append(" AND ")
//						.append(MEmpFine.COLUMNNAME_HR_Period_ID).append(" = ")
//						.append(mHRPeriodID);
//	
//				List<MEmpOTSlip> otSlips = new Query(ctx, MEmpOTSlip.Table_Name,
//						where.toString(), trxName).list();
//	
//				BigDecimal hours = Env.ZERO;
//	
//				for (MEmpOTSlip slip : otSlips) {
//					hours = hours.add(slip.getnumber_of_hours_worked());
//	
//				}
//	
//				if (hours.compareTo(Env.ZERO) <= 0) {
//					return new Double(0.0);
//				} else {
//					MIncentiveConf conf = getIncentiveInfo();
//	
//					if (conf == null) {
//						return new Double(0.0);
//					} 
//					else {
//	
//						//
//						// changes made by phani - 04/05/2011
//						// according to the update brs. incentiveamount =
//						// incentiveamount.multiply(new
//						// BigDecimal(getPresentDays(false))).divide(new
//						// BigDecimal(8));
//						// 8 in the above statement should be replaced with value
//						// retrived form hr_work_shift.
//						//
//						// MWorkShift shift = new
//						// Query(ctx,MWorkShift.Table_Name,MWorkShift.COLUMNNAME_HR_Work_Group_ID+" = "+"SELECT HR_Work_Group_ID FROM C_BPartner WHERE C_BPartner_ID ="+mCBPartnerID,trxName).first();
//	
//						MWorkGroup group = new Query(ctx, MWorkGroup.Table_Name,
//									 "" + MWorkGroup.COLUMNNAME_HR_Work_Group_ID
//										+ " = (SELECT HR_Work_Group_ID FROM C_BPartner WHERE C_BPartner_ID ="
//										+ mCBPartnerID + ")", trxName).first();
//						BigDecimal incentiveamount = conf.getovertimeincentive();
//						BigDecimal numberOfHoursInShiftGroup = new BigDecimal(group.getHR_Shift_Group().getnoofhoursthisshift());
//						BigDecimal multiplyFactor = hours.divide(numberOfHoursInShiftGroup, 2, BigDecimal.ROUND_HALF_DOWN);
//						BigDecimal otIncentiveAmt = incentiveamount.multiply(multiplyFactor);
//	
//						return otIncentiveAmt.doubleValue();
//					}
//				}
//			} else {
//				return ESConstants.DOUBLE_ZERO;
//			}
			
			// TODO - This will be done when incentive module done
			
			return EagleConstants.DOUBLE_ZERO;
		}
	
	/**
	 * Get the weekly off for employee in an period on which employee was
	 * present
	 * 
	 * 
	 */
	public Double getPresentedWeeklyOffs() {

		if (presenteedWeeklyOffs == -1.0) {

			presenteedWeeklyOffs = EagleConstants.DOUBLE_ZERO;
			double halfDaysHours = EagleConstants.DOUBLE_ZERO;
			double hours = EagleConstants.DOUBLE_ZERO;
			double hrsAtGateForNonStaff = EagleConstants.DOUBLE_ZERO;
			double daysAtGateForNonStaff = EagleConstants.DOUBLE_ZERO;

			StringBuffer allweekoffs = new StringBuffer();

			// Get the list of weekly offs for the employee
			
			List<Timestamp> employeeWeeklyOffList = PayrollManager.getNonWorkingDayDatesForEmployee( bpEmployee,
					 																				 period.getStartDate(), 
					 																				 period.getEndDate());
			
			allweekoffs = getParsedTimestampString(employeeWeeklyOffList);
			

			if (null != employeeWeeklyOffList && !employeeWeeklyOffList.isEmpty()) {

				boolean supervisorAttendance = MSysConfig.getBooleanValue( EagleConstants.USE_ATTENDANCE_BY_SUPERVISOR,Boolean.FALSE);
				
				List<MEmpSupAttendence> employeeSupervisorAttendanceListForWeeklyOff = null;
				List<MEmpGateAttendence> employeeGateAttendanceForWeeklyOff = null;

				if (supervisorAttendance) {

					employeeSupervisorAttendanceListForWeeklyOff = getEmployeeSupervisorAttendanceList(mCBPartnerID,allweekoffs);
					

					if(null != employeeSupervisorAttendanceListForWeeklyOff) {
						
						if (employeeIsStaff(bpEmployee)) {
						
							presenteedWeeklyOffs = employeeSupervisorAttendanceListForWeeklyOff.size();
						} else {

								for (MEmpSupAttendence att : employeeSupervisorAttendanceListForWeeklyOff) {

									if (!att.isadjusthours()) {
	
										presenteedWeeklyOffs = presenteedWeeklyOffs	+ EagleConstants.DOUBLE_ONE;
										
									} else {
	
										halfDaysHours = halfDaysHours + att.getadjusthours().intValue();
									}
							}
								

							int dailyWorkingHours = getHoursInShiftGroup(bpEmployee).intValue();

							if (dailyWorkingHours > 0 ) {

								hours = halfDaysHours / dailyWorkingHours;
							}
							
							presenteedWeeklyOffs = presenteedWeeklyOffs + hours;
						}
					}else {
						
						presenteedWeeklyOffs = EagleConstants.DOUBLE_ZERO;
					}
					
				} else {
					
					
					// Gate attendance
					
					employeeGateAttendanceForWeeklyOff =  getEmployeeGateAttendanceList(mCBPartnerID,allweekoffs );
					
					if(null != employeeGateAttendanceForWeeklyOff) {
						
						if (employeeIsStaff(bpEmployee)) {
						
							presenteedWeeklyOffs = employeeGateAttendanceForWeeklyOff.size();
						
						}else {
							
							for (MEmpGateAttendence att : employeeGateAttendanceForWeeklyOff) {
								
								hrsAtGateForNonStaff = hrsAtGateForNonStaff + att.getnumberofhours().doubleValue();
							}

							int dailyWorkingHours = getHoursInShiftGroup(bpEmployee).intValue();

							if (dailyWorkingHours > 0 ) {

								daysAtGateForNonStaff = hrsAtGateForNonStaff / dailyWorkingHours;
							}
							
							presenteedWeeklyOffs = presenteedWeeklyOffs + daysAtGateForNonStaff;
						}
					}else {
						
							presenteedWeeklyOffs = EagleConstants.DOUBLE_ZERO;
						}
					}
			}else {
				
				presenteedWeeklyOffs = EagleConstants.DOUBLE_ZERO;
			}
		}
		
		return presenteedWeeklyOffs;
	}

	private List<MEmpGateAttendence> getEmployeeGateAttendanceList(
			int mCBPartnerID2, StringBuffer allweekoffs) {
		
		if(allweekoffs != null ) {
			
			StringBuffer whereClause = new StringBuffer(I_HR_Emp_Gate_Attendence.COLUMNNAME_C_BPartner_ID + " = ? ");
						whereClause.append(" AND " + I_HR_Emp_Gate_Attendence.COLUMNNAME_workdate + " IN ( " + allweekoffs.toString() + " )"); 
			
			
			List<MEmpGateAttendence> employeeGateAttendanceList = new Query(Env.getCtx(),I_HR_Emp_Gate_Attendence.Table_Name,whereClause.toString(),null)
																 .setParameters(mCBPartnerID2)
																 .setOnlyActiveRecords(Boolean.TRUE)
																 .list();
			
			return employeeGateAttendanceList;
		}
		
		return null;
	}

	private List<MEmpSupAttendence> getEmployeeSupervisorAttendanceList(
			int mCBPartnerID2, StringBuffer allweekoffs) {

		if (allweekoffs != null) {

			StringBuffer whereClause = new StringBuffer(I_HR_Emp_Sup_Attendence.COLUMNNAME_C_BPartner_ID + " = ? ");
						whereClause.append(" AND " + I_HR_Emp_Sup_Attendence.COLUMNNAME_workdate + "IN ( " + allweekoffs.toString() + " )"); 
			
			List<MEmpSupAttendence> employeeSupAttendanceList = new Query( Env.getCtx(), 
																		   I_HR_Emp_Sup_Attendence.Table_Name,
																		   allweekoffs.toString(), null)
																.setParameters(mCBPartnerID2)
																.setOnlyActiveRecords(Boolean.TRUE)
																.list();

			return employeeSupAttendanceList;
		}

		return null;
	}

	private StringBuffer getParsedTimestampString( List<Timestamp> employeeWeeklyOffList) {
		
		StringBuffer allweekoffs = new StringBuffer();
		int count = 0;
		
		if(null != employeeWeeklyOffList && !employeeWeeklyOffList.isEmpty()) {
		
			for (Timestamp weekOff : employeeWeeklyOffList) {

					count++;

					allweekoffs.append("'").append(TimeUtil.getDay(weekOff)).append("'");

					if (count < employeeWeeklyOffList.size()) {

						allweekoffs.append(", ");
					}
			}
		}else {
			
			return allweekoffs;
		}
		
		return allweekoffs;
	}

	public Double getWeeklyOffIncentive() {

//		if (incentiveCheck()) {
//			//
//			// First check whether this employee is PF employee, then only he is
//			// eligible for weekly off incentive
//			//
//			if (employeeDetails.ishasoptedpf() == false) {
//				return new Double(0.0);
//			}
//			double days = EagleConstants.DOUBLE_ZERO;
//			days = getPresentedWeeklyOffs();
//
//			if (days > EagleConstants.DOUBLE_ZERO ) {
//				MIncentiveConf conf = getIncentiveInfo();
//
//				if (conf == null) {
//					return ESConstants.DOUBLE_ZERO;
//				} else {
//					BigDecimal incentiveamount = conf.getweeklyoffincentive();
//					incentiveamount = incentiveamount.multiply(new BigDecimal(days));
//					return incentiveamount.doubleValue();
//				}
//			}
//			return ESConstants.DOUBLE_ZERO;
//		} else {
//			return ESConstants.DOUBLE_ZERO;
//		}
		
		// TODO - This will be done when the incentive module done
		
		return EagleConstants.DOUBLE_ZERO;
		
	}
  
  
		public Double getThreeMonthIncentive() {
			
//			if (incentiveCheck()) {
//				boolean eligible = checkForThreeMonthEligibility();
//				if (eligible == false) {
//					return ESConstants.DOUBLE_ZERO;
//				} 
//				else {
//					MIncentiveConf conf = getIncentiveInfo();
//					if (conf == null) {
//						return ESConstants.DOUBLE_ZERO;
//					} 
//					else {
//						BigDecimal incentiveamount = conf.getthreemonthincentive();
//						incentiveamount = incentiveamount.multiply(new BigDecimal(getPresentDays(false)));
//						return incentiveamount.doubleValue();
//					}
//				}
//			} 
//			else {
//				return ESConstants.DOUBLE_ZERO;
//			}
			
			// TODO - This will be done when the incentive module done
			return EagleConstants.DOUBLE_ZERO;
		} 
//  
  
		public Double getLateComingDeduction()	{
			
			if (incentiveCheck()){
				
				int lateComingDays = getLateCommingDays(false);
				int allowedLateDays = MSysConfig.getIntValue(EagleConstants.LATE_COMING_THSHLD_DAYS, EagleConstants.DEFAULT_LATE_COMING_THSHLD_DAYS);
				if (lateComingDays > allowedLateDays) 
				{
					double lateComingCharge = MSysConfig.getDoubleValue(EagleConstants.LATE_COMING_DEDUCTION_AMT, EagleConstants.DEFAULT_LATE_COMING_DEDUCTION_AMT);
					lateComingCharge = lateComingCharge * lateComingDays;
					return lateComingCharge;
				} 
				else {
					
					return EagleConstants.DOUBLE_ZERO;
				}
			}
			return EagleConstants.DOUBLE_ZERO;
		}
  
		public Double getShiftChangeAbsentDeduction() {
	
			if (incentiveCheck()) 
			{
				Timestamp startDate = period.getStartDate();
				Timestamp endDate = period.getEndDate();
	
				int year = WTCTimeUtil.getYear(startDate);
				int month = WTCTimeUtil.getMonth(startDate);
				month = month + 1;
				Timestamp monthFirstDay = TimeUtil.getDay(year, month, 1);
				Timestamp monthTenDay = TimeUtil.getDay(year, month, 10);
				Timestamp monthElevenDay = TimeUtil.getDay(year, month, 11);
				Timestamp monthTwentyDay = TimeUtil.getDay(year, month, 20);
				Timestamp monthTwentyOneDay = TimeUtil.getDay(year, month, 21);
				Timestamp monthLastDay = TimeUtil.getMonthLastDay(startDate);
	
				int count = 0;
				Double amt = MSysConfig.getDoubleValue(
						EagleConstants.SHIFT_CHANGE_ABSENCE_DEDUCTION_AMT,
						EagleConstants.DEFAULT_SHIFT_CHANGE_ABSENCE_DEDUCTION_AMT);
				if (TimeUtil.isValid(startDate, endDate, monthFirstDay)) {
					boolean absent = isEmployeeAbsent(monthFirstDay);
					if (absent == true) {
						count++;
					}
				}
				if (TimeUtil.isValid(startDate, endDate, monthTenDay)) {
					boolean absent = isEmployeeAbsent(monthTenDay);
					if (absent == true) {
						count++;
					}
				}
				if (TimeUtil.isValid(startDate, endDate, monthElevenDay)) {
					boolean absent = isEmployeeAbsent(monthElevenDay);
					if (absent == true) {
						count++;
					}
				}
				if (TimeUtil.isValid(startDate, endDate, monthTwentyDay)) {
					boolean absent = isEmployeeAbsent(monthTwentyDay);
					if (absent == true) {
						count++;
					}
				}
				if (TimeUtil.isValid(startDate, endDate, monthTwentyOneDay)) {
					boolean absent = isEmployeeAbsent(monthTwentyOneDay);
					if (absent == true) {
						count++;
					}
				}
				if (TimeUtil.isValid(startDate, endDate, monthLastDay)) {
					boolean absent = isEmployeeAbsent(monthLastDay);
					if (absent == true) {
						count++;
					}
				}
				return amt * count;
			} 
			else {
				
				return EagleConstants.DOUBLE_ZERO;
			}
		}

	   public boolean isEmployeeAbsent( Timestamp date){

	       String msgSql="SELECT hr_emp_sup_attendence_id  From  hr_emp_sup_attendence WHERE c_bpartner_id="+mCBPartnerID+" and workdate='"+date+"'";

	       int attended = DB.getSQLValue(trxName, msgSql);

	       //
	       // Means he/she did not come on this date
	       //
	       if ( attended > 0){
	           //
	           // Now check whether he has any approved leave
	           //
	           String leaveSql="SELECT hr_leave_request_id  From  hr_leave_request " +
	                   "  WHERE c_bpartner_id="+mCBPartnerID+" and  '"+date+"' >= fromdate AND '"+date+"' <= todate AND "   // 
	                   +MLeaveRequest.COLUMNNAME_IsApproved+" = "+"'Y'";
	           int reqId = DB.getSQLValue(trxName, leaveSql);
	           if(reqId <=0)
	           {
	        	   return false;
	           }

	       }
	       return true;
	   } 

   
		public BigDecimal getGrossSalary(boolean processIt) {
			
			int formula = MSysConfig.getIntValue("GROSS_SALARY_FORMULA", 1);
			BigDecimal sal = Env.ZERO;
			
			Double earnedSalary = getEarnedSalary(processIt);
			
			switch (formula) {
			
			case 1:
				sal = BigDecimal.valueOf(earnedSalary);	
				break;
			case 2:
				
				Double gsal = 	earnedSalary 				
							  + getAttendanceIncentive()
							  + getThreeMonthIncentive() 
							  + getOnTimeIncentive()
							  + getWeeklyOffIncentive() 
							  + getOverTimeIncentive()
							  + getBackLogs() 
							  + getOtherEarnings()
							  + getEmployeeArriers(false)
							  + getLeaveEncasement();	// 201201061252
				
				
				sal = BigDecimal.valueOf(gsal);
				
				break;
			}
			return sal;
	
		}
	   
	   
	    public Double getBackLogs()
	    {
	  		 String whereClause=MOtherEarnings.COLUMNNAME_earningtype+"='"+MOtherEarnings.EARNINGTYPE_BackLogs +"' AND "+  
	  		                    MOtherEarnings.COLUMNNAME_HR_Period_ID +"= "+mHRPeriodID +" AND "+
	  		                    MOtherEarnings.COLUMNNAME_C_BPartner_ID +"= "+mCBPartnerID;
	  		 List<MOtherEarnings>  backlog=new Query(Env.getCtx(), MOtherEarnings.Table_Name, whereClause,trxName).list();
	  		 if(backlog != null)
	  		 {
	  			 BigDecimal TotalAmount=Env.ZERO;
	  			 for(MOtherEarnings rarn :backlog )
	  			 {
	  				 TotalAmount=TotalAmount.add(rarn.getearningamount());
	  				
	  				if((mHRProcessType).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General))
	  		    	{
	  					rarn.setProcessed(true);
	  					rarn.save(trxName);
	  		    	}
	  				
	  			 }
	  			 
	  			 return TotalAmount.doubleValue();
	  		 }
	  	  return new Double(0.0); 
	    }
	    
	    
	    public Double getOtherEarnings()   {
	    	
	  	 String whereClause=MOtherEarnings.COLUMNNAME_earningtype+"='"+MOtherEarnings.EARNINGTYPE_OtherEarnings +"' AND "+ 
	  	                    MOtherEarnings.COLUMNNAME_HR_Period_ID +"= "+mHRPeriodID +" AND "+
	  	                    MOtherEarnings.COLUMNNAME_C_BPartner_ID +"= "+mCBPartnerID;
	  	 List<MOtherEarnings>  otherEarnings=new Query(Env.getCtx(), MOtherEarnings.Table_Name, whereClause, trxName).list();
	  	 if(otherEarnings != null)
	  	 {
	  		 BigDecimal TotalAmount=Env.ZERO;
	  		 for(MOtherEarnings rarn :otherEarnings )
	  		 {
	  			 TotalAmount=TotalAmount.add(rarn.getearningamount());
	  			 
	  			if((mHRProcessType).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
	  				
					rarn.setProcessed(true);
					rarn.save(trxName);
		    	}
	  		 }
	  		 
	  		 return TotalAmount.doubleValue();
	  	 }
	  	 
	  	return new Double(0.0); 
	    }
	    
	    
	    public Double getOtherDeductions()   {
	    	
	  	 String whereClause=MOtherDeductions.COLUMNNAME_HR_Period_ID +"= "+mHRPeriodID +" AND "+ 
	  	                    MOtherDeductions.COLUMNNAME_C_BPartner_ID +"= "+mCBPartnerID;
	  	 List<MOtherDeductions>  deductions=new Query(Env.getCtx(), MOtherDeductions.Table_Name, whereClause, trxName).list();
	  	 
	  	 if(deductions != null) {
	  		 
	  		 BigDecimal TotalAmount=Env.ZERO;
	  		 for(MOtherDeductions rarn :deductions )
	  		 {
	  			 TotalAmount=TotalAmount.add(rarn.getdeductionamount());
	  			 
	  			if((mHRProcessType).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General))
		    	{
					rarn.setProcessed(true);
					rarn.save(trxName);
		    	}
	  		 }
	  		 
	  		 return TotalAmount.doubleValue();
	  	 }
	  	 
	  	  return new Double(0.0); 
	    }
	   
	/**
	 * 
	 * @return
	 */
	public Double getEffectiveSalaryDays(boolean processIt) {
		
		if (effectiveSalaryDays == -1.0) {
			
			double  workingDays = getDaysForPeriod(period.getStartDate(),period.getEndDate());
			
			double absentDays = getAbsentDays(processIt,bpEmployee.getC_BPartner_ID(),period.getStartDate(),period.getEndDate(),period);
			
			effectiveSalaryDays = workingDays - absentDays;
		}
		
		return effectiveSalaryDays;
	}
	   
	   
	public Double getEarnedSalary(boolean processIt) {

		double workingDays = getDaysForPeriod( period.getStartDate(), period.getEndDate());

		double effectiveSalaryDays = getEffectiveSalaryDays(processIt);
		
		BigDecimal sal = Env.ZERO;
		BigDecimal wDays = new BigDecimal(workingDays);
		BigDecimal salaryDays = new BigDecimal(effectiveSalaryDays);

		if (employeeIsStaff(bpEmployee)) {
			
			BigDecimal monthSalary = employeeDetails.getmonthly_salary();
			
			BigDecimal dailySalary = monthSalary.divide(wDays,12,BigDecimal.ROUND_HALF_UP);
			
			sal = (dailySalary.multiply(salaryDays));
			sal= sal.setScale(4, BigDecimal.ROUND_HALF_UP);
			
		} else if (!employeeIsStaff(bpEmployee)) {
			
			BigDecimal wage = employeeDetails.getdaily_salary();
			
			sal = wage.multiply(salaryDays);
		}
		return sal.doubleValue();
	}
   
		 
		/**
		 *  
		 *  Bug -1636
		 * [20111212:11:00]
		 * This method returns the previous Net Salary i.e period of the employee  from the hrmovement i.e from the last payslip generated
		 * for employee of employee type worker this method will return the salary for last 15 days period
		 * employee not for the business partner then return zero
		 *
		 * @param C_BPartner_ID
		 * @param requestDate
		 * @return
		 */
		 
		 public static BigDecimal getPreviousMonthSalary(int C_BPartner_ID,Timestamp requestDate)
		 {
			 
			 if (requestDate == null)
			 {
				 //We Need To Trunk Here In ESutil It is Get Trunk;
				 requestDate= new Timestamp(System.currentTimeMillis());
			 }
			 
			 
			 int periodId = getPeriodId(C_BPartner_ID, requestDate);
			 
			 if(periodId >  0)
			 {
			 
				 MHREmployee employee = new Query(Env.getCtx(), MHREmployee.Table_Name, MHREmployee.COLUMNNAME_C_BPartner_ID+" = "+C_BPartner_ID, null).setOnlyActiveRecords(true).first();
				 
				 if(employee == null) {
					 
					 return Env.ZERO;
				 }
				 
				 int payrollid = PayrollManager.getPayrollId(C_BPartner_ID);
				 /*
				  * Bug no:1015    Anitha     If previousPeriod did Not return proper value
				  * 						  handle that Situation 
				  */
				 
				 int previousPeriod= MHRPeriod.getPreviousPeriodId(Env.getCtx(), periodId, null);
				 
				 if(previousPeriod > 0)
				 {
					 MHRPeriod period = MHRPeriod.get(Env.getCtx(),previousPeriod);
					 if(period != null)
					 {
						
						 int hrProcessID=getHrProcessIDByPayrollProcessType(period, payrollid);
						
						 MHRMovement movement=null; 
						 if(hrProcessID > 0)
						 {
						    String where = MHRMovement.COLUMNNAME_HR_Concept_ID+" ="+EagleConstants.HR_CONCEPT_ID_NET_SALARY  // need to change now in eaglerp NetSalary Concept
						 				+" AND "+MHRMovement.COLUMNNAME_C_BPartner_ID+" = "+C_BPartner_ID+
						 				" AND "+MHRMovement.COLUMNNAME_HR_Process_ID+"="+hrProcessID;
						    movement = new Query(Env.getCtx(),MHRMovement.Table_Name,where,null).first();
						 }
						 
						 return movement != null ? movement.getAmount() : Env.ZERO;
					 }
					 else
					 {
						 return Env.ZERO;
					 }
				 }
				 else
				 {
					 return Env.ZERO;
				 }
			 }
			 else
			 {
				 return Env.ZERO;
			 }//1015
		 }
	   
	   
	   /**
	    * 
	    * @param bPartner
	    * @param NumberOfHoursThisShift
	    * @return
	    */
	   public static  BigDecimal getHourlyRate(MBPartner bPartner)
	   {
		   BigDecimal otAmount=Env.ZERO;
		   
		    int empTypeID=0;
			if(bPartner != null)
			{
			    empTypeID=bPartner.getHR_Employee_Type_ID();
			   
			   /* This Method Will Get The Number OF Hours in A SHift Group Based ON C_BPartner
			    * 
			    *  */
				int hours=getHoursInShiftGroup(bPartner).intValue();
				
				BigDecimal hoursCount = new BigDecimal(hours); 
				
				MHREmployee hremployee = new Query(Env.getCtx(), MHREmployee.Table_Name, MHREmployee.COLUMNNAME_C_BPartner_ID+" = "+bPartner.get_ID(), null).setOnlyActiveRecords(true).first();
				
				if(hremployee == null)
				{
					return Env.ZERO;
				}
				
				if(EagleConstants.EMPLOYEE_TYPE_STAFF == empTypeID)
				{
					//if employee is 'Staff',
					//OT Amount Per hour = Monthly Salary/(30(System Configuration Parameter)*number o.t hours in associated shift group))
	                // Comment I Got From BugZilla
					
					
					BigDecimal monthlySalary = hremployee.getmonthly_salary();
					
					
					BigDecimal dayCount=Env.ONE;
					
					String  num= MSysConfig.getValue("NUMBER_OF_DAYS_IN_A_MONTH");
					if(num != null)
					{
					   dayCount=new BigDecimal(num);
					}
					
					otAmount=monthlySalary.divide((dayCount.multiply(hoursCount)),EagleConstants.SCALE_ON_BIGDECIMAL_DIVISION,BigDecimal.ROUND_HALF_UP);
	
				}
				else if(EagleConstants.EMPLOYEE_TYPE_WORKER == empTypeID)
				{
					//If Employee is Worker
					//OT Amount Per hour = Daily Salary/number o.t hours in associated shift group
					//Comment I Got From BugZilla
					
					BigDecimal dialySal=hremployee.getdaily_salary();
					otAmount=dialySal.divide(hoursCount, EagleConstants.SCALE_ON_BIGDECIMAL_DIVISION , BigDecimal.ROUND_HALF_UP);
				}
			}
		   return otAmount;
	   }
	   
	   
	   //returns PF employee
	   public boolean isPFEmployee(){
		 return  employeeDetails.ishasoptedpf();
	   }
	   
	   /**
	    * If employee have ESI, then it returns true
	    * 
	    * @return boolean value
	    */
	   public boolean isESIEmployee() {
		   return employeeDetails.ishasoptedesi();
	   }
	   
	  
	  
	   
	   
	   /**
	    * it checks Employee type is Worker or Staff
	    * And based on system configuration value it returns true or false
	    * 
	    * @return boolean value
	    */
	   public boolean incentiveCheck() 
	   {
		   if (MSysConfig.getValue(EagleConstants.INCENTIVE_FOR_ALL, "N").equalsIgnoreCase("N")) 
		   {
				if (! employeeIsStaff(bpEmployee)) 
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		   return true;
	   }
	   
	   /**
	    * Department ID of respective Employee
	    * 
	    * @return int value
	    */
	   public int getEmployeeDepartment()
	   {
		   return bpEmployee.getHR_Department_ID(); 
	   }
	   
	   /**
	    * returns Designation ID of respective Employee
	    * 
	    * @return int value
	    */
	   public int getEmployeeDesignation()
	   {
		   return bpEmployee.getHR_Designation_ID();
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
	   public double getNoOfEarnedleavesForLeaveType(int  leaveType) {
		   
			double noOfDays = EagleConstants.DOUBLE_ZERO;
			
			Timestamp periodStartDate= period.getStartDate();
			Timestamp periodEndDate = period.getEndDate();
			
			noOfDays =  PayrollManager.getNoOfEarnedleavesForLeaveType(leaveType,periodStartDate,periodEndDate,mCBPartnerID);
			
			return noOfDays;
			
			
		}
	   
	  
	   
	   
	   /**
	    * This Method Will Return Payroll Process id Based On:
	    * IF There Is Already General Payroll Is Processed Then It Will Return That Process ID
	    * Else
	    * It Will Try For Any Mock Payroll Is Ran For this Period IN this Payroll.
	    * Else
	    * IT will Return 0 Value.
	    * @param period
	    * @param payrollid
	    * @return
	    */
	   public static int  getHrProcessIDByPayrollProcessType(MHRPeriod period,int payrollid)  {
		   
		  int hrProcessID=0;
		  
		  if(period != null) {
			  
			MHRProcess  payProcess=null;
			String generalWhereClause=MHRProcess.COLUMNNAME_HR_Period_ID+"="+period.getHR_Period_ID()+
			                  " AND "+MHRProcess.COLUMNNAME_HR_Payroll_ID+"="+payrollid + 
			                  " AND "+MHRProcess.COLUMNNAME_payrollprocesstype+"='"+
			                  MHRProcess.PAYROLLPROCESSTYPE_General+ "' AND " +
			                  MHRProcess.COLUMNNAME_Processed+"='Y'";
			payProcess=new Query(Env.getCtx(), MHRProcess.Table_Name, generalWhereClause, null).first();
			
			//If there is no General payroll
			if(payProcess == null)
			{
				String mockWhereClause=MHRProcess.COLUMNNAME_HR_Period_ID+"="+period.getHR_Period_ID()+
                               " AND "+MHRProcess.COLUMNNAME_HR_Payroll_ID+"="+payrollid + 
                               " AND "+MHRProcess.COLUMNNAME_payrollprocesstype+"='"+
                               MHRProcess.PAYROLLPROCESSTYPE_Mock+ "' AND "+
 			                   MHRProcess.COLUMNNAME_Processed+"='Y'";
                payProcess=new Query(Env.getCtx(), MHRProcess.Table_Name, mockWhereClause, null).first();
			}
			
			
			if(payProcess != null)
			{
				hrProcessID= payProcess.getHR_Process_ID();
			}
		  } 
		  
		 return hrProcessID;
	   }
	   
	 
	 
	 public static int getPeriodId(int employeeId,Timestamp date){
			
			int payrollId = PayrollManager.getPayrollId(employeeId);
			
			// preapre query to get the periods which belongs to payroll and whose startdate and end is between the parameter date
			Timestamp resultentDate=TimeUtil.trunc(date,null);
			
			StringBuffer where = new StringBuffer();
			where.append(MHRPeriod.COLUMNNAME_HR_Payroll_ID)
			.append(" = ")
			.append(payrollId)
			.append(" AND ")
			.append("'"+resultentDate+"'")
			.append(" >= ")
			.append(MHRPeriod.COLUMNNAME_StartDate).append(" AND ").append("'"+resultentDate+"'").append(" <= ").append(MHRPeriod.COLUMNNAME_EndDate);
			
			int periodId = new Query(Env.getCtx(),MHRPeriod.Table_Name,where.toString(),null).firstId();
			
			//[20111219:10:00]
			if (periodId > 0) {
				return periodId;
			}else {
				return 0;
			}
		}
		
	


		   
	/**
	 * <P>
	 * Checks whether provide employee is staff or not
	 * </P>
	 * 
	 * @param bpEmployee  : MBPartner
	 * @return 			  : TRUE if employee is staff else FALSE
	 */
	public static boolean employeeIsStaff(MBPartner bpEmployee) {

		if (null == bpEmployee)

			return Boolean.FALSE;

		if (bpEmployee.getHR_Employee_Type_ID() == EagleConstants.EMPLOYEE_TYPE_STAFF) {

			return Boolean.TRUE;
		} else {

			return Boolean.FALSE;
		}
	}
		  
		
		/**
		 * Bug- 1628
		 * [20111209:10:00]
		 * Returns The Number Of Hours In A Shift Group
		 * @param bPartner
		 * @return
		 */
		public static  BigDecimal getHoursInShiftGroup(MBPartner bPartner)
		{
			   	BigDecimal hoursCount = Env.ZERO;
			   	
			   	if( null == bPartner) {
			   		
			   		return hoursCount;
			   	}
			   
			    int workGroupID=bPartner.getHR_Work_Group_ID();
			    
			    X_HR_Work_Group workGroup=null;
			    if(workGroupID >0){
			    	workGroup=new X_HR_Work_Group(Env.getCtx(), workGroupID,null);
			    }
			    
			    String whereClause=X_HR_Shift_Group.COLUMNNAME_HR_Shift_Group_ID+"=";
			    
			    if(workGroup != null){
			    	whereClause=whereClause+workGroup.getHR_Shift_Group_ID();
			    }else{
			    	whereClause=whereClause+0;
			    }
			    
			    
			    X_HR_Shift_Group shiftGroup=new Query(Env.getCtx(), X_HR_Shift_Group.Table_Name, whereClause, null).first();
			    
				
				if(shiftGroup != null){
					hoursCount=shiftGroup.getnoofhoursthisshift();
				}
			   
			   return hoursCount;
		   }
		
		
		
		
	
		
		
		 /**
		  * Bug -1636
		  * [20111212:10:30]
		  * 20111230:6:30
		  * Returns The Current Month Salary
		  * @param C_BPartner_ID
		  * @param requestDate
		  * @return
		  */
		 public  BigDecimal getCurrentMonthSalary(int C_BPartner_ID,Timestamp requestDate)
		 {
			 if (requestDate == null) {
				 requestDate= WTCTimeUtil.getSystemCurrentTimestamp();
				 requestDate = TimeUtil.trunc(requestDate, null);
			 }
			 
			 int periodId =getPeriodId(C_BPartner_ID, requestDate);
			 BigDecimal sal = Env.ZERO;
			 MBPartner partner = new MBPartner(Env.getCtx(),C_BPartner_ID,null);
			 MHREmployee employee = new Query(Env.getCtx(), MHREmployee.Table_Name, MHREmployee.COLUMNNAME_C_BPartner_ID+" = "+C_BPartner_ID, null).setOnlyActiveRecords(true).first();
			 
			 if(employee == null) {
				 return sal;
			 }
			 
			 if(periodId > 0 && partner != null) {
				 
				MHRPeriod  period = MHRPeriod.get(Env.getCtx(), periodId);
				if(period !=null) {
					
					sal = getCurrentMonthSalaryofEmployee( period,  partner,  employee);
					
				}else {
					logger.log(Level.SEVERE, "Period Is Not Available For "+ requestDate);
				}
			 }else {
				 logger.log(Level.SEVERE, "Period Is Not Available For "+ requestDate);
			 }
			 
			 return sal;
		 }
		
		 
	/**
	 * [20111230:6:30]
	 * This Method Will Return The Current Month Salary OF The Employee	 
	 * @param period
	 * @param partner
	 * @param employee
	 * @return
	 */
	private  BigDecimal getCurrentMonthSalaryofEmployee(MHRPeriod period, 
															 MBPartner partner, 
															 MHREmployee employee) {
		BigDecimal sal = Env.ZERO;
		
		if(period != null && partner != null && employee != null) {
			//numberofPaidDays = The working days shall be the total number of working days applicable for the employee in a given period
			BigDecimal workingDays = WTCTimeUtil.getDaysBetween(period.getStartDate(), period.getEndDate());
			
			double effectiveSalaryDays = getEffectiveSalaryDays(period , false ,workingDays ,partner.getC_BPartner_ID());
			
			BigDecimal salaryDays = new BigDecimal(effectiveSalaryDays);
	
			if (employeeIsStaff(partner)) {
				
				BigDecimal monthSalary = employee.getmonthly_salary();
				
				BigDecimal dailySalary = monthSalary.divide(workingDays,12,BigDecimal.ROUND_HALF_UP);
				
				sal = (dailySalary.multiply(salaryDays));
				sal= sal.setScale(4, BigDecimal.ROUND_HALF_UP);
				
			} else if (!employeeIsStaff(partner)) {
				
				BigDecimal wage = employee.getdaily_salary();
				
				sal = wage.multiply(salaryDays);
			}
		}
		
		return sal;
	}
		
	
	/**
	 * This Method is Used To Get The Days Count For Which we Have to pay Salary
	 * @return
	 */
	private  Double getEffectiveSalaryDays(MHRPeriod  periodCurrent , 
												boolean processIt,
												BigDecimal workingDays ,
												int bpartnerID) {
	
		double effectiveSalaryDayCount = EagleConstants.DOUBLE_ZERO;
		if(periodCurrent != null) {
		   
		   double absentDays =  getAbsentDays(processIt, bpartnerID ,periodCurrent.getStartDate(),periodCurrent.getEndDate(),periodCurrent);
				
		    effectiveSalaryDayCount = (workingDays.doubleValue()) - absentDays;
		}
			
	   return effectiveSalaryDayCount;
	}
	
	
	/**
	 * [20111230:6:30]
	 * This Method Will Return The Number Of Days Need To Pay 
	 * Total Number Of Days - (Number Of Loss Of Pay Leaves)
	 * Ex:  For A month 31 days , Company Will Not Pay Only For Loss Of Pay Leaves 
	 *      And Our Assumption Is To Every Employee Should Create Loss Of Pay Leave If He Absent
	 *      For the day.
	 * @param period
	 * @param partner
	 * @return
	 */
	public static BigDecimal getActualDaysNeedsToPay(MHRPeriod period,
													 MBPartner partner) {
		//[20111230]
		//See : http://192.168.1.108:3000/projects/eaglerp/boards   (Project Specific Forum)
		
		//numberofPaidDays = The working days shall be the total number of working days applicable for the employee in a given period
		BigDecimal numberofPaidDays = WTCTimeUtil.getDaysBetween(period.getStartDate(), period.getEndDate());
		
		//earnedLeaves = I would recommend business to make Loss of Pay available for all type of employee!
		Double earnedLeaves  = PayrollManager.getNoOfEarnedleavesForLeaveType(EagleConstants.LOSS_OF_PAY_LEAVE_TYPE,period.getStartDate(), period.getEndDate(),partner.getC_BPartner_ID());
		
		if(numberofPaidDays.compareTo(Env.ZERO) == -1 ) {
			numberofPaidDays = Env.ZERO; 
		}
		
		if(earnedLeaves < 0) {
			earnedLeaves = EagleConstants.DOUBLE_ZERO;
		}
		
		BigDecimal actualDaysNeedTopaySal = numberofPaidDays.subtract(new BigDecimal(earnedLeaves));
		
		if(actualDaysNeedTopaySal.compareTo(Env.ZERO) == -1 ) {
			actualDaysNeedTopaySal = Env.ZERO;
		}
		
		return actualDaysNeedTopaySal;
	}


	public int getWorkingDaysForPeriod(Timestamp startDate, Timestamp endDate) {
		
		return PayrollManager.getWorkingDaysForPeriod(bpEmployee,startDate,endDate);
	}
	  
	public int getDaysForPeriod(Timestamp startDate, Timestamp endDate) {
		
		int days = TimeUtil.getDaysBetween(startDate, endDate) + 1;
		
		return days;
	}
	
	public static void updateEmployeeSalaryChange( int m_C_BPartner_ID,
												   MHRPeriod period, 
												   String trxName,boolean processIt) {

		String whereClause = MSalaryChange.COLUMNNAME_salaryupdated
							+ " = ? AND " + MSalaryChange.COLUMNNAME_C_BPartner_ID
							+ " = ? ";

		int salChangeId = new Query(Env.getCtx(), MSalaryChange.Table_Name,whereClause, trxName)
						 .setParameters(Boolean.FALSE, m_C_BPartner_ID)
						 .setOnlyActiveRecords(true).firstId();
		
		X_HR_SalaryChange salChange  = null;
		

		if(salChangeId > 1) {
		
			salChange = new X_HR_SalaryChange(Env.getCtx(),salChangeId, trxName);
			
			Timestamp salChangePeriodStartDate = salChange.getHR_Period().getStartDate();
			
			Timestamp payrollPeriodStartDate = period.getStartDate();
			
			/*
			  Ex - 
			  		1.  Current period start date = 1/12/2011
			     		Sal changer period start date = 1/12/2011 ( Equals condition )
			    		
			    		Process = TRUE
			    		
			    	2. Current period start date = 1/12/2011
			     	   Sal changer period start date = 1/01/2012 ( or greater than this date , > condition )
			    		
			    		Process = FALSE
					
					3. Current period start date = 1/12/2011
			     	   Sal changer period start date = 1/11/2011 (or less than this date , < condiiton ) 
			    		
			    		Process = TRUE
			  
			
			*/
			
			if(salChangePeriodStartDate.before(payrollPeriodStartDate) || salChangePeriodStartDate.equals(payrollPeriodStartDate)) {

				

				MHREmployee employee = new Query( Env.getCtx(), MHREmployee.Table_Name,MHREmployee.COLUMNNAME_C_BPartner_ID + " = " + m_C_BPartner_ID, trxName)
									   .setOnlyActiveRecords(true)
            						   .first();
				
			employee.setmonthly_salary(salChange.getnewsalary());
			employee.setdaily_salary(salChange.getnewdailywage());

			if (employee.save(trxName)) {

				if(processIt) { // 20120107
					
					salChange.setsalaryupdated(Boolean.TRUE);
				}

				//
				// Assumptuion is that if the salary change period & payroll
				// period is same then salary will be updated
				// and no arrriers required so setting the arriers calculated to
				// true, but it doesnt mean that arriers is calculated
				//

				if(salChangePeriodStartDate.equals(payrollPeriodStartDate)) {
					
					salChange.setarrierscalculated(Boolean.TRUE);
				}
				
				salChange.save(trxName);

			} else {

				logger.log( Level.SEVERE,
							"Faild to update the salary change to the hr_employee & hr_salarychange for business partner - "
							+ m_C_BPartner_ID);

			}

		}
		}
	}
	
	public double getEmployeeArriers(boolean processIt) {
		
		return getEmployeeArriers(mCBPartnerID,period ,trxName, processIt);
	}
	
	private  double getEmployeeArriers(int businessPartnerId , MHRPeriod period,String trxName,boolean processIt) {
		
		String where = MSalaryChange.COLUMNNAME_arrierscalculated + " = ? AND " 
					  + MSalaryChange.COLUMNNAME_salaryupdated + " = ? AND " + MSalaryChange.COLUMNNAME_C_BPartner_ID +" = ? ";
    	
    	MSalaryChange salChange = new Query(Env.getCtx(),MSalaryChange.Table_Name,where,trxName)
    							 .setParameters(Boolean.FALSE,Boolean.TRUE,businessPartnerId)
    							 .setOnlyActiveRecords(Boolean.TRUE)
    							 .first();
    	
    	double arriesAmt = EagleConstants.DOUBLE_ZERO;
    	
    	if(salChange != null) 	{
    		
    		
    		double salChangeAmt = getSalChangeAmt(salChange,trxName);
    		
    		Timestamp salChangePeriodStartDate = salChange.getHR_Period().getStartDate();
    		int salChangePeriodId = salChange.getHR_Period_ID();
    		
    			while(salChangePeriodStartDate.before(period.getStartDate())) {
    				
    				arriesAmt = arriesAmt + salChangeAmt;
    				
    				int salChangeperiodId = MHRPeriod.getNextPeriodId(Env.getCtx(), salChangePeriodId, trxName);
    				MHRPeriod newperiod = new MHRPeriod(Env.getCtx(), salChangeperiodId, trxName);
    				salChangePeriodStartDate = newperiod.getStartDate();
    			}
    			
    			// 201201061252
    			
    			if(processIt) {
    				
    				// 20120107
    				salChange.setarrierscalculated(Boolean.TRUE);
    			}
    			
    			salChange.save();
    			
    		}
    	
		return arriesAmt;
		
	}
	
	 
    private static double getSalChangeAmt(MSalaryChange salChange,String trxName)   {
    	
    	MBPartner emp = new MBPartner(Env.getCtx(),salChange.getC_BPartner_ID(),trxName);
    	
    	double changeAmt = EagleConstants.DOUBLE_ZERO;
    	
    	if(emp.getHR_Employee_Type_ID()==EagleConstants.EMPLOYEE_TYPE_STAFF){
    		
    		changeAmt = salChange.getnewsalary().doubleValue() - salChange.getoldsalary().doubleValue();
    	} else if(emp.getHR_Employee_Type_ID()==EagleConstants.EMPLOYEE_TYPE_WORKER) {
    		
    		changeAmt = salChange.getnewdailywage().doubleValue() - salChange.getolddailywage().doubleValue();
    	}
    	
    	return changeAmt;
    }

    
    public double getLeaveDeductionAmount() {

    		double workingDays = getDaysForPeriod( period.getStartDate(), period.getEndDate());

    		double absentDays = getAbsentDays(false,bpEmployee.getC_BPartner_ID(),period.getStartDate(),period.getEndDate(),period);
    		
    		BigDecimal amt = Env.ZERO;
    		BigDecimal wDays = new BigDecimal(workingDays);
    		BigDecimal days = new BigDecimal(absentDays);

    		if (employeeIsStaff(bpEmployee)) {
    			
    			BigDecimal monthSalary = employeeDetails.getmonthly_salary();
    			
    			BigDecimal dailySalary = monthSalary.divide(wDays,BigDecimal.ROUND_HALF_UP, 6);
    			
    			amt = (dailySalary.multiply(days));
    			
    		} else if (!employeeIsStaff(bpEmployee)) {
    			
    			BigDecimal wage = employeeDetails.getdaily_salary();
    			
    			amt = wage.multiply(days);
    		}
    		return amt.doubleValue();
    	}
	   
    /**
     * Calculates the net salary which is to paid for the employee in
     * a period
     * 
     * @return	: net amt
     */
    public Double getNetSalary()  { // 201201061252
    	
		BigDecimal sal = Env.ZERO;
		
		Double earnedSalary = getEarnedSalary(false);
			
		Double gsal = 	earnedSalary 				
						  + getAttendanceIncentive()
						  + getThreeMonthIncentive() 
						  + getOnTimeIncentive()
						  + getWeeklyOffIncentive() 
						  + getOverTimeIncentive()
						  + getBackLogs() 
						  + getOtherEarnings()
						  + getEmployeeArriers(false);
		
		gsal = gsal + getLeaveEncasement();
			
			
		sal = BigDecimal.valueOf(gsal);
			
		
		return sal.doubleValue();
    	
    }
    
    /**
     * Get employees leave encasement amount for the period
     * 
     * @return
     */

	public double getLeaveEncasement() { // 201201061252
		
		String where = MLeaveCompensation.COLUMNNAME_HR_Period_ID+" = "+period.get_ID()+" AND "+
    	MLeaveCompensation.COLUMNNAME_C_BPartner_ID+" = "+mCBPartnerID +" AND "+MLeaveCompensation.COLUMNNAME_IsApproved+" = 'Y'";
    	List<MLeaveCompensation> encash =new Query(Env.getCtx(),MLeaveCompensation.Table_Name,where,trxName).list();
    	
    	BigDecimal amt= Env.ZERO;
    	
    	for(MLeaveCompensation comp : encash) {
    		
    		amt = amt.add(comp.getcompensationamt());
    	}
    	
		return amt.doubleValue();
	}
    
}
