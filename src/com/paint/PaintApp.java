package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PaintApp {
    private JFrame jFrame;
    private JPanel mainPanel;
    private DrawingPanel drawingPanel;
    private ToolPanel toolPanel;
    private File currentFile;

    public BufferedImage getCanvasImage() {
        return drawingPanel.getCanvasImage();
    }

    public PaintApp() {
        setupMainFrame();
        setupDrawingPanel();
        setupToolPanel();
        setupLayout();
    }

    private void setupMainFrame() {
        jFrame = new JFrame("Advanced Paint App");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1000, 700);
        jFrame.setLocationRelativeTo(null);
    }

    private void setupDrawingPanel() {
        drawingPanel = new DrawingPanel();
    }

    private void setupLayout() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(toolPanel, BorderLayout.WEST);
        mainPanel.add(drawingPanel, BorderLayout.CENTER);
        jFrame.add(mainPanel);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaintApp::new);
    }

    private void setupToolPanel() {
        toolPanel = new ToolPanel(drawingPanel);
    }
}
