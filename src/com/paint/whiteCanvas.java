package com.paint;

import javax.swing.*;
import java.awt.*;


public class whiteCanvas extends JFrame {

    public whiteCanvas() {
        setTitle("New file");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawString("White canva", 20, 30);
            }
        };

        panel.setBackground(Color.WHITE);
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new whiteCanvas());
    }
}
