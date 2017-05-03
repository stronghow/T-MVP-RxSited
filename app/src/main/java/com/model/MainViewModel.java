package com.model;

import com.dao.engine.DdSource;

import org.noear.sited.ISdViewModel;
import org.noear.sited.SdNode;

import java.util.ArrayList;

import noear.snacks.ONode;

/**
 * Created by haozhong on 2017/4/9.
 */

public class MainViewModel extends ModelBase implements ISdViewModel{

    public MainViewModel()
    {
        tagList = new ArrayList<>();
        hotList 	= new ArrayList<>();
        updateList 	= new ArrayList<>();
    }


    public final ArrayList<Tags> tagList;
    public final ArrayList<Hots> 	    hotList;
    public final ArrayList<Updates>  updateList;

    public void clear() {
        tagList.clear();
        hotList.clear();
        updateList.clear();
    }

    @Override
    public void loadByConfig(SdNode config) {
        if (DdSource.isHots(config)) {
            hotList.clear();

            for (SdNode t1 : config.items()) {
                Hots b = new Hots();
                b.name = t1.title;
                b.url = t1.url;
                b.logo = t1.logo;

                //KLog.json("t1.url:" + t1.url);

                hotList.add(b);
            }
            return;
        }

        if (DdSource.isTags(config)) {
            tagList.clear();

            for (SdNode n : config.items()) {
                //KLog.json("t1.url:" + n.url);
                Tags t1 = new Tags();
                t1.title = n.title;
                t1.url   = n.url;
                //t1.group = n.get("group").getString();

                tagList.add(t1);
                //AddTagItem(t1);
            }
            return;
        }
    }

    @Override
    public void loadByJson(SdNode config, String... jsons) {
        if(jsons == null || jsons.length==0)
            return;

        for(String json : jsons){ //支持多个数据块加载
            ONode data = ONode.tryLoad(json).asArray();

            if(DdSource.isHots(config)) {

                for(ONode n:data){
                    //KLog.json("n.get(\"url\").getString()" + n.get("url").getString());
                    Hots b = new Hots();
                    b.name   = n.get("name").getString();
                    b.url    = n.get("url").getString();
                    b.logo   = n.get("logo").getString();

                    hotList.add(b);
                }
                return;
            }

            if(DdSource.isUpdates(config)){

                for(ONode n:data){
                    //KLog.json("n.get(\"url\").getString()" + n.get("url").getString());
                    Updates b = new Updates();
                    b.name   	 = n.get("name").getString();
                    b.url    	 = n.get("url").getString();
                    b.newSection = n.get("newSection").getString();
                    b.updateTime = n.get("updateTime").getString();

                    updateList.add(b);
                }
                return;
            }

            if(DdSource.isTags(config)){

                for(ONode n:data){
                    //KLog.json("n.get(\"url\").getString()" + n.get("url").getString());
                    Tags t1 = new Tags();
                    t1.title = n.get("title").getString();
                    t1.url   = n.get("url").getString();
                    //t1.group = n.get("group").getString();

                    tagList.add(t1);
                    //AddTagItem(t1);
                }
            }
        }
    }
    private  void AddTagItem(SdNode t1){
//        if (TextUtils.isEmpty(t1.group) == false) {
//            int temp = tagList.size() % 3;
//            if (temp > 0) {
//                temp = 3 - temp;
//            }
//
//            while (temp > 0) {
//                tagList.add(new Tags("", null, 1));
//                temp--;
//            }
//
//            tagList.add(new Tags("", null, 11));
//            tagList.add(new Tags("按" + t1.group, null, 10));
//            tagList.add(new Tags("", null, 11));
//
//        }
//        tagList.add(new Tags(t1.title, t1.url, 0));
    }
}
