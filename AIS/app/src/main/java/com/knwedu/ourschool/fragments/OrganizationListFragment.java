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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.adapter.OrganizationAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Orginization;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class OrganizationListFragment extends Fragment {
	private ListView list;
	private OrganizationAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private TextView textnodata;
	private ArrayList<Orginization> organizations;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_news_list, container, false);
		initialize();
		return view;
	}

	private void initialize() {
		textnodata=(TextView) view.findViewById(R.id.textnodata) ;
		list = (ListView) view.findViewById(R.id.listview);

//		((LinearLayout) view.findViewById(R.id.header))
//				.setVisibility(View.GONE);
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


	if (organizations != null) {
			adapter = new OrganizationAdapter(getActivity(), organizations);
			list.setAdapter(adapter);
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.CHILD_ID)));
			new GetOrginizeAsyntask().execute(nameValuePairs);
		}
	}

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

	private class GetOrginizeAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

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
			List<NameValuePair> nameValuePair = params[0];
			organizations = new ArrayList<Orginization>();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
					Urls.api_org_activity);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							organizations = new ArrayList<Orginization>();
							for (int i = 0; i < array.length(); i++) {
								Orginization org = new Orginization(
										array.getJSONObject(i));
								organizations.add(org);
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
				textnodata.setVisibility(View.GONE);
				if (organizations != null) {
					adapter = new OrganizationAdapter(getActivity(),
							organizations);
					list.setAdapter(adapter);
				}
			} else {
				if (organizations != null) {
					adapter = new OrganizationAdapter(getActivity(),
							organizations);
					list.setAdapter(adapter);
				}
				if (error != null) {
					if (error.length() > 0) {

						//SchoolAppUtils.showDialog(getActivity(), getResources().getString(R.string.error), error);
						textnodata.setVisibility(View.VISIBLE);
						textnodata.setText("No organization created yet");

					} else {
						SchoolAppUtils
								.showDialog(
										getActivity(),
										getResources()
												.getString(R.string.error),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}
}
