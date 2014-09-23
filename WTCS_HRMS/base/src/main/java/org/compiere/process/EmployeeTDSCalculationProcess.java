/**
 * 
 */
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import org.compiere.model.MBPartner;
import org.compiere.model.MTDS;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.eevolution.model.MHREmployee;
import org.eevolution.model.MHRProcess;
import org.wtc.util.EagleMessageConstants;
import org.wtc.util.GeneralUtil;
import org.wtc.util.WTCEmployeeUtil;
import org.wtc.util.WTCTimeUtil;

/**
 * @Bug    @author         @CahngeID               @Description
 *************************************************************************************************************
 * 1854    Arunkumar       [20120103]              Initial check in.
 * 1854    Arunkumar       [20120305]              Added A Method : isTDSExistsForThisEmployee 
 * 1854    Arunkumar       [20120305]              Added A method : prepareEmployeeDetails
 * 1854    Arunkumar       [20120305]			   Added A Method :getEarnedSalaryofEmployee   WHICH GETS Earned salary
 */
public class EmployeeTDSCalculationProcess extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: EmployeeTDSCalculationProcess.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	public EmployeeTDSCalculationProcess() {
		// TODO Auto-generated constructor stub
	}

	List<MBPartner>  businessPartner =  null;
	private int p_C_BPartner_ID = 0;
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] params = getParameter();
		
		for (int i = 0; i < params.length; i++)
		{
			String name = params[i].getParameterName();
			if (params[i].getParameter() == null)
				;
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = ((BigDecimal)params[i].getParameter()).intValue();
		}
		
		prepareEmployeeDetails();
	}

	private void prepareEmployeeDetails() {

		Timestamp currentDate = WTCTimeUtil.getSystemCurrentTimestamp();
		currentDate = TimeUtil.trunc(currentDate, null);
		
		//By This Query WE Get 
		//Business Partner Who is Employee
		//He Is Joined On Or Before The Current Date.
		//He is not Relieved Before This Date
		String whereClause =MBPartner.COLUMNNAME_IsEmployee+"='Y'"+" AND "+
							MBPartner.COLUMNNAME_IsActive+"='Y'"+" AND "+
							MBPartner.COLUMNNAME_joiningdate +"<='"+currentDate+"'"+" AND "+
							"( CASE WHEN "+MBPartner.COLUMNNAME_relievingdate+" IS NULL THEN 1=1 ELSE "+
							MBPartner.COLUMNNAME_relievingdate+">='"+currentDate +"' END )";
		
		if(p_C_BPartner_ID > 0) {
			whereClause = whereClause+" AND "+MBPartner.COLUMNNAME_C_BPartner_ID+" = "+ p_C_BPartner_ID;
		}
		businessPartner = new Query(getCtx(), MBPartner.Table_Name, whereClause, get_TrxName()).list();
		
	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
	  int numberOfTDSEntriesCreated = 0 ;
	  int numberOfSkippedEntries = 0 ;
      if(businessPartner != null && (!  businessPartner.isEmpty())) {
    	  BigDecimal salary = Env.ZERO;
    	  
			for(MBPartner bpartner : businessPartner) {
				if(bpartner != null) {
					int bpartnerID = bpartner.getC_BPartner_ID();
					
					Timestamp m_dateFrom = WTCTimeUtil.getSystemCurrentTimestamp();
					int periodID =  WTCEmployeeUtil.getPeriodId(bpartnerID, m_dateFrom);
					
					salary = getEarnedSalaryofEmployee(bpartnerID,periodID);
					
					String pList =  EagleConstants.EMPLOYEE_TDS_NAME_PAYROLL_TABLE;
					
						 
					//get the Percentage
					Double percentage =  getList(pList, salary.doubleValue() , m_dateFrom, String.valueOf(1));
						
					BigDecimal tdsAmt = salary.multiply(BigDecimal.valueOf(percentage));
					tdsAmt =   tdsAmt.divide(Env.ONEHUNDRED, 5, BigDecimal.ROUND_HALF_UP);
					  
					
					Boolean sucess = createEmployeeTDSEntry(bpartnerID ,tdsAmt,periodID , m_dateFrom );
					if(sucess) {
						 numberOfTDSEntriesCreated++; 
					}else {
						 numberOfSkippedEntries++;
					}
						 
				 
				}else {
					log.log(Level.SEVERE, "Business Partner Does Not Exists");
				}
			}
	   }else {
		   log.log(Level.SEVERE, "There is No Employee");
	   }
		
        String msg = Msg.getMsg(getCtx(), EagleMessageConstants.NUMBER_OF_EMPLOYEE_TDS_ENTRIES,new Object[] {numberOfTDSEntriesCreated,numberOfSkippedEntries});
		return msg;
	}

	
	
	/**
	 * This Method will return the earned salary of the employee
	 * @param bpartner
	 * @param periodID
	 * @return
	 */
	private BigDecimal getEarnedSalaryofEmployee(int bpartner,int periodID) {
		
		Double salary = EagleConstants.DOUBLE_ZERO;
		
		WTCEmployeeUtil  employeeUtil =new WTCEmployeeUtil(getCtx(), 
														   bpartner, 
														   periodID, 
														   MHRProcess.PAYROLLPROCESSTYPE_Mock, 
														   null, 
														   get_TrxName());
		salary =  employeeUtil.getEarnedSalary(false);
		return BigDecimal.valueOf(salary);
	}

	/**
	 * This will Create Employee Tds
	 * @param bpartnerID
	 * @param tDSAmt
	 * @param periodID
	 * @param m_dateFrom
	 */
	private Boolean createEmployeeTDSEntry(int bpartnerID, 
										BigDecimal tDSAmt,
										int periodID, 
										Timestamp m_dateFrom) {
		Boolean success = Boolean.TRUE;
		
		String name = GeneralUtil.getMonthName(m_dateFrom ,EagleMessageConstants.EMPLOYEE_TDS_NAME );
		
		Boolean isTDSExists = isTDSExistsForThisEmployee(bpartnerID,periodID);
		
		
		if( ! isTDSExists) {
			MTDS  tds =new MTDS(getCtx(), 0, get_TrxName());
			tds.setC_BPartner_ID(bpartnerID);
			tds.settdsamount(tDSAmt);
			tds.settdsdate(m_dateFrom);
			tds.setHR_Period_ID(periodID);
			tds.setName(name);
			
			success = tds.save();
			if(! success) {
				log.log(Level.SEVERE, "Employee TDS For Business Partner ID "+bpartnerID+" AND Amount"+tDSAmt+" IS Not Recorded");
			}
		}else {
			success = Boolean.FALSE;
		}
		return success;
	}

	/**
	 * Checks Where TDS Exists For This Month Or Not
	 * @param bpartnerID
	 * @param periodID
	 */
	private Boolean isTDSExistsForThisEmployee(int bpartnerID , int periodID) {
		
		List<Object> perameterList = new ArrayList<Object>();
		perameterList.add(bpartnerID);
		perameterList.add(periodID);
		String whereClause = MTDS.COLUMNNAME_C_BPartner_ID+" = ? "+" AND "+
							 MTDS.COLUMNNAME_HR_Period_ID+" = ?"+" AND "+
							 MTDS.COLUMNNAME_IsActive+"='Y'";
		MTDS tdsPrevious =new Query(getCtx(), 
							MTDS.Table_Name, 
							whereClause, get_TrxName())
							.setParameters(perameterList)
							.first();
		
		if(tdsPrevious == null) {
			return Boolean.FALSE;
		}else {
			return Boolean.TRUE;
		}
	}

	/**
	 * Helper Method : Get Concept [get concept to search key ]
	 * @param pList Value List
	 * @param amount Amount to search
	 * @param column Number of column to return (1.......8)
	 * @return The amount corresponding to the designated column 'column'
	 */
	public double getList (String pList, double amount, Timestamp m_dateFrom ,  String columnParam)
	{

		BigDecimal value = Env.ZERO;
		String column = columnParam;
			column = column.toString().length() == 1 ? "Col_"+column : "Amount"+column;
			ArrayList<Object> params = new ArrayList<Object>();
			String sqlList = "SELECT " +column+
				" FROM HR_List l " +
				"INNER JOIN HR_ListVersion lv ON (lv.HR_List_ID=l.HR_List_ID) " +
				"INNER JOIN HR_ListLine ll ON (ll.HR_ListVersion_ID=lv.HR_ListVersion_ID) " +
				"WHERE l.IsActive='Y' AND lv.IsActive='Y' AND ll.IsActive='Y' AND l.Value = ? AND " +
				"l.AD_Client_ID = ? AND " +
				"(? BETWEEN lv.ValidFrom AND lv.ValidTo ) AND " +
				"(? BETWEEN ll.MinValue AND	ll.MaxValue)";
			params.add(pList);
			params.add(getAD_Client_ID());
			params.add(m_dateFrom);
			params.add(BigDecimal.valueOf(amount));

 			value = DB.getSQLValueBDEx(get_TrxName(),sqlList,params);
		
		//
		if (value == null)
		{
			value = Env.ZERO;
			//throw new IllegalStateException("getList Out of Range");
		}
		return value.doubleValue();
	} // getList

}
