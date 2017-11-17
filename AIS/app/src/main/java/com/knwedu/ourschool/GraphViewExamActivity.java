package com.knwedu.ourschool;

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
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class GraphViewExamActivity extends Activity {
	private String exam_term_id, api_url;
	private WebView webview;
	private Button btn;
	private ProgressBar progressBar;
	private int exam_type;
	private TextView headerText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_graph);
		SchoolAppUtils.loadAppLogo(GraphViewExamActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
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
		InMobi.initialize(GraphViewExamActivity.this, getResources().getString(R.string.InMobi_Property_Id));
		IMBanner banner = (IMBanner) findViewById(R.id.banner);
		banner.loadBanner();
		webview = (WebView) findViewById(R.id.webview);
		btn = (Button) findViewById(R.id.btn);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		headerText = (TextView) findViewById(R.id.header_text);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		if (getIntent().getExtras() != null) {
			exam_term_id = getIntent().getStringExtra("exam_term_id");
			api_url = getIntent().getStringExtra("api_url");
		}
		
		// Pie=1, column =2
		btn.setText("Column Graph");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (btn.getText().toString().equalsIgnoreCase("Column Graph")) {
					btn.setText("Pie Graph");
					setData(2);
				} else {
					btn.setText("Column Graph");
					setData(1);

				}
			}
		});
		setData(1);
		btn.setVisibility(View.GONE);
	}

	private void setData(int position) {
		
		/*if( position == 1){
			headerText.setText("Pie Chart");
		}else{
			headerText.setText("Column Chart");
		}*/

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		double height = displaymetrics.heightPixels;
		double width = displaymetrics.widthPixels;
		/*
		 * String url = null; if(fromAssignment) {
		 * //http://64.150.189.99/mobility
		 * /webservices/graphs.php?mode=test_comparison
		 * &email=avishek@smonte.com&
		 * password=smonte&subject_id=5&graph_type=1&height=500&width=500 url =
		 * URLs.GetAssignmentGraph+ "subject_id=" + subject_id + "&"; } else
		 * if(fromTest) {
		 * //http://64.150.189.99/mobility/webservices/graphs.php?
		 * mode=test_comparison
		 * &email=avishek@smonte.com&password=smonte&subject_id
		 * =5&graph_type=1&height=500&width=500 url = URLs.GetTestGraph+
		 * "subject_id=" + subject_id + "&"; } else if(fromExamsTerm) { url =
		 * URLs.GetTermExamGraph + "term_id=" + subject_id + "&"; } else { url =
		 * URLs.GetExamGraph; }
		 */
		/*
		 * url = url + "email="
		 * +SchoolAppUtils.GetSharedParameter(GraphViewActivity.this,
		 * Constants.USERNAME) + "&password="
		 * +SchoolAppUtils.GetSharedParameter(GraphViewActivity.this,
		 * Constants.PASSWORD) + "&graph_type=" + position;; if(position == 1) {
		 * url = url + "&height=" + (height/3) + "&width=" + (width/2); } else {
		 * url = url + "&height=" + (height/3) + "&width=" + (width/2); }
		 */

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				9);
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SchoolAppUtils.GetSharedParameter(
						GraphViewExamActivity.this,
						Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(
						GraphViewExamActivity.this,
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair(
				"organization_id", SchoolAppUtils
						.GetSharedParameter(
								GraphViewExamActivity.this,
								Constants.UORGANIZATIONID)));
		
		nameValuePairs.add(new BasicNameValuePair(
				"student_id", SchoolAppUtils
						.GetSharedParameter(
								GraphViewExamActivity.this,
								Constants.CHILD_ID)));
	
	
		nameValuePairs.add(new BasicNameValuePair("exam_term_id", exam_term_id));
		
		nameValuePairs.add(new BasicNameValuePair("height", "" + (int)(height/3)));
		nameValuePairs.add(new BasicNameValuePair("width", "" + (int)(width/2)));

		
		 String postData = "";
	     for (NameValuePair nvp : nameValuePairs) {
	    	 postData += nvp.getName() + "=" + nvp.getValue() + "&";
			}
	     
	     if(postData.endsWith("&")){
	    	 postData = postData.substring(0, (postData.length()-1));
	     }
	     
	    	
	   webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
			webview.setWebViewClient(new WebViewClient() {
				ProgressDialog dialog = new ProgressDialog(GraphViewExamActivity.this);
				
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				//progressBar.setVisibility(View.VISIBLE);
				dialog.setTitle("Loading Analytics");
				dialog.setMessage("Please Wait...");
				dialog.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				//progressBar.setVisibility(View.GONE);
				dialog.dismiss();
			}
			
		});
		webview.postUrl(Urls.base_url + api_url, EncodingUtils.getBytes(postData, "BASE64"));
	  	
	}

}
