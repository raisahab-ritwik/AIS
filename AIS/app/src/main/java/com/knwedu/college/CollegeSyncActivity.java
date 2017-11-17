package com.knwedu.college;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.knwedu.college.utils.CollegeDataStructureFramwork.Announcement;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Attendance;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeSyncActivity extends Activity {
	String error;
	public CollegeDBAdapter mDatabase;
	private Button btnSync;
	private ImageButton btnBack;
	private TextView txtInfo, txtError;
	private long length;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDatabase = ((SchoolApplication) getApplication()).getCollegeDatabase();
		setContentView(R.layout.college_sync);
		SchoolAppUtils.loadAppLogo(CollegeSyncActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

		initialize();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	private void initialize() {

		btnBack = (ImageButton) findViewById(R.id.back_btn);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
		txtInfo = (TextView) findViewById(R.id.txtInfo);
		mDatabase.open();
		int pendingAssignments = mDatabase.getAssignmentCount();
		// int pendingAttendances = mDatabase.getAttendanceCount();
		int pendingAnnouncements = mDatabase.getAnnounceCount();
		// int pendingLessonPlans = mDatabase.getLessonCount();
		// int pendingWeeklyLessonPlans = mDatabase.getLessonCountWeekly();
		mDatabase.close();
		/*
		 * String txt = "Pending Data:\n" + "Assignment/Quiz/Activity: " +
		 * pendingAssignments + "\nAnnouncement/Coursework: " +
		 * pendingAnnouncements + "\nLesson Plan(Daily/Weekly): " +
		 * pendingLessonPlans + "+" + pendingWeeklyLessonPlans +
		 * "\nAttendance: " + pendingAttendances;
		 */
		String txt = "Pending Data:\n" + "Assignment/Internal: "
				+ pendingAssignments + "\nAnnouncement/Coursework: "
				+ pendingAnnouncements;
		txtInfo.setText(txt);
		btnSync = (Button) findViewById(R.id.btnSync);
		btnSync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (SchoolAppUtils.isOnline(CollegeSyncActivity.this)) {
					new OfflineSyncAsync().execute();
				} else {
					SchoolAppUtils.showDialog(CollegeSyncActivity.this,
							"Check Network Connection",
							"internet might be not working");
				}
			}
		});

		txtError = (TextView) findViewById(R.id.txtError);
		txtError.setText("Press Synchronize button and wait,\n this may take some time!");

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	protected void onStop() {
		super.onStop();
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	class OfflineSyncAsync extends AsyncTask<Void, Integer, Boolean> {
		private ProgressDialog dialog;
		private String errorMsg = "";
		private String dialogMsg = "Please Wait..";
		private int maxProgress = 0;
		private String type = "1";
		private String type1 = "2";

		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(CollegeSyncActivity.this);
			dialog.setTitle("Uploading Offline Data");
			dialog.setMessage("Please Wait..");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.show();

		}

		protected Boolean doInBackground(Void... params) {

			// Upload Assignment, Quiz, Activity
			mDatabase.open();
			int pendingAssignments = mDatabase.getAssignmentCount();
			mDatabase.close();
			if (pendingAssignments > 0) {
				uploadAssignmentRequest();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Upload Announcement, Coursework
			mDatabase.open();
			int pendingAnnouncements = mDatabase.getAnnounceCount();
			mDatabase.close();
			if (pendingAnnouncements > 0) {

				uploadAnnouncementRequest();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			/*
			 * // Upload Attendance uploadAttendanceRequest(); try {
			 * Thread.sleep(1000); } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } // Upload
			 * Lesson Plan UploadLessonPlan(type1); try { Thread.sleep(1000); }
			 * catch (InterruptedException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } // Upload Daily Lesson Plan
			 * uploadWeeklyLesson(type); try { Thread.sleep(1000); } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			this.dialog.setMessage(dialogMsg);
			if (maxProgress > 0) {
				dialog.setIndeterminate(false);
				dialog.setMax(maxProgress);
				dialog.setProgress(values[0]);
			} else {
				dialog.setIndeterminate(true);
				dialog.setMax(100);
				dialog.setProgress(0);
			}
			if (errorMsg.length() > 0) {
				txtError.setText(errorMsg);
			}
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {
				mDatabase.open();
				int pendingAssignments = mDatabase.getAssignmentCount();
				int pendingAnnouncements = mDatabase.getAnnounceCount();

				String txt = "Pending Data:\n" + "Assignment/Internal: "
						+ pendingAssignments + "\nAnnouncement/Coursework: "
						+ pendingAnnouncements;
				txtInfo.setText(txt);
				txtError.setText("Data Upload Complete!!");
			}
			mDatabase.close();
			btnBack.setClickable(true);
		}

		public void SizeFile(String sourceFileUri) {
			File file = new File(sourceFileUri);
			length = file.length();
			length = length / 1024;
			System.out.println("File Path : " + file.getPath()
					+ ", File size : " + length + " KB");
		}

		// Upload data from assignment table
		private void uploadAssignmentRequest() {
			boolean tryNext = true;
			ArrayList<Assignment> assignments = new ArrayList<Assignment>();
			mDatabase.open();
			assignments = mDatabase.getAllAssignmentsForUpload();
			mDatabase.close();
			dialogMsg = "Assignment, Internal";
			maxProgress = assignments.size();
			publishProgress(0);
			for (int i = 0; i < assignments.size(); i++) {
				if (tryNext) {
					tryNext = false;
					// Upload file first
					String fileName = assignments.get(i).file_name;
					Log.d("file_name", assignments.get(i).file_name);
					String newFileName = "";
					String newOrigName = "";

					if (!fileName.equalsIgnoreCase("null")) {
						HttpURLConnection conn = null;
						DataOutputStream dos = null;
						String lineEnd = "\r\n";
						String twoHyphens = "--";
						String boundary = "*****";
						int bytesRead, bytesAvailable, bufferSize;
						byte[] buffer;
						int maxBufferSize = 1 * 1024 * 1024;
						File sourceFile = new File(fileName);
						SizeFile(assignments.get(i).file_name);
						if (sourceFile.isFile()) {
							try {
								// open a URL connection to the Servlet
								FileInputStream fileInputStream = new FileInputStream(
										sourceFile);
								URL url = new URL(CollegeUrls.base_url
										+ CollegeUrls.upload_url);

								// Open a HTTP connection to the URL
								conn = (HttpURLConnection) url.openConnection();
								conn.setDoInput(true); // Allow Inputs
								conn.setDoOutput(true); // Allow Outputs
								conn.setUseCaches(false); // Don't use a Cached
															// Copy
								conn.setRequestMethod("POST");
								conn.setRequestProperty("Connection",
										"Keep-Alive");
								conn.setRequestProperty("ENCTYPE",
										"multipart/form-data");
								conn.setRequestProperty("Content-Type",
										"multipart/form-data;boundary="
												+ boundary);
								conn.setRequestProperty("uploaded_file",
										fileName);

								dos = new DataOutputStream(
										conn.getOutputStream());

								dos.writeBytes(twoHyphens + boundary + lineEnd);
								dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
										+ fileName + "\"" + lineEnd);

								dos.writeBytes(lineEnd);

								// create a buffer of maximum size
								bytesAvailable = fileInputStream.available();

								bufferSize = Math.min(bytesAvailable,
										maxBufferSize);
								buffer = new byte[bufferSize];

								// read file and write it into form...
								bytesRead = fileInputStream.read(buffer, 0,
										bufferSize);

								while (bytesRead > 0) {

									dos.write(buffer, 0, bufferSize);
									bytesAvailable = fileInputStream
											.available();
									bufferSize = Math.min(bytesAvailable,
											maxBufferSize);
									bytesRead = fileInputStream.read(buffer, 0,
											bufferSize);

								}

								dos.writeBytes(lineEnd);
								dos.writeBytes(twoHyphens + boundary
										+ twoHyphens + lineEnd);

								// Responses from the server (code and message)
								int serverResponseCode = conn.getResponseCode();
								InputStream stream = conn.getInputStream();
								InputStreamReader isReader = new InputStreamReader(
										stream);

								// put output stream into a string
								BufferedReader br = new BufferedReader(isReader);

								String result = null;
								String line;
								String encrypted_file_name = "";
								while ((line = br.readLine()) != null) {
									System.out.println(line);
									encrypted_file_name = line;
								}

								br.close();
								Log.i("uploadFile", "HTTP Response is : "
										+ result + ": " + serverResponseCode);

								if (serverResponseCode == 200) {
									final String[] tokens = fileName.split("/");
									for (String s : tokens) {
										newOrigName = s;
									}
								}
								newFileName = encrypted_file_name;

							} catch (IOException e) {

							}
						}

					} // end of file upload

					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
							13);
					nameValuePair.add(new BasicNameValuePair("id",
							CollegeAppUtils.GetSharedParameter(
									getApplicationContext(), "id")));
					Log.d("id", CollegeAppUtils.GetSharedParameter(
							getApplicationContext(), "id"));
					nameValuePair.add(new BasicNameValuePair(
							"subject_relation_id",
							assignments.get(i).subject_relation_id));
					Log.d("subject_relation_id",
							assignments.get(i).subject_relation_id);

					nameValuePair.add(new BasicNameValuePair("title",
							assignments.get(i).title));
					Log.d("title", assignments.get(i).title);
					nameValuePair.add(new BasicNameValuePair("description",
							assignments.get(i).description));
					Log.d("description", assignments.get(i).description);
					nameValuePair.add(new BasicNameValuePair("marks",
							assignments.get(i).total_marks));
					Log.d("marks", assignments.get(i).total_marks);
					nameValuePair.add(new BasicNameValuePair("submit_date",
							assignments.get(i).submit_date));
					Log.d("submit_date", assignments.get(i).submit_date);
					nameValuePair.add(new BasicNameValuePair("type", Integer
							.toString(assignments.get(i).type)));
					Log.d("type", Integer.toString(assignments.get(i).type));
					nameValuePair.add(new BasicNameValuePair("file_name",
							newOrigName));
					Log.d("file_name", newFileName);
					nameValuePair.add(new BasicNameValuePair("attachment",
							newFileName));
					Log.d("attachment", newOrigName);
					nameValuePair.add(new BasicNameValuePair("size", ""
							+ length));
					System.out.println("size" + length);
					JsonParser jParser = new JsonParser();

					JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
							CollegeUrls.api_activity_save);
					try {

						if (json != null) {
							if (json.getString("result").equalsIgnoreCase("1")) {
								// Now delete entry from database
								mDatabase.open();
								tryNext = mDatabase
										.deleteAssignment(assignments.get(i).row_id);
								mDatabase.close();

								publishProgress(i + 1);
							}
						}

					} catch (JSONException e) {
						Log.d("Json exception", e.getMessage());
					}
				} // end of if try next
			} // end of for loop

			if (tryNext == false) {
				errorMsg = "Error while uploading data\nPlease Try again..";
			}
			publishProgress(assignments.size());
		}

		private void uploadAttendanceRequest() {
			boolean tryNext = true;
			ArrayList<String> atten_lecturenum = new ArrayList<String>();
			atten_lecturenum = mDatabase.getAlllectureAttendance();
			dialogMsg = "Attendance";
			maxProgress = atten_lecturenum.size();
			publishProgress(0);
			for (int i = 0; i < atten_lecturenum.size(); i++) {
				if (tryNext) {
					tryNext = false;

					ArrayList<Attendance> attendance = new ArrayList<Attendance>();
					attendance = mDatabase
							.getAllAttendanceforUpload(atten_lecturenum.get(i));
					HashMap<String, String> marks = new HashMap<String, String>();
					HashMap<String, String> late = new HashMap<String, String>();

					for (int j = 0; j < attendance.size(); j++) {
						marks.put(attendance.get(j).student_id,
								attendance.get(j).status);
					}

					for (int z = 0; z < attendance.size(); z++) {
						late.put(attendance.get(z).student_id,
								attendance.get(z).status);
					}
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							5);
					nameValuePairs.add(new BasicNameValuePair("user_type_id",
							SchoolAppUtils.GetSharedParameter(
									getApplicationContext(),
									CollegeConstants.USERTYPEID)));
					nameValuePairs.add(new BasicNameValuePair(
							"organization_id", SchoolAppUtils
									.GetSharedParameter(
											getApplicationContext(),
											CollegeConstants.UORGANIZATIONID)));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							SchoolAppUtils.GetSharedParameter(
									getApplicationContext(),
									CollegeConstants.USERID)));
					nameValuePairs.add(new BasicNameValuePair("section_id",
							attendance.get(i).section_id));
					nameValuePairs.add(new BasicNameValuePair("teacher_id",
							attendance.get(i).teacher_id));
					nameValuePairs.add(new BasicNameValuePair("sr_id",
							attendance.get(i).sr_id));

					nameValuePairs.add(new BasicNameValuePair("group_id",
							attendance.get(i).group_id));
					nameValuePairs.add(new BasicNameValuePair("lecture_num",
							attendance.get(i).lecture_num));
					nameValuePairs.add(new BasicNameValuePair("date",
							attendance.get(i).date));
					nameValuePairs.add(new BasicNameValuePair("stu_stat", marks
							.toString()));
					nameValuePairs.add(new BasicNameValuePair("late_rea", late
							.toString()));
					JsonParser jParser = new JsonParser();
					JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
							CollegeUrls.api_submit_all_attendance);
					try {

						if (json != null) {
							if (json.getString("result").equalsIgnoreCase("1")) {
								// Now delete entry from database
								tryNext = mDatabase.deleteAttendance(attendance
										.get(i).lecture_num);

								publishProgress(i + 1);
							}
						}

					} catch (JSONException e) {
						Log.d("Json exception", e.getMessage());
					}
				}
			}
			if (tryNext == false) {
				errorMsg = "Error while uploading data\nPlease Try again..";
			}
			publishProgress(atten_lecturenum.size());

		}

		// Upload Data for Announcement table
		private void uploadAnnouncementRequest() {
			boolean tryNext = true;
			ArrayList<Announcement> announcement = new ArrayList<Announcement>();
			mDatabase.open();
			announcement = mDatabase.getAllAnnouncementforUpload();
			mDatabase.close();
			dialogMsg = "Announcement & Coursework";
			maxProgress = announcement.size();
			publishProgress(0);
			for (int i = 0; i < announcement.size(); i++) {
				if (tryNext) {
					tryNext = false;
					// Upload file first
					String fileName = announcement.get(i).file_name;
					Log.d("file_name", announcement.get(i).file_name);
					String newFileName = "";
					String newOrigName = "";

					if (!fileName.equalsIgnoreCase("null")) {
						HttpURLConnection conn = null;
						DataOutputStream dos = null;
						String lineEnd = "\r\n";
						String twoHyphens = "--";
						String boundary = "*****";
						int bytesRead, bytesAvailable, bufferSize;
						byte[] buffer;
						int maxBufferSize = 1 * 1024 * 1024;
						File sourceFile = new File(fileName);
						SizeFile(announcement.get(i).file_name);
						if (sourceFile.isFile()) {
							try {
								// open a URL connection to the Servlet
								FileInputStream fileInputStream = new FileInputStream(
										sourceFile);
								URL url = new URL(CollegeUrls.base_url
										+ CollegeUrls.upload_url);

								// Open a HTTP connection to the URL
								conn = (HttpURLConnection) url.openConnection();
								conn.setDoInput(true); // Allow Inputs
								conn.setDoOutput(true); // Allow Outputs
								conn.setUseCaches(false); // Don't use a Cached
															// Copy
								conn.setRequestMethod("POST");
								conn.setRequestProperty("Connection",
										"Keep-Alive");
								conn.setRequestProperty("ENCTYPE",
										"multipart/form-data");
								conn.setRequestProperty("Content-Type",
										"multipart/form-data;boundary="
												+ boundary);
								conn.setRequestProperty("uploaded_file",
										fileName);

								dos = new DataOutputStream(
										conn.getOutputStream());

								dos.writeBytes(twoHyphens + boundary + lineEnd);
								dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
										+ fileName + "\"" + lineEnd);

								dos.writeBytes(lineEnd);

								// create a buffer of maximum size
								bytesAvailable = fileInputStream.available();

								bufferSize = Math.min(bytesAvailable,
										maxBufferSize);
								buffer = new byte[bufferSize];

								// read file and write it into form...
								bytesRead = fileInputStream.read(buffer, 0,
										bufferSize);

								while (bytesRead > 0) {

									dos.write(buffer, 0, bufferSize);
									bytesAvailable = fileInputStream
											.available();
									bufferSize = Math.min(bytesAvailable,
											maxBufferSize);
									bytesRead = fileInputStream.read(buffer, 0,
											bufferSize);

								}

								dos.writeBytes(lineEnd);
								dos.writeBytes(twoHyphens + boundary
										+ twoHyphens + lineEnd);

								// Responses from the server (code and message)
								int serverResponseCode = conn.getResponseCode();
								InputStream stream = conn.getInputStream();
								InputStreamReader isReader = new InputStreamReader(
										stream);

								// put output stream into a string
								BufferedReader br = new BufferedReader(isReader);

								String result = null;
								String line;
								String encrypted_file_name = "";
								while ((line = br.readLine()) != null) {
									System.out.println(line);
									encrypted_file_name = line;
								}

								br.close();
								Log.i("uploadFile", "HTTP Response is : "
										+ result + ": " + serverResponseCode);

								if (serverResponseCode == 200) {
									final String[] tokens = fileName.split("/");
									for (String s : tokens) {
										newOrigName = s;
									}
								}
								newFileName = encrypted_file_name;

							} catch (IOException e) {

							}
						}

					} // end of file upload

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							15);
					nameValuePairs.add(new BasicNameValuePair("id",
							CollegeAppUtils.GetSharedParameter(
									getApplicationContext(), "id")));
					nameValuePairs.add(new BasicNameValuePair(
							"subject_relation_id",
							announcement.get(i).subject_relation_id));
					Log.d("subject_relation_id",
							announcement.get(i).subject_relation_id);
					/*
					 * nameValuePairs.add(new BasicNameValuePair("section_id",
					 * announcement.get(i).section_id));
					 */
					nameValuePairs.add(new BasicNameValuePair("title",
							announcement.get(i).title));
					nameValuePairs.add(new BasicNameValuePair("description",
							announcement.get(i).description));

					nameValuePairs.add(new BasicNameValuePair("type",
							announcement.get(i).type));
					nameValuePairs.add(new BasicNameValuePair("file_name",
							newOrigName));
					Log.d("file_name", newFileName);
					nameValuePairs.add(new BasicNameValuePair("attachment",
							newFileName));
					Log.d("attachment", newOrigName);
					nameValuePairs.add(new BasicNameValuePair("size", ""
							+ length));
					System.out.println("size" + length);
					JsonParser jParser = new JsonParser();
					JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
							CollegeUrls.api_announcement_create);
					try {

						if (json != null) {
							if (json.getString("result").equalsIgnoreCase("1")) {
								// Now delete entry from database
								mDatabase.open();
								tryNext = mDatabase
										.deleteAnnouncement(announcement.get(i).row_id);
								mDatabase.close();

								publishProgress(i + 1);
							}
						}

					} catch (JSONException e) {
						Log.d("Json exception", e.getMessage());
					}
				}
			}
			if (tryNext == false) {
				errorMsg = "Error while uploading data\nPlease Try again..";
			}
			publishProgress(announcement.size());

		}

	}

}
