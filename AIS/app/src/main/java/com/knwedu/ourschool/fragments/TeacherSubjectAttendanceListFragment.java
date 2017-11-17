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
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.TeacherAttendanceListActivity;
import com.knwedu.ourschool.adapter.TeacherSubjectAdapterByTimeTable;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DataStructureFramwork.TeacherSubjectByTimeTable;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherSubjectAttendanceListFragment extends Fragment {
	private ListView list;
	private TeacherSubjectAdapterByTimeTable adapter;
	private View view;
	private ProgressDialog dialog;
	private String dateSelected;
	public DatabaseAdapter mDatabase;
	private ArrayList<TeacherSubjectByTimeTable> subjects;
	private ArrayList<String> attendance_sub;
	private TextView textEmpty;
	private boolean internetAvailable = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_teacher_assignment_list,
				container, false);

		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));
		dateSelected = SchoolAppUtils.getCurrentDate();
		attendance_sub = new ArrayList<String>();
		list = (ListView) view.findViewById(R.id.listview);
		mDatabase = ((SchoolApplication) getActivity().getApplication())
				.getDatabase();
		textEmpty = (TextView) view.findViewById(R.id.textEmpty);
		if (SchoolAppUtils.isOnline(getActivity())) {
			internetAvailable = true;
			initialize();
		} else {
			view.findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
			new OfflineSubjectDetailsAsync().execute();

		}
		return view;
	}

	private void initialize() {
		// list = (ListView) view.findViewById(R.id.listview);

		dateSelected = SchoolAppUtils.getCurrentDate();
		if (subjects != null) {
			String temp = null;
			for (int i = 0; i < subjects.size(); i++) {
				if (i == 0) {
					subjects.get(0).check = true;
					temp = subjects.get(0).classname + " "
							+ subjects.get(0).section_name;
				} else {

					if (!temp.equalsIgnoreCase(subjects.get(i).classname + " "
							+ subjects.get(i).section_name)) {
						subjects.get(i).check = true;
						temp = subjects.get(i).classname + " "
								+ subjects.get(i).section_name;
					}
				}
			}
			adapter = new TeacherSubjectAdapterByTimeTable(getActivity(),
					subjects, true);
			list.setAdapter(adapter);
			list.setOnItemClickListener(listener);
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			new GetAssignmentAsyntask().execute(nameValuePairs);

		}
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (SchoolAppUtils.isOnline(getActivity())) {

				if (subjects.get(arg2).is_mark.equals("1")) {

					Intent intent = new Intent(getActivity(),
							TeacherAttendanceListActivity.class);
					intent.putExtra(Constants.ID,
							subjects.get(arg2).object.toString());
					intent.putExtra("Date", SchoolAppUtils.getCurrentDate());
					intent.putExtra(Constants.PAGE_TITLE, getActivity()
							.getTitle().toString());
					getActivity().startActivity(intent);

				} else {
					Toast.makeText(getActivity(), "No Permission",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Intent intent = new Intent(getActivity(),
						TeacherAttendanceListActivity.class);
				intent.putExtra(Constants.OFFLINE_SUBJECT_ID,
						subjects.get(arg2).srid.toString());
				intent.putExtra(Constants.OFFLINE_SECTION_ID,
						subjects.get(arg2).section_id.toString());
				intent.putExtra(Constants.OFFLINE_TEACHER_ID,
						subjects.get(arg2).teacher_id.toString());
				Log.d("Teacher_id", subjects.get(arg2).teacher_id.toString());
				intent.putExtra(Constants.OFFLINE_GROUP_ID,
						subjects.get(arg2).group_id.toString());
				intent.putExtra(Constants.OFFLINE_LECTURE_NUM,
						subjects.get(arg2).lecture_num.toString());
				intent.putExtra(Constants.OFFLINE_SECTION_NAME,
						subjects.get(arg2).section_name.toString());
				intent.putExtra(Constants.OFFLINE_CLASS_NAME,
						subjects.get(arg2).classname.toString());
				intent.putExtra("Date", SchoolAppUtils.getCurrentDate());
				intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
						.toString());
				getActivity().startActivity(intent);

			}

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
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

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
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_activity_get_subjects_teacher);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			subjects = new ArrayList<TeacherSubjectByTimeTable>();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_activity_get_subjects_teacher);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
						subjects = new ArrayList<TeacherSubjectByTimeTable>();
						for (int i = 0; i < array.length(); i++) {
							TeacherSubjectByTimeTable assignment = new TeacherSubjectByTimeTable(
									array.getJSONObject(i));
							subjects.add(assignment);
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

			if (subjects != null && result == true) {
				String temp = null;
				for (int i = 0; i < subjects.size(); i++) {
					if (i == 0) {
						subjects.get(0).check = true;
						temp = subjects.get(0).classname + " "
								+ subjects.get(0).section_name;

					} else {
						if (!temp.equalsIgnoreCase(subjects.get(i).classname
								+ " " + subjects.get(i).section_name)) {
							subjects.get(i).check = true;
							temp = subjects.get(i).classname + " "
									+ subjects.get(i).section_name;
						}
					}
				}
				adapter = new TeacherSubjectAdapterByTimeTable(getActivity(),
						subjects, true);
				list.setAdapter(adapter);
				list.setOnItemClickListener(listener);
			}

			else {
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

	class OfflineSubjectDetailsAsync extends AsyncTask<String, Subject, Void> {

		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected Void doInBackground(String... params) {

			mDatabase.open();
			if ((SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.ATTENDANCE_TYPE_PERMISSION))
					.equalsIgnoreCase("1")) {
				subjects = mDatabase
						.getAllofflineSubjectforattendance(SchoolAppUtils
								.getDay(dateSelected));
			} else {
				subjects = mDatabase.getAllSubjectforattendance(SchoolAppUtils
						.getDay(dateSelected));
			}
			mDatabase.close();

			return null;
		}

		@Override
		protected void onProgressUpdate(Subject... values) {
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (subjects != null) {
				String temp = "";
				for (int i = 0; i < subjects.size(); i++) {
					if (i == 0) {
						subjects.get(0).check = true;
						temp = subjects.get(0).classname + " "
								+ subjects.get(0).section_name;
						/*
						 * attendance_sub.add(subjects.get(0).srid);
						 * Log.d("Sr_id", subjects.get(0).srid);
						 */
						

					} else {
						if (!temp.equalsIgnoreCase(subjects.get(i).classname
								+ " " + subjects.get(i).section_name)) {
							subjects.get(i).check = true;
							temp = subjects.get(i).classname + " "
									+ subjects.get(i).section_name;

						}
					}

				}

				if (subjects.size() > 0) {
					textEmpty.setVisibility(View.GONE);
					adapter = new TeacherSubjectAdapterByTimeTable(
							getActivity(), subjects, true);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				} else {
					textEmpty.setText("No Subjects available in Offline mode");
					textEmpty.setVisibility(View.VISIBLE);
				}

			}
		}

	}
}
