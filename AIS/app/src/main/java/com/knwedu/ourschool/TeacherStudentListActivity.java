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
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.SubjectStudentsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.SubjectStudent;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;



public class TeacherStudentListActivity extends Activity{
	private ArrayList<SubjectStudent> students;
	private ProgressDialog dialog;

	Intent subjectIntent;


	private ListView listView;
	private SubjectStudentsAdapter adapter;
	ImageButton addAssignment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_student_list);
		SchoolAppUtils.loadAppLogo(TeacherStudentListActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
		subjectIntent=getIntent();
		String	id=subjectIntent.getStringExtra(Constants.ID);
		initialize(id);

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


	private void initialize(String id)
	{
		((ImageButton)findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
//		((LinearLayout)findViewById(R.id.header)).setVisibility(View.GONE);
		listView = (ListView) findViewById(R.id.listview);
//		((TextView)findViewById(R.id.header_text)).setText(R.string.students);;

		if(students == null)
		{

			Log.d("ID:", id);
//			String url = URLs.GetStudentlistWithOutLecture + 
//					"&email=" +SchoolAppUtils.GetSharedParameter(TeacherStudentListActivity.this, Constants.USERNAME) + 
//					"&password=" +SchoolAppUtils.GetSharedParameter(TeacherStudentListActivity.this, Constants.PASSWORD) + 
//					"&subject_id=" + id;
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(TeacherStudentListActivity.this, Constants.USERID)));
	        nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(TeacherStudentListActivity.this, Constants.USERTYPEID)));
	        nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(TeacherStudentListActivity.this, Constants.UORGANIZATIONID)));
	        nameValuePairs.add(new BasicNameValuePair("section_id", id));

			
			
		new GetAssignmentsAsyntask().execute(nameValuePairs);
		}
		else
		{
			adapter = new SubjectStudentsAdapter(TeacherStudentListActivity.this, students);
			listView.setAdapter(adapter);
		}
	}
	











	private class GetAssignmentsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean>
	{
		String error;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherStudentListActivity.this);
			dialog.setTitle(getResources().getString(R.string.students));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			JsonParser jParser = new JsonParser();
			// Log parameters:
			Log.d("url extension", Urls.api_teacher_subject_students);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_teacher_subject_students);
			
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
						students = new ArrayList<SubjectStudent>();
						for(int i = 0; i < array.length(); i++)
						{
							SubjectStudent student = new SubjectStudent(array.getJSONObject(i));
							students.add(student);
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
				if(students != null)
				{
					adapter = new SubjectStudentsAdapter(TeacherStudentListActivity.this,students);
					listView.setAdapter(adapter);
				}
			}
			else
			{
				if(error != null)
				{
					if(error.length() > 0)
					{
						SchoolAppUtils.showDialog(TeacherStudentListActivity.this,getTitle().toString(), error);
					}
					else
					{
						SchoolAppUtils.showDialog(TeacherStudentListActivity.this,getTitle().toString(), 
								getResources().getString(R.string.please_check_internet_connection));
					}
				}
				else
				{
					SchoolAppUtils.showDialog(TeacherStudentListActivity.this,getTitle().toString(), 
							getResources().getString(R.string.please_check_internet_connection));
				}

			}
		}

	}



}
