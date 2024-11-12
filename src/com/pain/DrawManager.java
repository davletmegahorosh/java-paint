package com.pain;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawManager {
    private BufferedImage bufferedImage;
    private JLabel imageLabel;
    private Color currentColor = Color.BLACK;
    private int strokeSize = 5;  // Default stroke size
    private int lastX = -1;
    private int lastY = -1;

    public void setImageLabel(JLabel imageLabel) {
        this.imageLabel = imageLabel;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setStrokeSize(int size) {
        this.strokeSize = size;
    }

    public static ImageIcon createColorIcon(Color color) {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, 30, 30);
        g2d.dispose();
        return new ImageIcon(image);
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        updateImageDisplay();
    }
    public void initializeImageIfNeeded(int width, int height) {
        if (bufferedImage == null || bufferedImage.getWidth() != width || bufferedImage.getHeight() != height) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setColor(Color.WHITE); // Set a background color if desired
            g2d.fillRect(0, 0, width, height);
            g2d.dispose();
            updateImageDisplay();
        }
    }


    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void draw(int x, int y) {
        if (bufferedImage != null && imageLabel != null) {
            double scaleX = (double) imageLabel.getWidth() / bufferedImage.getWidth();
            double scaleY = (double) imageLabel.getHeight() / bufferedImage.getHeight();
            double scaleFactor = Math.min(scaleX, scaleY);

            int displayedWidth = (int) (bufferedImage.getWidth() * scaleFactor);
            int displayedHeight = (int) (bufferedImage.getHeight() * scaleFactor);

            int offsetX = (imageLabel.getWidth() - displayedWidth) / 2;
            int offsetY = (imageLabel.getHeight() - displayedHeight) / 2;

            int adjustedX = (int) ((x - offsetX) / scaleFactor);
            int adjustedY = (int) ((y - offsetY) / scaleFactor);

            if (adjustedX >= 0 && adjustedX < bufferedImage.getWidth() &&
                    adjustedY >= 0 && adjustedY < bufferedImage.getHeight()) {

                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.setColor(currentColor);
                g2d.setStroke(new BasicStroke(strokeSize));  // Set stroke size based on slider

                if (lastX != -1 && lastY != -1) {
                    g2d.drawLine(lastX, lastY, adjustedX, adjustedY);
                } else {
                    g2d.fillOval(adjustedX - strokeSize / 2, adjustedY - strokeSize / 2, strokeSize, strokeSize);
                }

                g2d.dispose();
                lastX = adjustedX;
                lastY = adjustedY;

                updateImageDisplay();
            }
        }
    }

    public void resetLastPoint() {
        lastX = -1;
        lastY = -1;
    }

    private void updateImageDisplay() {
        if (bufferedImage != null && imageLabel != null) {
            double scaleX = (double) imageLabel.getWidth() / bufferedImage.getWidth();
            double scaleY = (double) imageLabel.getHeight() / bufferedImage.getHeight();
            double scaleFactor = Math.min(scaleX, scaleY);

            int displayedWidth = (int) (bufferedImage.getWidth() * scaleFactor);
            int displayedHeight = (int) (bufferedImage.getHeight() * scaleFactor);

            Image scaledImage = bufferedImage.getScaledInstance(displayedWidth, displayedHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.repaint(); // Ensure the label refreshes
        }
    }

}
