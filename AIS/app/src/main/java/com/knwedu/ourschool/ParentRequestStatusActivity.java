package com.knwedu.ourschool;



import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.RequestsStatus;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ParentRequestStatusActivity extends Activity {
	private TextView title, date, description, header, comment;
	private RequestsStatus requestsStatus;
	private Button getDocBtn;
	private RelativeLayout relative_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_request_status);
        SchoolAppUtils.loadAppLogo(ParentRequestStatusActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
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
	private void initialize()
	{

		((ImageButton)findViewById(R.id.back_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		title = (TextView) findViewById(R.id.title_txt);
		date = (TextView) findViewById(R.id.assignment_due_txt);
		description = (TextView) findViewById(R.id.assignment_txt);
		header = (TextView) findViewById(R.id.header_text);
		relative_layout = (RelativeLayout)findViewById(R.id.relative_layout);
		comment = (TextView) findViewById(R.id.comment_txt);
		header.setText(R.string.requests_status);
		getDocBtn = (Button) findViewById(R.id.download_btn);
		if(getIntent().getExtras() != null)
		{
			String temp = getIntent().getExtras().getString(Constants.REQUESTSTATUS);
			if(temp != null)
			{
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(object != null)
				{
					requestsStatus = new RequestsStatus(object);
				}
			}
		}
		if(requestsStatus != null)
		{
			
			title.setText(requestsStatus.reason_title);
			
			if(requestsStatus.date_from != null)
			{
				if(!requestsStatus.created_date.equalsIgnoreCase("null"))
				{
					date.setText(requestsStatus.created_date.replace("-", "/"));
				}
				else
				{
					date.setText("");
				}
			}
			else
			{
				date.setText("");
			}
			
			String desc = "";
			if(requestsStatus.description.equals("0")){
				desc = "N.A.";
			}else{
				desc = requestsStatus.description;
			}
			if (!requestsStatus.date_from.equals("000 00,0000")
					&& !requestsStatus.date_from.equalsIgnoreCase("null") && !requestsStatus.date_from.isEmpty()) {
				desc += "\n\nRequested from "
						+ requestsStatus.date_from.replace("-", "/") + " to "
						+ requestsStatus.date_to.replace("-", "/");
			}
			
			description.setText(desc);
			if(requestsStatus.status.equalsIgnoreCase("0"))
			{
				((TextView)findViewById(R.id.assigned_date_txt)).setText(R.string.pending);
				((TextView)findViewById(R.id.assigned_date_txt)).setTextColor(Color.YELLOW);
			}
			else if(requestsStatus.status.equalsIgnoreCase("1"))
			{
				((TextView)findViewById(R.id.assigned_date_txt)).setText(R.string.approved);
				((TextView)findViewById(R.id.assigned_date_txt)).setTextColor(Color.GREEN);
			}
			else if(requestsStatus.status.equalsIgnoreCase("2"))
			{
				((TextView)findViewById(R.id.assigned_date_txt)).setText(R.string.rejected);
				((TextView)findViewById(R.id.assigned_date_txt)).setTextColor(Color.RED);
			}

			if (requestsStatus.reject_reason != null) {
				if (!requestsStatus.reject_reason.equalsIgnoreCase("null") && !requestsStatus.reject_reason.isEmpty()) {
					relative_layout.setVisibility(View.VISIBLE);
					comment.setVisibility(View.VISIBLE);
					comment.setText(requestsStatus.reject_reason);
				} else {
					relative_layout.setVisibility(View.GONE);
					comment.setVisibility(View.GONE);
				}
			} else {
				relative_layout.setVisibility(View.GONE);
				comment.setVisibility(View.GONE);
			}
			
			if(!requestsStatus.attachment.equalsIgnoreCase("null") && requestsStatus.attachment.length() > 0){
				getDocBtn.setVisibility(View.VISIBLE);
				getDocBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(
								ParentRequestStatusActivity.this)
								.setTitle("Select option")
								.setPositiveButton("View Document",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// continue with view
												SchoolAppUtils
														.imagePdfViewDocument(
																ParentRequestStatusActivity.this,
																Urls.api_get_request_doc
																		+ requestsStatus.id,requestsStatus.file_name);
											}
										})
								.setNegativeButton("Download",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// download
												
												final DownloadTask downloadTask = new DownloadTask(ParentRequestStatusActivity.this, requestsStatus.file_name);
												downloadTask.execute(Urls.api_get_request_doc + requestsStatus.id);
												
                                              /*
												Intent i = new Intent(
														Intent.ACTION_VIEW);
												i.setData(Uri
														.parse(Urls.api_get_request_doc
																+ requestsStatus.id));
												startActivity(i);*/

											}
										})
								.setIcon(android.R.drawable.ic_dialog_info).show();

						
						;
					}
				});
			}else{
				getDocBtn.setVisibility(View.INVISIBLE);
			}
		}
	}
}
