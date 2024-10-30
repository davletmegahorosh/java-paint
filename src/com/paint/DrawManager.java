package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawManager {
    private Color currentColor = Color.BLACK;
    private BufferedImage bufferedImage;
    private JLabel imageLabel;
    private int lastX = -1, lastY = -1;

    public static ImageIcon createColorIcon(Color color) {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, 30, 30);
        g2d.dispose();
        return new ImageIcon(image);
    }

    public void setCurrentColor(Color color) {
        currentColor = color;
    }

    public void setImageLabel(JLabel label) {
        this.imageLabel = label;
        addDrawingListener();
    }

    public void loadImage(File file, JLabel label) {
        ImageLoader.loadImage(file, label);
        this.imageLabel = label;
    }

    private void addDrawingListener() {
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastX = -1;
                lastY = -1;
            }
        });

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draw(e.getX(), e.getY());
            }
        });
    }

    private void draw(int x, int y) {
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            imageLabel.setIcon(new ImageIcon(bufferedImage));
        }
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(currentColor);
        g2d.setStroke(new BasicStroke(5));

        if (lastX != -1 && lastY != -1) {
            g2d.drawLine(lastX, lastY, x, y);
        } else {
            g2d.fillOval(x - 2, y - 2, 5, 5);
        }
        g2d.dispose();
        imageLabel.repaint();
        lastX = x;
        lastY = y;
    }
}
