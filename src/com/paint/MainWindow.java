package com.paint;

import javax.swing.*;

public class MainWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageEditor());
    }
}
