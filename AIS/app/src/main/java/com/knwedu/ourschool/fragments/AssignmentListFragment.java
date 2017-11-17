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
import com.knwedu.ourschool.ActivitiesActivity;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.AssignmentActivity;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.ParentMainActivity;
import com.knwedu.ourschool.TestActivity;
import com.knwedu.ourschool.adapter.AssignmentsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class AssignmentListFragment extends Fragment {
	private ListView list;
	private AssignmentsAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Assignment> assignments;
	// private Spinner spinner;
	private final int type;
	private TextView textnodata;

	public AssignmentListFragment(int type) {
		super();
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_assignment_list, container,
				false);
		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));

		initialize();


		return view;
	}

	private void initialize() {
		list = (ListView) view.findViewById(R.id.listview);
		textnodata=(TextView) view.findViewById(R.id.textnodata) ;
		handleButton();
		loadData(Constants.VIEW_ALL);
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


	private void loadData(int listType) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		nameValuePairs
				.add(new BasicNameValuePair("type", String.valueOf(type)));
		nameValuePairs.add(new BasicNameValuePair("list_type", String
				.valueOf(listType)));

		new GetAssignmentAsyntask(type).execute(nameValuePairs);
	}
/*
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (type == 1) {
				Intent intent = new Intent(getActivity(),
						AssignmentActivity.class);
				intent.putExtra(Constants.ASSIGNMENT_TYPE, 1);
				intent.putExtra(Constants.ASSIGNMENT,
						assignments.get(arg2).object.toString());
				intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
						.toString());
				getActivity().startActivity(intent);
			} else if (type == 2) {
				Intent intent = new Intent(getActivity(), TestActivity.class);
				intent.putExtra(Constants.ASSIGNMENT_TYPE, 2);
				intent.putExtra(Constants.TEST,
						assignments.get(arg2).object.toString());
				intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
						.toString());
				getActivity().startActivity(intent);
			} else {
				Intent intent = new Intent(getActivity(),
						ActivitiesActivity.class);
				intent.putExtra(Constants.ASSIGNMENT_TYPE, 3);
				intent.putExtra(Constants.ACTIVITIES,
						assignments.get(arg2).object.toString());
				intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
						.toString());
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

	private class GetAssignmentAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		int position;

		public GetAssignmentAsyntask(int position) {
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
		protected Boolean doInBackground(List<NameValuePair>... params){
			assignments = new ArrayList<Assignment>();
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", Urls.api_activity);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_activity);
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
						assignments = new ArrayList<Assignment>();
						for (int i = 0; i < array.length(); i++) {
							Assignment assignment = new Assignment(
									array.getJSONObject(i));
							assignments.add(assignment);
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
					adapter = new AssignmentsAdapter(getActivity(), assignments,type,getActivity().getTitle().toString());
					list.setAdapter(adapter);
				}
			} else {

				if (error.length() > 0) {
					textnodata.setVisibility(View.VISIBLE);
					textnodata.setText(error);
					list.setAdapter(null);
				}else{
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), getResources().getString(R.string.error));
					list.setAdapter(null);
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
							String myStr = MainActivity.showMonthWeek.getText()
									.toString();
							if ((myStr.equals(getString(R.string.all)))) {
								MainActivity.showMonthWeek
										.setText(getString(R.string.weekly));
								loadData(Constants.VIEW_WEEKLY);
							} else if ((myStr
									.equals(getString(R.string.weekly)))) {
								MainActivity.showMonthWeek
										.setText(getString(R.string.monthly));
								loadData(Constants.VIEW_MONTHLY);
							} else if ((myStr
									.equals(getString(R.string.monthly)))) {
								MainActivity.showMonthWeek
										.setText(getString(R.string.all));
								loadData(Constants.VIEW_ALL);
							}

						}
					});
		}
		if (ParentMainActivity.showMonthWeek != null) {
			ParentMainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String myStr = ParentMainActivity.showMonthWeek
									.getText().toString();
							if ((myStr.equals(getString(R.string.all)))) {
								ParentMainActivity.showMonthWeek
										.setText(getString(R.string.weekly));
								loadData(Constants.VIEW_WEEKLY);
							} else if ((myStr
									.equals(getString(R.string.weekly)))) {
								ParentMainActivity.showMonthWeek
										.setText(getString(R.string.monthly));
								loadData(Constants.VIEW_MONTHLY);
							} else if ((myStr
									.equals(getString(R.string.monthly)))) {
								ParentMainActivity.showMonthWeek
										.setText(getString(R.string.all));
								loadData(Constants.VIEW_ALL);
							}
						}
					});
		}
	}

	private void changeStateButton(boolean state) {
		SchoolAppUtils.SetSharedBoolParameter(getActivity(),
				Constants.MONTHLYWEEKLYASSIGNMENT, state);
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
