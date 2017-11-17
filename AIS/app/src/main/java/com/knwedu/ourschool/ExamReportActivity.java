package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.ExamsReportAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Examreportcard;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ExamReportActivity extends Activity {
	private ArrayList<Examreportcard> examreportcard;
	private TextView header;
	private ExamsReportAdapter adapter;
	private ProgressDialog dialog;
	private ListView listView;
	private String page_title = "";
	private TextView textnodata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exams_schedule);
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
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initialize() {

		/*
		 * InMobi.initialize(ExamScheduleActivity.this, getResources()
		 * .getString(R.string.InMobi_Property_Id)); IMBanner banner =
		 * (IMBanner) findViewById(R.id.banner); banner.loadBanner();
		 */
		SchoolAppUtils.loadAppLogo(ExamReportActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		listView = (ListView) findViewById(R.id.listview);
		textnodata = (TextView) findViewById(R.id.textnodata);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);

		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);
		loadData();

	}



	private void loadData() {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(ExamReportActivity.this,
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(ExamReportActivity.this,
						Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("user_id", SchoolAppUtils
						.GetSharedParameter(ExamReportActivity.this,
								Constants.USERID)));

		nameValuePairs.add(new BasicNameValuePair("child_id",
				SchoolAppUtils.GetSharedParameter(ExamReportActivity.this,
						Constants.CHILD_ID)));

		new GetExamScheduleAsyntask().execute(nameValuePairs);

	}

	private class GetExamScheduleAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ExamReportActivity.this);
			dialog.setTitle("Report Card");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> nameValuePair = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_report_card);
			String parameters = "";
			for (NameValuePair nvp : nameValuePair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
					Urls.api_report_card);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("report_card");
						} catch (Exception e) {
							return true;
						}
						examreportcard = new ArrayList<Examreportcard>();
						for (int i = 0; i < array.length(); i++) {
							Examreportcard exam = new Examreportcard(
									array.getJSONObject(i));
							examreportcard.add(exam);
						}
						return true;
					} else {
						try {
							error = json.getString("report_card");
						} catch (Exception e) {
						}
						return false;
					}
				}

			} catch (JSONException e) {

			}
			return false;

		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

			if (result) {

				if (examreportcard.size() > 0) {
					adapter = new ExamsReportAdapter(ExamReportActivity.this);
					adapter.notifyDataSetChanged();
					for (int i = 0; i < examreportcard.size(); i++) {
						adapter.addItem(ExamReportActivity.this, examreportcard);
					}
					listView.setAdapter(adapter);
				} else {
					SchoolAppUtils.showDialog(ExamReportActivity.this,
							getTitle().toString(), "No Report Created yet");
				}

				//listView.setOnItemClickListener(null);

			}

			else {

				if (error.length() > 0) {
					//SchoolAppUtils.showDialog(ExamReportActivity.this,page_title, error);
					textnodata.setVisibility(View.VISIBLE);
					textnodata.setText("No Report Created yet");

				} else {
					SchoolAppUtils.showDialog(
							ExamReportActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

}
