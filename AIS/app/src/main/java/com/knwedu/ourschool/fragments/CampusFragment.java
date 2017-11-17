package com.knwedu.ourschool.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.CampusDetailsActivity;
import com.knwedu.ourschool.adapter.CampusDetailsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.CampusActivity;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

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
import android.widget.ListView;

public class CampusFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;
	private ArrayList<CampusActivity> campusActivities = new ArrayList<CampusActivity>();
	private ListView listviewCampusActivities;
	private CampusDetailsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.campus_view, container, false);
		initialize();

		return view;
	}

	private void initialize() {
		// TODO Auto-generated method stub
		listviewCampusActivities = (ListView) view.findViewById(R.id.listviewCampus);
		listviewCampusActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CampusDetailsActivity.class);
				intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle().toString());
				intent.putExtra("campus_id", campusActivities.get(position).id);
				intent.putExtra("campus_title", campusActivities.get(position).title);
				intent.putExtra("campus_description", campusActivities.get(position).description);
				intent.putExtra("campus_created_by", campusActivities.get(position).created_by);
				intent.putExtra("campus_created_on", campusActivities.get(position).created_date);
				intent.putExtra("campus_attachment", campusActivities.get(position).attachment);
				getActivity().startActivity(intent);
			}

		});
		requetsForCampuses();
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	private void requetsForCampuses() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));

		new RequestCampusDetailsAsyntask().execute(nameValuePairs);
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

	private class RequestCampusDetailsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
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
			Log.d("url extension: ", Urls.api_campus_details);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			campusActivities.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_campus_details);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							CampusActivity campus = new CampusActivity(array.getJSONObject(i));
							campusActivities.add(campus);
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

			} catch (

			JSONException e) {

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
				if (campusActivities != null) {
				
					adapter = new CampusDetailsAdapter(getActivity(), campusActivities);
					listviewCampusActivities.setAdapter(adapter);
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
