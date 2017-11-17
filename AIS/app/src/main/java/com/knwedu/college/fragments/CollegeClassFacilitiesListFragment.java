package com.knwedu.college.fragments;
/*package com.knwedu.ourschool.fragments;

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

import com.knwedu.ourschool.ParentChildRequestActivity;
import com.knwall.knwedumycollegeapp.R;
import com.knwedu.ourschool.adapter.ClassScheduleAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassSchedule;
import com.knwedu.ourschool.utils.DataStructureFramwork.TimeTable;

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
	private String dateSelected;

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
		setSpinners();
		
		 * if(statuses != null) { adapterClass = new
		 * ClassScheduleAdapter(getActivity(), statuses);
		 * list.setAdapter(adapterClass);
		 * list.setOnItemClickListener(clickListener); } else {
		 * initilizeList(SchoolAppUtils.getDay(dateSelected)); }
		 
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
		// ArrayList<String> type = new ArrayList<String>();
		// type.add("Class Facilities");
		
		 * type.add("Time Table"); ArrayAdapter<String> typeAdapter = new
		 * ArrayAdapter<String>( getActivity(),
		 * R.layout.simple_spinner_item_custom, type); typeAdapter
		 * .setDropDownViewResource
		 * (R.layout.simple_spinner_dropdown_item_custom);
		 * main.setAdapter(typeAdapter); main.setOnItemSelectedListener(new
		 * AdapterView.OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> arg0, View arg1,
		 * int arg2, long arg3) { if (arg2 == 0) { //loadScheduleData();
		 * list.setVisibility(View.GONE); date.setVisibility(View.GONE); } else
		 * { list.setVisibility(View.VISIBLE); loadTimeTableData();
		 * date.setVisibility(View.VISIBLE); // } }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> arg0) { } });
		 
		list.setVisibility(View.VISIBLE);
		loadTimeTableData();

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
		
		 * String url = URLs.GetClassFacilities + "email=" +
		 * SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERNAME)
		 * + "&password=" + SchoolAppUtils.GetSharedParameter(getActivity(),
		 * Constants.PASSWORD);
		 * 
		 * new GetAssignmentAsyntask().execute(url);
		 

	}

	private void loadTimeTableData() {

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
		new GetTimeTableAsyntask().execute(nameValuePairs);

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

	private class GetAssignmentAsyntask extends
			AsyncTask<String, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.class_facilities));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			statuses = new ArrayList<ClassSchedule>();
			String url = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrl(url);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							statuses = new ArrayList<ClassSchedule>();
							for (int i = 0; i < array.length(); i++) {
								ClassSchedule assignment = new ClassSchedule(
										array.getJSONObject(i));
								statuses.add(assignment);
							}
						} catch (Exception e) {

						}
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
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
					adapterClass = new ClassScheduleAdapter(getActivity(),
							statuses);
					list.setAdapter(adapterClass);
					list.setOnItemClickListener(clickListener);
				}
			} else {
				if (statuses != null) {

					adapterClass = new ClassScheduleAdapter(getActivity(),
							statuses);
					list.setAdapter(adapterClass);
					list.setOnItemClickListener(clickListener);
				}
				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils.showDialog(getActivity(), getResources()
								.getString(R.string.error), error);
					} else {
						SchoolAppUtils
								.showDialog(
										getActivity(),
										getResources()
												.getString(R.string.error),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	private class TimeTableAdapter extends BaseAdapter {
		ViewHolder holder;
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

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.timetable_item_updated,
						null);
				holder = new ViewHolder();
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
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (this.timeTable.get(position).subject.length() > 0) {
				holder.lecture
						.setText(this.timeTable.get(position).teacher_name);
				
				 * holder.lecture.setText("Lecture: " +
				 * (Integer.parseInt(this.timeTable
				 * .get(position).lecture_num)-count)+ ";");
				 
			} else {
				holder.lecture.setText("");
			}
			if (this.timeTable.get(position).subject.length() > 0) {
				holder.facility.setText(this.timeTable.get(position).facility);
			} else {
				holder.facility.setText("");
			}
			holder.startTime.setText(this.timeTable.get(position).start);
			holder.endTime.setText(this.timeTable.get(position).end);

			if (this.timeTable.get(position).subject.equalsIgnoreCase("null")) {
				holder.subject.setTextColor(Color.parseColor("#FF0000"));

				if (this.timeTable.get(position).subject.equals("Free")) {
					holder.subject
							.setText((this.timeTable.get(position).subject)
									+ " Period");
				} else {
					holder.subject
							.setText(this.timeTable.get(position).subject);
					holder.lecture_no
							.setText(this.timeTable.get(position).lecture_num);

				}
			} else {
				holder.subject.setText(this.timeTable.get(position).subject);
				holder.lecture_no
						.setText(this.timeTable.get(position).lecture_num);
			}
			
			return convertView;
		}

		private class ViewHolder {
			TextView lecture, subject, facility, startTime, endTime,
					lecture_no;
		}
	}

	private class GetTimeTableAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.time_table));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			timeTable = new ArrayList<TimeTable>();

			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_timetable_user);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");

						} catch (Exception e) {
							return true;
						}
						timeTable = new ArrayList<TimeTable>();

						for (int i = 0; i < array.length(); i++) {
							TimeTable attendance = new TimeTable(
									array.getJSONObject(i));
							timeTable.add(attendance);
						}

						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}

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
				if (timeTable != null) {
					if (timeTable.size() > 0) {
						Log.d("Day", SchoolAppUtils.getDay(dateSelected));
						initilizeList(SchoolAppUtils.getDay(dateSelected));
						class_name.setText("Class : "+timeTable.get(0).classs+" , "+timeTable.get(0).section);
                        
					}
				}
			} else {
				if (timeTable != null) {
					if (timeTable.size() > 0) {
						Log.d("Day", SchoolAppUtils.getDay(dateSelected));
						initilizeList(SchoolAppUtils.getDay(dateSelected));
						class_name.setText("Class : "+timeTable.get(0).classs+" , "+timeTable.get(0).section);
						
					}
				}
				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils.showDialog(getActivity(), getResources()
								.getString(R.string.error), error);
					} else {
						SchoolAppUtils
								.showDialog(
										getActivity(),
										getResources()
												.getString(R.string.error),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}
	}
}
*/