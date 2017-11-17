package com.knwedu.college;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.knwedu.college.utils.CollegeUtils;
import com.knwedu.comschoolapp.R;

public class CollegeWebviewActivity extends Activity {
	WebView webPage;
	ProgressBar mBar;
	String from_where;
	String announcementUrl;
	String assignmentUrl;
	String classworkUrl;
	String testUrl;
	String teacherAnnouncementUrl;
	String teacherAssignmentUrl;
	String teacherClassworkUrl;
	String teacherTestUrl;
	String teacherWeekLessonUrl;
	String teacherDailyLessonUrl;
	String teacherLessonUrl;
	String DailyLessonUrl;
	String WeeklyLessonUrl;
	String Bulletin;
	String CampusUrl;
	String RequestUrl;
    String BaseUrl = "https://docs.google.com/viewer?url=";
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_webview);
		mBar = (ProgressBar) findViewById(R.id.progressBar1);
		webPage = (WebView) findViewById(R.id.webView);
		webPage.getSettings().setJavaScriptEnabled(true);
		// define url to view
		if (getIntent().getExtras() != null) {
			from_where = getIntent().getExtras().getString("from");
			announcementUrl = getIntent().getExtras().getString(
					"Download_Accouncement");
			RequestUrl = getIntent().getExtras().getString(
					"Download_Request");
			CampusUrl = getIntent().getExtras().getString("Download_Campus");
			assignmentUrl = getIntent().getExtras().getString(
					"Download_Assignment");
			classworkUrl = getIntent().getExtras().getString(
					"Download_Classwork");
			testUrl = getIntent().getExtras().getString("Download_Test");
			teacherAnnouncementUrl = getIntent().getExtras().getString(
					"Download_Teacher_Accouncement");
			teacherAssignmentUrl = getIntent().getExtras().getString(
					"Download_Teacher_Assignment");
			teacherClassworkUrl = getIntent().getExtras().getString(
					"Download_Teacher_Classwork");
			teacherTestUrl = getIntent().getExtras().getString(
					"Download_Teacher_Test");
			teacherWeekLessonUrl = getIntent().getExtras().getString(
					"Download_Teacher_Week_Lesson");
			teacherDailyLessonUrl = getIntent().getExtras().getString(
					"Download_Teacher_Daily_Lesson");
			WeeklyLessonUrl = getIntent().getExtras().getString(
					"Download_Week_Lesson");
			DailyLessonUrl = getIntent().getExtras().getString(
					"Download_Daily_Lesson");
			Bulletin = getIntent().getExtras().getString("Bulletin");

		}
		if (from_where.equalsIgnoreCase("Announcement")) {
			webPage.loadUrl(BaseUrl+announcementUrl);
		} else if (from_where.equalsIgnoreCase("Assignment")) {
			webPage.loadUrl(BaseUrl+assignmentUrl);
		} else if (from_where.equalsIgnoreCase("Classwork")) {
			webPage.loadUrl(BaseUrl+classworkUrl);
		} else if (from_where.equalsIgnoreCase("Test")) {
			webPage.loadUrl(BaseUrl+testUrl);
		} else if (from_where.equalsIgnoreCase("TeacherAssignment")) {
			webPage.loadUrl(BaseUrl+teacherAssignmentUrl);
		} else if (from_where.equalsIgnoreCase("TeacherTest")) {
			webPage.loadUrl(BaseUrl+teacherTestUrl);
		} else if (from_where.equalsIgnoreCase("TeacherAnnouncement")) {
			webPage.loadUrl(BaseUrl+teacherAnnouncementUrl);
		} else if (from_where.equalsIgnoreCase("TeacherWeekLesson")) {
			webPage.loadUrl(BaseUrl+teacherWeekLessonUrl);
		} else if (from_where.equalsIgnoreCase("TeacherDailyLesson")) {
			webPage.loadUrl(BaseUrl+teacherDailyLessonUrl);
		} else if (from_where.equalsIgnoreCase("DailyLesson")) {
			webPage.loadUrl(BaseUrl+DailyLessonUrl);
		} else if (from_where.equalsIgnoreCase("WeekLesson")) {
			webPage.loadUrl(BaseUrl+WeeklyLessonUrl);
		} else if (from_where.equalsIgnoreCase("Bulletin")) {
			webPage.loadUrl(BaseUrl+Bulletin);
		}
		else if (from_where.equalsIgnoreCase("Campus")) {
			webPage.loadUrl(BaseUrl+CampusUrl);
		}
		else if (from_where.equalsIgnoreCase("Request")) {
			webPage.loadUrl(BaseUrl+RequestUrl);
		}
		// classwork,test,announcement,assignment
		CollegeUtils.setupWebView(webPage);
		//webPage.setWebChromeClient(new WebChromeClient());
		webPage.setWebViewClient(new LoadWebViewClient()); /*WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
*/
	}
	private class LoadWebViewClient extends WebViewClient
	{
		ProgressDialog dialog = new ProgressDialog(
				CollegeWebviewActivity.this);
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			// Log.v("shopping", url+" loaded");
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
			dialog.setTitle("Fetching Information...");
			dialog.setMessage("Please Wait...");
			dialog.show();
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			dialog.dismiss();
		}

	}

}
