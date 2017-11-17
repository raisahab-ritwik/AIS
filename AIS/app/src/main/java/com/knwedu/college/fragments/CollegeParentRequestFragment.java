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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.college.CollegeParentChildLeaveActivity;
import com.knwedu.college.CollegeParentRequestStatusListActivity;
import com.knwedu.college.adapter.CollegeRequestAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.RequestsStatus;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeParentRequestFragment extends Fragment {
	private View view;
	private ListView listRequest;
	private ProgressDialog dialog;
	private ArrayList<RequestsStatus> requests;
	private ArrayList<String> requestTitle;
	private CollegeRequestAdapter mAdapter;
	private TextView txtStatus;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_activity_request, container,
				false);
		initialize();
		return view;
	}

	private void initialize() {
//		ImageView adImageView = (ImageView)view.findViewById(R.id.adImageView);
//		if(Integer.parseInt(SchoolAppUtils.GetSharedParameter(getActivity(),
//				Constants.USERTYPEID)) == 5){
//			adImageView.setVisibility(View.VISIBLE);
//
//		}else{
//			adImageView.setVisibility(View.GONE);
//		}
//		adImageView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				startActivity(new Intent(getActivity(),
//						AdvertisementWebViewActivity.class));
//			}
//		});

		listRequest = (ListView) view.findViewById(R.id.listRequest);
		txtStatus = (TextView) view.findViewById(R.id.textStatus);
		listRequest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent i = new Intent(getActivity(),
						CollegeParentChildLeaveActivity.class);
				i.putExtra("request_id", requests.get(position).id);
				i.putExtra("request_show_date", requests.get(position).show_date);
				i.putExtra(CollegeConstants.PAGE_TITLE,
						requestTitle.get(position));
				startActivity(i);

			}

		});
		txtStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// requestview
				 if (CollegeAppUtils.GetSharedParameter(getActivity(),
							CollegeConstants.COLLEGE_REQUEST_VIEW).equals("1")) {
				Intent i = new Intent(getActivity(),
						CollegeParentRequestStatusListActivity.class);
				startActivity(i);
				
				 }
				 else
						
					{
						Toast.makeText(getActivity(), "you don't have permission to view request,Thankyou!!!",Toast.LENGTH_LONG).show();
					}
			}
		});
		if (CollegeAppUtils.GetSharedParameter(getActivity(),
				CollegeConstants.COLLEGE_REQUEST_CREATE).equals("1")) {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new RequestAsync().execute(nameValuePairs);
		
		} 
		else
			
		{
			Toast.makeText(getActivity(), "you don't have permission to request,Thankyou!!!",Toast.LENGTH_LONG).show();
		}
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

	private class RequestAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		private String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.requests));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			requests = new ArrayList<RequestsStatus>();
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_child_request);
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_child_request);

			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						requests = new ArrayList<RequestsStatus>();
						requestTitle = new ArrayList<String>();
						for (int i = 0; i < array.length(); i++) {
							RequestsStatus req = new RequestsStatus(
									array.getJSONObject(i));
							CollegeAppUtils.SetSharedParameter(getActivity(),
									"request_id", req.id);
							requestTitle.add(req.reason_title);
							requests.add(req);
						}
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				if (requests != null) {
					mAdapter = new CollegeRequestAdapter(getActivity(),
							requests);
					listRequest.setAdapter(mAdapter);
				}
			}
		}
	}
}
