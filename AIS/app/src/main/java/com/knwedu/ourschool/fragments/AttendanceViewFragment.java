package com.knwedu.ourschool.fragments;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AttendanceAdapter;
import com.knwedu.ourschool.adapter.CampusDetailsAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.DataStructureFramwork.StatusStudent;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class AttendanceViewFragment extends Fragment implements AdapterView.OnItemSelectedListener {
	private static final String tag = "MyCalendarActivity";
	private View view;
	private TextView currentMonth;
	private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	@SuppressLint("NewApi")
	private int month, current_month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private static final String dateTemplate = "MMMM yyyy";
	private final ArrayList<String> selectedDates = new ArrayList<String>();
	private Spinner spinnerLectureNo;
	private ProgressDialog dialog;
	private ArrayList<String> studentStatus = new ArrayList<String>();
	private ArrayList<String> dates = new ArrayList<String>();
	private int lecture_num;
	private ArrayList<String> lectures = new ArrayList<String>();
	private ArrayAdapter<String> lecadapter;
	private HashMap<String,String> dateStatusPair = new HashMap<String,String>();
	private HashMap<String,String> dateReasonPair = new HashMap<String,String>();
	private LinearLayout header;
	private boolean flag = false;

	private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.my_calendar_view, container,
				false);
		initialize();
		return view;
	}

	private void initialize() {
		header = (LinearLayout) view.findViewById(R.id.header);
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);
		current_month = month;
		selectedDayMonthYearButton = (Button) view.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");
		spinnerLectureNo = (Spinner) view.findViewById(R.id.spinnerLectureNo);
		spinnerLectureNo.setOnItemSelectedListener(this);
		prevMonth = (ImageView) view.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			if (current_month == month) {
				nextMonth.setVisibility(View.GONE);
			}else{
				nextMonth.setVisibility(View.VISIBLE);
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
			requestMonthwiseAttendanceParams();
			setGridCellAdapterToDate(month, year);

			}
		});

		currentMonth = (TextView) view.findViewById(R.id.currentMonth);
		currentMonth.setText(months[month-1] +", "+year);

		nextMonth = (ImageView) view.findViewById(R.id.nextMonth);

		if (current_month == month) {
			nextMonth.setVisibility(View.GONE);
		}else{
			nextMonth.setVisibility(View.VISIBLE);
		}
		nextMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (month > 11) {
					month = 1;
					year++;
				} else {
					month++;
				}
				if (current_month == month) {
					nextMonth.setVisibility(View.GONE);
				}else{
					nextMonth.setVisibility(View.VISIBLE);
				}
				Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
				requestMonthwiseAttendanceParams();
				setGridCellAdapterToDate(month, year);

			}
		});

		calendarView = (GridView) view.findViewById(R.id.calendar);
		/**
		* To fecth Lecture Numbers
		*/
		requestAttendanceSettings();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// On selecting a spinner item
		lecture_num = Integer.parseInt(parent.getItemAtPosition(position).toString().trim()	);
		requestMonthwiseAttendanceParams();
	}
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		//lecture_num = 0;
		lecture_num = Integer.parseInt(parent.getItemAtPosition(0).toString().trim());
	}

	private void requestMonthwiseAttendanceParams() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("month", month + ""));
		nameValuePairs.add(new BasicNameValuePair("year",year+""));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("child_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		nameValuePairs.add(new BasicNameValuePair("lecture_num",lecture_num+""));

		new FetchStudentAttendanceAsyntask().execute(nameValuePairs);
	}

	private void requestAttendanceSettings() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("child_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));

		new FetchAttendanceSettingsDataAsyncTask().execute(nameValuePairs);
	}

	/**
	 *
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(months[month-1] +", "+year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;

		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 *
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " "
						+ String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
				list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-"
						+ getMonthAsString(prevMonth) + "-" + prevYear);
			}

			// Current Month Days Rajhrita
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);


				if (i == getCurrentDayOfMonth()) {

					list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 *
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);

			Log.d(tag, list.get(position).split("-") + "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];

			// Set the Day GridCell
			gridcell.setTag(theday);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

			gridcell.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String date = (String) v.getTag();
					if (dates.contains(date)) {
						if (dateStatusPair.get(date).equalsIgnoreCase("Late")) {
							if(dateReasonPair.get(date).equalsIgnoreCase("null")){
								showViewDialog("No reason provided");
							} else{
								showViewDialog(dateReasonPair.get(date));
							}
						}
					}
				}
			});

			Log.d("STATUS : ", studentStatus.toString() + "Dates : " + dates.toString() + "Current Month : " + current_month);

			if(themonth.equalsIgnoreCase(months[month-1])) {
				for (int j = 0; j < dates.size(); j++) {
					if (theday.equalsIgnoreCase(dates.get(j))) {
						if (studentStatus.get(j).equalsIgnoreCase("Present")) {
							gridcell.setBackground(getResources().getDrawable(R.drawable.present));
						} else if (studentStatus.get(j).equalsIgnoreCase("Absent")) {
							gridcell.setBackground(getResources().getDrawable(R.drawable.absent));
						} else if (studentStatus.get(j).equalsIgnoreCase("Late")) {
							gridcell.setBackground(getResources().getDrawable(R.drawable.late));
						} else if (studentStatus.get(j).equalsIgnoreCase("Leave")) {
							gridcell.setBackground(getResources().getDrawable(R.drawable.leave));
						} else {
						}
					}
				}
			}

			if (day_color[1].equals("WHITE")) {
				gridcell.setText(theday);
				gridcell.setTextColor(getResources().getColor(R.color.black));
			}
			if (day_color[1].equals("BLUE") && themonth.equalsIgnoreCase(months[current_month-1])) {
				gridcell.setText(theday);
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
			}else if(day_color[1].equals("BLUE") && !themonth.equalsIgnoreCase(months[current_month-1])){
				gridcell.setText(theday);
				gridcell.setTextColor(getResources().getColor(R.color.black));
			}
			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			selectedDayMonthYearButton.setText("Selected: " + date_month_year);
			Log.e("Selected date", date_month_year);
			try {
				Date parsedDate = dateFormatter.parse(date_month_year);
				Log.d(tag, "Parsed Date: " + parsedDate.toString());

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}

	private void showViewDialog(String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
		// set dialog message
		alertDialogBuilder.setCancelable(false);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.alert_dialog, null);
		Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
		final TextView showComment = (TextView) view.findViewById(R.id.view_remarks);
		alertDialogBuilder.setView(view);
		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();
		showComment.setText(msg);
		// show it
		alertDialog.show();
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			alertDialog.cancel();
			}
		});
	}

	private class FetchStudentAttendanceAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(dialog == null) {
				dialog = new ProgressDialog(getActivity());
				dialog.setTitle(getActivity().getTitle().toString());
				dialog.setMessage(getResources().getString(R.string.please_wait));
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.show();
			}
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:

			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			JsonParser jParser = new JsonParser();
			// Clear all data
			studentStatus.clear();
			dates.clear();
			JSONObject json = null;
			json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_student_attendance);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONObject dataObj = json.getJSONObject("data");
						JSONArray attendanceArray = dataObj.getJSONArray("attendance_details");
						for(int i=0;i<attendanceArray.length();i++){
							try {
								studentStatus.add(((JSONObject)attendanceArray.get(i)).getString("classname"));
								String date = ((JSONObject)attendanceArray.get(i)).getString("date");
								String reason = ((JSONObject)attendanceArray.get(i)).getString("reason").trim();
								String[] day_color = date.split("-");
								String thedate = day_color[2];
								if (thedate.substring(0, 1).equalsIgnoreCase("0")) {
									thedate = thedate.substring(1);
								}
								dateStatusPair.put(thedate, ((JSONObject) attendanceArray.get(i)).getString("classname").trim());
								dateReasonPair.put(thedate, reason);
								dates.add(thedate);
							} catch (Exception e) {
								e.printStackTrace();
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

				} else {
					error = getResources().getString(R.string.unknown_response);
				}

			} catch (

					JSONException e) {

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
			if(result){
//				adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
//				adapter.notifyDataSetChanged();
//				calendarView.setAdapter(adapter);
			}
			else {
				if(!flag){
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
			adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
			adapter.notifyDataSetChanged();
			calendarView.setAdapter(adapter);
		}

	}

	private class FetchAttendanceSettingsDataAsyncTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		String oneTime = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(dialog == null) {
				dialog = new ProgressDialog(getActivity());
				dialog.setTitle(getActivity().getTitle().toString());
				dialog.setMessage(getResources().getString(R.string.please_wait));
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.show();
			}
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];

			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			JsonParser jParser = new JsonParser();
			// Clear all data
			studentStatus.clear();
			dates.clear();
			lectures.clear();

			JSONObject json = null;
			json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_get_attendance_setting);

			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONObject dataObj = json.getJSONObject("data");
						oneTime = dataObj.getString("one_time");

						if(oneTime.equalsIgnoreCase("0")){
							JSONArray lecArray = dataObj.getJSONArray("lecture_num");
							//lectures.add("Select Lecture Number");
							for(int i=0;i<lecArray.length();i++){
								try {
									lectures.add(((JSONObject)lecArray.get(i)).getString("lec"+i));
								} catch (Exception e) {
									e.printStackTrace();
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

				} else {
					error = getResources().getString(R.string.unknown_response);
				}

			} catch (

					JSONException e) {

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
			if(result){
				flag = true;
				if(lectures.size()>0) {
					header.setVisibility(View.VISIBLE);
					lecadapter = new ArrayAdapter<String>(getActivity(),
							R.layout.spinner_text, lectures);
					lecadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinnerLectureNo.setVisibility(View.VISIBLE);
					spinnerLectureNo.setAdapter(lecadapter);
				}else{
					header.setVisibility(View.GONE);
				}

				requestMonthwiseAttendanceParams();
			}
			else {
				flag = false;
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
