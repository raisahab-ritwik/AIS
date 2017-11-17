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

import com.knwedu.college.CollegeClassWorkActivity;
import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.adapter.CollegeAnnouncementAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Announcement;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeCourseworkListFragment extends Fragment {

	private ListView list;
	private CollegeAnnouncementAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Announcement> announcements;
	// private Spinner spinner;
	private int type;
	private String pageTitle = "";

	public CollegeCourseworkListFragment(int type) {
		super();
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_announcement_list, container,
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
		pageTitle = getActivity().getTitle().toString();
		list = (ListView) view.findViewById(R.id.listview);
		handleButton();
		loadData(1);
		/*
		 * if (SchoolAppUtils.GetSharedBoolParameter(getActivity(),
		 * Constants.MONTHLYWEEKLYANNOUNCEMENT)) { loadData(1); } else {
		 * loadData(2); }
		 */
	}

	private void loadData(int list_type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(getActivity(), "id")));
		nameValuePairs.add(new BasicNameValuePair("list_type", "3"));
		new GetAnnouncementAsyntask(type).execute(nameValuePairs);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					CollegeClassWorkActivity.class);
			intent.putExtra(CollegeConstants.ANNOUNCEMENT,
					announcements.get(arg2).object.toString());
			intent.putExtra(CollegeConstants.PAGE_TITLE, getActivity()
					.getTitle().toString());
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

	private class GetAnnouncementAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		int position;

		public GetAnnouncementAsyntask(int position) {
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.announcements));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			announcements = new ArrayList<Announcement>();
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_activity_get_classwork);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_activity_get_classwork);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						announcements = new ArrayList<Announcement>();
						for (int i = 0; i < array.length(); i++) {
							Announcement assignment = new Announcement(
									array.getJSONObject(i));
							announcements.add(assignment);
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
				if (announcements != null) {
					String temp = null;
					for (int i = 0; i < announcements.size(); i++) {
						if (i == 0) {
							announcements.get(0).check = true;
							temp = announcements.get(0).date;
						} else {
							if (!temp
									.equalsIgnoreCase(announcements.get(i).date)) {
								announcements.get(i).check = true;
								temp = announcements.get(i).date;
							}
						}
					}
					adapter = new CollegeAnnouncementAdapter(getActivity(),
							announcements);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}
			} else {
				if (announcements != null) {

					adapter = new CollegeAnnouncementAdapter(getActivity(),
							announcements);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
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
							if (CollegeAppUtils.GetSharedBoolParameter(
									getActivity(),
									CollegeConstants.MONTHLYWEEKLYANNOUNCEMENT)) {
								changeStateButton(false);
								loadData(2);
							} else {
								changeStateButton(true);
								loadData(1);
							}
						}
					});
		}
		if (CollegeParentMainActivity.showMonthWeek != null) {
			CollegeParentMainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (CollegeAppUtils.GetSharedBoolParameter(
									getActivity(),
									CollegeConstants.MONTHLYWEEKLYANNOUNCEMENT)) {
								changeStateButton(false);
								loadData(2);

							} else {
								changeStateButton(true);
								loadData(1);
							}
						}
					});
		}
	}

	private void changeStateButton(boolean state) {
		CollegeAppUtils.SetSharedBoolParameter(getActivity(),
				CollegeConstants.MONTHLYWEEKLYANNOUNCEMENT, state);
		if (CollegeMainActivity.showMonthWeek != null) {
			if (state) {
				CollegeMainActivity.showMonthWeek.setText(R.string.weekly);
			} else {
				CollegeMainActivity.showMonthWeek.setText(R.string.monthly);
			}
		}
		if (CollegeParentMainActivity.showMonthWeek != null) {
			if (state) {
				CollegeParentMainActivity.showMonthWeek
						.setText(R.string.weekly);
			} else {
				CollegeParentMainActivity.showMonthWeek
						.setText(R.string.monthly);
			}
		}
	}

}
