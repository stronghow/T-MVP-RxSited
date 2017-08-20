package com.sited;

import android.text.TextUtils;

import com.socks.library.KLog;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by haozhong on 2017/5/20.
 */
public class Visitor extends VisitorSupport {
    private RxSource source;

    public Visitor(RxSource source){
        this.source = source;
    }

    @Override
    public void visit(Element node) {
        switch (node.getName()){
            case "site":
                source.engine = Integer.valueOf(node.attribute("engine").getValue());
                break;
            case "ua":
                source.ua = RxNodeHelp.getString(node.getTextTrim(),RxSource.defUA);
                break;
            case "main":
                source.dtype = Integer.valueOf(node.attribute("dtype").getValue());
                break;
            case "encode":
                source.encode = RxNodeHelp.getString(node.getTextTrim(),RxSource.defEncode);
                break;
            case "expr":
                source.expr = node.getTextTrim();
                break;
            case "title":
                source.title = node.getTextTrim();
                break;
            case "url":
                source.url = node.getTextTrim();
                break;
            case "code":
                source.code = node.getTextTrim();
                break;
            case "require":
                source.require = new ArrayList<>();
                for(Element item : node.elements()){
                    source.require.add(new Lib(item.attributeValue("url"),item.attributeValue("lib")));
                }
                break;
            case "search":
                source.search = RxNodeHelp.buildNode(source,node);
                break;
            case "hots":
                source.hots = RxNodeHelp.buildNode(source,node);
                break;
            case "updates":
                source.updates = RxNodeHelp.buildNode(source,node);
                break;
            case "tags":
                if(source.tags == null) {
                    source.tags = RxNodeHelp.buildNode(source,node,"tags");

                    List<Element> items = node.elements("item");
                    if (items != null && items.size() > 0) {
                        StringBuilder sb = new StringBuilder("[");
                        for (Element item : items) {
                            sb.append("{");
                            for (Attribute attr : item.attributes()) {
                                sb.append("\"").append(attr.getName()).append("\":\"").append(attr.getValue()).append("\",");
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append("},");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        sb.append("]");
                        source.tags.staticData = sb.toString();
                        KLog.json("Tags = " + source.tags.staticData);
                    }
                }
                break;
            case "tag":
                if(source.tag == null)
                    source.tag  = RxNodeSet.getInstance()
                            .setRxSource(source)
                            .setNode(node)
                            .build();
                break;
            case "book":
                if(source.book == null) {
                    source.book = RxNodeSet.getInstance()
                            .setRxSource(source)
                            .setNode(node)
                            .build("sections");
                }
                break;
            case "section":
                if(source.section == null)
                    source.section  = RxNodeSet.getInstance()
                            .setRxSource(source)
                            .setNode(node)
                            .build();
                break;
            }
    }
}
