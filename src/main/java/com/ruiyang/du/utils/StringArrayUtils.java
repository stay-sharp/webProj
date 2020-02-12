package com.ruiyang.du.utils;

import java.text.DecimalFormat;

public class StringArrayUtils {
    private static final char[] hexChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private StringArrayUtils() {
    }

    public static int contain(String s, String[] ss) {
        for (int i = 0; i < ss.length; ++i) {
            if (s.equals(ss[i])) {
                return i;
            }
        }

        return -1;
    }

    public static String arrayToString(String[] ss, String delimeter) {
        StringBuffer sb = new StringBuffer();
        if (ss != null && ss.length > 0) {
            for (int i = 0; i < ss.length; ++i) {
                sb.append(ss[i]);
                if (i + 1 < ss.length) {
                    sb.append(delimeter);
                }
            }
        }

        return sb.toString();
    }

    public static String arrayToString(String[] ss, char delimeter) {
        return arrayToString(ss, Character.toString(delimeter));
    }

    public static boolean isValidDecString(String str) {
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }

        return true;
    }

    public static String int2char2(int val) {
        DecimalFormat fmt = new DecimalFormat("00");
        return val >= 0 && val <= 99 ? fmt.format((long) val) : null;
    }

    public static String int2char3(int val) {
        DecimalFormat fmt = new DecimalFormat("000");
        return val >= 0 && val <= 999 ? fmt.format((long) val) : null;
    }

    public static String StringFillLeftZero(String str, int len) {
        if (str.length() >= len) {
            return str;
        } else {
            StringBuffer sb = new StringBuffer(len);

            for (int i = 0; i < len - str.length(); ++i) {
                sb.append('0');
            }

            sb.append(str);
            return new String(sb);
        }
    }

    public static String StringFillRightBlank(String str, int len) {
        if (str.length() >= len) {
            return str;
        } else {
            StringBuffer sb = new StringBuffer(len);
            sb.append(str);

            for (int i = 0; i < len - str.length(); ++i) {
                sb.append(' ');
            }

            return new String(sb);
        }
    }

    public static String long2StringLeftZero(long val, int len) {
        String pattern = "";

        for (int i = 0; i < len; ++i) {
            pattern = pattern + '0';
        }

        DecimalFormat fmt = new DecimalFormat(pattern);
        return fmt.format(val);
    }

    public static String FillString(char val, int len) {
        String str = "";

        for (int i = 0; i < len; ++i) {
            str = str + val;
        }

        return str;
    }

    public static String long2StringRightBlank(long val, int len) {
        String pattern = "#";
        DecimalFormat fmt = new DecimalFormat(pattern);
        String str = fmt.format(val);
        return str.length() < len ? str + FillString(' ', len - str.length()) : str.substring(str.length() - len);
    }

    public static String byte2hex(byte[] b) {
        return byte2hex(b, '\u0000');
    }

    private static void byte2hex(byte b, StringBuffer buf) {
        int high = (b & 240) >> 4;
        int low = b & 15;
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    public static String byte2hex(byte b) {
        int high = (b & 240) >> 4;
        int low = b & 15;
        return "" + hexChars[high] + hexChars[low];
    }

    public static String byte2hex(byte[] b, char delimeter) {
        StringBuffer sb = new StringBuffer();

        for (int n = 0; n < b.length; ++n) {
            byte2hex(b[n], sb);
            if (n < b.length - 1 && delimeter != 0) {
                sb.append(delimeter);
            }
        }

        return sb.toString().toLowerCase();
    }

    public static byte[] hex2byte(String hexStr, int len) throws AssertionError {
        if (len % 2 != 0) {
            throw new AssertionError("Hex2Byte() fail: len should be divided by 2");
        } else {
            byte[] buf = new byte[len / 2];
            int i = 0;

            for (int j = 0; i < len; ++j) {
                byte hi = (byte) Character.toUpperCase(hexStr.charAt(i));
                byte lo = (byte) Character.toUpperCase(hexStr.charAt(i + 1));
                if (!Character.isDigit((char) hi) && (hi < 65 || hi > 70)) {
                    throw new AssertionError("Hex2Byte() fail: hex string is invalid in " + i + " with char '" + hexStr.charAt(i) + "'");
                }

                if (!Character.isDigit((char) lo) && (lo < 65 || lo > 70)) {
                    throw new AssertionError("Hex2Byte() fail: hex string is invalid in " + (i + 1) + " with char '" + hexStr.charAt(i + 1) + "'");
                }

                int ch = 0;
                ch = ch | (Character.isDigit((char) hi) ? hi - 48 : 10 + hi - 65) << 4;
                ch |= Character.isDigit((char) lo) ? lo - 48 : 10 + lo - 65;
                buf[j] = (byte) ch;
                i += 2;
            }

            return buf;
        }
    }

    public static String leftFillChar(String val, char c, int maxlen) {
        StringBuffer sb = new StringBuffer();
        if (val.length() < maxlen) {
            for (int i = 0; i < maxlen - val.length(); ++i) {
                sb.append('0');
            }
        }

        sb.append(val);
        return sb.toString();
    }

}
