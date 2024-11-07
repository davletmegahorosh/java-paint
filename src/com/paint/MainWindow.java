package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class MainWindow {
    private JFrame jFrame;
    private JPanel panel1;
    private JButton fileButton;
    private JButton saveButton;
    private JPanel colorPanel;
    private JLabel imageLabel;
    private JSlider sizeSlider;
    private DrawManager drawManager;
    private boolean isImageModified = false;  // Track modifications
    private boolean newImagePending = false;  // Track if a new image is pending

    public MainWindow() {
        panel1 = new JPanel(new BorderLayout());
        fileButton = new JButton("Выбрать файл");
        saveButton = new JButton("Сохранить");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fileButton);
        buttonPanel.add(saveButton);

        colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout());

        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(20, 20));
            colorButton.setIcon(DrawManager.createColorIcon(color));
            colorButton.addActionListener(e -> drawManager.setCurrentColor(color));
            colorPanel.add(colorButton);
        }

        sizeSlider = new JSlider(1, 20, 5);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizeSlider.addChangeListener(e -> drawManager.setStrokeSize(sizeSlider.getValue()));

        buttonPanel.add(colorPanel);
        buttonPanel.add(sizeSlider);
        panel1.add(buttonPanel, BorderLayout.SOUTH);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(imageLabel, BorderLayout.CENTER);

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newImagePending = true;  // Flag to indicate new image loading
                checkForUnsavedChanges();
            }
        });

        saveButton.addActionListener(e -> saveImage());

        jFrame = new JFrame("Megapushka");
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // Prevent immediate closing
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setContentPane(panel1);
        jFrame.setVisible(true);

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                newImagePending = false;  // Clear flag to differentiate from image loading
                checkForUnsavedChanges();
            }
        });

        drawManager = new DrawManager();
        drawManager.setImageLabel(imageLabel);
        drawManager.setStrokeSize(sizeSlider.getValue());

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
                isImageModified = true;  // Mark as modified on drawing
            }
        });
    }

    private void saveImage() {
        SaveImage.save(drawManager.getBufferedImage(), jFrame);
        isImageModified = false;  // Reset modification status after saving
    }

    private void checkForUnsavedChanges() {
        if (isImageModified) {
            int option = JOptionPane.showConfirmDialog(
                    jFrame,
                    "Сохранить изменения перед выходом?",
                    "Подтверждение",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                saveImage();
                if (newImagePending) {
                    openNewImage();
                } else {
                    jFrame.dispose();
                }
            } else if (option == JOptionPane.NO_OPTION) {
                if (newImagePending) {
                    openNewImage();  // Proceed to load new image
                } else {
                    jFrame.dispose();  // Close the application
                }
            } else if (option == JOptionPane.CANCEL_OPTION) {
                // Cancel the operation, return to the drawing page
                newImagePending = false;  // Reset flag to stay on the current drawing
            }
        } else if (newImagePending) {
            openNewImage();  // No changes, proceed to load new image directly
        } else {
            jFrame.dispose();  // No changes, close the application directly
        }
    }

    private void openNewImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (ImageLoader.isImageFile(selectedFile.getName())) {
                BufferedImage image = ImageLoader.loadImage(selectedFile);
                drawManager.setBufferedImage(image);
                ImageIcon scaledImageIcon = ImageLoader.scaleImage(image, imageLabel.getWidth(), imageLabel.getHeight());
                imageLabel.setIcon(scaledImageIcon);
                isImageModified = false;  // Reset modification status after loading new image
            } else {
                JOptionPane.showMessageDialog(null, "Выбран файл не является изображением: " + selectedFile.getName());
            }
        }
        newImagePending = false;  // Reset flag after loading new image
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
