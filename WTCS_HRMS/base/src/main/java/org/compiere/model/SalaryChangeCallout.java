/**
 * 
 */
package org.compiere.model;

import java.util.Properties;

import org.eevolution.model.MHREmployee;


public class SalaryChangeCallout extends CalloutEngine {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: SalaryChangeCallout.java 1009 2012-02-09 09:16:13Z suman $";

	
	public void updateOldSalary(Properties ctx, int WindowNo, GridTab mTab,GridField mField, Object value)
	{
		if(value == null || isCalloutActive())
			return;
		
		MHREmployee employee = new Query(ctx,MHREmployee.Table_Name,MHREmployee.COLUMNNAME_C_BPartner_ID+" = "
				+Integer.parseInt(value.toString()),null)
				.setOnlyActiveRecords(true)
				.first();
		if (null == employee)
			
			return;
		
		mTab.setValue(I_HR_SalaryChange.COLUMNNAME_oldsalary, employee.getmonthly_salary());
		mTab.setValue(I_HR_SalaryChange.COLUMNNAME_olddailywage, employee.getdaily_salary());
	}
}
