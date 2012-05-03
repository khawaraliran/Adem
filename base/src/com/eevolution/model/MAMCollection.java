package com.eevolution.model;

import java.util.Properties;

import org.compiere.model.MProject;

public class MAMCollection extends X_AM_Collection {

	public MAMCollection(Properties ctx, int AM_Collection_ID, String trxName) {
		super(ctx, AM_Collection_ID, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2580248572083221290L;
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(getC_Project_ID() <= 0)
		{	
			MProject project = new MProject(getCtx(), 0 , get_TrxName());
			project.setName(getName());
			project.setValue(getValue());
			project.saveEx();
		}
		return false;
	}

}
