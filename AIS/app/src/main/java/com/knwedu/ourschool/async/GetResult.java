package com.knwedu.ourschool.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.DataStructureFramwork.Examdetail;
import com.knwedu.ourschool.utils.DownloadTask;
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
 * Created by ddasgupta on 3/31/2016.
 */
public class GetResult extends AsyncTask {

    ProgressDialog mProgressDialog;
    Context context;
    String error;
    Examdetail detail;
    String exam_term_id;
    String file_name;

    public GetResult(Context context, String exam_term_id ){
        this.context = context ;
        this.exam_term_id = exam_term_id;
        mProgressDialog = new ProgressDialog(context);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create a progressdialog

            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMessage("  loading...");
            mProgressDialog.show();

    }

    @Override
    protected Object doInBackground(Object[] params) {
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
        nameValuePair.add(new BasicNameValuePair("user_id", SchoolAppUtils
                .GetSharedParameter(context,
                        Constants.USERID)));
        nameValuePair.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(context,
                        Constants.USERTYPEID)));
        nameValuePair.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(context,
                        Constants.UORGANIZATIONID)));
        nameValuePair
                .add(new BasicNameValuePair("exam_term_id", exam_term_id));
        nameValuePair.add(new BasicNameValuePair("student_id", SchoolAppUtils
                .GetSharedParameter(context,
                        Constants.CHILD_ID)));
        nameValuePair.add(new BasicNameValuePair("post_type", "1"));
        JsonParser jParser = new JsonParser();
        JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
                Urls.api_exam_result);



        try {

            if (json != null) {
                if (json.getString("result").equalsIgnoreCase("1")) {
                    // Details
                    JSONObject detail_data = json
                            .getJSONObject("detail_data");
                    JSONArray arrayDetails, arrayAssignment, arrayTest, arrayActivity;
                    JSONArray array_info;
                    try {
                        arrayDetails = detail_data
                                .getJSONArray("exam_data");
                        arrayAssignment = detail_data
                                .getJSONArray("assignment_data");
                        arrayTest = detail_data.getJSONArray("test_data");
                        arrayActivity = detail_data
                                .getJSONArray("activity_data");
                        array_info = detail_data
                                .getJSONArray("profile_data");
                        JSONObject obj = array_info.getJSONObject(0);
                        detail = new Examdetail(obj);
                    } catch (Exception e) {
                        return true;
                    }
                    return true;
                } else {
                    try {
                        error = "Data Error";
                    } catch (Exception e) {
                    }
                    return false;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }








        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        file_name = detail.exam + "_" + detail.session + ".pdf";
        if(mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        DownloadTask downloadTask = new DownloadTask(context, file_name);
        downloadTask.execute(Urls.base_url
                + Urls.api_report_card_download_mobile
                + "/"
                + exam_term_id
                + "/"
                + SchoolAppUtils
                .GetSharedParameter(
                        context,
                        Constants.UORGANIZATIONID)
                + "/"
                + SchoolAppUtils
                .GetSharedParameter(
                        context,
                        Constants.CHILD_ID));


    }



}
