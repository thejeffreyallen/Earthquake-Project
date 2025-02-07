import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class EarthQuake {

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
	String[] dateTime;

	public EarthQuake(String dateTime, String lat, String lon, String depth, String mag, String loc,
			double x, double y) {
		this.dateTime = dateTime.split("T");
		this.latitude = lat;
		this.longitude = lon;
		this.depth = depth;
		this.magnitude = mag;
		this.location = loc;
		this.x = x;
		this.y = y;

		p = new Point((int) x, (int) y);
		StringBuilder sb = new StringBuilder();
		sb.append("Date: ").append(this.dateTime[0] + " ").append("\nTime: ").append(this.dateTime[1] +" ").append("\nMagnitude: ").append(this.magnitude + " ")
				.append("\nDepth: ").append(this.depth + " km").append("\nLocation: ").append(this.location);
		this.data = sb.toString();
		
	}

	public String getData() {
		return this.data;
	}

}
