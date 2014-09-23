/**
 * 
 */
package org.wtc.util;

/**
 * @author phani
 *
 */
public class LeaveTypeFactory {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LeaveTypeFactory.java 1009 2012-02-09 09:16:13Z suman $";

	

	public static LeaveType getLeaveType(String leaveTypeCode)
	{
		if(leaveTypeCode.equalsIgnoreCase("WTC-CL"))
			return new WTCCLLeaveType(leaveTypeCode);
		else if(leaveTypeCode.equalsIgnoreCase("WTC-EL"))
			return new WTCELLeaveType(leaveTypeCode);
		else if(leaveTypeCode.equalsIgnoreCase("WTC-LPL"))
			return new WTCLPLLeaveType(leaveTypeCode);
		else
			return new GenericLeaveType(leaveTypeCode);
	}
}
