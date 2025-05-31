package org.gui;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import org.enums.DrawingMode;
import org.shapes.LineShape;
import org.shapes.DrawableShape;
import org.shapes.RectShape;
import org.shapes.OvalShape;
import org.shapes.FreehandShape;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DrawingPanel extends JPanel {

    private List<DrawableShape> shapes;
    private DrawingMode currentMode;
    private Color currentColor = Color.BLACK;
    private Point currentStartPoint;
    private Point currentEndPoint;
    private DrawableShape tempShape;
    private float currentThickness = 1.0f;

    public DrawingPanel() {
	setBackground(Color.WHITE);
	shapes = new ArrayList<>();
	currentMode = DrawingMode.LINE;

	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		currentStartPoint = e.getPoint();
		currentEndPoint = e.getPoint();

		if(currentMode == DrawingMode.FREEHAND) {
		    FreehandShape fhs = new FreehandShape(currentColor, currentThickness);
		    fhs.addPoint(currentStartPoint);
		    shapes.add(fhs);
		    tempShape = fhs;
		}
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		if(currentStartPoint != null) {
		    if(currentMode != DrawingMode.FREEHAND) {
			DrawableShape newShape = null;

			switch(currentMode) {
			    case LINE:
				newShape = new LineShape(currentStartPoint, e.getPoint(), currentColor, currentThickness);
				break;
			    case RECTANGLE:
				int x = Math.min(currentStartPoint.x, e.getPoint().x);
				int y = Math.min(currentStartPoint.y, e.getPoint().y);
				int width = Math.abs(currentStartPoint.x - e.getPoint().x);
				int height = Math.abs(currentStartPoint.y - e.getPoint().y);
				newShape = new RectShape(x, y, width, height, currentColor, false, currentThickness);
				break;
			    case OVAL:
				int ox = Math.min(currentStartPoint.x, e.getPoint().x);
				int oy = Math.min(currentStartPoint.y, e.getPoint().y);
				int ow = Math.abs(currentStartPoint.x - e.getPoint().x);
				int oh = Math.abs(currentStartPoint.y - e.getPoint().y);
				newShape = new OvalShape(ox, oy, ow, oh, currentColor, false, currentThickness);
				break;

			}

			if(newShape != null) {
			    shapes.add(newShape);
			}
		    }

		    currentStartPoint = null;
		    currentEndPoint = null;
		    tempShape = null;
		    repaint();
		}
		
	    }
	});

	addMouseMotionListener(new MouseMotionAdapter() {
	    @Override
	    public void mouseDragged(MouseEvent e) {
		if(currentStartPoint != null) {
		    if(currentMode == DrawingMode.FREEHAND) {
			if(tempShape instanceof FreehandShape) {
			    ((FreehandShape) tempShape).addPoint(e.getPoint());
			}
		    }
		    else {
			currentEndPoint = e.getPoint();
		    }
		    repaint();
		}
	    }
	});
    }

    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	for(DrawableShape shape : shapes) {
	    shape.draw(g);
	}
	
	if(currentStartPoint != null && currentEndPoint != null && currentMode != DrawingMode.FREEHAND) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setColor(currentColor);
	    g2d.setStroke(new BasicStroke(currentThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{3.0f, 3.0f}, 0.0f));
	    //g.setColor(Color.GRAY);
	    DrawableShape rubberBandShape = null;

	    switch(currentMode) {
		case LINE:
		    rubberBandShape = new LineShape(currentStartPoint, currentEndPoint, Color.GRAY, currentThickness);
		    break;
		case RECTANGLE:
		    int x = Math.min(currentStartPoint.x, currentEndPoint.x);
                    int y = Math.min(currentStartPoint.y, currentEndPoint.y);
                    int width = Math.abs(currentStartPoint.x - currentEndPoint.x);
                    int height = Math.abs(currentStartPoint.y - currentEndPoint.y);
                    rubberBandShape = new RectShape(x, y, width, height, Color.GRAY, false, currentThickness);
                    break;
		case OVAL:
		    int ox = Math.min(currentStartPoint.x, currentEndPoint.x);
                    int oy = Math.min(currentStartPoint.y, currentEndPoint.y);
                    int ow = Math.abs(currentStartPoint.x - currentEndPoint.x);
                    int oh = Math.abs(currentStartPoint.y - currentEndPoint.y);
                    rubberBandShape = new OvalShape(ox, oy, ow, oh, Color.GRAY, false, currentThickness);
                    break;
	    }
	    
	    if(rubberBandShape != null) {
		rubberBandShape.draw(g2d);
	    }
	    g2d.setStroke(new BasicStroke(1));
	}
    }

    public void setDrawingMode(DrawingMode mode) {
	this.currentMode = mode;
	tempShape = null;
	currentStartPoint = null;
	currentEndPoint = null;
	repaint();
    }

    public void setCurrentColor(Color color) {
	this.currentColor = color;
    }

    public Color getCurrentColor() {
	return this.currentColor;
    }

    public void setCurrentThickness(float thickness) {
	if(thickness > 0) {
	    this.currentThickness = thickness;
	}
    }

    public float getCurrentThickness() {
	return this.currentThickness;
    }

    public void saveImage(String filePath) {
	BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = image.createGraphics();

	paintComponent(g2d);

	g2d.dispose();

	try {
	    String format = "png";
	    int dotIndex = filePath.lastIndexOf('.');
	    if(dotIndex > 0 && dotIndex < filePath.length() - 1) {
		format = filePath.substring(dotIndex + 1).toLowerCase();
	    }

	    File outputFile = new File(filePath);
	    ImageIO.write(image, format, outputFile);
	    System.out.println("Drawing saved successfully to: " + filePath);
	}
	catch(IOException ex) {
	    System.err.println("Error saving image: " + ex.getMessage());
	    ex.printStackTrace();
	}
    }
}
