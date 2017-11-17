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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.anim.CollapseAnimation;
import com.knwedu.ourschool.anim.ExpandAnimation;
import com.knwedu.ourschool.fragments.AimsManageFeesFragment;
import com.knwedu.ourschool.fragments.AisDailyDiaryListFragment;
import com.knwedu.ourschool.fragments.AisExamScheduleFragments;
import com.knwedu.ourschool.fragments.AisHelpFragment;
import com.knwedu.ourschool.fragments.AisManageFeesFragment;
import com.knwedu.ourschool.fragments.ManageFeesFragment;
import com.knwedu.ourschool.fragments.AisSyllabus;
import com.knwedu.ourschool.fragments.AisTransportFragment;
import com.knwedu.ourschool.fragments.AnnouncementListFragment;
import com.knwedu.ourschool.fragments.AssignmentListFragment;
import com.knwedu.ourschool.fragments.AttendanceViewFragment;
import com.knwedu.ourschool.fragments.BlogListFragment;
import com.knwedu.ourschool.fragments.BulletinListFragment;
import com.knwedu.ourschool.fragments.CareerFragment;
import com.knwedu.ourschool.fragments.ClassFacilitiesListFragment;
import com.knwedu.ourschool.fragments.ClassFellowFragment;
import com.knwedu.ourschool.fragments.DailyDiaryParentFragment;
import com.knwedu.ourschool.fragments.ExamsListFragment;
import com.knwedu.ourschool.fragments.OrganizationListFragment;
import com.knwedu.ourschool.fragments.ParentChildrenListFragment;
import com.knwedu.ourschool.fragments.ParentFeedbackFragment;
import com.knwedu.ourschool.fragments.ParentProfileFragment;
import com.knwedu.ourschool.fragments.ParentRequestFragment;
import com.knwedu.ourschool.fragments.ProfileFragment;
import com.knwedu.ourschool.fragments.RequestToParentFragment;
import com.knwedu.ourschool.fragments.RightMenuFragment;
import com.knwedu.ourschool.fragments.SchoolInfoFragment;
import com.knwedu.ourschool.fragments.SpecialLectureFragment;
import com.knwedu.ourschool.fragments.StudentBehaviourListFragment;
import com.knwedu.ourschool.fragments.SubjectListFragment;
import com.knwedu.ourschool.fragments.SubjectListFragmentLessonPlan;
import com.knwedu.ourschool.fragments.SubjectListFragmentWeeklyPlan;
import com.knwedu.ourschool.fragments.CampusFragment;
import com.knwedu.ourschool.fragments.HostelFragment;
import com.knwedu.ourschool.fragments.TransportFragment;
import com.knwedu.ourschool.fragments.SchoolSyllabusFragment;
import com.knwedu.ourschool.fragments.WebAccessFragment;
import com.knwedu.com.knwedu.calendar.EventFragment;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.LoadImageAsyncTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import me.leolin.shortcutbadger.ShortcutBadger;

public class ParentMainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mMenuTags;
    private String[] mMenuTitles;
    private ImageButton button;
    private TextView title;
    private ProgressDialog dialog;
    private ProgressBar progress_bar;
    private MenuAdapter adapter;
    private static ImageView image;
    private static TextView name;
    private static TextView classs;
    // private static ImageLoader imageTemp = ImageLoader.getInstance();
    // private static DisplayImageOptions options;
    public static Button showMonthWeek;
    public static ImageButton btnAdd;
    private int mSelectedPos;
    private boolean doubleBackToExitPressedOnce = false;

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

    // Right drawer variables
    private boolean isExpanded;
    private int panelWidth;
    private LinearLayout mainlayout;
    FrameLayout.LayoutParams menuPanelParameters;
    FrameLayout.LayoutParams slidingPanelParameters;
    private FrameLayout framelayout;
    private String statusVal;
    private ImageView menuRightButton;
    private String appType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_parent_main);
        button = (ImageButton) findViewById(R.id.button);
        title = (TextView) findViewById(R.id.title_txt);
        menuRightButton = (ImageView) findViewById(R.id.image_drawer);
        //--------------premium service----------
        appType = SchoolAppUtils.GetSharedParameter(ParentMainActivity.this,
                Constants.APP_TYPE);
        if(SchoolAppUtils.GetSharedParameter(ParentMainActivity.this,
                Constants.PREMIUM_SERVICE_AVAILABLE).equalsIgnoreCase("1")){
            menuRightButton.setVisibility(View.VISIBLE);
        }else{
            menuRightButton.setVisibility(View.GONE);
        }

        if(getIntent().getExtras().getString("prompt")!=null){

            if(getIntent().getExtras().getString("prompt").equals("1")){
                popUpView();
                if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                    notifyUser();
                }

            }
        }







        //menuRightButton.setVisibility(View.VISIBLE);
        checkForPremimumService();



        // mMenuTitles =
        // getResources().getStringArray(R.array.menu_list_parent);
        mMenuTags = SchoolAppUtils.GetSharedParameter(ParentMainActivity.this,
                Constants.MENU_TAGS).split("\\|");
        mMenuTitles = SchoolAppUtils.GetSharedParameter(
                ParentMainActivity.this, Constants.MENU_TITLES).split("\\|");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        showMonthWeek = (Button) findViewById(R.id.show_monthly_weekly);
        showMonthWeek.setVisibility(View.INVISIBLE);

        btnAdd = (ImageButton) findViewById(R.id.add_assignment);
        btnAdd.setVisibility(View.GONE);

        progress_bar = (ProgressBar) findViewById(R.id.progressBar_autoSync);
        progress_bar.setVisibility(View.INVISIBLE);

        image = (ImageView) findViewById(R.id.image_view);
        name = (TextView) findViewById(R.id.name_txt);
        classs = (TextView) findViewById(R.id.class_txt);

        setUserImage(ParentMainActivity.this);
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
            Bundle extras1 = getIntent().getExtras();
            if (null != extras1) {
                selectItem(extras1.getInt("menu_val"));
            } else {
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

        /**
         * Right menu panel
         */
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        panelWidth = (int) ((metrics.widthPixels) * -0.75);
        mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
        framelayout = (FrameLayout) findViewById(R.id.menuPanel);
        slidingPanelParameters = (FrameLayout.LayoutParams) mainlayout
                .getLayoutParams();
        slidingPanelParameters.width = metrics.widthPixels;
        mainlayout.setLayoutParams(slidingPanelParameters);

        menuRightButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isExpanded) {
                    isExpanded = true;
                    // Expand
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.menuPanel, new RightMenuFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    new ExpandAnimation(mainlayout, panelWidth,
                            Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, -0.75f, 0, 0.0f, 0,
                            0.0f);
                } else {
                    isExpanded = false;
                    // Collapse
                    mainlayout.setBackgroundResource(R.drawable.profile_bg);
                    new CollapseAnimation(mainlayout, panelWidth,
                            TranslateAnimation.RELATIVE_TO_SELF, -0.75f,
                            TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f,
                            0, 0.0f);
                }
            }
        });
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        String label = mMenuTags[position];
        findViewById(R.id.graph_btn).setVisibility(View.GONE);

        if (label.equalsIgnoreCase(Constants.MENU_PARENT_CHILD_PROFILE)) {
            // ----------------Child Profile------------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ProfileFragment())
                    .commit();
//			fragmentManager.beginTransaction()
//					.replace(R.id.content_frame, new ChartFragment())
//					.commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_ACADEMICS)) {
            // -----------------Academics-----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new SubjectListFragment())
                    .commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_CLASS_FELLOW)) {
            // ----------------Class Fellow------------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ClassFellowFragment())
                    .commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_TEACHER_WEEKLY_PLAN)) {
            // -----------------Weekly plan-----------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            new SubjectListFragmentWeeklyPlan()).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_LESSON_PLAN)) {
            // -------------------Lesson Plan---------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            new SubjectListFragmentLessonPlan()).commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_TIME_TABLE)) {
            // ------------------Time Table----------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            new ClassFacilitiesListFragment()).commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_BEHAVIOUR)) {
            // -------------------Organization---------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            new StudentBehaviourListFragment()).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_ASSIGNMENT)) {
            // -----------------Assignment-----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AssignmentListFragment(1))
                    .commit();
            showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.month_and_week_btn),
                    null, null, null);

            showMonthWeek.setVisibility(View.VISIBLE);
            showMonthWeek.setText(R.string.all);
            btnAdd.setVisibility(View.GONE);

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_QUIZ)) {
            // -----------------Quiz-----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AssignmentListFragment(2))
                    .commit();
            showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.month_and_week_btn),
                    null, null, null);

            showMonthWeek.setVisibility(View.VISIBLE);
            showMonthWeek.setText(R.string.all);
            btnAdd.setVisibility(View.GONE);

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_ACTIVITY)) {
            // -------------------Activity---------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AssignmentListFragment(3))
                    .commit();
            showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.month_and_week_btn),
                    null, null, null);

            showMonthWeek.setVisibility(View.VISIBLE);
            showMonthWeek.setText(R.string.all);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_EXAMS)) {
            // -----------------Exam-----------------------
            if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new AisExamScheduleFragments())
                        .commit();
            }else {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new ExamsListFragment())
                        .commit();
            }

            showMonthWeek.setVisibility(View.INVISIBLE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_COURSEWORK)) {
            // ------------------Course work----------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            new AnnouncementListFragment(1)).commit();

            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_ANNOUNCEMENT)) {
            // ----------------Announcement------------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            new AnnouncementListFragment(2)).commit();

            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_BULLETINS)) {
            // -----------------Bulletins-----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new BulletinListFragment())
                    .commit();

            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_ORGANIZATION)) {
            // ----------------Organization------------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, new OrganizationListFragment())
                    .commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_ATTENDANCE_INFO)) {
            // ----------------Attendance Info------------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AttendanceViewFragment())
                    .commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_DIRECTORY)) {
            // ----------------Directory------------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new SchoolInfoFragment())
                    .commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_REQUEST)) {
            // ------------------Requests----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ParentRequestFragment())
                    .commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_CHILDREN)) {
            // -----------------Children-----------------------
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame,
                            new ParentChildrenListFragment()).commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_MY_PROFILE)) {
            // ------------------My Profile----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ParentProfileFragment())
                    .commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_BLOG)) {
            // ------------------Blog----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new BlogListFragment())
                    .commit();

        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_SPECIAL_CLASS)) {
            // ------------------Special Class----------------------
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new SpecialLectureFragment())
                    .commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_WEB_ACCESS)) {
            // ----------------Web Access------------------------
            fragmentManager.beginTransaction().replace(R.id.content_frame, new WebAccessFragment(0)).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_TRANSPORT)) {
            // ------------------Transport----------------------
            if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {

                fragmentManager.beginTransaction().replace(R.id.content_frame, new AisTransportFragment()).commit();
            }else{
                if(SchoolAppUtils.GetSharedParameter(
                        ParentMainActivity.this, Constants.NEWTRANSPORT_TYPE).equals("Y")){
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new AisTransportFragment()).commit();
                }else{
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new TransportFragment(1)).commit();
                }
            }
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_HOSTEL)) {
            // ------------------Hostel----------------------
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HostelFragment(1)).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.SYLLABUS)) {
            // --------------------Syllabus--------------------
            if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AisSyllabus()).commit();
            }else{

                fragmentManager.beginTransaction().replace(R.id.content_frame, new SchoolSyllabusFragment(1)).commit();
            }
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.EVENT)) {
            // --------------------Event--------------------
            fragmentManager.beginTransaction().replace(R.id.content_frame, new EventFragment(0)).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MANAGE_FEES)) {
            // --------------------Manage Fess--------------------
            //
            if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AimsManageFeesFragment(1)).commit();
            }else{
                //fragmentManager.beginTransaction().replace(R.id.content_frame, new ManageFeesFragment(1)).commit();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AimsManageFeesFragment(1)).commit();
            }
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.CAMPUS)) {
            // --------------------Campus--------------------
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CampusFragment()).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_CAREER)) {
            // ----------------Career------------------------
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CareerFragment(1)).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        }
        else if(label.equals(Constants.MENU_PARENT_HELP)){

            fragmentManager.beginTransaction().replace(R.id.content_frame, new AisHelpFragment()).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        }
        else if (label.equalsIgnoreCase(Constants.MENU_REQUEST_TO_PARENT)) {
            // ----------------Request Parent------------------------
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RequestToParentFragment()).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_FEEDBACK)) {
            // ----------------Parent Feedback------------------------
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ParentFeedbackFragment()).commit();
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_DAILY_DIARY)) {
            // -------------------Daily Diary---------------------
            if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new AisDailyDiaryListFragment())
                        .commit();

            }else {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new DailyDiaryParentFragment())
                        .commit();
            }
            showMonthWeek.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else if (label.equalsIgnoreCase(Constants.MENU_PARENT_SIGN_OUT)) {
            // ------------------Sign Out----------------------

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ParentMainActivity.this);
            builder.setTitle("Sign Out");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (SchoolAppUtils
                                    .isOnline(ParentMainActivity.this)) {
                                SchoolAppUtils.SetSharedBoolParameter(
                                        ParentMainActivity.this,
                                        Constants.ISPARENTSIGNIN, false);
                                SchoolAppUtils.SetSharedBoolParameter(
                                        ParentMainActivity.this,
                                        Constants.UISSIGNIN, false);

                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                        3);
                                nameValuePairs.add(new BasicNameValuePair(
                                        "user_type_id",
                                        SchoolAppUtils.GetSharedParameter(
                                                ParentMainActivity.this,
                                                Constants.USERTYPEID)));
                                nameValuePairs.add(new BasicNameValuePair(
                                        "organization_id",
                                        SchoolAppUtils.GetSharedParameter(
                                                ParentMainActivity.this,
                                                Constants.UORGANIZATIONID)));
                                nameValuePairs.add(new BasicNameValuePair(
                                        "user_id",
                                        SchoolAppUtils.GetSharedParameter(
                                                ParentMainActivity.this,
                                                Constants.USERID)));
                                new Signout().execute(nameValuePairs);
                            } else {
                                SchoolAppUtils.SetSharedParameter(
                                        ParentMainActivity.this,
                                        Constants.USERID, "0");
                                finish();
                                startActivity(new Intent(
                                        ParentMainActivity.this,
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
        if (SchoolAppUtils.GetSharedBoolParameter(ParentMainActivity.this,
                "showannouncements")) {
            SchoolAppUtils.SetSharedBoolParameter(ParentMainActivity.this,
                    "showannouncements", false);
            button.setImageResource(R.drawable.menu_icon);
            adapter.showAnnouncements();
            adapter.notifyDataSetChanged();
        }
        if (SchoolAppUtils.GetSharedBoolParameter(ParentMainActivity.this,
                "shownews")) {
            SchoolAppUtils.SetSharedBoolParameter(ParentMainActivity.this,
                    "shownews", false);
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
            if (tags[position].equals(Constants.MENU_PARENT_CHILD_PROFILE)) {
                holder.img.setImageResource(R.drawable.childprofile);
              //  holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_ACADEMICS)) {
                holder.img.setImageResource(R.drawable.academics);
              //  holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position]
                    .equals(Constants.MENU_PARENT_CLASS_FELLOW)) {
                holder.img.setImageResource(R.drawable.classfellow);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_LESSON_PLAN)) {
                holder.img.setImageResource(R.drawable.lessonplan);
              //  holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_TIME_TABLE)) {
                holder.img.setImageResource(R.drawable.timetable);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_ASSIGNMENT)) {
                holder.img.setImageResource(R.drawable.assignmentstests);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_QUIZ)) {
                holder.img.setImageResource(R.drawable.quiz);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position]
                    .equals(Constants.MENU_TEACHER_WEEKLY_PLAN)) {
                holder.img.setImageResource(R.drawable.weekly_plan_icon);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if(tags[position].equals(Constants.MENU_PARENT_HELP)){
                holder.img.setImageResource(R.drawable.help_icon);
            }


            else if (tags[position].equals(Constants.MENU_PARENT_ACTIVITY)) {
                holder.img.setImageResource(R.drawable.activity);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_EXAMS)) {
                holder.img.setImageResource(R.drawable.exam);
                //holder.btnNotificationBubble.setVisibility(View.GONE);
                if (showAnnouncements) {
                    holder.alert.setVisibility(View.VISIBLE);
                } else {
                    holder.alert.setVisibility(View.GONE);
                }
            } else if (tags[position].equals(Constants.MENU_PARENT_COURSEWORK)) {
                holder.img.setImageResource(R.drawable.coursework);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
                if (showNews) {
                    holder.alert.setVisibility(View.VISIBLE);
                } else {
                    holder.alert.setVisibility(View.GONE);
                }
            } else if (tags[position]
                    .equals(Constants.MENU_PARENT_ANNOUNCEMENT)) {
                holder.img.setImageResource(R.drawable.classannouncements);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
                if (showNews) {
                    holder.alert.setVisibility(View.VISIBLE);
                } else {
                    holder.alert.setVisibility(View.GONE);
                }
            } else if (tags[position].equals(Constants.MENU_PARENT_BULLETINS)) {
                holder.img.setImageResource(R.drawable.bulletins);
             //   holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position]
                    .equals(Constants.MENU_PARENT_ORGANIZATION)) {
                holder.img.setImageResource(R.drawable.organization);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position]
                    .equals(Constants.MENU_PARENT_ATTENDANCE_INFO)) {
                holder.img.setImageResource(R.drawable.attendanceinfo);
                //holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_DIRECTORY)) {
                holder.img.setImageResource(R.drawable.directory);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_REQUEST)) {
                holder.img.setImageResource(R.drawable.request);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            }

            if (tags[position].equals(Constants.MENU_PARENT_CHILDREN)) {
                holder.img.setImageResource(R.drawable.children);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_MY_PROFILE)) {
                holder.img.setImageResource(R.drawable.profile);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_BLOG)) {
                holder.img.setImageResource(R.drawable.blog);
                //holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_BEHAVIOUR)) {
                holder.img.setImageResource(R.drawable.behavior);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_TRANSPORT)) {
                holder.img.setImageResource(R.drawable.transport);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_HOSTEL)) {
                holder.img.setImageResource(R.drawable.hostel);
              //  holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_SPECIAL_CLASS)) {
                holder.img.setImageResource(R.drawable.special_class);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.SYLLABUS)) {
                holder.img.setImageResource(R.drawable.syllabus);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.EVENT)) {
                holder.img.setImageResource(R.drawable.evencalender);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_SIGN_OUT)) {
                holder.img.setImageResource(R.drawable.signout);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.CAMPUS)) {
                holder.img.setImageResource(R.drawable.campus);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_WEB_ACCESS)) {
                holder.img.setImageResource(R.drawable.webaccess);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.EVENT)) {
                holder.img.setImageResource(R.drawable.evencalender);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MANAGE_FEES)) {
                holder.img.setImageResource(R.drawable.fees);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position]
                    .equals(Constants.MENU_PARENT_SPECIAL_CLASS)) {
                holder.img.setImageResource(R.drawable.special_class);
                //holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_SIGN_OUT)) {
                holder.img.setImageResource(R.drawable.signout);
                //holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_CAREER)) {
                holder.img.setImageResource(R.drawable.career);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_REQUEST_TO_PARENT)) {
                holder.img.setImageResource(R.drawable.requestparent);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
            } else if (tags[position].equals(Constants.MENU_PARENT_FEEDBACK)) {
                holder.img.setImageResource(R.drawable.feedback);
               // holder.btnNotificationBubble.setVisibility(View.GONE);
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

    public static void setUserImage(Context context) {
        JSONObject c = null;
        try {
            c = new JSONObject(SchoolAppUtils.GetSharedParameter(context,
                    Constants.SELECTED_CHILD_OBJECT));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c != null) {
            StudentProfileInfo info = new StudentProfileInfo(c);
			/*
			 * if (SchoolAppUtils.GetSharedParameter(context,
			 * Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)) {
			 * name.setText("Class: " + info.classs); classs.setText("Section: "
			 * + info.section); image.setVisibility(View.INVISIBLE); }else{
			 */
            name.setText(info.fullname);

            classs.setText("Class: " + info.class_section);
            new LoadImageAsyncTask(context, image, Urls.image_url_userpic,
                    info.image, false).execute();
            // }

        }
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

    class Signout extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ParentMainActivity.this);
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
                Toast.makeText(ParentMainActivity.this, error,
                        Toast.LENGTH_LONG).show();
                SchoolAppUtils.SetSharedParameter(ParentMainActivity.this,
                        Constants.USERID, "0");
                finish();
                startActivity(new Intent(ParentMainActivity.this,
                        SigninActivity.class));
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(ParentMainActivity.this,
                            "Sign Out", error);
                } else {
                    SchoolAppUtils
                            .showDialog(
                                    ParentMainActivity.this,
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

    public void checkForPremimumService() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(ParentMainActivity.this, Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("student_id",
                SchoolAppUtils.GetSharedParameter(ParentMainActivity.this, Constants.CHILD_ID)));

        new RequestPremiumServiceStatusAsyntask().execute(nameValuePairs);
    }

    private class RequestPremiumServiceStatusAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_premium_service_status_check);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_premium_service_status_check);
            JSONArray dataArray = null;
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        try {
                            dataArray = json.getJSONArray("data");
                        } catch (Exception e) {
                            return true;
                        }
                        Log.d("Hellooo", dataArray.getJSONObject(0).getString("purchase_status").trim());
                        for (int i = 0; i < dataArray.length(); i++) {
                            statusVal = dataArray.getJSONObject(i).getString("purchase_status").trim();
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
                    error = getResources().getString(R.string.unknown_response);
                }
            } catch (
                    JSONException e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                //Toast.makeText(ParentMainActivity.this,statusVal,Toast.LENGTH_LONG).show();

                if (statusVal.equalsIgnoreCase("0")) {
                    menuRightButton.setImageResource(R.drawable.swap);
                } else {
                    menuRightButton.setImageResource(R.drawable.graph_button_yes);

                }
            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(ParentMainActivity.this, ParentMainActivity.this
                            .getTitle().toString(), error);
                } else {
                    SchoolAppUtils.showDialog(ParentMainActivity.this, ParentMainActivity.this
                                    .getTitle().toString(),
                            getResources().getString(R.string.error));
                }

            }
        }
    }


    public void popUpView(){

        LayoutInflater layoutInflater = LayoutInflater.from(ParentMainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.message_prompt_layout, null);

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ParentMainActivity.this,R.style.DialogTheme);
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

    /*
    public void notifyUser() {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Transport")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Want to track your child’s transport. Kindly subscribe from ‘Transport’ section"))
                .setContentText("Want to track your child’s transport. Kindly subscribe from ‘Transport’ section")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
    */

    public void notifyUser() {
        Intent notificationIntent = null;
        //Remove Badge
        ShortcutBadger.removeCount(this);

        //Log.d("test 1",notifications);

        if (SchoolAppUtils.GetSharedBoolParameter(this, Constants.UISSIGNIN) == true) {
            notificationIntent = new Intent(this, SplashActivity.class);
        } else if (SchoolAppUtils.GetSharedBoolParameter(this,
                Constants.ISPARENTSIGNIN) == true) {
            notificationIntent = new Intent(this, SplashActivity.class);
        } else if (SchoolAppUtils.GetSharedBoolParameter(this,
                Constants.ISSIGNIN) == true) {
            notificationIntent = new Intent(this, SplashActivity.class);
        } else {
            notificationIntent = new Intent(this, SplashActivity.class);
        }
        try {

            int icon = R.drawable.app_icon;
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.drawable.app_icon,
                    "Want to track your child’s transport. Kindly subscribe from ‘Transport’ section\"", when);

            NotificationManager mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            RemoteViews contentView = new RemoteViews(this.getPackageName(),
                    R.layout.custom_notification);
            contentView.setImageViewResource(R.id.image, R.drawable.app_icon);
			/*contentView.setTextViewText(R.id.title, SchoolAppUtils
					.GetSharedParameter(context, Constants.PUSH_TITLE));
			*/
            contentView.setTextViewText(R.id.title, "Transport");
            contentView.setTextViewText(R.id.text, "Want to track your child’s transport. Kindly subscribe from ‘Transport’ section\"");
            //contentView.setTextViewText(R.id.notification,notifications);
            //contentView.setTextViewText(R.id.count.msg_count);
            notification.contentView = contentView;

            // Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.contentIntent = contentIntent;

            notification.flags |= Notification.FLAG_AUTO_CANCEL; // Do not clear
            // the
            // notification
            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            notification.defaults |= Notification.DEFAULT_VIBRATE; // Vibration
            notification.defaults |= Notification.DEFAULT_SOUND; // Sound

            notification.number = 0;

            int notification_id = (int) System.currentTimeMillis();
            mNotificationManager.notify(notification_id, notification);

        } catch (Exception e) {
            // TODO: handle exception
        }


    }


    }
