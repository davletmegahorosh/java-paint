package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawingPanel extends JPanel {
    private BufferedImage canvasImage; // Изображение для холста, на котором будет рисование
    private Graphics2D g2; // Объект Graphics2D для рисования на холсте
    private Color currentColor = Color.BLACK; // Текущий цвет для рисования
    private Tool currentTool = Tool.BRUSH; // Текущий инструмент (кисть или ластик)
    private int brushSize = 5; // Размер кисти
    private int eraserSize = 10; // Размер ластика
    public boolean isModified = false;

    // Конструктор инициализирует размеры панели и цвет фона, а также добавляет обработчики мыши
    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600)); // Устанавливаем размер панели
        setBackground(Color.WHITE); // Устанавливаем белый цвет фона панели

        // Обработчик нажатий мыши, для начала рисования или стирания
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (g2 == null) { // Инициализация холста, если он ещё не создан
                    initializeCanvas();
                }
                useTool(e); // Используем выбранный инструмент в точке нажатия мыши
                setModified(); // Устанавливаем флаг изменений
            }
        });

        // Обработчик перетаскивания мыши для непрерывного рисования или стирания
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                useTool(e); // Используем инструмент, пока тянем мышь
                setModified(); // Устанавливаем флаг изменений
                repaint(); // Перерисовываем панель
            }
        });
    }

    // метод проверяет было ли изменение
    private void setModified() {
        boolean oldModified = this.isModified;
        this.isModified = true;
        firePropertyChange("canvasModified", oldModified, this.isModified); // Уведомление об изменении
    }

    // getter для переменной изменения
    public boolean getIsModified() {
        return isModified;
    }

    // Метод для получения текущего изображения холста
    public BufferedImage getCanvasImage() {
        return canvasImage;
    }

    // Метод для инициализации холста
    private void initializeCanvas() {
        if (canvasImage == null) { // Проверяем, создано ли изображение
            canvasImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            g2 = canvasImage.createGraphics(); // Получаем Graphics2D объект для рисования
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clearCanvas(); // Очищаем холст
        }
    }

    // Метод для отображения холста на панели
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvasImage == null) {
            initializeCanvas(); // Инициализируем холст, если он ещё не создан
        }
        g.drawImage(canvasImage, 0, 0, null); // Отображаем изображение холста
    }

    // Метод для использования текущего инструмента на основе события мыши
    private void useTool(MouseEvent e) {
        if (g2 == null) {
            initializeCanvas();
        }
        int x = e.getX();
        int y = e.getY();
        switch (currentTool) {
            case BRUSH:
                g2.setColor(currentColor); // Устанавливаем цвет кисти
                g2.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize); // Рисуем круг (кисть)
                break;
            case ERASER:
                g2.setColor(Color.WHITE); // Устанавливаем цвет ластика (белый)
                g2.fillRect(x - eraserSize / 2, y - eraserSize / 2, eraserSize, eraserSize); // Стираем прямоугольником
                break;
            default:
                break;
        }
    }

    // Метод для изменения текущего цвета рисования
    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    // Метод для изменения текущего инструмента
    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
    }

    // Метод для изменения размера кисти
    public void setBrushSize(int size) {
        this.brushSize = size;
    }

    // Метод для изменения размера ластика
    public void setEraserSize(int size) {
        this.eraserSize = size;
    }

    // Метод для очистки холста
    public void clearCanvas() {
        g2.setPaint(Color.WHITE); // Устанавливаем цвет для очистки
        g2.fillRect(0, 0, getWidth(), getHeight()); // Заполняем весь холст белым цветом
        g2.setPaint(Color.BLACK); // Возвращаем цвет по умолчанию
        repaint(); // Перерисовываем панель
    }

    // Метод для открытия изображения из файла
    public void openImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file); // Читаем изображение из файла
            canvasImage = resizeImageToFitCanvas(image); // Изменяем размер изображения, чтобы оно подошло под холст
            g2 = canvasImage.createGraphics(); // Обновляем Graphics2D для нового изображения
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Метод для изменения размера изображения, чтобы оно подходило под размеры холста
    private BufferedImage resizeImageToFitCanvas(BufferedImage image) {
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        double scale = Math.min((double) canvasWidth / image.getWidth(), (double) canvasHeight / image.getHeight());
        int newWidth = (int) (image.getWidth() * scale);
        int newHeight = (int) (image.getHeight() * scale);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null); // Рисуем изображение в новом размере
        g2d.dispose();
        return resizedImage;
    }
}
