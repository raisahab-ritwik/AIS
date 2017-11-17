package com.knwedu.ourschool.fragments;

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
import android.widget.ImageView;
import android.widget.ListView;

import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.CareerInterviewActivity;
import com.knwedu.ourschool.adapter.CareerListAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Career;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CareerFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Career> careers = new ArrayList<Career>();
	private ListView listviewCareer;
	private CareerListAdapter adapter;
	private ImageView btnInterview, btnViewSuccessful;
	private int type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.career_fragment_view, container, false);
		initialize();

		return view;
	}

	public CareerFragment(int type){
		this.type = type;
	}

	private void initialize() {
		// TODO Auto-generated method stub
		listviewCareer = (ListView) view.findViewById(R.id.listviewCareer);
		btnInterview = (ImageView) view.findViewById(R.id.btnViewInterview);
		btnViewSuccessful = (ImageView) view.findViewById(R.id.btnViewSuccessful);
		btnInterview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CareerInterviewActivity.class);
				intent.putExtra(CollegeConstants.PAGE_TITLE,"Interview");
				intent.putExtra("Type", type);
				intent.putExtra("CandidateType", 0);
				startActivity(intent);

			}
		});
		btnViewSuccessful.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CareerInterviewActivity.class);
				intent.putExtra(CollegeConstants.PAGE_TITLE,"Successful Candidate");
				intent.putExtra("Type", type);
				intent.putExtra("CandidateType", 1);
				startActivity(intent);

			}
		});
		/**
		 * Fetching Career list items
		 */
		requetsForCareers();
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	private void requetsForCareers() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));

		if(type == 1){
			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		}

		new RequestCareerListAsyntask().execute(nameValuePairs);
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

	private class RequestCareerListAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
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
			Log.d("url extension: ", Urls.api_career_list);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			careers.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_career_list);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							Career career = new Career(array.getJSONObject(i));
							careers.add(career);
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
				if (careers != null) {
				
					adapter = new CareerListAdapter(getActivity(), careers);
					listviewCareer.setAdapter(adapter);
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
