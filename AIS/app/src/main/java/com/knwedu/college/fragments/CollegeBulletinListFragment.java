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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeNewsActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.CollegeTeacherMainActivity;
import com.knwedu.college.adapter.CollegeNewsAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Carr;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeBulletinListFragment extends Fragment {
	private ListView list;
	private CollegeNewsAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Carr> news;
	private Spinner spinner;
	private int position;
	private String type;
	// ImageView imgSelected;
	private String pageTitle = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_news_list, container,
				false);
		pageTitle = getActivity().getTitle().toString();
		initialize();
		return view;
	}

	private void initialize() {
		handleButton();
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
		list = (ListView) view.findViewById(R.id.listview);
		spinner = (Spinner) view.findViewById(R.id.spinner);
		// imgSelected = (ImageView) view.findViewById(R.id.imageSelected);

		if (news != null) {
			adapter = new CollegeNewsAdapter(getActivity(), news);
			list.setAdapter(adapter);
			list.setOnItemClickListener(clickListener);
		} else {

		}
		ArrayList<String> temp = new ArrayList<String>();
		type = CollegeAppUtils.GetSharedParameter(getActivity(),
				CollegeConstants.USERTYPEID);
		if (type.equals("3")) {

			temp.add("College");
			temp.add("Program");
			temp.add("Term");
			temp.add("Me");
		} else if (type.equals("4")) {

			temp.add("College");
			temp.add("Program");
			temp.add("Term");
			temp.add("Me");
		} else {
			temp.add("College");
			temp.add("Program");
			temp.add("Term");
			temp.add("Child");
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
			// imgSelected.setImageResource(R.drawable.bulletins_2);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new GetNewsAsyntask().execute(nameValuePairs);
		} else if (arg2 == 1) {
			// imgSelected.setImageResource(R.drawable.bulletins_2);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new getProgramAsyncTask().execute(nameValuePairs);
		} else if (arg2 == 2) {
			// imgSelected.setImageResource(R.drawable.bulletins_2);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new getProgramBulletin().execute(nameValuePairs);
		} else if (arg2 == 3) {
			// imgSelected.setImageResource(R.drawable.bulletins_2);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new getMeBulletinAsync().execute(nameValuePairs);
		} else {
			// imgSelected.setImageResource(R.drawable.bulletins_1);
		}

	}

	OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(), CollegeNewsActivity.class);
			intent.putExtra(CollegeConstants.NEWS,
					news.get(arg2).object.toString());
			intent.putExtra(CollegeConstants.PAGE_TITLE, getActivity()
					.getTitle().toString());
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

	private class getProgramBulletin extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.bulletin));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_broadcast_termBulltin);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			news = new ArrayList<Carr>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_broadcast_termBulltin);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							news = new ArrayList<Carr>();
							for (int i = 0; i < array.length(); i++) {
								Carr newD = new Carr(array.getJSONObject(i));
								news.add(newD);
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
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
				}
			} else {
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
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

	private class getProgramAsyncTask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.bulletin));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_broadcast_programBulltin);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			news = new ArrayList<Carr>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_broadcast_programBulltin);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							news = new ArrayList<Carr>();
							for (int i = 0; i < array.length(); i++) {
								Carr newD = new Carr(array.getJSONObject(i));
								news.add(newD);
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
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
				}
			} else {
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
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

	class getMeBulletinAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.bulletin));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_me_programBulltin);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			news = new ArrayList<Carr>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_me_programBulltin);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							news = new ArrayList<Carr>();
							for (int i = 0; i < array.length(); i++) {
								Carr newD = new Carr(array.getJSONObject(i));
								news.add(newD);
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
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
				}
			} else {
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
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

	private class GetNewsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.bulletin));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_broadcast_viewbulletin);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			news = new ArrayList<Carr>();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_broadcast_viewbulletin);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							news = new ArrayList<Carr>();
							for (int i = 0; i < array.length(); i++) {
								Carr newD = new Carr(array.getJSONObject(i));
								news.add(newD);
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
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
					list.setAdapter(adapter);
					list.setOnItemClickListener(clickListener);
				}
			} else {
				if (news != null) {
					adapter = new CollegeNewsAdapter(getActivity(), news);
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

	private void handleButton() {
		if (CollegeMainActivity.showMonthWeek != null) {
			CollegeMainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (CollegeAppUtils.GetSharedBoolParameter(
									getActivity(),
									CollegeConstants.MONTHLYWEEKLYBULLETIN)) {
								changeStateButton(false);
								loadData(position);
							} else {
								changeStateButton(true);
								loadData(position);
							}
						}
					});
		}

		if (CollegeTeacherMainActivity.showMonthWeek != null) {
			CollegeTeacherMainActivity.showMonthWeek
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (CollegeAppUtils.GetSharedBoolParameter(
									getActivity(),
									CollegeConstants.MONTHLYWEEKLYBULLETIN)) {
								changeStateButton(false);
								loadData(position);
							} else {
								changeStateButton(true);
								loadData(position);
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
									CollegeConstants.MONTHLYWEEKLYBULLETIN)) {
								changeStateButton(false);
								loadData(position);

							} else {
								changeStateButton(true);
								loadData(position);
							}
						}
					});
		}
	}

	private void changeStateButton(boolean state) {
		CollegeAppUtils.SetSharedBoolParameter(getActivity(),
				CollegeConstants.MONTHLYWEEKLYBULLETIN, state);
		if (CollegeMainActivity.showMonthWeek != null) {
			if (state) {
				CollegeMainActivity.showMonthWeek.setText(R.string.weekly);
			} else {
				CollegeMainActivity.showMonthWeek.setText(R.string.monthly);
			}
		}
		if (CollegeTeacherMainActivity.showMonthWeek != null) {
			if (state) {
				CollegeTeacherMainActivity.showMonthWeek
						.setText(R.string.weekly);
			} else {
				CollegeTeacherMainActivity.showMonthWeek
						.setText(R.string.monthly);
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
