package me.noear.exts;

import java.util.List;

/**
 * Created by yuety on 14-6-16.
 */
public class ListExt {
    // for list
    //
    public static <T> T find(List<T> list, Fun1<Boolean,T> fun){
        for(int i=0,len = list.size(); i<len; i++)
        {
            T m1 = list.get(i);
            if(fun.run(m1))
                return m1;
        }
        return null;
    }

    public static <T> int findIndex(List<T> list, Fun1<Boolean,T> fun){
        for(int i=0,len = list.size(); i<len; i++)
        {
            T m1 = list.get(i);
            if(fun.run(m1))
                return i;
        }
        return -1;
    }

    public static <T> int remove(List<T> list, Fun1<Boolean,T> fun){
        int idx = findIndex(list,fun);

        if(idx>=0)
            list.remove(idx);

        return idx;
    }

    public static <T> String join(List<T> list, String spit)
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0,len=list.size(); i<len; i++)
        {
            sb.append(list.get(i));

            if(i+1<len)
                sb.append(spit);
        }

        return sb.toString();
    }
}
