package com.model;

import com.dao.engine.DdSource;

import org.noear.sited.ISdViewModel;
import org.noear.sited.SdNode;

import java.util.ArrayList;
import java.util.List;

import me.noear.utils.EncryptUtil;
import noear.snacks.ONode;

/**
 * Created by haozhong on 2017/4/19.
 */

public class BookViewModel extends ModelBase implements ISdViewModel {
    int _lastLookIndex = -1;
    int _lastLook; //根据url 生成 haskcode
    String lastLook;

    public final DdSource source;

    public BookViewModel(DdSource s, String url) {
        source = s;

        _sectionDownList = new ArrayList<>();
        _sectionUpList = new ArrayList<>();
        _sectionOrgList = new ArrayList<>();


        bookUrl = url;
        bookKey = EncryptUtil.md5(url);

        intro="";

        //DbApi.logBID(source, this);
        //bookBID = DbApi.getBID(bookKey);
    }

    //public final int    bookBID;
    public final String bookKey;//根据bookUrl生成
    public final String bookUrl;

    public String key;
    public String tags;
    public String name;
    public String author;
    public String intro;
    public String logo;
    public String updateTime;

    public boolean isSortUp;


    public List<Sections> sectionList(){
        return doSectionList();
    }

    private int _dtype = -1;
    public int dtype() {
        if (_dtype < 0) {
            _dtype = source.book(bookUrl).dtype();
        }

        return _dtype;
    }

    List<Sections> _sectionDownList;
    List<Sections> _sectionUpList;
    List<Sections> _sectionOrgList;


    List<Sections> doSectionList(){
        if(isSortUp)
            return _sectionUpList;
        else
            return _sectionDownList;
    }

    //==============
    public  void clear() {
        _sectionDownList.clear();
        _sectionUpList.clear();
        _sectionOrgList.clear();
    }

    public String lastLookUrl;
    public Integer lastLookUrlPage;
    public void setLastLook(String sectionUrl){
        if(sectionUrl==null)
            return;

        lastLookUrl = sectionUrl;
        _lastLook   = sectionUrl.hashCode();
        int idx = 0;
        for(Sections sec : doSectionList()){
            if(sec.code() == _lastLook){
                _lastLookIndex = idx;
                break;
            }
            idx++;
        }
    }

    public int lastLook(){
        return _lastLook;
    }

    public int lastLookIndex()
    {
        return _lastLookIndex;
    }

    public int sectionCount()
    {
        if(doSectionList() == null)
            return 0;
        else
            return doSectionList().size();
    }

    public int sectionOrgCount()
    {
        return _sectionOrgList.size();
    }


    public Sections getSection(int idx)
    {
        if(doSectionList() == null)
            return null;

        int len = doSectionList().size();
        if(idx>=len || idx<0)
            return null;
        else
            return doSectionList().get(idx);
    }


    public Sections getOrgSection(int idx)
    {
        if(_sectionOrgList == null)
            return null;

        int len = _sectionOrgList.size();
        if(idx>=len || idx<0)
            return null;
        else
            return _sectionOrgList.get(idx);
    }


    @Override
    public void loadByConfig(SdNode config){}

    @Override
    public void loadByJson(SdNode config, String... jsons) {
        if(jsons == null || jsons.length==0)
            return;

        //lastLook = SiteDbApi.getLastLook(bookUrl);

        if(DdSource.isBook(config)) {
            String json = jsons[0]; //不支持多个数据块加载
            ONode data = ONode.tryLoad(json);

            key =  data.get("key").getString();
            tags =  data.get("tags").getString();

            name = data.get("name").getString();
            author = data.get("author").getString();
            intro = data.get("intro").getString();
            logo = data.get("logo").getString();
            updateTime = data.get("updateTime").getString();
        }

        for (String json : jsons) //支持多个数据块加载
        {
            ONode data = ONode.tryLoad(json);
            ONode sl = data.get("sections").asArray();

            for(ONode n : sl){
                Sections sec = new Sections();
                sec.name = n.get("name").getString();
                sec.url  = n.get("url").getString();

                sec.bookName = name;
                sec.bookUrl  = bookUrl;
                //sec.isLook = (sec.url != null && sec.url.equals(lastLook)) ? true :false;

                sec.orgIndex = sectionOrgCount();

                _sectionOrgList.add(sec);

                sec.index    = sectionCount();
                _sectionDownList.add(sec);
                _sectionUpList.add(0, sec);
            }
        }

    }
}
