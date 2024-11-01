package com.paint;

import java.io.File;

public class FileInfoDisplay {
    public static String generateFileInfo(File selectedFile) {
        if (selectedFile.isDirectory()) {
            File[] files = selectedFile.listFiles();
            if (files != null) {
                StringBuilder fileList = new StringBuilder("Список файлов и папок:\n");
                for (File file : files) {
                    String fileName = file.getName();
                    if (FileUtils.isImageFile(fileName)) {
                        fileList.append(fileName).append(" (Изображение)\n");
                    } else {
                        fileList.append(fileName).append("\n");
                    }
                }
                return fileList.toString();
            } else {
                return "Папка пуста.";
            }
        } else {
            String fileName = selectedFile.getName();
            return FileUtils.isImageFile(fileName) ? fileName + " (Изображение)" : "Выбран файл: " + fileName;
        }
    }
}
