package com.frame.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @Author : Crazy.X
 * @Date : 2019/10/9
 */
public class Md5Utils {

    /**
     * 字符串签名
     *
     * @param text    需要签名的字符串
     * @param key     密钥
     * @param charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String charset) {
        //拼接key
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, charset));
    }

    /**
     * 根据参数map签名
     *
     * @param map     有序map
     * @param key
     * @param charset
     * @return
     */
    public static String sign(TreeMap<String, Object> map, String key, String charset) {
        Set<String> keySet = map.keySet();
        StringBuilder sb = new StringBuilder();
        for (String mapKey : keySet) {
            String value = (String) map.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        return sign(sb.toString(), key, charset);
    }


    /**
     * 根据参数map签名
     *
     * @param map     hashMap
     * @param key
     * @param charset
     * @return
     */
    public static String sign1(Map<String, Object> map, String key, String charset) {
        Set<String> keySet = map.keySet();
        StringBuilder sb = new StringBuilder();
        for (String mapKey : keySet) {
            String value = (String) map.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        return sign(sb.toString(), key, charset);
    }

    /**
     * 根据参数map签名
     *
     * @param map     hashMap
     * @param key
     * @param charset
     * @return
     */
    public static String sign(Map<String, String> map, String key, String charset) {
        Set<String> keySet = map.keySet();
        StringBuilder sb = new StringBuilder();
        for (String mapKey : keySet) {
            String value = (String) map.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        return sign(sb.toString(), key, charset);
    }

    /**
     * 根据字符串获取byte[]
     *
     * @param content
     * @param charset
     * @return
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (StringUtils.isEmpty(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("转码过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 签名，不用加密码
     *
     * @param origin
     * @param charsetname
     * @return
     */
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

}
