package com.knwedu.ourschool;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.DataStructureFramwork.Student;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherTestResultsActivity extends FragmentActivity implements
		OnClickListener {
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
	public DatabaseAdapter mDatabase;
	private boolean internetAvailable = false;
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_teacher_result);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);

		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
				false);
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
						Constants.ASSIGNMENTSTUDENT);
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
			title.setText("" + test.title);
			classs.setText("");

			if (internetAvailable) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						15);
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
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherTestResultsActivity.this);
			dialog.setTitle(page_title);
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
					Urls.api_activity_save_marks);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						error = "Marks Uploaded successfully";

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
						TeacherTestResultsActivity.this);
				dialoggg.setTitle(page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								TeacherTestResultsActivity.this.finish();
								Intent intent = new Intent(
										TeacherTestResultsActivity.this,
										TeacherMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(TeacherTestResultsActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(TeacherTestResultsActivity.this,
							page_title, getResources()
									.getString(R.string.error));
				}
			}
		}
	}

	private class GetListAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherTestResultsActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_activity_get_marks);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("student_info");
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
				if (students != null) {

					for (int i = 0; i < students.size(); i++) {
						addView(students.get(i).name, students.get(i).roll_no,
								i);
					}
				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(TeacherTestResultsActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(TeacherTestResultsActivity.this,
							page_title, getResources()
									.getString(R.string.error));
				}
			}
		}
	}

	private void addView(String name, String nums, final int num) {
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout row = (LinearLayout) inflater.inflate(
				R.layout.teacher_result_item, null);
		if (name != null && nums != null) {
			((TextView) row.findViewById(R.id.student_name_txt)).setText(nums
					+ " " + name);
		}
		if (students.get(num).total_marks.equals("na")) {
			students.get(num).total_marks = "na";
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
						String marks = ((EditText) row.findViewById(R.id.marks_edt))
								.getText().toString();
						if (marks.length() > 0) {
							boolean check = false;
							try {
								if (Double.parseDouble(arg0.toString()) > Double
										.parseDouble(test.total_marks)) {
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
								students.get(num).total_marks = marks;
							}
						} else {
							students.get(num).total_marks = "na";
						}
					}
				});
		((TextView) row.findViewById(R.id.total_marks_txt))
				.setText(test.total_marks);
		if (SchoolAppUtils.GetSharedParameter(TeacherTestResultsActivity.this,
				Constants.USERTYPEID).equalsIgnoreCase("3")) {
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
								TeacherTestResultsActivity.this);
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
													TeacherTestResultsActivity.this,
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
				if (students.get(i).total_marks.equalsIgnoreCase("na")) {
					Toast.makeText(TeacherTestResultsActivity.this,
							"All Students are not marked", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}
			HashMap<String, String> hash = new HashMap<String, String>();

			for (int i = 0; i < students.size(); i++) {
				/*if (Integer.parseInt(students.get(i).total_marks) > (Integer
						.parseInt(test.total_marks) + 1)) {
					SchoolAppUtils.showDialog(TeacherTestResultsActivity.this,
							"Marks",
							"One of your entered marks is greater than assigned marks. Please check "
									+ "you entered marks");
					return;
				}*/
				hash.put(students.get(i).id, students.get(i).total_marks);
			}
			HashMap<String, String> hash_com = new HashMap<String, String>();

			for (int i = 0; i < students.size(); i++) {
				/*if (Integer.parseInt(students.get(i).total_marks) > (Integer
						.parseInt(test.total_marks) + 1)) {
					SchoolAppUtils.showDialog(TeacherTestResultsActivity.this,
							"Marks",
							"One of your entered marks is greater than assigned marks. Please check "
									+ "you entered marks");
					return;
				}*/
				hash_com.put(students.get(i).id, students.get(i).comment);
			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getApplicationContext(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getApplicationContext(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getApplicationContext(),
							Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("id", test.id));
			nameValuePairs.add(new BasicNameValuePair("marks_info", hash
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("comments_info", hash_com
					.toString()));
			new UploadAsyntask().execute(nameValuePairs);
		}
	}
}
