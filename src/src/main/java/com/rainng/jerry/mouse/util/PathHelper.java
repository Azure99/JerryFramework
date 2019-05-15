package com.rainng.jerry.mouse.util;

public class PathHelper {
    public static String getExtension(String path) {
        int pos = path.lastIndexOf(".");
        if (pos != -1) {
            return path.substring(pos + 1);
        }

        return "";
    }
}
