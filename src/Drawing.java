
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
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
import javax.swing.JFrame;
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
	
	List<EarthQuake> quakes;
	
	// Boise, ID 43.6150° N, 116.2023° W

	double lat = -116.2023;
	double lon = 43.6150;

	public static void main(String[] args) {
		
		JPanel panel = new Drawing();
		JScrollPane scrollBar = new JScrollPane(panel,
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame = new JFrame("Earthquake Map");
		//Canvas canvas = new Drawing();
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
			data = new URL("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.csv");
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
				addMouseListener(q);
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

	public class EarthQuake implements MouseListener{

		String dateTime;
		String latitude;
		String longitude;
		String depth;
		String magnitude;
		String location;
		List<Shape> circle = new ArrayList<Shape>();
		String msg;
		double x;
		double y;
		Point p;
		Graphics2D g;
		String data;

		public EarthQuake(String dateTime, String lat, String lon, String depth, String mag, String loc, Graphics g1,
				double x, double y) {
			g = (Graphics2D) g1;
			this.dateTime = dateTime;
			this.latitude = lat;
			this.longitude = lon;
			this.depth = depth;
			this.magnitude = mag;
			this.location = loc;
			this.x = x;
			this.y = y;

			p = new Point((int) x, (int) y);

			double size = 0;
			if (mag.length() == 0)
				mag += ".0";
			size = Double.parseDouble(mag) * 3;
			if (size > 20)
				g.setColor(new Color(255, 0, 0, 255));
			else if (size > 10 && size <= 20)
				g.setColor(new Color(255, 165, 0, 200));
			else if (size > 5 && size <= 10)
				g.setColor(new Color(255, 255, 0, 200));
			else
				g.setColor(new Color(0, 255, 0, 200));
			Shape s = new Ellipse2D.Double((int) x, (int) y, (int) size, (int) size);
			g.fill(s);
			circle.add(s);
			StringBuilder sb = new StringBuilder();
			sb.append("Date / Time: ").append(this.dateTime + " ").append("Magnitude: ").append(this.magnitude + " ")
					.append("Depth: ").append(this.depth + " ").append("Location: ").append(this.location);
			this.data = sb.toString();
		}

		public String getData() {
			return this.data;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			for(Shape s : circle)
			{
				if(s.contains(e.getPoint()))
				{
					System.out.println(data);
				}
			}
		}
		
		
		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	

}
