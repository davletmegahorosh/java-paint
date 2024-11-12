package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class ToolPanel extends JPanel {
    private final DrawingPanel drawingPanel; // Панель для рисования
    private final PaintApp paintApp; // Ссылка на основное приложение
    private final JFileChooser fileChooser; // Диалог для выбора файла
    private JSlider brushSizeSlider; // Ползунок для размера кисти
    private JSlider eraserSizeSlider; // Ползунок для размера ластика

    public ToolPanel(DrawingPanel drawingPanel, PaintApp paintApp) {
        this.drawingPanel = drawingPanel;
        this.paintApp = paintApp; // Сохраняем ссылку на приложение
        fileChooser = new JFileChooser();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Устанавливаем отступы
        add(createFileOperationsPanel()); // Панель операций с файлами
        add(Box.createVerticalStrut(20)); // Пробел между панелями
        add(createColorPickerPanel()); // Панель выбора цвета
        add(createZoomPanel()); // Панель управления масштабом
        add(createToolSelectionPanel()); // Панель выбора инструмента
        add(createBrushSizeSlider()); // Панель для ползунка кисти
        add(createEraserSizeSlider()); // Панель для ползунка ластика
    }

    // Создает панель выбора инструмента
    private JPanel createToolSelectionPanel() {
        JPanel toolPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton brushButton = new JButton("Brush"); // Кнопка "Кисть"
        brushButton.addActionListener(e -> drawingPanel.setCurrentTool(Tool.BRUSH));
        JButton eraserButton = new JButton("Eraser"); // Кнопка "Ластик"
        eraserButton.addActionListener(e -> drawingPanel.setCurrentTool(Tool.ERASER));
        JButton fillButton = new JButton("Fill"); // Кнопка "Заливка"
        fillButton.addActionListener(e -> drawingPanel.setCurrentTool(Tool.FILL));

        toolPanel.add(brushButton);
        toolPanel.add(eraserButton);
        toolPanel.add(fillButton);
        return toolPanel;
    }

    // Создает ползунок для настройки размера кисти
    private JPanel createBrushSizeSlider() {
        JPanel brushPanel = new JPanel(new BorderLayout());
        JLabel brushLabel = new JLabel("Brush Size"); // Метка "Размер кисти"
        brushSizeSlider = new JSlider(1, 20, 5); // Ползунок с диапазоном от 1 до 20, начальное значение 5
        brushSizeSlider.addChangeListener(e -> drawingPanel.setBrushSize(brushSizeSlider.getValue()));

        brushPanel.add(brushLabel, BorderLayout.NORTH);
        brushPanel.add(brushSizeSlider, BorderLayout.CENTER);
        return brushPanel;
    }

    // Создает ползунок для настройки размера ластика
    private JPanel createEraserSizeSlider() {
        JPanel eraserPanel = new JPanel(new BorderLayout());
        JLabel eraserLabel = new JLabel("Eraser Size"); // Метка "Размер ластика"
        eraserSizeSlider = new JSlider(1, 20, 10); // Ползунок с диапазоном от 1 до 20, начальное значение 10
        eraserSizeSlider.addChangeListener(e -> drawingPanel.setEraserSize(eraserSizeSlider.getValue()));

        eraserPanel.add(eraserLabel, BorderLayout.NORTH);
        eraserPanel.add(eraserSizeSlider, BorderLayout.CENTER);
        return eraserPanel;
    }

    // Создает панель для операций с файлами (создание, открытие, сохранение)
    private JPanel createFileOperationsPanel() {
        JPanel filePanel = new JPanel(new GridLayout(3, 1, 5, 5)); // Панель с сеткой из 3 строк и 1 столбца

        JButton newFileButton = new JButton("New File"); // Кнопка "Новый файл"
        newFileButton.addActionListener(e -> {
            paintApp.handleWindowClosing(drawingPanel::clearCanvas); // Очищает холст при нажатии
        });

        filePanel.add(newFileButton);

        JButton openFileButton = new JButton("Open"); // Кнопка "Открыть"
        openFileButton.addActionListener(e -> {
            paintApp.handleWindowClosing(this::openFile); // Открывает файл
        });

        filePanel.add(openFileButton);

        JButton saveFileButton = new JButton("Save"); // Кнопка "Сохранить"
        saveFileButton.addActionListener(e -> saveFile()); // Сохраняет файл при нажатии
        filePanel.add(saveFileButton);

        return filePanel;
    }

    // Создает панель для управления масштабом
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
        fileChooser.setSelectedFile(new File("MyDrawing.jpg")); // Имя файла по умолчанию

        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            fileToSave = checkFileExistsAndAddIndex(fileToSave); // Проверка уникальности имени файла

            try {
                ImageIO.write(drawingPanel.getCanvasImage(), "jpg", fileToSave); // Сохраняем изображение
                return true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении файла: " + ex.getMessage());
            }
        }
        return false;
    }

    // Проверяет, существует ли файл, и добавляет индекс, если он существует
    private File checkFileExistsAndAddIndex(File file) {
        String filePath = file.getAbsolutePath();
        String fileName = file.getName();
        String fileExtension = "";
        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            fileExtension = fileName.substring(lastDotIndex); // Получаем расширение, например ".jpg"
            fileName = fileName.substring(0, lastDotIndex); // Имя файла без расширения
        }

        int index = 1;
        File newFile = file;

        // Если файл существует, добавляем индекс к имени
        while (newFile.exists()) {
            newFile = new File(file.getParent(), fileName + "(" + index + ")" + fileExtension);
            index++;
        }

        return newFile; // Возвращаем уникальный файл
    }
}
