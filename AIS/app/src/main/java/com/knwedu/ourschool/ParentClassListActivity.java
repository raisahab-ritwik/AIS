package com.knwedu.ourschool;

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
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.ParentChildrenAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ParentClassListActivity extends Activity {
	private ListView list;
	private ImageButton addChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_parent_class_list);
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
						ParentClassListActivity.this,
						ParentClassAddActivity.class);
				startActivity(intent);
			}
		});
		list = (ListView) findViewById(R.id.listview);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(ParentClassListActivity.this,
						Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(ParentClassListActivity.this,
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(ParentClassListActivity.this,
						Constants.UORGANIZATIONID)));

		new GetClassSectionAsyntask().execute(nameValuePairs);
	}

	private class GetClassSectionAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		ProgressDialog dialog;
		ArrayList<StudentProfileInfo> studentProfiles;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			studentProfiles = new ArrayList<StudentProfileInfo>();
			dialog = new ProgressDialog(ParentClassListActivity.this);
			dialog.setTitle("Fetching " + getResources().getString(R.string.my_children));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_parent_childrens);

			// Log parameters:
			Log.d("url extension", Urls.api_parent_childrens);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
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
				} else {
					error = getResources().getString(R.string.unknown_response);
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
				final ParentChildrenAdapter adapter = new ParentChildrenAdapter(ParentClassListActivity.this,
						studentProfiles);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						SchoolAppUtils.SetSharedParameter(ParentClassListActivity.this,
								Constants.SELECTED_CHILD_OBJECT,
								studentProfiles.get(arg2).object.toString());
						SchoolAppUtils.SetSharedParameter(ParentClassListActivity.this,
								Constants.CHILD_ID,
								studentProfiles.get(arg2).user_id);

						ParentMainActivity.setUserImage(ParentClassListActivity.this);
						adapter.notifyDataSetChanged();
				
					

					}
				});
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(ParentClassListActivity.this, getResources().getString(R.string.my_children), error);
				} else {
					SchoolAppUtils.showDialog(ParentClassListActivity.this, getResources().getString(R.string.my_children),
							getResources().getString(R.string.unknown_response));
				}

			}
		}

	}

}
