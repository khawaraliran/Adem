package org.wtc.util;

/**
 * 
 * @author Ranjit
 */

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.compiere.model.MBPartner;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.eevolution.model.MHRPeriod;

public class GeneralUtil {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: GeneralUtil.java 1009 2012-02-09 09:16:13Z suman $";
	
	
	/**
	 * 
	 * Returns the BigDecimal value for the provided the object value
	 * 
	 * @param obj	: Object
	 * @return		: BigDecimal
	 */
	
	public static BigDecimal getBigDecimalValue(Object obj) {
		BigDecimal value = new BigDecimal(0);

		if (null == obj) {
			return value;
		}

		if( obj instanceof BigDecimal) {
			value = (BigDecimal)obj;
		}
		else if (obj instanceof Integer) {
			Integer temp = (Integer)obj;
			value = new BigDecimal(temp);
		}
		else if( obj instanceof Double) {
			Double temp = (Double)obj;
			value = new BigDecimal(temp);
		}

		return value;
	}
	
	/**
	 * 
	 * @param employeeId
	 * @param date
	 * @return
	 */
	
	public static int getPeriodId(int employeeId,Timestamp date){
		
		
		int payrollId = getPayrollId(employeeId);
		
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
		
		return periodId;
	}
	
	 public static int getPayrollId(int employeeId) {
		 
		 MBPartner employee = new MBPartner(Env.getCtx(), employeeId, null);
		 
		//check HR_Designation for payroll id
			
			int payrollId = DB.getSQLValue(null, "SELECT HR_Payroll_ID FROM HR_Designation WHERE HR_Designation_ID = ?", employee.getHR_Designation_ID());
			
			// if payroll id doesnt exist for Designation then get payrollid from EmployeeType
			
			if(payrollId == 0)
			{
				payrollId = DB.getSQLValue(null, "SELECT HR_Payroll_ID FROM HR_Employee_Type WHERE HR_Employee_Type_ID = ?", employee.getHR_Employee_Type_ID());
			}
			
			return payrollId;
	 }

	 /**
	  * This Methos Will Return Name Of The Month IN A Message 
	  * @param m_dateFrom
	  * @param employeeTdsName
	  */
	public static String getMonthName(Timestamp m_dateFrom, String employeeTdsName) {
		
		m_dateFrom = TimeUtil.trunc(m_dateFrom, null);
		GregorianCalendar  calender =new GregorianCalendar();
		calender.setTimeInMillis(m_dateFrom.getTime());
		Locale locale = Locale.getDefault();
		String monthName = calender.getDisplayName(Calendar.MONTH, Calendar.LONG,locale);
		int year  = calender.get(Calendar.YEAR);
		monthName = (monthName != null ? (monthName) : "");
		String yearName  = (year > 0 ? ((new Integer(year)).toString()) : "");
		String name = Msg.getMsg(Env.getCtx(), EagleMessageConstants.EMPLOYEE_TDS_NAME,new Object[] {monthName,yearName});
		
		return name;
	}


}
