package com.knwedu.ourschool;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CampusDetailsActivity extends FragmentActivity {

	private ProgressDialog dialog;
	private TextView header;
	private String page_title = "";
	private String Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.campus_details_view);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		header = (TextView) findViewById(R.id.header_text);
		Button btnGotDoc = (Button) findViewById(R.id.download_btn);
		if (getIntent().getStringExtra("campus_attachment").equalsIgnoreCase("null")) {
			btnGotDoc.setVisibility(View.GONE);
		} else {
			btnGotDoc.setVisibility(View.VISIBLE);
		}

		btnGotDoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String syllabus = "syllabus";
				/**
				 * https://knweducollege.knwedu.com/mobile/download/2097/9/Syllabus
				 * CollegeUrls.api_get_doc = mobile/download
				 * id = 2097 (user_id)
				 * syllabus_id = getIntent().getStringExtra("syl_id")
				 * syllabus_name = Syllabus
				 */
				if(SchoolAppUtils.GetSharedParameter(CampusDetailsActivity.this, Constants.INSTITUTION_TYPE).equalsIgnoreCase("school")){
					Url = Urls.api_get_campus_doc+"/"
							+ getIntent().getStringExtra("campus_id");
				}else{
					Url = CollegeUrls.base_url + CollegeUrls.api_get_doc + "/"
							+ CollegeAppUtils.GetSharedParameter(CampusDetailsActivity.this, "id") + "/" + getIntent().getStringExtra("campus_id")
							+ "/Campus";
				}

				System.out.println("URL"+Url);
				showGetDocDialog();
			}
		});

		header.setText(page_title);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
	};
	
	private void showGetDocDialog() {
		new AlertDialog.Builder(CampusDetailsActivity.this)
				.setTitle("Select option")
				.setPositiveButton("View Document",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with view
								SchoolAppUtils
										.imagePdfViewDocument(
												CampusDetailsActivity.this,
												Url, getIntent()
														.getStringExtra("campus_attachment"));
							}
						})
				.setNegativeButton("Download",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// download
								final DownloadTask downloadTask = new DownloadTask(
										CampusDetailsActivity.this,
										getIntent()
												.getStringExtra("campus_attachment"));
								downloadTask.execute(Url);

							}
						}).setIcon(android.R.drawable.ic_dialog_info).show();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("Google Analytics", "Tracking Start");
		EasyTracker.getInstance(this).activityStart(this);

	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("Google Analytics", "Tracking Stop");
		EasyTracker.getInstance(this).activityStop(this);
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	private void initialize() {

		TextView textViewProgram = (TextView) findViewById(R.id.textViewTitle);
		textViewProgram.setText(getIntent().getStringExtra("campus_title"));
		TextView textViewSession = (TextView) findViewById(R.id.textViewDesc);
		textViewSession.setText(getIntent().getStringExtra("campus_description"));
		TextView textViewCreatedBy = (TextView) findViewById(R.id.textViewCreatedBy);
		textViewCreatedBy.setText(getIntent().getStringExtra("campus_created_by"));
		TextView textViewCreatedOn = (TextView) findViewById(R.id.textViewCreatedOn);
		textViewCreatedOn.setText(getIntent().getStringExtra("campus_created_on"));
	}

}
