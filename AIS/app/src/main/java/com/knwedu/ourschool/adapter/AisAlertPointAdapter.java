package com.knwedu.ourschool.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AisAlertPointView;
import com.knwedu.ourschool.model.AlertPointsData;
import com.knwedu.ourschool.model.ListBusData;
import com.knwedu.ourschool.utils.Constants;
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
 * Created by ddasgupta on 2/28/2017.
 */

public class AisAlertPointAdapter extends BaseAdapter {

    AlertPointsHolder holder;
    private LayoutInflater inflater;
    Context context;
    private ArrayList<AlertPointsData> mListAlertsPoints;

    public AisAlertPointAdapter(Context context, ArrayList<AlertPointsData> lstalt) {
        this.context = context;
        mListAlertsPoints = lstalt;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        if (this.mListAlertsPoints != null) {
            return this.mListAlertsPoints.size();
            //return 2;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_alert_points, parent, false);
            holder = new AlertPointsHolder();
            holder.alertlocation = (TextView) convertView.findViewById(R.id.location);
            //holder.toggle = (ImageView) convertView.findViewById(R.id.img);
            holder.remove = (TextView) convertView.findViewById(R.id.remove);


            convertView.setTag(holder);
        } else {
            holder = (AlertPointsHolder) convertView.getTag();
        }

        holder.alertlocation.setText(mListAlertsPoints.get(position).custom_name);

        /*if (mListAlertsPoints.get(position).notification_status.equals("1")) {
            holder.toggle.setImageResource(R.drawable.on);
        } else {
            holder.toggle.setImageResource(R.drawable.off);
        }*/
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("user_type_id",
                        SchoolAppUtils.GetSharedParameter(context,
                                Constants.USERTYPEID)));
                nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                        .GetSharedParameter(context, Constants.USERID)));
                nameValuePairs.add(new BasicNameValuePair("organization_id",
                        SchoolAppUtils.GetSharedParameter(context,
                                Constants.UORGANIZATIONID)));
                nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                        .GetSharedParameter(context, Constants.CHILD_ID)));
                nameValuePairs.add(new BasicNameValuePair("id", mListAlertsPoints.get(position).id));
                new DeleteAlertAsync().execute(nameValuePairs);


            }
        });

        /*holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("user_type_id",
                        SchoolAppUtils.GetSharedParameter(context,
                                Constants.USERTYPEID)));
                nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                        .GetSharedParameter(context, Constants.USERID)));
                nameValuePairs.add(new BasicNameValuePair("organization_id",
                        SchoolAppUtils.GetSharedParameter(context,
                                Constants.UORGANIZATIONID)));
                nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                        .GetSharedParameter(context, Constants.CHILD_ID)));
                String status ="";
                if(mListAlertsPoints.get(position).notification_status.equals("1")){
                    status = "0";
                }else{
                    status = "1";
                }
                nameValuePairs.add(new BasicNameValuePair("status", status));
                nameValuePairs.add(new BasicNameValuePair("id", mListAlertsPoints.get(position).id));

                new SwitchChangeAsync().execute(nameValuePairs);
                //((AisAlertPointView) context).RefreshList();


            }
        });*/


        return convertView;
    }


    private class AlertPointsHolder {
        TextView alertlocation,remove;
        ImageView toggle;

    }

    private class SwitchChangeAsync extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(context.getResources().getString(R.string.please_wait));
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
                            context, Constants.COMMON_URL)
                            + Urls.api_ais_switch_change);

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
                    error = context.getResources().getString(R.string.unknown_response);
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
                SchoolAppUtils.showDialog(context, "Information", error);
                ((AisAlertPointView) context).RefreshList();

            }else{
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(context, "Alert".toString(), error);

                }else{
                    SchoolAppUtils.showDialog(context, "Alert", "Error");
                    //list.setAdapter(null);
                }
            }

        }



    }

    private class DeleteAlertAsync extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(context.getResources().getString(R.string.please_wait));
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
                            context, Constants.COMMON_URL)
                            + Urls.api_ais_remove_alert);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        error = json.getString("data");
                        return true;

                    }else {
                        try {
                            Log.d("sayak","sayak");
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                }else {

                    error = context.getResources().getString(R.string.unknown_response);
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
                Log.d("result",result.toString());
                //SchoolAppUtils.showDialog(context, "Information", error);
                //((AisAlertPointView) context).RefreshList();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(error);
                builder.setCancelable(true);
                builder.setNegativeButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ((AisAlertPointView) context).RefreshList();
                            }
                        });

                AlertDialog alert11 = builder.create();
                alert11.show();

            }else{
                if (error.length() > 0) {
                    Log.d("remove",error);
                    SchoolAppUtils.showDialog(context, "Alert".toString(), error);

                }else{
                    Log.d("remove1",error);
                    SchoolAppUtils.showDialog(context, "Alert", "Error");
                    //list.setAdapter(null);
                }
            }

        }



    }

}
