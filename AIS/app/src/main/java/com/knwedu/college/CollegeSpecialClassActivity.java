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
import com.knwedu.college.utils.CollegeDataStructureFramwork.SpecialClass;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeSpecialClassActivity extends Activity {

	private TextView title, date, description, header, marks, comments;
	private TextView program,session,teacher,facility,time;
	private SpecialClass assignment;
	private Button getDocBtn;
	private Button graphBtn;
	private int assignment_type;
	private Context context = this;
	private String Url;
	private String assign;
	private String id;
	private String pageTitle = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_special_class);
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
		pageTitle = getIntent().getExtras().getString(CollegeConstants.PAGE_TITLE);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		title = (TextView) findViewById(R.id.title_txt);
		program = (TextView) findViewById(R.id.program_txt_cnt);
		session = (TextView) findViewById(R.id.session_cntnt);
		teacher = (TextView) findViewById(R.id.teach_cntnt);
		description = (TextView) findViewById(R.id.des_cntnt);
		facility = (TextView) findViewById(R.id.fac_cntnt);
		date = (TextView) findViewById(R.id.date_cntnt);
		time = (TextView) findViewById(R.id.time_cntnt);

		header = (TextView) findViewById(R.id.header_text);
		marks = (TextView) findViewById(R.id.assigned_marks_txt);
		comments = (TextView) findViewById(R.id.comment_txt);

		getDocBtn = (Button) findViewById(R.id.download_btn);
		//graphBtn = (Button) findViewById(R.id.btn_graph);
		header.setText(pageTitle);

		if (getIntent().getExtras() != null) {

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
					assignment = new SpecialClass(object);

				}
			}
		}

		if (assignment != null) {
			title.setText(assignment.title);
			program.setText(assignment.program);
			session.setText(assignment.session);
            teacher.setText(assignment.teacher);
			description.setText(assignment.description);
			facility.setText(assignment.facility);
			date.setText(assignment.sdate);
			time.setText(assignment.class_time);


			/*((TextView) findViewById(R.id.assigned_date_txt)).setText(""
					+ assignment.sdate);*/
			id = CollegeAppUtils.GetSharedParameter(getApplicationContext(),
					"id");
			assign = "SpecialClass";
			if (!assignment.attachment.equals("")
					&& !assignment.attachment.equals("")) {
				getDocBtn.setVisibility(View.VISIBLE);







				getDocBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showDialog();
					}

					private void showDialog() {

						Url = CollegeUrls.base_url+CollegeUrls.api_get_doc + "/" + id + "/"
								+ assignment.id + "/" + assign;
						final Dialog dialog2 = new Dialog(context);
						dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
						TextView txtView = (TextView) dialog2
								.findViewById(R.id.txtView);
						TextView txtDwnld = (TextView) dialog2
								.findViewById(R.id.txtDownload);
						// if button is clicked, close the custom dialog
						txtView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								/*Intent i = new Intent(getApplicationContext(),
										CollegeWebviewActivity.class);
								i.putExtra("Download_Assignment", Url);
								i.putExtra("from", "Assignment");
								startActivity(i);*/
								CollegeAppUtils.imagePdfViewDocument(CollegeSpecialClassActivity.this,Url,assignment.attachment);

								dialog2.dismiss();
							}
						});
						txtDwnld.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(CollegeUrls.base_url+CollegeUrls.api_get_doc
										+ "/" + id + "/" + assignment.id + "/"
										+ assign));
								startActivity(i);
								dialog2.dismiss();
							}
						});

						dialog2.show();

					}
				});
			}
			else {
				getDocBtn.setVisibility(View.GONE);
			}
		}
	}

}
