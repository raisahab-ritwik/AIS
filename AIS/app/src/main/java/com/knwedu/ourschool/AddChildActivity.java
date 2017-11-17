package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddasgupta on 1/17/2017.
 */

public class AddChildActivity extends Activity {
    private TextView header, btn_save;
    Spinner spinnerClass, spinnerSection;
    String selected_section_id, selected_class_id;
    EditText student_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_child);
        initialize();
    }

    private void initialize() {

        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        header = (TextView) findViewById(R.id.header_text);
        btn_save = (TextView)findViewById(R.id.btn_save);
        header.setText("Add Child");
        spinnerClass = (Spinner) findViewById(R.id.spinnerClass);
        spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
        student_name = (EditText)findViewById(R.id.student_name_txt);


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils
                .GetSharedParameter(AddChildActivity.this,
                        Constants.UORGANIZATIONID)));

        new SetClassListAsyn().execute(nameValuePairs);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int errorCount = 0;
                if (student_name.getText().length()==0) {
                    student_name.setError("Enter Student Name");
                    student_name.requestFocus();
                    errorCount++;
                }
                else {
                    student_name.setError(null);
                }

                if(selected_section_id.equals("0")){
                    SchoolAppUtils.showDialog(AddChildActivity.this,
                            "Add Child",
                            "Please select Class Section.");
                    errorCount++;
                }

                if(errorCount == 0) {

                    List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                    nvp.add(new BasicNameValuePair("parent_id", SchoolAppUtils.GetSharedParameter(AddChildActivity.this,
                            Constants.USERID)));
                    nvp.add(new BasicNameValuePair("student_name", student_name.getText().toString()));
                    nvp.add(new BasicNameValuePair("section_id", selected_section_id));
                    nvp.add(new BasicNameValuePair("organization_id", SchoolAppUtils
                            .GetSharedParameter(AddChildActivity.this,
                                    Constants.UORGANIZATIONID)));

                    new AddChildDataAsync().execute(nvp);
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Google Analytics", "Tracking Start");
        //EasyTracker.getInstance(this).activityStart(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Google Analytics", "Tracking Stop");
        //EasyTracker.getInstance(this).activityStop(this);
    }

    private class SetClassListAsyn extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        ArrayList<DataStructureFramwork.ClassOfOrg> classes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddChildActivity.this);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            AddChildActivity.this, Constants.COMMON_URL)
                            + Urls.api_ais_class_section_list);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        classes = new ArrayList<DataStructureFramwork.ClassOfOrg>();
                        classes.clear();
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }

                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.ClassOfOrg classOfOrg = new DataStructureFramwork.ClassOfOrg(
                                    array.getJSONObject(i));
                            classes.add(classOfOrg);
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
                ArrayList<String> mList = new ArrayList<String>();
               mList.add("Select Class");

                for (int i = 0; i < classes.size(); i++) {
                    mList.add(classes.get(i).class_name);
                }
                ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
                        AddChildActivity.this,
                        R.layout.simple_spinner_dropdown_item_custom, mList);
                adapterClass
                        .setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
                spinnerClass.setAdapter(adapterClass);

                spinnerClass
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int n, long arg3) {
                                ArrayList<String> mList = new ArrayList<String>();
                               mList.add("Select Section");

                                if (n > 0) {
                                    // TODO Auto-generated method stub
                                    DataStructureFramwork.ClassOfOrg classOfOrg = classes.get(n - 1);
                                    for (int i = 0; i < classOfOrg.section_List
                                            .size(); i++) {
                                        mList.add(classOfOrg.section_List
                                                .get(i).section_name);
                                    }

                                }
                                ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
                                        AddChildActivity.this,
                                        R.layout.simple_spinner_dropdown_item_custom,
                                        mList);
                                adapterClass
                                        .setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
                                spinnerSection.setAdapter(adapterClass);
                                spinnerSection
                                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(
                                                    AdapterView<?> arg0,
                                                    View arg1, int n, long arg3) {
                                                // TODO Auto-generated method
                                                // stub
                                                if (n > 0) {
                                                    selected_section_id = classes.get(spinnerClass
                                                            .getSelectedItemPosition() - 1).section_List
                                                            .get(n - 1).section_id;
                                                    selected_class_id = classes.get(spinnerClass
                                                            .getSelectedItemPosition() - 1).class_id;
                                                } else {
                                                    selected_section_id = "0";
                                                    selected_class_id = "0";
                                                }

                                            }

                                            @Override
                                            public void onNothingSelected(
                                                    AdapterView<?> arg0) {
                                                // TODO Auto-generated method
                                                // stub

                                            }
                                        });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });

            } else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AddChildActivity.this,
                            "Registration", error);
                } else {
                    SchoolAppUtils.showDialog(AddChildActivity.this,
                            "Registration",
                            getResources().getString(R.string.error));
                }

            }

        }
    }


    private class AddChildDataAsync extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddChildActivity.this);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            AddChildActivity.this, Constants.COMMON_URL)
                            + Urls.api_ais_add_child);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        try {
                        message =json.getString("data");
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
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            if(true){
                SchoolAppUtils.showDialog(AddChildActivity.this,
                        "Add Child", message);
                //initialize();
                student_name.setText("");
                spinnerClass.setSelection(0);
                spinnerSection.setSelection(0);
            }else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AddChildActivity.this,
                            "Add Child", error);
                } else {
                    SchoolAppUtils.showDialog(AddChildActivity.this,
                            "Add Child",
                            getResources().getString(R.string.error));
                }

            }
        }



    }




}
