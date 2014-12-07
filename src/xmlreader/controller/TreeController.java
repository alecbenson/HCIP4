package xmlreader.controller;

import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;

import xmlreader.view.MainView;

public class TreeController implements TreeSelectionListener{
	private JTextPane mainTextArea;
	private JTree navTree;
	private int pos = 0;
	
	public TreeController(MainView mainView){
		this.mainTextArea = mainView.getMainTextArea();
		this.navTree = mainView.getNavTree();
	};

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) navTree.getLastSelectedPathComponent();		
		jumpToSelection(node);
	}
	
	public void jumpToSelection(DefaultMutableTreeNode node) {
		String findText = node.getUserObject().toString().toLowerCase();
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
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
