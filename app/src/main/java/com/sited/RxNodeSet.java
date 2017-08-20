package com.sited;
import android.text.TextUtils;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haozhong on 2017/5/20.
 */
public class RxNodeSet implements IRxNode {

    private List<RxNode> items;

    private RxSource rxSource;

    private Element node;

    public static RxNodeSet getInstance(){
        return new RxNodeSet();
    }

    public RxSource getRxSource() {
        return rxSource;
    }

    public RxNodeSet setRxSource(RxSource rxSource){
        this.rxSource = rxSource;
        return this;
    }

    public RxNodeSet setNode(Element node){
        this.node = node;
        return this;
    }

    public RxNodeSet build(){
        items = RxNodeHelp.buildNodeSet(rxSource,node);
        return this;
    }

    //book
    public RxNodeSet build(String ChildrenName){
        items = RxNodeHelp.buildNodeSet(rxSource,node,ChildrenName);
        return this;
    }

    private RxNodeSet(){
    }


    @Override
    public boolean isEmpty() {
        return items == null || items.size() == 0;
    }

    @Override
    public RxNode nodeMatch(String url) {
        if(items.size()==1) return items.get(0);
        for(RxNode rxNode : items){
            String expr = rxNode.attrs.getString("expr");
            if(!TextUtils.isEmpty(expr)) {
                if (url.matches(expr))
                    return rxNode;
            }
        }
        return items.get(0);
    }

    @Override
    public String nodeName() {
        return node.getName();
    }

    @Override
    public int nodeType() {
        return 0;
    }
}
