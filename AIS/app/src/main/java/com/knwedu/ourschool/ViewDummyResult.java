package com.knwedu.ourschool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ViewDummyResult extends AppCompatActivity {
    private String term_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dummy_result);
        Intent in = getIntent();
        term_id = in.getStringExtra("term_id");

        SchoolAppUtils
                .viewDocument(
                ViewDummyResult.this,
                Urls.base_url
                        + Urls.api_report_card_download_mobile
                        + "/"
                        + term_id
                        + "/"
                        + SchoolAppUtils
                        .GetSharedParameter(
                                ViewDummyResult.this,
                                Constants.UORGANIZATIONID)
                        + "/"
                        + SchoolAppUtils
                        .GetSharedParameter(
                                ViewDummyResult.this,
                                Constants.CHILD_ID));

    }
}
