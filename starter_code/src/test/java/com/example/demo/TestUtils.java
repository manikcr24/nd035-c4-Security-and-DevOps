package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void inject(Object target, String field, Object toInject) {
        boolean wasPrivate = false;
        try {
            Field declaredField = target.getClass().getDeclaredField(field);
            if(!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
                wasPrivate = true;
            }

            declaredField.set(target, toInject);
            if(wasPrivate) {
                declaredField.setAccessible(true);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
