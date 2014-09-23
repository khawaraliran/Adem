package org.globalqss.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;

public class MLCOWithholdingType extends X_LCO_WithholdingType {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: MLCOWithholdingType.java 1009 2012-02-09 09:16:13Z suman $";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MLCOWithholdingType(Properties ctx, int LCO_WithholdingType_ID,
			String trxName) {
		super(ctx, LCO_WithholdingType_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLCOWithholdingType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		
		//
		// If the record is new then create an entry in the table LCO_WithholdingRuleConf
		// with LCO_WithholdingType_ID & IsUseBPTaxPayerType = true
		//
		
		if( newRecord && success ) {
			
			X_LCO_WithholdingRuleConf withholdingRuleConf = new X_LCO_WithholdingRuleConf( getCtx(), 0 , get_TrxName() );
			
			withholdingRuleConf.setLCO_WithholdingType_ID(this.getLCO_WithholdingType_ID());
			withholdingRuleConf.setIsUseBPTaxPayerType(Boolean.TRUE);
			
			if( !withholdingRuleConf.save() ) {
				
				log.log( Level.SEVERE, 
						 " Failed to create entry in the table LCO_WithholdingRuleConf for the LCO_WithholdingType_ID - " 
						+ this.getLCO_WithholdingType_ID());
				
				return Boolean.FALSE;
			}
		}
		
		return super.afterSave(newRecord, success);
	}

	@Override
	protected boolean beforeDelete() {

		//
		// Delete the LCO_WithholdingRuleConf entry before deleting the  LCO_WithholdingType 
		//
		
		PreparedStatement pstmtdel = null;
		
		try {
			
			String sql = " DELETE FROM LCO_WithholdingRuleConf WHERE LCO_WithholdingType_ID = ?";

			pstmtdel = DB.prepareStatement( sql,
															  get_TrxName());
			pstmtdel.setInt(1, this.getLCO_WithholdingType_ID());
			int no = pstmtdel.executeUpdate();

			log.info( "LCO_WithholdingRuleConf deleted=" + no);
			pstmtdel.close();
		} catch (SQLException e) {
			
			log.log(Level.SEVERE, "Failed to delete the LCO_WithholdingRuleConf entry for LCO_WithholdingType." + e.getMessage());
			return Boolean.FALSE;
		}
		
		return super.beforeDelete();
	}

}
