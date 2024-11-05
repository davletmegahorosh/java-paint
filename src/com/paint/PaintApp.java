package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PaintApp {
    private JFrame jFrame; // Основное окно приложения
    private JPanel mainPanel; // Основная панель, содержащая инструментальную панель и панель рисования
    private DrawingPanel drawingPanel; // Панель для рисования
    private ToolPanel toolPanel; // Панель инструментов
    private File currentFile; // Текущий файл для сохранения/открытия изображения

    // Метод для получения изображения с холста
    public BufferedImage getCanvasImage() {
        return drawingPanel.getCanvasImage();
    }

    // Конструктор приложения PaintApp, инициализирует компоненты
    public PaintApp() {
        setupMainFrame(); // Настройка основного окна
        setupDrawingPanel(); // Настройка панели рисования
        setupToolPanel(); // Настройка панели инструментов
        setupLayout(); // Настройка компоновки панелей
    }

    // Настройка основного окна (JFrame)
    private void setupMainFrame() {
        jFrame = new JFrame("Advanced Paint App"); // Создаем окно с заголовком
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Завершение программы при закрытии окна
        jFrame.setSize(1000, 700); // Устанавливаем размер окна
        jFrame.setLocationRelativeTo(null); // Окно будет по центру экрана
    }

    // Настройка панели рисования
    private void setupDrawingPanel() {
        drawingPanel = new DrawingPanel(); // Создаем экземпляр панели рисования
    }

    // Настройка панели инструментов
    private void setupToolPanel() {
        toolPanel = new ToolPanel(drawingPanel); // Создаем панель инструментов и связываем её с панелью рисования
    }

    // Настройка компоновки панели инструментов и панели рисования в основном окне
    private void setupLayout() {
        mainPanel = new JPanel(new BorderLayout()); // Основная панель с компоновкой BorderLayout
        mainPanel.add(toolPanel, BorderLayout.WEST); // Добавляем панель инструментов слева
        mainPanel.add(drawingPanel, BorderLayout.CENTER); // Добавляем панель рисования в центр
        jFrame.add(mainPanel); // Добавляем основную панель в окно
        jFrame.setVisible(true); // Отображаем окно
    }

    // Основной метод запуска приложения
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaintApp::new); // Запуск приложения в потоке GUI
    }
}
