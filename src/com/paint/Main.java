package com.paint;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaintApp::new);  // Запуск приложения
    }
}
