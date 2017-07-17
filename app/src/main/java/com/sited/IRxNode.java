package com.sited;
/**
 * Created by haozhong on 2017/5/22.
 */

public interface IRxNode {
    String nodeName();
    int nodeType();
    boolean isEmpty();
    RxNode nodeMatch(String url);
}
