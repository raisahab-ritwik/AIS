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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.college.CollegeTeacherAttendanceListActivity;
import com.knwedu.college.adapter.CollegeTeacherSubjectAdapterByTimeTable;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TeacherSubjectByTimeTable;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeTeacherSubjectAttendanceListFragment extends Fragment {
	private ListView list;
	private CollegeTeacherSubjectAdapterByTimeTable adapter;
	private View view;
	private ProgressDialog dialog;
	private String dateSelected;
	/*public DatabaseAdapter mDatabase*/;
	private ArrayList<TeacherSubjectByTimeTable> subjects;
	private ArrayList<String> attendance_sub;
	private TextView textEmpty;
	private boolean internetAvailable = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_teacher_assignment_list,
				container, false);
		dateSelected = CollegeAppUtils.getCurrentDate();
		attendance_sub= new ArrayList<String>();
		list = (ListView) view.findViewById(R.id.listview);
		/*mDatabase = ((SchoolApplication) getActivity().getApplication())
				.getDatabase();*/
		textEmpty = (TextView) view.findViewById(R.id.textEmpty);
		if (CollegeAppUtils.isOnline(getActivity())) {
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
		
		dateSelected = CollegeAppUtils.getCurrentDate();
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
			adapter = new CollegeTeacherSubjectAdapterByTimeTable(getActivity(),
					subjects, true);
			list.setAdapter(adapter);
			list.setOnItemClickListener(listener);
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new GetAssignmentAsyntask().execute(nameValuePairs);

		}
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (CollegeAppUtils.isOnline(getActivity()) && (CollegeAppUtils.GetSharedParameter(
					getActivity(),
					CollegeConstants.COLLEGE_ATTENDANCE_MARK)).equals("1")) {
				Intent intent = new Intent(getActivity(),
						CollegeTeacherAttendanceListActivity.class);
				intent.putExtra(CollegeConstants.ID,
						subjects.get(arg2).object.toString());
				intent.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
				intent.putExtra("Date", CollegeAppUtils.getCurrentDate());
				getActivity().startActivity(intent);
				}
				else
				{
					Toast.makeText(getActivity(), "You don't have permission to mark Attendance, Thankyou!!!",Toast.LENGTH_LONG).show();
						/*Intent intent = new Intent(getActivity(),
								CollegeTeacherAttendanceListActivity.class);
						intent.putExtra(CollegeConstants.OFFLINE_SUBJECT_ID,
								subjects.get(arg2).srid.toString());
						intent.putExtra(CollegeConstants.OFFLINE_SECTION_ID,
								subjects.get(arg2).section_id.toString());
						intent.putExtra(CollegeConstants.OFFLINE_TEACHER_ID,
								subjects.get(arg2).teacher_id.toString());
						intent.putExtra(CollegeConstants.OFFLINE_GROUP_ID,
								subjects.get(arg2).group_id.toString());
						intent.putExtra(CollegeConstants.OFFLINE_LECTURE_NUM,
								subjects.get(arg2).lecture_num.toString());
						intent.putExtra(CollegeConstants.OFFLINE_SECTION_NAME,
								subjects.get(arg2).sub_name.toString());
						intent.putExtra(CollegeConstants.OFFLINE_CLASS_NAME,
								subjects.get(arg2).classname.toString());
						intent.putExtra("Date", CollegeAppUtils.getCurrentDate());
						getActivity().startActivity(intent);*/

					
					/*JSONObject c = null;
					try {
						c = new JSONObject(subjects.get(arg2).toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TeacherSubjectByTimeTable subject= new TeacherSubjectByTimeTable(c);
					Intent intent_off = new Intent(getActivity(),
							CollegeTeacherAttendanceListActivity.class);
					intent_off.putExtra(CollegeConstants.OFFLINE_SUBJECT_ID,
							subject.srid);
					System.out.println("subject.srid----"+subject.srid);
					intent_off.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
					
					intent_off.putExtra("Date", CollegeAppUtils.getCurrentDate());
					getActivity().startActivity(intent_off);*/
					
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
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.attendance));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_activity_get_subjects_teacher);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			subjects = new ArrayList<TeacherSubjectByTimeTable>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_activity_get_subjects_teacher);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}// //////////////////////////////////////////////////////
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

			/*
			 * if(result) { // do Nothing... } else { dbHandler = new
			 * DataBaseHelper(getActivity()); subjects = new
			 * ArrayList<Subject>(); subjects =
			 * dbHandler.getAllTeacherClasses(SchoolAppUtils
			 * .GetSharedParameter(getActivity(), Constants.USERNAME),
			 * SchoolAppUtils.GetSharedParameter(getActivity(),
			 * Constants.PASSWORD)); dbHandler.close(); }
			 */

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
				adapter = new CollegeTeacherSubjectAdapterByTimeTable(getActivity(),
						subjects, true);
				list.setAdapter(adapter);
				list.setOnItemClickListener(listener);
			}

			else {
				if (subjects != null) {

					adapter = new CollegeTeacherSubjectAdapterByTimeTable(
							getActivity(), subjects, true);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}

			}
		}

	}

	class OfflineSubjectDetailsAsync extends AsyncTask<String, Subject, Void> {

		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		protected Void doInBackground(String... params) {

			/*mDatabase.open();
			subjects = mDatabase.getAllSubjectforattendance(SchoolAppUtils.getDay(dateSelected));
			mDatabase.close();
*/
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
						attendance_sub.add(subjects.get(0).srid);
						Log.d("Sr_id", subjects.get(0).srid);
						
					} else {
						if (!temp.equalsIgnoreCase(subjects.get(i).classname + " "
								+ subjects.get(i).section_name)) {
							subjects.get(i).check = true;
							temp = subjects.get(i).classname + " "
									+ subjects.get(i).section_name;
							
						}
					}

				}

				if (subjects.size() > 0) {
					textEmpty.setVisibility(View.GONE);
					adapter = new CollegeTeacherSubjectAdapterByTimeTable(
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
