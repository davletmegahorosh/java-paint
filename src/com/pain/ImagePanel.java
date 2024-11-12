package com.pain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImagePanel extends JLabel {
    private BufferedImage bufferedImage;
    private Color currentColor = Color.BLACK;
    private int lastX = -1;
    private int lastY = -1;
    private DrawManager drawManager;
    private ImageActions imageActions;

    // Updated constructor to receive DrawManager and ImageActions
    public ImagePanel(JFrame jFrame, DrawManager drawManager, ImageActions imageActions) {
        this.drawManager = drawManager;
        this.imageActions = imageActions;
        setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                drawManager.resetLastPoint();  // Reset drawing point in DrawManager
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                drawManager.draw(e.getX(), e.getY());  // Use DrawManager for drawing
                repaint();  // Refresh the display
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (bufferedImage != null) {
                    Image scaledImage = bufferedImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(scaledImage));
                }
            }
        });
    }

    public void loadImage(File imageFile) {
        try {
            bufferedImage = javax.imageio.ImageIO.read(imageFile);
            Image scaledImage = bufferedImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load image.");
        }
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        drawManager.setCurrentColor(color);  // Sync color with DrawManager
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        setIcon(new ImageIcon(bufferedImage));
    }
}
