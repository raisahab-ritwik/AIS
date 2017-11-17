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
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AttendanceAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.StatusStudent;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class AttendanceInfoActivity extends Activity {
	private ListView list;
	private AttendanceAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private Button showMonthWeek;
	private Subject subject;
	private ArrayList<StatusStudent> statuses;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_attendance_info);
		SchoolAppUtils.loadAppLogo(AttendanceInfoActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
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
			if(dialog.isShowing())
			{
				dialog.dismiss();
				dialog = null;
			}
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
		if(getIntent().getExtras() != null)
		{
			String temp = getIntent().getExtras().getString(Constants.ID);
			JSONObject c = null;
			try {
				c = new JSONObject(temp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(c != null)
			{
				subject = new Subject(c);
			}
		}
		list = (ListView) findViewById(R.id.listview);
		/*showMonthWeek = (Button) findViewById(R.id.show_monthly_weekly);
		if(SchoolAppUtils.GetSharedBoolParameter(AttendanceInfoActivity.this, Constants.MONTHLYWEEKLYATTENDANCE))
		{
			showMonthWeek.setText(R.string.weekly);
		}
		else
		{
			showMonthWeek.setText(R.string.monthly);
		}*/
		handleButton();
		if(statuses != null)
		{
			adapter = new AttendanceAdapter(AttendanceInfoActivity.this, statuses);
			list.setAdapter(adapter);
		}
		else
		{
			loadData();
		}

	}

	private void loadData()
	{
		if(subject != null)
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(AttendanceInfoActivity.this,
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(AttendanceInfoActivity.this,
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(AttendanceInfoActivity.this, Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("sub_id",subject.id));


			nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
					.GetSharedParameter(AttendanceInfoActivity.this, Constants.CHILD_ID)));
		    new GetAssignmentAsyntask().execute(nameValuePairs);
		}
	}

	

	private class GetAssignmentAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean>
	{
		String error;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(AttendanceInfoActivity.this);
			dialog.setTitle(getResources().getString(R.string.attendance));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			statuses = new ArrayList<StatusStudent>();
			List<NameValuePair> nameValuePair = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,Urls.api_attendance_student_parent);
			try {

				if(json!=null){
					if(json.getString("result").equalsIgnoreCase("1")){
						try
						{
							JSONArray array = json.getJSONArray("data");
							statuses = new ArrayList<StatusStudent>();
							for(int i = 0; i < array.length(); i++)
							{
								StatusStudent assignment = new StatusStudent(array.getJSONObject(i));
								statuses.add(assignment);
							}
						}
						catch (Exception e)
						{

						}
						return true;
					}
					else
					{
						try{
							error = json.getString("data");
						}
						catch (Exception e) {}
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
				if(statuses != null)
				{
					adapter = new AttendanceAdapter(AttendanceInfoActivity.this, statuses);
					list.setAdapter(adapter);
				}
			}
			else
			{
				if(statuses != null)
				{

					adapter = new AttendanceAdapter(AttendanceInfoActivity.this, statuses);
					list.setAdapter(adapter);
				}
				if(error != null)
				{
					if(error.length() > 0)
					{
						SchoolAppUtils.showDialog(AttendanceInfoActivity.this, getTitle().toString(), error);
					}
					else
					{
						SchoolAppUtils.showDialog(AttendanceInfoActivity.this, getTitle().toString(), 
								getResources().getString(R.string.please_check_internet_connection));
					}
				}
				else
				{
					SchoolAppUtils.showDialog(AttendanceInfoActivity.this, getTitle().toString(), 
							getResources().getString(R.string.please_check_internet_connection));
				}

			}
		}

	}
	private void handleButton()
	{
		if(showMonthWeek != null)
		{
			showMonthWeek.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(SchoolAppUtils.GetSharedBoolParameter(AttendanceInfoActivity.this, Constants.MONTHLYWEEKLYATTENDANCE))
					{
						changeStateButton(false);
						loadData();
					}
					else
					{
						changeStateButton(true);
						loadData();
					}
				}
			});
		}

	}
	private void changeStateButton(boolean state)
	{
		SchoolAppUtils.SetSharedBoolParameter(AttendanceInfoActivity.this, Constants.MONTHLYWEEKLYATTENDANCE, state);
		if(showMonthWeek != null)
		{
			if(state)
			{
				showMonthWeek.setText(R.string.weekly);
			}
			else
			{
				showMonthWeek.setText(R.string.monthly);
			}
		}
	}
}
