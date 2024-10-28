package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class main {
    private JButton button1;
    private JPanel panel1;
    private JLabel imageLabel;
    private JFrame jFrame;

    public main() {
        // Создаем главную панель с компоновкой BorderLayout
        panel1 = new JPanel(new BorderLayout());

        // Кнопка "Выбрать файл" будет внизу
        button1 = new JButton("Выбрать файл");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        panel1.add(buttonPanel, BorderLayout.SOUTH);

        // Место для изображения (пустое при запуске)
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(imageLabel, BorderLayout.CENTER);

        // Настройка основного окна
        this.jFrame = getJFrame();
        this.jFrame.setContentPane(panel1);
        this.jFrame.setVisible(true);

        // Обработка события нажатия на кнопку
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();

                    if (isImageFile(fileName)) {
                        showImageInMainFrame(selectedFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Выбран файл не является изображением: " + fileName);
                    }
                }
            }
        });
    }

    private boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") ||
                fileNameLower.endsWith(".png") || fileNameLower.endsWith(".gif") ||
                fileNameLower.endsWith(".svg");
    }

    private void showImageInMainFrame(File imageFile) {
        ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
        Image originalImage = imageIcon.getImage();

        // Сохранение пропорций изображения
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);
        float aspectRatio = (float) width / height;

        int displayWidth = panel1.getWidth();
        int displayHeight = panel1.getHeight() - button1.getHeight();

        if (displayWidth / aspectRatio < displayHeight) {
            displayHeight = (int) (displayWidth / aspectRatio);
        } else {
            displayWidth = (int) (displayHeight * aspectRatio);
        }

        Image scaledImage = originalImage.getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
        jFrame.revalidate(); // Обновляем окно после изменения
    }

    private JFrame getJFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(450, 350, 800, 600);
        jFrame.setTitle("Megapushka");
        return jFrame;
    }

    public static void main(String[] args) {
        new main();
    }
}
