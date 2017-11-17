package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.fragments.AisDailyDiaryListFragment;
import com.knwedu.ourschool.fragments.AisExamScheduleFragments;
import com.knwedu.ourschool.fragments.AisHelpFragment;
import com.knwedu.ourschool.fragments.AisSyllabus;
import com.knwedu.ourschool.fragments.AnnouncementListFragment;
import com.knwedu.ourschool.fragments.AssignmentListFragment;
import com.knwedu.ourschool.fragments.AttendanceViewFragment;
import com.knwedu.ourschool.fragments.Attendancrinfofragment;
import com.knwedu.ourschool.fragments.BlogListFragment;
import com.knwedu.ourschool.fragments.BulletinListFragment;
import com.knwedu.ourschool.fragments.CareerFragment;
import com.knwedu.ourschool.fragments.ClassFacilitiesListFragment;
import com.knwedu.ourschool.fragments.ClassFellowFragment;
import com.knwedu.ourschool.fragments.DailyDiaryParentFragment;
import com.knwedu.ourschool.fragments.DailyDiaryStudentFragment;
import com.knwedu.ourschool.fragments.ExamsListFragment;
import com.knwedu.ourschool.fragments.OrganizationListFragment;
import com.knwedu.ourschool.fragments.ProfileFragment;
import com.knwedu.ourschool.fragments.SchoolInfoFragment;
import com.knwedu.ourschool.fragments.SpecialLectureFragment;
import com.knwedu.ourschool.fragments.StudentBehaviourListFragment;
import com.knwedu.ourschool.fragments.SubjectListFragment;
import com.knwedu.ourschool.fragments.SubjectListFragmentLessonPlan;
import com.knwedu.ourschool.fragments.SubjectListFragmentWeeklyPlan;
import com.knwedu.ourschool.fragments.WeeklyPlanListFragment;
import com.knwedu.ourschool.fragments.HostelFragment;
import com.knwedu.ourschool.fragments.SchoolSyllabusFragment;
import com.knwedu.ourschool.fragments.ManageFeesFragment;
import com.knwedu.ourschool.fragments.TransportFragment;
import com.knwedu.ourschool.fragments.CampusFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.com.knwedu.calendar.EventFragment;
import com.knwedu.ourschool.push.CommonUtilities;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mMenuTags;
	private String[] mMenuTitles;
	private ImageButton button;
	private TextView title;
	private ProgressDialog dialog;
	private MenuAdapter adapter;
	private Button btnBubblenotification;
	public static Button showMonthWeek;
	private EditText edTxt_mail;
	private EditText edTxt_phone;
	private int mSelectedPos;
	private int type;
	private boolean doubleBackToExitPressedOnce = false;
	private String userPhNo, userOrgMail;

	private String mQuiz,mAssignment,mActivity,mClass,mAnnouncement,mDailydairy,mExam,mClasswork,mBulletin,mRequest,mAttendanceInfo,mCampus;
	private String mResponse;


	private static String url = "mobile/updateBadgeCount";

	//JSON Node Names
	private static final String TAG_USER = "user_id,";
	private static final String TAG_ID = "device_token";
	private static final String TAG_NAME = "organization_id";
	private static final String TAG_EMAIL = "module_name";




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

	GoogleCloudMessaging gcm;
	String regid;
	//private InterstitialAd interstitial;
	public static final int AD_REQUEST_SUCCEEDED = 101;
	public static final int AD_REQUEST_FAILED = 102;
	public static final int ON_SHOW_MODAL_AD = 103;
	public static final int ON_DISMISS_MODAL_AD = 104;
	public static final int ON_LEAVE_APP = 105;
	public static final int ON_CLICK = 106;

	private IMInterstitial interstitialAd;
	private AdInterstitialListener adInterstitialListener;
	private int  Menu=-1;
	private String Title,Message;
	private String appType;
	//private List;
	JSONArray NotificationArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// add noification value
		//Log.d("Received_Menu", getIntent().getIntExtra("menu_val", 0) + "");
		//Log.d("");

		 Title=CommonUtilities.getFromSP(getApplicationContext(), "Title");
		 Message=CommonUtilities.getFromSP(getApplicationContext(), "Message");
		 Menu=CommonUtilities.getFromSP(getApplicationContext(), "menu",0);

		appType = SchoolAppUtils.GetSharedParameter(MainActivity.this,
				Constants.APP_TYPE);
		//NotificationsInfo=CommonUtilities.getFromSP(getApplicationContext(),"NotificationInfo");
		//sec =cu.getValue..
		//SharedPreferences preferences=this.getSharedPreferences("notificationInfo",Context.MODE_PRIVATE);
		//String notiInfo=preferences.getString("Quiz","");
		//Log.d("value :",notiInfo);

		Log.d("title",Title);
		Log.d("message",Message);
		Log.d("menu",""+Menu);

		//ArrayList<HashMap<String, String>> contactList;


		//---------------------notification-----------------
		//		if(Integer.parseInt(mQuiz)>0) {
			mResponse = CommonUtilities.getFromSP(getApplicationContext(), "notificationInfo");
			if (!mResponse.isEmpty() || mResponse != null) {
				jsonParser(mResponse);
			}
			Log.i("test 3", mResponse);
			//JSONObject mainObject=new JSONObject(mResponse);

		//}
		//Log.d("NotificationInfo value come as:::",NotificationsInfo);

		// InMobi.initialize(this, InMobi_Property_Id);
		/*
		 * IMBanner banner = (IMBanner) findViewById(R.id.banner);
		 * banner.loadBanner();
		 */

		button = (ImageButton) findViewById(R.id.button);
		title = (TextView) findViewById(R.id.title_txt);

		// mMenuTitles = getResources().getStringArray(R.array.menu_list);

		mMenuTags = SchoolAppUtils.GetSharedParameter(MainActivity.this,
				Constants.MENU_TAGS).split("\\|");
		mMenuTitles = SchoolAppUtils.GetSharedParameter(MainActivity.this,
				Constants.MENU_TITLES).split("\\|");

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		showMonthWeek = (Button) findViewById(R.id.show_monthly_weekly);
		showMonthWeek.setVisibility(View.INVISIBLE);
		//  custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		adapter = new MenuAdapter(mMenuTitles, mMenuTags);

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

		userPhNo = SchoolAppUtils.GetSharedParameter(MainActivity.this,
				Constants.UMOBILENO);
		userOrgMail = SchoolAppUtils.GetSharedParameter(MainActivity.this,
				Constants.ORGEMAIL);
		if ((userOrgMail.length() > 0) && (userPhNo.length() > 0)) {
		} else {
			if (getIntent().hasExtra("fromlogin")) {

			final Dialog dialogn = new Dialog(MainActivity.this);
			dialogn.setContentView(R.layout.submit_phone_mail_dialog);
			dialogn.setTitle("Please Update Details");

			Button btnSubmit = (Button) dialogn.findViewById(R.id.btn_submit);
			Button btnCancel = (Button) dialogn.findViewById(R.id.btn_cancel);
			edTxt_phone = (EditText) dialogn
					.findViewById(R.id.dialog_txt_phone);
			edTxt_mail = (EditText) dialogn.findViewById(R.id.dialog_txt_mail);

			edTxt_phone.setText(userPhNo);
			edTxt_mail.setText(userOrgMail);

			dialogn.setCancelable(false);
			// show it
			dialogn.show();

			btnSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if(userPhNo.length()>0){
						if ((edTxt_phone.getText().toString().length() <= 0)) {
							Toast.makeText(MainActivity.this,
									"You can not specify it as blank.", Toast.LENGTH_SHORT)
									.show();
							return;
						}
					}

					if(userOrgMail.length()>0){
						if (!SchoolAppUtils.isValidEmail(edTxt_mail.getText()
								.toString())) {
							Toast.makeText(MainActivity.this,
									"Please enter valid Email", Toast.LENGTH_SHORT)
									.show();
							return;
						}

					}

					if ((edTxt_phone.getText().toString().length() <= 0) && (!SchoolAppUtils.isValidEmail(edTxt_mail.getText()
							.toString()))) {
						Toast.makeText(MainActivity.this,
								"Please enter Ph no and email", Toast.LENGTH_SHORT)
								.show();
						return;
					}

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							5);
					nameValuePairs.add(new BasicNameValuePair("user_type_id",
							SchoolAppUtils.GetSharedParameter(
									MainActivity.this, Constants.USERTYPEID)));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							SchoolAppUtils.GetSharedParameter(
									MainActivity.this, Constants.USERID)));
					nameValuePairs.add(new BasicNameValuePair(
							"organization_id", SchoolAppUtils
							.GetSharedParameter(MainActivity.this,
									Constants.UORGANIZATIONID)));
					nameValuePairs.add(new BasicNameValuePair("alt_email",
							edTxt_mail.getText().toString()));

					nameValuePairs.add(new BasicNameValuePair("mobile_no",
							edTxt_phone.getText().toString()));

					new checkmailidandphone().execute(nameValuePairs);

					dialogn.dismiss();

				}

			});

			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					dialogn.dismiss();
				}
			});

		}
	}


	}
	//================notification Parser======================
	private void jsonParser(String mResponse) {

		try {
			JSONObject responseParser = new JSONObject(mResponse);
			mAssignment = responseParser.getString("Assignment");
			//if(mQuiz!=null) {
			mQuiz = responseParser.getString("Quiz");
			Log.d("Quizeeee", mQuiz);
			//}
			mActivity=responseParser.getString("Activity");
			//mClass = responseParser.getString("Class");
			//mAnnouncement = responseParser.getString("Announcement");
			mDailydairy=responseParser.getString("Daily Diary");
			//mExam=responseParser.getString("Exam");
			mClasswork=responseParser.getString("Class Work");
			mBulletin=responseParser.getString("Bulletin");
			//mAttendanceInfo=responseParser.getString("Attendance Info");
			//mCampus=responseParser.getString("Campus");
			//mRequest=responseParser.getString("Request");


		} catch (JSONException e) {
			e.printStackTrace();
		}

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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AD_REQUEST_SUCCEEDED:
				onShowAd(null);
				break;
			case AD_REQUEST_FAILED:
				break;
			case ON_SHOW_MODAL_AD:
				break;
			case ON_DISMISS_MODAL_AD:
				break;
			case ON_LEAVE_APP:
				break;
			case ON_CLICK:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		/*if (interstitial.isLoaded()) {
			interstitial.show();
		}*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
					SchoolAppUtils.SetSharedBoolParameter(MainActivity.this,
							"showannouncements", false);
					button.setImageResource(R.drawable.menu_icon);
					adapter.showAnnouncements();
					adapter.notifyDataSetChanged();
				} else if (intent.getExtras().getBoolean("newincomming", false)) {
					SchoolAppUtils.SetSharedBoolParameter(MainActivity.this,
							"shownews", false);
					button.setImageResource(R.drawable.menu_icon);
					adapter.showNews();
					adapter.notifyDataSetChanged();
				}
			}
		}
	};

	/* The click listner for ListView in the navigation drawer */
	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		//AsyncTaskRunner runner = new AsyncTaskRunner();

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

			mActivity=0+"";
			mQuiz=0+"";
			mClasswork=0+"";
			mDailydairy=0+"";
			mAnnouncement=0+"";
			mAttendanceInfo=0+"";
			mCampus=0+"";
			mBulletin=0+"";
			mRequest=0+"";





		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		String label = mMenuTags[position];


		findViewById(R.id.graph_btn).setVisibility(View.GONE);

		String feildname= label.substring(0,1).toUpperCase()+ label.substring(1);
		//CommonUtilities.updateValueInSP(MainActivity.this,"notificationInfo",mResponse,feildname);
		updateNotificationDetails(feildname);


		if (label.equalsIgnoreCase(Constants.MENU_STUDENT_MY_PROFILE)) {
			// ----------------Profile------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new ProfileFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_ACADEMICS)) {
			// ----------------Academics------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new SubjectListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_WEEKLY_PLAN)) {
			// -----------------weekly plan-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new WeeklyPlanListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_CLASS_FELLOW)) {
			// ----------------Class Fellows------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new ClassFellowFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_LESSON_PLAN)) {
			// ------------------Lesson Plan----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new SubjectListFragmentLessonPlan()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_WEEKLY_PLAN)) {
			// -----------------Weekly Plan-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new SubjectListFragmentWeeklyPlan()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_TIME_TABLE)) {
			// -------------------Time Table---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new ClassFacilitiesListFragment()).commit();

			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_ASSIGNMENT)) {
			// -------------------Assignment---------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new AssignmentListFragment(1))
					.commit();

//			CommonUtilities.updateValueInSPAssignment(MainActivity.this, "notificationInfo", mResponse);
			mAssignment = 0+"";
			//Log.d("Shared Assignment:", mAssignment);

			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.VISIBLE);
			showMonthWeek.setText(R.string.all);


		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_QUIZ)) {
			// -------------------Quiz---------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new AssignmentListFragment(2))
				.commit();
//			String feildname= label.substring(0,1).toUpperCase()+ label.substring(1);
			//CommonUtilities.updateValueInQuiz(MainActivity.this, "notificationInfo", mResponse);
			//CommonUtilities.updateValueInSPQuiz(MainActivity.this,"notificationInfo",mResponse);
			mQuiz=0+"";
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.VISIBLE);
			showMonthWeek.setText(R.string.all);

		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_ACTIVITY)) {
			// --------------------Activity--------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new AssignmentListFragment(3))
					.commit();
//			String feildname= label.substring(0,1).toUpperCase()+ label.substring(1);
			//CommonUtilities.updateValueInSPActivity(MainActivity.this, "notificationInfo", mResponse);
			mActivity=0+"";
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.VISIBLE);
			showMonthWeek.setText(R.string.all);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_EXAMS)) {
			// ------------------Exams----------------------
			/*fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new ExamsListFragment())
					.commit();*/
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
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_COURSEWORK)) {
			// -----------------Course Work-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new AnnouncementListFragment(1)).commit();
			//CommonUtilities.updateValueInSPClasswork(MainActivity.this, "notificationInfo", mResponse);
			mClasswork=0+"";
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_ANNOUNCEMENT)) {
			// -------------------Announcement---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new AnnouncementListFragment(2)).commit();
			mAnnouncement=0+"";

			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_BULLETINS)) {
			// --------------------Bulletins--------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new BulletinListFragment())
					.commit();
			//CommonUtilities.updateValueInSPBulletin(MainActivity.this, "notificationInfo", mResponse);
			mBulletin=0+"";
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_BEHAVIOUR)) {
			// -------------------Behavior---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new StudentBehaviourListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_ORGANIZATION)) {
			// -------------------Organization---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame, new OrganizationListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(Constants.MENU_STUDENT_ATTENDANCE_INFO)) {
			// -------------------Attendance Info---------------------
//			fragmentManager.beginTransaction()
//					.replace(R.id.content_frame, new Attendancrinfofragment())
//					.commit();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new AttendanceViewFragment())
					.commit();
			mAttendanceInfo=0+"";
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_DIRECTORY)) {
			// --------------------Directory--------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new SchoolInfoFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_BLOG)) {
			// --------------------Blog--------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new BlogListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}
		else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_HELP)) {
			// --------------------Blog--------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new AisHelpFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}


		else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_TRANSPORT)) {
			// ------------------Transport----------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new TransportFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_HOSTEL)) {
			// ------------------Hostel----------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new HostelFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_SPECIAL_CLASS)) {
			// ------------------Special Class----------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new SpecialLectureFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.SYLLABUS)) {
			// --------------------Syllabus--------------------
			if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
				fragmentManager.beginTransaction().replace(R.id.content_frame, new AisSyllabus()).commit();
			}else{

				fragmentManager.beginTransaction().replace(R.id.content_frame, new SchoolSyllabusFragment(0)).commit();
			}

			//fragmentManager.beginTransaction().replace(R.id.content_frame, new SchoolSyllabusFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.EVENT)) {
			// --------------------Event Details--------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new EventFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MANAGE_FEES)) {
			// --------------------Manage Fees--------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new ManageFeesFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.CAMPUS)) {
			// --------------------Campus--------------------

			fragmentManager.beginTransaction().replace(R.id.content_frame, new CampusFragment()).commit();
			mCampus=0+"";
			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_WEB_ACCESS)) {
			// ----------------Web Access------------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new WebAccessFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		}

		else if (label.equalsIgnoreCase(Constants.MENU_CAREER)) {
			// ----------------Career------------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new CareerFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		}
		else if (label.equalsIgnoreCase(Constants.MENU_DAILY_DIARY)) {
			// -------------------Daily Diary---------------------
			/*fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new DailyDiaryStudentFragment())
					.commit();*/
			if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, new AisDailyDiaryListFragment())
						.commit();

			}else {
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, new DailyDiaryStudentFragment())
						.commit();
			}
			//CommonUtilities.updateValueInSPDailydiary(MainActivity.this, "notificationInfo", mResponse);
			mDailydairy=0+"";
			showMonthWeek.setVisibility(View.GONE);
		}
		else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_SIGN_OUT)) {
			// ------------------Sign Out----------------------
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("Sign Out");
			builder.setMessage("Are you sure?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if (SchoolAppUtils.isOnline(MainActivity.this)) {
								SchoolAppUtils.SetSharedBoolParameter(
										MainActivity.this, Constants.UISSIGNIN,
										false);
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										3);
								nameValuePairs.add(new BasicNameValuePair(
										"user_type_id", SchoolAppUtils
												.GetSharedParameter(
														MainActivity.this,
														Constants.USERTYPEID)));
								nameValuePairs.add(new BasicNameValuePair(
										"organization_id",
										SchoolAppUtils.GetSharedParameter(
												MainActivity.this,
												Constants.UORGANIZATIONID)));
								nameValuePairs.add(new BasicNameValuePair(
										"user_id", SchoolAppUtils
												.GetSharedParameter(
														MainActivity.this,
														Constants.USERID)));
								new Signout().execute(nameValuePairs);
							} else {
								SchoolAppUtils.SetSharedParameter(
										MainActivity.this, Constants.USERID,
										"0");
								finish();
								startActivity(new Intent(MainActivity.this,
										SigninActivity.class));
							}
						}
					});
			builder.setNegativeButton("No", null);
			builder.create().show();

		}

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

		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void updateNotificationDetails(String moduleName) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(
				new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(MainActivity.this, Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("device_token",
				SchoolAppUtils.GetSharedParameter(MainActivity.this, Constants.DEVICE_TOKEN)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(MainActivity.this, Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("module_name", moduleName));
		Log.d("module nameeee", moduleName);
		new UpdateNotificationAsyntask().execute(nameValuePairs);
	}

	private class UpdateNotificationAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		String message = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			dialog = new ProgressDialog(MainActivity.this);
//			dialog.setTitle(MainActivity.this.getTitle().toString());
//			dialog.setMessage(getResources().getString(R.string.please_wait));
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.setCancelable(false);
//			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_updatenotification);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";

			}
			Log.d("param1",nameValuePairs+"");
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,Urls.api_updatenotification
			);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						message = json.getString("data");
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

			} catch (JSONException e) {

			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
//			if (dialog != null) {
//				dialog.dismiss();
//				dialog = null;
//			}
			if (result) {
//				for (int i = 0; i < transports.size(); i++) {
//					DataStructureFramwork.Transport transport = transports.get(i);
//					tvTransportCode.setText(transport.transport_code);
//					tvRouteName.setText(transport.transport_route_name);
//					tvRouteDesc.setText(transport.transport_desc);
//					tvPickPoint.setText(transport.transport_pick_point);
//					tvDropPoint.setText(transport.transport_drop_point);
//
//					// getFromServer();
				//Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
				Log.d("messsss",message);

			}
//			else {
//				if (error.length() > 0) {
//					SchoolAppUtils.showDialog(MainActivity.this, MainActivity.this.getTitle().toString(), error);
//				} else {
//					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(),
//							getResources().getString(R.string.error));
//				}
//				mainLayout.setVisibility(View.GONE);
//			}
		}

	}
	/*
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

				holder.btnNotificationBubble =(Button)convertView.findViewById(R.id.btnNotificationBubble);

				//holder.btnNotificationBubble.setOnClickListener(R.id.btnNotificationBubble);
				//button.setImageResource(R.drawable.notification_final);
				adapter.showAnnouncements();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			//sec




			//------------notification---------------
//			if(titles[position].equalsIgnoreCase("notificationtype")){
//				holder.btnNotificationBubble.setVisibility(View.VISIBLE);
//				holder.btnNotificationBubble.setText("notifications");
//			}
//			else{
//				holder.btnNotificationBubble.setVisibility(View.GONE);
//			}

			holder.textView.setText(titles[position]);
			holder.alert.setVisibility(View.GONE);
			if (tags[position].equals(Constants.MENU_STUDENT_MY_PROFILE)) {
				holder.img.setImageResource(R.drawable.profile);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_ACADEMICS)) {
				holder.img.setImageResource(R.drawable.academics);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_STUDENT_LESSON_PLAN)) {
				holder.img.setImageResource(R.drawable.lessonplan);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_STUDENT_CLASS_FELLOW)) {
				holder.img.setImageResource(R.drawable.classfellow);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_TIME_TABLE)) {
				holder.img.setImageResource(R.drawable.timetable);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_ASSIGNMENT)) {
				holder.img.setImageResource(R.drawable.assignmentstests);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mAssignment!=null){
					buttonVisibilty(holder,mAssignment);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);

			} else if (tags[position].equals(Constants.MENU_STUDENT_QUIZ)) {
				holder.img.setImageResource(R.drawable.quiz);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mQuiz!=null){
					buttonVisibilty(holder,mQuiz);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);


			}

			else if(tags[position].equals(Constants.MENU_STUDENT_HELP)){
				holder.img.setImageResource(R.drawable.help_icon);
			}

			else if (tags[position].equals(Constants.MENU_STUDENT_ACTIVITY)) {
				holder.img.setImageResource(R.drawable.activity);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mActivity!=null){
					buttonVisibilty(holder,mActivity);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);
//				holder.img2.setImageResource();
			} else if (tags[position].equals(Constants.MENU_STUDENT_EXAMS)) {
				holder.img.setImageResource(R.drawable.exam);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mExam!=null){
					buttonVisibilty(holder,mExam);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_COURSEWORK)) {
				holder.img.setImageResource(R.drawable.coursework);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mClasswork!=null){
					buttonVisibilty(holder,mClasswork);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);

			} else if (tags[position]
					.equals(Constants.MENU_TEACHER_WEEKLY_PLAN)) {
				holder.img.setImageResource(R.drawable.weekly_plan_icon);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_STUDENT_ANNOUNCEMENT)) {
				holder.img.setImageResource(R.drawable.classannouncements);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mAnnouncement!=null){
					buttonVisibilty(holder,mAnnouncement);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);

			} else if (tags[position].equals(Constants.MENU_STUDENT_BULLETINS)) {
				holder.img.setImageResource(R.drawable.bulletins);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mBulletin!=null){
					buttonVisibilty(holder,mBulletin);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);


			} else if (tags[position]
					.equals(Constants.MENU_STUDENT_ORGANIZATION)) {
				holder.img.setImageResource(R.drawable.organization);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_STUDENT_ATTENDANCE_INFO)) {
				holder.img.setImageResource(R.drawable.attendanceinfo);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mAttendanceInfo!=null){
					buttonVisibilty(holder,mAttendanceInfo);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);

			} else if (tags[position].equals(Constants.MENU_STUDENT_DIRECTORY)) {
				holder.img.setImageResource(R.drawable.directory);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_BLOG)) {
				holder.img.setImageResource(R.drawable.blog);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_TRANSPORT)) {
				holder.img.setImageResource(R.drawable.transport);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_HOSTEL)) {
				holder.img.setImageResource(R.drawable.hostel);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_BEHAVIOUR)) {
				holder.img.setImageResource(R.drawable.behavior);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(Constants.MENU_STUDENT_SPECIAL_CLASS)) {
				holder.img.setImageResource(R.drawable.special_class);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_SIGN_OUT)) {
				holder.img.setImageResource(R.drawable.signout);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.CAMPUS)) {
				holder.img.setImageResource(R.drawable.campus);
				holder.btnNotificationBubble.setVisibility(View.GONE);
				if(mCampus!=null){
					buttonVisibilty(holder,mCampus);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);

			} else if (tags[position].equals(Constants.MENU_WEB_ACCESS)) {
				holder.img.setImageResource(R.drawable.webaccess);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			}  else if (tags[position].equals(Constants.EVENT)) {
				holder.img.setImageResource(R.drawable.evencalender);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MANAGE_FEES)) {
				holder. img.setImageResource(R.drawable.fees);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_CAREER)) {
				holder.img.setImageResource(R.drawable.career);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.SYLLABUS)) {
				holder.img.setImageResource(R.drawable.syllabus);
				holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_DAILY_DIARY)) {
				holder.img.setImageResource(R.drawable.diary);
				if(mDailydairy!=null){
					buttonVisibilty(holder,mDailydairy);
				}
				else
					holder.btnNotificationBubble.setVisibility(View.GONE);
			}

			if (mSelectedPos == position)
				convertView.setBackgroundResource(R.drawable.menu_hover);
			else
				convertView.setBackgroundResource(R.drawable.menu_roll);

			return convertView;
		}

		private class ViewHolder {
			ImageView img,img2, alert;
			TextView textView;
			Button btnNotificationBubble;

		}

		public void update(int position) {
			//
			mSelectedPos = position;

			notifyDataSetChanged();
		}
	}

	private class checkmailidandphone extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		LayoutInflater inflater;
		String response = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setTitle(getResources()
					.getString(R.string.checkmailandphone));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nvp = params[0];

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_save_mail_mobile);
			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("0")) {
						response = "not Provided";
					} else {
						response = "ok";

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return false;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (response.equalsIgnoreCase("not Provided")) {
			} else {
				dialog.dismiss();
				SchoolAppUtils.SetSharedParameter(MainActivity.this,
						Constants.UMOBILENO, edTxt_phone.getText()
								.toString());
				SchoolAppUtils.SetSharedParameter(MainActivity.this,
						Constants.UALTEMAIL, edTxt_mail.getText()
								.toString());

			}
		}
	}

	class AdInterstitialListener implements IMInterstitialListener {

		@Override
		public void onLeaveApplication(IMInterstitial arg0) {
			handler.sendEmptyMessage(ON_LEAVE_APP);
		}

		@Override
		public void onDismissInterstitialScreen(IMInterstitial arg0) {
			handler.sendEmptyMessage(ON_DISMISS_MODAL_AD);
		}

		@Override
		public void onInterstitialFailed(IMInterstitial arg0, IMErrorCode eCode) {
			Message msg = handler.obtainMessage(AD_REQUEST_FAILED);
			msg.obj = eCode;
			handler.sendMessage(msg);
		}

		@Override
		public void onInterstitialInteraction(IMInterstitial arg0,
				Map<String, String> arg1) {
			// no-op
		}

		@Override
		public void onInterstitialLoaded(IMInterstitial arg0) {
			handler.sendEmptyMessage(AD_REQUEST_SUCCEEDED);
		}

		@Override
		public void onShowInterstitialScreen(IMInterstitial arg0) {
			handler.sendEmptyMessage(ON_SHOW_MODAL_AD);
		}
	};

	public void onShowAd(View view) {
		if (interstitialAd != null)
			interstitialAd.show();
	}

	class Signout extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		ProgressDialog dialog;
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
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
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG)
						.show();
				SchoolAppUtils.SetSharedParameter(MainActivity.this,
						Constants.USERID, "0");
				finish();
				startActivity(new Intent(MainActivity.this,
						SigninActivity.class));
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(MainActivity.this, "Sign Out",
							error);
				} else {
					SchoolAppUtils
							.showDialog(
									MainActivity.this,
									"Sign Out",
									getResources().getString(
											R.string.unknown_response));
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



	public void buttonVisibilty(final MenuAdapter.ViewHolder holder1,String message){
			if(Integer.parseInt(message)>0){
				holder1.btnNotificationBubble.setVisibility(View.VISIBLE);
				holder1.btnNotificationBubble.setText(message);
			}else {
				holder1.btnNotificationBubble.setVisibility(View.GONE);
			}
	}


}
