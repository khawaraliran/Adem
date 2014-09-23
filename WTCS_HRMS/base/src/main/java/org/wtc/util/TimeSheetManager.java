/**
 * 
 */
package org.wtc.util;

import java.util.ArrayList;
import java.util.Calendar;
import org.compiere.model.MSysConfig;
import org.compiere.util.EagleConstants;

/**
 * @Bug   @author      @Change ID         @Description
 * ************************************************************************************************************
 * 1630   Arunkumar   [20120101:10:00]    Initial Check In
 * 
 * 1630   Arunkumar   [20120106:10:00]    getDaytoRollBack  As Parivate Method 
 * 
 * 1630   Arunkumar   [20120107:10:00]    From date And To date Values are Setting to Current Time
 */
public class TimeSheetManager {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: TimeSheetManager.java 1098 2012-03-03 05:57:48Z arun $";

	
	/**
	 * [20120101:10:00]
	 * This Method Will Return The List OF From Date And To date Of Period 
	 * 
	 * This Return Null If It does not statisfy any condition
	 * 
	 * @param date
	 */
	public static ArrayList<Calendar> getTimeSheetPeriod ( Calendar current ){
		
		ArrayList<Calendar>  periodDates = new ArrayList<Calendar>();
		
		int noOfDaysInCurrentMonth = current.getActualMaximum(Calendar.DAY_OF_MONTH);
		int timePeriod =0;
		
		//[20111226:10:00]
		String periodType  = MSysConfig.getValue(EagleConstants.TIME_SHEET_PERIOD ,EagleConstants.FORT_NIGHTLY );
		
		if(periodType.equalsIgnoreCase(EagleConstants.FORT_NIGHTLY))  // if time sheet period is bi- weekly  i.e fort nightly
		{
			int day = current.get(Calendar.DAY_OF_MONTH);
			timePeriod = noOfDaysInCurrentMonth/2;
			if(day > timePeriod)											// checking whether this is first half or second half
			{  // second half of the current month
//				/20120107:10:00
				timePeriod = noOfDaysInCurrentMonth-timePeriod;   				    // as this is second half of the month timperiod would be maxdays in month minus first timeperiod
				Calendar fromDate = Calendar.getInstance();
				fromDate.setTimeInMillis(current.getTimeInMillis());
				fromDate.set(Calendar.DAY_OF_MONTH, timePeriod);  					// set from date to the next day of the first half last day
				Calendar toDate = Calendar.getInstance();
				toDate.setTimeInMillis(current.getTimeInMillis());
				toDate.set(Calendar.DAY_OF_MONTH,noOfDaysInCurrentMonth);   		// set todate to the last day of the month
				periodDates.add(0,fromDate);
				periodDates.add(1,toDate);
				return periodDates;
			}
			else
			{ 	// first half of the month
				Calendar fromDate =Calendar.getInstance();  	                     // fromdate assigning to current date
				//20120107:10:00
				fromDate.setTimeInMillis(current.getTimeInMillis());
				fromDate.set(Calendar.DAY_OF_MONTH, current.getActualMinimum(Calendar.DAY_OF_MONTH)); // set fromDate to first day of the current month
				Calendar toDate = Calendar.getInstance(); 
				//20120107:10:00
				toDate.setTimeInMillis(current.getTimeInMillis());
				toDate.set(Calendar.DAY_OF_MONTH,timePeriod);  						// set toDate to the timeperiod
				periodDates.add(0,fromDate);
				periodDates.add(1,toDate); 							// set dates in window
				return periodDates;
			}
			
		}// bi-weekly
		
		else if(periodType.equalsIgnoreCase(EagleConstants.WEEKLY))
		{	// if time period is weekly

			int day =0;
			day = current.get(Calendar.DAY_OF_WEEK);								// frist day of the current will be the from date fo the time sheet
			Calendar fromDate =Calendar.getInstance();  
			//20120107:10:00
			fromDate.setTimeInMillis(current.getTimeInMillis());// fromdate assigning to current date
			fromDate.roll(Calendar.DATE, getDaytoRollBack(day));								// set fromDate to first day of the current week
			Calendar toDate = Calendar.getInstance();    
			//20120107:10:00
			toDate.setTimeInMillis(current.getTimeInMillis());
			toDate.set(Calendar.DAY_OF_MONTH,((fromDate.get(Calendar.DAY_OF_MONTH))+6));  							// set toDate to the last day of the current week
			periodDates.add(0,fromDate);
			periodDates.add(1,toDate); 								// set dates in window
			return periodDates;
		}// weekly
		
		else if(periodType.equalsIgnoreCase(EagleConstants.DAILY))
		{  // time period id daily
			
			Calendar fromDate =Calendar.getInstance();  							// fromdate assigning to current date , set fromDate to current date as time period is daily
			//20120107:10:00
			fromDate.setTimeInMillis(current.getTimeInMillis());
			Calendar toDate = Calendar.getInstance();								// toDate will be the current date only as time period is daily
			//20120107:10:00
			toDate.setTimeInMillis(current.getTimeInMillis());
			periodDates.add(0,fromDate);
			periodDates.add(1,toDate);  							                // set dates in window			
			return periodDates;
		}// daily
		
		else if(periodType.equalsIgnoreCase(EagleConstants.MONTHLY))
		{  // time period id monthly
			
			Calendar fromDate =Calendar.getInstance();
			//20120107:10:00
			fromDate.setTimeInMillis(current.getTimeInMillis());
			fromDate.set(Calendar.DAY_OF_MONTH, 1);									// fromdate assigning to current date , set fromDate to first day of the current month			
			Calendar toDate = Calendar.getInstance();
			//20120107:10:00
			toDate.setTimeInMillis(current.getTimeInMillis());
			toDate.set(Calendar.DAY_OF_MONTH, current.getActualMaximum(Calendar.DAY_OF_MONTH));    // toDate will be the last day of current  month    			
			periodDates.add(0,fromDate);
			periodDates.add(1,toDate); 								// set dates in window			
			return periodDates;
		}// daily
		return periodDates;
	}
	
	
	/**
	 * [20120101:10:00]
	 * Sunday -0  to   Saturday-7  On Week  .
	 * 
	 *    If It is 7  we should Return a Negative 5  to Roll back 5 days In The Week
	 *    Which gives Number of days to roll back 
	 * @param currentday
	 * @return
	 */
	private static int getDaytoRollBack(int currentday)
	{  // based on current day no of this method will return how many todays should rolled back to get the current weeks start date
		int day =0;
		switch(currentday){
		case 1:
			day = 6;
			break;
		case 2:
			day=0;
			break;
		case 3:
			day=1;
			break;
		case 4:
			day=2;
			break;
		case 5:
			day=3;
			break;
		case 6:
			day=4;
			break;
		case 7:
			day=5;
			break;
		}
		return -day;
	}

}
