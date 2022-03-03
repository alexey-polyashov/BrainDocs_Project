package com.braindocs.common;

import org.springframework.stereotype.Component;

@Component
public class Utils {
    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName){
        if (enumName == null) {
            return false;
        } else {
            try {
                Enum.valueOf(enumClass, enumName);
                return true;
            } catch (IllegalArgumentException var4) {
                return false;
            }
        }
    }
}
