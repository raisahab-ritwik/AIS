package com.knwedu.ourschool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.InterviewAdapter;
import com.knwedu.ourschool.adapter.PaymentReminderAdapter;
import com.knwedu.ourschool.adapter.SuccessfulCandidateAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.SuccessfulCandidate;
import com.knwedu.ourschool.utils.DataStructureFramwork.Interview;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CareerInterviewActivity extends FragmentActivity {

	private ProgressDialog dialog;
	private TextView header;
	private String page_title = "";
	private String Url;
	private int type,cadidateType;
	private ArrayList<Interview> interviews = new ArrayList<Interview>();
	private ArrayList<SuccessfulCandidate> successfulCandidates = new ArrayList<SuccessfulCandidate>();
	private ListView listvals;
	private InterviewAdapter adapter;
	private SuccessfulCandidateAdapter successAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.career_type_details_activity);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);
		listvals = (ListView) findViewById(R.id.listviewValues);
		((ImageButton) findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		type = getIntent().getExtras().getInt("Type");
		cadidateType = getIntent().getExtras().getInt("CandidateType");
		setParametersToCallAPI();
	}

	private void setParametersToCallAPI() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(
				new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(this, Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(this, Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(this, Constants.USERID)));
		if (type == 1) {
			nameValuePairs.add(
					new BasicNameValuePair("child_id", SchoolAppUtils.GetSharedParameter(this, Constants.CHILD_ID)));
		}

		new GetInterviewsAsyntask().execute(nameValuePairs);
	};

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	};
	
	private class GetInterviewsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CareerInterviewActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			interviews.clear();
			successfulCandidates.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = null;
			if(cadidateType == 0){
				 json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_interview_list);
				Log.d("url extension: ", Urls.api_interview_list);
			}else{
				json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_success_list);
				Log.d("url extension: ", Urls.api_success_list);
			}

			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");


						if(cadidateType == 0){
							for (int i = 0; i < array.length(); i++) {

								Interview interview = new Interview(array.getJSONObject(i));
								interviews.add(interview);
							}
						}else{
							for (int i = 0; i < array.length(); i++) {

								SuccessfulCandidate successfulCandidate = new SuccessfulCandidate(array.getJSONObject(i));
								successfulCandidates.add(successfulCandidate);
							}
						}

						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}

				} else {
					error = getResources().getString(R.string.unknown_response);
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
				if (interviews != null && interviews.size()>0) {

					adapter = new InterviewAdapter(CareerInterviewActivity.this, interviews);
					listvals.setAdapter(adapter);
				}
				else if (successfulCandidates != null && successfulCandidates.size()>0) {

					successAdapter = new SuccessfulCandidateAdapter(CareerInterviewActivity.this, successfulCandidates);
					listvals.setAdapter(successAdapter);
				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(CareerInterviewActivity.this, page_title, error);
				} else {
					SchoolAppUtils.showDialog(CareerInterviewActivity.this, page_title,
							getResources().getString(R.string.error));
				}

			}
		}

	}
}
