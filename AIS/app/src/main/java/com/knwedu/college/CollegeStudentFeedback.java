package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.college.adapter.CollegeFeedbackAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Feedback;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeStudentFeedback extends Activity {
	private View view;
	private ArrayList<Feedback> feedbcak;
	private ProgressDialog dialog;
	private ListView listView;
	private CollegeFeedbackAdapter adapter;
	ImageButton addAssignment;
	private Subject subject;
	private Button monthlyWeekly;
	private TextView textEmpty;
	private String from_where;
	private boolean internetAvailable = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_feedback_list_fragment);
		from_where = getIntent().getExtras().getString("from");
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (from_where.equalsIgnoreCase("Student")) {
							CollegeMainActivity.mDrawerLayout
									.openDrawer(CollegeMainActivity.mDrawerList);
							onBackPressed();
						} else if (from_where.equalsIgnoreCase("Parent")) 
						{
							CollegeParentMainActivity.mDrawerLayout
									.openDrawer(CollegeParentMainActivity.mDrawerList);
							onBackPressed();
						}
					}
				});
		addAssignment = (ImageButton) findViewById(R.id.add_assignment);
		if ((CollegeAppUtils.GetSharedParameter(
				CollegeStudentFeedback.this,
				CollegeConstants.COLLEGE_FEEDBACK_CREATE)).equals("0")) {
			addAssignment.setVisibility(View.INVISIBLE);
		}
		addAssignment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(CollegeStudentFeedback.this,
						CollegeFeedbackActivity.class);
				startActivity(i);
			}
		});

		if (CollegeAppUtils.GetSharedParameter(CollegeStudentFeedback.this,
				CollegeConstants.USERTYPEID).equalsIgnoreCase(
				CollegeConstants.USERTYPE_PARENT)) {
			addAssignment.setVisibility(View.GONE);
		}
		monthlyWeekly = (Button) findViewById(R.id.monthly_weekly_btn);
		textEmpty = (TextView) findViewById(R.id.textEmpty);
		listView = (ListView) findViewById(R.id.listview);
		feedbcak = new ArrayList<Feedback>();
		// loadData();
	}

	private void loadData() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(CollegeStudentFeedback.this, "id")));
		new FeedbackAsync().execute(nameValuePairs);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	private class FeedbackAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeStudentFeedback.this);
			dialog.setTitle("Feedback");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			feedbcak.clear();
			List<NameValuePair> namevaluepair = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					CollegeUrls.api_student_feedback);

			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							feedbcak = new ArrayList<Feedback>();
							for (int i = 0; i < array.length(); i++) {
								Feedback assignment = new Feedback(
										array.getJSONObject(i));
								feedbcak.add(assignment);
							}
						} catch (Exception e) {

						}
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {

				if (feedbcak != null) {
					String temp = null;
					adapter = new CollegeFeedbackAdapter(
							CollegeStudentFeedback.this, feedbcak);
					listView.setAdapter(adapter);
					// listView.setOnItemClickListener(listener);
				}

			}
		}
	}
}
