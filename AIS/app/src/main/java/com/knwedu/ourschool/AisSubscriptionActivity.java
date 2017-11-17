package com.knwedu.ourschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.model.ListBusData;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddasgupta on 3/2/2017.
 */

public class AisSubscriptionActivity extends Activity {
    private TextView begin_time, end_time, rt_no, operator_name, con_no,description,subscribe;
    private RadioButton radioButton;
    private EditText coupon_code;
    private LinearLayout layout_submit;
    private ListBusData busData;
    private Intent getValue;
    private LinearLayout dwn_route_map;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ais_subscription);

        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        initialize();
    }

    private void initialize() {
        begin_time = (TextView)findViewById(R.id.begin_time);
        end_time = (TextView) findViewById(R.id.end_time);
        rt_no = (TextView)findViewById(R.id.route_no);
        operator_name = (TextView)findViewById(R.id.operator_name);
        con_no = (TextView)findViewById(R.id.con_no);
        description = (TextView)findViewById(R.id.description);
        subscribe = (TextView)findViewById(R.id.subscribe);
        coupon_code = (EditText)findViewById(R.id.coupon_code);
        layout_submit = (LinearLayout)findViewById(R.id.layout_submit);
        radioButton = (RadioButton)findViewById(R.id.radioButton);
        dwn_route_map = (LinearLayout)findViewById(R.id.downnloadd_route_map);

        //busData = (ListBusData) getIntent().getSerializableExtra("value");
        getValue = getIntent();
        begin_time.setText(getValue.getStringExtra("begin_time"));
        end_time.setText(getValue.getStringExtra("end_time"));
        rt_no.setText(getValue.getStringExtra("route_number"));//route_number
        operator_name.setText(getValue.getStringExtra("operator_name"));
        con_no.setText(getValue.getStringExtra("emergency_contact_number"));
        description.setText(getValue.getStringExtra("description"));
        downloadUrl = "https://trainingschool.knwedu.com/mobile/downloadMap/";



        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_submit.setVisibility(View.VISIBLE);
            }
        });

        dwn_route_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(
                        AisSubscriptionActivity.this)
                        .setTitle("Select option")
                        .setPositiveButton("View Document",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        // continue with view
                                        SchoolAppUtils
                                                .imagePdfViewDocument(
                                                        AisSubscriptionActivity.this,
                                                        downloadUrl
                                                                + getValue.getStringExtra("id"),getValue.getStringExtra("attachment"));
                                    }
                                })
                        .setNegativeButton("Download",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        // download
                                        final DownloadTask downloadTask = new DownloadTask(AisSubscriptionActivity.this, getValue.getStringExtra("attachment"));
                                        downloadTask.execute(downloadUrl + getValue.getStringExtra("id"));



                                             /*
												Intent i = new Intent(
														Intent.ACTION_VIEW);
												i.setData(Uri
														.parse(Urls.api_get_doc
																+ assignment.id));
												startActivity(i);*/

                                    }
                                })
                        .setIcon(android.R.drawable.ic_dialog_info).show();

            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coupon_code.getText().toString().equals(getValue.getStringExtra("transport_coupon"))){
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

                    nameValuePairs.add(new BasicNameValuePair("user_type_id",
                            SchoolAppUtils.GetSharedParameter(AisSubscriptionActivity.this,
                                    Constants.USERTYPEID)));
                    nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(AisSubscriptionActivity.this, Constants.USERID)));
                    nameValuePairs.add(new BasicNameValuePair("organization_id",
                            SchoolAppUtils.GetSharedParameter(AisSubscriptionActivity.this,
                                    Constants.UORGANIZATIONID)));
                    nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                            .GetSharedParameter(AisSubscriptionActivity.this, Constants.CHILD_ID)));
                    nameValuePairs.add(new BasicNameValuePair("id",getValue.getStringExtra("id")));
                    new SubscribeAsync().execute(nameValuePairs);


                }else{
                    SchoolAppUtils.showDialog(AisSubscriptionActivity.this, AisSubscriptionActivity.this
                            .getTitle().toString(), "Invalid Coupon Code");
                }
            }
        });


    }


    private class SubscribeAsync extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AisSubscriptionActivity.this);
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
                            AisSubscriptionActivity.this, Constants.COMMON_URL)
                            + Urls.api_ais_subscribe);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        error = json.getString("data");
                        return true;

                    }else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                }else {
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
                Toast.makeText(AisSubscriptionActivity.this,
                        error, Toast.LENGTH_SHORT)
                        .show();
                AisBusListActivity.self_intent.finish();
                finish();
            }else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AisSubscriptionActivity.this, AisSubscriptionActivity.this
                            .getTitle().toString(), error);
                }else{
                    SchoolAppUtils.showDialog(AisSubscriptionActivity.this, AisSubscriptionActivity.this
                            .getTitle().toString(), getResources().getString(R.string.error));
                }

            }

        }



    }

}
