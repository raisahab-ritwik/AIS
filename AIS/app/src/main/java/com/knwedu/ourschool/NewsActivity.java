package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.News;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class NewsActivity extends Activity {
	private TextView description, header, date;
	private News news;
	private Button getDocBtn;
	private String page_title = "", reply_txt = "", position = "";
	private ImageView reply;
	private Dialog dialogCustom;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bulletin_reply);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(NewsActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

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

		TextView header_text = (TextView) findViewById(R.id.header_text);
		header_text.setText(page_title);
		description = (TextView) findViewById(R.id.assignment_txt);

		reply = (ImageView) findViewById(R.id.reply);

		header = (TextView) findViewById(R.id.header_2_text);
		getDocBtn = (Button) findViewById(R.id.download_btn);
		date = (TextView) findViewById(R.id.date_text);
		if (getIntent().getExtras() != null) {
			String temp = getIntent().getExtras().getString(Constants.NEWS);
			position = getIntent().getExtras().getString("type");
			Log.d("type...........", position);
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object != null) {
					news = new News(object);
				}
			}
		}
		if (SchoolAppUtils.GetSharedParameter(NewsActivity.this,
				Constants.USERTYPEID).equals(Constants.USERTYPE_PARENT)
				|| SchoolAppUtils.GetSharedParameter(NewsActivity.this,
						Constants.USERTYPEID)
						.equals(Constants.USERTYPE_STUDENT)) {
			reply.setVisibility(View.INVISIBLE);
		} else {
			if (position.equalsIgnoreCase("2")) {
				reply.setVisibility(View.VISIBLE);
			} else {
				reply.setVisibility(View.INVISIBLE);
			}
		}
		reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogCustom = new Dialog(NewsActivity.this);
				dialogCustom.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogCustom.setContentView(R.layout.add_reply_dialog);
				dialogCustom.findViewById(R.id.comments_edt);
				((Button) dialogCustom.findViewById(R.id.add_btn))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (((EditText) dialogCustom
										.findViewById(R.id.comments_edt))
										.getText().toString().length() <= 0) {
									Toast.makeText(NewsActivity.this,
											"Enter Reply", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								reply_txt = ((EditText) dialogCustom
										.findViewById(R.id.comments_edt))
										.getText().toString();
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										5);
								nameValuePairs.add(new BasicNameValuePair(
										"user_type_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.USERTYPEID)));
								nameValuePairs.add(new BasicNameValuePair(
										"organization_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.UORGANIZATIONID)));
								nameValuePairs.add(new BasicNameValuePair(
										"user_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.USERID)));
								nameValuePairs.add(new BasicNameValuePair(
										"bulletin_id", news.id));
								nameValuePairs.add(new BasicNameValuePair(
										"reply", reply_txt));

								new GetBulletinsAsynkTask()
										.execute(nameValuePairs);
								dialogCustom.dismiss();
							}
						});

				((Button) dialogCustom.findViewById(R.id.cancel_btn))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialogCustom.dismiss();
							}
						});
				dialogCustom.show();
			}
		});

		if (news != null) {
			header.setText("" + news.title);
			description.setText("" + news.description);
			date.setText("" + news.date);
			if (news.doc != null) {
				if (!news.doc.equalsIgnoreCase("null")) {
					getDocBtn.setVisibility(View.VISIBLE);
					getDocBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							new AlertDialog.Builder(NewsActivity.this)
									.setTitle("Select option")
									.setPositiveButton(
											"View Document",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// continue with view
													String te = Urls.api_get_bulatine_doc
															+ news.id;
													SchoolAppUtils
															.imagePdfViewDocument(
																	NewsActivity.this,
																	Urls.api_get_bulatine_doc
																			+ news.id,news.file_name);
												}
											})
									.setNegativeButton(
											"Download",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// download
													
													final DownloadTask downloadTask = new DownloadTask(NewsActivity.this, news.file_name);
													downloadTask.execute(Urls.api_get_bulatine_doc + news.id);
													
													

													/*Intent i = new Intent(
															Intent.ACTION_VIEW);
													i.setData(Uri
															.parse(Urls.api_get_bulatine_doc
																	+ news.id));
													startActivity(i);*/

												}
											})
									.setIcon(android.R.drawable.ic_dialog_info)
									.show();

						}
					});
				} else {
					getDocBtn.setVisibility(View.GONE);
				}
			} else {
				getDocBtn.setVisibility(View.GONE);
			}
		}
	}

	private class GetBulletinsAsynkTask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(NewsActivity.this);
			dialog.setTitle("Creating " + page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			Log.d("url extension: ", Urls.api_reply_bulletin_save);
			String parameters = "";
			for (NameValuePair nvp : namevaluepair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_reply_bulletin_save);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						error = json.getString("data");
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} else {
					error = getResources().getString(R.string.unknown_response);
				}

			} catch (JSONException e) {

			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {
				dialoggg = new AlertDialog.Builder(NewsActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								NewsActivity.this.finish();

							}
						});
				dialoggg.show();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(NewsActivity.this, page_title,
							error);
				} else {
					SchoolAppUtils.showDialog(NewsActivity.this, page_title,
							getResources().getString(R.string.unknown_response));
				}

			}

		}

	}

}
