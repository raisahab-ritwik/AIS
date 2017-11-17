package com.knwedu.college;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.PaymentReminderAdapter;
import com.knwedu.ourschool.bo.PayTMResponse;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.GatewayDetails;
import com.knwedu.ourschool.utils.DataStructureFramwork.PayTMDetails;
import com.knwedu.ourschool.utils.DataStructureFramwork.PayUMoneyDetails;
import com.knwedu.ourschool.utils.DataStructureFramwork.PayUMoneyResponse;
import com.knwedu.ourschool.utils.DataStructureFramwork.PaymentReminderDeatils;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentFeesDetailsActivity extends Activity {

    /*private TableLayout table_layout;

    private TextView tvName, tvClass, tvDueDate, lblCommission, lblConvenience, lblTotalPaid;

    private ProgressDialog dialog;

    private TextView header;

    private String page_title = "";

    private String Url;

    private int type;

    private ArrayList<PaymentReminderDeatils> paymentReminder = new ArrayList<PaymentReminderDeatils>();

    private ArrayList<GatewayDetails> gatewayDetails = new ArrayList<GatewayDetails>();

    private ArrayList<PayUMoneyDetails> paymoneydetails = new ArrayList<PayUMoneyDetails>();

    private ArrayList<PayTMDetails> paytmdetails = new ArrayList<PayTMDetails>();

    private ArrayList<PayUMoneyResponse> payumoneyresponses = new ArrayList<PayUMoneyResponse>();

    private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

    private ListView reminderList;

    private PaymentReminderAdapter adapter;

    private TableLayout t1;

    private String fees_total, fine_total, total_amount, scholarship_amount;

    private String payt_STATUS;

    private String checksum_hash;

    private String reminderId;

    private String orderId;

    private LinearLayout llPaymentGateways;

    private HashMap<String, String> params = new HashMap<>();

    private static final String TAG = "PayUMoneySDK ";

    private RadioGroup rg;

    private Button btnPayment;

    private int payment_type;

    private Intent payUMoneyResponse;

    private PayTMResponse payTMResponse = new PayTMResponse();

    private int class_id;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reminder_details_activity);



    }

    /**
     * Calling
     * https://kolkataschool.knwedu.com/mobile/feePaymentGenerateChecksumMobile
     * by passing the following parameters
     */

    private void requetsForChecksumForPayTM() {


    }

    private void requetsForChecksumForPayUMoney(String id) {




    }

    private void requetsForPayTMDetails() {



    }

    private void sendingPayumoneyResponse(int paymentType) {


    }

    /**
     * Urls.api_payment_generate_checksum =
     * "mobile/feePaymentGenerateChecksumMobile"
     *
     * @author Rajhrita
     */



    /**
     * Urls.api_payment_generate_checksum_payumoney =
     * "mobile/getpayumoney"
     *
     * @author Rajhrita
     */



}