/**
 * 
 */
package org.wtc.util;

import java.math.BigDecimal;

import org.compiere.model.MLeaveType;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 * @author phani
 *
 */
public abstract class AbstractLeaveType implements LeaveType {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: AbstractLeaveType.java 1009 2012-02-09 09:16:13Z suman $";

	
	protected MLeaveType leaveType = null;
	protected String trxName = null;
	/**
	 * 
	 */
	public AbstractLeaveType(String leaveTypeCode) {
		Trx localTrx = Trx.get("WTCLeaveType"+Math.random(), true);
		trxName = localTrx.getTrxName();
		leaveType = new Query(Env.getCtx(),MLeaveType.Table_Name,MLeaveType.COLUMNNAME_leavetypecode+" = '"+leaveTypeCode+"'",trxName).firstOnly();
	}

	/* (non-Javadoc)
	 * @see org.wtc.util.LeaveType#allowEncashment()
	 */
	public boolean allowEncashment() {
		return leaveType.isallowedencashment();
	}


	public boolean considerAdjustcentHolidaysasLeaves() {
		return leaveType.isconadjholasleave();
	}


	/* (non-Javadoc)
	 * @see org.wtc.util.LeaveType#halfDayAllowed()
	 */
	public boolean halfDayAllowed() {
		return leaveType.ishalfdayleaveallowed();
	}

	/* (non-Javadoc)
	 * @see org.wtc.util.LeaveType#isForPFEmployee()
	 */
	public boolean isForPFEmployee() {
		return leaveType.isforpfemployee();
	}

	/* (non-Javadoc)
	 * @see org.wtc.util.LeaveType#maximumContinuosLeaves()
	 */
	public BigDecimal maximumContinuosLeaves() {
		return leaveType.getmaxcontinousleaves();
	}

	/* (non-Javadoc)
	 * @see org.wtc.util.LeaveType#minimumDaysForEnCashment()
	 */
	public BigDecimal minimumDaysForEnCashment() {
		return leaveType.getmindaysforencashment();
	}


	public int getLeaveTypeId()
	{
		return leaveType.getHR_LeaveType_ID();
	}
	
	public boolean canLeavesForwardedToNextYear() {
		
		return leaveType.isleaves_forward_tonextyear();
	}
	
	public BigDecimal maximumLeavesForwardedToNextYear() {
		
		return leaveType.getmax_leaves_toforward();
	}

}
