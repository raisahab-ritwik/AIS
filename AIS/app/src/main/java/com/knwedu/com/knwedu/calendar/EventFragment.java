package com.knwedu.com.knwedu.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.com.knwedu.calendar.CalendarPickerView.SelectionMode;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.EventDetailsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Event;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class EventFragment extends Fragment {
	private CalendarPickerView calendar;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Event> events;
	// private ArrayList<Date> dates = new ArrayList<Date>();
	private Calendar todayDate, nextYear;
	private EventDetailsAdapter adapter;
	private ListView listviewEvents;
	private LinearLayout listLayout;
	private int type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.sample_calendar_picker, container, false);
		initialize();
		return view;
	}

	public EventFragment(int type) {
		super();
		this.type = type;
	}

	private void initialize() {
		nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);
		todayDate = Calendar.getInstance();
		todayDate.add(Calendar.MONTH, -1);
		listLayout = (LinearLayout) view.findViewById(R.id.listLayout);
		listviewEvents = (ListView) view.findViewById(R.id.listviewEvents);
		listLayout.setVisibility(View.GONE);
		calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
		calendar.init(todayDate.getTime(), nextYear.getTime()) //
				.inMode(SelectionMode.SINGLE) //
				.withSelectedDate(new Date());
		loadData();
		calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
			@Override
			public boolean onCellClicked(Date date) {
				listLayout.setVisibility(View.VISIBLE);
				System.out.println("Selected Date : " + date);
				ArrayList<Date> dates = new ArrayList<Date>();
				ArrayList<Event> events1 = new ArrayList<Event>();
				for (int i = 0; i < events.size(); i++) {
					Calendar startDate = Calendar.getInstance();
					dates.clear();
					String year = "";
					String month = "";
					String day = "";
					Event event = events.get(i);
					String[] array = event.event_start_date.split("\\-");
					for (int j = 0; j < array.length; j++) {
						year = array[0];
						month = array[1];
						day = array[2];
					}
					int month1 = Integer.parseInt(month);
					startDate.set(Integer.parseInt(year), month1 - 1, Integer.parseInt(day));
					dates.add(startDate.getTime());
					System.out.println("Start Date" + startDate.getTime());
					Calendar endDate = Calendar.getInstance();
					String[] array1 = event.event_end_date.split("\\-");
					for (int j = 0; j < array1.length; j++) {
						year = array1[0];
						month = array1[1];
						day = array1[2];
					}
					int month2 = Integer.parseInt(month);
					endDate.set(Integer.parseInt(year), month2 - 1, Integer.parseInt(day));
					dates.add(endDate.getTime());
					System.out.println(date + "End Date" + endDate.getTime());
					System.out.println(isSameDay(date, dates.get(0)) + "calculation :" + dates.get(0).compareTo(date) * date.compareTo(dates.get(1)));
					if (dates.get(0).compareTo(date) * date.compareTo(dates.get(1)) > 0) {
						events1.add(event);
					} else if (isSameDay(date, dates.get(0))) {
						events1.add(event);
					} else {
						System.out.println(dates.get(0) + "ELSE" + dates.get(1));
					}
				}
				adapter = new EventDetailsAdapter(getActivity(), events1);
				listviewEvents.setEmptyView(getActivity().findViewById(android.R.id.empty));
				listviewEvents.setAdapter(adapter);
				return true;

			}
		});
	}

	/**
	 * Check whether dates are same or not
	 * @param date1
	 * @param date2
	 * @return
	 */
	private boolean isSameDay(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
		boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
		boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
		return (sameDay && sameMonth && sameYear);
	}


	private void loadData() {
		// Initialize Calendar Elements

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		if(type == 0){
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		}else{
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					CollegeAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		}
		new GetEventDetailsAsyntask().execute(nameValuePairs);
	}

	private class GetEventDetailsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("Events");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			events = new ArrayList<Event>();
			events.clear();
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:

			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			JSONObject json = null;
			JsonParser jParser = new JsonParser();
			if(type == 0){
				json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_event);
				Log.d("url extension", Urls.api_event);
			}else{
				json = jParser.getJSONFromUrlnew(nameValuePairs, CollegeUrls.api_event_calendar);
				Log.d("url extension", CollegeUrls.api_event_calendar);
			}

			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							for (int i = 0; i < array.length(); i++) {
								Event event = new Event(array.getJSONObject(i));
								System.out.println("Event name : " + event.event_name);
								events.add(event);
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
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				error = getResources().getString(R.string.unknown_response);
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
				HashMap<Integer, ArrayList<Date>> hashmap = new HashMap<Integer, ArrayList<Date>>();
				ArrayList<Date> dates = null;
				if (events.size() > 0) {
					for (int i = 0; i < events.size(); i++) {
						dates = new ArrayList<Date>();

						String year = "";
						String month = "";
						String day = "";
						calendar.setCustomDayView(new DefaultDayViewAdapter());
						Calendar startDate = Calendar.getInstance();
						Event event = events.get(i);
						String[] array = event.event_start_date.split("\\-");
						for (int j = 0; j < array.length; j++) {
							year = array[0];
							month = array[1];
							day = array[2];
						}
						//System.out.println(month+"Test"+(Integer.parseInt(month) -1));
						int month1 = Integer.parseInt(month);
						startDate.set(Integer.parseInt(year), month1 -1 , Integer.parseInt(day));
						dates.add(startDate.getTime());
						//System.out.println("Start Date" + startDate.getTime() + month);

						Calendar endDate = Calendar.getInstance();
						String[] array1 = event.event_end_date.split("\\-");
						for (int j = 0; j < array1.length; j++) {
							year = array1[0];
							month = array1[1];
							day = array1[2];
						}
						int month2 = Integer.parseInt(month);
						endDate.set(Integer.parseInt(year), month2 -1, Integer.parseInt(day));
						dates.add(endDate.getTime());
						//System.out.println("End Date" + endDate.getTime());

						calendar.setDecorators(Collections.<CalendarCellDecorator> emptyList());
						hashmap.put(i + 1, dates);

					}
					calendar.init(todayDate.getTime(), nextYear.getTime()).inMode(SelectionMode.RANGE)
							.withSelectedDates(hashmap);

					//System.out.println(dates.get(0) + "dates" + dates.get(1));
				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), error);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(),
							getResources().getString(R.string.error));
				}

			}
		}

	}

}
