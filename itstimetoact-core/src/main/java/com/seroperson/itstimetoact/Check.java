package com.seroperson.itstimetoact;

import android.text.TextUtils;

public class Check {

    public static void checkArgumentNotNull(Object object, String message) {
        if(checkIsNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkArgumentNotEmpty(String object, String message) {
        if(checkIsEmpty(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean checkIsEmpty(String object) {
        return TextUtils.isEmpty(object);
    }

    public static boolean checkIsNull(Object object) {
        return object == null;
    }

}
