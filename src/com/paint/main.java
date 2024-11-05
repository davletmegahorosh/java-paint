package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaintFrame::new);
    }
}

class PaintFrame extends JFrame {
    public PaintFrame() {
        setTitle("Advanced Paint App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImagePanel imagePanel = new ImagePanel();
        ControlPanel controlPanel = new ControlPanel(imagePanel);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(controlPanel.getLoadButton());
        bottomPanel.add(controlPanel.getSaveButton());
        bottomPanel.add(controlPanel.getEraserButton());
        bottomPanel.add(controlPanel.getColorPanel());
        bottomPanel.add(controlPanel.getBrushSizeSlider());

        add(imagePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class ControlPanel {
    private final JButton loadButton;
    private final JButton saveButton;
    private final JButton eraserButton;
    private final JPanel colorPanel;
    private final JSlider brushSizeSlider;

    public ControlPanel(ImagePanel imagePanel) {
        loadButton = new JButton("Load Image");
        loadButton.addActionListener(e -> imagePanel.loadImage());

        saveButton = new JButton("Save Image");
        saveButton.addActionListener(e -> imagePanel.saveImageWithFormat());

        eraserButton = new JButton("Eraser");
        eraserButton.addActionListener(e -> imagePanel.activateEraser());

        colorPanel = new JPanel(new FlowLayout());
        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE, Color.GRAY, Color.WHITE};
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBackground(color);
            colorButton.addActionListener(e -> imagePanel.setCurrentColor(color));
            colorPanel.add(colorButton);
        }

        brushSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 5);
        brushSizeSlider.addChangeListener(e -> imagePanel.setBrushSize(brushSizeSlider.getValue()));
    }

    public JButton getLoadButton() { return loadButton; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getEraserButton() { return eraserButton; }
    public JPanel getColorPanel() { return colorPanel; }
    public JSlider getBrushSizeSlider() { return brushSizeSlider; }
}

class ImagePanel extends JLabel {
    private BufferedImage originalImage, drawingLayer;
    private Color currentColor = Color.BLACK;
    private int brushSize = 5;
    private int lastX = -1, lastY = -1;
    private boolean eraserMode = false;

    public ImagePanel() {
        setPreferredSize(new Dimension(800, 600));
        setHorizontalAlignment(SwingConstants.CENTER);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draw(e.getX(), e.getY());
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastX = -1;
                lastY = -1;
                eraserMode = false;
            }
        });
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        this.eraserMode = false;
    }

    public void setBrushSize(int size) { this.brushSize = size; }

    public void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                originalImage = ImageIO.read(file);
                drawingLayer = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                setIcon(new ImageIcon(mergeImages()));
                repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading image.");
            }
        }
    }

    public void saveImageWithFormat() {
        String[] formats = {"PNG", "JPG"};
        String format = (String) JOptionPane.showInputDialog(null, "Choose format:", "Save Image", JOptionPane.QUESTION_MESSAGE, null, formats, formats[0]);
        if (format != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Image");
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + "." + format.toLowerCase());
                    BufferedImage mergedImage = mergeImages();

                    if (format.equals("JPG") && mergedImage.getColorModel().hasAlpha()) {
                        BufferedImage jpgImage = new BufferedImage(mergedImage.getWidth(), mergedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = jpgImage.createGraphics();
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(0, 0, jpgImage.getWidth(), jpgImage.getHeight());
                        g2d.drawImage(mergedImage, 0, 0, null);
                        g2d.dispose();
                        ImageIO.write(jpgImage, format.toLowerCase(), file);
                    } else {
                        ImageIO.write(mergedImage, format.toLowerCase(), file);
                    }

                    JOptionPane.showMessageDialog(null, "Image saved successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving image.");
                }
            }
        }
    }

    public void activateEraser() {
        eraserMode = true;
        currentColor = new Color(0, 0, 0, 0);
    }

    private void draw(int x, int y) {
        if (originalImage == null) return;

        int cursorX = (int) ((double) x / getWidth() * originalImage.getWidth());
        int cursorY = (int) ((double) y / getHeight() * originalImage.getHeight());

        Graphics2D g2d = drawingLayer.createGraphics();
        g2d.setColor(currentColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (eraserMode) {
            g2d.setComposite(AlphaComposite.Clear);
        }

        if (lastX != -1 && lastY != -1) {
            g2d.drawLine(lastX, lastY, cursorX, cursorY);
        } else {
            g2d.fillOval(cursorX - brushSize / 2, cursorY - brushSize / 2, brushSize, brushSize);
        }

        lastX = cursorX;
        lastY = cursorY;

        g2d.dispose();
        repaint();
    }

    private BufferedImage mergeImages() {
        BufferedImage mergedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = mergedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.drawImage(drawingLayer, 0, 0, null);
        g.dispose();
        return mergedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (originalImage != null) {
            g.drawImage(mergeImages(), 0, 0, getWidth(), getHeight(), null);
        }
    }
}
