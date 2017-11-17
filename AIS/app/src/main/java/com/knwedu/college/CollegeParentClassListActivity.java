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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.adapter.CollegeParentChildrenAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StudentProfileInfo;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeParentClassListActivity extends Activity {
	private ListView list;
	private CollegeParentChildrenAdapter adapter;
	private ImageButton addChild;
	private ProgressDialog dialog;
	private ArrayList<StudentProfileInfo> studentProfiles;
	StudentProfileInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_parent_class_list);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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

		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		addChild = (ImageButton) findViewById(R.id.add_child);
		addChild.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				  Intent intent = new Intent(
				  CollegeParentClassListActivity.this,
				  CollegeParentClassAddActivity.class); startActivity(intent);
				 
			}
		});
		list = (ListView) findViewById(R.id.listview);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs
				.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(
								CollegeParentClassListActivity.this, "id")));
		new GetChildrenAsyntask().execute(nameValuePairs);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			for (int i = 0; i < arg0.getChildCount(); i++) {
				if (i == arg2) {
					arg0.getChildAt(i).setBackgroundResource(
							R.drawable.child_on);
				} else {
					arg0.getChildAt(i).setBackgroundResource(
							R.drawable.child_off);
				}
			}

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(CollegeParentClassListActivity.this,
							"id")));
			nameValuePairs.add(new BasicNameValuePair("child_id",
					studentProfiles.get(arg2).id));
			new GetSwapAsyntask().execute(nameValuePairs);

		}
	};

	private class GetChildrenAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentClassListActivity.this);
			dialog.setTitle(getResources().getString(R.string.children));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_swap_children);

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_swap_children);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
						studentProfiles = new ArrayList<StudentProfileInfo>();
						for (int i = 0; i < array.length(); i++) {
							StudentProfileInfo studentProfile = new StudentProfileInfo(
									array.getJSONObject(i));
							studentProfiles.add(studentProfile);
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
				e.printStackTrace();

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
				adapter = new CollegeParentChildrenAdapter(
						CollegeParentClassListActivity.this, studentProfiles);
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				list.setOnItemClickListener(listener);
			} else {
				if (studentProfiles != null) {
					adapter = new CollegeParentChildrenAdapter(
							CollegeParentClassListActivity.this,
							studentProfiles);
					list.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					list.setOnItemClickListener(listener);

				}
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeParentClassListActivity.this, getTitle()
										.toString(), error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeParentClassListActivity.this,
										getTitle().toString(),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeParentClassListActivity.this,
							getTitle().toString(),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}
	}

	private class GetSwapAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		int position;

		/*
		 * public GetAssignmentAsyntask() { this.position = position; }
		 */

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentClassListActivity.this);
			dialog.setTitle(getResources().getString(R.string.swap));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			// studentProfiles = new ArrayList<StudentProfileInfo>();
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_swap_children);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_swap_child);
			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONObject jsonObj = json.getJSONObject("childs");
						user = new StudentProfileInfo(jsonObj);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {

				CollegeParentMainActivity.name.setText(user.name);
				CollegeParentMainActivity.classs.setText(user.class_name);
				CollegeAppUtils.SetSharedParameter(
						CollegeParentClassListActivity.this,
						"session_student_id", user.id);
				CollegeAppUtils.SetSharedParameter(
						CollegeParentClassListActivity.this, "student_name",
						user.name);
				CollegeAppUtils.SetSharedParameter(
						CollegeParentClassListActivity.this, "student_class",
						user.class_name);
				CollegeAppUtils.SetSharedParameter(
						CollegeParentClassListActivity.this, "student_image",
						user.image);

			} else {

			}
		}

	}

}
