package com.knwedu.ourschool;

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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.FileUtils;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ParentChildRequestActivity extends FragmentActivity {
	private TextView header, select_file;
	private Button submite;
	private EditText titleEdt, discription;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	private String type,title;
	private static final int FILE_SELECT_CODE = 0;
	private String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_parent_request);
		SchoolAppUtils.loadAppLogo(ParentChildRequestActivity.this, (ImageButton) findViewById(R.id.app_logo));
		
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
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		submite = (Button) findViewById(R.id.submit_btn);
		discription = (EditText) findViewById(R.id.description_edt);
		titleEdt = (EditText) findViewById(R.id.title_edt);
		select_file = (TextView) findViewById(R.id.attachment);
		select_file.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showFileChooser();
			}
		});
		header = (TextView) findViewById(R.id.header_text);
		if (getIntent().getExtras() != null) {
			type = getIntent().getExtras().getString("request_id");
			title = getIntent().getExtras().getString(Constants.PAGE_TITLE);
		}
		if (type.equalsIgnoreCase("2")) {
			header.setText(title);
			select_file.setVisibility(View.GONE);
		} else if (type.equalsIgnoreCase("3")) {
			header.setText(title);
			select_file.setVisibility(View.GONE);
		} else if (type.equalsIgnoreCase("4")) {
			header.setText(title);
			select_file.setVisibility(View.GONE);
		} else if (type.equalsIgnoreCase("5")) {
			header.setText(title);
			select_file.setVisibility(View.VISIBLE);
		}
		submite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (titleEdt.getText().toString().length() <= 0) {
					Toast.makeText(ParentChildRequestActivity.this,
							"Enter Title", Toast.LENGTH_SHORT).show();
					return;
				}

				if (discription.getText().toString().length() <= 0) {
					Toast.makeText(ParentChildRequestActivity.this,
							"Enter Description", Toast.LENGTH_SHORT).show();
					return;
				}

				String title = null, descp = null;
				title = titleEdt.getText().toString().trim();
				descp = discription.getText().toString().trim();

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						11);
				nameValuePairs.add(new BasicNameValuePair("user_id",
						SchoolAppUtils.GetSharedParameter(
								ParentChildRequestActivity.this,
								Constants.USERID)));
				nameValuePairs.add(new BasicNameValuePair("user_type_id",
						SchoolAppUtils.GetSharedParameter(
								ParentChildRequestActivity.this,
								Constants.USERTYPEID)));
				nameValuePairs.add(new BasicNameValuePair("organization_id",
						SchoolAppUtils.GetSharedParameter(
								ParentChildRequestActivity.this,
								Constants.UORGANIZATIONID)));
				nameValuePairs.add(new BasicNameValuePair("child_id",
						SchoolAppUtils.GetSharedParameter(
								ParentChildRequestActivity.this,
								Constants.CHILD_ID)));
				nameValuePairs.add(new BasicNameValuePair("cl_title", title));
				nameValuePairs
						.add(new BasicNameValuePair("description", descp));
				nameValuePairs.add(new BasicNameValuePair("type", "" + type));

				if (path == null) {
					nameValuePairs.add(new BasicNameValuePair("file_name", ""));
					nameValuePairs.add(new BasicNameValuePair("orig_name", ""));
					new CreateRequestAsyntask().execute(nameValuePairs);
				} else {
					// changeImages(selectedImagePath, "file", url);
					new SendImageTask(path, nameValuePairs)
							.execute(Urls.base_url
									+ Urls.api_parent_child_request_upload);
				}
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private class CreateRequestAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ParentChildRequestActivity.this);
			dialog.setTitle("Creating " + getResources().getString(R.string.requests));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", Urls.api_parent_child_request_save);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_parent_child_request_save);

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
				}else {
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
						ParentChildRequestActivity.this);
				dialoggg.setTitle(header.getText().toString());
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								ParentChildRequestActivity.this.finish();
							}
						});
				dialoggg.show();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(ParentChildRequestActivity.this, header.getText().toString(), error);
				}else{
					SchoolAppUtils.showDialog(ParentChildRequestActivity.this, header.getText().toString(), getResources().getString(R.string.unknown_response));
				}

			}
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
								ParentChildRequestActivity.this, selectedUri);
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

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute() Shows the dialog and sets
		 *      the text
		 */
		private SendImageTask(String selectedImagePath,
				List<NameValuePair> nameValuePairs) {
			this.selectedImagePath = selectedImagePath;
			this.nameValuePairs = nameValuePairs;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ParentChildRequestActivity.this);
			dialog.setTitle("Creating " + getResources().getString(R.string.special_request));
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
					
					String uploadfile_name = "";

					final String[] tokens = select_file.getText()
							.toString().split("/");
					for (String s : tokens) {
						System.out.println(s);
						uploadfile_name = s;
					}
					if (uploadfile_name
							.equalsIgnoreCase(getString(R.string.attachment))) {
						uploadfile_name = "";
					}
					nameValuePairs.add(new BasicNameValuePair("orig_name",
							uploadfile_name));
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

				new CreateRequestAsyntask().execute(nameValuePairs);

			} else {
				if (checkSize) {
					SchoolAppUtils
							.showDialog(ParentChildRequestActivity.this,
									getTitle().toString(),
									"Sorry, File is too large to upload. Please select another file.");

				} else if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils.showDialog(
								ParentChildRequestActivity.this, getTitle().toString(), error);
					} else {
						SchoolAppUtils
								.showDialog(
										ParentChildRequestActivity.this,
										getTitle().toString(),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							ParentChildRequestActivity.this,
							getTitle().toString(),
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
