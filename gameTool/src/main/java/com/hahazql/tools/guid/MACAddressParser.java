package com.hahazql.tools.guid;/**
 * Created by zql on 16/1/20.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zql on 16/1/20.
 *
 * @className MACAddressParser
 * @classUse
 */
public class MACAddressParser {

    public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);

    /**
     * Attempts to find a pattern in the given String.
     *
     * @param in the String, may not be <code>null</code>
     * @return the substring that matches this pattern or <code>null</code>
     */
    static String parse(String in) {
        Matcher m = MAC_ADDRESS.matcher(in);
        if (m.find()) {
            String g = m.group(2);
            if (g == null) {
                g = m.group(1);
            }
            return g == null ? g : g.replace('-', ':');
        }
        return null;
    }

}