package com.knwedu.ourschool.fragments;

import java.text.SimpleDateFormat;
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
import android.widget.ImageButton;
import android.widget.ListView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AttendanceAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.StatusStudent;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class Attendancrinfofragment extends DialogFragment {
	private ListView list;
	private String dateSelected, currentDate, child_id;
	private AttendanceAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private Button showMonthWeek;
	private Subject subject;
	private ArrayList<StatusStudent> statuses;
	private Button date;
	DatabaseAdapter mDatabase;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_attendance_info, container,
				false);
		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));
		initialize();
		return view;
	}

	private void initialize() {

		list = (ListView) view.findViewById(R.id.listview);
		dateSelected = SchoolAppUtils.getCurrentDate();
		currentDate = SchoolAppUtils.getCurrentDate();
		date = (Button) view.findViewById(R.id.date_btns);
		child_id = SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.CHILD_ID);
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getDatabase();
		date.setText(SchoolAppUtils.getDayDifferentDif(SchoolAppUtils
				.getCurrentDate()));
		statuses = new ArrayList<StatusStudent>();
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
			date.setText(SchoolAppUtils.getDayDifferent(year + "/" + mon + "/"
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

	private void loadData() {

		if (SchoolAppUtils.isOnline(getActivity())) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("attendence_date",
					dateSelected));
			nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
			new OfflineAttendanceAsync().execute(nameValuePairs);
		}else{

			mDatabase.open();
			StatusStudent att = new StatusStudent();
			statuses.clear();
			if(mDatabase.getStudetParentAttendanceCount(dateSelected + " 00:00:00", child_id) > 0){
				att = mDatabase.getAttendanceByDate(dateSelected + " 00:00:00", child_id);

				statuses.add(att);
				mDatabase.close();
				adapter = new AttendanceAdapter(getActivity(), statuses);
				list.setAdapter(adapter);
			}else{
				if(adapter != null ) {
					adapter.setData(statuses);
					adapter.notifyDataSetChanged();
				}

				SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), "No record found.");
			}
		}

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
			nameValuePair = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
					Urls.api_offline_attendance_student);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
						mDatabase.open();
						mDatabase.deleteStudentParentAttendanceByID(child_id);
						for (int i = 0; i < array.length(); i++) {
							StatusStudent assignment = new StatusStudent(
									array.getJSONObject(i));
							//statuses.add(assignment);
							mDatabase.addStudentParentAttendance(assignment, child_id);
						}
						mDatabase.close();
						return true;
					} else {

						return false;
					}
				}else {
					return false;
				}

			} catch (JSONException e) {

			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			new GetAttendanceInfoAsyntask().execute(nameValuePair);
		}
	}



	private class GetAttendanceInfoAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			//statuses = new ArrayList<StatusStudent>();
			List<NameValuePair> nameValuePair1 = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair1,
					Urls.api_attendance_info);
			statuses.clear();
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");

						for (int i = 0; i < array.length(); i++) {
							StatusStudent assignment = new StatusStudent(
									array.getJSONObject(i));
							statuses.add(assignment);
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
				if (statuses != null) {
					adapter = new AttendanceAdapter(getActivity(), statuses);
					list.setAdapter(adapter);
				}
			} else {
				if(adapter != null ) {
					adapter.setData(statuses);
					adapter.notifyDataSetChanged();
				}
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), error);
				}else{
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), getResources().getString(R.string.error));
				}

			}
		}

	}

}
