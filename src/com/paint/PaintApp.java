package com.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Завершение программы при закрытии окна
        jFrame.setSize(1000, 700); // Устанавливаем размер окна
        jFrame.setLocationRelativeTo(null); // Окно будет по центру экрана

        // Добавляем обработчик закрытия окна
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing(() -> jFrame.dispose()); // Close window on proceed
            }
        });

    }

    // Метод для обработки закрытия окна с диалогом
    void handleWindowClosing(Runnable onProceed) {
        if (drawingPanel.getIsModified()) { // Check for unsaved changes
            int result = JOptionPane.showConfirmDialog(
                    jFrame, "You have unsaved changes. Do you want to save before continuing?",
                    "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                if (toolPanel.saveFile()) {
                    onProceed.run(); // Proceed if saving is successful
                    drawingPanel.setIsModified(false);
                }
            } else if (result == JOptionPane.NO_OPTION) {
                onProceed.run(); // Proceed without saving
                drawingPanel.setIsModified(false);
            }
            // If "Cancel" is chosen, do nothing, so we stay on the current file
        } else {
            onProceed.run(); // No modifications, proceed immediately
            drawingPanel.setIsModified(false);
        }
    }


    // Настройка панели рисования
    private void setupDrawingPanel() {
        drawingPanel = new DrawingPanel(); // Создаем экземпляр панели рисования
    }

    // Настройка панели инструментов
    private void setupToolPanel() {
        toolPanel = new ToolPanel(drawingPanel, this); // Pass the PaintApp instance
    }


    // Настройка компоновки панели инструментов и панели рисования в основном окне
    private void setupLayout() {
        JScrollPane scrollPane = new JScrollPane(drawingPanel);
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
