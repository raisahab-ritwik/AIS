package com.knwedu.college;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.knwedu.college.fragments.CollegeAnnouncementListFragment;
import com.knwedu.college.fragments.CollegeAssignmentListFragment;
import com.knwedu.college.fragments.CollegeAttendancrinfofragment;
import com.knwedu.college.fragments.CollegeBlogListFragment;
import com.knwedu.college.fragments.CollegeBulletinListFragment;
import com.knwedu.college.fragments.CollegeCareerFragment;
import com.knwedu.college.fragments.CollegeClaassFellowsFrag;
import com.knwedu.college.fragments.CollegeCourseworkListFragment;
import com.knwedu.college.fragments.CollegeParentChildrenListFragment;
import com.knwedu.college.fragments.CollegeParentProfileFragment;
import com.knwedu.college.fragments.CollegeParentRequestFragment;
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
import com.knwedu.college.utils.CollegeDataStructureFramwork.StudentProfileInfo;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.com.knwedu.calendar.EventFragment;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.SigninActivity;
import com.knwedu.college.fragments.AttendanceViewFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeParentMainActivity extends FragmentActivity {
	public static DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private String[] mMenuTags;
	private String[] mMenuTitles;
	private ImageButton button;
	private TextView title;

	private ProgressDialog dialog;
	private ProgressBar progress_bar;
	Context context = this;
	private MenuAdapter adapter;

	public static ImageView image;
	public static TextView name, roll;
	public static TextView classs;
	// private static ImageLoader imageTemp = ImageLoader.getInstance();
	// private static DisplayImageOptions options;
	public static Button showMonthWeek;
	private int mSelectedPos;

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
	SharedPreferences prefs;
	String regid;
	private boolean doubleBackToExitPressedOnce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_parent_main);
		button = (ImageButton) findViewById(R.id.button);
		title = (TextView) findViewById(R.id.title_txt);

		// mMenuTitles =
		// getResources().getStringArray(R.array.menu_list_parent);
		mMenuTags = CollegeAppUtils.GetSharedParameter(
				CollegeParentMainActivity.this, CollegeConstants.MENU_TAGS)
				.split("\\|");
		// Remove the last item from String Array
		if(mMenuTags.equals("webaccess")) {
			mMenuTags = Arrays.copyOf(mMenuTags, mMenuTags.length - 1);
		}
		mMenuTitles = CollegeAppUtils.GetSharedParameter(
				CollegeParentMainActivity.this, CollegeConstants.MENU_TITLES)
				.split("\\|");
		// Remove the last item from String Array
		if(mMenuTitles.equals("Web Access")) {
			mMenuTitles = Arrays.copyOf(mMenuTitles, mMenuTitles.length - 1);
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		showMonthWeek = (Button) findViewById(R.id.show_monthly_weekly);
		showMonthWeek.setVisibility(View.INVISIBLE);
		progress_bar = (ProgressBar) findViewById(R.id.progressBar_autoSync);
		progress_bar.setVisibility(View.INVISIBLE);
		image = (ImageView) findViewById(R.id.image_view);
		name = (TextView) findViewById(R.id.name_txt);
		roll = (TextView) findViewById(R.id.roll_txt);
		classs = (TextView) findViewById(R.id.class_txt);
		// roll.setText(CollegeAppUtils.GetSharedParameter(CollegeParentMainActivity.this,
		// "student_roll"));
		name.setText(CollegeAppUtils.GetSharedParameter(
				CollegeParentMainActivity.this, "student_name"));
		classs.setText("Program: "
				+ CollegeAppUtils.GetSharedParameter(
						CollegeParentMainActivity.this, "student_class"));
		String images = CollegeAppUtils.GetSharedParameter(
				CollegeParentMainActivity.this, "student_image");
		new CollegeLoadImageAsyncTask(context, image,
				CollegeUrls.image_url_userpic, images, false).execute();

		/*
		 * options = new DisplayImageOptions.Builder()
		 * .showImageForEmptyUri(R.drawable.no_photo)
		 * .showImageOnFail(R.drawable.no_photo)
		 * .showStubImage(R.drawable.no_photo).cacheInMemory()
		 * .cacheOnDisc().build();
		 */// setUserImage(ParentMainActivity.this);
			// setUserImage(ParentMainActivity.this);
			// set a custom shadow that overlays the main content when the
			// drawer
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
		mDrawerList.setSelection(0);
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

	}

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
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeParentProfileFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_CLASSFELLOWS)) {
			// ----------------Class Fellows------------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame, new CollegeClaassFellowsFrag())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
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
			showMonthWeek.setVisibility(View.INVISIBLE);
			if (CollegeAppUtils.GetSharedBoolParameter(
					CollegeParentMainActivity.this,
					CollegeConstants.MONTHLYWEEKLYASSIGNMENT)) {
				showMonthWeek.setText(R.string.weekly);
			} else {
				showMonthWeek.setText(R.string.monthly);
			}
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
			if (CollegeAppUtils.GetSharedBoolParameter(
					CollegeParentMainActivity.this,
					CollegeConstants.MONTHLYWEEKLYANNOUNCEMENT)) {
				showMonthWeek.setText(R.string.weekly);
			} else {
				showMonthWeek.setText(R.string.monthly);
			}
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
			if (CollegeAppUtils.GetSharedBoolParameter(
					CollegeParentMainActivity.this,
					CollegeConstants.MONTHLYWEEKLYANNOUNCEMENT)) {
				showMonthWeek.setText(R.string.weekly);
			} else {
				showMonthWeek.setText(R.string.monthly);
			}
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_BULLETINS)) {
			// --------------------Bulletins--------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeBulletinListFragment()).commit();
			/*
			showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.month_and_week_btn),
					null, null, null);
			showMonthWeek.setVisibility(View.VISIBLE);
			if (CollegeAppUtils.GetSharedBoolParameter(
					CollegeParentMainActivity.this,
					CollegeConstants.MONTHLYWEEKLYBULLETIN)) {
				showMonthWeek.setText(R.string.weekly);
			} else {
				showMonthWeek.setText(R.string.monthly);
			}*/
			showMonthWeek.setVisibility(View.GONE);

		}

		else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_DIRECTORY)) {
			// ------------------Directory----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeSchoolInfoFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		}


		else if (label.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_BLOG)) {
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
			// ------------------Feedback----------------------
			/*
			 * fragmentManager.beginTransaction() .replace(R.id.content_frame,
			 * new CollegeStudentFeedback()) .commit();
			 */
			Intent i = new Intent(getApplicationContext(),
					CollegeStudentFeedback.class);
			i.putExtra("from", "Parent");
			startActivity(i);
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_REQUESTS)) {
			// ------------------Requests----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeParentRequestFragment()).commit();
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
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_SPECIAL_LECTURE)) {
			// ------------------Special Class----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeSpecialLectureFragment(1)).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_SWAP_CHILDREN)) {
			// ------------------Swap----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeParentChildrenListFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label
				.equalsIgnoreCase(CollegeConstants.MENU_TEACHER_CAMPUS_ACTIVITY)) {
			// -----------------Sync-----------------------
			fragmentManager
					.beginTransaction()
					.replace(R.id.content_frame,
							new CollegeTeacherCampusFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
			/*
			 * startActivity(new Intent(TeacherMainActivity.this,
			 * SyncActivity.class));
			 */
		} else if (label.equalsIgnoreCase(CollegeConstants.MENU_STUDENT_SYLLABUS)) {
			// ------------------Syllabus----------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new CollegeSyllabusFragment())
					.commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.MENU_STUDENT_HOSTEL)) {
			// ------------------Hostel----------------------
			fragmentManager.beginTransaction().replace(R.id.content_frame, new CollegeHostelFragment()).commit();
			showMonthWeek.setVisibility(View.GONE);
		} else if (label.equalsIgnoreCase(Constants.EVENT)) {
			// -----------------Event-----------------------

			fragmentManager.beginTransaction().replace(R.id.content_frame, new EventFragment(1)).commit();
			showMonthWeek.setVisibility(View.GONE);
		}  else if (label.equalsIgnoreCase(Constants.MANAGE_FEES)) {
			// -----------------Manage Fee-----------------------

			fragmentManager.beginTransaction().replace(R.id.content_frame, new ManageFeesFragment(1)).commit();
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
					CollegeParentMainActivity.this);
			builder.setTitle("Log Out");
			builder.setMessage("Are you sure you want to exit?");
			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						private Context context;

						public void onClick(DialogInterface dialog, int which) {
							if (CollegeAppUtils
									.isOnline(CollegeParentMainActivity.this)) {
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										0);
								nameValuePairs.add(new BasicNameValuePair("id",
										CollegeAppUtils.GetSharedParameter(
												CollegeParentMainActivity.this,
												"id")));
								new LogoutAsyntask().execute(nameValuePairs);
							} else {
								SchoolAppUtils.SetSharedParameter(CollegeParentMainActivity.this,
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

	@Override
	protected void onResume() {
		super.onResume();
		if (CollegeAppUtils.GetSharedBoolParameter(
				CollegeParentMainActivity.this, "showannouncements")) {
			CollegeAppUtils.SetSharedBoolParameter(
					CollegeParentMainActivity.this, "showannouncements", false);
			button.setImageResource(R.drawable.menu_icon);
			adapter.showAnnouncements();
			adapter.notifyDataSetChanged();
		}
		if (CollegeAppUtils.GetSharedBoolParameter(
				CollegeParentMainActivity.this, "shownews")) {
			CollegeAppUtils.SetSharedBoolParameter(
					CollegeParentMainActivity.this, "shownews", false);
			button.setImageResource(R.drawable.menu_icon);
			adapter.showNews();
			adapter.notifyDataSetChanged();
		}
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
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
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
			//	holder.btnNotificationBubble.setVisibility(View.GONE);
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
					.equals(CollegeConstants.MENU_STUDENT_SPECIAL_LECTURE)) {
				holder.img.setImageResource(R.drawable.special_class);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position]
					.equals(CollegeConstants.MENU_STUDENT_SWAP_CHILDREN)) {
				holder.img.setImageResource(R.drawable.children);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_PARENT_TRANSPORT)) {
				holder.img.setImageResource(R.drawable.transport);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_PARENT_HOSTEL)) {
				holder.img.setImageResource(R.drawable.hostel);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(CollegeConstants.MENU_STUDENT_SYLLABUS)) {
				holder.img.setImageResource(R.drawable.syllabus);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(CollegeConstants.MENU_STUDENT_SWAP_CHILDREN)) {
				holder.img.setImageResource(R.drawable.children);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(Constants.MENU_STUDENT_HOSTEL)) {
				holder.img.setImageResource(R.drawable.hostel);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(CollegeConstants.MENU_STUDENT_SIGN_OUT)) {
				holder.img.setImageResource(R.drawable.signout);
				//holder.btnNotificationBubble.setVisibility(View.GONE);
			} else if (tags[position].equals(CollegeConstants.MENU_TEACHER_CAMPUS_ACTIVITY)) {
				holder.img.setImageResource(R.drawable.campusactivites);
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

	public static void setUserImage(Context context) {
		JSONObject c = null;
		try {
			c = new JSONObject(CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.SELECTED_CHILD_OBJECT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c != null) {
			StudentProfileInfo info = new StudentProfileInfo(c);
			name.setText(info.name);

			classs.setText("Program: " + info.class_name);

			/*
			 * new CollegeLoadImageAsyncTask(context, image,
			 * CollegeUrls.image_url_userpic, info.image, false).execute();
			 */

		}
	}

	private void registerPushNoti() {
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
			// registerBackground();
		} else {
			// updateUserGCMRegId();
		}// end else
	}

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
			SchoolAppUtils.SetSharedParameter(CollegeParentMainActivity.this,
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
