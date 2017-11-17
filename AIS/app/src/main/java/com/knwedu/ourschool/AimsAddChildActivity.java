package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.model.StudentData;
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
 * Created by ddasgupta on 27/03/17.
 */

public class AimsAddChildActivity  extends Activity {
    private TextView header, btn_save;
    Spinner spinnerClass, spinnerSection, spinnerStudent;
    String selected_section_id, selected_class_id, selected_student_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aims_add_child);
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
        btn_save = (TextView) findViewById(R.id.btn_save);
        header.setText("Add Child");
        spinnerClass = (Spinner) findViewById(R.id.spinnerClass);
        spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
        spinnerStudent = (Spinner) findViewById(R.id.spinnerStudent);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int errorCount = 0;
                if(selected_section_id.equals("0")){
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                            "Add Child",
                            "Please select Class Section.");
                    return;
                }
                if(selected_student_id.equals("0")){
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                            "Add Chils",
                            "Please select Student");
                    return;
                }

                if(errorCount == 0) {

                    List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                    nvp.add(new BasicNameValuePair("parent_id", SchoolAppUtils.GetSharedParameter(AimsAddChildActivity.this,
                            Constants.USERID)));
                    nvp.add(new BasicNameValuePair("child_id", selected_student_id));
                    nvp.add(new BasicNameValuePair("section_id", selected_section_id));
                    nvp.add(new BasicNameValuePair("organization_id", SchoolAppUtils
                            .GetSharedParameter(AimsAddChildActivity.this,
                                    Constants.UORGANIZATIONID)));

                    new AddChildDataAsync().execute(nvp);
                }


            }
        });

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils
                .GetSharedParameter(AimsAddChildActivity.this,
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
            dialog = new ProgressDialog(AimsAddChildActivity.this);
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
                            AimsAddChildActivity.this, Constants.COMMON_URL)
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
                        AimsAddChildActivity.this,
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
                                        AimsAddChildActivity.this,
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
                                                    List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                                                    nvp.add(new BasicNameValuePair("organization_id", SchoolAppUtils
                                                            .GetSharedParameter(AimsAddChildActivity.this,
                                                                    Constants.UORGANIZATIONID)));
                                                    nvp.add(new BasicNameValuePair("section_id",selected_section_id));
                                                    nvp.add(new BasicNameValuePair("user_id",SchoolAppUtils.GetSharedParameter(AimsAddChildActivity.this,
                                                            Constants.USERID)));

                                                    new GetStudentList().execute(nvp);

                                                } else {
                                                    selected_section_id = "0";
                                                    selected_class_id = "0";
                                                    selected_student_id = "0";

                                                    ArrayList<String> mssList = new ArrayList<String>();

                                                        mssList.add("Select Student");

                                                    ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
                                                            AimsAddChildActivity.this,
                                                            R.layout.simple_spinner_dropdown_item_custom, mssList);
                                                    adapterClass
                                                            .setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
                                                    spinnerStudent.setAdapter(adapterClass);


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
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                            "Registration", error);
                } else {
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                            "Registration",
                            getResources().getString(R.string.error));
                }

            }

        }
    }



    public class GetStudentList extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {

        ProgressDialog dialog;
        String error = "";
        String url="";
        ArrayList<StudentData> mListStudentData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AimsAddChildActivity.this);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
            mListStudentData = new ArrayList<>();


        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];
            JsonParser jParser = new JsonParser();


                url = SchoolAppUtils.GetSharedParameter(
                        AimsAddChildActivity.this, Constants.COMMON_URL)
                        + Urls.api_aims_student_list ;

            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,url);
            try{
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        array = json.getJSONArray("data");

                        mListStudentData.clear();
                        for (int i = 0; i < array.length(); i++) {

                                StudentData stdData = new StudentData(array.getJSONObject(i));
                                mListStudentData.add(stdData);



                        }

                        return true;


                    }else {
                        error = json.getString("data");
                        return false;
                    }


                }else {
                    error = getResources().getString(R.string.unknown_response);
                }
            }catch (JSONException e) {
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

                    mList.add("Select Student");
                    for (int i = 0; i < mListStudentData.size(); i++) {
                        mList.add(mListStudentData.get(i).name);
                    }


                ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
                        AimsAddChildActivity.this,
                        R.layout.simple_spinner_dropdown_item_custom, mList);
                adapterClass
                        .setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
                spinnerStudent.setAdapter(adapterClass);

                spinnerStudent
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(
                                    AdapterView<?> arg0,
                                    View arg1, int n, long arg3) {
                                // TODO Auto-generated method
                                // stub
                                if (n > 0) {

                                        selected_student_id = mListStudentData.get(n-1).id;


                                }else{
                                    selected_student_id= "0";
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
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                            "Registration", error);
                } else {
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
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
            dialog = new ProgressDialog(AimsAddChildActivity.this);
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
                            AimsAddChildActivity.this, Constants.COMMON_URL)
                            + Urls.api_aims_add_child);
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
                SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                        "Add Child", message);
                //initialize();
                spinnerStudent.setSelection(0);
                spinnerClass.setSelection(0);
                spinnerSection.setSelection(0);
            }else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                            "Add Child", error);
                } else {
                    SchoolAppUtils.showDialog(AimsAddChildActivity.this,
                            "Add Child",
                            getResources().getString(R.string.error));
                }

            }
        }



    }

}
