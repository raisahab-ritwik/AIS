package com.knwedu.ourschool;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.CollegeTeacherMainActivity;
import com.knwedu.college.adapter.CollegeListUserNameAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.model.InstituteData;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.OrgSet;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddasgupta on 1/11/2017.
 */

public class AisSignInActivity extends Activity {

    private EditText username, password;
    private TextView forgotPassword, resetOrganization, txt_signup, txtPrivacyPolicy;
    private AutoCompleteTextView edt_subdomain;
    private Button signIn, signUp, next, clear_institute;
    private ProgressDialog dialog;
    private ImageView image;
    private List<InstituteData> orgList = new ArrayList<InstituteData>();
    private DatabaseAdapter mDatabase;
    private ArrayList<DataStructureFramwork.TimeTable> timeTable;

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

    private String curVersion = "0";
    private final int REQUEST_SIGNUP = 200;

    private ArrayList<CollegeDataStructureFramwork.UserInfo> userInfoList;
    private ArrayList<CollegeDataStructureFramwork.userType> userTypeList;
    private Context context = this;

    private int menu_val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ais_signin);
        initialize_view();
        mDatabase = ((SchoolApplication) getApplication()).getDatabase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerPushNotification();
        Log.d("ORGANIZATION", SchoolAppUtils.GetSharedParameter(
                AisSignInActivity.this, Constants.UORGANIZATIONID));
        //initialize();

        String test = SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                Constants.UORGANIZATIONID);
        if (SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                Constants.UORGANIZATIONID).equals("0")) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    1);
            nameValuePairs.add(new BasicNameValuePair(
                    "type", "all"));
            new SubdomainAsync().execute(nameValuePairs);
            disable_enable_view("1", "0", "0", "0", "0", "0", "0", "0");
        }else {
            String app_type = SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                    Constants.APP_TYPE);

            disable_enable_view("0","1","1","1","0","1","0","1");

            String logo_url = SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                    Constants.COMMON_URL)+"/images/"+SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                    Constants.AIS_LOGO);
            edt_subdomain.setText("");

            if(app_type.equals("3")){
                signUp.setVisibility(View.VISIBLE);
            }else{
                signUp.setVisibility(View.GONE);
            }
            Picasso.with( AisSignInActivity.this )
                    .load( logo_url )
                    .error( R.drawable.app_icon)
                    .placeholder( R.drawable.progress_animation)
                    .into( image);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                        Constants.UORGANIZATIONID).equals("0")) {
                    Toast.makeText(AisSignInActivity.this,
                            R.string.please_select_institution,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                clear_institute.setVisibility(View.GONE);
                String app_type = SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                        Constants.APP_TYPE);
                disable_enable_view("0","1","1","1","0","1","0","1");
                String logo_url = SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                        Constants.COMMON_URL)+"/images/"+SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                        Constants.AIS_LOGO);
                if(app_type.equals("3")){
                    signUp.setVisibility(View.VISIBLE);
                }else{
                    signUp.setVisibility(View.GONE);
                }
                Picasso.with( AisSignInActivity.this )
                        .load( logo_url )
                        .error( R.drawable.app_icon)
                        .placeholder( R.drawable.progress_animation)
                        .into( image);

            }
        });

        resetOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with( AisSignInActivity.this )
                        .load( R.drawable.login_logo )
                        .error( R.drawable.app_icon)
                        .placeholder( R.drawable.progress_animation)
                        .into( image);
                edt_subdomain.setText("");
                clear_institute.setVisibility(View.GONE);
                disable_enable_view("1","0","0","0","0","0","0","0");
                if(orgList.size() == 0){
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                            1);
                    nameValuePairs.add(new BasicNameValuePair(
                            "type", "all"));
                    new SubdomainAsync().execute(nameValuePairs);

                }
            }
        });

        clear_institute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_subdomain.setText("");
                clear_institute.setVisibility(View.GONE);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(AisSignInActivity.this,
                        ForgotPasswordActivity.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                if (username.getText().toString().length() <= 0) {
                    Toast.makeText(AisSignInActivity.this,
                            R.string.enter_your_username, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (password.getText().toString().length() <= 0) {
                    Toast.makeText(AisSignInActivity.this,
                            R.string.enter_your_password, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                new retriveToken().execute();

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(AisSignInActivity.this,
                        RegistrationActivity.class);
                intent.putExtra("devicetoken", regid);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
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
        EasyTracker.getInstance(this).activityStart(this);
    }

    @SuppressWarnings("unchecked")
    private void registerPushNotification() {
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
                }

                ;

            }.execute(null, null, null);
        }

        Log.e("devicetoken", "" + regid);
    }

    void initialize_view(){
        image = (ImageView) findViewById(R.id.image);
        username = (EditText) findViewById(R.id.username_edittxt);
        password = (EditText) findViewById(R.id.password_edittxt);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        txtPrivacyPolicy = (TextView) findViewById(R.id.privacy_policy);
        resetOrganization = (TextView) findViewById(R.id.reset_org);
        signIn = (Button) findViewById(R.id.signin_btn);
        signUp = (Button) findViewById(R.id.signup_btn);
        next = (Button) findViewById(R.id.next_btn);
        clear_institute = (Button) findViewById(R.id.calc_clear_txt_Prise);
        edt_subdomain = (AutoCompleteTextView) findViewById(R.id.domain_edittxt);
    }

    // All Institution names
    private class SubdomainAsync extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orgList.clear();
            dialog = new ProgressDialog(AisSignInActivity.this);
            //dialog.setTitle(getResources().getString(
                    //R.string.fetch_institutions));
           // dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(namevaluepair,
                    Urls.url_ais_institute_list);
            Log.d("nvp",namevaluepair.toString());
            if (json != null) {
                try {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = null;

                        try {
                            array = json.getJSONArray("data");
                            Log.d("array alies",array.toString());
                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                try {

                                    JSONObject obj = array.getJSONObject(i);
                                    InstituteData orgSet = new InstituteData();
                                    orgSet.setBase_url(obj
                                            .getString("base_url"));
                                    orgSet.setOrganizaion_id(obj
                                            .getString("organizaion_id"));
                                    orgSet.setOrganization_name(obj.getString("organization_name"));
                                    orgSet.setApp_logo(obj
                                            .getString("app_logo"));
                                    orgSet.setType(obj
                                            .getString("type"));
                                    orgSet.setApp_type(obj
                                            .getString("app_type"));


                                    orgList.add(orgSet);

                                } catch (Exception e) {

                                }

                            }
                        }

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
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }

            final ArrayList<String> mList = new ArrayList<String>();
            for (int i = 0; i < orgList.size(); i++) {
                mList.add(orgList.get(i).getOrganization_name());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    AisSignInActivity.this,
                    R.layout.simple_spinner_dropdown_item_custom_new, mList);
            // adapter.setDropDownViewResource(R.layout.simple_drop_down_item_line_custom);
            edt_subdomain.setAdapter(adapter);
            edt_subdomain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    next.setVisibility(View.VISIBLE);
                    int index = mList.indexOf(edt_subdomain.getText()
                            .toString());
                    Log.d("institutionssss", orgList.get(index).getOrganization_name() + "\n"
                            + orgList.get(index).getBase_url());

                    Log.d("institutionssss","hello");

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_subdomain.getWindowToken(),
                            0);

                    InstituteData m = orgList.get(index);

                    SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.COMMON_URL, m.getBase_url());
                    SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.UORGANIZATIONID, m.getOrganizaion_id());
                    SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.INSTITUTION_TYPE, m.getType());
                    SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.INSTITUTION_NAME, m.getOrganization_name());
                    SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.APP_TYPE, m.getApp_type());

                    SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.AIS_LOGO, m.getApp_logo());
                    /*SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.PASSWORD_PROTECTION,
                            m.getPassword_protection());*/

                    SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                            Constants.PUSH_TITLE, m.getOrganization_name());
                    new Urls(m.getBase_url());
                    CollegeUrls.base_url = SchoolAppUtils.GetSharedParameter(
                            AisSignInActivity.this, Constants.COMMON_URL);
                   // new DownloadFile().execute(m.getThumbnailUrl(),
                     //       m.getFooterUrl());
                }
            });
            edt_subdomain.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                    // TODO Auto-generated method stub

                    if (arg0.toString().isEmpty()) {
                        //domain_view.setText("");
                        SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                                Constants.UORGANIZATIONID, "0");
                        SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                                Constants.INSTITUTION_NAME, "School Name");
                        SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                                Constants.APP_TYPE,
                                Constants.APP_TYPE_EXCLUSIVE);
                        /*SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                                Constants.PASSWORD_PROTECTION, "0");

                        image.setImageResource(R.drawable.login_logo);

                        txt_signup.setVisibility(View.GONE);
                        signUp.setVisibility(View.GONE);*/
                        clear_institute.setVisibility(View.GONE);
                    }
                    else{
                        clear_institute.setVisibility(View.VISIBLE);

                    }
                }

            });

        }

    }

    void disable_enable_view(String subdomain,String user_name, String pass,
                             String sign_in, String sign_up, String forgot_password, String text_next, String reset_org ){
        if(subdomain.equals("1"))
            edt_subdomain.setVisibility(View.VISIBLE);
        else
            edt_subdomain.setVisibility(View.GONE);
        if(text_next.equals("1"))
            next.setVisibility(View.VISIBLE);
        else
            next.setVisibility(View.INVISIBLE);

        if(user_name.equals("1"))
            username.setVisibility(View.VISIBLE);
        else
            username.setVisibility(View.INVISIBLE);

        if(pass.equals("1"))
            password.setVisibility(View.VISIBLE);
        else
            password.setVisibility(View.INVISIBLE);

        if(sign_in.equals("1"))
            signIn.setVisibility(View.VISIBLE);
        else
            signIn.setVisibility(View.INVISIBLE);

        if(sign_up.equals("1"))
            signUp.setVisibility(View.VISIBLE);
        else
            signUp.setVisibility(View.INVISIBLE);

        if(forgot_password.equals("1"))
            forgotPassword.setVisibility(View.VISIBLE);
        else
            forgotPassword.setVisibility(View.INVISIBLE);



        if(reset_org.equals("1"))
            resetOrganization.setVisibility(View.VISIBLE);
        else
            resetOrganization.setVisibility(View.INVISIBLE);
    }


    private class retriveToken extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AisSignInActivity.this);
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
                    curVersion = getApplicationContext()
                            .getPackageManager()
                            .getPackageInfo(getApplicationInfo().packageName, 0).versionName;
                    Log.d("CURRENT VERSION....", curVersion);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        4);
                nameValuePairs.add(new BasicNameValuePair("email", username
                        .getText().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("username", username
                        .getText().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("password", password
                        .getText().toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("devicetype",
                        "android"));
                nameValuePairs.add(new BasicNameValuePair("organization_id",
                        SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                                Constants.UORGANIZATIONID)));
                nameValuePairs
                        .add(new BasicNameValuePair("devicetoken", regid));
                nameValuePairs.add(new BasicNameValuePair("version_number",
                        curVersion));

                new SignInAsyntask().execute(nameValuePairs);
            } else {

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                SchoolAppUtils.showDialog(AisSignInActivity.this, getResources()
                        .getString(R.string.unknown_response), getResources()
                        .getString(R.string.please_check_internet_connection));
            }
        }
    }


    private class SignInAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        DataStructureFramwork.UserInfo userInfo;

        String error = "";
        DataStructureFramwork.Permission permission;
        DataStructureFramwork.PermissionAdd permissionadd;
        DataStructureFramwork.Request requests;
        CollegeDataStructureFramwork.CollegePermission per;

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            Log.d("institution_type", SchoolAppUtils.GetSharedParameter(
                    AisSignInActivity.this, Constants.INSTITUTION_TYPE));

            if (SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                    Constants.INSTITUTION_TYPE).equals(
                    Constants.INSTITUTION_TYPE_SCHOOL)) {
                List<NameValuePair> nameValuePairs = params[0];
                JsonParser jParser = new JsonParser(AisSignInActivity.this);
                JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
                        Urls.api_login);

                // Log parameters:
                Log.d("url extension: ", Urls.api_login);
                String parameters = "";
                for (NameValuePair nvp : nameValuePairs) {
                    parameters += nvp.getName() + "=" + nvp.getValue() + ",";
                }
                Log.d("Parameters: ", parameters);

                try {

                    if (json != null) {
                        if (json.getString("result").equalsIgnoreCase("1")) {
                            JSONObject object = json.getJSONObject("data");
                            String menu_tag = json.getString("menu_info") + "|webaccess";
                            String menu_title = json.getString("menu_caption") + "|Web Access";

                            //notification counter
                            // String menu_Noticounter=json.getString("menu_counter")+"|web access";
                            JSONObject permission_config = json
                                    .getJSONObject("activity_permission");
                            JSONObject request_config = json
                                    .getJSONObject("requests");
                            JSONObject permission_add = json
                                    .getJSONObject("permission");


                            JSONObject orgConfigJSONObj = json.getJSONObject("org_config");
                            //if(orgConfigJSONObj != null){
                            if(orgConfigJSONObj != null){
                                //--------------------------------INSURANCE and premium--------------//
                                try {
                                    String isInsuranceAvailable = orgConfigJSONObj.getString("is_insurance").trim();
                                    String isPremiumServiceAvailable = orgConfigJSONObj.getString("is_premium_service").trim();
                                    SchoolAppUtils.SetSharedParameter(
                                            AisSignInActivity.this,
                                            Constants.INSURANCE_AVIALABLE,
                                            isInsuranceAvailable);

                                    SchoolAppUtils.SetSharedParameter(
                                            AisSignInActivity.this,
                                            Constants.PREMIUM_SERVICE_AVAILABLE,
                                            isPremiumServiceAvailable);

                                }catch (Exception e1){
                                    e1.printStackTrace();
                                }
                            }


                            if (object != null) {
                                // user details
                                userInfo = new DataStructureFramwork.UserInfo(object);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.USERTYPEID,
                                        userInfo.usertypeid);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this, Constants.USERID,
                                        userInfo.userid);
                                SchoolAppUtils
                                        .SetSharedParameter(
                                                AisSignInActivity.this,
                                                Constants.UALTEMAIL,
                                                userInfo.alt_email);

                                SchoolAppUtils
                                        .SetSharedParameter(AisSignInActivity.this,
                                                Constants.ORGEMAIL,
                                                userInfo.org_email);

                                SchoolAppUtils
                                        .SetSharedParameter(
                                                AisSignInActivity.this,
                                                Constants.UMOBILENO,
                                                userInfo.mobile_no);
                                SchoolAppUtils.SetSharedParameter(AisSignInActivity.this, Constants.PASSWORD,password.getText().toString().trim());

                                SchoolAppUtils
                                        .SetSharedParameter(
                                                AisSignInActivity.this,
                                                Constants.DEVICE_TOKEN,
                                                regid);
                            }
                            // Menu tag
                            if (menu_tag != null) {
                                //Log.d("menu", menu_tag.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.MENU_TAGS,
                                        menu_tag.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.MENU_TITLES,
                                        menu_title.toString());
                            }

                            // Permissions
                            if (permission_config != null) {
                                permission = new DataStructureFramwork.Permission(permission_config);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.ASSIGNMENT_MARK_PERMISSION,
                                        permission.assignment_permission
                                                .toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.TEST_MARK_PERMISSION,
                                        permission.test_permission.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.ACTIVITY_MARK_PERMISSION,
                                        permission.activity_permission
                                                .toString());
                            }

                            // Parent Request
                            if (request_config != null) {
                                requests = new DataStructureFramwork.Request(request_config);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.LEAVE_REQUEST,
                                        requests.leave_request.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.REQUEST_FOR_BOOK,
                                        requests.request_for_book.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.REQUEST_FOR_ID_CARD,
                                        requests.request_for_id.toString());
                                SchoolAppUtils
                                        .SetSharedParameter(
                                                AisSignInActivity.this,
                                                Constants.REQUEST_FOR_UNIFORM,
                                                requests.request_for_uniform
                                                        .toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.SPECIAL_REQUEST,
                                        requests.special_request.toString());
                            }

                            // Permission Add
                            if (permission_add != null) {
                                permissionadd = new DataStructureFramwork.PermissionAdd(
                                        permission_add);
                                SchoolAppUtils
                                        .SetSharedParameter(
                                                AisSignInActivity.this,
                                                Constants.ASSIGNMENT_ADD_PERMISSION,
                                                permissionadd.assignment_add
                                                        .toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.TEST_ADD_PERMISSION,
                                        permissionadd.test_add.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.ACTIVITY_ADD_PERMISSION,
                                        permissionadd.activity_add.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.ANNOUNCEMENT_ADD_PERMISSION,
                                        permissionadd.announcement_add
                                                .toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.COURSEWORK_ADD_PERMISSION,
                                        permissionadd.classwork_add.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.LESSONS_ADD_PERMISSION,
                                        permissionadd.lessons_add.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.ATTENDANCE_TYPE_PERMISSION,
                                        permissionadd.attendance_type
                                                .toString());
                                Log.d("assignment",
                                        permissionadd.attendance_type
                                                .toString());
                            }

                            if (userInfo.usertypeid
                                    .equalsIgnoreCase(Constants.USERTYPE_PARENT)) {
                                JSONArray child_info_arry = json
                                        .getJSONArray("child_info");
                                JSONObject object_chield_info = child_info_arry
                                        .getJSONObject(0);

                                DataStructureFramwork.StudentProfileInfo selectedStudent = new DataStructureFramwork.StudentProfileInfo(
                                        object_chield_info);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.CHILD_ID,
                                        selectedStudent.user_id);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.CHILD_NAME,
                                        selectedStudent.fullname);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.STUDENT_CLASS_SECTION,
                                        selectedStudent.class_section);
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.SELECTED_CHILD_OBJECT,
                                        object_chield_info.toString());
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.SECTION_ID,
                                        selectedStudent.section_id);

                            } else if (userInfo.usertypeid
                                    .equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {
                                SchoolAppUtils.SetSharedParameter(
                                        AisSignInActivity.this,
                                        Constants.CHILD_ID, userInfo.userid);

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
                        error = getResources().getString(
                                R.string.unknown_response);
                    }

                } catch (JSONException e) {

                }
                return false;
            }

            // ---------------------College------------------------------
            else {
                List<NameValuePair> nameValuePairs = params[0];
                CollegeJsonParser jParser = new CollegeJsonParser();
                JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
                        CollegeUrls.api_login);

                // Log parameters:
                Log.d("url extension: ", CollegeUrls.api_login);
                String parameters = "";
                for (NameValuePair nvp : nameValuePairs) {
                    parameters += nvp.getName() + "=" + nvp.getValue() + ",";
                }
                Log.d("Parameters: ", parameters);
                userInfoList = new ArrayList<CollegeDataStructureFramwork.UserInfo>();
                userTypeList = new ArrayList<CollegeDataStructureFramwork.userType>();
                try {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray jsonData = json.getJSONArray("data");
                        for (int i = 0; i < jsonData.length(); i++) {
                            CollegeDataStructureFramwork.UserInfo collegeUserInfo = new CollegeDataStructureFramwork.UserInfo(
                                    jsonData.getJSONObject(i));
                            CollegeAppUtils.SetSharedParameter(
                                    AisSignInActivity.this, Constants.USERID,
                                    collegeUserInfo.id);
                            CollegeAppUtils.SetSharedParameter(context, "id",
                                    collegeUserInfo.id);
                            CollegeAppUtils.SetSharedParameter(context,
                                    "session_student_id",
                                    collegeUserInfo.session_student_id);
                            CollegeAppUtils.SetSharedParameter(context,
                                    Constants.CHILD_ID,
                                    collegeUserInfo.session_student_id);
                            CollegeAppUtils.SetSharedParameter(context,
                                    "student_name",
                                    collegeUserInfo.student_name);
                            CollegeAppUtils.SetSharedParameter(context,
                                    "student_class",
                                    collegeUserInfo.student_class);
                            CollegeAppUtils.SetSharedParameter(context,
                                    "student_image",
                                    collegeUserInfo.student_image);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.USERTYPEID,
                                    collegeUserInfo.usertypeid);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.UALTEMAIL,
                                    collegeUserInfo.alt_email);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.ORGEMAIL,
                                    collegeUserInfo.email);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.UMOBILENO,
                                    collegeUserInfo.mobile_no);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.USERTYPEID,
                                    collegeUserInfo.usertypeid);
                            userInfoList.add(collegeUserInfo);

                        }

                        JSONArray jsonUser = json.getJSONArray("user_types");
                        for (int j = 0; j < jsonUser.length(); j++) {
                            CollegeDataStructureFramwork.userType userTYpe = new CollegeDataStructureFramwork.userType(
                                    jsonUser.getJSONObject(j));
                            CollegeAppUtils.SetSharedParameter(context,
                                    "role_id", userTYpe.id);
                            userTypeList.add(userTYpe);
                        }
                        if (userTypeList.size() == 1) {
                            String menu_tag = json.getString("menu_info");
                            menu_tag.split("\\|");
                            String menu_title = json.getString("menu_caption");
                            JSONObject permission_add = json
                                    .getJSONObject("permissions");
                            if (jsonData != null) {
                                per = new CollegeDataStructureFramwork.CollegePermission(permission_add);
                            }
                            if (jsonData != null) {
                                if (menu_tag
                                        .contains(Constants.MENU_TEACHER_ASSIGNMENT)) {
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_ASSIGNMENT_CREATE,
                                                    per.assignment_create
                                                            .toString());
                                    //Log.d("VALUE OF ASSIGNMENT CREATE",per.assignment_create.toString());

                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_ASSIGNMENT_MARK,
                                                    per.assignment_mark
                                                            .toString());

                                    Log.d("ASSIGNMENT MARK",per.assignment_mark.toString());
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH,
                                                    per.assignment_publish.toString());

                                    Log.d("ASSIGNMENT PUBLISH ",per.assignment_publish.toString());
                                }
                                if (menu_tag.contains(Constants.MENU_TEACHER_ANNOUNCEMENT)) {
                                    CollegeAppUtils
                                            .SetSharedParameter(AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_ANNOUNCEMENT_CREATE, per.announcement_create.toString());
                                }
                                if (menu_tag
                                        .contains(Constants.MENU_TEACHER_COURSE_WORK)) {
                                    CollegeAppUtils
                                            .SetSharedParameter(AisSignInActivity.this,CollegeConstants.COLLEGE_CLASSWORK_CREATE,per.classwork_create.toString());
                                }
                                if (menu_tag
                                        .contains(Constants.MENU_TEACHER_QUIZ)) {
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_INTERNAL_CREATE,
                                                    per.test_create.toString());
                                    // marking & publish

                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_INTERNAL_MARK,
                                                    per.test_mark.toString());

                                    Log.d("VALUE OF TEST MARK...",
                                            per.test_mark.toString());
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_INTERNAL_PUBLISH,
                                                    per.test_publish.toString());

                                    Log.d("VALUE OF TEST PUBLISH", per.test_publish.toString());
                                }

                                // attendance mark

                                if (!CollegeAppUtils
                                        .GetSharedParameter(context,
                                                CollegeConstants.USERTYPEID)
                                        .equalsIgnoreCase(
                                                CollegeConstants.USERTYPE_PARENT)) {
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_ATTENDANCE_MARK,
                                                    per.attendance_mark
                                                            .toString());

                                    // lessons edit
                                    if (per.lessons_edit != null) {
                                        CollegeAppUtils
                                                .SetSharedParameter(
                                                        AisSignInActivity.this,
                                                        CollegeConstants.COLLEGE_LESSONS_EDIT,
                                                        per.lessons_edit.toString());
                                    }

                                }

								/*
                                 * //feedback create CollegeAppUtils
								 * .SetSharedParameter( SigninActivity.this,
								 * CollegeConstants.COLLEGE_FEEDBACK_CREATE,
								 * per.feedback_create.toString());
								 */
                                // marking & publish

                                if (!CollegeAppUtils
                                        .GetSharedParameter(context,
                                                CollegeConstants.USERTYPEID)
                                        .equalsIgnoreCase(
                                                CollegeConstants.USERTYPE_TEACHER)) {
                                    if (per.request_create != null) {
                                        CollegeAppUtils
                                                .SetSharedParameter(
                                                        AisSignInActivity.this,
                                                        CollegeConstants.COLLEGE_REQUEST_CREATE,
                                                        per.request_create
                                                                .toString());
                                    }

                                    if (per.request_delete != null) {
                                        CollegeAppUtils
                                                .SetSharedParameter(
                                                        AisSignInActivity.this,
                                                        CollegeConstants.COLLEGE_REQUEST_DELETE,
                                                        per.request_delete
                                                                .toString());

                                    }

                                    if (per.request_edit != null) {
                                        CollegeAppUtils
                                                .SetSharedParameter(
                                                        AisSignInActivity.this,
                                                        CollegeConstants.COLLEGE_REQUEST_EDIT,
                                                        per.request_edit.toString());
                                    }
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_REQUEST_MARK,
                                                    per.request_mark.toString());

                                    if(per.request_publish!= null) {
                                        CollegeAppUtils
                                                .SetSharedParameter(
                                                        AisSignInActivity.this,
                                                        CollegeConstants.COLLEGE_REQUEST_PUBLISH,
                                                        per.request_publish
                                                                .toString());
                                    }
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    AisSignInActivity.this,
                                                    CollegeConstants.COLLEGE_REQUEST_VIEW,
                                                    per.request_view.toString());
                                    // feedback create
                                    CollegeAppUtils
                                            .SetSharedParameter(
                                                    context,
                                                    CollegeConstants.COLLEGE_FEEDBACK_CREATE,
                                                    per.feedback_create
                                                            .toString());

                                }

                            }

                            if (menu_tag != null) {
                                Log.d("menu", menu_tag.toString());
                                CollegeAppUtils.SetSharedParameter(context,
                                        CollegeConstants.MENU_TAGS,
                                        menu_tag.toString());
                                CollegeAppUtils.SetSharedParameter(context,
                                        CollegeConstants.MENU_TITLES,
                                        menu_title.toString());
                            }
                        }
                        JSONObject jsonOrg = json.getJSONObject("org_config");
                        if (jsonOrg != null) {
                            CollegeDataStructureFramwork.UserInfo collegeUserInfo = new CollegeDataStructureFramwork.UserInfo(
                                    jsonOrg);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.ASSIGNMENT_MARKING,
                                    collegeUserInfo.assignment_marking);
                            Log.d("ASSIGNMENT MARKING",
                                    collegeUserInfo.assignment_marking);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.TEST_MARKING,
                                    collegeUserInfo.test_marking);
                            CollegeAppUtils.SetSharedParameter(context,
                                    CollegeConstants.CLASSWORK_MARKING,
                                    collegeUserInfo.classwork_marking);
                        }
                        return true;
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

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
			/*if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}*/

            if (result) {
                if (SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                        Constants.INSTITUTION_TYPE).equals(
                        Constants.INSTITUTION_TYPE_SCHOOL)) {
                    if (!SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.USERTYPEID).equalsIgnoreCase(
                            Constants.USERTYPE_PARENT)) {

                        SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                                Constants.USERINFO, userInfo.toString());

                        if (SchoolAppUtils.GetSharedParameter(
                                AisSignInActivity.this, Constants.USERTYPEID)
                                .equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {
                            SchoolAppUtils.SetSharedBoolParameter(
                                    AisSignInActivity.this, Constants.UISSIGNIN,
                                    true);
                            Intent ints = new Intent(AisSignInActivity.this,
                                    MainActivity.class);
                            ints.putExtra("fromlogin", "1");
                            ints.putExtra("menu_val", menu_val);
                            startActivity(ints);
                            if (dialog != null) {
                                dialog.dismiss();
                                dialog = null;
                            }
                            finish();
                        } else if (SchoolAppUtils.GetSharedParameter(
                                AisSignInActivity.this, Constants.USERTYPEID)
                                .equalsIgnoreCase(Constants.USERTYPE_TEACHER)) {
                            SchoolAppUtils.SetSharedBoolParameter(
                                    AisSignInActivity.this, Constants.ISSIGNIN,
                                    true);

                            // Download data for offline access
                            new GetOfflineDataAsyntask().execute();
                        }
                    } else {
                        SchoolAppUtils.SetSharedBoolParameter(
                                AisSignInActivity.this, Constants.ISSIGNIN, true);
                        SchoolAppUtils.SetSharedBoolParameter(
                                AisSignInActivity.this, Constants.ISPARENTSIGNIN,
                                true);

                        Intent  intent = new Intent(AisSignInActivity.this,
                                ParentMainActivity.class);
                        intent.putExtra("menu_val", menu_val);
                        startActivity(intent);

                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        finish();
                    }
                }
                // College
                else {
                    if (userTypeList.size() > 1) {

                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                                Constants.USERID, "0");
                        showDialog();
                    } else {

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                                0);
                        nameValuePairs.add(new BasicNameValuePair("id",
                                CollegeAppUtils.GetSharedParameter(context,
                                        "id")));
                        Log.d("PARAMS OF TEACHER", "" + nameValuePairs);
                        new GetTimetableListAsyntaskCollege()
                                .execute(nameValuePairs);
                    }

                }
            } else {

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AisSignInActivity.this,
                            getResources().getString(R.string.sign_in), error);
                } else {
                    SchoolAppUtils
                            .showDialog(
                                    AisSignInActivity.this,
                                    getResources().getString(R.string.sign_in),
                                    getResources().getString(
                                            R.string.unknown_response));
                }
            }
        }
    }

    // OFFLINE DATABASE INITIALIZATION
    private class GetOfflineDataAsyntask extends AsyncTask<Void, Void, Boolean> {
        String error;
        private ArrayList<DataStructureFramwork.Subject> subjects;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            subjects = new ArrayList<DataStructureFramwork.Subject>();
            subjects.clear();
			/*dialog = new ProgressDialog(SigninActivity.this);
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
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id",
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.UORGANIZATIONID)));
            nameValuePairs
                    .add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(AisSignInActivity.this,
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
                            DataStructureFramwork.Subject subject = new DataStructureFramwork.Subject(
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
			 * if (dialog != null) { // dialog.dismiss(); dialog = null; }
			 */

            if (subjects != null) {

                mDatabase.open();
                // Delete all data in database
                mDatabase.deleteAllSubjects();
                for (int i = 0; i < subjects.size(); i++) {
                    Log.d("Insert: ", "Inserting .." + i);
                    mDatabase.addSubjects(subjects.get(i));
                }
                mDatabase.close();

            }

            // Go to Teacher Main Page
			/*
			 * startActivity(new Intent(SigninActivity.this,
			 * TeacherMainActivity.class)); finish();
			 */
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_type_id",
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id",
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.UORGANIZATIONID)));
            nameValuePairs
                    .add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(AisSignInActivity.this,
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
            timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
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
                        timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.TimeTable attendance = new DataStructureFramwork.TimeTable(
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
                    mDatabase.deleteAllTimetable();
                    for (int i = 0; i < timeTable.size(); i++) {
                        Log.d("Insert: ", "Inserting ..");
                        mDatabase.addTimetable(timeTable.get(i));
                    }
                    mDatabase.close();

                }
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_type_id",
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id",
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.UORGANIZATIONID)));
            nameValuePairs
                    .add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(AisSignInActivity.this,
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
            timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
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
                        timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.TimeTable attendance = new DataStructureFramwork.TimeTable(
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
                        Log.d("Insert: ", "Inserting ..");
                        mDatabase.addOfflineteacherattendance(timeTable.get(i));
                    }
                    mDatabase.close();

                }
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_type_id",
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.USERTYPEID)));
            nameValuePairs.add(new BasicNameValuePair("organization_id",
                    SchoolAppUtils.GetSharedParameter(AisSignInActivity.this,
                            Constants.UORGANIZATIONID)));
            nameValuePairs
                    .add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(AisSignInActivity.this,
                                    Constants.USERID)));
            new GetOfflineDataStudentAsyntask().execute(nameValuePairs);

        }
    }

    // OFFLINE DATABASE INITIALIZATION
    private class GetOfflineDataStudentAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;
        private ArrayList<DataStructureFramwork.Attendance> students;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            // Log parameters:
            students = new ArrayList<DataStructureFramwork.Attendance>();
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
                            DataStructureFramwork.Attendance subject = new DataStructureFramwork.Attendance(
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


            if (students != null) {

                mDatabase.open();
                // Delete all data in database
                mDatabase.deleteAllStudents();
                for (int i = 0; i < students.size(); i++) {
                    Log.d("Insert: ", "Inserting .." + i);
                    mDatabase.addStudents(students.get(i));
                }
                mDatabase.close();
                Intent intent = new Intent(AisSignInActivity.this,
                        TeacherMainActivity.class);
                intent.putExtra("menu_val", menu_val);
                startActivity(intent);

                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                finish();

            }

        }

    }


    private class GetTimetableListAsyntaskCollege extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

			/*dialog = new ProgressDialog(SigninActivity.this);
			dialog.setTitle(getResources().getString(R.string.fetching_offline));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();*/

        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
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
                        timeTable = new ArrayList<DataStructureFramwork.TimeTable>();
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.TimeTable attendance = new DataStructureFramwork.TimeTable(
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
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            if (CollegeAppUtils.GetSharedParameter(context,
                    CollegeConstants.USERTYPEID).equalsIgnoreCase(
                    CollegeConstants.USERTYPE_STUDENT)) {
                Intent incol = new Intent(context,
                        CollegeMainActivity.class);
                incol.putExtra("fromlogin", "1");

                context.startActivity(incol);
                ((Activity) context).finish();
            } else if (CollegeAppUtils.GetSharedParameter(context,
                    CollegeConstants.USERTYPEID).equalsIgnoreCase(
                    CollegeConstants.USERTYPE_PARENT)) {
                context.startActivity(new Intent(context,
                        CollegeParentMainActivity.class));
                ((Activity) context).finish();
            } else if (CollegeAppUtils.GetSharedParameter(context,
                    CollegeConstants.USERTYPEID).equalsIgnoreCase(
                    CollegeConstants.USERTYPE_TEACHER)) {
                context.startActivity(new Intent(context,
                        CollegeTeacherMainActivity.class));
                ((Activity) context).finish();
            }

        }
    }


    private void showDialog() {
        // Strings to Show In Dialog with Radio Buttons
        final Dialog dialog2 = new Dialog(context);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // TODO Auto-generated method stub
                SchoolAppUtils.SetSharedParameter(AisSignInActivity.this,
                        Constants.USERID, "0");
            }
        });
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.custom_list_dialog);
        ListView listName = (ListView) dialog2.findViewById(R.id.listView1);

        CollegeListUserNameAdapter mAdapter = new CollegeListUserNameAdapter(
                context, userTypeList);
        listName.setAdapter(mAdapter);
        dialog2.show();
    }


}
