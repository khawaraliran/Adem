/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.FillMandatoryException;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.wtc.util.WTCTimeUtil;

/**
 *  Physical Inventory Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInventory.java 1032 2012-02-15 05:03:13Z phani $
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 * 			<li>FR [ 1948157  ]  Is necessary the reference for document reverse
 * 			<li> FR [ 2520591 ] Support multiples calendar for Org 
 *			@see http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962 	
 *  @author Armen Rizal, Goodwill Consulting
 * 			<li>BF [ 1745154 ] Cost in Reversing Material Related Docs
 *  @see http://sourceforge.net/tracker/?func=detail&atid=879335&aid=1948157&group_id=176962
 *  
 *  @author				@changeID		 @IssueID		@Description 
 *  PhaniKiran.Gutha	20111226913			1673			negative qty should not be used
 * 
 *  D. Yadagiri Rao		20120104			1687		create a inventory record Using Maintenance 
 * 	D. Yadagiri Rao		20120105			1687		cost details return negative value now we change to positive 
 *  PhaniKiran.Gutha	20120113			1933			 MInventory (MWarehouse wh) constructor chagned to add the movement type to inventoryIn and request date as the current date
 *  PhaniKiran.Gutha	20120113			1933		MInventory (MWarehouse wh) constructor chagned to add the movement type to inventoryIn and request date as the current date
 *  D. Yadagiri Rao		201202072212		2067		Set isApproved And isHodApproved at unlockIt() Method
 *  PhaniKiran.Gutha	20120210			2038		Create linema with new attribute set instance if attribute set instance is not available in the line. And inventory is not a return.
 * 
 */
public class MInventory extends X_M_Inventory implements DocAction
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: MInventory.java 1032 2012-02-15 05:03:13Z phani $";
	/**
	 * 
	 */
	private static final long serialVersionUID = 910998472569265447L;


	/**
	 * 	Get Inventory from Cache
	 *	@param ctx context
	 *	@param M_Inventory_ID id
	 *	@return MInventory
	 */
	public static MInventory get (Properties ctx, int M_Inventory_ID)
	{
		Integer key = new Integer (M_Inventory_ID);
		MInventory retValue = (MInventory) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MInventory (ctx, M_Inventory_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MInventory> s_cache = new CCache<Integer,MInventory>("M_Inventory", 5, 5);


	/**
	 * 	Standard Constructor
	 *	@param ctx context 
	 *	@param M_Inventory_ID id
	 *	@param trxName transaction
	 */
	public MInventory (Properties ctx, int M_Inventory_ID, String trxName)
	{
		super (ctx, M_Inventory_ID, trxName);
		if (M_Inventory_ID == 0)
		{ 
		//	setName (null);
		//  setM_Warehouse_ID (0);		//	FK
			setMovementDate (new Timestamp(System.currentTimeMillis()));
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setIsApproved (false);
			setMovementDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setPosted (false);
			setProcessed (false);
		}
	}	//	MInventory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MInventory (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInventory
	
	
	/**
	 * 
	 * 
	 * @param mMaintenance
	 */
	public MInventory (MWTCMaintenance mMaintenance)
	{
		this (mMaintenance.getCtx(), 0, mMaintenance.get_TrxName());
		setClientOrg(mMaintenance);
		this.setWTC_Maintenance_ID(mMaintenance.getWTC_Maintenance_ID());

	}
	
	/**
	 * 
	 * Setting movement date,request date and movement type to M_Inventory
	 * @param mMaintenance
	 * @param type
	 */
	public MInventory(MWTCMaintenance mMaintenance,
			MWTCMaintenanceType type) {

		this ( mMaintenance );
		setMovementDate( mMaintenance.getStartTime() );
		setRequestDate( mMaintenance.getEndTime() );
		setDescription( mMaintenance.getDescription() );
		setWFState("Drafted");
		setDocAction(DocAction.ACTION_Complete);
		setDocStatus(DOCSTATUS_Drafted);
		setMovementType( X_M_Inventory.MOVEMENTTYPE_InventoryOut );
		int wareHouseId = Env.getContextAsInt(Env.getCtx(), "M_Warehouse_ID");
		this.setM_Warehouse_ID(wareHouseId);


		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Warehouse Constructor
	 *	@param wh warehouse
	 */
	public MInventory (MWarehouse wh)
	{
		this (wh.getCtx(), 0, wh.get_TrxName());
		setClientOrg(wh);
		setM_Warehouse_ID(wh.getM_Warehouse_ID());
		//20120113
		setRequestDate( WTCTimeUtil.getSystemCurrentTimestamp() ); 
		setMovementType( X_M_Inventory.MOVEMENTTYPE_InventoryOut );
	}	//	MInventory
	
	
	/**	Lines						*/
	private MInventoryLine[]	m_lines = null;
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public MInventoryLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		//
		List<MInventoryLine> list = new Query(getCtx(), MInventoryLine.Table_Name, "M_Inventory_ID=?", get_TrxName())
										.setParameters(new Object[]{get_ID()})
										.setOrderBy(MInventoryLine.COLUMNNAME_Line)
										.list();
		m_lines = list.toArray(new MInventoryLine[list.size()]);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	/**
	 * 	Overwrite Client/Org - from Import.
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInventory[");
		sb.append (get_ID())
			.append ("-").append (getDocumentNo())
			.append (",M_Warehouse_ID=").append(getM_Warehouse_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

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
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getC_DocType_ID() == 0)
		{
			MDocType types[] = MDocType.getOfDocBaseType(getCtx(), MDocType.DOCBASETYPE_MaterialPhysicalInventory);
			if (types.length > 0)	//	get first
				setC_DocType_ID(types[0].getC_DocType_ID());
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @C_DocType_ID@"));
				return false;
			}
		}
		
//		Default Warehouse
		if (getM_Warehouse_ID() == 0)
		{
			
				throw new FillMandatoryException(COLUMNNAME_M_Warehouse_ID);
		}
		//20111226912
		/*In before save of the document, if isUrgent is true and Urgency is reason is populated or 
		( Need by Date is equals to Request Date(only date comparison, truncate time part ) 
		and Requested Time( no date component)  is greater than   MAX_INDENT_CRETAION_TIME then set  
		isRedIndent to true*/
		boolean isEqual=false;  // Need by Date is equals to Request Date(only date comparison, truncate time part )
		
		Timestamp reqDate=TimeUtil.trunc(this.getRequestDate(),null);
		Timestamp moveDate=TimeUtil.trunc(this.getMovementDate(),null);

		String timeConfigured=MSysConfig.getValue("MAX_INDENT_CRETAION_TIME", "12:00");
		
		String delim = ":";
		String[] tokens = timeConfigured.split(delim);
		int confHour=new Integer(tokens[0]);
		int confMinits=new Integer(tokens[1]);
		
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(this.getRequestDate());
		int hours=cal.get(Calendar.HOUR_OF_DAY);                   
	    int minits=cal.get(Calendar.MINUTE);
		
		if(moveDate.equals(reqDate) && ((confHour>=hours)&&(confMinits>=minits)))
		{
			isEqual=true;
		}
		
		if((this.isUrgent() && !((this.getUrgencyReason()).isEmpty()))  ||  isEqual )
		{
			this.setIsRedIndent(true);
		}
		else
		{
			this.setIsRedIndent(false);
		}

		
		
		
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		//
		final String sql = "UPDATE M_InventoryLine SET Processed=? WHERE M_Inventory_ID=?";
		int noLine = DB.executeUpdateEx(sql, new Object[]{processed, getM_Inventory_ID()}, get_TrxName());
		m_lines = null;
		log.fine("Processed=" + processed + " - Lines=" + noLine);
	}	//	setProcessed

	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
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
		log.info(toString());
		
		//
		// 201202072212
		// Introduced one more node to Workflow Implemented This Method
		// This node used for Restarting workflow From First If at anybody rejected the workflow
		//
		
		setIsApproved( Boolean.FALSE );
		setIsHODApproved( Boolean.FALSE );
		setIsIssued( Boolean.FALSE );
		setProcessing( Boolean.FALSE );
		return  Boolean.TRUE ;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Std Period open?
		MPeriod.testPeriodOpen(getCtx(), getMovementDate(), MDocType.DOCBASETYPE_MaterialPhysicalInventory, getAD_Org_ID());
		MInventoryLine[] lines = getLines(false);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}

		//	TODO: Add up Amounts
	//	setApprovalAmt();
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		if(! isHODApproved())
		{
			this.setIsHODApproved(true);
		}
		
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		setDocAction(STATUS_Voided);

		return true;
	}	//	rejectIt
	
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

		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());

		MInventoryLine[] lines = getLines(false);
		for (MInventoryLine line : lines)
		{
			if (!line.isActive())
				continue;

			MProduct product = line.getProduct();	

			if( isHODApproved() && isApproved() && ( line.getQtyInternalUse() != null && line.getQtyInternalUse().compareTo( Env.ZERO) == 0) ){
				line.setQtyInternalUse( line.getRequest_Qty() );
				line.saveEx(); 
			}
			//Get Quantity to Inventory Inernal Use
			BigDecimal qtyDiff = line.getQtyInternalUse();
			//If Quantity to Inventory Internal Use = Zero Then is Physical Inventory  Else is  Inventory Internal Use 
			if (qtyDiff.signum() == 0)
				qtyDiff = line.getQtyCount().subtract(line.getQtyBook());

			
			int deptID=this.getHR_Department_ID();
			int productID=line.getM_Product_ID();
			Timestamp requestDate=TimeUtil.trunc(this.getRequestDate(), null);
			if(this.getMovementType().equalsIgnoreCase(MInventory.MOVEMENTTYPE_InventoryOut))
			{//20111226912
				String updatesql="UPDATE  WTC_Dept_Consumption_Limit SET Act_Consumption=Act_Consumption +"+line.getQtyInternalUse()+
						" WHERE m_product_id="+productID+"  and hr_department_id="+deptID+" and " +
				        " ('"+requestDate+"' between fromdate and todate)";
				
				int no = DB.executeUpdate(updatesql, get_TrxName());
				if (no != 1)
				{
					log.warning("(1) #" + no);
				}
			}
			else if(this.getMovementType().equalsIgnoreCase(MInventory.MOVEMENTTYPE_InventoryIn))
			{//20111226912
				String updatesql="UPDATE WTC_Dept_Consumption_Limit  SET Act_Consumption=Act_Consumption -"+line.getQtyInternalUse()+
				" WHERE m_product_id="+productID+"  and hr_department_id="+deptID+" and " +
		        " ('"+requestDate+"' between fromdate and todate)";
		
				int no = DB.executeUpdate(updatesql, get_TrxName());
				if (no != 1)
				{
					log.warning("(1) #" + no);
				}
			}
			
			//Ignore the Material Policy when is Reverse Correction
			if(!isReversal())
				checkMaterialPolicy(line, qtyDiff);

			//	Stock Movement - Counterpart MOrder.reserveStock
			if (product != null 
					&& product.isStocked() )
			{
				log.fine("Material Transaction");
				MTransaction mtrx = null; 

				if( getMovementType().equals( MTransaction.MOVEMENTTYPE_InventoryOut ) ){
					qtyDiff = qtyDiff.negate();
					
				}
				
				//If AttributeSetInstance = Zero then create new  AttributeSetInstance use Inventory Line MA else use current AttributeSetInstance
				if (line.getM_AttributeSetInstance_ID() == 0 || qtyDiff.compareTo(Env.ZERO) == 0)
				{
					MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
							line.getM_InventoryLine_ID(), get_TrxName());

					for (int j = 0; j < mas.length; j++)
					{
						MInventoryLineMA ma = mas[j];
						BigDecimal QtyMA = ma.getMovementQty();
						
						BigDecimal QtyNew = QtyMA.add(qtyDiff);
						if( getMovementType().equals( MTransaction.MOVEMENTTYPE_InventoryOut ) ){
							QtyMA = QtyMA.negate();
							
						}
						log.fine("Diff=" + qtyDiff 
								+ " - Instance OnHand=" + QtyMA + "->" + QtyNew);

						if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
								line.getM_Locator_ID(),
								line.getM_Product_ID(), 
								ma.getM_AttributeSetInstance_ID(), 0, 
								QtyMA, Env.ZERO, Env.ZERO, get_TrxName()))
						{
							m_processMsg = "Cannot correct Inventory (MA)";
							return DocAction.STATUS_Invalid;
						}

						// Only Update Date Last Inventory if is a Physical Inventory
						if(line.getQtyInternalUse().compareTo(Env.ZERO) == 0)
						{	
							MStorage storage = MStorage.get(getCtx(), line.getM_Locator_ID(), 
									line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), get_TrxName());		 				
							storage.setDateLastInventory(getMovementDate());
							if (!storage.save(get_TrxName()))
							{
								m_processMsg = "Storage not updated(2)";
								return DocAction.STATUS_Invalid;
							}
						}

						//	Transaction
						mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(), getMovementType(),
								line.getM_Locator_ID(), line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
								QtyMA, getMovementDate(), get_TrxName());

						mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
						if (!mtrx.save())
						{
							m_processMsg = "Transaction not inserted(2)";
							return DocAction.STATUS_Invalid;
						}
						qtyDiff = QtyNew;						

					}	
				}

				//sLine.getM_AttributeSetInstance_ID() != 0
				// Fallback
				if (mtrx == null)
				{
					//Fallback: Update Storage - see also VMatch.createMatchRecord
					if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
							line.getM_Locator_ID(),
							line.getM_Product_ID(), 
							line.getM_AttributeSetInstance_ID(), 0, 
							qtyDiff, Env.ZERO, Env.ZERO, get_TrxName()))
					{
						m_processMsg = "Cannot correct Inventory (MA)";
						return DocAction.STATUS_Invalid;
					}

					// Only Update Date Last Inventory if is a Physical Inventory
					if(line.getQtyInternalUse().compareTo(Env.ZERO) == 0)
					{	
						MStorage storage = MStorage.get(getCtx(), line.getM_Locator_ID(), 
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), get_TrxName());						

						storage.setDateLastInventory(getMovementDate());
						if (!storage.save(get_TrxName()))
						{
							m_processMsg = "Storage not updated(2)";
							return DocAction.STATUS_Invalid;
						}
					}

					//	Transaction
					mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(), getMovementType(),
							line.getM_Locator_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							qtyDiff, getMovementDate(), get_TrxName());
					mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
					if (!mtrx.save())
					{
						m_processMsg = "Transaction not inserted(2)";
						return DocAction.STATUS_Invalid;
					}

					//CostEngineFactory.getCostEngine(getAD_Client_ID()).createCostDetail(mtrx);
				}	//	Fallback
			}	//	stock movement

		}	//	for all lines

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE); 
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		// Set the definite document number after completed (if needed)
		setDefiniteDocumentNo();
		setDefectSparesCost(getWTC_Defect_ID());

		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setMovementDate(new Timestamp (System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
			if (value != null)
				setDocumentNo(value);
		}
	}
	
	/**
	 * 
	 * @author Giri
	 * @ChangeID: 20140105
	 * <BR> 
	 * If this Indent raised based on the defect then we calculate the sparecost amount from M_costDetail 
	 * 
	 * @param defectId
	 */
	private void setDefectSparesCost(int defectId) {

		if (defectId > 0) {
			String sql = "SELECT sum( amt ) FROM M_costDetail mcd "
					+ " JOIN M_InventoryLine mil ON mcd.M_InventoryLine_ID = mil.M_InventoryLine_ID"
					+ " JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID"
					+ " JOIN WTC_Defect def ON mi.WTC_Defect_ID = def.WTC_Defect_ID"
					+ " WHERE def.WTC_Defect_ID = ? ";

			BigDecimal sum = DB.getSQLValueBD(get_TrxName(), sql, defectId);
			
			if(sum.signum() == -1){
				sum = sum.negate();
				
			}	

			MWTCDefect defect = new MWTCDefect(getCtx(), defectId,
					get_TrxName());

			BigDecimal serviceCost = defect.getServiceCost();

			BigDecimal totalCost = Env.ZERO;

			//
			// Check with null if service cost or sum are null then assign
			//
			if (serviceCost == null) {
				serviceCost = Env.ZERO;
			}
			if (sum == null) {
				sum = Env.ZERO;
			}

			totalCost = sum.add(serviceCost);

			defect.setSpareCost(sum);
			defect.setTotalCost(totalCost);
			
			if (defect.save()) {
				log.log(Level.SEVERE, " Defect Saved successfully ");
			} else {
				log.log(Level.SEVERE, "Failed to save Defect ");
			}
		}
	}

	/**
	 * 	Check Material Policy.
	 */
	private void checkMaterialPolicy(MInventoryLine line, BigDecimal qtyDiff)
	{
		MInventoryLineMA.deleteInventoryLineMA(line.getM_InventoryLine_ID(), get_TrxName());

		//	Check Line
		boolean needSave = false;
		//	Attribute Set Instance
		if (line.getM_AttributeSetInstance_ID() == 0)
		{
			MProduct product = MProduct.get(getCtx(), line.getM_Product_ID());
			if ( getMovementType().equals( X_M_Inventory.MOVEMENTTYPE_InventoryIn ) )	//	Incoming Trx
			{//20111226912
					
					Query query = MTable.get(getCtx(), MInventoryLineMA.Table_Name)
					.createQuery(MInventoryLineMA.COLUMNNAME_M_InventoryLine_ID+"=?", get_TrxName() ); 
					query.setParameters( line.getOriginalIndentLine_ID() );
					List<MInventoryLineMA> list = query.setOrderBy( MInventoryLineMA.COLUMNNAME_M_AttributeSetInstance_ID + " ASC ").list();
					List<LineMA> linemas = getReturnLineMA( line );
					
					BigDecimal movQty = line.getMovementQty();
					for( MInventoryLineMA linema : list ){
						BigDecimal alocQty = linema.getMovementQty();
						Boolean found = Boolean.FALSE;
						for( LineMA lma : linemas ){
							if( lma.getAttributeSetId() == linema.getM_AttributeSetInstance_ID() ){
								alocQty = alocQty.subtract( lma.getQty() );
								break;
							}
						}
						 
						if( !found ){
							if( alocQty.compareTo(movQty) > 0 ){
								alocQty = movQty.abs();
							}
						}
						
						if( alocQty.signum() == 0 )
							continue;
						MInventoryLineMA ma = new MInventoryLineMA( line, linema.getM_AttributeSetInstance_ID(), alocQty );
						ma.saveEx();
						movQty = movQty.subtract( alocQty );
						if( movQty.signum() == 0 ){
							break;
						}
					}
					//20120210
					if( list.isEmpty() ){
						
						MAttributeSetInstance attributesetInstance = MAttributeSetInstance.create(getCtx(),  line.getProduct(),  get_TrxName() );
						MInventoryLineMA ma = new MInventoryLineMA( line, attributesetInstance.get_ID(), movQty );
						ma.saveEx();
					}
			}
			else	//	Outgoing Trx
			{
				String MMPolicy = product.getMMPolicy();
				MStorage[] storages = MStorage.getWarehouse(getCtx(), getM_Warehouse_ID(), line.getM_Product_ID(), 0,
						null, MClient.MMPOLICY_FiFo.equals(MMPolicy), true, line.getM_Locator_ID(), get_TrxName());

				BigDecimal qtyToDeliver = qtyDiff;

				for (MStorage storage: storages)
				{					
					if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0)
					{
						MInventoryLineMA ma = new MInventoryLineMA (line, 
								storage.getM_AttributeSetInstance_ID(),
								qtyToDeliver);
						ma.saveEx();		
						qtyToDeliver = Env.ZERO;
						log.fine( ma + ", QtyToDeliver=" + qtyToDeliver);		
					}
					else
					{	
						MInventoryLineMA ma = new MInventoryLineMA (line, 
								storage.getM_AttributeSetInstance_ID(),
								storage.getQtyOnHand());
						ma.saveEx();	
						qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
						log.fine( ma + ", QtyToDeliver=" + qtyToDeliver);		
					}
					if (qtyToDeliver.signum() == 0)
						break;
				}

				//	No AttributeSetInstance found for remainder
				if (qtyToDeliver.signum() != 0)
				{
					//deliver using new asi
					MAttributeSetInstance asi = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
					int M_AttributeSetInstance_ID = asi.getM_AttributeSetInstance_ID();
					MInventoryLineMA ma = new MInventoryLineMA (line, M_AttributeSetInstance_ID , qtyToDeliver);

					ma.saveEx();
					log.fine("##: " + ma);
				}
			}	//	outgoing Trx

			if (needSave)
			{
				line.saveEx();
			}
		}	//	for all lines

	}	//	checkMaterialPolicy

	/**
	 * get the MInventoryLineMA's of the orginial Indent
	 * @param line
	 * @return
	 */
	private List<LineMA> getReturnLineMA(MInventoryLine line){
		
		List<LineMA> linemas = new ArrayList<LineMA>();
		// using original indent line for returns
		String sql = "SELECT ilma.movementQty,ilma.M_AttributeSetInstance_ID " +
				"FROM M_InventoryLineMA ilma " +
				"JOIN M_InventoryLine mil ON ( mil.M_inventoryLine_ID = ilma.M_InventoryLine_ID ) " +
				"WHERE mil.OriginalIndentLine_ID =? AND mil.M_Inventory_ID IN " +
				" (SELECT mi.M_Inventory_ID FROM M_Inventory mi WHERE mi.DocStatus IN ('CO','CL') AND mi.MovementType ='I+') "; 
		
		PreparedStatement pstmt = DB.prepareStatement(sql , null );
		ResultSet rs = null;
		try {
			pstmt.setInt(1, line.getOriginalIndentLine_ID() );
			 rs = pstmt.executeQuery();
			while( rs.next() ){
				LineMA ma = new LineMA();
				ma.setQty( rs.getBigDecimal(1));
				ma.setAttributeSetId(rs.getInt(2)); 
				linemas.add(ma);
			}

		} catch (SQLException e) {
			log.severe( e.getMessage() );
		}finally {
			DB.close(rs, pstmt);
		}
		return linemas;
	}
	
	class LineMA{
		int attributeSetId;
		public int getAttributeSetId() {
			return attributeSetId;
		}
		public void setAttributeSetId(int line_Id) {
			this.attributeSetId = line_Id;
		}
		public BigDecimal getQty() {
			return qty;
		}
		public void setQty(BigDecimal qty) {
			this.qty = qty;
		}
		BigDecimal qty;
	}
	/**
	 * 	Void Document.
	 * 	@return false 
	 */
	public boolean voidIt()
	{
		log.info(toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			//	Set lines to 0
			MInventoryLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MInventoryLine line = lines[i];
				BigDecimal oldCount = line.getQtyCount();
				BigDecimal oldInternal = line.getQtyInternalUse();
				if (oldCount.compareTo(line.getQtyBook()) != 0 
					|| oldInternal.signum() != 0)
				{
					line.setQtyInternalUse(Env.ZERO);
					line.setQtyCount(line.getQtyBook());
					line.addDescription("Void (" + oldCount + "/" + oldInternal + ")");
					line.save(get_TrxName());
				}
			}
		}
		else
		{
			return reverseCorrectIt();
		}
			
		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info(toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;
		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;		

		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return false 
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		MPeriod.testPeriodOpen(getCtx(), getMovementDate(), dt.getDocBaseType(), getAD_Org_ID());

		//	Deep Copy
		MInventory reversal = new MInventory(getCtx(), 0, get_TrxName());
		copyValues(this, reversal, getAD_Client_ID(), getAD_Org_ID());
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		reversal.setIsApproved (false);
		reversal.setPosted(false);
		reversal.setProcessed(false);
		reversal.addDescription("{->" + getDocumentNo() + ")");
		//FR1948157
		reversal.setReversal_ID(getM_Inventory_ID());
		reversal.saveEx();
		reversal.setReversal(true);

		//	Reverse Line Qty
		MInventoryLine[] oLines = getLines(true);
		for (int i = 0; i < oLines.length; i++)
		{
			MInventoryLine oLine = oLines[i];
			MInventoryLine rLine = new MInventoryLine(getCtx(), 0, get_TrxName());
			copyValues(oLine, rLine, oLine.getAD_Client_ID(), oLine.getAD_Org_ID());
			rLine.setM_Inventory_ID(reversal.getM_Inventory_ID());
			rLine.setParent(reversal);
			//AZ Goodwill
			// store original (voided/reversed) document line
			rLine.setReversalLine_ID(oLine.getM_InventoryLine_ID());
			//
			rLine.setQtyBook (oLine.getQtyCount());		//	switch
			rLine.setQtyCount (oLine.getQtyBook());
			rLine.setQtyInternalUse (oLine.getQtyInternalUse().negate());		
			
			rLine.saveEx();

			//We need to copy MA
			if (rLine.getM_AttributeSetInstance_ID() == 0)
			{
				MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
						oLines[i].getM_InventoryLine_ID(), get_TrxName());
				for (int j = 0; j < mas.length; j++)
				{
					MInventoryLineMA ma = new MInventoryLineMA (rLine, 
							mas[j].getM_AttributeSetInstance_ID(),
							mas[j].getMovementQty().negate());
					ma.saveEx();
				}
			}
		}
		//
		if (!reversal.processIt(DocAction.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.saveEx();
		m_processMsg = reversal.getDocumentNo();

		//	Update Reversed (this)
		addDescription("(" + reversal.getDocumentNo() + "<-)");
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		setProcessed(true);
		//FR1948157
		setReversal_ID(reversal.getM_Inventory_ID());
		setDocStatus(DOCSTATUS_Reversed);	//	may come from void
		setDocAction(DOCACTION_None);

		return true;
	}	//	reverseCorrectIt
	
	/**
	 * 	Reverse Accrual
	 * 	@return false 
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return false 
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		return false;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary
	
	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		int userId = new Query( getCtx(), I_AD_User.Table_Name , I_AD_User.COLUMNNAME_C_BPartner_ID +" = " + getC_BPartner_ID() , get_TrxName() )
						.setOnlyActiveRecords( Boolean.TRUE ).firstId(); 
		
		if( userId <= 0 ){
			userId = getUpdatedBy();
		}
		return userId;  
	}	//	getDoc_User_ID
	
	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID
	
	/** Reversal Flag		*/
	private boolean m_reversal = false;
	
	/**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
	private void setReversal(boolean reversal)
	{
		m_reversal = reversal;
	}	//	setReversal
	/**
	 * 	Is Reversal
	 *	@return reversal
	 */
	private boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal

	/**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds) 
			|| DOCSTATUS_Closed.equals(ds)
			|| DOCSTATUS_Reversed.equals(ds);
	}	//	isComplete
	
	public static MInventory[] getIndents() {
		
		ArrayList<MInventory> list = new ArrayList<MInventory>();
		String sql = "SELECT *, AD_Org_ID, HR_department_ID, M_Inventory_ID, urgencyreason "
						+" FROM M_Inventory "  
						+" WHERE requestdate = (select current_date-1) AND isredindent = 'Y' order by requestdate ";


		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new MInventory(Env.getCtx(), rs, null));
			}

		} catch (SQLException e) {
				e.printStackTrace();
		} finally {
			DB.close(rs, pstmt);
		}

		MInventory indents[] = new MInventory[list.size()];
		list.toArray(indents);
		return indents;
	    }

	
	
	
}	//	MInventory
