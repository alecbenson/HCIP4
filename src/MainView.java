import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class MainView extends JFrame{
	public MainView() {
		getContentPane().setLayout(new MigLayout("", "[][][grow]", "[][grow]"));
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, "cell 0 1 3 1,grow");
		
		JTree navTree = new JTree();
		splitPane.setLeftComponent(navTree);
		
		JScrollPane mainTextPane = new JScrollPane();
		splitPane.setRightComponent(mainTextPane);
		
		JTextArea mainTextArea = new JTextArea();
		mainTextPane.setViewportView(mainTextArea);
	}

}
