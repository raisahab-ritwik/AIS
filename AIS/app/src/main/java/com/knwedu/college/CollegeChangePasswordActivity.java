package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeChangePasswordActivity extends Activity {
	private ImageView userImage;
	private EditText oldPass, newPass, conPass;
	private Button changePass;
	private String page_title = "";
	private ProgressDialog dialog;
	private TextView nameProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_change_password);
		page_title = CollegeChangePasswordActivity.this.getTitle().toString();
		initialize();
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
			dialog.dismiss();
			dialog = null;
		}
	}

	private void initialize() {
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		nameProfile = (TextView) findViewById(R.id.name_txt_profile);
		oldPass = (EditText) findViewById(R.id.old_password_edt);
		oldPass.setVisibility(View.GONE);
		newPass = (EditText) findViewById(R.id.new_password_edt);
		conPass = (EditText) findViewById(R.id.confirm_password_edt);
		changePass = (Button) findViewById(R.id.change_password_btn);
		userImage = (ImageView) findViewById(R.id.image_vieww);
		if (getIntent().getExtras() != null) {
			String full_name = getIntent().getExtras().getString(
					CollegeConstants.FULL_NAME);
			String imageUrl = getIntent().getExtras().getString(
					CollegeConstants.IMAGE_URL);

			nameProfile.setText(full_name);
			new CollegeLoadImageAsyncTask(CollegeChangePasswordActivity.this,
					userImage, CollegeUrls.image_url_userpic, imageUrl, true)
					.execute();

		}

		changePass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (newPass.getText().toString().length() <= 0) {
					Toast.makeText(CollegeChangePasswordActivity.this,
							R.string.enter_new_password, Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (conPass.getText().toString().length() <= 0) {
					Toast.makeText(CollegeChangePasswordActivity.this,
							R.string.enter_new_confirm_password,
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!conPass.getText().toString()
						.equals(newPass.getText().toString())) {
					Toast.makeText(CollegeChangePasswordActivity.this,
							R.string.new_and_confirm_password_do_not_match,
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					String newPassword = null;
					newPassword = newPass.getText().toString();

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							3);
					nameValuePairs.add(new BasicNameValuePair("id",
							CollegeAppUtils.GetSharedParameter(
									CollegeChangePasswordActivity.this, "id")));
					nameValuePairs.add(new BasicNameValuePair("password",
							newPassword));
					new ChangePasswordAsyntask().execute(nameValuePairs);
				}
			}
		});
	}

	private class ChangePasswordAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeChangePasswordActivity.this);
			dialog.setTitle(getResources().getString(R.string.user_information));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_change_password);
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_change_password);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						try {
							error = json.getString("data");
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
				CollegeAppUtils
						.SetSharedParameter(CollegeChangePasswordActivity.this,
								CollegeConstants.PASSWORD, newPass.getText()
										.toString());
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						CollegeChangePasswordActivity.this);
				dialog.setTitle(R.string.change_password);
				dialog.setMessage(error);
				dialog.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								CollegeChangePasswordActivity.this.finish();
							}
						});
				;
				dialog.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeChangePasswordActivity.this, page_title,
								error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeChangePasswordActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeChangePasswordActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}
}
