package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class whiteCanva extends JFrame {

    private JPanel panel1;
    private JLabel imageLabel;
    private Color currentColor = Color.BLACK;

    private int lastX = -1;
    private int lastY = -1;

    public whiteCanva() {
        setTitle("Новый файл");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Закрытие окна без завершения программы
        setLocationRelativeTo(null); // Центрируем окно

        panel1 = new JPanel(new BorderLayout());

        // Создаем панель для кнопок
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Сохранить файл");
        JButton chooseColorButton = new JButton("Выбрать цвет");

        // Слушатель для кнопки сохранения
        saveButton.addActionListener(e -> {
            // Для сохранения файла можно добавить здесь код
            // Например, использовать JFileChooser для выбора места сохранения
            JOptionPane.showMessageDialog(this, "Функция сохранения пока не реализована.");
        });

        // Слушатель для выбора цвета
        chooseColorButton.addActionListener(e -> {
            currentColor = JColorChooser.showDialog(this, "Выберите цвет", currentColor);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(chooseColorButton);

        // Панель для рисования
        imageLabel = new JLabel();
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setOpaque(true); // Чтобы фон был видимым
        imageLabel.setPreferredSize(new Dimension(800, 500));
        panel1.add(imageLabel, BorderLayout.CENTER);

        // Панель с кнопками добавляем в нижнюю часть
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        add(panel1);

        // Обработчики мыши для рисования
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastX = -1;
                lastY = -1;
            }
        });

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draw(e.getX(), e.getY());
            }
        });

        setVisible(true);
    }

    private void draw(int x, int y) {
        // Получаем Graphics и приводим его к Graphics2D, чтобы использовать setStroke
        if (lastX != -1 && lastY != -1) {
            Graphics g = imageLabel.getGraphics();
            Graphics2D g2d = (Graphics2D) g; // Приведение типа к Graphics2D
            g2d.setColor(currentColor);
            g2d.setStroke(new BasicStroke(5)); // Толщина линии
            g2d.drawLine(lastX, lastY, x, y); // Рисуем линию

            // Обновляем координаты для следующего движения мыши
            lastX = x;
            lastY = y;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new whiteCanva());
    }
}
