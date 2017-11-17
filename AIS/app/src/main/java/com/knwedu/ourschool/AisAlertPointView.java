package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AisAlertPointAdapter;
import com.knwedu.ourschool.model.AlertPointsData;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.ourschool.showPopupNew;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddasgupta on 2/27/2017.
 */

public class AisAlertPointView extends Activity  {
    private ListView list;
    private ArrayList<AlertPointsData> mListAlertPoints;
    private AisAlertPointAdapter mAdapter;
    ImageView set_point;
    int PLACE_PICKER_REQUEST = 1;
    public String route_id;
    private TextView header;
    RelativeLayout popuplayout;
    public Button savecustompostion;
    public EditText position_nick_name;
    public  int maxvalue=0;
    public  int maxvaluecheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_alert_view);

        header = (TextView) findViewById(R.id.header_text);
        header.setText("Set alert points (Max 10)");

        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        initialize();




    }


    public void RefreshList() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                        Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                .GetSharedParameter(AisAlertPointView.this, Constants.USERID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                        Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                .GetSharedParameter(AisAlertPointView.this, Constants.CHILD_ID)));

        new GetAlertPointsList().execute(nameValuePairs);

    }

    private void initialize() {

        list = (ListView)findViewById(R.id.listview_alerts);
        set_point = (ImageView) findViewById(R.id.set_point);

        Intent intent = getIntent();
        route_id = intent.getStringExtra("rout_id");
        savecustompostion=(Button) findViewById(R.id.savecustompostionname);
        position_nick_name=(EditText)findViewById(R.id.position_nick_name);
        //position_nick_name = intent.getStringExtra("position_nick_name");
        popuplayout = (RelativeLayout) findViewById(R.id.popuplayout);




        set_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(maxvaluecheck==10){
                    String error="You can add maximum 10 alert points.";
                    SchoolAppUtils.showDialog(AisAlertPointView.this, "Information", error);
                }
                else {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Intent intent;
                    try {
                        intent = builder.build(getApplicationContext());
                        startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });










        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                        Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                .GetSharedParameter(AisAlertPointView.this, Constants.USERID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                        Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                .GetSharedParameter(AisAlertPointView.this, Constants.CHILD_ID)));

        new GetAlertPointsList().execute(nameValuePairs);

    }






    private class GetAlertPointsList extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AisAlertPointView.this);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            Log.d("refresh","refresh");

            List<NameValuePair> namevaluepair = params[0];
            mListAlertPoints = new ArrayList<AlertPointsData>();

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            AisAlertPointView.this, Constants.COMMON_URL)
                            + Urls.api_ais_alert_list);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                            Log.d("array",array.toString());
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }

                        for (int i = 0; i < array.length(); i++) {

                            AlertPointsData mAlerts = new AlertPointsData(array.getJSONObject(i));
                            mListAlertPoints.add(mAlerts);
                            maxvalue=array.length();
                        }

                        return true;

                    }else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                }else {
                    //Log.d("dsdsds","gjg");
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
                mAdapter = new AisAlertPointAdapter(AisAlertPointView.this, mListAlertPoints);
                list.setAdapter(mAdapter);
                maxvaluecheck=maxvalue;

            }
            else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AisAlertPointView.this, AisAlertPointView.this
                            .getTitle().toString(), error);
                    list.setAdapter(null);
                    /*if(error.equalsIgnoreCase("max")){
                        Log.d("max","max");
                        SchoolAppUtils.showDialog(AisAlertPointView.this, AisAlertPointView.this
                                .getTitle().toString(), "You can add maximum 10 alert points");
                    }else {
                        SchoolAppUtils.showDialog(AisAlertPointView.this, AisAlertPointView.this
                                .getTitle().toString(), error);
                        list.setAdapter(null);
                    }   */
                }
                else{
                    SchoolAppUtils.showDialog(AisAlertPointView.this, AisAlertPointView.this
                            .getTitle().toString(), getResources().getString(R.string.error));
                    list.setAdapter(null);
                }

            }
        }



    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String test = "";

        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode!=0) {
                Log.d("data>>>>",data.toString());
                Place place = PlacePicker.getPlace(data, this);
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;
                final String address = place.getAddress().toString();
                final String lat = String.valueOf(latitude);
                final String lon = String.valueOf(longitude);
                //final String position_nick_name = position_nick_name.getText().toString();

                popuplayout.setVisibility(View.VISIBLE);
                savecustompostion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position_nick_name.getText().toString().isEmpty()){
                            position_nick_name.setError("Enter alert point name");
                            position_nick_name.requestFocus();
                        }else{
                            popuplayout.setVisibility(View.GONE);
                            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                            nvp.add(new BasicNameValuePair("user_type_id",
                                    SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                                            Constants.USERTYPEID)));
                            nvp.add(new BasicNameValuePair("user_id", SchoolAppUtils
                                    .GetSharedParameter(AisAlertPointView.this, Constants.USERID)));
                            nvp.add(new BasicNameValuePair("organization_id",
                                    SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                                            Constants.UORGANIZATIONID)));
                            nvp.add(new BasicNameValuePair("child_id", SchoolAppUtils
                                    .GetSharedParameter(AisAlertPointView.this, Constants.CHILD_ID)));
                            nvp.add(new BasicNameValuePair("position_name", address));
                            nvp.add(new BasicNameValuePair("latitude", lat));
                            nvp.add(new BasicNameValuePair("longitude", lon));
                            nvp.add(new BasicNameValuePair("route_id", route_id));
                            //Log.d("nme>>>>",position_nick_name.getText().toString().trim());
                            nvp.add(new BasicNameValuePair("position_nick_name", position_nick_name.getText().toString().trim()));
                            //nvp.add(new BasicNameValuePair("ss", "ss"));
                            new SaveLocationAsync().execute(nvp);
                        }
                    }
                });

                    /*List<NameValuePair> nvp = new ArrayList<NameValuePair>(4);
                    nvp.add(new BasicNameValuePair("user_type_id",
                            SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                                    Constants.USERTYPEID)));
                    nvp.add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(AisAlertPointView.this, Constants.USERID)));
                    nvp.add(new BasicNameValuePair("organization_id",
                            SchoolAppUtils.GetSharedParameter(AisAlertPointView.this,
                                    Constants.UORGANIZATIONID)));
                    nvp.add(new BasicNameValuePair("child_id", SchoolAppUtils
                            .GetSharedParameter(AisAlertPointView.this, Constants.CHILD_ID)));
                    nvp.add(new BasicNameValuePair("position_name", address));
                    nvp.add(new BasicNameValuePair("latitude", lat));
                    nvp.add(new BasicNameValuePair("longitude", lon));
                    nvp.add(new BasicNameValuePair("route_id", route_id));
                    //nvp.add(new BasicNameValuePair())

                    new SaveLocationAsync().execute(nvp);*/
            }



        }
    }


    private class SaveLocationAsync extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AisAlertPointView.this);
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
                            AisAlertPointView.this, Constants.COMMON_URL)
                            + Urls.api_ais_save_location);
            Log.d("dataaaaaaa",namevaluepair.toString());
            Log.d("urlpostlatlong",SchoolAppUtils.GetSharedParameter(
                    AisAlertPointView.this, Constants.COMMON_URL)
                    + Urls.api_ais_save_location);
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
                SchoolAppUtils.showDialog(AisAlertPointView.this, "Information", error);
                position_nick_name.setText("");
                RefreshList();

            }else{
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AisAlertPointView.this, "Alert".toString(), error);

                }else{
                    SchoolAppUtils.showDialog(AisAlertPointView.this, "Alert", "Error");
                    //list.setAdapter(null);
                }
            }
        }



    }
}
