package org.adempiere.webui.process;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.adempiere.exceptions.AdempiereException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.compiere.model.EagleSystemconfiguratorConstants;
import org.compiere.model.MBPartner;
import org.compiere.model.MProject;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTimeSheet;
import org.compiere.model.MTimesheetLine;
import org.compiere.model.MWorkShift;
import org.compiere.model.Query;
import org.compiere.model.X_HR_Emp_Gate_Attendence;
import org.compiere.model.X_HR_Emp_Sup_Attendence;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.wtc.util.EagleMessageConstants;
import org.wtc.util.TimeSheetManager;
import org.wtc.util.WTCEmployeeUtil;
import org.wtc.util.WTCTimeUtil;
import org.wtc.util.WTCUtil;
/**
 * 
 * @Bug      @author     @ChangeID    		@Discription
 * ****************************************************************************************************************************
 * 1630      Arunkumar                		Initial Check In 
 * 1630      Arunkumar  [20111223:4:00]	 	Checking For File Extensions
 * 1630      Arunkumar  [20111231:4:00]     Import Time sheet should create time sheet if doesnot exists
 *                             
 * 1630      Arunkumar  [20111231:4:30]     Import Time sheet should ask for Project Task
 * 
 * 1630      Arunkumar  [20111231:4:30]     For This We Have Given Project Task Id  
 *                              
 * 1630      Arunkumar  [20111231:5:00]     Change message when Import file format is not matched.
 *								            like File format does not matched. Upload file of format (.xls and etc... )
 * 1630      Arunkumar  [20120106:5:00]     Resolved Format Problem			
 * 1630      Arunkumar  [20120106:5:05]		truncate the timeSheetFinalDate	 to only date	   
 * 1630      Arunkumar  [20120107:4:30]     Modified : This Method Will Get The Planned Work Shift , 
 *											It  will Get 0 If THere is No Work Shift Planned For The Given Date .
 *
 * 2490      Arunkumar  [20120316:2:00]     Modified :  createTimeSheetLine(Timestamp, MBPartner, int, BigDecimal, BigDecimal, Timestamp, Timestamp, Integer, String, int)
 *                                            We have an issue ‘Time sheet’ is importing for an Employee after approval of supervisor. 
 *                                            According to this issue we should not import the records which are having the data in time sheets which are already completed (Approved).
 * 2490      Arunkumar  [20120317:2:30]     Added Method : getTimeStampFromHssfStringCell(HSSFCell)
 * 											    This method is useful to get the date from string format cells                                                  
 */
public class TimeSheetImport extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: TimeSheetImport.java 1164 2012-03-17 11:53:38Z arun $";
	public String path = "";
	private int insertCount = 0;
	private int skipCount = 0;

	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equalsIgnoreCase("UploadFile"))
				path = (String) para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter ....." + name);
		}
	}

	protected String doIt() throws Exception {

		if (path == null) {
			String message = Msg.getMsg(Env.getCtx(), "TIME_SHEET_PATH_NOT_AVAILABLE");
			throw new AdempiereException(message);
		}

		File file = new File(path);

		if (!file.exists()) {
			String message = Msg.getMsg(Env.getCtx(), "TIME_SHEET_DOES_NOT_EXISTS");
			throw new AdempiereException(message);
		}

		if (!file.canRead()) {
			String message = Msg.getMsg(Env.getCtx(), "TIME_SHEET_FILE_READ_ONLY");
			throw new AdempiereException(message);
		}

		if (!file.isFile()) {
			String message = Msg.getMsg(Env.getCtx(), "TIME_SHEET_FILE_IS_NOT_A_FILE");
			throw new AdempiereException(message);
		}

		if (file.length() <= 0L) {
			String message = Msg.getMsg(Env.getCtx(), "TIME_SHEET_FILE_IS_ZERO_LENGTH");
			throw new AdempiereException(message);
		}

		//[20111223:4:00]
		//Checking For File Extensions
		if (file != null) {
			String filename = file.getName();
			if (filename != null) {
				int extensionIndex = filename.lastIndexOf(".");
				String extension = filename.substring(extensionIndex+1);
				
				String extensionFile =MSysConfig.getValue(EagleSystemconfiguratorConstants.ACCEPTED_FILE_EXTANSION_IN_TIMESHEET_IMPORT,"xls");
				
				if(! extensionFile.contains(extension.trim())) {
					String msg = Msg.getMsg(getCtx(), EagleMessageConstants.FILE_EXTENSION_NOT_MATCH , new Object[] {extensionFile});
					throw new AdempiereException(msg);
				}
			}
		}
		
		
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;

		List<List<HSSFCell>> cellDataList = new ArrayList<List<HSSFCell>>();

		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
			sheet = workbook.getSheetAt(0);
			Iterator<?> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				HSSFRow hssfRow = (HSSFRow) rowIterator.next();
				Iterator<?> iterator = hssfRow.cellIterator();
				List<HSSFCell> cellTempList = new ArrayList<HSSFCell>();
				while (iterator.hasNext()) {
					HSSFCell hssfCell = (HSSFCell) iterator.next();
					cellTempList.add(hssfCell);
				}
				cellDataList.add(cellTempList);
			}
			updateCellForDB(cellDataList);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed To Import Record" + e.getMessage());
		}

		String message = Msg.getMsg(Env.getCtx(), "IMPORTED_RECORD_MSG",	new Object[] { insertCount, skipCount });
		return message;

	}

	/**
	 * 
	 * @param cellDataList
	 */
	public void updateCellForDB(List<List<HSSFCell>> cellDataList) {
	
		for (int i = 3; i < cellDataList.size(); i++) {
			List<?> cellTempList = (List<?>) cellDataList.get(i);

			if (cellTempList.size() == 9 && i >= 3) {

				//This Method is Responsible For Validating The Input values
				validateInputValues(cellTempList , i);
			}
		}
	}
	
	
	/**
	 * This Method is Responsible For Validating The Input values
	 * @param cellTempList
	 * @param rowNumber
	 */
	private void validateInputValues(List<?> cellTempList , int rowNumber) {
		
		//Project 
		int projectID = 0;
		MProject project  = validateProject(cellTempList);
		if (project != null) {
			projectID= project.getC_Project_ID();
		}
		
		//Employee
		MBPartner employee = validatingEmployee( cellTempList);
	
		
		//Time Sheet Hours
		//[20111231:4:30]
		Integer projectTask = 0;
		if (cellTempList.get(2).toString().isEmpty()) {
			projectTask = 0;
			//[20120106:5:00]  //Resolved Format Problem     
			String msg = Msg.getMsg(getCtx(), 
	                EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
	                new Object[] {EagleConstants.TIMESHEET_IMPORT_PROJECT_TASK});
					throw new AdempiereException(msg);
		}else {
			//IT will Give Number format exception if we Use Integer.parse(string) directly 
			//Because USer Gives 100000 it takes it as 100000.0 So IT Can't parse.
			String taskID = cellTempList.get(2).toString();
			Double taskDoubleID = Double.valueOf(taskID);
			projectTask = taskDoubleID.intValue();
		}
		
		//TimeSheet Date
		Timestamp timeSheetDate   = validateTimeSheetDate(cellTempList);
		
		//Time Sheet Hours
		ArrayList<Timestamp>  timesheetDates = validateTimeSheetHours(cellTempList);
		Timestamp  fromTime = null;
		Timestamp  toTime   =  null;
		BigDecimal timesheethours = Env.ZERO;
		if(timesheetDates != null && ( ! timesheetDates.isEmpty() )) {
			  fromTime = timesheetDates.get(0);
			  toTime   = timesheetDates.get(1);
			if(fromTime !=null && toTime != null) {
				
				if ( fromTime.after(toTime)){
					GregorianCalendar gre=(GregorianCalendar) Calendar.getInstance();
					  gre.setTimeInMillis(toTime.getTime());
					  gre.add(Calendar.DAY_OF_MONTH, 1);
					  toTime= new Timestamp(gre.getTimeInMillis());
				}
				
				timesheethours =  WTCTimeUtil.getHoursBetween(fromTime, toTime);
			}else {
				log.log(Level.SEVERE, "From date and To Date Not Available");
			}
		}else {
			log.log(Level.SEVERE, "From date and To Date Not Available");
		}
        if(timesheethours == null) {
        	timesheethours =Env.ZERO;
        }
		
		//Attendance Hours
		BigDecimal attendanceHours = Env.ZERO;
		if (cellTempList.get(7).toString().equalsIgnoreCase("0")) {
			attendanceHours = Env.ZERO;
		}else {
			attendanceHours = new BigDecimal(cellTempList.get(7).toString());
		}

		//Comment
		String commentCell = cellTempList.get(8).toString();
	
		try {
			//This Method Will Create Time Sheet Line Entry According To The Given Date And Employee
			createTimeSheetLine(timeSheetDate , employee , projectID , attendanceHours ,timesheethours ,fromTime , toTime, projectTask ,commentCell , rowNumber );
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"TimeSheet Importing not successfull.....", e);
		}
	}
	
	

	/**
	 * This Method Validate the Given From time and to time and get the number of hours between the two 
	 * times and return That value
	 * @param cellTempList
	 * @return
	 */
	private ArrayList<Timestamp> validateTimeSheetHours(List<?> cellTempList) {
		
		ArrayList<Timestamp>  timesheetDates = new ArrayList<Timestamp>();
		
		
		HSSFCell timeSheetFromTime = (HSSFCell) cellTempList.get(5); 

		HSSFCell timeSheetToTime   = (HSSFCell) cellTempList.get(6); 
		
		if(timeSheetFromTime == null) {
			String msg = Msg.getMsg(getCtx(), 
					                EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
					                new Object[] {EagleConstants.TIMESHEET_IMPORT_FROMTIME});
			throw new AdempiereException(msg);
		}

		if(timeSheetToTime == null) {
			String msg = Msg.getMsg(getCtx(), 
					                EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
					                new Object[] {EagleConstants.TIMESHEET_IMPORT_TOTIME});
			throw new AdempiereException(msg);
		}
		
		
		
		Timestamp  fromTime = getTimeStampFromHssfDateCell(timeSheetFromTime);
		Timestamp  toTime   = getTimeStampFromHssfDateCell(timeSheetToTime);
		
		
		if(fromTime !=null && toTime != null) {
			timesheetDates.add(0,fromTime);
			timesheetDates.add(1,toTime);
		}else {
			String msg = Msg.getMsg(getCtx(), 
	                EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
	                new Object[] {EagleConstants.TIMESHEET_IMPORT_TIME_SHEET_DATE});
			throw new AdempiereException(msg);
		}
		
		return timesheetDates;
	}

	

	/**
	 * This Method Will Return Either Project Object Or Adempiere Exception With proper Msg
	 * @param cellTempList
	 * @return
	 */
	private MProject validateProject(List<?> cellTempList) {
		//Project
		MProject project= null; 
		String projectName = cellTempList.get(1).toString();
		if (projectName != null) {
			String whereClause ="lower("+MProject.COLUMNNAME_Name +") =lower('"+projectName.toLowerCase()+"')";
			project= new Query(getCtx(), MProject.Table_Name, whereClause, get_TrxName()).first();
			if (project == null) {
				String msg = Msg.getMsg(getCtx(), 
		 								EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS, 
		 								new Object[] {EagleConstants.TIMESHEET_IMPORT_PROJECT_NAME});
					throw new AdempiereException(msg);
			}
		}else {
			String msg = Msg.getMsg(getCtx(), 
					 				EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS, 
					 				new Object[] {EagleConstants.TIMESHEET_IMPORT_PROJECT_NAME});
			throw new AdempiereException(msg);
		}
		
		return project;
		
	}
	
	
	/**
	 * This Method Will Give An Employee object
	 * @param cellTempList
	 * @return
	 */
	private MBPartner validatingEmployee(List<?> cellTempList) {
		
		MBPartner employee=null;
		String employeeName = cellTempList.get(3).toString();
		if (employeeName != null) {
			String whereClause ="lower("+MBPartner.COLUMNNAME_Name +") =lower('"+employeeName.toLowerCase()+"')";
			employee= new Query(getCtx(), MBPartner.Table_Name, whereClause, get_TrxName()).first();
			if(employee == null) {
				String msg = Msg.getMsg(getCtx(),
										EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
										new Object[] {EagleConstants.TIMESHEET_IMPORT_EMPLOYEE_NAME});

						throw new AdempiereException(msg);
			}
		}else {
			String msg = Msg.getMsg(getCtx(),
									EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
									new Object[] {EagleConstants.TIMESHEET_IMPORT_EMPLOYEE_NAME});
			
			throw new AdempiereException(msg);
		}
		
		return employee;
	}
	
	
	/**
	 * This Method Will validate Time Sheet Date
	 * @param cellTempList
	 * @return timeSheetDate  of Type HSSFCell  if it is not A Null Value Other Wise IT is An Exception
	 */
	private Timestamp validateTimeSheetDate(List<?> cellTempList) {
		
		
		HSSFCell timeSheetDate = (HSSFCell) cellTempList.get(4);

		if(timeSheetDate == null) {
			String msg = Msg.getMsg(getCtx(), 
					                EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
					                new Object[] {EagleConstants.TIMESHEET_IMPORT_TIME_SHEET_DATE});
			throw new AdempiereException(msg);
		}
		
		
		//Get The Timstamp object Fromm HSSF Cell
		Timestamp timeSheetFinalDate = getTimeStampFromHssfStringCell(timeSheetDate);
		
		if(timeSheetFinalDate == null) {
			String msg = Msg.getMsg(getCtx(), 
	                EagleConstants.TIMESHEET_IMPORT_MANDATORY_FIELDS ,
	                new Object[] {EagleConstants.TIMESHEET_IMPORT_TIME_SHEET_DATE});
				throw new AdempiereException(msg);
		}
		
		return timeSheetFinalDate;
	}
	
	/**
	 * [20120317:2:30]
	 * This method is useful to get the date from string format cells 
	 * @param timeSheetDate
	 * @return
	 */
    @SuppressWarnings("deprecation")
	private Timestamp getTimeStampFromHssfStringCell(HSSFCell timeSheetDate) {
    	
    	Date theDate = null;
    	String dateFormatImport = MSysConfig.getValue(EagleSystemconfiguratorConstants.DATE_FORMAT_TIMESHEET_IMPORT);
    	
		try {
			SimpleDateFormat  importDateFormat=new SimpleDateFormat(dateFormatImport);
			String dateValue = timeSheetDate.getStringCellValue();	
			theDate = (Date)importDateFormat.parse(dateValue);
		}catch (Exception e) {
			log.log(Level.SEVERE, "Can't able to get the date from given format , please verify format of the cell");
		}
		
		Timestamp timeSheetFinalDate = null;
		if(theDate != null) {
			timeSheetFinalDate = new Timestamp(theDate.getTime());
		}
		
		return timeSheetFinalDate;
    }
    
	
	/**
	 * This Will prse The Given Date In To Specific Date Format And Returns Timestamp
	 * Retruns Null If Not Posible To Parse
	 * @param timeSheetDate
	 * @return
	 */
    private Timestamp getTimeStampFromHssfDateCell(HSSFCell timeSheetDate) {
		
		Date theDate = null;
		theDate = timeSheetDate.getDateCellValue();
		Timestamp timeSheetFinalDate = null;
		if(theDate != null) {
			timeSheetFinalDate = new Timestamp(theDate.getTime());
		}
		
		return timeSheetFinalDate;
	}
	
	
	/**
	 * This Method Will Create Time Sheet Line Entry According To The Given Date And Employee
	 * @param timeSheetDate
	 * @param employee
	 * @param projectID
	 * @param attendanceHours
	 * @param timesheethours
	 * @param commentCell
	 */
	private void createTimeSheetLine(Timestamp timeSheetFinalDate ,
									 MBPartner employee ,
									 int  projectID ,
									 BigDecimal attendanceHours ,
									 BigDecimal timesheethours ,
									 Timestamp fromTime,
									 Timestamp toTime,
									 Integer projectTask,
									 String commentCell ,
									 int rowNumber) {
		try {
			
			//[20120106:5:05] //truncate the timeSheetFinalDate
			timeSheetFinalDate = TimeUtil.trunc(timeSheetFinalDate, null);
			//We Are Checking For Not Null Before This Method Call So No Need To Check Again
			int employeeID = 0 ;
			if(employee != null) {
				employeeID = employee.getC_BPartner_ID();
			}
						
			String whereClause  = MTimeSheet.COLUMNNAME_C_BPartner_ID +" = "+employeeID+" AND "+
								  MTimeSheet.COLUMNNAME_FromDate +"<='"+timeSheetFinalDate+"' AND "+
								  MTimeSheet.COLUMNNAME_ToDate +">='"+timeSheetFinalDate+" '";
			MTimeSheet  timesheet = new Query(getCtx(), MTimeSheet.Table_Name, whereClause, get_TrxName()).first();
			
			
			if(timesheet == null) {

				// This Will Create A Timesheet 
				timesheet = createTimeSheetForPeriod(employeeID,  timeSheetFinalDate );
			}
			
			if(timesheet != null) {
				
				//2490 [20120316:2:00]
				if(timesheet.isProcessed()){
				   log.log(Level.INFO, "Time Sheet for the period is already completed so Skipping this Line");
				   skipCount++;
				   return ;
				}
				
				MTimesheetLine  timesheetLine = new MTimesheetLine(getCtx(), 0, get_TrxName());
				timesheetLine.setC_Project_ID(projectID);
				timesheetLine.setHR_Time_Sheet_ID(timesheet.getHR_Time_Sheet_ID());
				timesheetLine.settimesheetdate(timeSheetFinalDate);
				timesheetLine.setfromtime(fromTime);
				timesheetLine.settotime(toTime);
				timesheetLine.setC_ProjectTask_ID(projectTask);
				timesheetLine.setnoofhours(timesheethours);
				timesheetLine.setComments(commentCell != null ? commentCell : "");
				if(! timesheetLine.save()) {
					log.log(Level.SEVERE, "TimeSheet Line Is Not Saved For ROW Nummber "+(rowNumber+1));
					skipCount++;
				}
				else {
					
					//This Method Will Create Attendance For This Time Sheet Date For This Employee
					createAttendaceForTheDay(employee ,  attendanceHours ,  timeSheetFinalDate);
					
					insertCount++;	
				}
			}
			else {
				log.log(Level.SEVERE, "Time Sheet For The Date :  "+timeSheetFinalDate+" Does not exist.");
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, "Time Sheet Line Creation Failed Due To Parsing Of Date");
		}
	}
	
	/**
	 * [20111231:4:00]
	 * This method will create a Timesheet
	 * @param employeeID
	 * @param timeSheetFinalDate
	 * @return
	 */
	private MTimeSheet createTimeSheetForPeriod(int employeeID,
										  		Timestamp timeSheetFinalDate) {
		
		List<Calendar>  periodDates = new ArrayList<Calendar>();
		
			MTimeSheet   timesheet = new MTimeSheet(getCtx(), 0, get_TrxName());
			timesheet.setC_BPartner_ID(employeeID);
			Calendar  Timeperiod = Calendar.getInstance();
			Timeperiod.setTimeInMillis(timeSheetFinalDate.getTime());
			periodDates = TimeSheetManager.getTimeSheetPeriod(Timeperiod);
			if(periodDates != null && ( ! periodDates.isEmpty() ) ) {
				Calendar fromDateCal  = periodDates.get(0);
				Calendar toDateCal    = periodDates.get(1);
				if(fromDateCal != null && toDateCal != null) {
					Timestamp fromDate = new Timestamp(fromDateCal.getTimeInMillis());
					Timestamp toDate = new Timestamp(toDateCal.getTimeInMillis());
					timesheet.setFromDate(fromDate);
					timesheet.setToDate(toDate);
				}
			}else {
				log.log(Level.SEVERE, "From Date And To date Not Available");
			}
			
			timesheet.setDocStatus(DocAction.STATUS_Drafted);
			timesheet.setDocAction(DocAction.STATUS_Completed);
			timesheet.setWFState(EagleConstants.DOCUMENTSTATUS_REQUESTED_STATUS);
			
			Boolean success =  timesheet.save();
			if(! success) {
				log.log(Level.SEVERE, "Time Sheet Failed To save");
			}
			
         return timesheet; 
	}

	/**
	 * This Method Will Create Attendance For This Time Sheet Date For This Employee
	 * @param employee
	 * @param attendanceHours
	 * @param timeSheetFinalDate
	 */
	private void createAttendaceForTheDay(MBPartner   employee , 
			 							  BigDecimal attendanceHours ,
			 							  Timestamp timeSheetFinalDate) {
		int employeeID = 0;
		if(employee != null) {
			employeeID = employee.getC_BPartner_ID() ;
		
		    Boolean  isAttExist = WTCUtil.isAttandanceExistForGivenDate(timeSheetFinalDate , employeeID);
		
			if (! isAttExist) {
			
				String useSupAttendance = MSysConfig.getValue(EagleConstants.USE_ATTENDANCE_BY_SUPERVISOR, "N");
				if(useSupAttendance.equalsIgnoreCase("Y")) {
					
					
					X_HR_Emp_Sup_Attendence   attendance =new X_HR_Emp_Sup_Attendence(getCtx(),
							 															0, 
							 															get_TrxName());
					
					attendance.setC_BPartner_ID(employeeID);
					attendance.setSupervisor_ID(employee.getBPartner_Parent_ID());
					attendance.setworkdate(timeSheetFinalDate);
					attendance.setHR_Work_Group_ID(employee.getHR_Work_Group_ID());
					
					//[20120107:4:30]
					//This Method Will Get The Planned Work Shift , 
					//It  will Get 0 If THere is No Work Shift Planned For The Given Date
					int workshift = MWorkShift.getWorkShift(timeSheetFinalDate , employeeID, this.get_TrxName() );
					attendance.setPlan_Work_Shift_ID(workshift);
					attendance.setActual_Work_Shift_ID(workshift);
					attendance.setnumberofhours(attendanceHours);
					
					if (workshift > 0) {
						MWorkShift shift = new MWorkShift(getCtx(), workshift, get_TrxName());
						if(shift != null ) {
							Timestamp  fromTime =shift.getshiftfromtime();
							GregorianCalendar  calFrom = new GregorianCalendar();
							calFrom.setTimeInMillis(fromTime.getTime());
							calFrom.add(Calendar.HOUR_OF_DAY, attendanceHours.intValue());
							
							Timestamp toTime = new Timestamp(calFrom.getTimeInMillis());
							
							attendance.setincomingtime(fromTime);
							attendance.setoutgoingtime(toTime);
						}
					}
					
					
					if (! attendance.save() ) {
						log.log(Level.SEVERE, "Supervisor Attendance is Not Saved");
					}
					
				}else {
					X_HR_Emp_Gate_Attendence   attendance =new X_HR_Emp_Gate_Attendence(getCtx(),
																						0, 
																						get_TrxName());
					attendance.setC_BPartner_ID(employeeID);
					attendance.setworkdate(timeSheetFinalDate);
					attendance.setHR_Work_Group_ID(employee.getHR_Work_Group_ID());
					//[20120107:4:30]
					//This Method Will Get The Planned Work Shift , 
					//It  will Get 0 If THere is No Work Shift Planned For The Given Date
					int workshift = MWorkShift.getWorkShift(timeSheetFinalDate , employeeID, this.get_TrxName() );
					attendance.setPlan_Work_Shift_ID(workshift);
					attendance.setActual_Work_Shift_ID(workshift);
		
					if (workshift > 0) {
						MWorkShift shift = new MWorkShift(getCtx(), workshift, get_TrxName());
						if(shift != null ) {
							Timestamp  fromTime =shift.getshiftfromtime();
							GregorianCalendar  calFrom = new GregorianCalendar();
							calFrom.setTimeInMillis(fromTime.getTime());
							calFrom.add(Calendar.HOUR_OF_DAY, attendanceHours.intValue());
							
							Timestamp toTime = new Timestamp(calFrom.getTimeInMillis());
							
							attendance.setincomingtime(fromTime);
							attendance.setoutgoingtime(toTime);
						}
					}
					
					if (! attendance.save() ) {
						log.log(Level.SEVERE, "Gate Attendance is Not Saved");
					}
				}
			}
			else {
				log.log(Level.SEVERE, "Attandance Alreasy exist");
			}
		}else {
			log.log(Level.SEVERE, "Employee Is Not There So Can't Create Attandance");
		}
	}
	
}