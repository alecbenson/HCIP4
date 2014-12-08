package xmlreader.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import xmlreader.model.XMLTreeNode;
import xmlreader.view.MainView;
import xmlreader.view.ToolbarView;


public class FileController implements ActionListener{
	private final static JFileChooser fc = new JFileChooser();
	private File xmlFile;
	private MainView mainView;
	private String filePath;
	private JTextPane mainTextArea;
	private ToolbarView toolbarView;
	private HTMLDocument doc;
	private static int index;
	HTMLEditorKit editorKit;
	
	private JTree navTree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode treeRoot;
	private ArrayList<DefaultMutableTreeNode> treeStructureList;
	
	
	public FileController(ToolbarView toolbarView){
		
		this.mainView = toolbarView.getMainView();
		this.toolbarView = toolbarView;
		this.filePath = null;
		this.mainTextArea = mainView.getMainTextArea();
		this.doc = (HTMLDocument) mainTextArea.getDocument();
		this.editorKit = (HTMLEditorKit)mainTextArea.getEditorKit();
		
		this.navTree = mainView.getNavTree();
		this.treeModel = mainView.getTreeModel();
		this.treeRoot = mainView.getRoot();
		this.treeStructureList = new ArrayList<DefaultMutableTreeNode>();
		treeStructureList.add(treeRoot);
		
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
			Node node = document.getDocumentElement();
			editorKit.insertHTML(doc, 0, "<style> .nobullet{list-style-type: none;} </style><ul>", 0, 0, null );
			readFile(node,0);
			editorKit.insertHTML(doc, doc.getLength(), "</ul>", 0, 0, null );
			
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
			formatValue(child, indentLevel);
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
				editorKit.insertHTML(doc,doc.getLength(), "<li class='nobullet' style='margin-left:" + indentLevel*20 + "px'>"
					+ node.getNodeValue().trim()
					+ "</li>", 0, 0, null );
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
		editorKit.insertHTML(doc, doc.getLength(), "<li class='nobullet' style='margin-left:" + indentLevel*20 + "px'><b>"
			+ node.getNodeName().toUpperCase()
			+ "</b></li>", 0, 0, null );
		addElementToTree(node);
	}
	
	public void addElementToTree(Node node){
		XMLTreeNode parentNode = (XMLTreeNode) getTreeNode(node.getParentNode() );
		XMLTreeNode nodeToAdd = new XMLTreeNode(node, index++);
		
		//If the node has a parent node in the tree
		if(parentNode != null){
			treeModel.insertNodeInto( nodeToAdd, parentNode, parentNode.getChildCount() );
			treeStructureList.add(nodeToAdd);
		} else {
			treeStructureList.add(nodeToAdd);
			treeModel.insertNodeInto( nodeToAdd, treeRoot, treeRoot.getChildCount() );
		}
	}
	
	public XMLTreeNode getTreeNode(Node node){
		XMLTreeNode lastNode = null;
		for( DefaultMutableTreeNode treeNode : treeStructureList){
			if( treeNode.getUserObject().toString().equals(node.getNodeName()) )
				lastNode = (XMLTreeNode) treeNode;
		}
		return lastNode;
	}
	
	

}
