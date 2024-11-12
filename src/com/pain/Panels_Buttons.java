package com.pain;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Panels_Buttons {
    private JPanel panel1;
    private JButton fileButton;
    private JButton saveButton;
    private JButton newCanvasButton;
    private JPanel colorPanel;
    private JSlider sizeSlider;
    private JLabel imageLabel;

    public Panels_Buttons(ActionListener fileButtonListener, ActionListener saveButtonListener,
                          ActionListener newCanvasButtonListener, DrawManager drawManager) {

        panel1 = new JPanel(new BorderLayout());
        fileButton = new JButton("Выбрать файл");
        saveButton = new JButton("Сохранить");
        newCanvasButton = new JButton("Новый холст");

        // Add listeners
        fileButton.addActionListener(fileButtonListener);
        saveButton.addActionListener(saveButtonListener);
        newCanvasButton.addActionListener(newCanvasButtonListener);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fileButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(newCanvasButton);

        // Color Panel setup
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

        // Size slider setup
        sizeSlider = new JSlider(1, 20, 5);
        sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        buttonPanel.add(colorPanel);
        buttonPanel.add(sizeSlider);

        panel1.add(buttonPanel, BorderLayout.SOUTH);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(imageLabel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel1;
    }

    public JSlider getSizeSlider() {
        return sizeSlider;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }
}
