package com.pain;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageActions {
    private final DrawManager drawManager;
    private final JFrame jFrame;
    private boolean isImageModified = false;

    public ImageActions(DrawManager drawManager, JFrame jFrame) {
        this.drawManager = drawManager;
        this.jFrame = jFrame;
    }



    public void openNewCanvas(JLabel imageLabel) {
        if (isImageModified) {
            int option = JOptionPane.showConfirmDialog(
                    jFrame,
                    "Сохранить изменения перед открытием нового холста?",
                    "Подтверждение",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                saveImageDialog(imageLabel);
                if (!isImageModified) { // Only proceed if save was successful
                    createNewCanvas(imageLabel);
                }
            } else if (option == JOptionPane.NO_OPTION) {
                createNewCanvas(imageLabel);
            }
        } else {
            createNewCanvas(imageLabel); // If no modifications, just create the new canvas
        }
    }

    public void saveImageDialog(JLabel imageLabel) {
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

        isImageModified = false;
    }

    public void checkForUnsavedChanges(boolean newImagePending, JLabel imageLabel) {
        if (isImageModified) {
            int option = JOptionPane.showConfirmDialog(
                    jFrame,
                    "Сохранить изменения перед выходом?",
                    "Подтверждение",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                saveImageDialog(imageLabel);
                if (!newImagePending) {
                    jFrame.dispose();
                }
            } else if (option == JOptionPane.NO_OPTION) {
                if (newImagePending) {
                    openNewImage(imageLabel);
                } else {
                    jFrame.dispose();
                }
            }
        } else if (newImagePending) {
            openNewImage(imageLabel);
        } else {
            jFrame.dispose();
        }
    }

    public void openNewImage(JLabel imageLabel) {
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
    }

    private void createNewCanvas(JLabel imageLabel) {
        BufferedImage blankImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = blankImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageLabel.getWidth(), imageLabel.getHeight());
        g2d.dispose();
        drawManager.setBufferedImage(blankImage);
        imageLabel.setIcon(new ImageIcon(blankImage));
        isImageModified = false;
    }

    public void setImageModified(boolean modified) {
        this.isImageModified = modified;
    }
}
