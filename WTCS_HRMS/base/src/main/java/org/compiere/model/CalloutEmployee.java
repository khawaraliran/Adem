/**
 * 
 */
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;

import org.eevolution.model.MHREmployee;

/**
 * @author Alok Ranjan
 * @author Arunkumar   Bug-930   According To Coding Guide Line The Method Name IS Changed (hasOptedPF).
 *
 */
public class CalloutEmployee extends CalloutEngine {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: CalloutEmployee.java 1009 2012-02-09 09:16:13Z suman $";
	
	public void calculateAge(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (value == null || isCalloutActive()) {
			return;
		}
		
		Timestamp timestamp = (Timestamp) value;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp.getTime());
		int currentyear = Calendar.getInstance().get(Calendar.YEAR);
		int birthyear = calendar.get(Calendar.YEAR);
		int currentday = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		int birthday = 365 - calendar.get(Calendar.DAY_OF_YEAR);
		int totaldays = currentday + birthday;
		
		 if(currentyear < birthyear) {
		    mTab.fireDataStatusEEvent("", "Invalid data of birth", false);
		    return ;
		 
		 }
		
		/*if (calendar.after(Calendar.getInstance().getTime())) {
			mTab.fireDataStatusEEvent("", "Invalid data of birth", false);
		}*/
		int age = currentyear - birthyear;
		if (totaldays >= 365)
			age = age + 1;
		mTab.setValue("age", new Integer(age));
		return;
	}
	
	public void calculateInsuranceBalance(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (value == null || isCalloutActive()) {
			return;
		}
		
		double balanceAmount = Double.parseDouble( mTab.get_ValueAsString("coverage_amount") ) - Double.parseDouble(mTab.get_ValueAsString("claimed_amount") );
		
		mTab.setValue("balance_amount", new BigDecimal(balanceAmount) );
		
		return;
	}
	
	public void hasOptedPF(Properties ctx, int WindowNo, GridTab mTab,GridField mField, Object value)
	{
		Boolean hasOpted=(Boolean)mTab.getValue(MHREmployee.COLUMNNAME_hasoptedpf);
		
		if(!hasOpted)
		{
			mTab.setValue(MHREmployee.COLUMNNAME_pfnumber,  "");
			mTab.setValue(MHREmployee.COLUMNNAME_esinumber, "");
			mTab.setValue(MHREmployee.COLUMNNAME_hasoptedesi, false);
		}
	}
	
	
	public void hasOptedESI(Properties ctx, int WindowNo, GridTab mTab,GridField mField, Object value)
	{
		Boolean hasOpted=(Boolean)mTab.getValue(MHREmployee.COLUMNNAME_hasoptedesi);
		if(!hasOpted)
		{
			mTab.setValue(MHREmployee.COLUMNNAME_esinumber, "");
		}
	}
}
