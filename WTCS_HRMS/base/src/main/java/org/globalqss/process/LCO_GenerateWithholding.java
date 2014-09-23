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
package org.globalqss.process;


import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.globalqss.model.LCO_MInvoice;

/**
 *	LCO_GenerateWithholding
 *
 *  @author Carlos Ruiz - globalqss - Quality Systems & Solutions - http://globalqss.com 
 *  @version  $Id: LCO_GenerateWithholding.java 1009 2012-02-09 09:16:13Z suman $
 *  
 *  Change History
 *  
 *  Date 			Issue ID		Identifier		Author			Change Description
 *  --------------------------------------------------------------------------------------
 *  
 *  23/01/2012		1969			201201230304	Ranjit			When TDS is applicable for the business partner
 *  																& genrate tds option at the doc type is not "No"
 *  																then only we will calculate the TDS.
 *  
 */
public class LCO_GenerateWithholding extends SvrProcess
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: LCO_GenerateWithholding.java 1009 2012-02-09 09:16:13Z suman $";

	/** The Record						*/
	private int		p_Record_ID = 0;
	private int		paymentNatureId = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			
			
			
			if (para[i].getParameter() == null)
				;	
			else if (name.equals("LCO_WithholdingType_ID"))
			{
				paymentNatureId = para[i].getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		int cnt = 0;
		
		LCO_MInvoice inv = new LCO_MInvoice(getCtx(), p_Record_ID, get_TrxName());
		if (inv.getC_Invoice_ID() == 0)
			throw new AdempiereUserError("@No@ @Invoice@");

		// 201201230304 - [Chqange Id]
		
		MDocType dt = new MDocType(getCtx(),inv.getC_DocTypeTarget_ID(), inv.get_TrxName());
		String genwh = dt.get_ValueAsString("GenerateWithholding");
		
		if( inv.getC_BPartner().istdsapplicable() && genwh != null && !genwh.equals("N")) {
			
			cnt = inv.recalcWithholdings( paymentNatureId );
		}

		
		if (cnt == -1)
			throw new AdempiereUserError("Error calculating withholding, please check log");

		return "@Inserted@=" + cnt;		
	}	//	doIt
	
}	//	LCO_GenerateWithholding