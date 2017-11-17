package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AisTeacherDailyDiaryAdapter;
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
 * Created by ddasgupta on 29/03/17.
 */

public class AisTeacherDailyDiaryList extends Activity {

    private String page_title = "";
    private ArrayList<DataStructureFramwork.Assignment> activities;
    private ProgressDialog dialog;
    private ListView listView;
    private TextView header;
    private DataStructureFramwork.Subject subject;
    AisTeacherDailyDiaryAdapter adapter;
    private TextView textEmpty;
    private Button monthlyWeekly;
    ImageButton addDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_teacher_assignment_list);
        page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
        monthlyWeekly = (Button) findViewById(R.id.monthly_weekly_btn);
        addDaily = (ImageButton) findViewById(R.id.add_assignment);
        monthlyWeekly.setVisibility(View.GONE);

        header = (TextView) findViewById(R.id.header_text);
        header.setText(page_title);
        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        listView = (ListView) findViewById(R.id.listview);
        textEmpty = (TextView) findViewById(R.id.textEmpty);
        activities = new ArrayList<DataStructureFramwork.Assignment>();

        if (getIntent().getExtras() != null) {
            String temp = getIntent().getExtras().getString(
                    Constants.TASSIGNMENT);
            if (temp != null) {
                JSONObject object = null;
                try {
                    object = new JSONObject(temp);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (object != null) {
                    subject = new DataStructureFramwork.Subject(object);

                }

            }
        }

        addDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AisTeacherDailyDiaryList.this, AisTeacherDailyAddActivity.class);
                myIntent.putExtra(Constants.TSUBJECT,
                        subject.object.toString());
                myIntent.putExtra(Constants.PAGE_TITLE, "Daily Diary");
                startActivity(myIntent);
            }
        });





        }

    private void initialize(){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(getApplicationContext(),
                        Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getApplicationContext(),
                        Constants.UORGANIZATIONID)));
        nameValuePairs
                .add(new BasicNameValuePair("user_id", SchoolAppUtils
                        .GetSharedParameter(getApplicationContext(),
                                Constants.USERID)));

        nameValuePairs.add(new BasicNameValuePair("sub_id", subject.id));
        //nameValuePairs
        //.add(new BasicNameValuePair("class_id", subject.class_id));

        nameValuePairs.add(new BasicNameValuePair("section_id", subject.section_id));


        new GetDailyDiaryAsyntask().execute(nameValuePairs);
    }

    @Override
    protected void onResume() {
        super.onResume();
       initialize();
    }


    private class GetDailyDiaryAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AisTeacherDailyDiaryList.this);
            dialog.setTitle(page_title);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            activities.clear();
            List<NameValuePair> namevaluepair = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
                    Urls.api_ais_daily_diary);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }

                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.Assignment activity = new DataStructureFramwork.Assignment(
                                    array.getJSONObject(i));
                            activities.add(activity);
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
                error = "Connection Failure";
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
                if (activities != null) {
                    String temp = null;
                    for (int i = 0; i < activities.size(); i++) {
                        activities.get(i).sub_name = subject.sub_name;
                        if (i == 0) {
                            activities.get(0).check = true;
                            temp = activities.get(0).created_date;
                        } else {
                            if (!temp
                                    .equalsIgnoreCase(activities.get(i).created_date)) {
                                activities.get(i).check = true;
                                temp = activities.get(i).created_date;
                            }
                        }
                    }
                    if (activities.size() > 0) {
                        textEmpty.setVisibility(View.GONE);
                        adapter = new AisTeacherDailyDiaryAdapter(
                                AisTeacherDailyDiaryList.this, activities,
                                subject);
                        listView.setAdapter(adapter);
                        //	listView.setOnItemClickListener(itemClickListener);
                    } else {
                        textEmpty.setText("No data available");
                        textEmpty.setVisibility(View.VISIBLE);
                    }

                }
            } else {
                textEmpty.setText(error.toString());
                textEmpty.setVisibility(View.VISIBLE);
            }
        }

    }


}
