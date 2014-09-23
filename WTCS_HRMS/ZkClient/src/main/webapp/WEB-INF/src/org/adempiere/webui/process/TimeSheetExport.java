package org.adempiere.webui.process;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.compiere.model.MBPartner;
import org.compiere.model.MProject;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTimeSheet;
import org.compiere.model.MTimesheetLine;
import org.compiere.model.Query;
import org.compiere.model.X_HR_Emp_Gate_Attendence;
import org.compiere.model.X_HR_Emp_Sup_Attendence;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.wtc.util.EagleMessageConstants;
import org.wtc.util.WTCTimeUtil;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;



/**
 * @Bug      @author     @ChangeID    		@Discription
 * ****************************************************************************************************************************
 * 1630      Arunkumar               		 Initial Check In 
 * 1630      Arunkumar   [20111223:5:00]     This Method Will PrepareTimeSheet Columns       
 * 1630      Arunkumar   [20111223:8:00PM]   Getting the number of working dayscreateReportHeading       
 * 1630      Arunkumar   [20111223:9:00PM]   Report Name Should Not Exceed 30 , Work sheet Can't Take More Than That
 * 1630      Arunkumar   [20120106:10:05]    Query Is changed in a way that fallows  Adempiere coding guidelines.
 * 1630      Arunkumar   [20120106:10:15]    Changed Name Of the Variable(C_Project_ID) According to Coding Guid Lines
 * 1630      Arunkumar   [20120109:9:00PM]   Modified : prepareQuery()  For Considering the Is Employee OR Not
 * 1630      Arunkumar   [20120111:2:00PM]   Modified : prepareQuery()  We have Wrongly given Formed The Query .
 *                                                                      Now Query is changed and Given Test Query Format In Comments.
 * 1630      Arunkumar   [20120111:9:00]     Modified: fillTimeSheetDetails  while Exportting If the Gate Attendance dont have to time
 *                                                      it is considering current time, so it is giving wrong values.
 * 														                                                                
 */


public class TimeSheetExport extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: TimeSheetExport.java 1009 2012-02-09 09:16:13Z suman $";

	private Timestamp FromDate = null;

	private Timestamp ToDate = null;
	
	private BigDecimal customerID = Env.ZERO;
	
	//[20120106:10:15] //Changed Name Of the Variable According to Coding Guid Lines
	private BigDecimal project_ID = Env.ZERO;
	
	private BigDecimal employeeID = Env.ZERO;

	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;

			else if (name.equals("From_Date")) {
				FromDate = ((Timestamp) para[i].getParameter());
				
			} else if (name.equals("To_Date")) {
				ToDate = ((Timestamp) para[i].getParameter());
				
			}else if ( name.equals("C_BPartner_ID")) {
				customerID = (BigDecimal) para[i].getParameter();
				
			}else if ( name.equals("C_Project_ID")) {
				project_ID = (BigDecimal) para[i].getParameter();
				
			}else if ( name.equals("Employee")) {
				employeeID = (BigDecimal) para[i].getParameter();
				
			}
		}

	}

	protected String doIt() throws Exception {

		String msg =Msg.getMsg(getCtx(), EagleMessageConstants.TIME_SHEET_IS_EXPORTING_SUCCESS_FULLY);
		
		if(FromDate != null && ToDate != null) {
			
			Date dateFrom = new Date( FromDate.getTime() ); 
			Date dateTo = new Date( ToDate.getTime() );   
	
			
			//Date Object Format Preparation
			SimpleDateFormat  dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String fromDate = dateFormat.format(dateFrom);
			String toDate   = dateFormat.format(dateTo);
	
			
			//[20111223:9:00PM]
			//Report Name Should Not Exceed 30 , Work sheet Can't Take More Than That
			String name = "Timesheet"+ ".xls";
	
			String location = "TimeSheet_" + fromDate + "to" + toDate + ".xls";
			if (name.length() > 30)
				name = name.substring(0);
	
			HSSFWorkbook wb = new HSSFWorkbook();
			
			HSSFSheet sheet = wb.createSheet(name);
			
			//[20111223:5:00]
			//This Method Will PrepareTimeSheet Columns
			prepareSheetColumns(sheet);
			
			//Font Style
		    HSSFFont font = wb.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeight((short)400);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	
			HSSFCellStyle fontStyle = wb.createCellStyle();
			fontStyle.setFont(font);
		
			//This Method Will Create Report Head
			createReportHeading (sheet , fontStyle ,fromDate ,toDate );
			
			//This Method Will Create The Column Header
			createColumnHeader (sheet , wb);
			
			//This Method will save a file in the specified location with all time sheet data
			createTimeSheetFile(sheet,wb,location);
		
		}else {
			msg = Msg.getMsg(getCtx(), EagleMessageConstants.FROM_DATE_AND_TO_DATE_ARE_MANDATORY);
		}
		
		return msg;
	}
	
	
	/**
	 * 
	 * @param sheet
	 * @param wb
	 * @param location
	 */
	private void createTimeSheetFile(HSSFSheet sheet, HSSFWorkbook wb,	String location) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String timeSheetSql = prepareQuery();
			
			ps = DB.prepareStatement(timeSheetSql, get_TrxName());

			rs = ps.executeQuery();
			int index = 5;

			while (rs.next()) {
				
				HSSFRow rows = sheet.createRow((short) index);
				rows.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));

				//This Method will fill the data in to created row.
				fillTimeSheetDetails(rs,rows,wb);

				index++;
			}
			
			HSSFPrintSetup print = sheet.getPrintSetup();
			print.setFitWidth((short) 1);
			sheet.setAutobreaks(false);
			sheet.autoSizeColumn((short) 2);
			sheet.setProtect(false);
			FileOutputStream fileOut = null;
			fileOut = new FileOutputStream(location);
			wb.write(fileOut);
			fileOut.flush();
			File file = new File(location);
			AMedia media = new AMedia(file, null, null);
			Filedownload.save(media);

		} 
		catch (Exception e) {
			log.log(Level.SEVERE, "Timesheet_" + FromDate + "to" + ToDate
					+ ".xls Exporting.....", e);
		}
		finally {
			DB.close(rs, ps);
		}
	}

	private void prepareSheetColumns(HSSFSheet sheet) {
		
		sheet.setColumnWidth((short)0, (short)6000);
		sheet.setColumnWidth((short)1, (short)6000);
		sheet.setColumnWidth((short)2, (short)6500);
		sheet.setColumnWidth((short)3, (short)6000);
		sheet.setColumnWidth((short)4, (short)6000);
		sheet.setColumnWidth((short)5, (short)6000);
		sheet.setColumnWidth((short)6, (short)7000);
		
		Region  region = new Region(0, (short) 0,3, (short) 6);
		sheet.addMergedRegion(region);
	}

	/**
	 * This Method Will Prepare Query According To In put Parameters
	 * @return
	 */
	private String prepareQuery() {

		//[20120106:10:05]
		//Query Is changed in a way that fallows  Adempiere coding guidelines.
		String whereClause = " ";
		if(customerID.compareTo(Env.ZERO) == 1) {
			whereClause = " AND  cp."+MProject.COLUMNNAME_C_BPartner_ID+" = "+customerID ;
		}
		
		if( employeeID.compareTo(Env.ZERO) == 1) {
			whereClause = " AND ts."+MTimeSheet.COLUMNNAME_C_BPartner_ID +"= "+employeeID ;
		}
		
		if ( project_ID.compareTo(Env.ZERO) == 1 ) {
			whereClause = " AND cp."+MProject.COLUMNNAME_C_Project_ID+" = "+project_ID ;
		}

		//[20120109:9:00PM]
       
		String timeSheetSql = "SELECT cp."+MBPartner.COLUMNNAME_C_BPartner_ID +",cp."+MBPartner.COLUMNNAME_Name+
									  ", ts."+MTimeSheet.COLUMNNAME_C_BPartner_ID+" ,tsl."+MTimesheetLine.COLUMNNAME_timesheetdate+
		                              ", tsl."+MTimesheetLine.COLUMNNAME_noofhours+", tsl."+MTimesheetLine.COLUMNNAME_Comments+" "+
		                              " FROM "+MTimesheetLine.Table_Name +" tsl  "+
		                              " JOIN "+MTimeSheet.Table_Name + " ts ON ts."+MTimeSheet.COLUMNNAME_HR_Time_Sheet_ID +"="+
		                                                                      "tsl."+MTimesheetLine.COLUMNNAME_HR_Time_Sheet_ID+" "+
		                              " JOIN "+MProject.Table_Name+" cp ON cp."+MProject.COLUMNNAME_C_Project_ID+
		                              										  " = tsl."+MTimesheetLine.COLUMNNAME_C_Project_ID+" "+
		                              " JOIN "+MBPartner.Table_Name+" bp ON bp."+MBPartner.COLUMNNAME_C_BPartner_ID+
		                              										  " = ts."+MTimeSheet.COLUMNNAME_C_BPartner_ID+" "+										  
		                              " WHERE  ( tsl."+MTimesheetLine.COLUMNNAME_timesheetdate +" BETWEEN ('"+FromDate+"')  AND ('"+ToDate+"') ) "+
		                              " AND bp."+MBPartner.COLUMNNAME_IsEmployee+"='Y' "+whereClause +" "+
		                              " ORDER BY tsl."+MTimesheetLine.COLUMNNAME_timesheetdate+" ";
		//[20120111:2:00PM]
		//For Understaing the Query I am Giving This Values.
		//SELECT cp.C_BPartner_ID,cp.Name, ts.C_BPartner_ID ,tsl.timesheetdate, tsl.noofhours,
		//       tsl.Comments  FROM HR_Timesheet_Line tsl   
		//JOIN HR_Time_Sheet ts ON ts.HR_Time_Sheet_ID=tsl.HR_Time_Sheet_ID  
		//JOIN C_Project cp ON cp.C_Project_ID = tsl.C_Project_ID  
		//JOIN C_BPartner bp ON bp.C_BPartner_ID = ts.C_BPartner_ID  
		//WHERE  ( tsl.timesheetdate BETWEEN ('2012-01-01 00:00:00.0')  AND ('2012-01-15 00:00:00.0') )  
	    //	 AND bp.IsEmployee='Y'  
		//   AND ts.C_BPartner_ID= 113  
		//ORDER BY tsl.timesheetdate 
		//
		
		return timeSheetSql;
	}

	
	
	/**
	 * This Method Will Create Report Head
	 * @param sheet
	 * @param fontStyle
	 * @param fromDate
	 * @param toDate
	 * @throws SQLException 
	 */
	private void createReportHeading(HSSFSheet sheet , HSSFCellStyle fontStyle ,String fromDate ,String toDate) {
		//Report Heading
		HSSFRow headingRow = sheet.createRow((short) 0);
		headingRow.setHeightInPoints((7 * sheet.getDefaultRowHeightInPoints()));

		HSSFCell headingCell = headingRow.createCell((short) 0);
		
		String customerName = new String("ALL");
		if(customerID.compareTo(Env.ZERO) == 1) {
			MBPartner  customer = new MBPartner(getCtx(), customerID.intValue(), get_TrxName());
			if(customer != null) {
				customerName = new String(customer.getName());
			}
		}
		
		String employee = "ALL";
		if( employeeID.compareTo(Env.ZERO) == 1) {
			MBPartner  employeeObj= new MBPartner(getCtx(), employeeID.intValue(), get_TrxName());
			if (employeeObj != null) {
				employee = employeeObj.getName();
			}
		}
		
		String projectName = "ALL";
		if ( project_ID.compareTo(Env.ZERO) == 1 ) {
			MProject project= new MProject(getCtx(), project_ID.intValue(), get_TrxName());
			if (project != null) {
				projectName =project.getName();
			}
		}
		
		String fromDateHeading = Msg.getMsg(getCtx(), EagleConstants.FROMDATE_HEADING);
		String toDateHeading = Msg.getMsg(getCtx(), EagleConstants.TODATE_HEADING);
		
		
		
		Date dateFrom = new Date( FromDate.getTime() ); 
		Date dateTo = new Date( ToDate.getTime() );  
		
		Integer numberofWorkingDays  = new Integer(0);
			
		//[20111223:8:00PM]
		//Getting the number of working days
		PreparedStatement ps=null;
		ResultSet rs=null;
		String numberofWorkingDaysSql = "SELECT getnumberofworkingdays(?,?)";

		try	{
			 ps=DB.prepareStatement(numberofWorkingDaysSql, this.get_TrxName());
		     ps.setDate(1,dateFrom);
		     ps.setDate(2,dateTo);
		     
		     rs=ps.executeQuery();
		     
		     if(rs != null) { 
		    	 while(rs.next()) {
		    		 numberofWorkingDays  =  rs.getInt(1); 
	    	   }
		     }
		}catch (Exception e) {
             e.printStackTrace();  
		}finally {
			DB.close(rs,ps);
		}
		
		numberofWorkingDays = numberofWorkingDays == null ? ( new Integer(0) ) : numberofWorkingDays ;
		
		String headingText = Msg.getMsg(getCtx(), "TIME_SHEET_HEADING" , new Object[] {fromDateHeading,
																					   toDateHeading,
			                                                                           fromDate , 
																					   toDate , 
																					   customerName , 
			 																		   new String(projectName), 
			 																		   new String(employee),
			 																		   numberofWorkingDays.toString()});
		headingCell.setCellValue(new HSSFRichTextString(headingText));
		headingCell.setCellStyle(fontStyle);
	}
	
	
	
	
	/**
	 * This Method Will Create The Column Header
	 * @param sheet
	 * @param wb
	 */
	private void createColumnHeader(HSSFSheet sheet ,HSSFWorkbook wb  ) {
		
		//Headers Definition
		HSSFFont fontHeader = wb.createFont();
		fontHeader.setFontName(HSSFFont.FONT_ARIAL);
		fontHeader.setFontHeight((short)200);
		//fontHeader.setColor((short)HSSFColor.GREEN.index);
		fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(fontHeader);
		headerStyle.setFillBackgroundColor((short)HSSFColor.YELLOW.index);
		headerStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		
		HSSFRow row1 = sheet.createRow((short) 4);
		row1.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
		
		HSSFCell dataCell2 = row1.createCell((short) 0);
		dataCell2.setCellStyle(headerStyle);
		dataCell2.setCellValue(new HSSFRichTextString("Customer Name "));

		HSSFCell dataCell3 = row1.createCell((short) 1);
		dataCell3.setCellStyle(headerStyle);
		dataCell3.setCellValue(new HSSFRichTextString("Project Name"));

		HSSFCell dataCell4 = row1.createCell((short) 2);
		dataCell4.setCellStyle(headerStyle);
		dataCell4.setCellValue(new HSSFRichTextString("Employee Name   "));

		HSSFCell dataCell5 = row1.createCell((short) 3);
		dataCell5.setCellStyle(headerStyle);
		dataCell5.setCellValue(new HSSFRichTextString("Date (DD/MM/YYYY)"));

		HSSFCell dataCell6 = row1.createCell((short) 4);
		dataCell6.setCellStyle(headerStyle);
		dataCell6.setCellValue(new HSSFRichTextString("Time Sheet (Hours)"));

		HSSFCell dataCell7 = row1.createCell((short) 5);
		dataCell7.setCellStyle(headerStyle);
		dataCell7.setCellValue(new HSSFRichTextString("Attendance (Hours)"));
		
		HSSFCell dataCell8 = row1.createCell((short) 6);
		dataCell8.setCellStyle(headerStyle);
		dataCell8.setCellValue(new HSSFRichTextString("Remarks "));

	}
	
	
	
	/**
	 * This Method Will Fill The Data Getting From The Result set
	 * @param rs
	 * @param rows
	 * @param wb
	 */
	private void fillTimeSheetDetails(ResultSet rs, HSSFRow rows ,HSSFWorkbook wb){
		HSSFCell cell = null;
		
		
		//Unlock Style
		HSSFCellStyle unLocked = wb.createCellStyle();
		unLocked.setWrapText(true);
		unLocked.setLocked(false);
		
		//Date Style
		String datePatern =  "dd/m/yy";
		HSSFCellStyle dateStyle = wb.createCellStyle();
		dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(datePatern));
		dateStyle.setWrapText(true);
		
		
		
		try {
			//Customer
			String customerName= "";
			int customerID = rs.getInt(1);
				if (customerID > 0) {
				MBPartner customer = new MBPartner(getCtx(), customerID , this.get_TrxName());
				if(customer != null) {
					customerName = customer.getName();
				}
			}
			cell = rows.createCell((short) 0);
			cell.setCellValue(new HSSFRichTextString(customerName));   
			cell.setCellStyle(unLocked);
			
			
			//Project Name
			cell = rows.createCell((short) 1);
			cell.setCellValue(new HSSFRichTextString(rs.getString(2)));
			cell.setCellStyle(unLocked);
	
			
			
			//Employee Name
			
			int EmployeeID = rs.getInt(3);
			
			String EmployeeName = "";
			if(EmployeeID > 0) {
				MBPartner employee = new MBPartner(getCtx(), EmployeeID, get_TrxName());
				if (employee != null ) {
					EmployeeName = employee.getName();
				}
			}
			cell = rows.createCell((short) 2);
			cell.setCellValue(new HSSFRichTextString(EmployeeName));
			cell.setCellStyle(unLocked);
	
			//Time Sheet Line Date
			Timestamp timeSheetLineDate = rs.getTimestamp(4);
			java.sql.Date timesheetLineDate = new Date(timeSheetLineDate.getTime());		 
			cell = rows.createCell((short) 3);
			cell.setCellValue(new HSSFRichTextString(timesheetLineDate.toString()));
			cell.setCellStyle(dateStyle);
	
			
			//Time Sheet Line Number Of Hours
			cell = rows.createCell((short) 4);
			cell.setCellValue(rs.getBigDecimal(5).doubleValue());
			cell.setCellStyle(unLocked);
	
			//Attendance Hours
			//We Can't Estimate The Number Of hours Should Be Taken From Gate Attendance Or Supervisor attendance.
			//Because There may be a chance of Change System Configuration Variable After This Time Sheet
			//So First Try According To System Configuration Then After Go For Remaining.
			
			String   useSupAttendace = MSysConfig.getValue(EagleConstants.USE_ATTENDANCE_BY_SUPERVISOR, "N");
			
			timeSheetLineDate = TimeUtil.trunc(timeSheetLineDate, null);
			String whereClause = X_HR_Emp_Gate_Attendence.COLUMNNAME_C_BPartner_ID+" = "+EmployeeID + " AND " +
								 X_HR_Emp_Gate_Attendence.COLUMNNAME_workdate +"= '"+timeSheetLineDate+"'";
			
			BigDecimal hours = Env.ZERO;
			
			if(useSupAttendace.equalsIgnoreCase("N")) {
				
				X_HR_Emp_Gate_Attendence   gateAttendence = new Query(getCtx(),
																	   X_HR_Emp_Gate_Attendence.Table_Name, 
																	   whereClause, 
																	   get_TrxName()).first();
				if (gateAttendence != null) {
					Timestamp incomingTime = gateAttendence.getincomingtime();
					Timestamp outGoingTime = gateAttendence.getoutgoingtime();
					
					// [20120111:9:00]
					 hours =gateAttendence.getnumberofhours();
					 //WTCTimeUtil.getHoursBetween(incomingTime , outGoingTime );
				}
				
				
			}else {
				X_HR_Emp_Sup_Attendence   supAttendance = new Query(getCtx(),
																	X_HR_Emp_Sup_Attendence.Table_Name, 
						   											whereClause, 
						   											get_TrxName()).first();
				
				hours = supAttendance.getnumberofhours();
			}
			
			if(hours == null) {
				hours =Env.ZERO;
			}
			cell = rows.createCell((short) 5);
			cell.setCellValue(hours.doubleValue() );
			cell.setCellStyle(unLocked);
	
			//Time sheet line Comments
			String remarks = rs.getString(6);
			if (remarks == null) {
				remarks ="";
			}
			cell = rows.createCell((short) 6);
			cell.setCellValue(new HSSFRichTextString(remarks));
			cell.setCellStyle(unLocked);
	
		}catch (Exception e) {
			log.log(Level.SEVERE, "Can't Get Values From Resultset :");
		}
	}
}
