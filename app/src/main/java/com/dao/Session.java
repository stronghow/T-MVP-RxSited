//package com.dao;
//
//import android.content.Context;
//import android.telephony.TelephonyManager;
//
//import com.App;
//import com.Config;
//
//import java.io.UnsupportedEncodingException;
//import java.util.UUID;
//
///**
// * Created by yuety on 15/7/22.
// */
//public class Session {
//
//    public static long userID;
//
//    public static String nickname;
//    public static String icon;
//    public static int sex;
//    public static int isVip;
//    public static int level;
//    public static String city;
//    public static String sign;
//    public static int dayNum;
//    public static String vipTimeout;
//
//    public static int userInfoGetCount;
//    public static boolean isAccountChange;
//
//    static {
//
//        userID = Setting.getLong("userID");
//
//        nickname = Setting.getString("nickname");
//        icon = Setting.getString("icon");
//
//
//        sign = Setting.getString("sign");
//        city = Setting.getString("city");
//
//        level = Setting.getInt("level");
//        sex = Setting.getInt("sex");
//        isVip = Setting.getInt("isVip");
//
//        dayNum = Setting.getInt("dayNum");
//        vipTimeout = Setting.getString("vipTimeout");
//
//        if(userID>0) {
//            trySetAlias();
//        }
//    }
//
//    static  void trySetAlias() {
//        //调用JPush API设置Alias
//        //new AddExclusiveAliasTask(userID + "", "DDCAT").execute();
//    }
//
//
//    public static void clear(){
//        userID = 0;
//        nickname="";
//        icon="";
//        sign="";
//        city="";
//        level = 0;
//        sex =0;
//        isVip = 0;
//
//        dayNum =0;
//        vipTimeout="";
//
//        save();
//        isAccountChange=true;
//    }
//
//
////    public static void CheckGPS;
////    public static void TryGPS;
//
//    public static void save() {
//
//        Setting.setLong("userID", userID);
//
//        Setting.setString("nickname", nickname);
//        Setting.setString("icon", icon);
//
//        Setting.setString("sign", sign);
//        Setting.setString("city", city);
//
//        Setting.setInt("level", level);
//        Setting.setInt("sex", sex);
//        Setting.setInt("isVip", isVip);
//        Setting.setInt("dayNum", dayNum);
//
//        Setting.setString("vipTimeout", vipTimeout);
//
//
//        trySetAlias();
//    }
//
//
//
//    //
//    //------------------
//    //
//
//    static String _uuid;
//
//    public static String udid() {
//        if (_uuid == null) {
//            if (_uuid == null) {
//
//                final String id = Setting.getString("DEVICE_UDID");
//
//                if (id != null) {
//                    _uuid = id;
//                } else {
//
//                    final String androidId = android.provider.Settings.Secure.getString(App.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//                    // Use the Android ID unless it's broken, in which case fallback on deviceId,
//                    // unless it's not available, then fallback on a random number which we store
//                    // to a prefs file
//                    try {
//                        if (!"9774d56d682e549c".equals(androidId)) {
//                            _uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
//                        } else {
//                            final String deviceId = ((TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                            _uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
//                        }
//                    } catch (UnsupportedEncodingException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    // Write the value out to the prefs file
//                    Setting.setString("DEVICE_UDID", _uuid);
//                }
//            }
//        }
//        return _uuid;
//    }
//
//    public static String device(){
//        return "Android";
//    }
//
//    public static int addinMaxNum() {
//
//        int max = Session.level + Config.MAX_ADDIN_NUM;
//
//        if (Session.isVip > 0)
//            max += 10;
//
//        if (Session.isVip > 1)
//            max += 10;
//
//        if (Session.isVip > 2)
//            max += 99;
//
//        return max;
//    }
//
//}
