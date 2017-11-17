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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.RequestsStatus;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeParentRequestStatusActivity extends Activity {
	private TextView title, date, description, header, comment;
	private RequestsStatus requestsStatus;
	private Button imggetDoc;
	private String assign;
	private String id;
	private Context context = this;
	private String Url;
	private RelativeLayout relative_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_request_status);
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
		id = CollegeAppUtils.GetSharedParameter(getApplicationContext(), "id");
		assign = "Requests";
		imggetDoc = (Button) findViewById(R.id.download_btn);
		relative_layout = (RelativeLayout)findViewById(R.id.relative_layout);
		
		
		title = (TextView) findViewById(R.id.title_txt);
		date = (TextView) findViewById(R.id.assignment_due_txt);
		description = (TextView) findViewById(R.id.assignment_txt);
		header = (TextView) findViewById(R.id.header_text);
		comment = (TextView) findViewById(R.id.comment_txt);
		header.setText(R.string.requests_status);
		if (getIntent().getExtras() != null) {
			String temp = getIntent().getExtras().getString(
					CollegeConstants.REQUESTSTATUS);
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object != null) {
					requestsStatus = new RequestsStatus(object);
				}
			}
		}
		if (requestsStatus != null) {
			boolean check = false;

			/*
			 * if(requestsStatus.tittle != null) {
			 * if(!requestsStatus.tittle.equalsIgnoreCase("null")) {
			 * title.setText(requestsStatus.tittle); check = true; } }
			 * if(!check) { if(requestsStatus.requesttype.equalsIgnoreCase("1"))
			 * { title.setText("Card"); } else
			 * if(requestsStatus.requesttype.equalsIgnoreCase("2")) {
			 * title.setText("Uniform"); } else
			 * if(requestsStatus.requesttype.equalsIgnoreCase("3")) {
			 * title.setText("Books"); } else
			 * if(requestsStatus.requesttype.equalsIgnoreCase("4")) {
			 * title.setText("Special"); } }
			 */

			title.setText(requestsStatus.reason_title);

			if (requestsStatus.date_from != null) {
				if (!requestsStatus.created_date.equalsIgnoreCase("null")) {
					date.setText(requestsStatus.created_date.replace("-", "/"));
				} else {
					date.setText("");
				}
			} else {
				date.setText("");
			}

			String desc = "";
			if (requestsStatus.description.equals("0")) {
				desc = "N.A.";
			} else {
				desc = requestsStatus.description;
			}
			if (!requestsStatus.date_from.equals("000 00,0000")
					&& !requestsStatus.date_from.equalsIgnoreCase("null") && !requestsStatus.date_from.isEmpty()) {
				desc += "\n\nRequested from "
						+ requestsStatus.date_from.replace("-", "/") + " to "
						+ requestsStatus.date_to.replace("-", "/");
			}
			if (!requestsStatus.file_name.equals("")
					&& !requestsStatus.attachment.equals("")) {
				imggetDoc.setVisibility(View.VISIBLE);
				imggetDoc.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showDialog();
					}
				});
			} else {
				imggetDoc.setVisibility(View.GONE);
			}
			description.setText(desc);
			((TextView) findViewById(R.id.assigned_date_txt))
					.setText(requestsStatus.status);
			/*
			 * if(requestsStatus.status.equalsIgnoreCase("0")) {
			 * 
			 * ((TextView)findViewById(R.id.assigned_date_txt)).setTextColor(Color
			 * .YELLOW); } else if(requestsStatus.status.equalsIgnoreCase("1"))
			 * {
			 * ((TextView)findViewById(R.id.assigned_date_txt)).setText(R.string
			 * .approved);
			 * ((TextView)findViewById(R.id.assigned_date_txt)).setTextColor
			 * (Color.GREEN); } else
			 * if(requestsStatus.status.equalsIgnoreCase("2")) {
			 * ((TextView)findViewById
			 * (R.id.assigned_date_txt)).setText(R.string.rejected);
			 * ((TextView)findViewById
			 * (R.id.assigned_date_txt)).setTextColor(Color.RED); }
			 */
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
		}
	}

	private void showDialog() {
		Url = CollegeUrls.base_url + CollegeUrls.api_get_doc + "/" + id + "/"
				+ requestsStatus.id + "/" + assign;
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
				i.putExtra("Download_Request", Url);
				i.putExtra("from", "Request");
				startActivity(i);*/
				CollegeAppUtils.imagePdfViewDocument(CollegeParentRequestStatusActivity.this, Url,requestsStatus.file_name);
				dialog2.dismiss();
			}
		});
		txtDwnld.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(CollegeUrls.base_url
						+ CollegeUrls.api_get_doc + "/" + id + "/"
						+ requestsStatus.id + "/" + assign));
				startActivity(i);
				dialog2.dismiss();
			}
		});

		dialog2.show();
	}

}
