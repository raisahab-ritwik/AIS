package com.knwedu.college.utils;

import java.io.InputStream;
import java.io.OutputStream;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
/**
 * This class handles copying
 * @author Saad
 *
 */
public class CollegeUtils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    public static void setupWebView(WebView mWebView)
	{
		mWebView.setBackgroundColor(0);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.getSettings().setDomStorageEnabled(true); //
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.setScrollbarFadingEnabled(false);
		mWebView.setPadding(0, 0, 0, 0);
	}
}