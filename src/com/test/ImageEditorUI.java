//package com.test;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class ImageEditorUI {
//    private JFrame jFrame;
//    private JPanel panel1;
//    private JButton fileButton;
//    private JButton saveButton;
//    private JButton newCanvasButton;
//    private JPanel colorPanel;
//    private JLabel imageLabel;
//    private JSlider sizeSlider;
//
//    public ImageEditorUI() {
//        setupUI();
//    }
//
//    private void setupUI() {
//        panel1 = new JPanel(new BorderLayout());
//        fileButton = new JButton("Выбрать файл");
//        saveButton = new JButton("Сохранить");
//        newCanvasButton = new JButton("Новый холст");
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(fileButton);
//        buttonPanel.add(saveButton);
//        buttonPanel.add(newCanvasButton);
//
//        colorPanel = new JPanel(new FlowLayout());
//        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
//        for (Color color : colors) {
//            JButton colorButton = new JButton();
//            colorButton.setBackground(color);
//            colorButton.setPreferredSize(new Dimension(20, 20));
//            colorButton.setIcon(DrawManager.createColorIcon(color));
//            colorPanel.add(colorButton);
//        }
//
//        sizeSlider = new JSlider(1, 20, 5);
//        sizeSlider.setMajorTickSpacing(5);
//        sizeSlider.setPaintTicks(true);
//        sizeSlider.setPaintLabels(true);
//
//        buttonPanel.add(colorPanel);
//        buttonPanel.add(sizeSlider);
//        panel1.add(buttonPanel, BorderLayout.SOUTH);
//
//        imageLabel = new JLabel();
//        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        panel1.add(imageLabel, BorderLayout.CENTER);
//
//        jFrame = new JFrame("Megapushka");
//        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        jFrame.setSize(800, 600);
//        jFrame.setLocationRelativeTo(null);
//        jFrame.setContentPane(panel1);
//    }
//
//    public JFrame getFrame() {
//        return jFrame;
//    }
//
//    public JLabel getImageLabel() {
//        return imageLabel;
//    }
//
//    public JButton getFileButton() {
//        return fileButton;
//    }
//
//    public JButton getSaveButton() {
//        return saveButton;
//    }
//
//    public JButton getNewCanvasButton() {
//        return newCanvasButton;
//    }
//
//    public JSlider getSizeSlider() {
//        return sizeSlider;
//    }
//}
