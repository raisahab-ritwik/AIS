package com.knwedu.college;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.knwedu.college.utils.CollegeDataStructureFramwork.Carr;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeNewsActivity extends Activity {
	private TextView description, header, date,created_by;
	private Carr news;
	private Button getDocBtn;
	private String id;
	private String assign;

	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_news);
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
		description = (TextView) findViewById(R.id.assignment_txt);
		header = (TextView) findViewById(R.id.header_2_text);
		getDocBtn = (Button) findViewById(R.id.download_btn);
		date = (TextView) findViewById(R.id.date_text);
		created_by = (TextView) findViewById(R.id.textViewGivenBy);
		if (getIntent().getExtras() != null) {
			String temp = getIntent().getExtras().getString(
					CollegeConstants.NEWS);
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object != null) {
					news = new Carr(object);
				}
			}
		}
		if (news != null) {
			if ((news.prog_name.equalsIgnoreCase("null"))
					&& (news.term_name.equalsIgnoreCase("null"))) {
				header.setText(news.title);
			} else if (news.term_name.equalsIgnoreCase("null")) {
				header.setText(news.prog_name + " " + news.title);
			} else {
				header.setText(news.prog_name + " " + news.term_name + " "
						+ news.title);
			}
			created_by.setText(news.created_by);
			description.setText("" + news.description);
			date.setText("" + news.date);
			id = CollegeAppUtils.GetSharedParameter(getApplicationContext(),
					"id");
			assign = "Bulletin";
			if (news.doc != null) {
				if (!news.doc.equalsIgnoreCase("null")) {
					getDocBtn.setVisibility(View.VISIBLE);
					getDocBtn.setOnClickListener(new OnClickListener() {
						private String Url;

						@Override
						public void onClick(View v) {
							showDialog();
							/*
							 * Intent i = new Intent(Intent.ACTION_VIEW);
							 * i.setData
							 * (Uri.parse(CollegeUrls.api_get_bulatine_doc +
							 * news.id)); startActivity(i);
							 */
						}

						private void showDialog() {
							Url = CollegeUrls.base_url
									+ CollegeUrls.api_get_doc + "/" + id + "/"
									+ news.id + "/" + assign;
							new AlertDialog.Builder(CollegeNewsActivity.this)
									.setTitle("Select option")
									.setPositiveButton("View Document",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
																	int which) {
													CollegeAppUtils.imagePdfViewDocument(CollegeNewsActivity.this, Url, news.file_name);
												}
											})
									.setNegativeButton("Download",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog,
																	int which) {
													// download
													Intent i = new Intent(Intent.ACTION_VIEW);
													i.setData(Uri
															.parse(CollegeUrls.base_url
																	+ CollegeUrls.api_get_doc
																	+ "/" + id + "/" + news.id
																	+ "/" + assign));
													startActivity(i);

												}
											}).setIcon(android.R.drawable.ic_dialog_info).show();
						}

//						private void showDialog() {
//							Url = CollegeUrls.base_url
//									+ CollegeUrls.api_get_doc + "/" + id + "/"
//									+ news.id + "/" + assign;
//							final Dialog dialog2 = new Dialog(context);
//							dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//							dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
//							TextView txtView = (TextView) dialog2
//									.findViewById(R.id.txtView);
//							TextView txtDwnld = (TextView) dialog2
//									.findViewById(R.id.txtDownload);
//							// if button is clicked, close the custom dialog
//							txtView.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									/*Intent i = new Intent(
//											getApplicationContext(),
//											CollegeWebviewActivity.class);
//									i.putExtra("Download_Bulletin", Url);
//									i.putExtra("from", "Bulletin");
//									startActivity(i);*/
//									CollegeAppUtils.imagePdfViewDocument(CollegeNewsActivity.this,Url,news.file_name);
//									dialog2.dismiss();
//								}
//							});
//							txtDwnld.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									Intent i = new Intent(Intent.ACTION_VIEW);
//									i.setData(Uri
//											.parse(CollegeUrls.base_url
//													+ CollegeUrls.api_get_doc
//													+ "/" + id + "/" + news.id
//													+ "/" + assign));
//									startActivity(i);
//									dialog2.dismiss();
//								}
//							});
//
//							dialog2.show();
//						}
					});
				} else {
					getDocBtn.setVisibility(View.GONE);
				}
			} else {
				getDocBtn.setVisibility(View.GONE);
			}
		}
	}
}
