package com.braindocs.common;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class Utils {

    public static final Collection EMPTY_COLLECTION = new ArrayList();

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
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

    public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
        return collection == null ? EMPTY_COLLECTION : collection;
    }
}
