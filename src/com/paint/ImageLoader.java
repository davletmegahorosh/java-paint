package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageLoader {
    public static boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") ||
                fileNameLower.endsWith(".png") || fileNameLower.endsWith(".gif") ||
                fileNameLower.endsWith(".svg");
    }

    public static BufferedImage loadImage(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon scaleImage(BufferedImage image, int targetWidth, int targetHeight) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double widthRatio = (double) targetWidth / imageWidth;
        double heightRatio = (double) targetHeight / imageHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio);  // Выбираем минимальный масштаб, чтобы поместиться по одной из сторон

        int newWidth = (int) (imageWidth * scaleFactor);
        int newHeight = (int) (imageHeight * scaleFactor);

        // Создаем новое изображение с фоном, чтобы оно занимало полный размер JLabel
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);

        // Центрируем изображение
        int x = (targetWidth - newWidth) / 2;
        int y = (targetHeight - newHeight) / 2;
        g2d.drawImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), x, y, null);
        g2d.dispose();

        return new ImageIcon(scaledImage);
    }
}
