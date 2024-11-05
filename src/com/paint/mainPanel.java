package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class mainPanel extends JPanel {
    private JButton button1;
    imagePanel imagePanel;

    public mainPanel(JFrame jFrame) {
        setLayout(new BorderLayout());

        button1 = new JButton("Выбрать файл");
        button1.addActionListener(this::openFileChooser);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        buttonPanel.add(new colorButton(this));

        imagePanel = new imagePanel(jFrame);

        add(buttonPanel, BorderLayout.SOUTH);
        add(imagePanel, BorderLayout.CENTER);
    }

    private void openFileChooser(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagePanel.loadImage(fileChooser.getSelectedFile());
        }
    }
}
