package com.konivax.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class StringUtils {

    private StringUtils() { }

    public static String defaultString(String string, String defaultValue) {
        return string == null ? defaultValue : string;
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

    public static Boolean parseBoolean(final String string) {
        return null;
    }

    public static String capFirst(String string) {
        return string.substring(0, 1).toUpperCase()+string.substring(1);
    }

    public static String uncapFirst(String string) {
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

    public static String snip(String string, int leftCut, int rightCut) {
        if(StringUtils.isEmpty(string))
            return string;
        if(leftCut < 0 || rightCut < 0)
            return null;
        if(leftCut + rightCut > string.length())
            return "";
        return string.substring(leftCut, string.length() - rightCut);
    }

    public static List<String> splitByPredicate(String string, Predicate<Character> condition) {
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
     * @param string the String to split, may be {@code null}
     * @param camelCase whether to use so-called "camel-case" for letter types
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static List<String> splitByCharacterType(final String string, boolean camelCase) {
        if(string == null)
            return null;
        if(string.length() == 0)
            return new ArrayList<String>(0);
        char[] c = string.toCharArray();
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
        return list;
    }
}
