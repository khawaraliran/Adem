
package org.eevolution.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.BusyDialog;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.DesktopTabpanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Tab;
import org.adempiere.webui.component.Tabbox;
import org.adempiere.webui.component.Tabpanels;
import org.adempiere.webui.component.Tabs;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.window.FDialog;
import org.compiere.apps.ADialogDialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.form.FormFrame;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MPInstance;
import org.compiere.model.MPInstancePara;
import org.compiere.model.MPrivateAccess;
import org.compiere.model.MRMA;
import org.compiere.print.ReportCtl;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoUtil;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Space;
import org.adempiere.webui.panel.StatusBarPanel;


/**
 *Generate Shipment And Invoice Manual
 * Bug No:2059
 * @author Anitha.k    
 * @version $Id: WInOutInvoiceGen.java
 * Date :22/02/2012
 */
public class WInOutInvoiceGen extends ADForm implements IFormController, EventListener, ValueChangeListener, 
		                                  ASyncProcess , WTableModelListener
 { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	private boolean			m_selectionActive = true;
	private Object 			m_M_Warehouse_ID = null;
	private Object 			m_C_BPartner_ID = null;
	ProcessInfo pifo = null;
	Trx trxn = null;
	
	private static CLogger log = CLogger.getCLogger(WInOutInvoiceGen.class);
	
	private Tabbox tabbedPane = new Tabbox();
	private Borderlayout selPanel = new Borderlayout();
	private Grid selNorthPanel = GridFactory.newGridLayout();
	private ConfirmPanel confirmPanelSel = new ConfirmPanel(true);
	private ConfirmPanel confirmPanelGen = new ConfirmPanel(false, true, false, false, false, false, false);
	private StatusBarPanel statusBar = new StatusBarPanel();
	private Borderlayout genPanel = new Borderlayout();
	private Html info = new Html();
	private WListbox miniTable = ListboxFactory.newDataTable();
	private BusyDialog progressWindow;
	//private JScrollPane scrollPane = new JScrollPane();
	
	private Label lWarehouse = new Label();
	private WTableDirEditor fWarehouse;
	private Label lBPartner = new Label();
	private WSearchEditor fBPartner;
	private Label     lDocType = new Label();
	private Listbox  cmbDocType = ListboxFactory.newDropdownListbox();
		
	/** User selection */
	private ArrayList<Integer> selection = null;
	private StringBuffer iText = new StringBuffer();
	//private int[] m_ids;

	public void WInOutGen()
	{
		log.info("");
		Env.setContext(Env.getCtx(), getWindowNo(), "IsSOTrx", "Y");
		
	}
	protected void initForm() 
	{
		try
		{
			
			fillPicks();
			zkInit();  
			dynInit();
			Borderlayout contentPane = new Borderlayout();
			this.appendChild(contentPane);
			contentPane.setWidth("99%");
			contentPane.setHeight("100%");
			Center center = new Center();
			center.setStyle("border: none");
			contentPane.appendChild(center);
			center.appendChild(tabbedPane);
			center.setFlex(true);
			South south = new South();
			south.setStyle("border: none");
			contentPane.appendChild(south);
			south.appendChild(statusBar);
			LayoutUtils.addSclass("status-border", statusBar);
			south.setHeight("22px");			
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "init", ex);
		}
	}
		
		/**
		 *	Static Init.
		 *  <pre>
		 *  selPanel (tabbed)
		 *      fOrg, fBPartner
		 *      scrollPane & miniTable
		 *  genPanel
		 *      info
		 *  </pre>
		 *  @throws Exception
		 */
		void zkInit() throws Exception
		{
			selPanel.setWidth("99%");
			selPanel.setHeight("90%");
			selPanel.setStyle("border: none; position: absolute");
			
			DesktopTabpanel tabpanel = new DesktopTabpanel();
			tabpanel.appendChild(selPanel);
			Tabpanels tabPanels = new Tabpanels();
			tabPanels.appendChild(tabpanel);
			tabbedPane.appendChild(tabPanels);
			Tabs tabs = new Tabs();
			tabbedPane.appendChild(tabs);
			Tab tab = new Tab(Msg.getMsg(Env.getCtx(), "Select"));
			tabs.appendChild(tab);
			
			North north = new North();
			selPanel.appendChild(north);
			north.appendChild(selNorthPanel);
			
			South south = new South();
			selPanel.appendChild(south);
			south.appendChild(confirmPanelSel);
			
			Center center = new Center();
			selPanel.appendChild(center);
			center.appendChild(miniTable);
			center.setFlex(true);
//			miniTable.setHeight("99%");
			confirmPanelSel.addActionListener(this);
			//
			tabpanel = new DesktopTabpanel();
			tabPanels.appendChild(tabpanel);
			tabpanel.appendChild(genPanel);
			tab = new Tab(Msg.getMsg(Env.getCtx(), "Generate"));
			tabs.appendChild(tab);
			genPanel.setWidth("99%");
			genPanel.setHeight("90%");
			genPanel.setStyle("border: none; position: absolute");
			center = new Center();
			genPanel.appendChild(center);
			Div div = new Div();
			div.appendChild(info);
			center.appendChild(div);
			south = new South();
			genPanel.appendChild(south);
			south.appendChild(confirmPanelGen);
			confirmPanelGen.addActionListener(this);
			
			lBPartner.setText("BPartner");
			
			Rows rows = new Rows();
			Row row = new Row();
			rows.appendChild( row );
			row.appendChild(lWarehouse.rightAlign());
			row.appendChild(fWarehouse.getComponent());
			row.appendChild(new Space());
			row.appendChild(lBPartner.rightAlign());
			row.appendChild(fBPartner.getComponent());
			row.appendChild(new Space());
						
			row.appendChild(lDocType.rightAlign());
			row.appendChild(cmbDocType);
			row.appendChild(new Space());
			
			selNorthPanel.appendChild(rows);

			
			   
		}	//	
		
		
		public void fillPicks(){
			
 			MLookup orgL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2223, DisplayType.TableDir);
			fWarehouse = new WTableDirEditor ("M_Warehouse_ID", true, false, true, orgL);
			lWarehouse.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
			fWarehouse.addValueChangeListener(this);
			m_M_Warehouse_ID = Env.getContextAsInt( Env.getCtx(), "M_Warehouse_ID");
			fWarehouse.setValue(m_M_Warehouse_ID);
			//m_M_Warehouse_ID = fWarehouse.getValue();
			//	C_Order.C_BPartner_ID
			MLookup bpL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2762, DisplayType.Search);
			fBPartner = new WSearchEditor ("C_BPartner_ID", false, false, true, bpL);
			lBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
			fBPartner.addValueChangeListener(this);
			//Document Type Sales Order/Vendor RMA
			lDocType.setText(Msg.translate(Env.getCtx(), "C_DocType_ID"));
			cmbDocType.addItem(new KeyNamePair(MOrder.Table_ID, Msg.translate(Env.getCtx(), "Order")));
			cmbDocType.addItem(new KeyNamePair(MRMA.Table_ID, Msg.translate(Env.getCtx(), "VendorRMA")));
			cmbDocType.addActionListener(this);
			
			
		}
		
		public void dynInit()
		{
			//  create Columns
			miniTable.addColumn("C_Order_ID");
			miniTable.addColumn("AD_Org_ID");
			miniTable.addColumn("C_DocType_ID");
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
			miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
			miniTable.setColumnClass(5, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateOrdered"));
			miniTable.setColumnClass(6, BigDecimal.class, true, Msg.translate(Env.getCtx(), "TotalLines"));
			//
			miniTable.autoSize();
			miniTable.getModel().addTableModelListener( this );
			//	Info
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateSel"));//@@
			statusBar.setStatusDB(" ");
			//	Tabbed Pane Listener
			tabbedPane.addEventListener(Events.ON_SELECT, this);
			executeQuery();
		
		}
		
		
		private String getOrderSQL()
		{
		//  Create SQL
	        StringBuffer sql = new StringBuffer(
	            "SELECT C_Order_ID, o.Name, dt.Name, DocumentNo, bp.Name, DateOrdered, TotalLines "
	            + "FROM M_InOut_Candidate_v ic, AD_Org o, C_BPartner bp, C_DocType dt "
	            + "WHERE ic.AD_Org_ID=o.AD_Org_ID"
	            + " AND ic.C_BPartner_ID=bp.C_BPartner_ID"
	            + " AND ic.C_DocType_ID=dt.C_DocType_ID"
	            + " AND ic.AD_Client_ID=?");

	        if (m_M_Warehouse_ID != null)
	            sql.append(" AND ic.M_Warehouse_ID=").append(m_M_Warehouse_ID);
	        if (m_C_BPartner_ID != null)
	            sql.append(" AND ic.C_BPartner_ID=").append(m_C_BPartner_ID);
	        
	        // bug - [ 1713317 ] Generate Shipments (manual) show locked records
	        /* begin - Exclude locked records; @Trifon */
	        int AD_User_ID = Env.getContextAsInt(Env.getCtx(), "#AD_User_ID");
	        String lockedIDs = MPrivateAccess.getLockedRecordWhere(MOrder.Table_ID, AD_User_ID);
	        if (lockedIDs != null)
	        {
	            if (sql.length() > 0)
	                sql.append(" AND ");
	            sql.append("C_Order_ID").append(lockedIDs);
	        }
	        /* eng - Exclude locked records; @Trifon */
	          
	        //
	        sql.append(" ORDER BY o.Name,bp.Name,DateOrdered");
	        
	        return sql.toString();
		}
		
		/**
		 * Get SQL for Vendor RMA that need to be shipped
		 * @return sql
		 */
		private String getRMASql()
		{
		    StringBuffer sql = new StringBuffer();
		    
		    sql.append("SELECT rma.M_RMA_ID, org.Name, dt.Name, rma.DocumentNo, bp.Name, rma.Created, rma.Amt ");
		    sql.append("FROM M_RMA rma INNER JOIN AD_Org org ON rma.AD_Org_ID=org.AD_Org_ID ");
		    sql.append("INNER JOIN C_DocType dt ON rma.C_DocType_ID=dt.C_DocType_ID ");
		    sql.append("INNER JOIN C_BPartner bp ON rma.C_BPartner_ID=bp.C_BPartner_ID ");
		    sql.append("INNER JOIN M_InOut io ON rma.InOut_ID=io.M_InOut_ID ");
		    sql.append("WHERE rma.DocStatus='CO' ");
		    sql.append("AND dt.DocBaseType = 'POO' ");
		    sql.append("AND EXISTS (SELECT * FROM M_RMA r INNER JOIN M_RMALine rl ");
		    sql.append("ON r.M_RMA_ID=rl.M_RMA_ID WHERE r.M_RMA_ID=rma.M_RMA_ID ");
		    sql.append("AND rl.IsActive='Y' AND rl.M_InOutLine_ID > 0 AND rl.QtyDelivered < rl.Qty) ");
		    sql.append("AND NOT EXISTS (SELECT * FROM M_InOut oio WHERE oio.M_RMA_ID=rma.M_RMA_ID ");
		    sql.append("AND oio.DocStatus IN ('IP', 'CO', 'CL')) " );
		    sql.append("AND rma.AD_Client_ID=?");
		    
		    if (m_M_Warehouse_ID != null)
	            sql.append(" AND io.M_Warehouse_ID=").append(m_M_Warehouse_ID);
	        if (m_C_BPartner_ID != null)
	            sql.append(" AND bp.C_BPartner_ID=").append(m_C_BPartner_ID);
	        
	        int AD_User_ID = Env.getContextAsInt(Env.getCtx(), "#AD_User_ID");
	        String lockedIDs = MPrivateAccess.getLockedRecordWhere(MRMA.Table_ID, AD_User_ID);
	        if (lockedIDs != null)
	        {
	            sql.append(" AND rma.M_RMA_ID").append(lockedIDs);
	        }
		    
		    sql.append(" ORDER BY org.Name, bp.Name, rma.Created ");

		    return sql.toString();
		}
		
		/**
		 *  Query Info
		 */
		private void executeQuery()
		{
			log.info("");
			int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
			
			String sql = "";
			
			KeyNamePair docTypeKNPair = (KeyNamePair)cmbDocType.getSelectedItem().toKeyNamePair();
			
			if (docTypeKNPair.getKey() == MRMA.Table_ID)
			{
			    sql = getRMASql();
			}
			else
			{
			    sql = getOrderSQL();
			}

			log.fine(sql);
			//  reset table
			int row = 0;
			miniTable.setRowCount(row);
			//  Execute
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
				pstmt.setInt(1, AD_Client_ID);
				ResultSet rs = pstmt.executeQuery();
				//
				while (rs.next())
				{
					//  extend table
					miniTable.setRowCount(row+1);
					//  set values
					miniTable.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
					miniTable.setValueAt(rs.getString(2), row, 1);              //  Org
					miniTable.setValueAt(rs.getString(3), row, 2);              //  DocType
					miniTable.setValueAt(rs.getString(4), row, 3);              //  Doc No
					miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
					miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
					miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
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
		//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
		}   //  executeQuery

	public ADForm getForm() {
		// TODO Auto-generated method stub
		return getForm();
	}
	public void valueChange(ValueChangeEvent e) {

		log.info(e.getPropertyName() + "=" + e.getNewValue());
		if (e.getPropertyName().equals("M_Warehouse_ID"))
			m_M_Warehouse_ID = e.getNewValue();
		if (e.getPropertyName().equals("C_BPartner_ID"))
		{
			m_C_BPartner_ID = e.getNewValue();
			fBPartner.setValue(m_C_BPartner_ID);	//	display value
		}
		executeQuery();
	
		
	}
	
	
	public void onEvent(Event e) throws Exception
	{

		log.info("Cmd=" + e.getName());
		//
		if (e.getName().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
			return;
		}
		if (cmbDocType.equals(e.getTarget()) || fWarehouse.equals(e.getTarget()))
		{
			executeQuery();
			return;
		}
		//
		saveSelection();
		if (selection != null
				&& selection.size() > 0
				&& m_selectionActive	//	on selection tab
				&& m_M_Warehouse_ID != null)
		{	
			generateShipments();
		}

	}
		
	
	private void saveSelection()
	{
		log.info("");
		//  ID selection may be pending
//		miniTable.removeActionListener(new ChangeEvent(this));
		//  Array of Integers
		ArrayList<Integer> results = new ArrayList<Integer>();
		selection = null;

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
		selection = results;
		
	}	
	
	boolean shipments = Boolean.FALSE;
	
	private ProcessInfo generateShipments ()
	{
		log.info("M_Warehouse_ID=" + m_M_Warehouse_ID);
		String trxName = Trx.createTrxName("IOG");	
		Trx trx = Trx.get(trxName, true);	//trx needs to be committed too
		//String trxName = null;
		//Trx trx = null;
		
		m_selectionActive = false;  //  prevents from being called twice
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateGen"));
		statusBar.setStatusDB(String.valueOf(selection.size()));

		//	Prepare Process
		int AD_Process_ID = 0;	  
		KeyNamePair docTypeKNPair = (KeyNamePair)cmbDocType.getSelectedItem().toKeyNamePair();
        
        if (docTypeKNPair.getKey() == MRMA.Table_ID)
        {
            AD_Process_ID = 52001; // M_InOut_GenerateRMA - org.adempiere.process.InOutGenerateRMA
        }
        else
        {
            AD_Process_ID = 199;      // M_InOut_Generate - org.compiere.process.InOutGenerate
        }
		
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, 0);
		if (!instance.save())
		{
			info.setContent(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
			return null;
		}
		
		//insert selection
		StringBuffer insert = new StringBuffer();
		insert.append("INSERT INTO T_SELECTION(AD_PINSTANCE_ID, T_SELECTION_ID) ");
		int counter = 0;
		for(Integer selectedId : selection)
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
					String msg = "No Shipments";     //  not translated!
					log.config(msg);
					info.setContent(msg);
					trx.rollback();
					return null;
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
				String msg = "No Shipments";     //  not translated!
				log.config(msg);
				info.setContent(msg);
				trx.rollback();
				return null;
			}
		}
		
		//call process
		ProcessInfo pi = new ProcessInfo ("VInOutGen", AD_Process_ID);
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		//	Add Parameter - Selection=Y
		MPInstancePara ip = new MPInstancePara(instance, 10);
		ip.setParameter("Selection","Y");
		if (!ip.save())
		{
			String msg = "No Parameter added";  //  not translated
			info.setContent(msg);
			log.log(Level.SEVERE, msg);
			return null;
		}
		//	Add Parameter - M_Warehouse_ID=x
		ip = new MPInstancePara(instance, 20);
		ip.setParameter("M_Warehouse_ID", Integer.parseInt(m_M_Warehouse_ID.toString()));
		if (!ip.save())
		{
			String msg = "No Parameter added";  //  not translated
			info.setContent(msg);
			log.log(Level.SEVERE, msg);
			return null;
		}

//		this.lockUI(pi);
		Clients.response(new AuEcho(this, "runProcess", null));
		pifo = pi;
		trxn = trx;
		shipments= true;
		return pi;
	}	//	generateShipments


	
	public void afterShipment(){
		generateInvoices();
	}
	public void runProcess(){
		
		ProcessCtl worker = new ProcessCtl(this, getWindowNo(), pifo, trxn);
		try {                    						
			worker.run();     //  complete tasks in unlockUI / generateShipments_complete						
		} finally{		
			if(shipments)
			generateShipments_complete(pifo);
			else {
				generateInvoice_complete(pifo);
			}
		}
	}
	/**
	 *  Complete generating shipments.
	 *  Called from Unlock UI
	 *  @param pi process info
	 */
	private void generateShipments_complete (ProcessInfo pi)
	{
		//  Switch Tabs
		tabbedPane.setSelectedIndex(1);
		//
		ProcessInfoUtil.setLogFromDB(pi);
		//StringBuffer iText = new StringBuffer();
		iText.append("<b>").append(pi.getSummary())
			.append("</b><br>(")
			.append(Msg.getMsg(Env.getCtx(), "InOutGenerateInfo"))
			//  Shipments are generated depending on the Delivery Rule selection in the Order
			.append(")<br>")
			.append(pi.getLogInfo(true));
		info.setContent(iText.toString());

		//	Reset Selection
		/*
		String sql = "UPDATE C_Order SET IsSelected='N' WHERE " + m_whereClause;
		int no = DB.executeUpdate(sql, null);
		log.config("Reset=" + no);*/

		//	Get results
		int[] ids = pi.getIDs();
		if (ids == null || ids.length == 0){
			this.unlockUI(pifo); 
			Clients.response(new AuEcho(this, "afterShipment", null));
			return;
		}
			
		log.config("PrintItems=" + ids.length);

		confirmPanelGen.getOKButton().setEnabled(false);
		//	OK to print shipments
		if (FDialog.ask(m_WindowNo, this, "PrintShipments"))
		{
			//Clients.showBusy("Processing...", true);
			Clients.response(new AuEcho(this, "onPrint", null));		
			int retValue = ADialogDialog.A_CANCEL;	//	see also ProcessDialog.printShipments/Invoices
			do
			{
				//	Loop through all items
				for (int i = 0; i < ids.length; i++)
				{
					int M_InOut_ID = ids[i];
					ReportCtl.startDocumentPrint(ReportEngine.SHIPMENT, M_InOut_ID, this,getWindowNo(), true);
				}
				ADialogDialog d = new ADialogDialog (m_frame,
					Env.getHeader(Env.getCtx(), m_WindowNo),
					Msg.getMsg(Env.getCtx(), "PrintoutOK?"),
					JOptionPane.QUESTION_MESSAGE);
				retValue = d.getReturnCode();
			}
			while (retValue == ADialogDialog.A_CANCEL);
//			setCursor(Cursor.getDefaultCursor());
		}	//	OK to print shipments

		//
		confirmPanelGen.getOKButton().setEnabled(true);
		
//		this.lockUI();
		Clients.response(new AuEcho(this, "afterShipment", null));
		
	}   //  generateShipments_complete

	/**************************************************************************
	 *	Generate Invoices
	 */
	private void generateInvoices ()
	{
		String trxName = Trx.createTrxName("IVG");
		Trx trx = Trx.get(trxName, true);	//trx needs to be committed too
		//String trxName = null;
		//Trx trx = null;
		
		m_selectionActive = false;  //  prevents from being called twice
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InvGenerateGen"));
		statusBar.setStatusDB(String.valueOf(selection.size()));

		//	Prepare Process
		int AD_Process_ID = 0;
		KeyNamePair docTypeKNPair = (KeyNamePair)cmbDocType.getSelectedItem().toKeyNamePair();
        
        if (docTypeKNPair.getKey() == MRMA.Table_ID)
        {
            AD_Process_ID = 52002; // C_Invoice_GenerateRMA - org.adempiere.process.InvoiceGenerateRMA
        }
        else
        {
            AD_Process_ID = 134;  // HARDCODED    C_InvoiceCreate
        }
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, 0);
		if (!instance.save())
		{
			info.setContent(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
			return;
		}
		
		//insert selection
		/*Selection exist from shipment*/
		StringBuffer insert = new StringBuffer();
		insert.append("INSERT INTO T_SELECTION(AD_PINSTANCE_ID, T_SELECTION_ID) ");
		int counter = 0;
		for(Integer selectedId : selection)
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
					String msg = "No Shipments";     //  not translated!
					log.config(msg);
					info.setContent(msg);
					trx.rollback();
					return;
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
				String msg = "No Shipments";     //  not translated!
				log.config(msg);
				info.setContent(msg);
				trx.rollback();
				return;
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
			info.setContent(msg);
			log.log(Level.SEVERE, msg);
			return;
		}
		para = new MPInstancePara(instance, 20);
		para.setParameter("DocAction", "CO");
		if (!para.save())
		{
			String msg = "No DocAction Parameter added";  //  not translated
			info.setContent(msg);
			log.log(Level.SEVERE, msg);
			return;
		}

//		this.lockUI(pi);
		Clients.response(new AuEcho(this, "runProcess", null));
		pifo = pi;
		trxn = trx;
		shipments= false;   //  complete tasks in unlockUI / generateInvoice_complete
	}	//	generateInvoices

	/**
	 *  Complete generating invoices.
	 *  Called from Unlock UI
	 *  @param pi process info
	 */
	private void generateInvoice_complete (ProcessInfo pi)
	{
		//  Switch Tabs
		tabbedPane.setSelectedIndex(1);
		//
		ProcessInfoUtil.setLogFromDB(pi);
		//StringBuffer iText = new StringBuffer();
		iText.append("<b>").append(pi.getSummary())
			.append("</b><br>(")
			.append(Msg.getMsg(Env.getCtx(), "InvGenerateInfo"))
			//Invoices are generated depending on the Invoicing Rule selection in the Order
			.append(")<br>")
			.append(pi.getLogInfo(true));
		info.setContent(iText.toString());

		//	Reset Selection
		/*
		String sql = "UPDATE C_Order SET IsSelected = 'N' WHERE " + m_whereClause;
		int no = DB.executeUpdate(sql, null);
		log.config("Reset=" + no);*/

		//	Get results
		int[] ids = pi.getIDs();
		if (ids == null || ids.length == 0)
			return;

		confirmPanelGen.getOKButton().setEnabled(false);
		//	OK to print invoices
		if (FDialog.ask(m_WindowNo, this, "PrintInvoices"))
		{
		//	info.append("\n\n" + Msg.getMsg(Env.getCtx(), "PrintInvoices"));
//			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			//Clients.showBusy("Processing...", true);
			Clients.response(new AuEcho(this, "onPrint", null));		
			int retValue = ADialogDialog.A_CANCEL;
			do
			{
				//	Loop through all items
				for (int i = 0; i < ids.length; i++)
				{
					int C_Invoice_ID = ids[i];
					ReportCtl.startDocumentPrint(ReportEngine.INVOICE, C_Invoice_ID, this, getWindowNo(), true);
				}
				ADialogDialog d = new ADialogDialog (m_frame,
					Env.getHeader(Env.getCtx(), m_WindowNo),
					Msg.getMsg(Env.getCtx(), "PrintoutOK?"),
					JOptionPane.QUESTION_MESSAGE);
				retValue = d.getReturnCode();
			}
			while (retValue == ADialogDialog.A_CANCEL);
//			setCursor(Cursor.getDefaultCursor());
		}	//	OK to print invoices

		this.unlockUI(pifo); 
		//
		confirmPanelGen.getOKButton().setEnabled(true);
	}   //  generateInvoices_complete

	/**************************************************************************
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI ()
	{
		progressWindow = new BusyDialog();
		progressWindow.setPage(this.getPage());
		progressWindow.doHighlighted();
	}   //  l//  lockUI
	
	public void tableChanged(WTableModelEvent event) {
		{
			int rowsSelected = 0;
			int rows = miniTable.getRowCount();
			for (int i = 0; i < rows; i++)
			{
				IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0
				if (id != null && id.isSelected())
					rowsSelected++;
			}
			statusBar.setStatusDB(" " + rowsSelected + " ");
		}
		
	}
	
	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi)
	{
		if (progressWindow != null) {
			progressWindow.dispose();
			progressWindow = null;
		}
		
		//  Switch Tabs
		tabbedPane.setSelectedIndex(1);
		
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return this.isUILocked();
	}   //  isUILocked

	/**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
	public void executeASync (ProcessInfo pi)
	{
	}   //  executeASync

	public void lockUI(ProcessInfo pi) {
		progressWindow = new BusyDialog();
		progressWindow.setPage(this.getPage());
		progressWindow.doHighlighted();
		
	}
	
}
