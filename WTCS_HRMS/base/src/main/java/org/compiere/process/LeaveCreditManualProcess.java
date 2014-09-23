/**
 * 
 */
package org.compiere.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.model.I_HR_Leave_Assign;
import org.compiere.model.MLeaveAssign;
import org.compiere.model.MWTCLeaveCreditHistory;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.wtc.util.GeneralUtil;

/**
 * This process will credits the provided number of leaves for the 
 * employee for particular leave type
 * 
 * @author Ranjit
 * 
 * Task 		Date		Identifier		Author			Change
 * 
 * 1869			2/1/2012	212012			Ranjit			Added the values to the add_leaves field
 *
 */
public class LeaveCreditManualProcess extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LeaveCreditManualProcess.java 1009 2012-02-09 09:16:13Z suman $";
	
	
	private int recordId				= 0; 
	MLeaveAssign leaveAssign			= null;
	private BigDecimal  noOfLeaves 		= Env.ZERO;
	private String leaveCreditReason    = "";
	

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] params = getParameter();
		
		for(ProcessInfoParameter param : params){
			
			if(param.getParameterName().equalsIgnoreCase("manualleavescredit")){
				
				noOfLeaves = GeneralUtil.getBigDecimalValue(param.getParameter());
			}
			if(param.getParameterName().equalsIgnoreCase("creditLeaveReason")){
				
				leaveCreditReason = (String)param.getParameter();
				
			}
		}
		
		recordId = getRecord_ID();

	}

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {

		if( recordId <= 0 )
			
		return "@Error@";
		
		if( noOfLeaves.intValue() > 0) {
		
			//
			// Get the Leave Assign
			//
			
			leaveAssign = getLeaveAssign();
			
			//
			// Update the MLeaveAssign record
			//
			
			boolean leaveAssignUpdated = updateLeaveAssignRecord(leaveAssign);
			
			if( leaveAssignUpdated ) {
				
				log.log( Level.FINE, 
						 " Employee Leave Assign record updated successfully");
			}else {
				
				addLog("Failed to credit leaves to the employee");
				return "@Error@";
			}
		
		}
		
		
		
		return "Sucess";
	}

	/**
	 * Update the MLeaveAssign record for the employee <BR>
	 * 
	 * Update the Total Leaves & Balance Leaves field with existing values. 
	 * 
	 * 
	 * @param leaveAssign2		: MLeaveAssign
	 * @return					: TRUE if record updated successfully else FALSE
	 */
	
	private boolean updateLeaveAssignRecord(MLeaveAssign leaveAssign) {
		
		if( null == leaveAssign )
			
			return Boolean.FALSE;
		
		BigDecimal  totalLeaves = leaveAssign.gettotal_leaves().add(noOfLeaves);
		BigDecimal  balanceLeaves = leaveAssign.getbalance_leaves().add(noOfLeaves);
		
		
		leaveAssign.settotal_leaves(totalLeaves);
		leaveAssign.setbalance_leaves(balanceLeaves);
		leaveAssign.setadd_leaves(leaveAssign.getadd_leaves().add(noOfLeaves)); // 212012
		
		
		if ( leaveAssign.save() ) {
			
			boolean hisotryCreaded = MWTCLeaveCreditHistory.createLeaveCreditHistory( leaveAssign, 
																					  leaveCreditReason,
															 						  GeneralUtil.getBigDecimalValue(noOfLeaves), 
															 						  get_TrxName());
			if( Boolean.FALSE == hisotryCreaded ) {
				
				log.log( Level.SEVERE, 
						 "Failed to create the hisotry for the employee leave assign - " 
						+ leaveAssign.get_ID());
			}
			
			addLog( noOfLeaves + " Leaves credited to the Employee");
			return Boolean.TRUE;
		}
		
		return false;
	}

	

	/**
	 * Gets the MLeaveAssign for the record
	 * 
	 * @return	: MLeaveAssign
	 */
	private MLeaveAssign getLeaveAssign() {
		
		StringBuffer whereClause = new StringBuffer( MLeaveAssign.COLUMNNAME_HR_Leave_Assign_ID + " = ? " );
		
		leaveAssign = new Query(getCtx(), I_HR_Leave_Assign.Table_Name, whereClause.toString(),  get_TrxName())
						.setParameters( recordId )
						.setOnlyActiveRecords(true)
						.firstOnly();	
		return leaveAssign;
	}

}
