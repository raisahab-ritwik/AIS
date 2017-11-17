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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.TeacherAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;


public class TeacherClassesSubjectsListActivity extends FragmentActivity {

	private ListView list;
	private TeacherAdapter adapter;
	private ProgressDialog dialog;
	private TextView header;
	private ArrayList<Subject> subjects;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_teacher_classes_subject_list);
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
		InMobi.initialize(TeacherClassesSubjectsListActivity.this, getResources().getString(R.string.InMobi_Property_Id));
		IMBanner banner = (IMBanner) findViewById(R.id.banner);
		banner.loadBanner();
		SchoolAppUtils.loadAppLogo(TeacherClassesSubjectsListActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
		
		
((ImageButton)findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		list = (ListView) findViewById(R.id.listview);
		header = (TextView) findViewById(R.id.header_text);
		header.setText(R.string.classes_and_subjects);
		if(subjects != null)
		{
			String temp = null;
			for(int i = 0; i < subjects.size(); i++)
			{
				if(i == 0)
				{
					subjects.get(0).check = true;
					temp = subjects.get(0).classs +" "+ subjects.get(0).section_name;
				}
				else
				{
					if(!temp.equalsIgnoreCase(subjects.get(i).classs +" "+ subjects.get(i).section_name))
					{
						subjects.get(i).check = true;
						temp = subjects.get(i).classs  +" "+ subjects.get(i).section_name;
					}
				}
			}
			adapter = new TeacherAdapter(TeacherClassesSubjectsListActivity.this, subjects, false);
			list.setAdapter(adapter);
			list.setOnItemClickListener(listener);
		}
		else
		{
			//http://64.150.189.99/school_app/webservices/user.php?mode=view_assignment&email=adil@smonte.com&password=smonte&type=1
			/*String url = URLs.GetTeacherClassesList + 
					"email=" +SchoolAppUtils.GetSharedParameter(TeacherClassesSubjectsListActivity.this, Constants.USERNAME) + 
					"&password=" +SchoolAppUtils.GetSharedParameter(TeacherClassesSubjectsListActivity.this, Constants.PASSWORD);
			*/
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(TeacherClassesSubjectsListActivity.this, Constants.USERID)));
		        nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(TeacherClassesSubjectsListActivity.this, Constants.USERTYPEID)));
		        nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(TeacherClassesSubjectsListActivity.this, Constants.UORGANIZATIONID)));

			
			
			
			new GetAssignmentAsyntask().execute(nameValuePairs);
		}
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(TeacherClassesSubjectsListActivity.this, TeacherStudentListActivity.class);
			intent.putExtra(Constants.ID, subjects.get(arg2).section_id);
		startActivity(intent);
			
		}
	};
	
	
	
	
	
	
	
	

	private class GetAssignmentAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean>
	{
		String error;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherClassesSubjectsListActivity.this);
			dialog.setTitle(getResources().getString(R.string.classes_and_subject_list));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			subjects = new ArrayList<Subject>();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair, Urls.api_activity_get_subjects);
			
			// Log parameters:
			Log.d("url extension", Urls.api_activity_get_subjects);
			String parameters = "";
			for (NameValuePair nvp : namevaluepair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

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
						}////////////////////////////////////////////////////////
						subjects = new ArrayList<Subject>();
						for(int i = 0; i < array.length(); i++)
						{
							Subject assignment = new Subject(array.getJSONObject(i));
							subjects.add(assignment);
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
				if(subjects !=null)
				{
				String temp = null;
				for(int i = 0; i < subjects.size(); i++)
				{
					if(i == 0)
					{
						subjects.get(0).check = true;
						temp = subjects.get(0).classs +" "+ subjects.get(0).section_name;
					}
					else
					{
						if(!temp.equalsIgnoreCase(subjects.get(i).classs +" "+ subjects.get(i).section_name))
						{
							subjects.get(i).check = true;
							temp = subjects.get(i).classs  +" "+ subjects.get(i).section_name;
						}
					}
				}
				adapter = new TeacherAdapter(TeacherClassesSubjectsListActivity.this, subjects, false);
				list.setAdapter(adapter);
				list.setOnItemClickListener(listener);
				
				
			}
			}
			else
			{
				if(subjects != null)
				{

					adapter = new TeacherAdapter(TeacherClassesSubjectsListActivity.this, subjects, false);
					list.setAdapter(adapter);
				}
				if(error != null)
				{
					if(error.length() > 0)
					{
						SchoolAppUtils.showDialog(TeacherClassesSubjectsListActivity.this,getTitle().toString(), error);
					}
					else
					{
						SchoolAppUtils.showDialog(TeacherClassesSubjectsListActivity.this,getTitle().toString(), 
								getResources().getString(R.string.please_check_internet_connection));
					}
				}
				else
				{
					SchoolAppUtils.showDialog(TeacherClassesSubjectsListActivity.this,getTitle().toString(), 
							getResources().getString(R.string.please_check_internet_connection));
				}

			}
		}

	}
}
