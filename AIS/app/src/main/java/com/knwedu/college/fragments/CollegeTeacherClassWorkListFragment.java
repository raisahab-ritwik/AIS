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

import com.knwedu.college.CollegeTeacherClassWorkListActivity;
import com.knwedu.college.adapter.CollegeTeacherAdapter;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;

public class CollegeTeacherClassWorkListFragment extends Fragment {
	private ListView list;
	private CollegeTeacherAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Subject> subjects;
	public CollegeDBAdapter mDatabase;
	private TextView textEmpty;
	private boolean internetAvailable = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mDatabase = ((SchoolApplication) getActivity().getApplication())
				.getCollegeDatabase();
		view = inflater.inflate(R.layout.college_fragment_teacher_assignment_list,
				container, false);
		textEmpty = (TextView) view.findViewById(R.id.textEmpty);
		list = (ListView) view.findViewById(R.id.listview);
		subjects = new ArrayList<Subject>();
		if (CollegeAppUtils.isOnline(getActivity())) {
			internetAvailable = true;
			getOnlineData();
		} else {
			view.findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
			new OfflineSubjectDetailsAsync().execute();
		}
		return view;
	}

	private void getOnlineData() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(getActivity(), "id")));

		new GetSubjectsAsyntask().execute(nameValuePairs);

	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					CollegeTeacherClassWorkListActivity.class);
			intent.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
			if (internetAvailable) {
				intent.putExtra(CollegeConstants.TANNOUNCEMENT,
						subjects.get(arg2).object.toString());
			} else {

				intent.putExtra(CollegeConstants.OFFLINE_SUBJECT_ID,
						subjects.get(arg2).subject_relation_id);
			}
			intent.putExtra(CollegeConstants.PAGE_TITLE, getActivity()
					.getTitle().toString());

			getActivity().startActivity(intent);

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

	private class GetSubjectsAsyntask extends
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
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_activity_get_subjects);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_activity_get_subjects);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("info");
						} catch (Exception e) {
							return true;
						}
						// //////////////////////////////////////////////////////

						subjects.clear();
						for (int i = 0; i < array.length(); i++) {
							Subject subject = new Subject(
									array.getJSONObject(i));

							subjects.add(subject);
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
			} else {
			}

			if (subjects != null) {

				mDatabase.open();
				// Delete all data in database
				mDatabase.deleteAllSubjects();
				String temp = null;
				for (int i = 0; i < subjects.size(); i++) {
					if (i == 0) {
						subjects.get(0).check = true;
						temp = subjects.get(0).classs + " "
								+ subjects.get(0).section_name;
					} else {
						if (!temp.equalsIgnoreCase(subjects.get(i).classs + " "
								+ subjects.get(i).section_name)) {
							subjects.get(i).check = true;
							temp = subjects.get(i).classs + " "
									+ subjects.get(i).section_name;
						}
					}

					Log.d("Insert: ", "Inserting ..");
					mDatabase.addSubjects(subjects.get(i));
				}

				mDatabase.close();
				if (subjects.size() > 0) {
					textEmpty.setVisibility(View.GONE);
					adapter = new CollegeTeacherAdapter(getActivity(),
							subjects, true);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				} else {
					textEmpty.setText("No Classwork available");
					textEmpty.setVisibility(View.VISIBLE);
				}

			}
		}

	}

	class OfflineSubjectDetailsAsync extends AsyncTask<String, Subject, Void> {

		protected void onPreExecute() {
			super.onPreExecute();
			subjects.clear();
		}

		protected Void doInBackground(String... params) {

			mDatabase.open();
			subjects = mDatabase.getAllSubject();
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
						temp = subjects.get(0).classs + " "
								+ subjects.get(0).section_name;
					} else {
						if (!temp.equalsIgnoreCase(subjects.get(i).classs + " "
								+ subjects.get(i).section_name)) {
							subjects.get(i).check = true;
							temp = subjects.get(i).classs + " "
									+ subjects.get(i).section_name;
						}
					}
				}
				if (subjects.size() > 0) {
					textEmpty.setVisibility(View.GONE);
					adapter = new CollegeTeacherAdapter(getActivity(),
							subjects, true);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				} else {
					textEmpty.setText("No Classwork available in Offline mode");
					textEmpty.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}