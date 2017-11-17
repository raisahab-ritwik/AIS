package com.knwedu.ourschool.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
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
 * Created by ddasgupta on 3/18/2017.
 */

public class AisHelpFragment extends Fragment {
    private View view;
    private TextView from, btn_send;
    private EditText subject, message;
    String mail_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ais_help, container,
                false);
        initialize();


        return view;
    }

    private void initialize() {
        from = (TextView) view.findViewById(R.id.mail_id);
        btn_send = (TextView) view.findViewById(R.id.btn_send);
        subject = (EditText) view.findViewById(R.id.txt_subject);
        message = (EditText) view.findViewById(R.id.txt_message);

        mail_id = SchoolAppUtils
                .GetSharedParameter(getActivity(), Constants.ORGEMAIL);
        from.setText(mail_id);




        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int errorCount = 0;



                if (message.getText().toString().isEmpty()) {
                    message.setError("Enter Subject");
                    message.requestFocus();
                    errorCount++;
                } else {
                    message.setError(null);
                }

                if (subject.getText().toString().isEmpty()) {
                    subject.setError("Enter Subject");
                    subject.requestFocus();
                    errorCount++;
                } else {
                    subject.setError(null);
                }


                if(errorCount==0) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("from_email", mail_id));
                    nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                            .GetSharedParameter(getActivity(), Constants.USERID)));
                    nameValuePairs.add(new BasicNameValuePair("organization_id",
                            SchoolAppUtils.GetSharedParameter(getActivity(),
                                    Constants.UORGANIZATIONID)));
                    nameValuePairs.add(new BasicNameValuePair("subject", subject.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("message", message.getText().toString()));


                    new SendHelpRequest().execute(nameValuePairs);
                }





            }
        });
    }



    private class SendHelpRequest extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        String succ_messgae;

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


            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            getActivity(), Constants.COMMON_URL)
                            + Urls.api_ais_help);

            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        try {
                            succ_messgae =  json.getString("data");
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
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
                SchoolAppUtils.showDialog(getActivity(), getActivity()
                        .getTitle().toString(), succ_messgae);
                subject.setText("");
                message.setText("");
            }else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), error);

                }else{
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                            .getTitle().toString(), getResources().getString(R.string.error));

                }
            }
        }
    }



}
