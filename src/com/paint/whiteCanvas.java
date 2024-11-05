package com.paint;

import javax.swing.*;
import java.awt.*;


public class WhiteCanvas extends JFrame {

    public WhiteCanvas() {
        setTitle("New file");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Рисуем белый фон
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawString("Простой белый лист", 20, 30);
            }
        };

        // Устанавливаем фон панели на белый
        panel.setBackground(Color.WHITE);
        add(panel);

        // Показываем окно
        setVisible(true);
    }

    public static void main(String[] args) {
        // Запуск приложения в потоке диспетчера событий
        SwingUtilities.invokeLater(() -> new WhiteCanvas());
    }
}
