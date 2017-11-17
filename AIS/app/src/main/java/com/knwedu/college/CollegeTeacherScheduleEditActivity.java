package com.knwedu.college;

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
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TeacherSchedule;
import com.knwedu.college.utils.CollegeFileUtils;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeTeacherScheduleEditActivity extends FragmentActivity {
	private Button submite, selectfile;
	private EditText discription;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	private String id, descp, encrypted_file_name;
	private int type;
	String uploadFileName = "";

	private static final int FILE_SELECT_CODE = 0;
	private static final int GALLERY_KITKAT = 0;
	int serverResponseCode = 0;
	/* public DatabaseAdapter mDatabase; */
	String path = "null";
	private TextView header;
	private String page_title = "";
	private long length;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		/*
		 * mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		 */setContentView(R.layout.college_activity_teacher_schedule_edit);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
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
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		// intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		// intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
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

				// Get the Uri of the selected file
				Uri uri = data.getData();
				Log.d("TAG", "File Uri: " + uri.toString());

				// Get the path
				try {
					path = CollegeFileUtils.getPath(this, uri);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (TextUtils.isEmpty(path)) {
					try {

						path = CollegeFileUtils.getDriveFileAbsolutePath(
								CollegeTeacherScheduleEditActivity.this, uri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Log.d("TAG", "File Path: " + path);
				selectfile.setText(path);

			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initialize() {

		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		submite = (Button) findViewById(R.id.submit_btn);
		selectfile = (Button) findViewById(R.id.select_file_for_upload);
		discription = (EditText) findViewById(R.id.description_edt);
		if (getIntent().getExtras() != null) {
			id = getIntent().getExtras()
					.getString(CollegeConstants.TSCHEDULEID);
			String edit = getIntent().getExtras().getString(
					CollegeConstants.TSCHEDULE);
			if (edit.equalsIgnoreCase("null")) {
				discription.setText("");
			} else {
				discription.setText(edit);
			}
		}
		selectfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showFileChooser();
			}
		});
		submite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (discription.getText().toString().length() <= 0) {
					Toast.makeText(CollegeTeacherScheduleEditActivity.this,
							"Enter Description", Toast.LENGTH_SHORT).show();
					return;
				}

				descp = null;
				descp = discription.getText().toString();
				/*
				 * if(!CollegeAppUtils.isOnline(CollegeTeacherScheduleEditActivity
				 * .this)) { new offlineFileAsync().execute(); }
				 */
				if (selectfile.getText().toString()
						.equalsIgnoreCase("attachment")) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							3);

					nameValuePairs.add(new BasicNameValuePair("topic", descp));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							CollegeAppUtils.GetSharedParameter(
									CollegeTeacherScheduleEditActivity.this,
									"id")));
					nameValuePairs.add(new BasicNameValuePair("id", id));

					nameValuePairs.add(new BasicNameValuePair("file_name", ""));
					nameValuePairs.add(new BasicNameValuePair("orig_name", ""));
					nameValuePairs.add(new BasicNameValuePair("size", ""));
					new UploadAsyntask().execute(nameValuePairs);
				} else {
					new UploadFileAsyntask().execute();
				}
			}
		});
	}

	public void SizeFile(String sourceFileUri) {
		File file = new File(sourceFileUri);
		length = file.length();
		length = length / 1024;
		System.out.println("File Path : " + file.getPath() + ", File size : "
				+ length + " KB");
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
		SizeFile(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					selectfile.setText("Source File not exist :"
							+ selectfile.getText().toString());
				}
			});

			return 0;
		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(CollegeUrls.base_url + CollegeUrls.upload_url);
				Log.d("test", "test");
				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				Log.d("test", "test");
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
				Log.d("test", "test");
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
						selectfile
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(CollegeTeacherScheduleEditActivity.this,
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
						selectfile.setText("Got Exception : see logcat ");
						Toast.makeText(CollegeTeacherScheduleEditActivity.this,
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

	private class offlineFileAsync extends
			AsyncTask<String, TeacherSchedule, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			long row_id = 0;
			/*
			 * mDatabase.open(); TeacherSchedule schedule = new
			 * TeacherSchedule(); schedule.topic = descp; schedule.id = id;
			 * schedule.doc = path; schedule.file = "null"; row_id =
			 * mDatabase.addLessonWeekly(schedule); mDatabase.close();
			 */
			return (row_id > 0);
		}

		@Override
		protected void onProgressUpdate(TeacherSchedule... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (result) {
				dialoggg = new AlertDialog.Builder(
						CollegeTeacherScheduleEditActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage("Updated Weekly in Offline Mode");
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeAppUtils
										.SetSharedBoolParameter(
												CollegeTeacherScheduleEditActivity.this,
												"update_list", true);
								CollegeTeacherScheduleEditActivity.this
										.finish();
								Intent intent = new Intent(
										CollegeTeacherScheduleEditActivity.this,
										CollegeTeacherMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

								startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeTeacherScheduleEditActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeTeacherScheduleEditActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeTeacherScheduleEditActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	private class UploadFileAsyntask extends AsyncTask<Void, Void, Void> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeTeacherScheduleEditActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (serverResponseCode == 200) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						String uuploadfile_name = null;
						String uuuploadfile_name;

						final String[] tokens = selectfile.getText().toString()
								.split("/");
						for (String s : tokens) {
							System.out.println(s);
							uuploadfile_name = s;
						}
						uuuploadfile_name = uuploadfile_name;
						if (uuuploadfile_name
								.equalsIgnoreCase("Select file for upload")) {
							uuuploadfile_name = "";
						}

						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								3);

						nameValuePairs.add(new BasicNameValuePair("topic",
								descp));
						nameValuePairs
								.add(new BasicNameValuePair(
										"user_id",
										CollegeAppUtils
												.GetSharedParameter(
														CollegeTeacherScheduleEditActivity.this,
														"id")));
						nameValuePairs.add(new BasicNameValuePair("id", id));

						nameValuePairs.add(new BasicNameValuePair("file_name",
								encrypted_file_name));
						nameValuePairs.add(new BasicNameValuePair("orig_name",
								uuploadfile_name));
						nameValuePairs.add(new BasicNameValuePair("size", ""
								+ length));
						new UploadAsyntask().execute(nameValuePairs);

					}
				});
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			uploadFile(selectfile.getText().toString());
			return null;
		}

	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeTeacherScheduleEditActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> url = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					CollegeUrls.api_weekly_schedule_save);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						try {
							error = json.getString("data");
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
				dialoggg = new AlertDialog.Builder(
						CollegeTeacherScheduleEditActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeAppUtils
										.SetSharedBoolParameter(
												CollegeTeacherScheduleEditActivity.this,
												"update_list", true);
								CollegeTeacherScheduleEditActivity.this
										.finish();

								Intent intent = new Intent(
										CollegeTeacherScheduleEditActivity.this,
										CollegeTeacherMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

								startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils
								.showDialog(
										CollegeTeacherScheduleEditActivity.this,
										CollegeAppUtils
												.GetSharedParameter(
														CollegeTeacherScheduleEditActivity.this,
														CollegeConstants.PAGE_TITLE),
										error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeTeacherScheduleEditActivity.this,
										CollegeAppUtils
												.GetSharedParameter(
														CollegeTeacherScheduleEditActivity.this,
														CollegeConstants.PAGE_TITLE),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeTeacherScheduleEditActivity.this,
							CollegeAppUtils.GetSharedParameter(
									CollegeTeacherScheduleEditActivity.this,
									CollegeConstants.PAGE_TITLE),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

}
