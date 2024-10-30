package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") ||
                fileNameLower.endsWith(".png") || fileNameLower.endsWith(".gif") ||
                fileNameLower.endsWith(".svg");
    }

    public static void loadImage(File imageFile, JLabel imageLabel) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            int displayWidth = imageLabel.getWidth();
            int displayHeight = imageLabel.getHeight();
            float aspectRatio = (float) originalImage.getWidth() / originalImage.getHeight();

            if (displayWidth / aspectRatio < displayHeight) {
                displayHeight = (int) (displayWidth / aspectRatio);
            } else {
                displayWidth = (int) (displayHeight * aspectRatio);
            }

            BufferedImage scaledImage = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage.getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Не удалось загрузить изображение.");
        }
    }
}
