package xmlreader.controller;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import xmlreader.view.MainView;
import xmlreader.view.ToolbarView;

public class SearchController implements TreeSelectionListener, KeyListener, ChangeListener  {
	private JTextPane mainTextArea;
	private JTree navTree;
	private TreeModel navModel;
	private DefaultMutableTreeNode navRoot;
	private JTextField searchBox;
	private JSpinner findResultSpinner;
	private ArrayList<Integer> findResults;
	private int pos = 0;
	
	public SearchController(ToolbarView toolbarView){
		MainView mainView = toolbarView.getMainView();
		this.mainTextArea = mainView.getMainTextArea();
		this.navTree = mainView.getNavTree();
		this.navModel = navTree.getModel();
		this.navRoot = (DefaultMutableTreeNode) navTree.getModel().getRoot();
		this.searchBox = toolbarView.getSearchField();
		this.findResultSpinner = toolbarView.getSpinner();
		this.findResults = new ArrayList<Integer>();
	};

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) navTree.getLastSelectedPathComponent();		
		String findText = node.getUserObject().toString().toLowerCase();
		searchForText(findText);
	}
	
	public ArrayList<Integer> searchForText(String text) {
		ArrayList<Integer> findResults = new ArrayList<Integer>();
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
					findResults.add(pos);
					found = true;
				}
				pos++;
			}
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		searchBox.requestFocus();
		return findResults;
	}
	
	public void jumpToText(String searchTerm, int pos) throws BadLocationException{
		Rectangle viewRectangle = mainTextArea.modelToView(pos);
		mainTextArea.scrollRectToVisible(viewRectangle);
		mainTextArea.setCaretPosition(pos + searchTerm.length());
		mainTextArea.moveCaretPosition(pos);
		pos += searchTerm.length();
	}
	
	public void searchBoxSearch(String text) throws BadLocationException{
		findResults.clear();
		this.findResults = searchForText(text);
		
		if(findResults.size() == 0){
			searchBox.setBackground(new Color(255,120,120));
			return;
		}
		
		searchBox.setBackground(Color.white);
		int spinnerVal = (Integer)findResultSpinner.getValue();
		spinnerVal = spinnerVal > findResults.size()-1 ? findResults.size()-1 : spinnerVal;
		findResultSpinner.setValue(spinnerVal);
		System.out.println("Found " + findResults.size() + " results for the string " + text);
		
		jumpToText(text, findResults.get(spinnerVal));
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		String text = searchBox.getText();
		try {
			searchBoxSearch(text);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public void stateChanged(ChangeEvent e) {
		
		int spinnerVal = (Integer)findResultSpinner.getValue();
		
		if(spinnerVal > 0 && spinnerVal >= findResults.size() - 1){
			System.out.println("Setting spinner value to " + (findResults.size() -1));
			findResultSpinner.setValue( this.findResults.size()-1 );
			spinnerVal = findResults.size()-1;
		}
		
		if(spinnerVal < 0){
			findResultSpinner.setValue(0);
			spinnerVal = 0;
		}
		
		try {
			if(findResults.size() > 0)
				jumpToText(searchBox.getText(), findResults.get(spinnerVal));
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}