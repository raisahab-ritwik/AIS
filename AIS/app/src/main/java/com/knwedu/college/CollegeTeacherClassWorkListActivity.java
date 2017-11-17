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
import com.knwedu.college.adapter.CollegeAnnouncementAdapter;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Announcement;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;

public class CollegeTeacherClassWorkListActivity extends FragmentActivity {
	private ArrayList<Announcement> classworks;
	private ProgressDialog dialog;
	private TextView header;
	private ListView listView;
	private CollegeAnnouncementAdapter adapter;
	ImageButton addAssignment;
	private Subject subject;
	private Button monthlyWeekly;
	private TextView textEmpty;
	public CollegeDBAdapter mDatabase;
	private boolean internetAvailable = false;
	private String page_title = "";
	String subject_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_teacher_assignment_list);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		mDatabase = ((SchoolApplication)getApplication())
				.getCollegeDatabase();
		subject_id = getIntent().getExtras().getString(
				CollegeConstants.OFFLINE_SUBJECT_ID);
		textEmpty = (TextView) findViewById(R.id.textEmpty);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		listView = (ListView) findViewById(R.id.listview);
		addAssignment = (ImageButton) findViewById(R.id.add_assignment);
		if ((CollegeAppUtils.GetSharedParameter(
				CollegeTeacherClassWorkListActivity.this,
				CollegeConstants.COLLEGE_CLASSWORK_CREATE)).equalsIgnoreCase("0")) {
			addAssignment.setVisibility(View.INVISIBLE);
		}
		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);
		monthlyWeekly = (Button) findViewById(R.id.monthly_weekly_btn);
		monthlyWeekly.setVisibility(View.GONE);
		classworks = new ArrayList<Announcement>();
		internetAvailable = getIntent().getBooleanExtra(CollegeConstants.IS_ONLINE,
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
					CollegeConstants.TANNOUNCEMENT);
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
		}
		if (subject != null) {
			loadData();
		}
		addAssignment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CollegeTeacherClassWorkListActivity.this,
						CollegeTeacherClassWorkActivity.class);
				intent.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
				intent.putExtra(CollegeConstants.TSUBJECT, subject.object.toString());
				intent.putExtra(CollegeConstants.PAGE_TITLE, page_title);
				startActivity(intent);
			}
		});

	}

	private void loadData() {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("id",
				CollegeAppUtils.GetSharedParameter(CollegeTeacherClassWorkListActivity.this,
						"id")));
		/*if (monthlyWeekly.getText().equals(R.string.weekly)) {
			nameValuePairs.add(new BasicNameValuePair("list_type", "1"));
		} else {

			nameValuePairs.add(new BasicNameValuePair("list_type", "2"));
		}*/
		nameValuePairs.add(new BasicNameValuePair("type", "2"));
		nameValuePairs.add(new BasicNameValuePair("sr_id", subject.subject_relation_id));
		new GetAssignmentsAsyntask().execute(nameValuePairs);

	}

	private void handleMonthlyWeekly() {
		if (CollegeAppUtils.GetSharedBoolParameter(
				CollegeTeacherClassWorkListActivity.this,
				CollegeConstants.TEACHERMONTHLYWEEKLYCLASSWORK)) {
			monthlyWeekly.setText(R.string.weekly);
		} else {
			monthlyWeekly.setText(R.string.monthly);
		}
		monthlyWeekly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CollegeAppUtils.GetSharedBoolParameter(
						CollegeTeacherClassWorkListActivity.this,
						CollegeConstants.TEACHERMONTHLYWEEKLYCLASSWORK)) {
					CollegeAppUtils.SetSharedBoolParameter(
							CollegeTeacherClassWorkListActivity.this,
							CollegeConstants.TEACHERMONTHLYWEEKLYCLASSWORK, false);
					monthlyWeekly.setText(R.string.monthly);
					loadData();
				} else {
					CollegeAppUtils.SetSharedBoolParameter(
							CollegeTeacherClassWorkListActivity.this,
							CollegeConstants.TEACHERMONTHLYWEEKLYCLASSWORK, true);
					monthlyWeekly.setText(R.string.weekly);
					loadData();
				}
			}
		});
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(CollegeTeacherClassWorkListActivity.this,
					CollegeClassWorkActivity.class);
			intent.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
			intent.putExtra(CollegeConstants.PAGE_TITLE, page_title);
			if (internetAvailable) {
				intent.putExtra(CollegeConstants.ANNOUNCEMENT,
						classworks.get(arg2).object.toString());
			} else {

				intent.putExtra(CollegeConstants.OFFLINE_ANNOUNCEMENT_ROWID,
						classworks.get(arg2).row_id);
			}
			startActivity(intent);
		}
	};

	
	private class GetAssignmentsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String mode = "classwork";
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeTeacherClassWorkListActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nvp = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					CollegeUrls.api_announcement_view_teacher);
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
						classworks = new ArrayList<Announcement>();
						for (int i = 0; i < array.length(); i++) {
							Announcement assignment = new Announcement(
									array.getJSONObject(i));
							classworks.add(assignment);
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
				if (classworks != null) {
					String temp = null;
					for (int i = 0; i < classworks.size(); i++) {

						classworks.get(i).sub_name = subject.sub_name;
						if (i == 0) {
							classworks.get(0).check = true;
							temp = classworks.get(0).date;
						} else {
							if (!temp.equalsIgnoreCase(classworks.get(i).date)) {
								classworks.get(i).check = true;
								temp = classworks.get(i).date;
							}
						}
					}
					if (classworks.size() > 0) {
						textEmpty.setVisibility(View.GONE);
						adapter = new CollegeAnnouncementAdapter(
								CollegeTeacherClassWorkListActivity.this, classworks);
						listView.setAdapter(adapter);
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
			classworks.clear();
		}

		protected Void doInBackground(String... params) {
			
			mDatabase.open();
			subject = mDatabase.getSubject(subject_id);
			classworks = mDatabase.getAllAnnouncements(2, subject_id);
			mDatabase.close();
			addAssignment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(
							CollegeTeacherClassWorkListActivity.this,
							CollegeTeacherClassWorkActivity.class);
					intent.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
					intent.putExtra(CollegeConstants.OFFLINE_SUBJECT_ID, subject_id);
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

			if (classworks != null) {
				String temp = null;
				for (int i = 0; i < classworks.size(); i++) {
					classworks.get(i).sub_name = subject.sub_name;
					if (i == 0) {
						classworks.get(0).check = true;
						temp = classworks.get(0).created_date;
					} else {
						if (!temp.equalsIgnoreCase(classworks.get(i).created_date)) {
							classworks.get(i).check = true;
							temp = classworks.get(i).created_date;
						}
					}
				}
				if (classworks.size() > 0) {
					textEmpty.setVisibility(View.GONE);
					adapter = new CollegeAnnouncementAdapter(
							CollegeTeacherClassWorkListActivity.this, classworks);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(itemClickListener);
				} else {
					textEmpty
							.setText("No data available in Offline mode");
					textEmpty.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}