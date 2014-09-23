package org.wtc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.compiere.model.MPeriod;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.TimeUtil;
import org.eevolution.model.MHRPeriod;

/**
 * 
 * @author Arunkumar
 * Bug- 1628
 * 
 *  ADDED Method : getSystemCurrentTimestamp   to  return The Current Time of the System as Timestamp.
 *  
 *****************************************************************************************************************************
 *@BugNo	@author			@Date			@Description 
 * 1633	    D.Yadagiri Rao	[20111229]		Added 3 Method i.e addMonths to TimeStamp
 * 																getDaysInMonth it return noOf days from a  month 
 * 																getMonthsDiff it return two timeStamps months differance
 *****************************************************************************************************************************
 */
public class WTCTimeUtil {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: WTCTimeUtil.java 1009 2012-02-09 09:16:13Z suman $";
	
	
	private static final  List<Integer> firstQuarter = new ArrayList<Integer>(Arrays.asList(Calendar.JANUARY,Calendar.FEBRUARY,Calendar.MARCH));
	private static final  List<Integer> secondQuarter = new ArrayList<Integer>(Arrays.asList(Calendar.APRIL,Calendar.MAY,Calendar.JUNE));
	private static final  List<Integer> thirdQuarter = new ArrayList<Integer>(Arrays.asList(Calendar.JULY,Calendar.AUGUST,Calendar.SEPTEMBER));
//	private static final  List<Integer> fourthQuarter = new ArrayList<Integer>(Arrays.asList(Calendar.OCTOBER,Calendar.NOVEMBER,Calendar.DECEMBER));
	

	 //
	 // This method returns the Previous months last day time stamp based on the time stamp passed as parameter. 
	 // 

	 public static Timestamp getPreviousMonthLastDay(Timestamp day)
	 {
		 day = TimeUtil.getDay(day);
		 Calendar cal = Calendar.getInstance();
		 cal.setTimeInMillis(day.getTime());
		 
		 if(cal.getActualMinimum(Calendar.MONTH) == cal.get(Calendar.MONTH))
		 {
			 cal.roll(Calendar.YEAR, false);
			 cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		 }
		 else
		 {
			cal.roll(Calendar.MONTH, false);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		 }
		 return new Timestamp(cal.getTimeInMillis());
	 }
	 
	 /**
	  * Gets the period id for the provided date 
	  * 
	  * @param date			: Timestamp
	  * @return				: Period ID
	  */
	 
	public static int getCPeriodId(Timestamp date) {
		
		Timestamp timestamp = TimeUtil.trunc( date, null);
		
		StringBuffer whereClause = new StringBuffer();
		
		whereClause.append("'" + timestamp + "' BETWEEN ")
				   .append(MPeriod.COLUMNNAME_StartDate).append(" AND ")
				   .append(MPeriod.COLUMNNAME_EndDate);
		
		int periodId = new Query( Env.getCtx(), 
					   			  MPeriod.Table_Name,
					   			  whereClause.toString(), 
					   			  null).firstId();
		return periodId;
	}
	 
	/**
	 * 
	 * @param fromdate
	 * @param todate
	 * @param DayOfWeek
	 * @return
	 */
	public static List<Timestamp> getDaysInRange(Timestamp fromdate,Timestamp todate,int DayOfWeek) {
		List<Timestamp> dateList = new ArrayList<Timestamp>();
		
		if(fromdate == null || todate== null)
			return dateList;
		
		if(fromdate.after(todate))
			return dateList;
		
		// To compare final day of the month , We are Taking In To Consideration
		while(! fromdate.after(todate)){
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(fromdate.getTime());
			
			if(cal.get(Calendar.DAY_OF_WEEK) == DayOfWeek){
				
				dateList.add(TimeUtil.getDay(fromdate));
			}
			
			fromdate = TimeUtil.getNextDay(fromdate);
		}
		
		return dateList;
	}
	
	/**
	 * Returns The Year Of The Time stamp Given As In put
	 * @param inputTime
	 * @return
	 */
	 public static int  getYear(Timestamp inputTime) {
			
			long time = inputTime.getTime();
			GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
			cal.setTimeInMillis(time);			
			return cal.get(Calendar.YEAR);		    
	   }
	 
	 /**
	  *  Returns The month Of The Time stamp Given As In put
	  * @param inputTime
	  * @return
	  */
	 public static int  getMonth(Timestamp inputTime) {
			
			long time = inputTime.getTime();
			GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
			cal.setTimeInMillis(time);			
			return cal.get(Calendar.MONTH);		    
	 }
	 
	 /**
	  * Returns Only Time From A Given Input Time stamp 
	  * @param inputTime
	  * @return
	  */
	 public static Timestamp getOnlyTime(Timestamp inputTime) {
			
			long time = inputTime.getTime();
			GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
			cal.setTimeInMillis(time);
			
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.YEAR,0);
			cal.set(Calendar.MONTH,0);
			return new Timestamp (cal.getTimeInMillis());
		    
	   }
	 
	 /**
	  * Return Hours Between These Two Time Stamps
	  * @param start
	  * @param end
	  * @return
	  */
	 public static BigDecimal getHoursBetween (Timestamp start, Timestamp end)
		{
			long startTime = 0;
			if (start == null)
				startTime = System.currentTimeMillis();
			else
				startTime = start.getTime();
			//
			long endTime = 0;
			if (end == null)
				endTime = System.currentTimeMillis();
			else
				endTime = end.getTime();
			Long timeDiff = endTime - startTime;
			//
			// Get Number of Hours from Milli Sec
			Double hours = timeDiff.doubleValue()/(1000*60*60);
			
			BigDecimal noOfHours = new BigDecimal(hours);
			noOfHours = noOfHours.setScale(2, BigDecimal.ROUND_HALF_UP);
			return noOfHours; 
			
		}	//	formatElapsed
	 
	 
		/**
		 * get The Full Time stamp From Time stamp and Date
		 * Here Time stamp does not have any Time only it contain Only Date (get the time means hours and minutes)
		 * Here Date contain Time
		 */
		public static Timestamp getTimestamp(Timestamp timestamp, Date time) {
			Timestamp fullTimestamp = null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(timestamp);
			calendar.set(Calendar.DATE, calendar1.get(Calendar.DATE));
			calendar.set(Calendar.MONTH, calendar1.get(Calendar.MONTH));
			calendar.set(Calendar.YEAR, calendar1.get(Calendar.YEAR));
			calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR));
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
			calendar.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));
			fullTimestamp	=  new Timestamp(calendar.getTimeInMillis());
			return fullTimestamp;
		}
		
		/**
		 * Returns A Time stamp With Given Input Parameters
		 * @param year
		 * @param month
		 * @param day
		 * @param hour
		 * @param minute
		 * @param second
		 * @param millisecond
		 * @return
		 */
		public static Timestamp getTimeStamp(int year, int month, int day, int hour, int minute,
			      int second, int millisecond) {
			    Calendar cal = new GregorianCalendar();
			    cal.set(Calendar.YEAR, year);
			    cal.set(Calendar.MONTH, month);
			    cal.set(Calendar.DATE, day);
			    cal.set(Calendar.HOUR_OF_DAY, hour);
			    cal.set(Calendar.MINUTE, minute);
			    cal.set(Calendar.SECOND, second);
			    cal.set(Calendar.MILLISECOND, millisecond);

			    // now convert GregorianCalendar object to Timestamp object
			    return new Timestamp(cal.getTimeInMillis());
		}
		
		/**
		 * Returns The Day Of The Month
		 * @param year
		 * @param month
		 * @return
		 */
		public static int getDaysInMonth(int year, int month){
			Calendar calendar = Calendar.getInstance();
	        calendar.set(year, month, 1);
	        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		
		/**
		 * return The Current Time of the System as Timestamp.
		 * @return
		 */
		public static Timestamp getSystemCurrentTimestamp() {
			
			Timestamp  currentTimeStamp = new Timestamp(System.currentTimeMillis());

			return currentTimeStamp;
		}
		
		public static int getYearIdForDate(Timestamp timestamp){
			
			StringBuffer sql = new StringBuffer(" SELECT c.c_year_id FROM c_year c");
			sql.append(" JOIN c_period cp ON cp.c_year_id = c.c_year_id ");
			sql.append(" WHERE ? >= startdate AND ?<= enddate ");
			
			
			int yearId  = DB.getSQLValue(null, sql.toString(), new Object[] {timestamp , timestamp});
			
			return yearId;
		}
		public static Timestamp getPreviousDay(Timestamp day)
		 {
				if (day == null)
					day = new Timestamp(System.currentTimeMillis());
				GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
				cal.setTimeInMillis(day.getTime());
				cal.add(Calendar.DAY_OF_YEAR, -1);	//	next
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				return new Timestamp (cal.getTimeInMillis());
		 }
		
	/**
	 * <P>
	 * 	Gets the first day of the provided type in future <BR>
	 * 	Type can be any of the following: <BR>
	 * 
	 * <LI>Month</LI>
	 * <LI>Quarter</LI>
	 * <LI>Year</LI>
	 * 
	 * <blockquote> Example : If date = 1 st January - 2011 & Type = Month (value  = 1)
	 * Result :	First date of the next month from date i.e. - 1 st February 2011
	 * 
	 * @param date	: Date 
	 * @param type	: Period type
	 * @return
	 */
	public static Timestamp firstDayOfProvidedType(Timestamp date, int type) {

		date = TimeUtil.getDay(date);
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTimeInMillis(date.getTime());

		int month = nowCal.get(Calendar.MONTH) + type;
		int year = nowCal.get(Calendar.YEAR);

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return new Timestamp(cal.getTimeInMillis());
	}
		 
		 
		
		 

		 public static Timestamp lastDayOfPreviousYear(Timestamp date) {
			 
			 	date = TimeUtil.getDay(date);
			    Calendar nowCal = Calendar.getInstance();
			    nowCal.setTimeInMillis(date.getTime());
			    
			    int month = nowCal.get(Calendar.MONTH) - 12 ;
			    int year = nowCal.get(Calendar.YEAR);

			    Calendar cal = Calendar.getInstance();
			    cal.clear();
			    cal.set(Calendar.YEAR, year);
			    cal.set(Calendar.MONTH, month);
			    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			    
			    System.out.println( new Timestamp(cal.getTimeInMillis()));
			    
			    return new Timestamp(cal.getTimeInMillis());
			}
		 
		 public static Timestamp firstDayOfPreviousYear(Timestamp date) {
			 
			 	date = TimeUtil.getDay(date);
			    Calendar nowCal = Calendar.getInstance();
			    nowCal.setTimeInMillis(date.getTime());
			    
			    int month = nowCal.get(Calendar.MONTH) - 12 ;
			    int year = nowCal.get(Calendar.YEAR) - 1;

			    Calendar cal = Calendar.getInstance();
			    cal.clear();
			    cal.set(Calendar.YEAR, year);
			    cal.set(Calendar.MONTH, month);
			    cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
			    
			    System.out.println( new Timestamp(cal.getTimeInMillis()));
			    
			    return new Timestamp(cal.getTimeInMillis());
			}
		 
		 
		 
		 private static Timestamp getFirstDateOfQuarter(Timestamp timestamp , int firstMonthOfQuarter ){
				
				Timestamp date = TimeUtil.getDay(timestamp);
			    Calendar nowCal = Calendar.getInstance();
			    nowCal.setTimeInMillis(date.getTime());
			    
			    int year = nowCal.get(Calendar.YEAR);

			    Calendar cal = Calendar.getInstance();
			    cal.clear();
			    cal.set(Calendar.YEAR, year);
			    cal.set(Calendar.MONTH, firstMonthOfQuarter);
			    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
				
				return new Timestamp(cal.getTimeInMillis());
			}
			
			private static Timestamp getLastDateOfQuarter(Timestamp timestamp , int lastMonthOfQuarter ){
				
				Timestamp date = TimeUtil.getDay(timestamp);
			    Calendar nowCal = Calendar.getInstance();
			    nowCal.setTimeInMillis(date.getTime());
			    
			    int year = nowCal.get(Calendar.YEAR);

			    Calendar cal = Calendar.getInstance();
			    cal.clear();
			    cal.set(Calendar.YEAR, year);
			    cal.set(Calendar.MONTH, lastMonthOfQuarter);
			    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				return new Timestamp(cal.getTimeInMillis());
			}
			
			
			public static Timestamp getFirstayOfPreviousQuarter(Timestamp date) {
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
						
				if(firstQuarter.contains(cal.get(Calendar.MONTH))){
					
					return getFirstDateOfQuarter(date,Calendar.JANUARY);
					
				}else if(secondQuarter.contains(cal.get(Calendar.MONTH))){
					
					return getFirstDateOfQuarter(date,Calendar.APRIL);
					
				}else if(thirdQuarter.contains(cal.get(Calendar.MONTH))){
					
					return getFirstDateOfQuarter(date,Calendar.JULY);
					
				}else{
					
					return getFirstDateOfQuarter(date,Calendar.OCTOBER);
				}
			}
			
			
			public static Timestamp getLastDayOfPreviousQuarter(Timestamp date) {
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
						
				if(firstQuarter.contains(cal.get(Calendar.MONTH))){
					
					return getLastDateOfQuarter(date,Calendar.MARCH);
					
				}else if(secondQuarter.contains(cal.get(Calendar.MONTH))){
					
					return getLastDateOfQuarter(date,Calendar.JUNE);
					
				}else if(thirdQuarter.contains(cal.get(Calendar.MONTH))){
					
					return getLastDateOfQuarter(date,Calendar.SEPTEMBER);
					
				}else{
					
					return getLastDateOfQuarter(date,Calendar.DECEMBER);
				}
			}
			
			
			public static Timestamp getImmediateNextTimestamp(Timestamp timestamp) {
				
				Timestamp date = TimeUtil.getDay(timestamp);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
				
				cal.add(Calendar.DAY_OF_YEAR, 1);
				
				return new Timestamp(cal.getTimeInMillis());
			}
			
		 public static BigDecimal getDaysBetween(Timestamp timestamp1,Timestamp timestamp2) {
			 
			 if(null != timestamp1 && null != timestamp2) {
				 
				 if( timestamp1.equals(timestamp2)) {
					 
					 return Env.ONE;
				 }else {
					 
					 int days =  TimeUtil.getDaysBetween(timestamp1, timestamp2);
					 return new BigDecimal(days + 1  );
				 }
				 
				 
			 }else {
				 return  Env.ZERO;
			 }
		 }
		 
		 
	/**
	 * 
	 * <P>
	 * Checks wther the fromDate is after toDate for the leave request <BR>
	 * </P>
	 * 
	 * @param fromDate
	 *            : Date
	 * @param toDate
	 *            : Date
	 * @return : TRUE if fromDate is after the toDate else TRUE
	 */
	public static boolean isFromDateAfterToDate(Date fromDate, Date toDate) {

		if (null == fromDate || null == toDate)

			return Boolean.FALSE;

		if (fromDate.after(toDate)) {

			return Boolean.TRUE;
		} else {

			return Boolean.FALSE;
		}
	}
	
	public static int getNoDaysOfDay(Timestamp fromdate,Timestamp todate,int DayOfWeek){
		int days = 0;
		
		if(DayOfWeek==7)
			DayOfWeek=0;
		
		if(fromdate == null || todate== null)
			return -1;
		
		if(fromdate.after(todate))
			return -2;
		
		while(! fromdate.after(todate)){
			
			if(fromdate.getDay()==DayOfWeek)
				days++;
			
			fromdate = TimeUtil.getNextDay(fromdate);
		}
		
		return days;
	}
	
	 public static boolean isPeriodIntern(MHRPeriod period)
	 {
		 int days = TimeUtil.getDaysBetween(period.getStartDate(), period.getEndDate())+1;
		 if(days >= 28)
			 return false;
		 return true;
	 }
		
	 /**
	  * This Method is used for get the previous month last date
	  * @param day
	  * @return
	  */
	 public static Timestamp getPreviousMonthLasDay(Timestamp day)
	 {
		 day = TimeUtil.getDay(day);
		 Calendar cal = Calendar.getInstance();
		 cal.setTimeInMillis(day.getTime());
		 
		 if(cal.getActualMinimum(Calendar.MONTH) == cal.get(Calendar.MONTH))
		 {
			 cal.roll(Calendar.YEAR, false);
			 cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		 }
		 else
		 {
			cal.roll(Calendar.MONTH, false);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		 }
		 return new Timestamp(cal.getTimeInMillis());
	 }
	 
	 /**
	  * 
	  * @param timestamp
	  * @return	timestamp
	  */
	 public static Timestamp getDate(Timestamp timestamp) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(timestamp.getTime()));
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.AM_PM, Calendar.AM);
			timestamp = new Timestamp(calendar.getTime().getTime());
			return timestamp;
		
		}
	 
	 /**
	  * 
	  *<Li> If you want to add days to a TimeStamp then parameter send as an +Ve
	  *<Li> If you want to subtract days from a TimeStmap then parameter send as an -Ve	
	  * 
	  * @param timestamp
	  * @param days
	  * @return timeStamp
	  */
	 public static Timestamp addDays(Timestamp timestamp, int days) {
		
		 if(timestamp == null)
		 {
			 timestamp	=	getSystemCurrentTimestamp();
		 }
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp.getTime()));
		calendar.add(Calendar.DATE, days);
		timestamp	=	new Timestamp(calendar.getTime().getTime());
		return timestamp;
		
	 }
	 
	 
	 /**
	  * 
	  * Adding no of months to a timeStamp
	  * 
	  * @param timestamp
	  * @param noofMonths
	  * @return timeStamp
	  */
	 public static Timestamp addMonths(Timestamp timestamp, int noofMonths) {
		
		 if(timestamp == null)
		 {
			 timestamp	=	getSystemCurrentTimestamp();
		 }
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timestamp.getTime()));
		calendar.add(Calendar.MONTH, noofMonths);
		timestamp	=	new Timestamp( calendar.getTimeInMillis() );
		return timestamp;
		
	 }
	 
	 /**
	  * Get The Month Deference 
	  * @param from
	  * @param to
	  * @return
	  */
	 public static int getMonthsDifference(Timestamp from, Timestamp to){
		 int noofMonths=0;
		 if(from == null || to == null){
			 return 0;
		 }
		 int fromMonth = getMonth(from);
		 int toMonth = getMonth(to);
		 noofMonths = toMonth- fromMonth;
		 
		 return noofMonths;
	 }
	 
	 /**
	  * Return Days In a month 
	  * 
	  * @param timestamp
	  * @return Days In a month 
	  */
	 public static int getDaysInMonth(Timestamp timestamp) {
		 
		 int daysInMonth = 0;
		 if(timestamp == null){
			 timestamp = getSystemCurrentTimestamp();
		 }
		 
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(new Date(timestamp.getTime()));
		 daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		 
		 
		 return daysInMonth;
	 }

	 
	 /**
	  * Returns POSIX time 
	  * @param day
	  * @return
	  */
	 public static Timestamp getCalendereDefaultDate(Timestamp day) {
		
		 Timestamp inputTime = null;
		 if(day != null) {
			 inputTime = day;
		 }else {
			 inputTime =  getSystemCurrentTimestamp();
		 }
		 
		 GregorianCalendar inputTimeCal = new GregorianCalendar(1970, 00, 01, inputTime.getHours(), inputTime.getMinutes());
		 inputTime=new Timestamp(inputTimeCal.getTimeInMillis());
		 
		 
		 return inputTime;
	 }
	
}
