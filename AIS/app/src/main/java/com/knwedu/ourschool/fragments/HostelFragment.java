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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.BlogActivity;
import com.knwedu.ourschool.adapter.BlogAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.DataStructureFramwork.Hostel;
import com.knwedu.ourschool.vo.BlogBean;

public class HostelFragment extends Fragment {
	private TextView tvHostelCode;
	private TextView tvHostelName;
	private TextView tvRoomNo;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Hostel> hostels;
	private int type;
	private RelativeLayout mainLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.hostel_view, container, false);
		initialize();
		return view;
	}
	
	public HostelFragment(int type) {
		super();
		this.type = type;
	}

	private void initialize() {
		tvHostelCode = (TextView) view.findViewById(R.id.txt_hostel_code);
		tvHostelName = (TextView) view.findViewById(R.id.txt_hostel_name);
		tvRoomNo = (TextView) view.findViewById(R.id.txt_room_no);
		mainLayout = (RelativeLayout) view.findViewById(R.id.main_layout);
	}

	@Override
	public void onResume() {
		super.onResume();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.UORGANIZATIONID)));
		if(type == 1){
			nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		}
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

	private class GetHostelAsyntask extends
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
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_hostel);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			hostels = new ArrayList<Hostel>();
			hostels.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_hostel);
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
							Hostel hostel = new Hostel(
									array.getJSONObject(i));
							hostels.add(hostel);
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
				for (int i = 0; i < hostels.size(); i++) {
					Hostel hostel = hostels.get(i);
					tvHostelCode.setText(hostel.hostel_code);
					tvHostelName.setText(hostel.hostel_name);
					tvRoomNo.setText(hostel.hostel_room_no);
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
				mainLayout.setVisibility(View.GONE);
			}
		}

	}

}
