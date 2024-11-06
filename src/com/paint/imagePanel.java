package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class imagePanel extends JLabel {
    private BufferedImage bufferedImage;
    private Color currentColor = Color.BLACK;
    private int lastX = -1;
    private int lastY = -1;

    public imagePanel(JFrame jFrame) {
        setHorizontalAlignment(SwingConstants.CENTER);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastX = -1;
                lastY = -1;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draw(e.getX(), e.getY());
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (bufferedImage != null) {
                    // Update icon with the scaled image
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
            JOptionPane.showMessageDialog(this, "Не удалось загрузить изображение.");
        }
    }

    private void draw(int x, int y) {
        if (bufferedImage != null) {
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setColor(currentColor);
            g2d.setStroke(new BasicStroke(5));

            if (lastX != -1 && lastY != -1) {
                g2d.drawLine(lastX, lastY, x, y);
            } else {
                g2d.fillOval(x - 2, y - 2, 5, 5);
            }

            g2d.dispose();
            setIcon(new ImageIcon(bufferedImage));
            repaint();
            lastX = x;
            lastY = y;
        }
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }
}
