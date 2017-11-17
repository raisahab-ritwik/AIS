package com.knwedu.ourschool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.fragments.AisExamScheduleFragments;
import com.knwedu.ourschool.fragments.AisHelpFragment;
import com.knwedu.ourschool.fragments.AisSyllabus;
import com.knwedu.ourschool.fragments.AisTeacherSUbjectList;
import com.knwedu.ourschool.fragments.BehaviourListFragment;
import com.knwedu.ourschool.fragments.BlogListFragment;
import com.knwedu.ourschool.fragments.BulletinListFragment;
import com.knwedu.ourschool.fragments.CareerFragment;
import com.knwedu.ourschool.fragments.DailyDiaryClassListFragment;
import com.knwedu.ourschool.fragments.ExamsListFragment;
import com.knwedu.ourschool.fragments.SchoolInfoFragment;
import com.knwedu.ourschool.fragments.SpecialLectureFragment;
import com.knwedu.ourschool.fragments.SubjectListFragment;
import com.knwedu.ourschool.fragments.SynchronizeFragment;
import com.knwedu.ourschool.fragments.TeacherActivityListFragment;
import com.knwedu.ourschool.fragments.TeacherAnnouncementListFragment;
import com.knwedu.ourschool.fragments.TeacherAssignmentListFragment;
import com.knwedu.ourschool.fragments.TeacherClassWorkListFragment;
import com.knwedu.ourschool.fragments.TeacherProfileFragment;
import com.knwedu.ourschool.fragments.TeacherScheduleListFragment;
import com.knwedu.ourschool.fragments.TeacherSubjectAttendanceListFragment;
import com.knwedu.ourschool.fragments.TeacherTestListFragment;
import com.knwedu.ourschool.fragments.TeacherTimeTableFragment;
import com.knwedu.ourschool.fragments.TeacherWeeklyPlanListFragment;
import com.knwedu.ourschool.fragments.SchoolSyllabusFragment;
import com.knwedu.ourschool.fragments.CampusFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.com.knwedu.calendar.EventFragment;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherMainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public static final String PROPERTY_REG_ID = "registration_id";
	private String[] mMenuTags;
	private String[] mMenuTitles;
	private ImageButton button;
	private TextView title;
	public static Button showMonthWeek;
	SharedPreferences prefs;
	String regid;
	GoogleCloudMessaging gcm;
	String GCM_SENDER_ID = "250972306499";
	private ProgressBar progress_bar;
	AlertDialog.Builder dialoggg;
	private MenuAdapter adapter;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	RelativeLayout rellayout;
	public static final String EXTRA_MESSAGE = "message";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private int mSelectedPos;
	private boolean doubleBackToExitPressedOnce = false;
	private String appType;

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "Al_Wahda_GCM";
	Context context = this;
	//private InterstitialAd interstitial;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_teacher_main);

		Bundle extras = getIntent().getExtras();
		int menu_val = 0;

		button = (ImageButton) findViewById(R.id.button);
		title = (TextView) findViewById(R.id.title_txt);

		// mMenuTitles =
		// getResources().getStringArray(R.array.menu_list_teacher);
		mMenuTags = SchoolAppUtils.GetSharedParameter(TeacherMainActivity.this,
				Constants.MENU_TAGS).split("\\|");
		mMenuTitles = SchoolAppUtils.GetSharedParameter(
				TeacherMainActivity.this, Constants.MENU_TITLES).split("\\|");
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		showMonthWeek = (Button) findViewById(R.id.show_monthly_weekly);
		showMonthWeek.setVisibility(View.INVISIBLE);

		progress_bar = (ProgressBar) findViewById(R.id.progressBar_autoSync);
		progress_bar.setVisibility(View.INVISIBLE);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		adapter = new MenuAdapter(mMenuTitles, mMenuTags);

		appType = SchoolAppUtils.GetSharedParameter(TeacherMainActivity.this,
				Constants.APP_TYPE);


		if(getIntent().getExtras().getString("prompt")!=null) {

			if (getIntent().getExtras().getString("prompt").equals("1")) {
				popUpView();
			}
		}

		// set up the drawer's list view with items and click listener
		/*
		 * mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		 * R.layout.drawer_list_item, mMenuTitles));
		 */
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.menu_icon, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			@Override
			public void onDrawerClosed(View view) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				showMonthWeek.setEnabled(true);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						drawerView.getApplicationWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				showMonthWeek.setEnabled(false);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// uploadOfflineData();
			Bundle extras1 = getIntent().getExtras();
			if(null != extras1){
				selectItem(extras1.getInt("menu_val"));
			} else{
				selectItem(0);
			}
		}

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					mDrawerLayout.openDrawer(mDrawerList);
				}
			}
		});

		mDrawerLayout.openDrawer(mDrawerList);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit",
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);

	}

	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		/*if (interstitial.isLoaded()) {
			interstitial.show();
		}*/
	}

	private void sendRegistrationIdToBackend() {
		// Your implementation here.
		// ServerUtilities.register(getApplicationContext(),
		// SchoolAppUtils.GetSharedParameter(TeacherMainActivity.this,
		// Constants.USERNAME),
		// SchoolAppUtils.GetSharedParameter(TeacherMainActivity.this,
		// Constants.PASSWORD), regid);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(TeacherMainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	@SuppressWarnings("unchecked")
	private void registerInBackground() {
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(GCM_SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.
					// sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(Object result) {
				String msg = (String) result;
				System.out.println("Message: " + msg);
			};

		}.execute(null, null, null);

	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}

	}

	private void selectItem(int position) {

		// update the main content by replacing fragments
		showMonthWeek.setVisibility(View.INVISIBLE);
		// showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(null, null,
		// null, null);
		FragmentManager fragmentManager = getSupportFragmentManager();
		String label = mMenuTags[position];

		if (label.equalsIgnoreCase(Constants.MENU_TEACHER_MY_PROFILE)) {
			// ----------------Profile------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new TeacherProfileFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_ACADEMICS)) {
			// ----------------Academics------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new SubjectListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_ATTENDANCE)) {
			// ------------------Attendance----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new TeacherSubjectAttendanceListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_ASSIGNMENT)) {
			// ----------------Assignment------------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new TeacherAssignmentListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_QUIZ)) {
			// ------------------Quiz----------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new TeacherTestListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_ACTIVITY)) {
			// ----------------Activity------------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new TeacherActivityListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_ANNOUNCEMENT)) {
			// ------------------Announcement----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new TeacherAnnouncementListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_BEHAVIOUR)) {
			// -------------------Behaviour---------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new BehaviourListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_COURSE_WORK)) {
			// -----------------Course work-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new TeacherClassWorkListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_LESSON_PLAN)) {
			// -----------------Lesson Plan-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new TeacherScheduleListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_WEEKLY_PLAN)) {
			// ----------------Weekly Plan------------------------
						fragmentManager.beginTransaction()
								.replace(R.id.content_frame, new TeacherWeeklyPlanListFragment())
								.commit();
						showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_BULLETIN)) {
			// ------------------Bulletin----------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new BulletinListFragment())
					.commit();

			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_EXAMS)) {
			// ---------------Exams-------------------------
			/*fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new ExamsListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);*/
			if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, new AisExamScheduleFragments())
						.commit();
			}else {
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, new ExamsListFragment())
						.commit();
			}
			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_TIME_TABLE)) {
			// ----------------Time Table------------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame, new TeacherTimeTableFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_DIRECTORY)) {
			// ----------------Directory------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new SchoolInfoFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_BLOG)) {
			// ----------------Blog------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new BlogListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}
	
		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_SPECIAL_CLASS)) {
			// ------------------Special Class----------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new SpecialLectureFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}
		else if (label.equalsIgnoreCase(Constants.SYLLABUS)) {
			// --------------------Syllabus--------------------
			/*fragmentManager.beginTransaction().replace(R.id.content_frame, new SchoolSyllabusFragment(2)).commit();*/
			if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
				fragmentManager.beginTransaction().replace(R.id.content_frame, new AisSyllabus()).commit();
			}else{

				fragmentManager.beginTransaction().replace(R.id.content_frame, new SchoolSyllabusFragment(1)).commit();
			}
			showMonthWeek.setVisibility(View.GONE);

		}

		else if (label.equalsIgnoreCase(Constants.EVENT)) {
			// -----------------Event-----------------------

			fragmentManager.beginTransaction().replace(R.id.content_frame, new EventFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		}
		else if(label.equals(Constants.MENU_TEACHER_HELP)){

			fragmentManager.beginTransaction().replace(R.id.content_frame, new AisHelpFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
			//btnAdd.setVisibility(View.GONE);
		}
		else if (label.equalsIgnoreCase(Constants.CAMPUS)) {
			// --------------------Campus--------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new CampusFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		}
		else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_SYNCHRONIZE)) {
			// -----------------Sync-----------------------

			fragmentManager.beginTransaction().replace(R.id.content_frame, new SynchronizeFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_WEB_ACCESS)) {
			// ----------------Web Access------------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new WebAccessFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_CAREER)) {
			// ----------------Career------------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new CareerFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_DAILY_DIARY)) {
			// -------------------Daily Diary---------------------

			if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, new AisTeacherSUbjectList())
						.commit();
			}else {
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, new DailyDiaryClassListFragment())
						.commit();
			}
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_SIGN_OUT)) {
			// ---------------Sign Out-------------------------
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TeacherMainActivity.this);
			builder.setTitle("Sign Out");
			builder.setMessage("Are you sure?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							SchoolAppUtils.deleteTutorialFiles(context);
							SchoolAppUtils.SetSharedIntParameter(context, Constants.TUTORIAL_FILE_COUNT, 0);
							if (SchoolAppUtils
									.isOnline(TeacherMainActivity.this)) {
								SchoolAppUtils.SetSharedBoolParameter(
										TeacherMainActivity.this,
										Constants.UISSIGNIN, false);
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										3);
								nameValuePairs.add(new BasicNameValuePair(
										"user_type_id",
										SchoolAppUtils.GetSharedParameter(
												TeacherMainActivity.this,
												Constants.USERTYPEID)));
								nameValuePairs.add(new BasicNameValuePair(
										"organization_id",
										SchoolAppUtils.GetSharedParameter(
												TeacherMainActivity.this,
												Constants.UORGANIZATIONID)));
								nameValuePairs.add(new BasicNameValuePair(
										"user_id",
										SchoolAppUtils.GetSharedParameter(
												TeacherMainActivity.this,
												Constants.USERID)));
								new Signout().execute(nameValuePairs);
							} else {
								SchoolAppUtils.SetSharedParameter(
										TeacherMainActivity.this,
										Constants.USERID, "0");
								finish();
								startActivity(new Intent(
										TeacherMainActivity.this,
										SigninActivity.class));
							}

						}
					});
			builder.setNegativeButton("No", null);
			builder.create().show();

		}

		if (!mMenuTags[position]
				.equalsIgnoreCase(Constants.MENU_TEACHER_SYNCHRONIZE)) {
			adapter.update(position);

			// update selected item and title, then close the drawer

			mDrawerList.setItemChecked(position, true);
			if (mMenuTitles[position].equalsIgnoreCase("Sign Out")) {

			} else {
				try {
					setTitle(mMenuTitles[position]);
					title.setText(mMenuTitles[position]);
				} catch (Exception e) {
				}
			}
		}
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("Google Analytics", "Tracking Start");
		EasyTracker.getInstance(this).activityStart(this);
		registerReceiver(receiver, new IntentFilter("alerts"));

	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("Google Analytics", "Tracking Stop");
		EasyTracker.getInstance(this).activityStop(this);
		unregisterReceiver(receiver);
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras() != null) {
				if (intent.getExtras().getBoolean("announcementincomming",
						false)) {
					SchoolAppUtils.SetSharedBoolParameter(
							TeacherMainActivity.this, "showannouncements",
							false);
					button.setImageResource(R.drawable.menu_icon);
					adapter.showAnnouncements();
					adapter.notifyDataSetChanged();
				} else if (intent.getExtras().getBoolean("newincomming", false)) {
					SchoolAppUtils.SetSharedBoolParameter(
							TeacherMainActivity.this, "shownews", false);
					button.setImageResource(R.drawable.menu_icon);
					adapter.showNews();
					adapter.notifyDataSetChanged();
				}
			}
		}
	};

	@SuppressWarnings("unchecked")
	private void registerBackground() {
		new AsyncTask() {
			@Override
			protected String doInBackground(Object... params) {
				String msg = "";
				try {
					regid = gcm.register(GCM_SENDER_ID);
					msg = "Device registered, registration id=" + regid;
					// Log.v("device registered ",msg);
					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that will echo
					// back
					// the message using the 'from' address in the message.

					// Save the regid for future use - no need to register
					// again.
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(PROPERTY_REG_ID, regid);
					editor.commit();

					String url = "http://206.225.81.253/web_services/ebitzy/updateusergcmregid.php?phone="
							+ "&regId=" + regid;
					Log.i("URL", ">" + url);
					HttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url);
					HttpResponse httpResponse = client.execute(httpGet);
					HttpEntity resEntity = httpResponse.getEntity();

					String response = EntityUtils.toString(resEntity, "utf-8")
							.trim();
					Log.i("RESPONSE", "Response>> " + response);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			// Once registration is done, display the registration status
			// string in the Activity's UI.

			@SuppressWarnings("unused")
			protected void onPostExecute(String msg) {

			}

		}.execute(null, null, null);
	}

	private String getRegistrationId(Context context) {
		// TODO Auto-generated method stub
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.equalsIgnoreCase("")) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private void registerPushNoti() {
		/**
		 * For Google Cloud Messaging
		 */
		// Make sure the app is registered with GCM and with the server
		prefs = getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);

		regid = prefs.getString(PROPERTY_REG_ID, null);
		// If there is no registration ID, the app isn't registered.
		// Call registerBackground() to register it.
		gcm = GoogleCloudMessaging.getInstance(this);

		if (regid == null) {
			registerBackground();
		} else {
			updateUserGCMRegId();
		}// end else
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class MenuAdapter extends BaseAdapter {
		LayoutInflater inflater;
		private String[] titles;
		private String[] tags;
		ViewHolder holder;
		private boolean showNews, showAnnouncements;

		public void showNews() {
			showNews = true;
		}

		public void showAnnouncements() {
			showAnnouncements = true;
		}

		public MenuAdapter(String[] titles, String[] tags) {
			this.titles = titles;
			this.tags = tags;
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			if (titles != null) {
				return titles.length;
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.menu_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.menu_image_view);
				holder.textView = (TextView) convertView
						.findViewById(R.id.menu_txt);
				holder.alert = (ImageView) convertView
						.findViewById(R.id.menu_alert_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(titles[position]);
			holder.alert.setVisibility(View.GONE);

			if (tags[position].equals(Constants.MENU_TEACHER_MY_PROFILE)) {
				holder.img.setImageResource(R.drawable.profile);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_ACADEMICS)) {
				holder.img.setImageResource(R.drawable.academics);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_ATTENDANCE)) {
				holder.img.setImageResource(R.drawable.attendanceinfo);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_ASSIGNMENT)) {
				holder.img.setImageResource(R.drawable.assignmentstests);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_QUIZ)) {
				holder.img.setImageResource(R.drawable.quiz);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_ACTIVITY)) {
				holder.img.setImageResource(R.drawable.activity);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_WEEKLY_PLAN)) {
				holder.img.setImageResource(R.drawable.weekly_plan_icon);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_TEACHER_ANNOUNCEMENT)) {
				holder.img.setImageResource(R.drawable.classannouncements);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_TEACHER_COURSE_WORK)) {
				holder.img.setImageResource(R.drawable.coursework);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_TEACHER_LESSON_PLAN)) {
				holder.img.setImageResource(R.drawable.lessonplan);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_BULLETIN)) {
				holder.img.setImageResource(R.drawable.bulletins);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
				if (showNews) {
					holder.alert.setVisibility(View.VISIBLE);
				} else {
					holder.alert.setVisibility(View.GONE);
				}
			} else if (tags[position].equals(Constants.MENU_TEACHER_EXAMS)) {
				holder.img.setImageResource(R.drawable.exam);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_TIME_TABLE)) {
				holder.img.setImageResource(R.drawable.timetable);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_DIRECTORY)) {
				holder.img.setImageResource(R.drawable.directory);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_BLOG)) {
				holder.img.setImageResource(R.drawable.blog);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_BEHAVIOUR)) {
				holder.img.setImageResource(R.drawable.behavior);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			}
			 else if (tags[position]
						.equals(Constants.MENU_TEACHER_SPECIAL_CLASS)) {
					holder.img.setImageResource(R.drawable.special_class);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
				}
			else if (tags[position]
					.equals(Constants.MENU_TEACHER_SYNCHRONIZE)) {
				holder.img.setImageResource(R.drawable.sync);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.SYLLABUS)) {
				holder.img.setImageResource(R.drawable.syllabus);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.EVENT)) {
				holder.img.setImageResource(R.drawable.evencalender);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_TEACHER_SIGN_OUT)) {
				holder.img.setImageResource(R.drawable.signout);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.CAMPUS)) {
				holder.img.setImageResource(R.drawable.campus);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_WEB_ACCESS)) {
				holder.img.setImageResource(R.drawable.webaccess);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			}  else if (tags[position].equals(Constants.EVENT)) {
				holder.img.setImageResource(R.drawable.evencalender);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MANAGE_FEES)) {
				holder.img.setImageResource(R.drawable.fees);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			}
			else if(tags[position].equals(Constants.MENU_TEACHER_HELP)){
				holder.img.setImageResource(R.drawable.help_icon);
			}
			else if (tags[position].equals(Constants.MENU_CAREER)) {
				holder.img.setImageResource(R.drawable.career);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_DAILY_DIARY)) {
				holder.img.setImageResource(R.drawable.diary);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			}

			if (mSelectedPos == position)
				convertView.setBackgroundResource(R.drawable.menu_hover);
			else
				convertView.setBackgroundResource(R.drawable.menu_roll);
			return convertView;
		}

		private class ViewHolder {
			ImageView img, alert;
			TextView textView;
			Button btnNotificationBubble;
		}

		public void update(int position) {
			//
			mSelectedPos = position;

			notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	private void updateUserGCMRegId() {
		new AsyncTask() {
			@Override
			protected String doInBackground(Object... params) {
				String msg = "";
				try {
					String url = "http://206.225.81.253/web_services/ebitzy/updateusergcmregid.php?phone="
							+ "&regId=" + regid;
					Log.i("URL", ">" + url);
					HttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url);

					HttpResponse httpResponse = client.execute(httpGet);
					HttpEntity resEntity = httpResponse.getEntity();
					String response = EntityUtils.toString(resEntity, "utf-8")
							.trim();
					Log.i("RESPONSE", "Response>> " + response);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			// Once registration is done, display the registration status
			// string in the Activity's UI.

			@SuppressWarnings("unused")
			protected void onPostExecute(String msg) {

			}

		}.execute(null, null, null);
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	class Signout extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		ProgressDialog dialog;
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherMainActivity.this);
			dialog.setMessage("Signout Please Wait....");
			dialog.show();

		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

			if (result) {
				Toast.makeText(TeacherMainActivity.this, error,
						Toast.LENGTH_LONG).show();
				SchoolAppUtils.SetSharedParameter(TeacherMainActivity.this,
						Constants.USERID, "0");
				finish();
				startActivity(new Intent(TeacherMainActivity.this,
						SigninActivity.class));
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(TeacherMainActivity.this,
							"Sign Out", error);
				} else {
					SchoolAppUtils.showDialog(TeacherMainActivity.this,
							"Sign Out", getResources()
									.getString(R.string.error));
				}
			}

		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_signout);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						error = json.getString("data");
						return true;
					} else {
						error = json.getString("data");
						return false;
					}
				} else {
					error = getResources().getString(R.string.unknown_response);
				}
			} catch (JSONException e) {

			}
			return false;
		}
	}

	public void popUpView(){

		LayoutInflater layoutInflater = LayoutInflater.from(TeacherMainActivity.this);
		View promptView = layoutInflater.inflate(R.layout.message_teacher_prompt, null);
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(TeacherMainActivity.this,R.style.DialogTheme);
		alertDialogBuilder.setView(promptView);
		Button btn_ok;
		btn_ok = (Button) promptView.findViewById(R.id.btn_ok);
		final android.support.v7.app.AlertDialog alertD = alertDialogBuilder.create();

		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertD.dismiss();

			}
		});

		alertD.show();
	}
	
	

}
