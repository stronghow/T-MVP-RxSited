package com.sited;

import com.socks.library.KLog;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import java.util.ArrayList;

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
                    source.ua = node.getTextTrim();
                    break;
                case "main":
                    source.dtype = Integer.valueOf(node.attribute("dtype").getValue());
                    break;
                case "encode":
                    source.encode = node.getTextTrim();
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
                    source.search = new RxNode();
                    for(Attribute attribute : node.attributes())
                        source.search.attrs.set(attribute.getName(),attribute.getValue());
                    source.search.build(source);
                    break;
                case "tags":
                      source.tags = new RxNode();
                      Element tags = node;
                      for(Attribute attribute : tags.attributes())
                          source.tags.attrs.set(attribute.getName(),attribute.getValue());
                      if(tags.elements().size()>0) {
                          StringBuilder sb = new StringBuilder("[");
                          for (Element item : tags.elements()) {
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
                      source.tags.build(source);
                      break;
                  case "tag":
                      source.tag = new RxNode();
                      for(Attribute attribute : node.attributes())
                          source.tag.attrs.set(attribute.getName(),attribute.getValue());
                      source.tag.build(source);
                      break;
                  case "book":
                      if(source.book == null) {
                          source.book = new RxNode();
                          Element book = node;

                          for (Attribute attribute : book.attributes())
                              source.book.attrs.set(attribute.getName(), attribute.getValue());

                          source.book.build(source);
                      }
                      break;
                  case "sections":
                      if (source.sections == null) {
                          source.sections = new RxNode();
                          for (Attribute attr : node.attributes())
                              source.sections.attrs.set(attr.getName(), attr.getValue());
                          source.sections.build(source);
                      }
                  case "section":
                      if(source.section == null)
                        source.section  = RxNodeSet.getInstance().build(node,source);
                      break;
        }
    }
}
