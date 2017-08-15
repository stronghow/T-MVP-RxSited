package com.sited;

import android.support.annotation.NonNull;

import com.app.annotation.aspect.MemoryCache;
import com.socks.library.KLog;
import com.utils.Base64Util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by haozhong on 2017/7/7.
 */

public class RxSourceApi {
    public static RxSource getRxSource(InputStream stream) throws DocumentException,IOException {
        SAXReader saxReader = new SAXReader();
        RxSource source = new RxSource();
        Document document = saxReader.read(stream);
        long time = System.currentTimeMillis();
        document.accept(new Visitor(source));
        KLog.json("时间 = " + (System.currentTimeMillis() - time));
        source.PreLoadJS();
        KLog.json("时间 = " + (System.currentTimeMillis() - time));
        RxSource.put(source.url,source); //缓存RxSource
        return source;
    }

    @MemoryCache
    public static RxSource getRxSource(String xml){
        try {
            RxSource source = new RxSource();
            xml = unsuanxml(xml);
            KLog.json("Sited::Xml = " + xml);
            Document document = DocumentHelper.parseText(xml); // 将字符串转为XML
            long time = System.currentTimeMillis();
            document.accept(new Visitor(source));
            KLog.json("时间 = " + (System.currentTimeMillis() - time));
            source.PreLoadJS();
            KLog.json("时间 = " + (System.currentTimeMillis() - time));
            RxSource.put(source.url,source); //缓存RxSource
            return source;
        }catch (DocumentException e){
            e.printStackTrace();
            return null;
        }
    }

    private static String unsuanxml(String xml){
        if (xml.startsWith("sited::")) {
            int start = xml.indexOf("::") + 2;
            int end = xml.lastIndexOf("::");
            String txt = xml.substring(start, end);
            String key = xml.substring(end + 2);
           return unsuan(txt, key);
        }
        return xml;
    }

    @NonNull
    private static String unsuan(String str, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = str.length(); i < len; i++) {
            if (i % 2 == 0) {
                sb.append(str.charAt(i));
            }
        }

        str = sb.toString();
        str = Utils.Base64_decode(str);
        key = key + "ro4w78Jx";

        Charset coder = Charset.forName("UTF-8");

        byte[] data = str.getBytes(coder);
        byte[] keyData = key.getBytes(coder);
        int keyIndex = 0;

        for (int x = 0; x < data.length; x++) {
            data[x] = (byte)(data[x] ^ keyData[keyIndex]);
            if (++keyIndex == keyData.length) {
                keyIndex = 0;
            }
        }
        str = new String(data,coder);

        return Utils.Base64_decode(str);
    }
}
