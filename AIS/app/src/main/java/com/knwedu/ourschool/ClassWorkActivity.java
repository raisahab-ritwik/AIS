package com.knwedu.ourschool;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Announcement;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ClassWorkActivity extends Activity {
	private TextView title, date, description, header;
	private Announcement classwork;
	private Button getDocBtn;
	public DatabaseAdapter mDatabase;
	private boolean internetAvailable = false;
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_announcement);
		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
				true);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(ClassWorkActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
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
		header = (TextView) findViewById(R.id.header_txt);
		header.setText(page_title);
		getDocBtn = (Button) findViewById(R.id.download_btn);
		if ((SchoolAppUtils.GetSharedParameter(ClassWorkActivity.this,
				Constants.USERTYPEID)
				.equalsIgnoreCase(Constants.USERTYPE_STUDENT))
				|| SchoolAppUtils.GetSharedParameter(ClassWorkActivity.this,
						Constants.USERTYPEID).equalsIgnoreCase(
						Constants.USERTYPE_PARENT)) {
			if (getIntent().getExtras() != null) {
				// if (internetAvailable) {
				String temp = getIntent().getExtras().getString(
						Constants.ANNOUNCEMENT);
				if (temp != null) {
					JSONObject object = null;
					try {
						object = new JSONObject(temp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (object != null) {
						classwork = new Announcement(object);
					}
					/*
					 * } } else { int row_id =
					 * getIntent().getIntExtra(Constants.
					 * OFFLINE_ANNOUNCEMENT_ROWID,0); mDatabase.open();
					 * classwork = mDatabase.getAnnouncement(row_id, 2);
					 * mDatabase.close(); }
					 */
				}
			}
		} else {
			if (internetAvailable) {
				String temp = getIntent().getExtras().getString(
						Constants.ANNOUNCEMENT);
				if (temp != null) {
					JSONObject object = null;
					try {
						object = new JSONObject(temp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (object != null) {
						classwork = new Announcement(object);
					}
				}
			} else {
				int row_id = getIntent().getIntExtra(
						Constants.OFFLINE_ANNOUNCEMENT_ROWID, 0);
				mDatabase.open();
				classwork = mDatabase.getAnnouncement(row_id, 1);
				mDatabase.close();
			}
		}

		if (classwork != null) {
			title.setText(classwork.title);
			date.setText(classwork.date.replace("-", "/"));
			description.setText(classwork.description);

			if (!classwork.file_name.equals("null")
					&& !classwork.attachment.equals("null")
					&& internetAvailable) {
				getDocBtn.setVisibility(View.VISIBLE);
				getDocBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(
								ClassWorkActivity.this)
								.setTitle("Select option")
								.setPositiveButton("View Document",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// continue with view
												SchoolAppUtils
														.imagePdfViewDocument(
																ClassWorkActivity.this,
																Urls.api_get_announcement_classwork_doc
																		+ classwork.id,classwork.file_name);
											}
										})
								.setNegativeButton("Download",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// download
												
												final DownloadTask downloadTask = new DownloadTask(ClassWorkActivity.this, classwork.file_name);
												downloadTask.execute(Urls.api_get_announcement_classwork_doc + classwork.id);
												
												

												/*Intent i = new Intent(
														Intent.ACTION_VIEW);
												i.setData(Uri
														.parse(Urls.api_get_announcement_classwork_doc
																+ classwork.id));
												startActivity(i);*/

											}
										})
								.setIcon(android.R.drawable.ic_dialog_info).show();

						
						
					}
				});
			} else {
				getDocBtn.setVisibility(View.GONE);
			}
		}
	}
}
