package com.knwedu.college;

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
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;
import com.knwedu.college.fragments.CollegeAnnouncementListFragment;
import com.knwedu.college.fragments.CollegeAssignmentListFragment;
import com.knwedu.college.fragments.CollegeAttendancrinfofragment;
import com.knwedu.college.fragments.CollegeBlogListFragment;
import com.knwedu.college.fragments.CollegeBulletinListFragment;
import com.knwedu.college.fragments.CollegeCareerFragment;
import com.knwedu.college.fragments.CollegeClaassFellowsFrag;
import com.knwedu.college.fragments.CollegeCourseworkListFragment;
import com.knwedu.college.fragments.CollegeParentRequestFragment;
import com.knwedu.college.fragments.CollegeProfileFragment;
import com.knwedu.college.fragments.CollegeSchoolInfoFragment;
import com.knwedu.college.fragments.CollegeSpecialLectureFragment;
import com.knwedu.college.fragments.CollegeStudentTestFragment;
import com.knwedu.college.fragments.CollegeSubjectListFragment;
import com.knwedu.college.fragments.CollegeSubjectListFragmentLessonPlan;
import com.knwedu.college.fragments.CollegeTeacherCampusFragment;
import com.knwedu.college.fragments.CollegeTeacherTimeTableFragment;
import com.knwedu.college.fragments.CollegeHostelFragment;
import com.knwedu.college.fragments.CollegeSyllabusFragment;
import com.knwedu.college.fragments.ManageFeesFragment;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.MenuItems;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.com.knwedu.calendar.EventFragment;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.SigninActivity;
import com.knwedu.college.fragments.AttendanceViewFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeMainActivity extends FragmentActivity {
	public static DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mMenuTags;
	private String[] mMenuTitles;
	private ImageButton button;
	private TextView title;
	private ProgressDialog dialog;
	private MenuAdapter adapter;
	public static Button showMonthWeek;
	EditText edTxt_mail;
	EditText edTxt_phone;
	private int mSelectedPos;
	// /menu parse
	private String mMenuName = "[]";
	private JSONArray menuArray;
	private String userPhNo, userOrgMail;

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
	ArrayList<MenuItems> marks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_main);
		marks = new ArrayList<MenuItems>();
		// InMobi.initialize(this, InMobi_Property_Id);
		/*
		 * IMBanner banner = (IMBanner) findViewById(R.id.banner);
		 * banner.loadBanner();
		 */
		if (getIntent() != null) {
			mMenuName = getIntent().getStringExtra("menuArray");
		}
		button = (ImageButton) findViewById(R.id.button);
		title = (TextView) findViewById(R.id.title_txt);

		// mMenuTitles = getResources().getStringArray(R.array.menu_list);

		mMenuTags = CollegeAppUtils.GetSharedParameter(
				CollegeMainActivity.this, CollegeConstants.MENU_TAGS).split(
				"\\|");
		mMenuTitles = CollegeAppUtils.GetSharedParameter(
				CollegeMainActivity.this, CollegeConstants.MENU_TITLES).split(
				"\\|");
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		showMonthWeek = (Button) findViewById(R.id.show_monthly_weekly);
		showMonthWeek.setVisibility(View.INVISIBLE);
		// set a custom shadow that overlays the main content when the drawer
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
			selectItem(0);
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


		userPhNo = CollegeAppUtils.GetSharedParameter(CollegeMainActivity.this,
				CollegeConstants.UMOBILENO);
		userOrgMail = CollegeAppUtils.GetSharedParameter(CollegeMainActivity.this,
				CollegeConstants.ORGEMAIL);

		if ((userOrgMail.length() > 0) && (userPhNo.length() > 0)) {
		}
		else{
			if (getIntent().hasExtra("fromlogin")) {

				final Dialog dialogn = new Dialog(CollegeMainActivity.this);
				dialogn.setContentView(R.layout.submit_phone_mail_dialog);
				dialogn.setTitle("Please Update Details");

				Button btnSubmit = (Button) dialogn.findViewById(R.id.btn_submit);
				Button btnCancel = (Button) dialogn.findViewById(R.id.btn_cancel);
				edTxt_phone = (EditText) dialogn.findViewById(R.id.dialog_txt_phone);
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
								Toast.makeText(CollegeMainActivity.this,
										"You can not specify it as blank.", Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}

						if(userOrgMail.length()>0){
							if (!CollegeAppUtils.isValidEmail(edTxt_mail.getText()
									.toString())) {
								Toast.makeText(CollegeMainActivity.this,
										"Please enter valid Email", Toast.LENGTH_SHORT)
										.show();
								return;
							}

						}

						if ((edTxt_phone.getText().toString().length() <= 0) && (!CollegeAppUtils.isValidEmail(edTxt_mail.getText()
								.toString()))) {
							Toast.makeText(CollegeMainActivity.this,
									"Please enter Ph no and email", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								5);
						nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
								.GetSharedParameter(CollegeMainActivity.this, "id")));
						nameValuePairs.add(new BasicNameValuePair("alt_email",
								edTxt_mail.getText().toString()));

						nameValuePairs.add(new BasicNameValuePair("alt_mobile_no",
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


		/*if (getIntent().hasExtra("fromlogin")) {
			if (((CollegeAppUtils.GetSharedParameter(CollegeMainActivity.this,
					CollegeConstants.UALTEMAIL)).length() > 0)
					|| ((CollegeAppUtils.GetSharedParameter(
					CollegeMainActivity.this, CollegeConstants.UMOBILENO))
					.length() > 0)) {
				showDialog1();
			} else {
				showDialog();
			}
		}*/



	}

	private void showDialog1() {

		final Dialog dialogn = new Dialog(CollegeMainActivity.this);
		dialogn.setContentView(R.layout.submit_phone_mail_dialog);
		dialogn.setTitle("Submit Phone and Mail");

		Button btnSubmit = (Button) dialogn.findViewById(R.id.btn_submit);
		Button btnCancel = (Button) dialogn.findViewById(R.id.btn_cancel);
		edTxt_phone = (EditText) dialogn.findViewById(R.id.dialog_txt_phone);
		edTxt_phone.setText(CollegeAppUtils.GetSharedParameter(
				CollegeMainActivity.this, CollegeConstants.UMOBILENO));
		edTxt_mail = (EditText) dialogn.findViewById(R.id.dialog_txt_mail);
		edTxt_mail.setText(CollegeAppUtils.GetSharedParameter(
				CollegeMainActivity.this, CollegeConstants.UALTEMAIL));
		dialogn.setCancelable(false);
		// show it
		dialogn.show();

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((edTxt_phone.getText().toString().length() <= 0)
						&& (edTxt_mail.getText().toString().length() <= 0)) {
					Toast.makeText(CollegeMainActivity.this,
							"Please enter mail and phone no.",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
				}
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(CollegeMainActivity.this, "id")));
				nameValuePairs.add(new BasicNameValuePair("alt_email",
						edTxt_mail.getText().toString()));

				nameValuePairs.add(new BasicNameValuePair("alt_mobile_no",
						edTxt_phone.getText().toString()));

				new checkmailidandphone().execute(nameValuePairs);
				dialogn.dismiss();
			}

		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogn.dismiss();
			}
		});
	}

	// / MENU PARSE METHOD.........
	private void showDialog() {
		final Dialog dialogn = new Dialog(CollegeMainActivity.this);
		dialogn.setContentView(R.layout.submit_phone_mail_dialog);
		dialogn.setTitle("Submit Phone and Mail");
		Button btnSubmit = (Button) dialogn.findViewById(R.id.btn_submit);
		Button btnCancel = (Button) dialogn.findViewById(R.id.btn_cancel);
		edTxt_phone = (EditText) dialogn.findViewById(R.id.dialog_txt_phone);
		edTxt_mail = (EditText) dialogn.findViewById(R.id.dialog_txt_mail);
		dialogn.setCancelable(false);
		// show it
		dialogn.show();

		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((edTxt_phone.getText().toString().length() <= 0)
						&& (edTxt_mail.getText().toString().length() <= 0)) {
					Toast.makeText(CollegeMainActivity.this,
							"Please enter mail and phone no.",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
				}

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(CollegeMainActivity.this, "id")));
				nameValuePairs.add(new BasicNameValuePair("alt_email",
						edTxt_mail.getText().toString()));

				nameValuePairs.add(new BasicNameValuePair("alt_mobile_no",
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

	protected JSONArray createMenuRequest() {
		menuArray = new JSONArray();
		try {
			for (int i = 0; i < menuArray.length(); i++) {
				MenuItems menu = new MenuItems(menuArray.getJSONObject(i));
				marks.add(menu);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return menuArray;
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

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */
	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		/*if (interstitial.isLoaded()) {
			interstitial.show();
		}*/
	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */

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
					CollegeAppUtils.SetSharedBoolParameter(
							CollegeMainActivity.this, "showannouncements",
							false);
					button.setImageResource(R.drawable.menu_icon);
					adapter.showAnnouncements();
					adapter.notifyDataSetChanged();
				} else if (intent.getExtras().getBoolean("newincomming", false)) {
					CollegeAppUtils.SetSharedBoolParameter(
							CollegeMainActivity.this, "shownews", false);
					button.setImageResource(R.drawable.menu_icon);
					adapter.showNews();
					adapter.notifyDataSetChanged();
				}
			}
		}
	};
	private boolean doubleBackToExitPressedOnce;

	/* The click listner for ListView in the navigation drawer */
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
		// menu:
		// profile|assignment|test|bulletin|classwork|subject|career|timetable|attendance|announcement|requests|blog|specialclass|feedback|classfellows|signout

		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		String label = mMenuTags[position];
		if (label.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_MY_PROFILE)) {
			// ----------------Profile------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new CollegeProfileFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_CLASSFELLOWS)) {
			// ----------------Class Fellows------------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame, new CollegeClaassFellowsFrag())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		}
		 else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_TIME_TABLE)) {
			// -------------------Time Table---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeTeacherTimeTableFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_ASSIGNMENT)) {
			// -------------------Assignment---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeAssignmentListFragment(1)).commit();
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.VISIBLE);
			showMonthWeek.setText(R.string.all);

		} else if (label.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_QUIZ)) {
			// -------------------Quiz---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeStudentTestFragment(2)).commit();
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.VISIBLE);
			showMonthWeek.setText(R.string.all);

		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_SUBJECT)) {
			// --------------------Activity--------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeSubjectListFragment()).commit();
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_COURSEWORK)) {
			// -----------------Course Work-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeAnnouncementListFragment(1)).commit();
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_ANNOUNCEMENT)) {
			// -------------------Announcement---------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeCourseworkListFragment(2)).commit();

			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_BULLETINS)) {
			// --------------------Bulletins--------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeBulletinListFragment()).commit();
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.GONE);
		}  else if (label.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_BLOG)) {
			// --------------------Blog--------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new CollegeBlogListFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_CAREER)) {
			// ------------------Career----------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new CollegeCareerFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_FEEDBACK)) {
			// ------------------Student Feedback----------------------
			/*
			 * fragmentManager.beginTransaction() .replace(R.id.content_frame,
			 * new CollegeStudentFeedback()) .commit();
			 */
			Intent i = new Intent(getApplicationContext(),
					CollegeStudentFeedback.class);
			i.putExtra("from", "Student");
			startActivity(i);
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_REQUESTS)) {
			// ------------------Manage Request----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeParentRequestFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		}else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_DIRECTORY)) {
			// ------------------Directory----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeSchoolInfoFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		}



		else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_SPECIAL_LECTURE)) {
			// ------------------Special Class----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeSpecialLectureFragment(1)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_LESSON_PLAN)) {
			// ------------------Lesson Plan----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeSubjectListFragmentLessonPlan())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_TEACHER_CAMPUS_ACTIVITY)) {
			// -----------------Campus Activity-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeTeacherCampusFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
			/*
			 * startActivity(new Intent(TeacherMainActivity.this,
			 * SyncActivity.class));
			 */
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_HOSTEL)) {
			// ------------------Hostel----------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new CollegeHostelFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_SYLLABUS)) {
			// ------------------Syllabus----------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new CollegeSyllabusFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		}  else if (label.equalsIgnoreCase(Constants.EVENT)) {
			// -----------------Event-----------------------

			fragmentManager.beginTransaction().replace(R.id.content_frame, new EventFragment(1)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MANAGE_FEES)) {
			// -----------------Manage Fee-----------------------

			fragmentManager.beginTransaction().replace(R.id.content_frame, new ManageFeesFragment(0)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_PARENT_ATTENDANCE_INFO)) {
			// ----------------Attendance Info------------------------
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new AttendanceViewFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_SIGN_OUT)) {
			// ------------------Sign Out----------------------

			AlertDialog.Builder builder = new AlertDialog.Builder(
					CollegeMainActivity.this);
			builder.setTitle("Log Out");
			builder.setMessage("Are you sure you want to exit?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						private Context context;

						public void onClick(DialogInterface dialog, int which) {
							if (CollegeAppUtils
									.isOnline(CollegeMainActivity.this)) {
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										0);
								nameValuePairs.add(new BasicNameValuePair("id",
										CollegeAppUtils.GetSharedParameter(
												CollegeMainActivity.this, "id")));
								new LogoutAsyntask().execute(nameValuePairs);
							} else {
								SchoolAppUtils.SetSharedParameter(CollegeMainActivity.this,
										Constants.USERID, "0");
								// CollegeAppUtils.preferences.edit().clear().commit();
								Intent i = new Intent(getApplicationContext(), SigninActivity.class);
								startActivity(i);
								finish();
							}

						}

						private void showDialog(String str) {
							final Dialog dialog2 = new Dialog(context);
							dialog2.setCanceledOnTouchOutside(false);
							dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog2.setContentView(R.layout.custom_dialog);
							TextView text = (TextView) dialog2
									.findViewById(R.id.text);
							text.setText(str);
							text.setGravity(Gravity.CENTER);
							Button dialogButton = (Button) dialog2
									.findViewById(R.id.dialogButtonOK);
							// if button is clicked, close the custom dialog
							dialogButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog2.dismiss();
										}
									});
							dialog2.show();
						}
					});
			builder.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
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
				convertView.setTag(holder);

				holder.btnNotificationBubble =(Button)convertView.findViewById(R.id.btnNotificationBubble);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(titles[position]);
			holder.alert.setVisibility(View.GONE);
			if (tags[position].equals(CollegeConstants.MENU_STUDENT_MY_PROFILE)) {
				holder.img.setImageResource(R.drawable.profile);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_CLASSFELLOWS)) {
				holder.img.setImageResource(R.drawable.classfellow);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_LESSON_PLAN)) {
				holder.img.setImageResource(R.drawable.lessonplan);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_TIME_TABLE)) {
				holder.img.setImageResource(R.drawable.timetable);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_ASSIGNMENT)) {
				holder.img.setImageResource(R.drawable.assignmentstests);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_QUIZ)) {
				holder.img.setImageResource(R.drawable.quiz);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_SUBJECT)) {
				holder.img.setImageResource(R.drawable.academics);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_EXAMS)) {
				holder.img.setImageResource(R.drawable.exam);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_COURSEWORK)) {
				holder.img.setImageResource(R.drawable.coursework);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_ANNOUNCEMENT)) {
				holder.img.setImageResource(R.drawable.classannouncements);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_BULLETINS)) {
				holder.img.setImageResource(R.drawable.bulletins);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_ORGANIZATION)) {
				holder.img.setImageResource(R.drawable.organization);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_ATTENDANCE_INFO)) {
				holder.img.setImageResource(R.drawable.attendanceinfo);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_DIRECTORY)) {
				holder.img.setImageResource(R.drawable.directory);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_BLOG)) {
				holder.img.setImageResource(R.drawable.blog);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_CAREER)) {
				holder.img.setImageResource(R.drawable.career);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_FEEDBACK)) {
				holder.img.setImageResource(R.drawable.feedback);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_REQUESTS)) {
				holder.img.setImageResource(R.drawable.request);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_HOSTEL)) {
				holder.img.setImageResource(R.drawable.hostel);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_SPECIAL_LECTURE)) {
				holder.img.setImageResource(R.drawable.special_class);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_SIGN_OUT)) {
				holder.img.setImageResource(R.drawable.signout);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_TEACHER_CAMPUS_ACTIVITY)) {
				holder.img.setImageResource(R.drawable.campusactivites);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_SYLLABUS)) {
				holder.img.setImageResource(R.drawable.syllabus);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.EVENT)) {
				holder.img.setImageResource(R.drawable.evencalender);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MANAGE_FEES)) {
				holder.img.setImageResource(R.drawable.fees);
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

	private class checkmailidandphone extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		LayoutInflater inflater;
		String response = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeMainActivity.this);
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

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					CollegeUrls.api_save_mail_mobile);
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
				dialog.dismiss();
			} else {
				dialog.dismiss();
				CollegeAppUtils.SetSharedParameter(
						CollegeMainActivity.this,
						CollegeConstants.UMOBILENO, edTxt_phone
								.getText().toString());
				CollegeAppUtils.SetSharedParameter(
						CollegeMainActivity.this,
						CollegeConstants.UALTEMAIL, edTxt_mail
								.getText().toString());
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

	class LogoutAsyntask extends
			AsyncTask<List<NameValuePair>, String, Boolean> {
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_student_logout);

			return true;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SchoolAppUtils.SetSharedParameter(CollegeMainActivity.this,
					Constants.USERID, "0");
			// CollegeAppUtils.preferences.edit().clear().commit();
			Intent i = new Intent(getApplicationContext(), SigninActivity.class);
			startActivity(i);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
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
}
