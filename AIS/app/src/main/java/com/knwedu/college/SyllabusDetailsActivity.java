package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.adapter.CollegeAnnouncementAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Announcement;
import com.knwedu.college.utils.CollegeDataStructureFramwork.SpecialClass;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.TeacherAssignmentDetailActivity;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class SyllabusDetailsActivity extends FragmentActivity {

	private ProgressDialog dialog;
	private TextView header;
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.syllabus_details_view);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		header = (TextView) findViewById(R.id.header_text);
		Button btnGotDoc = (Button) findViewById(R.id.download_btn);
		if (!getIntent().getStringExtra("sub_attachment").isEmpty()) {
			btnGotDoc.setVisibility(View.VISIBLE);
		} else {
			btnGotDoc.setVisibility(View.GONE);
		}

		btnGotDoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String syllabus = "Syllabus";
				String url = "";
				/**
				 * https://knweducollege.knwedu.com/mobile/download/2097/9/Syllabus
				 * CollegeUrls.api_get_doc = mobile/download
				 * id = 2097 (user_id)
				 * syllabus_id = getIntent().getStringExtra("syl_id")
				 * syllabus_name = Syllabus
				 */
				url = CollegeUrls.base_url
						+ CollegeUrls.api_get_doc
						+ "/"
						+ CollegeAppUtils.GetSharedParameter(
						SyllabusDetailsActivity.this,
						"id") + "/" + getIntent().getStringExtra("sub_id")
						+ "/" + syllabus+"/"+getIntent().getStringExtra("sub_attachment");
				showGetDocDialog(url);
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

	private void showGetDocDialog(final String url) {
		new AlertDialog.Builder(SyllabusDetailsActivity.this)
				.setTitle("Select option")
				.setPositiveButton("View Document",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								// continue with view
								System.out.println("Previous URL"+url);
								SchoolAppUtils
										.imagePdfViewDocument(
												SyllabusDetailsActivity.this,
												url, getIntent()
														.getStringExtra("sub_attachment"));
							}
						})
				.setNegativeButton("Download",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								// download
								final DownloadTask downloadTask = new DownloadTask(
										SyllabusDetailsActivity.this,
										getIntent()
												.getStringExtra("sub_attachment"));
								downloadTask.execute(url);

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
		textViewProgram.setText(getIntent().getStringExtra("sub_title"));
		TextView textViewSession = (TextView) findViewById(R.id.textViewDesc);
		textViewSession.setText(getIntent().getStringExtra("sub_desc"));
	}

}
