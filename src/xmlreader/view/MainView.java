package xmlreader.view;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import javax.swing.JEditorPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;


public class MainView extends JFrame{
	private JTextPane mainTextArea;
	private JTree navTree;
	
	public MainView() {
		getContentPane().setLayout(new MigLayout("", "[grow][grow]", "[][grow]"));
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, "cell 0 1 3 1,grow");
		
		this.navTree = new JTree();
		splitPane.setLeftComponent(navTree);
		
		JScrollPane mainTextPane = new JScrollPane();
		splitPane.setRightComponent(mainTextPane);
		
		this.mainTextArea = new JTextPane();
		mainTextArea.setContentType( "text/html" );
		mainTextPane.setViewportView(mainTextArea);
		
		JPanel toolbar = new ToolbarView(this);
		getContentPane().add(toolbar, "cell 0 0 2 1,grow");
	}
	
	public JTextPane getMainTextArea(){
		return this.mainTextArea;
	}
	
	public JTree getNavTree(){
		return this.navTree;
	}

}
