package com.rainng.jerry.util;

import java.io.File;
import java.util.ArrayList;

public class FileClassScanner extends AbstractClassScanner {
    @Override
    public Class<?>[] scanClasses(Class<?> root) {
        String[] classNames = scanClassNames(root);
        ArrayList<Class<?>> classList = new ArrayList<>(classNames.length);

        ClassLoader classLoader = root.getClassLoader();
        for (String className : classNames) {
            try {
                classList.add(classLoader.loadClass(className));
            } catch (ClassNotFoundException ex) {
                Logger.ex("Class not found", ex);
            }
        }

        return classList.toArray(new Class<?>[0]);
    }

    @Override
    public String[] scanClassNames(Class<?> root) {
        String basePath = root.getResource("/").getPath();
        String packagePath = root.getResource("").getPath();
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
