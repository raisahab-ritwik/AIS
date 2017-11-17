package com.knwedu.college.fragments;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TimeTable;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeTeacherTimeTableFragment extends Fragment {
    private String dateSelected;
    private ArrayList<TimeTable> timeTable;
    private ListView listView;
    private int[] items;
    private TimeTableAdapter adapter;
    private ProgressDialog dialog;
    public CollegeDBAdapter mDatabase;
    private View view;
    private Spinner date;
    private List<String> days;
    private String mode = "timetable";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.college_fragment_teacher_timetable,
                container, false);
        initialize();

        return view;
    }


    private void initialize() {
        date = (Spinner) view.findViewById(R.id.date_btns);
        listView = (ListView) view.findViewById(R.id.listview);
        dateSelected = CollegeAppUtils.getCurrentDate();
        Log.d("dateSelected", dateSelected);
        days = new ArrayList<String>();
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");

        mDatabase = ((com.knwedu.ourschool.db.SchoolApplication) getActivity()
                .getApplication()).getCollegeDatabase();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.college_simple_spinner_item_custom_new,
                days);
        dataAdapter
                .setDropDownViewResource(R.layout.college_simple_spinner_dropdown_item_custom_new);
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
        if (CollegeAppUtils.isOnline(getActivity())) {
            // initialize();

            if (timeTable != null) {
                initilizeList(CollegeAppUtils.getDay(dateSelected));
            } else {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        0);
                nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
                        .GetSharedParameter(getActivity(), "id")));
                Log.d("PARAMS OF TEACHER", "" + nameValuePairs);
                new GetListAsyntask().execute(nameValuePairs);
            }
        } else {
            view.findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
            new OfflineSubjectDetailsAsync().execute();
        }
    }

    private void initilizeList(String day) {
        ArrayList<TimeTable> temp = new ArrayList<TimeTable>();
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

        adapter = new TimeTableAdapter(CollegeAppUtils.getDay(dateSelected),
                temp);
        listView.setAdapter(adapter);
    }

    private class TimeTableAdapter extends BaseAdapter {
        ViewHolder holder;
        private LayoutInflater inflater;
        private String day;
        private ArrayList<TimeTable> timeTable;

        public TimeTableAdapter(String day, ArrayList<TimeTable> timeTable) {
            inflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            this.day = day;
            this.timeTable = timeTable;
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
                convertView = inflater.inflate(
                        R.layout.college_teacher_timetable_item, null);
                holder = new ViewHolder();
                holder.lecture = (TextView) convertView
                        .findViewById(R.id.lecture_txt);
                holder.subject = (TextView) convertView
                        .findViewById(R.id.start_time_txt);
                holder.className = (TextView) convertView
                        .findViewById(R.id.facility_txt);
                holder.section = (TextView) convertView
                        .findViewById(R.id.subject_txt);
                holder.startTime = (TextView) convertView
                        .findViewById(R.id.start_time);
                holder.endTime = (TextView) convertView
                        .findViewById(R.id.end_time);
                holder.facility = (TextView) convertView
                        .findViewById(R.id.facility_num);
                holder.class_type = (TextView) convertView
                        .findViewById(R.id.txtClassType);
                holder.teacher_name_text = (TextView) convertView.findViewById(R.id.teacher_name_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.lecture.setText(this.timeTable.get(position).lecture_num);
            holder.className.setText(this.timeTable.get(position).program);
            holder.section.setText(this.timeTable.get(position).section_id);
            holder.startTime.setText(this.timeTable.get(position).start);
            holder.endTime.setText(this.timeTable.get(position).end);
            holder.facility.setText(this.timeTable.get(position).facility);
            holder.teacher_name_text.setText(this.timeTable.get(position).teacher_name);

            if (this.timeTable.get(position).subject.equalsIgnoreCase("null")) {
                holder.subject.setText("Break");
            } else {
                if ((this.timeTable.get(position).subject_type
                        .equalsIgnoreCase(""))
                        && (this.timeTable.get(position).group_name
                        .equalsIgnoreCase("")))
                    holder.subject
                            .setText(this.timeTable.get(position).subject);
                else
                    holder.subject.setText(this.timeTable.get(position).subject
                            + "" + "("
                            + this.timeTable.get(position).subject_type + ""
                            + ")" + this.timeTable.get(position).group_name);

            }
            /*
			 * if (this.timeTable.get(position).class_type.equalsIgnoreCase(""))
			 * { holder.class_type.setText(""); } else { holder.class_type
			 * .setText(this.timeTable.get(position).class_type + "" + "" +
			 * "Class"); }
			 */
            return convertView;
        }

        private class ViewHolder {
            TextView lecture, subject, className, section, startTime, endTime,
                    facility, class_type, teacher_name_text;
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressLint("NewApi")
    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    private class GetListAsyntask extends
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
            CollegeJsonParser jParser = new CollegeJsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nvp,
                    CollegeUrls.api_timetable_user);
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
                // do Nothing...
                // dateSelected = "Mon";

            } else {

				/*
				 * * DataBaseHelper dataBaseHelper = new
				 * DataBaseHelper(getActivity()); timeTable = new
				 * ArrayList<TimeTable>(); timeTable =
				 * dataBaseHelper.getAllTeacherTimeTable
				 * (SchoolAppUtils.GetSharedParameter(getActivity(),
				 * Constants.USERNAME),
				 * SchoolAppUtils.GetSharedParameter(getActivity(),
				 * Constants.PASSWORD),mode); dataBaseHelper.close();
				 */
            }

            if (timeTable != null) {
                if (timeTable.size() > 0) {

                    mDatabase.open(); // Delete all data in database
                    mDatabase.deleteAllTimetable();
                    for (int i = 0; i < timeTable.size(); i++) {
                        Log.d("Insert: ", "Inserting ..");
                        mDatabase.addTimetable(timeTable.get(i));
                    }
                    mDatabase.close();

                    initilizeList(CollegeAppUtils.getDay(dateSelected));

                }
            }

			/*
			 * * if(result) { if(timeTable != null) { if(timeTable.size() > 0) {
			 * initilizeList(SchoolAppUtils.getDay(dateSelected)); } } } else {
			 * if(timeTable != null) { if(timeTable.size() > 0) {
			 * initilizeList(SchoolAppUtils.getDay(dateSelected)); } } if(error
			 * != null) { if(error.length() > 0) {
			 * SchoolAppUtils.showDialog(getActivity
			 * (),getResources().getString(R.string.error), error); } else {
			 * SchoolAppUtils
			 * .showDialog(getActivity(),getResources().getString(R
			 * .string.error),
			 * getResources().getString(R.string.please_check_internet_connection
			 * )); } } else {
			 * SchoolAppUtils.showDialog(getActivity(),getResources
			 * ().getString(R.string.error),
			 * getResources().getString(R.string.please_check_internet_connection
			 * )); } }
			 */

        }
    }

    class OfflineSubjectDetailsAsync extends AsyncTask<String, Subject, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            timeTable = new ArrayList<TimeTable>();
            timeTable.clear();
        }

        protected Void doInBackground(String... params) {

            mDatabase.open();
            timeTable = mDatabase.getAllTimetable();
            mDatabase.close();

            return null;
        }

        @Override
        protected void onProgressUpdate(Subject... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mDatabase.close();
            Log.d("DATA List in subject", "" + timeTable);
            if (timeTable != null) {
                if (timeTable.size() > 0) {
                    initilizeList(CollegeAppUtils.getDay(dateSelected));
                }

            }
        }

    }

}
