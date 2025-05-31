package org.shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.BasicStroke;



public class FreehandShape implements DrawableShape {
    private List<Point> points;
    private Color color;
    private BasicStroke stroke;

    public FreehandShape(Color color, float thickness) {
	this.points = new ArrayList<>();
	this.color = color;
	this.stroke = new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    public void addPoint(Point p) {
	this.points.add(p);
    }

    @Override
    public void draw(Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setColor(this.color);
	g2d.setStroke(this.stroke);
	if(points.size() > 1) {
	    for(int i = 0; i < points.size() - 1; i++) {
		Point p1 = points.get(i);
		Point p2 = points.get(i + 1);
		g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
	    }
	}
    }
}
