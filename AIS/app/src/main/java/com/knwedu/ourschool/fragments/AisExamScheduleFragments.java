package com.knwedu.ourschool.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.AisSyllabusAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.AisSyllabusData;
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
 * Created by ddasgupta on 2/2/2017.
 */

public class AisExamScheduleFragments extends Fragment {
    private View view;
    private ListView list;
    private ArrayList<AisSyllabusData> mlistSyllabus;
    private AisSyllabusAdapter adapter;
    private TextView textnodata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_syllabus_list, container,
                false);


        initialize();


        return view;
    }

    private void initialize() {
        Log.d("ini","AisExamScheduleFragments");
        list = (ListView) view.findViewById(R.id.listview_syllabus);
        textnodata=(TextView) view.findViewById(R.id.textnodata) ;

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

        new GetExamAsyntask().execute(nameValuePairs);



    }

    private class GetExamAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            //dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];
            mlistSyllabus = new ArrayList<AisSyllabusData>();

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            getActivity(), Constants.COMMON_URL)
                            + Urls.api_ais_exam_list);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                            Log.d("arrayais",array.toString());
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }

                        for (int i = 0; i < array.length(); i++) {
                            AisSyllabusData mSyllabus = new AisSyllabusData(
                                    array.getJSONObject(i));
                            mlistSyllabus.add(mSyllabus);
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
                adapter = new AisSyllabusAdapter(getActivity(), mlistSyllabus,2);
                list.setAdapter(adapter);

            }else {

                if (error.length() > 0) {
                    //SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), error);
                    list.setAdapter(null);
                    textnodata.setVisibility(View.VISIBLE);
                    textnodata.setText("No Exam Schedule Created yet");
                }else{
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), getResources().getString(R.string.error));
                    list.setAdapter(null);
                }

            }
        }




    }
}
