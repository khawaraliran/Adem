/**
 * 
 */
package org.eevolution.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;

/**
 * HR Period
 * @author Teo Sarca, www.arhipac.ro
 */
public class MHRPeriod extends X_HR_Period
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: MHRPeriod.java 1009 2012-02-09 09:16:13Z suman $";
	/**
	 * 
	 */
	private static final long serialVersionUID = -7787966459848200539L;
	
	private static CCache<Integer, MHRPeriod> s_cache = new CCache<Integer, MHRPeriod>(Table_Name, 20);
	
	public static MHRPeriod get(Properties ctx, int HR_Period_ID)
	{
		if (HR_Period_ID <= 0)
		{
			return null;
		}
		//
		MHRPeriod period = s_cache.get(HR_Period_ID);
		if (period != null)
		{
			return period;
		}
		// Try Load
		period = new MHRPeriod(ctx, HR_Period_ID, null);
		if (period.get_ID() == HR_Period_ID)
		{
			s_cache.put(HR_Period_ID, period);
		}
		else
		{
			period = null;
		}
		return period;
	}

	public MHRPeriod(Properties ctx, int HR_Period_ID, String trxName)
	{
		super(ctx, HR_Period_ID, trxName);
	}
	public MHRPeriod(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	public static int getNextPeriodId(Properties ctx, int HR_Period_ID, String trxName){
		
		MHRPeriod currentPeriod = MHRPeriod.get(ctx, HR_Period_ID);
		
		int periodNo = currentPeriod.getPeriodNo();
		int yearID = currentPeriod.getHR_Year_ID();
		int payrollID = currentPeriod.getHR_Payroll_ID();
		//
		// Increase the period number for the same year and try to get 
		// the period
		//
		
		String nextperiod = "SELECT HR_Period_ID FROM HR_Period WHERE isactive = 'Y' and HR_Year_ID= "+ yearID + " and periodNo="+ (periodNo+1);
    	int nextperiodID = DB.getSQLValue(trxName,nextperiod);
    	if ( nextperiodID > 0){
    	
    		//
    		// We got the proper period id, then return
    		//
    		return nextperiodID;
    	}
    	//
    	// May be we need to check in next year
    	//
    	else{
    		//
    		// Set the periodno to 1 as we are moving to new year
    		//
    		periodNo = 1;
    		
    		//
    		// Get the next year
    		//TODO -  Need to handle if future periods created by skipping the previous period
    		//
    		String nextYear = "SELECT min(HR_Year_ID) FROM HR_Year WHERE isactive = 'Y' and HR_Year_ID > "+ yearID + " and HR_Payroll_ID="+ payrollID;
        	int nextYearID = DB.getSQLValue(trxName,nextYear);
        	
        	if (nextYearID >0){
        		
        		nextperiod = "SELECT HR_Period_ID FROM HR_Period WHERE isactive = 'Y' and HR_Year_ID= "+ nextYearID + " and periodNo="+ periodNo;
            	nextperiodID = DB.getSQLValue(trxName,nextperiod);
            	if ( nextperiodID > 0){
            	
            		//
            		// We got the proper period id, then return
            		//
            		return nextperiodID;
            	}
        		
        	}
    		
    	}
		
		
	  return 0;
	}
	
    public static int getPreviousPeriodId(Properties ctx, int HR_Period_ID, String trxName){
		
		MHRPeriod currentPeriod = MHRPeriod.get(ctx, HR_Period_ID);
		
		int periodNo = currentPeriod.getPeriodNo();
		int yearID = currentPeriod.getHR_Year_ID();
		int payrollID = currentPeriod.getHR_Payroll_ID();
		//
		// Increase the period number for the same year and try to get 
		// the period
		//
		
		String previousPeriod = "SELECT HR_Period_ID FROM HR_Period WHERE isactive = 'Y' and HR_Year_ID= "+ yearID + " and periodNo="+ (periodNo-1);
    	int previousperiodID = DB.getSQLValue(trxName,previousPeriod);
    	if ( previousperiodID > 0){
    	
    		//
    		// We got the proper period id, then return
    		//
    		return previousperiodID;
    	}
    	//
    	// May be we need to check in next year
    	//
    	else{
    		//
    		// Set the periodno to 1 as we are moving to new year
    		//
    		periodNo = 1;
    		
    		//
    		// Get the next year
    		//
    		String previousYear = "SELECT max(HR_Year_ID) FROM HR_Year WHERE isactive = 'Y' and HR_Year_ID < "+ yearID + " and HR_Payroll_ID="+ payrollID;
        	int previousYearID = DB.getSQLValue(trxName,previousYear);
        	
        	if (previousYearID >0){
        		
        		previousPeriod = "SELECT max(HR_Period_ID) FROM HR_Period WHERE isactive = 'Y' and HR_Year_ID= "+ previousYearID;
        		previousperiodID = DB.getSQLValue(trxName,previousPeriod);
            	if ( previousperiodID > 0){
            	
            		//
            		// We got the proper period id, then return
            		//
            		return previousperiodID;
            	}
        		
        	}
    		
    	}
		
		
	  return 0;
	}
	

}
