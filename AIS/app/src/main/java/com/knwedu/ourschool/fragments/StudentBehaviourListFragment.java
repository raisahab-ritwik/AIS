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
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.BehaviourAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Behaviour;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassFellows;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class StudentBehaviourListFragment extends Fragment {
	private ArrayList<Behaviour> behaviour;
	private ProgressDialog dialog;
	private TextView header;
	private ListView listView;
	private BehaviourAdapter adapter;
	ImageButton addAssignment;
	private Subject subject;
	private ClassFellows class_fellow;
	private Button monthlyWeekly;
	private TextView textEmpty;
	public DatabaseAdapter mDatabase;
	private boolean internetAvailable = false;
	private String page_title = "";
	private ArrayList<ClassFellows> fellows;
	private View view;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mDatabase = ((SchoolApplication) getActivity().getApplication())
				.getDatabase();
		view = inflater.inflate(R.layout.fragment_teacher_assignment_list,
				container, false);
		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));
		textEmpty = (TextView) view.findViewById(R.id.textEmpty);
		listView = (ListView) view.findViewById(R.id.listview);
		if (SchoolAppUtils.isOnline(getActivity())) {
			internetAvailable = true;
			getOnlineData();
		} else {
			view.findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);

		}
		return view;
	}

	private void getOnlineData() {
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
		
		new GetAssignmentsAsyntask().execute(nameValuePairs);

	}

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

	private class GetAssignmentsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String mode = "class_anouncements";
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			
			List<NameValuePair> namevaluepair = params[0];
			Log.d("url extension: ", Urls.api_behaviour_view);
			String parameters = "";
			for (NameValuePair nvp : namevaluepair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			behaviour = new ArrayList<Behaviour>();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_behaviour_view);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							error = "Error in Data";
							return false;
						}
						for (int i = 0; i < array.length(); i++) {
							Behaviour assignment = new Behaviour(
									array.getJSONObject(i));
							behaviour.add(assignment);
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
				error = "Connection Failure";
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
				if (behaviour != null) {
					String temp = null;

					if (behaviour.size() > 0) {
						textEmpty.setVisibility(View.GONE);
						adapter = new BehaviourAdapter(
								getActivity(), behaviour,"student");
						listView.setAdapter(adapter);

					} else {
						textEmpty.setText("No data available");
						textEmpty.setVisibility(View.VISIBLE);
					}

				}
			} else {
				textEmpty.setText(error.toString());
				textEmpty.setVisibility(View.VISIBLE);
			}

		}

	}
}
