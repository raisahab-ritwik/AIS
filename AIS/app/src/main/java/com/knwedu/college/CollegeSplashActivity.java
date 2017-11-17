package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeCustomExceptionHandler;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Attendance;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subjectversion;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TimeTable;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

import me.leolin.shortcutbadger.ShortcutBadger;

public class CollegeSplashActivity extends ActionBarActivity {
	private Handler mHandler;
	private ServiceWaitThread mThread;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	String curVersion;
	// private DatabaseAdapter mDatabase;
	private ArrayList<TimeTable> timeTable;

	private ListView listView;
	private String page_title = "";

	// private EasyTracker easyTracker = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_splash);

		//Remove Badge
		ShortcutBadger.removeCount(this);

		page_title = CollegeSplashActivity.this.getTitle().toString();
		/* mDatabase = ((SchoolApplication) getApplication()).getDatabase(); */
		Thread.setDefaultUncaughtExceptionHandler(new CollegeCustomExceptionHandler());

		curVersion = "0";
		try {
			curVersion = getApplicationContext().getPackageManager()
					.getPackageInfo(getApplicationInfo().packageName, 0).versionName;
			Log.d("CURRENT VERSION....", curVersion
					+ getApplicationInfo().packageName.toString());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("Org_id", CollegeAppUtils.GetSharedParameter(
				getApplicationContext(), CollegeConstants.UORGANIZATIONID));
		Log.d("User_type", CollegeAppUtils.GetSharedParameter(
				getApplicationContext(), CollegeConstants.USERTYPEID));
		Log.d("User_id", CollegeAppUtils.GetSharedParameter(
				getApplicationContext(), CollegeConstants.USERID));

		// No internet
		if (!CollegeAppUtils.isOnline(CollegeSplashActivity.this)) {
			// Check already Logged in or not

			if (!CollegeAppUtils.GetSharedParameter(getApplicationContext(),
					CollegeConstants.USERTYPEID).equals("0")) {
				// Logged in
				if (CollegeAppUtils
						.GetSharedParameter(CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								CollegeConstants.USERTYPE_STUDENT)) {
					startActivity(new Intent(CollegeSplashActivity.this,
							CollegeMainActivity.class));
					finish();
					return;
				} else if (CollegeAppUtils
						.GetSharedParameter(CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								CollegeConstants.USERTYPE_TEACHER)) {
					startActivity(new Intent(CollegeSplashActivity.this,
							CollegeTeacherMainActivity.class));
					finish();
					return;
				} else if (CollegeAppUtils
						.GetSharedParameter(CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								CollegeConstants.USERTYPE_PARENT)) {
					startActivity(new Intent(CollegeSplashActivity.this,
							CollegeParentMainActivity.class));
					finish();
					return;
				}

			}
			// No internet and first time login
			else {
				CollegeAppUtils.showDialog(CollegeSplashActivity.this, "Alert",
						"No internet Connectivity");
			}

		}

		// Internet Available
		else {
			// Already Logged in
			if (!CollegeAppUtils.GetSharedParameter(getApplicationContext(),
					CollegeConstants.USERTYPEID).equals("0")) {

				
				if (CollegeAppUtils
						.GetSharedParameter(CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								CollegeConstants.USERTYPE_STUDENT)) {
					startActivity(new Intent(CollegeSplashActivity.this,
							CollegeMainActivity.class));
					finish();
					return;
				} else if (CollegeAppUtils
						.GetSharedParameter(CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								CollegeConstants.USERTYPE_TEACHER)) {
					startActivity(new Intent(CollegeSplashActivity.this,
							CollegeTeacherMainActivity.class));
					finish();
					return;
				} else if (CollegeAppUtils
						.GetSharedParameter(CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								CollegeConstants.USERTYPE_PARENT)) {
					startActivity(new Intent(CollegeSplashActivity.this,
							CollegeParentMainActivity.class));
					finish();
					return;
				}
			}
			// For first time app installation
			else {
				// Go to Sign in page

				startActivity(new Intent(CollegeSplashActivity.this,
						CollegeSigninActivity.class));
				finish();

				// new SetOrganizationAsyn().execute();
			}

		}

	}

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
		startActivity(new Intent(CollegeSplashActivity.this,
				CollegeSigninActivity.class));
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
		Subjectversion version;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeSplashActivity.this);
			dialog.setTitle(getResources().getString(R.string.please_wait));
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
					CollegeUrls.api_splash_version);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						String menu_tag = json.getString("menu_info");
						String menu_title = json.getString("menu_caption");
						JSONArray array = json.getJSONArray("version_data");
						JSONObject obj = array.getJSONObject(0);
						if (menu_tag != null) {
							Log.d("menu", menu_tag.toString());
							CollegeAppUtils.SetSharedParameter(
									CollegeSplashActivity.this,
									CollegeConstants.MENU_TAGS,
									menu_tag.toString());
							CollegeAppUtils.SetSharedParameter(
									CollegeSplashActivity.this,
									CollegeConstants.MENU_TITLES,
									menu_title.toString());
						}

						version = new Subjectversion(obj);
						System.out.println(version.version_name);
						// error = "Activity Published.";

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
				if (curVersion.equalsIgnoreCase(version.version_name)) {

					// SplashActivity.this.finish();

					if (CollegeAppUtils.GetSharedBoolParameter(
							CollegeSplashActivity.this,
							CollegeConstants.ISSIGNIN)
							|| (CollegeAppUtils.GetSharedBoolParameter(
									CollegeSplashActivity.this,
									CollegeConstants.UISSIGNIN))) {
						if (CollegeAppUtils.GetSharedBoolParameter(
								CollegeSplashActivity.this,
								CollegeConstants.ISPARENTSIGNIN)) {
							startActivity(new Intent(
									CollegeSplashActivity.this,
									CollegeParentMainActivity.class));
							finish();
							return;
						}
						if (CollegeAppUtils.GetSharedBoolParameter(
								CollegeSplashActivity.this,
								CollegeConstants.UISSIGNIN)) {
							startActivity(new Intent(
									CollegeSplashActivity.this,
									CollegeMainActivity.class));
							finish();
							return;
						}
						// startActivity(new
						// Intent(SplashActivity.this,
						// MainActivity.class));
						if (CollegeAppUtils.GetSharedParameter(
								CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								"4")) {
							startActivity(new Intent(
									CollegeSplashActivity.this,
									CollegeMainActivity.class));
							finish();
							return;
						} else if (CollegeAppUtils.GetSharedParameter(
								CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								"3")) {

							// Download data for offline access
							new GetOfflineDataAsyntask().execute();
							return;
						} else if (CollegeAppUtils.GetSharedParameter(
								CollegeSplashActivity.this,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								"5")) {
							startActivity(new Intent(
									CollegeSplashActivity.this,
									CollegeParentMainActivity.class));
							finish();
							return;
						}
					} else {
						mHandler = new Handler();
						mThread = new ServiceWaitThread();
						mThread.start();
					}

				}

				else {
					Intent i = new Intent(android.content.Intent.ACTION_VIEW);
					i.setData(Uri
							.parse("https://play.google.com/store/apps/details?id="
									+ getApplicationInfo().packageName));
					startActivity(i);
				}
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(CollegeSplashActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeSplashActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeSplashActivity.this,
							page_title,
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
			dialog = new ProgressDialog(CollegeSplashActivity.this);
			dialog.setTitle(getResources().getString(R.string.fetching_offline));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
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
					CollegeAppUtils.GetSharedParameter(
							CollegeSplashActivity.this,
							CollegeConstants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					CollegeAppUtils.GetSharedParameter(
							CollegeSplashActivity.this,
							CollegeConstants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id",
					CollegeAppUtils
							.GetSharedParameter(CollegeSplashActivity.this,
									CollegeConstants.USERID)));
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_activity_get_subjects);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_activity_get_subjects);
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
			if (dialog != null) {
				// dialog.dismiss();
				dialog = null;
			}

			if (subjects != null) {

				/*
				 * mDatabase.open(); // Delete all data in database
				 * mDatabase.deleteAllSubjects(); for (int i = 0; i <
				 * subjects.size(); i++) { Log.d("Insert: ", "Inserting .." +
				 * i); mDatabase.addSubjects(subjects.get(i)); }
				 * mDatabase.close();
				 */

			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					CollegeAppUtils.GetSharedParameter(
							CollegeSplashActivity.this,
							CollegeConstants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					CollegeAppUtils.GetSharedParameter(
							CollegeSplashActivity.this,
							CollegeConstants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id",
					CollegeAppUtils
							.GetSharedParameter(CollegeSplashActivity.this,
									CollegeConstants.USERID)));
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
			CollegeJsonParser jParser = new CollegeJsonParser();
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
				// dialog.dismiss();
				dialog = null;
			}
			if (timeTable != null) {
				if (timeTable.size() > 0) {
					/*
					 * mDatabase.open(); // Delete all data in database
					 * mDatabase.deleteAllTimetable(); for (int i = 0; i <
					 * timeTable.size(); i++) { Log.d("Insert: ",
					 * "Inserting ..");
					 * mDatabase.addTimetable(timeTable.get(i)); }
					 * mDatabase.close();
					 */
				}
			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					CollegeAppUtils.GetSharedParameter(
							CollegeSplashActivity.this,
							CollegeConstants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					CollegeAppUtils.GetSharedParameter(
							CollegeSplashActivity.this,
							CollegeConstants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id",
					CollegeAppUtils
							.GetSharedParameter(CollegeSplashActivity.this,
									CollegeConstants.USERID)));
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
			Log.d("url extension: ", CollegeUrls.api_students_info);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_students_info);
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

				/*
				 * mDatabase.open(); // Delete all data in database
				 * mDatabase.deleteAllStudents(); for (int i = 0; i <
				 * students.size(); i++) { Log.d("Insert: ", "Inserting .." +
				 * i); mDatabase.addStudents(students.get(i)); }
				 * mDatabase.close();
				 */
				startActivity(new Intent(CollegeSplashActivity.this,
						CollegeTeacherMainActivity.class));
				finish();

			}

		}

	}

}
