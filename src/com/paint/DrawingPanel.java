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
    private double zoomLevel = 1.0; // Начальный уровень увеличения
    private final double zoomIncrement = 0.1; //Коэффецент повышения
    private int panX = 0, panY = 0; // Значения смещения для панорамирования
    private int lastMouseX, lastMouseY; // Чтобы отслеживать последнее положение мыши во время перетаскивания
    private boolean isPanning = false; // Отслеживание, выполняет ли пользователь панорамирование в данный момент

    // Конструктор инициализирует размеры панели и цвет фона, а также добавляет обработчики мыши
    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600)); // Устанавливаем размер панели
        setBackground(Color.WHITE); // Устанавливаем белый цвет фона панели

        // Обработчик нажатий мыши, для начала рисования или стирания
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) { // Щелк правой кнопкой мыши, чтобы начать панорамирование
                    isPanning = true;
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                } else {
                    useTool(e); // Используйте кисть или ластик, щелкнув левой кнопкой мыши.
                    setModified();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPanning = false; // Остановка панорамирования при отпускании мыши
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPanning) {
                    int deltaX = e.getX() - lastMouseX;
                    int deltaY = e.getY() - lastMouseY;
                    panX += deltaX;
                    panY += deltaY;
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                    repaint();
                } else {
                    useTool(e);
                    setModified();
                    repaint();
                }
            }
        });
    }

    public void zoomIn (){
        zoomLevel += zoomIncrement;
        adjustPanForZoom();
        repaint();
    }

    public void zoomOut (){
        zoomLevel = Math.max(zoomLevel-zoomIncrement, zoomIncrement); // Предотвращение слишком низкого уровня масштабирования
        adjustPanForZoom();
        repaint();
    }

    // Регулировка смещения панорамирования в зависимости от уровня масштабирования, чтобы сохранить вид по центру.
    private void adjustPanForZoom() {
        panX = (int) ((getWidth() / 2 - (getWidth() / 2 - panX) * zoomLevel));
        panY = (int) ((getHeight() / 2 - (getHeight() / 2 - panY) * zoomLevel));
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
    // setter для переменной изменения
    public void setIsModified(boolean value) {
        this.isModified = value;

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
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(panX, panY); // Apply panning
        g2d.scale(zoomLevel, zoomLevel); // Apply zooming
        g2d.drawImage(canvasImage, 0, 0, null); // Draw the transformed image
        g2d.dispose();
    }


    // Метод для использования текущего инструмента на основе события мыши
    private void useTool(MouseEvent e) {
        if (g2 == null) {
            initializeCanvas();
        }
        // Adjust mouse coordinates based on zoom and pan
        int x = (int) ((e.getX() - panX) / zoomLevel);
        int y = (int) ((e.getY() - panY) / zoomLevel);

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
        repaint();
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
