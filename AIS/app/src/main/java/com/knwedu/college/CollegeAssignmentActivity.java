package com.knwedu.college;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeAssignmentActivity extends Activity {
	private TextView title, date, description, header, marks, comments,comment_main_txt;
	private Assignment assignment;
	private Button getDocBtn;
	private ImageButton graphBtn;
	private int assignment_type;
	private String id;
	private String assign;
	private String page_title = "";
	private Context context = this;
	private String Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_assignment);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
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
		title = (TextView) findViewById(R.id.title_txt);
		date = (TextView) findViewById(R.id.assignment_due_txt);
		description = (TextView) findViewById(R.id.assignment_txt);
		header = (TextView) findViewById(R.id.header_text);
		marks = (TextView) findViewById(R.id.assigned_marks_txt);
		comments = (TextView) findViewById(R.id.comment_txt);
		comment_main_txt= (TextView) findViewById(R.id.comment_main_txt);
		getDocBtn = (Button) findViewById(R.id.download_btn);
		graphBtn = (ImageButton) findViewById(R.id.btn_graph);
		header.setText(page_title);
		if (getIntent().getExtras() != null) {

			assignment_type = getIntent().getIntExtra(
					CollegeConstants.ASSIGNMENT_TYPE, 1);
			String temp = getIntent().getExtras().getString(
					CollegeConstants.ASSIGNMENT);
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
			date.setText(assignment.created_date.replace("-", "/"));
			description.setText(assignment.description);

			if(!assignment.comments.isEmpty() && !assignment.comments.equalsIgnoreCase("null") ){
				comment_main_txt.setVisibility(View.VISIBLE);
				comments.setVisibility(View.VISIBLE);
				comments.setText(assignment.comments);
			}else{
				comment_main_txt.setVisibility(View.GONE);
				comments.setVisibility(View.GONE);
			}
			if (!assignment.marks.equalsIgnoreCase("null")
					&& assignment.is_published.equals("1")) {
				marks.setText(assignment.marks + "/" + assignment.total_marks);
			} else {
				marks.setText(assignment.total_marks);
			}

			((TextView) findViewById(R.id.assigned_date_txt)).setText(""
					+ assignment.submit_date);
			id = CollegeAppUtils.GetSharedParameter(getApplicationContext(),
					"id");
			assign = "Assignment";
			if (!assignment.file_name.equals("")
					&& !assignment.attachment.equals("")) {
				getDocBtn.setVisibility(View.VISIBLE);
				getDocBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showDialog();
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
						Intent intent = new Intent(
								CollegeAssignmentActivity.this,
								CollegeGraphViewActivity.class);
						intent.putExtra("subject_id", assignment.subject_id);
						intent.putExtra("object_id", assignment.id);
						intent.putExtra(CollegeConstants.ASSIGNMENT_TYPE,"Assignment");
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

	private void showDialog() {
		Url = CollegeUrls.base_url+CollegeUrls.api_get_doc + "/" + id + "/" + assignment.id + "/"
				+ assign;
		final Dialog dialog2 = new Dialog(context);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
		TextView txtView = (TextView) dialog2.findViewById(R.id.txtView);
		TextView txtDwnld = (TextView) dialog2.findViewById(R.id.txtDownload);
		// if button is clicked, close the custom dialog
		txtView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				/*Intent i = new Intent(getApplicationContext(),
						CollegeWebviewActivity.class);
				i.putExtra("Download_Assignment", Url);
				i.putExtra("from", "Assignment");
				startActivity(i);*/
				CollegeAppUtils.imagePdfViewDocument(CollegeAssignmentActivity.this,Url,assignment.file_name);
				dialog2.dismiss();
			}
		});
		txtDwnld.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(CollegeUrls.base_url+CollegeUrls.api_get_doc + "/" + id + "/"
						+ assignment.id + "/" + assign));
				startActivity(i);
				dialog2.dismiss();
			}
		});

		dialog2.show();
	}

}
