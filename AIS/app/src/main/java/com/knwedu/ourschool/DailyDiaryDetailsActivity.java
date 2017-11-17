package com.knwedu.ourschool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

/**
 * Created by ddasgupta on 28/03/17.
 */

public class DailyDiaryDetailsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assignment);
        //page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
        SchoolAppUtils.loadAppLogo(DailyDiaryDetailsActivity.this, (ImageButton) findViewById(R.id.app_logo));

        initialize();
    }

    private void initialize() {

        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        //title = (TextView) findViewById(R.id.title_txt);

    }

}
