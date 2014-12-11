package xmlreader.view;
import java.awt.Insets;
import java.util.Hashtable;

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
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.JCheckBox;


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
	
	public ToolbarView(MainView mainView) {
		setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][grow]", "[][][][][]"));
		this.mainView = mainView;
		
		browseField = new JTextField();
		add(browseField, "cell 0 1,growx");
		browseField.setEditable(false);
		browseField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		add(btnBrowse, "cell 1 1");
		
		String iconPath = "/xmlreader/icons/search.png";
		
		searchField = new JTextField();
		searchField.setColumns(10);
		searchField.setMargin(new Insets(0,0,0,0));
		searchField.setText("Search this document");
		searchField.setEnabled(false);
		add(searchField, "cell 2 1,growx,aligny center");;
		
		lblView = new JLabel("Show");
		lblView.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblView, "cell 4 0,alignx left,aligny top");
		chckbxTopic = new JCheckBox("Topic");
		chckbxTopic.setLayout(new MigLayout("insets 0", "", ""));
		add(chckbxTopic, "cell 4 1");
		chckbxSummary = new JCheckBox("Summary");
		chckbxSummary.setLayout(new MigLayout("insets 0", "", ""));
		add(chckbxSummary, "cell 4 2");
		chckbxDetails = new JCheckBox("Details");
		chckbxDetails.setLayout(new MigLayout("insets 0", "", ""));
		add(chckbxDetails, "cell 4 3");
		
		lblResult = new JLabel();
		add(lblResult, "cell 2 4,alignx left");
		
		findResultSpinner = new JSpinner();
		findResultSpinner.setValue(1);
		findResultSpinner.setEnabled(false);
		JComponent editor = findResultSpinner.getEditor();
		JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		add(findResultSpinner, "flowx,cell 3 1,alignx left");
		searchLabel = new JLabel(new ImageIcon(getClass().getResource(iconPath)));
		add(searchLabel, "cell 3 1,alignx left");
		
		SearchController searchController = new SearchController(this);
		btnBrowse.addActionListener(new FileController(this));
		
		findResultSpinner.addChangeListener(searchController);
		searchField.addKeyListener(searchController);
	}
	
	public JTextField getSearchField(){
		return searchField;
	}
	
	public JSpinner getSpinner(){
		return findResultSpinner;
	}
	
	
	public MainView getMainView(){
		return mainView;
	}
	
	public JLabel getResultLabel(){
		return lblResult;
	}
	
	public JTextField getBrowseField(){
		return browseField;
	}
	

}
