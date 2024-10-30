package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow {
    private JFrame jFrame;
    private JPanel panel1;
    private JButton fileButton;
    private JPanel colorPanel;
    private JLabel imageLabel;
    private DrawManager drawManager;

    public MainWindow() {
        panel1 = new JPanel(new BorderLayout());
        fileButton = new JButton("Выбрать файл");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fileButton);

        colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout());

        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setIcon(DrawManager.createColorIcon(color));
            colorButton.addActionListener(e -> drawManager.setCurrentColor(color));
            colorPanel.add(colorButton);
        }

        buttonPanel.add(colorPanel);
        panel1.add(buttonPanel, BorderLayout.SOUTH);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(imageLabel, BorderLayout.CENTER);

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (ImageLoader.isImageFile(selectedFile.getName())) {
                        drawManager.loadImage(selectedFile, imageLabel);
                    } else {
                        JOptionPane.showMessageDialog(null, "Выбран файл не является изображением: " + selectedFile.getName());
                    }
                }
            }
        });

        // Создание окна и инициализация DrawManager
        jFrame = new JFrame("Megapushka");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(450, 350, 800, 600);
        jFrame.setContentPane(panel1);
        jFrame.setVisible(true);

        drawManager = new DrawManager();
        drawManager.setImageLabel(imageLabel);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
