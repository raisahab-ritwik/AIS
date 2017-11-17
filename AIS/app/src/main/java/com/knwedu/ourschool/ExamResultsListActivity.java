package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.ExamResultsAdapterNew;
import com.knwedu.ourschool.adapter.ResultAdapterDetailMarks;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Examdetail;
import com.knwedu.ourschool.utils.DataStructureFramwork.MarksConsolidated;
import com.knwedu.ourschool.utils.DataStructureFramwork.MarksDetails;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ExamResultsListActivity extends Activity {
	private ArrayList<MarksConsolidated> marks;
	private ProgressDialog dialog;
	private TextView name, classs, term, session, total, obtained, address,
			school_name, roll_no, rank, remarks, comments;
	private ListView listView;
	// private ExamResultsAdapter adapter;

	private ImageButton graphBtn;
	private ImageButton getDetailsResultBtn, getConsolidatedBtn;
	private String gradeS;
	private String EXAM_TERM_ID;
	TextView headerText;
	private ImageView download_report;
	private LinearLayout infoLayout;
	private String file_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exams_list);
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initialize() {

			((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		infoLayout = (LinearLayout) findViewById(R.id.info_layout);
		headerText = (TextView) findViewById(R.id.header_text);
		listView = (ListView) findViewById(R.id.listview);
		graphBtn = (ImageButton) findViewById(R.id.graph_btn);
		name = (TextView) findViewById(R.id.name_txt);
		classs = (TextView) findViewById(R.id.class_txt);
		term = (TextView) findViewById(R.id.exam_term_txt);
		session = (TextView) findViewById(R.id.session_txt);
		total = (TextView) findViewById(R.id.total_txt);
		obtained = (TextView) findViewById(R.id.obtained_txt);
		address = (TextView) findViewById(R.id.address_txt);
		school_name = (TextView) findViewById(R.id.school_name_txt);
		roll_no = (TextView) findViewById(R.id.roll_no_txt);
		rank = (TextView) findViewById(R.id.rank_txt);
		remarks = (TextView) findViewById(R.id.remarks_txt);
		comments = (TextView) findViewById(R.id.comments_txt);
		download_report = (ImageView) findViewById(R.id.download_attchment);
		download_report.setOnClickListener(listener);
		getConsolidatedBtn = (ImageButton) findViewById(R.id.btn_consolidated);
		getDetailsResultBtn = (ImageButton) findViewById(R.id.btn_details);

		EXAM_TERM_ID = getIntent().getExtras().getString("EXAM_TERM_ID");

		// Selected Child info
		if (SchoolAppUtils.GetSharedParameter(ExamResultsListActivity.this,
				Constants.USERTYPEID).equalsIgnoreCase(
				Constants.USERTYPE_PARENT)) {
			JSONObject c = null;
			try {
				c = new JSONObject(SchoolAppUtils.GetSharedParameter(
						ExamResultsListActivity.this,
						Constants.SELECTED_CHILD_OBJECT));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StudentProfileInfo info = new StudentProfileInfo(c);
			name.setText(info.fname + " " + info.lname);
			classs.setText(info.class_section);

			infoLayout.setVisibility(View.VISIBLE);
		} else {
			infoLayout.setVisibility(View.GONE);

		}

		graphBtn.setVisibility(View.VISIBLE);

		headerText.setText(R.string.exam_results);
		term.setText(SchoolAppUtils.termName);

		graphBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ExamResultsListActivity.this,
						GraphViewExamActivity.class);
				intent.putExtra("exam_term_id", EXAM_TERM_ID);

				intent.putExtra("api_url", Urls.api_graph_exam_result);
				startActivity(intent);

			}
		});

		// Default consolidated
		//getConsolidatedResult();
		getDetailsResult();
		/*getDetailsResultBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getDetailsResult();

			}
		});

		getConsolidatedBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getConsolidatedResult();

			}
		});*/

	}

	private void getConsolidatedResult() {
		headerText.setText("Result: Consolidated");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(ExamResultsListActivity.this,
						Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(ExamResultsListActivity.this,
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(ExamResultsListActivity.this,
						Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("exam_term_id", EXAM_TERM_ID));
		nameValuePairs.add(new BasicNameValuePair("post_type", "2"));

		nameValuePairs.add(new BasicNameValuePair("student_id", SchoolAppUtils
				.GetSharedParameter(ExamResultsListActivity.this,
						Constants.CHILD_ID)));
		new GetConsolidatedAsyntask().execute(nameValuePairs);

		getConsolidatedBtn.setVisibility(View.GONE);
		getDetailsResultBtn.setVisibility(View.VISIBLE);

	}

	private void getDetailsResult() {

		headerText.setText("Result: Detailed");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(ExamResultsListActivity.this,
						Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(ExamResultsListActivity.this,
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(ExamResultsListActivity.this,
						Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("exam_term_id", EXAM_TERM_ID));
		nameValuePairs.add(new BasicNameValuePair("student_id", SchoolAppUtils
				.GetSharedParameter(ExamResultsListActivity.this,
						Constants.CHILD_ID)));
		nameValuePairs.add(new BasicNameValuePair("post_type", "1"));

		new GetDetailResultAsyntask().execute(nameValuePairs);
		getConsolidatedBtn.setVisibility(View.GONE);
		getDetailsResultBtn.setVisibility(View.VISIBLE);

	}

	private class GetConsolidatedAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		Examdetail detail;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ExamResultsListActivity.this);
			dialog.setTitle("Loading Consolidated Result");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> namevaluepair = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_exam_result);
			String parameters = "";
			for (NameValuePair nvp : namevaluepair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_exam_result);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						JSONObject detail_data = json
								.getJSONObject("consolidate");
						JSONArray array;
						JSONArray array_info;
						try {

							array = detail_data.getJSONArray("consolidated");
							array_info = detail_data
									.getJSONArray("profile_data");
							JSONObject obj = array_info.getJSONObject(0);
							detail = new Examdetail(obj);
						} catch (Exception e) {
							return true;
						}

						marks = new ArrayList<MarksConsolidated>();
						for (int i = 0; i < array.length(); i++) {
							MarksConsolidated assignment = new MarksConsolidated(
									array.getJSONObject(i));
							marks.add(assignment);
						}
						return true;
					}
				} else {
					try {
						error = json.getString("consolidate");
					} catch (Exception e) {
					}
					return false;
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
				if (marks != null) {
					if (SchoolAppUtils.GetSharedParameter(
							ExamResultsListActivity.this, Constants.USERTYPEID)
							.equalsIgnoreCase(Constants.USERTYPE_TEACHER)) {

					} else {
						name.setText(detail.fname + " " + detail.lname);
						classs.setText(detail.class_name);
						school_name.setText(detail.school_name);
						roll_no.setText(detail.roll_no);
						address.setText(detail.address);
						rank.setText(detail.rank);
						remarks.setText(detail.remarks);
						comments.setText(detail.comments);
						term.setText(detail.exam);
						session.setText(detail.session);
						infoLayout.setVisibility(View.VISIBLE);
					}
					String temp = null;
					for (int i = 0; i < marks.size(); i++) {
						if (i == 0) {
							marks.get(0).check = true;
							temp = marks.get(0).group_name;
						} else {
							if (!temp.equalsIgnoreCase(marks.get(i).group_name)) {
								marks.get(i).check = true;
								temp = marks.get(i).group_name;
							}
						}
					}
					ExamResultsAdapterNew adapter = new ExamResultsAdapterNew(
							ExamResultsListActivity.this, marks);
					listView.setAdapter(adapter);
					int totalMarks = 0;
					int marksobtained = 0;
					for (int i = 0; i < marks.size(); i++) {
						marksobtained = marksobtained
								+ Integer.parseInt(marks.get(i).Marks);
						totalMarks = totalMarks
								+ Integer.parseInt(marks.get(i).Out_of_marks);
					}
					total.setText("" + totalMarks);
					obtained.setText("" + marksobtained);

				}
			} else {
				SchoolAppUtils.showDialog(
						ExamResultsListActivity.this,
						getTitle().toString(),
						getResources().getString(
								R.string.please_check_internet_connection));
				total.setVisibility(View.INVISIBLE);
				obtained.setVisibility(View.INVISIBLE);
				graphBtn.setVisibility(View.INVISIBLE);
			}
		}

	}

	private class GetDetailResultAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;
		ArrayList<MarksDetails> marksDetailList = null;
		Examdetail detail;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ExamResultsListActivity.this);
			dialog.setTitle("Loading Detailed Result");
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> nameValuePair = params[0];

			// Log parameters:
			Log.d("url extension: ", Urls.api_exam_result);
			String parameters = "";
			for (NameValuePair nvp : nameValuePair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
					Urls.api_exam_result);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						// Details
						JSONObject detail_data = json
								.getJSONObject("detail_data");
						JSONArray arrayDetails, arrayAssignment, arrayTest, arrayActivity;
						JSONArray array_info;
						try {
							arrayDetails = detail_data
									.getJSONArray("exam_data");
							arrayAssignment = detail_data
									.getJSONArray("assignment_data");
							arrayTest = detail_data.getJSONArray("test_data");
							arrayActivity = detail_data
									.getJSONArray("activity_data");
							array_info = detail_data
									.getJSONArray("profile_data");
							JSONObject obj = array_info.getJSONObject(0);
							detail = new Examdetail(obj);
						} catch (Exception e) {
							return true;
						}
						marksDetailList = new ArrayList<MarksDetails>();
						for (int i = 0; i < arrayDetails.length(); i++) {
							MarksDetails mark = new MarksDetails(
									arrayDetails.getJSONObject(i));
							marksDetailList.add(mark);
						}

						for (int i = 0; i < arrayAssignment.length(); i++) {
							MarksDetails mark = new MarksDetails(
									arrayAssignment.getJSONObject(i));
							marksDetailList.add(mark);
						}

						for (int i = 0; i < arrayTest.length(); i++) {
							MarksDetails mark = new MarksDetails(
									arrayTest.getJSONObject(i));
							marksDetailList.add(mark);
						}

						for (int i = 0; i < arrayActivity.length(); i++) {
							MarksDetails mark = new MarksDetails(
									arrayActivity.getJSONObject(i));
							marksDetailList.add(mark);
						}

						return true;
					} else {
						try {
							error = "Data Error";
						} catch (Exception e) {
						}
						return false;
					}
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

			if (marksDetailList != null) {

				/*if (SchoolAppUtils.GetSharedParameter(
						ExamResultsListActivity.this, Constants.USERTYPEID)
						.equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {*/

					name.setText(detail.fname + " " + detail.lname);
					classs.setText(detail.class_name);
					school_name.setText(detail.school_name);
					roll_no.setText(detail.roll_no);
					address.setText(detail.address);
					rank.setText(detail.rank);
					remarks.setText(detail.remarks);
					comments.setText(detail.comments);
					term.setText(detail.exam);
					session.setText(detail.session);
					infoLayout.setVisibility(View.VISIBLE);
					
					file_name = detail.exam + "_" + detail.session + ".pdf";
//				}
				String temp = null;
				for (int i = 0; i < marksDetailList.size(); i++) {
					if (i == 0) {
						marksDetailList.get(0).check = true;
						temp = marksDetailList.get(0).subject_group;
					} else {
						if (!temp
								.equalsIgnoreCase(marksDetailList.get(i).subject_group)) {
							marksDetailList.get(i).check = true;
							temp = marksDetailList.get(i).subject_group;
						}
					}
				}
				ResultAdapterDetailMarks adapter = new ResultAdapterDetailMarks(
						ExamResultsListActivity.this, marksDetailList);

				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				listView.setOnItemClickListener(null);
			}

			else {

				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils.showDialog(ExamResultsListActivity.this,
								getTitle().toString(), error);

					} else {
						SchoolAppUtils
								.showDialog(
										ExamResultsListActivity.this,
										getTitle().toString(),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							ExamResultsListActivity.this,
							getTitle().toString(),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			new AlertDialog.Builder(ExamResultsListActivity.this)
					.setTitle("Select option")
					.setPositiveButton("View Document",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with view
									SchoolAppUtils
											.viewDocument(
													ExamResultsListActivity.this,
													Urls.base_url
															+ Urls.api_report_card_download_mobile
															+ "/"
															+ EXAM_TERM_ID
															+ "/"
															+ SchoolAppUtils
																	.GetSharedParameter(
																			ExamResultsListActivity.this,
																			Constants.UORGANIZATIONID)
															+ "/"
															+ SchoolAppUtils
																	.GetSharedParameter(
																			ExamResultsListActivity.this,
																			Constants.CHILD_ID));
								}
							})
					.setNegativeButton("Download",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// download





									final DownloadTask downloadTask = new DownloadTask(ExamResultsListActivity.this, file_name);
									downloadTask.execute(Urls.base_url
											+ Urls.api_report_card_download_mobile
											+ "/"
											+ EXAM_TERM_ID
											+ "/"
											+ SchoolAppUtils
													.GetSharedParameter(
															ExamResultsListActivity.this,
															Constants.UORGANIZATIONID)
											+ "/"
											+ SchoolAppUtils
													.GetSharedParameter(
															ExamResultsListActivity.this,
															Constants.CHILD_ID));
									
									
									
									
									
									/*
									Intent i = new Intent(Intent.ACTION_VIEW);
									i.setData(Uri.parse(Urls.base_url
											+ Urls.api_report_card_download_mobile
											+ "/"
											+ EXAM_TERM_ID
											+ "/"
											+ SchoolAppUtils
													.GetSharedParameter(
															ExamResultsListActivity.this,
															Constants.UORGANIZATIONID)
											+ "/"
											+ SchoolAppUtils
													.GetSharedParameter(
															ExamResultsListActivity.this,
															Constants.CHILD_ID)));
									startActivity(i);*/

								}
							}).setIcon(android.R.drawable.ic_dialog_info)
					.show();

		}
	};
}
