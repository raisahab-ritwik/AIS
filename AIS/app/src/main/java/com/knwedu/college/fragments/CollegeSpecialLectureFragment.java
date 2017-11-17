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

import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.CollegeSpecialClassActivity;
import com.knwedu.college.CollegeTeacherMainActivity;
import com.knwedu.college.adapter.CollegeSpecialClassAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.SpecialClass;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeSpecialLectureFragment extends Fragment {
	private ListView list;
	private CollegeSpecialClassAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<SpecialClass> specialClass;
	private final int type;
	private String pageTitle = "";

	public CollegeSpecialLectureFragment(int type) {
		super();
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_assignment_list, container,
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
		if (CollegeParentMainActivity.showMonthWeek != null) {
			CollegeParentMainActivity.showMonthWeek.setText("All");
			loadData(0);
		} else if (CollegeMainActivity.showMonthWeek != null) {
			{
				loadData(0);
				CollegeMainActivity.showMonthWeek.setText("All");
			}

		}
		else if (CollegeTeacherMainActivity.showMonthWeek != null) {
			loadData(0);
			CollegeTeacherMainActivity.showMonthWeek.setText("All");

		}
	}

	private void loadData(int listType) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(getActivity(), "id")));
		nameValuePairs.add(new BasicNameValuePair("list_type", String
				.valueOf(listType)));
		/*
		 * nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
		 * .GetSharedParameter(getActivity(), Constants.USERID)));
		 * nameValuePairs.add(new BasicNameValuePair("organization_id",
		 * SchoolAppUtils.GetSharedParameter(getActivity(),
		 * Constants.UORGANIZATIONID))); nameValuePairs.add(new
		 * BasicNameValuePair("child_id", SchoolAppUtils
		 * .GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		 * nameVsaluePairs .add(new BasicNameValuePair("type",
		 * String.valueOf(type))); nameValuePairs .add(new
		 * BasicNameValuePair("list_type", String.valueOf(listType)));
		 */
		new GetAssignmentAsyntask().execute(nameValuePairs);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					CollegeSpecialClassActivity.class);
			intent.putExtra(CollegeConstants.ASSIGNMENT_TYPE, 1);
			intent.putExtra(CollegeConstants.ASSIGNMENT,
					specialClass.get(arg2).object.toString());
			intent.putExtra(CollegeConstants.PAGE_TITLE, getActivity()
					.getTitle().toString());
			getActivity().startActivity(intent);
			if (type == 1) {
				/*
				 * Intent intent = new Intent(getActivity(),
				 * AssignmentActivity.class);
				 * intent.putExtra(Constants.ASSIGNMENT_TYPE, 1);
				 * intent.putExtra(Constants.ASSIGNMENT,
				 * assignments.get(arg2).object.toString());
				 * getActivity().startActivity(intent);
				 */
			} else if (type == 2) {
				/*
				 * Intent intent = new Intent(getActivity(),
				 * TestActivity.class);
				 * intent.putExtra(Constants.ASSIGNMENT_TYPE, 2);
				 * intent.putExtra(Constants.TEST,
				 * assignments.get(arg2).object.toString());
				 * getActivity().startActivity(intent);
				 */
			} else {
				/*
				 * Intent intent = new Intent(getActivity(),
				 * ActivitiesActivity.class);
				 * intent.putExtra(Constants.ASSIGNMENT_TYPE, 3);
				 * intent.putExtra(Constants.ACTIVITIES,
				 * assignments.get(arg2).object.toString());
				 * getActivity().startActivity(intent);
				 */
			}

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
			dialog.setTitle(getResources().getString(R.string.special_class));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			specialClass = new ArrayList<SpecialClass>();
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_special_class);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_special_class);

			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("info");
							specialClass = new ArrayList<SpecialClass>();
							for (int i = 0; i < array.length(); i++) {
								SpecialClass assignment = new SpecialClass(
										array.getJSONObject(i));
								specialClass.add(assignment);
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
				if (specialClass != null) {
					String temp = null;
					for (int i = 0; i < specialClass.size(); i++) {
						if (i == 0) {
							specialClass.get(0).check = true;
							//temp = specialClass.get(0).submission_date;
						} else {
							/*if (!temp
									.equalsIgnoreCase(specialClass.get(i).submission_date)) {
								specialClass.get(i).check = true;
								temp = specialClass.get(i).submission_date;
							}*/
						}
					}
					adapter = new CollegeSpecialClassAdapter(getActivity(),
							specialClass);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}
			} else {
				if (specialClass != null) {

					adapter = new CollegeSpecialClassAdapter(getActivity(),
							specialClass);
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
									CollegeConstants.MONTHLYWEEKLYASSIGNMENT)) {
								changeStateButton(false);
								loadData(2);
							} else {
								changeStateButton(true);
								loadData(1);
							}
						}
					});
		}
		/*
		 * if (ParentMainActivity.showMonthWeek != null) {
		 * ParentMainActivity.showMonthWeek .setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if
		 * (SchoolAppUtils.GetSharedBoolParameter( getActivity(),
		 * Constants.MONTHLYWEEKLYASSIGNMENT)) { changeStateButton(false);
		 * loadData(2);
		 * 
		 * } else { changeStateButton(true); loadData(1); } } }); }
		 */
	}

	private void changeStateButton(boolean state) {
		CollegeAppUtils.SetSharedBoolParameter(getActivity(),
				CollegeConstants.MONTHLYWEEKLYASSIGNMENT, state);
		if (CollegeMainActivity.showMonthWeek != null) {
			if (state) {
				CollegeMainActivity.showMonthWeek.setText(R.string.weekly);
			} else {
				CollegeMainActivity.showMonthWeek.setText(R.string.monthly);
			}
		}
		/*
		 * if (ParentMainActivity.showMonthWeek != null) { if (state) {
		 * ParentMainActivity.showMonthWeek.setText(R.string.weekly); } else {
		 * ParentMainActivity.showMonthWeek.setText(R.string.monthly); } }
		 */
	}

}
