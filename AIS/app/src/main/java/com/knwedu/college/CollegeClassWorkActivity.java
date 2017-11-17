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
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Announcement;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;

public class CollegeClassWorkActivity extends Activity {
	private TextView title, date, description, header;
	private Announcement classwork;
	private Button getDocBtn;
	public CollegeDBAdapter mDatabase;
	private boolean internetAvailable = false;
	private String assign;
	private String id;
	private Context context = this;
	private String page_title = "";
	private String Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_announcement);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		mDatabase = ((SchoolApplication) getApplication()).getCollegeDatabase();
		internetAvailable = getIntent().getBooleanExtra(
				CollegeConstants.IS_ONLINE, true);
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
		if ((CollegeAppUtils.GetSharedParameter(CollegeClassWorkActivity.this,
				CollegeConstants.USERTYPEID)
				.equalsIgnoreCase(CollegeConstants.USERTYPE_STUDENT))
				|| CollegeAppUtils.GetSharedParameter(
						CollegeClassWorkActivity.this,
						CollegeConstants.USERTYPEID).equalsIgnoreCase(
						CollegeConstants.USERTYPE_PARENT)) {
			if (getIntent().getExtras() != null) {
				// if (internetAvailable) {
				String temp = getIntent().getExtras().getString(
						CollegeConstants.ANNOUNCEMENT);
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

				} else {
					int row_id = getIntent().getIntExtra(
							CollegeConstants.OFFLINE_ANNOUNCEMENT_ROWID, 0);
					mDatabase.open();
					classwork = mDatabase.getAnnouncement(row_id, 2);
					mDatabase.close();
				}

			}
		} else {
			if (internetAvailable) {
				String temp = getIntent().getExtras().getString(
						CollegeConstants.ANNOUNCEMENT);
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
						CollegeConstants.OFFLINE_ANNOUNCEMENT_ROWID, 0);
				mDatabase.open();
				classwork = mDatabase.getAnnouncement(row_id, 2);
				mDatabase.close();
			}

		}

		if (classwork != null) {
			title.setText(classwork.title);
			date.setText(classwork.date.replace("-", "/"));
			description.setText(classwork.description);
			id = CollegeAppUtils.GetSharedParameter(getApplicationContext(),
					"id");
			assign = "Classwork";
			if (!classwork.file_name.equals("")
					&& !classwork.attachment.equals("") && internetAvailable) {
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
		}

	}

	private void showDialog() {
		Url = CollegeUrls.base_url + CollegeUrls.api_get_doc + "/" + id + "/"
				+ classwork.id + "/" + assign;
		final Dialog dialog2 = new Dialog(context);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
		TextView txtView = (TextView) dialog2.findViewById(R.id.txtView);
		TextView txtDwnld = (TextView) dialog2.findViewById(R.id.txtDownload);
		// if button is clicked, close the custom dialog
		txtView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			/*	Intent i = new Intent(getApplicationContext(),
						CollegeWebviewActivity.class);
				i.putExtra("Download_Classwork", Url);
				i.putExtra("from", "Classwork");
				startActivity(i);
				*/
				CollegeAppUtils.imagePdfViewDocument(CollegeClassWorkActivity.this,Url,classwork.file_name);
				dialog2.dismiss();
			}
		});
		txtDwnld.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(CollegeUrls.base_url
						+ CollegeUrls.api_get_doc + "/" + id + "/"
						+ classwork.id + "/" + assign));
				startActivity(i);
				dialog2.dismiss();
			}
		});

		dialog2.show();
	}

}
