package com.knwedu.college.fragments;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.college.PaymentReminderActivity;
import com.knwedu.college.adapter.PaymentHistoryDetailsAdapter;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.PaymentHistory;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageFeesFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;
	private ArrayList<PaymentHistory> paymentHistory = new ArrayList<PaymentHistory>();
	private int type;
	private ImageView btnViewPaymentDetails;
	private ListView listviewPaymentHistory;
	private PaymentHistoryDetailsAdapter adapter;
	private TextView emptyView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.manage_fee_view, container, false);
		initialize();

		return view;
	}

	public ManageFeesFragment(int type) {
		super();
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	private void initialize() {
		// TODO Auto-generated method stub
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
		listviewPaymentHistory = (ListView) view.findViewById(R.id.listviewPaymentHistory);
		btnViewPaymentDetails = (ImageView) view.findViewById(R.id.btnViewReminders);
		emptyView = (TextView) view.findViewById(R.id.emptyView);
		emptyView.setVisibility(View.GONE);

		btnViewPaymentDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PaymentReminderActivity.class);
				intent.putExtra(CollegeConstants.PAGE_TITLE, "Payment Reminders");
				intent.putExtra("Type", type);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		requetsForPaymentHistory();
	}

	private void requetsForPaymentHistory() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				CollegeAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				CollegeAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		nameValuePairs.add(
				new BasicNameValuePair("user_id", CollegeAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		if (type == 1) {
			nameValuePairs.add(new BasicNameValuePair(Constants.CHILD_ID,CollegeAppUtils.GetSharedParameter(getActivity(),
					Constants.CHILD_ID) ));
		}

		new RequestPaymentHistoryDetailsAsyntask().execute(nameValuePairs);
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

	private class RequestPaymentHistoryDetailsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
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
			Log.d("url extension: ", CollegeUrls.api_get_payment_history_details);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			paymentHistory.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, CollegeUrls.api_get_payment_history_details);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							PaymentHistory payment = new PaymentHistory(array.getJSONObject(i));
							paymentHistory.add(payment);
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

			} catch (

			JSONException e) {

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
				if (paymentHistory != null) {
					adapter = new PaymentHistoryDetailsAdapter(getActivity(), paymentHistory);
					listviewPaymentHistory.setAdapter(adapter);
					emptyView.setVisibility(View.GONE);
				}
			} else {
				if (error.length() > 0) {
					emptyView.setVisibility(View.VISIBLE);
					CollegeAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), error);
				} else {
					CollegeAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(),
							getResources().getString(R.string.error));
				}

			}
		}

	}

}
