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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.TeacherWeeklyplanActivity;
import com.knwedu.ourschool.adapter.TeacherAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class SubjectListFragmentWeeklyPlan extends Fragment {
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
		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));
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

			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.CHILD_ID)));
			new GetSubjectListAsyntask().execute(nameValuePairs);
		}
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					TeacherWeeklyplanActivity.class);
			intent.putExtra(Constants.TSCHEDULEID,
					subjects.get(arg2).object.toString());
			intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
					.toString());
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

	private class GetSubjectListAsyntask extends
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
			List<NameValuePair> url = params[0];
			subjects = new ArrayList<Subject>();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					Urls.api_activity_get_subjects_weeklyplan);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
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
				if (subjects != null) {
					String temp = null;
					for (int i = 0; i < subjects.size(); i++) {
						if (i == 0) {
							subjects.get(0).check = true;
							temp = subjects.get(0).classs + " "
									+ subjects.get(0).section_id;
						} else {
							if (!temp.equalsIgnoreCase(subjects.get(i).classs
									+ " " + subjects.get(i).section_id)) {
								subjects.get(i).check = true;
								temp = subjects.get(i).classs + " "
										+ subjects.get(i).section_id;
							}
						}
					}
					adapter = new TeacherAdapter(getActivity(), subjects, true);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}
			} else {
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
