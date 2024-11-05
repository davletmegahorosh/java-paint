package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class colorButton extends JPanel {
    public colorButton(mainPanel mainPanel) {
        setLayout(new FlowLayout());

        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.addActionListener(e -> mainPanel.imagePanel.setCurrentColor(color));
            add(colorButton);
        }
    }
}
