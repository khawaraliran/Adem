package org.adempiere.webui.process;
import java.math.BigDecimal;
import java.util.logging.Level;
import org.adempiere.webui.apps.AEnv;
import org.compiere.model.MDocType;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MQuery;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.EagleConstants;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.wtc.util.EagleMessageConstants;

/**
 * 
 * @author PhaniKiran.Gutha
 *
 *@author				changeID 		Issue		Description	 
 *PhaniKiran.Gutha		20120107458		1673		When Default PO Pricelist doesnot exits then Intiamte User
 */
public class CreateRequisition extends SvrProcess {
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: CreateRequisition.java 1009 2012-02-09 09:16:13Z suman $";

	private int				m_M_Inventory_ID = 0;

	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		
		getRecord_ID();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				continue;
			else if (name.equals("m_M_Inventory_ID"))
				m_M_Inventory_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare
	protected String doIt() 
	{
		m_M_Inventory_ID = getRecord_ID();
		
		if(m_M_Inventory_ID > 0)
		{
			MInventory inv = new MInventory( this.getCtx(),m_M_Inventory_ID,this.get_TrxName());
	        if(inv != null)
	        {
				try
				{
					MRequisition requisition= new MRequisition(inv);
					
					if(requisition != null)
					{
						requisition.setC_BPartner_ID(inv.getC_BPartner_ID());
						
						int userID=Env.getContextAsInt(Env.getCtx(), "#AD_User_ID");
						requisition.setAD_User_ID(userID);
						
						requisition.setM_Warehouse_ID(inv.getM_Warehouse_ID());
						
						requisition.setHR_Department_ID(inv.getHR_Department_ID());
						
						requisition.setWFState(EagleConstants.WF_STATE_REQUESTED);
						
						int docTypeID = MDocType.getDocType(MDocType.DOCBASETYPE_PurchaseRequisition);	
						requisition.setC_DocType_ID(docTypeID);
						
						MPriceList defaultPriceList = MPriceList.getDefault(this.getCtx(), false);
						//20120107458
						if( defaultPriceList == null ){
							String logStr = Msg.getMsg(getCtx(), EagleMessageConstants.NO_DEFAULT_PO_PRICELIST );
							log.severe(logStr);
						}
						requisition.setM_PriceList_ID(defaultPriceList.getM_PriceList_ID());
						
						if ( requisition.save())
						{			 
							MInventoryLine lines[] = inv.getLines(true);
							
			                int totalInventoryLines=lines.length;
			                int requisitionLinesSuccess=0;
			                
							for (int i = 0; i < totalInventoryLines; i++)
							{
								MInventoryLine invLine=lines[i];
								if(invLine != null)
								{
									MRequisitionLine reqLine = new MRequisitionLine(lines[i]);
									reqLine.setM_Requisition_ID(requisition.getM_Requisition_ID());
									if( invLine.getQtyInternalUse().signum() == 0 ){
										reqLine.setQty(  invLine.getRequest_Qty() ); 
									}else {
										reqLine.setQty( invLine.getQtyInternalUse() );
									}
									reqLine.setDrawing_Number(invLine.getDrawing_Number() != null ? invLine.getDrawing_Number() : " ");
									invLine.setCatalog_Number(invLine.getCatalog_Number() != null ? invLine.getCatalog_Number() : " ");
									if(reqLine.save(this.get_TrxName()))
									{
										requisitionLinesSuccess=requisitionLinesSuccess++;
									}
								}
							}
							
							
							//Zooming To Requisition Window
							Trx trx = Trx.get(this.get_TrxName(), false);
							trx.commit();
		
							MQuery mquery = new MQuery("M_Requisition");
							StringBuffer where = new StringBuffer();
							where.append("M_Requisition_ID IN ");
							where.append("( ").append(String.valueOf(requisition.getM_Requisition_ID())).append(" )");
							mquery.addRestriction(where.toString());
							AEnv.zoom(mquery);
							
							String msg=Msg.getMsg(Env.getCtx(), "CREATE_REQUISITION_SUCCESS",new Object[] {new Integer(requisitionLinesSuccess),new Integer(totalInventoryLines)});
							if(msg == null)
							{
								msg="";
							}
							log.log(Level.INFO, msg);
							return msg;
						}
					}
				}
				catch(Exception e)
				{
					log.severe("Failed to created Requisistion" + e.getMessage());
				}
	        }
		}
		
		String msgfail=Msg.getMsg(Env.getCtx(), "FAILED_TO_CREATE_REQUISITION");
		if(msgfail == null)
		{
			msgfail="";
		}
		
		return msgfail;
	}
}


