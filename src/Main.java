import java.awt.Dimension;

import javax.swing.JFrame;

import xmlreader.view.MainView;


/**
 * @author Alec
 * The main class that kicks everything off
 */
public class Main {
	public static void main(String[] args){
      JFrame f = new MainView();
      f.setSize(new Dimension(1000,800));
      f.setMinimumSize(new Dimension(900,300));
      f.setVisible(true);
	}
}
