/**
 * 
 */
package org.compiere.apps.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.apps.IStatusBar;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MPInstance;
import org.compiere.model.MPInstancePara;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;


/**
 * @author PhaniKiran.Gutha
 *
 */
public class TaxDebitGen extends GenForm{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: TaxDebitGen.java 1009 2012-02-09 09:16:13Z suman $";

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(TaxDebitGen.class);
	
	public Object 			m_AD_Org_ID = null;
	public Object 			m_C_BPartner_ID = null;
	public Object			m_formType= null;
	public Object			m_IsSOTrx = null;
	
	public void dynInit() throws Exception
	{
		setTitle("Tax Debit Note Preperation");
		setReportEngineType(ReportEngine.INVOICE);  
		setAskPrintMsg("PrintInvoices");
	}
	

	public void configureMiniTable(IMiniTable miniTable)
	{
		//  create Columns
		miniTable.addColumn("C_Inovoice_ID");
		miniTable.addColumn("AD_Org_ID");
		miniTable.addColumn("C_DocType_ID");
		miniTable.addColumn("WTC_FormType_ID");
		miniTable.addColumn("DocumentNo");
		miniTable.addColumn("C_BPartner_ID");
		miniTable.addColumn("DateOrdered");
		miniTable.addColumn("TotalLines");
		//
		miniTable.setMultiSelection(true);
		//  set details
		miniTable.setColumnClass(0, IDColumn.class, false, " ");
		miniTable.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "AD_Org_ID"));
		miniTable.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "C_DocType_ID"));
		miniTable.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "DocumentNo"));
		miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "WTC_FormType_ID"));
		miniTable.setColumnClass(5, String.class, true, "Business Partner"); 
		miniTable.setColumnClass(6, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateInvoice"));
		miniTable.setColumnClass(7, BigDecimal.class, true, Msg.translate(Env.getCtx(), "GrandTotal"));
		//
		miniTable.autoSize();
	}

	/**
	 *	Save Selection & return selecion Query or ""
	 *  @return where clause like C_Order_ID IN (...)
	 */
	public void saveSelection(IMiniTable miniTable)
	{
		log.info("");
		//  Array of Integers
		ArrayList<Integer> results = new ArrayList<Integer>();
		setSelection(null);

		//	Get selected entries
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0
		//	log.fine( "Row=" + i + " - " + id);
			if (id != null && id.isSelected())
				results.add(id.getRecord_ID());
		}

		if (results.size() == 0)
			return;
		log.config("Selected #" + results.size());
		setSelection(results);
	}	//	saveSelection


	public void executeQuery( IMiniTable miniTable)
	{
		log.info("");
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		//  Create SQL
		
		String sql = "SELECT c.C_Invoice_ID, o.Name, dt.Name, DocumentNo,f.Name, bp.Name, c.DateInvoiced, c.grandtotal "
            + "FROM C_Invoice c, AD_Org o, C_BPartner bp, C_DocType dt,WTC_FormType f "
		+ "WHERE c.AD_Org_ID=o.AD_Org_ID"
        + " AND c.C_BPartner_ID=bp.C_BPartner_ID"
        + " AND c.C_DocType_ID=dt.C_DocType_ID"
        + " AND c.AD_Client_ID=?  "
        + " AND c.FormStatus='E' AND c.DocStatus='CO' AND c.WTC_FormType_ID = f.WTC_FormType_ID "; 
		
		
		if( m_IsSOTrx != null ){
			sql = sql + " AND c.isSoTrx= ?  ";
		}
		
		if( m_formType != null ){
			sql = sql + " AND f.WTC_FormType_ID = ?";
		}
		if( m_C_BPartner_ID != null ){
			sql = sql + " AND c.C_BPartner_ID = ? ";
		}
       
		if( m_AD_Org_ID != null ){
			sql = sql + " AND c.AD_Org_ID =  ? ";
		}

		//  reset table
		int row = 0;
		miniTable.setRowCount(row);
		//  Execute
		try
		{
			int index = 1;
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);  
			pstmt.setInt(index++, AD_Client_ID);
			if( m_IsSOTrx != null ){
				pstmt.setString(index++, m_IsSOTrx == Boolean.TRUE ? "Y" : "N");
			}
			if( m_formType != null ){ 
				pstmt.setInt( index++ , Integer.parseInt( m_formType.toString()) );
			}
			if( m_C_BPartner_ID != null ){
				pstmt.setInt( index++ , Integer.parseInt( m_C_BPartner_ID.toString()) );
			}
			if( m_AD_Org_ID != null ){
				pstmt.setInt( index++ , Integer.parseInt( m_AD_Org_ID.toString()) );
			}
			
			
			ResultSet rs = pstmt.executeQuery(); 
			//
			while (rs.next())
			{
				//  extend table
				miniTable.setRowCount(row+1);
				//  set values
				miniTable.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Invoice_ID
				miniTable.setValueAt(rs.getString(2), row, 1);              //  Org
				miniTable.setValueAt(rs.getString(3), row, 2);              //  DocType
				miniTable.setValueAt(rs.getString(4), row, 3);              //  Doc No
				miniTable.setValueAt(rs.getString(5), row, 4);              //  FormType
				miniTable.setValueAt(rs.getString(6), row, 5);              //  BPartner
				miniTable.setValueAt(rs.getTimestamp(7), row, 6);           //  DateOrdered
				miniTable.setValueAt(rs.getBigDecimal(8), row, 7);          //  TotalLines
				//  prepare next
				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		//
		miniTable.autoSize();
	}   //  executeQuery
	
	
	/**************************************************************************
	 *	Generate Invoices
	 */
	public String generate(IStatusBar statusBar )
	{
		String info = "";
		String trxName = Trx.createTrxName("IVG");
		Trx trx = Trx.get(trxName, true);	//trx needs to be committed too
		
		setSelectionActive(false);  //  prevents from being called twice
		statusBar.setStatusLine( "  Tax Debit Note Generated ");
		statusBar.setStatusDB(String.valueOf(getSelection().size()));

		//	Prepare Process
		int AD_Process_ID = 1000013;
        
        
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, 0);
		if (!instance.save())
		{
			info = Msg.getMsg(Env.getCtx(), "ProcessNoInstance");
			return info;
		}
		
		//insert selection
		StringBuffer insert = new StringBuffer();
		insert.append("INSERT INTO T_SELECTION(AD_PINSTANCE_ID, T_SELECTION_ID) ");
		int counter = 0;
		for(Integer selectedId : getSelection())
		{
			counter++;
			if (counter > 1)
				insert.append(" UNION ");
			insert.append("SELECT ");
			insert.append(instance.getAD_PInstance_ID());
			insert.append(", ");
			insert.append(selectedId);
			insert.append(" FROM DUAL ");
			
			if (counter == 1000) 
			{
				if ( DB.executeUpdate(insert.toString(), trxName) < 0 )
				{
					String msg = "No Invoices";     //  not translated!
					info = msg;
					log.config(msg);
					trx.rollback();
					return info;
				}
				insert = new StringBuffer();
				insert.append("INSERT INTO T_SELECTION(AD_PINSTANCE_ID, T_SELECTION_ID) ");
				counter = 0;
			}
		}
		if (counter > 0)
		{
			if ( DB.executeUpdate(insert.toString(), trxName) < 0 )
			{
				String msg = "No Invoices";     //  not translated!
				info = msg;
				log.config(msg);
				trx.rollback();
				return info;
			}
		}
		
		ProcessInfo pi = new ProcessInfo ("", AD_Process_ID);
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		//	Add Parameters
		MPInstancePara para = new MPInstancePara(instance, 10);
		para.setParameter("Selection", "Y");
		if (!para.save())
		{
			String msg = "No Selection Parameter added";  //  not translated
			info = msg;
			log.log(Level.SEVERE, msg);
			return info;
		}
		
		
		setTrx(trx);
		setProcessInfo(pi);
		
		return info;
	}	//	generateInvoices
}
