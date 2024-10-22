package com.paint;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class main {
    private JButton button1;
    private JPanel panel1;
    private JFrame jFrame;

    public main(JFrame jFrame) {
        this.jFrame = jFrame;
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    if (selectedFile.isDirectory()) {
                        File[] files = selectedFile.listFiles();
                        if (files != null) {
                            StringBuilder fileList = new StringBuilder("Список файлов и папок:\n");
                            for (File file : files) {
                                String fileName = file.getName();
                                if (isImageFile(fileName)) {
                                    fileList.append(fileName).append(" (Изображение)\n");
                                } else {
                                    fileList.append(fileName).append("\n");
                                }
                            }
                            JOptionPane.showMessageDialog(null, fileList.toString());
                        } else {
                            JOptionPane.showMessageDialog(null, "Папка пуста.");
                        }
                    } else {
                        String fileName = selectedFile.getName();
                        if (isImageFile(fileName)) {
                            JOptionPane.showMessageDialog(null, fileName + " (Изображение)");
                        } else {
                            JOptionPane.showMessageDialog(null, "Выбран файл: " + fileName);
                        }
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

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setContentPane(new main(jFrame).panel1);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(450, 350, 500, 300);
        jFrame.setTitle("Megapushka");
        jFrame.setVisible(true);
    }
}
