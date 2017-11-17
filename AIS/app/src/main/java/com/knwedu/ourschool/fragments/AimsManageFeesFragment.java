package com.knwedu.ourschool.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knwedu.ourschool.ShowAttachmentFeesActivity;
import com.knwedu.ourschool.WebViewActivity;
import com.knwedu.ourschool.adapter.PaymentHistoryDetailsAdapter;
import com.knwedu.ourschool.utils.DataStructureFramwork.PaymentHistory;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.PaymentWebViewActivity;
import com.knwedu.ourschool.ccavenue.AvenuesParams;
import com.knwedu.ourschool.ccavenue.ServiceUtility;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.DialogInterface;

/**
 *
 * created by sayakpal 2/5/2017
 */
public class AimsManageFeesFragment extends Fragment {

    private View view;
    private int type;
    private TextView student_name,class_name,section_name,lbl_commission,
            commission,lbl_convenience, convenience,lbl_total_amnt,total_amnt,btn_pay,btn_view_image,emptyView,btnPaymentHistory;
    private EditText aims_fee_type,reg_no, amount;
    private Context context;
    private ProgressDialog dialog;
    private String pg_commission, pg_convenience_fee,pg_id;
    private float total_amount;
    ArrayList<DataStructureFramwork.GatewayData> lstPg_data;
    DataStructureFramwork.StudentProfileInfo info;
    float amt_by_user ;
    float comm_percent ;
    float pg_convenience_value ;
    float comm_amount;
    private ArrayList<PaymentHistory> paymentHistory = new ArrayList<>();
    private ListView listviewPaymentHistory;
    private PaymentHistoryDetailsAdapter adapter;
    private RelativeLayout bodylayout,header_layout;
    private LinearLayout feelayout;
    private ImageButton btnBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.aims_manage_fee_view, container, false);
        initialize();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //initialize();
        lbl_commission.setVisibility(View.GONE);
        commission.setVisibility(View.GONE);
        lbl_convenience.setVisibility(View.GONE);
        convenience.setVisibility(View.GONE);
        lbl_total_amnt.setVisibility(View.GONE);
        total_amnt.setVisibility(View.GONE);
        amount.setText("");
        aims_fee_type.setText("");
        //reg_no.setText("");
    }

    public AimsManageFeesFragment(int type) {
        super();
        // TODO Auto-generated constructor stub
        this.type = type;
    }

    private void initialize() {
        JSONObject c = null;
        context = getActivity();
        student_name = (TextView) view.findViewById(R.id.stduent_name);
        class_name = (TextView) view.findViewById(R.id.class_name);
        section_name = (TextView) view.findViewById(R.id.section_name);
        lbl_commission = (TextView) view.findViewById(R.id.lbl_commission);
        commission = (TextView) view.findViewById(R.id.commission);
        lbl_convenience = (TextView) view.findViewById(R.id.lbl_convenience);
        convenience = (TextView) view.findViewById(R.id.convenience);
        lbl_total_amnt = (TextView) view.findViewById(R.id.lbl_total_amnt);
        total_amnt = (TextView) view.findViewById(R.id.total_amnt);
        amount = (EditText) view.findViewById(R.id.amount);
        reg_no = (EditText) view.findViewById(R.id.reg_number);
        aims_fee_type=(EditText) view.findViewById(R.id.aims_fee_type);
        btn_pay = (TextView) view.findViewById(R.id.btn_pay);
        btn_view_image = (TextView) view.findViewById(R.id.btn_view_image);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
        );
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(context,
                        Constants.UORGANIZATIONID)));
        new GetCommission().execute(nameValuePairs);


        try {
            c = new JSONObject(SchoolAppUtils.GetSharedParameter(getActivity(),
                    Constants.SELECTED_CHILD_OBJECT));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c != null) {
            info = new DataStructureFramwork.StudentProfileInfo(c);
            student_name.setText(info.fullname);
            class_name.setText(info.classs);
            section_name.setText(info.section);

        }

        amount.addTextChangedListener(new TextChangedListener<EditText>(amount) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if(amount.getText().toString().length()>0){

                    //lbl_commission.setVisibility(View.VISIBLE);
                    //commission.setVisibility(View.VISIBLE);
                    //lbl_convenience.setVisibility(View.VISIBLE);
                    //convenience.setVisibility(View.VISIBLE);
                    lbl_total_amnt.setVisibility(View.VISIBLE);
                    total_amnt.setVisibility(View.VISIBLE);
                    amt_by_user = Float.parseFloat(amount.getText().toString());
                    if(pg_commission!=null)
                        comm_percent = Float.parseFloat(pg_commission);
                    else
                        comm_percent=0;

                    if(pg_convenience_fee!=null)
                        pg_convenience_value = Float.parseFloat(pg_convenience_fee);
                    else
                        pg_convenience_value=0;


                    //comm_amount = (amt_by_user * comm_percent)/100;
                    //total_amount = amt_by_user + comm_amount + pg_convenience_value;
                    total_amount = amt_by_user;
                    //convenience.setText(pg_convenience_fee);
                    //commission.setText(Float.toString(comm_amount));
                    total_amnt.setText(Float.toString(total_amount));

                }else{
                    lbl_commission.setVisibility(View.GONE);
                    commission.setVisibility(View.GONE);
                    lbl_convenience.setVisibility(View.GONE);
                    convenience.setVisibility(View.GONE);
                    lbl_total_amnt.setVisibility(View.GONE);
                    total_amnt.setVisibility(View.GONE);
                    //amount.setVisibility(View.GONE);

                }
            }
        });


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int errorCount = 0;
                if (amount.getText().toString().isEmpty()) {
                    amount.setError("Enter Amount");
                    amount.requestFocus();
                    errorCount++;
                } else {
                    amount.setError(null);
                }
                if (reg_no.getText().toString().isEmpty()) {
                    reg_no.setError("Enter Registration Number");
                    reg_no.requestFocus();
                    errorCount++;
                } else {
                    reg_no.setError(null);
                }
                if (aims_fee_type.getText().toString().isEmpty()) {
                    aims_fee_type.setError("Enter fee type");
                    aims_fee_type.requestFocus();
                    errorCount++;
                } else {
                    aims_fee_type.setError(null);
                }

                if(errorCount == 0) {

                    if(SchoolAppUtils.isOnline(getActivity())
                            && pg_convenience_fee != null && pg_commission !=null) {

                        List<NameValuePair> nvp = new ArrayList<NameValuePair>(
                        );
                        nvp.add(new BasicNameValuePair("parent_id", SchoolAppUtils.GetSharedParameter(context,
                                Constants.USERID)));
                        nvp.add(new BasicNameValuePair("student_id", SchoolAppUtils
                                .GetSharedParameter(context,
                                        Constants.CHILD_ID)));
                        nvp.add(new BasicNameValuePair("pg_master_id","3"));
                        nvp.add(new BasicNameValuePair("registration_num",
                                reg_no.getText().toString()));
                        nvp.add(new BasicNameValuePair("amount",
                                Float.toString(total_amount)));
                        nvp.add(new BasicNameValuePair("fee_type",
                                aims_fee_type.getText().toString()));
                        nvp.add(new BasicNameValuePair("orig_amount", amount.getText().toString()));
                        nvp.add(new BasicNameValuePair("organization_id",
                                SchoolAppUtils.GetSharedParameter(context,
                                        Constants.UORGANIZATIONID)));
                        new GetCCavenueInformation().execute(nvp);
                    }else{
                        SchoolAppUtils.showDialog(getActivity(), "Alert",
                                "Connectivity problem. Please try again.");

                    }

                }


            }
        });
        //to show documents uploaded by school
        btn_view_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>(
                );
                nvps.add(new BasicNameValuePair("child_id", SchoolAppUtils
                        .GetSharedParameter(context,
                                Constants.CHILD_ID)));
                new showDocument().execute(nvps);

            }
        });

        List<NameValuePair> nvpair = new ArrayList<NameValuePair>(
        );
        nvpair.add(new BasicNameValuePair("user_id", SchoolAppUtils
                .GetSharedParameter(context,
                        Constants.CHILD_ID)));
        nvpair.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(context,
                        Constants.UORGANIZATIONID)));
        new GetRegistrationNumber().execute(nvpair);
        //----------------view history------------------//
        listviewPaymentHistory = (ListView) view.findViewById(R.id.listviewPaymentHistory);
        emptyView = (TextView) view.findViewById(R.id.emptyView);
        btnPaymentHistory= (TextView) view.findViewById(R.id.btnPaymentHistory);
        bodylayout= (RelativeLayout) view.findViewById(R.id.bodylayout);
        header_layout= (RelativeLayout) view.findViewById(R.id.header_layout);
        feelayout= (LinearLayout) view.findViewById(R.id.feelayout);
        btnBack = (ImageButton) view.findViewById(R.id.back_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodylayout.setVisibility(View.GONE);
                header_layout.setVisibility(View.GONE);
                feelayout.setVisibility(View.VISIBLE);

            }
        });

        btnPaymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requetsForPaymentHistory();
            }
        });



    }




    @Override
    public void onResume() {
        super.onResume();
        //requetsForPaymentHistory();
    }
    /*------view history-----------------------*/
    private void requetsForPaymentHistory() {
        bodylayout.setVisibility(View.VISIBLE);
        header_layout.setVisibility(View.VISIBLE);
        feelayout.setVisibility(View.GONE);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
        nameValuePairs.add(
                new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
        new RequestPaymentHistoryDetailsAsyntask().execute(nameValuePairs);
    };

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressLint("NewApi")
    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    private class RequestPaymentHistoryDetailsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

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
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_get_payment_history_details_new);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);

            paymentHistory.clear();
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_get_payment_history_details_new);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = json.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            PaymentHistory payment = new PaymentHistory(array.getJSONObject(i));
                            paymentHistory.add(payment);
                        }

                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                            Log.d("error", error);
                        } catch (Exception e) {
                        }
                        return false;
                    }

                } else {
                    //error = getResources().getString(R.string.unknown_response);
                    error = "No payment made yet";
                    Log.d("error1", error);
                }

            } catch (

                    JSONException e) {

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
                if (paymentHistory != null) {
                    emptyView.setVisibility(View.GONE);
                    adapter = new PaymentHistoryDetailsAdapter(getActivity(), paymentHistory);
                    listviewPaymentHistory.setAdapter(adapter);
                }
            } else {
                //Log.d("error2", error);
                if (error.length() > 0) {
                    Log.d("error3", error);
                    emptyView.setVisibility(View.VISIBLE);
                    //SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), error);
                } else {
                    Log.d("error4","error");
                    SchoolAppUtils.showDialog(getActivity(), getActivity()
                                    .getTitle().toString(),
                            getResources().getString(R.string.error));
                }

            }
        }

    }

    private class GetCommission extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
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
                            context, Constants.COMMON_URL)
                            + Urls.api_ais_getCommission);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        lstPg_data = new ArrayList<DataStructureFramwork.GatewayData>();
                        lstPg_data.clear();
                        JSONArray array;
                        try {
                            array = json.getJSONArray("data");
                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }

                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.GatewayData pg_data = new DataStructureFramwork.GatewayData(
                                    array.getJSONObject(i));
                            lstPg_data.add(pg_data);
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
            if(result){
                pg_convenience_fee = lstPg_data.get(0).pg_convenience_fee;
                pg_commission = lstPg_data.get(0).pg_commission;
                pg_id = lstPg_data.get(0).pg_master_id;
            }else {

                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(context,
                            "Registration", error);
                } else {
                    SchoolAppUtils.showDialog(context,
                            "Registration",
                            getResources().getString(R.string.error));
                }

            }


        }



    }

    public abstract class TextChangedListener<T> implements TextWatcher {
        private T target;

        public TextChangedListener(T target) {
            this.target = target;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target, s);
        }

        public abstract void onTextChanged(T target, Editable s);
    }


    private class GetCCavenueInformation extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        JSONObject data_obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
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
                            context, Constants.COMMON_URL)
                            + Urls.api_ais_getCcavenueDetails);
            Log.d("paramaims",namevaluepair.toString());
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        try {
                            data_obj = json.getJSONObject("data");
                            Log.d("dataobj>>>>>>>",data_obj.toString());
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
            String acc_code="", marchent_id = "", order_id= "", currency ="",
                    amount="", re_url="", cancel_url="", rsa_url="", marchent_param1 = "", marchent_param2 = "", billing_country="";
            try {
                acc_code = data_obj.getString("access_code").trim();
                marchent_id= data_obj.getString("merchant_id").trim();
                order_id= data_obj.getString("order_id").trim();
                currency= data_obj.getString("currency").trim();
                amount= data_obj.getString("amount").trim();
                re_url= data_obj.getString("redirect_url").trim();
                cancel_url= data_obj.getString("cancel_url").trim();
                rsa_url= data_obj.getString("RSA_key_url").trim();
                marchent_param1 = data_obj.getString("merchant_param1");
                marchent_param2 = data_obj.getString("merchant_param2");
                billing_country = data_obj.getString("billing_country");
                Log.d("biilingcountry",billing_country);

            } catch (Exception e) {
            }
            Intent intent = new Intent(context,PaymentWebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(acc_code).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(marchent_id).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(order_id).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency).toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount).toString().trim());

            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(re_url).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancel_url).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsa_url).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_PARAM1, ServiceUtility.chkNull(marchent_param1).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_PARAM2, ServiceUtility.chkNull(marchent_param2).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, ServiceUtility.chkNull(billing_country).toString().trim());
            //intent.putExtra("callType","ais");
            //intent.putExtra("callType","aims");

            startActivity(intent);

        }
    }

    //----------------get regno and set reg no--------------------//
    private class GetRegistrationNumber extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        String data_obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
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
                            context, Constants.COMMON_URL)
                            + Urls.api_get_registration_no_mobile);
            Log.d("paramaims",namevaluepair.toString());
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {


                        try {
                            data_obj = json.getString("data");
                            Log.d("dataobj", data_obj);
                            //Log.d("dataobj>>", "dataobj");
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
            reg_no.setText(data_obj);

        }
    }

    //----------------get doc and show doc--------------------//
    private class showDocument extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        JSONObject data_obj;
        //String data_obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
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
                            context, Constants.COMMON_URL)
                            + Urls.api_get_documents_aims_fee);
            String url= SchoolAppUtils.GetSharedParameter(context, Constants.COMMON_URL)+ Urls.api_get_documents_aims_fee;
            Log.d("urldd>>>>>>>",url);
            Log.d("paramaims",namevaluepair.toString());
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        try {
                            data_obj = json.getJSONObject("data");
                            Log.d("dataobj>>>>>>>>>>>", data_obj.toString());
                            Log.d("dataobj>>>>>", "dataobj");

                        } catch (Exception e) {
                            error = "Error in Data";
                            return false;
                        }


                        return true;
                    } else {
                        try {
                            error = json.getString("data");
                            Log.d("error>>>",error);
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
            if(data_obj!=null) {
                try {
                    final String docid = data_obj.getString("id").trim();
                    Log.d("id",docid);
                    //showGetDocDialog(docid);
                    Intent my_intent = new Intent(context, ShowAttachmentFeesActivity.class);
                    my_intent.putExtra("id",data_obj.getString("id").trim());
                    my_intent.putExtra("file_name",data_obj.getString("attachment").trim());
                    context.startActivity(my_intent);
                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                }
            }
            else {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage(error);
                builder1.setCancelable(true);
                builder1.setNegativeButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }

    /*private void showGetDocDialog(final String docid){
        final String file_id = docid;
        new AlertDialog.Builder(context)
                .setTitle("Select option")
                .setPositiveButton("View Document",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with view
                                Intent intent = new Intent(context, WebViewActivity.class);
                                //Log.d("url viewdoc>>>>>",Urls.api_download_fee_doc+ File.separator+filename);
                                String url= Urls.api_download_fee_doc + "/" + file_id;;
                                Log.d("url viewdoc>>>>>",url);
                                intent.putExtra("url", url);
                                context.startActivity(intent);
                            }
                        })
                .setNegativeButton("Download",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // download only PDF files as report
                                final DownloadTask downloadTask = new DownloadTask(context, file_id);
                                String url=Urls.api_download_fee_doc + "/" + file_id;;
                                Log.d("url viewdoc>>>>>",url);
                                downloadTask.execute(url);

                            }
                        }).setIcon(android.R.drawable.ic_dialog_info).show();
    }*/
}
