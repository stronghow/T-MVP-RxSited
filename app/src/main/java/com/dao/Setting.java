//package com.dao;
//
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//
//import com.App;
//import com.Config;
//import com.model.ViewSetModel;
//import com.utils.theme.BlackTheme;
//import com.utils.theme.ITheme;
//import com.utils.theme.SkinModel;
//import com.utils.theme.WhiteTheme;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//import java.util.regex.Pattern;
//
//import me.noear.exts.Fun1;
//import me.noear.exts.ListExt;
//import me.noear.exts.TimeExt;
//import me.noear.utils.SettingsUtil;
//
//
///**
// * Created by yuety on 15/7/22.
// */
//public class Setting extends SettingsUtil {
//    static {
//        mSets = App.getSettings("setting", Context.MODE_PRIVATE);
//    }
//
//    public static void tryInit(){
//        mSets = App.getSettings("setting", Context.MODE_PRIVATE);
//    }
//
//
//    public static ITheme whiteTheme = null;
//    public static ITheme blackTheme = null;
//    static SkinModel _skin = null;
//
//    public static ITheme theme() {
//        if (whiteTheme == null) {
//            whiteTheme = new WhiteTheme();
//            blackTheme = new BlackTheme();
//
//            _isNightMode      = getBoolean("isNightMode");
//            _isUseShake       = getBoolean("isUseShake",false);
//          //  _isUseLandscape   = getBoolean("isUseLandscape",false);
//        }
//
//        if (isNightMode())
//            return blackTheme;
//        else
//            return whiteTheme;
//    }
//
//    static Boolean _isNightMode = false;
//    public static Boolean isNightMode() {
//        return _isNightMode;
//    }
//    public static void setIsNightMode(Boolean value) {
//        _isNightMode = value;
//        setBoolean("isNightMode", value);
//    }
//
//    static Boolean _isUseShake = false;
//    public static Boolean isUseShake() {
//        return _isUseShake;
//    }
//    public static void setIsUseShake(Boolean value) {
//        _isUseShake = value;
//        setBoolean("isUseShake", value);
//    }
//
//    //只对平板有效
////    static Boolean _isUseLandscape = false;
////    public static Boolean isUseLandscape() {
////        return _isUseLandscape && Config.isPhone()==false;
////    }
////    public static void setIsUseLandscape(Boolean value) {
////        _isUseLandscape = value;
////        setBoolean("isUseLandscape", value);
////    }
//
//    public static Boolean isInitedAddins() {
//        return getBoolean("isInitedAddins");
//    }
//
//    public static void setIsInitedAddins(Boolean value) {
//        setBoolean("isInitedAddins", value);
//    }
//
//
//    public static Boolean isDeveloperModel() {
//        return getBoolean("isDeveloperModel");
//    }
//    public static void setIsDeveloperModel(Boolean value) {
//        setBoolean("isDeveloperModel", value);
//    }
//
//
////    public static Boolean isWebModel(){
////       return getBoolean("isWebModel", Config.isWebModelDefault);
////    }
////    public static void setIsWebModel(Boolean value) {
////        setBoolean("isWebModel", value);
////    }
//
//
//
//    public static Boolean isUseMultiThreadDown(){
//        return getBoolean("isUseMultiThreadDown", true);
//    }
//    public static void setIsUseMultiThreadDown(Boolean value) {
//        setBoolean("isUseMultiThreadDown", value);
//    }
//
//
//    public static Boolean isUsePressSavePic(){
//        return getBoolean("isUsePressSavePic", true);
//    }
//    public static void setIsUsePressSavePic(Boolean value) {
//        setBoolean("isUsePressSavePic", value);
//    }
//
//
//    public static Boolean isUsingImmersive() {
//        return Config.isCanImmersive() && mSets.getBoolean("isUsingImmersive", Config.isAbsImmersive());
//    }
//
//    public static void setIsUsingImmersive(Boolean value) {
//        setBoolean("isUsingImmersive", value);
//    }
//
//
//
//
//    public static Boolean isUseSDCard() {
//        return Config.isSDCardAvailable() && mSets.getBoolean("isUseSDCard", Config.isSDCardAvailable());
//    }
//
//    public static void setIsUseSDCard(Boolean value) {
//        setBoolean("isUseSDCard", value);
//    }
//
//
//
//
//
//    public static String logVer(){
//        return getString("logVer");
//    }
//
//    public static void setLogVer(String logVer)
//    {
//        setString("logVer", logVer);
//    }
//
//    public static int logBuild(){
//        return getInt("logBuild");
//    }
//
//    public static void setLogBuild(int logVer)
//    {
//        setInt("logBuild", logVer);
//    }
//
//
//    public static int filterVer(){
//        return getInt("filterVer",0);
//    }
//
//    public static void setFilterVer(int filterVer)
//    {
//        setInt("filterVer", filterVer);
//    }
//
//    static Pattern _filterPattern;
//    public static Pattern filter() {
//        if (_filterPattern == null) {
//            String temp = mSets.getString("filter", "##");
//
//            _filterPattern = Pattern.compile(temp);
//        }
//
//        return _filterPattern;
//    }
//
//    public static void setFilter(String filter)
//    {
//        _filterPattern = null;
//        setString("filter", filter);
//    }
//
//    // ======================
//    // 使用次数
//    public static int useCount() {
//        return getInt("useCount");
//    }
//
//    public static void addUseCount() {
//        int value = useCount() + 1;
//
//        if (value == 1)
//            setString("useBeginTime",
//                    TimeExt.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
//
//        setInt("useCount", value);
//    }
//
//    public static String useBeginTime() {
//        return getString("useBeginTime");
//    }
//
//    public static Boolean isRootAsHub(){
//        return getBoolean("isRootIsHub");
//    }
//
//    public static void setIsRootAsHub(boolean isRootIsHub){
//        setBoolean("isRootIsHub",isRootIsHub);
//    }
//
//
//    public static Boolean isLocalSite(){
//        return Config.IS_ONLY_TOOL_MODEL() || getBoolean("isLocalSite",false);
//    }
//
//    public static void setIsLocalSite(boolean isLocalSite){
//        setBoolean("isLocalSite",isLocalSite);
//    }
//
//    public static Boolean isHintUpdate() {
//        return getBoolean("isHintUpdate");
//    }
//
//    public static void setIsHintUpdate(float value) {
//        setFloat("isHintUpdate", value);
//    }
//
//    public static String password() {
//        return getString("password");
//    }
//
//    public static void setPassword(String value) {
//        setString("password", value);
//    }
//
//
//    public static Boolean isReviewed() {
//        return getBoolean("isReviewed");
//    }
//
//    public static void setIsReviewed(Boolean value) {
//        setBoolean("isReviewed", value);
//    }
//
//    public static Boolean isSecretModel() {
//        return getBoolean("isSecretModel",false);
//    }
//
//    public static void setIsSecretModel(Boolean value) {
//        setBoolean("isSecretModel", value);
//    }
//
//
//    // =================
//
//    public static String randomColorUsed() {
//        String temp = getString("randomColorUsed");
//
//        if(temp == null)
//            return "";
//        else
//            return temp;
//    }
//
//    public static void setRandomColorUsed(String value) {
//        setString("randomColorUsed", value);
//    }
//
//    public static Boolean isRandomColor() {
//        return getBoolean("isRandomColor");
//    }
//
//    public static void setIsRandomColor(Boolean value) {
//        setBoolean("isRandomColor", value);
//        if(value){
//            _skin=null;
//        }
//    }
//
//    public  static boolean isUseAllAddinSearch(){
//        return getBoolean("isUseAllAddinSearch",false);
//    }
//
//    public static void setIsUseAllAddinSearch(boolean val){
//        setBoolean("isUseAllAddinSearch",val);
//    }
//
//    public  static boolean isUseAllAddinCheck(){
//        return getBoolean("isUseAllAddinCheck",false);
//    }
//
//    public static void setIsUseAllAddinCheck(boolean val){
//        setBoolean("isUseAllAddinCheck",val);
//    }
//
//    public static void skinReset(){
//        _skin=null;
//    }
//
//    public static SkinModel skin() {
//        if (_skin == null) {
//            if (isRandomColor()) {
//                List<String> used = new ArrayList<String>();
//                used.addAll(Arrays.asList(randomColorUsed().split(",")));
//                List<SkinModel> skins = SkinModel.getSkins();
//
//                int max = skins.size();
//                int idx = 0;
//                Random rand = new Random();
//                while (true) {
//                    idx = rand.nextInt(max);
//                    String no = String.valueOf(idx);
//                    if (used.size() < 7 && used.contains(no))
//                        continue;
//                    else {
//                        used.add(no);
//                        if (used.size() >= 7)
//                            used.clear();
//
//                        setRandomColorUsed(ListExt.join(used, ","));
//                        break;
//                    }
//                }
//
//                if(idx>=max)
//                    idx=max-1;
//
//                _skin = skins.get(idx);
//            } else {
//                final int skinID = getInt("skin");
//                if (skinID > 0) {
//                    List<SkinModel> temp = SkinModel.getSkins();
//                    _skin = ListExt.find(temp, new Fun1<Boolean, SkinModel>() {
//                        @Override
//                        public Boolean run(SkinModel m1) {
//                            return m1.skinID == skinID;
//                        }
//                    });
//                }
//            }
//        }
//
//        if (_skin == null)
//            _skin = SkinModel.def();
//
//        return _skin;
//    }
//
//
//    public static class Section
//    {
//
//        //亮度值(手动模式下)
//        public static int lightValue() {
//            return getInt("lightValue",-1);
//        }
//
//        public static void setLightValue(int value) {
//            setInt("lightValue", value);
//        }
//
//        //是否使用系统亮度
//        public static boolean useSysLight(){
//            return getBoolean("useSysLight",true);
//        }
//        public static void setUseSysLight(boolean value) {
//            setBoolean("useSysLight", value);
//        }
//
//        public static boolean showTitle() {
//            return mSets.getBoolean("showTitle", true);
//        }
//        public static void setShowTitle(boolean value) {
//            setBoolean("showTitle", value);
//        }
//
//        public static boolean showLine() {
//            return mSets.getBoolean("showPicLine", true);
//        }
//        public static void setShowLine(boolean value) {
//            setBoolean("showPicLine", value);
//        }
//
//
//        //
//        //-----------------------------------------------------
//        //
//        public static Boolean isLandscape(ViewSetModel set) {
//            if (set.view_orientation < 0)
//                set.view_orientation = orientation();
//
//            return set.view_orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        }
//
//        public static int orientation() {
//            return getInt("view_orientation", 1);//0横屏；1竖屏
//        }
//
//        public static void setOrientation(int value) {
//            setInt("view_orientation", value);
//        }
//
//        public final static int MODEL_FLOW = 0;
//        public final static int MODEL_CLICK = 1;
//        public final static int MODEL_CLICK_PUSH = 2;
//
//        public final static int DIRECTION_T2B = 0;
//        public final static int DIRECTION_L2R = 1;
//        public final static int DIRECTION_R2L = 2;
//
//        public final static int SCALE_SCREEN = 0;//不超界
//        public final static int SCALE_MAX    = 1;//单边最大化
//        public final static int SCALE_IMAGE  = 2;//原始大小
//
//        //0瀑布流；1点击与手势
//        public static int model() {
//            return getInt("view_model",0);
//        }
//
//        public static void setModel(int value) {
//            setInt("view_model", value);
//        }
//
//        public static int direction(){
//            return getInt("view_direction",0);
//        }
//
//        public static void setDirection(int direction) {
//            setInt("view_direction", direction);
//        }
//
//        public static int scale(){
//            return getInt("view_scale",0);
//        }
//
//        public static void setScale(int scale) {
//            setInt("view_scale", scale);
//        }
//
//    }
//
//    public static class Book{
//        public static boolean isSortUp(){
//            return getBoolean("book_isSortUp");
//        }
//        public static void setIsSortUp(boolean isSortUp){
//            setBoolean("book_isSortUp",isSortUp);
//        }
//    }
//
//    public static class Down{
//        public static Boolean isDownallFromBottom() {
//            return getBoolean("isDownallFromBottom");
//        }
//
//        public static void setIsDownallFromBottom(Boolean value) {
//            setBoolean("isDownallFromBottom", value);
//        }
//
//        public static Boolean isWIFIOnlyDown() {
//            return mSets.getBoolean("isWIFIOnlyDown", true);
//        }
//
//        public static void setIsWIFIOnlyDown(Boolean value) {
//            setBoolean("isWIFIOnlyDown", value);
//        }
//
//        public static Boolean isUsingSDCard() {
//            return getBoolean("isUsingSDCard");
//        }
//
//        public static void setIsUsingSDCard(Boolean value) {
//            setBoolean("isUsingSDCard", value);
//        }
//    }
//}
