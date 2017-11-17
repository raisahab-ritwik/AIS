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

import com.knwedu.college.CollegeAssignmentActivity;
import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.adapter.CollegeAssignmentsAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeAssignmentListFragment extends Fragment {
	private ListView list;
	private CollegeAssignmentsAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Assignment> assignments;
	// private Spinner spinner;
	private final int type;
	private String pageTitle = "";

	public CollegeAssignmentListFragment(int type) {
		super();
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_assignment_list,
				container, false);
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
		pageTitle = getActivity().getTitle().toString();
		list = (ListView) view.findViewById(R.id.listview);
		handleButton();
		loadData(CollegeConstants.VIEW_ALL);

	}

	private void loadData(int listType) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(getActivity(), "id")));
		nameValuePairs.add(new BasicNameValuePair("list_type", String
				.valueOf(listType)));
		new GetAssignmentAsyntask().execute(nameValuePairs);
	}

	/*OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					CollegeAssignmentActivity.class);
			intent.putExtra(CollegeConstants.ASSIGNMENT_TYPE, 1);
			intent.putExtra(CollegeConstants.ASSIGNMENT,
					assignments.get(arg2).object.toString());
			intent.putExtra(CollegeConstants.PAGE_TITLE, getActivity()
					.getTitle().toString());
			getActivity().startActivity(intent);

		}
	};*/

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
		int position;

		/*
		 * public GetAssignmentAsyntask() { this.position = position; }
		 */

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.assignments));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			assignments = new ArrayList<Assignment>();
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_activity);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_activity);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							assignments = new ArrayList<Assignment>();
							for (int i = 0; i < array.length(); i++) {
								Assignment assignment = new Assignment(
										array.getJSONObject(i));
								assignments.add(assignment);
							}
						} catch (Exception e) {

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
				if (assignments != null) {
					String temp = null;
					for (int i = 0; i < assignments.size(); i++) {
						if (i == 0) {
							assignments.get(0).check = true;
							temp = assignments.get(0).submit_date;
						} else {
							if (!temp
									.equalsIgnoreCase(assignments.get(i).submit_date)) {
								assignments.get(i).check = true;
								temp = assignments.get(i).submit_date;
							}
						}
					}
					adapter = new CollegeAssignmentsAdapter(getActivity(),
							assignments,type,getActivity().getTitle().toString());
					list.setAdapter(adapter);
					//list.setOnItemClickListener(listener);
				}
			} else {
				if (assignments != null) {

					adapter = new CollegeAssignmentsAdapter(getActivity(),
							assignments,type,getActivity().getTitle().toString());
					list.setAdapter(adapter);
					//list.setOnItemClickListener(listener);
				}
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(getActivity(), pageTitle,
								error);
					} else {
						CollegeAppUtils
								.showDialog(
										getActivity(),
										pageTitle,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							getActivity(),
							pageTitle,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	private void handleButton() {
		if (CollegeMainActivity.showMonthWeek != null) {
			CollegeMainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String myStr = CollegeMainActivity.showMonthWeek.getText()
									.toString();
							if ((myStr.equals(getString(R.string.all)))) {
								CollegeMainActivity.showMonthWeek
										.setText(getString(R.string.weekly));
								loadData(CollegeConstants.VIEW_WEEKLY);
							} else if ((myStr
									.equals(getString(R.string.weekly)))) {
								CollegeMainActivity.showMonthWeek
										.setText(getString(R.string.monthly));
								loadData(CollegeConstants.VIEW_MONTHLY);
							} else if ((myStr
									.equals(getString(R.string.monthly)))) {
								CollegeMainActivity.showMonthWeek
										.setText(getString(R.string.all));
								loadData(CollegeConstants.VIEW_ALL);
							}

						}
					});
		}
		if (CollegeParentMainActivity.showMonthWeek != null) {
			CollegeParentMainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String myStr = CollegeParentMainActivity.showMonthWeek
									.getText().toString();
							if ((myStr.equals(getString(R.string.all)))) {
								CollegeParentMainActivity.showMonthWeek
										.setText(getString(R.string.weekly));
								loadData(CollegeConstants.VIEW_WEEKLY);
							} else if ((myStr
									.equals(getString(R.string.weekly)))) {
								CollegeParentMainActivity.showMonthWeek
										.setText(getString(R.string.monthly));
								loadData(CollegeConstants.VIEW_MONTHLY);
							} else if ((myStr
									.equals(getString(R.string.monthly)))) {
								CollegeParentMainActivity.showMonthWeek
										.setText(getString(R.string.all));
								loadData(CollegeConstants.VIEW_ALL);
							}
						}
					});
		}
	}
}
