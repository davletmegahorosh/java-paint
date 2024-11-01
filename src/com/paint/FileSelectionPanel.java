package com.paint;

import javax.swing.*;
import java.awt.*;

public class FileSelectionPanel {
    private JButton fileSelectButton;
    private JPanel mainPanel;
    private JFrame jFrame;

    public FileSelectionPanel() {
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new GridBagLayout());
        fileSelectButton = new JButton("Выбрать файл");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(fileSelectButton, gbc);

        fileSelectButton.addActionListener(e -> new FileChooserHandler().openFileChooser(this));
    }

    public void showFrame() {
        jFrame = new JFrame("Megapushka");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(450, 350, 500, 300);
        jFrame.setContentPane(mainPanel);
        jFrame.setVisible(true);
    }

    public void displayFileInfo(String fileInfo) {
        JOptionPane.showMessageDialog(null, fileInfo);
    }
}
