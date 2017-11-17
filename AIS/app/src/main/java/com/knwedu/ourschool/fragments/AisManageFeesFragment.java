package com.knwedu.ourschool.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.PaymentWebViewActivity;
import com.knwedu.ourschool.ccavenue.AvenuesParams;
import com.knwedu.ourschool.ccavenue.ServiceUtility;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddasgupta on 1/20/2017.
 */

public class AisManageFeesFragment extends Fragment {
    private View view;
    private int type;
    private TextView student_name,class_name,section_name,lbl_commission,
            commission,lbl_convenience, convenience,lbl_total_amnt,total_amnt,btn_pay;
    private EditText ais_fee_type,reg_no, amount;
    private Context context;
    private String pg_commission, pg_convenience_fee,pg_id;
    private float total_amount;
    ArrayList<DataStructureFramwork.GatewayData> lstPg_data;
    DataStructureFramwork.StudentProfileInfo info;
    float amt_by_user ;
    float comm_percent ;
    float pg_convenience_value ;
    float comm_amount;
    private String appType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ais_manage_fee_view, container, false);
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
        reg_no.setText("");
    }





    public AisManageFeesFragment(int type) {
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
        ais_fee_type=(EditText) view.findViewById(R.id.ais_fee_type);
        btn_pay = (TextView) view.findViewById(R.id.btn_pay);
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

                    lbl_commission.setVisibility(View.VISIBLE);
                    commission.setVisibility(View.VISIBLE);
                    lbl_convenience.setVisibility(View.VISIBLE);
                    convenience.setVisibility(View.VISIBLE);
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


                    comm_amount = (amt_by_user * comm_percent)/100;
                    total_amount = amt_by_user + comm_amount + pg_convenience_value;
                    convenience.setText(pg_convenience_fee);
                    commission.setText(Float.toString(comm_amount));
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
                                ais_fee_type.getText().toString()));
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
        /*if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
            Log.d("apptype>>",Constants.APP_TYPE_COMMON_STANDARD);
        }*/



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
            Log.d("param",namevaluepair.toString());
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {


                        try {
                            data_obj = json.getJSONObject("data");
                            Log.d("dataobj",data_obj.toString());
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
                    amount="", re_url="", cancel_url="", rsa_url="", marchent_param1 = "", marchent_param2 = "";
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
            //intent.putExtra("callType","ais");
            //intent.putExtra("callType","aims");

            startActivity(intent);

        }
    }





    }
