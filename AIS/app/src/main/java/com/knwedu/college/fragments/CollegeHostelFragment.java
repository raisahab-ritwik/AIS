package com.knwedu.college.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Hostel;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollegeHostelFragment extends Fragment {
	private TextView tvHostelCode;
	private TextView tvHostelName;
	private TextView tvRoomNo;
	private TextView tvBedNo;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Hostel> hostels;
	private RelativeLayout mainLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.hostel_view, container, false);
		initialize();
		return view;
	}

	public CollegeHostelFragment() {
		super();
	}

	private void initialize() {
		tvHostelCode = (TextView) view.findViewById(R.id.txt_hostel_code);
		tvHostelName = (TextView) view.findViewById(R.id.txt_hostel_name);
		tvRoomNo = (TextView) view.findViewById(R.id.txt_room_no);
		tvBedNo=(TextView) view.findViewById(R.id.txt_bed_no);
		mainLayout = (RelativeLayout) view.findViewById(R.id.main_layout);
	}

	@Override
	public void onResume() {
		super.onResume();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(
				new BasicNameValuePair("user_id", CollegeAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		new GetHostelAsyntask().execute(nameValuePairs);
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

	private class GetHostelAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
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
			Log.d("url extension: ", CollegeUrls.api_get_hostel);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			hostels = new ArrayList<Hostel>();
			hostels.clear();
			CollegeJsonParser jParser = new CollegeJsonParser();

			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, CollegeUrls.api_get_hostel);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("info");
						} catch (Exception e) {
							error = "No Data Found";
							return false;
						}
						for (int i = 0; i < array.length(); i++) {
							Hostel hostel = new Hostel(array.getJSONObject(i));
							hostels.add(hostel);
						}
						return true;
					} else {
						try {
							error = json.getString("info");
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
				for (int i = 0; i < hostels.size(); i++) {
					Hostel hostel = hostels.get(i);
					tvHostelCode.setText(hostel.hostel_code);
					tvHostelName.setText(hostel.hostel_name);
					tvRoomNo.setText(hostel.hostel_room_no);
					tvBedNo.setText(hostel.hostel_bed_no);
				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), error);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(),
							getResources().getString(R.string.error));
				}
				mainLayout.setVisibility(View.GONE);
			}
		}

	}

}
