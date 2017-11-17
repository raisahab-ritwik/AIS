package com.knwedu.college;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.adapter.CollegeSubjectScheduleAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TeacherSchedule;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeSubjectDetailActivityLessonPlan<SubjectScheduleAdapter>
		extends Activity {
	private ListView list;
	private CollegeSubjectScheduleAdapter adapter;
	private ArrayList<TeacherSchedule> schedules;
	private ArrayList<TeacherSchedule> daily_schedules;
	private TextView name, phone, subject_txt, email;
	private TextView header, text;
	private LayoutInflater inflater;
	private LinearLayout linearLayout;
	private ProgressDialog dialog;
	private Spinner date;
	private Subject info;
	private int pos_spinner;
	private Button directory;
	private String page_title = "";
	private static int selectedPosition;
	private List<String> days;
	private ImageView userImage;
	private ImageButton download;
	private String assign = "Dailyschedule";
	private String Url;
	String file_id;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_subject_detail);
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

	private void initialize() {
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		((TextView) findViewById(R.id.header_text)).setText("Lesson Plan");
		list = (ListView) findViewById(R.id.listview);
		date = (Spinner) findViewById(R.id.date_btns);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		text = (TextView) findViewById(R.id.schedule_txt);
		linearLayout = (LinearLayout) findViewById(R.id.layout_schedule_row);
		download = (ImageButton) findViewById(R.id.download_btn_weekly);
		download.setVisibility(View.GONE);
		/*
		 * userImage = (ImageView) findViewById(R.id.image_vieww); name =
		 * (TextView) findViewById(R.id.teacher_name_txt); email = (TextView)
		 * findViewById(R.id.email_txt); phone = (TextView)
		 * findViewById(R.id.phone_txt); subject_txt = (TextView)
		 * findViewById(R.id.subject_txt); options = new
		 * DisplayImageOptions.Builder()
		 * .showImageForEmptyUri(R.drawable.no_photo)
		 * .showImageOnFail(R.drawable.no_photo)
		 * .showStubImage(R.drawable.no_photo) .cacheInMemory() .cacheOnDisc()
		 * .build();
		 */
		if (getIntent().getExtras() != null) {
			String temp = getIntent().getExtras().getString("StudentSubject");
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object != null) {
					info = new Subject(object);
				}
			}
		}
		if (info != null) {
			initializeData();
		}
		/*
		 * ((Button)findViewById(R.id.class_fellow_btn)).setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(SubjectDetailActivityLessonPlan.this,
		 * ClassFellowListActivity.class); intent.putExtra("Section_id",
		 * info.section_id); startActivity(intent); //startActivity(new
		 * Intent(SubjectDetailActivity.this, ClassFellowListActivity.class)); }
		 * });
		 */
	}

	private void initializeData() {
		/*
		 * if(info != null) { name.setText(info.fname + " " + info.lname);
		 * email.setText(info.email); phone.setText(info.phone);
		 * subject_txt.setText(info.sub_name); if(info.image != null) { new
		 * LoadImageAsyncTask(SubjectDetailActivity.this, userImage,
		 * Urls.image_url_userpic, info.image, true).execute(); } }
		 */if (schedules != null) {
			setListView();
		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs
					.add(new BasicNameValuePair(
							"id",
							CollegeAppUtils
									.GetSharedParameter(
											CollegeSubjectDetailActivityLessonPlan.this,
											"id")));
			nameValuePairs.add(new BasicNameValuePair("sr_id",
					info.subject_relation_id));

			new GetAssignmentAsyntask().execute(nameValuePairs);
		}
	}

	private void setListView() {
		if (schedules != null) {
			adapter = new CollegeSubjectScheduleAdapter(
					CollegeSubjectDetailActivityLessonPlan.this, schedules,
					daily_schedules);
			list.setAdapter(adapter);
		}
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

	private class GetAssignmentAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		int position;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(
					CollegeSubjectDetailActivityLessonPlan.this);

			dialog.setTitle(getResources().getString(R.string.subject_schedule));

			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			schedules = new ArrayList<TeacherSchedule>();
			daily_schedules = new ArrayList<TeacherSchedule>();
			List<NameValuePair> url = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					CollegeUrls.api_activity_get_Lesson);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONArray array;
						JSONArray array_daily;
						try {
							array = json.getJSONArray("data");

						} catch (Exception e) {
							return true;
						}
						;// //////////////////////////////////////////////////////

						for (int i = 0; i < array.length(); i++) {
							TeacherSchedule assignment = new TeacherSchedule(
									array.getJSONObject(i));
							schedules.add(assignment);

							JSONObject jBlog = null;
							jBlog = array.getJSONObject(i);
							JSONArray child_info_arry = null;
							child_info_arry = jBlog
									.getJSONArray("daily_schedule");

							for (int j = 0; j < child_info_arry.length(); j++) {
								TeacherSchedule assignment_new = new TeacherSchedule(
										child_info_arry.getJSONObject(j));
								// daily_schedules.add(assignment_new);
								daily_schedules.add(assignment_new);

							}

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
				// setListView();

				Log.e("Current Date...", CollegeAppUtils.getCurrentDate());
				days = new ArrayList<String>();

				for (TeacherSchedule schdule : schedules) {

					days.add(schdule.start_date.replace("-", "/") + " - "
							+ schdule.end_date.replace("-", "/"));
				}
				for (int i = 0; i < schedules.size(); i++) {

					try {

						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date date1 = null;
						try {
							date1 = sdf.parse(schedules.get(i).start_date
									.toString());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Date date2 = null;
						try {
							date2 = sdf.parse(schedules.get(i).end_date
									.toString());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Date date3 = null;
						try {
							date3 = sdf.parse(CollegeAppUtils.getCurrentDate());
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

							pos_spinner = i;
						}

					} catch (ParseException ex) {
						ex.printStackTrace();
					}

				}

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						CollegeSubjectDetailActivityLessonPlan.this,
						R.layout.simple_spinner_item_custom_new, days);
				dataAdapter
						.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
				date.setAdapter(dataAdapter);
				date.setSelection(pos_spinner);
				date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {

						String topicString = "";
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
									R.layout.subject_schedule_each_row_lesson,
									null);
							viewHolder = new ViewHolder();
							viewHolder.topic = (TextView) rowView
									.findViewById(R.id.textView_topic);
							viewHolder.date = (TextView) rowView
									.findViewById(R.id.textView_date);
							viewHolder.edit = (ImageButton) rowView
									.findViewById(R.id.button_edit);
							if (daily_schedules.get(i).topic
									.equalsIgnoreCase("null")) {
								viewHolder.topic.setText("Topic: ");
							} else {
								viewHolder.topic.setText("Topic: "
										+ daily_schedules.get(i).topic);

							}
							viewHolder.date.setText("Date: "
									+ CollegeAppUtils
											.getDateFormat(daily_schedules
													.get(i).date));

							viewHolder.getdoc = (ImageButton) rowView
									.findViewById(R.id.download_btn);
							if (!daily_schedules.get(i).doc
									.equalsIgnoreCase("")) {
								file_id = daily_schedules.get(i).id;
								viewHolder.getdoc.setVisibility(View.VISIBLE);
								viewHolder.getdoc
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												/*
												 * Intent intent = new Intent(
												 * Intent.ACTION_VIEW);
												 * intent.setData(Uri
												 * .parse(CollegeUrls
												 * .api_get_schedule_doc + "2/"
												 * + file_id));
												 * startActivity(intent);
												 */
												showDialog();
											}

										});
							} else {
								viewHolder.getdoc.setVisibility(View.GONE);
							}

							linearLayout.addView(rowView);

						}

						if (schedules.get(arg2).doc.length() > 0) {
							download.setVisibility(View.VISIBLE);
						} else {
							download.setVisibility(View.INVISIBLE);
						}
						download.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								showDialog1();
								/*
								 * Intent i = new Intent(Intent.ACTION_VIEW);
								 * i.setData(Uri
								 * .parse(CollegeUrls.api_get_schedule_doc +
								 * "1/" + schedules.get(arg2).id));
								 * startActivity(i);
								 */

							}

							private void showDialog1() {
								final String id1 = CollegeAppUtils
										.GetSharedParameter(
												CollegeSubjectDetailActivityLessonPlan.this,
												"id");
								final String assign2 = "Weekschedule";
								Url = CollegeUrls.base_url
										+ CollegeUrls.api_get_doc + "/" + id1
										+ "/" + schedules.get(arg2).id + "/"
										+ assign2;
								Log.d("URL OF DOWNLOAD", Url);
								final Dialog dialog2 = new Dialog(context);
								dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
								TextView txtView = (TextView) dialog2
										.findViewById(R.id.txtView);
								TextView txtDwnld = (TextView) dialog2
										.findViewById(R.id.txtDownload);
								// if button is clicked, close
								// the custom dialog
								txtView.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent i = new Intent(
												getApplicationContext(),
												CollegeWebviewActivity.class);
										i.putExtra("Download_Week_Lesson", Url);
										i.putExtra("from", "WeekLesson");
										startActivity(i);
										dialog2.dismiss();
									}
								});
								txtDwnld.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent i = new Intent(
												Intent.ACTION_VIEW);
										i.setData(Uri
												.parse(CollegeUrls.base_url
														+ CollegeUrls.api_get_doc
														+ "/"
														+ id1
														+ "/"
														+ schedules.get(arg2).id
														+ "/" + assign2));
										startActivity(i);
										dialog2.dismiss();
									}
								});

								dialog2.show();
							}
						});
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

			} else {
				// setListView();
				Log.e("Current Date...", CollegeAppUtils.getCurrentDate());
				days = new ArrayList<String>();

				for (TeacherSchedule schdule : schedules) {

					try {

						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date date1 = null;
						try {
							date1 = sdf.parse(schdule.start_date.toString());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Date date2 = null;
						try {
							date2 = sdf.parse(schdule.end_date.toString());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Date date3 = null;
						try {
							date3 = sdf.parse(CollegeAppUtils.getCurrentDate());
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("last" + sdf.format(date1));
						System.out.println("last" + sdf.format(date2));
						System.out.println("last" + sdf.format(date3));

						if ((date1.before(date3)) && (date2.after(date3))) {
							// System.out.println(date1 +""+ date2);
							days.add(
									0,
									schdule.start_date.replace("-", "/")
											+ " - "
											+ schdule.end_date
													.replace("-", "/"));
						} else {
							days.add(schdule.start_date.replace("-", "/")
									+ " - "
									+ schdule.end_date.replace("-", "/"));
						}

					} catch (ParseException ex) {
						ex.printStackTrace();
					}

					// days.add(SchoolAppUtils.getDateFormat(schdule.date) +
					// " - " +
					// SchoolAppUtils.getDateFormat(SchoolAppUtils.getLastDayofWeek(schdule.date,
					// 6)));
				}

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						CollegeSubjectDetailActivityLessonPlan.this,
						R.layout.simple_spinner_item_custom_new, days);
				dataAdapter
						.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
				date.setAdapter(dataAdapter);
				date.setSelection(selectedPosition);
				date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {

						String topicString = "";
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
									+ CollegeAppUtils
											.getDateFormat(daily_schedules
													.get(i).date));

							/*
							 * difference = SchoolAppUtils.getDiffBTDates(
							 * daily_schedules.get(i).date,
							 * schedules.get(i).date);
							 */

							viewHolder.edit.setTag(new ScheduleTag(
									daily_schedules.get(i).id, daily_schedules
											.get(i).topic, daily_schedules
											.get(i).date, difference));
							viewHolder.getdoc = (ImageButton) rowView
									.findViewById(R.id.download_btn);
							if (daily_schedules.get(i).doc.length() > 0) {
								final String file_id = daily_schedules.get(i).id;
								viewHolder.getdoc.setVisibility(View.VISIBLE);
								viewHolder.getdoc
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												/*
												 * Intent intent = new Intent(
												 * Intent.ACTION_VIEW);
												 * intent.setData(Uri
												 * .parse(CollegeUrls
												 * .api_get_schedule_doc + "2/"
												 * + file_id));
												 * startActivity(intent);
												 */
												showDialog();
											}
										});
							} else {
								viewHolder.getdoc.setVisibility(View.GONE);
							}

							linearLayout.addView(rowView);

						}

						if (schedules.get(arg2).doc.length() > 0) {
							download.setVisibility(View.VISIBLE);
						} else {
							download.setVisibility(View.INVISIBLE);
						}
						download.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								/*
								 * Intent i = new Intent(Intent.ACTION_VIEW);
								 * i.setData(Uri
								 * .parse(CollegeUrls.api_get_schedule_doc +
								 * "1/" + schedules.get(arg2).doc));
								 * startActivity(i);
								 */
								showDialog2();
							}

							private void showDialog2() {

								final String id1 = CollegeAppUtils
										.GetSharedParameter(
												CollegeSubjectDetailActivityLessonPlan.this,
												"id");
								final String assign2 = "Weekschedule";
								Url = CollegeUrls.base_url
										+ CollegeUrls.api_get_doc + "/" + id1
										+ "/" + schedules.get(arg2).id + "/"
										+ assign2;
								Log.d("URL OF DOWNLOAD", Url);
								final Dialog dialog2 = new Dialog(context);
								dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
								TextView txtView = (TextView) dialog2
										.findViewById(R.id.txtView);
								TextView txtDwnld = (TextView) dialog2
										.findViewById(R.id.txtDownload);
								// if button is clicked, close
								// the custom dialog
								txtView.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent i = new Intent(
												getApplicationContext(),
												CollegeWebviewActivity.class);
										i.putExtra("Download_Week_Lesson", Url);
										i.putExtra("from", "WeekLesson");
										startActivity(i);
										dialog2.dismiss();
									}
								});
								txtDwnld.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent i = new Intent(
												Intent.ACTION_VIEW);
										i.setData(Uri
												.parse(CollegeUrls.base_url
														+ CollegeUrls.api_get_doc
														+ "/"
														+ id1
														+ "/"
														+ schedules.get(arg2).id
														+ "/" + assign2));
										startActivity(i);
										dialog2.dismiss();
									}
								});

								dialog2.show();

							}
						});

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(
								CollegeSubjectDetailActivityLessonPlan.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeSubjectDetailActivityLessonPlan.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				}
				/*
				 * else {
				 * SchoolAppUtils.showDialog(SubjectDetailActivity.this,getResources
				 * ().getString(R.string.error),
				 * getResources().getString(R.string
				 * .please_check_internet_connection)); }
				 */

			}
		}
	}

	public void showDialog() {

		final String id1 = CollegeAppUtils.GetSharedParameter(
				CollegeSubjectDetailActivityLessonPlan.this, "id");
		Url = CollegeUrls.base_url + CollegeUrls.api_get_doc + "/" + id1 + "/"
				+ file_id + "/" + assign;
		Log.d("URL OF DOWNLOAD", Url);
		final Dialog dialog2 = new Dialog(context);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
		TextView txtView = (TextView) dialog2.findViewById(R.id.txtView);
		TextView txtDwnld = (TextView) dialog2.findViewById(R.id.txtDownload);
		// if button is clicked, close
		// the custom dialog
		txtView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						CollegeWebviewActivity.class);
				i.putExtra("Download_Daily_Lesson", Url);
				i.putExtra("from", "DailyLesson");
				startActivity(i);
				dialog2.dismiss();
			}
		});
		txtDwnld.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(CollegeUrls.base_url
						+ CollegeUrls.api_get_doc + "/" + id1 + "/" + file_id
						+ "/" + assign));
				startActivity(i);
				dialog2.dismiss();
			}
		});

		dialog2.show();

	}

}
