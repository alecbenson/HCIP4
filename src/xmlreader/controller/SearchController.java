package xmlreader.controller;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import xmlreader.view.MainView;
import xmlreader.view.ToolbarView;

public class SearchController implements TreeSelectionListener, ActionListener{
	private JTextPane mainTextArea;
	private JTree navTree;
	private TreeModel navModel;
	private DefaultMutableTreeNode navRoot;
	private JTextField searchBox;
	private int pos = 0;
	
	public SearchController(ToolbarView toolbarView){
		MainView mainView = toolbarView.getMainView();
		this.mainTextArea = mainView.getMainTextArea();
		this.navTree = mainView.getNavTree();
		this.navModel = navTree.getModel();
		this.navRoot = (DefaultMutableTreeNode) navTree.getModel().getRoot();
		this.searchBox = toolbarView.getSearchField();
	};

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) navTree.getLastSelectedPathComponent();		
		String findText = node.getUserObject().toString().toLowerCase();
		jumpToSelection(findText);
	}
	
	public void jumpToSelection(String text) {
		mainTextArea.requestFocus();
		String findText = text.toLowerCase();
		int findLength = findText.length();
		Document document = mainTextArea.getDocument();
		
		try{
			boolean found = false;
			if(pos + findLength > document.getLength());
				pos = 0;
		
			while ( pos + findLength <= document.getLength() ){
				String match = document.getText(pos, findLength).toLowerCase();
				if(match.equals(findText)){
					found = true;
					break;
				}
				pos++;
			}
			
			if(found) {
				Rectangle viewRectangle = mainTextArea.modelToView(pos);
				mainTextArea.scrollRectToVisible(viewRectangle);
				mainTextArea.setCaretPosition(pos + findLength);
				mainTextArea.moveCaretPosition(pos);
				pos += findLength;
			}
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = searchBox.getText();
		jumpToSelection(text);
	}

}
