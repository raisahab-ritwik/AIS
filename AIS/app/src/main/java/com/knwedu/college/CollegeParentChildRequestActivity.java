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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentActivity;
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
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
public class CollegeParentChildRequestActivity extends FragmentActivity {
	private TextView header, attachment;
	private Button submite;
	private EditText titleEdt, discription;
	private ProgressDialog dialog;
	AlertDialog.Builder dialoggg;
	private String type;
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_parent_request);
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
		attachment = (TextView) findViewById(R.id.attachment);
		attachment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogBoxForImages();
			}
		});
		header = (TextView) findViewById(R.id.header_text);
		if (getIntent().getExtras() != null) {
			type = getIntent().getExtras().getString("request_id");
		}
		if (type.equals("2")) {
			header.setText(R.string.request_ID_card);
			attachment.setVisibility(View.GONE);
		} else if (type.equals("3")) {
			header.setText(R.string.request_books);
			attachment.setVisibility(View.GONE);
		} else if (type.equals("4")) {
			header.setText(R.string.special_request);
			attachment.setVisibility(View.VISIBLE);
		}
		submite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(titleEdt.getText().toString().length() <= 0)
				{
					Toast.makeText(CollegeParentChildRequestActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
					return;
				}

				if (discription.getText().toString().length() <= 0) {
					Toast.makeText(CollegeParentChildRequestActivity.this,
							"Enter Description", Toast.LENGTH_SHORT).show();
					return;
				}
				/*
				 * Pattern p = Pattern.compile("[A-Z0-9a-z_.+-/=* ]*"); Matcher
				 * m = p.matcher(discription.getText().toString().trim());
				 * boolean b= m.matches(); if(!b) {
				 * Toast.makeText(ParentChildRequestActivity.this,
				 * "Please Correct the Description entered",
				 * Toast.LENGTH_SHORT).show(); return; }
				 */

				String title = null, descp = null;
				title = titleEdt.getText().toString().trim();
				descp = discription.getText().toString().trim();

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						7);
				nameValuePairs.add(new BasicNameValuePair("id",
						CollegeAppUtils.GetSharedParameter(
								CollegeParentChildRequestActivity.this,
								"id")));
				nameValuePairs.add(new BasicNameValuePair("title", title));
				nameValuePairs.add(new BasicNameValuePair("description", descp));
				nameValuePairs.add(new BasicNameValuePair("type", "" + type));

				if (selectedImagePath == null) {
					 nameValuePairs.add(new BasicNameValuePair("file_name", ""));
					 nameValuePairs.add(new BasicNameValuePair("attachment", ""));
					new UploadAsyntask().execute(nameValuePairs);
				} else {
//					changeImages(selectedImagePath, "file", url);
					new SendImageTask(selectedImagePath, nameValuePairs).execute(CollegeUrls.base_url + CollegeUrls.api_parent_child_request_upload);
				}
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	
	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentChildRequestActivity.this);
			dialog.setTitle(getResources().getString(R.string.requests));
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
						CollegeParentChildRequestActivity.this);
				dialoggg.setTitle(getResources().getString(R.string.requests));
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeParentChildRequestActivity.this.finish();
							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeParentChildRequestActivity.this,page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeParentChildRequestActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeParentChildRequestActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String selectedImagePath = null;
	private Uri selectedImageUri = null;
	private static final int SELECT_PICTURE = 11412;
	private static final int SELECT_CAMERA = 1337;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean checkError = true;
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE || requestCode == SELECT_CAMERA) {
				try {
					if (data.getData() == null) {

						// Describe the columns you'd like to have returned.
						// Selecting from the Thumbnails location gives you both
						// the
						// Thumbnail Image ID, as well as the original image ID
						String[] projection = {
								BaseColumns._ID, // The columns we wANT
								MediaStore.Images.Thumbnails.IMAGE_ID,
								MediaStore.Images.Thumbnails.KIND,
								MediaStore.Images.Thumbnails.DATA };
						String selection = MediaStore.Images.Thumbnails.KIND
								+ "=" + // Select
								// only // mini's
								MediaStore.Images.Thumbnails.MINI_KIND;
						String sort = BaseColumns._ID + " DESC";
						// At the moment, this is a bit of a hack, as I'm
						// returning
						// ALL images, and just taking the latest one. There is
						// a
						// better way to narrow this down I think with a WHERE
						// clause which is currently the selection variable
						Cursor myCursor = this
								.managedQuery(
										MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
										projection, selection, null, sort);
						long imageId = 0l;
						long thumbnailImageId = 0l;
						String thumbnailPath = "";
						try {
							myCursor.moveToFirst();
							imageId = myCursor
									.getLong(myCursor
											.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
							thumbnailImageId = myCursor.getLong(myCursor
									.getColumnIndexOrThrow(BaseColumns._ID));
							thumbnailPath = myCursor
									.getString(myCursor
											.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
						} finally {
							myCursor.close();
						}
						// Create new Cursor to obtain the file Path for the
						// large
						// image
						String[] largeFileProjection = { BaseColumns._ID,
								MediaColumns.DATA };
						String largeFileSort = BaseColumns._ID + " DESC";
						myCursor = this.managedQuery(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								largeFileProjection, null, null, largeFileSort);
						String largeImagePath = "";
						try {
							myCursor.moveToFirst();
							// This will actually give yo uthe file path
							// location of
							// the image.
							largeImagePath = myCursor.getString(myCursor
									.getColumnIndexOrThrow(MediaColumns.DATA));
						} finally {
							myCursor.close();
						}
						// These are the two URI's you'll be interested in. They
						// give you a handle to the actual images
						Uri uriLargeImage = Uri.withAppendedPath(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								String.valueOf(imageId));
						Uri uriThumbnailImage = Uri
								.withAppendedPath(
										MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
										String.valueOf(thumbnailImageId));
						selectedImageUri = uriLargeImage;

					} else {
						selectedImageUri = data.getData();
					}
					/**
					 * if(mBitmap != null) { mBitmap.recycle(); mBitmap = null;
					 * }
					 */
					selectedImagePath = getPath(selectedImageUri);
					Log.e("UMW", selectedImageUri.toString());
					Log.v("path", selectedImagePath);
					Log.e("MyThreaded",
							getImageContentUri(CollegeParentChildRequestActivity.this,
									new File(selectedImagePath)).toString());
					getPreview(selectedImagePath).getWidth();
					checkError = false;
				} catch (Exception e) {
					e.printStackTrace();
					selectedImagePath = null;
					Toast.makeText(CollegeParentChildRequestActivity.this,
							R.string.please_try_again, Toast.LENGTH_SHORT)
							.show();
				}
				if (!checkError) {
					if (Environment.MEDIA_MOUNTED.equals(Environment
							.getExternalStorageState())) {
						displayImages();
						// ((ImageView)findViewById(R.id.faxes_data_imageview)).setImageBitmap(getPreview(selectedImagePath));
					}
				}

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

	private void displayImages() {
		attachment.setText(getFileName(selectedImagePath));
	}

	private String getFileName(String filePath) {
		String fileName = null;
		int lenght = filePath.lastIndexOf("/");
		fileName = filePath.substring(lenght + 1);
		return fileName;
	}

	private void showDialogBoxForImages() {

		AlertDialog.Builder alertbox = new AlertDialog.Builder(
				CollegeParentChildRequestActivity.this);
		alertbox.setTitle("Select Option");

		alertbox.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		alertbox.setNeutralButton(R.string.camera,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, SELECT_CAMERA);
					}
				});
		alertbox.setPositiveButton(R.string.gallery,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Picture"),
								SELECT_PICTURE);
					}
				});
		alertbox.show();

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
		/** (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 * Shows the dialog and sets the text
		 */
		private SendImageTask(String selectedImagePath, List<NameValuePair> nameValuePairs) {
			this.selectedImagePath = selectedImagePath;
			this.nameValuePairs = nameValuePairs;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentChildRequestActivity.this);
			dialog.setTitle(getResources().getString(R.string.request_leave));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		/** (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 * Making HTTP call, writing bytes to a file, saving it to sd card
		 * and view 
		 */
		@SuppressLint("ParserError") @Override
		protected Boolean doInBackground(String... params) {

			HttpURLConnection connection = null;
			DataOutputStream outputStream = null;

			String pathToOurFile = this.selectedImagePath;
			String urlServer = params[0];
			urlServer = urlServer.replaceAll("(?:\\n|\\r)", "%20");
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary =  "*****";

			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1*1024*1024;

			try
			{
				FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

				URL url = new URL(urlServer);
				connection = (HttpURLConnection) url.openConnection();

				// Allow Inputs & Outputs
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);

				// Enable POST method
				connection.setRequestMethod("POST");

				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
				connection.setRequestProperty("ENCTYPE", "multipart/form-data");

				connection.setRequestProperty("uploaded_file", pathToOurFile);

				outputStream = new DataOutputStream( connection.getOutputStream() );
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + pathToOurFile +"\"" + lineEnd);
				outputStream.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// Read file
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				try
				{
				while (bytesRead > 0)
				{
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				}
				catch(OutOfMemoryError e)
				{
					e.printStackTrace();
					checkSize = true;
					fileInputStream.close();
					return false;
				}
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				int serverResponseCode = connection.getResponseCode();
				String serverResponseMessage = connection.getResponseMessage();
				InputStream input = null;
				total = new StringBuilder();
				try
				{
					BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));

					String line;
					while ((line = r.readLine()) != null) {
						total.append(line);
					}
				}
				catch (Exception e) {
					// TODO: handle exception
				}

				fileInputStream.close();
				outputStream.flush();
				outputStream.close();
				if(total != null)
				{
					
					
					nameValuePairs.add(new BasicNameValuePair("file_name", total.toString()));
			        nameValuePairs.add(new BasicNameValuePair("orig_name", attachment.getText().toString()));
					return true;
					

				}
				else
				{
					return false;
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return false;
			}


		}

		/** (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 * Opening next screen or show error if its an expection
		 *  and mark it as read
		 */
		@Override
		protected void onPostExecute(Boolean result){

			super.onPostExecute(result);
			if(dialog != null)
			{
				dialog.dismiss();
				dialog = null;
			}
			if(result)
			{
				

				new UploadAsyntask().execute(nameValuePairs);	
				
				
			}
			else
			{
				if(checkSize)
				{
					CollegeAppUtils.showDialog(CollegeParentChildRequestActivity.this,page_title, 
							"Sorry, File is too large to upload. Please select another file.");
				
				}
				else if(error != null)
				{
					if(error.length() > 0)
					{
						CollegeAppUtils.showDialog(CollegeParentChildRequestActivity.this,page_title, error);
					}
					else
					{
						CollegeAppUtils.showDialog(CollegeParentChildRequestActivity.this,page_title, 
								getResources().getString(R.string.please_check_internet_connection));
					}
				}
				else
				{
					CollegeAppUtils.showDialog(CollegeParentChildRequestActivity.this,page_title, 
							getResources().getString(R.string.please_check_internet_connection));
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
//		new SendImageTask(path, name).execute(url);
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
