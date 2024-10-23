package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class main {
    private JButton button1;
    private JPanel panel1;
    private JFrame jFrame;

    public main() {
        panel1 = new JPanel(new GridBagLayout());
        button1 = new JButton("Выбрать файл");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;

        panel1.add(button1, gbc);

        this.jFrame = getjFrame();
        this.jFrame.setContentPane(panel1);
        this.jFrame.setVisible(true);

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

    public JFrame getjFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(450, 350, 500, 300);
        jFrame.setTitle("Megapushka");
        jFrame.setVisible(true);
        return jFrame;
    }

    public static void main(String[] args) {
        new main();
    }


}
