package xmlreader.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import xmlreader.view.MainView;


public class FileController implements ActionListener{
	private final static JFileChooser fc = new JFileChooser();
	private File xmlFile;
	private String indentChar = "\t";
	private MainView mainView;
	private String filePath;
	private JTextPane mainTextArea;
	private HTMLDocument doc;
	HTMLEditorKit editorKit;
	private JTree navTree;
	
	
	public FileController(MainView mainView){
		this.filePath = null;
		this.mainTextArea = mainView.getMainTextArea();
		this.doc = (HTMLDocument) mainTextArea.getDocument();
		this.editorKit = (HTMLEditorKit)mainTextArea.getEditorKit();
		this.navTree = mainView.getNavTree();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			mainTextArea.setText("");
			navTree.setModel(null);
			this.xmlFile = fc.getSelectedFile();
			this.filePath = xmlFile.getAbsolutePath();
			
			try {
				openFile(xmlFile);
			} catch (FileNotFoundException e1) {
				System.out.println("Could not find file " + filePath);
				//e1.printStackTrace();
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
			readFile(node,0);
			
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
				for(int i = 0; i < indentLevel; i++)
					editorKit.insertHTML(doc, doc.getLength(), indentChar, 0, 0, null );
				editorKit.insertHTML(doc, doc.getLength(), indentChar + node.getNodeValue().trim(), 0, 0, null );
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
		editorKit.insertHTML(doc, doc.getLength(), "\n", 0, 0, null );
		for(int i = 0; i < indentLevel; i++)
			editorKit.insertHTML(doc, doc.getLength(), indentChar, 0, 0, null );
		editorKit.insertHTML(doc, doc.getLength(), "<html><b>" + node.getNodeName().toUpperCase() + "</b></html>", 0, 0, null );
	}
	
	

}
