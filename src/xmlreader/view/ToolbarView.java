package xmlreader.view;
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
import javax.swing.event.ChangeListener;


public class ToolbarView extends JPanel {
	private JTextField browseField;
	private JTextField searchField;
	private JSpinner findResultSpinner;
	private MainView mainView;
	private JLabel searchLabel;
	private JLabel lblResult;
	
	public ToolbarView(MainView mainView) {
		setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow][grow]", "[][]"));
		this.mainView = mainView;
		
		browseField = new JTextField();
		add(browseField, "cell 0 0,growx");
		browseField.setEditable(false);
		browseField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		add(btnBrowse, "cell 1 0");
		
		String iconPath = "/xmlreader/icons/search.png";
		
		searchField = new JTextField();
		searchField.setColumns(10);
		searchField.setMargin(new Insets(0,0,0,0));
		add(searchField, "cell 2 0,growx,aligny center");
		
		lblResult = new JLabel();
		add(lblResult, "cell 2 1,alignx left");
		
		findResultSpinner = new JSpinner();
		JComponent editor = findResultSpinner.getEditor();
		JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		add(findResultSpinner, "flowx,cell 3 0,alignx left");
				
		JButton btnBack = new JButton("Back");
		btnBack.setHorizontalAlignment(SwingConstants.TRAILING);
		add(btnBack, "cell 5 0,alignx right");
		
		JButton btnForward = new JButton("Forward");
		add(btnForward, "cell 6 0,alignx left");
		searchLabel = new JLabel(new ImageIcon(getClass().getResource(iconPath)));
		add(searchLabel, "cell 3 0,alignx left");
		
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
