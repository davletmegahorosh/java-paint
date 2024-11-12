package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class ToolPanel extends JPanel {
    private final DrawingPanel drawingPanel;
    private final PaintApp paintApp; // Reference to PaintApp
    private final JFileChooser fileChooser;

    public ToolPanel(DrawingPanel drawingPanel, PaintApp paintApp) {
        this.drawingPanel = drawingPanel;
        this.paintApp = paintApp; // Store the reference to PaintApp
        fileChooser = new JFileChooser();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(createFileOperationsPanel());
        add(Box.createVerticalStrut(20));
        add(createColorPickerPanel());
        add(createZoomPanel());

    }



    // Создает панель для операций с файлами (создание, открытие, сохранение)
    private JPanel createFileOperationsPanel() {
        JPanel filePanel = new JPanel(new GridLayout(3, 1, 5, 5)); // Панель с сеткой из 3 строк и 1 столбца

        JButton newFileButton = new JButton("New File"); // Кнопка "Новый файл"
        newFileButton.addActionListener(e -> {
            paintApp.handleWindowClosing(drawingPanel::clearCanvas); // Pass clearCanvas as the action to proceed
        }); // Очищает холст при нажатии


        filePanel.add(newFileButton);

        JButton openFileButton = new JButton("Open"); // Кнопка "Открыть"
        openFileButton.addActionListener(e -> {
            paintApp.handleWindowClosing(this::openFile); // Pass openFile as the action to proceed
        });


        filePanel.add(openFileButton);

        JButton saveFileButton = new JButton("Save"); // Кнопка "Сохранить"
        saveFileButton.addActionListener(e -> saveFile()); // Сохраняет файл при нажатии
        filePanel.add(saveFileButton);

        return filePanel;
    }


    // Панель для увеличения, уменьшения
    private JPanel createZoomPanel(){
        JPanel zoomPanel = new JPanel(new GridLayout(1,2,5,5)); // Панель управления масштабированием
        JButton zoomInButton = new JButton("+");
        zoomInButton.addActionListener(e -> drawingPanel.zoomIn()); // Увеличение
        JButton zoomOutButton = new JButton("-");
        zoomOutButton.addActionListener(e -> drawingPanel.zoomOut()); // Уменьшение

        zoomPanel.add(zoomInButton);
        zoomPanel.add(zoomOutButton);
        return zoomPanel;
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
    public boolean saveFile() {
        JFileChooser fileChooser = new JFileChooser();

        // Установка имени файла по умолчанию
        fileChooser.setSelectedFile(new File("MyDrawing.jpg"));

        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Проверяем, существует ли файл с таким именем, и если да, добавляем индекс
            fileToSave = checkFileExistsAndAddIndex(fileToSave);

            try {
                // Сохранение изображения в файл
                ImageIO.write(drawingPanel.getCanvasImage(), "jpg", fileToSave);
                return true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
        return false;
    }

    // Метод для проверки, существует ли файл, и добавления индекса, если файл существует
    private File checkFileExistsAndAddIndex(File file) {
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();
        String fileExtension = "";
        int lastDotIndex = fileName.lastIndexOf('.');

        // Извлекаем расширение файла, если оно есть
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            fileExtension = fileName.substring(lastDotIndex); // ".jpg", ".png", и т.д.
            fileName = fileName.substring(0, lastDotIndex); // Имя файла без расширения
        }

        int index = 1;
        File newFile = file;

        // Пока файл существует, добавляем индекс к имени
        while (newFile.exists()) {
            newFile = new File(file.getParent(), fileName + "(" + index + ")" + fileExtension);
            index++;
        }

        return newFile; // Возвращаем уникальный файл
    }
}
