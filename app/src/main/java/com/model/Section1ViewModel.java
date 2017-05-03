package com.model;

import org.noear.sited.ISdViewModel;
import org.noear.sited.SdNode;

import java.util.ArrayList;
import java.util.List;

import noear.snacks.ONode;

/**
 * Created by haozhong on 2017/4/19.
 */

public class Section1ViewModel extends ModelBase implements ISdViewModel {
    int _total;
    public final List<PicModel> items;
    public final List<PicModel> newItems;//新增项//用于记录当前加载新项的项目
    public int currentIndex;

    public PicModel get(int index){
        return items.get(index);
    }
    public int total(){return _total;}

    public int invertedIndex(int index){
        return _total - 1 - index;
    }

    public void clear(){
        _total = 0;
        items.clear();
        newItems.clear();
    }

    public Section1ViewModel()
    {
        items = new ArrayList<PicModel>();
        newItems = new ArrayList<PicModel>();
    }


    public Sections currentSection;
    public Sections fromSection;

    public boolean isNext(){
        if(fromSection != null && fromSection.orgIndex > currentSection.orgIndex){ //现在比之前的后面些
            return true;
        }else{
            return false;
        }
    }

    public boolean isPrve(){
        if(fromSection != null && fromSection.orgIndex < currentSection.orgIndex){ //现在比之前的后面些
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void loadByConfig(SdNode config){}

    @Override
    public void loadByJson(SdNode config, String... jsons) {
        if(jsons == null || jsons.length==0)
            return;

        if (currentSection.total == 0) { //阅读时
            boolean isBef = isPrve();

            newItems.clear();
            int index = 0;
            for (String json : jsons)//支持多个数据块加载
            {
                ONode list = ONode.tryLoad(json);
                for (ONode n : list) {
                    PicModel pic = new PicModel();
                    pic.sections = currentSection;
                    pic.url = n.getString();
                    pic.secIndex = index;
                    pic.cacheID = items.size();
                    if (isBef) {
                        items.add(index, pic);
                    } else {
                        items.add(pic);
                    }
                    newItems.add(pic);
                    index++;
                }
            }

            currentSection.total = index;
            _total = items.size();

            if (isBef) { //重新算位置
                currentIndex = index; //保持原位
            }
        }
    }
}
