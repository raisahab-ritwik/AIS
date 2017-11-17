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
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DataStructureFramwork.TeacherWeeklyplan;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.FileUtils;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class WeeklyplanActivity extends Activity implements
		OnClickListener {
	private ArrayList<TeacherWeeklyplan> schedules;
	private ArrayList<TeacherWeeklyplan> daily_schedules;
	private Button edit;
	ImageButton addAssignment;
	private ImageButton download;
	Button select_file;
	private Spinner date, type;
	private TextView header, text;
	private List<String> days, type_selection, days_type_id,type_selection_id;
	private String id, selectedId = "", topic, filename_weekly, selected_id="",
			selectedtypeId = "";
	private static int selectedPosition;
	private ProgressDialog dialog;
	private String mode = "subject_schedule", encrypted_file_name, descp,
			id_for_daily_schedule;
	private LayoutInflater inflater;
	private LinearLayout linearLayout;
	private AlertDialog.Builder dialoggg;
	private EditText edTxt;
	private static final int FILE_SELECT_CODE = 0;
	private static final int GALLERY_KITKAT = 0;
	private Subject subject;
	int serverResponseCode = 0;
	int pos_date = 0;
	private TextView textEmpty;
	private boolean internetAvailable = false;

	String path = "null";
	Date date1, date2, date3 = null;
	String topicString = "";
	String file_id = "";
	private String page_title = "";
	private boolean service = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_teacher_weeklyplan);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(WeeklyplanActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		service = true;
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*
		 * download = (ImageButton) findViewById(R.id.download_btn_weekly);
		 * download.setVisibility(View.GONE);
		 */
		addAssignment = (ImageButton) findViewById(R.id.add_assignment);

		linearLayout = (LinearLayout) findViewById(R.id.layout_schedule_row);
		header = (TextView) findViewById(R.id.header_text);
		text = (TextView) findViewById(R.id.schedule_txt);
		date = (Spinner) findViewById(R.id.date_btns);
		type = (Spinner) findViewById(R.id.date_btns_type);
		header.setText(page_title);
		/*
		 * edit = (Button) findViewById(R.id.edt_btn);
		 * edit.setVisibility(View.GONE);
		 */
		/*
		 * if
		 * ((SchoolAppUtils.GetSharedParameter(TeacherWeeklyplanActivity.this,
		 * Constants.LESSONS_ADD_PERMISSION)).equalsIgnoreCase("0")) {
		 * edit.setVisibility(View.INVISIBLE); } edit.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { Intent intent = new
		 * Intent(TeacherWeeklyplanActivity.this,
		 * TeacherWeeklyplanEditActivity.class);
		 * intent.putExtra(Constants.TSCHEDULE, topic);
		 * intent.putExtra(Constants.TSCHEDULEID, selectedId);
		 * intent.putExtra(Constants.PAGE_TITLE, page_title);
		 * startActivity(intent); } });
		 */

		if (SchoolAppUtils.GetSharedParameter(WeeklyplanActivity.this,
				Constants.USERTYPEID).equals(Constants.USERTYPE_PARENT)
				|| SchoolAppUtils.GetSharedParameter(
						WeeklyplanActivity.this, Constants.USERTYPEID)
						.equals(Constants.USERTYPE_STUDENT)) {
			addAssignment.setVisibility(View.GONE);
		} else if(SchoolAppUtils.isOnline(getApplicationContext())){
			addAssignment.setVisibility(View.VISIBLE);
		} else
		{
			addAssignment.setVisibility(View.GONE);
		}

		addAssignment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WeeklyplanActivity.this,
						TeacherWeeklyplanAddActivity.class);
				intent.putExtra(Constants.IS_ONLINE, internetAvailable);
				intent.putExtra(Constants.TSUBJECT, subject.object.toString());
				intent.putExtra(Constants.PAGE_TITLE, page_title);
				startActivity(intent);
			}
		});
		schedules = new ArrayList<TeacherWeeklyplan>();
		daily_schedules = new ArrayList<TeacherWeeklyplan>();
		days = new ArrayList<String>();
		days_type_id= new ArrayList<String>();
		type_selection = new ArrayList<String>();
		days_type_id = new ArrayList<String>();
		type_selection_id = new ArrayList<String>();
		if (SchoolAppUtils.isOnline(WeeklyplanActivity.this)) {
			internetAvailable = true;
			initialize();
		} else {
			new offlineLessonAsync().execute();
		}
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
								WeeklyplanActivity.this, selectedUri);
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
	protected void onResume() {
		super.onResume();
		if (SchoolAppUtils.GetSharedBoolParameter(
				WeeklyplanActivity.this, Constants.UPDATE)) {
			SchoolAppUtils.SetSharedBoolParameter(
					WeeklyplanActivity.this, Constants.UPDATE, false);
			// getData();
		}
	}

	private void initialize() {

		if (getIntent().getExtras() != null) {
			String temp = getIntent().getExtras().getString(
					Constants.TSCHEDULEID);
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
		}
		getData();
	}

	private void getData() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getApplicationContext(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getApplicationContext(),
						Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("user_id", SchoolAppUtils
						.GetSharedParameter(getApplicationContext(),
								Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("section_id",
				subject.section_id));
		nameValuePairs.add(new BasicNameValuePair("subject_id", subject.id));
		Log.d("PARAMS OF LESSON PLAN", "" + nameValuePairs);
		new GetScheduleAsyntask().execute(nameValuePairs);
	}

	private class GetScheduleAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(WeeklyplanActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
			// mDatabase.open();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			schedules.clear();

			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_get_week_plan);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
						System.out.println("Size..." + array.length());
						for (int i = 0; i < array.length(); i++) {
							TeacherWeeklyplan assignment = new TeacherWeeklyplan(
									array.getJSONObject(i));
							schedules.add(assignment);
							Log.d("Insert in SCH: ", "Inserting ..");
							// mDatabase.addLesson(assignment);
							Log.d("STORAGE IN DB", "" + assignment);
							/*
							 * if (schedules.get(i).week_plan_id
							 * .equalsIgnoreCase("Yes")) { JSONObject jBlog =
							 * null; jBlog = array.getJSONObject(i); JSONArray
							 * child_info_arry = null;
							 * 
							 * child_info_arry = jBlog
							 * .getJSONArray("daily_plan");
							 * System.out.println("child_info_arry.length" +
							 * child_info_arry.length());
							 * 
							 * for (int j = 0; j < child_info_arry.length();
							 * j++) { TeacherWeeklyplan assignment_new = new
							 * TeacherWeeklyplan(
							 * child_info_arry.getJSONObject(j));
							 * daily_schedules.add(assignment_new); //
							 * mDatabase.addLessonDaily(assignment_new);
							 * Log.d("Insert in DAILY SCH: ", "" +
							 * assignment_new);
							 * 
							 * } }
							 */
						}
						System.out.println("Size..." + schedules.size());

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
				e.printStackTrace();
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
				int position = 0;
				
				if (schedules != null) {
					Log.e("Current Date...", SchoolAppUtils.getCurrentDate());

					for (TeacherWeeklyplan schdule : schedules) {
						if (!type_selection.contains(schdule.type_name)) {
							type_selection.add(schdule.type_name);
							type_selection_id.add(schdule.type_id);
						}
					}
					for (TeacherWeeklyplan schdule : schedules) {
						days.add(schdule.week_start_date);
						days_type_id.add(schdule.week_id);
					}

					ArrayAdapter<String> dataAdapter_type = new ArrayAdapter<String>(
							WeeklyplanActivity.this,
							R.layout.simple_spinner_item_custom_weekly_plan,
							type_selection);
					dataAdapter_type
							.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
					dataAdapter_type.notifyDataSetChanged();
					type.setAdapter(dataAdapter_type);
					type.setSelection(position);
					type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, final int arg2, long arg3) {
							days.clear();
							days_type_id.clear();
							
							for (int i = 0; i < schedules.size(); i++) {
								if (schedules.get(i).type_name
										.equalsIgnoreCase(type_selection
												.get(arg2))) {
									days.add(schedules.get(i).week_start_date);
									days_type_id.add(schedules.get(i).week_id);
								}

							}
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
									WeeklyplanActivity.this,
									R.layout.simple_spinner_item_custom_weekly_plan,
									days);
							dataAdapter
									.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
							dataAdapter.notifyDataSetChanged();
							date.setAdapter(dataAdapter);
							selectedtypeId = type_selection_id.get(arg2).toString();
							selected_id =  days_type_id.get(0);
							if (service == true) {
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										3);
								nameValuePairs.add(new BasicNameValuePair(
										"user_type_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.USERTYPEID)));
								nameValuePairs.add(new BasicNameValuePair(
										"organization_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.UORGANIZATIONID)));
								nameValuePairs.add(new BasicNameValuePair(
										"user_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.USERID)));
								nameValuePairs.add(new BasicNameValuePair(
										"section_id", subject.section_id));
								nameValuePairs.add(new BasicNameValuePair(
										"sr_id", subject.id));
								/*if (selected_id.equalsIgnoreCase(""))
									nameValuePairs.add(new BasicNameValuePair(
											"week_id", schedules.get(0).week_id));
								else*/
									nameValuePairs.add(new BasicNameValuePair(
											"week_id", selected_id));
								nameValuePairs.add(new BasicNameValuePair(
										"type_id", type_selection_id.get(arg2).toString()));
								Log.d("PARAMS OF LESSON PLAN", ""
										+ nameValuePairs);
								new GetWeekPlanAsyntask()
										.execute(nameValuePairs);
								service = false;
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

					
					date.setSelection(position);
					date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, final int arg2, long arg3) {
							pos_date = arg2;
							System.out.println("Size-------"+days_type_id.size()+"----"+days_type_id.get(arg2));
							selected_id =  days_type_id.get(arg2);
							if (service == true) {
								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										3);
								nameValuePairs.add(new BasicNameValuePair(
										"user_type_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.USERTYPEID)));
								nameValuePairs.add(new BasicNameValuePair(
										"organization_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.UORGANIZATIONID)));
								nameValuePairs.add(new BasicNameValuePair(
										"user_id",
										SchoolAppUtils.GetSharedParameter(
												getApplicationContext(),
												Constants.USERID)));
								nameValuePairs.add(new BasicNameValuePair(
										"section_id", subject.section_id));
								nameValuePairs.add(new BasicNameValuePair(
										"sr_id", subject.id));
								nameValuePairs.add(new BasicNameValuePair(
										"week_id", selected_id));
								
								nameValuePairs.add(new BasicNameValuePair(
										"type_id", selectedtypeId));

								Log.d("PARAMS OF LESSON PLAN", ""
										+ nameValuePairs);
								new GetWeekPlanAsyntask()
										.execute(nameValuePairs);
								service = false;
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

				}
			}

			else {

				if (error.length() > 0) {
					SchoolAppUtils.showDialog(WeeklyplanActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(WeeklyplanActivity.this,
							page_title, getResources()
									.getString(R.string.error));
				}
			}

		}

	}

	private class GetWeekPlanAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(WeeklyplanActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
			// mDatabase.open();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			daily_schedules.clear();
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_get_week_plan_list);
			String parameters = "";
			for (NameValuePair namevp : nvp) {
				parameters += namevp.getName() + "=" + namevp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
						System.out.println("Size..." + array.length());
						for (int i = 0; i < array.length(); i++) {
							TeacherWeeklyplan assignment = new TeacherWeeklyplan(
									array.getJSONObject(i));
							daily_schedules.add(assignment);
							Log.d("Insert in SCH: ", "Inserting ..");
							// mDatabase.addLesson(assignment);
							Log.d("STORAGE IN DB", "" + assignment);

						}
						System.out.println("Size..." + schedules.size());

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
				e.printStackTrace();
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
				int position = 0;
				if (schedules != null) {
					service = true;
					View rowView;
					ViewHolder viewHolder;
					long difference = 0;

					try {
						linearLayout.removeAllViews();
					} catch (Exception ex) {
					}

					for (int i = 0; i < daily_schedules.size(); i++) {
						System.out.println("Postion.....i..." + i);
						System.out.println("Postion.....i..."
								+ daily_schedules.get(i).date);

						rowView = inflater.inflate(
								R.layout.subject_schedule_each_row, null);
						viewHolder = new ViewHolder();
						viewHolder.topic = (TextView) rowView
								.findViewById(R.id.textView_topic);
						viewHolder.date = (TextView) rowView
								.findViewById(R.id.textView_date);
						viewHolder.edit = (ImageButton) rowView
								.findViewById(R.id.button_edit);

						viewHolder.topic.setText("Topic: "
								+ daily_schedules.get(i).topic);
						viewHolder.date.setText("Date: "
								+ SchoolAppUtils.getDateFormat(daily_schedules
										.get(i).weekly_date));

						viewHolder.edit.setTag(new ScheduleTag(daily_schedules
								.get(i).id, daily_schedules.get(i).topic,
								daily_schedules.get(i).date, difference));
						if ((SchoolAppUtils.GetSharedParameter(
								WeeklyplanActivity.this,
								Constants.LESSONS_ADD_PERMISSION))
								.equalsIgnoreCase("0")) {
							viewHolder.edit.setVisibility(View.INVISIBLE);
						}
						viewHolder.edit
								.setOnClickListener(WeeklyplanActivity.this);
						viewHolder.getdoc = (ImageButton) rowView
								.findViewById(R.id.download_btn);
						viewHolder.getdoc.setVisibility(View.GONE);
						final int att_pos = i;
						if (daily_schedules.get(i).attachment.length() > 0) {
							file_id = daily_schedules.get(i).daily_id;
							viewHolder.getdoc.setVisibility(View.VISIBLE);
							viewHolder.getdoc
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {

											/*
											 * Intent intent = new Intent(
											 * Intent.ACTION_VIEW);
											 * intent.setData(Uri
											 * .parse(Urls.base_url +
											 * Urls.api_get_weeklyplan_doc + "/"
											 * +
											 * daily_schedules.get(att_pos).id))
											 * ; startActivity(intent);
											 */

											new AlertDialog.Builder(
													WeeklyplanActivity.this)
													.setTitle("Select option")
													.setPositiveButton(
															"View Document",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	// continue
																	// with view
																	SchoolAppUtils
																			.viewDocument(
																					WeeklyplanActivity.this,
																					Urls.base_url
																							+ Urls.api_get_weeklyplan_doc
																							+ "/"
																							+ daily_schedules
																									.get(att_pos).id);
																}
															})
													.setNegativeButton(
															"Download",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	// download

																	final DownloadTask downloadTask = new DownloadTask(
																			WeeklyplanActivity.this,
																			daily_schedules
																					.get(att_pos).attachment);
																	downloadTask
																			.execute(Urls.base_url
																					+ Urls.api_get_weeklyplan_doc
																					+ "/"
																					+ daily_schedules
																							.get(att_pos).id);

																	/*
																	 * Intent i
																	 * = new
																	 * Intent(
																	 * Intent
																	 * .ACTION_VIEW
																	 * );
																	 * i.setData
																	 * (Uri
																	 * .parse
																	 * (Urls
																	 * .api_get_doc
																	 * +
																	 * test.id)
																	 * );
																	 * startActivity
																	 * (i);
																	 */

																}
															})
													.setIcon(
															android.R.drawable.ic_dialog_info)
													.show();

										}
									});
						} else {
							viewHolder.getdoc.setVisibility(View.GONE);
						}
						linearLayout.addView(rowView);

					}

				}
			}

			else {

				if (error.length() > 0) {
					SchoolAppUtils.showDialog(WeeklyplanActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(WeeklyplanActivity.this,
							page_title, getResources()
									.getString(R.string.error));
				}
			}

		}

	}

	@Override
	public void onBackPressed() {
		selectedPosition = 0;
		super.onBackPressed();
	}

	class ViewHolder {
		TextView topic, date;
		ImageButton edit, getdoc;
	}

	class ScheduleTag {
		String id, topic, date;
		long difference;

		public ScheduleTag(String id, String topic, String date, long difference) {
			// TODO Auto-generated constructor stub
			this.id = id;
			this.topic = topic;
			this.date = date;
			this.difference = difference;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_edit:
			final ScheduleTag tag = (ScheduleTag) v.getTag();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					WeeklyplanActivity.this);

			// set title
			// alertDialogBuilder.setTitle("Edit Topic!");

			// set dialog message
			alertDialogBuilder.setCancelable(false);

			View view = inflater.inflate(R.layout.late_remarks_dialog, null);
			Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
			Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
			select_file = (Button) view
					.findViewById(R.id.select_file_for_upload);
			edTxt = (EditText) view.findViewById(R.id.dialog_txt_remarks);
			edTxt.setText(tag.topic);

			alertDialogBuilder.setView(view);

			// create alert dialog
			final AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

			select_file.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showFileChooser();
				}
			});
			btnSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					id_for_daily_schedule = tag.id;
					if (edTxt.getText().toString().length() <= 0) {
						Toast.makeText(WeeklyplanActivity.this,
								"Enter Description", Toast.LENGTH_SHORT).show();
						return;
					}
					descp = edTxt.getText().toString();
					if (!SchoolAppUtils
							.isOnline(WeeklyplanActivity.this)) {
						new offlineFileAsync().execute();
					} else if (select_file.getText().toString()
							.equalsIgnoreCase(getString(R.string.attachment))) {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								3);
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
						nameValuePairs.add(new BasicNameValuePair("topic",
								descp));
						nameValuePairs.add(new BasicNameValuePair("edit_id",
								tag.id));
						nameValuePairs.add(new BasicNameValuePair("file_name",
								""));
						nameValuePairs.add(new BasicNameValuePair("orig_name",
								""));
						new UploadAsyntask().execute(nameValuePairs);
					} else {
						new UploadFileAsyntask().execute();
					}
				}
			});

			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.cancel();
				}
			});

			break;
		}
	}

	class offlineLessonAsync extends
			AsyncTask<String, TeacherWeeklyplan, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			schedules.clear();
			daily_schedules.clear();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			long row_id = 0;
			final String subject_id = getIntent().getExtras().getString(
					Constants.OFFLINE_SUBJECT_ID);
			final String section_id = getIntent().getExtras().getString(
					Constants.OFFLINE_SECTION_ID);
			// mDatabase.open();
			/*
			 * schedules = mDatabase.getAllLessonPlan(subject_id, section_id);
			 * daily_schedules = mDatabase.getAllDailyLessonPlan(subject_id,
			 * section_id); System.out.println("Size..." + schedules.size());
			 * System.out.println("Size..." + daily_schedules.size());
			 */
			// mDatabase.close();
			return (row_id > 0);
		}

		@Override
		protected void onProgressUpdate(TeacherWeeklyplan... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			int position = 0;
			if (schedules != null) {
				for (TeacherWeeklyplan schdule : schedules) {

					days.add(schdule.week_start_date);
				}
				/*
				 * for (int i = 0; i < schedules.size(); i++) {
				 * 
				 * try {
				 * 
				 * SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
				 * try { date1 = sdf.parse(schedules.get(i).start_date
				 * .toString()); } catch (java.text.ParseException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 * 
				 * try { date2 = sdf.parse(schedules.get(i).end_date
				 * .toString()); } catch (java.text.ParseException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); } try { date3
				 * = sdf.parse(SchoolAppUtils.getCurrentDate()); } catch
				 * (java.text.ParseException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); } System.out.println("last" +
				 * sdf.format(date1)); System.out.println("last" +
				 * sdf.format(date2)); System.out.println("last" +
				 * sdf.format(date3));
				 * 
				 * if (((date1.before(date3)) || (date1.equals(date3))) &&
				 * ((date2.after(date3)) || (date2 .equals(date3)))) { position
				 * = i; } } catch (ParseException ex) { ex.printStackTrace(); }
				 * }
				 */
				// /Log.e("Current Date...", SchoolAppUtils.getCurrentDate());
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						WeeklyplanActivity.this,
						R.layout.simple_spinner_item_custom_new, days);
				dataAdapter
						.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
				date.setAdapter(dataAdapter);
				date.setSelection(position);
				date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {

						View rowView;
						ViewHolder viewHolder;
						long difference = 0;

						try {
							linearLayout.removeAllViews();
						} catch (Exception ex) {
						}

						if (schedules.get(arg2).topic.length() > 0)
							topicString = "Topic: " + schedules.get(arg2).topic;
						text.setText(topicString);
						System.out.println("Postion....." + arg2);
						for (int i = daily_schedules.size() / schedules.size()
								* arg2; i < daily_schedules.size()
								/ schedules.size() * (arg2 + 1); i++) {
							System.out.println("Postion.....i..." + i);
							System.out.println("Postion.....i..."
									+ daily_schedules.get(i).date);

							rowView = inflater.inflate(
									R.layout.subject_schedule_each_row, null);
							viewHolder = new ViewHolder();
							viewHolder.topic = (TextView) rowView
									.findViewById(R.id.textView_topic);
							viewHolder.date = (TextView) rowView
									.findViewById(R.id.textView_date);
							viewHolder.edit = (ImageButton) rowView
									.findViewById(R.id.button_edit);

							viewHolder.topic.setText("Topic: "
									+ daily_schedules.get(i).topic);
							viewHolder.date.setText("Date: "
									+ SchoolAppUtils
											.getDateFormat(daily_schedules
													.get(i).date));

							viewHolder.edit.setTag(new ScheduleTag(
									daily_schedules.get(i).id, daily_schedules
											.get(i).topic, daily_schedules
											.get(i).date, difference));
							if ((SchoolAppUtils.GetSharedParameter(
									WeeklyplanActivity.this,
									Constants.LESSONS_ADD_PERMISSION))
									.equalsIgnoreCase("0")) {
								viewHolder.edit.setVisibility(View.INVISIBLE);
							}
							viewHolder.edit
									.setOnClickListener(WeeklyplanActivity.this);
							viewHolder.getdoc = (ImageButton) rowView
									.findViewById(R.id.download_btn);
							viewHolder.getdoc.setVisibility(View.GONE);
							if (daily_schedules.get(i).file.length() > 0) {
								file_id = daily_schedules.get(i).daily_id;
								viewHolder.getdoc.setVisibility(View.VISIBLE);
								viewHolder.getdoc
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {

												Intent intent = new Intent(
														Intent.ACTION_VIEW);
												intent.setData(Uri
														.parse(Urls.base_url
																+ Urls.api_get_schedule_doc
																+ "2/"
																+ file_id));
												startActivity(intent);
											}
										});
							} else {
								viewHolder.getdoc.setVisibility(View.GONE);
							}
							linearLayout.addView(rowView);

						}

						topic = schedules.get(arg2).topic;
						selectedId = schedules.get(arg2).daily_id;
						selectedPosition = arg2;
						if (schedules.get(arg2).file.length() > 0) {
							download.setVisibility(View.VISIBLE);
						} else {
							download.setVisibility(View.INVISIBLE);
						}
						download.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(Urls.base_url
										+ Urls.api_get_schedule_doc + "1/"
										+ schedules.get(arg2).daily_id));
								startActivity(i);
							}
						});
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

			}

		}
	}

	private class UploadFileAsyntask extends AsyncTask<Void, Void, Void> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(WeeklyplanActivity.this);
			dialog.setTitle("Updating Daily " + page_title);
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

				String uuploadfile_name = null;
				String uuuploadfile_name;

				final String[] tokens = select_file.getText().toString()
						.split("/");
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
						3);
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
				nameValuePairs.add(new BasicNameValuePair("topic", descp));
				nameValuePairs.add(new BasicNameValuePair("edit_id",
						id_for_daily_schedule));
				Log.d("ID IN ONLN", id_for_daily_schedule);
				nameValuePairs.add(new BasicNameValuePair("file_name",
						encrypted_file_name));
				nameValuePairs.add(new BasicNameValuePair("orig_name",
						uuploadfile_name));
				Log.d("PARAMS FILE", "" + nameValuePairs);
				new UploadAsyntask().execute(nameValuePairs);

			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			uploadFile(select_file.getText().toString());
			return null;
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
				URL url = new URL(Urls.base_url
						+ Urls.upLoadServerUriweeklyplan);
				Log.d("test", "test");
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
				Log.d("test", "test");
				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);
				Log.d("test", "test");
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

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

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
						Toast.makeText(WeeklyplanActivity.this,
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
						Toast.makeText(WeeklyplanActivity.this,
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
			AsyncTask<String, TeacherWeeklyplan, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			long row_id = 0;

			return (row_id > 0);
		}

		@Override
		protected void onProgressUpdate(TeacherWeeklyplan... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);

			if (result) {
				dialoggg = new AlertDialog.Builder(
						WeeklyplanActivity.this);
				dialoggg.setTitle("Daily " + page_title);
				dialoggg.setMessage("Daily " + page_title
						+ " updated in Offline Mode");
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								SchoolAppUtils.SetSharedBoolParameter(
										WeeklyplanActivity.this,
										"update_list", true);
								WeeklyplanActivity.this.finish();
								Intent intent = new Intent(
										WeeklyplanActivity.this,
										TeacherMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				SchoolAppUtils.showDialog(WeeklyplanActivity.this,
						page_title, "Database Error");

			}
		}
	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(WeeklyplanActivity.this);
			dialog.setTitle("Updating Daily " + page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> url = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					Urls.api_weekly_plan_save);
			String parameters = "";
			for (NameValuePair namevp : url) {
				parameters += namevp.getName() + "=" + namevp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

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
						WeeklyplanActivity.this);
				dialoggg.setTitle("Daily " + page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								SchoolAppUtils.SetSharedBoolParameter(
										WeeklyplanActivity.this,
										"update_list", true);
								WeeklyplanActivity.this.finish();
								Intent intent = new Intent(
										WeeklyplanActivity.this,
										TeacherMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

								startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(WeeklyplanActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(WeeklyplanActivity.this,
							page_title, getResources()
									.getString(R.string.error));
				}
			}
		}

	}

}
