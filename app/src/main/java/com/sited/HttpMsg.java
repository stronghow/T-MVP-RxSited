package com.sited;

import java.util.LinkedHashMap;

/**
 * Created by haozhong on 2017/7/14.
 */

public interface HttpMsg {
    String getUrl();
    String getMethod();
    String getEncode();
    LinkedHashMap<String,String> getHeaders();
    LinkedHashMap<String,String> getParams();
}
