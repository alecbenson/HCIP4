package xmlreader.view;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import xmlreader.controller.SearchController;
import xmlreader.controller.HighlightCaret;


public class MainView extends JFrame{
	private JTextPane mainTextArea;
	private JTree navTree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;

	public MainView() {
		getContentPane().setLayout(new MigLayout("", "[grow][grow]", "[][grow]"));
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, "cell 0 1 3 1,grow");
		
		JScrollPane mainTextPane = new JScrollPane();
		splitPane.setRightComponent(mainTextPane);
		
		this.mainTextArea = new JTextPane();
		mainTextArea.setContentType( "text/html" );
		mainTextArea.setEditable(false);
		mainTextArea.setCaret(new HighlightCaret());
		mainTextPane.setViewportView(mainTextArea);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		this.navTree = new JTree();
		this.treeModel = (DefaultTreeModel) navTree.getModel();
		this.root = (DefaultMutableTreeNode) treeModel.getRoot();
		scrollPane.setViewportView(navTree);
		
		JPanel toolbar = new ToolbarView(this);
		navTree.addTreeSelectionListener(new SearchController((ToolbarView)toolbar) );
		getContentPane().add(toolbar, "cell 0 0 2 1,grow");
	}
	
	public JTextPane getMainTextArea(){
		return this.mainTextArea;
	}
	
	public JTree getNavTree(){
		return this.navTree;
	}
	
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public DefaultMutableTreeNode getRoot() {
		return root;
	}

	public void setRoot(DefaultMutableTreeNode root) {
		this.root = root;
	}

}
