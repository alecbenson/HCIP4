import java.awt.Dimension;

import javax.swing.JFrame;

import xmlreader.view.MainView;


public class Main {
	public static void main(String[] args){
      JFrame f = new MainView();
      f.setSize(new Dimension(800,600));
      f.setMinimumSize(new Dimension(800,600));
      f.setVisible(true);
	}
}
