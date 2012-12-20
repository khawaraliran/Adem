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

package org.adempiere.webui.trees;

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Icon;

import org.compiere.model.MClient;
import org.compiere.model.MMenu;
import org.compiere.model.MRole;
import org.compiere.model.MTree;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Menu;
import org.compiere.print.MPrintColor;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWFNode;

/**
 * Web Tree Node (not a PO).
 * 
 * @author Jan Thielemann - evenos GmbH - jan.thielemann@evenos.de - www.evenos.de
 * @version $Id: WTree.java,v 1.0 2012/12/4 08:22:02 jan.thielemann $
 */
public class WTreeNode {

	/** Tree ID */
	private WTree m_tree;
	/** Node ID */
	private int m_node_ID;
	/** SeqNo */
	private int m_seqNo;
	/** Name */
	private String m_name;
	/** Description */
	private String m_description;
	/** Parent ID */
	private int m_parent_ID;
	/** Summary */
	private boolean m_isSummary;
	/** Image Indicator */
	private String m_imageIndicator;
	/** Index to Icon */
	private int m_imageIndex = 0;
	/** On Bar */
	private boolean m_onBar;
	/** Color */
	private Color m_color;

	/** Already ensured Children*/
	private boolean childrenEnsured = false;
	
	/** Logger */
	private static CLogger log = CLogger.getCLogger(WTreeNode.class);
	
	
	
	/** Children List*/
	private List<WTreeNode> children = new ArrayList();
	
	
	/**
	 * Construct Model TreeNode
	 * 
	 * @param node_ID
	 *            node
	 * @param seqNo
	 *            sequence
	 * @param name
	 *            name
	 * @param description
	 *            description
	 * @param parent_ID
	 *            parent
	 * @param isSummary
	 *            summary
	 * @param imageIndicator
	 *            image indicator
	 * @param onBar
	 *            on bar
	 * @param color
	 *            color
	 */
	public WTreeNode(WTree tree, int node_ID, int seqNo, String name,
			String description, int parent_ID, boolean isSummary,
			String imageIndicator, boolean onBar, Color color) {
		super();
		m_tree = tree;
		m_node_ID = node_ID;
		m_seqNo = seqNo;
		m_name = name;
		m_description = description == null ? "" : description;
		m_parent_ID = parent_ID;
		setSummary(isSummary);
		setImageIndicator(imageIndicator);
		m_onBar = onBar;
		m_color = color;
		
		
	} // WTreeNode


	/*************************************************************************/

	/** Window - 1 */
	public static int TYPE_WINDOW = 1;
	/** Report - 2 */
	public static int TYPE_REPORT = 2;
	/** Process - 3 */
	public static int TYPE_PROCESS = 3;
	/** Workflow - 4 */
	public static int TYPE_WORKFLOW = 4;
	/** Workbench - 5 */
	public static int TYPE_WORKBENCH = 5;
	/** Variable - 6 */
	public static int TYPE_SETVARIABLE = 6;
	/** Choice - 7 */
	public static int TYPE_USERCHOICE = 7;
	/** Action - 8 */
	public static int TYPE_DOCACTION = 8;
	
	
//	public static Icon[] IMAGES = new Icon[] { null,
//		Env.getImageIcon("mWindow", 16), Env.getImageIcon("mReport", 16),
//		Env.getImageIcon("mProcess", 16),
//		Env.getImageIcon("mWorkFlow", 16),
//		Env.getImageIcon("mWorkbench", 16),
//		Env.getImageIcon("mSetVariable", 16),
//		Env.getImageIcon("mUserChoice", 16),
//		Env.getImageIcon("mDocAction", 16) };	
	
	

	/**************************************************************************
	 * Get Tree
	 * 
	 * @return WTree 
	 */
	public WTree getWTree() {
		return m_tree;
	}

	
	/**************************************************************************
	 * Get Node ID
	 * 
	 * @return node id (e.g. AD_Menu_ID)
	 */
	public int getNode_ID() {
		return m_node_ID;
	} // getID


	/**
	 * Set Name
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		if (name == null)
			m_name = "";
		else
			m_name = name;
	} // setName

	
	/**
	 * Get Name
	 * 
	 * @return name
	 */
	public String getName() {
		return m_name;
	} // setName	
	
	
	public List<WTreeNode> getChildren(){
		ensureChildren();
		return children;
	}
	
	
	public void setDescription(String desc){
		if(desc==null)
			m_description = "";
		else
			m_description = desc;
	}
	
	
	
	
	/**
	 * Get SeqNo (Index) as formatted String 0000 for sorting
	 * 
	 * @return SeqNo as String
	 */
	public String getSeqNo() {
		String retValue = "0000" + m_seqNo; // not more than 100,000 nodes
		if (m_seqNo > 99999)
			log.severe("MTreeNode.getIndex - TreeNode Index is higher than 99999");
		if (retValue.length() > 5)
			retValue = retValue.substring(retValue.length() - 5); // last 5
		return retValue;
	} // getSeqNo
	
	
	/**
	 * Return parent
	 * 
	 * @return Parent_ID (e.g. AD_Menu_ID)
	 */
	public int getParent_ID() {
		return m_parent_ID;
	} // getParent

	/**
	 * Print Name
	 * 
	 * @return info
	 */
	public String toString() {
		return m_name;
	} // toString

	
	/**
	 * Get Description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return m_description;
	} // getDescription
	
	
	
	/**
	 * Set Summary (allow children)
	 * 
	 * @param isSummary
	 *            summary node
	 */
	public void setSummary(boolean isSummary) {
		m_isSummary = isSummary;
	} // setSummary

	/**
	 * Set Summary (allow children)
	 * 
	 * @param isSummary
	 *            true if summary
	 */
	public void setAllowsChildren(boolean isSummary) {
		m_isSummary = isSummary;
	} // setAllowsChildren

	/**
	 * Allow children to be added to this node
	 * 
	 * @return true if summary node
	 */
	public boolean isSummary() {
		return m_isSummary;
	} // isSummary

	
	public boolean isLeaf() {
		return !isSummary();
	}
	
	
	/**
	 * Get Image Indicator/Index
	 * 
	 * @param imageIndicator
	 *            image indicator (W/X/R/P/F/T/B) MWFNode.ACTION_
	 * @return index of image
	 */
	public static int getImageIndex(String imageIndicator) {
		int imageIndex = 0;
		if (imageIndicator == null)
			;
		else if (imageIndicator.equals(MWFNode.ACTION_UserWindow) // Window
				|| imageIndicator.equals(MWFNode.ACTION_UserForm))
			imageIndex = TYPE_WINDOW;
		else if (imageIndicator.equals(MWFNode.ACTION_AppsReport)) // Report
			imageIndex = TYPE_REPORT;
		else if (imageIndicator.equals(MWFNode.ACTION_AppsProcess) // Process
				|| imageIndicator.equals(MWFNode.ACTION_AppsTask))
			imageIndex = TYPE_PROCESS;
		else if (imageIndicator.equals(MWFNode.ACTION_SubWorkflow)) // WorkFlow
			imageIndex = TYPE_WORKFLOW;
		
		else if (imageIndicator.equals(MWFNode.ACTION_SetVariable)) // Set
																	// Variable
			imageIndex = TYPE_SETVARIABLE;
		else if (imageIndicator.equals(MWFNode.ACTION_UserChoice)) // User
																	// Choice
			imageIndex = TYPE_USERCHOICE;
		else if (imageIndicator.equals(MWFNode.ACTION_DocumentAction)) // Document
																		// Action
			imageIndex = TYPE_DOCACTION;
		else if (imageIndicator.equals(MWFNode.ACTION_WaitSleep)) // Sleep
			;
		return imageIndex;
	} // getImageIndex
	
	/**
	 * Set Image Indicator and Index
	 * 
	 * @param imageIndicator
	 *            image indicator (W/X/R/P/F/T/B) MWFNode.ACTION_
	 */
	public void setImageIndicator(String imageIndicator) {
		if (imageIndicator != null) {
			m_imageIndicator = imageIndicator;
			m_imageIndex = getImageIndex(m_imageIndicator);
		}
	} // setImageIndicator
	
	
	

	/**
	 * Get Image Indicator
	 * 
	 * @return image indicator
	 */
	public String getImageIndiactor() {
		return m_imageIndicator;
	} // getImageIndiactor
	

	
	/**
	 * Get Image Icon
	 * 
	 * @param index
	 *            image index
	 * @return Icon
	 */
//	public static RenderedImage getIcon(int index) {
//		switch (index) {
//		case 0:
//			return Env.getRenderedImage("treeLeaf", 16);
//		case 1:
//			return Env.getRenderedImage("mWindow", 16);
//		case 2:
//			return Env.getRenderedImage("mReport", 16);
//		case 3:
//			return Env.getRenderedImage("mProcess", 16);
//		case 4:
//			return Env.getRenderedImage("mWorkFlow", 16);
//		case 5:
//			return Env.getRenderedImage("mWorkbench", 16);
//		case 6:
//			return Env.getRenderedImage("mSetVariable", 16);
//		case 7:
//			return Env.getRenderedImage("mUserChoice", 16);
//		case 8:
//			return Env.getRenderedImage("mDocAction", 16);
//		default:
//			return null;
//		}
//	} // getIcon

	/**
	 * Icon cache for Menu Tree
	 */
	private static CCache<Integer, RenderedImage> treeIconCache = new CCache<Integer, RenderedImage>(
			"MTreeNode.TreeIcons", 20);

	/**
	 * Get Image Icon
	 * 
	 * @return Icon
	 */
//	public RenderedImage getIcon() {
//
//		// Check if TreeIcon for this Node is already cached
//		if (WTreeNode.treeIconCache.containsKey(new Integer(m_node_ID))) {
//
//			// Load icon if icon is cached
//			RenderedImage icon = WTreeNode.treeIconCache.get(m_node_ID);
//
//			// If Icon was loaded, return the icon. Otherwise return a standard
//			// icon for this node type (e.g. Window, Process, etc.)
//			if (icon != null) {
//				return icon;
//			} else {
//				return getIcon(m_imageIndex);
//			}
//
//			// If TreeIcon for this Node is not already in cache, try to load
//			// and cache a icon
//		} else {
//
//			// First cache an empty icon so next time this method is called we
//			// return either a standard icon or a loaded icon but don't try to
//			// load the icon again
//			WTreeNode.treeIconCache.put(new Integer(m_node_ID), null);
//
//			// Try to load the Menu entry for this node
//			MMenu menuEntry = null;
//			if(getWTree().getTreeType().equals(MTree.TREETYPE_Menu)){
//				menuEntry = new Query(Env.getCtx(), MMenu.Table_Name,"ad_menu_id=" + m_node_ID, null).first();
//			}
//			// If Menu entry was found
//			if (menuEntry != null) {
//
//				// Try to load a Icon (if one was selected in Menu window
//				int iconID = menuEntry.getIcon();
//				MIcon m_icon = new MIcon(Env.getCtx(), iconID, null);
//				if (m_icon != null) {
//
//					// If a icon was found, try to load and cache the icon by
//					// its name from DB/Filesytem via Env.getImagIcon.
//					RenderedImage icon = null;
//					if(m_icon.getName()!=null && m_icon.getName().length()>0)
//						icon = Env.getRenderedImage(m_icon.getName(), 16);
//					
//					if (icon != null) {
//						// If an icon was found, cache and return it
//						WTreeNode.treeIconCache.put(new Integer(m_node_ID),	icon);
//						return icon;
//					}
//				}
//			}
//		}
//
//		// Fallback strategy if no icon was found at the first time. Next time
//		// this method gets called we will return this earlier
//		return getIcon(m_imageIndex);
//	} // getIcon

	/**
	 * Get Shortcut Bar info
	 * 
	 * @return true if node on bar
	 */
	public boolean isOnBar() {
		return m_onBar;
	} // isOnBar

	/**
	 * Is Process
	 * 
	 * @return true if Process
	 */
	public boolean isProcess() {
		return X_AD_Menu.ACTION_Process.equals(m_imageIndicator);
	} // isProcess

	/**
	 * Is Report
	 * 
	 * @return true if report
	 */
	public boolean isReport() {
		return X_AD_Menu.ACTION_Report.equals(m_imageIndicator);
	} // isReport

	/**
	 * Is Window
	 * 
	 * @return true if Window
	 */
	public boolean isWindow() {
		return X_AD_Menu.ACTION_Window.equals(m_imageIndicator);
	} // isWindow

	/**
	 * Is Workbench
	 * 
	 * @return true if Workbench
	 */
	public boolean isWorkbench() {
		return X_AD_Menu.ACTION_Workbench.equals(m_imageIndicator);
	} // isWorkbench

	/**
	 * Is Workflow
	 * 
	 * @return true if Workflow
	 */
	public boolean isWorkFlow() {
		return X_AD_Menu.ACTION_WorkFlow.equals(m_imageIndicator);
	} // isWorkFlow

	/**
	 * Is Form
	 * 
	 * @return true if Form
	 */
	public boolean isForm() {
		return X_AD_Menu.ACTION_Form.equals(m_imageIndicator);
	} // isForm

	/**
	 * Is Task
	 * 
	 * @return true if Task
	 */
	public boolean isTask() {
		return X_AD_Menu.ACTION_Task.equals(m_imageIndicator);
	} // isTask

	/**
	 * Get Color
	 * 
	 * @return color or black if not set
	 */
	public Color getColor() {
		if (m_color != null)
			return m_color;
		return Color.black;
	} // getColor

	/*************************************************************************/


	
	
	/**
	 * Return the Node with ID in list of children
	 * 
	 * @param ID
	 *            id
	 * @return WTreeNode with ID or null
	 */
	public WTreeNode getChildWithNodeID(int id){
		ensureChildren();
		for(WTreeNode node : children){
			if(node.getNode_ID() == id){
				return node;
			}
		}
		return null;
	}

	/*************************************************************************/

	
	public int getChildCount() {
		log.finest("");
		ensureChildren();
		return children.size();
	}

	
	public WTreeNode getChildAt(int index) {
		log.finest("");
		ensureChildren();
		return (WTreeNode) children.get(index);
	}
	
	public int getIndexOfChild(WTreeNode child){
		ensureChildren();
		return children.indexOf(child);
	}

	public void add(WTreeNode node){
		children.add(node);		
	}
	
	private void ensureChildren() {
		if (isSummary() && !childrenEnsured) {
			childrenEnsured = true;
			
			log.fine("Load children for node=" + m_node_ID + ", tree="
					+ m_tree.get_ID() + ", tablename=" + m_tree.getNodeTableName());
	
			System.out.println("Load children for node=" + m_node_ID + ", tree="
					+ m_tree.get_ID() + ", tablename=" + m_tree.getNodeTableName());
	
			
			// Try to load nodes children
			try {
				
				// Create a prepared statement which loads all child nodes for the current node 
				PreparedStatement pstmt = pstmtForLoadingChildNodes();
				
				// Execute the Query
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {

					int node_ID = rs.getInt(1);
					int parent_ID = rs.getInt(2);
					int seqNo = rs.getInt(3);
					boolean onBar = "Y".equalsIgnoreCase(rs.getString(4));
					String name = rs.getString(5);
					String description = rs.getString(6);
					boolean isSummary = "Y".equalsIgnoreCase(rs.getString(7));
					String actionColor = rs.getString(8);
					
					if (node_ID == 0 && parent_ID == 0) {
						log.info("Root Node, not Adding this one.");
					} 
					else {
						
						//Dummy node
						WTreeNode child = null;
						
						//Menu only
						if (m_tree.getTreeType().equals(MTree.TREETYPE_Menu) && !isSummary)
							{
								int AD_Window_ID = rs.getInt(9);
								int AD_Process_ID = rs.getInt(10);
								int AD_Form_ID = rs.getInt(11);
								int AD_Workflow_ID = rs.getInt(12);
								int AD_Task_ID = rs.getInt(13);
								//int AD_Workbench_ID = rs.getInt(14);
								
								//Check if node is accessable by current role
								MRole role = MRole.getDefault(Env.getCtx(), false);
								Boolean access = null;
								if (X_AD_Menu.ACTION_Window.equals(actionColor))
									access = role.getWindowAccess(AD_Window_ID);
								else if (X_AD_Menu.ACTION_Process.equals(actionColor) 
									|| X_AD_Menu.ACTION_Report.equals(actionColor))
									access = role.getProcessAccess(AD_Process_ID);
								else if (X_AD_Menu.ACTION_Form.equals(actionColor))
									access = role.getFormAccess(AD_Form_ID);
								else if (X_AD_Menu.ACTION_WorkFlow.equals(actionColor))
									access = role.getWorkflowAccess(AD_Workflow_ID);
								else if (X_AD_Menu.ACTION_Task.equals(actionColor))
									access = role.getTaskAccess(AD_Task_ID);
								if (access != null		//	rw or ro for Role 
									|| m_tree.isEditable())		//	Menu Window can see all
								{
									child = new WTreeNode (m_tree, node_ID, seqNo,
										name, description, parent_ID, isSummary,
										actionColor, onBar, null);	//	menu has no color
								}
							}
							else	//	always add
							{
								Color color = null;	//	action
								if (actionColor != null && !m_tree.getTreeType().equals(MTree.TREETYPE_Menu))
								{
									MPrintColor printColor = MPrintColor.get(Env.getCtx(), actionColor);
									if (printColor != null)
										color = printColor.getColor();
								}
								//Create new Node
								child = new WTreeNode (m_tree, node_ID, seqNo,
									name, description, parent_ID, isSummary,
									null, onBar, color);			//	no action
							}
						//Get the TreeNode for a given id/parent
						if (child != null) {
							add(child);
							child = null;
						}
					}
				}
				rs.close();
				pstmt.close();

			} catch (SQLException e) {

			}
		}
	}
	
	/**
	 * Creates a PreparedStatement which loads all child nodes for the current node
	 * depending on its tree, tree type, and node id 
	 */
	private PreparedStatement pstmtForLoadingChildNodes() throws SQLException{
		String sql = null;
		StringBuilder sqlNode = new StringBuilder();
		String nodeTable = m_tree.getNodeTableName();
		String sourceTable = "t";
		String fromClause = m_tree.getSourceTableName(false);	//	fully qualified
		String columnNameX = m_tree.getSourceTableName(true); //--> AD_Menu, C_BPartner, AD_Org...
		String color = m_tree.getActionColorName();
		
		
		//User ID, used if tree should only show user specific nodes. 
		//Currently used only in CM Container Stage, CM Container, CM Media, CM Template trees
		int ad_user_id = m_tree.isAllNodes() ? -1 : Env.getContextAsInt(Env.getCtx(), "AD_User_ID");
		
		//is base language used or do we need translations?
		boolean base = Env.isBaseLanguage(Env.getCtx(), "AD_Menu");
		
		if (m_tree.getTreeType().equals(MTree.TREETYPE_Menu))
		{
			sourceTable = "m";
			if (base){
				sqlNode.append("SELECT tn.Node_ID, tn.Parent_ID, tn.SeqNo, tb.IsActive, m.Name,m.Description, m.IsSummary, ");
				sqlNode.append("m.Action, m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Workflow_ID, m.AD_Task_ID, m.AD_Workbench_ID ");
				sqlNode.append("FROM AD_Menu m JOIN AD_TreeNodeMM tn ON m.ad_menu_id=tn.node_id ");
				sqlNode.append("LEFT OUTER JOIN AD_TreeBar tb ON (tn.AD_Tree_ID=tb.AD_Tree_ID AND tn.Node_ID=tb.Node_ID ");
				sqlNode.append((ad_user_id != -1 ? " AND tb.AD_User_ID=? ) " : ") "));
			}
			else{
				sqlNode.append("SELECT tn.Node_ID, tn.Parent_ID, tn.SeqNo, tb.IsActive, t.Name,t.Description, m.IsSummary, ");
				sqlNode.append("m.Action, m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Workflow_ID, m.AD_Task_ID, m.AD_Workbench_ID ");
				sqlNode.append("FROM AD_Menu m JOIN AD_TreeNodeMM tn ON m.ad_menu_id=tn.node_id ");
				sqlNode.append("LEFT OUTER JOIN AD_TreeBar tb ON (tn.AD_Tree_ID=tb.AD_Tree_ID AND tn.Node_ID=tb.Node_ID ");
				sqlNode.append((ad_user_id != -1 ? " AND tb.AD_User_ID=?) " : ") "));
				sqlNode.append("JOIN AD_Menu_Trl t on m.ad_menu_id = t.ad_menu_id ");
				sqlNode.append("WHERE m.AD_Menu_ID=t.AD_Menu_ID AND t.AD_Language=? ");
			}
			
			if (!m_tree.isEditable())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ").append("m.IsActive='Y' ");
			}
			//	Do not show Beta
			if (!MClient.get(Env.getCtx()).isUseBetaFunctions())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Window_ID IS NULL OR EXISTS (SELECT * FROM AD_Window w WHERE m.AD_Window_ID=w.AD_Window_ID AND w.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Process_ID IS NULL OR EXISTS (SELECT * FROM AD_Process p WHERE m.AD_Process_ID=p.AD_Process_ID AND p.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Workflow_ID IS NULL OR EXISTS (SELECT * FROM AD_Workflow wf WHERE m.AD_Workflow_ID=wf.AD_Workflow_ID AND wf.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND f.IsBetaFunctionality='N'))");
			}
			//	In R/O Menu - Show only defined Forms
			if (!m_tree.isEditable())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND ");
				if (m_tree.isClientTree())
					sqlNode.append("f.Classname");
				else
					sqlNode.append("f.JSPURL");
				sqlNode.append(" IS NOT NULL))");
			}
			
			//Show only child for current node
			boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
			sqlNode.append(hasWhere ? " AND " : " WHERE ");
			sqlNode.append(" tn.AD_Tree_ID= ? AND tn.Parent_ID = ? ");
			
			if (!m_tree.isEditable())
				sqlNode.append(" AND tn.IsActive='Y' ");
			sqlNode.append(" ORDER BY COALESCE(tn.Parent_ID, -1), tn.SeqNo ");
			
			
			
	
		}else{
			if (columnNameX == null)
				throw new IllegalArgumentException("Unknown TreeType=" + m_tree.getTreeType());
					
			sqlNode.append("SELECT tn.Node_ID, tn.Parent_ID, tn.SeqNo, tb.IsActive, t.Name,t.Description,t.IsSummary, ");
			sqlNode.append(color);
			sqlNode.append(" FROM "+fromClause+" JOIN " + nodeTable + " tn ON t."+columnNameX+"_ID = tn.Node_ID ");
			sqlNode.append(" LEFT OUTER JOIN AD_TreeBar tb ON (tn.AD_Tree_ID=tb.AD_Tree_ID AND tn.Node_ID=tb.Node_ID) ");
			if (!m_tree.isEditable())
				sqlNode.append(" WHERE t.IsActive='Y'");
			
			//Show only child for current node
			boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
			sqlNode.append(hasWhere ? " AND " : " WHERE ");
			sqlNode.append(" tn.AD_Tree_ID = ? AND tn.Parent_ID = ? ");
		}
		
			
		
		//Add the Access SQL
		sql = sqlNode.toString();
		if (!m_tree.isEditable())	//	editable = menu/etc. window
			sql = MRole.getDefault(Env.getCtx(), false).addAccessSQL(sql, 
				sourceTable, MRole.SQL_FULLYQUALIFIED, m_tree.isEditable());
		
		
		//PreparedStatement which we will return
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
				
		//Set statement parameter
		int i = 1;
		if (m_tree.getTreeType().equals(MTree.TREETYPE_Menu)){
			//Set ad_user_id if needed
			if(ad_user_id != -1)
				pstmt.setInt(i++, ad_user_id);
			
			//Set base language if needed
			if (!base)
				pstmt.setString(i++, Env.getAD_Language(Env.getCtx()));
					
			//Set ad_tree_id
			pstmt.setInt(i++, m_tree.get_ID());
			
			//Set node_id
			pstmt.setInt(i++, m_node_ID);
			
		}else{
			//Set ad_tree_id
			pstmt.setInt(i++, m_tree.get_ID());
			
			//Set node_id
			pstmt.setInt(i++, m_node_ID);			
		}
		
		return pstmt;
	}
	
	public Enumeration<?> preorderEnumeration() {
		ensureChildren();
		return Collections.enumeration(children);
	}
	    
}

