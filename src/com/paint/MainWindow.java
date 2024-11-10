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
    private JButton newCanvasButton; // New button for blank canvas
    private JPanel colorPanel;
    private JLabel imageLabel;
    private JSlider sizeSlider;
    private DrawManager drawManager;
    private boolean isImageModified = false;
    private boolean newImagePending = false;

    public MainWindow() {
        panel1 = new JPanel(new BorderLayout());
        fileButton = new JButton("Выбрать файл");
        saveButton = new JButton("Сохранить");
        newCanvasButton = new JButton("Новый холст"); // Initialize new button

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fileButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(newCanvasButton); // Add new button to panel

        colorPanel = new JPanel(new FlowLayout());
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

        fileButton.addActionListener(e -> {
            newImagePending = true;
            checkForUnsavedChanges();
        });

        saveButton.addActionListener(e -> saveImageDialog());

        newCanvasButton.addActionListener(e -> openNewCanvas()); // Action for new blank canvas

        jFrame = new JFrame("Megapushka");
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setContentPane(panel1);
        jFrame.setVisible(true);

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                newImagePending = false;
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
                isImageModified = true;
            }
        });
    }

    private void openNewCanvas() {
        int width = imageLabel.getWidth();
        int height = imageLabel.getHeight();
        BufferedImage blankImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = blankImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        drawManager.setBufferedImage(blankImage);
        imageLabel.setIcon(new ImageIcon(blankImage));
        isImageModified = false; // Reset modification status for new canvas
    }

    private void saveImageDialog() {
        if (drawManager.getBufferedImage() == null) {
            JOptionPane.showMessageDialog(jFrame, "Нет изображения для сохранения.");
            return;
        }

        String[] options = {"Сохранить как новый", "Заменить"};
        int choice = JOptionPane.showOptionDialog(
                jFrame,
                "Сохранить изображение как новый файл или заменить существующий?",
                "Сохранение изображения",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            SaveImage.saveAsNew(drawManager.getBufferedImage(), jFrame);
        } else if (choice == 1) {
            SaveImage.replace(drawManager.getBufferedImage(), jFrame);
        }

        isImageModified = false; // Reset modification status after saving
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
                saveImageDialog();
                if (newImagePending) {
                    openNewImage();
                } else {
                    jFrame.dispose();
                }
            } else if (option == JOptionPane.NO_OPTION) {
                if (newImagePending) {
                    openNewImage();
                } else {
                    jFrame.dispose();
                }
            } else {
                newImagePending = false;
            }
        } else if (newImagePending) {
            openNewImage();
        } else {
            jFrame.dispose();
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
                isImageModified = false;
            } else {
                JOptionPane.showMessageDialog(null, "Выбран файл не является изображением: " + selectedFile.getName());
            }
        }
        newImagePending = false;
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
