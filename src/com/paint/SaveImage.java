package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SaveImage {
    public static void saveAsNew(BufferedImage image, JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить как новый файл");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG и JPEG файлы", "png", "jpg", "jpeg"));

        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();

            if (!filePath.endsWith(".png") && !filePath.endsWith(".jpg") && !filePath.endsWith(".jpeg")) {
                filePath += ".png";
            }

            try {
                ImageIO.write(image, "png", new File(filePath));
                JOptionPane.showMessageDialog(parentFrame, "Изображение сохранено: " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parentFrame, "Ошибка при сохранении изображения.");
            }
        }
    }

    public static void replace(BufferedImage image, JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Заменить файл");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG и JPEG файлы", "png", "jpg", "jpeg"));

        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();

            if (!filePath.endsWith(".png") && !filePath.endsWith(".jpg") && !filePath.endsWith(".jpeg")) {
                filePath += ".png";
            }

            try {
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(parentFrame, "Изображение сохранено: " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parentFrame, "Ошибка при сохранении изображения.");
            }
        }
    }
}
