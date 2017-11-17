package com.knwedu.ourschool;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import me.leolin.shortcutbadger.ShortcutBadger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.CollegeTeacherMainActivity;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.CustomExceptionHandler;
import com.knwedu.ourschool.utils.DataStructureFramwork.Attendance;
import com.knwedu.ourschool.utils.DataStructureFramwork.Permission;
import com.knwedu.ourschool.utils.DataStructureFramwork.PermissionAdd;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DataStructureFramwork.TimeTable;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class SplashActivity extends ActionBarActivity {
	private Handler mHandler;
	private ServiceWaitThread mThread;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	String curVersion;
	private DatabaseAdapter mDatabase;
	private ArrayList<TimeTable> timeTable;
	private int menu_val;


	// private EasyTracker easyTracker = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		//Remove Badge
		ShortcutBadger.removeCount(this);

		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		//startService(new Intent(this, CollegeSyncronizeService.class));
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

		//---------------deeplink-------------------

		//onNewIntent(getIntent());
//		Uri data = this.getIntent().getData();
//		if (data != null && data.isHierarchical()) {
//			String uri = this.getIntent().getDataString();
//			Log.i("Knwedu", "Deep link clicked " + uri);
//		}


		//====================deeplink========
//		Intent intent1 = new Intent();
//		// set data
//		intent1.setData(Uri.parse("knwedu://app/"));
//		// add flags
//		intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//						Intent.FLAG_ACTIVITY_CLEAR_TOP |
//						Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//     // The Intent.FLAG_INCLUDE_STOPPED_PACKAGES flag must be added only
//	//	if the API level of your Android SDK is over 11 like honeycomb
//		startActivity(intent1);
//
//
//
//



		// ==========Handle update from school to common app=============
		if (SchoolAppUtils.GetSharedParameter(SplashActivity.this,
				Constants.INSTITUTION_TYPE).equals("0")) {
			SchoolAppUtils.SetSharedParameter(SplashActivity.this,
					Constants.USERID, "0");
			SchoolAppUtils.SetSharedParameter(SplashActivity.this,
					Constants.UORGANIZATIONID, "0");

		}
		Bundle extras = getIntent().getExtras();
		if(null != extras){
			menu_val = extras.getInt("menu_val");
		}


		// ===========================================================

		curVersion = "0";
		try {
			curVersion = getApplicationContext().getPackageManager()
					.getPackageInfo(getApplicationInfo().packageName, 0).versionName;
			Log.d("Current Version", curVersion
					+ getApplicationInfo().packageName.toString());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("Org_id", SchoolAppUtils.GetSharedParameter(
				getApplicationContext(), Constants.UORGANIZATIONID));
		Log.d("User_type", SchoolAppUtils.GetSharedParameter(
				getApplicationContext(), Constants.USERTYPEID));
		Log.d("User_id", SchoolAppUtils.GetSharedParameter(
				getApplicationContext(), Constants.USERID));
		Log.d("UserApp_id is true",SchoolAppUtils.GetSharedParameter(getApplicationContext(),Constants.APP_TYPE));


		new Urls(SchoolAppUtils.GetSharedParameter(SplashActivity.this,
				Constants.COMMON_URL));
		CollegeUrls.base_url = SchoolAppUtils.GetSharedParameter(
				SplashActivity.this, Constants.COMMON_URL);

		Log.d("School Base Url", Urls.base_url);
		Log.d("College Url", CollegeUrls.base_url);

		// No internet
		if (!SchoolAppUtils.isOnline(SplashActivity.this)) {
			// Check already Logged in or not
			if (!SchoolAppUtils.GetSharedParameter(getApplicationContext(),
					Constants.USERID).equals("0")) {
				if (SchoolAppUtils.GetSharedParameter(SplashActivity.this,
						Constants.INSTITUTION_TYPE).equals(
						Constants.INSTITUTION_TYPE_SCHOOL)) {
					// Logged in
					if (SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.USERTYPEID).equalsIgnoreCase(
							Constants.USERTYPE_STUDENT)) {
						Intent intent = new Intent(SplashActivity.this,
								MainActivity.class);
						intent.putExtra("menu_val",menu_val);
						startActivity(intent);
						finish();
						return;
					} else if (SchoolAppUtils.GetSharedParameter(
							SplashActivity.this, Constants.USERTYPEID)
							.equalsIgnoreCase(Constants.USERTYPE_TEACHER)) {
						Intent intent = new Intent(SplashActivity.this,
								TeacherMainActivity.class);
						intent.putExtra("menu_val",menu_val);
						startActivity(intent);
						finish();
						return;
					} else if (SchoolAppUtils.GetSharedParameter(
							SplashActivity.this, Constants.USERTYPEID)
							.equalsIgnoreCase(Constants.USERTYPE_PARENT)) {
						Intent intent = new Intent(SplashActivity.this,
								ParentMainActivity.class);
						intent.putExtra("menu_val",menu_val);
						startActivity(intent);
						finish();
						return;
					}
				}
				// College
				else {
					redirectCollege();
				}

			}
			// No internet and first time login
			else {
				SchoolAppUtils.showDialog(SplashActivity.this, "Alert",
						"No internet Connectivity");
			}

		}

		// Internet Available
		else {
			// ----------------Current version check-----------------
			new VersionCheckAsync().execute();

		}

	}

//	//------------deeplink----------------
//
//	protected void onNewIntent(Intent intent) {
//		String action = intent.getAction();
//		String data = intent.getDataString();
//		if (Intent.ACTION_VIEW.equals(action) && data != null) {
//			String recipeId = data.substring(data.lastIndexOf("/") + 1);
//			Uri contentUri = RecipeContentProvider.CONTENT_URI.buildUpon()
//					.appendPath(recipeId).build();
//			showRecipe(contentUri);
//		}
//	}

	private class ServiceWaitThread extends Thread {
		@Override
		public void run() {
			// while (!DigiconnectService.isReady()) {
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(
						"waiting thread sleep() has been interrupted");
			}
			// }

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					onServiceReady();
				}
			});
			mThread = null;
		}
	}

	protected void onServiceReady() {
		startActivity(new Intent(SplashActivity.this, SigninActivity.class));
//		startActivity(new Intent(SplashActivity.this, SearchInstitutionActivity.class));
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
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
	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		Permission permission;
		PermissionAdd permissionadd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(SplashActivity.this);
			dialog.setTitle(getResources().getString(R.string.please_wait));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			//dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_splash_version);
			String menu_tag;
			String menu_title;
			String transport_type;
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						if (SchoolAppUtils.GetSharedParameter(SplashActivity.this,
								Constants.APP_TYPE).equals(
								Constants.APP_TYPE_COMMON_STANDARD)) {
							menu_tag = json.getString("menu_info");
							menu_title = json.getString("menu_caption");
						}else{
							menu_tag = json.getString("menu_info") + "|webaccess";
							menu_title = json.getString("menu_caption") + "|Web Access";
						}

						transport_type = json.getString("transport_mobile_based");

						SchoolAppUtils.SetSharedParameter(
								SplashActivity.this,
								Constants.NEWTRANSPORT_TYPE,
								transport_type);

						if (menu_tag != null) {
							Log.d("menu", menu_tag.toString());
							SchoolAppUtils.SetSharedParameter(
									SplashActivity.this, Constants.MENU_TAGS,
									menu_tag.toString());
							SchoolAppUtils.SetSharedParameter(
									SplashActivity.this, Constants.MENU_TITLES,
									menu_title.toString());
						}

						JSONObject permission_config = json.getJSONObject("activity_permission");
						permission = new Permission(permission_config);
						JSONObject permission_add = json
								.getJSONObject("permission");
						permissionadd = new PermissionAdd(permission_add);
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.ASSIGNMENT_MARK_PERMISSION,
								permission.assignment_permission.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.TEST_MARK_PERMISSION,
								permission.test_permission.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.ACTIVITY_MARK_PERMISSION,
								permission.activity_permission.toString());

						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.ASSIGNMENT_ADD_PERMISSION,
								permissionadd.assignment_add.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.TEST_ADD_PERMISSION,
								permissionadd.test_add.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.ACTIVITY_ADD_PERMISSION,
								permissionadd.activity_add.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.ANNOUNCEMENT_ADD_PERMISSION,
								permissionadd.announcement_add.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.COURSEWORK_ADD_PERMISSION,
								permissionadd.classwork_add.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.LESSONS_ADD_PERMISSION,
								permissionadd.lessons_add.toString());
						SchoolAppUtils.SetSharedParameter(SplashActivity.this,
								Constants.ATTENDANCE_TYPE_PERMISSION,
								permissionadd.attendance_type.toString());
						Log.d("assignment",
								permissionadd.attendance_type.toString());

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
			/*if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}*/
			if (result) {

				if (SchoolAppUtils.GetSharedBoolParameter(SplashActivity.this,
						Constants.ISSIGNIN)
						|| (SchoolAppUtils.GetSharedBoolParameter(
								SplashActivity.this, Constants.UISSIGNIN))) {
					if (SchoolAppUtils.GetSharedBoolParameter(
							SplashActivity.this, Constants.ISPARENTSIGNIN)) {
						Intent intent = new Intent(SplashActivity.this,
								ParentMainActivity.class);
						intent.putExtra("menu_val",menu_val);
						startActivity(intent);
						finish();
						return;
					}
					if (SchoolAppUtils.GetSharedBoolParameter(
							SplashActivity.this, Constants.UISSIGNIN)) {
						Intent intent = new Intent(SplashActivity.this,
								MainActivity.class);
						intent.putExtra("menu_val",menu_val);
						startActivity(intent);
						finish();
						return;
					}
					// startActivity(new
					// Intent(SplashActivity.this,
					// MainActivity.class));
					if (SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.USERTYPEID).equalsIgnoreCase("4")) {
						if (dialog != null) {
							dialog.dismiss();
							dialog = null;
						}
						Intent intent = new Intent(SplashActivity.this,
								MainActivity.class);
						intent.putExtra("menu_val",menu_val);
						startActivity(intent);
						finish();
						return;
					} else if (SchoolAppUtils.GetSharedParameter(
							SplashActivity.this, Constants.USERTYPEID)
							.equalsIgnoreCase("3")) {

						// Download data for offline access
						new GetOfflineDataAsyntask().execute();
						return;
					} else if (SchoolAppUtils.GetSharedParameter(
							SplashActivity.this, Constants.USERTYPEID)
							.equalsIgnoreCase("5")) {
						if (dialog != null) {
							dialog.dismiss();
							dialog = null;
						}
						Intent intent = new Intent(SplashActivity.this,
								ParentMainActivity.class);
						intent.putExtra("menu_val",menu_val);
						startActivity(intent);
						finish();
						return;
					}
				} else {
					mHandler = new Handler();
					mThread = new ServiceWaitThread();
					mThread.start();
				}

			} else {

				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils
								.showDialog(SplashActivity.this, getResources()
										.getString(R.string.unknown_response), error);
					} else {
						SchoolAppUtils
								.showDialog(
										SplashActivity.this,
										getResources()
												.getString(R.string.unknown_response),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							SplashActivity.this,
							getResources().getString(R.string.unknown_response),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}
	}

	// OFFLINE DATABASE INITIALIZATION
	private class GetOfflineDataAsyntask extends AsyncTask<Void, Void, Boolean> {
		String error;
		private ArrayList<Subject> subjects;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			subjects = new ArrayList<Subject>();
			subjects.clear();
			/*dialog = new ProgressDialog(SplashActivity.this);
			dialog.setTitle(getResources().getString(R.string.fetching_offline));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();*/
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.UORGANIZATIONID)));
			nameValuePairs
					.add(new BasicNameValuePair("user_id", SchoolAppUtils
							.GetSharedParameter(SplashActivity.this,
									Constants.USERID)));
			// Log parameters:
			Log.d("url extension: ", Urls.api_activity_get_subjects);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_activity_get_subjects);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						for (int i = 0; i < array.length(); i++) {
							Subject subject = new Subject(
									array.getJSONObject(i));

							subjects.add(subject);
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
			/*
			 * if (dialog != null) { dialog.dismiss(); dialog = null; }
			 */

			if (subjects != null) {

				mDatabase.open();
				// Delete all data in database
				mDatabase.deleteAllSubjects();
				for (int i = 0; i < subjects.size(); i++) {
					Log.d("Insert: ", "Inserting Subject .." + i);
					mDatabase.addSubjects(subjects.get(i));
				}
				mDatabase.close();

			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.UORGANIZATIONID)));
			nameValuePairs
					.add(new BasicNameValuePair("user_id", SchoolAppUtils
							.GetSharedParameter(SplashActivity.this,
									Constants.USERID)));
			new GetListAsyntask().execute(nameValuePairs);
			// Go to Teacher Main Page

		}

	}

	private class GetListAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * dialog = new ProgressDialog(getActivity());
			 * dialog.setTitle(getResources().getString(R.string.time_table));
			 * dialog
			 * .setMessage(getResources().getString(R.string.please_wait));
			 * dialog.setCanceledOnTouchOutside(false);
			 * dialog.setCancelable(false); dialog.show();
			 */
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			timeTable = new ArrayList<TimeTable>();
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_timetable_user);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						timeTable = new ArrayList<TimeTable>();
						for (int i = 0; i < array.length(); i++) {
							TimeTable attendance = new TimeTable(
									array.getJSONObject(i));
							timeTable.add(attendance);
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
			/*
			 * if (dialog != null) { // dialog.dismiss(); dialog = null; }
			 */
			if (timeTable != null) {
				if (timeTable.size() > 0) {
					mDatabase.open();
					// Delete all data in database
					mDatabase.deleteAllTimetable();
					for (int i = 0; i < timeTable.size(); i++) {
						Log.d("Insert: ", "Inserting Time Table.." + i);
						mDatabase.addTimetable(timeTable.get(i));
					}
					mDatabase.close();

				}
			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.UORGANIZATIONID)));
			nameValuePairs
					.add(new BasicNameValuePair("user_id", SchoolAppUtils
							.GetSharedParameter(SplashActivity.this,
									Constants.USERID)));
			new GetListAttendanceAsyntask().execute(nameValuePairs);

		}
	}

	private class GetListAttendanceAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * dialog = new ProgressDialog(getActivity());
			 * dialog.setTitle(getResources().getString(R.string.time_table));
			 * dialog
			 * .setMessage(getResources().getString(R.string.please_wait));
			 * dialog.setCanceledOnTouchOutside(false);
			 * dialog.setCancelable(false); dialog.show();
			 */
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			timeTable = new ArrayList<TimeTable>();
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_teacher_offline_attendance_user);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						timeTable = new ArrayList<TimeTable>();
						for (int i = 0; i < array.length(); i++) {
							TimeTable attendance = new TimeTable(
									array.getJSONObject(i));
							timeTable.add(attendance);
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
			/*
			 * if (dialog != null) { dialog.dismiss(); dialog = null; }
			 */
			if (timeTable != null) {
				if (timeTable.size() > 0) {
					mDatabase.open();
					// Delete all data in database
					mDatabase.deleteAllofflineattendance();
					for (int i = 0; i < timeTable.size(); i++) {
						Log.d("Insert: ", "Inserting Attendance..");
						mDatabase.addOfflineteacherattendance(timeTable.get(i));
					}
					mDatabase.close();

				}
			}

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(SplashActivity.this,
							Constants.UORGANIZATIONID)));
			nameValuePairs
					.add(new BasicNameValuePair("user_id", SchoolAppUtils
							.GetSharedParameter(SplashActivity.this,
									Constants.USERID)));
			new GetOfflineDataStudentAsyntask().execute(nameValuePairs);

		}
	}

	// OFFLINE DATABASE INITIALIZATION
	private class GetOfflineDataStudentAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		private ArrayList<Attendance> students;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			// Log parameters:
			students = new ArrayList<Attendance>();
			List<NameValuePair> nameValuePairs = params[0];
			Log.d("url extension: ", Urls.api_students_info);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_students_info);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						for (int i = 0; i < array.length(); i++) {
							Attendance subject = new Attendance(
									array.getJSONObject(i));

							students.add(subject);
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

			if (students != null) {

				mDatabase.open();
				// Delete all data in database
				mDatabase.deleteAllStudents();
				for (int i = 0; i < students.size(); i++) {
					Log.d("Insert: ", "Inserting Students.." + i);
					mDatabase.addStudents(students.get(i));
				}
				mDatabase.close();
				Intent intent = new Intent(SplashActivity.this,
						TeacherMainActivity.class);
				intent.putExtra("menu_val", menu_val);
				startActivity(intent);
				finish();

			}

		}

	}

	class DownloadFile extends AsyncTask<String, Integer, Boolean> {
		ProgressDialog dialog;
		String strFolderName;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(SplashActivity.this);
			dialog.setMessage(getResources().getString(
					R.string.organization_set));
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(String... aurl) {
			int count;
			int count2;
			try {
				URL url = new URL((String) aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				ContextWrapper cw = new ContextWrapper(getApplicationContext());
				// path to /data/data/yourapp/app_data/imageDir
				File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
				// Create imageDir
				File path = new File(directory, "applogo.png");

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(path);
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress((int) (total * 100 / lenghtOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();

				// Download footer
				URL url2 = new URL((String) aurl[1]);
				URLConnection conexion2 = url2.openConnection();
				conexion2.connect();

				int lenghtOfFile2 = conexion2.getContentLength();
				File path2 = new File(directory, "footer_logo.png");

				InputStream input2 = new BufferedInputStream(url2.openStream());
				OutputStream output2 = new FileOutputStream(path2);
				byte data2[] = new byte[1024];
				long total2 = 0;
				while ((count2 = input2.read(data2)) != -1) {
					total2 += count2;
					publishProgress((int) (total2 * 100 / lenghtOfFile2));
					output2.write(data2, 0, count2);
				}
				output2.flush();
				output2.close();
				input2.close();

			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			SplashActivity.this.finish();
			Intent intent = new Intent(SplashActivity.this,
					SigninActivity.class);
			startActivity(intent);
//			SplashActivity.this.finish();
//			Intent intent = new Intent(SplashActivity.this,
//					SearchInstitutionActivity.class);
//			startActivity(intent);

		}
	}

	class PasswordCheck extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		ProgressDialog dialog;// Change Mainactivity.this with your
		// activity name.
		String pass_check;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//dialog = new ProgressDialog(SplashActivity.this);
			//dialog.setMessage("Please Wait....");
			//dialog.show();

		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			/*if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}*/
			if(pass_check!=null) {
				if (pass_check.equalsIgnoreCase("match")) {

					// Check for Latest version
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							5);
					nameValuePairs
							.add(new BasicNameValuePair("user_type_id",
									SchoolAppUtils.GetSharedParameter(
											getApplicationContext(),
											Constants.USERTYPEID)));
					nameValuePairs.add(new BasicNameValuePair("organization_id",
							SchoolAppUtils.GetSharedParameter(
									getApplicationContext(),
									Constants.UORGANIZATIONID)));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							SchoolAppUtils.GetSharedParameter(
									getApplicationContext(), Constants.USERID)));
					nameValuePairs.add(new BasicNameValuePair("devicetype",
							"android"));
					nameValuePairs.add(new BasicNameValuePair("version_number",
							curVersion));
					nameValuePairs.add(new BasicNameValuePair("app_type",
							SchoolAppUtils.GetSharedParameter(getApplicationContext(),Constants.APP_TYPE)
							));


					new UploadAsyntask().execute(nameValuePairs);
				} else {

					if (dialog != null) {
						dialog.dismiss();
						dialog = null;
					}
					startActivity(new Intent(SplashActivity.this,
							SigninActivity.class));

//					startActivity(new Intent(SplashActivity.this,
//							SearchInstitutionActivity.class));

				}
			}
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_password);
			if (json != null) {

				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						pass_check = "match";
					} else {
						pass_check = "missmatch";
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return true;
				}
			}
			return false;
		}
	}

	// Version check
	public class Version implements Comparable<Version> {

		private String version;

		public final String get() {
			return this.version;
		}

		public Version(String version) {
			if (version == null)
				throw new IllegalArgumentException("Version can not be null");
			if (!version.matches("[0-9]+(\\.[0-9]+)*"))
				throw new IllegalArgumentException("Invalid version format");
			this.version = version;
		}

		@Override
		public int compareTo(Version that) {
			if (that == null)
				return 1;
			String[] thisParts = this.get().split("\\.");
			String[] thatParts = that.get().split("\\.");
			int length = Math.max(thisParts.length, thatParts.length);
			for (int i = 0; i < length; i++) {
				int thisPart = i < thisParts.length ? Integer
						.parseInt(thisParts[i]) : 0;
				int thatPart = i < thatParts.length ? Integer
						.parseInt(thatParts[i]) : 0;
				if (thisPart < thatPart)
					return -1;
				if (thisPart > thatPart)
					return 1;
			}
			return 0;
		}

		@Override
		public boolean equals(Object that) {
			if (this == that)
				return true;
			if (that == null)
				return false;
			if (this.getClass() != that.getClass())
				return false;
			return this.compareTo((Version) that) == 0;
		}

	}

	private void redirectCollege() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				0);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(SplashActivity.this, "id")));
		Log.d("PARAMS OF TEACHER", "" + nameValuePairs);
		new GetTimetableListAsyntaskCollege().execute(nameValuePairs);
	
	}

	private class VersionCheckAsync extends AsyncTask<Void, Void, Boolean> {
		String error;
		String playstore_version;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			playstore_version = "0";
			if(null == dialog) {
				dialog = new ProgressDialog(SplashActivity.this);
				dialog.setMessage(getResources().getString(R.string.please_wait));
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.show();
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("package",
					getApplicationInfo().packageName));
			nameValuePairs.add(new BasicNameValuePair("type", "android"));
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlfrist(nameValuePairs,
					Urls.url_playstore_version_common);
			try {

				if (json != null) {
					if (json.getString("status").equalsIgnoreCase("1")) {

						playstore_version = json.getString("data");
						Log.d("Playstore version", playstore_version);

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
			/*if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}*/
			if (result) {
				Version a = new Version(curVersion);
				Version b = new Version(playstore_version);
				Log.d("Version Check",
						"Current: " + a.get() + " Latest:" + b.get()
								+ " Compare:" + a.compareTo(b));
				if (a.compareTo(b) >= 0) {
					// Redirect user to menu, or login
					System.out.println("Rajhrita : "+SchoolAppUtils.GetSharedParameter(
							getApplicationContext(), Constants.USERID).equals(
							"0"));
					// Already Logged in
					if (!SchoolAppUtils.GetSharedParameter(
							getApplicationContext(), Constants.USERID).equals(
							"0")) {
						if (SchoolAppUtils
								.GetSharedParameter(SplashActivity.this,
										Constants.INSTITUTION_TYPE).equals(
										Constants.INSTITUTION_TYPE_SCHOOL)) {
							// Check for password change
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									5);
							nameValuePairs.add(new BasicNameValuePair(
									"user_type_id", SchoolAppUtils
											.GetSharedParameter(
													getApplicationContext(),
													Constants.USERTYPEID)));
							nameValuePairs
									.add(new BasicNameValuePair(
											"organization_id",
											SchoolAppUtils.GetSharedParameter(
													getApplicationContext(),
													Constants.UORGANIZATIONID)));
							nameValuePairs.add(new BasicNameValuePair(
									"user_id", SchoolAppUtils
											.GetSharedParameter(
													getApplicationContext(),
													Constants.USERID)));
							nameValuePairs.add(new BasicNameValuePair(
									"password", SchoolAppUtils
											.GetSharedParameter(
													getApplicationContext(),
													Constants.PASSWORD)));
							new PasswordCheck().execute(nameValuePairs);
						}
						// college
						else {

							redirectCollege();
						}
					}

					// Not Logged in
					else {
						// Go to Sign in page

						startActivity(new Intent(SplashActivity.this,
								SigninActivity.class));
//						startActivity(new Intent(SplashActivity.this,
//								SearchInstitutionActivity.class));
						finish();

					}

				}

				else {
					Intent i = new Intent(android.content.Intent.ACTION_VIEW);
					i.setData(Uri
							.parse("https://play.google.com/store/apps/details?id="
									+ getApplicationInfo().packageName));
					startActivity(Intent.createChooser(i,
							"Update KnwEdu using.."));
				}
			} else {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils
								.showDialog(SplashActivity.this, getResources()
										.getString(R.string.unknown_response), error);
					} else {
						SchoolAppUtils
								.showDialog(
										SplashActivity.this,
										getResources()
												.getString(R.string.unknown_response),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							SplashActivity.this,
							getResources().getString(R.string.unknown_response),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}
	}

	private class GetTimetableListAsyntaskCollege extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			/*dialog = new ProgressDialog(SplashActivity.this);
			dialog.setTitle(getResources().getString(R.string.fetching_offline));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
*/
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			timeTable = new ArrayList<TimeTable>();
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					CollegeUrls.api_timetable_user);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						timeTable = new ArrayList<TimeTable>();
						for (int i = 0; i < array.length(); i++) {
							TimeTable attendance = new TimeTable(
									array.getJSONObject(i));
							timeTable.add(attendance);
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

			if (timeTable != null) {
				if (timeTable.size() > 0) {
					mDatabase.open();
					// Delete all data in database
					mDatabase.deleteAllTimetable();
					for (int i = 0; i < timeTable.size(); i++) {
						Log.d("Insert: ", "Inserting ..");
						mDatabase.addTimetable(timeTable.get(i));
					}
					mDatabase.close();

				}
			}
			Log.d("College user type", CollegeAppUtils.GetSharedParameter(
					SplashActivity.this, CollegeConstants.USERTYPEID));
			if (CollegeAppUtils.GetSharedParameter(SplashActivity.this,
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_STUDENT)) {
				startActivity(new Intent(SplashActivity.this,
						CollegeMainActivity.class));
				finish();
				return;
			} else if (CollegeAppUtils.GetSharedParameter(SplashActivity.this,
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_TEACHER)) {
				startActivity(new Intent(SplashActivity.this,
						CollegeTeacherMainActivity.class));
				finish();
				return;
			} else if (CollegeAppUtils.GetSharedParameter(SplashActivity.this,
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_PARENT)) {
				startActivity(new Intent(SplashActivity.this,
						CollegeParentMainActivity.class));
				finish();
				return;
			}

		}
	}

}
