package me.noear.utils;

import android.content.SharedPreferences;

/**
 * Created by yuety on 15/8/21.
 */
public abstract class SettingsUtil {
    protected static SharedPreferences mSets;

    public static int getInt(String name)
    {
        return mSets.getInt(name, 0);
    }

    public static int getInt(String name,int def)
    {
        return mSets.getInt(name, def);
    }

    public static long getLong(String name)
    {
        return mSets.getLong(name, 0);
    }

    public static float getFloat(String name)
    {
        return mSets.getFloat(name, 0);
    }

    public static boolean getBoolean(String name)
    {
        return mSets.getBoolean(name, false);
    }

    public static boolean getBoolean(String name, boolean def)
    {
        return mSets.getBoolean(name, def);
    }

    public static String getString(String name)
    {
        return mSets.getString(name, null);
    }

    public static boolean contains(String name)
    {
        return mSets.contains(name);
    }

    public static void setInt(String name,int value)
    {
        SharedPreferences.Editor editor = mSets.edit();

        editor.putInt(name,value);
        editor.commit();
    }

    public static void setLong(String name,long value)
    {
        SharedPreferences.Editor editor = mSets.edit();

        editor.putLong(name,value);
        editor.commit();
    }

    public static void setFloat(String name,float value)
    {
        SharedPreferences.Editor editor = mSets.edit();

        editor.putFloat(name,value);
        editor.commit();
    }

    public static void setBoolean(String name,boolean value)
    {
        SharedPreferences.Editor editor = mSets.edit();

        editor.putBoolean(name,value);
        editor.commit();
    }

    public static void setString(String name,String value)
    {
        SharedPreferences.Editor editor = mSets.edit();

        editor.putString(name,value);
        editor.commit();
    }
}
