package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.adapter.CollegeTeacherAssignmentsAdapter;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;

public class CollegeTeacherAssignmentListActivity extends FragmentActivity {
	private ArrayList<Assignment> assignments;
	private ProgressDialog dialog;
	private ListView listView;
	private CollegeTeacherAssignmentsAdapter adapter;
	public CollegeDBAdapter mDatabase;
	ImageButton addAssignment;
	private Subject subject;
	private Button monthlyWeekly;
	private TextView textEmpty;
	private boolean internetAvailable = false;
	private String page_title = "";
	String subject_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDatabase = ((SchoolApplication) getApplication()).getCollegeDatabase();

		setContentView(R.layout.college_activity_teacher_assignment_list);
		subject_id = getIntent().getExtras().getString(
				CollegeConstants.OFFLINE_SUBJECT_ID);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		TextView header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);

		addAssignment = (ImageButton) findViewById(R.id.add_assignment);
		if ((CollegeAppUtils.GetSharedParameter(
				CollegeTeacherAssignmentListActivity.this,
				CollegeConstants.COLLEGE_ASSIGNMENT_CREATE)).equals("0")) {
			addAssignment.setVisibility(View.INVISIBLE);
		}

		monthlyWeekly = (Button) findViewById(R.id.monthly_weekly_btn);
		
		textEmpty = (TextView) findViewById(R.id.textEmpty);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		listView = (ListView) findViewById(R.id.listview);
		assignments = new ArrayList<Assignment>();
		internetAvailable = getIntent().getBooleanExtra(
				CollegeConstants.IS_ONLINE, false);
		/*if (internetAvailable) {
			initialize();
		} else {
			findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
			monthlyWeekly.setVisibility(View.GONE);
			new OfflineSubjectListenerAsync().execute();
		}*/

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (internetAvailable) {
			initialize();
		} else {
			findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
			monthlyWeekly.setVisibility(View.GONE);
			new OfflineSubjectListenerAsync().execute();
		}
	};

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

	private void initialize() {
		handleButton();
		if (getIntent().getExtras() != null) {

			String temp = getIntent().getExtras().getString(
					CollegeConstants.TASSIGNMENT);
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (object != null) {
					subject = new Subject(object);

				}

			}
			if (subject != null) {
				CollegeTeacherMainActivity.showMonthWeek
						.setVisibility(View.GONE);
				monthlyWeekly.setText(R.string.all);
				handleButton();
				loadData(CollegeConstants.VIEW_ALL);
			}
			addAssignment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(
							CollegeTeacherAssignmentListActivity.this,
							CollegeTeacherAssignmentAddActivity.class);
					intent.putExtra(CollegeConstants.IS_ONLINE,
							internetAvailable);
					intent.putExtra(CollegeConstants.TSUBJECT,
							subject.object.toString());
					intent.putExtra(CollegeConstants.PAGE_TITLE, page_title);
					startActivity(intent);
				}
			});

		}
	}

	private void handleButton() {
		monthlyWeekly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String myStr = monthlyWeekly.getText().toString();
				if ((myStr.equals(getString(R.string.all)))) {
					monthlyWeekly.setText(getString(R.string.monthly));
					loadData(CollegeConstants.VIEW_MONTHLY);
				} else if ((myStr.equals(getString(R.string.weekly)))) {
					monthlyWeekly.setText(getString(R.string.all));
					loadData(CollegeConstants.VIEW_ALL);
				} else if ((myStr.equals(getString(R.string.monthly)))) {
					monthlyWeekly.setText(getString(R.string.weekly));
					loadData(CollegeConstants.VIEW_WEEKLY);
				}
				

			

			}
		});

	}

	private void loadData(int listType) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(getApplicationContext(), "id")));
		nameValuePairs.add(new BasicNameValuePair("list_type", String
				.valueOf(listType)));
		nameValuePairs.add(new BasicNameValuePair("type", "1"));
		nameValuePairs.add(new BasicNameValuePair("sr_id",
				subject.subject_relation_id));
		new GetAssignmentsAsyntask().execute(nameValuePairs);

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(
					CollegeTeacherAssignmentListActivity.this,
					CollegeTeacherAssignmentDetailActivity.class);
			intent.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
			intent.putExtra(CollegeConstants.PAGE_TITLE, page_title);
			if (internetAvailable) {
				intent.putExtra(CollegeConstants.ASSIGNMENT,
						assignments.get(arg2).object.toString());
				intent.putExtra(CollegeConstants.SUBJECT,
						subject.object.toString());
			} else {

				intent.putExtra(CollegeConstants.OFFLINE_SUBJECT_ID,
						subject.subject_relation_id);
				intent.putExtra(CollegeConstants.OFFLINE_ASSIGNMENT_ROWID,
						assignments.get(arg2).row_id);
			}
			startActivity(intent);
		}
	};

	private class GetAssignmentsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(
					CollegeTeacherAssignmentListActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			assignments.clear();
			List<NameValuePair> namevaluepair = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					CollegeUrls.api_activity_get);
			// Log parameters:
			Log.d("url extension", CollegeUrls.api_activity_get);
			String parameters = "";
			for (NameValuePair nvp : namevaluepair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							error = "Error Occured";
							return false;
						}

						for (int i = 0; i < array.length(); i++) {
							Assignment assignment = new Assignment(
									array.getJSONObject(i));
							assignments.add(assignment);
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
					error = "Connection Failure";
				}

			} catch (JSONException e) {
				error = "Connection Failure";
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
				if (assignments != null) {
					String temp = null;
					for (int i = 0; i < assignments.size(); i++) {
						assignments.get(i).sub_name = subject.sub_name;
						if (i == 0) {
							assignments.get(0).check = true;
							temp = assignments.get(0).submit_date;
						} else {
							if (!temp
									.equalsIgnoreCase(assignments.get(i).submit_date)) {
								assignments.get(i).check = true;
								temp = assignments.get(i).submit_date;
							}
						}
					}
					if (assignments.size() > 0) {
						textEmpty.setVisibility(View.GONE);
						adapter = new CollegeTeacherAssignmentsAdapter(
								CollegeTeacherAssignmentListActivity.this,
								assignments,
								subject,
								1,
								internetAvailable,
								CollegeAppUtils
										.GetSharedParameter(
												CollegeTeacherAssignmentListActivity.this,
												CollegeConstants.ASSIGNMENT_MARKING),
								"Assignment");
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						listView.setOnItemClickListener(itemClickListener);
					} else {
						textEmpty.setText("No data available");
						textEmpty.setVisibility(View.VISIBLE);
					}

				}
			} else {
				textEmpty.setText(error.toString());
				textEmpty.setVisibility(View.VISIBLE);
			}
		}

	}

	class OfflineSubjectListenerAsync extends AsyncTask<String, Subject, Void> {

		protected void onPreExecute() {
			super.onPreExecute();
			assignments.clear();
		}

		protected Void doInBackground(String... params) {
			mDatabase.open();
			subject = mDatabase.getSubject(subject_id);
			assignments = mDatabase.getAllAssignments(1, subject_id);
			mDatabase.close();

			addAssignment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent intent = new Intent(
							CollegeTeacherAssignmentListActivity.this,
							CollegeTeacherAssignmentAddActivity.class);
					intent.putExtra(CollegeConstants.IS_ONLINE,
							internetAvailable);
					intent.putExtra(CollegeConstants.OFFLINE_SUBJECT_ID,
							subject_id);
					intent.putExtra(CollegeConstants.PAGE_TITLE, page_title);
					startActivity(intent);

				}
			});

			return null;
		}

		@Override
		protected void onProgressUpdate(Subject... values) {
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (assignments != null) {
				String temp = null;
				for (int i = 0; i < assignments.size(); i++) {
					assignments.get(i).sub_name = subject.sub_name;
					if (i == 0) {
						assignments.get(0).check = true;
						temp = assignments.get(0).submit_date;
					} else {
						if (!temp
								.equalsIgnoreCase(assignments.get(i).submit_date)) {
							assignments.get(i).check = true;
							temp = assignments.get(i).submit_date;
						}
					}
				}
				if (assignments.size() > 0) {
					textEmpty.setVisibility(View.GONE);
					adapter = new CollegeTeacherAssignmentsAdapter(
							CollegeTeacherAssignmentListActivity.this,
							assignments, subject, 1, internetAvailable,
							CollegeAppUtils.GetSharedParameter(
									CollegeTeacherAssignmentListActivity.this,
									CollegeConstants.ASSIGNMENT_MARKING),
							"Assignment");
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(itemClickListener);
				} else {
					textEmpty.setText("No data available in Offline mode");
					textEmpty.setVisibility(View.VISIBLE);
				}

			}
		}

	}

}
