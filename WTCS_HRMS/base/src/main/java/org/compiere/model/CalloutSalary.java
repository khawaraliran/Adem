/**
 * 
 */
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;

import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.eevolution.model.MHRProcess;
import org.wtc.util.EagleMessageConstants;
import org.wtc.util.GeneralUtil;
import org.wtc.util.WTCEmployeeUtil;


/**
 *  @Bug     @author       @ChangeID   			@Description    
 *  ------   -------       ---------   			------------
 *  1638     Arunkumar                 			Initial Check in
 *  1638     Arunkumar     [20120103:4:00PM]    ADDED A METHOD :validateNumberOfInstallments(Properties, int, GridTab, GridField, Object)
 *                                                 This Method is used to Validate the Requested Number Of installments and Approved Installments
 *	1638     Arunkumar     [20120107:12:00]     ADDED A METHOD :validateEMIAmountAndActualEMI(Properties, int, GridTab, GridField, Object)
 *												   This Method is used to check where Actula Emi Amount is Grater Than the Given Emi Amount.
 *  1903   Arunkumar  [20120109:6:00]     Modified :  current month salary functionality to get 2 decimal precision 		
 */
public class CalloutSalary extends CalloutEngine {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: CalloutSalary.java 1009 2012-02-09 09:16:13Z suman $";
	
	
	public void updateEMIAmount(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (value == null || isCalloutActive()) {
			return;
		}
		
		BigDecimal emiAmount = null;
		
		BigDecimal approvedAmt = new BigDecimal(mTab.get_ValueAsString(MSalAdvReq.COLUMNNAME_approvedamt));
		
		if ( approvedAmt != null && !approvedAmt.equals(BigDecimal.ZERO) ) {
			mTab.setValue(MSalAdvReq.COLUMNNAME_advancegivenamt, approvedAmt);
			
			BigDecimal numberOfMonths = new BigDecimal(mTab.get_ValueAsString(MSalAdvReq.COLUMNNAME_noofinstalments));
			
			
			if ( numberOfMonths != null && !numberOfMonths.equals(BigDecimal.ZERO) ) {
				emiAmount = approvedAmt.divide( numberOfMonths ,2,BigDecimal.ROUND_HALF_UP);
			}
		}
		
		
		if ( emiAmount != null ) {
			mTab.setValue(MSalAdvReq.COLUMNNAME_emiamount, emiAmount);
		}
		
		return;
	}
	/**
	 * This call out will populate Employee data based employee ID
	 * 
	 * 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 */
	public void updateEmployeeDetails(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		if (value == null || isCalloutActive()) {
			return;
		}
		
		Timestamp dateRequest=(Timestamp)mTab.getValue(MSalAdvReq.COLUMNNAME_requestdate);
		
		int bPartnerID = Integer.parseInt(mTab.get_ValueAsString(MSalAdvReq.COLUMNNAME_C_BPartner_ID ) );
		MBPartner employee = new MBPartner(ctx, bPartnerID, null);
		BigDecimal currMonthSal= Env.ZERO;
		if(dateRequest != null && bPartnerID > 0) {
		 int mHRPeriodID = WTCEmployeeUtil.getPeriodId(bPartnerID, dateRequest);
			String processType = MHRProcess.PAYROLLPROCESSTYPE_Mock;
			WTCEmployeeUtil    employeeUtil = new WTCEmployeeUtil(Env.getCtx(), bPartnerID, mHRPeriodID, processType, null);
			if(employeeUtil != null) {

			    currMonthSal  =   employeeUtil.getCurrentMonthSalary(bPartnerID,dateRequest);
//			    [20120109:6:00]
			    currMonthSal  =   currMonthSal.setScale(2,BigDecimal.ROUND_HALF_UP);
			}
		}
		mTab.setValue(MSalAdvReq.COLUMNNAME_currentmonthsalary, currMonthSal );
		mTab.setValue(MSalAdvReq.COLUMNNAME_previousmonthsalary,WTCEmployeeUtil.getPreviousMonthSalary(bPartnerID,dateRequest) );
		mTab.setValue(MSalAdvReq.COLUMNNAME_HR_Department_ID, employee.getHR_Department_ID());
		mTab.setValue(MSalAdvReq.COLUMNNAME_HR_Designation_ID, employee.getHR_Designation_ID());
		return;
	}
	

	
	/**
	 * This call out will populate or reset the value of paidoff date
	 * 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 */
	public void updatePaidOffDate( Properties ctx, 
								   int WindowNo, 
								   GridTab mTab,
								   GridField mField, 
								   Object value ) {
		
		if (value == null || isCalloutActive()) {
			return;
		}
		
		Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
		
		if (((Boolean)value).equals(Boolean.TRUE) ) {
			mTab.setValue(MSalAdvReq.COLUMNNAME_paidoffdate, timestamp );
		} else {
			mTab.setValue(MSalAdvReq.COLUMNNAME_paidoffdate, null );
		}
	}
	
	
	/**
	 * Shows Warning MEssages When Skip the EMI
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 */
	public void showIsSkipWarning( Properties ctx, 
								   int WindowNo, 
								   GridTab mTab,
								   GridField mField, 
								   Object value ) {
		
		if (value == null || isCalloutActive()) {
			return;
		}
		
		if (((Boolean)value).equals(Boolean.TRUE) ) {
			mTab.fireDataStatusEEvent(EagleConstants.EMI_SKIP_YES, null, false);
		} else {
			mTab.fireDataStatusEEvent(EagleConstants.EMI_SKIP_NO, null, false);
		}
		
	}
	
	
	/**
	 * 1638
	 * [20120103:4:00PM]
	 * This Method is used to Validate the Requested Number Of installments and Approved Installments 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String validateNumberOfInstallments( Properties ctx, 
											    int WindowNo, 
											    GridTab mTab,
											    GridField mField, 
											    Object value ) {

		if (value == null || isCalloutActive()) {
			return "";
		}
		
		Object requestedInstalmentsObject = mTab.getValue(MSalAdvReq.COLUMNNAME_reqnoofinstalments);
		Object givenInstalmentsObject     = mTab.getValue(MSalAdvReq.COLUMNNAME_noofinstalments);
		
		
		if(requestedInstalmentsObject != null ) {
			
			BigDecimal  numberofInstallments = GeneralUtil.getBigDecimalValue(requestedInstalmentsObject);
			if(numberofInstallments.compareTo(Env.ONE) < 0) {
				String mesgInstallment = EagleMessageConstants.NUMBER_OF_INSTALL_MENTS_SHOULD_NOT_BE_LESS_THAN_ONE;
				mesgInstallment = Msg.getMsg(Env.getCtx(), mesgInstallment); 
				mTab.fireDataStatusEEvent(mesgInstallment, "", true);
				mTab.setValue(MSalAdvReq.COLUMNNAME_reqnoofinstalments, new Integer(1));
			}
		}
		
		if(givenInstalmentsObject != null) {
			
			BigDecimal  numberofInstallments = GeneralUtil.getBigDecimalValue(givenInstalmentsObject);
			if(numberofInstallments.compareTo(Env.ONE) < 0) {
				String mesgInstallment = EagleMessageConstants.NUMBER_OF_INSTALL_MENTS_SHOULD_NOT_BE_LESS_THAN_ONE;
				mesgInstallment = Msg.getMsg(Env.getCtx(), mesgInstallment);
				mTab.fireDataStatusEEvent(mesgInstallment, "", true);
				mTab.setValue(MSalAdvReq.COLUMNNAME_noofinstalments, new Integer(1));
			}
		}

		return "";
	}
	
	/**
	 * [20120107:12:00]
	 * This Method is used to check where Actula Emi Amount is Grater Than the Given Emi Amount.
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 */
	public void validateEMIAmountAndActualEMI( Properties ctx, 
											     int WindowNo, 
											     GridTab mTab,
											     GridField mField, 
											     Object value ) {
		/*if (value == null || isCalloutActive()) {
			return "";
		}*/
		
		Object actualEmiAmountObject = mTab.getValue(MSalAdvEMI.COLUMNNAME_actualemiamount);
		Object emiAmountObject = mTab.getValue(MSalAdvEMI.COLUMNNAME_emiamount);
		
		if(actualEmiAmountObject != null && emiAmountObject != null) {
			
			BigDecimal  actualEmi = GeneralUtil.getBigDecimalValue(actualEmiAmountObject);
			BigDecimal  emiAmount = GeneralUtil.getBigDecimalValue(emiAmountObject);
			if(actualEmi.compareTo(emiAmount)  == 1 ) {
				String msg = Msg.getMsg(Env.getCtx(), EagleMessageConstants.ACTUAL_EMI_AMOUNT_SHOULD_NOT_GRATER_THAN_EMI);
				mTab.fireDataStatusEEvent(msg, "", true);
				mTab.setValue(MSalAdvEMI.COLUMNNAME_actualemiamount, Env.ZERO);
			}
		}
	}
	
}
