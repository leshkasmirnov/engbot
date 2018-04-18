package ru.asmirnov.engbot.util;

/**
 * @author Alexey Smirnov at 18/04/2018
 */
public final class JdbsUtils {

    private JdbsUtils() {
    }

    public static Long getLong(Long value, boolean wasNull) {
        if (wasNull) {
            return null;
        }
        return value;
    }
}
