package org.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import org.gui.DrawingPanel;
import org.enums.DrawingMode;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import java.awt.Color;
import javax.swing.JColorChooser;

public class Window extends JFrame {

    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;
    private String title;

    private DrawingPanel drawingPanel;

    public Window(String title)
    {
	super(title);
	this.WINDOW_WIDTH = 1000;
	this.WINDOW_HEIGHT = 800;
	setSize(this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);

	drawingPanel = new DrawingPanel();
	add(drawingPanel, BorderLayout.CENTER);

	JPanel toolPanel = new JPanel();
	toolPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

	JButton lineButton = new JButton("Line");
	lineButton.addActionListener(e -> drawingPanel.setDrawingMode(DrawingMode.LINE));
	toolPanel.add(lineButton);

	JButton rectButton = new JButton("Rectangle");
	rectButton.addActionListener(e -> drawingPanel.setDrawingMode(DrawingMode.RECTANGLE));
	toolPanel.add(rectButton);

	JButton ovalButton = new JButton("Oval");
	ovalButton.addActionListener(e -> drawingPanel.setDrawingMode(DrawingMode.OVAL));
	toolPanel.add(ovalButton);

	JButton freehandButton = new JButton("Freehand");
	freehandButton.addActionListener(e -> drawingPanel.setDrawingMode(DrawingMode.FREEHAND));
	toolPanel.add(freehandButton);

	JButton saveButton = new JButton("Save");
	saveButton.addActionListener(e -> {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setSelectedFile(new File("my_drawing.png"));
	    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
	    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Images", "jpg", "jpeg"));
	    fileChooser.setAcceptAllFileFilterUsed(false);

	    int userSelection = fileChooser.showSaveDialog(this);

	    if(userSelection == JFileChooser.APPROVE_OPTION) {
		File fileToSave = fileChooser.getSelectedFile();
		String filePath = fileToSave.getAbsolutePath();

		if(!filePath.toLowerCase().endsWith(".png") &&
		   !filePath.toLowerCase().endsWith(".jpg") &&
		   !filePath.toLowerCase().endsWith(".jpeg")) {
			FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
			if(selectedFilter != null && selectedFilter.getExtensions().length > 0) {
	filePath += "." + selectedFilter.getExtensions()[0];
			}
			else {
			    filePath += ".png";
			}
		   }
		   drawingPanel.saveImage(filePath);
	    }
	});

	toolPanel.add(saveButton);

	JButton colorButton = new JButton("Choose Color");
	colorButton.addActionListener(e -> {
	    Color selectedColor = JColorChooser.showDialog(this, "Choose a color", drawingPanel.getCurrentColor());
	    if(selectedColor != null) {
		drawingPanel.setCurrentColor(selectedColor);
	    }
	});
	
	toolPanel.add(colorButton);

	JLabel thicknessLabel = new JLabel("Thickness:");
	toolPanel.add(thicknessLabel);

	JSlider thicknessSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, (int)drawingPanel.getCurrentThickness());
	thicknessSlider.setMajorTickSpacing(5);
	thicknessSlider.setMinorTickSpacing(1);
	thicknessSlider.setPaintTicks(true);
	thicknessSlider.setPaintLabels(true);
	thicknessSlider.addChangeListener(new ChangeListener() {
	    @Override
	    public void stateChanged(ChangeEvent e) {
		drawingPanel.setCurrentThickness(thicknessSlider.getValue());
	    }
	});

	toolPanel.add(thicknessSlider);

	add(toolPanel, BorderLayout.NORTH);

	setVisible(true);
	
    }

}

