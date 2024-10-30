package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
                        BufferedImage image = ImageLoader.loadImage(selectedFile);
                        drawManager.setBufferedImage(image);

                        // Масштабируем изображение с учетом пустого пространства
                        ImageIcon scaledImageIcon = ImageLoader.scaleImage(image, imageLabel.getWidth(), imageLabel.getHeight());
                        imageLabel.setIcon(scaledImageIcon);
                    } else {
                        JOptionPane.showMessageDialog(null, "Выбран файл не является изображением: " + selectedFile.getName());
                    }
                }
            }
        });

        jFrame = new JFrame("Megapushka");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setContentPane(panel1);
        jFrame.setVisible(true);

        drawManager = new DrawManager();
        drawManager.setImageLabel(imageLabel);

        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                drawManager.resetLastPoint();
            }
        });

        imageLabel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                drawManager.draw(e.getX(), e.getY());
            }
        });
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
