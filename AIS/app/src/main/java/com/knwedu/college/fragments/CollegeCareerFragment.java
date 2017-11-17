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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.knwedu.college.CollegeCareerActivity;
import com.knwedu.college.adapter.CollegeCareerAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Carreer;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeCareerFragment extends Fragment {
	private ListView list;
	private CollegeCareerAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Carreer> career;
	private Spinner spinner;
	private int position;
	private String type;
	private String pageTitle = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_news_list, container,
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
		// handleButton();
		list = (ListView) view.findViewById(R.id.listview);
		spinner = (Spinner) view.findViewById(R.id.spinner);

		if (career != null) {
			adapter = new CollegeCareerAdapter(getActivity(), career, 1);
			list.setAdapter(adapter);
			list.setOnItemClickListener(clickListener);
		} else {

		}
		ArrayList<String> temp = new ArrayList<String>();
		type = CollegeAppUtils.GetSharedParameter(getActivity(),
				CollegeConstants.USERTYPEID);
		if (type.equals("3")) {

			temp.add("Jobs");
			temp.add("Interviews");
			temp.add("Campus Selection");

		} else {
			temp.add("Jobs");
			temp.add("Interviews");
			temp.add("Campus Selection");
		}
		ArrayAdapter<String> data = new ArrayAdapter<String>(getActivity(),
				R.layout.simple_spinner_item_custom_new, temp);
		data.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);

		spinner.setAdapter(data);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				loadData(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void loadData(int arg2) {

		this.position = arg2;
		String url = null;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
		if (arg2 == 0) {
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new GetJobsAsyntask().execute(nameValuePairs);
		} else if (arg2 == 1) {
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new getInterviewAsync().execute(nameValuePairs);
		} else if (arg2 == 2) {
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new getSuccefullCandidateAsync().execute(nameValuePairs);
		}
	}

	OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),
					CollegeCareerActivity.class);
			intent.putExtra(CollegeConstants.NEWS,
					career.get(arg2).object.toString());

			startActivity(intent);
		}
	};

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public void onStop() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
		super.onStop();
	}

	private class getInterviewAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.career));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_college_interview);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			career = new ArrayList<Carreer>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_college_interview);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							career = new ArrayList<Carreer>();
							for (int i = 0; i < array.length(); i++) {
								Carreer newD = new Carreer(
										array.getJSONObject(i));
								career.add(newD);
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
				if (career != null) {
					adapter = new CollegeCareerAdapter(getActivity(), career, 2);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
				}
			} else {
				if (career != null) {
					adapter = new CollegeCareerAdapter(getActivity(), career, 2);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
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

	private class getSuccefullCandidateAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.campus));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_college_selection);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			career = new ArrayList<Carreer>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_college_selection);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							career = new ArrayList<Carreer>();
							for (int i = 0; i < array.length(); i++) {
								Carreer newD = new Carreer(
										array.getJSONObject(i));
								career.add(newD);
							}
						} catch (Exception e) {

						}
						return true;
					}
					else {
						try {
							error = json.getString("data");
						}
						catch (Exception e) {
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
				if (career != null) {
					adapter = new CollegeCareerAdapter(getActivity(), career, 3);
					list.setAdapter(adapter);
					list.setOnItemClickListener(null);
				}
			} else {
				if (career != null) {
					adapter = new CollegeCareerAdapter(getActivity(), career, 3);
					list.setAdapter(adapter);
					list.setOnItemClickListener(null);
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

	private class GetJobsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.jobs));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_college_jobs);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			career = new ArrayList<Carreer>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_college_jobs);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							career = new ArrayList<Carreer>();
							for (int i = 0; i < array.length(); i++) {
								Carreer newD = new Carreer(
										array.getJSONObject(i));
								career.add(newD);
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
				if (career != null) {
					adapter = new CollegeCareerAdapter(getActivity(), career, 1);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
				}
			} else {
				if (career != null) {
					adapter = new CollegeCareerAdapter(getActivity(), career, 1);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
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
}
