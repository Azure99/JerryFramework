package com.rainng.jerry.mvc.mapping;

import com.rainng.jerry.mvc.Controller;
import com.rainng.jerry.util.FileClassScanner;

import java.util.ArrayList;

public class ControllerScanner {
    private ControllerScanner() {

    }

    public static Class<?>[] scan(Class<?> app) {
        Class<?>[] classes = new FileClassScanner().scanClasses(app);
        ArrayList<Class<?>> controllerList = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Class<?> nowClazz = clazz;
            while (nowClazz != null) {
                if (Controller.class.equals(nowClazz.getSuperclass())) {
                    controllerList.add(clazz);
                }
                nowClazz = nowClazz.getSuperclass();
            }
        }

        return controllerList.toArray(new Class[0]);
    }
}
