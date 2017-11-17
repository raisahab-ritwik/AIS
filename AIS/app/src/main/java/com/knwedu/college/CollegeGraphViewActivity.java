package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeGraphViewActivity extends Activity {
	private String subject_id, object_id;
	private WebView webview;
	private Button btn;
	private ProgressBar progressBar;
	private String assignment_type;
	private TextView headerText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_graph);
		initialize();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("Google Analytics", "Tracking Start");
		EasyTracker.getInstance(this).activityStart(this);

	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("Google Analytics", "Tracking Stop");
		EasyTracker.getInstance(this).activityStop(this);
	}

	private void initialize() {
		webview = (WebView) findViewById(R.id.webview);
		btn = (Button) findViewById(R.id.btn);
		btn.setVisibility(View.GONE);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		headerText = (TextView) findViewById(R.id.header_text);
		headerText.setText("Compare Marks:");
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		if (getIntent().getExtras() != null) {
			subject_id = getIntent().getStringExtra("subject_id");
			object_id = getIntent().getStringExtra("object_id");
			assignment_type = getIntent().getStringExtra(CollegeConstants.ASSIGNMENT_TYPE);
		}

		// Pie=1, column =2
		/*
		 * btn.setText("Column Graph"); btn.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if
		 * (btn.getText().toString().equalsIgnoreCase("Column Graph")) {
		 * btn.setText("Pie Graph"); setData(2); } else {
		 * btn.setText("Column Graph"); setData(1);
		 * 
		 * } } });
		 */setData(1);
	}

	private void setData(int position) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		double height = displaymetrics.heightPixels;
		double width = displaymetrics.widthPixels;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(CollegeGraphViewActivity.this, "id")));
		nameValuePairs.add(new BasicNameValuePair("module_type",assignment_type));
		nameValuePairs.add(new BasicNameValuePair("object_id", object_id));
		nameValuePairs.add(new BasicNameValuePair("height", ""
				+ (int) (height / 3)));
		nameValuePairs.add(new BasicNameValuePair("width", ""
				+ (int) (width / 2)));
		Log.d("PARAMS OF GRAPH...", ""+nameValuePairs);
		String postData = "";
		for (NameValuePair nvp : nameValuePairs) {
			postData += nvp.getName() + "=" + nvp.getValue() + "&";
		}
		if (postData.endsWith("&")) {
			postData = postData.substring(0,(postData.length() - 1));
		}
		Log.d("Graph API", postData);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.setWebViewClient(new WebViewClient() {
			ProgressDialog dialog = new ProgressDialog(
					CollegeGraphViewActivity.this);

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// progressBar.setVisibility(View.VISIBLE);
				dialog.setTitle("Loading Analytics");
				dialog.setMessage("Please Wait...");
				dialog.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// progressBar.setVisibility(View.GONE);
				dialog.dismiss();
			}
		});
		webview.postUrl(CollegeUrls.base_url + CollegeUrls.api_graph,
				EncodingUtils.getBytes(postData, "BASE64"));

	}

}
