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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
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
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DataStructureFramwork.TeacherSchedule;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.FileUtils;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherScheduleActivity extends Activity implements
		OnClickListener {
	private ArrayList<TeacherSchedule> schedules;
	private ArrayList<TeacherSchedule> daily_schedules;
	private Button edit;
	private ImageButton download;
	Button select_file;
	private Spinner date;
	private TextView header, text;
	private List<String> days;
	private String id, selectedId, topic, filename_weekly;
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
	private TextView textEmpty;
	private boolean internetAvailable = false;
	public DatabaseAdapter mDatabase;
	String path = "null";
	Date date1, date2, date3 = null;
	String topicString = "";
	String file_id = "";
	private String page_title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mDatabase = ((SchoolApplication) getApplication()).getDatabase();
		setContentView(R.layout.activity_teacher_schedule);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(TeacherScheduleActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		download = (ImageButton) findViewById(R.id.download_btn_weekly);
		download.setVisibility(View.GONE);
		linearLayout = (LinearLayout) findViewById(R.id.layout_schedule_row);
		header = (TextView) findViewById(R.id.header_text);
		text = (TextView) findViewById(R.id.schedule_txt);
		date = (Spinner) findViewById(R.id.date_btns);
		header.setText(page_title);
		edit = (Button) findViewById(R.id.edt_btn);
		if ((SchoolAppUtils.GetSharedParameter(TeacherScheduleActivity.this,
				Constants.LESSONS_ADD_PERMISSION)).equalsIgnoreCase("0")) {
			edit.setVisibility(View.INVISIBLE);
		}
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(TeacherScheduleActivity.this,
						TeacherScheduleEditActivity.class);
				intent.putExtra(Constants.TSCHEDULE, topic);
				intent.putExtra(Constants.TSCHEDULEID, selectedId);
				intent.putExtra(Constants.PAGE_TITLE, page_title);
				startActivity(intent);
			}
		});
		schedules = new ArrayList<TeacherSchedule>();
		daily_schedules = new ArrayList<TeacherSchedule>();
		days = new ArrayList<String>();
		if (SchoolAppUtils.isOnline(TeacherScheduleActivity.this)) {
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
								TeacherScheduleActivity.this, selectedUri);
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
		if (SchoolAppUtils.GetSharedBoolParameter(TeacherScheduleActivity.this,
				Constants.UPDATE)) {
			SchoolAppUtils.SetSharedBoolParameter(TeacherScheduleActivity.this,
					Constants.UPDATE, false);
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
			dialog = new ProgressDialog(TeacherScheduleActivity.this);
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
			mDatabase.open();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			schedules.clear();
			daily_schedules.clear();
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_get_week_lesson);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array = json.getJSONArray("data");
						System.out.println("Size..." + array.length());
						for (int i = 0; i < array.length(); i++) {
							TeacherSchedule assignment = new TeacherSchedule(
									array.getJSONObject(i));
							schedules.add(assignment);
							Log.d("Insert in SCH: ", "Inserting ..");
							mDatabase.addLesson(assignment);
							Log.d("STORAGE IN DB", "" + assignment);

							JSONObject jBlog = null;
							jBlog = array.getJSONObject(i);
							JSONArray child_info_arry = null;
							child_info_arry = jBlog
									.getJSONArray("daily_schedule");
							System.out.println("child_info_arry.length"
									+ child_info_arry.length());
							for (int j = 0; j < child_info_arry.length(); j++) {
								TeacherSchedule assignment_new = new TeacherSchedule(
										child_info_arry.getJSONObject(j));
								daily_schedules.add(assignment_new);
								mDatabase.addLessonDaily(assignment_new);
								Log.d("Insert in DAILY SCH: ", ""
										+ assignment_new);

							}

						}
						System.out.println("Size..." + schedules.size());
						System.out.println("Size..." + daily_schedules.size());
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
					mDatabase.close();

					Log.e("Current Date...", SchoolAppUtils.getCurrentDate());

					for (TeacherSchedule schdule : schedules) {

						days.add(schdule.start_date.replace("-", "/") + " - "
								+ schdule.end_date.replace("-", "/"));
					}
					for (int i = 0; i < schedules.size(); i++) {

						try {

							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							try {
								date1 = sdf.parse(schedules.get(i).start_date
										.toString());
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								date2 = sdf.parse(schedules.get(i).end_date
										.toString());
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								date3 = sdf.parse(SchoolAppUtils
										.getCurrentDate());
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("last" + sdf.format(date1));
							System.out.println("last" + sdf.format(date2));
							System.out.println("last" + sdf.format(date3));

							if (((date1.before(date3)) || (date1.equals(date3)))
									&& ((date2.after(date3)) || (date2
											.equals(date3)))) {
								position = i;
							}

						} catch (ParseException ex) {
							ex.printStackTrace();
						}
					}
					// mDatabase.close();
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
							TeacherScheduleActivity.this,
							R.layout.simple_spinner_item_custom_new, days);
					dataAdapter
							.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
					date.setAdapter(dataAdapter);
					date.setSelection(position);
					date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, final int arg2, long arg3) {

							View rowView;
							ViewHolder viewHolder;
							long difference = 0;

							try {
								linearLayout.removeAllViews();
							} catch (Exception ex) {
							}

							if (schedules.get(arg2).topic.length() > 0)
								topicString = "Topic: "
										+ schedules.get(arg2).topic;
							text.setText(topicString);
							System.out.println("Postion....." + arg2);
							for (int i = daily_schedules.size()
									/ schedules.size() * arg2; i < daily_schedules
									.size() / schedules.size() * (arg2 + 1); i++) {
								rowView = inflater.inflate(
										R.layout.subject_schedule_each_row,
										null);
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
										daily_schedules.get(i).id,
										daily_schedules.get(i).topic,
										daily_schedules.get(i).date, difference));
								if ((SchoolAppUtils.GetSharedParameter(
										TeacherScheduleActivity.this,
										Constants.LESSONS_ADD_PERMISSION))
										.equalsIgnoreCase("0")) {
									viewHolder.edit
											.setVisibility(View.INVISIBLE);
								}
								viewHolder.edit
										.setOnClickListener(TeacherScheduleActivity.this);
								viewHolder.getdoc = (ImageButton) rowView
										.findViewById(R.id.download_btn);
								if (daily_schedules.get(i).doc.length() > 0) {
									final String file_id = daily_schedules
											.get(i).id;
									final String file_name = daily_schedules
											.get(i).doc;
									viewHolder.getdoc
											.setVisibility(View.VISIBLE);
									viewHolder.getdoc
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {

													new AlertDialog.Builder(
															TeacherScheduleActivity.this)
															.setTitle(
																	"Select option")
															.setPositiveButton(
																	"View Document",
																	new DialogInterface.OnClickListener() {
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			// continue
																			// with
																			// view
																			SchoolAppUtils
																					.viewDocument(
																							TeacherScheduleActivity.this,
																							Urls.api_get_schedule_doc
																									+ "2/"
																									+ file_id);
																		}
																	})
															.setNegativeButton(
																	"Download",
																	new DialogInterface.OnClickListener() {
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			// download
																			final DownloadTask downloadTask = new DownloadTask(TeacherScheduleActivity.this, file_name);
																			downloadTask.execute(Urls.api_get_schedule_doc
																					+ "2/"
																					+ file_id);
																			
																			

																			/*Intent i = new Intent(
																					Intent.ACTION_VIEW);
																			i.setData(Uri
																					.parse(Urls.api_get_schedule_doc
																							+ "2/"
																							+ file_id));
																			startActivity(i);*/

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
							// mDatabase.close();
							topic = schedules.get(arg2).topic;
							selectedId = schedules.get(arg2).id;
							selectedPosition = arg2;
							if (schedules.get(arg2).doc.length() > 0) {
								download.setVisibility(View.VISIBLE);
							} else {
								download.setVisibility(View.INVISIBLE);
							}
							download.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {

									new AlertDialog.Builder(
											TeacherScheduleActivity.this)
											.setTitle("Select option")
											.setPositiveButton(
													"View Document",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															// continue with
															// view
															SchoolAppUtils
																	.viewDocument(
																			TeacherScheduleActivity.this,
																			Urls.api_get_schedule_doc
																					+ "1/"
																					+ schedules
																							.get(arg2).id);
														}
													})
											.setNegativeButton(
													"Download",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															// download
															
															final DownloadTask downloadTask = new DownloadTask(TeacherScheduleActivity.this, schedules
																	.get(arg2).doc);
															downloadTask.execute(Urls.api_get_schedule_doc
																	+ "1/"
																	+ schedules
																			.get(arg2).id);
															
															

															/*Intent i = new Intent(
																	Intent.ACTION_VIEW);
															i.setData(Uri.parse(Urls.api_get_schedule_doc
																	+ "1/"
																	+ schedules
																			.get(arg2).id));
															startActivity(i);*/

														}
													})
											.setIcon(
													android.R.drawable.ic_dialog_info)
											.show();

								}
							});
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

				}
			}

			else {

				if (error.length() > 0) {
					SchoolAppUtils.showDialog(TeacherScheduleActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(TeacherScheduleActivity.this,
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
					TeacherScheduleActivity.this);

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
						Toast.makeText(TeacherScheduleActivity.this,
								"Enter Description", Toast.LENGTH_SHORT).show();
						return;
					}
					descp = edTxt.getText().toString();
					if (!SchoolAppUtils.isOnline(TeacherScheduleActivity.this)) {
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
						nameValuePairs.add(new BasicNameValuePair("type", "2"));
						nameValuePairs
								.add(new BasicNameValuePair("id", tag.id));
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
			AsyncTask<String, TeacherSchedule, Boolean> {
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
			mDatabase.open();
			schedules = mDatabase.getAllLessonPlan(subject_id, section_id);
			daily_schedules = mDatabase.getAllDailyLessonPlan(subject_id,
					section_id);
			System.out.println("Size..." + schedules.size());
			System.out.println("Size..." + daily_schedules.size());
			mDatabase.close();
			return (row_id > 0);
		}

		@Override
		protected void onProgressUpdate(TeacherSchedule... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			int position = 0;
			if (schedules != null) {
				for (TeacherSchedule schdule : schedules) {

					days.add(schdule.start_date.replace("-", "/") + " - "
							+ schdule.end_date.replace("-", "/"));
				}
				for (int i = 0; i < schedules.size(); i++) {

					try {

						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						try {
							date1 = sdf.parse(schedules.get(i).start_date
									.toString());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							date2 = sdf.parse(schedules.get(i).end_date
									.toString());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							date3 = sdf.parse(SchoolAppUtils.getCurrentDate());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("last" + sdf.format(date1));
						System.out.println("last" + sdf.format(date2));
						System.out.println("last" + sdf.format(date3));

						if (((date1.before(date3)) || (date1.equals(date3)))
								&& ((date2.after(date3)) || (date2
										.equals(date3)))) {
							position = i;
						}
					} catch (ParseException ex) {
						ex.printStackTrace();
					}
				}
				// /Log.e("Current Date...", SchoolAppUtils.getCurrentDate());
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						TeacherScheduleActivity.this,
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
									TeacherScheduleActivity.this,
									Constants.LESSONS_ADD_PERMISSION))
									.equalsIgnoreCase("0")) {
								viewHolder.edit.setVisibility(View.INVISIBLE);
							}
							viewHolder.edit
									.setOnClickListener(TeacherScheduleActivity.this);
							viewHolder.getdoc = (ImageButton) rowView
									.findViewById(R.id.download_btn);
							viewHolder.getdoc.setVisibility(View.GONE);
							if (daily_schedules.get(i).doc.length() > 0) {
								file_id = daily_schedules.get(i).id;
								final String file_name = daily_schedules.get(i).doc;
								viewHolder.getdoc.setVisibility(View.VISIBLE);
								viewHolder.getdoc
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												
												final DownloadTask downloadTask = new DownloadTask(TeacherScheduleActivity.this, file_name);
												downloadTask.execute(Urls.base_url
														+ Urls.api_get_schedule_doc
														+ "2/"
														+ file_id);
												
												
												

												/*Intent intent = new Intent(
														Intent.ACTION_VIEW);
												intent.setData(Uri
														.parse(Urls.base_url
																+ Urls.api_get_schedule_doc
																+ "2/"
																+ file_id));
												startActivity(intent);*/
											}
										});
							} else {
								viewHolder.getdoc.setVisibility(View.GONE);
							}
							linearLayout.addView(rowView);

						}

						topic = schedules.get(arg2).topic;
						selectedId = schedules.get(arg2).id;
						selectedPosition = arg2;
						if (schedules.get(arg2).doc.length() > 0) {
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
										+ schedules.get(arg2).id));
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
			dialog = new ProgressDialog(TeacherScheduleActivity.this);
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
				/*
				 * dialog =
				 * ProgressDialog.show(TeacherAssignmentAddActivity.this, "",
				 * "Uploading file...", true);
				 */
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
				nameValuePairs.add(new BasicNameValuePair("type", "2"));
				nameValuePairs.add(new BasicNameValuePair("id",
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
				URL url = new URL(Urls.base_url + Urls.upLoadServerUri);
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
						Toast.makeText(TeacherScheduleActivity.this,
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
						Toast.makeText(TeacherScheduleActivity.this,
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

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			long row_id = 0;
			mDatabase.open();
			TeacherSchedule schedule = new TeacherSchedule();
			schedule.topic = descp;
			schedule.id = id_for_daily_schedule;
			schedule.doc = path;
			schedule.file = "null";
			row_id = mDatabase.addLessonUpload(schedule);
			mDatabase.close();
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
				dialoggg = new AlertDialog.Builder(TeacherScheduleActivity.this);
				dialoggg.setTitle("Daily " + page_title);
				dialoggg.setMessage("Daily " + page_title
						+ " updated in Offline Mode");
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								SchoolAppUtils.SetSharedBoolParameter(
										TeacherScheduleActivity.this,
										"update_list", true);
								TeacherScheduleActivity.this.finish();
								Intent intent = new Intent(
										TeacherScheduleActivity.this,
										TeacherMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				SchoolAppUtils.showDialog(TeacherScheduleActivity.this,
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
			dialog = new ProgressDialog(TeacherScheduleActivity.this);
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
					Urls.api_schedule_save);
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
				dialoggg = new AlertDialog.Builder(TeacherScheduleActivity.this);
				dialoggg.setTitle("Daily " + page_title);
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								SchoolAppUtils.SetSharedBoolParameter(
										TeacherScheduleActivity.this,
										"update_list", true);
								TeacherScheduleActivity.this.finish();
								Intent intent = new Intent(
										TeacherScheduleActivity.this,
										TeacherMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

								startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(TeacherScheduleActivity.this,
							page_title, error);
				} else {
					SchoolAppUtils.showDialog(TeacherScheduleActivity.this,
							page_title, getResources()
									.getString(R.string.error));
				}
			}
		}

	}

}
