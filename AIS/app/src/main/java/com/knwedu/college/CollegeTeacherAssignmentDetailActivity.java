package com.knwedu.college;

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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;

public class CollegeTeacherAssignmentDetailActivity extends Activity {
	private TextView title, date, description, header, marks, code;
	private Assignment assignment;
	private Button addResult, getdoc;
	private Subject subject;
	private Button btnPublish;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	private String runMode = "";
	int row_id;
	String subject_id;
	private boolean internetAvailable = false;
	private String page_title = "";
	private String id;
	private String assign;
	private Context context = this;
	public CollegeDBAdapter mDatabase;
	private String Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_teacher_assignment_detail);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);

		mDatabase = ((SchoolApplication) getApplication()).getCollegeDatabase();
		internetAvailable = getIntent().getBooleanExtra(
				CollegeConstants.IS_ONLINE, false);
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
				temp = getIntent().getExtras().getString(
						CollegeConstants.SUBJECT);
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
						CollegeConstants.OFFLINE_SUBJECT_ID);
				row_id = getIntent().getIntExtra(
						CollegeConstants.OFFLINE_ASSIGNMENT_ROWID, 0);

				mDatabase.open();
				subject = mDatabase.getSubject(subject_id);
				assignment = mDatabase.getAssignment(row_id, 1);
				mDatabase.close();

			}

		}
		if (assignment != null) {
			code.setText(assignment.sub_code);
			title.setText(assignment.title);
			date.setText(assignment.created_date.replace("-", "/"));
			description.setText(assignment.description);
			marks.setText(assignment.total_marks);
			if (CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.COLLEGE_ASSIGNMENT_MARK).equals("1")) {
				
				if (assignment.is_marked.equalsIgnoreCase("1")) {
					addResult.setText("Edit Marks");
				} else {
					addResult.setText("Add Marks");
				}
				if (internetAvailable) {
					addResult.setVisibility(View.VISIBLE);
				} else {
					addResult.setVisibility(View.INVISIBLE);
				}
			} else {

				addResult.setVisibility(View.INVISIBLE);
			}
			((TextView) findViewById(R.id.assigned_date_txt)).setText(""
					+ assignment.submit_date);
		}

		addResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.d("..................", "Assignmentresult");
				Intent intent = new Intent(
						CollegeTeacherAssignmentDetailActivity.this,
						CollegeTeacherAssignmentResultsActivity.class);
				intent.putExtra(CollegeConstants.IS_ONLINE, internetAvailable);
				intent.putExtra(CollegeConstants.PAGE_TITLE, page_title);
				if (internetAvailable) {
					intent.putExtra(CollegeConstants.ASSIGNMENTSTUDENT,
							assignment.object.toString());
					intent.putExtra(CollegeConstants.SUBJECT,
							subject.object.toString());
					startActivity(intent);

				} else {
					intent.putExtra(CollegeConstants.OFFLINE_SUBJECT_ID,
							subject_id);
					intent.putExtra(CollegeConstants.OFFLINE_ASSIGNMENT_ROWID,
							row_id);

				}
				startActivity(intent);
			}
		});
		
		id = CollegeAppUtils.GetSharedParameter(getApplicationContext(), "id");
		assign = "Assignment";
		if (!assignment.file_name.equals("")
				&& !assignment.attachment.equals("") && internetAvailable) {
			getdoc.setVisibility(View.VISIBLE);
			getdoc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
					 * Intent i = new Intent(Intent.ACTION_VIEW); i.setData(Uri
					 * .parse(CollegeUrls.base_url + CollegeUrls.api_get_doc +
					 * assignment.id)); startActivity(i);
					 */
					showDialog();
				}

				private void showDialog() {
					Url = CollegeUrls.base_url + CollegeUrls.api_get_doc + "/"
							+ id + "/" + assignment.id + "/" + assign;
					Log.d("URL OF TEACHER ASSIGNMENT..", Url);
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
							i.putExtra("Download_Teacher_Assignment", Url);
							i.putExtra("from", "TeacherAssignment");
							startActivity(i);*/

							CollegeAppUtils.imagePdfViewDocument(CollegeTeacherAssignmentDetailActivity.this, Url,assignment.file_name);
							dialog2.dismiss();
						}
					});
					txtDwnld.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(CollegeUrls.base_url
									+ CollegeUrls.api_get_doc + "/" + id + "/"
									+ assignment.id + "/" + assign));
							startActivity(i);
							dialog2.dismiss();
						}
					});

					dialog2.show();

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

				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(getApplicationContext(), "id")));
				nameValuePairs.add(new BasicNameValuePair("activity_id",
						assignment.id));
				new UploadAsyntask().execute(nameValuePairs);
			}
		});
		if (CollegeAppUtils.GetSharedParameter(context,
				CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH).equals("1")) {

			if ((assignment.is_published.equals("1"))
					&& (assignment.is_marked.equals("1"))) {
				btnPublish.setVisibility(View.INVISIBLE);
				addResult.setVisibility(View.INVISIBLE);
			} else if ((assignment.is_published.equals("0"))
					&& (assignment.is_marked.equals("0"))) {
				btnPublish.setVisibility(View.INVISIBLE);
			} else {
				btnPublish.setVisibility(View.VISIBLE);
			}
		} else {
			btnPublish.setVisibility(View.GONE);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(
					CollegeTeacherAssignmentDetailActivity.this);
			dialog.setTitle(getResources().getString(R.string.publish));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					CollegeUrls.api_activity_publish);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						error = page_title + " Published.";

						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
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
						CollegeTeacherAssignmentDetailActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeAppUtils
										.SetSharedBoolParameter(
												CollegeTeacherAssignmentDetailActivity.this,
												"update", true);
								/*
								 * if(runMode.equalsIgnoreCase(Constants.
								 * MODE_OFFLINE)) { Intent intent = new
								 * Intent(TeacherAssignmentDetailActivity.this,
								 * TeacherAssignmentListActivity.class);
								 * intent.putExtra(Constants.TASSIGNMENT,
								 * subject.toString()); startActivity(intent); }
								 */

								CollegeTeacherAssignmentDetailActivity.this
										.finish();

							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeTeacherAssignmentDetailActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeTeacherAssignmentDetailActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeTeacherAssignmentDetailActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
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
