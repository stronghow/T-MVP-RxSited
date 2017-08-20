package com;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.base.DataFactory;
import com.base.adapter.TypeSelector;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.Tags;
import com.model.dtype1;
import com.model.dtype1;
import com.model.dtype2;
import com.model.dtype3;
import com.socks.library.KLog;
import com.ui.main.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by haozhong on 2017/8/20.
 */

public class SitedManage {

    private static SparseArray<databind> databinds;

    public static databind getDataBind(int dtype){
        if(databinds == null)
            databinds = createDataBind();
        return databinds.get(dtype);
    }

    public static  List toList(String data, int dtype, String QueryKey){
        List  models = new ArrayList<>();
        KLog.json("toList",MessageFormat.format("dtype ==> {0}\ndata ==> {1}",dtype,data));
        switch (dtype){
            case 1:
            case 4:
                JsonElement element = new JsonParser().parse(data);
                if(element.isJsonArray()) {
                    for (JsonElement el : element.getAsJsonArray()) {
                        final dtype1 model = new dtype1();
                        model.url = el.getAsString();
                        model.QueryKey = QueryKey;
                        models.add(model);
                    }
                }else{
                   JsonArray list = element.getAsJsonObject().get("list").getAsJsonArray();
                   for (JsonElement el : list) {
                       final dtype1 model = new dtype1();
                       if(el.isJsonObject()){
                           JsonObject object = el.getAsJsonObject();
                           model.url = object.get("url").getAsString();
                           //model.time = object.get("time").getAsInt();
                       }else{
                           model.url = el.getAsString();
                       }
                       model.QueryKey = QueryKey;
                       models.add(model);
                   }
                }
                break;

            case 2:
            case 6:
                models = DataFactory.JsonToList(data,dtype2.class);
                for(dtype2 it : (List<dtype2>)models)
                    it.QueryKey = QueryKey;
                break;

            case 3:
            case 7:
                if(data.startsWith("[") && data.endsWith("]")){
                    JsonElement element3 = new JsonParser().parse(data);
                    JsonArray array;
                    if(element3.isJsonObject())
                        array = element3.getAsJsonObject().get("list").getAsJsonArray();
                    else
                        array = element3.getAsJsonArray();
                    for(JsonElement el : array){
                        JsonObject object = el.getAsJsonObject();
                        dtype3 model = new dtype3();
                        model.url = object.get("url").getAsString();
                        model.type = object.get("type").getAsString();
                        model.mime = object.get("mime").getAsString();
                        model.logo = object.get("logo").getAsString();
                        model.QueryKey = QueryKey;
                        models.add(model);
                    }
                }else {
                    dtype3 model = new dtype3();
                    model.url = data;
                    model.QueryKey = QueryKey;
                    models.add(model);
                }
                break;

            case 5:
                break;

            default:
                break;
        }
        return models;
    }

    private static SparseArray<databind> createDataBind(){
        SparseArray<databind> databinds = new SparseArray<>(10);
        databind<dtype1> databind1 = new databind<>();
        databind1.dtype = 1;
        databind1.model = dtype1.class;
        databind1.layoutId = R.layout.sited_section_dtype1;
        databind1.isNo_More = false;
        databind1.isGoBook = true;

        databind<dtype2> databind2 = new databind<>();
        TypeSelector<dtype2> typeSelector2 = (dtype2) -> dtype2.t == 1 ? R.layout.sited_section_dtype2_item2 : R.layout.sited_section_dtype2_item1;
        databind2.dtype = 2;
        databind2.model = dtype2.class;
        databind2.typeSelector = typeSelector2;
        databind2.isNo_More = false;
        databind2.isGoBook = true;

        databind<dtype3> databind3 = new databind<>();
        databind3.dtype = 3;
        databind3.model = dtype3.class;
        databind3.layoutId = R.layout.sited_section_dtype3;
        databind3.isNo_More = false;
        databind3.isGoBook = true;

        databind<dtype1> databind4 = new databind<>();
        databind4.dtype = 4;
        databind4.model = dtype1.class;
        databind4.layoutId = R.layout.sited_section_dtype1;
        databind4.isNo_More = true;
        databind4.isGoBook = false;

        databind<dtype2> databind6 = new databind<>();
        TypeSelector<dtype2> typeSelector6 = (dtype2) -> dtype2.t == 1 ? R.layout.sited_section_dtype2_item2 : R.layout.sited_section_dtype2_item1;
        databind6.dtype = 6;
        databind6.model = dtype2.class;
        databind6.typeSelector = typeSelector6;
        databind6.isNo_More = true;
        databind6.isGoBook = false;

        databind<dtype3> databind7 = new databind<>();
        databind7.dtype = 7;
        databind7.model = dtype3.class;
        databind7.layoutId = R.layout.sited_section_dtype3;
        databind7.isNo_More = true;
        databind7.isGoBook = false;

        databinds.put(1,databind1);
        databinds.put(2,databind2);
        databinds.put(3,databind3);
        databinds.put(4,databind4);
        databinds.put(5,null);
        databinds.put(6,databind6);
        databinds.put(7,databind7);

        return databinds;
    }


    public static final class databind<M>{
       public int dtype;
       public Class model;
       public int layoutId;
       public boolean isNo_More;
       public TypeSelector<M> typeSelector;
       public boolean isGoBook;
    }
}
