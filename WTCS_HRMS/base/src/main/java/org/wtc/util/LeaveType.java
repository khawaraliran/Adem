/**
 * 
 */
package org.wtc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MBPartner;
import org.compiere.model.MEmployeeType;
import org.compiere.model.MLeaveType;

/**
 * @author phani
 *
 *  Modifications 
 *-------------------------
 *
 *  No 		Date 		  Bug NO 					Change				  						Author
 *----------------------------------------------------------------------------------------------------  
 *              									Added new methods -
 *  												canLeavesForwardedToNextYear &
 *	1		6/12/2011	  1631						maximumLeavesForwardedToNextYear			Ranjit  												
 *  2       7/12/2011     1631                      Added new method - quarterCarryForward		Ranjit
 *
 *
 *
 */
public interface LeaveType {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LeaveType.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * The implementation class of this interface will contains a code to query the employee 
	 * type's who are eligible to avail this leave type.
	 * returns the array of Employee types 
	 * @return MEmployeeType[]
	 */
	public MEmployeeType[] getEligibleEmployeeType();
	
	/**
	 * This method retrives the Y/N value form the HR_LeaveType table and return 
	 * true/false respectively
	 * @return bolean
	 */
	public boolean isForPFEmployee();
	
	/**
	 * Calculate's the no of leaves which can carry forward for the next month.
	 * @param MEmployee employee
	 * @param Timestamp date of the month for which carry forwarded should be calculated
	 * @return no of leaves can be carry forwarded for next month
	 */
	
	public BigDecimal monthCarryForward(MBPartner employee, Timestamp date);
	
	/**
	 * Calculate's the no of leaves which can carry forward for the next year.
	 * @param MEmployee employee 
	 * @param Timestamp date
	 * @return no of leaves can be carry forwared for next year
	 */
	
	public BigDecimal yearCarryForward(MBPartner employee,Timestamp date);
	
	
	/**
	 * Calculate's the no of continous leaves allowed of the specific leave type
	 * according to the company leave policy
	 * @return no of continous leaves allowed
	 */

	public BigDecimal maximumContinuosLeaves();
	
	
	/**
	 * Calculate's the no of continous leaves allowed of the specific leave type
	 * according to the company leave policy
	 * @return no of continous leaves allowed
	 */
	
	public boolean considerAdjustcentHolidaysasLeaves();
	
	/**
	 * verifies the specific leave type can allow half day leaves or not
	 * @return no of continous leaves allowed
	 */
	public boolean halfDayAllowed();
	
	
	/**
	 * calculates the no of earned leaves in the Month param montha of the year param Year
	 * @param year
	 * @param month
	 * @return no of earne leaves as int
	 */
	public int monthlyEarnedLeaves(int year, int month);
	
	
	/**
	 * verifies all the leave type on which combination this specific leave type is allowed 
	 * according to the company leave policy
	 * @return Array of MLeaveType - combination allowed leavetype's
	 */
	
	public MLeaveType[] allowedCombinedLeaves();

	/**
	 * verifies leave Encashment is allowed or not returns true/false respectively 
	 * according to the company leave policy
	 * @return boolean
	 */

	public boolean allowEncashment();
	
	/**
	 * Returns the no of minimum days to Encash the leaves
	 * according to the company leave policy
	 * @return int
	 */
	public BigDecimal minimumDaysForEnCashment();
	
	/**
	 * returns the id of the leave type 
	 * @return id
	 */
	public int getLeaveTypeId();
	
	/**
	 * <P>
	 *   If leaves for leave type can be forwarded to next year then <BR>
	 *   TRUE else FALSE <BR> 
	 * </P>
	 * 
	 * @return TRUE / FALSE
	 */
	public boolean canLeavesForwardedToNextYear();
	
	/**
	 * <P>
	 *   If leaves for leave type can be carry forward to next year then return the <BR> 
	 *   maximum number of leaves to carry forward to next year <BR>
	 * 
	 *  </P>
	 * @return  : No of leaves which can be carry forwarded to next year
	 */
	public BigDecimal maximumLeavesForwardedToNextYear();
	
	/**
	 * Number of leaves which can be forwarded to next quarter
	 * 
	 * @param employee	: MBPartner
	 * @param date		: Timestamp
	 * @return			: No of leaves to credit quarterly
	 */
	public BigDecimal quarterCarryForward(MBPartner employee, Timestamp date);
	
}
