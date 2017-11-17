package com.knwedu.ourschool;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.ParentRequestAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ParentRequest;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.LoadImageAsyncTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestToParentForClassActivity extends Activity {
	private ArrayList<ParentRequest> requests;
	private ProgressDialog dialog;
	private Spinner spinner;
	private ListView listView;
	private ParentRequestAdapter adapter;
	private static ImageView image;
	private static TextView name;
	private static TextView classs;
	private int type;

	// private static ImageLoader imageTemp = ImageLoader.getInstance();
	// private static DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_request_to_parent_class);
		SchoolAppUtils.loadAppLogo(RequestToParentForClassActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

		image = (ImageView) findViewById(R.id.image_view);
		name = (TextView) findViewById(R.id.name_txt);
		classs = (TextView) findViewById(R.id.class_txt);

		type = getIntent().getExtras().getInt("type");

		setUserImage(RequestToParentForClassActivity.this);
		initialize();

		((ImageButton) findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {

			@Override

			public void onClick(View v) {

				onBackPressed();

			}

		});

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


		listView = (ListView) findViewById(R.id.listview);
		((TextView) findViewById(R.id.header_text))
				.setText(R.string.parent_request);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Resume","Resume");
		loadData();
	}

	private void loadData() {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("parent_id", SchoolAppUtils
				.GetSharedParameter(RequestToParentForClassActivity.this,
						Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(
						RequestToParentForClassActivity.this,
						Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("student_id", SchoolAppUtils
				.GetSharedParameter(RequestToParentForClassActivity.this,
						Constants.CHILD_ID)));
		nameValuePairs
				.add(new BasicNameValuePair("type", String.valueOf(type)));

		new GetRequestsAsyntask().execute(nameValuePairs);
	}

	private void setUserImage(Context context) {
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

	private class GetRequestsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(RequestToParentForClassActivity.this);
			dialog.setTitle("Loading ");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", Urls.api_request_parent);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_request_parent);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						requests = new ArrayList<ParentRequest>();
						for (int i = 0; i < array.length(); i++) {
							ParentRequest request = new ParentRequest(
									array.getJSONObject(i));
							requests.add(request);
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
				if (requests != null) {
					adapter = new ParentRequestAdapter(
							RequestToParentForClassActivity.this, requests);
					listView.setAdapter(adapter);
				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(
							RequestToParentForClassActivity.this,
							getResources().getString(R.string.requests_status),
							error);
				} else {
					SchoolAppUtils.showDialog(
							RequestToParentForClassActivity.this,
							getResources().getString(R.string.requests_status),
							getResources().getString(R.string.unknown_response));
				}
			}
		}

	}
}
