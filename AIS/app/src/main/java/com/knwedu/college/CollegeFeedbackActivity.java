package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.ParentProfileInfo;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeFeedbackActivity extends Activity {
	private TextView ttitle, title, header;
	private Button date, submite, select_file;
	private Subject subject;
	private EditText discription, titleEdt, marksEdt;/* codeEdt */
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	private String dateSelected;
	// public DatabaseAdapter mDatabase;
	private Spinner spinner;
	ArrayList<ParentProfileInfo> temp;
	ArrayList<String> name;
	private ArrayList<String> id;
	ParentProfileInfo newD;
	private String page_title = "";
	String teacher_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		setContentView(R.layout.college_activity_feedback);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
		getList();
	}

	private void getList() {
		initialize();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				1);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(getApplicationContext(), "id")));
		new TeacherListAsync().execute(nameValuePairs);
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
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	private void initialize() {
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		spinner = (Spinner) findViewById(R.id.spnList);
		ttitle = (TextView) findViewById(R.id.ttitle_txt);
		title = (TextView) findViewById(R.id.title_txt);
		submite = (Button) findViewById(R.id.submit_btn);
		submite.setText("New Feedback");
		discription = (EditText) findViewById(R.id.description_edt);
		header = (TextView) findViewById(R.id.header_text);
		header.setText("Feedback");
        
		submite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (discription.getText().toString().length() <= 0) {
					Toast.makeText(CollegeFeedbackActivity.this,
							"Enter Description", Toast.LENGTH_SHORT).show();
					return;
				}
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(getApplicationContext(), "id")));
				nameValuePairs.add(new BasicNameValuePair("teacher_id",
						teacher_id));
				nameValuePairs.add(new BasicNameValuePair("description",
						discription.getText().toString().trim()));
				Log.d("ASSIGMENTTTTTT", "" + nameValuePairs);
				new UploadAsyntask().execute(nameValuePairs);

				// new UploadAsyntask().execute(nameValuePairs);
			}

		});
	}
	


	private class TeacherListAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeFeedbackActivity.this);
			dialog.setTitle("Teachers");
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
					CollegeUrls.api_list_teacher);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONArray array = json.getJSONArray("data");
							temp = new ArrayList<ParentProfileInfo>();
							name = new ArrayList<String>();
							id = new ArrayList<String>();
							for (int i = 0; i < array.length(); i++) {
								newD = new ParentProfileInfo(
										array.getJSONObject(i));
								name.add(newD.fullname);
								id.add(newD.id);
								temp.add(newD);
							}
						} catch (Exception e) {

						}
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
				ArrayAdapter<String> data = new ArrayAdapter<String>(
						CollegeFeedbackActivity.this,
						R.layout.simple_spinner_item_custom_new, name);
				data.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);

				spinner.setAdapter(data);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						teacher_id = id.get(pos);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

			} else {
			}

		}
	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeFeedbackActivity.this);
			dialog.setTitle("Feedback");
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
					CollegeUrls.api_create_feedback);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						error = "Feedback Created.";
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
				dialoggg = new AlertDialog.Builder(CollegeFeedbackActivity.this);
				dialoggg.setTitle(getResources()
						.getString(R.string.assignments));
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeFeedbackActivity.this.finish();
								/*
								 * Intent intent = new Intent(
								 * TeacherAssignmentAddActivity.this,
								 * TeacherMainActivity.class);
								 * intent.setFlags(Intent
								 * .FLAG_ACTIVITY_CLEAR_TOP);
								 * 
								 * startActivity(intent);
								 */
							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils
								.showDialog(CollegeFeedbackActivity.this,
										page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeFeedbackActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeFeedbackActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}

		}

	}

}
