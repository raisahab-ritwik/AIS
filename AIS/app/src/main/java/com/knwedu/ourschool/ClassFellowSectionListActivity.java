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
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.ClassFellowsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassFellows;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.LoadImageAsyncTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ClassFellowSectionListActivity extends Activity {
	private ArrayList<ClassFellows> fellows;
	private ProgressDialog dialog;
	private String section_id,page_title;
	private static ImageView image;
	private static TextView name,header;
	private static TextView classs;
	private static RelativeLayout child_panel;
	private Subject subject;
	private ListView listView;
	private boolean internetAvailable = false;
	private ClassFellowsAdapter adapter;
	ImageButton addAssignment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_class_fellow_list);
		SchoolAppUtils.loadAppLogo(ClassFellowSectionListActivity.this, (ImageButton) findViewById(R.id.app_logo));
		image = (ImageView) findViewById(R.id.image_view);
		name = (TextView) findViewById(R.id.name_txt);
		classs = (TextView) findViewById(R.id.class_txt);
		// child_panel = (RelativeLayout) findViewById(R.id.child_panel);
		// setUserImage(ClassFellowListActivity.this);
		// SchoolAppUtils.loadAppLogo(ClassFellowListActivity.this,
		// (ImageButton) findViewById(R.id.app_logo));
		if (SchoolAppUtils.isOnline(ClassFellowSectionListActivity.this)) {
			internetAvailable = true;
			initialize();
		} 


		//initialize();
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
		if (getIntent().getExtras() != null) {
			page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
			header = (TextView) findViewById(R.id.header_text);
			header.setText(page_title);
			
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

			}
		}
		
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		listView = (ListView) findViewById(R.id.listview);

		if (fellows == null) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getApplicationContext(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getApplicationContext(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getApplicationContext(),
							Constants.USERID)));
			nameValuePairs
					.add(new BasicNameValuePair("section_id", subject.section_id));
			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getApplicationContext(),
							Constants.CHILD_ID)));

			new GetClassFellowListAsyntask().execute(nameValuePairs);
		} else {
			adapter = new ClassFellowsAdapter(ClassFellowSectionListActivity.this,
					fellows);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(listener);
		}
	}
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(ClassFellowSectionListActivity.this,
					TeacherBehaviourListActivity.class);
			intent.putExtra(Constants.IS_ONLINE, internetAvailable);
			intent.putExtra(Constants.PAGE_TITLE, page_title);
			if (internetAvailable) {
				intent.putExtra(Constants.TANNOUNCEMENT,
						subject.object.toString());
				intent.putExtra(Constants.CLASSFELLOW,
						fellows.get(arg2).object.toString());
			}else{

				intent.putExtra(Constants.OFFLINE_SUBJECT_ID, subject.id);
				Log.d("row_id", "" + subject.row_id);
			}
			ClassFellowSectionListActivity.this.startActivity(intent);

		}
	};
	public static void setUserImage(Context context) {
		if (SchoolAppUtils.GetSharedBoolParameter(context,
				Constants.ISPARENTSIGNIN)) {
			child_panel.setVisibility(View.VISIBLE);
		} else {
			child_panel.setVisibility(View.GONE);
		}
		JSONObject c = null;
		try {
			c = new JSONObject(SchoolAppUtils.GetSharedParameter(context,
					Constants.SELECTED_CHILD_OBJECT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c != null) {
			StudentProfileInfo info = new StudentProfileInfo(c);
			name.setText(info.fullname);
			classs.setText("Class: " + info.class_section);

			new LoadImageAsyncTask(context, image, Urls.image_url_userpic,
					info.image, true).execute();

		}
	}

	private class GetClassFellowListAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ClassFellowSectionListActivity.this);
			dialog.setTitle("Students");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> url = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					Urls.api_get_student_by_section);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						
						fellows = new ArrayList<ClassFellows>();
						for (int i = 0; i < array.length(); i++) {
							ClassFellows fellow = new ClassFellows(
									array.getJSONObject(i));
							if (SchoolAppUtils.GetSharedParameter(
									getApplicationContext(),
									Constants.USERTYPEID).equalsIgnoreCase(
									Constants.USERTYPE_PARENT)) {

								if (SchoolAppUtils.GetSharedParameter(
										getApplicationContext(),
										Constants.CHILD_ID).equalsIgnoreCase(
										fellow.id)) {

								} else {
									fellows.add(fellow);
								}
							} else {
								if (SchoolAppUtils.GetSharedParameter(
										getApplicationContext(),
										Constants.USERID).equalsIgnoreCase(
										fellow.id)) {

								} else {
									fellows.add(fellow);
								}
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
				}else {
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
				if (fellows != null) {
					adapter = new ClassFellowsAdapter(
							ClassFellowSectionListActivity.this, fellows);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(listener);
				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(ClassFellowSectionListActivity.this, getTitle().toString(), error);
				}else{
					SchoolAppUtils.showDialog(ClassFellowSectionListActivity.this, getTitle().toString(), getResources().getString(R.string.error));
				}

			}
		}

	}
}
