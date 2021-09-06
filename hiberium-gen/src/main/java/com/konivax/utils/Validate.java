package com.konivax.utils;

public final class Validate {

    private Validate() { }

    public static void notEmpty(String string, String msg) {
        if(StringUtils.isEmpty(string))
            throw new IllegalArgumentException(msg);
    }

    public static void notBlank(String string, String msg) {
        if(StringUtils.isBlank(string))
            throw new IllegalArgumentException(msg);
    }

    public static void isTrue(final boolean cond, final String message, final Object... values) {
        if (cond == false)
            throw new IllegalArgumentException(String.format(message, values));
    }

    public static void isFalse(final boolean cond, final String message, final Object... values) {
        if (cond)
            throw new IllegalArgumentException(String.format(message, values));
    }

    public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null)
            throw new NullPointerException(String.format(message, values));
        return object;
    }

    public static void isMatch(final String string, String pattern) {
        notBlank(string, "string is not present");
        if (!string.matches(pattern))
            throw new IllegalArgumentException(
                    String.format("'%s' does not match pattern: %s", string, pattern));
    }
}
