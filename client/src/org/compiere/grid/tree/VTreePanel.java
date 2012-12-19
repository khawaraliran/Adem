/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.compiere.grid.tree;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import org.adempiere.plaf.AdempierePLAF;
import org.adempiere.plaf.AdempiereTaskPaneUI;
import org.compiere.apps.ADialog;
import org.compiere.model.MRole;
import org.compiere.model.MTree;
import org.compiere.model.MTreeNode;
import org.compiere.model.X_AD_Menu;
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CMenuItem;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.swing.ShadowBorder;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import de.schaeffer.compiere.tools.DocumentSearch;

/**
 *  Tree Panel displays trees.
 *  <br>
 *	When a node is selected by Left Click, a propertyChange (NODE_SELECTION) event is fired
 *  <pre>
 *		PropertyChangeListener -
 *			treePanel.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
 *			calls: public void propertyChange(PropertyChangeEvent e)
 *  </pre>
 *  To select a specific node call
 *      setSelectedNode(NodeID);
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VTreePanel.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 *
 * @author kthiemann / Carlos Ruiz
 * 			<li>2761420 - Advanced Search
 *  
 * @author Teo Sarca
 *   		<li>BF [ 2866493 ] VTreePanel is not saving who did the node move
 *			https://sourceforge.net/tracker/?func=detail&atid=879332&aid=2866493&group_id=176962
 *
 * @author Paul Bowden
 *          <li>FR [ 2032092 ] Java 6 improvements to tree drag and drop
 *          https://sourceforge.net/tracker/index.php?func=detail&aid=2032092&group_id=176962&atid=879335
 *    
 * @author jant - Jan Thielemann - jan.thielemann@evenos.de - evenos GmbH
 * 			<li>Lazy loading http://www.adempiere.com/Lazy_loading_Trees https://adempiere.atlassian.net/browse/ADEMPIERE-171
 */
public final class VTreePanel extends CPanel
	implements ActionListener
{
/******************************************************************************
	 *  Mouse Clicked
	 */
	class VTreePanel_mouseAdapter extends java.awt.event.MouseAdapter
	{
		VTreePanel m_adaptee;

		/**
		 * 	VTreePanel_mouseAdapter
		 *	@param adaptee
		 */
		VTreePanel_mouseAdapter(VTreePanel adaptee)
		{
			m_adaptee = adaptee;
		}

		/**
		 *	Mouse Clicked
		 *	@param e
		 */
		public void mouseClicked(MouseEvent e)
		{
			m_adaptee.mouseClicked(e);
		}


	}   //  VTreePanel_mouseAdapter

	/**
	 *  Key Pressed
	 */
	class VTreePanel_keyAdapter extends java.awt.event.KeyAdapter
	{
		VTreePanel m_adaptee;

		/**
		 * 	VTreePanel_keyAdapter
		 *	@param adaptee
		 */
		VTreePanel_keyAdapter(VTreePanel adaptee)
		{
			m_adaptee = adaptee;
		}

		/**
		 * 	Key Pressed
		 *	@param e
		 */
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				m_adaptee.keyPressed(e);
		}
	}   //  VTreePanel_keyAdapter
	
	
	/**
	 *
	 */
	private static final long serialVersionUID = -6798614427038652192L;

	private static final String PREFIX_DOCUMENT_SEARCH = "/";

	protected boolean m_lookAndFeelChanged = false;

	private VTreeTransferHandler handler = new VTreeTransferHandler();

	/**
	 *  Tree Panel for browsing and editing of a tree.
	 *  Need to call initTree
	 *  @param  WindowNo	WindowNo
	 *  @param  editable    if true you can edit it
	 *  @param  hasBar      has OutlookBar
	 */
	public VTreePanel(int WindowNo, boolean hasBar, boolean editable)
	{
		super();
		toolbar = new ArrayList<JToolBar>();
		log.config("Bar=" + hasBar + ", Editable=" + editable);
		m_WindowNo = WindowNo;
		m_hasBar = hasBar;
		m_editable = editable;

		//	static init
		jbInit();
		if (!hasBar)
		{
			barScrollPane.setPreferredSize(new Dimension(0,0));
			barScrollPane.setMaximumSize(new Dimension(0,0));
			barScrollPane.setMinimumSize(new Dimension(0,0));
			//Begin - [FR 1953769]
			bar.setBackground(AdempierePLAF.getFormBackground());
			//End - [FR 1953769]
			centerSplitPane.setDividerLocation(0);
			centerSplitPane.setDividerSize(0);
			popMenuTree.remove(mBarAdd);
		}
		else {
			centerSplitPane.setDividerLocation(80);
			UIManager.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if ("lookAndFeel".equals(evt.getPropertyName()))
						m_lookAndFeelChanged = true;
				}
				
			});
		}
		//  base settings
		if (editable) {
			tree.setDragEnabled(true);
			tree.setTransferHandler(handler);
			tree.setDropMode(DropMode.INSERT);
			setMappings(tree);
		}
		else
		{
			popMenuTree.remove(mFrom);
			popMenuTree.remove(mTo);
		}
	}   //  VTreePanel

	/** Trim the Tree so all empty Folders get removed. This will need to load the whole tree which can take some time*/
	public void trimTree(){
		if(tree!=null){
			AdempiereTreeModel model = (AdempiereTreeModel)tree.getModel();
			if(model!=null){
				MTreeNode root = (MTreeNode)model.getRoot();
				if(root!=null){
					MTree vTree = root.getMTree();
					if(vTree!=null){
						vTree.trimTree();
						tree.updateUI();
					}
				}
			}			
		}
	}
	
	/**
	 *  Tree initialization.
	 * 	May be called several times
	 *	@param	AD_Tree_ID	tree to load
	 *  @return true if loaded ok
	 */
	public boolean initTree (int AD_Tree_ID)
	{
		log.config("Load Tree with AD_Tree_ID=" + AD_Tree_ID);
		
		
		//Remember AD_Tree_ID
		m_AD_Tree_ID = AD_Tree_ID;

				
		//Get MTree from AD_Tree_ID
		MTree vTree = new MTree (Env.getCtx(), AD_Tree_ID, m_editable, true, null);
		
		
		//Get Root Node from MTree
		m_root = vTree.getRoot();
		log.config("Trees root node=" + m_root);
		
		
		//Create TreeModel for root node
		treeModel = new AdempiereTreeModel(m_root, true);
				
		
		//Set JTrees TreeModel
		tree.setModel(treeModel);
		
				
		//Create the Shortcut Bar if necessary
		if (m_hasBar)
		{
			//TrimTree->load all nodes and remove empty folders (should only be used with small trees because it's time consuming and blocking the ui!). used only in main window i think
			trimTree();
			
			//Clear all Toolbars
			for (JToolBar jt : toolbar)
				jt.removeAll();
			toolbarMap = new HashMap<Integer, JToolBar>();
			
			//Get Enumeration from roots children.
			Enumeration<?> enTop = m_root.children();
			JToolBar jt = null;	
			Map<JToolBar,String> titleMap = new HashMap<JToolBar, String>();
			while (enTop.hasMoreElements())
			{
				//Get enumeration from every top level child. This way we build toolbars only for 
				//top level folder even if a node is e.g. at level 3
				MTreeNode ndTop = (MTreeNode)enTop.nextElement();
				Enumeration<?> en = ndTop.preorderEnumeration();
				boolean labelDrawn=false;
				while (en.hasMoreElements())
				{
					MTreeNode nd = (MTreeNode)en.nextElement();
					
					//Check if node is on menu bar
					if (nd.isOnBar()) {
						
						//Check if Label for top level node is already drawn. If not, draw (e.g. System Admin or Application Dictionary)
						if (!labelDrawn) {
							jt = new JToolBar(JToolBar.VERTICAL);
							titleMap.put(jt, ndTop.toString().trim());
							labelDrawn=true;
							toolbarMap.put(ndTop.getNode_ID(), jt);
						}
						
						//Add the found node to the bar
						addToBar(nd, jt, false);
					}
				}
				
				//If we found at least one node to add to the bar for the current top level child, jt != null. If so,
				//add jt to the toolbar list and set it to null for the next top level child.
				if (jt!=null)
					toolbar.add(jt);
				jt=null;
			}
			
			//Now that we have all possible toolbars loaded, add them to the toolbar panel
			for (JToolBar jt2 : toolbar) {
				jt2.setOpaque(false);
				jt2.setFloatable(false);
				jt2.setRollover(true);
				jt2.setBorder(BorderFactory.createEmptyBorder());
				
				JXTaskPane barPart = new JXTaskPane();
				//Begin - [FR 1953769]
				barPart.setUI(new AdempiereTaskPaneUI());
				barPart.getContentPane().setBackground(AdempierePLAF.getFormBackground());
				//End - [FR 1953769]
				barPart.setAnimated(true);
				barPart.setLayout(new BorderLayout());
				barPart.add(jt2, BorderLayout.NORTH);
				barPart.setTitle(titleMap.get(jt2));
				
				bar.add(barPart);
				//Begin - [FR 1953769]
				bar.setBackground(AdempierePLAF.getFormBackground());
				//End - [FR 1953769]
			}
		}
		return true;
	}   //  initTree


	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTreePanel.class);

	private BorderLayout mainLayout = new BorderLayout();
	private JTree tree = new JTree();
	private AdempiereTreeModel treeModel;
	private DefaultTreeSelectionModel treeSelect = new DefaultTreeSelectionModel();
	private CPanel southPanel = new CPanel();
	private CCheckBox treeExpand = new CCheckBox();
	private CTextField treeSearch = new CTextField(10);
	private CLabel treeSearchLabel = new CLabel();
	private JPopupMenu popMenuTree = new JPopupMenu();
	private JPopupMenu popMenuBar = new JPopupMenu();
	private CMenuItem mFrom = new CMenuItem();
	private CMenuItem mTo = new CMenuItem();
	private JXTaskPaneContainer bar = new JXTaskPaneContainer();
	private java.util.List<JToolBar> toolbar;
	private HashMap<Integer, JToolBar> toolbarMap;
	private int toolBarCols=3;
	private CMenuItem mBarAdd = new CMenuItem();
	private CMenuItem mBarRemove = new CMenuItem();
	private BorderLayout southLayout = new BorderLayout();
	private JSplitPane centerSplitPane = new JSplitPane();
	private JScrollPane treePane = new JScrollPane();
	private MouseListener mouseListener = new VTreePanel_mouseAdapter(this);
	private KeyListener keyListener = new VTreePanel_keyAdapter(this);

	//
	private int			m_WindowNo;
	/** Tree ID                     */
	private int			m_AD_Tree_ID = 0;
	/** Tree is editable (can move nodes) - also not active shown   */
	private boolean     m_editable;
	/** Tree has a shortcut Bar     */
	private boolean     m_hasBar;
	/** The root node               */
	private MTreeNode  	m_root = null;


	private String      m_search = "";
	private Enumeration<?> m_nodeEn;
	private MTreeNode   m_selectedNode;	//	the selected model node
	private CButton     m_buttonSelected;

	private JScrollPane barScrollPane;

	/**	Property Listener NodeSelected	by Left Click		*/
	public static final String NODE_SELECTION = "NodeSelected";

	/**
	 *  Static Component initialization.
	 *  <pre>
	 *  - centerSplitPane
	 *      - treePane
	 *          - tree
	 *      - bar
	 *  - southPanel
	 *  </pre>
	 */
	private void jbInit()
	{
		this.setLayout(mainLayout);
		mainLayout.setVgap(5);
		//
		//  only one node to be selected
		treeSelect.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(treeSelect);
		tree.setEditable(false);		            //	allows to change the text
		tree.addMouseListener(mouseListener);
		tree.addKeyListener(keyListener);
		tree.setCellRenderer(new VTreeCellRenderer());
		treePane.getViewport().add(tree, null);
		treePane.setBorder(new ShadowBorder());
		tree.setBorder(BorderFactory.createEmptyBorder());
		
		
		ToolTipManager.sharedInstance().registerComponent(tree);
		
		
		CPanel treePart = new CPanel();
		treePart.setLayout(new BorderLayout());
		treePart.add(treePane, BorderLayout.CENTER);
		treePart.setBorder(BorderFactory.createEmptyBorder());
		
		//
		treeExpand.setText(Msg.getMsg(Env.getCtx(), "ExpandTree"));
		treeExpand.setActionCommand("Expand");
		treeExpand.addMouseListener(mouseListener);
		treeExpand.addActionListener(this);
		//
		treeSearchLabel.setText(Msg.getMsg(Env.getCtx(), "TreeSearch") + " ");
		treeSearchLabel.setLabelFor(treeSearch);
		treeSearchLabel.setToolTipText(Msg.getMsg(Env.getCtx(), "TreeSearchText"));

		treeSearch.setBackground(AdempierePLAF.getInfoBackground());
		treeSearch.addKeyListener(keyListener);
		southPanel.setLayout(southLayout);
		southPanel.add(treeExpand, BorderLayout.WEST);
		southPanel.add(treeSearchLabel, BorderLayout.CENTER);
		southPanel.add(treeSearch, BorderLayout.EAST);
		treePart.add(southPanel, BorderLayout.SOUTH);
		//
		centerSplitPane.setOpaque(false);
		barScrollPane = new JScrollPane();
		barScrollPane.getViewport().add(bar);
		centerSplitPane.add(barScrollPane, JSplitPane.LEFT); //hengsin, jxtaskpane
		centerSplitPane.add(treePart, JSplitPane.RIGHT);
		centerSplitPane.setBorder(BorderFactory.createEmptyBorder());
		removeSplitPaneBorder();

		this.add(centerSplitPane, BorderLayout.CENTER);
		
		//
		mFrom.setText(Msg.getMsg(Env.getCtx(), "ItemMove"));
		mFrom.setActionCommand((String)TransferHandler.getCutAction().
	             getValue(Action.NAME));
		mFrom.addActionListener(this);
		mFrom.setAccelerator(
			      KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		mTo.setText(Msg.getMsg(Env.getCtx(), "ItemInsert"));
		mTo.setActionCommand((String)TransferHandler.getPasteAction().
	             getValue(Action.NAME));
		mTo.setAccelerator(
			      KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		mTo.setMnemonic(KeyEvent.VK_V);
		mTo.addActionListener(this);
		
		mBarAdd.setText(Msg.getMsg(Env.getCtx(), "BarAdd"));
		mBarAdd.setActionCommand("BarAdd");
		mBarAdd.addActionListener(this);
		mBarRemove.setText(Msg.getMsg(Env.getCtx(), "BarRemove"));
		mBarRemove.setActionCommand("BarRemove");
		mBarRemove.addActionListener(this);
		//
		popMenuTree.setLightWeightPopupEnabled(false);
		popMenuTree.add(mBarAdd);
		popMenuTree.add(mFrom);
		if(!m_hasBar){
		popMenuTree.addSeparator();
		}
		popMenuTree.add(mTo);
		popMenuBar.setLightWeightPopupEnabled(false);
		popMenuBar.add(mBarRemove);
	}   //  jbInit


	private void removeSplitPaneBorder() {
		if (centerSplitPane != null) {
			SplitPaneUI splitPaneUI = centerSplitPane.getUI();
	        if (splitPaneUI instanceof BasicSplitPaneUI) {
	            BasicSplitPaneUI basicUI = (BasicSplitPaneUI) splitPaneUI;
	            basicUI.getDivider().setBorder(BorderFactory.createEmptyBorder());
	        }
		}
	}
	
	/**
	 * 	Set Divider Location
	 *	@param location location (80 default)
	 */
	public void setDividerLocation(int location)
	{
		centerSplitPane.setDividerLocation(location);
	}	//	setDividerLocation
	
	/**
	 * 	Get Divider Location
	 *	@return divider location
	 */
	public int getDividerLocation()
	{
		return centerSplitPane.getDividerLocation();
	}	//	getDividerLocation
	

	/**
	 *  Enter Key
	 *  @param e event
	 */
	protected void keyPressed(KeyEvent e)
	{

		//CHANGED - document search
		if (e.getSource() == treeSearch 
				&& treeSearch.getText() != null
				&& treeSearch.getText().length() > 0
				&& treeSearch.getText().substring(0, 1).equals(PREFIX_DOCUMENT_SEARCH)) {
			setBusy(true);
			DocumentSearch search = new DocumentSearch();
			if (search.openDocumentsByDocumentNo(treeSearch.getText()))
				treeSearch.setText(null);
			setBusy(false);
			return;
		}

		//  *** Tree ***
		if (e.getSource() instanceof JTree
			|| (e.getSource() == treeSearch && e.getModifiers() != 0))	//	InputEvent.CTRL_MASK
		{
			TreePath tp = tree.getSelectionPath();
			if (tp == null)
				ADialog.beep();
			else
			{
				MTreeNode tn = (MTreeNode)tp.getLastPathComponent();
				setSelectedNode(tn);
			}
		}

		//  *** treeSearch ***
		else if (e.getSource() == treeSearch)
		{
				String searchText = treeSearch.getText();
				if(searchText!=null && searchText.length()>0){
					treeSearch.setEnabled(false);
					setBusy(true);
					findNodeInSearchList(searchText);
					
				}
		}   //  treeSearch
	
	}   //  keyPressed


	/*************************************************************************/

	/**
	 *  Mouse clicked
	 *  @param e event
	 */
	protected void mouseClicked(MouseEvent e)
	{
		//  *** JTree ***
		if (e.getSource() instanceof JTree)
		{
			//  Left Double Click
			if (SwingUtilities.isLeftMouseButton(e)
				&& e.getClickCount() > 0)
			{
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				if(selRow != -1)
				{
					MTreeNode tn = (MTreeNode)tree.getPathForLocation
						(e.getX(), e.getY()).getLastPathComponent();
					setSelectedNode(tn);
				}
			}

			//  Right Click for PopUp
			else if ((m_editable || m_hasBar)
				&& SwingUtilities.isRightMouseButton(e) )
			{
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				if(selRow != -1)
				{
					tree.setSelectionRow(selRow);
				}
				if (tree.getSelectionPath() != null)         //  need select first
				{
					MTreeNode nd = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
					if (nd.isLeaf())                    //  only add leaves to bar
						mBarAdd.setEnabled(true);
					else
						mBarAdd.setEnabled(false);
					Rectangle r = tree.getPathBounds(tree.getSelectionPath());
					popMenuTree.show(tree, (int)r.getMaxX(), (int)r.getY());
				}
			}
		}   //  JTree

		//  *** JButton ***
		else if (e.getSource() instanceof JButton)
		{
			if (SwingUtilities.isRightMouseButton(e))
			{
				m_buttonSelected = (CButton)e.getSource();
				popMenuBar.show(m_buttonSelected, e.getX(), e.getY());
			}
		}   //  JButton

	}   //  mouseClicked


	/**
	 *  Get currently selected node
	 *  @return MTreeNode
	 */
	public MTreeNode getSelectedNode()
	{
		return m_selectedNode;
	}   //  getSelectedNode

	/**
	 *  Search Field
	 *  @return Search Field
	 */
	public JComponent getSearchField()
	{
		return treeSearch;
	}   //  getSearchField
	
	public MTreeNode getRoot(){
		return m_root;		
	}
	
	public JTree getTree(){
		return tree;
	}

	/**
	 *  Set Selection to Node in Event
	 *  @param nodeID Node ID
	 * 	@return true if selected
	 */
	public boolean setSelectedNode (int nodeID)
	{
		log.config("ID=" + nodeID);
		if (nodeID != -1)				//	new is -1
			return selectID(nodeID, true);     //  show selection
		return false;
	}   //  setSelectedNode

	/**
	 *  Select ID in Tree
	 *  @param nodeID	Node ID
	 *  @param show	scroll to node
	 * 	@return true if selected
	 */
	private boolean selectID (int nodeID, boolean show)
	{
		if (m_root == null)
			return false;
		log.config("NodeID=" + nodeID 
			+ ", Show=" + show + ", root=" + m_root);
		//  try to find the node
		MTreeNode node = m_root.findNode (nodeID);
		if (node != null)
		{
			TreePath treePath = new TreePath(node.getPath());
			log.config("Node=" + node 
				+ ", Path=" + treePath.toString());
			tree.setSelectionPath(treePath);
			if (show)
			{
				tree.makeVisible(treePath);       	//	expand it
				tree.scrollPathToVisible(treePath);
			}
			return true;
		}
		log.info("Node not found; ID=" + nodeID);
		return false;
	}   //  selectID


	/**
	 *  Set the selected node & initiate all listeners
	 *  @param nd node
	 */
	public void setSelectedNode (MTreeNode nd)
	{
		log.config("Node = " + nd);
		m_selectedNode = nd;
		//
		firePropertyChange(NODE_SELECTION, null, nd);
	}   //  setSelectedNode

	
	/**************************************************************************
	 *  Node Changed - synchronize Node
	 *
	 *  @param  save    true the node was saved (changed/added), false if the row was deleted
	 *  @param  keyID   the ID of the row changed
	 *  @param  name	name
	 *  @param  description	description
	 *  @param  isSummary	summary node
	 *  @param  imageIndicator image indicator
	 */
	public void nodeChanged (boolean save, int keyID,
		String name, String description, boolean isSummary, String imageIndicator)
	{
		log.info("Save=" + save + ", KeyID=" + keyID
			+ ", Name=" + name + ", Description=" + description 
			+ ", IsSummary=" + isSummary + ", ImageInd=" + imageIndicator
			+ ", root=" + m_root);
		
		//if id==0, its the root node - don't update it
		if (keyID == 0)
			return;	
			
		//  try to find the node
		MTreeNode node = m_root.findNode(keyID);

		//  Node not found and saved -> new
		if (node == null && save)
		{	
			//Create node
			node = new MTreeNode (m_root.getMTree(), keyID, 0, name, description,
				m_root.getNode_ID(), isSummary, imageIndicator, false, null);
			m_root.add (node);
			
		}

		//  Node found and saved -> change
		else if (node != null && save)
		{
			//Update node		
			node.setName (name);
			node.setAllowsChildren(isSummary);
			
		}

		//  Node found and not saved -> delete
		else if (node != null && !save)
		{	
			//Remove node reference from searchList
			//removeNodeFromSearchList(node.getNode_ID());
			
			//Remove node
			MTreeNode parent = (MTreeNode)node.getParent();
			//FIXME-evenos neuer test
			treeModel.deleteNode(node);
//			node.removeFromParent();
			node = parent;  //  select Parent
		}
		
		//  Error
		else
		{
			log.log(Level.SEVERE, "Save=" + save + ", KeyID=" + keyID + ", Node=" + node);
			node = null;
		}

			
		
		//  Nothing to display
		if (node == null)
			return;

		//  (Re) Display Node
		tree.updateUI();
		TreePath treePath = new TreePath(node.getPath());
		tree.setSelectionPath(treePath);
		tree.makeVisible(treePath);       	//	expand it
		tree.scrollPathToVisible(treePath);
	}   //  nodeChanged


	/**************************************************************************
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		//  bar button pressed
		if (e.getSource() instanceof JButton)
		{
			//  Find Node - don't show
			selectID(Integer.parseInt(e.getActionCommand()), false);
			//  Select it
			MTreeNode tn = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
			setSelectedNode(tn);
		}

		//  popup menu commands
		else if (e.getSource() instanceof JMenuItem)
		{
	        String action = (String)e.getActionCommand();
	        Action a = tree.getActionMap().get(action);
	        if (a != null) {
	            a.actionPerformed(new ActionEvent(tree,
	                                              ActionEvent.ACTION_PERFORMED,
	                                              null));
	        }
			else if (action.equals("BarAdd"))
				barAdd();
			else if (action.equals("BarRemove"))
				barRemove();
		}

		else if (e.getSource() instanceof JCheckBox)
		{
			if (e.getActionCommand().equals("Expand"))
				expandTree();
		}
	}   //  actionPerformed

	/**
	 *  Add selected TreeNode to Bar
	 */
	private void barAdd()
	{
		MTreeNode nd = (MTreeNode)tree.getSelectionPath().getLastPathComponent();
		if (barDBupdate(true, nd.getNode_ID()))
			addToBar(nd, getParentToolBar(nd), false);
		else{ 
			String errorMessage = CLogger.retrieveException().getMessage();
			if (errorMessage.indexOf("ORA-00001") != -1 
				|| errorMessage.indexOf("duplicate key value violates unique constraint") != -1) 
				/* FIXME: we added error for postgresql. mysql probably needed here too */
			{
				ADialog.info(0, this, "BookmarkExist", null);
			}
			
		}
	}   //  barAdd
	
	/**
	 * Returns the top level parent JToolBar for the given MTreenode. If the parent is not on 
	 * the CPanel yet a new one is created and added.
	 * @param nd
	 * @return top level parent JToolBar for the given MTreenode
	 */
	private JToolBar getParentToolBar(MTreeNode nd){
		int topParentId = getTopParentId(nd);
		JToolBar parent = toolbarMap.get(topParentId);
		if(parent==null){
			Enumeration<?> enTop =m_root.children();
			while (enTop.hasMoreElements()) {
				MTreeNode ndTop = (MTreeNode)enTop.nextElement();
				if(ndTop.getNode_ID()==topParentId){
					log.fine("add new category: " + ndTop);
					parent = new JToolBar(JToolBar.VERTICAL);

					toolbarMap.put(ndTop.getNode_ID(), parent);
					toolbar.add(parent);
					parent.setOpaque(false);
					parent.setFloatable(false);
					parent.setRollover(true);
					parent.setBorder(BorderFactory.createEmptyBorder());

					JXTaskPane barPart = new JXTaskPane();
					//Begin - [FR 1953769]
					barPart.setUI(new AdempiereTaskPaneUI());
					barPart.getContentPane().setBackground(AdempierePLAF.getFormBackground());
					//End - [FR 1953769]
					barPart.setTitle(ndTop.toString().trim());
					barPart.setAnimated(true);
					barPart.setLayout(new BorderLayout());
					barPart.add(parent, BorderLayout.NORTH);

					bar.add(barPart);
					return parent;
				}
			}
		} else {
			log.fine("parent found: " + parent);
		}
		return parent;
	}

	/**
	 * Returns the id of the top level parent of the given MTreenode
	 * @param nd
	 * @return
	 */
	private int getTopParentId(MTreeNode nd) {
		MTreeNode parent = (MTreeNode) nd.getParent();
		if(parent!=null && parent.getNode_ID()!=0){
			return getTopParentId(parent);
		}
		return nd.getNode_ID();
	}

	/**
	 *  Add TreeNode to Bar
	 *  @param nd node
	 */
	private void addToBar(MTreeNode nd, JToolBar currentToolBar, boolean isLabel)
	{
		//	Only first word of Label
		String label = nd.toString().trim();
	//	int space = label.indexOf(' ');
	//	if (space != -1)
	//		label = label.substring(0, space);

		if (!isLabel) {
			CButton button = new CButton(label);
			button.setOpaque(false);
			button.setHorizontalAlignment(JButton.LEFT);
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setIcon(nd.getIcon());
			button.setRequestFocusEnabled(false);
			button.setToolTipText(nd.getDescription());
			button.setActionCommand(String.valueOf(nd.getNode_ID()));
			button.addActionListener(this);
			button.addMouseListener(mouseListener);
			currentToolBar.add(button);
	    } else {
	    	currentToolBar.add(new JLabel("<html><u><b>" +label+"</b></u></html>"));
	    }
		bar.validate();
		//if (centerSplitPane.getDividerLocation() == -1)
		//	centerSplitPane.setDividerLocation(button.getPreferredSize().width);
		bar.repaint();
	}   //  addToBar

	/**
	 *  Remove from Bar
	 */
	private void barRemove()
	{
		/*
		 * Sidebar Hierachie:
		 * JXTaskPaneContainer (bar)
		 * --JXTaskPane (parentPanel)
		 * ----JXCollapsiblePane
		 * ------JXCollapsiblePane$WrapperContainer
		 * --------JXPanel
		 * ----------JToolBar (parentBar)
		 */
		
		//The button in on a JToolBar
		JToolBar parentBar = (JToolBar) m_buttonSelected.getParent();
		
		//Get JXTaskPane for JToolBar (we want to remove this one from the "bar" if there are no more nodes) 
		Container parentPanel = null;
		if(parentBar!=null){
			parentPanel = parentBar.getParent().getParent().getParent().getParent();
		}
				
		//Remove the entry from the JToolBar
		for (JToolBar jt : toolbar) {
			jt.remove(m_buttonSelected);
		}

		//If we found the JXTaskPane for our toolbar and the toolbar is now empty, we want to remove the whole JXTaskPane
		if(parentPanel != null && parentBar.getComponentCount()==0){
			
			//Remove JXTaskPane from JXTaskPaneContainer
			bar.remove(parentPanel);
			
			//Remove JToolBar from the toolBarMap..
			toolbarMap.values().remove(parentBar);
			
		}
		
		//Recreate the Sidebar
		bar.validate();
		bar.repaint();
		
		//Persist the changes
		barDBupdate(false, Integer.parseInt(m_buttonSelected.getActionCommand()));
	}   //  barRemove

	/**
	 *	Make Bar add/remove persistent
	 *  @param add true if add - otherwise remove
	 *  @param Node_ID Node ID
	 *  @return true if updated
	 */
	private boolean barDBupdate (boolean add, int Node_ID)
	{
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), "#AD_Org_ID");
		int AD_User_ID = Env.getContextAsInt(Env.getCtx(), "#AD_User_ID");
		StringBuffer sql = new StringBuffer();
		if (add)
			sql.append("INSERT INTO AD_TreeBar "
				+ "(AD_Tree_ID,AD_User_ID,Node_ID, "
				+ "AD_Client_ID,AD_Org_ID, "
				+ "IsActive,Created,CreatedBy,Updated,UpdatedBy)VALUES (")
				.append(m_AD_Tree_ID).append(",").append(AD_User_ID).append(",").append(Node_ID).append(",")
				.append(AD_Client_ID).append(",").append(AD_Org_ID).append(",")
				.append("'Y',SysDate,").append(AD_User_ID).append(",SysDate,").append(AD_User_ID).append(")");
			//	if already exist, will result in ORA-00001: unique constraint (ADEMPIERE.AD_TREEBAR_KEY)
		else
			sql.append("DELETE AD_TreeBar WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
				.append(" AND AD_User_ID=").append(AD_User_ID)
				.append(" AND Node_ID=").append(Node_ID);
		int no = DB.executeUpdate(sql.toString(), false, null);
		return no == 1;
	}	//	barDBupdate


	/**
	 *  Clicked on Expand All
	 */
	private void expandTree()
	{
		if (treeExpand.isSelected())
		{
			for (int row = 0; row < tree.getRowCount(); row++)
				tree.expandRow(row);
			
		}
		else
		{
//			patch: 1654055 +jgubo Changed direction of collapsing the tree nodes
			for (int row = tree.getRowCount(); row > 0; row--)
				tree.collapseRow(row);
		}
	}   //  expandTree

	@Override
	public void paint(Graphics g) {
		if (m_lookAndFeelChanged) {
			m_lookAndFeelChanged = false;
			if (m_hasBar) removeSplitPaneBorder();
		}
		super.paint(g);
	}
	
    private void setMappings(JTree tree) { 
        ActionMap map = tree.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
    }
    

	/**
	 *	Indicate Busy
	 *  @param busy busy
	 */
	public void setBusy (boolean busy)
	{
		JFrame frame = Env.getFrame(this);
		log.config("frame: " + frame);
		if (frame == null)  //  during init
			return;
		if (busy)
		{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			treeSearch.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else
		{
			this.setCursor(Cursor.getDefaultCursor());
			frame.setCursor(Cursor.getDefaultCursor());
			treeSearch.setCursor(Cursor.getDefaultCursor());
		}
	}	//	set Busy
    
	
	
	
	
	/*************************************************************************************
	 * Searching in lazy tree
	 *************************************************************************************/
	/* TODO-evenos: Search is currently unsorted. Find a way to search the tree from top to bottom */
	
	
	/**
	 * copy of current search string to determine if search string has changed
	 * since last search
	 */
	private static String currentSearchText;

	/**
	 * List of current results so we can go through them each time we press
	 * enter (without changing the search term)
	 */
	private static List<HashMap<String, Object>> currentSearchResults;

	/** The current selected result */
	private static HashMap<String, Object> currentSelectedSearchresult;
	

	/**
	 * Search for a node in the searchList depending on text which can be either
	 * name or description of the node
	 * 
	 * @param searchText
	 */
	private void findNodeInSearchList(String searchText) {
		log.fine("Searching for \"" + searchText + "\"");

		// Check if searching a new searchterm
		if (!searchText.equals(currentSearchText)) {

			// Init search list for new search term
			initSearchList(searchText);

			// Set current search text to new search term
			currentSearchText = searchText;

		}

		// If the search has results...
		if (currentSearchResults.size() > 0) {

			// ...check if we already selected a result
			int index = currentSearchResults.indexOf(currentSelectedSearchresult);

			// Check if the currentSelectedSearchResult is in the
			// currentSearchResults, if not, set it to results 0 item
			if (index == -1 || !(index < currentSearchResults.size() - 1)) {
				index = 0;
			} else {
				index++;
			}

			// Get map to result
			currentSelectedSearchresult = currentSearchResults.get(index);

			// Search for node in tree by its map (which
			searchNodeIdInTree(currentSelectedSearchresult);

		} else {
			log.config("No entry found for \"" + searchText + "\"");
			searchDone();
		}

	}

	/**
	 * Initialize the SearchList. We use the Search List to find nodes in the
	 * tree even if they are not loaded because of the lazy loading feature.
	 */
	private void initSearchList(String searchText) {

		// Reset Search Results
		currentSearchResults = new ArrayList<HashMap<String, Object>>();

	
		// PreparedStatement & ResultSet
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			// PreparedStatement loads all nodes which are not summary and which contain the search text in their name or description
			pstmt = treeModel.pstmtForLoadingLeafWithNameOrDescription(searchText);
			
			// Execture the Query
			rs = pstmt.executeQuery();

			// While we have lines in the ResultSet...
			while (rs.next()) {
				// ...create a HashMap from the ResultSet...
				HashMap<String, Object> rsMap = new HashMap<String, Object>();
				rsMap.put("name",(rs.getString("name") == null ? "" : rs.getString("name")));
				rsMap.put("description",(rs.getString("description") == null ? "" : rs.getString("description")));
				rsMap.put("parent_id", rs.getInt("parent_id"));
				rsMap.put("node_id", rs.getInt("node_id"));

				// If Tree is a menu tree, we only search for Windows,
				// Processes, Forms, Workflows or Tasks - Summary Folder or
				// Workbench is not supported
				if (getRoot().getMTree().isMenu()) {
					MRole role = MRole.getDefault(Env.getCtx(), false);
					Boolean access = null;

					String action = rs.getString("action");
					int AD_Window_ID = rs.getInt("ad_window_id");
					int AD_Process_ID = rs.getInt("ad_process_id");
					int AD_Form_ID = rs.getInt("ad_form_id");
					int AD_Workflow_ID = rs.getInt("ad_workflow_id");
					int AD_Task_ID = rs.getInt("ad_task_id");

					if (X_AD_Menu.ACTION_Window.equals(action))
						access = role.getWindowAccess(AD_Window_ID);
					else if (X_AD_Menu.ACTION_Process.equals(action)
							|| X_AD_Menu.ACTION_Report.equals(action))
						access = role.getProcessAccess(AD_Process_ID);
					else if (X_AD_Menu.ACTION_Form.equals(action))
						access = role.getFormAccess(AD_Form_ID);
					else if (X_AD_Menu.ACTION_WorkFlow.equals(action))
						access = role.getWorkflowAccess(AD_Workflow_ID);
					else if (X_AD_Menu.ACTION_Task.equals(action))
						access = role.getTaskAccess(AD_Task_ID);
					if (access != null) {
						currentSearchResults.add(rsMap);
					}

				}
				// If not menu, always add
				else {
					// ...and put the Map into the searchList
					currentSearchResults.add(rsMap);
				}

				log.fine("add to searchList: name=" + rs.getString("name")
						+ " desc=" + rs.getString("description") + " parent="
						+ rs.getInt("parent_id") + " node_id="
						+ rs.getInt("node_id"));

			}

		} catch (Exception e) {
			log.severe("Error during initialization of Search Map");
		}finally{
			// Finally close ResultSet & PreparedStatement
			DB.close(rs, pstmt);
		}

		
	}

	private void searchNodeIdInTree(HashMap<String, Object> nodeMap) {
		// This list is used to determine the treepath
		List<Integer> nodeList = new ArrayList<Integer>();

		String nodeTableName = getRoot().getMTree().getNodeTableName();
		
		//Build sql to get the reverse path to the searched node
		StringBuilder sql = new StringBuilder();
		sql.append("with recursive children as ");
		sql.append("( ");
		sql.append("select * from ");
		sql.append(nodeTableName);
		sql.append(" where node_id = ? and ad_tree_id = ? ");
		sql.append("union all ");
		sql.append("select child.* from ");
		sql.append(nodeTableName);
		sql.append(" as child ");
		sql.append("join children as childs on (child.node_id = childs.parent_id) ");
		sql.append(") ");
		sql.append("select * from children where ad_tree_id = ? ");

		log.fine(sql.toString());

		//PreparedStatement and ResultSet for Database query
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			//Create PreparedStatement
			pstmt = DB.prepareStatement(sql.toString(), null);
			
			//Set statement parameter
			pstmt.setInt(1, (int)nodeMap.get("node_id"));
			pstmt.setInt(2, getRoot().getMTree().get_ID());
			pstmt.setInt(3, getRoot().getMTree().get_ID());
			
			//Execute the query
			rs = pstmt.executeQuery();
			
			//Add each node id to the nodeList
			while (rs.next()) {
				nodeList.add(rs.getInt(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//Close ResultSet and PreparedStatement
			DB.close(rs, pstmt);	
		}
		
		try {
			// If there is a path, try to expand it
			if (nodeList.size() > 0) {
				MTreeNode node = null;
				for (int i = nodeList.size() - 1; i >= 0; i--) {
					// Try to find the node which we want to expand/show
					node = getRoot().findNode((Integer)nodeList.get(i));

					// Get the TreePath to the node
					TreePath treePath = new TreePath(node.getPath());

					//If the node isn't the last node in the TreePath, expand it
					if(i>0)	
						getTree().expandPath(treePath);
					
					// Select the node, make it visible and scroll to the node
					// so the user sees it
					getTree().setSelectionPath(treePath);
					getTree().scrollPathToVisible(treePath);
				}
				
				// Select node only if not menu (in menu this would open e.g. a
				// window instead of only selecting it
				if (!getRoot().getMTree().isMenu())
					setSelectedNode(node);

			}
		} catch (NullPointerException e) {
			log.fine("Could not find and select node. " + e);
		} finally {
			searchDone();
		}
		log.config("Search done");
	}

	private void searchDone(){
		getSearchField().setEnabled(true);
		getSearchField().requestFocus();
		setBusy(false);
	}

}   //  VTreePanel



 	  	 
