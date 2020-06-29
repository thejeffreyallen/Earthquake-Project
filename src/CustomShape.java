import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class CustomShape extends JComponent{
	
	private boolean mouseOver = false;
	private boolean mousePressed = false;
	int x;
	int y;
	int width, height;
	Color col = new Color(255 ,255 ,255 ,255);
	JOptionPane pane = null;
	JDialog d = null;
	Graphics g2;
	Color temp;
	
	CustomShape(int x, int y, int diameter, EarthQuake q){
		this.x = x;
		this.y = y;
		this.width = diameter;
		this.height = diameter;
		
		MouseAdapter mouseListener = new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				pane = new JOptionPane(q.data, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
			    d = new JDialog();
			    d.setTitle("Earthquake Details");
			    d.setModal(false);

			    d.setContentPane(pane);

			    d.setLocation((int) x + 50,(int) y);
			    d.pack();
			    d.setVisible(true);
			    temp = col;
			    setColor(Color.WHITE);
				
			}
			
			 public void mouseExited(MouseEvent e) {
				 setColor(temp);
				 d.dispose();
			    }
		};
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}
	
	public void setColor(Color col) {
		this.col = col;
		this.setBackground(col);
	}
	
	public boolean contains(int x, int y) {
		int radius = width / 2;
		return Point2D.distance(x, y, width / 2, width / 2) < radius;
	}
	
	public void paintComponent(Graphics g) {
		g2 = g;
		int diameter = width;
		int radius = diameter / 2;
		g.setColor(col);
		g.fillOval(diameter / 2 - radius, diameter / 2 - radius, diameter, diameter);
		g.setFont(getFont());
		
		FontMetrics metrics = g.getFontMetrics(getFont());
		int stringHeight = metrics.getHeight();
	}
	
	
	
}
