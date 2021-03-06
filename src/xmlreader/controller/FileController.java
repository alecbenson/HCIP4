package xmlreader.controller;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import xmlreader.model.XMLTreeNode;
import xmlreader.view.MainView;
import xmlreader.view.ToolbarView;


/**
 * @author Alec
 * This file is responsible for parsing the XML file and adding the elements to the mainview and JTree
 */
public class FileController implements ActionListener{
	private final static JFileChooser fc = new JFileChooser();
	private File xmlFile;
	private MainView mainView;
	private String filePath;
	private JTextPane mainTextArea;
	private ToolbarView toolbarView;
	private HTMLDocument doc;
	HTMLEditorKit editorKit;
	
	private JTree navTree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode treeRoot;
	private ArrayList<DefaultMutableTreeNode> treeStructureList;
	private Element rootNode;
	
	
	public FileController(ToolbarView toolbarView){
		
		this.mainView = toolbarView.getMainView();
		this.toolbarView = toolbarView;
		this.filePath = null;
		this.mainTextArea = mainView.getMainTextArea();
		this.doc = (HTMLDocument) mainTextArea.getDocument();
		this.editorKit = (HTMLEditorKit)mainTextArea.getEditorKit();
		
		this.navTree = mainView.getNavTree();
		this.navTree.setModel(null);
		this.treeStructureList = new ArrayList<DefaultMutableTreeNode>();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			mainTextArea.setText("");
			this.xmlFile = fc.getSelectedFile();
			this.filePath = xmlFile.getAbsolutePath();
			toolbarView.getBrowseField().setText(filePath);
			toolbarView.getSearchField().setEnabled(true);
			
			try {
				openFile(xmlFile);
			} catch (FileNotFoundException e1) {
				System.out.println("Could not find file " + filePath);
			}
		}
		
	}
	
	/**
	 * Opens the XML file and triggers the parsing of it
	 * @throws FileNotFoundException
	 */
	public void openFile(File xml) throws FileNotFoundException{
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(xml);
			document.getDocumentElement().normalize();
			this.rootNode = document.getDocumentElement();
			
			this.navTree.setModel(new DefaultTreeModel(new XMLTreeNode(rootNode)));
			this.treeModel = (DefaultTreeModel) navTree.getModel();
			this.treeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
			
			readFile(rootNode,0);
			
			//Close body html
			editorKit.insertHTML(doc, doc.getLength(), "</body>", 0, 0, null );
			//Expand first item in tree
			navTree.expandRow(0);
			//Scroll to the top of the file
			Rectangle topRect = mainTextArea.modelToView(0);
			mainTextArea.scrollRectToVisible(topRect);
			
		} catch(Exception e){
			e.printStackTrace();
			throw new FileNotFoundException();
		}
	}
	
	/**
	 * recursively iterates through the XML file and parses it
	 * @param node - the node to start at
	 * @param indentLevel - the indentation level to start at
	 * @throws BadLocationException 
	 * @throws IOException 
	 */
	public void readFile(Node node, int indentLevel) throws BadLocationException, IOException{
		formatName(node, indentLevel);
		NodeList nodeList = node.getChildNodes();

		for(int index = 0; index < nodeList.getLength(); index++){
			Node child = nodeList.item(index);
			formatValue(child, indentLevel+1);
			readFile(child, indentLevel+1);
		}
	}
	
	
	/**
	 * Format the value of the node so that it is readable
	 * @param node - the node to format
	 * @param indentLevel - an integer that represents how much the text should be indented
	 * @throws BadLocationException 
	 * @throws IOException 
	 */
	public void formatValue(Node node, int indentLevel) throws BadLocationException, IOException{
		if(node instanceof Text)
			if(!node.getNodeValue().trim().isEmpty()){
				String formatted = node.getNodeValue().replace("\n", "<br>");
				editorKit.insertHTML(doc,doc.getLength(), "<p style='margin-left:" + indentLevel*20 + "px'>"
					+ formatted
					+ "</p>", 0, 0, null );
			}
	}
	
	
	/**
	 * Formats the title of the node so that it is readable
	 * @param node - the node to format
	 * @param indentLevel - an integer that represents how much the text should be indented
	 * @throws BadLocationException 
	 * @throws IOException 
	 */
	public void formatName(Node node, int indentLevel) throws IOException, BadLocationException{
		if(node instanceof Text)
			return;
		
		//If this node is the main root, then add it as an H1
		if(node.equals(rootNode)){
			editorKit.insertHTML(doc, doc.getLength(), "<h1 style='margin-left:" + indentLevel*20 + "px'><b>"
					+ node.getNodeName().toUpperCase()
					+ "</b></h1>", 0, 0, null );
				addElementToTree(node);
			return;
		}
		
		//If this is a main title, add a page break and a separator, and add the node as an H2
		if(node.getParentNode().equals(rootNode)){
			editorKit.insertHTML(doc, doc.getLength(), "<br><hr>", 0, 0, null);
		
			editorKit.insertHTML(doc, doc.getLength(), "<h2 style='margin-left:" + indentLevel*20 + "px'><b>"
				+ node.getNodeName().toUpperCase()
				+ "</b></h2>", 0, 0, null );
			addElementToTree(node);
			return;
		} 
		
		//Otherwise, this is just a  normal title element and we should add it as a normal paragraph header
		editorKit.insertHTML(doc, doc.getLength(), "<p style='margin-left:" + indentLevel*20 + "px'><i><b>"
				+ node.getNodeName().toUpperCase()
				+ "</b></i></p>", 0, 0, null );
			addElementToTree(node);
	}
	
	/**
	 * Adds an element to the JTree on the right
	 * @param node - the XML node to add
	 */
	public void addElementToTree(Node node){
		XMLTreeNode parentNode = (XMLTreeNode) getTreeNode(node.getParentNode() );
		XMLTreeNode nodeToAdd = new XMLTreeNode(node, getNodeIndex(node));
		
		if(node.equals(rootNode))
			return;
		
		//If the node has a parent node in the tree
		if(parentNode != null){
			treeModel.insertNodeInto( nodeToAdd, parentNode, parentNode.getChildCount() );
			treeStructureList.add(nodeToAdd);
		} else {
			treeStructureList.add(nodeToAdd);
			treeModel.insertNodeInto( nodeToAdd, treeRoot, treeRoot.getChildCount() );
		}
	}
	
	/**
	 * Get the index of the node within the tree. This is used to identify where in the page we should
	 * jump to when the user clicks on the item in the tree
	 * @param node - the node to get the index of
	 * @return
	 */
	public int getNodeIndex(Node node){
		int index = 0;
		Enumeration e = treeRoot.depthFirstEnumeration();
		while(e.hasMoreElements()){
			if( ((DefaultMutableTreeNode)e.nextElement()).toString() == node.getNodeName() )
				index++;
		}
		return index;
	}
	
	
	/**
	 * Given an XML node, find the corresponding element within the Jtree
	 * @param node - the XML node to get the tree element of
	 * @return null if no match, XMLTreeNode otherwise
	 */
	public XMLTreeNode getTreeNode(Node node){
		XMLTreeNode lastNode = null;
		if(treeStructureList.isEmpty())
			return null;
		
		for( DefaultMutableTreeNode treeNode : treeStructureList){
			if( treeNode.getUserObject().toString().equals(node.getNodeName()) )
				lastNode = (XMLTreeNode) treeNode;
		}
		return lastNode;
	}
	
	

}
