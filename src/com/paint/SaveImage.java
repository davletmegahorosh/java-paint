package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SaveImage {
    public static void save(BufferedImage image, JFrame parentFrame) {
        // Проверяем, есть ли изображение для сохранения
        if (image == null) {
            JOptionPane.showMessageDialog(parentFrame, "Нет изображения для сохранения.");
            return;
        }

        // Создаем диалог выбора вида файла для сохранения
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить изображение");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG и JPEG файлы", "png", "jpg", "jpeg"));

        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();

            // Если пользователь не указал расширение, добавляем .png по умолчанию
            if (!filePath.endsWith(".png") && !filePath.endsWith(".jpg") && !filePath.endsWith(".jpeg")) {
                filePath += ".png";
            }

            try {
                // Сохраняем изображение в файл
                ImageIO.write(image, "png", new File(filePath));
                JOptionPane.showMessageDialog(parentFrame, "Изображение сохранено: " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parentFrame, "Ошибка при сохранении изображения.");
            }
        }
    }
}
