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

import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.adapter.CollegeParentChildrenAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StudentProfileInfo;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;

public class CollegeParentChildrenListFragment extends Fragment {
	private ListView list;
	private CollegeParentChildrenAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<StudentProfileInfo> studentProfiles;
	StudentProfileInfo user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_list, container, false);
		initialize();
		return view;
	}

	private void initialize() {
		list = (ListView) view.findViewById(R.id.listview);
		if (studentProfiles != null) {

			adapter = new CollegeParentChildrenAdapter(getActivity(),
					studentProfiles);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			list.setOnItemClickListener(listener);
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new GetChildrenAsyntask().execute(nameValuePairs);
		}
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {


			for (int i = 0; i < arg0.getChildCount(); i++) {
				if (i == arg2) {
//					arg0.getChildAt(i).setBackgroundResource(
//							R.drawable.childon);
					} else {
//					arg0.getChildAt(i).setBackgroundResource(
//							R.drawable.childoff);
				}
			}
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			nameValuePairs.add(new BasicNameValuePair("child_id",studentProfiles.get(arg2).id));
			CollegeAppUtils.SetSharedParameter(getActivity(),
					Constants.CHILD_ID,
					studentProfiles.get(arg2).id);
			new GetSwapAsyntask().execute(nameValuePairs);



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

	private class GetChildrenAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.children));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_swap_children);

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_swap_children);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
						studentProfiles = new ArrayList<StudentProfileInfo>();
						for (int i = 0; i < array.length(); i++) {
							StudentProfileInfo studentProfile = new StudentProfileInfo(
									array.getJSONObject(i));
							studentProfiles.add(studentProfile);
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
				e.printStackTrace();

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
				adapter = new CollegeParentChildrenAdapter(getActivity(),
						studentProfiles);
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				list.setOnItemClickListener(listener);
			} else {
				if (studentProfiles != null) {
					adapter = new CollegeParentChildrenAdapter(getActivity(),
							studentProfiles);
					list.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					list.setOnItemClickListener(listener);

				}
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(getActivity(), getActivity()
								.getTitle().toString(), error);
					} else {
						CollegeAppUtils
								.showDialog(
										getActivity(),
										getActivity().getTitle().toString(),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							getActivity(),
							getActivity().getTitle().toString(),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}
	}

	private class GetSwapAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		int position;

		/*
		 * public GetAssignmentAsyntask() { this.position = position; }
		 */

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.swap));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			//studentProfiles = new ArrayList<StudentProfileInfo>();
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_swap_children);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_swap_child);
			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONObject jsonObj = json.getJSONObject("childs");
						user = new StudentProfileInfo(jsonObj);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
			    dialog.dismiss();
				dialog = null;
			}
			if (result) {
				
				CollegeParentMainActivity.name.setText(user.name);
				CollegeParentMainActivity.classs.setText(user.class_name);
				CollegeAppUtils.SetSharedParameter(getActivity(), "session_student_id", user.id);
				CollegeAppUtils.SetSharedParameter(getActivity(), "student_name", user.name);
				CollegeAppUtils.SetSharedParameter(getActivity(), "student_class", user.class_name);
				CollegeAppUtils.SetSharedParameter(getActivity(), "student_image", user.image);


			} else {

			}
		}

	}

}
