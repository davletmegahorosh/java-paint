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
    private BufferedImage canvasImage; // Изображение для рисования
    private Graphics2D g2; // Графический объект для рисования на canvasImage
    private Color currentColor = Color.BLACK; // Текущий цвет кисти
    private Tool currentTool = Tool.BRUSH; // Текущий инструмент (по умолчанию кисть)
    private int brushSize = 5; // Размер кисти
    private int eraserSize = 10; // Размер ластика
    public boolean isModified = false; // Флаг для отслеживания изменений на холсте
    private double zoomLevel = 1.0; // Уровень масштабирования
    private final double zoomIncrement = 0.1; // Шаг масштабирования
    private int panX = 0, panY = 0; // Координаты сдвига (панорамирования)
    private int lastMouseX, lastMouseY; // Последние координаты мыши для панорамирования
    private boolean isPanning = false; // Флаг для отслеживания панорамирования
    private Point lastPoint = null; // Точка для отслеживания последней позиции при рисовании

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
    public void zoomIn() {
        zoomLevel += zoomIncrement;
        adjustPanForZoom();
        repaint();
    }

    // Метод для уменьшения масштаба
    public void zoomOut() {
        zoomLevel = Math.max(zoomLevel - zoomIncrement, zoomIncrement);
        adjustPanForZoom();
        repaint();
    }

    // Метод для корректировки сдвига после масштабирования
    private void adjustPanForZoom() {
        panX = (int) ((getWidth() / 2 - (getWidth() / 2 - panX) * zoomLevel));
        panY = (int) ((getHeight() / 2 - (getHeight() / 2 - panY) * zoomLevel));
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
        int x = (int) ((e.getX() - panX) / zoomLevel);
        int y = (int) ((e.getY() - panY) / zoomLevel);

        switch (currentTool) {
            case BRUSH:
                g2.setColor(currentColor);
                if (lastPoint != null) {
                    int lastX = (int) ((lastPoint.x - panX) / zoomLevel);
                    int lastY = (int) ((lastPoint.y - panY) / zoomLevel);
                    g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(lastX, lastY, x, y);
                } else {
                    g2.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
                }
                lastPoint = e.getPoint();
                break;
            case ERASER:
                g2.setColor(Color.WHITE);
                g2.fillRect(x - eraserSize / 2, y - eraserSize / 2, eraserSize, eraserSize);
                break;
            default:
                break;
        }
        repaint();
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
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setPaint(Color.BLACK);
        repaint();
    }

    // Открывает изображение из файла и подгоняет его под размеры холста
    public void openImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            canvasImage = resizeImageToFitCanvas(image);
            g2 = canvasImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
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
