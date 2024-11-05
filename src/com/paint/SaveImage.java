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

            File targetFile = new File(filePath);

            if (targetFile.exists()) {
                // Показываем диалоговое окно, если файл уже существует
                int userChoice = JOptionPane.showOptionDialog(
                        parentFrame,
                        "Файл уже существует. Хотите изменить его или создать новый файл?",
                        "Подтверждение",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Изменить", "Создать новый"},
                        "Создать новый"
                );

                // Если пользователь выбрал "Изменить" (изменит файл)
                if (userChoice == JOptionPane.YES_OPTION) {
                    try {
                        ImageIO.write(image, "png", new File(filePath));
                        JOptionPane.showMessageDialog(parentFrame, "Изображение сохранено: " + filePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(parentFrame, "Ошибка при сохранении изображения.");
                    }
                    return;
                }
                // Если пользователь выбрал "Создать новый" (создаст новый файл с новым именем)
                else {
                    int suffixNumber = 1;
                    String newFilePath = filePath;

                    while (new File(newFilePath).exists()) {
                        int dotIndex = filePath.lastIndexOf('.'); // Ищем точки (расширение файла)
                        String baseName = (dotIndex == -1) ? filePath : filePath.substring(0, dotIndex); // Имя файла без расширения
                        String extension = (dotIndex == -1) ? "" : filePath.substring(dotIndex); // Расширение файла


                        newFilePath = baseName + "(" + suffixNumber + ")" + extension;
                        suffixNumber++;
                    }

                    // Сохраняем изображение в новый файл с новым именем
                    try {
                        ImageIO.write(image, "png", new File(newFilePath));
                        JOptionPane.showMessageDialog(parentFrame, "Изображение сохранено: " + newFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(parentFrame, "Ошибка при сохранении изображения.");
                    }
                }
            } else {

                try {
                    ImageIO.write(image, "png", new File(filePath));
                    JOptionPane.showMessageDialog(parentFrame, "Изображение сохранено: " + filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame, "Ошибка при сохранении изображения.");
                }
            }
        }
    }
}