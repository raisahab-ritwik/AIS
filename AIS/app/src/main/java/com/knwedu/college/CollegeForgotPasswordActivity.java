package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.SigninActivity;
import com.knwedu.ourschool.utils.DataStructureFramwork.UserInfo;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeForgotPasswordActivity extends Activity {
	private EditText username;
	private Button signIn, login;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgot_password);
		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void onStart() {
		super.onStart();
		Log.d("Google Analytics", "Tracking Start");
		EasyTracker.getInstance(this).activityStart(this);

	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("Google Analytics", "Tracking Stop");
		EasyTracker.getInstance(this).activityStop(this);
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
	}

	private void initialize() {
		username = (EditText) findViewById(R.id.username_edittxt);
		signIn = (Button) findViewById(R.id.signin_btn);
		login = (Button) findViewById(R.id.login_btn);

		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (username.getText().toString().length() <= 0) {
					Toast.makeText(CollegeForgotPasswordActivity.this,
							R.string.enter_your_email, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				Pattern p = Pattern
						.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
								+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				Matcher m = p.matcher(username.getText().toString().trim());
				boolean b = m.matches();
				if (!b) {
					Toast.makeText(CollegeForgotPasswordActivity.this,
							R.string.enter_correct_email, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						15);
				nameValuePairs.add(new BasicNameValuePair("email", username
						.getText().toString()));

				new SendPasswordAsyntask().execute(nameValuePairs);

			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(CollegeForgotPasswordActivity.this,
						SigninActivity.class));
			}
		});
	}

	
	/**
	 * This retrieves information for the of the user
	 * 
	 * @author Smonte
	 * 
	 */
	private class SendPasswordAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		UserInfo userInfo;
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeForgotPasswordActivity.this);
			dialog.setTitle("Send Password");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					CollegeUrls.api_org_forget_password);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						error = json.getString("data");

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
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						CollegeForgotPasswordActivity.this);
				dialog.setTitle("Sending Password");
				dialog.setMessage(error);
				dialog.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});
				dialog.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils
								.showDialog(CollegeForgotPasswordActivity.this,
										getTitle().toString(),
										error);
					} else {
						SchoolAppUtils
								.showDialog(
										CollegeForgotPasswordActivity.this,
										getTitle().toString(),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							CollegeForgotPasswordActivity.this,
							getTitle().toString(),
							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}
	}

}
