package com.knwedu.ourschool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.FileUtils;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherActivityAddActivity extends FragmentActivity {
	private TextView ttitle, title, header;
	private Button  submite, select_file;
	private Subject subject;
	private static Button date;
	private EditText discription, titleEdt, marksEdt;/* codeEdt */
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	private static String dateSelected;
	public DatabaseAdapter mDatabase;
	private static final int FILE_SELECT_CODE = 0;
	private String encrypted_file_name = null;
	int serverResponseCode = 0;
	private boolean internetAvailable = false;
	String path = "null";
	private String page_title = "", chk_marks_per = "Yes";
	private CheckBox chk_marks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		setContentView(R.layout.activity_teacher_assignment);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(TeacherActivityAddActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

		internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
				false);
		initialize();
	}

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(
					Intent.createChooser(intent, "Select a File to Upload"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				Uri selectedUri = data.getData();
				if (selectedUri == null) {
					return;
				}

				try {
					path = FileUtils.getPath(this, selectedUri);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (TextUtils.isEmpty(path)) {
					try {

						path = FileUtils.getDriveFileAbsolutePath(
								TeacherActivityAddActivity.this, selectedUri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Log.d("TAG", "File Path: " + path);
				select_file.setText(path);

			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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

		ttitle = (TextView) findViewById(R.id.ttitle_txt);
		title = (TextView) findViewById(R.id.title_txt);
		date = (Button) findViewById(R.id.date_btns);
		submite = (Button) findViewById(R.id.submit_btn);
		submite.setText("Create " + page_title);
		discription = (EditText) findViewById(R.id.description_edt);
		titleEdt = (EditText) findViewById(R.id.title_edt);
		select_file = (Button) findViewById(R.id.select_file_for_upload);
		// codeEdt = (EditText) findViewById(R.id.code_edt);
		chk_marks = (CheckBox) findViewById(R.id.chk_marks);
		chk_marks.setChecked(true);
		chk_marks.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					marksEdt.setVisibility(View.VISIBLE);
					chk_marks_per = "Yes";
				} else {
					marksEdt.setVisibility(View.INVISIBLE);
					chk_marks_per = "No";
				}
			}
		});
		marksEdt = (EditText) findViewById(R.id.marks_edt);
		if ((SchoolAppUtils.GetSharedParameter(TeacherActivityAddActivity.this,
				Constants.ACTIVITY_MARK_PERMISSION)).equalsIgnoreCase("0")) {
			marksEdt.setVisibility(View.GONE);
			chk_marks.setVisibility(View.GONE);
		} else {
			marksEdt.setVisibility(View.VISIBLE);
			chk_marks.setVisibility(View.VISIBLE);
		}
		dateSelected = SchoolAppUtils.getCurrentDate();
		date.setText(SchoolAppUtils.getDayDifferentDif(SchoolAppUtils
				.getCurrentDate()));

		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);

		if (getIntent().getExtras() != null) {
			if (internetAvailable) {
				String temp = getIntent().getExtras().getString(
						Constants.TSUBJECT);
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
				String subject_id = getIntent().getExtras().getString(
						Constants.OFFLINE_SUBJECT_ID);
				mDatabase.open();
				subject = mDatabase.getSubject(subject_id);
				mDatabase.close();
			}
		}
		if (subject != null) {
			ttitle.setText("Class: " + subject.classs + " "
					+ subject.section_name);
			title.setText("" + subject.sub_name);
		} else {
			ttitle.setText("");
			title.setText("");
		}

		date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		select_file.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showFileChooser();
			}
		});
		submite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (dateSelected.toString().equalsIgnoreCase("date")) {
					Toast.makeText(TeacherActivityAddActivity.this,
							"Select Date", Toast.LENGTH_SHORT).show();
					return;
				}
				if (titleEdt.getText().toString().length() <= 0) {
					Toast.makeText(TeacherActivityAddActivity.this,
							"Enter Title", Toast.LENGTH_SHORT).show();
					return;
				}
				if (discription.getText().toString().length() <= 0) {
					Toast.makeText(TeacherActivityAddActivity.this,
							"Enter Description", Toast.LENGTH_SHORT).show();
					return;
				}
				if ((SchoolAppUtils.GetSharedParameter(
						TeacherActivityAddActivity.this,
						Constants.ACTIVITY_MARK_PERMISSION))
						.equalsIgnoreCase("0")) {
					marksEdt.setVisibility(View.INVISIBLE);
				} else if (chk_marks_per.equalsIgnoreCase("Yes")) {
					if (marksEdt.getText().toString().length() <= 0) {
						if (marksEdt.getText().toString().length() <= 0 || marksEdt.getText().toString().contains(".") || marksEdt.getText().toString().equalsIgnoreCase("0")) {
							Toast.makeText(TeacherActivityAddActivity.this,
									"Enter Marks", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}
				/*
				 * if(codeEdt.getText().toString().length() <= 0) {
				 * Toast.makeText(TeacherAssignmentAddActivity.this,
				 * "Enter Code", Toast.LENGTH_SHORT).show(); return; }
				 */
				if (!internetAvailable) {
					new OfflineAssignmentListenerAsync().execute();
				} else {

					if (select_file.getText().toString()
							.equalsIgnoreCase(getString(R.string.attachment))) {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								15);
						nameValuePairs.add(new BasicNameValuePair(
								"user_type_id", SchoolAppUtils
										.GetSharedParameter(
												getApplicationContext(),
												Constants.USERTYPEID)));
						nameValuePairs.add(new BasicNameValuePair(
								"organization_id", SchoolAppUtils
										.GetSharedParameter(
												getApplicationContext(),
												Constants.UORGANIZATIONID)));
						nameValuePairs.add(new BasicNameValuePair("user_id",
								SchoolAppUtils.GetSharedParameter(
										getApplicationContext(),
										Constants.USERID)));
						nameValuePairs.add(new BasicNameValuePair("subject_id",
								subject.id));
						nameValuePairs.add(new BasicNameValuePair("section_id",
								subject.section_id));
						nameValuePairs.add(new BasicNameValuePair("cl_title",
								titleEdt.getText().toString().trim()));
						nameValuePairs.add(new BasicNameValuePair(
								"description", discription.getText().toString()
										.trim()));
						nameValuePairs.add(new BasicNameValuePair("marks1",
								marksEdt.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair(
								"submit_date", dateSelected));
						nameValuePairs.add(new BasicNameValuePair(
								"created_type", "3"));
						nameValuePairs
								.add(new BasicNameValuePair("a_type", "2"));
						nameValuePairs.add(new BasicNameValuePair("file_name",
								""));
						nameValuePairs.add(new BasicNameValuePair("orig_name",
								""));
						new UploadAsyntask().execute(nameValuePairs);
					} else {
						dialog = ProgressDialog.show(
								TeacherActivityAddActivity.this, "",
								"Uploading file...", true);

						new Thread(new Runnable() {
							@Override
							public void run() {
								uploadFile(select_file.getText().toString());
							}
						}).start();
					}
				}
			}
		});
	}

	@SuppressLint("ValidFragment")
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {

			Calendar newDate = Calendar.getInstance();
			newDate.set(year, month, day);
			Calendar minDate = Calendar.getInstance();
			if (minDate != null && minDate.after(newDate)) {
				Toast.makeText(
						((TeacherActivityAddActivity)getActivity()),
						"Please set todays or future dates," + " not past date",
						Toast.LENGTH_LONG).show();
				onCreateDialog(getArguments());
				return;
			}
			String mon, da;
			if (month < 10) {
				mon = "0" + month;
			} else {
				mon = "" + month;
			}
			if (day < 10) {
				da = "0" + day;
			} else {
				da = "" + day;
			}
			date.setText(SchoolAppUtils.getDayDifferent(year + "/" + mon + "/"
					+ da));
			month = month + 1;
			if (month < 10) {
				mon = "0" + month;
			} else {
				mon = "" + month;
			}
			dateSelected = year + "-" + mon + "-" + da;
		}
	}

	public int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			// TeacherAssignmentAddActivity.this.finish();
			Intent intent = new Intent(TeacherActivityAddActivity.this,
					TeacherActivityListActivity.class);
			intent.putExtra(Constants.TASSIGNMENT, subject.object.toString());
			startActivity(intent);
			Log.e("uploadFile", "Source File not exist :"
					+ select_file.getText().toString());

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					select_file.setText("Source File not exist :"
							+ select_file.getText().toString());
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(Urls.upload_url);
				String test = Urls.upload_url;

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();
				InputStream stream = conn.getInputStream();
				InputStreamReader isReader = new InputStreamReader(stream);

				// put output stream into a string
				BufferedReader br = new BufferedReader(isReader);

				String result = null;
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					encrypted_file_name = line;
				}

				System.out.println(encrypted_file_name);
				br.close();
				Log.i("uploadFile", "HTTP Response is : " + result + ": "
						+ serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							/*
							 * Toast.makeText(TeacherAssignmentAddActivity.this,
							 * "File Upload Complete.", Toast.LENGTH_SHORT)
							 * .show();
							 */
							String title = null, descp = null, code = null;
							title = titleEdt.getText().toString();
							descp = discription.getText().toString();
							// code = codeEdt.getText().toString();
							String uuploadfile_name = null;
							String uuuploadfile_name;

							final String[] tokens = select_file.getText()
									.toString().split("/");
							for (String s : tokens) {
								System.out.println(s);
								uuploadfile_name = s;
							}
							uuuploadfile_name = uuploadfile_name;
							if (uuuploadfile_name
									.equalsIgnoreCase(getString(R.string.attachment))) {
								uuuploadfile_name = "";
							}
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									15);
							nameValuePairs.add(new BasicNameValuePair(
									"user_type_id", SchoolAppUtils
											.GetSharedParameter(
													getApplicationContext(),
													Constants.USERTYPEID)));
							nameValuePairs
									.add(new BasicNameValuePair(
											"organization_id",
											SchoolAppUtils.GetSharedParameter(
													getApplicationContext(),
													Constants.UORGANIZATIONID)));
							nameValuePairs.add(new BasicNameValuePair(
									"user_id", SchoolAppUtils
											.GetSharedParameter(
													getApplicationContext(),
													Constants.USERID)));
							nameValuePairs.add(new BasicNameValuePair(
									"subject_id", subject.id));
							nameValuePairs.add(new BasicNameValuePair(
									"section_id", subject.section_id));
							nameValuePairs.add(new BasicNameValuePair(
									"cl_title", title));
							nameValuePairs.add(new BasicNameValuePair(
									"description", descp));
							nameValuePairs.add(new BasicNameValuePair("marks1",
									marksEdt.getText().toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"submit_date", dateSelected));
							nameValuePairs.add(new BasicNameValuePair(
									"created_type", "3"));
							nameValuePairs.add(new BasicNameValuePair("a_type",
									"2"));
							nameValuePairs.add(new BasicNameValuePair(
									"file_name", encrypted_file_name));
							nameValuePairs.add(new BasicNameValuePair(
									"orig_name", uuploadfile_name));
							new UploadAsyntask().execute(nameValuePairs);

						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();

				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						select_file
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(TeacherActivityAddActivity.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();

				e.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						select_file.setText("Got Exception : see logcat ");
						Toast.makeText(TeacherActivityAddActivity.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();

			return serverResponseCode;

		} // End else block
	}

	class OfflineAssignmentListenerAsync extends
			AsyncTask<String, Assignment, Boolean> {

		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(String... params) {
			long row_id = 0;
			mDatabase.open();
			Assignment activity = new Assignment();
			activity.submit_date = dateSelected;
			activity.created_date = SchoolAppUtils.getCurrentDate();
			activity.description = discription.getText().toString().trim();
			activity.total_marks = marksEdt.getText().toString();
			activity.marks = "";
			activity.title = titleEdt.getText().toString().trim();
			activity.is_marked = "0";
			activity.is_published = "0";
			activity.file_name = path;
			activity.attachment = "null";
			activity.subject_id = subject.id;
			activity.section_id = subject.section_id;

			row_id = mDatabase.addAssignment(activity, 3);
			mDatabase.close();

			return (row_id > 0);
		}

		@Override
		protected void onProgressUpdate(Assignment... values) {
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			final AlertDialog.Builder dialog = new AlertDialog.Builder(
					TeacherActivityAddActivity.this);
			dialog.setTitle("New " + page_title);
			// mDatabase.close();
			if (result) {
				dialog.setMessage(page_title + " created in Offline Mode");
			} else {
				dialog.setMessage("Error in inserting Data");
			}

			dialog.setNegativeButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			dialog.show();
		}

	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherActivityAddActivity.this);
			dialog.setTitle("Creating " + page_title);
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
					Urls.api_activity_save);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						error = page_title + " created successfully";
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
						TeacherActivityAddActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								TeacherActivityAddActivity.this.finish();

							}
						});
				dialoggg.show();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(TeacherActivityAddActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(TeacherActivityAddActivity.this,
							page_title,
							getResources().getString(R.string.error));
				}

			}

		}

	}

}
