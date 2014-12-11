package xmlreader.view;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import xmlreader.controller.FileController;
import xmlreader.controller.SearchController;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;


/**
 * @author Alec
 * This class is responsible for displaying all of the contents of the toolba
 */
public class ToolbarView extends JPanel {
	private JTextField browseField;
	private JTextField searchField;
	private JSpinner findResultSpinner;
	private MainView mainView;
	private JLabel searchLabel;
	private JLabel lblResult;
	private JLabel lblView;
	private JCheckBox chckbxTopic;
	private JCheckBox chckbxSummary;
	private JCheckBox chckbxDetails;
	private JSeparator separator;
	private JSeparator separator_1;
	private JLabel lblInstruction;
	private JLabel labelSearch;
	
	/**
	 * This view is responsible for displaying the contents of the toolbar
	 * @param mainView
	 */
	public ToolbarView(MainView mainView) {
		setLayout(new MigLayout("", "[grow][grow][][grow][][grow][][grow][grow][grow]", "[][][]"));
		this.mainView = mainView;
		String searchIconPath = "/xmlreader/icons/search.png";
		String browseIconPath = "/xmlreader/icons/browse.png";
		
		lblInstruction = new JLabel("Open a file:");
		lblInstruction.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblInstruction, "cell 0 0");
		
		labelSearch = new JLabel("Search this file:");
		labelSearch.setHorizontalAlignment(SwingConstants.CENTER);
		add(labelSearch, "cell 3 0");
		
		lblView = new JLabel("(non-functioning) Show these sections:");
		lblView.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblView, "cell 8 0,alignx left,aligny top");
		
		browseField = new JTextField();
		add(browseField, "cell 0 1,growx");
		browseField.setEditable(false);
		browseField.setColumns(10);
		browseField.setMinimumSize(new Dimension(200,20));
		
		JButton btnBrowse = new JButton("Open", new ImageIcon(getClass().getResource(browseIconPath)));
		btnBrowse.setMargin(new Insets(0,0,0,0));
		add(btnBrowse, "cell 1 1,alignx left");
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		add(separator_1, "cell 2 1,alignx center,growy");
		
		searchField = new JTextField();
		searchField.setHorizontalAlignment(SwingConstants.LEFT);
		searchField.setColumns(10);
		searchField.setMargin(new Insets(0,0,0,0));
		searchField.setPreferredSize(new Dimension(200,20));
		searchField.setEnabled(false);
		add(searchField, "flowx,cell 3 1,aligny center");
		
		findResultSpinner = new JSpinner();
		findResultSpinner.setValue(1);
		findResultSpinner.setEnabled(false);
		JComponent editor = findResultSpinner.getEditor();
		JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		add(findResultSpinner, "cell 3 1,alignx leading");
		
		searchLabel = new JLabel(new ImageIcon(getClass().getResource(searchIconPath)));
		add(searchLabel, "cell 3 1,alignx leading");
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		add(separator, "cell 6 1,alignx center,growy");
		chckbxTopic = new JCheckBox("Topic");
		chckbxTopic.setLayout(new MigLayout("insets 0", "", ""));
		add(chckbxTopic, "cell 7 1,alignx center");
		chckbxSummary = new JCheckBox("Summary");
		chckbxSummary.setLayout(new MigLayout("insets 0", "", ""));
		add(chckbxSummary, "cell 8 1,alignx center");
		chckbxDetails = new JCheckBox("Details");
		chckbxDetails.setLayout(new MigLayout("insets 0", "", ""));
		add(chckbxDetails, "cell 9 1,alignx center");
		
		lblResult = new JLabel();
		add(lblResult, "cell 3 2 3 1,alignx left");
		
		SearchController searchController = new SearchController(this);
		searchField.addKeyListener(searchController);
		findResultSpinner.addChangeListener(searchController);
		btnBrowse.addActionListener(new FileController(this));
		
	}
	
	/**
	 * @return the searchfield component
	 */
	public JTextField getSearchField(){
		return searchField;
	}
	
	/**
	 * @return the searchfield's result spinner
	 */
	public JSpinner getSpinner(){
		return findResultSpinner;
	}
	
	
	/**
	 * @return the mainview that contains all of the parsed XML
	 */
	public MainView getMainView(){
		return mainView;
	}
	
	/**
	 * @return the label that tells users how many results have been found
	 */
	public JLabel getResultLabel(){
		return lblResult;
	}
	
	/**
	 * @return the text field that tells users what file they have opened.
	 */
	public JTextField getBrowseField(){
		return browseField;
	}
	

}
