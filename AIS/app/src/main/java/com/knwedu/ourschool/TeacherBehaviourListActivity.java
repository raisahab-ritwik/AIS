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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.BehaviourAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Behaviour;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassFellows;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherBehaviourListActivity extends FragmentActivity {
	private ArrayList<Behaviour> behaviour;
	private ProgressDialog dialog;
	private TextView header;
	private ListView listView;
	private BehaviourAdapter adapter;
	ImageButton addAssignment;
	private Subject subject;
	private ClassFellows class_fellow;
	private Button monthlyWeekly;
	private TextView textEmpty;
	public DatabaseAdapter mDatabase;
	private boolean internetAvailable = false;
	private String page_title = "";
	private ArrayList<ClassFellows> fellows;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_teacher_assignment_list);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		//SchoolAppUtils.loadAppLogo(TeacherBehaviourListActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		addAssignment = (ImageButton) findViewById(R.id.add_assignment);
		if ((SchoolAppUtils.GetSharedParameter(
				TeacherBehaviourListActivity.this,
				Constants.ANNOUNCEMENT_ADD_PERMISSION)).equalsIgnoreCase("0")) {
			addAssignment.setVisibility(View.INVISIBLE);
		}
		monthlyWeekly = (Button) findViewById(R.id.monthly_weekly_btn);
		monthlyWeekly.setVisibility(View.GONE);
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
		behaviour = new ArrayList<Behaviour>();
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
			//new OfflineSubjectListenerAsync().execute();
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
					Constants.TANNOUNCEMENT);
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
				String tempclassfellow = getIntent().getExtras().getString(
						Constants.CLASSFELLOW);
				if (temp != null) {
					JSONObject object_classfellow = null;
					try {
						object_classfellow = new JSONObject(tempclassfellow);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (object_classfellow != null) {
						class_fellow = new ClassFellows(object_classfellow);

					}
				}
             
			}
			if (subject != null) {
				loadData();
			}
			addAssignment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(
							TeacherBehaviourListActivity.this,
							TeacherAddBehaviourActivity.class);
					intent.putExtra(Constants.IS_ONLINE, internetAvailable);
					intent.putExtra(Constants.TSUBJECT,
							subject.object.toString());
					intent.putExtra(Constants.CLASSFELLOW,
							class_fellow.object.toString());
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
			nameValuePairs.add(new BasicNameValuePair("student_id",
				class_fellow.id));
		
		new GetAssignmentsAsyntask().execute(nameValuePairs);

	}

	private void handleMonthlyWeekly() {
		if (SchoolAppUtils.GetSharedBoolParameter(
				TeacherBehaviourListActivity.this,
				Constants.TEACHERMONTHLYWEEKLYANNOUNCEMENT)) {
			monthlyWeekly.setText(R.string.weekly);
		} else {
			monthlyWeekly.setText(R.string.monthly);
		}
		monthlyWeekly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SchoolAppUtils.GetSharedBoolParameter(
						TeacherBehaviourListActivity.this,
						Constants.TEACHERMONTHLYWEEKLYANNOUNCEMENT)) {
					SchoolAppUtils.SetSharedBoolParameter(
							TeacherBehaviourListActivity.this,
							Constants.TEACHERMONTHLYWEEKLYANNOUNCEMENT, false);
					monthlyWeekly.setText(R.string.monthly);
					loadData();
				} else {
					SchoolAppUtils.SetSharedBoolParameter(
							TeacherBehaviourListActivity.this,
							Constants.TEACHERMONTHLYWEEKLYANNOUNCEMENT, true);
					monthlyWeekly.setText(R.string.weekly);
					loadData();
				}
			}
		});
	}

	
	

	private class GetAssignmentsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String mode = "class_anouncements";
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherBehaviourListActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			behaviour.clear();
			List<NameValuePair> namevaluepair = params[0];
			Log.d("url extension: ", Urls.api_behaviour_view);
			String parameters = "";
			for (NameValuePair nvp : namevaluepair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_behaviour_view);
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
							Behaviour assignment = new Behaviour(
									array.getJSONObject(i));
							behaviour.add(assignment);
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
				if (behaviour != null) {
					String temp = null;
					
					if (behaviour.size() > 0) {
						textEmpty.setVisibility(View.GONE);
						adapter = new BehaviourAdapter(
								TeacherBehaviourListActivity.this,
								behaviour,"teacher");
						listView.setAdapter(adapter);
						
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

	

}
