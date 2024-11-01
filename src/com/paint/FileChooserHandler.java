package com.paint;
import javax.swing.*;
import java.io.File;

public class FileChooserHandler {
    public void openFileChooser(FileSelectionPanel panel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileInfo = FileInfoDisplay.generateFileInfo(selectedFile);
            panel.displayFileInfo(fileInfo);
        }
    }
}
