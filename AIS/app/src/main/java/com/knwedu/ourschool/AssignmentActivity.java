package com.knwedu.ourschool;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class AssignmentActivity extends Activity {
	private TextView title, date, description, header, marks, comments,
			assigned_marks,comment_main_txt, lbl_submission,lbl_assigned, sub_date;
	private Assignment assignment;
	private Button getDocBtn;
	private ImageButton graphBtn;
	private int assignment_type;
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_assignment);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(AssignmentActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
		initialize();
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
	}

	private void initialize() {

		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		Log.d("annc msg>>>>>>>","ss");
		title = (TextView) findViewById(R.id.title_txt);
		date = (TextView) findViewById(R.id.assignment_due_txt);
		description = (TextView) findViewById(R.id.assignment_txt);
		header = (TextView) findViewById(R.id.header_text);
		marks = (TextView) findViewById(R.id.assigned_marks_txt);
		comments = (TextView) findViewById(R.id.comment_txt);
		getDocBtn = (Button) findViewById(R.id.download_btn);
		graphBtn = (ImageButton) findViewById(R.id.btn_graph);

		header.setText(page_title);
		sub_date = (TextView) findViewById(R.id.assigned_date_txt);
		lbl_submission = (TextView)findViewById(R.id.assigned_date_main_txt);
		lbl_assigned = (TextView)findViewById(R.id.assigned_marks) ;


		
		assigned_marks = (TextView)findViewById(R.id.assigned_marks);
		comment_main_txt = (TextView)findViewById(R.id.comment_main_txt);

		comments.setVisibility(View.VISIBLE);
		comment_main_txt.setVisibility(View.VISIBLE);

		if (SchoolAppUtils.GetSharedParameter(AssignmentActivity.this,
				Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)) {
			//lbl_submission.setVisibility(View.GONE);
			lbl_assigned.setVisibility(View.GONE);
			assigned_marks.setVisibility(View.GONE);
			//sub_date.setVisibility(View.GONE);
			marks.setVisibility(View.GONE);
		}
		else{
			lbl_submission.setVisibility(View.VISIBLE);
			lbl_assigned.setVisibility(View.VISIBLE);
			assigned_marks.setVisibility(View.VISIBLE);
			sub_date.setVisibility(View.VISIBLE);
			marks.setVisibility(View.VISIBLE);
		}
		
		if (getIntent().getExtras() != null) {

			assignment_type = getIntent().getIntExtra(
					Constants.ASSIGNMENT_TYPE, 1);
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
					assignment = new Assignment(object);

				}
			}
		}
		if (assignment != null) {
			title.setText(assignment.title);
			String tt = assignment.created_date;
			date.setText(assignment.created_date);
			description.setText(assignment.description);
			if (!assignment.marks.equalsIgnoreCase("null")
					&& assignment.is_published.equals("1")) {
				marks.setText(assignment.marks + "/" + assignment.total_marks);
			} else {
				marks.setText(assignment.total_marks);
			}

			if(!assignment.comments.isEmpty() && !assignment.comments.equalsIgnoreCase("null") ){
				comment_main_txt.setVisibility(View.VISIBLE);
				comments.setVisibility(View.VISIBLE);
				comments.setText(assignment.comments);
			}else{
				comment_main_txt.setVisibility(View.GONE);
				comments.setVisibility(View.GONE);
			}


			sub_date.setText(""
					+ assignment.submit_date);
			if (!assignment.file_name.equals("null")
					&& !assignment.attachment.equals("null")) {
				getDocBtn.setVisibility(View.VISIBLE);
				getDocBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(
								AssignmentActivity.this)
								.setTitle("Select option")
								.setPositiveButton("View Document",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// continue with view
												SchoolAppUtils
														.imagePdfViewDocument(
																AssignmentActivity.this,
																Urls.api_get_doc
																		+ assignment.id,assignment.file_name);
											}
										})
								.setNegativeButton("Download",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// download
												final DownloadTask downloadTask = new DownloadTask(AssignmentActivity.this, assignment.file_name);
												downloadTask.execute(Urls.api_get_doc + assignment.id);
												
												
												
                                             /*
												Intent i = new Intent(
														Intent.ACTION_VIEW);
												i.setData(Uri
														.parse(Urls.api_get_doc
																+ assignment.id));
												startActivity(i);*/

											}
										})
								.setIcon(android.R.drawable.ic_dialog_info).show();

						
					}
				});
			} else {
				getDocBtn.setVisibility(View.GONE);
			}

			if (assignment.is_published.equals("1")) {
				graphBtn.setVisibility(View.VISIBLE);
				graphBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(AssignmentActivity.this,
								GraphViewActivity.class);
						intent.putExtra("subject_id", assignment.subject_id);
						intent.putExtra("object_id", assignment.id);
						intent.putExtra(Constants.ASSIGNMENT_TYPE,
								assignment_type);
						intent.putExtra(Constants.PAGE_TITLE, page_title);
						startActivity(intent);
					}
				});
			} else {
				graphBtn.setVisibility(View.GONE);

			}
		} else {
			getDocBtn.setVisibility(View.GONE);
		}
	}

}
