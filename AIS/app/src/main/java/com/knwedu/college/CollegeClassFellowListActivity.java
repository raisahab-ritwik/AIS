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
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.adapter.ClassFellowsAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.ClassFellows;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeClassFellowListActivity extends Activity {
	private ArrayList<ClassFellows> fellows;
	private ProgressDialog dialog;
	private static RelativeLayout child_panel;
	private ListView listView;
	private ClassFellowsAdapter adapter;
	ImageButton addAssignment;
	private String page_title = "", subject_relation_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_class_fellow_list);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		
		subject_relation_id = getIntent().getStringExtra("subject_relation_id");

		// image = (ImageView) findViewById(R.id.image_view);
		// name = (TextView) findViewById(R.id.name_txt);
		// classs = (TextView) findViewById(R.id.class_txt);
		// child_panel = (RelativeLayout) findViewById(R.id.child_panel);
		// setUserImage(ClassFellowListActivity.this);
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

	private void initialize() {

		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		listView = (ListView) findViewById(R.id.listview);

		if (fellows == null) {
		
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getApplicationContext(), "id")));
			nameValuePairs.add(new BasicNameValuePair("sr_id",
					subject_relation_id));

			new GetTeacherAssignmentsAsyntask().execute(nameValuePairs);
		} else {
			adapter = new ClassFellowsAdapter(
					CollegeClassFellowListActivity.this, fellows);
			listView.setAdapter(adapter);
		}
	}

	/*
	 * public static void setUserImage(Context context) { if
	 * (SchoolAppUtils.GetSharedBoolParameter(context,
	 * Constants.ISPARENTSIGNIN)) { child_panel.setVisibility(View.VISIBLE); }
	 * else { child_panel.setVisibility(View.GONE); } JSONObject c = null; try {
	 * c = new JSONObject(SchoolAppUtils.GetSharedParameter(context,
	 * Constants.SELECTED_CHILD_OBJECT)); } catch (JSONException e) {
	 * e.printStackTrace(); } if (c != null) { StudentProfileInfo info = new
	 * StudentProfileInfo(c); name.setText(info.fullname);
	 * classs.setText("Class: " + info.class_name); new
	 * LoadImageAsyncTask(context, image, Urls.image_url_userpic, info.image,
	 * true).execute(); } }
	 */

	private class GetAssignmentsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeClassFellowListActivity.this);
			dialog.setTitle(getResources().getString(R.string.class_fellows));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			fellows = new ArrayList<ClassFellows>();
			List<NameValuePair> url = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					CollegeUrls.api_get_student_by_section);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						fellows = new ArrayList<ClassFellows>();
						for (int i = 0; i < array.length(); i++) {
							ClassFellows assignment = new ClassFellows(
									array.getJSONObject(i));
							fellows.add(assignment);
						}
						return true;
					} else {
						try {
							error = json.getString("data");
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
				if (fellows != null) {
					adapter = new ClassFellowsAdapter(
							CollegeClassFellowListActivity.this, fellows);
					listView.setAdapter(adapter);
				}
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeClassFellowListActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeClassFellowListActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeClassFellowListActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}

	}

	private class GetTeacherAssignmentsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeClassFellowListActivity.this);
			dialog.setTitle(getResources().getString(R.string.class_fellows));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			fellows = new ArrayList<ClassFellows>();
			List<NameValuePair> url = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					CollegeUrls.api_get_student_by_section_teacher);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						fellows = new ArrayList<ClassFellows>();
						for (int i = 0; i < array.length(); i++) {
							ClassFellows assignment = new ClassFellows(
									array.getJSONObject(i));
							fellows.add(assignment);
						}
						return true;
					} else {
						try {
							error = json.getString("data");
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
				if (fellows != null) {
					adapter = new ClassFellowsAdapter(
							CollegeClassFellowListActivity.this, fellows);
					listView.setAdapter(adapter);
				}
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeClassFellowListActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeClassFellowListActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeClassFellowListActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}

	}
}
