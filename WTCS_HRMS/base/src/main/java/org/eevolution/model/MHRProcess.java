/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2007 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): Victor Perez www.e-evolution.com                           *
 *****************************************************************************/
package org.eevolution.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MEmpFine;
import org.compiere.model.MEmployeeInsurance;
import org.compiere.model.MLeaveAssign;
import org.compiere.model.MPaySlip;
import org.compiere.model.MPeriod;
import org.compiere.model.MQuartEmpCharges;
import org.compiere.model.MRule;
import org.compiere.model.MSalAdvEMI;
import org.compiere.model.MSalAdvReq;
import org.compiere.model.MSalStructure;
import org.compiere.model.MSysConfig;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.model.Scriptlet;
import org.compiere.model.X_HR_Designation;
import org.compiere.model.X_HR_Employee_Insurance;
import org.compiere.model.X_HR_Insurance_History;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.wtc.util.PayrollManager;
import org.wtc.util.WTCEmployeeUtil;
import org.wtc.util.WTCTimeUtil;

/**
 * HR Process Model
 *
 *  @author Arunkumar  Bug 1282   2nd sep 2011 We have Include New Concept PartialNetAmount 
 *                                        And This IS Subtracted From 
 *  @author oscar.gomez@e-evolution.com, e-Evolution http://www.e-evolution.com
 *			<li> Original contributor of Payroll Functionality
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 * 			<li> FR [ 2520591 ] Support multiples calendar for Org 
 *			@see http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962
 * @author Cristina Ghita, www.arhipac.ro
 * 
 * @author  	<a href="phani.gutha@gmail.com">PhaniKiran Gutha</a>  @date    Apr 26, 2011			 @Identifier		201104262158
 * 
 * @author Giri		@Date Wed Aug 24 18:24:50 2011		@BugNo: 1059	@Identifier 201108241321
 *									Description :-
 *									1. Modified below methods     
 *					getCasualLeavesConsumed(), getCasualLeaveBalance(), getEarnedLeaveConsumed(), getEarnedLeaveBalance() 
 *
 * @author Arunkumar @Date 13th sep 2011 @Bug  :  1282   
 *           If This Payroll Process Is General Then Only We Make This Partial Payroll 
 * 		     As Processed True
 *	
 * Modification history
 * 
 * Task No			Date			Identifier			Author			Change
 * ----------------------------------------------------------------------------------------------------
 * 1655				25/12/2011		251220110221		Ranjit			For any process type we need to calculate the LTA, DA
 * 
 * 1638             03/01/2011      [20120103:9:00PM]   Arunkumar       According to code review Comments ,
 *                                                                      Modified Method getAdvanceSalary()
 *                                                                      Do not fetch already paid emi's 
 *                                                                      but get isskippedpayslip entries to set to processed
 * 1638             04/01/2011      [20120104:9:00PM]   Arunkuamr       Removed Code For Making Payment , This Posting Is Handeled In 
 * 																		Payroll It Self.
 * 
 * 1655				05/01/2012		050120120303		Ranjit			1. Created method in PayrollManager for deleting the old mock movements based on the periodId
 * 																		2. Created method in PayrollManager for deleting the old salary slip entries for the mock 
 * 																		3. Code modified to get the employee arriers amount	
 * 																		4. Change the code to get the leave deduction amount for the employee
 * 																		5. 
 * 					
 * 1655				05/01/2012		050120120330		Ranjit			Default value changed to 30 instead of 0 at getInsurance method
 * 
 * 1655				6/1/2012		201201061252		Ranjit			1. Created method for the net salary 
 * 																		2. Created method for the leave encashment	
 * 																		3. Setting the extra salary in the field to tally the
 * 																			gross deductions, net salary & gross salary
 * 
 * 1655				06/01/2012		201201060720		Ranjit			1. Code removed in method processIt() as per the review comments
 * 																		2. 
 * 1655				7/1/2012		201201070806		Ranjit			1. Null pointer access at getHRA method
 * 
 * 1655				9/1/2012		201201090637		Ranjit			1. Adding the sum of power bill to variable
 * 
 *								  
 */
public class MHRProcess extends X_HR_Process implements DocAction
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: MHRProcess.java 1009 2012-02-09 09:16:13Z suman $";
	/**
	 * 
	 */
	private static final long serialVersionUID = 570699817555475782L;
	
	public int m_C_BPartner_ID = 0;
	public int m_HR_Period_ID =0;
	public int m_AD_User_ID = 0;
	public int m_HR_Concept_ID = 0;
	public WTCEmployeeUtil employeeUtil = null;
	public MHRPeriod period = null;
	public MHREmployee employee= null;
	public String m_columnType   = "";
	public Timestamp m_dateFrom;
	public Timestamp m_dateTo;
	/** HR_Concept_ID->MHRMovement */
	public Hashtable<Integer, MHRMovement> m_movement = new Hashtable<Integer, MHRMovement>();
	public MHRPayrollConcept[] linesConcept;
	
	private Double basicPercentage = null;
    private Double HRAPercentage   = null;
    private Double LTAPercentage   = null;
    private Double DAPercentage    = null;
    private Double earnedSalary    = null;
    private Double grossSalary	   = null;
    private Double basicSalary	   = null;
    private Double MDAPercentage   = null;
    private Double saPercentage	   = null;
    private Double caPercentage	   = null;

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MHREmployee.class);
	public static final String CONCEPT_PP_COST_COLLECTOR_LABOR = "PP_COST_COLLECTOR_LABOR"; // HARDCODED


	private static StringBuffer s_scriptImport = new StringBuffer(	 " import org.eevolution.model.*;" 
			+" import org.compiere.util.DB;"
			+" import java.math.*;"
			+" import java.sql.*;");

	public static void addScriptImportPackage(String packageName)
	{
		s_scriptImport.append(" import ").append(packageName).append(";");
	}

	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  HR_Process_ID    To load, (0 create new order)
	 */
	public MHRProcess(Properties ctx, int HR_Process_ID, String trxName) 
	{
		super(ctx, HR_Process_ID,trxName);
		if (HR_Process_ID == 0)
		{
			setDocStatus(DOCSTATUS_Drafted);
			setDocAction(DOCACTION_Prepare);
			setC_DocType_ID(0);
			set_ValueNoCheck ("DocumentNo", null);
			setProcessed(false);
			setProcessing(false);
			setPosted(false);
			setHR_Department_ID(0);
			setC_BPartner_ID(0);
		}
	}

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MHRProcess(Properties ctx, ResultSet rs, String trxName) 
	{
		super(ctx, rs,trxName);
	}	//	MHRProcess

	@Override
	public final void setProcessed(boolean processed)
	{
		super.setProcessed(processed);
		if (get_ID() <= 0)
		{
			return;
		}
		final String sql = "UPDATE HR_Process SET Processed=? WHERE HR_Process_ID=?";
		DB.executeUpdateEx(sql, new Object[]{processed, get_ID()}, get_TrxName());
	}	//	setProcessed

	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (getAD_Client_ID() == 0)
		{
			throw new AdempiereException("@AD_Client_ID@ = 0");
		}
		if (getAD_Org_ID() == 0)
		{
			int context_AD_Org_ID = getAD_Org_ID();
			if (context_AD_Org_ID == 0)
			{
				throw new AdempiereException("@AD_Org_ID@ = *");
			}
			setAD_Org_ID(context_AD_Org_ID);
			log.warning("Changed Org to Context=" + context_AD_Org_ID);
		}
		setC_DocType_ID(getC_DocTypeTarget_ID());

		return true;
	}       


	/**
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt(String processAction) { 
		
		// 201201060720
		
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(processAction, getDocAction());
	}	//	processIt

	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;


	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt() 
	{
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt


	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt() 
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt


	/**************************************************************************
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt() {
		
		log.info("prepareIt - " + toString());

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		
		if (m_processMsg != null){
			
			return DocAction.STATUS_Invalid;
		}

		 //	Std Period open?
		
		 period= MHRPeriod.get(getCtx(), getHR_Period_ID());
		
		 MPeriod.testPeriodOpen(getCtx(), getHR_Period_ID() > 0 ? period.getDateAcct():getDateAcct(), getC_DocTypeTarget_ID(), getAD_Org_ID());

		//	New or in Progress/Invalid
		 
		if (   DOCSTATUS_Drafted.equals(getDocStatus()) 	|| 
			   DOCSTATUS_InProgress.equals(getDocStatus()) 	|| 
			   DOCSTATUS_Invalid.equals(getDocStatus())		|| 
			   getC_DocType_ID() == 0)	{
			
			setC_DocType_ID(getC_DocTypeTarget_ID()); 
		}

		try {
			
			createMovements();
		} 
		catch (Exception e)	{
			
			throw new AdempiereException(e);
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		m_justPrepared = true;
		
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		
		return DocAction.STATUS_InProgress;
	}	//	prepareIt


	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	User Validation
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (m_processMsg != null)
		{
			return DocAction.STATUS_Invalid;
		}
		//
			setProcessed(true);	
			setDocAction(DOCACTION_Close);
			return DocAction.STATUS_Completed;

	}	//	completeIt

	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt() {
		return true;
	}	//	approveIt


	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt() {
		log.info("rejectIt - " + toString());
		return true;
	}	//	rejectIt

	/**
	 * 	Post Document - nothing
	 * 	@return true if success
	 */
	public boolean postIt() {
		log.info("postIt - " + toString());
		return false;
	}	//	postIt


	/**
	 * 	Void Document.
	 * 	Set Qtys to 0 - Sales: reverse all documents
	 * 	@return true if success
	 */
	public boolean voidIt() {
		log.info("voidIt - " + toString());
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt


	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		if (isProcessed())
		{
			log.info(toString());
			setProcessed(true);
			setDocAction(DOCACTION_None);
			return true;
		}     	
		return false;
	}	//	closeIt


	/**
	 * 	Reverse Correction - same void
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt() {
		log.info("reverseCorrectIt - " + toString());
		return voidIt();
	}	//	reverseCorrectionIt


	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success
	 */
	public boolean reverseAccrualIt() {
		log.info("reverseAccrualIt - " + toString());
		return false;
	}	//	reverseAccrualIt


	/**
	 * 	Re-activate.
	 * 	@return true if success
	 */
	public boolean reActivateIt() {
		log.info("reActivateIt - " + toString());

		org.compiere.model.MDocType dt = org.compiere.model.MDocType.get(getCtx(), getC_DocType_ID());
		String DocSubTypeSO = dt.getDocSubTypeSO();

		//	Reverse Direct Documents
		if (   MDocType.DOCSUBTYPESO_OnCreditOrder.equals(DocSubTypeSO)		//	(W)illCall(I)nvoice
				|| MDocType.DOCSUBTYPESO_WarehouseOrder.equals(DocSubTypeSO)	//	(W)illCall(P)ickup
				|| MDocType.DOCSUBTYPESO_POSOrder.equals(DocSubTypeSO))			//	(W)alkIn(R)eceipt
		{
			return false;
		}
		else {
			log.fine("reActivateIt - Existing documents not modified - SubType=" + DocSubTypeSO);
		}

		//	Delete 
		String sql = "DELETE FROM HR_Movement WHERE HR_Process_ID =" + this.getHR_Process_ID() + " AND IsRegistered = 'N'" ;
		int no = DB.executeUpdate(sql, get_TrxName());
		log.fine("HR_Process deleted #" + no);

		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	}	//	reActivateIt


	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID() {
		return 0;
	}	//	getDoc_User_ID


	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public java.math.BigDecimal getApprovalAmt() 
	{
		return BigDecimal.ZERO;
	}	//	getApprovalAmt

	/**
	 * 
	 */
	public int getC_Currency_ID() 
	{
		return 0;
	}

	public String getProcessMsg() 
	{
		return m_processMsg;
	}

	public String getSummary()
	{
		return "";
	}

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.ORDER, 0);
		if (re == null)
			return null;
		return re.getPDF(file);
	}	//	createPDF

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		org.compiere.model.MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo


	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return lines
	 */
	public MHRMovement[] getLines (boolean requery)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		// For HR_Process:
		whereClause.append(MHRMovement.COLUMNNAME_HR_Process_ID+"=?");
		params.add(getHR_Process_ID());
		// With Qty or Amounts
		whereClause.append("AND (Qty <> 0 OR Amount <> 0)"); // TODO: it's really needed ?
		// Only Active Concepts
		whereClause.append(" AND EXISTS(SELECT 1 FROM HR_Concept c WHERE c.HR_Concept_ID=HR_Movement.HR_Concept_ID"
				+" AND c.IsActive=?"
				+" AND c.AccountSign<>?)"); // TODO : why ?
		params.add(true);
		params.add(MHRConcept.ACCOUNTSIGN_Natural); // TODO : why ?
		// Concepts with accounting
		whereClause.append(" AND EXISTS(SELECT 1 FROM HR_Concept_Acct ca WHERE ca.HR_Concept_ID=HR_Movement.HR_Concept_ID"
				+" AND ca.IsActive=? AND ca.IsBalancing<>?)");
		params.add(true);
		params.add(true);
		// BPartner field is filled
		whereClause.append(" AND C_BPartner_ID IS NOT NULL");
		//
		// ORDER BY
		StringBuffer orderByClause = new StringBuffer();
		orderByClause.append("(SELECT bp.C_BP_Group_ID FROM C_BPartner bp WHERE bp.C_BPartner_ID=HR_Movement.C_BPartner_ID)");
		//
		List<MHRMovement> list = new Query (getCtx(), MHRMovement.Table_Name, whereClause.toString(), get_TrxName())
		.setParameters(params)
		.setOrderBy(orderByClause.toString())
		.list();
		return list.toArray(new MHRMovement[list.size()]);
	}

	/**
	 * Load HR_Movements and store them in a HR_Concept_ID->MHRMovement hashtable
	 * @param movements hashtable
	 * @param C_PBartner_ID
	 */
	private void loadMovements(Hashtable<Integer,MHRMovement> movements, int C_PBartner_ID)
	{
		final String whereClause = MHRMovement.COLUMNNAME_HR_Process_ID+"=?"
		+" AND "+MHRMovement.COLUMNNAME_C_BPartner_ID+"=?";
		List<MHRMovement> list = new Query(getCtx(), MHRMovement.Table_Name, whereClause, get_TrxName())
		.setParameters(new Object[]{getHR_Process_ID(), C_PBartner_ID})
		.list();
		for (MHRMovement mvm : list)
		{
			if(movements.containsKey(mvm.getHR_Concept_ID()))
			{
				MHRMovement lastM = movements.get(mvm.getHR_Concept_ID());
				String columntype = lastM.getColumnType();
				if (columntype.equals(MHRConcept.COLUMNTYPE_Amount))
				{
					mvm.addAmount(lastM.getAmount());
				}
				else if (columntype.equals(MHRConcept.COLUMNTYPE_Quantity))
				{
					mvm.addQty(lastM.getQty());
				}
			}
			movements.put(mvm.getHR_Concept_ID(), mvm);
		}
	}

	/**
	 * Execute the script
	 * @param scriptCtx
	 * @param AD_Rule_ID
	 * @return
	 */
	private Object executeScript(HashMap<String, Object> scriptCtx, int AD_Rule_ID)
	{
		MRule rulee = MRule.get(getCtx(), AD_Rule_ID);
		Object result = null;
		try
		{
			String text = "";
			if (rulee.getScript() != null)
			{
				text = rulee.getScript().trim().replaceAll("\\bget", "process.get")
				.replace(".process.get", ".get");
			}
			final String script =
				s_scriptImport.toString()
				+" double result = 0;"
				+ text;
			Scriptlet engine = new Scriptlet (Scriptlet.VARIABLE, script, scriptCtx);	
			Exception ex = engine.execute();
			if (ex != null)
			{
				throw ex;
			}
			result = engine.getResult(false);
		}
		catch (Exception e)
		{
			throw new AdempiereException("Execution error - @AD_Rule_ID@="+rulee.getValue());
		}
		return result;
	}

	/**
	 * creates movements for concepts related to labor
	 * @param C_BPartner_ID
	 * @param period
	 * @param scriptCtx
	 */
	private void createCostCollectorMovements(int C_BPartner_ID, MHRPeriod period ,HashMap<String, Object> scriptCtx)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		whereClause.append("EXISTS (SELECT 1 FROM AD_User u WHERE u.AD_User_ID=PP_Cost_Collector.AD_User_ID AND u.C_BPartner_ID=?)");
		params.add(C_BPartner_ID);
		whereClause.append(" AND "+MPPCostCollector.COLUMNNAME_MovementDate + ">=?");
		params.add(period.getStartDate());
		whereClause.append(" AND "+MPPCostCollector.COLUMNNAME_MovementDate + "<=?");
		params.add(period.getEndDate());
		whereClause.append(" AND "+MPPCostCollector.COLUMNNAME_DocStatus + " IN (?,?)");
		params.add(MPPCostCollector.DOCSTATUS_Completed);
		params.add(MPPCostCollector.DOCSTATUS_Closed);
		String name = MPPCostCollector.Table_Name;
		List<MPPCostCollector> listColector = new Query(getCtx(), name, 
				whereClause.toString(), get_TrxName())
		.setOnlyActiveRecords(true)
		.setParameters(params)
		.setOrderBy(MPPCostCollector.COLUMNNAME_PP_Cost_Collector_ID+" DESC") 
		.list();


		for (MPPCostCollector cc : listColector)
		{
			createMovementForCC(C_BPartner_ID, cc, scriptCtx);
		}
	}

	/**
	 * create movement for cost collector
	 * @param C_BPartner_ID
	 * @param cc
	 * @param scriptCtx
	 * @return
	 */
	private MHRMovement createMovementForCC(int C_BPartner_ID, I_PP_Cost_Collector cc, HashMap<String, Object> scriptCtx)
	{
		//get the concept that should store the labor
		MHRConcept concept = MHRConcept.forValue(getCtx(), CONCEPT_PP_COST_COLLECTOR_LABOR);

		//get the attribute for specific concept
		List<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		whereClause.append("? >= ValidFrom AND ( ? <= ValidTo OR ValidTo IS NULL)");
		params.add(m_dateFrom);
		params.add(m_dateTo);
		whereClause.append(" AND HR_Concept_ID = ? ");
		params.add(concept.get_ID());
		whereClause.append(" AND EXISTS (SELECT 1 FROM HR_Concept conc WHERE conc.HR_Concept_ID = HR_Attribute.HR_Concept_ID )");
		MHRAttribute att = new Query(getCtx(), MHRAttribute.Table_Name, whereClause.toString(), get_TrxName())
		.setParameters(params)
		.setOnlyActiveRecords(true)
		.setOrderBy(MHRAttribute.COLUMNNAME_ValidFrom + " DESC")
		.first();
		if (att == null)
		{
			throw new AdempiereException(); // TODO ?? is necessary
		}

		if (MHRConcept.TYPE_RuleEngine.equals(concept.getType()))
		{
			Object result = null;

			scriptCtx.put("_CostCollector", cc);
			try
			{
				result = executeScript(scriptCtx, att.getAD_Rule_ID());
			}
			finally
			{
				scriptCtx.remove("_CostCollector");
			}
			if(result == null)
			{
				// TODO: throw exception ???
				log.warning("Variable (result) is null");
			}

			//[201104262158]
			// changed employee type from MHREmployee to MEmployee as HR_Employee Table po is MEmployee
			//get employee
			
			MHREmployee employee = MHREmployee.getActiveEmployee(getCtx(), C_BPartner_ID, get_TrxName());

			//create movement
			MHRMovement mv = new MHRMovement(this, concept);
			mv.setC_BPartner_ID(C_BPartner_ID);
			mv.setAD_Rule_ID(att.getAD_Rule_ID());
			mv.setHR_Job_ID(employee.getHR_Job_ID());
			mv.setHR_Department_ID(employee.getHR_Department_ID());
			mv.setC_Activity_ID(employee.getC_Activity_ID());
			mv.setValidFrom(m_dateFrom);
			mv.setValidTo(m_dateTo); 
			mv.setPP_Cost_Collector_ID(cc.getPP_Cost_Collector_ID());	
			//[201104262158]
			mv.setIsManual(true);
			mv.setColumnValue(result);
			if ((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General))
			{
				mv.setProcessed(true);
			}
			mv.saveEx();
			return mv;
		}
		else
		{
			throw new AdempiereException(); //TODO ?? is necessary
		}

	}



	/**
	 * Creates the movements for the corresponding process and period
	 * 
	 * @throws Exception
	 */
	private void createMovements() throws Exception {

		HashMap<String, Object> scriptCtx = new HashMap<String, Object>();

		scriptCtx.put("process", this);
		scriptCtx.put("_Process", getHR_Process_ID());
		scriptCtx.put("_Period", getHR_Period_ID());
		scriptCtx.put("_Payroll", getHR_Payroll_ID());
		scriptCtx.put("_Department", getHR_Department_ID());

		log.info( "info data - " 
				  + " Process: " + getHR_Process_ID()
				  + ", Period: " + getHR_Period_ID() 
				  + ", Payroll: "+ getHR_Payroll_ID() 
				  + ", Department: " + getHR_Department_ID());
		
		MHRPeriod period = new MHRPeriod(getCtx(), getHR_Period_ID(),get_TrxName());
		m_HR_Period_ID = getHR_Period_ID();
		
		if (period != null) {
		
			m_dateFrom = period.getStartDate();
			m_dateTo = period.getEndDate();
			scriptCtx.put("_From", period.getStartDate());
			scriptCtx.put("_To", period.getEndDate());
		}

		// RE-Process, delete movement except concept type Incidence
		
		PayrollManager.deletemovements(getHR_Period_ID(), Boolean.TRUE, get_TrxName());	// 050120120303

		// Deletes the pay slip entries
		
		PayrollManager.deletePaySlipEntriesForPeriod(getHR_Period_ID(), get_TrxName()); // 050120120303

		// Get the payroll concepts
		
		linesConcept = MHRPayrollConcept.getPayrollConcepts(this);
		
		// Get the all employees i.e. business partners who are employees
		
		MBPartner[] employeeLines = PayrollManager.getEmployees(this);
		
		int count = 1;
		
		if (null != employeeLines && employeeLines.length > 0) {
			
			// Iterate for each employee
			
			for (MBPartner bpEmployee : employeeLines)	{
			
				log.info("Employee - " + count + "--- [ "	+ bpEmployee.getName() + "]");
				
				// Increase the employee count
				count++;
				
				// Buisness partner id for the employee
				m_C_BPartner_ID = bpEmployee.get_ID();	
				
				//  HR Employee
				
				employee = MHREmployee.getActiveEmployee( getCtx(),
														  m_C_BPartner_ID, 
														  get_TrxName());
				
				// WTCEmployeeUtil
				
				employeeUtil = new WTCEmployeeUtil( this.getCtx(),
													m_C_BPartner_ID, 
													m_HR_Period_ID, 
													this.getpayrollprocesstype(),
													employee,
													this.get_TrxName());
				
				boolean generalPayroll = Boolean.TRUE;
				
				if( PAYROLLPROCESSTYPE_Mock.equalsIgnoreCase(getpayrollprocesstype())) {
					
					generalPayroll = Boolean.FALSE;
				}
				
				if ( employeeUtil.getPresentDays(generalPayroll) <= EagleConstants.DOUBLE_ZERO && 
					PayrollManager.getEmployeeNumberOfLeaveDaysForPeriod(m_C_BPartner_ID, period) <= 0) {
					
					log.warning("[ " + employee.getName()+ " ] doesn't have attendance ");
					continue;
				}
				
				earnedSalary = null;
				grossSalary = null;
				basicSalary = null;
				
				// scriptCtx.put("_DateBirth", employee.getDateBirth());
				scriptCtx.put("_DateStart", employee.getStartDate());
				scriptCtx.put("_DateEnd",	employee.getEndDate() == null ? TimeUtil.getDay(2999,12, 31) : employee.getEndDate());
				scriptCtx.put("_Days",TimeUtil.getDaysBetween(period.getStartDate(),period.getEndDate()) + 1);
				scriptCtx.put("_C_BPartner_ID", bpEmployee.getC_BPartner_ID());

				createCostCollectorMovements(bpEmployee.get_ID(), period, scriptCtx);

				m_movement.clear();
				loadMovements(m_movement, m_C_BPartner_ID);
				//
				for (MHRPayrollConcept pc : linesConcept) // ====================================================
															// Concept
				{
					
					m_HR_Concept_ID = pc.getHR_Concept_ID();
					MHRConcept concept = MHRConcept.get(getCtx(),
							m_HR_Concept_ID);
					m_columnType = concept.getColumnType();

					List<Object> params = new ArrayList<Object>();
					StringBuffer whereClause = new StringBuffer();
					whereClause
							.append("? >= ValidFrom AND ( ? <= ValidTo OR ValidTo IS NULL)");
					params.add(m_dateFrom);
					params.add(m_dateTo);
					whereClause.append(" AND HR_Concept_ID = ? ");
					params.add(m_HR_Concept_ID);
					whereClause
							.append(" AND EXISTS (SELECT 1 FROM HR_Concept conc WHERE conc.HR_Concept_ID = HR_Attribute.HR_Concept_ID )");

					// Check the concept is within a valid range for the
					// attribute
					if (concept.isEmployee()) {
						whereClause
								.append(" AND C_BPartner_ID = ? AND (HR_Employee_ID = ? OR HR_Employee_ID IS NULL)");
						params.add(employee.getC_BPartner_ID());
						params.add(employee.get_ID());
					}

					MHRAttribute att = new Query( getCtx(),
												  MHRAttribute.Table_Name, 
												  whereClause.toString(),
												  get_TrxName())
										.setParameters(params)
										.setOnlyActiveRecords(true)
										.setOrderBy(MHRAttribute.COLUMNNAME_ValidFrom + " DESC")
										.first();

					//
					// skip the concept if concept is monthly and period is
					// intern
					//

					if (att == null
							|| concept.isManual()
							|| (concept.ismonthly() && WTCTimeUtil.isPeriodIntern(period))) {
						
						log.info("Skip concept " + concept
								+ " - attribute not found");
						continue;
					}

					log.info("Concept - " + concept.getName());
					MHRMovement movement = new MHRMovement(getCtx(), 0,	get_TrxName());
					
					movement.setC_BPartner_ID(m_C_BPartner_ID);
					movement.setHR_Concept_ID(m_HR_Concept_ID);
					movement.setHR_Concept_Category_ID(concept.getHR_Concept_Category_ID());
					movement.setHR_Process_ID(getHR_Process_ID());
					movement.setHR_Department_ID(employee.getHR_Department_ID());
					movement.setHR_Job_ID(employee.getHR_Job_ID());
					movement.setColumnType(m_columnType);
					movement.setAD_Rule_ID(att.getAD_Rule_ID());
					movement.setValidFrom(m_dateFrom);
					movement.setValidTo(m_dateTo);
					movement.setIsPrinted(att.isPrinted());
					movement.setIsManual(concept.isManual());
					movement.setC_Activity_ID(employee.getC_Activity_ID());
					
					if (MHRConcept.TYPE_RuleEngine.equals(concept.getType())) {
						Object result = executeScript(scriptCtx,
								att.getAD_Rule_ID());
						if (result == null) {
							// TODO: throw exception ???
							log.warning("Variable (result) is null");

							// [201104262158]
							// if concept print always is true, base on
							// columntype
							// pass defualt values to the result
							//
							if (concept.isprintallways()) {
								
								String columnType = concept.getColumnType();
								if (MHRConcept.COLUMNTYPE_Quantity
										.equals(columnType)) {
									result = new BigDecimal(0);

								} else if (MHRConcept.COLUMNTYPE_Amount
										.equals(columnType)) {
									int precision = MCurrency.getStdPrecision(
											getCtx(), Env.getContextAsInt(
													p_ctx, "#C_Currency_ID"));
									result = new BigDecimal(0.00)
											.setScale(precision,
													BigDecimal.ROUND_HALF_UP);

								} else if (MHRConcept.COLUMNTYPE_Text
										.equals(columnType)) {
									result = "  ";
								} else if (MHRConcept.COLUMNTYPE_Date
										.equals(columnType)) {
									result = new Timestamp(TimeUtil.getToday()
											.getTimeInMillis());
								} else {
									log.warning("concept print always is true and column type is unsupported");
									continue;
								}
							} else {
								continue;
							}
						}

						movement.setColumnValue(result);
					} else {
						movement.setQty(att.getQty());
						movement.setAmount(att.getAmount());
						movement.setTextMsg(att.getTextMsg());
						movement.setServiceDate(att.getServiceDate());
					}
					if ((getpayrollprocesstype())
							.equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)) {
						movement.setProcessed(true);
					}
					m_movement.put(m_HR_Concept_ID, movement);
				} // concept

				// Save movements:
				for (MHRPayrollConcept pc : linesConcept) {
					MHRMovement m = m_movement.get(pc.getHR_Concept_ID());
					if (m == null) {
						continue;
					}
					MHRConcept c = MHRConcept.get(getCtx(),
							pc.getHR_Concept_ID());
					if (!c.isprintallways() && (c.isManual() || m.isEmpty())) {
						log.fine("Skip saving " + m);
					} else {
						m.saveEx();
					}
				}
			} // for each employee
		}	//
			// Save period & finish
		if ((getpayrollprocesstype())
				.equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)) {
			period.setProcessed(true);
		}
		period.saveEx();
	} // createMovements



	// Helper methods -------------------------------------------------------------------------------

	/**
	 * Helper Method : get the value of the concept
	 * @param pconcept
	 * @return
	 */
	public double getConcept (String pconcept)
	{
		MHRConcept concept = MHRConcept.forValue(getCtx(), pconcept.trim());

		if (concept == null)
		{
			return 0; // TODO throw exception ?
		}

		MHRMovement m = m_movement.get(concept.get_ID());
		if (m == null)
		{
			return 0; // TODO throw exception ?
		}

		String type = m.getColumnType();
		if (MHRMovement.COLUMNTYPE_Amount.equals(type))
		{
			return m.getAmount().doubleValue();
		}
		else if (MHRMovement.COLUMNTYPE_Quantity.equals(type))
		{
			return m.getQty().doubleValue();
		}
		else
		{
			// TODO: throw exception ?
			return 0;
		}
	} // getConcept

	/**
	 * Helper Method : sets the value of a concept
	 * @param conceptValue
	 * @param value
	 */
	public void setConcept (String conceptValue, double value)
	{
		try
		{
			MHRConcept c = MHRConcept.forValue(getCtx(), conceptValue); 
			if (c == null)
			{
				return; // TODO throw exception
			}
			MHRMovement m = new MHRMovement(getCtx(), 0, get_TrxName());
			m.setColumnType(c.getColumnType());
			m.setColumnValue(BigDecimal.valueOf(value));

			m.setHR_Process_ID(getHR_Process_ID());
			m.setHR_Concept_ID(m_HR_Concept_ID);
			m.setC_BPartner_ID(m_C_BPartner_ID);
			m.setDescription("Added From Rule"); // TODO: translate
			m.setValidFrom(m_dateTo);
			m.setValidTo(m_dateTo);
			m.saveEx();
		} 
		catch(Exception e)
		{
			s_log.warning(e.getMessage());
		}
	} // setConcept
	
	/* Helper Method : sets the value of a concept and set if isRegistered 
	* @param conceptValue
	* @param value
	* @param isRegistered
	*/
	public void setConcept (String conceptValue,double value,boolean isRegistered)
	{
		try
		{
			MHRConcept c = MHRConcept.forValue(getCtx(), conceptValue); 
			if (c == null)
			{
				return; // TODO throw exception
			}
			MHRMovement m = new MHRMovement(Env.getCtx(),0,get_TrxName());
			m.setColumnType(c.getColumnType());
			if (c.getColumnType().equals(MHRConcept.COLUMNTYPE_Amount))
				m.setAmount(BigDecimal.valueOf(value));
			else if (c.getColumnType().equals(MHRConcept.COLUMNTYPE_Quantity))
				m.setQty(BigDecimal.valueOf(value));
			else
				return;
			m.setHR_Process_ID(getHR_Process_ID());
			m.setHR_Concept_ID(c.getHR_Concept_ID());
			m.setC_BPartner_ID(m_C_BPartner_ID);
			m.setDescription("Added From Rule"); // TODO: translate
			m.setValidFrom(m_dateTo);
			m.setValidTo(m_dateTo);
			m.setIsManual(isRegistered);
			m.saveEx();
		} 
		catch(Exception e)
		{
			s_log.warning(e.getMessage());
		}
	} // setConcept

	/**
	 * Helper Method : get the sum of the concept values, grouped by the Category
	 * @param pconcept
	 * @return
	 */
	public double getConceptGroup (String pconcept)
	{
		final MHRConceptCategory category = MHRConceptCategory.forValue(getCtx(), pconcept);
		if (category == null)
		{
			return 0.0; // TODO: need to throw exception ?
		}
		//
		double value = 0.0;
		for(MHRPayrollConcept pc : linesConcept)
		{
			MHRConcept con = MHRConcept.get(getCtx(), pc.getHR_Concept_ID());
			if(con.getHR_Concept_Category_ID() == category.get_ID())
			{
				MHRMovement movement = m_movement.get(pc.getHR_Concept_ID());
				if (movement==null)
				{
					continue;
				}
				else
				{
					String columnType = movement.getColumnType();
					if(MHRConcept.COLUMNTYPE_Amount.equals(columnType))
					{
						value += movement.getAmount().doubleValue();
					}
					else if (MHRConcept.COLUMNTYPE_Quantity.equals(columnType))
					{
						value += movement.getQty().doubleValue();
					}
				}
			}
		}
		return value;
	} // getConceptGroup


	/**
	 * Helper Method : Get Concept [get concept to search key ]
	 * @param pList Value List
	 * @param amount Amount to search
	 * @param column Number of column to return (1.......8)
	 * @return The amount corresponding to the designated column 'column'
	 */
	public double getList (String pList, double amount, String columnParam)
	{
		BigDecimal value = Env.ZERO;
		String column = columnParam;
		if (m_columnType.equals(MHRConcept.COLUMNTYPE_Amount))
		{
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
		}
		//
		if (value == null)
		{
			value = Env.ZERO;
			//throw new IllegalStateException("getList Out of Range");
		}
		return value.doubleValue();
	} // getList


	/**
	 * Helper Method : Get Attribute [get Attribute to search key concept ]
	 * @param pConcept - Value to Concept
	 * @return	Amount of concept, applying to employee
	 */ 
	public double getAttribute (String pConcept)
	{
		MHRConcept concept = MHRConcept.forValue(getCtx(), pConcept);
		if (concept == null)
			return 0;

		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		// check ValidFrom:
		whereClause.append(MHRAttribute.COLUMNNAME_ValidFrom + "<=?");
		params.add(m_dateFrom);
		//check client
		whereClause.append(" AND AD_Client_ID = ?");
		params.add(getAD_Client_ID());
		//check concept
		whereClause.append(" AND EXISTS (SELECT 1 FROM HR_Concept c WHERE c.HR_Concept_ID=HR_Attribute.HR_Concept_ID" 
				+ " AND c.Value = ?)");
		params.add(pConcept);
		//
		if (!concept.getType().equals(MHRConcept.TYPE_Information))
		{
			whereClause.append(" AND " + MHRAttribute.COLUMNNAME_C_BPartner_ID + " = ?");
			params.add(m_C_BPartner_ID);
		}

		MHRAttribute attribute = new Query(getCtx(), MHRAttribute.Table_Name, whereClause.toString(), get_TrxName())
		.setParameters(params)
		.setOrderBy(MHRAttribute.COLUMNNAME_ValidFrom + " DESC")
		.first();
		if (attribute == null)
			return 0.0;

		// if column type is Quantity return quantity
		if (concept.getColumnType().equals(MHRConcept.COLUMNTYPE_Quantity))
			return attribute.getQty().doubleValue();

		// if column type is Amount return amount
		if (concept.getColumnType().equals(MHRConcept.COLUMNTYPE_Amount))
			return attribute.getAmount().doubleValue();

		//something else
		return 0.0; //TODO throw exception ?? 
	} // getAttribute


	/**
	 * 	Helper Method : Get Attribute [get Attribute to search key concept ]
	 *  @param conceptValue
	 *  @return ServiceDate
	 */ 
	public Timestamp getAttributeDate (String conceptValue)
	{
		MHRConcept concept = MHRConcept.forValue(getCtx(), conceptValue);
		if (concept == null)
			return null;

		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		//check client
		whereClause.append("AD_Client_ID = ?");
		params.add(getAD_Client_ID());
		//check concept
		whereClause.append(" AND EXISTS (SELECT 1 FROM HR_Concept c WHERE c.HR_Concept_ID=HR_Attribute.HR_Concept_ID" 
				+ " AND c.Value = ?)");
		params.add(conceptValue);
		//
		if (!concept.getType().equals(MHRConcept.TYPE_Information))
		{
			whereClause.append(" AND " + MHRAttribute.COLUMNNAME_C_BPartner_ID + " = ?");
			params.add(m_C_BPartner_ID);
		}

		MHRAttribute attribute = new Query(getCtx(), MHRAttribute.Table_Name, whereClause.toString(), get_TrxName())
		.setParameters(params)
		.setOrderBy(MHRAttribute.COLUMNNAME_ValidFrom + " DESC")
		.first();
		if (attribute == null)
			return null;

		return attribute.getServiceDate();
	} // getAttributeDate

	/**
	 * 	Helper Method : Get the number of days between start and end, in Timestamp format
	 *  @param date1 
	 *  @param date2
	 *  @return no. of days
	 */ 
	public int getDays (Timestamp date1, Timestamp date2)
	{		
		// adds one for the last day
		return org.compiere.util.TimeUtil.getDaysBetween(date1,date2) + 1;
	} // getDays


	/**
	 * 	Helper Method : Get the number of days between start and end, in String format
	 *  @param date1 
	 *  @param date2
	 *  @return no. of days
	 */  
	public  int getDays (String date1, String date2)
	{		
		Timestamp dat1 = Timestamp.valueOf(date1);
		Timestamp dat2 = Timestamp.valueOf(date2);
		return getDays(dat1, dat2);
	}  // getDays

	/**
	 * 	Helper Method : Get Months, Date in Format Timestamp
	 *  @param start
	 *  @param end
	 *  @return no. of month between two dates
	 */ 
	public int getMonths(Timestamp startParam,Timestamp endParam)
	{
		boolean negative = false;
		Timestamp start = startParam;
		Timestamp end = endParam;
		if (end.before(start))
		{
			negative = true;
			Timestamp temp = start;
			start = end;
			end = temp;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GregorianCalendar calEnd = new GregorianCalendar();

		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);

		if (cal.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR))
		{
			if (negative)
				return (calEnd.get(Calendar.MONTH) - cal.get(Calendar.MONTH)) * -1;
			return calEnd.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
		}

		//	not very efficient, but correct
		int counter = 0;
		while (calEnd.after(cal))
		{
			cal.add (Calendar.MONTH, 1);
			counter++;
		}
		if (negative)
			return counter * -1;
		return counter;
	} // getMonths


	/**
	 * Helper Method : Concept for a range from-to in periods.
	 * Periods with values of 0 -1 1, etc. actual previous one period, next period
	 * 0 corresponds to actual period.
	 * @param conceptValue concept key(value)
	 * @param periodFrom the search is done by the period value, it helps to search from previous years
	 * @param periodTo
	 */
	public double getConcept (String conceptValue, int periodFrom, int periodTo)
	{
		return getConcept(conceptValue, null, periodFrom,periodTo);
	} // getConcept

	/**
	 *  Helper Method : Concept by range from-to in periods from a different payroll
	 *  periods with values 0 -1 1, etc. actual previous one period, next period
	 *  0 corresponds to actual period
	 *  @param conceptValue 
	 *  @param pFrom 
	 *  @param pTo the search is done by the period value, it helps to search from previous years
	 *  @param payrollValue is the value of the payroll.
	 */
	public double getConcept(String conceptValue, String payrollValue,int periodFrom,int periodTo)
	{
		int payroll_id;
		if (payrollValue == null)
		{
			payroll_id = getHR_Payroll_ID();
		}
		else
		{
			payroll_id = MHRPayroll.forValue(getCtx(), payrollValue).get_ID();
		}

		MHRConcept concept = MHRConcept.forValue(getCtx(), conceptValue);
		if (concept == null)
			return 0.0;
		//
		// Detect field name
		final String fieldName;
		if (MHRConcept.COLUMNTYPE_Quantity.equals(concept.getColumnType()))
		{
			fieldName = MHRMovement.COLUMNNAME_Qty;
		}
		else if (MHRConcept.COLUMNTYPE_Amount.equals(concept.getColumnType()))
		{
			fieldName = MHRMovement.COLUMNNAME_Amount;
		}
		else
		{
			return 0; // TODO: throw exception?
		}
		//
		MHRPeriod p = MHRPeriod.get(getCtx(), getHR_Period_ID());
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		//check client
		whereClause.append("AD_Client_ID = ?");
		params.add(getAD_Client_ID());
		//check concept
		whereClause.append(" AND " + MHRMovement.COLUMNNAME_HR_Concept_ID + "=?");
		params.add(concept.get_ID());
		//check partner
		whereClause.append(" AND " + MHRMovement.COLUMNNAME_C_BPartner_ID  + "=?");
		params.add(m_C_BPartner_ID);
		//
		//check process and payroll
		whereClause.append(" AND EXISTS (SELECT 1 FROM HR_Process p"
				+" INNER JOIN HR_Period pr ON (pr.HR_Period_id=p.HR_Period_ID)"
				+" WHERE HR_Movement.HR_Process_ID = p.HR_Process_ID" 
				+" AND p.HR_Payroll_ID=?");

		params.add(payroll_id);
		if (periodFrom < 0)
		{
			whereClause.append(" AND pr.PeriodNo >= ?");
			params.add(p.getPeriodNo() +periodFrom);
		}
		if (periodTo > 0)
		{
			whereClause.append(" AND pr.PeriodNo <= ?");
			params.add(p.getPeriodNo() +periodTo);
		}
		whereClause.append(")");
		//
		StringBuffer sql = new StringBuffer("SELECT COALESCE(SUM(").append(fieldName).append("),0) FROM ").append(MHRMovement.Table_Name)
		.append(" WHERE ").append(whereClause);
		BigDecimal value = DB.getSQLValueBDEx(get_TrxName(), sql.toString(), params);
		return value.doubleValue();

	} // getConcept

	/**
	 * Helper Method: gets Concept value of a payrroll between 2 dates
	 * @param pConcept
	 * @param pPayrroll
	 * @param from
	 * @param to
	 * */
	public double getConcept (String conceptValue, String payrollValue,Timestamp from,Timestamp to)
	{
		int payroll_id;
		if (payrollValue == null)
		{
			payroll_id = getHR_Payroll_ID();
		}
		else
		{
			payroll_id = MHRPayroll.forValue(getCtx(), payrollValue).get_ID();
		}
		
		MHRConcept concept = MHRConcept.forValue(getCtx(), conceptValue);
		if (concept == null)
			return 0.0;
		//
		// Detect field name
		final String fieldName;
		if (MHRConcept.COLUMNTYPE_Quantity.equals(concept.getColumnType()))
		{
			fieldName = MHRMovement.COLUMNNAME_Qty;
		}
		else if (MHRConcept.COLUMNTYPE_Amount.equals(concept.getColumnType()))
		{
			fieldName = MHRMovement.COLUMNNAME_Amount;
		}
		else
		{
			return 0; // TODO: throw exception?
		}
		//
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		//check client
		whereClause.append("AD_Client_ID = ?");
		params.add(getAD_Client_ID());
		//check concept
		whereClause.append(" AND " + MHRMovement.COLUMNNAME_HR_Concept_ID + "=?");
		params.add(concept.get_ID());
		//check partner
		whereClause.append(" AND " + MHRMovement.COLUMNNAME_C_BPartner_ID  + "=?");
		params.add(m_C_BPartner_ID);
		//Adding dates 
		whereClause.append(" AND validTo BETWEEN ? AND ?");
		params.add(from);
		params.add(to);
		//
		//check process and payroll
		whereClause.append(" AND EXISTS (SELECT 1 FROM HR_Process p"
							+" INNER JOIN HR_Period pr ON (pr.HR_Period_id=p.HR_Period_ID)"
							+" WHERE HR_Movement.HR_Process_ID = p.HR_Process_ID" 
							+" AND p.HR_Payroll_ID=?");

		params.add(payroll_id);
		
		whereClause.append(")");
		//
		StringBuffer sql = new StringBuffer("SELECT COALESCE(SUM(").append(fieldName).append("),0) FROM ").append(MHRMovement.Table_Name)
								.append(" WHERE ").append(whereClause);
		BigDecimal value = DB.getSQLValueBDEx(get_TrxName(), sql.toString(), params);
		return value.doubleValue();
		
	} // getConcept
	
	/**
	 * Helper Method : Attribute that had from some date to another to date,
	 * if it finds just one period it's seen for the attribute of such period 
	 * if there are two or more attributes based on the days
	 * @param ctx
	 * @param vAttribute
	 * @param dateFrom
	 * @param dateTo
	 * @return attribute value
	 */
	public double getAttribute (Properties ctx, String vAttribute, Timestamp dateFrom, Timestamp dateTo)
	{
		// TODO ???
		log.warning("not implemented yet -> getAttribute (Properties, String, Timestamp, Timestamp)");
		return 0;
	} // getAttribute

	/**
	 *  Helper Method : Attribute that had from some period to another to period,
	 *   periods with values 0 -1 1, etc. actual previous one period, next period
	 *  0 corresponds to actual period
	 *  Value of HR_Attribute
	 *  if it finds just one period it's seen for the attribute of such period 
	 *  if there are two or more attributes 
	 *  pFrom and pTo the search is done by the period value, it helps to search 
	 *  from previous year based on the days
	 *  @param ctx
	 *  @param vAttribute
	 *  @param periodFrom
	 *  @param periodTo
	 *  @param pFrom
	 *  @param pTo
	 *  @return attribute value	  
	 */
	public double getAttribute (Properties ctx, String vAttribute, int periodFrom,int periodTo,
			String pFrom,String pTo)
	{
		// TODO ???
		log.warning("not implemented yet -> getAttribute (Properties, String, int, int)");
		return 0;
	} // getAttribute
	
	
		
	/**
	 * Helper Method : Get AttributeInvoice 
	 * @param pConcept - Value to Concept
	 * @return	C_Invoice_ID, 0 if does't
	 */ 
	public int getAttributeInvoice (String pConcept)
	{
		MHRConcept concept = MHRConcept.forValue(getCtx(), pConcept);
		if (concept == null)
			return 0;
		
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		// check ValidFrom:
		whereClause.append(MHRAttribute.COLUMNNAME_ValidFrom + "<=?");
		params.add(m_dateFrom);
		//check client
		whereClause.append(" AND AD_Client_ID = ?");
		params.add(getAD_Client_ID());
		//check concept
		whereClause.append(" AND EXISTS (SELECT 1 FROM HR_Concept c WHERE c.HR_Concept_ID=HR_Attribute.HR_Concept_ID" 
						   + " AND c.Value = ?)");
		params.add(pConcept);
		//
		if (!MHRConcept.TYPE_Information.equals(concept.getType()))
		{
			whereClause.append(" AND " + MHRAttribute.COLUMNNAME_C_BPartner_ID + " = ?");
			params.add(m_C_BPartner_ID);
		}
		
		MHRAttribute attribute = new Query(getCtx(), MHRAttribute.Table_Name, whereClause.toString(), get_TrxName())
		.setParameters(params)
		.setOrderBy(MHRAttribute.COLUMNNAME_ValidFrom + " DESC")
		.first();
		
		if(attribute!=null)
			return (Integer) attribute.get_Value("C_Invoice_ID");
		else
			return 0;
		
	} // getAttributeInvoice
		
	/**
	 * Helper Method : Get AttributeDocType
	 * @param pConcept - Value to Concept
	 * @return	C_DocType_ID, 0 if does't
	 */ 
	public int getAttributeDocType (String pConcept)
	{
		MHRConcept concept = MHRConcept.forValue(getCtx(), pConcept);
		if (concept == null)
			return 0;
		
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		// check ValidFrom:
		whereClause.append(MHRAttribute.COLUMNNAME_ValidFrom + "<=?");
		params.add(m_dateFrom);
		//check client
		whereClause.append(" AND AD_Client_ID = ?");
		params.add(getAD_Client_ID());
		//check concept
		whereClause.append(" AND EXISTS (SELECT 1 FROM HR_Concept c WHERE c.HR_Concept_ID=HR_Attribute.HR_Concept_ID" 
						   + " AND c.Value = ?)");
		params.add(pConcept);
		//
		if (!MHRConcept.TYPE_Information.equals(concept.getType()))
		{
			whereClause.append(" AND " + MHRAttribute.COLUMNNAME_C_BPartner_ID + " = ?");
			params.add(m_C_BPartner_ID);
		}
		
		MHRAttribute attribute = new Query(getCtx(), MHRAttribute.Table_Name, whereClause.toString(), get_TrxName())
		.setParameters(params)
		.setOrderBy(MHRAttribute.COLUMNNAME_ValidFrom + " DESC")
		.first();
		
		if(attribute!=null)
			return (Integer) attribute.get_Value("C_DocType_ID");
		else
			return 0;
		 
	} // getAttributeDocType

	/**
	 * Helper Method : get days from specific period
	 * @param period
	 * @return no. of days
	 */
	
	public double getDays (int period)
	{
		return Env.getContextAsInt(getCtx(), "_DaysPeriod") + 1;
	} // getDays
	

	
	/**
	 * <P>
	 * Retrives the no of days for which employee was absent in sepecified<BR>
	 * period.
	 * </P>
	 * 
	 * @return : No fo absent days
	 */
	public Double getAbsentDays() {
		
		
		if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
			return employeeUtil.getAbsentDays(true, m_C_BPartner_ID,period.getStartDate(), period.getEndDate(),period);
		}
		else{
			
			return employeeUtil.getAbsentDays(false, m_C_BPartner_ID,period.getStartDate(), period.getEndDate(),period);
		}
	}
	
	
	/**
	 * Get the present days for the employee
	 * 
	 * @return	: Present days for the employee
	 */
	public Double getPresentDays(){
		
		if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
			return employeeUtil.getPresentDays(true);
		}
		else{
			
			return employeeUtil.getPresentDays(false);
		}
	}
	
	/**
	 * Get working days for the period
	 * 
	 * @return :  Number of working days for the period
	 */
	public int getWokingDays() {

		// return employeeUtil.getWorkingDaysForPeriod(period.getStartDate(), period.getEndDate());
		
		return employeeUtil.getDaysForPeriod(period.getStartDate(), period.getEndDate());
	}	
	
	public int getLateCommingDays(){
		
		if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
			return employeeUtil.getLateCommingDays(true);
		}
		else{
			
			return employeeUtil.getLateCommingDays(false);
		}
	}
	
	/**
	 * Get weekly offs for employee in the period
	 * 
	 * @return : Number of weekly offs
	 */
	public int getWeeklyOffs(){
		
		return employeeUtil.getWeeklyOffs();
	}
	
	/**
	 * Get the number of days for salary to pay
	 * 
	 * @return Get number of effective days
	 */
	public Double getEffectiveSalaryDays() {
		
		double effectiveSalaryDays = EagleConstants.DOUBLE_ZERO;
		
		if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
			effectiveSalaryDays =  employeeUtil.getEffectiveSalaryDays(true);
		}
		else{
			
			effectiveSalaryDays =  employeeUtil.getEffectiveSalaryDays(false);
		}
		
		return effectiveSalaryDays;
		
	}
	
	    
	public Double getCanteenAmount(){
		
		
		BigDecimal totalCanteenAmt = Env.ZERO;
		
		if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
			totalCanteenAmt = PayrollManager.getCanteenAmountForEmployee(m_C_BPartner_ID,m_HR_Period_ID,true,get_TrxName());
		}
		else{
			
			totalCanteenAmt = PayrollManager.getCanteenAmountForEmployee(m_C_BPartner_ID,m_HR_Period_ID,false,get_TrxName());
		}
		
		
		if(null != totalCanteenAmt) {
			
			return totalCanteenAmt.doubleValue();
		}else {
			
			return EagleConstants.DOUBLE_ZERO;
		}
		
	}
	
	public Double getInsurance()
	{
		BigDecimal totalLicAmt =Env.ZERO;
		StringBuffer where = new StringBuffer();
		where.append(MEmployeeInsurance.COLUMNNAME_C_BPartner_ID).append(" = ")
				.append(m_C_BPartner_ID).append(" AND ")
				.append(MEmployeeInsurance.COLUMNNAME_paymentdate)
				.append(" BETWEEN  '")
				.append(period.getStartDate()).append("' AND '").append(period.getEndDate())
				.append("' AND ")
				.append(MEmployeeInsurance.COLUMNNAME_IsActive)
				.append(" = 'Y'");
		
		int days = MSysConfig.getIntValue("NUMBER_OF_DAYS_IN_A_MONTH", 30); // 050120120330
		
		List<MEmployeeInsurance> insurance = new Query(getCtx(),MEmployeeInsurance.Table_Name,where.toString(),get_TrxName()).list();	
		for(MEmployeeInsurance lic : insurance)
		{
			Timestamp lastPremiumDate = lic.getlastpremiumdate();
			Timestamp payDate = null;
			Timestamp lastPaidDate = null;
			totalLicAmt= totalLicAmt.add(lic.getpremium_amount());
             if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General))
             {
            	 payDate = lic.getpaymentdate();
            	 lastPaidDate = payDate;
            	 String payFrequency = lic.getpaymentfrequency();
            	 
            	 if(payFrequency.equalsIgnoreCase( X_HR_Employee_Insurance.PAYMENTFREQUENCY_Monthly))
            	 {
            		 payDate = TimeUtil.addDays(payDate, days);
            	 }
            	 else if(payFrequency.equalsIgnoreCase(X_HR_Employee_Insurance.PAYMENTFREQUENCY_Quarterly))
            	 {
            		 payDate = TimeUtil.addDays(payDate, (days * 3));
            	 }
            	 else if(payFrequency.equalsIgnoreCase(X_HR_Employee_Insurance.PAYMENTFREQUENCY_Semi_Yearly))
            	 {
            		 payDate = TimeUtil.addDays(payDate, (days * 6));
            	 }
            	 else if(payFrequency.equalsIgnoreCase(X_HR_Employee_Insurance.PAYMENTFREQUENCY_Yearly))
            	 {
            		 payDate = TimeUtil.addDays(payDate, (days * 12));
            	 }
            	
            	 X_HR_Insurance_History history = new X_HR_Insurance_History(getCtx(), 0, get_TrxName());
            	 
            	 history.setC_BPartner_ID(m_C_BPartner_ID);
            	 history.setHR_Employee_Insurance_ID(lic.getHR_Employee_Insurance_ID());
            	 history.setHR_Period_ID(m_HR_Period_ID);
            	 history.setHR_Insurance_Type_ID(lic.getHR_Insurance_Type_ID());
            	 history.setAmount(lic.getpremium_amount());
            	 history.setrundate(m_dateFrom);
            	 history.setName(lic.getinsurance_reference());
            	 if(! history.save())
            	 {
            		 log.warning(employee.getName() + " insurance history reocrd not saved ");
            	 }
            	 if(lastPremiumDate == null )
            	 {
            		 lic.setpaymentdate(payDate);
            		 lic.setlastpaiddate(lastPaidDate);
 					 if(! lic.save())
 					 {
 						 log.warning(employee.getName() + " lic payment date not updated in payroll process");
 					 }
            	 }
            	 else if(lastPremiumDate.after(payDate))
            	 {
					 lic.setpaymentdate(payDate);
					 lic.setlastpaiddate(lastPaidDate);
					 if(! lic.save())
					 {
					 	 log.warning(employee.getName() + " lic payment date not updated in payroll process");
					 }
				 }
             }
		}
		return totalLicAmt.doubleValue();
	}
	
	public Double getStoreAmount(){
		
		BigDecimal totalStoreAmt = Env.ZERO;
		
		if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
			totalStoreAmt = PayrollManager.getStoreAmmountForEmployee(m_C_BPartner_ID,m_HR_Period_ID,true,get_TrxName());
		}
		else{
			
			totalStoreAmt = PayrollManager.getStoreAmmountForEmployee(m_C_BPartner_ID,m_HR_Period_ID,false,get_TrxName());
		}
		
		if(null != totalStoreAmt) {
			
			return totalStoreAmt.doubleValue();
		}else {
			
			return EagleConstants.DOUBLE_ZERO;
		}
	}
	
	public Double getFineAmount() {
		
		StringBuffer where = new StringBuffer();
		where.append(MEmpFine.COLUMNNAME_C_BPartner_ID).append(" = ")
		.append(m_C_BPartner_ID).append(" AND ").append(MEmpFine.COLUMNNAME_HR_Period_ID).append(" = ").append(m_HR_Period_ID);
		
		List<MEmpFine> fines =  new Query(getCtx(),MEmpFine.Table_Name,where.toString(),get_TrxName()).list();
		
		BigDecimal totalFineAmt =Env.ZERO;
		
		for(MEmpFine fine : fines)
		{
			totalFineAmt = totalFineAmt.add(fine.getfineamount());
			
			//
			// set processed value of the record to true and save the record
			//
			if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General))
			{
				fine.setProcessed(true);
				fine.save(get_TrxName());
			}
		}
		
		return totalFineAmt.doubleValue();
	}
	
	
	
	/**
	 * This Method is Useed To Do Payment To The Company of EMI Amounts.
	 * 
	 * [20120103:9:00PM]
	 * According to code review Comments , Made Code Change 
	 * Do not fetch already paid emi's but get isskippedpayslip entries & set to processed
	 * 
	 * [20120104:9:00PM]     Removed Code For Making Payment , This Posting Is Handeled In Payroll It Self.
	 * @return
	 */
    public Double getAdvancePayment(){

    	
    	List<MSalAdvReq> advReqs =  MSalAdvReq.getUnpaidAdvanceReqs(getCtx(), m_C_BPartner_ID, this.get_TrxName());
		
		BigDecimal totalEMIAmt =Env.ZERO;
		
		for(MSalAdvReq advReq : advReqs)
		{
			//
			// Get the current month EMI
			//
			StringBuffer where = new StringBuffer();
			where.append(MSalAdvEMI.COLUMNNAME_HR_Sal_Adv_Req_ID).append(" = ")
			.append(advReq.get_ID()).append(" AND ").append(MSalAdvEMI.COLUMNNAME_HR_Period_ID).append(" = ")
			.append(m_HR_Period_ID).append(" AND ").append(MSalAdvEMI.COLUMNNAME_ispaidoff).append(" = 'N'");
			
			List<MSalAdvEMI> EMIList =  new Query(getCtx(),MSalAdvEMI.Table_Name,where.toString(),get_TrxName()).list();
			for(MSalAdvEMI emi : EMIList)
			{
				if ( emi.isskipinpayslip() == false ){
					
					BigDecimal  actualEmi = emi.getactualemiamount();
				    totalEMIAmt = totalEMIAmt.add(actualEmi);
				}
				
				//
				// set processed value of the record to true and save the record
				//
				if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
					
					emi.setispaidoff(true);
					Timestamp  paidOffDate = WTCTimeUtil.getSystemCurrentTimestamp();
					emi.setpaidoffdate(paidOffDate);
					emi.setProcessed(true);
					Boolean success = emi.save(get_TrxName());
					if(! success ) {
						log.log(Level.SEVERE, "Emi Is Not Save : "+emi.getHR_Sal_Adv_EMI_ID() );
					}
				}
			}
			
			
			if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General))
			{
				if(totalEMIAmt == null) {
					totalEMIAmt = Env.ZERO;
				}
				BigDecimal advanceReturned = advReq.getadvancereturnamt();
				if(advanceReturned == null) {
					advanceReturned = Env.ZERO;
				}
			
				advanceReturned =advanceReturned.add(totalEMIAmt) ;
				advReq.setadvancereturnamt(advanceReturned);
				if(! advReq.save()) {
					log.log(Level.SEVERE, "Advance Request is not updated with returned Amount");
				}
			}
		}
		return totalEMIAmt.doubleValue();

	}
	
    //--------------------------------------
	public Double getWageperDay(){
		
		StringBuffer where = new StringBuffer();
		where.append(MHREmployee.COLUMNNAME_C_BPartner_ID).append(" = ").append(m_C_BPartner_ID);
		
		MHREmployee employee =  new Query(getCtx(),MHREmployee.Table_Name,where.toString(),get_TrxName()).first();
		
		BigDecimal totalDailyWage =employee.getdaily_salary();
		
		
		return totalDailyWage.doubleValue();
	}
	
	
	public Double getProfessionalTax() {
		
		Double result = new Double(0.0);
		
		// Rule - Calcualate the profeesional tax only to employees who opted for the PF
		
		if (employeeUtil.isPFEmployee()) {
		
			StringBuffer where = new StringBuffer();
						where.append(MHREmployee.COLUMNNAME_C_BPartner_ID).append("  = ")
							 .append(m_C_BPartner_ID).append("  AND ")
							 .append(MHREmployee.COLUMNNAME_hasoptedpf).append(" = ")
							 .append("'Y'");
						
			MHREmployee emp = new Query(getCtx(), MHREmployee.Table_Name, where.toString(), get_TrxName()).first();

			if (emp != null) {
				
				double earn = getGrossSalary();
				result = getList("ProfTax", earn, "1");
			}
		}

		return result;
	}
	
	public Double getGrossSalary() {
		
		
		if(null == grossSalary)	{
			
			if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
				
				grossSalary =  employeeUtil.getGrossSalary(true).doubleValue();
			}
			else{
				

				grossSalary =  employeeUtil.getGrossSalary(false).doubleValue();
			}
		}
		
		return grossSalary;
	}
	
	public Double getAttendanceIncentive() {
		
		return employeeUtil.getAttendanceIncentive();
	}
	
    public Double getOnTimeIncentive() {

    	return employeeUtil.getOnTimeIncentive();
	}
    
    public Double getOverTimeIncentive() {

    	return employeeUtil.getOverTimeIncentive();
	}
    
    public Double getWeeklyOffIncentive() {
    	
    	return employeeUtil.getWeeklyOffIncentive();
	}
    public Double getThreeMonthIncentive() {

    	return employeeUtil.getThreeMonthIncentive();
	} 
        
    public Double getShiftChangeAbsentDeduction() {
      
    	return employeeUtil.getShiftChangeAbsentDeduction();
	}
    
    public Double getLateComingDeduction() {
    	
    	return employeeUtil.getLateComingDeduction();
    }

    public Double getBackLogs() {
    	return employeeUtil.getBackLogs();
    }
   
    public Double getOtherEarnings() {
    	return employeeUtil.getOtherEarnings();
    }
    
    public Double getOtherDeductions() {
    	
    	return employeeUtil.getOtherDeductions();
    }
    
    public Double getPresentedWeeklyDays() {
    	
    	return employeeUtil.getPresentedWeeklyOffs();
    }
    
    public Double getActualPresentedDays() {
    	
    	if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
    		return employeeUtil.getActualPresentedDays(true);
		}
		else{
			
			return employeeUtil.getActualPresentedDays(false);
		}
    }
    
  //-------------- 
    public Double createPaySlipEntry(){
    	
    	if(!(getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_Worksheet))
		{
			MPaySlip paySlip = new MPaySlip(getCtx(),0,get_TrxName());
			if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_Mock))
			{
				paySlip.setismock(true);
			}
			else
			{
				paySlip.setismock(false);
			}
			paySlip.setC_BPartner_ID(m_C_BPartner_ID);
			paySlip.setHR_Process_ID(get_ID());
			paySlip.setFromDate(m_dateFrom);
			paySlip.setHR_Period_ID(period.get_ID());
			paySlip.setToDate(m_dateTo);
			paySlip.setempattendance(employeeUtil.getPresentDays(false).intValue());
			paySlip.setworkingdays(getWokingDays());
			
			Double grossal= getGrossSalary();
			
			// Round the gross sal 
			BigDecimal grossRounddedSal = new BigDecimal(grossal);
			grossRounddedSal = grossRounddedSal.setScale(2, BigDecimal.ROUND_HALF_DOWN);
			
			paySlip.setgrosssalary(grossRounddedSal);
			Double GrosDed=getConcept("GrossDeductions");
			paySlip.setgrossdeductions(new BigDecimal(GrosDed));
			
			
			Double netsal = getConcept("NetSalary");
			
			// Round the sal
			BigDecimal netRounddedSal = new BigDecimal(netsal);
			netRounddedSal = netRounddedSal.setScale(2, BigDecimal.ROUND_HALF_DOWN);
			paySlip.setnetsalary(netRounddedSal);
			
			
			
			BigDecimal gd = new BigDecimal(GrosDed);
			BigDecimal extraEarnings = (netRounddedSal.add(gd)).subtract(grossRounddedSal);
			paySlip.setgrossearnings(extraEarnings);
			
			
			paySlip.setIsActive(true);
			
			if(!paySlip.save(get_TrxName()))
			{
				log.log(Level.SEVERE, "PaySlip Can't Be Generate, Save Not Happened");
			}
		}
		return new Double(0.0);
	}
    //--------------------
    

    
    private void getSalaryComponents()
    {
    	 StringBuffer salCompWhere = null;
		
         List<MSalStructure> components = null;
    	salCompWhere = new StringBuffer().append(MSalStructure.COLUMNNAME_HR_Sal_Struct_Header_ID)
    	.append(" = ").append("(SELECT "
    			+MSalStructure.COLUMNNAME_HR_Sal_Struct_Header_ID+ " FROM "
    			+X_HR_Designation.Table_Name
    			+" WHERE " 
    			+X_HR_Designation.COLUMNNAME_HR_Designation_ID
    			+" = "
    			+" (SELECT "
    			+X_HR_Designation.COLUMNNAME_HR_Designation_ID
    			+" FROM "
    			+ MBPartner.Table_Name
    			+ " WHERE "
    			+MBPartner.COLUMNNAME_C_BPartner_ID
    			+" = "+m_C_BPartner_ID+")"
    			+")");
    	components =  new Query(getCtx(),MSalStructure.Table_Name,salCompWhere.toString(),get_TrxName()).list();
    	
    	for(MSalStructure comp : components)
    	{
    		BigDecimal percentage = comp.getPercentage();
//    		if(percentage.compareTo(BigDecimal.valueOf(0.0)) != 0)
//    		{
    			if(comp.getHR_Sal_Earn_Comp_ID()==EagleConstants.SAL_BASIC)
    			{
    				basicPercentage = percentage.doubleValue();
    			}
    			else if(comp.getHR_Sal_Earn_Comp_ID()==EagleConstants.SAL_HRA)
    			{
    				HRAPercentage = percentage.doubleValue();
    			}
    			else if(comp.getHR_Sal_Earn_Comp_ID()==EagleConstants.SAL_DA)
    			{
    				DAPercentage = percentage.doubleValue();
    			}
    			else if(comp.getHR_Sal_Earn_Comp_ID()==EagleConstants.SAL_LTA)
    			{
    				LTAPercentage = percentage.doubleValue();
    			}
    			else if(comp.getHR_Sal_Earn_Comp_ID()==EagleConstants.SAL_SA)
    			{
    				saPercentage = percentage.doubleValue();
    			}
    			else if(comp.getHR_Sal_Earn_Comp_ID()==EagleConstants.SAL_MDA)
    			{
    				MDAPercentage = percentage.doubleValue();
    			}
    			else if(comp.getHR_Sal_Earn_Comp_ID()==EagleConstants.SAL_CA)
    			{
    				caPercentage = percentage.doubleValue();
    			}
//    		}
    	}
    }
    

	public Double getEarnedSalary()
	{
		if(null == earnedSalary){
			
			if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
				
				earnedSalary =  employeeUtil.getEarnedSalary(true);
			}
			else{
				
				earnedSalary =  employeeUtil.getEarnedSalary(false);
			}
		}
		
		return earnedSalary;
		
	}
    
    public Double getBasicSalary()
    {
    	
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	if(! employeeUtil.isPFEmployee()){
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	
    	else if (null == basicSalary)
    	{
    		if(MSysConfig.getValue(EagleConstants.PAYROLL_MODE,"Custom").equalsIgnoreCase(EagleConstants.PAYROLL_MODE_VALUE))
    		{
    			if(basicPercentage == null)
    				getSalaryComponents();
    			
    			if( null == basicPercentage) { // 201201070806

    				return EagleConstants.DOUBLE_ZERO;
    			}
    				
    			
    			basicSalary = (getGrossSalary()*basicPercentage)/100;
    		}
    		else
    		{
    			Double basicPercentage = MSysConfig.getDoubleValue(EagleConstants.BASIC_PERCENTAGE, 0.6);
    			basicSalary =  (getEarnedSalary() * basicPercentage);
    		}
    	}
    	return basicSalary;
    }
    
    public Double getHRA(){
    	
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	if(! employeeUtil.isPFEmployee())
    	{
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else {
    		
		    	if(HRAPercentage == null)
		    		getSalaryComponents();
		    	
		    	if(null != HRAPercentage) {
		    		// 201201070806
		    		return (getGrossSalary() * HRAPercentage)/100;
		    	}else {
		    		
		    		return EagleConstants.DOUBLE_ZERO;
		    	}
		    		
    	}
    }
    
    /**
     * Get LTA amount for the employee for period
     * 
     * @return	: Employee LTA amount
     */
    public Double getLTA(){
    	
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	
    	if(! employeeUtil.isPFEmployee()) {
    		
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else {
    		// 251220110221
    			if(LTAPercentage == null)
		    	
    				getSalaryComponents();
    			
    			if(LTAPercentage == null) {

    				// 201201070806
    				return EagleConstants.DOUBLE_ZERO;
    			}
    				
    			
		    	return (getGrossSalary()*LTAPercentage)/100;
    	}
    }
    
    /**
     * Get PF amount for the employee
     * 
     * @return	: PF amount for the employee
     */
    public Double getPF(){
    	
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	
    	if(! employeeUtil.isPFEmployee()) {
    		
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else
    	{
    		Double pfamt =new Double(0.0);
    		if(MSysConfig.getValue(EagleConstants.PAYROLL_MODE,"Custom").equalsIgnoreCase(EagleConstants.PAYROLL_MODE_VALUE))
    		{
    			StringBuffer where = new StringBuffer();
    	    	where.append(MHREmployee.COLUMNNAME_C_BPartner_ID).append("  = ").append(m_C_BPartner_ID).append("  AND ")
    	    	.append(MHREmployee.COLUMNNAME_hasoptedpf).append(" = ").append("'Y'");
    	    	
    	    	MHREmployee emp =new Query(getCtx(),MHREmployee.Table_Name,where.toString(),get_TrxName()).first();
    	    	
    	      	
    	    	if(emp != null)
    	    	{
    	    		pfamt =MSysConfig.getDoubleValue(EagleConstants.PF_PERCNTG, 0);
    	   		 // PF amount in sysconfig is percentage of Basic salary
    	    		
    	    		Double basicSal = (null == getBasicSalary() ? EagleConstants.DOUBLE_ZERO : getBasicSalary());
    	    		pfamt = (basicSal * pfamt)/100;
    	    	}
    	    	return pfamt;
    		}
    		else
    		{
    			Double pfMaxLimit = MSysConfig.getDoubleValue(EagleConstants.PF_COMPARISON_FOR_APPLY, 780);
    			Double pfPercentage = MSysConfig.getDoubleValue(EagleConstants.PF_PERCNTG, 0.12);
    			pfamt = getBasicSalary() * pfPercentage;
    			if(pfamt > pfMaxLimit)
    			{
    				pfamt = pfMaxLimit;
    			}
    			return pfamt;
    		}
    	}

    }
    
    /**
     * Get DA amount for the employee
     * 
     * @return
     */
    public Double getDA(){
    	
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	
    	if(! employeeUtil.isPFEmployee()) {
    		
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else {
    		
    		// 251220110221
    		
    		if(DAPercentage == null)
			
    			getSalaryComponents();	    	
			    
    		if(null == DAPercentage) {
    			
    			return EagleConstants.DOUBLE_ZERO; // 201201070806
    		}
    			
    		
    		return (getGrossSalary()*DAPercentage)/100;
    		
    		
    	}
    }
    
    public Double getMDA(){
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	
    	if(! employeeUtil.isPFEmployee()) {
    	
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else {

    		if(MDAPercentage == null)
		    
    			getSalaryComponents();
		    	
		    if(MDAPercentage == null)
		    
		    	return EagleConstants.DOUBLE_ZERO;
		    	
		    Double totalAmt = EagleConstants.DOUBLE_ZERO;
		    	
		    if(getESIAmount() <= EagleConstants.DOUBLE_ZERO) {
		    	
		    	Double maxMDALimit = MSysConfig.getDoubleValue(EagleConstants.MEDICAL_ALLOWANCE_COMPARISON, 1250);

		    	totalAmt = (getGrossSalary()*MDAPercentage)/100;
		    		
				if(totalAmt > maxMDALimit) 	{
						
					// Maximum medicall allowances elgible
						
					totalAmt = maxMDALimit;
				}
					
				return totalAmt;
		    
		   	}
		    	
		    	return totalAmt;
    		}
    }
    
    public Double getESIAmount()
    {
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	if(! employeeUtil.isPFEmployee())
    	{
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else
    	{
    		Double esiAmt =new Double(0.0);
    		double grossSalary = getGrossSalary();
    		//
    		//Here checking, Employee is applicable for ESI Amount or not
    		//
    		if(employeeUtil.isESIEmployee())
	    	{
				
					Double esiMaxLimit = MSysConfig.getDoubleValue(EagleConstants.ESI_COMPARISON_FOR_APPLY, 15000);
					Double esiPercentage = MSysConfig.getDoubleValue(EagleConstants.ESI_PERCENTAGE, 0.0175);
					
					//
					// ESI amount in sysconfig is percentage of gross salary
					//
					
					if (grossSalary <= esiMaxLimit) {
						
						esiAmt = (grossSalary * esiPercentage) / 100;
					}	
					
				
				esiAmt = Math.ceil(esiAmt);
				return esiAmt;
			}
	    	return EagleConstants.DOUBLE_ZERO;
    	}
    }
    
    public Double getSA()
    {
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	if(! employeeUtil.isPFEmployee())
    	{
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else
    	{
    		Double saAmt =new Double(0.0);

    		if( saPercentage == null)
    		    
    			getSalaryComponents();
		    	
		    if(saPercentage == null) {
		    	
		    	return EagleConstants.DOUBLE_ZERO;	// 201201070806
		    }
		    
		    
		    saAmt = (getGrossSalary() * saPercentage)/100;
    		
		    return saAmt;
    	}
    }
    public Double getCA()
    {
    	//PF employees we will process salary components, for non PF employees these components will not be considered.
    	if(! employeeUtil.isPFEmployee())
    	{
    		return EagleConstants.DOUBLE_ZERO;
    	}
    	else
    	{
    		Double caAmt =new Double(0.0);
    		
    		if(MSysConfig.getValue(EagleConstants.PAYROLL_MODE,"Custom").equalsIgnoreCase(EagleConstants.PAYROLL_MODE_VALUE))
    		{

        		if( caPercentage == null)
        		    
        			getSalaryComponents();
    		    	
    		    if(caPercentage == null) {
    		    	
    		    	return EagleConstants.DOUBLE_ZERO;		// 201201070806
    		    }
    		    
    		    
    		    caAmt = (getGrossSalary() *caPercentage)/100;
    			
    	    	return caAmt;
    		}
    		else
    		{
    			//This is custom change as per client requirement 
    			Double caPercentage = MSysConfig.getDoubleValue(EagleConstants.CONVEYANCE_ALLOWANCE_PERCENTAGE, 0.5);
    			caAmt = (getEarnedSalary() - (getBasicSalary() + getHRA() + getMDA())) * (caPercentage) ;
    			return caAmt;
    		}
    	}
    }
    
	public Double getPower() 
	{
		StringBuffer where = new StringBuffer();
		where.append(MQuartEmpCharges.COLUMNNAME_C_BPartner_ID).append(" = ")
				.append(m_C_BPartner_ID).append("  AND ")
				.append(MQuartEmpCharges.COLUMNNAME_HR_Period_ID).append(" = ")
				.append(m_HR_Period_ID).append(" AND ")
				.append(MQuartEmpCharges.COLUMNNAME_chargetype).append(" = '")
				.append(MQuartEmpCharges.CHARGETYPE_Power).append("' ");
		List<MQuartEmpCharges> charges = new Query(getCtx(), MQuartEmpCharges.Table_Name, where.toString(), get_TrxName()).list();

		BigDecimal powerCharges = Env.ZERO;
		if (null != charges && (! charges.isEmpty()) ) 
		{
			for (MQuartEmpCharges quarterCharges : charges) 
			{
				powerCharges = powerCharges.add(quarterCharges.getAmount());		// 201201090637
				if ((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)) 
				{
					quarterCharges.setProcessed(true);
					quarterCharges.save(get_TrxName());
				}
			}
			return powerCharges.doubleValue();
		}

		return EagleConstants.DOUBLE_ZERO;
	}
	
	public Double getDish() 
	{
		StringBuffer where = new StringBuffer();
		where.append(MQuartEmpCharges.COLUMNNAME_C_BPartner_ID).append(" = ")
				.append(m_C_BPartner_ID).append("  AND ")
				.append(MQuartEmpCharges.COLUMNNAME_HR_Period_ID).append(" = ")
				.append(m_HR_Period_ID).append(" AND ")
				.append(MQuartEmpCharges.COLUMNNAME_chargetype).append(" = '")
				.append(MQuartEmpCharges.CHARGETYPE_Dish).append("' ");
		List <MQuartEmpCharges> charges = new Query(getCtx(), MQuartEmpCharges.Table_Name, where.toString(), get_TrxName()).list();

		BigDecimal powerCharges = Env.ZERO;
		if (null != charges && (! charges.isEmpty()) ) 
		{
			for(MQuartEmpCharges quarterCharges : charges)
			{
				powerCharges = powerCharges.add(quarterCharges.getAmount());
				if ((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)) 
				{
					quarterCharges.setProcessed(true);
					quarterCharges.save(get_TrxName());
				}
			}
			return powerCharges.doubleValue();
		}
		return EagleConstants.DOUBLE_ZERO;
	}
    
	/**
	 * Get TDS amount for the employee
	 * 
	 * @return	: Employee TDS amount
	 */
    public Double getTDSAmount()  {
    	
    	double tdsAmount = PayrollManager.getEmployeeTDSAmount( m_C_BPartner_ID, 
    															m_HR_Period_ID, 
    															getpayrollprocesstype(), 
    															get_TrxName());
    	
    	return tdsAmount;
    }
	
	public Double getQuarterCharges() {
		
		String where = MQuartEmpCharges.COLUMNNAME_C_BPartner_ID + " = "
				+ m_C_BPartner_ID + " AND "
				+ MQuartEmpCharges.COLUMNNAME_HR_Period_ID + " = "
				+ m_HR_Period_ID;

		MQuartEmpCharges quartCharge = new Query(getCtx(), MQuartEmpCharges.Table_Name, where, get_TrxName()).first();
		BigDecimal amt = new BigDecimal(0.0);
		if (quartCharge != null) {
			amt = quartCharge.getAmount();
		}
		return amt.doubleValue();
	}
    
    public Double getLeaveEncashment()  {
    	
    	// 201201061252
    	 
    	double amt  = EagleConstants.DOUBLE_ZERO;
    	
    	amt = employeeUtil.getLeaveEncasement();
    	
    	return amt;
    }
    
    /**
     * 050120120303
     * 
     * <P>
     * 	Get the employee deduction amount for the loss of pay	<BR>
     * 	leave request<BR>
     * </P>
     * @return
     */
    public Double getLeaveDeductions()  {
    	
    	double amt  = employeeUtil.getLeaveDeductionAmount();
    	
    	return amt;
    }
    
    /**
     * Modified by Giri
     * 
     * @return no of leaves consumed of type Loss of pay, for respective employee
     * 
     * @author Ranjit
     * 
     * Changed the complete logic
     */
    
	public Double getLossOfPayConsumed() {

		double absentDays = employeeUtil.getNoOfEarnedleavesForLeaveType(EagleConstants.LOSS_OF_PAY_LEAVE_TYPE);
		
		return absentDays;
	}
    
	/**
	 * Modified by Giri
	 * @BugNo 			: 1059.
	 * @Identifoier		: 201108241321
	 * @Modification 	: getting Earned Leave Balance - used Earned Leaves
	 * 
	 * @return No of Balanced Earned Leaves 
	 */
	public Double getEarnedLeaveBalance() {
		String where = MLeaveAssign.COLUMNNAME_C_BPartner_ID + " = "
				+ m_C_BPartner_ID + " AND "
				+ MLeaveAssign.COLUMNNAME_HR_LeaveType_ID + " = "
				+ EagleConstants.EARNED_LEAVE_TYPE + " AND "
				+ MLeaveAssign.COLUMNNAME_C_Year_ID + " = "
				+ period.getC_Year_ID();

		MLeaveAssign assign = new Query(getCtx(), MLeaveAssign.Table_Name, where, get_TrxName()).first();
		if (assign != null) {
			
			// As leave request is approved & availed then leave balance is updated hence getting only leave balance for employee
			
			double leaveBalance = assign.getbalance_leaves().doubleValue();
			
			return  leaveBalance;
		}
		else
			return EagleConstants.DOUBLE_ZERO;
	}
    
    /**
     * Get the earned leaves consumed by the employee in the period
     * 
     * @return		: Get the number of earned leaves used
     */
    public Double getEarnedLeaveConsumed()
    {
		double  consumed = employeeUtil.getNoOfEarnedleavesForLeaveType(EagleConstants.EARNED_LEAVE_TYPE);
		
		return new Double(consumed);
	}
    
    /**
     * Modified by Giri
     * @BugNo 			: 1059.
	 * @Identifoier		: 201108241321
	 * @Modification 	: getting Casual Leave Balance - used Casual Leaves
     * @return no of leaves consumed of type Earned, for respective employee
     */
	public Double getCasualLeaveBalance() {
		
		String where = MLeaveAssign.COLUMNNAME_C_BPartner_ID + " = "
				+ m_C_BPartner_ID + " AND "
				+ MLeaveAssign.COLUMNNAME_HR_LeaveType_ID + " = "
				+ EagleConstants.CASUAL_LEAVE_TYPE + " AND "
				+ MLeaveAssign.COLUMNNAME_C_Year_ID + " = "
				+ period.getC_Year_ID();

		MLeaveAssign assign = new Query(getCtx(), MLeaveAssign.Table_Name, where, get_TrxName()).first();

		if (assign != null) {
			
			double leaveBalance = assign.getbalance_leaves().doubleValue(); 
			
			return leaveBalance;
		}
		else
			return EagleConstants.DOUBLE_ZERO;
	}
    
    /**
     * 
     * Modified by Giri
   	 * @BugNo 			: 1059.
	 * @Identifoier		: 201108241321
	 * @Modification 	: getting Casual Leave Consumed from getApprovedCasualAndEarnedLeaves (Existing logic removed)
     * @return no of leaves consumed of type Casual, for respective employee
     */
	public Double getCasualLeavesConsumed() {
		
		double  consumed = employeeUtil.getNoOfEarnedleavesForLeaveType(EagleConstants.CASUAL_LEAVE_TYPE);
		
		return consumed;
	}
    
	/**
	 * 050120120303
	 * 
	 * Get the arriers for the employee
	 * 
	 * @return : Arrier amount for the employee
	 */
    public Double getArriers()  {
    	
    	double arriresAmount = EagleConstants.DOUBLE_ZERO;
    	
    	
    	if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
			
    		arriresAmount = employeeUtil.getEmployeeArriers(true);
		}
		else{
			
    		arriresAmount = employeeUtil.getEmployeeArriers(false);
		}
    	
    	
    	
    	return new Double(arriresAmount);
    }
    
    
    public Double updateSalaryChanges()  {
    	
    	if((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)){
    		
    		WTCEmployeeUtil.updateEmployeeSalaryChange(m_C_BPartner_ID,period,get_TrxName(),true);
    	}else{
			
    		WTCEmployeeUtil.updateEmployeeSalaryChange(m_C_BPartner_ID,period,get_TrxName(),false);
		}
    	
    	
    	return new Double(0);
    }
    
    
    
    /**
     * Bug:- Bug 1282 
     * This Method Is Responsible For The Calculating The Total partial Net Salary given 
     * To A Particular Employee of Type Worker .
     * Partial Payroll should be paid  At that time
     *  
     * @return  double Value Either 0 or The Value Sum of partial NetAmounts
     */
    public Double getPartialNetAmount()  {
    	
    	// TODO - Code commented. Kept commented code to get when partial payment module implemented
    	
//    	if(employeeUtil != null)
//    	{
//	    	//We Should Go For this Calculation When This Employee is of Type Worker..
//	    	if(!employeeUtil.isStaff())
//	    	{
//		    	String whereClause = X_HR_Partial_Payroll.COLUMNNAME_IsPaid +"='Y'  "+" AND "+
//		    	                     X_HR_Partial_Payroll.COLUMNNAME_HR_Period_ID+"= "+ m_HR_Period_ID +" AND "+
//		    	                     X_HR_Partial_Payroll.COLUMNNAME_C_BPartner_ID+"= "+m_C_BPartner_ID; 
//		    	
//		    	List <X_HR_Partial_Payroll> partialPayrolls=new Query(Env.getCtx(), 
//		    			                                              X_HR_Partial_Payroll.Table_Name, 
//		    			                                              whereClause, this.get_TrxName()).list();
//		    	
//		    	if(partialPayrolls != null)
//		    	{
//		    		BigDecimal totalPartialNetAmt=Env.ZERO;
//		    		
//		    		for(X_HR_Partial_Payroll partialPayroll: partialPayrolls )
//		    		{
//		    			if(partialPayroll != null)
//		    			{
//		    				totalPartialNetAmt=totalPartialNetAmt.add(partialPayroll.getnetsalary());
//		    				
//		    				//If This Payroll Process Is General Then Only We Make This Partial Payroll 
//		    				//As Processed True
//		    				if ((getpayrollprocesstype()).equalsIgnoreCase(MHRProcess.PAYROLLPROCESSTYPE_General)) 
//		    				{
//		    					partialPayroll.setProcessed(true);
//		    					if(!partialPayroll.save())
//			    				{
//			    					log.log(Level.SEVERE, "Updation On Partail Payroll To Set Processed True Failed...");
//			    				}
//		    				}
//		    			}
//		    		}
//		    		
//		    		return totalPartialNetAmt.doubleValue();
//		    	}
//	    	}
//    	}
    	return new Double(0);
    }
    
    public Double getNetSalary()  {
    	
    	// 201201061252
    	double amt =  employeeUtil.getNetSalary()  - getConcept("GrossDeductions");
    	
    	if(amt < EagleConstants.DOUBLE_ZERO)
    		
    		amt = EagleConstants.DOUBLE_ZERO;
    	
    	return new Double(amt);
    	
    }
    
    
}	//	MHRProcess
