package xmlreader.view;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

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
	public ToolbarView(MainView mainView) {
		setLayout(new MigLayout("", "[grow][grow][grow][][grow][grow][grow]", "[]"));
		this.mainView = mainView;
		
		browseField = new JTextField();
		add(browseField, "cell 0 0,growx");
		browseField.setEditable(false);
		browseField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new FileController(this));
		add(btnBrowse, "cell 1 0");
		
		findResultSpinner = new JSpinner();
		add(findResultSpinner, "cell 3 0");
		
		searchField = new JTextField();
		add(searchField, "cell 2 0,growx");
		searchField.setColumns(10);
		
		SearchController searchController = new SearchController(this);
		searchField.addKeyListener(searchController);
		findResultSpinner.addChangeListener(searchController);
				
		JButton btnBack = new JButton("Back");
		btnBack.setHorizontalAlignment(SwingConstants.TRAILING);
		add(btnBack, "cell 5 0,alignx right");
		
		JButton btnForward = new JButton("Forward");
		add(btnForward, "cell 6 0,alignx left");
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
	
	public JTextField getBrowseField(){
		return browseField;
	}
	

}
