//package com.test;
//
//import javax.swing.*;
//
//public class ImageEditor {
//    private JFrame jFrame;
//    private DrawManager drawManager;
//    private ImageEditorUI ui;
//    private ImageEditorListeners listeners;
//
//    public ImageEditor() {
//        this.ui = new ImageEditorUI(); // Создаем интерфейс
//        this.listeners = new ImageEditorListeners(this); // Создаем обработчики событий
//        setup();
//    }
//
//    private void setup() {
//        this.jFrame = ui.getFrame(); // Получаем окно из UI класса
//        this.drawManager = new DrawManager();
//        drawManager.setImageLabel(ui.getImageLabel()); // Устанавливаем Label для рисования
//        listeners.setupListeners(ui, drawManager); // Настраиваем слушатели с UI и DrawManager
//        jFrame.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        new ImageEditor();
//    }
//}
