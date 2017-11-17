package com.knwedu.ourschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.comschoolapp.R;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddasgupta on 3/7/2017.
 */

public class AimsPaymentFeeDetailsActivity extends Activity {

    private TableLayout table_layout;
    private TextView tvName, tvClass, tvDueDate, lblCommission, lblConvenience, lblTotalPaid;
    private ProgressDialog dialog;
    private TextView header;
    private String page_title = "";
    private String Url;
    private int type;
    private ArrayList<DataStructureFramwork.GatewayDetails> gatewayDetails = new ArrayList<DataStructureFramwork.GatewayDetails>();
    private ArrayList<DataStructureFramwork.PaymentReminderDeatils> paymentReminder = new ArrayList<DataStructureFramwork.PaymentReminderDeatils>();
    private TableLayout t1;

    private String fees_total, fine_total, total_amount,scholarship_amount;
    private String payt_STATUS;
    private String checksum_hash;
    private String reminderId;
    private String orderId;
    private LinearLayout llPaymentGateways;
    private static final String TAG = "PayUMoneySDK ";
    private Button btnPayment;
    private int payment_type;
    private int class_id;
    float sum = 0;
    public static Activity self_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aims_reminder_details_activity);
        self_intent =this;
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        llPaymentGateways = (LinearLayout) findViewById(R.id.llPaymentGateways);
        tvName = (TextView) findViewById(R.id.tvName);
        tvClass = (TextView) findViewById(R.id.tvClass);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        lblCommission = (TextView) findViewById(R.id.lblCommission);
        lblConvenience = (TextView) findViewById(R.id.lblConvenience);
        lblTotalPaid = (TextView) findViewById(R.id.lblTotalPaid);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
        reminderId = getIntent().getStringExtra("Reminder_id");
        class_id = Integer.parseInt(getIntent().getStringExtra("class_id"));
        header = (TextView) findViewById(R.id.header_text);
        header.setText(page_title);
        type = getIntent().getExtras().getInt("Type");
        requetsForRemindersDetails();



        ((ImageButton) findViewById(R.id.back_btn)).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                onBackPressed();

            }

        });











        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("organization_id",
                        SchoolAppUtils.GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.UORGANIZATIONID)));

                nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                        .GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.USERID)));
                if(SchoolAppUtils
                        .GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.USERTYPEID).equals("5")) {
                    nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils.GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.CHILD_ID)));
                }else{
                    nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                            .GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.USERID)));
                }
                nameValuePairs.add(new BasicNameValuePair("fees_due_date", paymentReminder.get(0).fees_due_date));
                nameValuePairs.add(new BasicNameValuePair("class_id", class_id+""));
                nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils
                        .GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.USERTYPEID)));
                nameValuePairs.add(new BasicNameValuePair("pg_master_id",gatewayDetails.get(0).gateway_id ));

                new GetCCavenueInformation().execute(nameValuePairs);



            }
        });





    }





    private void requetsForRemindersDetails() {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(this, Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(this, Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("user_id",
                SchoolAppUtils.GetSharedParameter(this, Constants.USERID)));
        nameValuePairs.add(new BasicNameValuePair("reminder_id", reminderId));
        if (type == 1) {
            nameValuePairs.add(
                    new BasicNameValuePair("child_id", SchoolAppUtils.GetSharedParameter(this, Constants.CHILD_ID)));
        }

        new RequestPaymentReminderDetailsAsyntask().execute(nameValuePairs);

    }



    private class RequestPaymentReminderDetailsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {

        String error = "";

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AimsPaymentFeeDetailsActivity.this);
            dialog.setTitle(page_title);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override

        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:

            Log.d("url extension: ", Urls.api_get_payment_details);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);
            paymentReminder.clear();
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_get_payment_details);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONObject jsonObj = json.getJSONObject("data");
                        JSONArray array = jsonObj.getJSONArray("fees_details");
                        for (int i = 0; i < array.length(); i++) {
                            DataStructureFramwork.PaymentReminderDeatils reminder = new DataStructureFramwork.PaymentReminderDeatils(array.getJSONObject(i));
                            paymentReminder.add(reminder);
                        }
                        fees_total = jsonObj.getString("fees_amount");
                        fine_total = jsonObj.getString("total_fine_amount");
                        total_amount = jsonObj.getString("total_payable_amount");
                        scholarship_amount = jsonObj.getString("fees_scholarship_amount");
                        JSONArray gatewayArray = jsonObj.getJSONArray("gateway_details");
                        //Clear array values
                        gatewayDetails.clear();
                        //bitmaps.clear();
                        for (int j = 0; j < gatewayArray.length(); j++) {
                            DataStructureFramwork.GatewayDetails gateway = new DataStructureFramwork.GatewayDetails(gatewayArray.getJSONObject(j));
                            gatewayDetails.add(gateway);
                            //bitmaps.add(getBitmapFromURL(Urls.base_url+gateway.pg_image));
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
                table_layout.removeAllViews();
                // Create Table Header
                //tvName.setText(SchoolAppUtils.GetSharedParameter(PaymentFeesDetailsActivity.this, Constants.FULL_NAME));
                if (SchoolAppUtils.GetSharedParameter(AimsPaymentFeeDetailsActivity.this,
                        CollegeConstants.USERTYPEID).equalsIgnoreCase(
                        CollegeConstants.USERTYPE_PARENT)) {
                    tvName.setText(SchoolAppUtils.GetSharedParameter(
                            AimsPaymentFeeDetailsActivity.this, Constants.CHILD_NAME));
                    tvClass.setText(SchoolAppUtils.GetSharedParameter(
                            AimsPaymentFeeDetailsActivity.this, Constants.STUDENT_CLASS_SECTION));
                } else {
                    tvName.setText(SchoolAppUtils.GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.FULL_NAME));
                    tvClass.setText(SchoolAppUtils.GetSharedParameter(AimsPaymentFeeDetailsActivity.this, Constants.STUDENT_CLASS_SECTION));
                }
                if (paymentReminder.get(0).fees_due_date != null) {
                    tvDueDate.setText("Due Date : " + paymentReminder.get(0).fees_due_date);
                }
                //tvClass.setText(SchoolAppUtils.GetSharedParameter(PaymentFeesDetailsActivity.this, Constants.STUDENT_CLASS_SECTION));
                BuildTableHeader();
                if (paymentReminder != null && paymentReminder.size() > 0) {
                    // Create Table Body
                    buildBody();
                    if (!scholarship_amount.equalsIgnoreCase("0")) {
                        buildScholarShipRow();
                    }
                    // Create Table Footer
                    buildTableFooter();


                    lblCommission.setVisibility(View.VISIBLE);
                    lblConvenience.setVisibility(View.VISIBLE);
                    lblTotalPaid.setVisibility(View.VISIBLE);
                    float fees_value = (float)Float.parseFloat(fees_total);
                    float com_percentage = (float)Float.parseFloat(gatewayDetails.get(0).pg_commission);
                    float commision_value=(com_percentage/100)*fees_value;
                    float convenience_value= (float)Float.parseFloat(gatewayDetails.get(0).pg_convenience_fee);
                    sum =fees_value+convenience_value+commision_value;

                    lblCommission.setText("Commission("+gatewayDetails.get(0).pg_commission+"%) : "+commision_value);
                    lblConvenience.setText("Convenience Fee : "+convenience_value);
                    lblTotalPaid.setText("Total Payable Amount :"+sum);


                }
            }else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(AimsPaymentFeeDetailsActivity.this, page_title, error);
                } else {
                    SchoolAppUtils.showDialog(AimsPaymentFeeDetailsActivity.this, page_title,
                            getResources().getString(R.string.error));
                }
            }

        }
    }


    private void BuildTableHeader() {

        // outer for loop
        for (int i = 1; i <= 1; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setBackgroundResource(R.drawable.cell_shape);
            tv.setPadding(5, 5, 5, 5);
            tv.setText("Description");
            row.addView(tv);

            TextView tv1 = new TextView(this);
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setBackgroundResource(R.drawable.cell_shape);
            tv1.setPadding(5, 5, 5, 5);
            tv1.setText("Fees Amount");
            row.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setBackgroundResource(R.drawable.cell_shape);
            tv2.setPadding(5, 5, 5, 5);
            tv2.setText("Fines Amount");
            row.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setBackgroundResource(R.drawable.cell_shape);
            tv3.setPadding(5, 5, 5, 5);
            tv3.setText("Total Amount");
            row.addView(tv3);
            table_layout.addView(row);

        }
    }

    private void buildTableFooter() {

        // outer for loop
        for (int i = 1; i <= 1; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setBackgroundResource(R.drawable.cell_shape);
            tv.setPadding(5, 5, 5, 5);
            tv.setText("TOTAL");
            row.addView(tv);

            TextView tv1 = new TextView(this);
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setBackgroundResource(R.drawable.cell_shape);
            tv1.setPadding(5, 5, 5, 5);
            tv1.setText(fees_total);
            row.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setBackgroundResource(R.drawable.cell_shape);
            tv2.setPadding(5, 5, 5, 5);
            tv2.setText(fine_total);
            row.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setBackgroundResource(R.drawable.cell_shape);
            tv3.setPadding(5, 5, 5, 5);
            tv3.setText(total_amount);
            row.addView(tv3);
            table_layout.addView(row);

        }
    }

    private void buildBody() {
        // outer for loop
        for (int i = 1; i < paymentReminder.size() + 1; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setBackgroundResource(R.drawable.cell_shape);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(paymentReminder.get(i - 1).fees_name);
            row.addView(tv);

            TextView tv1 = new TextView(this);
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv1.setBackgroundResource(R.drawable.cell_shape);
            tv1.setPadding(5, 5, 5, 5);
            tv1.setText(paymentReminder.get(i - 1).fees_amount);
            row.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setBackgroundResource(R.drawable.cell_shape);
            tv2.setPadding(5, 5, 5, 5);
            tv2.setText(paymentReminder.get(i - 1).fine_amount);
            row.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setBackgroundResource(R.drawable.cell_shape);
            tv3.setPadding(5, 5, 5, 5);
            tv3.setText(paymentReminder.get(i - 1).sub_total);
            row.addView(tv3);
            table_layout.addView(row);

        }
    }

    private void buildScholarShipRow(){
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.cell_shape);
        tv.setPadding(5, 5, 5, 5);
        tv.setText("Scholarship");
        row.addView(tv);

        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv1.setBackgroundResource(R.drawable.cell_shape);
        tv1.setPadding(5, 5, 5, 5);
        tv1.setText("-");
        row.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv2.setBackgroundResource(R.drawable.cell_shape);
        tv2.setPadding(5, 5, 5, 5);
        tv2.setText("-");
        row.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv3.setBackgroundResource(R.drawable.cell_shape);
        tv3.setPadding(5, 5, 5, 5);
        tv3.setText(scholarship_amount);
        row.addView(tv3);
        table_layout.addView(row);
    }


    private class GetCCavenueInformation extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        ProgressDialog dialog;
        String error = "";
        JSONObject data_obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AimsPaymentFeeDetailsActivity.this);
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
                            AimsPaymentFeeDetailsActivity.this, Constants.COMMON_URL)
                            + Urls.api_aims_getCcavenueDetails);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {


                        try {
                            data_obj = json.getJSONObject("data");
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
            Intent intent = new Intent(AimsPaymentFeeDetailsActivity.this,PaymentWebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(acc_code).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(marchent_id).toString().trim());
            intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(order_id).toString().trim());
            intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency).toString().trim());
            intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(sum).toString().trim());
            //intent.put

            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(re_url).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancel_url).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsa_url).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_PARAM1, ServiceUtility.chkNull(marchent_param1).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_PARAM2, ServiceUtility.chkNull(marchent_param2).toString().trim());
            intent.putExtra("callType","aims");

            startActivity(intent);

        }
    }
}
