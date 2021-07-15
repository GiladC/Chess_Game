import java.awt.*;
import java.io.IOException;

import javax.swing.*;

public class board {
	public static void main(String[] args) throws IOException{
		final JFrame frame = new JFrame();
		frame.setBounds(10, 10, 525, 549);
	    JPanel panel = new JPanel() {
		private static final long serialVersionUID = -4396970882832779043L;
		public void paint(Graphics g) {
			super.paint(g);
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					if ((x%2) == (y%2)) { 
						g.setColor(Color.white);
					}
					else {
						g.setColor(new Color(67, 121, 44));
					}
					g.fillRect(x*64, y*64, 64, 64);
				}
			}
		}
	};
	frame.add(panel);
	frame.setDefaultCloseOperation(3);
	frame.setVisible(true);
	}
}
