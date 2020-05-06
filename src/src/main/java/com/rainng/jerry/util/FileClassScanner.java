package com.rainng.jerry.util;

import java.io.File;
import java.util.ArrayList;

public class FileClassScanner extends AbstractClassScanner {
    @Override
    public Class<?>[] scan(Class<?> root) {
        String[] classNames = scanName(root);
        ArrayList<Class<?>> classList = new ArrayList<>(classNames.length);

        ClassLoader classLoader = root.getClassLoader();
        for (String className : classNames) {
            try {
                classList.add(classLoader.loadClass(className));
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        return classList.toArray(new Class<?>[0]);
    }

    @Override
    public String[] scanName(Class<?> root) {
        String basePath = root.getResource("/").toString().substring(6);
        String packagePath = root.getResource("").toString().substring(6);
        ArrayList<String> paths = findAllFiles(new ArrayList<>(), new File(packagePath));

        ArrayList<String> classNameList = new ArrayList<>();
        for (String path : paths) {
            if (path.endsWith(".class")) {
                String className = path
                        .replace(basePath, "")
                        .replace("/", ".")
                        .replace(".class", "");
                classNameList.add(className);
            }
        }

        return classNameList.toArray(new String[0]);
    }

    private ArrayList<String> findAllFiles(ArrayList<String> list, File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    list.add(f.getPath().replace("\\", "/"));
                } else {
                    findAllFiles(list, f);
                }
            }
        }

        return list;
    }
}
