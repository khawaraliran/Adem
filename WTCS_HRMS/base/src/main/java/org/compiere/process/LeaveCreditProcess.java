/**
 * 
 */
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MEmployeeType;
import org.compiere.model.MLeaveAssign;
import org.compiere.model.MLeaveType;
import org.compiere.model.MWTCLeaveCreditHistory;
import org.compiere.model.X_HR_LeaveType;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.wtc.util.LeaveType;
import org.wtc.util.LeaveTypeManager;
import org.wtc.util.WTCTimeUtil;

/**
 * 
 * @author Ranjit
 * 
 * <P>
 * 		Leave credit process is used to credit the leaves for the employees <BR> 
 * 		of the company based on the different rules configured at the "Leave Type" <BR>
 * 		tab.
 * </P>
 * <P>
 * 		Process takes the leave credit frequency as input & based on which <BR>
 * 		it will derive the leaves for all the eligible employees & credits the leave <BR><BR>
 * 
 * 		Following are the options available for the leave credit frequency :<BR>

 * 		<LI> Monthly 	</LI>		 
 * 		<LI> Quarterly  </LI>
 * 		<LI> Yearly 	</LI>
 * 
 * 
 * @author phani
 *
 */
public class LeaveCreditProcess extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LeaveCreditProcess.java 1009 2012-02-09 09:16:13Z suman $";

	
	public LeaveCreditProcess() {

	}

	String ChangeType = null;
	
	LeaveType leavetype = null;
	
	
	
	@Override
	protected String doIt() throws Exception {

		
		if(ChangeType != null && ChangeType.equalsIgnoreCase("M")){
			
			processCreditLeaves(WTCTimeUtil.getSystemCurrentTimestamp(),X_HR_LeaveType.REPEATEDTYPE_Monthly);
		}
		else if(ChangeType != null && ChangeType.equalsIgnoreCase("Q"))	{
			
			processCreditLeaves(WTCTimeUtil.getSystemCurrentTimestamp(),X_HR_LeaveType.REPEATEDTYPE_Quarterly);
		}
		else if(ChangeType != null && ChangeType.equalsIgnoreCase("Y"))	{
			
			processCreditLeaves(WTCTimeUtil.getSystemCurrentTimestamp(),X_HR_LeaveType.REPEATEDTYPE_Yearly);
		}
		
		return "Sucess";
	}

	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] params = getParameter();
		
		for(ProcessInfoParameter param : params)
		{
			if(param.getParameterName().equalsIgnoreCase("Change_Type"))
			{
				ChangeType = param.getParameter().toString();
			}
		}
	}

	/**
	 * <P>
	 *  Generic method for preparing the basic data which needs to credit leaves
	 *  for provided <BR>
	 *  frequency i.e. Month,Quarter,Year etc <BR>
	 * </P>
	 * 
	 * @param date				: Timestamp for executing the process
	 * @param repeatedType		: Process input frequency i.e. Month, Quarter or Year
	 */
	private void processCreditLeaves(Timestamp date, String repeatedType) {

		//
		// Get all the leave types available in the system for the repeatedType
		//
		
		List<MLeaveType> leaveTypeList = MLeaveType.getLeaveTypeList(repeatedType);

		if ( null != leaveTypeList && !leaveTypeList.isEmpty() ) {

			for (MLeaveType mleaveType : leaveTypeList) {

				LeaveType leavetype = LeaveTypeManager.getLeaveTypeInstance(mleaveType.getleavetypecode());

				//
				// Get all the employee types who are eligible for this leave type
				//
				
				MEmployeeType[] employeeTypes = leavetype.getEligibleEmployeeType();

				boolean success = Boolean.FALSE;
				
				for (MEmployeeType emptype : employeeTypes) {

					
					//
					// Get all the employees for employee type
					//
					
					List<MBPartner> employeeList = LeaveTypeManager.getEmployeeListForEmployeeType(emptype.getHR_Employee_Type_ID());

					for (MBPartner employee : employeeList) {

						MLeaveAssign empLeaveAssign = LeaveTypeManager.getEmployeeLeaveAssignDetailForLeaveType( employee.getC_BPartner_ID(),
																												 mleaveType.getHR_LeaveType_ID());
						
						if( null != empLeaveAssign) {

							//
							// Based on the credit repetition frequency call the  credit the leave process for employee
							//
						
							if (repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Monthly)) {

								success = creditLeavesForMonth(employee, empLeaveAssign,leavetype, date, get_TrxName());
							
							} else if (repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Quarterly)) {

								success = creditLeavesForQuarter(employee,empLeaveAssign, leavetype, date,get_TrxName());
							
							} else if (repeatedType.equalsIgnoreCase(X_HR_LeaveType.REPEATEDTYPE_Yearly)) {

								success = creditLeavesForYear(employee, empLeaveAssign,leavetype, date, get_TrxName());
							}

							if (success) {
							
								if(false == success) {
									success = true;
								}
							
								log.log( Level.FINE, 
										"Leaves Assign details upated successfully for employee - "
										+ employee.get_ID());
								}else {
							
										log.log( Level.SEVERE, 
												"Leave credit process failed to credit the leave of type - [ " 
												+ mleaveType.get_ID() 
												+ " ] for employee - [ " 
												+ employee.get_ID() 
												+ employee.getName() 
												+ "]");
								}
						}
					}
				}
				
				if(success)
					
				LeaveTypeManager.updateNextRunDateForLeaveType(date, repeatedType, mleaveType);
			}
		}

		if (null != leaveTypeList && leaveTypeList.isEmpty()) {

			String msg = "All leave types are processed for this period";
			addLog(msg);
			log.log(Level.FINE, msg);
		}
	}
	
	
	/**
	 * 
	 * Process the yearly leave type leaves for all the employees 
	 * 
	 * @param employee       : MBPartner - employee
	 * @param empLeaveAssign : MLeaveAssign - Employee leave assign detail
	 * @param leavetype      : LeaveType
	 * @param date           : Timestamp - Process run date
	 * @param trxName        :
	 * @return : TRUE / FALSE
	 */
	private boolean creditLeavesForYear( MBPartner employee,
										 MLeaveAssign empLeaveAssign, 
										 LeaveType leavetype, 
										 Timestamp date,
										 String trxName) {

		boolean success = Boolean.TRUE;

		if (null != empLeaveAssign) {

			//
			// Get the carry forward leaves for the year
			//

			BigDecimal noOfLeavesForCredit = leavetype.yearCarryForward( employee, date );

			//
			// As year process runs once in year, no need to check the year change.
			// Creates the new leave assign detail record with noOfLeavesForCredit
			//
			
			MLeaveAssign newLeaveAssign = LeaveTypeManager.createNewLeaveAssignDetail( employee, 
																					   empLeaveAssign,
																					   noOfLeavesForCredit, 
																					   date, 
																					   get_TrxName());

			if (null != newLeaveAssign) {

				//
				// Get the leaves to carry forward to next year
				//
				
				BigDecimal noOfLeavesForCarryForward = LeaveTypeManager.getCarryForwardedLeavesForLeaveType( leavetype, 
																											 empLeaveAssign,
																											 get_TrxName());

				if (noOfLeavesForCarryForward.compareTo(Env.ZERO) > 0) {

					//
					// Update the same leaves assignee detail for carry forward leaves from previous year
					//
					
					if (LeaveTypeManager.updateLeaveAssignDetailWithNoOfLeavesCarryForward(	noOfLeavesForCarryForward, 
																							newLeaveAssign,
																							get_TrxName())) {
						//
						// Make the previous year inactive
						//
						
						empLeaveAssign.setIsActive(Boolean.FALSE);
						
						if(empLeaveAssign.save()) {
						
							success = Boolean.TRUE;
							log.log( Level.FINE, "New entry for leave assign is creaed towards employee - " + employee.get_ID());
						}
					}
				}
			}
		}
		
		return success;
	}
	
	/**
	 * 
	 * Process leave type which are repeated quarterly for all the employees
	 * 
	 * @param employee			: MBPartner
	 * @param empLeaveAssign	: MLeaveAssign
	 * @param leavetype			: LeaveType
	 * @param date				: Process date
	 * @param trxName			: 
	 * @return					: TRUE / FALSE
	 */
	private boolean  creditLeavesForQuarter( MBPartner employee, 
											 MLeaveAssign empLeaveAssign, 
											 LeaveType leavetype, 
											 Timestamp date, String trxName) {
		
		boolean success = Boolean.TRUE;
		
		//
		// Get the number of leaves for the crediting  to the current quarter 
		//
		
		BigDecimal noOfLeavesForCredit = leavetype.quarterCarryForward(employee, date);
		
		//
		// 1. Check if the year is changing for the date with previous quarter date
		// 2. If changing then create the new record & get the maximum carry forward leaves for new quarter
		// 3. Update the new record & inactive old one
		//
		
		
		boolean newYear  = LeaveTypeManager.checDatekForNewYear( date,X_HR_LeaveType.REPEATEDTYPE_Quarterly);//1
		
		if( newYear ) {
			
			MLeaveAssign newLeaveAssign = LeaveTypeManager.createNewLeaveAssignDetail(employee,empLeaveAssign,noOfLeavesForCredit,date,get_TrxName());//2
			
			if( null != newLeaveAssign) {
				
				BigDecimal noOfLeavesForCarryForward = LeaveTypeManager.getCarryForwardedLeavesForLeaveType(leavetype,empLeaveAssign,get_TrxName()); //3
				
				if(noOfLeavesForCarryForward.compareTo(Env.ZERO) > 0) {
					
					if( LeaveTypeManager.updateLeaveAssignDetailWithNoOfLeavesCarryForward(noOfLeavesForCarryForward,newLeaveAssign,get_TrxName())) {
						
						empLeaveAssign.setIsActive(Boolean.FALSE); //3
						empLeaveAssign.save();
					}
				}
			}
		}else {
			
			//
			// Update the existing record with - noOfLeavesForCredit
			//
			
			empLeaveAssign.setadd_leaves( noOfLeavesForCredit.add(empLeaveAssign.getadd_leaves()));
			empLeaveAssign.setbalance_leaves((noOfLeavesForCredit.add(empLeaveAssign.getbalance_leaves())));
			empLeaveAssign.settotal_leaves((noOfLeavesForCredit.add(empLeaveAssign.gettotal_leaves())));
			
			if (empLeaveAssign.save()) {

				//
				// Creates the history for the employee leave assign entry
				//

				boolean createdHistory = MWTCLeaveCreditHistory.createLeaveCreditHistory( empLeaveAssign,
																						  EagleConstants.LEAVE_CREDIT_PROCESS_REASON,
																						  null,
																						  get_TrxName());

				if (createdHistory) {

					log.log( Level.FINE,
							 " Leave Credit History created for the leave assign id - "
							+ empLeaveAssign.get_ID());
				}
			}
		}
		
		return success;
	}

	/**
	 * <P>
	 * 	Credit leaves for month provided month date <BR>
	 * <P>
	 * 
	 * @param employee			: Employee
	 * @param empLeaveAssign	: MLeaveAssign
	 * @param leavetype			: LeaveType
	 * @param date				: Date
	 * @return					: TRUE if credit successfully else FALSE
	 */
	private boolean creditLeavesForMonth( MBPartner employee, MLeaveAssign empLeaveAssign, LeaveType leavetype, Timestamp date,String trxName ) {
		
		boolean success = Boolean.TRUE;
		
		BigDecimal noOfLeavesForCredit = leavetype.monthCarryForward(employee, date);
		
		//
		// Check if the year is changing for the date with previous month date
		// if changing then create the new record & get the max carry forward leaves for month
		// & update the new record & inactive old one
		//
		
		
		boolean newYear  = LeaveTypeManager.checDatekForNewYear(date,X_HR_LeaveType.REPEATEDTYPE_Monthly);
		
		if( newYear ) {
			
			//
			// 1. Create new employee leave assign entry with  - noOfLeavesForCredit
			// 2. Get the max carry forward leaves to next year
			// 3. Update the newly created record for prev. year carry forward leaves
			
			MLeaveAssign newLeaveAssign = LeaveTypeManager.createNewLeaveAssignDetail(employee,empLeaveAssign,noOfLeavesForCredit,date,trxName);
			
			if( null != newLeaveAssign) {
				
				BigDecimal noOfLeavesForCarryForward = LeaveTypeManager.getCarryForwardedLeavesForLeaveType(leavetype,empLeaveAssign,trxName); 
				
				if(noOfLeavesForCarryForward.compareTo(Env.ZERO) > 0) {
					
					if( LeaveTypeManager.updateLeaveAssignDetailWithNoOfLeavesCarryForward(noOfLeavesForCarryForward,newLeaveAssign,trxName)) {
						
						empLeaveAssign.setIsActive(Boolean.FALSE);
						empLeaveAssign.save();
					}
				}
			}
			
		}else {
			
			//
			// Update the existing record with - noOfLeavesForCredit
			//
			
			empLeaveAssign.setadd_leaves( noOfLeavesForCredit.add(empLeaveAssign.getadd_leaves()));
			empLeaveAssign.setbalance_leaves((noOfLeavesForCredit.add(empLeaveAssign.getbalance_leaves())));
			empLeaveAssign.settotal_leaves((noOfLeavesForCredit.add(empLeaveAssign.gettotal_leaves())));
			
			if (empLeaveAssign.save()) {

				//
				// Creates the history for the employee leave assign entry
				//

				boolean createdHistory = MWTCLeaveCreditHistory.createLeaveCreditHistory( empLeaveAssign,
																						  EagleConstants.LEAVE_CREDIT_PROCESS_REASON,
																						  null,
																						  trxName);

				if (createdHistory) {

					log.log( Level.FINE,
							 " Leave Credit History created for the leave assign id - "
							+ empLeaveAssign.get_ID());
				}
			}
		}
		
		return success;
	}
}
