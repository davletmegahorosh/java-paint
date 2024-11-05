package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class ToolPanel extends JPanel {
    private final DrawingPanel drawingPanel; // Панель рисования, с которой взаимодействуют инструменты
    private final JFileChooser fileChooser; // Диалог выбора файла для открытия и сохранения

    // Конструктор ToolPanel, инициализирует интерфейс панели инструментов
    public ToolPanel(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        fileChooser = new JFileChooser();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Устанавливаем вертикальную компоновку
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Добавляем отступы для панели

        add(createFileOperationsPanel()); // Добавляем панель с файлами (создать, открыть, сохранить)
        add(Box.createVerticalStrut(20)); // Добавляем вертикальный промежуток между элементами
        add(createColorPickerPanel()); // Добавляем панель выбора цвета
    }

    // Создает панель для операций с файлами (создание, открытие, сохранение)
    private JPanel createFileOperationsPanel() {
        JPanel filePanel = new JPanel(new GridLayout(3, 1, 5, 5)); // Панель с сеткой из 3 строк и 1 столбца

        JButton newFileButton = new JButton("New File"); // Кнопка "Новый файл"
        newFileButton.addActionListener(e -> drawingPanel.clearCanvas()); // Очищает холст при нажатии
        filePanel.add(newFileButton);

        JButton openFileButton = new JButton("Open"); // Кнопка "Открыть"
        openFileButton.addActionListener(e -> openFile()); // Открывает файл при нажатии
        filePanel.add(openFileButton);

        JButton saveFileButton = new JButton("Save"); // Кнопка "Сохранить"
        saveFileButton.addActionListener(e -> saveFile()); // Сохраняет файл при нажатии
        filePanel.add(saveFileButton);

        return filePanel;
    }

    // Создает панель выбора цвета
    private JPanel createColorPickerPanel() {
        JPanel colorPanel = new JPanel(new GridLayout(2, 3, 5, 5)); // Сетка 2 строки, 3 столбца
        colorPanel.setBorder(BorderFactory.createTitledBorder("Colors")); // Заголовок панели

        // Массив цветов для кнопок выбора цвета
        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE};

        // Создаем кнопки для каждого цвета
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color); // Устанавливаем цвет кнопки
            colorButton.setPreferredSize(new Dimension(30, 30)); // Устанавливаем размер кнопки
            colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Рамка вокруг кнопки
            colorButton.addActionListener(e -> drawingPanel.setCurrentColor(color)); // Устанавливает текущий цвет при выборе
            colorPanel.add(colorButton);
        }

        return colorPanel;
    }

    // Открывает изображение из файла
    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) { // Если файл выбран
            File selectedFile = fileChooser.getSelectedFile();
            drawingPanel.openImage(selectedFile); // Открываем изображение в панели рисования
        }
    }

    // Сохраняет изображение в файл
    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) { // Если место для сохранения выбрано
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(drawingPanel.getCanvasImage(), "jpg", fileToSave); // Сохраняем изображение в формате JPG
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage()); // Показываем сообщение об ошибке
            }
        }
    }
}
