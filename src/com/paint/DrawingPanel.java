package com.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class DrawingPanel extends JPanel {
    private BufferedImage canvasImage; // Изображение для рисования
    private Graphics2D g2; // Графический объект для рисования на canvasImage
    private Color currentColor = Color.BLACK; // Текущий цвет кисти
    private Tool currentTool = Tool.BRUSH; // Текущий инструмент (по умолчанию кисть)
    private int brushSize = 5; // Размер кисти
    private int eraserSize = 10; // Размер ластика
    public boolean isModified = false; // Флаг для отслеживания изменений на холсте
    private double zoomLevel = 1.0; // Уровень масштабирования
    private final double zoomIncrement = 0.1; // Шаг масштабирования
    private final double minZoomLevel = 0.5; // Минимальный уровень масштабирования
    private final double maxZoomLevel = 2.1; // Максимальный уровень масштабирования
    private int panX = 0, panY = 0; // Координаты сдвига (панорамирования)
    private int lastMouseX, lastMouseY; // Последние координаты мыши для панорамирования
    private boolean isPanning = false; // Флаг для отслеживания панорамирования
    private Point lastPoint = null; // Точка для отслеживания последней позиции при рисовании
    private BufferedImage backgroundImage; // Основное изображение
    private boolean canvasOpened = true;


    // Конструктор панели для рисования
    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600)); // Устанавливаем размер панели
        setBackground(Color.WHITE); // Устанавливаем фон панели в белый цвет

        // Добавляем слушатель событий для обработки нажатий мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Проверяем, используется ли правая кнопка мыши для панорамирования
                if (SwingUtilities.isRightMouseButton(e)) {
                    isPanning = true; // Включаем режим панорамирования
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                } else {
                    lastPoint = e.getPoint();
                    useTool(e); // Используем инструмент для рисования
                    setModified(); // Устанавливаем флаг изменений
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPanning = false; // Отключаем панорамирование
                lastPoint = null; // Сбрасываем последнюю точку
            }
        });

        // Добавляем слушатель событий для обработки перетаскивания мыши
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPanning) {
                    // Если включено панорамирование, сдвигаем холст
                    int deltaX = e.getX() - lastMouseX;
                    int deltaY = e.getY() - lastMouseY;
                    panX += deltaX;
                    panY += deltaY;
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                    repaint(); // Перерисовываем панель
                } else {
                    useTool(e); // Используем инструмент для рисования
                    setModified(); // Устанавливаем флаг изменений
                    repaint();
                }
            }
        });
    }

    // Метод для увеличения масштаба
// Метод для увеличения масштаба
    public void zoomIn() {
        zoomLevel = Math.min(zoomLevel + zoomIncrement, maxZoomLevel); // Ограничиваем максимальный масштаб
        adjustPanForZoom();
        repaint();
    }

    // Метод для уменьшения масштаба
    public void zoomOut() {
        zoomLevel = Math.max(zoomLevel - zoomIncrement, minZoomLevel); // Ограничиваем минимальный масштаб
        adjustPanForZoom();
        repaint();
    }

    private void adjustPanForZoom() {
        // Пересчитываем панорамирование, чтобы картинка оставалась по центру
        panX = (int) ((getWidth() - (getWidth() / zoomLevel)) / 2);
        panY = (int) ((getHeight() - (getHeight() / zoomLevel)) / 2);

        // Убедимся, что изображение не выходит за пределы
        panX = Math.max(panX, 0); // Ограничиваем движение влево
        panY = Math.max(panY, 0); // Ограничиваем движение вверх
    }

    // Устанавливает флаг "изменено" и уведомляет слушателей
    private void setModified() {
        boolean oldModified = this.isModified;
        this.isModified = true;
        firePropertyChange("canvasModified", oldModified, this.isModified);
    }

    // Инициализирует холст, если он еще не создан
    private void initializeCanvas() {
        if (canvasImage == null) {
            canvasImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            g2 = canvasImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clearCanvas(); // Очистка холста
        }
    }

    // Возвращает текущее изображение холста
    public BufferedImage getCanvasImage(){
        return canvasImage;
    }

    // Получает состояние флага "изменено"
    public boolean getIsModified() {
        return isModified;
    }

    // Устанавливает флаг "изменено"
    public void setIsModified(boolean modified) {
        this.isModified = modified;
    }

    // Метод для отрисовки компонентов на панели
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvasImage == null) {
            initializeCanvas(); // Инициализация холста, если он не создан
        }
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(panX, panY); // Применение панорамирования
        g2d.scale(zoomLevel, zoomLevel); // Применение масштабирования
        g2d.drawImage(canvasImage, 0, 0, null); // Отрисовка холста
        g2d.dispose();
    }

    // Метод для применения инструмента в зависимости от текущих настроек
    private void useTool(MouseEvent e) {
        if (g2 == null) {
            initializeCanvas();
        }

        // Adjust coordinates according to pan and zoom
        int x = (int) ((e.getX() - panX) / zoomLevel);
        int y = (int) ((e.getY() - panY) / zoomLevel);

        switch (currentTool) {
            case BRUSH:
                g2.setColor(currentColor);
                if (lastPoint != null) {
                    int lastX = (int) ((lastPoint.x - panX) / zoomLevel);
                    int lastY = (int) ((lastPoint.y - panY) / zoomLevel);
                    g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(lastX, lastY, x, y);  // Рисуем линию от последней точки до текущей точки
                } else {
                    g2.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
                }
                lastPoint = e.getPoint();
                break;

            case ERASER:
                if (canvasOpened) {
                    // Ластик закрашивает белым цветом, если холст пустой (без загруженного изображения)
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x - eraserSize / 2, y - eraserSize / 2, eraserSize, eraserSize);
                } else if (backgroundImage != null) {
                    // Ластик восстанавливает пиксели из фонового изображения
                    for (int dx = -eraserSize / 2; dx <= eraserSize / 2; dx++) {
                        for (int dy = -eraserSize / 2; dy <= eraserSize / 2; dy++) {
                            int px = x + dx;
                            int py = y + dy;
                            if (px >= 0 && py >= 0 && px < backgroundImage.getWidth() && py < backgroundImage.getHeight()) {
                                canvasImage.setRGB(px, py, backgroundImage.getRGB(px, py));
                            }
                        }
                    }
                }
                break;

        }
        repaint();
    }

    // Реализация алгоритма заливки (Flood Fill)
    private void floodFill(int x, int y, Color fillColor) {
        int targetColor = canvasImage.getRGB(x, y);
        if (targetColor != fillColor.getRGB()) {
            fillArea(x, y, targetColor, fillColor.getRGB());
        }
    }

    private void fillArea(int x, int y, int targetColor, int fillColor) {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point point = stack.pop();
            int currentX = point.x;
            int currentY = point.y;

            if (currentX < 0 || currentX >= canvasImage.getWidth() || currentY < 0 || currentY >= canvasImage.getHeight()) {
                continue;
            }
            if (canvasImage.getRGB(currentX, currentY) == targetColor) {
                canvasImage.setRGB(currentX, currentY, fillColor);
                stack.push(new Point(currentX + 1, currentY));
                stack.push(new Point(currentX - 1, currentY));
                stack.push(new Point(currentX, currentY + 1));
                stack.push(new Point(currentX, currentY - 1));
            }
        }
    }

    // Метод для установки текущего цвета кисти
    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    // Метод для установки текущего инструмента
    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
    }

    // Метод для установки размера кисти
    public void setBrushSize(int size) {
        this.brushSize = size;
    }

    // Метод для установки размера ластика
    public void setEraserSize(int size) {
        this.eraserSize = size;
    }

    // Очищает холст
    public void clearCanvas() {
        backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        canvasImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        g2 = canvasImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        repaint();
        isModified = false;
        canvasOpened = true;
    }



    // Открывает изображение из файла и подгоняет его под размеры холста
    public void openImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            backgroundImage = resizeImageToFitCanvas(image); // Сохраняем копию основного изображения
            canvasImage = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            g2 = canvasImage.createGraphics();
            g2.drawImage(backgroundImage, 0, 0, null); // Копируем основное изображение на холст
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
            canvasOpened = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // Изменяет размер изображения для подгонки под размеры холста
    private BufferedImage resizeImageToFitCanvas(BufferedImage image) {
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        double scale = Math.min((double) canvasWidth / image.getWidth(), (double) canvasHeight / image.getHeight());
        int newWidth = (int) (image.getWidth() * scale);
        int newHeight = (int) (image.getHeight() * scale);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return resizedImage;
    }
}
