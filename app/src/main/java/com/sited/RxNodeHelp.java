package com.sited;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.socks.library.KLog;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by haozhong on 2017/8/18.
 */

public class RxNodeHelp {
    public static RxNode buildNode(@NonNull RxSource rxSource,@NonNull Element node){
        return buildNode(rxSource,node,null);
    }

    public static RxNode buildNode(@NonNull RxSource rxSource,@NonNull Element node, String ChildrenName){
        RxNode rxNode = new RxNode(rxSource);

        if(!TextUtils.isEmpty(ChildrenName) && node.hasContent()) {
            Element Children_element = node.element(ChildrenName);
            if (Children_element != null) node = Children_element;
        }

        for (Attribute attr : node.attributes()) {
            rxNode.attrs.set(attr.getName(), attr.getValue());
        }

        rxNode.build();
        return rxNode;
    }

    public static List<RxNode> buildNodeSet(@NonNull RxSource rxSource,@NonNull Element node){
        return buildNodeSet(rxSource,node,null);
    }

    public static List<RxNode> buildNodeSet(@NonNull RxSource rxSource,@NonNull Element node, String ChildrenName){
        List<RxNode> items;
        if(node.hasContent()){
            List<Element> elements = node.elements();
            items = new ArrayList<>(elements.size());
            for (Element element : elements)
                items.add(buildNode(rxSource,element, ChildrenName));
        }else {
            items = new ArrayList<>(1);
            items.add(buildNode(rxSource,node, ChildrenName));
        }
        return items;
    }

    public static String getString(String str,String def){
        return TextUtils.isEmpty(str) ? def : str;
    }

    public static LinkedHashMap<String,String> getMap(final String data, final int version){
        LinkedHashMap<String, String> map;
        if (!TextUtils.isEmpty(data)) {
            if (version < 34) {
                return RxNodeHelp.StringToMap(data, ";", "=");
            } else {
                return RxNodeHelp.StringToMap(data, "\\$\\$", ":");//new
            }
        }
        return null;
    }

    public static LinkedHashMap<String,String> StringToMap(@NonNull final String data, final String split_item, final String split_KeyValue) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String kv : data.split(split_item)) {
            String[] item = kv.split(split_KeyValue);
            if (item.length == 2)
                map.put(item[0].trim(), item[1].trim());
        }
        return map;
    }
}
