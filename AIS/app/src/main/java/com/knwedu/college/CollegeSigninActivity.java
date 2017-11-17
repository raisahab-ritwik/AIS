package com.knwedu.college;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.knwedu.college.adapter.CollegeListUserNameAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TimeTable;
import com.knwedu.college.utils.CollegeDataStructureFramwork.UserInfo;
import com.knwedu.college.utils.CollegeDataStructureFramwork.userType;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeSigninActivity extends Activity {
	private EditText username, password;
	private TextView forgotPassword;
	private Button signIn;
	private ProgressDialog dialog;
	//private DatabaseAdapter mDatabase;
	private ArrayList<TimeTable> timeTable;
	AlertDialog levelDialog;
	private ArrayList<String> userName;
	private String page_title = "";
	/*
	 * For Google Cloud Messaging
	 */
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	/**
	 * You can use your own project ID instead. This sender is a test CCS echo
	 * server.
	 */
	String GCM_SENDER_ID = "250972306499";

	// Tag for log messages.
	static final String TAG = "GCMDemo";
	String curVersion = "0";
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	String regid;
	private String id;
	private String roleId;
	UserInfo userInfo;
	userType userTYpe;
	String error;
	private ArrayList<UserInfo> userInfoList;
	private ArrayList<userType> userTypeList;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_signin);
		page_title = CollegeSigninActivity.this.getTitle().toString();
		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerPushNotification();
	}

	@SuppressWarnings("unchecked")
	private void registerPushNotification() {
		/**
		 * For Google Cloud Messaging
		 */
		// Make sure the app is registered with GCM and with the server
		prefs = getSharedPreferences(CollegeMainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);

		regid = prefs.getString(PROPERTY_REG_ID, null);
		// If there is no registration ID, the app isn't registered.
		// Call registerBackground() to register it.
		gcm = GoogleCloudMessaging.getInstance(this);

		if (regid == null) {
			new AsyncTask() {
				@Override
				protected Object doInBackground(Object[] arg0) {
					try {
						regid = gcm.register(GCM_SENDER_ID);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.e("devicetoken", "" + regid);
					return null;
				};

			}.execute(null, null, null);
		}

		Log.e("devicetoken", "" + regid);
	}

	private void initialize() {
		username = (EditText) findViewById(R.id.username_edittxt);
		password = (EditText) findViewById(R.id.password_edittxt);
		forgotPassword = (TextView) findViewById(R.id.forgot_password);
		signIn = (Button) findViewById(R.id.signin_btn);
		forgotPassword.setPaintFlags(forgotPassword.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);

		forgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * startActivity(new Intent(SigninActivity.this,
				 * ForgotPasswordActivity.class));
				 */
			}
		});
		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (username.getText().toString().length() <= 0) {
					Toast.makeText(CollegeSigninActivity.this,
							R.string.enter_your_username, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (password.getText().toString().length() <= 0) {
					Toast.makeText(CollegeSigninActivity.this,
							R.string.enter_your_password, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				new retriveToken().execute();

			}
		});

	}

	private class retriveToken extends AsyncTask<String, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeSigninActivity.this);
			dialog.setTitle(getResources().getString(R.string.signing_in));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			try {
				regid = gcm.register(GCM_SENDER_ID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("devicetoken", "" + regid);
			if (regid != null) {
				return true;
			}
			return false;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				try {
					curVersion = getApplicationContext().getPackageManager()
							.getPackageInfo("com.knwedu.college", 0).versionName;
					Log.d("CURRENT VERSION....", curVersion);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						4);
				nameValuePairs.add(new BasicNameValuePair("username", username
						.getText().toString().trim()));
				nameValuePairs.add(new BasicNameValuePair("password", password
						.getText().toString().trim()));
				nameValuePairs.add(new BasicNameValuePair("devicetype",
						"android"));
				nameValuePairs
						.add(new BasicNameValuePair("devicetoken", regid));
				nameValuePairs.add(new BasicNameValuePair("version_number",
						curVersion));
				Log.d("PARAMS....", "" + nameValuePairs);
				new SignInAsyntask().execute(nameValuePairs);
			} else {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				CollegeAppUtils.showDialog(
						CollegeSigninActivity.this,
						page_title,
						getResources().getString(
								R.string.please_check_internet_connection));
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
	}

	private class SignInAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_login);
			userInfoList = new ArrayList<UserInfo>();
			userTypeList = new ArrayList<userType>();

			try {
				if (json.getString("result").equalsIgnoreCase("1")) {
					JSONArray jsonData = json.getJSONArray("data");
					for (int i = 0; i < jsonData.length(); i++) {
						userInfo = new UserInfo(jsonData.getJSONObject(i));
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "id", userInfo.id);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "student_name", userInfo.student_name);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "student_class", userInfo.student_class);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "student_image", userInfo.student_image);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.USERTYPEID, userInfo.usertypeid);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.UORGANIZATIONID, "1");
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.UALTEMAIL, userInfo.alt_email);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.UMOBILENO, userInfo.alt_mobile_no);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.USERTYPEID, userInfo.usertypeid);
						userInfoList.add(userInfo);

					}

					JSONArray jsonUser = json.getJSONArray("user_types");
					for (int j = 0; j < jsonUser.length(); j++) {
						userTYpe = new userType(jsonUser.getJSONObject(j));
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "role_id",
								userTYpe.id);
						userTypeList.add(userTYpe);
					}
					if(userTypeList.size() == 1)
					{
					String menu_tag = json.getString("menu_info");
					String menu_title = json.getString("menu_caption");
					if (menu_tag != null) {
						Log.d("menu", menu_tag.toString());
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.MENU_TAGS, menu_tag.toString());
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.MENU_TITLES, menu_title.toString());
					}
					}
					JSONObject jsonOrg = json.getJSONObject("org_config");
					if (jsonOrg != null) {
						userInfo = new UserInfo(jsonOrg);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.ASSIGNMENT_MARKING,
								userInfo.assignment_marking);
						Log.d("ASSIGNMENT MARKING", userInfo.assignment_marking);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.TEST_MARKING,
								userInfo.test_marking);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.CLASSWORK_MARKING,
								userInfo.classwork_marking);
					}
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
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (error != null) {
				if (error.length() > 0) {
					CollegeAppUtils
							.showDialog(CollegeSigninActivity.this, getResources()
									.getString(R.string.sign_in), error);
				}
			} 
			if (result != null) {
				if (userTypeList.size() > 1) {
					showDialog();
				}
				else
				{
					if(userTypeList.size() == 1)
					{
						try {
							curVersion = getApplicationContext().getPackageManager()
									.getPackageInfo("com.knwedu.college", 0).versionName;
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
						Log.d("CURRENT VERSION....", curVersion);
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								4);
						nameValuePairs.add(new BasicNameValuePair("username", username
								.getText().toString().trim()));
						nameValuePairs.add(new BasicNameValuePair("password", password
								.getText().toString().trim()));
						nameValuePairs.add(new BasicNameValuePair("devicetype",
								"android"));
						nameValuePairs
								.add(new BasicNameValuePair("devicetoken", regid));
						nameValuePairs.add(new BasicNameValuePair("version_number",
								curVersion));
						Log.d("PARAMS....", "" + nameValuePairs);
						new SignInNoUser().execute(nameValuePairs);
					}
					if(CollegeAppUtils.GetSharedParameter(
							context, CollegeConstants.USERTYPEID)
							.equalsIgnoreCase(CollegeConstants.USERTYPE_STUDENT))
					{
					context.startActivity(new Intent(context, CollegeMainActivity.class));
					((Activity) context).finish();
					}
					else if(CollegeAppUtils.GetSharedParameter(
							context, CollegeConstants.USERTYPEID)
							.equalsIgnoreCase(CollegeConstants.USERTYPE_PARENT))
					{
						context.startActivity(new Intent(context, CollegeParentMainActivity.class));
						((Activity) context).finish();
					}
					else if(CollegeAppUtils.GetSharedParameter(
							context, CollegeConstants.USERTYPEID)
							.equalsIgnoreCase(CollegeConstants.USERTYPE_TEACHER))
					{
						context.startActivity(new Intent(context, CollegeTeacherMainActivity.class));
						((Activity) context).finish();
					}
				}
			}
		}
	}
	public class SignInNoUser extends AsyncTask<List<NameValuePair>, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) 
		{
			List<NameValuePair> nameValuePairs = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_login);
			userInfoList = new ArrayList<UserInfo>();
			userTypeList = new ArrayList<userType>();

			try {
				if (json.getString("result").equalsIgnoreCase("1")) {
					JSONArray jsonData = json.getJSONArray("data");
					for (int i = 0; i < jsonData.length(); i++) {
						userInfo = new UserInfo(jsonData.getJSONObject(i));
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "id", userInfo.id);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "student_name", userInfo.student_name);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "student_class", userInfo.student_class);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "student_image", userInfo.student_image);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.USERTYPEID, userInfo.usertypeid);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.UORGANIZATIONID, "1");
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.UALTEMAIL, userInfo.alt_email);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.UMOBILENO, userInfo.alt_mobile_no);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.USERTYPEID, userInfo.usertypeid);
						userInfoList.add(userInfo);

					}

					JSONArray jsonUser = json.getJSONArray("user_types");
					for (int j = 0; j < jsonUser.length(); j++) {
						userTYpe = new userType(jsonUser.getJSONObject(j));
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this, "role_id",
								userTYpe.id);
						Log.d("TEACHER ROLE ID", userTYpe.id);
						userTypeList.add(userTYpe);
					}

					String menu_tag = json.getString("menu_info");
					String menu_title = json.getString("menu_caption");
					if (menu_tag != null) {
						Log.d("menu", menu_tag.toString());
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.MENU_TAGS, menu_tag.toString());
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.MENU_TITLES, menu_title.toString());
					}
					JSONObject jsonOrg = json.getJSONObject("org_config");
					if (jsonOrg != null) {
						userInfo = new UserInfo(jsonOrg);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.ASSIGNMENT_MARKING,
								userInfo.assignment_marking);
						Log.d("ASSIGNMENT MARKING", userInfo.assignment_marking);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.TEST_MARKING,
								userInfo.test_marking);
						CollegeAppUtils.SetSharedParameter(
								CollegeSigninActivity.this,
								CollegeConstants.CLASSWORK_MARKING,
								userInfo.classwork_marking);
					}
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
			return false;
		}
		
	}

	private void showDialog() {
		// Strings to Show In Dialog with Radio Buttons
		final Dialog dialog2 = new Dialog(context);
		dialog2.setCanceledOnTouchOutside(false);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.custom_list_dialog);
		ListView listName = (ListView) dialog2.findViewById(R.id.listView1);
		listName.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				roleId = userTYpe.id;
			}
		});
		CollegeListUserNameAdapter mAdapter = new CollegeListUserNameAdapter(CollegeSigninActivity.this,userTypeList);
		listName.setAdapter(mAdapter);
		dialog2.show();
	}
}
