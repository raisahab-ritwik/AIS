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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.adapter.CollegeRequestStatusAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.RequestsStatus;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StudentProfileInfo;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeParentRequestStatusListActivity extends Activity {
	private ArrayList<RequestsStatus> requests;
	private ProgressDialog dialog;
	private Spinner spinner;
	private ListView listView;
	private CollegeRequestStatusAdapter adapter;
	private static ImageView image;
	private static TextView name;
	private static TextView classs;
	private String page_title = "";
	//private static ImageLoader imageTemp = ImageLoader.getInstance();
	//private static DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_request_list);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		/*image = (ImageView) findViewById(R.id.image_view);
		name = (TextView) findViewById(R.id.name_txt);
		classs = (TextView) findViewById(R.id.class_txt);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.no_photo)
				.showImageOnFail(R.drawable.no_photo)
				.showStubImage(R.drawable.no_photo).cacheInMemory()
				.cacheOnDisc().build(); 
		setUserImage(CollegeParentRequestStatusListActivity.this);*/
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
		if(dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}
	private void initialize()
	{
		((ImageButton)findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		/*spinner = (Spinner) findViewById(R.id.spinner);
		String[] temppp = getResources().getStringArray(R.array.request_type_list);
		ArrayAdapter<String> data = new ArrayAdapter<String>(CollegeParentRequestStatusListActivity.this, R.layout.simple_spinner_item_custom_new, temppp);
		data.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
		spinner.setAdapter(data);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				loadData(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		*/
		listView = (ListView) findViewById(R.id.listview);
		((TextView)findViewById(R.id.header_text)).setText(R.string.requests_status);;
		loadData();
		
	}
	private void loadData()
	{
		
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					1);
			nameValuePairs.add(new BasicNameValuePair("id",
					CollegeAppUtils.GetSharedParameter(
							CollegeParentRequestStatusListActivity.this,
							"id")));
			
			new GetRequestsAsyntask().execute(nameValuePairs);
		}
		
	
	
	
	private void setUserImage(Context context) {
		JSONObject c = null;
		try {
			c = new JSONObject(CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.SELECTED_CHILD_OBJECT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c != null) {
			StudentProfileInfo info = new StudentProfileInfo(c);
			name.setText(info.fullname);
			classs.setText("Class: " + info.class_name);
			
			new CollegeLoadImageAsyncTask(context, image, CollegeUrls.image_url_userpic, info.image, true).execute();
	
		}
	}

	
	
	private class GetRequestsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean>
	{
		String error;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentRequestStatusListActivity.this);
			dialog.setTitle(getResources().getString(R.string.requests_status));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_parent_request_status);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_parent_request_status);

			try {

				if(json!=null){
					if(json.getString("result").equalsIgnoreCase("1")){
						JSONArray array;
						try
						{
							array = json.getJSONArray("data");
						}
						catch (Exception e)
						{
							return true;
						}
						requests = new ArrayList<RequestsStatus>();
						for(int i = 0; i < array.length(); i++)
						{
							RequestsStatus assignment = new RequestsStatus(array.getJSONObject(i));
							requests.add(assignment);
						}


						return true;
					}
					else
					{
						try{error = json.getString("data");}catch (Exception e) {}
						return false;
					}
				}

			}
			catch (JSONException e) {

			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if(dialog != null)
			{
				dialog.dismiss();
				dialog = null;
			}
			if(result)
			{
				if(requests != null)
				{
					adapter = new CollegeRequestStatusAdapter(CollegeParentRequestStatusListActivity.this, requests);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Intent intent = new Intent(CollegeParentRequestStatusListActivity.this, CollegeParentRequestStatusActivity.class);
							intent.putExtra(CollegeConstants.REQUESTSTATUS, requests.get(arg2).object.toString());
							startActivity(intent);
						}
					});
				}
			}
			else
			{
				if(error != null)
				{
					if(error.length() > 0)
					{
						CollegeAppUtils.showDialog(CollegeParentRequestStatusListActivity.this,page_title, error);
					}
					else
					{
						CollegeAppUtils.showDialog(CollegeParentRequestStatusListActivity.this,page_title, 
								getResources().getString(R.string.please_check_internet_connection));
					}
				}
				else
				{
					CollegeAppUtils.showDialog(CollegeParentRequestStatusListActivity.this,page_title, 
							getResources().getString(R.string.please_check_internet_connection));
				}

			}
		}

	}
}
