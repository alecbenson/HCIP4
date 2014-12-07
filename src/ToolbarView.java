import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;


public class ToolbarView extends JPanel {
	private JTextField browseField;
	private JTextField searchField;
	public ToolbarView() {
		setLayout(new MigLayout("", "[grow][grow][grow][grow][grow][grow]", "[]"));
		
		browseField = new JTextField();
		add(browseField, "cell 0 0,growx");
		browseField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		add(btnBrowse, "cell 1 0");
		
		searchField = new JTextField();
		add(searchField, "cell 2 0,growx");
		searchField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		add(btnSearch, "cell 3 0");
		
		JButton btnBack = new JButton("Back");
		btnBack.setHorizontalAlignment(SwingConstants.TRAILING);
		add(btnBack, "cell 4 0,alignx right");
		
		JButton btnForward = new JButton("Forward");
		add(btnForward, "cell 5 0,alignx left");
	}
	

}
