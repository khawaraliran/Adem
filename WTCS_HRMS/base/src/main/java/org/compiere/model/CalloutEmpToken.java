package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.eevolution.model.MHRProcess;
import org.wtc.util.GeneralUtil;
import org.wtc.util.WTCEmployeeUtil;

/**
 * @Bug   @author      @Change ID         @Description
 * ************************************************************************************************************
 * 1636    Arunkumar   [20111214:9 :50]    Using Generic Method To Validate Bidecimal
 * 1636    Arunkumar   [20111214:10:00]    This Method Refresh The Issue Token Window When There is A Change in Token Level
 * 1636    Arunkumar   [20111214:10:05]    There May be A chance To Put null In This So Checking And Place Correct Value
 * 1903    Arunkumar   [20120109:6:00]     Modified :  current month salary functionality to get 2 decimal precision             
 */
public class CalloutEmpToken extends CalloutEngine {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: CalloutEmpToken.java 1009 2012-02-09 09:16:13Z suman $";
	
	/**
	 * Populates Token Amount
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String getTokenAmount(Properties ctx, int WindowNo, GridTab mTab,GridField mField, Object value)
	{
		 if (isCalloutActive() || value == null)
		 {
		   return "";
		 }
		 
		 Integer tokenID = (Integer) value;
		 
		 if ( tokenID == null || tokenID.intValue() <= 0)
		 {
			 return "";
		 }
		 
		 String tokenAmtSql = "SELECT coalesce(amount,0.0) FROM HR_Token WHERE Hr_Token_ID=?";
		 BigDecimal tokenAmount = DB.getSQLValueBD(null,tokenAmtSql, tokenID.intValue());
		 //[20111214:10:05]
		 //There May be A chance To Put null In This So Checking And Place Correct Value
		 tokenAmount =tokenAmount == null ? Env.ZERO : tokenAmount;
	     mTab.setValue(MEmpToken.COLUMNNAME_tokenamount, tokenAmount);
				
		 return "";

	}
	
	
	/**
	 * Gets The Total Amount Based On Given number Of Token And Each Token Amount.
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String totalAmount(Properties ctx, int WindowNo, GridTab mTab,GridField mField, Object value)
	{
		 if (isCalloutActive() || value == null)
		 {
		   return "";
		 }
		 
		 //[20111214:9:50]
		 Object noOfTokensObj =  mTab.getValue(MEmpToken.COLUMNNAME_nooftokens);
		 Integer noOfTokens =  null;
		 if(noOfTokensObj != null) {
			 noOfTokens= ( GeneralUtil.getBigDecimalValue(noOfTokensObj) ).intValue();
		 }
		 
		 BigDecimal tokenAmount = (BigDecimal) mTab.getValue(MEmpToken.COLUMNNAME_tokenamount);
		 
		 if ( noOfTokens != null && tokenAmount != null){
			 
			 BigDecimal totalTokenAmt = tokenAmount.multiply(new BigDecimal(noOfTokens));
			 mTab.setValue(MEmpToken.COLUMNNAME_totalamount,totalTokenAmt);
		 }
						
		 return "";

	}
	
	
	/**
	 * Gets The Current Month And Previous Month Salary
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	public String getSalDetails(Properties ctx, int WindowNo, GridTab mTab,GridField mField, Object value) 
	{
		 if (isCalloutActive() || value == null)
		 {
		   return "";
		 }
		 
		Object  bpart=mTab.getValue(MEmpToken.COLUMNNAME_C_BPartner_ID);
		 
		if (bpart != null)	
		{
			int bPartnerID =(Integer) bpart;
		
			Timestamp issueDate=(Timestamp) mTab.getValue(MEmpToken.COLUMNNAME_issuedate);
			 
			 if ( bPartnerID > 0  && ( issueDate != null) )
			 {
				    int mHRPeriodID = WTCEmployeeUtil.getPeriodId(bPartnerID, issueDate);
					String processType = MHRProcess.PAYROLLPROCESSTYPE_Mock;
					WTCEmployeeUtil    employeeUtil = new WTCEmployeeUtil(Env.getCtx(), bPartnerID, mHRPeriodID, processType, null);
					BigDecimal currMonthSal= Env.ZERO;
					if(employeeUtil != null) {
					    currMonthSal  =   employeeUtil.getCurrentMonthSalary(bPartnerID,issueDate);
//					    [20120109:6:00]
					    currMonthSal  =   currMonthSal.setScale(2,BigDecimal.ROUND_HALF_UP);
					}
				 mTab.setValue(MEmpToken.COLUMNNAME_currmonthsal, currMonthSal );
				 mTab.setValue(MEmpToken.COLUMNNAME_prevmonthsal, WTCEmployeeUtil.getPreviousMonthSalary(bPartnerID,issueDate));
			 }
		}	
		 return "";
	}
	
	/**
	 * Bug -1636
	 * This Method Refresh The Issue Token Window When There is A Change in Token Level
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * [20111214:10:00]
	 */
	public void refreshIssueTokenWhenTokenLevelChange(Properties ctx, 
														int WindowNo, 
														GridTab mTab,
														GridField mField, 
														Object value) {
		 if (isCalloutActive() || value == null) {
		   return ;
		 }
		 
		Object TokenLevelObj =  mTab.getValue(MEmpToken.COLUMNNAME_tokenlevel);
		if(TokenLevelObj != null ) {
			mTab.setValue(MEmpToken.COLUMNNAME_tokenamount, Env.ZERO);
			mTab.setValue(MEmpToken.COLUMNNAME_nooftokens, new Integer(0));
			mTab.setValue(MEmpToken.COLUMNNAME_totalamount, Env.ZERO);
		}
	}

}
