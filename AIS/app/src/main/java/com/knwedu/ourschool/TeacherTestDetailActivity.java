package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherTestDetailActivity extends Activity {
	private TextView title, date, description, header, marks, code;
	private Assignment test;
	private Button addResult, getdoc;
	private Subject subject;
	private Button btnPublish;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	int row_id;
	String subject_id;
	public DatabaseAdapter mDatabase;
	private boolean internetAvailable = false;
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_teacher_assignment_detail);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(TeacherTestDetailActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
				false);
		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();

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
		code = (TextView) findViewById(R.id.code_txt);
		header = (TextView) findViewById(R.id.header_text);
		marks = (TextView) findViewById(R.id.marks_txt);
		header.setText(page_title);
		getdoc = (Button) findViewById(R.id.btn_graph);
		addResult = (Button) findViewById(R.id.add_results_btn);
		btnPublish = (Button) findViewById(R.id.assignment_btn_publish);
		// btnPublish.setText(R.string.publish);
		getdoc.setVisibility(View.GONE);
		if (getIntent().getExtras() != null) {
			if (internetAvailable) {
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
						test = new Assignment(object);
					}
				}
				temp = getIntent().getExtras().getString(Constants.SUBJECT);
				if (temp != null) {
					JSONObject object = null;
					try {
						object = new JSONObject(temp);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (object != null) {
						subject = new Subject(object);
					}
				}
			} else {

				findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
				subject_id = getIntent().getExtras().getString(
						Constants.OFFLINE_SUBJECT_ID);
				row_id = getIntent().getIntExtra(
						Constants.OFFLINE_ASSIGNMENT_ROWID, 0);
				mDatabase.open();
				subject = mDatabase.getSubject(subject_id);
				test = mDatabase.getAssignment(row_id, 2);
				mDatabase.close();
			}

		}
		if (test != null) {
			code.setText(test.sub_code);
			title.setText(test.title);
			date.setText(test.created_date.replace("-", "/"));
			description.setText(test.description);
			marks.setText(test.total_marks);
			if (test.is_marked.equalsIgnoreCase("1")) {
				addResult.setText("Edit Marks");
			} else {
				addResult.setText("Add Marks");
			}
			((TextView) findViewById(R.id.assigned_date_txt)).setText(""
					+ test.submit_date);
		}

		addResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.d("..................", "Assignmentresult");
				Intent intent = new Intent(TeacherTestDetailActivity.this,
						TeacherTestResultsActivity.class);
				intent.putExtra(Constants.IS_ONLINE, internetAvailable);
				intent.putExtra(Constants.PAGE_TITLE, page_title);
				if (internetAvailable) {
					intent.putExtra(Constants.ASSIGNMENTSTUDENT,
							test.object.toString());
					intent.putExtra(Constants.SUBJECT,
							subject.object.toString());
					startActivity(intent);

				} else {
					intent.putExtra(Constants.OFFLINE_SUBJECT_ID, subject_id);
					intent.putExtra(Constants.OFFLINE_ASSIGNMENT_ROWID, row_id);

				}
				startActivity(intent);
			}
		});
		if (internetAvailable) {
			if ((SchoolAppUtils.GetSharedParameter(
					TeacherTestDetailActivity.this,
					Constants.TEST_MARK_PERMISSION)).equalsIgnoreCase("0")) {
				addResult.setVisibility(View.INVISIBLE);
				marks.setVisibility(View.INVISIBLE);
				findViewById(R.id.title_marks_txt)
						.setVisibility(View.INVISIBLE);
			} else if (test.total_marks.equalsIgnoreCase("0")) {
				addResult.setVisibility(View.INVISIBLE);
				marks.setVisibility(View.INVISIBLE);
				findViewById(R.id.title_marks_txt)
						.setVisibility(View.INVISIBLE);
			} else {
				addResult.setVisibility(View.VISIBLE);
				marks.setVisibility(View.VISIBLE);
				findViewById(R.id.title_marks_txt).setVisibility(View.VISIBLE);
			}
		}
		if (!test.file_name.equals("null") && !test.attachment.equals("null")
				&& internetAvailable) {
			getdoc.setVisibility(View.VISIBLE);
			getdoc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new AlertDialog.Builder(TeacherTestDetailActivity.this)
							.setTitle("Select option")
							.setPositiveButton("View Document",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// continue with view
											SchoolAppUtils
													.imagePdfViewDocument(
															TeacherTestDetailActivity.this,
															Urls.api_get_doc
																	+ test.id,test.file_name);
										}
									})
							.setNegativeButton("Download",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// download
											
											final DownloadTask downloadTask = new DownloadTask(TeacherTestDetailActivity.this, test.file_name);
											downloadTask.execute(Urls.api_get_doc
													+ test.id);
											
											

											/*Intent i = new Intent(
													Intent.ACTION_VIEW);
											i.setData(Uri
													.parse(Urls.api_get_doc
															+ test.id));
											startActivity(i);*/

										}
									})
							.setIcon(android.R.drawable.ic_dialog_info).show();

				}
			});
		} else {
			getdoc.setVisibility(View.GONE);
		}
		btnPublish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs
						.add(new BasicNameValuePair("user_type_id",
								SchoolAppUtils.GetSharedParameter(
										getApplicationContext(),
										Constants.USERTYPEID)));
				nameValuePairs.add(new BasicNameValuePair("organization_id",
						SchoolAppUtils.GetSharedParameter(
								getApplicationContext(),
								Constants.UORGANIZATIONID)));
				nameValuePairs.add(new BasicNameValuePair("user_id",
						SchoolAppUtils.GetSharedParameter(
								getApplicationContext(), Constants.USERID)));
				nameValuePairs.add(new BasicNameValuePair("id", test.id));
				new UploadAsyntask().execute(nameValuePairs);
			}
		});

		if ((test.is_published.equals("1")) && (test.is_marked.equals("1"))) {
			btnPublish.setVisibility(View.INVISIBLE);
			addResult.setVisibility(View.INVISIBLE);
		} else if ((test.is_published.equals("0"))
				&& (test.is_marked.equals("0"))) {
			btnPublish.setVisibility(View.INVISIBLE);
		} else {
			btnPublish.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherTestDetailActivity.this);
			dialog.setTitle("Publishing Result");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_activity_publish);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						error = "Result published successfully";

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
				dialoggg = new AlertDialog.Builder(
						TeacherTestDetailActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								SchoolAppUtils.SetSharedBoolParameter(
										TeacherTestDetailActivity.this,
										"update", true);

								TeacherTestDetailActivity.this.finish();

							}
						});
				dialoggg.show();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(TeacherTestDetailActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(TeacherTestDetailActivity.this,
							page_title,
							getResources().getString(R.string.error));
				}

			}
		}

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	protected void onStop() {
		super.onStop();
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}
}
