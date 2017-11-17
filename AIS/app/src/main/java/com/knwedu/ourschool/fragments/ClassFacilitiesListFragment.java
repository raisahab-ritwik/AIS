package com.knwedu.ourschool.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.ClassScheduleAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassSchedule;
import com.knwedu.ourschool.utils.DataStructureFramwork.TimeTable;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ClassFacilitiesListFragment extends Fragment {
	private ListView list;
	private ClassScheduleAdapter adapterClass;
	private TimeTableAdapter adapterTimeTable;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<ClassSchedule> statuses;
	private ArrayList<TimeTable> timeTable;
	private TextView class_name;
	private Spinner main, date;
	private String dateSelected,child_id;
	DatabaseAdapter mDatabase;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_schedule_list, container,
				false);
		initialize();
		return view;
	}

	private void initialize() {
		list = (ListView) view.findViewById(R.id.listview);
		// main = (Spinner) view.findViewById(R.id.spinner);
		date = (Spinner) view.findViewById(R.id.date_btns);
		class_name = (TextView) view.findViewById(R.id.lecture_class);
		timeTable = new ArrayList<TimeTable>();
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getDatabase();
		child_id = SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.CHILD_ID);
		mDatabase.open();
		setSpinners();

	}

	private void setSpinners() {
		dateSelected = SchoolAppUtils.getCurrentDate();
		ArrayList<String> days = new ArrayList<String>();
		days.add("Sunday");
		days.add("Monday");
		days.add("Tuesday");
		days.add("Wednesday");
		days.add("Thursday");
		days.add("Friday");
		days.add("Saturday");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.simple_spinner_item_custom_new, days);
		dataAdapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
		date.setAdapter(dataAdapter);
		date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				if (arg2 == 0) {
					initilizeList("sun");
				}
				if (arg2 == 1) {
					initilizeList("mon");
				}
				if (arg2 == 2) {
					initilizeList("tue");
				}
				if (arg2 == 3) {
					initilizeList("wed");
				}
				if (arg2 == 4) {
					initilizeList("thu");
				}
				if (arg2 == 5) {
					initilizeList("fri");
				}
				if (arg2 == 6) {
					initilizeList("sat");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		list.setVisibility(View.VISIBLE);

		if (SchoolAppUtils.isOnline(getActivity())) {

			loadTimeTableData();
		} else {


			//timeTable.clear();

			timeTable = mDatabase.getAllStudentTimeTable(child_id);
			mDatabase.close();


			date.setVisibility(View.VISIBLE);
			class_name.setVisibility(View.VISIBLE);
			if (timeTable != null) {
				if (timeTable.size() > 0) {
					Log.d("Day", SchoolAppUtils.getDay(dateSelected));
					initilizeList(SchoolAppUtils.getDay(dateSelected));
					class_name.setText("Class : " + timeTable.get(0).classs
							+ " , " + timeTable.get(0).section);
				}
			}


			SchoolAppUtils.showDialog(getActivity(), getActivity()
					.getTitle().toString(), "You are offline.");
		}

	}


	private ArrayList<TimeTable> listInitialization(String day) {
		ArrayList<TimeTable> temp = new ArrayList<TimeTable>();
		int count = 0;
		JSONObject object = new JSONObject();
		for (int i = 0; i < timeTable.size(); i++) {

			if (day.equalsIgnoreCase(timeTable.get(i).week_day)) {

				temp.add(timeTable.get(i));
			}
		}
		if (day.equalsIgnoreCase("sun")) {
			date.setSelection(0);
		} else if (day.equalsIgnoreCase("mon")) {
			date.setSelection(1);
		} else if (day.equalsIgnoreCase("tue")) {
			date.setSelection(2);
		} else if (day.equalsIgnoreCase("wed")) {
			date.setSelection(3);
		} else if (day.equalsIgnoreCase("thu")) {
			date.setSelection(4);
		} else if (day.equalsIgnoreCase("fri")) {
			date.setSelection(5);
		} else if (day.equalsIgnoreCase("sat")) {
			date.setSelection(6);
		}

		return temp;

	}


	@SuppressWarnings("null")
	private void initilizeList(String day) {
		ArrayList<TimeTable> temp = new ArrayList<TimeTable>();
		int count = 0;
		JSONObject object = new JSONObject();
		for (int i = 0; i < timeTable.size(); i++) {

			if (day.equalsIgnoreCase(timeTable.get(i).week_day)) {

				temp.add(timeTable.get(i));
			}
		}

		if (day.equalsIgnoreCase("sun")) {
			date.setSelection(0);
		} else if (day.equalsIgnoreCase("mon")) {
			date.setSelection(1);
		} else if (day.equalsIgnoreCase("tue")) {
			date.setSelection(2);
		} else if (day.equalsIgnoreCase("wed")) {
			date.setSelection(3);
		} else if (day.equalsIgnoreCase("thu")) {
			date.setSelection(4);
		} else if (day.equalsIgnoreCase("fri")) {
			date.setSelection(5);
		} else if (day.equalsIgnoreCase("sat")) {
			date.setSelection(6);
		}
		adapterTimeTable = new TimeTableAdapter(
				SchoolAppUtils.getDay(dateSelected), temp);
		list.setAdapter(adapterTimeTable);
		list.setOnItemClickListener(null);


	}

	private void loadScheduleData() {

		// http://64.150.189.99/school_app/webservices/user.php?mode=view_assignment&email=adil@smonte.com&password=smonte&type=1
		/*
		 * String url = URLs.GetClassFacilities + "email=" +
		 * SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERNAME)
		 * + "&password=" + SchoolAppUtils.GetSharedParameter(getActivity(),
		 * Constants.PASSWORD);
		 * 
		 * new GetAssignmentAsyntask().execute(url);
		 */

	}

	private void loadTimeTableData() {

		mDatabase.open();
		timeTable = mDatabase.getAllStudentTimeTable(child_id);
		mDatabase.close();

		ArrayList<TimeTable> temp = new ArrayList<TimeTable>();
		temp = listInitialization(SchoolAppUtils.getDay(dateSelected));

		if(timeTable.size() > 0) {
			class_name.setText("Class : " + timeTable.get(0).classs + " , " + timeTable.get(0).section);
			adapterTimeTable = new TimeTableAdapter(
					SchoolAppUtils.getDay(dateSelected), temp);
			list.setAdapter(adapterTimeTable);
			list.setOnItemClickListener(null);
		}
/*
		date.setVisibility(View.VISIBLE);
		class_name.setVisibility(View.VISIBLE);
		if(timeTable.size()>0){
		if (timeTable != null) {
			if (timeTable.size() > 0) {
				Log.d("Day", SchoolAppUtils.getDay(dateSelected));
				initilizeList(SchoolAppUtils.getDay(dateSelected));
				class_name.setText("Class : " + timeTable.get(0).classs
						+ " , " + timeTable.get(0).section);
			}
		}
		}*/


		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.CHILD_ID)));


		//new GetTimeTableAsyntask(getActivity(), mDatabase).execute(nameValuePairs);

		new GetAllTimeTable(getActivity(), mDatabase, adapterTimeTable).execute(nameValuePairs);

	}

	OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {

		}
	};

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}


	private class TimeTableAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private String day;
		private int count = 0;
		private ArrayList<TimeTable> timeTable;

		// private ArrayList<TimeTable_braks> timeTable_braks;

		public TimeTableAdapter(String day, ArrayList<TimeTable> timeTable) {
			inflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			this.day = day;
			this.timeTable = timeTable;
			// this.timeTable_braks = timetablebraks;
		}

		@Override
		public int getCount() {
			if (this.timeTable != null) {
				return this.timeTable.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setData(ArrayList<TimeTable> data) {
			this.timeTable = data;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.timetable_item_updated,
					null);
			holder.lecture = (TextView) convertView
					.findViewById(R.id.lecture_txt);
			holder.subject = (TextView) convertView
					.findViewById(R.id.subject_txt);
			holder.facility = (TextView) convertView
					.findViewById(R.id.facility_txt);
			holder.startTime = (TextView) convertView
					.findViewById(R.id.start_time_txt);
			holder.endTime = (TextView) convertView
					.findViewById(R.id.end_time_txt);
			holder.lecture_no = (TextView) convertView
					.findViewById(R.id.lec_no_txt);

			holder.lecture_txt = (TextView) convertView
					.findViewById(R.id.lec_no);
			holder.subject_txt = (TextView) convertView
					.findViewById(R.id.subject);
			holder.facility_txt = (TextView) convertView
					.findViewById(R.id.facility);

			holder.teacher_txt = (TextView) convertView
					.findViewById(R.id.lecture);

			convertView.setTag(holder);

			holder.startTime.setText(this.timeTable.get(position).start);
			holder.endTime.setText(this.timeTable.get(position).end);

			if (this.timeTable.get(position).subject.equalsIgnoreCase("null")) {
				holder.lecture.setTextColor(Color.parseColor("#FF0000"));
				holder.lecture
						.setText((this.timeTable.get(position).break_txt));
				holder.subject.setVisibility(View.GONE);
				holder.lecture_no.setVisibility(View.GONE);
				holder.facility.setVisibility(View.GONE);

				holder.subject_txt.setVisibility(View.GONE);
				holder.teacher_txt.setVisibility(View.GONE);
				holder.facility_txt.setVisibility(View.GONE);
				holder.lecture_txt.setVisibility(View.GONE);

			} else {
				holder.subject.setText(this.timeTable.get(position).subject);
				holder.lecture_no
						.setText(this.timeTable.get(position).lecture_num);
				holder.lecture
						.setText(this.timeTable.get(position).teacher_name);
				holder.facility.setText(this.timeTable.get(position).facility);
			}
			notifyDataSetChanged();
			return convertView;
		}

		private class ViewHolder {
			TextView lecture, subject, facility, startTime, endTime,
					lecture_no, subject_txt, facility_txt, lecture_txt,
					teacher_txt;
		}
	}
/*
	private class GetTimeTableAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		Context cntx;
		DatabaseAdapter minDatabase;

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

		public GetTimeTableAsyntask(Context cntx, DatabaseAdapter minDatabase) {
			this.minDatabase = minDatabase;
			this.cntx = cntx;
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			//timeTable = new ArrayList<TimeTable>();

			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			minDatabase.open();
			minDatabase.deleteStudentTimetable();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_timetable_user);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							TimeTable attendance = new TimeTable(
									array.getJSONObject(i));

							timeTable.add(attendance);
							minDatabase.addStudentTimetable(attendance);
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
				date.setVisibility(View.VISIBLE);
				class_name.setVisibility(View.VISIBLE);
				if (timeTable != null) {
					if (timeTable.size() > 0) {
						Log.d("Day", SchoolAppUtils.getDay(dateSelected));
						initilizeList(SchoolAppUtils.getDay(dateSelected));
						class_name.setText("Class : " + timeTable.get(0).classs
								+ " , " + timeTable.get(0).section);

					}
				}
			} else {
				date.setVisibility(View.INVISIBLE);
				class_name.setVisibility(View.INVISIBLE);
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), error);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
									.getTitle().toString(),
							getResources().getString(R.string.error));
				}
			}
		}
	}

	*/


	private class GetAllTimeTable extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		Context cntxt;
		DatabaseAdapter minDatabase;
		TimeTableAdapter mTimetableAdapter;
		String error = "";
		ArrayList<TimeTable> localtemp = new ArrayList<TimeTable>();


		public GetAllTimeTable(Context cntxt, DatabaseAdapter minDatabase, TimeTableAdapter mTimetableAdapter) {

			this.cntxt = cntxt;
			this.minDatabase = minDatabase;
			this.mTimetableAdapter = mTimetableAdapter;
		}

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
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			minDatabase.open();
			minDatabase.deleteStudenTimetableByID(child_id);

			JSONObject json = jParser.getJSONFromUrlnew(nvp, Urls.api_timetable_user);
			timeTable.clear();
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							TimeTable attendance = new TimeTable(
									array.getJSONObject(i));
							timeTable.add(attendance);
							minDatabase.addStudentTimetable(attendance,child_id);
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
				date.setVisibility(View.VISIBLE);
				class_name.setVisibility(View.VISIBLE);

				if (timeTable != null) {
					if (timeTable.size() > 0) {
						class_name.setText("Class : " + timeTable.get(0).classs + " , " + timeTable.get(0).section);
						Log.d("Day", SchoolAppUtils.getDay(dateSelected));
						localtemp = listInitialization(SchoolAppUtils.getDay(dateSelected));
						adapterTimeTable.setData(localtemp);
						adapterTimeTable.notifyDataSetChanged();
					}
				}
			} else {
				date.setVisibility(View.INVISIBLE);
				class_name.setVisibility(View.INVISIBLE);
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), error);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
									.getTitle().toString(),
							getResources().getString(R.string.error));
				}
			}
		}


	}


}


