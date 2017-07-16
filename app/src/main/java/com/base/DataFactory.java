package com.base;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haozhong on 2017/5/31.
 */

public class DataFactory {
    public static <T> T JsonToBean(String json,Class<T> clazz)
    {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> ArrayList<T> JsonToArrayList(String json)
    {
        return new Gson().fromJson(json, new TypeToken<ArrayList<T>>(){}.getType());
    }

    /**
     * @author I321533
     * @param json
     * @param clazz
     * @return
     */
//    public static <T> List<T> JsonToList(String json, Class<T[]> clazz)
//    {
//        Gson gson = new Gson();
//        T[] array = gson.fromJson(json, clazz);
//        return Arrays.asList(array);
//    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> JsonToList(String json, Class<T> clazz)
    {
        Type type = new TypeToken<ArrayList<JsonObject>>()
        {}.getType();
        List<JsonObject> jsonObjects = new Gson().fromJson(json, type);

       List<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }
}
