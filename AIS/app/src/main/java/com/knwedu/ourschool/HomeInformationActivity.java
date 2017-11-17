package com.knwedu.ourschool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;

/**
 * Created by ddasgupta on 30/03/17.
 */

public class HomeInformationActivity extends Activity {
    private TextView header, tutorial, policy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_information);

        header = (TextView) findViewById(R.id.header_text);
        //header.setText("Alert Points");

        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        initialize();
    }
    private void initialize() {

        tutorial = (TextView)findViewById(R.id.tutorial);
        policy = (TextView)findViewById(R.id.policy);

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeInformationActivity.this,
                        TutorialActivity.class));
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeInformationActivity.this,
                        PrivacyPolicyActivity.class));
            }
        });


    }


}
