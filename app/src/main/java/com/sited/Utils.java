package com.sited;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * Created by haozhong on 2017/6/27.
 */

public class Utils {
    public static String urlEncode(String str, String encode) {
        try {
            return URLEncoder.encode(str, encode);
        } catch (Exception ex) {
            return "";
        }
    }

   public static String getFileString(int resID) {
        try {
            InputStream is = RxSource.getContext().getResources().openRawResource(resID);
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
            return doToString(in);
        } catch (Exception ex) {
            return null;
        }
    }

   public static String doToString(BufferedReader in) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static String Base64_encode(String text){
        return Base64.encodeToString(text.getBytes(Charset.forName("UTF-8")),Base64.NO_WRAP);
    }

    public static String  Base64_decode(String code){
        byte[] temp = Base64.decode(code.getBytes(), Base64.NO_WRAP);
        return new String(temp);
    }

    /*生成MD5值*/
   public static String md5(String code) {

        String s = null;

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] code_byts = code.getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(code_byts);
            byte tmp[] = md.digest();          // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0;                                // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i];                 // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
            }
            s = new String(str);                                 // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
