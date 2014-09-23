/**
 * 
 */
package org.wtc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.adempiere.util.ProcessUtil;
import org.compiere.model.I_AD_Process;
import org.compiere.model.I_C_Period;
import org.compiere.model.I_R_MailText;
import org.compiere.model.MBankAccount;
import org.compiere.model.MEmpGateAttendence;
import org.compiere.model.MEmpSupAttendence;
import org.compiere.model.MMailText;
import org.compiere.model.MPInstance;
import org.compiere.model.MPeriod;
import org.compiere.model.MProcess;
import org.compiere.model.MSysConfig;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.eevolution.model.MHRPeriod;

/**
 * @author PhaniKiran.Gutha
 *
 * @author Arunkumar
 * Bug -1628 
 *  ADDED Method : getNoDaysOfDay(Timestamp, Timestamp, int)  to get
 *  
 *  This method is used to get the no of days with week equal to the DayOfWeek between the passed 
	 * from date and to date
	 * Example: fromdate:apr 1st 2011, todate: apr 30th 2011, DayOfWeek=0(Sunday),
	 * this method will return 4
	 * 
 *
 * ADDED Method : getHRPeriodId(Date)  to Compare the Date tp period start date and end date , Date belongs which Period
 * 
 * ADDED Method: getPeriodId(int, Timestamp)
 * This method will return the period id of period to which the date and business partner are eligible
 * 
 * @Bug     @author       		@ChangeID             @Description
 * -----------------------------------------------------------------
 * 1628      Arunkumar    		[20111209:3:00PM]      For Now We Are NOT Using The HRPeriod 
 * 1762		 Phanikiran.Gutha	[2011141029]      	   Util method to get the previous period id based on the period id
 * 1638      Arunkumar          [20111221:2:30pm]      Added A Method getDocTypeID  Based On Base Doc Type  
 * 1638      Arunkumar          [20111221:3:00]        Added A Method To Get CashBook
 * 1633		 D.Yadagiri Rao     [20111229:21:31]		Added A Method(getListAsString) To get list of items in string format separated by , .
 * 2013		 D.Yadagiri	Rao		[201202041742]		   Added one functionality 
 * 													 	Based on this we introduced 4 methods 
 * 														1. getRegexForDateFormat(String dateFormat)			:- 
 * 														Helping methods :-getRegexSymbol(String dateFormat)
 * 																		isValidDateFormat( format )	
 * 																		getRegexFormat( format, symbol )
 * 
 * 2013		D.Yadagiri Rao		[201202041742]			Modified at getRegexFormat() Method (regex symbol set \\ instead of || )
 * 
 * 2013		D.Yadagiri Rao		[201202041230]			Added one more method it checks a string date is valid or not based on the  date format
 * 
 * 2114		D.Yadagiri Rao		[201202151144]			Added readCommaSeparatedStrings Method It returns Comma Separated ArrayList of type Object
 * 
 */
public class WTCUtil {

/**
 * Kindly do not delete below line as it is being used for svn version maintenance
 */
public static final String svnRevision =  "$Id: WTCUtil.java 1036 2012-02-15 09:53:27Z yadagiri $";

	private static CLogger log = CLogger.getCLogger( WTCUtil.class );

	/**
	 * Executes the report associated with the ProcessId and prepares a pdf file 
	 * @param processID
	 * @param params
	 * @return  return the processInfo of the report process
	 */

	public static ProcessInfo getPDFReportProcessInfo ( int processID , ProcessInfoParameter[] params ){

		MProcess proc = new MProcess(Env.getCtx(),processID, null);
		MPInstance instance = new MPInstance(proc, processID);
		int instanceId=0;
		if(instance.save())    {
			instanceId=instance.get_ID();
		}       

		ProcessInfo poInfo = new ProcessInfo(proc.getName(), proc.getAD_Process_ID());
		if(params != null){
			poInfo.setParameter(params);
		}
		poInfo.setRecord_ID(processID);
		poInfo.setAD_Process_ID(processID);
		poInfo.setAD_PInstance_ID(instanceId);
		poInfo.setIsBatch(true);
		poInfo.setPrintPreview(false);

		//Here It Is Mandatory Other Wise It Will Give Null Pointer Exception
		//We are Not Creating New One Just We Are Getting Existing Trx
		String trxName = Trx.createTrxName("WTCUtil.getPDF");
		Trx trx = Trx.get(trxName, false);
		boolean isSuccess=ProcessUtil.startJavaProcess(Env.getCtx(), poInfo, trx, true);

		if( isSuccess ){
			return poInfo;
		}
		return null;
	}

	/**
	 * Executes the report associated with the ProcessId and prepares a pdf file 
	 * @param processID
	 * @param params
	 * @return  return the PDF File object, generated out of the process
	 */
	public static File getPDFReportFile( int processID , ProcessInfoParameter[] params ){

		ProcessInfo pInfo = WTCUtil.getPDFReportProcessInfo( processID, params  );

		if( pInfo != null ){

			File f = pInfo.getPDFReport();
			return f;
		}
		return null;
	}


	/**
	 * This Method IS Responsible To Create A Directory If Not Exist And Take A Back Up Of Temp Directory
	 * Report PDF File
	 * @param file
	 * @param directoryName 
	 * @return destinationFile 
	 */
	public static File moveToHomeDir(File file , String directoryName ) {

		File destinationFile = null;

		try {

			String reportDirectory =  System.getProperty("ADEMPIERE_HOME");

			String separator = System.getProperty("file.separator");

			if( separator == null ) {

				separator = "/";
			}

			reportDirectory  =  reportDirectory + separator + directoryName + separator;           
			File directory = new File(reportDirectory);

			if( !directory.exists() ) {

				boolean success = (directory).mkdir();

				if( !success ) {

					log.log(Level.SEVERE, "Destination Directory Created Failed");
				}

			}else {
				log.log(Level.WARNING, "Destination Directory Already Existed");
			}

			String sourcePath =  file.getAbsolutePath();
			String tokens[] =  sourcePath.split("/");
			String fileName = null;

			if(tokens != null) {

				int leangth = tokens.length;
				fileName = tokens[leangth-1];
			}

			if(fileName != null) {

				destinationFile = new File(reportDirectory, fileName);
			}

			InputStream in = new FileInputStream(file);
			OutputStream out = new FileOutputStream(destinationFile);
			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {

				out.write(buf, 0, len);
			}

			in.close();
			out.close();

			log.fine( "File " + file.getAbsolutePath() + " moved to the desitnation direcotry" + directoryName );

		}catch (IOException e){

			log.severe("Cannot move the home dir  - "+ e.getMessage());
		}

		return destinationFile;
	}

	/**
	 * 
	 * @param AD_Client_ID
	 * @param mailTemplateName
	 * @param ctx
	 * @param trxName
	 * @return the mail template po object with the Specific clinet and the Specific name
	 */
	public static MMailText getMailTemplate( int AD_Client_ID , String mailTemplateName  , Properties ctx , String trxName ){

		return new Query( ctx , 
				I_R_MailText.Table_Name ,
				I_R_MailText.COLUMNNAME_Name + " = ? AND " + I_R_MailText.COLUMNNAME_AD_Client_ID + " = ? " ,
				trxName )
		.setParameters(  mailTemplateName , AD_Client_ID )
		.first();
	}

	/**
	 * 
	 * @param AD_Client_ID
	 * @param mailTemplateName
	 * @param ctx
	 * @param trxName
	 * @return the mail templateID with the Specific clinet and the Specific name, Mail template doesnot exists then return null
	 */
	public static Integer getMailTemplateID( int AD_Client_ID , String mailTemplateName  , Properties ctx , String trxName ){

		MMailText mailTemplate = WTCUtil.getMailTemplate(AD_Client_ID, mailTemplateName, ctx, trxName);

		if( mailTemplate != null  && mailTemplate.get_ID() > 0 ){		

			return mailTemplate.get_ID();

		} else {

			return null;
		}
	}

	/**
	 * 
	 * @param tableName
	 * @param columnName
	 * @return the AD_Column_ID of the column of the particular table, if not found return zero
	 */
	public static int getADColumnID( String tableName , String columnName ){

		String sql = " SELECT AD_Column_ID FROM AD_Column c " +
						" JOIN AD_Table t ON ( t.AD_Table_ID = c.AD_Table_ID )" +
						" WHERE c.columnname = ? AND t.	tablename = ?";

		PreparedStatement pstmt = DB.prepareStatement( sql , null );
		ResultSet rs = null;
		try {
			pstmt.setString( 1,  columnName );
			pstmt.setString( 2,  tableName  );
			rs = pstmt.executeQuery();
			if( rs.next() ){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			log.severe( e.getMessage() );
		}


		return 0;
	}

	/**
	 * @param name
	 * @return Return Process Id based on the process name
	 */
	public static int getAD_Process_ID( String name ){

		return new Query( Env.getCtx() , I_AD_Process.Table_Name , I_AD_Process.COLUMNNAME_Name + " = ? ",null).setParameters(name).firstId();
	}

	/**
	 * Bug- 1628
	 * This method is used to get the no of days with week equal to the DayOfWeek between the passed 
	 * from date and to date
	 * Example: fromdate:apr 1st 2011, todate: apr 30th 2011, DayOfWeek=0(Sunday),
	 * this method will return 4
	 * @param fromdate 
	 * @param todate 
	 * @param DayOfWeek 
	 * @return get No Days Of Day
	 */
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
	

	/**
	 * Bug -1628
	 * Based on the Date get HR_Period ID
	 * 
	 * Compare the Date tp period start date and end date , Date belongs which Period
	 * @param date 
	 * @return Return HR_period_ID based on the Date
	 */
	public static int getHRPeriodId(Date date) {
		int periodId = 0;
		Timestamp dateToTime = new Timestamp(date.getTime());
		Timestamp timestamp=TimeUtil.trunc(dateToTime,null);
		StringBuffer where = new StringBuffer();
		where.append("'"+timestamp+"' BETWEEN ").append(MHRPeriod.COLUMNNAME_StartDate).append(" AND ").append(MPeriod.COLUMNNAME_EndDate);
		periodId = new Query(Env.getCtx(),MHRPeriod.Table_Name,where.toString(),null).firstId();
		return periodId;
	}
	

	/**
	 * Bug -1628
	 * This method will return the period id of period to which the date and business partner are eligible
	 * @param employeeId 
	 * @param date 
	 * @return Return C_Period_Id based on the Employee Id and  TimeStamp 
	 */
	public static int getPeriodId(int employeeId,Timestamp date)
	{
		//[20111209:3:00PM]
		//TODO :  After Implementing The Payroll We Have To UnComment This Code
		
		//int payrollId = getPayrollId(employeeId);
		
		// prepare query to get the periods which belongs to payroll and whose start date and end is between the parameter date
		Timestamp resultentDate=TimeUtil.trunc(date,null);
		
		StringBuffer where = new StringBuffer();
		//where.append(MHRPeriod.COLUMNNAME_HR_Payroll_ID)
		//.append(" = ")
		//.append(payrollId)
		//.append(" AND ")
		where.append(" '"+resultentDate+"'")
		.append(" >= ")
		.append(MPeriod.COLUMNNAME_StartDate).append(" AND ").append("'"+resultentDate+"'").append(" <= ").append(MPeriod.COLUMNNAME_EndDate);
		
		int periodId = new Query(Env.getCtx(),MPeriod.Table_Name,where.toString(),null).firstId();
		
		return periodId;
	}
	
	/**
	 * @author PhaniKiran.Gutha
	 * Issue No : 1762
	 * 2011141029
	 * Get the Previous period Id based on the current Period Id. if the current period is the first period of the year
	 * then will return last period id of the previous year
	 * @param currentPeriodId
	 * @param ctx
	 * @return return previous period Id
	 */
	public static int getPreviousPeriod( int currentPeriodId ,Properties ctx ){
		
		log.info( "CurrentPeiodID "+ currentPeriodId );
		MPeriod currentPeriod = new MPeriod( ctx, currentPeriodId, null );
		int prevPeriodId  =0;
		if( currentPeriod.getPeriodNo() == 1 ){
			
			log.warning( " Period passed is First Period of the year " );
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis( currentPeriod.getStartDate().getTime() );
			cal.add( Calendar.DAY_OF_YEAR, -7 );
			Timestamp st = new Timestamp( cal.getTimeInMillis() );
			currentPeriod = MPeriod.get(ctx, st , Env.getAD_Org_ID(ctx));
			prevPeriodId = currentPeriod.get_ID();
			
		}else {
		
		 prevPeriodId = DB.getSQLValue(null, "SELECT " + I_C_Period.COLUMNNAME_C_Period_ID +" FROM " +
											I_C_Period.Table_Name +" WHERE "+ I_C_Period.COLUMNNAME_PeriodNo + " = " +(currentPeriod.getPeriodNo() -1) + " AND "
											+ I_C_Period.COLUMNNAME_C_Year_ID + " = " + currentPeriod.getC_Year_ID() );
		}
		
		log.finest(" Pervious period id of the current peiord "+ currentPeriodId + " is " + prevPeriodId );
		return prevPeriodId;
	}
	
	
	
	/**
	 * Bug-1638
	 * [20111221:2:30pm]
	 * This method allows user to get the doc type id for a given doc type. 
	 * For example following query returns doc type id for the AP Payment.
	 * select C_DocType_ID from C_DocType where C_DocType.DocBaseType IN ('APP')
	 * 
	 * @param docBaseType
	 * @return Get DocType based on the base doc type
	 */
	public static int getDocTypeID( String docBaseType ) {
		String sqlDocType = "SELECT c_doctype_id FROM c_doctype WHERE docbasetype = ? ";
		int docTypeID = DB.getSQLValue(null, sqlDocType, docBaseType ); 
		
		return docTypeID;
	}
	
	
	/**
	 * Bug-1638
	 * [20111221:3:00]
	 * First We Will See For The Organization Cash Book, If It is Not Available Then We go For Any Cash Book Which is Default
	 * @return return default cash book Id
	 */
	public static int defaultCashBook( ) {
		
		String sqlCashBookID = null;
		int bookID = 0;
		int orgId = Env.getAD_Org_ID(Env.getCtx());
		if ( orgId != 0) {
		     sqlCashBookID="SELECT c_cashbook_id FROM c_cashbook WHERE isdefault = 'Y' and AD_Org_ID="+orgId;
		     bookID = DB.getSQLValue(null, sqlCashBookID); 
		     
		     if(bookID < 1) {
		    	 log.log(Level.SEVERE, "There is No Organization Default Cash Book , Looking for Non Defaults");
		    	 sqlCashBookID="SELECT c_cashbook_id FROM c_cashbook WHERE  AD_Org_ID="+orgId;
		    	 bookID = DB.getSQLValue(null, sqlCashBookID); 
		     }
	    }
		
		if(bookID < 1) {
			
			log.log(Level.SEVERE, "Organization Cashbook Does Not Exist , Taking Default CashBook");
			sqlCashBookID = "SELECT c_cashbook_id FROM c_cashbook WHERE isdefault = 'Y' ";
			
			bookID = DB.getSQLValue(null, sqlCashBookID); 
		}
		
		return bookID;
	}
	
	/**
	 * This method will try for organization bank acct , if we login with organization other than ' * ',
	 * if we login with * the default bank account.
	 * if it can't get bank acct of organization then it again go for Default One.
	 * @param trxName
	 * @return Return bank Account C_Bank_Account_ID 
	 */
	public static int getBankAccount(String trxName) {
		int bankAcctNumber = 0;
		int orgId = Env.getAD_Org_ID(Env.getCtx());
		
		if (orgId <=  0 ) {
			//Will Get The Default Bank Account
			bankAcctNumber = getDefaultBankAccount(trxName);
		}else {
			 String whereClause = MBankAccount.COLUMNNAME_IsDefault+" ='Y'"+" AND "+
								 MBankAccount.COLUMNNAME_AD_Org_ID +" = "+orgId +" AND "+
								 MBankAccount.COLUMNNAME_IsActive+"='Y'";
			 MBankAccount  bankAct =new Query(Env.getCtx(), 
											 MBankAccount.Table_Name, 
											 whereClause , 
											 trxName).first();
			 if(bankAct != null) {
				 bankAcctNumber = bankAct.getC_BankAccount_ID();
			 }else {
				 //It is trying for only organizational bank acct , not default in that 
				 String whereClause1 = MBankAccount.COLUMNNAME_AD_Org_ID +" = "+orgId +" AND "+
				 					   MBankAccount.COLUMNNAME_IsActive+"='Y'";
				 MBankAccount  bankAct1 =new Query(Env.getCtx(), 
												   MBankAccount.Table_Name, 
												   whereClause1 , 
												   trxName).first();
				 if(bankAct1 != null) {
					 bankAcctNumber = bankAct1.getC_BankAccount_ID();
				 }else {
					 //Will Get The Default Bank Account
					bankAcctNumber = getDefaultBankAccount(trxName);	
				 }
			 }
		}
		return bankAcctNumber;
	}
	
	/**
	 * This Method Returns The Organizational Bank Account
	 * Other Wise ZERO
	 * @param orgId
	 * @return Return organizational Bank Account
	 */
	public static int organizationalBankAccount(int orgId) {
		
		int bankAcctNumber = 0;
		 String whereClause = MBankAccount.COLUMNNAME_IsDefault+" ='Y'"+" AND "+
		 					  MBankAccount.COLUMNNAME_AD_Org_ID +" = "+orgId +" AND "+
		 					  MBankAccount.COLUMNNAME_IsActive+"='Y'";
		 MBankAccount  bankAct =new Query(Env.getCtx(), 
					 					  MBankAccount.Table_Name, 
					 					  whereClause , 
					 					  null).first();
		if(bankAct != null) {
			bankAcctNumber = bankAct.getC_BankAccount_ID();
		}
		else {
			 String whereClause1 =  MBankAccount.COLUMNNAME_AD_Org_ID +" = "+orgId +" AND "+
			  					   MBankAccount.COLUMNNAME_IsActive+"='Y'";
			 MBankAccount  bankAct1 =new Query(Env.getCtx(), 
					 						   MBankAccount.Table_Name, 
					 						   whereClause1 , 
					 						   null).first();
			 if(bankAct1 != null) {
					bankAcctNumber = bankAct1.getC_BankAccount_ID();
			 }
		}
		
		return bankAcctNumber;
	}
	
	/**
	 * This Method Will Return Default Bank Account With out 
	 * Considering the Organization
	 * @param trxName
	 * @return Return Default bank Account_ID 
	 */
	public static int getDefaultBankAccount(String trxName) {
		int bankAcctNumber = 0;
		 String whereClause = MBankAccount.COLUMNNAME_IsDefault+"='Y'"+" AND "+
		 					  MBankAccount.COLUMNNAME_IsActive+"='Y'";
		 MBankAccount  bankAct =new Query(Env.getCtx(), 
										 MBankAccount.Table_Name, 
										 whereClause , 
										 trxName).first();
		
		 if(bankAct != null) {
			 bankAcctNumber = bankAct.getC_BankAccount_ID();
		 }else {
			 log.log(Level.SEVERE, "There is no default Bank Account Defined");
		 }
		 
		return  bankAcctNumber;
	}
	
	
	/**
	 * This Method Will Return Boolean Value
	 * True  - IF Attandance Exist
	 * False - If Not Exist
	 * @param dateon
	 * @param BpartnerID
	 * @return Return True IF is Attendance Exist For Given Date
	 */
	
	public static Boolean isAttandanceExistForGivenDate(Timestamp  dateon, int BpartnerID ) {
		
		dateon = TimeUtil.trunc(dateon, null);
		String useSupAttendance = MSysConfig.getValue(EagleConstants.USE_ATTENDANCE_BY_SUPERVISOR, "N");
		if(useSupAttendance.equalsIgnoreCase("Y")) {
			String whereClause = MEmpSupAttendence.COLUMNNAME_C_BPartner_ID+"="+BpartnerID +" AND "+
			                     MEmpSupAttendence.COLUMNNAME_workdate+" = '"+dateon+"'";
			
			MEmpSupAttendence  supatt= new Query(Env.getCtx(), MEmpSupAttendence.Table_Name, whereClause, null).first();
			if (supatt != null) {
				return Boolean.TRUE;
			}else {
				return Boolean.FALSE;
			}
		}else {
			String whereClause = MEmpGateAttendence.COLUMNNAME_C_BPartner_ID+"="+BpartnerID +" AND "+
								 MEmpGateAttendence.COLUMNNAME_workdate+" = '"+dateon+"'";

			MEmpGateAttendence  gateatt= new Query(Env.getCtx(), MEmpGateAttendence.Table_Name, whereClause, null).first();
			if (gateatt != null) {
				return Boolean.TRUE;
			}else {
				return Boolean.FALSE;
			}
		}
	}
	
	/**
	 * @BUG :1633
	 * @author D.Yadagiri Rao     
	 * @ChangeID [20111229:21:31]		
	 * @Description To get list of items in string format separated by , .
	 * @param list
	 * @return String 
	 */
	public static String getListAsString(List<?> list) {
		
		if(list.isEmpty()){
			return "";
		}
		StringBuffer stringList = new StringBuffer();
		int i=0;
		while( i < list.size() ) {
			if(i > 0) {
				stringList = stringList.append(", ");
			}
			stringList.append(list.get(i));
			i++;
		}
		return stringList.toString();
	}
	

	
	
	
	/**
	 * 
	 * @param dateFormat
	 * @return Regex Format
	 */
	public static String getRegexForDateFormat( String dateFormat ) {
		
		if( null == dateFormat ){
			return null;
		}
		String regexdateformat	=	"";
		
		String symbol	=	getRegexSymbol( dateFormat );
		if( ! dateFormat.isEmpty() ) {
			regexdateformat = getRegexFormat( dateFormat, symbol );
		}

		return regexdateformat;
		
	}
	
	/**
	 * 
	 * @param format
	 * @return return true if it is correct format
	 * 
	 */
	private static boolean isValidDateFormat( String format ) {
		
		String[] symbols	=	{"/", " ", ".", "-" };
		
		int count = 0;
		
		boolean isValid = Boolean.FALSE;
		
		for ( int i = 0; i < symbols.length; i++ ) {
			
			
			if ( format.contains( symbols[i] ) ) {
				isValid = Boolean.TRUE;
				count++;
			}
		}
		
		if( ! isValid || count != 1 ) {
			
			return Boolean.FALSE;
			
		}
		
		return isValid;
		
	}
	
	/**
	 * 
	 * @param format
	 * @return Regex Symbol 
	 */
	private static String getRegexSymbol(String format) {
		
		String symbol = "";
		
		if ( isValidDateFormat( format ) ) {
			
			String[] symbols = { "/", " ", ".", "-" };

			for (int i = 0; i < symbols.length; i++) {
				if ( format.contains( symbols[i] ) ) {
					symbol = symbols[i];
					break;
				}
			}
		}
		
		return symbol;
	}
	
	/**
	 * 
	 * @param format
	 * @param symbol
	 * @return Regex Date Format
	 */
	private static String getRegexFormat (String format, String symbol ){
		
		StringBuffer buffer	=	new StringBuffer();
		
		
		if (  Util.isEmpty(format, Boolean.TRUE )  || 
				 Util.isEmpty(symbol, Boolean.TRUE ) ) {
			
			return buffer.toString();
			
		}
		
		StringTokenizer	token	=	new StringTokenizer(format, symbol);
		
		int noOfTokens =	token.countTokens();
		
		
		while ( token.hasMoreTokens() ) {
			
				int nextTokenLength	=	token.nextToken().length();
				buffer.append("(");
				while( nextTokenLength	> 0 ) {
					buffer.append("\\S" );
					nextTokenLength--;
				}
				buffer.append(")");
				if( noOfTokens > 1 ) {
					buffer.append( symbol );
					noOfTokens--;
				}
		}
		
		return buffer.toString();
		
	}
	
	/**
	 * 
	 * It Checks Given String Date Followed with the dateFormat or not
	 * @author Giri
	 * @ChangeID 201202041230
	 * @param stringDate
	 * @param dateFormat
	 * @return	true if StrigDate satisfies Date-format
	 */
	public static boolean isValidDate(String stringDate, String dateFormat ) {
		
		boolean isValidDate	=	 Boolean.FALSE;
		
		//
		// StringDate or dateFormat is anyone null or "" then return as FALSE
		//
		if( Util.isEmpty(stringDate, Boolean.TRUE ) || Util.isEmpty( dateFormat, Boolean.TRUE ) ) {
			
			return isValidDate;
		}
		
		//
		// Creating a simpleDateFormat object With using out dateFormat
		
		SimpleDateFormat simpleDateFormat	=	new SimpleDateFormat( dateFormat );
		
		try {
			
			Date date =	simpleDateFormat.parse( stringDate );
			
			if( null != date ){
				
				isValidDate = Boolean.TRUE;
			}
			
		} catch (Exception e) {
			
			return isValidDate;
			
		}
		
		return isValidDate;
	}
	
	/**
	 * @param roles
	 * @return Return Comma Separated ArrayList of type Object
	 */
	public static ArrayList<Object> readCommaSeparatedStrings( String roles) {

		ArrayList<Object> commaSeperated = new ArrayList<Object>();
		
		if(roles == null) {
			return commaSeperated;
		}
		
		StringTokenizer token	=	new StringTokenizer(roles, ",");
		
		while ( token.hasMoreElements() ) {
			Object obj	=	token.nextToken();
			if( null != obj )	{
				if ( obj instanceof String && Util.isEmpty( ( String ) obj, Boolean.TRUE )) {
						commaSeperated.add( obj );
				}
				commaSeperated.add( obj );
			}
		}
		return commaSeperated ;
		
	}
}
