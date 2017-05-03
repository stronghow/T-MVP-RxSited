package com.model;

import org.noear.sited.ISdViewModel;
import org.noear.sited.SdNode;

import java.util.ArrayList;

import noear.snacks.ONode;

/**
 * Created by haozhong on 2017/4/18.
 */

public class TagViewModel extends ModelBase implements ISdViewModel {

    public TagViewModel()
    {
        resultList = new ArrayList<>();
        currentPage = 1;
    }

    public int currentPage;
    public ArrayList<Tag> resultList;


    @Override
    public void loadByConfig(SdNode config){}

    @Override
    public void loadByJson(SdNode config, String... jsons) {
        if(jsons == null || jsons.length == 0)
            return;

        for(String json : jsons) { //支持多个数据块加载
            ONode data = ONode.tryLoad(json);

            if(data.isArray()) {
                for (ONode n : data) {

                    String name = n.get("name").getString();

//                    if (Session.isVip == 0 && SourceApi.isFilter(name)) //非vip进行过滤
//                        continue;

                    Tag b = new Tag();

                    b.name = name;
                    b.url = n.get("url").getString();
                    b.logo = n.get("logo").getString();
                    b.author = n.get("author").getString();
                    b.newSection = n.get("newSection").getString();
                    b.updateTime = n.get("updateTime").getString();
                    b.status = n.get("status").getString();

                    resultList.add(b);
                }
            }
        }
    }
}
