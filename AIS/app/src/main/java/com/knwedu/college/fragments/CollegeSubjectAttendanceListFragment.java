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

import com.knwedu.ourschool.AttendanceInfoActivity;
import com.knwall.knwedumycollegeapp.R;
import com.knwedu.ourschool.adapter.TeacherAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;

public class SubjectAttendanceListFragment extends Fragment {
	private ListView list;
	private TeacherAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Subject> subjects;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_teacher_assignment_list,
				container, false);
		initialize();
		return view;
	}

	private void initialize() {
		list = (ListView) view.findViewById(R.id.listview);
		if (subjects != null) {
			adapter = new TeacherAdapter(getActivity(), subjects, true);
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

			nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
			new GetAssignmentAsyntask().execute(nameValuePairs);
		}
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					AttendanceInfoActivity.class);
			intent.putExtra(Constants.ID, subjects.get(arg2).object.toString());
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
			subjects = new ArrayList<Subject>();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					Urls.api_activity_get_subjects);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						;// //////////////////////////////////////////////////////
						subjects = new ArrayList<Subject>();
						for (int i = 0; i < array.length(); i++) {
							Subject assignment = new Subject(
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
			if (result) {
				if (subjects != null) {
					adapter = new TeacherAdapter(getActivity(),
							subjects,true);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}
			} else {
				if (subjects != null) {
					adapter = new TeacherAdapter(getActivity(),
							subjects,true);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
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