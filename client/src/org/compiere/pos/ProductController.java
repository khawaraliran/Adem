package org.compiere.pos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JComboBox;

import org.compiere.util.DB;
import org.compiere.util.KeyNamePair;

public class ProductController implements ActionListener , KeyListener
{
	private SubCurrentLine form = null;
	private PosTextField f_name = null;
	private long lastKeyboardEvent = 0;
	private boolean searched = false;
	private boolean selectlock = false; 
	private javax.swing.Timer timer = null;  
	private JComboBox<KeyNamePair> component = null;
	private int m_PriceListVersion_ID = 0;
	
	public ProductController (SubCurrentLine form, PosTextField f_name, long lastKeyboardEvent)
	{
		this.form = form;
		this.f_name = f_name;
		this.lastKeyboardEvent = lastKeyboardEvent;
	}
	
	public void setLastKeyboardEvent(long lastKeyboardEvent)
	{
		this.lastKeyboardEvent = lastKeyboardEvent;
	}
	
	public void setTimer(javax.swing.Timer timer)
	{
		this.timer = timer; 
	}
	
	public void setFillingComponent(JComboBox<KeyNamePair> component)
	{
		this.component = component;
		component.addActionListener(this);
		component.addKeyListener(this);
	}
	
	public void setPriceList_ID(int pricelist_id)
	{
		this.m_PriceListVersion_ID = pricelist_id;
	}
			
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==timer)
		{
			long now = System.currentTimeMillis();
			
			if( (now - lastKeyboardEvent) > 500 && !searched && f_name.getText()!= null && f_name.getText().length()>2)
			{
				searched = true;
				executeQuery();
			}
			else if(!searched && (f_name.getText()== null ||  f_name.getText().length() == 0))
			{
				component.hidePopup();
				component.removeAllItems();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==40 && e.getSource()==f_name) // Key down on product text field
		{
			component.requestFocus();
		}
		else if (e.getSource()==f_name) //writing product name or value
		{
			searched = false;
			this.lastKeyboardEvent = System.currentTimeMillis();
			timer.restart();
		}
		else if(e.getKeyCode()==10 && e.getSource()==component) //Enter on component field
		{
			KeyNamePair item = (KeyNamePair) component.getSelectedItem();
			if(item!=null && !selectlock)
			{
				f_name.setText(item.getName().substring(0, item.getName().indexOf("_")));
				form.findProduct();
				form.updateInfo();
				component.removeAllItems();
				f_name.requestFocus();
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {			
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}
	
	
	private void executeQuery()
	{
		component.hidePopup();
		
		String sql = "SELECT M_Product.M_Product_ID, M_Product.Value, M_Product.Name "
				+ " FROM M_Product M_Product "
				+ " WHERE EXISTS (SELECT 1 FROM M_ProductPrice pp "
				+ " WHERE pp.M_Product_ID = M_Product.M_Product_ID "
				+ " AND pp.M_PriceList_Version_ID = ? "
				+ " AND pp.IsActive='Y') "
				+ " AND (UPPER(M_Product.Name) like UPPER('"+ "%" + f_name.getText().replace(" ", "%") + "%" +"')"
				+ " OR UPPER(M_Product.Value) like UPPER('" + "%" + f_name.getText().replace(" ", "%") + "%" + "')) "
				+ " ORDER By 3";
		
		PreparedStatement pstmt = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_PriceListVersion_ID);

			ResultSet rs = pstmt.executeQuery();

			component.removeAllItems();
			
			selectlock = true;
			
			while (rs.next())
				component.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2) + "_" + rs.getString(3)));
			
			
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		component.showPopup();
		selectlock = false; 
	}
}