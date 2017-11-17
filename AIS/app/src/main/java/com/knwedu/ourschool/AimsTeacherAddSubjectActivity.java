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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by ddasgupta on 27/03/17.
 */
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

public class AimsTeacherAddSubjectActivity extends Activity {

    private TextView header,save_subject;
    Spinner spinnerClass, spinnerSection, spinnerSubject;
    String selected_section_id, selected_class_id, selected_subject_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aims_add_subject);
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
        save_subject = (TextView) findViewById(R.id.save_subject);

        header.setText("Add Subject");
        spinnerClass = (Spinner) findViewById(R.id.spinnerClass);
        spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
        spinnerSubject = (Spinner)findViewById(R.id.spinnerSubject);

        save_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_section_id.equals("0")){
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Add Subject",
                            "Please select Class Section.");
                    return;
                }
                if(selected_subject_id.equals("0")){
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Add Subject",
                            "Please select Subject");
                    return;
                }
                List<NameValuePair> addNameValuePairs = new ArrayList<NameValuePair>();
                addNameValuePairs.add(new BasicNameValuePair("organization_id",SchoolAppUtils
                        .GetSharedParameter(AimsTeacherAddSubjectActivity.this,
                                Constants.UORGANIZATIONID)));
                addNameValuePairs.add(new BasicNameValuePair("teacher_id",SchoolAppUtils
                        .GetSharedParameter(AimsTeacherAddSubjectActivity.this,
                                Constants.USERID)));
                addNameValuePairs.add(new BasicNameValuePair("section_id",selected_section_id));
                addNameValuePairs.add(new BasicNameValuePair("subject_id",selected_subject_id));
                new AddSubjectAsyntask().execute(addNameValuePairs);

            }
        });

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils
                .GetSharedParameter(AimsTeacherAddSubjectActivity.this,
                        Constants.UORGANIZATIONID)));
        new SetClassListAsyn().execute(nameValuePairs);

    }


    private class SetClassListAsyn extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        ArrayList<DataStructureFramwork.ClassOfOrg> classes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AimsTeacherAddSubjectActivity.this);
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
                            AimsTeacherAddSubjectActivity.this, Constants.COMMON_URL)
                            + Urls.api_aims_class_section_list);
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
                        AimsTeacherAddSubjectActivity.this,
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
                                        AimsTeacherAddSubjectActivity.this,
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
                                                ArrayList<String> msubList = new ArrayList<String>();
                                                msubList.add("Select Subject");

                                                if (n > 0) {
                                                    selected_section_id = classes.get(spinnerClass
                                                            .getSelectedItemPosition() - 1).section_List
                                                            .get(n - 1).section_id;
                                                    selected_class_id = classes.get(spinnerClass
                                                            .getSelectedItemPosition() - 1).class_id;
                                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                                    nameValuePairs.add(new BasicNameValuePair("teacher_id", SchoolAppUtils.GetSharedParameter(AimsTeacherAddSubjectActivity.this, Constants.USERID)));
                                                    nameValuePairs.add(new BasicNameValuePair("section_id", selected_section_id));
                                                    nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(AimsTeacherAddSubjectActivity.this, Constants.UORGANIZATIONID)));

                                                    new SetSubjectListAsync().execute(nameValuePairs);


                                                } else {
                                                    selected_section_id = "0";
                                                    selected_class_id = "0";
                                                }

                                                ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
                                                        AimsTeacherAddSubjectActivity.this,
                                                        R.layout.simple_spinner_dropdown_item_custom,
                                                        msubList);
                                                adapterClass
                                                        .setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
                                                spinnerSubject.setAdapter(adapterClass);

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
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Registration", error);
                } else {
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Registration",
                            getResources().getString(R.string.error));
                }

            }

        }
    }



    private class SetSubjectListAsync extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        ArrayList<DataStructureFramwork.AisTeacherSubject> subjects;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AimsTeacherAddSubjectActivity.this);
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
                            AimsTeacherAddSubjectActivity.this, Constants.COMMON_URL)
                            + Urls.api_ais_techer_get_subject);
            String url=Constants.COMMON_URL+ Urls.api_ais_techer_get_subject;
            Log.d("valueadsubaims",namevaluepair.toString());
            Log.d("valueadsubaimsurl",url);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        subjects = new ArrayList<DataStructureFramwork.AisTeacherSubject>();
                        subjects.clear();
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }

                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.AisTeacherSubject subOfOrg = new DataStructureFramwork.AisTeacherSubject(
                                    array.getJSONObject(i));
                            subjects.add(subOfOrg);
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

            if(result){
                ArrayList<String> mList = new ArrayList<String>();
                mList.add("Select Subject");

                for (int i = 0; i < subjects.size(); i++) {
                    mList.add(subjects.get(i).sub_name);
                }
                ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
                        AimsTeacherAddSubjectActivity.this,
                        R.layout.simple_spinner_dropdown_item_custom, mList);
                adapterClass
                        .setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
                spinnerSubject.setAdapter(adapterClass);

                spinnerSubject
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(
                                    AdapterView<?> arg0,
                                    View arg1, int n, long arg3) {
                                // TODO Auto-generated method
                                // stub
                                if (n > 0) {

                                    selected_subject_id = subjects.get(spinnerSubject
                                            .getSelectedItemPosition() - 1).id;

                                } else {
                                    selected_subject_id = "0";

                                }
                            }

                            @Override
                            public void onNothingSelected(
                                    AdapterView<?> arg0) {
                                // TODO Auto-generated method
                                // stub

                            }
                        });



            }else {
                if (error.length() > 0) {
                    selected_subject_id = "0";
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Add Subject", error);
                } else {
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Add Subject",
                            getResources().getString(R.string.error));
                }
            }



        }



    }

    private class AddSubjectAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        String message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AimsTeacherAddSubjectActivity.this);
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
                            AimsTeacherAddSubjectActivity.this, Constants.COMMON_URL)
                            + Urls.api_ais_subject_save);
            Log.d("namevaluepairsave",namevaluepair.toString());
            String url=SchoolAppUtils.GetSharedParameter(
                    AimsTeacherAddSubjectActivity.this, Constants.COMMON_URL)
                    + Urls.api_ais_subject_save;
            Log.d("urlsave",url);
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
                SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                        "Add Subject", message);
                //initialize();
                //student_name.setText("");
                spinnerClass.setSelection(0);
                spinnerSection.setSelection(0);
                spinnerSubject.setSelection(0);
            }else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Add Subject", error);
                } else {
                    SchoolAppUtils.showDialog(AimsTeacherAddSubjectActivity.this,
                            "Add Subject",
                            getResources().getString(R.string.error));
                }

            }

        }
    }

}
