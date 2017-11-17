package com.knwedu.ourschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ddasgupta on 29/03/17.
 */

public class AisTeacherDailyDiaryDetailActivity extends Activity {
    private TextView title, date, description, header, marks, code,assigned_date_main_txt,title_marks_txt;
    private DataStructureFramwork.Assignment test;
    private Button addResult, getdoc;
    private DataStructureFramwork.Subject subject;
    private Button btnPublish;
    private ProgressDialog dialog;
    AlertDialog.Builder dialoggg;
    int row_id;
    String subject_id;
    public DatabaseAdapter mDatabase;
    private boolean internetAvailable = false;
    private String page_title = "";
    String dnldUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_teacher_assignment_detail);
        page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
        SchoolAppUtils.loadAppLogo(this,
                (ImageButton) findViewById(R.id.app_logo));


        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initialize() {
        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        title = (TextView) findViewById(R.id.title_txt);
        date = (TextView) findViewById(R.id.assignment_due_txt);
        description = (TextView) findViewById(R.id.assignment_txt);
        code = (TextView) findViewById(R.id.code_txt);
        header = (TextView) findViewById(R.id.header_text);
        marks = (TextView) findViewById(R.id.marks_txt);
        header.setText(page_title);
        getdoc = (Button) findViewById(R.id.btn_graph);
        addResult = (Button) findViewById(R.id.add_results_btn);
        btnPublish = (Button) findViewById(R.id.assignment_btn_publish);
        assigned_date_main_txt = (TextView)findViewById(R.id.assigned_date_main_txt);
        title_marks_txt = (TextView)findViewById(R.id.title_marks_txt);
        title_marks_txt.setVisibility(View.INVISIBLE);
        addResult.setVisibility(View.INVISIBLE);
        btnPublish.setVisibility(View.GONE);
        assigned_date_main_txt.setVisibility(View.INVISIBLE);

        String temp = getIntent().getExtras().getString(
                Constants.ASSIGNMENT);
        if (temp != null) {
            JSONObject object = null;
            try {
                object = new JSONObject(temp);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (object != null) {
                test = new DataStructureFramwork.Assignment(object);
            }
        }
        /*
        if (getIntent().getExtras() != null) {
            temp = getIntent().getExtras().getString(Constants.SUBJECT);
            if (temp != null) {
                JSONObject object = null;
                try {
                    object = new JSONObject(temp);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (object != null) {
                    subject = new DataStructureFramwork.Subject(object);
                }
            }

        }*/

        if (test != null) {
            code.setText(test.sub_code);
            title.setText(test.title);
            date.setText(test.created_date.replace("-", "/"));
            description.setText(test.description);


        }

        dnldUrl = SchoolAppUtils.GetSharedParameter(AisTeacherDailyDiaryDetailActivity.this,
                Constants.COMMON_URL ) + Urls.api_ais_daily_diary_download;

        if (!test.file_name.equals("null") && !test.attachment.equals("null")
                ) {
            getdoc.setVisibility(View.VISIBLE);
            getdoc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(AisTeacherDailyDiaryDetailActivity.this)
                            .setTitle("Select option")
                            .setPositiveButton("View Document",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // continue with view
                                            SchoolAppUtils
                                                    .imagePdfViewDocument(
                                                            AisTeacherDailyDiaryDetailActivity.this,
                                                            dnldUrl + "/"
                                                                    + test.id,test.file_name);
                                        }
                                    })
                            .setNegativeButton("Download",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // download

                                            final DownloadTask downloadTask = new DownloadTask(AisTeacherDailyDiaryDetailActivity.this, test.file_name);
                                            downloadTask.execute(dnldUrl + "/"
                                                    + test.id);



											/*Intent i = new Intent(
													Intent.ACTION_VIEW);
											i.setData(Uri
													.parse(Urls.api_get_doc
															+ test.id));
											startActivity(i);*/

                                        }
                                    })
                            .setIcon(android.R.drawable.ic_dialog_info).show();

                }
            });
        }else{
            getdoc.setVisibility(View.INVISIBLE);
        }

    }

}
