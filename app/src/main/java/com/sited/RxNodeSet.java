package com.sited;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haozhong on 2017/5/20.
 */
public class RxNodeSet implements IRxNode {

    List<RxNode> items;

    public static RxNodeSet getInstance(){
        return new RxNodeSet();
    }

    public RxNodeSet build(Element node, RxSource rxSource){
        items = new ArrayList<>();
        if(node.elements().size()>0) {
            for (Element element : node.elements()) {
                RxNode rxNode = new RxNode();
                for (Attribute attr : element.attributes()) {
                    //System.out.println("key = " + attr.getName() +" value = " + attr.getValue());
                    rxNode.attrs.set(attr.getName(), attr.getValue());
                }
                rxNode.build(rxSource);
                items.add(rxNode);
            }
        }else{
            RxNode rxNode = new RxNode();
            for (Attribute attr : node.attributes()) {
                rxNode.attrs.set(attr.getName(), attr.getValue());
            }
            rxNode.build(rxSource);
            items.add(rxNode);
        }
        return this;
    }

    private RxNodeSet(){
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public RxNode nodeMatch(String url) {
        if(items.size()==1) return items.get(0);
        for(RxNode rxNode : items){
            if(rxNode.attrs.getString("expr")==null)
                continue;
            if(url.matches(rxNode.attrs.getString("expr")))
                return rxNode;
        }
        return items.get(0);
    }

    @Override
    public String nodeName() {
        return null;
    }

    @Override
    public int nodeType() {
        return 0;
    }
}
