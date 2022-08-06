package com.hakan.core.utils;

import com.hakan.core.HCore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * General utils class for hCore.
 */
@SuppressWarnings({"unchecked"})
public final class ReflectionUtils {

    /**
     * Creates new instance of the given class.
     *
     * @param path Path of the class.
     * @param <T>  Type.
     * @return New instance of created class.
     */
    @Nonnull
    public static <T> T newInstance(@Nonnull String path) {
        return newInstance(path, new Class[0], new Object[0]);
    }

    /**
     * Creates new instance of given class.
     *
     * @param path    Path of class.
     * @param classes Classes to be used in constructor.
     * @param objects Objects to be used in constructor.
     * @param <T>     Type of class.
     * @return New instance of created class.
     */
    @Nonnull
    public static <T> T newInstance(@Nonnull String path, @Nonnull Class<?>[] classes, @Nonnull Object[] objects) {
        try {
            Validate.notNull(path, "path cannot be null!");
            Validate.notNull(classes, "classes cannot be null!");
            Validate.notNull(objects, "objects cannot be null!");

            Class<T> tClass = null;
            for (String version : HCore.getProtocolVersion().getKeys()) {
                try {
                    tClass = (Class<T>) Class.forName(path.replace("%s", version));
                    break;
                } catch (Exception ignored) {

                }
            }

            if (tClass == null)
                throw new NullPointerException("could not find class for path (" + path + ")");

            Constructor<T> constructor = tClass.getDeclaredConstructor(classes);
            constructor.setAccessible(true);
            T instance = constructor.newInstance(objects);
            constructor.setAccessible(false);

            return instance;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Gets value of the given field.
     *
     * @param object    Object.
     * @param fieldName Field name.
     * @param <T>       Type of field.
     * @return Value of the given field.
     */
    @Nullable
    public static <T> T getField(@Nonnull Object object, @Nonnull String fieldName) {
        try {
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(fieldName, "fieldName cannot be null!");

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            T value = (T) field.get(object);
            field.setAccessible(false);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets field value.
     *
     * @param object    Object.
     * @param fieldName Field name.
     * @param value     Value.
     * @param <T>       Type.
     */
    public static <T> void setField(@Nonnull Object object, @Nonnull String fieldName, @Nonnull T value) {
        try {
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(fieldName, "fieldName cannot be null!");
            Validate.notNull(value, "value cannot be null!");

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(false);
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}