package com.knwedu.college.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.knwedu.college.adapter.CollegeAttendanceAdapter;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StatusStudent;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.DataStructureFramwork;

public class CollegeAttendancrinfofragment extends DialogFragment{
	private ListView list;
	private String dateSelected;
	private CollegeAttendanceAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private Button showMonthWeek;
	private Subject subject;
	private ArrayList<StatusStudent> statuses;
	private Button date;
	private String pageTitle = "";
	CollegeDBAdapter mDatabase;
	String dbid;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_activity_attendance_info,
				container, false);
		initialize();
		return view;
	}
	private void initialize()
	{
		pageTitle = getActivity().getTitle().toString();
		list = (ListView) view.findViewById(R.id.listview);
		dateSelected = CollegeAppUtils.getCurrentDate();
		date = (Button) view.findViewById(R.id.date_btns);
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getCollegeDatabase();
		statuses = new ArrayList<StatusStudent>();
		date.setText(CollegeAppUtils.getDayDifferentDif(CollegeAppUtils
				.getCurrentDate()));
		date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getFragmentManager(), "datePicker");
				
			}
		});
		loadData();
       
	}
	@SuppressLint("ValidFragment")
	private class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
			
		}
		
		

		@Override
		public void onDismiss(DialogInterface dialog) {
			// TODO Auto-generated method stub
			super.onDismiss(dialog);
			loadData();
		}



		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {

			Calendar newDate = Calendar.getInstance();
			newDate.set(year, month, day);
			Calendar minDate = Calendar.getInstance();
			
			String mon, da;
			if (month < 10) {
				mon = "0" + month;
			} else {
				mon = "" + month;
			}
			if (day < 10) {
				da = "0" + day;
			} else {
				da = "" + day;
			}
			date.setText(CollegeAppUtils.getDayDifferent(year + "/" + mon + "/"
					+ da));
			month = month + 1;
			if (month < 10) {
				mon = "0" + month;
			} else {
				mon = "" + month;
			}
			dateSelected = year + "-" + mon + "-" + da;
			
		}
		
	}
	private void loadData()
	{

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			String usertype = CollegeAppUtils.GetSharedParameter(getActivity(),
					CollegeConstants.USERTYPEID);
			String child_id = CollegeAppUtils.GetSharedParameter(getActivity(),
					"session_student_id");

			if (usertype.equalsIgnoreCase("5")) {
				nameValuePairs.add(new BasicNameValuePair("id", child_id));
				dbid = child_id;
			} else {
				nameValuePairs.add(new BasicNameValuePair("id",
						CollegeAppUtils.GetSharedParameter(getActivity(),
								"id")));
				dbid =CollegeAppUtils.GetSharedParameter(getActivity(),
						"id");
			}
			nameValuePairs.add(new BasicNameValuePair("date",
					dateSelected));

		if (CollegeAppUtils.isOnline(getActivity())) {

			new OfflineAttendanceAsync().execute(nameValuePairs);

		}
		else{

			mDatabase.open();
			StatusStudent att = new StatusStudent();
			statuses.clear();
			if(mDatabase.getStudetParentAttendanceCount(dateSelected, dbid ) > 0){
				statuses = mDatabase.getAttendanceByDate(dateSelected,dbid );

				//statuses.add(att);
				mDatabase.close();
				adapter = new CollegeAttendanceAdapter(getActivity(), statuses);
				list.setAdapter(adapter);
			}else{
				if(adapter != null ) {
					adapter.setData(statuses);
					adapter.notifyDataSetChanged();
				}

				CollegeAppUtils.showDialog(getActivity(),pageTitle,
						"No Record found.");
			}


		}
	
		   // new GetAssignmentAsyntask().execute(nameValuePairs);
		
	}




	private class OfflineAttendanceAsync extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		List<NameValuePair> nameValuePair;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getActivity().getTitle().toString());
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			//statuses = new ArrayList<StatusStudent>();
			List<NameValuePair> nameValuePair = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair, CollegeUrls.api_offline_attendance);

			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("info");
						mDatabase.open();
						mDatabase.deleteStudentParentAttendance();
						for (int i = 0; i < array.length(); i++) {
							StatusStudent assignment = new StatusStudent(
									array.getJSONObject(i));
							//statuses.add(assignment);
							mDatabase.addStudentParentAttendance(assignment,dbid);
						}
						mDatabase.close();
						return true;
					} else {

						return false;
					}
				} else {
					return false;
				}

			} catch (JSONException e) {

			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(3);
			String usertype = CollegeAppUtils.GetSharedParameter(getActivity(),
					CollegeConstants.USERTYPEID);
			String child_id = CollegeAppUtils.GetSharedParameter(getActivity(),
					"session_student_id");

			if (usertype.equalsIgnoreCase("5")) {
				nameValuePairs1.add(new BasicNameValuePair("id", child_id));
			} else {
				nameValuePairs1.add(new BasicNameValuePair("id",
						CollegeAppUtils.GetSharedParameter(getActivity(),
								"id")));
			}
			nameValuePairs1.add(new BasicNameValuePair("date",
					dateSelected));

			new GetAssignmentAsyntask().execute(nameValuePairs1);
		}



	}


	private class GetAssignmentAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean>
	{
		String error;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.attendance));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();*/
		}
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			statuses = new ArrayList<StatusStudent>();
			List<NameValuePair> nameValuePair = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,CollegeUrls.api_attendance_info);
			try {

				if(json!=null){
					if(json.getString("result").equalsIgnoreCase("1")){
						try
						{
							JSONArray array = json.getJSONArray("data");
							//statuses = new ArrayList<StatusStudent>();
							statuses.clear();
							for(int i = 0; i < array.length(); i++)
							{
								StatusStudent assignment = new StatusStudent(array.getJSONObject(i));
								statuses.add(assignment);
							}
						}
						catch (Exception e)
						{
							try{error = json.getString("data");}catch (Exception e1) {}
							
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
				if(statuses != null)
				{
					adapter = new CollegeAttendanceAdapter(getActivity(), statuses);
					list.setAdapter(adapter);
				}
			}
			else
			{
				if(statuses != null)
				{

					adapter = new CollegeAttendanceAdapter(getActivity(), statuses);
					list.setAdapter(adapter);
				}
				else
				{
					CollegeAppUtils.showDialog(getActivity(),pageTitle,
							getResources().getString(R.string.please_check_internet_connection));
				}

			}
		}

	}
	
}
