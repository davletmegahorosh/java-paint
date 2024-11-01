package com.paint;
public class FileUtils {
    public static boolean isImageFile(String fileName) {
        String fileNameLower = fileName.toLowerCase();
        return fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg") ||
                fileNameLower.endsWith(".png") || fileNameLower.endsWith(".gif") ||
                fileNameLower.endsWith(".svg");
    }
}
