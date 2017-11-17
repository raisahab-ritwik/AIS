package com.knwedu.ourschool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;

/**
 * Created by ddasgupta on 1/25/2017.
 */

public class StatusActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_status);

        Intent mainIntent = getIntent();
        TextView tv4 = (TextView) findViewById(R.id.textView1);
        tv4.setText(mainIntent.getStringExtra("transStatus"));
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
}