package com.dgp.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Slf4j
public class Base64Util {

    private static final char _$2[];

    private static final int _$1[];

    static {
        _$2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_.".toCharArray();
        _$1 = new int[256];
        Arrays.fill(_$1, -1);
        int i = 0;
        for (int j = _$2.length; i < j; i++)
            _$1[_$2[i]] = i;

        _$1[61] = 0;
    }

    public static final char[] encodeToChar(byte abyte0[], boolean flag) {
        int i = abyte0 == null ? 0 : abyte0.length;
        if (i == 0)
            return new char[0];
        int j = (i / 3) * 3;
        int k = (i - 1) / 3 + 1 << 2;
        int l = k + (flag ? (k - 1) / 76 << 1 : 0);
        char ac[] = new char[l];
        int i1 = 0;
        int j1 = 0;
        int l1 = 0;
        do {
            if (i1 >= j)
                break;
            int i2 = (abyte0[i1++] & 0xff) << 16 | (abyte0[i1++] & 0xff) << 8 | abyte0[i1++] & 0xff;
            ac[j1++] = _$2[i2 >>> 18 & 0x3f];
            ac[j1++] = _$2[i2 >>> 12 & 0x3f];
            ac[j1++] = _$2[i2 >>> 6 & 0x3f];
            ac[j1++] = _$2[i2 & 0x3f];
            if (flag && ++l1 == 19 && j1 < l - 2) {
                ac[j1++] = '\r';
                ac[j1++] = '\n';
                l1 = 0;
            }
        } while (true);
        i1 = i - j;
        if (i1 > 0) {
            int k1 = (abyte0[j] & 0xff) << 10 | (i1 != 2 ? 0 : (abyte0[i - 1] & 0xff) << 2);
            ac[l - 4] = _$2[k1 >> 12];
            ac[l - 3] = _$2[k1 >>> 6 & 0x3f];
            ac[l - 2] = i1 != 2 ? '.' : _$2[k1 & 0x3f];
            ac[l - 1] = '.';
        }
        return ac;
    }

    public static final byte[] decodeFast(String s) {
        int i = s.length();
        if (i == 0)
            return new byte[0];
        int j = 0;
        int k;
        for (k = i - 1; j < k && _$1[s.charAt(j) & 0xff] < 0; j++)
            ;
        for (; k > 0 && _$1[s.charAt(k) & 0xff] < 0; k--)
            ;
        byte byte0 = s.charAt(k) != '.' ? 0 : ((byte) (s.charAt(k - 1) != '.' ? 1 : 2));
        int l = (k - j) + 1;
        int i1 = i <= 76 ? 0 : (s.charAt(76) != '\r' ? 0 : l / 78) << 1;
        int j1 = ((l - i1) * 6 >> 3) - byte0;
        byte abyte0[] = new byte[j1];
        int k1 = 0;
        int l1 = 0;
        int j2 = (j1 / 3) * 3;
        do {
            if (k1 >= j2)
                break;
            int i3 = _$1[s.charAt(j++)] << 18 | _$1[s.charAt(j++)] << 12 | _$1[s.charAt(j++)] << 6 | _$1[s.charAt(j++)];
            abyte0[k1++] = (byte) (i3 >> 16);
            abyte0[k1++] = (byte) (i3 >> 8);
            abyte0[k1++] = (byte) i3;
            if (i1 > 0 && ++l1 == 19) {
                j += 2;
                l1 = 0;
            }
        } while (true);
        if (k1 < j1) {
            int i2 = 0;
            for (int k2 = 0; j <= k - byte0; k2++)
                i2 |= _$1[s.charAt(j++)] << 18 - k2 * 6;

            for (int l2 = 16; k1 < j1; l2 -= 8)
                abyte0[k1++] = (byte) (i2 >> l2);

        }
        return abyte0;
    }

    /**
     * @param s
     * @return String
     * @description:base64编码
     */
    public static final String encode(String s) {
        try {
            return new String(encodeToChar(s.getBytes("UTF-8"), false));
        } catch (UnsupportedEncodingException e) {
            log.error("encode to char  error", e);
        }
        return null;
    }

    /**
     * @param s
     * @return String
     * @author:LQL
     * @date:2019年10月8日
     * @description:
     */
    public static final String encode(byte[] s) {
        String result = null;
        try {
            result = new String(encodeToChar(s, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param s
     * @return String
     * @throws UnsupportedEncodingException
     * @author:LQL
     * @date:2019年10月5日
     * @description:base64解码
     */
    public static String decode(String s) throws UnsupportedEncodingException {
        try {
            return new String(decodeFast(s), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeToByte(String s) throws UnsupportedEncodingException {
        return decodeFast(s);
    }

    /**
     * @param s
     * @return String
     * @throws UnsupportedEncodingException
     * @author:LQL
     * @date:2019年10月5日
     * @description:base64解码
     */
    public static String decode(byte s[]) throws UnsupportedEncodingException {
        try {
            return new String(decodeFast(new String(s, "UTF-8")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param orinigStr
     * @return
     * @author:LQL
     * @date:2019年12月8日
     * @return: String
     * @description:替换特殊字符
     */

    public static String relaceChar(String orinigStr) {
        if (StringUtils.isBlank(orinigStr)) {
            return null;
        }
        orinigStr = orinigStr.replace("+", "_");
        orinigStr = orinigStr.replace("=", ".");
        return orinigStr;

    }

    public static void main(String args[]) throws UnsupportedEncodingException {
        String s = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=java%20base64%20%E7%89%B9%E6%AE%8A%E5%AD%97%E7%AC%A6&oq=java%2520base64%2520%25E5%25A4%2584%25E7%2590%2586&rsv_pq=a2970e150019d7f2&rsv_t=20ca10QMEyYZH4YEo%2BU9YhK%2BOokffxZSn0gjHiLia%2BIJlVvn2Vir2x2Tsk0&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=3418&rsv_sug3=155&rsv_sug1=95&rsv_sug7=000&rsv_sug2=0&rsv_sug4=4282&rsv_sug=1";
        String s1 = encode(s);
        System.out.println(s1);
        System.out.print(decode(s1));
    }

}
