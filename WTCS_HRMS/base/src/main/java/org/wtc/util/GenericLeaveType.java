/**
 * 
 */
package org.wtc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MBPartner;
import org.compiere.model.MEmployeeType;
import org.compiere.model.MLeaveType;
import org.compiere.model.Query;
import org.compiere.util.Env;

/**
 * @author phani
 *
 */
public class GenericLeaveType extends AbstractLeaveType {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: GenericLeaveType.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	public GenericLeaveType(String leaveTypeCode) {
		super(leaveTypeCode);
	}

	public MLeaveType[] allowedCombinedLeaves() {
		return (MLeaveType[]) new Query(Env.getCtx(),MLeaveType.Table_Name,"",trxName).setOnlyActiveRecords(true).list().toArray();
	}

	public int monthlyEarnedLeaves(int year, int month) {
		return 0;
	}

	public BigDecimal yearCarryForward(MBPartner employee,Timestamp date) {
		
		return LeaveTypeManager.getCreditLeavesForYear(leaveType, employee, date);
	}

	public BigDecimal monthCarryForward(MBPartner employee, Timestamp date) {
		
		return LeaveTypeManager.getCreditLeavesForMonth(leaveType,employee,date);
	}

	public MEmployeeType[] getEligibleEmployeeType() {

		return LeaveTypeManager.getEmployeeTypeForLeaveType(getLeaveTypeId());
	}
	
	public BigDecimal quarterCarryForward(MBPartner employee, Timestamp date) {
		
		return LeaveTypeManager.getCreditLeavesForQuarter(leaveType,employee,date);
	}

}
