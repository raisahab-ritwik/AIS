package com.knwedu.ourschool.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AisDailyDiaryAdapter;
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

public class AisDailyDiaryListFragment extends Fragment {

    private ListView list;
    private View view;
    private ProgressDialog dialog;
    private ArrayList<DataStructureFramwork.Assignment> assignments;
    private AisDailyDiaryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assignment_list, container,
                false);
        SchoolAppUtils.loadAppLogo(getActivity(),
                (ImageButton) view.findViewById(R.id.app_logo));

        initialize();


        return view;
    }
    private void initialize() {
        list = (ListView) view.findViewById(R.id.listview);
        loadData();

    }

    private void loadData() {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(getActivity(),
                        Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                .GetSharedParameter(getActivity(), Constants.USERID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getActivity(),
                        Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                .GetSharedParameter(getActivity(), Constants.CHILD_ID)));
        new GetDailyDiaryAsyntask().execute(nameValuePairs);


    }

    private class GetDailyDiaryAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";
        int position;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(getActivity().getTitle().toString());
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            assignments = new ArrayList<DataStructureFramwork.Assignment>();
            List<NameValuePair> nameValuePairs = params[0];

            // Log parameters:
            Log.d("url extension", Urls.api_activity);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
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
                        assignments = new ArrayList<DataStructureFramwork.Assignment>();
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.Assignment assignment = new DataStructureFramwork.Assignment(
                                    array.getJSONObject(i));
                            assignments.add(assignment);
                        }
                        return true;
                    } else {
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
                if (assignments != null) {
                    String temp = null;
                    for (int i = 0; i < assignments.size(); i++) {
                        if (i == 0) {
                            assignments.get(0).check = true;
                            temp = assignments.get(0).created_date;
                        } else {
                            if (!temp
                                    .equalsIgnoreCase(assignments.get(i).created_date)) {
                                assignments.get(i).check = true;
                                temp = assignments.get(i).created_date;
                            }
                        }
                    }


                    adapter = new AisDailyDiaryAdapter(getActivity(), assignments);
                    list.setAdapter(adapter);
                }
            } else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), error);
                    list.setAdapter(null);
                }else{
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), getResources().getString(R.string.error));
                    list.setAdapter(null);
                }

            }
        }

    }
}
