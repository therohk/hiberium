package com.konivax.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class StringUtils {

    private StringUtils() { }

    public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static boolean isEmpty(final CharSequence string) {
        if(string == null || string.length() == 0)
            return true;
        return false;
    }

    public static boolean notEmpty(final CharSequence string) {
        if(string == null || string.length() == 0)
            return false;
        return true;
    }

    public static boolean isBlank(final String string) {
        if(isEmpty(string))
            return true;
        int l = string.length();
        for (int i = 0; i < l; i++) {
            char c = string.charAt(i);
            if(!(c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r'))
                return false;
        }
        return true;
    }

    public static boolean notBlank(final String string) {
        return !isBlank(string);
    }

    public static List<String> splitByPredicate(String string, Predicate<Character> condition) {
        return null;
    }

    public static String toFirstUpperCase(String string) {
        return string.substring(0, 1).toUpperCase()+string.substring(1);
    }

    public static String toFirstLowerCase(String string) {
        return string.substring(0, 1).toLowerCase()+string.substring(1);
    }

    public static String toLowerCaseUnderscore(List<String> strings) {
        if(CollectionUtils.isEmpty(strings))
            return null;
        return strings.stream()
                .map(s -> s.toLowerCase())
                .collect(Collectors.joining("_"));
    }

    public static String toLowerCaseHyphen() {
        return null;
    }

    public static String toCamelCaseClassType() {
        return null;
    }

    public static String toCamelCaseVariable() {
        return null;
    }

    /**
     * Splits a String by Character type as returned by
     * {@code java.lang.Character.getType(char)}. Groups of contiguous
     * characters of the same type are returned as complete tokens, with the
     * following exception: if {@code camelCase} is {@code true},
     * the character of type {@code Character.UPPERCASE_LETTER}, if any,
     * immediately preceding a token of type {@code Character.LOWERCASE_LETTER}
     * will belong to the following token rather than to the preceding, if any,
     * {@code Character.UPPERCASE_LETTER} token.
     *
     * @param str the String to split, may be {@code null}
     * @param camelCase whether to use so-called "camel-case" for letter types
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] splitByCharacterType(final String str, boolean camelCase) {
        if(str == null)
            return null;
        if(str.length() == 0)
            return new String[0];
        char[] c = str.toCharArray();
        List<String> list = new ArrayList<String>();
        int tokenStart = 0;
        int currentType = Character.getType(c[tokenStart]);
        for(int pos = tokenStart + 1; pos < c.length; pos++) {
            int type = Character.getType(c[pos]);
            if(type == currentType) {
                continue;
            }
            if(camelCase && type == Character.LOWERCASE_LETTER && currentType == Character.UPPERCASE_LETTER) {
                int newTokenStart = pos - 1;
                if(newTokenStart != tokenStart) {
                    list.add(new String(c, tokenStart, newTokenStart - tokenStart));
                    tokenStart = newTokenStart;
                }
            } else {
                list.add(new String(c, tokenStart, pos - tokenStart));
                tokenStart = pos;
            }
            currentType = type;
        }
        list.add(new String(c, tokenStart, c.length - tokenStart));
        return list.toArray(new String[list.size()]);
    }
}
