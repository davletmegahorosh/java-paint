package com.paint;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;





public class main {
    private JButton button1;
    private JButton openWhiteCanvaButton;
    private JPanel panel1;
    private JLabel imageLabel;
    private JFrame jFrame;
    private JButton saveButton;

    private int lastX = -1;
    private int lastY = -1;

    private BufferedImage bufferedImage;

    private Color currentColor = Color.BLACK;

    private ImageIcon createColorIcon(Color color) {
        // Создаем изображение 30x30 пикселей с заданным цветом
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, 30, 30); // Заполняем прямоугольник цветом
        g2d.dispose();
        return new ImageIcon(image);
    }

    public main() {
        panel1 = new JPanel(new BorderLayout());

        button1 = new JButton("Выбрать файл");
        openWhiteCanvaButton = new JButton("Открыть новый файл");
        saveButton = new JButton("Сохранить файл");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        buttonPanel.add(openWhiteCanvaButton);
        buttonPanel.add(saveButton);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout());

        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setIcon(createColorIcon(color)); // Устанавливаем иконку
            colorButton.addActionListener(e -> currentColor = color);
            colorPanel.add(colorButton);
        }

        buttonPanel.add(colorPanel);
        panel1.add(buttonPanel, BorderLayout.SOUTH);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(imageLabel, BorderLayout.CENTER);

        this.jFrame = getJFrame();
        this.jFrame.setContentPane(panel1);
        this.jFrame.setVisible(true);

        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (bufferedImage != null) {
                    updateImageDisplay();
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();

                    if (isImageFile(fileName)) {
                        showImageInMainFrame(selectedFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Выбран файл не является изображением: " + fileName);
                    }
                }
            }
        });


        openWhiteCanvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new whiteCanva(); // Открыть новое окно с пустым холстом
            }
        });



        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Вызываем метод для сохранения изображения
                SaveImage.save(bufferedImage, jFrame);
            }
        });

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
    }

    private boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") ||
                fileNameLower.endsWith(".png") || fileNameLower.endsWith(".gif") ||
                fileNameLower.endsWith(".svg");
    }

    private void loadImage(File imageFile) {
        ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
        Image originalImage = imageIcon.getImage();

        // Создаем BufferedImage на основе размеров imageLabel после отображения
        bufferedImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(originalImage.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        // Устанавливаем BufferedImage в imageLabel
        imageLabel.setIcon(new ImageIcon(bufferedImage));
        jFrame.revalidate();
        jFrame.repaint();
    }

    private void draw(int x, int y) {
        if (bufferedImage != null) {
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setColor(currentColor);
            g2d.setStroke(new BasicStroke(5)); // Толщина линии

            if (lastX != -1 && lastY != -1) {
                g2d.drawLine(lastX, lastY, x, y); // Соединяем точки линией
            } else {
                g2d.fillOval(x - 2, y - 2, 5, 5); // Если точка одна, рисуем маленький круг
            }

            g2d.dispose();
            imageLabel.repaint();

            // Обновляем координаты последней точки
            lastX = x;
            lastY = y;
        }
    }

    private void showImageInMainFrame(File imageFile) {
        try {
            // Загружаем изображение из файла
            BufferedImage originalImage = javax.imageio.ImageIO.read(imageFile);

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            float aspectRatio = (float) width / height;

            int displayWidth = panel1.getWidth();
            int displayHeight = panel1.getHeight() - button1.getHeight();

            if (displayWidth / aspectRatio < displayHeight) {
                displayHeight = (int) (displayWidth / aspectRatio);
            } else {
                displayWidth = (int) (displayHeight * aspectRatio);
            }

            bufferedImage = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(originalImage.getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(bufferedImage));
            jFrame.revalidate();
            jFrame.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(jFrame, "Не удалось загрузить изображение.");
        }
    }


    private void updateImageDisplay() {
        imageLabel.setIcon(new ImageIcon(bufferedImage));
    }

    private JFrame getJFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(450, 350, 800, 600);
        jFrame.setTitle("Megapushka");
        return jFrame;
    }

    public static void main(String[] args) {
        new main();
    }

}

