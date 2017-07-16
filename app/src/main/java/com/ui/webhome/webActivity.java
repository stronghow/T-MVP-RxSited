package com.ui.webhome;

import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.C;
import com.RxRouter;
import com.app.annotation.apt.Extra;
import com.app.annotation.apt.Router;
import com.base.DataBindingActivity;
import com.ui.main.R;
import com.ui.main.databinding.ActivityWebBinding;
import com.utils.Base64Util;

import java.net.URI;

/**
 * Created by haozhong on 2017/4/3.
 */
@Router(C.WEB)
public class webActivity extends DataBindingActivity<ActivityWebBinding>{

    @Extra(C.URL)
    public String url;

    private WebView webView;
    private WebSettings wset;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        webView = mViewBinding.webview;
        initWebview();
        webView.loadUrl(url);
    }

    private void initWebview(){
        if(wset == null) {
            wset = webView.getSettings();
            wset.setAllowFileAccessFromFileURLs(true);
            wset.setAllowFileAccess(true);
            wset.setAllowContentAccess(true);
            wset.setAllowUniversalAccessFromFileURLs(true);

            wset.setBuiltInZoomControls(false);
            wset.setSupportZoom(true);
            wset.setJavaScriptEnabled(true);

            wset.setLoadWithOverviewMode(true);

            if(Build.VERSION.SDK_INT >= 19) {
                wset.setLoadsImagesAutomatically(true);
            } else {
                wset.setLoadsImagesAutomatically(false);
                //wset.setCacheMode(WebSettings.LOAD_NO_CACHE);
            }

            webView.requestFocus();
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if(url.indexOf("sited://")>=0){
                        URI uri =  URI.create(url);
                        String host = uri.getHost();

                        int code =  parseInteger(host);

                        if(code>0){
                            //Navigation.navByCode(wseft,code);
                        }else {
                            String webUrl = null;

                            if ("data".equals(host))
                                webUrl = Base64Util.decode(uri.getQuery());
                            else {
                                if (host == null)
                                    host = url.substring(8);

                                webUrl = Base64Util.decode(host);
                            }

                           RxRouter.navByUri(webUrl);
                        }
                    }else{
                        view.loadUrl(url);
                    }
                    return true;
                }
            });
        }
    }

    public static int parseInteger(String value) {
        try {
            if(value.length()>20)
                return 0;

            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
