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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.knwedu.college.CollegeClassFellowListActivity;
import com.knwedu.college.adapter.CollegeSubjectAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.SubjectList;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeClaassFellowsFrag extends Fragment {
	private ListView list;
	private CollegeSubjectAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<SubjectList> subjects;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_teacher_assignment_list,
				container, false);
		initialize();
		return view;
	}

	private void initialize() {
		list = (ListView) view.findViewById(R.id.listview);
		if (subjects != null) {
			adapter = new CollegeSubjectAdapter(getActivity(), subjects, true);
			list.setAdapter(adapter);
			list.setOnItemClickListener(listener);
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("id",
					CollegeAppUtils.GetSharedParameter(getActivity(),
							"id")));
			new GetAssignmentAsyntask().execute(nameValuePairs);
		}
	}

	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					CollegeClassFellowListActivity.class);
			intent.putExtra("subject_relation_id", subjects.get(arg2).subject_relation_id);
			startActivity(intent);
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
			dialog.setTitle(getResources().getString(R.string.subjects));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> url = params[0];
			subjects = new ArrayList<SubjectList>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					CollegeUrls.api_student_get_subjects);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("info");
						} catch (Exception e) {
							return true;
						}
						;// //////////////////////////////////////////////////////
						subjects = new ArrayList<SubjectList>();
						for (int i = 0; i < array.length(); i++) {
							SubjectList assignment = new SubjectList(
									array.getJSONObject(i));
							subjects.add(assignment);
						}
						return true;
					} else {
						try {
							error = json.getString("info");
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
			} else {/*
					 * DataBaseHelper dataBaseHelper = new
					 * DataBaseHelper(getActivity()); subjects = new
					 * ArrayList<Subject>(); subjects =
					 * dataBaseHelper.getAllTeacherClasses
					 * (SchoolAppUtils.GetSharedParameter(getActivity(),
					 * Constants.USERNAME),
					 * SchoolAppUtils.GetSharedParameter(getActivity(),
					 * Constants.PASSWORD)); dataBaseHelper.close();
					 */
			}

			if (subjects != null) {
				String temp = null;
				for (int i = 0; i < subjects.size(); i++) {
					if (i == 0) {
						subjects.get(0).check = true;
						temp = subjects.get(0).program_name + " "
								+ subjects.get(0).term_name;
					} else {
						if (!temp.equalsIgnoreCase(subjects.get(i).program_name
								+ " " + subjects.get(i).term_name)) {
							subjects.get(i).check = true;
							temp = subjects.get(i).program_name + " "
									+ subjects.get(i).term_name;
						}
					}
				}
				adapter = new CollegeSubjectAdapter(getActivity(), subjects, true);
				list.setAdapter(adapter);
				list.setOnItemClickListener(listener);
			}

			/*
			 * else { if(subjects != null) {
			 * 
			 * adapter = new TeacherAdapter(getActivity(), subjects, true);
			 * list.setAdapter(adapter); list.setOnItemClickListener(listener);
			 * } if(error != null) { if(error.length() > 0) {
			 * SchoolAppUtils.showDialog
			 * (getActivity(),getResources().getString(R.string.error), error);
			 * } else {
			 * SchoolAppUtils.showDialog(getActivity(),getResources().getString
			 * (R.string.error),
			 * getResources().getString(R.string.please_check_internet_connection
			 * )); } } else {
			 * SchoolAppUtils.showDialog(getActivity(),getResources
			 * ().getString(R.string.error),
			 * getResources().getString(R.string.please_check_internet_connection
			 * )); }
			 * 
			 * }
			 */
		}

	}

}
