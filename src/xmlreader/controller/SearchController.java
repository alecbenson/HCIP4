package xmlreader.controller;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
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

import xmlreader.model.XMLTreeNode;
import xmlreader.view.MainView;
import xmlreader.view.ToolbarView;

public class SearchController implements TreeSelectionListener, KeyListener, ChangeListener  {
	private JTextPane mainTextArea;
	private JTree navTree;
	private JTextField searchBox;
	private JSpinner findResultSpinner;
	private ArrayList<Integer> findResults;
	private int pos = 0;
	private JLabel resultLabel;
	private MainView mainView;
	
	public SearchController(ToolbarView toolbarView){
		this.mainView = toolbarView.getMainView();
		this.resultLabel = toolbarView.getResultLabel();
		this.mainTextArea = mainView.getMainTextArea();
		this.searchBox = toolbarView.getSearchField();
		this.findResultSpinner = toolbarView.getSpinner();
		this.findResults = new ArrayList<Integer>();
	};

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		this.navTree = mainView.getNavTree();
		if(navTree == null)
			return;
		
		XMLTreeNode node = (XMLTreeNode) navTree.getLastSelectedPathComponent();
		if(node == null)
			return;
		
		String findText = node.getUserObject().toString().toLowerCase();
		int index = node.getIndex();
		System.out.println("Index is " + index);
		ArrayList<Integer> jumpPos = searchForText(findText);
		
		try {
			jumpToText(findText, jumpPos.get(index));
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	
		if(findResults.size() == 0 || text.isEmpty()){
			searchBox.setBackground(new Color(255,120,120));
			resultLabel.setText("No results found.");
			findResultSpinner.setEnabled(false);
			return;
		}
		
		resultLabel.setVisible(true);
		findResultSpinner.setEnabled(true);
		searchBox.setBackground(Color.white);
		int spinnerVal = (Integer)findResultSpinner.getValue();
		spinnerVal = spinnerVal > findResults.size()-1 ? findResults.size()-1 : spinnerVal;
		findResultSpinner.setValue(spinnerVal);
		System.out.println("Found " + findResults.size() + " results for the string " + text);
		
		jumpToText(text, findResults.get(spinnerVal));
		this.resultLabel.setText("Showing result " + (spinnerVal+1) + " of " + (findResults.size()));
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		String text = searchBox.getText();
		try {
			searchBoxSearch(text);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	
	public void stateChanged(ChangeEvent e) {
		int spinnerVal = (Integer)findResultSpinner.getValue();
		
		if(spinnerVal > 1 && spinnerVal >= findResults.size()){
			System.out.println("Setting spinner value to " + (findResults.size()));
			findResultSpinner.setValue( this.findResults.size() );
			spinnerVal = findResults.size();
		}
		
		if(spinnerVal < 1){
			findResultSpinner.setValue(1);
			spinnerVal = 1;
		}
		
		try {
			if(findResults.size() > 0)
				jumpToText(searchBox.getText(), findResults.get(spinnerVal-1));
				this.resultLabel.setText("Showing result " + (spinnerVal) + " of " + findResults.size());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
