package com.knwedu.college;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeFileUtils;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.college.utils.CollegeAppUtils;

import com.knwedu.comschoolapp.R;

public class CollegeParentChildLeaveActivity extends FragmentActivity {
	private TextView header;
	private Button fromDate, toDate, submite, attachment;
	private EditText discription, titleEdt;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	private boolean checkDateSelect;
	private String dateToSelected, dateFromSelected;
	private String request_id, request_show_date;
	private String page_title = "";
	private static final int FILE_SELECT_CODE = 0;
	private static final int GALLERY_KITKAT = 0;
	private String encrypted_file_name = null;
	int serverResponseCode = 0;
	private boolean internetAvailable = false;
	String path = "null";
	long length;
	Date date1, date2, date3 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_parent_leave_request);
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
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	private void initialize() {
		if (getIntent().getExtras() != null) {
			request_id = getIntent().getExtras().getString("request_id");
			request_show_date = getIntent().getExtras().getString(
					"request_show_date");
		}
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		fromDate = (Button) findViewById(R.id.from_date_btns);
		toDate = (Button) findViewById(R.id.to_date_btns);
		submite = (Button) findViewById(R.id.submit_btn);
		discription = (EditText) findViewById(R.id.description_edt);
		titleEdt = (EditText) findViewById(R.id.title_edt);
		attachment = (Button) findViewById(R.id.attachment);
		attachment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showFileChooser();
				// Intent intent = new Intent();
				// intent.setType("*/*");
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// startActivityForResult(Intent.createChooser(intent,
				// "Select File"), SELECT_PICTURE);
			}
		});

		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);
		/*
		 * if (page_title.equalsIgnoreCase("ID Request")) {
		 * fromDate.setVisibility(View.GONE); toDate.setVisibility(View.GONE); }
		 * else if (page_title.equalsIgnoreCase("Books Request")) {
		 * fromDate.setVisibility(View.GONE); toDate.setVisibility(View.GONE); }
		 * else
		 */
		if (request_show_date.equalsIgnoreCase("0")) {
			fromDate.setVisibility(View.GONE);
			toDate.setVisibility(View.GONE);
		}
		dateToSelected = "date";
		dateFromSelected = "date";

		fromDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkDateSelect = false;
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		toDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkDateSelect = true;
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		submite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * if (page_title.equalsIgnoreCase("ID Request") ||
				 * page_title.equalsIgnoreCase("Books Request")) {
				 */
				if (request_show_date.equalsIgnoreCase("0")) {
				} else if (dateFromSelected.equalsIgnoreCase("date")) {
					Toast.makeText(CollegeParentChildLeaveActivity.this,
							"Select Start Date", Toast.LENGTH_SHORT).show();
				}
				/*
				 * if (page_title.equalsIgnoreCase("ID Request") ||
				 * page_title.equalsIgnoreCase("Books Request")) { }
				 */
				if (request_show_date.equalsIgnoreCase("0")) {

				} else if (dateToSelected.equalsIgnoreCase("date")) {
					Toast.makeText(CollegeParentChildLeaveActivity.this,
							"Select End Date", Toast.LENGTH_SHORT).show();
					return;
				}
				if (titleEdt.getText().toString().length() <= 0) {
					Toast.makeText(CollegeParentChildLeaveActivity.this,
							"Enter Title", Toast.LENGTH_SHORT).show();
					return;
				}
				if (discription.getText().toString().length() <= 0) {
					Toast.makeText(CollegeParentChildLeaveActivity.this,
							"Enter Description", Toast.LENGTH_SHORT).show();
					return;
				}

				String title = null, descp = null;
				title = titleEdt.getText().toString();
				descp = discription.getText().toString();
				if (attachment.getText().toString()
						.equalsIgnoreCase("attachment")) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							7);
					nameValuePairs.add(new BasicNameValuePair("id",
							CollegeAppUtils.GetSharedParameter(
									CollegeParentChildLeaveActivity.this, "id")));
					nameValuePairs.add(new BasicNameValuePair("type",
							request_id));
					nameValuePairs.add(new BasicNameValuePair("title", title));
					nameValuePairs.add(new BasicNameValuePair("description",
							descp));
					nameValuePairs.add(new BasicNameValuePair("date_to",
							dateFromSelected));
					nameValuePairs.add(new BasicNameValuePair("date_end",
							dateToSelected));
					nameValuePairs.add(new BasicNameValuePair("file_name", ""));
					nameValuePairs.add(new BasicNameValuePair("orig_name", ""));
					nameValuePairs.add(new BasicNameValuePair("size", ""));
					new UploadAsyntask().execute(nameValuePairs);
				} else {

					dialog = ProgressDialog.show(
							CollegeParentChildLeaveActivity.this, "",
							"Uploading file...", true);

					new Thread(new Runnable() {
						@Override
						public void run() {
							/*
							 * new uploadFile().execute(select_file.getText()
							 * .toString());
							 */
							uploadFile(attachment.getText().toString());
						}

					}).start();

					// changeImages(selectedImagePath, "file", url);
					// new
					// SendImageTask().execute(attachment.getText().toString());
					/*
					 * new SendImageTask(selectedImagePath, nameValuePairs)
					 * .execute(CollegeUrls.base_url +
					 * CollegeUrls.api_parent_child_request_upload);
					 */
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

	public String uploadFile(String sourceFileUri) {

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

			CollegeParentChildLeaveActivity.this.finish();
			/*
			 * Intent intent = new Intent( CollegeParentChildLeaveActivity.this,
			 * CollegeTeacherAssignmentListActivity.class);
			 * intent.putExtra(CollegeConstants.TASSIGNMENT,
			 * subject.object.toString()); startActivity(intent);
			 */
			Log.e("uploadFile", "Source File not exist :"
					+ attachment.getText().toString());

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					attachment.setText("Source File not exist :"
							+ attachment.getText().toString());
				}
			});

			return null;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(CollegeUrls.base_url + CollegeUrls.upload_url);

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
							String title = null, descp = null, code = null;
							title = titleEdt.getText().toString();
							descp = discription.getText().toString();
							SizeFile(attachment.getText().toString());
							String uuploadfile_name = null;
							String uuuploadfile_name;

							final String[] tokens = attachment.getText()
									.toString().split("/");
							for (String s : tokens) {
								System.out.println(s);
								uuploadfile_name = s;
							}
							uuuploadfile_name = uuploadfile_name;
							if (uuuploadfile_name
									.equalsIgnoreCase("attachment")) {
								uuuploadfile_name = "";
							}

							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									7);
							nameValuePairs
									.add(new BasicNameValuePair(
											"id",
											CollegeAppUtils
													.GetSharedParameter(
															CollegeParentChildLeaveActivity.this,
															"id")));
							nameValuePairs.add(new BasicNameValuePair("type",
									request_id));
							nameValuePairs.add(new BasicNameValuePair("title",
									title));
							nameValuePairs.add(new BasicNameValuePair(
									"description", descp));
							nameValuePairs.add(new BasicNameValuePair(
									"date_to", dateFromSelected));
							nameValuePairs.add(new BasicNameValuePair(
									"date_end", dateToSelected));
							nameValuePairs.add(new BasicNameValuePair(
									"file_name", encrypted_file_name));
							nameValuePairs.add(new BasicNameValuePair(
									"orig_name", uuploadfile_name));
							nameValuePairs.add(new BasicNameValuePair("size",
									"" + length));
							new UploadAsyntask().execute(nameValuePairs);

						}
					});
				}

				// close the streams // fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();

				ex.printStackTrace();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						attachment
								.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(CollegeParentChildLeaveActivity.this,
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
						attachment.setText("Got Exception : see logcat ");
						Toast.makeText(CollegeParentChildLeaveActivity.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();

			return null;

		} // End else block

	}

	@SuppressLint("ValidFragment")
	private class DatePickerFragment extends DialogFragment implements
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
			if (checkDateSelect) {
				toDate.setText(CollegeAppUtils.getDayDifferent(year + "/" + mon
						+ "/" + da));
			} else {
				fromDate.setText(CollegeAppUtils.getDayDifferent(year + "/"
						+ mon + "/" + da));
			}

			month = month + 1;
			if (month < 10) {
				mon = "0" + month;
			} else {
				mon = "" + month;
			}

			if (checkDateSelect) {
				String dafrmselec = year + "-" + mon + "-" + da;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date1 = sdf.parse(dafrmselec.toString());
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					date3 = sdf.parse(dateFromSelected.toString());
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("last" + sdf.format(date1));
				// System.out.println("last" + sdf.format(date2));
				// System.out.println("last" + sdf.format(date3));

				if (date1.before(date3)) {

					Toast.makeText(CollegeParentChildLeaveActivity.this,
							"End date can not be less than start date.",
							Toast.LENGTH_SHORT).show();
					toDate.setText("End Date");
				} else {
					dateToSelected = year + "-" + mon + "-" + da;
				}
			} else {

				String dafrmselec = year + "-" + mon + "-" + da;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date1 = sdf.parse(dafrmselec.toString());
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					date3 = sdf.parse(CollegeAppUtils.getCurrentDate());
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("last" + sdf.format(date1));
				// System.out.println("last" + sdf.format(date2));
				// System.out.println("last" + sdf.format(date3));

				if (date3.after(date1)) {

					Toast.makeText(CollegeParentChildLeaveActivity.this,
							"Start date cannot be less than current date",
							Toast.LENGTH_SHORT).show();
					fromDate.setText("Start Date");
				} else {
					dateFromSelected = year + "-" + mon + "-" + da;
				}

			}

			/*if (checkDateSelect) {
				dateToSelected = year + "-" + mon + "-" + da;
			} else {
				dateFromSelected = year + "-" + mon + "-" + da;
			}*/
		}
	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentChildLeaveActivity.this);
			dialog.setTitle(getResources().getString(R.string.request_leave));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_parent_child_request_save);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_parent_child_request_save);

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
						CollegeParentChildLeaveActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeParentChildLeaveActivity.this.finish();
							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeParentChildLeaveActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeParentChildLeaveActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeParentChildLeaveActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaColumns.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { BaseColumns._ID }, MediaColumns.DATA + "=? ",
				new String[] { filePath }, null);

		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaColumns.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	/**
	 * Get bitmap
	 * 
	 * @param file
	 * @param size
	 * @return
	 */
	public static Bitmap decodeFile(String file, int size) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file, o);

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;// (int)Math.pow(2, (double)(scale-1));
		while (true) {
			if (width_tmp / 2 < size || height_tmp / 2 < size) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale++;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeFile(file, o2);
	}

	public String getPath(Uri uri) {
		try {
			String[] projection = { MediaColumns.DATA };
			Cursor cursor = managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get preview
	 * 
	 * @param selectedImageUri2
	 * @return
	 */
	Bitmap getPreview(String selectedImageUri2) {
		File image = new File(selectedImageUri2);

		BitmapFactory.Options bounds = new BitmapFactory.Options();
		bounds.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(image.getPath(), bounds);
		if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
			return null;

		int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
				: bounds.outWidth;

		BitmapFactory.Options opts = new BitmapFactory.Options();
		/**
		 * if((int)Math.pow(2, (double)(originalSize-1)) > 0) {
		 */
		// opts.inSampleSize = powerOfTwo(originalSize);// / (int)Math.pow(2,
		// (double)(originalSize-1));
		/**
		 * } else { opts.inSampleSize = originalSize; }
		 */
		opts.inSampleSize = originalSize / 100;
		return BitmapFactory.decodeFile(image.getPath(), opts);
	}

	private String getFileName(String filePath) {
		String fileName = null;
		int lenght = filePath.lastIndexOf("/");
		fileName = filePath.substring(lenght + 1);
		return fileName;
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
								CollegeParentChildLeaveActivity.this, uri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Log.d("TAG", "File Path: " + path);
				attachment.setText(path);

			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*********************************************************************
	 * AsyncTask for sending Image
	 * 
	 * @see android.os.AsyncTask
	 ******************************************************************/
	private class SendImageTask extends AsyncTask<String, Void, Boolean> {

		String selectedImagePath;
		StringBuilder total;
		String error;
		private boolean checkSize;
		List<NameValuePair> nameValuePairs;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentChildLeaveActivity.this);
			dialog.setTitle(getResources().getString(R.string.request_leave));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[]) Making HTTP call,
		 *      writing bytes to a file, saving it to sd card and view
		 */
		@SuppressLint("ParserError")
		@Override
		protected Boolean doInBackground(String... params) {

			HttpURLConnection connection = null;
			DataOutputStream outputStream = null;

			String pathToOurFile = this.selectedImagePath;
			String urlServer = params[0];
			urlServer = urlServer.replaceAll("(?:\\n|\\r)", "%20");
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";

			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			try {
				FileInputStream fileInputStream = new FileInputStream(new File(
						pathToOurFile));

				URL url = new URL(urlServer);
				connection = (HttpURLConnection) url.openConnection();

				// Allow Inputs & Outputs
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);

				// Enable POST method
				connection.setRequestMethod("POST");

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				connection.setRequestProperty("ENCTYPE", "multipart/form-data");

				connection.setRequestProperty("uploaded_file", pathToOurFile);

				outputStream = new DataOutputStream(
						connection.getOutputStream());
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream
						.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
								+ pathToOurFile + "\"" + lineEnd);
				outputStream.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// Read file
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				try {
					while (bytesRead > 0) {
						outputStream.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					checkSize = true;
					fileInputStream.close();
					return false;
				}
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens
						+ lineEnd);

				// Responses from the server (code and message)
				int serverResponseCode = connection.getResponseCode();
				String serverResponseMessage = connection.getResponseMessage();
				InputStream input = null;
				total = new StringBuilder();
				try {
					BufferedReader r = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));

					String line;
					while ((line = r.readLine()) != null) {
						total.append(line);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				fileInputStream.close();
				outputStream.flush();
				outputStream.close();
				if (total != null) {

					nameValuePairs.add(new BasicNameValuePair("file_name",
							total.toString()));
					nameValuePairs.add(new BasicNameValuePair("orig_name",
							attachment.getText().toString()));
					return true;
				} else {
					return false;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}

		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object) Opening
		 *      next screen or show error if its an expection and mark it as
		 *      read
		 */
		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {

				new UploadAsyntask().execute(nameValuePairs);

			} else {
				if (checkSize) {
					CollegeAppUtils
							.showDialog(CollegeParentChildLeaveActivity.this,
									page_title,
									"Sorry, File is too large to upload. Please select another file.");

				} else if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeParentChildLeaveActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeParentChildLeaveActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeParentChildLeaveActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}
	}

	/**
	 * Translate the JSON response from server
	 * 
	 * @param urlServer
	 */
	public void getjsonresponse(String urlServer) {
		JSONObject json = getJSONFromUrl(urlServer);

		try {
			if (json != null) {
				json.getString("data");
				Log.e("json", "results:" + json.getString("result"));
				Log.e("json", "data:" + json.getString("data"));
				// Log.e("places", "total places archieved = " + count);
			}

		} catch (JSONException e) {
			Log.e("jsonexp", "exception agayi");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * get JSON from URL
	 * 
	 * @param String
	 *            url
	 * @return
	 */
	public JSONObject getJSONFromUrl(String url) {
		Log.e("json", "getJSONfromurl" + url);

		Log.e("UMWA1", "Start webservie");
		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10000);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					10000);
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// sb.append(line + "n");
				sb.append(line);
			}
			is.close();
			json = sb.toString();

			// try parse the string to a JSON object

			jObj = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
			jObj = null;
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		} catch (UnsupportedEncodingException e) {
			jObj = null;
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			jObj = null;
			e.printStackTrace();
		} catch (IOException e) {
			jObj = null;
			e.printStackTrace();
		} catch (Exception e) {
			jObj = null;
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		Log.e("UMWA1", "end");
		// return JSON String
		return jObj;

	}

	ContentResolver mContentResolver;

	@SuppressLint({ "NewApi", "NewApi" })
	private void changeImages(String path, String name, String url) {
		if (path != null && name != null) {
			String dir = Environment.getExternalStorageDirectory()
					+ "/SchoolApp/Images/";

			Bitmap out = decodeFile(new File(path));
			File temp = new File(dir);
			boolean cehk = temp.mkdirs();
			File file = new File(temp, name + ".jpeg");
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream fOut;
			try {
				fOut = new FileOutputStream(file);
				out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
				out.recycle();

			} catch (Exception e) {

			}
			path = file.getAbsolutePath();
		}
		// new SendImageTask(path, name).execute(url);
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 300;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
}
