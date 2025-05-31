package org.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class RectShape implements DrawableShape {
    int x, y, width, height;
    Color color;
    boolean filled;
    BasicStroke stroke;

    public RectShape(int x, int y, int width, int height, Color color, boolean filled, float thickness) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.color = color;
	this.filled = filled;
	this.stroke = new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    @Override
    public void draw(Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setColor(this.color);
	g2d.setStroke(this.stroke);
	if(this.filled) {
	    g2d.fillRect(this.x, this.y, this.width, this.height);
	}
	else {
	    g2d.drawRect(this.x, this.y, this.width, this.height);
	}
    }
}
