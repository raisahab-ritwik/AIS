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
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.AnnouncementActivity;
import com.knwedu.ourschool.ClassWorkActivity;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.ParentMainActivity;
import com.knwedu.ourschool.adapter.AnnouncementAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Announcement;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class AnnouncementListFragment extends Fragment {
	private ListView list;
	private AnnouncementAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Announcement> announcements;
	// private Spinner spinner;
	private int type;
	private TextView textnodata;

	public AnnouncementListFragment(int type) {
		super();
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_announcement_list, container,
				false);
		SchoolAppUtils.loadAppLogo(getActivity(), (ImageButton) view.findViewById(R.id.app_logo));
		
		initialize();
		return view;
	}

	private void initialize() {
		list = (ListView) view.findViewById(R.id.listview);
		textnodata=(TextView) view.findViewById(R.id.textnodata) ;
		//handleButton();
		if (SchoolAppUtils.GetSharedBoolParameter(getActivity(),
				Constants.MONTHLYWEEKLYANNOUNCEMENT)) {
			loadData(1);
		} else {
			loadData(2);
		}

		//-----------------for insurance-----------------------
		ImageView adImageView = (ImageView)view.findViewById(R.id.adImageView);

		if(Integer.parseInt(SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.USERTYPEID))== 5 && (SchoolAppUtils.GetSharedParameter(getActivity(),Constants.INSURANCE_AVIALABLE).equalsIgnoreCase("1")))
		{
			adImageView.setVisibility(View.VISIBLE);

		}
		else{
			adImageView.setVisibility(View.GONE);
		}
		adImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AD_URL));
//				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				browserIntent.setPackage("com.android.chrome");
//				try {
//					startActivity(browserIntent);
//				} catch (ActivityNotFoundException ex) {
//					// Chrome browser presumably not installed so allow user to choose instead
//					browserIntent.setPackage(null);
//					startActivity(browserIntent);
//				}
				startActivity(new Intent(getActivity(),
						AdvertisementWebViewActivity.class));
			}
		});
	}


	private void loadData(int list_type) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.USERID)));

		nameValuePairs.add(new BasicNameValuePair("list_type", String
				.valueOf(list_type)));

		nameValuePairs
				.add(new BasicNameValuePair("type", String.valueOf(type)));
		nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.CHILD_ID)));

		new GetAnnouncementAsyntask(type).execute(nameValuePairs);
	}
/*
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (type == 2) {
				Intent intent = new Intent(getActivity(),
						AnnouncementActivity.class);
				intent.putExtra(Constants.ANNOUNCEMENT,
						announcements.get(arg2).object.toString());
				intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle().toString());
				getActivity().startActivity(intent);
			} else {
				Intent intent = new Intent(getActivity(),
						ClassWorkActivity.class);
				intent.putExtra(Constants.ANNOUNCEMENT,
						announcements.get(arg2).object.toString());
				intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle().toString());
				getActivity().startActivity(intent);
			}

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

	private class GetAnnouncementAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		int position;

		public GetAnnouncementAsyntask(int position) {
			this.position = position;
		}

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
			announcements = new ArrayList<Announcement>();
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_announcement_view);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_announcement_view);
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
				}else {
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
				textnodata.setVisibility(View.GONE);
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
					adapter = new AnnouncementAdapter(getActivity(),
							announcements,type,getActivity().getTitle().toString());
					list.setAdapter(adapter);
					//list.setOnItemClickListener(listener);
				}
			} else {
				if (error.length() > 0) {
					textnodata.setVisibility(View.VISIBLE);
					textnodata.setText(error);
				}else{
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), getResources().getString(R.string.error));
				}

			}
		}

	}

	private void handleButton() {
		if (MainActivity.showMonthWeek != null) {
			MainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (SchoolAppUtils.GetSharedBoolParameter(
									getActivity(),
									Constants.MONTHLYWEEKLYANNOUNCEMENT)) {
								changeStateButton(false);
								loadData(2);
							} else {
								changeStateButton(true);
								loadData(1);
							}
						}
					});
		}
		if (ParentMainActivity.showMonthWeek != null) {
			ParentMainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (SchoolAppUtils.GetSharedBoolParameter(
									getActivity(),
									Constants.MONTHLYWEEKLYANNOUNCEMENT)) {
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
		SchoolAppUtils.SetSharedBoolParameter(getActivity(),
				Constants.MONTHLYWEEKLYANNOUNCEMENT, state);
		if (MainActivity.showMonthWeek != null) {
			if (state) {
				MainActivity.showMonthWeek.setText(R.string.weekly);
			} else {
				MainActivity.showMonthWeek.setText(R.string.monthly);
			}
		}
		if (ParentMainActivity.showMonthWeek != null) {
			if (state) {
				ParentMainActivity.showMonthWeek.setText(R.string.weekly);
			} else {
				ParentMainActivity.showMonthWeek.setText(R.string.monthly);
			}
		}
	}

}
