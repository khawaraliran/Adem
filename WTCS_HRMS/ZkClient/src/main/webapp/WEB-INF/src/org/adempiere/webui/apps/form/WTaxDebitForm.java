/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.webui.apps.form;

import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.apps.form.TaxDebitGen;
import org.compiere.model.I_WTC_Form;
import org.compiere.model.I_WTC_FormType;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.Query;
import org.compiere.model.X_WTC_Form;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.wtc.util.WTCUtil;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Space;

/**
 * Generate Tax Debit Notes (manual) view class
 * 
 */
public class WTaxDebitForm extends TaxDebitGen implements IFormController, EventListener, ValueChangeListener
{
// Kindly do not delete below line as it is being used for svn version maintenance
public static final String svnRevision =  "$Id: WTaxDebitForm.java 1009 2012-02-09 09:16:13Z suman $";
	private static WGenForm form;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(WInOutGen.class);
	//
	private Label lOrg = new Label();
	private WTableDirEditor fOrg;
	private Label lBPartner = new Label();
	private WSearchEditor fBPartner;
	
	private Label lFormType = new Label();
	private WTableDirEditor fFormType;
	
	private Label lexpected = new Label();
	private WTableDirEditor expected;
	
	public WTaxDebitForm()
	{
		log.info("");
		
		form = new WGenForm(this);
		Env.setContext(Env.getCtx(), form.getWindowNo(), "IsSOTrx", "Y");
		
		try
		{
			super.dynInit();
			dynInit();
			zkInit();
			
			form.postQueryEvent();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "init", ex);
		}
	}	//	init
	
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
		lOrg.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		lBPartner.setText("BPartner");
		lFormType.setText( Msg.translate( Env.getCtx(), "WTC_FormType_ID"));
		lexpected.setText("Issuable/Receivable");
		
		Rows rows = form.getParameterPanel().newRows();
		Row row = rows.newRow(); 
		row.appendChild(lOrg.rightAlign());
		row.appendChild(fOrg.getComponent());
		row.appendChild(new Space());
		row.appendChild(lFormType.rightAlign());
		row.appendChild(fFormType.getComponent());
		row.appendChild(new Space());
		Row row1 = rows.newRow();
		row1.appendChild(lexpected.rightAlign());
		row1.appendChild(expected.getComponent());
		row1.appendChild(new Space());
		row1.appendChild(lBPartner.rightAlign());
		row1.appendChild(fBPartner.getComponent()); 
		row1.appendChild(new Space());
		
	}	//	jbInit

	/**
	 *	Fill Picks.
	 *		Column_ID from C_Order
	 *  @throws Exception if Lookups cannot be initialized
	 */
	public void dynInit() throws Exception
	{
		MLookup orgL = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, 2163, DisplayType.TableDir);
		fOrg = new WTableDirEditor ("AD_Org_ID", false, false, true, orgL);
		fOrg.addValueChangeListener(this);
		//
		MLookup bpL = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, 2762, DisplayType.Search);
		fBPartner = new WSearchEditor ("C_BPartner_ID", false, false, true, bpL);
		fBPartner.addValueChangeListener(this);
		
		MLookup bftL = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, WTCUtil.getADColumnID(I_WTC_Form.Table_Name, I_WTC_Form.COLUMNNAME_WTC_FormType_ID), DisplayType.TableDir);
		fFormType = new WTableDirEditor (I_WTC_Form.COLUMNNAME_WTC_FormType_ID, true, false, true, bftL);
		fFormType.addValueChangeListener(this);
		m_formType = new Query( Env.getCtx() , I_WTC_FormType.Table_Name , "1=1" , null ).firstId();
		fFormType.setValue( m_formType );
		MLookup bexl = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, WTCUtil.getADColumnID(I_WTC_Form.Table_Name, I_WTC_Form.COLUMNNAME_Type), DisplayType.List);
		expected = new WTableDirEditor (I_WTC_Form.COLUMNNAME_Type, true, false, true, bexl); 
		expected.addValueChangeListener(this);
		m_IsSOTrx = Boolean.FALSE;
		expected.setValue( X_WTC_Form.TYPE_Issuable );
        
        form.getStatusBar().setStatusLine( "Generate Tax Debit Notes ");//@@
	}	//	fillPicks
    
	/**
	 *  Query Info
	 */
	public void executeQuery()
	{
		executeQuery( form.getMiniTable());
		form.getMiniTable().repaint();
		form.invalidate();
	}   //  executeQuery

	/**
	 *	Action Listener
	 *  @param e event
	 */
	public void onEvent(Event e)
	{
		log.info("Cmd=" + e.getTarget().getId());
		//
		if(fBPartner.equals(e.getTarget()))
		{
		    form.postQueryEvent();
		    return;
		}
		
		//
		validate();
	}	//	actionPerformed
	
	public void validate()
	{
		form.saveSelection();
		
		ArrayList<Integer> selection = getSelection();
		if (selection != null && selection.size() > 0 && isSelectionActive())
			form.generate();
		else
			form.dispose();
	}

	/**
	 *	Value Change Listener - requery
	 *  @param e event
	 */
	public void valueChange(ValueChangeEvent e)
	{
		log.info(e.getPropertyName() + "=" + e.getNewValue());
		if (e.getPropertyName().equals("AD_Org_ID"))
			m_AD_Org_ID = e.getNewValue();
		if (e.getPropertyName().equals("C_BPartner_ID"))
		{
			m_C_BPartner_ID = e.getNewValue();
			fBPartner.setValue(m_C_BPartner_ID);	//	display value
		}else if( e.getPropertyName().equals( I_WTC_FormType.COLUMNNAME_WTC_FormType_ID ) ){
			m_formType = e.getNewValue();
		} else if( e.getPropertyName().equals( I_WTC_Form.COLUMNNAME_Type ) ){
			String val = (String) e.getNewValue();
			if( val.equals( X_WTC_Form.TYPE_Issuable ) ){
				m_IsSOTrx = Boolean.FALSE;
			} else if( val.equals( X_WTC_Form.TYPE_Receivable ) ){
				m_IsSOTrx = Boolean.TRUE;
			} else {
				m_IsSOTrx = null;
			}
		}
		form.postQueryEvent();
	}	//	vetoableChange
	
	/**************************************************************************
	 *	Generate Shipments
	 */
	public String generate()
	{
			
		return generate(form.getStatusBar());
	}	//	generateShipments
	
	public ADForm getForm()
	{
		return form;
	}
	
	
}