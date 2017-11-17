package com.knwedu.ourschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.UserInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivacyPolicyActivity extends Activity {
	int touchCount = 0;
	private WebView webView;
	ProgressDialog mProgressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_privacy_policy);
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
		header_text.setText("Privacy Policy");
		webView = (WebView) findViewById(R.id.webView_to_show);
		webView.getSettings().setJavaScriptEnabled(true);

		if (SchoolAppUtils.isOnline(this)) {
			webView.loadUrl(Urls.api_privacy_policy);
		}else{
			SchoolAppUtils.showDialog(this,
					getResources().getString(R.string.privacy), "No Network Connectivity.");
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

