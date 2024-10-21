package com.paint;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class main {
    private JButton button1;
    private JPanel panel1;

    public main() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Создаем экземпляр JFileChooser
                JFileChooser fileChooser = new JFileChooser();
                // Устанавливаем режим выбора файлов и папок
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                // Показываем диалоговое окно для выбора
                int result = fileChooser.showOpenDialog(null);

                // Если пользователь выбрал файл или папку
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    // Получаем список всех файлов и папок в выбранной директории
                    if (selectedFile.isDirectory()) {
                        File[] files = selectedFile.listFiles();
                        if (files != null) {
                            StringBuilder fileList = new StringBuilder("Список файлов и папок:\n");
                            for (File file : files) {
                                fileList.append(file.getName()).append("\n");
                            }
                            JOptionPane.showMessageDialog(null, fileList.toString());
                        } else {
                            JOptionPane.showMessageDialog(null, "Папка пуста.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Выбран файл: " + selectedFile.getName());
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("main");
        frame.setContentPane(new main().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
