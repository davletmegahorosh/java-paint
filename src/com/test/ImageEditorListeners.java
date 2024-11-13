//package com.test;
//
//import com.paint.SaveImage;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.image.BufferedImage;
//
//public class ImageEditorListeners {
//    private ImageEditor imageEditor;
//    private boolean isImageModified = false;
//
//    public ImageEditorListeners(ImageEditor imageEditor) {
//        this.imageEditor = imageEditor;
//    }
//
//    public void setupListeners(ImageEditorUI ui, DrawManager drawManager) {
//        ui.getFileButton().addActionListener(e -> openNewImage(drawManager, ui));
//        ui.getSaveButton().addActionListener(e -> saveImageDialog(drawManager));
//        ui.getNewCanvasButton().addActionListener(e -> openNewCanvas(drawManager, ui));
//
//        ui.getSizeSlider().addChangeListener(e -> drawManager.setStrokeSize(ui.getSizeSlider().getValue()));
//
//        ui.getFrame().addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                checkForUnsavedChanges(drawManager, ui);
//            }
//        });
//    }
//
//    private void openNewCanvas() {
//        if (isImageModified) {
//            int option = JOptionPane.showConfirmDialog(
//                    jFrame,
//                    "Сохранить изменения перед открытием нового холста?",
//                    "Подтверждение",
//                    JOptionPane.YES_NO_CANCEL_OPTION
//            );
//            if (option == JOptionPane.YES_OPTION) {
//                saveImageDialog(); // Save the image before opening the new canvas
//                if (isImageModified) { // Check if the save was successful
//                    BufferedImage blankImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
//                    Graphics2D g2d = blankImage.createGraphics();
//                    g2d.setColor(Color.WHITE);
//                    g2d.fillRect(0, 0, imageLabel.getWidth(), imageLabel.getHeight());
//                    g2d.dispose();
//                    drawManager.setBufferedImage(blankImage);
//                    imageLabel.setIcon(new ImageIcon(blankImage));
//                    isImageModified = false; // Reset modification status for new canvas
//                }
//            } else if (option == JOptionPane.NO_OPTION) {
//                BufferedImage blankImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
//                Graphics2D g2d = blankImage.createGraphics();
//                g2d.setColor(Color.WHITE);
//                g2d.fillRect(0, 0, imageLabel.getWidth(), imageLabel.getHeight());
//                g2d.dispose();
//                drawManager.setBufferedImage(blankImage);
//                imageLabel.setIcon(new ImageIcon(blankImage));
//                isImageModified = false; // Reset modification status for new canvas
//            }
//        } else {
//            // If no changes have been made, directly create the new canvas
//            BufferedImage blankImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2d = blankImage.createGraphics();
//            g2d.setColor(Color.WHITE);
//            g2d.fillRect(0, 0, imageLabel.getWidth(), imageLabel.getHeight());
//            g2d.dispose();
//            drawManager.setBufferedImage(blankImage);
//            imageLabel.setIcon(new ImageIcon(blankImage));
//            isImageModified = false; // Reset modification status for new canvas
//        }
//    }
//
//    private void saveImageDialog() {
//        if (drawManager.getBufferedImage() == null) {
//            JOptionPane.showMessageDialog(jFrame, "Нет изображения для сохранения.");
//            return;
//        }
//
//        String[] options = {"Сохранить как новый", "Заменить"};
//        int choice = JOptionPane.showOptionDialog(
//                jFrame,
//                "Сохранить изображение как новый файл или заменить существующий?",
//                "Сохранение изображения",
//                JOptionPane.DEFAULT_OPTION,
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                options,
//                options[0]
//        );
//
//        if (choice == 0) {
//            SaveImage.saveAsNew(drawManager.getBufferedImage(), jFrame);
//        } else if (choice == 1) {
//            SaveImage.replace(drawManager.getBufferedImage(), jFrame);
//        }
//
//        isImageModified = false; // Reset modification status after saving
//    }
//
//    private void openNewImage(DrawManager drawManager, ImageEditorUI ui) {
//        JFileChooser fileChooser = new JFileChooser();
//        int result = fileChooser.showOpenDialog(null);
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            BufferedImage image = ImageLoader.loadImage(selectedFile);
//            drawManager.setBufferedImage(image);
//            ui.getImageLabel().setIcon(new ImageIcon(image));
//            isImageModified = false;
//        }
//    }
//
//    private void checkForUnsavedChanges(DrawManager drawManager, ImageEditorUI ui) {
//        if (isImageModified) {
//            int option = JOptionPane.showConfirmDialog(
//                    ui.getFrame(),
//                    "Сохранить изменения перед выходом?",
//                    "Подтверждение",
//                    JOptionPane.YES_NO_CANCEL_OPTION
//            );
//            if (option == JOptionPane.YES_OPTION) {
//                saveImageDialog(drawManager);
//                ui.getFrame().dispose();
//            } else if (option == JOptionPane.NO_OPTION) {
//                ui.getFrame().dispose();
//            }
//        } else {
//            ui.getFrame().dispose();
//        }
//    }
//}
