package org.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class LineShape implements DrawableShape {
    int x1, y1, x2, y2;
    Color color;
    BasicStroke stroke;

    public LineShape(Point p1, Point p2, Color color, float thickness) {
	this.x1 = p1.x;
	this.y1 = p1.y;
	this.x2 = p2.x;
	this.y2 = p2.y;
	this.color = color;
	this.stroke = new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    @Override
    public void draw(Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setColor(this.color);
	g2d.setStroke(this.stroke);
	g2d.drawLine(this.x1, this.y1, this.x2, this.y2);
    }
}
