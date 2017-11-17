package com.knwedu.ourschool;

import com.knwedu.comschoolapp.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

	private WebView mWebView;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		url = getIntent().getStringExtra("url");
		System.out.println("URL : "+url);
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.setWebViewClient(new WebViewClient() {
			ProgressDialog dialog = new ProgressDialog(WebViewActivity.this);

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// progressBar.setVisibility(View.VISIBLE);
				dialog.setTitle("Fetching information");
				dialog.setMessage("Please Wait...");
				dialog.show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// view.loadUrl(url);

				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// progressBar.setVisibility(View.GONE);
				dialog.dismiss();
			}
		});

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setDomStorageEnabled(true);

		System.out.println("Modified URL" + url);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();

		String data = "<body style='margin:0px; padding:0px;'><center><img width=\"" + width + "\" src=\""
				+ url.substring(0, url.lastIndexOf('/')) + "\" /></center></body></html>";
		String extension = url.substring(url.length() - 4);
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase("jpeg")) {
			mWebView.loadData(data, "text/html", null);
		} else {
			mWebView.loadUrl(
					"https://docs.google.com/gview?embedded=true&url=" + url.substring(0, url.lastIndexOf('/')));
		}
		//setContentView(mWebView);
	}

}
