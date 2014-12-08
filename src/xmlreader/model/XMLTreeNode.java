package xmlreader.model;

import javax.swing.tree.DefaultMutableTreeNode;
import org.w3c.dom.Node;

public class XMLTreeNode extends DefaultMutableTreeNode{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8289807993786806836L;
	private Node node;
	private int index;

	public XMLTreeNode(Node node, int index){
		this.node = node;
		this.index = index;
	}
	
	@Override
	public String toString(){
		return node.getNodeName();
	}
	
	public int getIndex(){
		return index;
	}
	
	@Override
	public Object getUserObject(){
		return node.getNodeName();
	}
}
