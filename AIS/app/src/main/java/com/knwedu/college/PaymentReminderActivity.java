package com.knwedu.college;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.college.PaymentFeesDetailsActivity;
import com.knwedu.college.adapter.PaymentReminderAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.PaymentReminder;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.college.utils.CollegeAppUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentReminderActivity extends FragmentActivity {

	private ProgressDialog dialog;
	private TextView header,emptyView;
	private String page_title = "";
	private String Url;
	private int type;
	private ArrayList<PaymentReminder> paymentReminder = new ArrayList<PaymentReminder>();
	private ListView reminderList;
	private PaymentReminderAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reminder_list_activity);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		header = (TextView) findViewById(R.id.header_text);
		emptyView = (TextView) findViewById(R.id.emptyView);
		header.setText(page_title);
		reminderList = (ListView) findViewById(R.id.listviewReminders);
		((ImageButton) findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		type = getIntent().getExtras().getInt("Type");
		reminderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PaymentReminderActivity.this, PaymentFeesDetailsActivity.class);
				intent.putExtra(CollegeConstants.PAGE_TITLE,"Fee Details");
				intent.putExtra("Type", type);
				intent.putExtra("Reminder_id", paymentReminder.get(position).reminder_id);
				intent.putExtra("program_term_id", paymentReminder.get(position).program_term_id);
				startActivity(intent);
			}
			
		});
	}

	private void requetsForReminders() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(
				new BasicNameValuePair("user_type_id", CollegeAppUtils.GetSharedParameter(this, Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				CollegeAppUtils.GetSharedParameter(this, Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("user_id", CollegeAppUtils.GetSharedParameter(this, Constants.USERID)));
		if (type == 1) {
			nameValuePairs.add(
					new BasicNameValuePair("child_id", CollegeAppUtils.GetSharedParameter(this, Constants.CHILD_ID)));
		}

		new RequestPaymentRemindersAsyntask().execute(nameValuePairs);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		requetsForReminders();
	}
	
	private class RequestPaymentRemindersAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(PaymentReminderActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_get_payment_reminder_details);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			paymentReminder.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, CollegeUrls.api_get_payment_reminder_details);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							PaymentReminder reminder = new PaymentReminder(array.getJSONObject(i));
							paymentReminder.add(reminder);
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
					//error = getResources().getString(R.string.unknown_response);
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
				if (paymentReminder != null) {
					if(paymentReminder.size() > 0){
						adapter = new PaymentReminderAdapter(PaymentReminderActivity.this, paymentReminder);
						reminderList.setAdapter(adapter);
						emptyView.setVisibility(View.GONE);
						reminderList.setVisibility(View.VISIBLE);
					}else{
						emptyView.setVisibility(View.VISIBLE);
						reminderList.setVisibility(View.GONE);
						//finish();
					}
				}
			} else {
				if (error.length() > 0) {
					CollegeAppUtils.showDialog(PaymentReminderActivity.this, page_title, error);
					reminderList.setAdapter(adapter);
				} else {
					CollegeAppUtils.showDialog(PaymentReminderActivity.this, page_title,
							getResources().getString(R.string.error));
				}

			}
		}

	}
}
