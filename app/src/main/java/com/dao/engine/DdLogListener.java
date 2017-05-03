package com.dao.engine;


import com.utils.LogWriter;

import org.noear.sited.SdLogListener;
import org.noear.sited.SdSource;

/**
 * Created by yuety on 16/2/1.
 */
public class DdLogListener implements SdLogListener {
    @Override
    public void run(SdSource source, String tag, String msg, Throwable tr) {
//        if(Setting.isDeveloperModel()) {

            LogWriter.tryInit();

            LogWriter.loger.print(tag, msg, tr);

            if ("JsEngine.print".equals(tag)) {
                //HintUtil.show(msg);
                LogWriter.jsprint.print(tag, msg, tr);
            }

            if (tr != null) {
                LogWriter.error.print(source.url + "::\r\n" + tag, msg, null);
            }
//        }
    }
}
