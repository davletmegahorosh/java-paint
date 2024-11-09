package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class whiteCanva extends JFrame {
    private JPanel panel1;
    private JLabel imageLabel;
    private Color currentColor = Color.BLACK;  // Начальный цвет - черный
    private int lastX = -1;
    private int lastY = -1;
    private BufferedImage drawingImage;  // BufferedImage для хранения рисунка

    public whiteCanva() {
        setTitle("Новый файл");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Закрытие окна без завершения программы
        setLocationRelativeTo(null); // Центрирование окна

        panel1 = new JPanel(new BorderLayout());

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Сохранить файл");
        JButton newFileButton = new JButton("Открыть новый файл");
        JButton chooseFileButton = new JButton("Выбрать файл");  // Кнопка для выбора файла

        // Слушатели для кнопок
        saveButton.addActionListener(e -> {
            JFileChooser saveChooser = new JFileChooser();
            int result = saveChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = saveChooser.getSelectedFile();
                try {
                    ImageIO.write(drawingImage, "png", selectedFile); // Сохранение изображения
                    JOptionPane.showMessageDialog(this, "Файл сохранен: " + selectedFile.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка при сохранении файла.");
                }
            }
        });

        newFileButton.addActionListener(e -> {
            new whiteCanva(); // Создаем новый пустой холст
        });

        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName();

                if (isImageFile(fileName)) {
                    loadImage(selectedFile); // Загружаем изображение в холст
                } else {
                    JOptionPane.showMessageDialog(this, "Выбран файл не является изображением: " + fileName);
                }
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(newFileButton);
        buttonPanel.add(chooseFileButton); // Добавляем кнопку выбора файла

        // Панель для цветов
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout()); // Цвета располагаются по горизонтали

        // Цвета для кнопок
        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));  // Размер кнопки
            colorButton.addActionListener(e -> currentColor = color);  // Изменяем текущий цвет
            colorPanel.add(colorButton);
        }

        // Теперь панель с цветами добавляется снизу
        panel1.add(colorPanel, BorderLayout.SOUTH);

        // Панель для рисования (JLabel для отображения изображения)
        imageLabel = new JLabel();
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setOpaque(true); // Чтобы фон был видимым
        imageLabel.setPreferredSize(new Dimension(800, 500));

        // Инициализация изображения (белый холст)
        drawingImage = new BufferedImage(800, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = drawingImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, drawingImage.getWidth(), drawingImage.getHeight()); // Заполняем холст белым
        g2d.dispose();

        // Устанавливаем JLabel для отображения BufferedImage
        imageLabel.setIcon(new ImageIcon(drawingImage));

        buttonPanel.add(colorPanel);
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        panel1.add(imageLabel, BorderLayout.CENTER); // Холст для рисования

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

    // Метод рисования
    private void draw(int x, int y) {
        if (lastX != -1 && lastY != -1) {
            // Получаем Graphics2D объект из drawingImage
            Graphics2D g2d = drawingImage.createGraphics();
            g2d.setColor(currentColor);
            g2d.setStroke(new BasicStroke(5)); // Устанавливаем толщину линии

            g2d.drawLine(lastX, lastY, x, y); // Рисуем линию

            g2d.dispose(); // Освобождаем ресурсы

            imageLabel.repaint(); // Перерисовываем JLabel для обновления отображения

            lastX = x;
            lastY = y;
        }
    }

    // Проверка, является ли файл изображением
    private boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") ||
                fileNameLower.endsWith(".png") || fileNameLower.endsWith(".gif") ||
                fileNameLower.endsWith(".svg");
    }

    // Загрузка изображения
    private void loadImage(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            Graphics2D g2d = drawingImage.createGraphics();
            g2d.drawImage(image, 0, 0, imageLabel.getWidth(), imageLabel.getHeight(), null);
            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(drawingImage)); // Обновляем изображение в imageLabel
            imageLabel.repaint();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке изображения.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(whiteCanva::new);
    }
}
