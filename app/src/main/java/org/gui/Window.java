package org.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
    private JTextField thicknessField;

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

	thicknessField = new JTextField(String.valueOf((int)drawingPanel.getCurrentThickness()), 5);

	thicknessField.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		updateThickness();
	    }
	});

	thicknessField.addFocusListener(new FocusListener() {
	    @Override
	    public void focusGained(FocusEvent e) {
		thicknessField.selectAll();
	    }
	    @Override
	    public void focusLost(FocusEvent e) {
		updateThickness();
	    }
	});

	toolPanel.add(thicknessField);

	add(toolPanel, BorderLayout.NORTH);

	setVisible(true);
	
    }

    private void updateThickness() {
	try {
	    String text = thicknessField.getText().trim();
	    if(text.isEmpty()) {
		thicknessField.setText("1");
		drawingPanel.setCurrentThickness(1.0f);
		return;
	    }

	    float thickness = Float.parseFloat(text);

	    if(thickness < 0.1f) {
		thickness = 0.1f;
		thicknessField.setText("0.1");
	    }
	    else if(thickness > 100.0f) {
		thickness = 100.0f;
		thicknessField.setText("100");
	    }

	    drawingPanel.setCurrentThickness(thickness);
	}
	catch(NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this, "Please enter a valid number for thickness (0.1 - 100)", "Invalid Input", JOptionPane.ERROR_MESSAGE);
	    thicknessField.setText(String.valueOf(drawingPanel.getCurrentThickness()));

	}
    }

}

