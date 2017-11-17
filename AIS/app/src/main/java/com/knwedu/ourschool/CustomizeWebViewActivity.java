package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CustomizeWebViewActivity extends Activity {
	int touchCount = 0;
	private WebView webView;
	ProgressDialog mProgressDialog;
	private String url,header;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_privacy_policy);
		url = getIntent().getStringExtra("url");
		header = getIntent().getStringExtra("title");
		((ImageButton) findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		initialize();
	}

	private void initialize() {
		mProgressDialog = new ProgressDialog(this);
		TextView header_text = (TextView)findViewById(R.id.header_text);
		header_text.setText(header);
		webView = (WebView) findViewById(R.id.webView_to_show);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);

		if (SchoolAppUtils.isOnline(this)) {
			webView.loadUrl(url);
		}else{
			SchoolAppUtils.showDialog(this,
					"", "No Network Connectivity.");
		}
		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchCount += 1;
				return false;
			}
		});
		webView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				mProgressDialog.dismiss();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mProgressDialog.setMessage("Loading..");
				mProgressDialog.show();
			}
		});

	}

}

