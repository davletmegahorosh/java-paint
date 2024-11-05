package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class ToolPanel extends JPanel {
    private final DrawingPanel drawingPanel;
    private final JFileChooser fileChooser;

    public ToolPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        fileChooser = new JFileChooser();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(createFileOperationsPanel());
        add(Box.createVerticalStrut(20));
        add(createColorPickerPanel());
    }

    private JPanel createFileOperationsPanel() {
        JPanel filePanel = new JPanel(new GridLayout(3, 1, 5, 5));

        JButton newFileButton = new JButton("New File");
        newFileButton.addActionListener(e -> drawingPanel.clearCanvas());
        filePanel.add(newFileButton);

        JButton openFileButton = new JButton("Open");
        openFileButton.addActionListener(e -> openFile());
        filePanel.add(openFileButton);

        JButton saveFileButton = new JButton("Save");
        saveFileButton.addActionListener(e -> saveFile());
        filePanel.add(saveFileButton);

        return filePanel;
    }

    private JPanel createColorPickerPanel() {
        JPanel colorPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        colorPanel.setBorder(BorderFactory.createTitledBorder("Colors"));

        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE};

        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            colorButton.addActionListener(e -> drawingPanel.setCurrentColor(color));
            colorPanel.add(colorButton);
        }

        return colorPanel;
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            drawingPanel.openImage(selectedFile);
        }
    }

    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(drawingPanel.getCanvasImage(), "jpg", fileToSave);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
    }
}
