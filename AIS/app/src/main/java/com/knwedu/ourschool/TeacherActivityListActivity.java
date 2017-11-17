package com.knwedu.ourschool;

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
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.TeacherAssignmentsAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherActivityListActivity extends FragmentActivity {
	private ArrayList<Assignment> activities;
	private ProgressDialog dialog;
	private ListView listView;
	private TeacherAssignmentsAdapter adapter;
	public DatabaseAdapter mDatabase;
	ImageButton addActivity;
	private Subject subject;
	private Button monthlyWeekly;
	private TextView textEmpty;
	private TextView header;
	private boolean internetAvailable = false;
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		setContentView(R.layout.activity_teacher_assignment_list);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(TeacherActivityListActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
		addActivity = (ImageButton) findViewById(R.id.add_assignment);
		if ((SchoolAppUtils.GetSharedParameter(
				TeacherActivityListActivity.this,
				Constants.ACTIVITY_ADD_PERMISSION)).equalsIgnoreCase("0")) {
			addActivity.setVisibility(View.INVISIBLE);
		}
		monthlyWeekly = (Button) findViewById(R.id.monthly_weekly_btn);
		textEmpty = (TextView) findViewById(R.id.textEmpty);
		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		listView = (ListView) findViewById(R.id.listview);
		activities = new ArrayList<Assignment>();
		internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
				false);

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

		handleMonthlyWeekly();
		
		if (getIntent().getExtras() != null) {

			String temp = getIntent().getExtras().getString(
					Constants.TASSIGNMENT);
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object != null) {
					subject = new Subject(object);

				}

			}
			if (subject != null) {
				loadData();
			}
			addActivity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(
							TeacherActivityListActivity.this,
							TeacherActivityAddActivity.class);
					intent.putExtra(Constants.IS_ONLINE, internetAvailable);
					intent.putExtra(Constants.TSUBJECT,
							subject.object.toString());
					intent.putExtra(Constants.PAGE_TITLE, page_title);
					startActivity(intent);
				}
			});

		}
	}

	private void loadData() {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getApplicationContext(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getApplicationContext(),
						Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("user_id", SchoolAppUtils
						.GetSharedParameter(getApplicationContext(),
								Constants.USERID)));
		if (monthlyWeekly.getText().toString()
				.equals(getString(R.string.weekly))) {
			nameValuePairs.add(new BasicNameValuePair("list_type", "1"));
		} else if (monthlyWeekly.getText().toString()
				.equals(getString(R.string.monthly))) {

			nameValuePairs.add(new BasicNameValuePair("list_type", "2"));
		} else {
			nameValuePairs.add(new BasicNameValuePair("list_type", "3"));
		}
		nameValuePairs.add(new BasicNameValuePair("type", "3"));
		nameValuePairs.add(new BasicNameValuePair("sub_id", subject.id));
		//nameValuePairs
				//.add(new BasicNameValuePair("class_id", subject.class_id));

		nameValuePairs.add(new BasicNameValuePair("section_id", subject.section_id));

		new GetAssignmentsAsyntask().execute(nameValuePairs);

	}

	private void handleMonthlyWeekly() {

		monthlyWeekly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String myStr = monthlyWeekly.getText().toString();
				if ((myStr.equals(getString(R.string.all)))) {
					monthlyWeekly.setText(getString(R.string.weekly));
				} else if ((myStr.equals(getString(R.string.weekly)))) {
					monthlyWeekly.setText(getString(R.string.monthly));
				} else if ((myStr.equals(getString(R.string.monthly)))) {
					monthlyWeekly.setText(getString(R.string.all));
				}
				loadData();
			}
		});
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(TeacherActivityListActivity.this,
					TeacherActivityDetailActivity.class);
			intent.putExtra(Constants.IS_ONLINE, internetAvailable);
			intent.putExtra(Constants.PAGE_TITLE, page_title);
			if (internetAvailable) {
				intent.putExtra(Constants.ASSIGNMENT,
						activities.get(arg2).object.toString());
				intent.putExtra(Constants.SUBJECT, subject.object.toString());
			} else {

				intent.putExtra(Constants.OFFLINE_SUBJECT_ID, subject.id);
				intent.putExtra(Constants.OFFLINE_ASSIGNMENT_ROWID,
						activities.get(arg2).row_id);
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
			dialog = new ProgressDialog(TeacherActivityListActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			activities.clear();
			List<NameValuePair> namevaluepair = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_activity_get);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							error = "Error in Data";
							return false;
						}

						for (int i = 0; i < array.length(); i++) {
							Assignment activity = new Assignment(
									array.getJSONObject(i));
							activities.add(activity);
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
				if (activities != null) {
					String temp = null;
					for (int i = 0; i < activities.size(); i++) {
						activities.get(i).sub_name = subject.sub_name;
						if (i == 0) {
							activities.get(0).check = true;
							temp = activities.get(0).submit_date;
						} else {
							if (!temp
									.equalsIgnoreCase(activities.get(i).submit_date)) {
								activities.get(i).check = true;
								temp = activities.get(i).submit_date;
							}
						}
					}
					if (activities.size() > 0) {
						textEmpty.setVisibility(View.GONE);
						adapter = new TeacherAssignmentsAdapter(
								TeacherActivityListActivity.this, activities,
								subject, 3, true,
								SchoolAppUtils.GetSharedParameter(
										TeacherActivityListActivity.this,
										Constants.ACTIVITY_MARK_PERMISSION), page_title);
						listView.setAdapter(adapter);
					//	listView.setOnItemClickListener(itemClickListener);
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
			activities.clear();
		}

		protected Void doInBackground(String... params) {
			final String subject_id = getIntent().getExtras().getString(
					Constants.OFFLINE_SUBJECT_ID);
			mDatabase.open();
			subject = mDatabase.getSubject(subject_id);
			activities = mDatabase.getAllAssignments(3, subject_id);
			mDatabase.close();

			addActivity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(
							TeacherActivityListActivity.this,
							TeacherActivityAddActivity.class);
					intent.putExtra(Constants.IS_ONLINE, internetAvailable);
					intent.putExtra(Constants.OFFLINE_SUBJECT_ID, subject_id);
					intent.putExtra(Constants.PAGE_TITLE, page_title);
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
			if (activities != null) {
				String temp = null;
				for (int i = 0; i < activities.size(); i++) {
					activities.get(i).sub_name = subject.sub_name;
					if (i == 0) {
						activities.get(0).check = true;
						temp = activities.get(0).submit_date;
					} else {
						if (!temp
								.equalsIgnoreCase(activities.get(i).submit_date)) {
							activities.get(i).check = true;
							temp = activities.get(i).submit_date;
						}
					}
				}
				if (activities.size() > 0) {
					textEmpty.setVisibility(View.GONE);
					adapter = new TeacherAssignmentsAdapter(
							TeacherActivityListActivity.this, activities,
							subject, 3, false,
							SchoolAppUtils.GetSharedParameter(
									TeacherActivityListActivity.this,
									Constants.ACTIVITY_MARK_PERMISSION), page_title);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					listView.setOnItemClickListener(itemClickListener);
				} else {
					textEmpty.setText("No data available in Offline mode");
					textEmpty.setVisibility(View.VISIBLE);
				}

			}
		}

	}

}
