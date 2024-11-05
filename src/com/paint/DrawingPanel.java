package com.paint;

import com.paint.Tool;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawingPanel extends JPanel {
    private BufferedImage canvasImage;
    private Graphics2D g2;
    private Color currentColor = Color.BLACK;
    private Tool currentTool = Tool.BRUSH;
    private int brushSize = 5;
    private int eraserSize = 10;

    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (g2 == null) {
                    initializeCanvas();
                }
                useTool(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                useTool(e);
                repaint();
            }
        });
    }

    public BufferedImage getCanvasImage() {
        return canvasImage;
    }

    private void initializeCanvas() {
        if (canvasImage == null) {
            canvasImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            g2 = canvasImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clearCanvas();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvasImage == null) {
            initializeCanvas();
        }
        g.drawImage(canvasImage, 0, 0, null);
    }

    private void useTool(MouseEvent e) {
        if (g2 == null) {
            initializeCanvas();
        }
        int x = e.getX();
        int y = e.getY();
        switch (currentTool) {
            case BRUSH:
                g2.setColor(currentColor);
                g2.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
                break;
            case ERASER:
                g2.setColor(Color.WHITE);
                g2.fillRect(x - eraserSize / 2, y - eraserSize / 2, eraserSize, eraserSize);
                break;
            default:
                break;
        }
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
    }

    public void setBrushSize(int size) {
        this.brushSize = size;
    }

    public void setEraserSize(int size) {
        this.eraserSize = size;
    }

    public void clearCanvas() {
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setPaint(Color.BLACK);
        repaint();
    }

    public void openImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            canvasImage = resizeImageToFitCanvas(image);
            g2 = canvasImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private BufferedImage resizeImageToFitCanvas(BufferedImage image) {
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        double scale = Math.min((double) canvasWidth / image.getWidth(), (double) canvasHeight / image.getHeight());
        int newWidth = (int) (image.getWidth() * scale);
        int newHeight = (int) (image.getHeight() * scale);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return resizedImage;
    }
}
