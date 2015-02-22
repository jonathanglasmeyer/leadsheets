package com.example.jwerner.mmd.helpers;

/**
 * Created by jwerner on 2/19/15.
 */
public class Strings {

    public static String capitalize(String s) {
        char[] chars = s.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i])) { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

}
