
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class Drawing extends JPanel {

	private BufferedImage img;
	BufferedReader in;
	URL data;
	static JFrame frame;
	public static int width = 1280;
	public static int height = 1280;
	String[] s;
	boolean done;
	Graphics2D g;
	static JPanel panel;

	List<EarthQuake> quakes;

	// Boise, ID 43.6150� N, 116.2023� W

	double lat = -116.2023;
	double lon = 43.6150;

	public static void main(String[] args) {

		panel = new Drawing();
		JScrollPane scrollBar = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame = new JFrame("Earthquake Map");
		// Canvas canvas = new Drawing();
		frame.setPreferredSize(new Dimension(width, height));
		frame.add(scrollBar);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public double mercX(double lat) {
		lat = lat * (Math.PI / 180);
		double x = ((width / 2) / (2 * Math.PI));
		x = x * Math.pow(2, 1);
		x = x * (lat + Math.PI);
		return x;
	}

	public double mercY(double lon) {
		lon = lon * (Math.PI / 180);
		double y = (((height / 2) / (2 * Math.PI)) * Math.pow(2, 1))
				* (Math.PI - Math.log(Math.tan(Math.PI / 4 + lon / 2)));
		return y;
	}

	public void paintComponent(Graphics g1) {
		quakes = new ArrayList<EarthQuake>();
		g = (Graphics2D) g1;
		try {
			img = ImageIO.read(new URL(
					"https://api.mapbox.com/styles/v1/mapbox/dark-v10/static/0,0,1/1280x1280?access_token=pk.eyJ1IjoibGluZXJ5ZGVyIiwiYSI6ImNrYnExZXEwZTJldDcycnBmaW9xZ3Ztd3MifQ.ifsUXW3aGQPuVkFzq5BlsA"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g.drawImage(img, 0, 0, null);

		// https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.csv
		// https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.csv

		try {
			data = new URL("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.csv");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			in = new BufferedReader(new InputStreamReader(data.openStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String inputLine;

		try {
			inputLine = in.readLine();
			while ((inputLine = in.readLine()) != null) {
				s = inputLine.split(",");

				double y = mercY(Double.parseDouble(s[1]));
				double x = mercX(Double.parseDouble(s[2]));
				EarthQuake q = new EarthQuake(s[0], s[1], s[2], s[3], s[4], s[13], g, x, y);
				quakes.add(q);

				
				JButton addBtn = new CircleButton("");
				
				
				addBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
//						JOptionPane.showMessageDialog(null, q.data, "Earthquake Details",
//								JOptionPane.INFORMATION_MESSAGE);
						final JOptionPane pane = new JOptionPane(q.data);
					    final JDialog d = pane.createDialog((JFrame)null, "Earthquake Details");
					    d.setLocation((int) x,(int) y);
					    d.setVisible(true);
					}
				});
				addBtn.setBounds((int) x, (int) y, 15, 15);
				addBtn.setBorder(new RoundBorder((int) Double.parseDouble(s[2]))); // 10 is the radius
//				addBtn.setForeground(Color.BLUE);
//				addBtn.setBackground(Color.RED);
				double size = 0;
				if (q.magnitude.length() == 0)
					q.magnitude += ".0";
				size = Double.parseDouble(q.magnitude) * 3;
				if (size > 15)
					((CircleButton) addBtn).buttonColor(new Color(255, 0, 0, 200));
				else if (size > 10 && size <= 15)
					((CircleButton) addBtn).buttonColor(new Color(255, 165, 0, 200));
				else if (size > 5 && size <= 10)
					((CircleButton) addBtn).buttonColor(new Color(255, 255, 0, 200));
				else
					((CircleButton) addBtn).buttonColor(new Color(0, 255, 0, 200));
				panel.add(addBtn);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
