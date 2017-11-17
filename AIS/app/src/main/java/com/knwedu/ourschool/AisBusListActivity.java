package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AisBusListAdapter;
import com.knwedu.ourschool.model.ListBusData;
import com.knwedu.ourschool.utils.Constants;
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
 * Created by ddasgupta on 2/27/2017.
 */

public class AisBusListActivity extends Activity {

    private ListView list;
    private ArrayList<ListBusData> mListBus;
    private AisBusListAdapter mAdapter;
    private TextView header;
    public static Activity self_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_bus);
        self_intent =this;
        header = (TextView) findViewById(R.id.header_text);
        //header.setText("Alert Points");

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
        list = (ListView) findViewById(R.id.listview_buslist);

        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(this,
                        Constants.UORGANIZATIONID)));
        new GetBusList().execute(nvp);
    }

    private class GetBusList extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AisBusListActivity.this);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];
            mListBus = new ArrayList<ListBusData>();

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            AisBusListActivity.this, Constants.COMMON_URL)
                            + Urls.api_ais_bus_list);
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

                            ListBusData mBus = new ListBusData( array.getJSONObject(i) );
                            mListBus.add(mBus);
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
                mAdapter = new AisBusListAdapter(AisBusListActivity.this, mListBus);
                list.setAdapter(mAdapter);
            }else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AisBusListActivity.this, AisBusListActivity.this
                            .getTitle().toString(), error);
                    list.setAdapter(null);
                }else{
                    SchoolAppUtils.showDialog(AisBusListActivity.this, AisBusListActivity.this
                            .getTitle().toString(), getResources().getString(R.string.error));
                    list.setAdapter(null);
                }

            }
        }



    }
}

