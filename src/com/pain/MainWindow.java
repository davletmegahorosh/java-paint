package com.pain;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow {
    private JFrame jFrame;
    private DrawManager drawManager;
    private JLabel imageLabel;
    private boolean newImagePending = false;
    private ImageActions imageActions;
    private ImagePanel imagePanel;

    public MainWindow() {
        drawManager = new DrawManager();
        imageActions = new ImageActions(drawManager, jFrame);

        imagePanel = new ImagePanel(jFrame, drawManager, imageActions);


        ActionListener fileButtonListener = e -> {
            newImagePending = true;
            imageActions.checkForUnsavedChanges(newImagePending, imageLabel);
        };

        ActionListener saveButtonListener = e -> imageActions.saveImageDialog(imageLabel);

        ActionListener newCanvasButtonListener = e -> imageActions.openNewCanvas(imageLabel);

        Panels_Buttons panelsButtons = new Panels_Buttons(fileButtonListener, saveButtonListener, newCanvasButtonListener, drawManager);

        jFrame = new JFrame("Megapushka");
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.setContentPane(panelsButtons.getPanel());
        jFrame.setVisible(true);

        // Set up drawManager and imageActions
        imageLabel = panelsButtons.getImageLabel();
        drawManager.setImageLabel(imageLabel);
        drawManager.setStrokeSize(panelsButtons.getSizeSlider().getValue());

        imageActions = new ImageActions(drawManager, jFrame);

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                newImagePending = false;
                imageActions.checkForUnsavedChanges(newImagePending, imageLabel);
            }
        });
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
