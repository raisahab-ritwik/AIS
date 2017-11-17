package com.knwedu.college;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Student;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;

public class CollegeTeacherTestResultsActivity extends FragmentActivity
		implements OnClickListener {
	private TextView title, classs, header;
	private Button submitBtn;
	private Assignment test;
	private LinearLayout listView;
	private ArrayList<Student> students;
	private ProgressDialog dialog;
	private LayoutInflater inflater;
	private Subject subject;
	private AlertDialog.Builder dialoggg;
	private Dialog dialogCustom;
	int row_id;
	String subject_id;
	public CollegeDBAdapter mDatabase;
	private boolean internetAvailable = false;
	private String page_title = "";
	private ImageView chk;
	private HashMap<String, String> hash_retake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_teacher_result);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
	    mDatabase = ((SchoolApplication) getApplication()).getCollegeDatabase();
		internetAvailable = getIntent().getBooleanExtra(
				CollegeConstants.IS_ONLINE, false);
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		title = (TextView) findViewById(R.id.title_txt);
		classs = (TextView) findViewById(R.id.class_txt);
		header = (TextView) findViewById(R.id.header_text);
		submitBtn = (Button) findViewById(R.id.submit_btn);
		if (internetAvailable) {
			submitBtn.setOnClickListener(this);
		}
		listView = (LinearLayout) findViewById(R.id.add_data_linearlayout);

		header.setText(R.string.results);

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
			if (internetAvailable) {
				String temp = getIntent().getExtras().getString(
						CollegeConstants.ASSIGNMENTSTUDENT);
				if (temp != null) {
					JSONObject object = null;
					try {
						object = new JSONObject(temp);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (object != null) {
						test = new Assignment(object);
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
				test = mDatabase.getAssignment(row_id, 2);
				mDatabase.close();

			}
		}
		if (test != null) {
			title.setText("" + test.title);
			classs.setText("");

			if (internetAvailable) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						15);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(getApplicationContext(), "id")));
				nameValuePairs.add(new BasicNameValuePair("activity_id",
						test.id));

				new GetListAsyntask().execute(nameValuePairs);
			} else {
				// Get Offline data
			}
		}
		if (students != null) {

			for (int i = 0; i < students.size(); i++) {
				addView(students.get(i).name, students.get(i).roll_no, i);
			}

		}

	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeTeacherTestResultsActivity.this);
			dialog.setTitle(page_title);
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
					CollegeUrls.api_activity_save_marks);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						error = "Marks Uploaded";

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
						CollegeTeacherTestResultsActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Intent intent = new Intent(
										CollegeTeacherTestResultsActivity.this,
										CollegeTeacherTestListActivity.class);
								
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								CollegeTeacherTestResultsActivity.this.finish();
								

							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeTeacherTestResultsActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeTeacherTestResultsActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeTeacherTestResultsActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}
	}

	private class GetListAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeTeacherTestResultsActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nvp = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					CollegeUrls.api_activity_get_marks);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("student_info");
						} catch (Exception e) {
							return true;
						}
						students = new ArrayList<Student>();
						for (int i = 0; i < array.length(); i++) {
							Student student = new Student(
									array.getJSONObject(i));
							students.add(student);
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

			if (result) {
				if (students != null) {

					for (int i = 0; i < students.size(); i++) {
						addView(students.get(i).name, students.get(i).roll_no,
								i);
					}
				}
				dialog.dismiss();
			} else {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeTeacherTestResultsActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeTeacherTestResultsActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeTeacherTestResultsActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}
	}

	private void addView(String name, String nums, final int num) {
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout row = (LinearLayout) inflater.inflate(
				R.layout.college_teacher_result_item, null);
		chk = (ImageView) row.findViewById(R.id.retake_chk);
		if (students.get(num).retake.equals("0")) {
			chk.setImageResource(R.drawable.unchk_retake72);
			// do nothing
		} else if (students.get(num).retake.equals("1")) {
			chk.setImageResource(R.drawable.chk_retake72);
		}
        chk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (students.get(num).retake.equals("1")) {
					Log.d("CHECKEDDDDDDDDDD", "true");
					((ImageView) row.findViewById(R.id.retake_chk))
							.setImageResource(R.drawable.unchk_retake72);
					students.get(num).retake = "0";
					students.get(num).total_marks = "0";
					((EditText) row.findViewById(R.id.marks_edt))
							.setText(students.get(num).total_marks);
					
				} else if (students.get(num).retake.equals("0")) {
					((ImageView) row.findViewById(R.id.retake_chk))
							.setImageResource(R.drawable.chk_retake72);
					students.get(num).retake = "1";
					students.get(num).total_marks = "0";
					((EditText) row.findViewById(R.id.marks_edt))
							.setText(students.get(num).total_marks);
				} else if (students.get(num).retake.equals("")) {
					((ImageView) row.findViewById(R.id.retake_chk))
							.setImageResource(R.drawable.chk_retake72);
					students.get(num).retake = "1";
					students.get(num).total_marks = "0";
					((EditText) row.findViewById(R.id.marks_edt))
							.setText(students.get(num).total_marks);
				}
			}

		});

		if (name != null) {
			((TextView) row.findViewById(R.id.student_name_txt)).setText(nums
					+ " " + name);
		}
		if (students.get(num).total_marks.equals("NA")) {
			students.get(num).total_marks = "NA";
		} else {
			((EditText) row.findViewById(R.id.marks_edt)).setText(students
					.get(num).total_marks);
		}
		((EditText) row.findViewById(R.id.marks_edt))
				.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
					}

					@Override
					public void afterTextChanged(Editable arg0) {
						if (((EditText) row.findViewById(R.id.marks_edt))
								.getText().toString().length() > 0) {
							boolean check = false;
							try {
								if (Integer.parseInt(arg0.toString()) > Integer
										.parseInt(test.total_marks)) {
									((EditText) row
											.findViewById(R.id.marks_edt))
											.setText(test.total_marks);
									((EditText) row
											.findViewById(R.id.marks_edt))
											.setSelection(test.total_marks
													.length());
									students.get(num).total_marks = test.total_marks;
									check = true;
								}
							} catch (Exception c) {
								check = true;
								students.get(num).total_marks = test.total_marks;
							}
							if (!check) {
								students.get(num).total_marks = arg0.toString();
							}
						} else {
							students.get(num).total_marks = "0";
						}
					}
				});
		((TextView) row.findViewById(R.id.total_marks_txt))
				.setText(test.total_marks);
		if (CollegeAppUtils.GetSharedParameter(
				CollegeTeacherTestResultsActivity.this,
				CollegeConstants.USERTYPEID).equalsIgnoreCase("3")) {
			((ImageButton) row.findViewById(R.id.comment_btn))
					.setVisibility(View.VISIBLE);
		} else {
			((ImageButton) row.findViewById(R.id.comment_btn))
					.setVisibility(View.GONE);
		}
		((ImageButton) row.findViewById(R.id.comment_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogCustom = new Dialog(
								CollegeTeacherTestResultsActivity.this);
						dialogCustom
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialogCustom
								.setContentView(R.layout.add_comments_dialog);
						if (students.get(num).comment != null) {
							if (students.get(num).comment.length() > 0) {
								((EditText) dialogCustom
										.findViewById(R.id.comments_edt))
										.setText(students.get(num).comment);
							}
						}
						((Button) dialogCustom.findViewById(R.id.add_btn))
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (((EditText) dialogCustom
												.findViewById(R.id.comments_edt))
												.getText().toString().length() <= 0) {
											Toast.makeText(
													CollegeTeacherTestResultsActivity.this,
													"Enter Comments",
													Toast.LENGTH_SHORT).show();
											return;
										}
										students.get(num).comment = ((EditText) dialogCustom
												.findViewById(R.id.comments_edt))
												.getText().toString();
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
		listView.addView(row);
	}

	private boolean doesStudentExists(String id) {
		if (students != null) {
			for (Student stu : students) {
				if (stu.id.equals(id)) {
					return true;
				}
			}
		}
		return false;
	}

	private void removeStudent(String id) {
		if (students != null) {
			for (Student stu : students) {
				if (stu.id.equals(id)) {
					students.remove(stu);
					break;
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.submit_btn) {
			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).total_marks.equals("NA")) {
					Toast.makeText(CollegeTeacherTestResultsActivity.this,
							"All Students are not marked", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}
			HashMap<String, String> hash = new HashMap<String, String>();
			for (int j = 0; j < students.size(); j++) {
				if (students.get(j).total_marks.equalsIgnoreCase("NA")) {
				}

				else {

					if(students.get(j).total_marks.equals("")){
						CollegeAppUtils.showDialog(
								CollegeTeacherTestResultsActivity.this,
								"Marks",
								"You can not put it blank or zero");
						return;

					}

					/*if (Integer.parseInt(students.get(j).total_marks) > (Integer
							.parseInt(test.total_marks) + 1)) {
						CollegeAppUtils.showDialog(
								CollegeTeacherTestResultsActivity.this,
								"Marks",
								"One of your entered marks is greater than assigned marks. Please check "
										+ "you entered marks");
						return;
					}
					*/
					hash.put(students.get(j).id, students.get(j).total_marks);
				}

			}
			HashMap<String, String> hash_com = new HashMap<String, String>();
			for (int z = 0; z < students.size(); z++) {
				if (students.get(z).total_marks.equalsIgnoreCase("NA")) {
				}

				else {

					if(students.get(z).total_marks.equals("")){
						CollegeAppUtils.showDialog(
								CollegeTeacherTestResultsActivity.this,
								"Marks",
								"You can not put it blank or zero");
						return;

					}

					/*if (Integer.parseInt(students.get(z).total_marks) > (Integer
							.parseInt(test.total_marks) + 1)) {
						CollegeAppUtils.showDialog(
								CollegeTeacherTestResultsActivity.this,
								"Marks",
								"One of your entered marks is greater than assigned marks. Please check "
										+ "you entered marks");
						return;
					}
					*/
					hash_com.put(students.get(z).id, students.get(z).comment);
				}
			}
			hash_retake = new HashMap<String, String>();
			for (int k = 0; k < students.size(); k++) {
				hash_retake.put(students.get(k).id, students.get(k).retake);

			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getApplicationContext(), "id")));
			nameValuePairs.add(new BasicNameValuePair("activity_id", test.id));
			nameValuePairs.add(new BasicNameValuePair("marks_info", hash
					.toString()));

			nameValuePairs.add(new BasicNameValuePair("comments_info", hash_com
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("retake_info", hash_retake
					.toString()));
			Log.d("PARAMSSSSSSSSSSSS", "" + nameValuePairs);
			
			new UploadAsyntask().execute(nameValuePairs);

		}

	}
}
