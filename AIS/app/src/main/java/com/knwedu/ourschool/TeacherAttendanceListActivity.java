package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Attendance;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DataStructureFramwork.TeacherSubjectByTimeTable;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherAttendanceListActivity extends FragmentActivity {
	private TextView title, header;
	private Button date, submitBtn;
	private String dateSelected, section, nDate;
	private ArrayList<Attendance> attendances;
	private ArrayList<Attendance> attendancesupdate;
	public DatabaseAdapter mDatabase;
	private TextView textEmpty;

	// private ListView listView;
	private LinearLayout scroll_layout;
	private int[] items;
	private AttendanceAdapter adapter;
	private ProgressDialog dialog;
	AttendanceAdapter vie;
	private String page_title = "";

	private TeacherSubjectByTimeTable subject;
	private TextView sectionClass;
	int checkAttendance = 0, loopCount = 0, initialAttendance = 0,
			initialDefaultValue = 0;
	private static boolean showDetailInfo;
	String mode = "view_attendance_subject", subject_id, section_id,
			id_teacher, group_id, lecture_num, class_name, section_name;
	private EditText edTxt;
	int teacher_id;
	AlertDialog.Builder dialoggg;
	private boolean internetAvailable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_teacher_attendance_list);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);

		mDatabase = ((SchoolApplication) TeacherAttendanceListActivity.this
				.getApplication()).getDatabase();
		if (SchoolAppUtils.isOnline(TeacherAttendanceListActivity.this)) {
			internetAvailable = true;

		} else {
			findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
			new OfflineSubjectDetailsAsync().execute();

		}

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
		// internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
		// false);
		System.out.println("Internate=====" + internetAvailable);
		if (internetAvailable) {
			if (getIntent().getExtras() != null) {
				String temp = getIntent().getExtras().getString(Constants.ID);
				JSONObject c = null;
				try {
					c = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				subject = new TeacherSubjectByTimeTable(c);
				dateSelected = getIntent().getExtras().getString("Date");
				// section=getIntent().getExtras().getString("section");

			}
		} else {
			subject_id = getIntent().getExtras().getString(
					Constants.OFFLINE_SUBJECT_ID);
			id_teacher = getIntent().getExtras().getString(
					Constants.OFFLINE_TEACHER_ID);
			section_id = getIntent().getExtras().getString(
					Constants.OFFLINE_SECTION_ID);
			group_id = getIntent().getExtras().getString(
					Constants.OFFLINE_GROUP_ID);
			lecture_num = getIntent().getExtras().getString(
					Constants.OFFLINE_LECTURE_NUM);
			class_name = getIntent().getExtras().getString(
					Constants.OFFLINE_CLASS_NAME);
			section_name = getIntent().getExtras().getString(
					Constants.OFFLINE_SECTION_NAME);
			System.out.println("subject_id=====" + subject_id);
			dateSelected = getIntent().getExtras().getString("Date");

		}
		// textEmpty = (TextView) findViewById(R.id.textEmpty);

		header = (TextView) findViewById(R.id.header_text);
		header.setText(page_title);
		title = (TextView) findViewById(R.id.title_txt);
		date = (Button) findViewById(R.id.date_btns);
		scroll_layout = (LinearLayout) findViewById(R.id.scroll_layout);
		submitBtn = (Button) findViewById(R.id.submit_btn);
		sectionClass = (TextView) findViewById(R.id.class_txt);
		if (internetAvailable) {
			if (subject != null) {
				sectionClass
						.setText(subject.classname + " " + subject.section_name);
			}
		} else {
			sectionClass.setText(class_name + " " + section_name);
		}
		/* dateSelected = SchoolAppUtils.getCurrentDate(); */
		date.setText(SchoolAppUtils.getDayDifferentDif(SchoolAppUtils
				.getCurrentDate()));
		Log.d("NewDATE:", dateSelected);

		if (attendances != null) {
			adapter = new AttendanceAdapter();
			// listView.setAdapter(adapter);
			adapter.updateView();
		} else {

			initialAttendance = 1;

			if (SchoolAppUtils.isOnline(getApplicationContext())) {
				if (subject != null) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							3);
					nameValuePairs.add(new BasicNameValuePair("user_type_id",
							SchoolAppUtils.GetSharedParameter(
									TeacherAttendanceListActivity.this,
									Constants.USERTYPEID)));
					nameValuePairs.add(new BasicNameValuePair(
							"organization_id", SchoolAppUtils
									.GetSharedParameter(
											TeacherAttendanceListActivity.this,
											Constants.UORGANIZATIONID)));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							SchoolAppUtils.GetSharedParameter(
									TeacherAttendanceListActivity.this,
									Constants.USERID)));

					nameValuePairs.add(new BasicNameValuePair("section_id",
							subject.section_id));
					nameValuePairs.add(new BasicNameValuePair("lecture_num",
							subject.lecture_num));
					nameValuePairs.add(new BasicNameValuePair("group_id",
							subject.group_id));

					new GetListAsyntask().execute(nameValuePairs);
				}
			} else {
				new OfflineSubjectDetailsAsync().execute();
			}

			initialDefaultValue = 0;

			// currentAttendance();
			// attendanceView(dateSelected);

		}

		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (items != null) {
					if (attendances != null) {
						if (internetAvailable) {
							if (dateSelected.toString()
									.equalsIgnoreCase("date")) {
								Toast.makeText(
										TeacherAttendanceListActivity.this,
										"Select Date", Toast.LENGTH_SHORT)
										.show();
								return;
							}
							HashMap<String, String> marks = new HashMap<String, String>();
							HashMap<String, String> late = new HashMap<String, String>();

							for (int i = 0; i < items.length; i++) {
								String temp;
								if (items[i] == 0) {
									temp = "1";
								} else if (items[i] == 1) {
									temp = "4";
								} else if (items[i] == 3) {
									temp = "3";
								} else {
									temp = "2";
								}
								marks.put(attendances.get(i).student_id, temp);

							}

							for (int i = 0; i < attendances.size(); i++) {
								String descp = attendances.get(i).late;

								late.put(attendances.get(i).student_id, descp);

							}
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
									5);
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
							nameValuePairs.add(new BasicNameValuePair(
									"user_id", SchoolAppUtils
											.GetSharedParameter(
													getApplicationContext(),
													Constants.USERID)));
							nameValuePairs.add(new BasicNameValuePair(
									"section_id", subject.section_id));
							nameValuePairs.add(new BasicNameValuePair(
									"teacher_id", subject.teacher_id));
							nameValuePairs.add(new BasicNameValuePair("sr_id",
									subject.srid));

							nameValuePairs.add(new BasicNameValuePair(
									"group_id", subject.group_id));
							nameValuePairs.add(new BasicNameValuePair(
									"lecture_num", subject.lecture_num));
							nameValuePairs.add(new BasicNameValuePair("date",
									dateSelected));
							nameValuePairs.add(new BasicNameValuePair(
									"stu_stat", marks.toString()));
							nameValuePairs.add(new BasicNameValuePair(
									"late_rea", late.toString()));

							new UploadAsyntask().execute(nameValuePairs);
						}

						else {
							ArrayList<Attendance> attendance = new ArrayList<Attendance>();
							mDatabase.open();
							attendance = mDatabase
									.getAllAttendanceforUpload(lecture_num);
							if (attendance.size() > 0) {
								mDatabase.deleteAttendance(lecture_num);
							}
							mDatabase.close();
							HashMap<String, String> marks = new HashMap<String, String>();
							HashMap<String, String> late = new HashMap<String, String>();

							for (int i = 0; i < items.length; i++) {
								String temp;
								if (items[i] == 0) {
									temp = "1";
								} else if (items[i] == 1) {
									temp = "4";
								} else if (items[i] == 3) {
									temp = "3";
								} else {
									temp = "2";
								}
								marks.put(attendances.get(i).student_id, temp);

							}

							for (int i = 0; i < attendances.size(); i++) {
								String descp = attendances.get(i).late;

								if(attendances.get(i).student_id.equals("")){
									late.put(attendances.get(i).student_id, "NA");
								}
								else {
									late.put(attendances.get(i).student_id, descp);
								}

							}
							mDatabase.open();
							for (int i = 0; i < items.length; i++) {

								mDatabase.addAttenstatus(
										attendances.get(i).student_id,
										section_id, id_teacher, subject_id,
										group_id, lecture_num, dateSelected,
										attendances.get(i).late, items[i],
										attendances.get(i).student_name,
										attendances.get(i).roll_no,
										attendances.get(i).leave_master_reason);
								System.out.println("database--"
										+ attendances.get(i).student_id
										+ ""
										+ section_id
										+ ""
										+ id_teacher
										+ ""
										+ subject_id
										+ ""
										+ group_id
										+ ""
										+ lecture_num
										+ ""
										+ dateSelected
										+ ""
										+ attendances.get(i).late
										+ ""
										+ items[i]
										+ ""
										+ attendances.get(i).student_name
										+ ""
										+ attendances.get(i).roll_no
										+ ""
										+ attendances.get(i).leave_master_reason);

							}
							mDatabase.close();
							final AlertDialog.Builder dialog = new AlertDialog.Builder(
									TeacherAttendanceListActivity.this);
							dialog.setTitle("Attendance");
							// mDatabase.close();
							dialog.setMessage("Attendance Added in Offline Mode");

							dialog.setNegativeButton("OK",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									});
							dialog.show();
						}
					}

				}
			}
		});
	}

	private class AttendanceAdapter {
		ViewHolder holder;
		private LayoutInflater inflater;
		View convertView;
		int position;

		public AttendanceAdapter() {
			inflater = (LayoutInflater) TeacherAttendanceListActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		private void notifyUI(ViewHolderPosition viewHolder) {
			ViewHolder holder = viewHolder.view_holder;
			int position = viewHolder.view_position;

			if (items[position] == 0) {
				holder.present.setImageResource(R.drawable.p_btn);
				holder.leave.setImageResource(R.drawable.l_btn_slect);
				holder.absent.setImageResource(R.drawable.a_btn_slect);
				holder.late.setImageResource(R.drawable.lt_btn_slect);
				holder.early_departure.setImageResource(R.drawable.blue_ede);

			} else if (items[position] == 1) {
				holder.present.setImageResource(R.drawable.p_btn_slect);
				holder.leave.setImageResource(R.drawable.l_btn);
				holder.absent.setImageResource(R.drawable.a_btn_slect);
				holder.late.setImageResource(R.drawable.lt_btn_slect);
				holder.early_departure.setImageResource(R.drawable.blue_ede);

			} else if (items[position] == 2) {
				holder.present.setImageResource(R.drawable.p_btn_slect);
				holder.leave.setImageResource(R.drawable.l_btn_slect);
				holder.absent.setImageResource(R.drawable.a_btn);
				holder.late.setImageResource(R.drawable.lt_btn_slect);
				holder.early_departure.setImageResource(R.drawable.blue_ede);

			} else if (items[position] == 3) {

				holder.absent.setImageResource(R.drawable.a_btn_slect);
				holder.present.setImageResource(R.drawable.p_btn_slect);
				holder.leave.setImageResource(R.drawable.l_btn_slect);
				holder.late.setImageResource(R.drawable.lt_btn);
				holder.early_departure.setImageResource(R.drawable.blue_ede);
			}
			else if (items[position] == 4) {

				holder.absent.setImageResource(R.drawable.a_btn_slect);
				holder.present.setImageResource(R.drawable.p_btn_slect);
				holder.leave.setImageResource(R.drawable.l_btn_slect);
				holder.late.setImageResource(R.drawable.lt_btn_slect);
				holder.early_departure.setImageResource(R.drawable.gray_ede);

			}

		}

		public void updateView() {
			try {
				scroll_layout.removeAllViews();
			} catch (Exception ex) {
			}

			items = new int[attendances.size()];
			System.out.println("Attendence Size:: " + attendances.size());

			for (position = 0; position < attendances.size(); position++) {
				convertView = inflater.inflate(R.layout.attendance_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.student_name_txt);
				holder.leave = (ImageView) convertView
						.findViewById(R.id.leave_img);
				holder.absent = (ImageView) convertView
						.findViewById(R.id.absent_img);
				holder.present = (ImageView) convertView
						.findViewById(R.id.present_img);
				holder.late = (ImageView) convertView
						.findViewById(R.id.btn_late);
				holder.early_departure=(ImageView)convertView.findViewById(R.id.btn_earlyDeparture);

				holder.leave.setTag(new ViewHolderPosition(holder, position));
				holder.absent.setTag(new ViewHolderPosition(holder, position));
				holder.present.setTag(new ViewHolderPosition(holder, position));
				holder.late.setTag(new ViewHolderPosition(holder, position));
				holder.early_departure.setTag(new ViewHolderPosition(holder, position));


				convertView.setTag(holder);
				if (!attendances.get(position).late.equalsIgnoreCase("NA")) {
					items[position] = 1;
				}

				Log.d("positions:", Integer.toString(items[position]));

				if (items[position] == 0) {
					holder.present.setImageResource(R.drawable.p_btn);
					holder.leave.setImageResource(R.drawable.l_btn_slect);
					holder.absent.setImageResource(R.drawable.a_btn_slect);
					holder.late.setImageResource(R.drawable.lt_btn_slect);
					holder.early_departure.setImageResource(R.drawable.blue_ede);

				} else if (items[position] == 1) {
					if (!attendances.get(position).leave_master_reason
							.equalsIgnoreCase("NA")) {

						holder.absent.setVisibility(View.GONE);

					}
					holder.present.setImageResource(R.drawable.p_btn_slect);
					holder.leave.setImageResource(R.drawable.l_btn);
					holder.absent.setImageResource(R.drawable.a_btn_slect);
					holder.late.setImageResource(R.drawable.lt_btn_slect);
					holder.early_departure.setImageResource(R.drawable.blue_ede);
				}
				else if (items[position] == 2) {
					holder.present.setImageResource(R.drawable.p_btn_slect);
					holder.leave.setImageResource(R.drawable.l_btn_slect);
					holder.absent.setImageResource(R.drawable.a_btn);
					holder.late.setImageResource(R.drawable.lt_btn_slect);
					holder.early_departure.setImageResource(R.drawable.blue_ede);
				} else if (items[position] == 3) {
					holder.absent.setImageResource(R.drawable.a_btn_slect);
					holder.present.setImageResource(R.drawable.p_btn_slect);
					holder.leave.setImageResource(R.drawable.l_btn_slect);
					holder.late.setBackgroundResource(R.drawable.lt_btn);
					holder.early_departure.setImageResource(R.drawable.blue_ede);
				}
				else if (items[position] == 4) {
					holder.absent.setImageResource(R.drawable.a_btn_slect);
					holder.present.setImageResource(R.drawable.p_btn_slect);
					holder.leave.setImageResource(R.drawable.l_btn_slect);
					holder.late.setBackgroundResource(R.drawable.lt_btn_slect);
					holder.early_departure.setImageResource(R.drawable.gray_ede);
				}

				holder.present.setOnClickListener(new OnClickListener() {
					// /look for changing
					@Override
					public void onClick(View v) {
						ViewHolderPosition viewHolderPosition = (ViewHolderPosition) v
								.getTag();
						System.out.println("Button Position: "
								+ viewHolderPosition.view_position);
						items[viewHolderPosition.view_position] = 0;
						notifyUI(viewHolderPosition);
					}
				});

				holder.leave.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolderPosition viewHolderPosition = (ViewHolderPosition) v
								.getTag();
						System.out.println("Button Position: "
								+ viewHolderPosition.view_position);
						items[viewHolderPosition.view_position] = 1;
						notifyUI(viewHolderPosition);
					}
				});

				holder.absent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolderPosition viewHolderPosition = (ViewHolderPosition) v
								.getTag();
						System.out.println("Button Position: "
								+ viewHolderPosition.view_position);
						items[viewHolderPosition.view_position] = 2;
						notifyUI(viewHolderPosition);
					}
				});

				holder.late.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolderPosition viewHolderPosition = (ViewHolderPosition) v
								.getTag();
						final int position = viewHolderPosition.view_position;
						System.out.println("Button Position: " + position);

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								TeacherAttendanceListActivity.this);

						// set title
						alertDialogBuilder.setTitle("Late Remarks!");

						// set dialog message
						alertDialogBuilder.setCancelable(false);

						View view = inflater.inflate(
								R.layout.late_remarks_dialog, null);
						Button btnSubmit = (Button) view
								.findViewById(R.id.btn_submit);
						Button btnCancel = (Button) view
								.findViewById(R.id.btn_cancel);
						Button selectfile = (Button) view
								.findViewById(R.id.select_file_for_upload);
						selectfile.setVisibility(View.GONE);
						edTxt = (EditText) view
								.findViewById(R.id.dialog_txt_remarks);
						if(attendances.get(position).late.equalsIgnoreCase("NA") ){
							edTxt.setText("");
							edTxt.setHint("Enter Remarks");

						}else {
							edTxt.setText(attendances.get(position).late);
						}
						if(!attendances.get(position).late.equalsIgnoreCase("NA") ) {
							edTxt.setSelection(attendances.get(position).late
									.length());
						}

						LinearLayout layout = (LinearLayout) view
								.findViewById(R.id.layout_student_info);
						layout.setVisibility(View.VISIBLE);

						TextView txtName = (TextView) view
								.findViewById(R.id.textView_name);
						txtName.setTextColor(Color.WHITE);
						txtName.setText("Name: "
								+ attendances.get(position).student_name);

						TextView txtRollNumber = (TextView) view
								.findViewById(R.id.textView_roll);
						txtRollNumber.setTextColor(Color.WHITE);

						txtRollNumber.setText("Roll Number: "
								+ attendances.get(position).roll_no);

						alertDialogBuilder.setView(view);

						// create alert dialog
						final AlertDialog alertDialog = alertDialogBuilder
								.create();

						// show it
						alertDialog.show();

						btnSubmit.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (edTxt.getText().toString().length() <= 0) {
									Toast.makeText(
											TeacherAttendanceListActivity.this,
											"Enter Remarks", Toast.LENGTH_SHORT)
											.show();
									return;
								}

								String descp = null;
								descp = edTxt.getText().toString();

								attendances.get(position).late = descp;

								alertDialog.cancel();

							}
						});

						btnCancel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								alertDialog.cancel();
							}
						});

						items[viewHolderPosition.view_position] = 3;
						notifyUI(viewHolderPosition);
					}
				});

				String rn = attendances.get(position).roll_no;

				if (attendances.get(position).leave_master_reason
						.equalsIgnoreCase("NA")) {

					holder.name.setTextColor(Color.WHITE);
					holder.name.setText(attendances.get(position).roll_no
							+ ". " + attendances.get(position).student_name);

				} else {
					holder.name.setTextColor(Color.RED);
					holder.name.setText(attendances.get(position).roll_no
							+ ". " + attendances.get(position).student_name
							+ " Leave from :"
							+ attendances.get(position).leave_master_reason);

				}

				Log.d("check:", Integer.toString(checkAttendance));

				if ((checkAttendance == 1 || initialAttendance == 1)) {

					if (attendances.get(position).status.equals("1")) {

						holder.present.setImageResource(R.drawable.p_btn);
						holder.leave.setImageResource(R.drawable.l_btn_slect);
						holder.absent.setImageResource(R.drawable.a_btn_slect);
						items[loopCount] = 0;
					} else if (attendances.get(position).status.equals("4")) {
						holder.present.setImageResource(R.drawable.p_btn_slect);
						holder.leave.setImageResource(R.drawable.l_btn);
						holder.absent.setImageResource(R.drawable.a_btn_slect);
						items[loopCount] = 1;
					} else if (attendances.get(position).status.equals("2")) {
						holder.present.setImageResource(R.drawable.p_btn_slect);
						holder.leave.setImageResource(R.drawable.l_btn_slect);
						holder.absent.setImageResource(R.drawable.a_btn);
						items[loopCount] = 2;
					} else if (attendances.get(position).status.equals("3")) {
						holder.present.setImageResource(R.drawable.p_btn_slect);
						holder.leave.setImageResource(R.drawable.l_btn_slect);
						holder.absent.setImageResource(R.drawable.a_btn_slect);
						holder.late.setImageResource(R.drawable.lt_btn);
						items[loopCount] = 3;
					}else if (attendances.get(position).status.equals("4")) {
					holder.present.setImageResource(R.drawable.p_btn_slect);
					holder.leave.setImageResource(R.drawable.l_btn_slect);
					holder.absent.setImageResource(R.drawable.a_btn_slect);
					holder.late.setImageResource(R.drawable.lt_btn_slect);
					holder.early_departure.setImageResource(R.drawable.gray_ede);
					items[loopCount] = 4;
				}


				else {

					}
					loopCount++;

					if (loopCount == attendances.size()) {
						checkAttendance = 0;
						loopCount = 0;
						initialAttendance = 0;
					}

				}

				scroll_layout.addView(convertView);
			}

		}

		private class ViewHolder {
			TextView name;
			ImageView leave, absent, present, late , early_departure;

		}

		private class ViewHolderPosition {
			ViewHolder view_holder;
			int view_position;

			ViewHolderPosition(ViewHolder view_holder, int view_position) {
				this.view_holder = view_holder;
				this.view_position = view_position;
			}
		}
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
			return new DatePickerDialog(TeacherAttendanceListActivity.this,
					this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {


			Calendar newDate = Calendar.getInstance();
			newDate.set(year, month, day);
			int mo = month + 1;

			nDate = year + "-" + mo + "-" + day;

			Calendar minDate = Calendar.getInstance();
			if (minDate != null && minDate.before(newDate)) {
				Toast.makeText(
						TeacherAttendanceListActivity.this,
						"Please set todays or past dates," + " not future date",
						Toast.LENGTH_LONG).show();
				onCreateDialog(getArguments());

				/*
				 * attendances = null; dateSelected =
				 * SchoolAppUtils.getCurrentDate();
				 * attendanceView(dateSelected);
				 */

				return;
			}
			checkAttendance = 1;
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
			date.setText(SchoolAppUtils.getDayDifferent(year + "/" + mon + "/"
					+ da));
			month = month + 1;
			if (month < 10) {
				mon = "0" + month;
			} else {
				mon = "" + month;
			}
			dateSelected = year + "/" + mon + "/" + da;
			attendanceView(nDate);

			// Intent intent = new Intent(TeacherAttendanceListActivity.this,
			// TeacherAttendanceListActivity.class);
			// intent.putExtra(Constants.ID,subject.toString());
			// intent.putExtra("Date", year + "-" + mon + "-" + da);
			// startActivity(intent);

		}

	}

	public void attendanceView(String newDate) {
		initialDefaultValue = 0;
		if (subject != null) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(
							TeacherAttendanceListActivity.this,
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(
							TeacherAttendanceListActivity.this,
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(TeacherAttendanceListActivity.this,
							Constants.USERID)));

			nameValuePairs.add(new BasicNameValuePair("section_id",
					subject.section_id));
			nameValuePairs.add(new BasicNameValuePair("lecture_num",
					subject.lecture_num));

			new GetListAsyntask().execute(nameValuePairs);
		}
	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(TeacherAttendanceListActivity.this);
			dialog.setTitle("Uploading Attendance");
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
					Urls.api_submit_all_attendance);
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
			if (error.length() > 0) {
				SchoolAppUtils.showDialog(TeacherAttendanceListActivity.this,
						getResources().getString(R.string.attendance), error);

			}
		}
	}

	private class GetListAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		private ProgressDialog dialog1;
		ArrayList<Attendance> attendancesUpdated = new ArrayList<Attendance>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (initialDefaultValue != 1) {
				dialog1 = new ProgressDialog(TeacherAttendanceListActivity.this);
				dialog1.setTitle(page_title);
				dialog1.setMessage(getResources().getString(
						R.string.please_wait));
				dialog1.setCanceledOnTouchOutside(false);
				dialog1.setCancelable(false);
				dialog1.show();
			}
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_get_all_student_attendance);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						attendancesUpdated = new ArrayList<Attendance>();
						for (int i = 0; i < array.length(); i++) {
							Attendance attendance = new Attendance(
									array.getJSONObject(i));
							attendancesUpdated.add(attendance);
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

			if (dialog1 != null) {
				if (dialog1.isShowing()) {
					dialog1.dismiss();
					dialog1 = null;
				}
			}

			if (initialAttendance == 1 && attendancesUpdated.size() == 0) {
				// dialog.dismiss();
				initialAttendance = 0;
				initialDefaultValue = 1;
				// currentAttendance();
			} else {

				if (result) {
					if (attendancesUpdated != null) {
						if (attendancesUpdated.size() > 0)

						{
							attendances = attendancesUpdated;

							for (int i = 0; i < attendances.size(); i++) {
								System.out.println("attendances.."
										+ attendances.get(i).student_id);
							}
							if (checkAttendance == 1) {
								Log.d("empty1", Integer
										.toString(attendancesUpdated.size()));

								TeacherAttendanceListActivity.this
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												if (adapter != null) {
													adapter.updateView();
												}
											}
										});

							} else {
								Log.d("emptya", Integer
										.toString(attendancesUpdated.size()));
								// title.setText(attendancesUpdated.get(0).classs);
								adapter = new AttendanceAdapter();
								// listView.setAdapter(adapter);
								adapter.updateView();
							}

						}
					}
				} else {
					if (error.length() > 0) {
						SchoolAppUtils.showDialog(
								TeacherAttendanceListActivity.this, page_title,
								error);
					} else {
						SchoolAppUtils.showDialog(
								TeacherAttendanceListActivity.this, page_title,
								getResources().getString(R.string.error));
					}
				}

				if (attendancesUpdated.size() == 0 && attendances != null) {

					// addthisone
					initialDefaultValue = 1;
					attendanceView(nDate);
					initialDefaultValue = 0;
					if (dialog1 != null) {
						if (dialog1.isShowing()) {
							dialog1.dismiss();
							dialog1 = null;
						}
					}
					// ///
					for (int i = 0; i < attendances.size(); i++) {
						Attendance obj = attendances.get(i);
						obj.status = "p";
						items[i] = 0;

						attendances.set(i, obj);
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (adapter != null) {
								adapter.updateView();
							}
						}
					});

				}

				Log.d("empty1", Integer.toString(attendancesUpdated.size()));
				if (dialog1 != null && initialDefaultValue != 1) {
					if (dialog1 != null) {
						if (dialog1.isShowing()) {
							dialog1.dismiss();
							dialog1 = null;
						}
					}
				}

			}

		}

	}

	class OfflineSubjectDetailsAsync extends AsyncTask<String, Subject, Void> {

		protected void onPreExecute() {
			super.onPreExecute();
			attendances = new ArrayList<Attendance>();
			attendances.clear();
		}

		protected Void doInBackground(String... params) {

			mDatabase.open();

			attendancesupdate = mDatabase
					.getAllAttendanceforUpload(lecture_num);

			if ((SchoolAppUtils.GetSharedParameter(
					TeacherAttendanceListActivity.this,
					Constants.ATTENDANCE_TYPE_PERMISSION))
					.equalsIgnoreCase("1")) {
				attendances = mDatabase
						.getAllAttendancewithsectionid(section_id);
			} else {
				attendances = mDatabase.getAllAttendance(subject_id);
			}

			mDatabase.close();

			return null;
		}

		@Override
		protected void onProgressUpdate(Subject... values) {
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// mDatabase.close();
			Log.d("DATA List in subject", "" + attendances);
			if (attendances != null) {
				System.out.println("attendancesupdate---"
						+ attendancesupdate.size());
				System.out.println("attendances---" + attendances.size());
				if (attendancesupdate.size() > 0) {
					checkAttendance =1;
					initialAttendance =1;
					adapter = new AttendanceAdapter();
					items = new int[attendancesupdate.size()];
					for (int i = 0; i < attendancesupdate.size(); i++) {
						Attendance obj = attendancesupdate.get(i);
						System.out.println("status----"
								+ attendancesupdate.get(i).status);
						items[i] = Integer
								.parseInt(attendancesupdate.get(i).status);

						if (items[i] == 0) {
							//obj.status = "p";
							obj.status = "0";
						} else if (items[i] == 1) {
							//obj.status = "le";
							obj.status = "4";
						} else if (items[i] == 2) {
							//obj.status = "a";
							obj.status = "2";
						} else {
							//obj.status = "la";
							obj.status = "3";
						}
						attendances.set(i, obj);

					}

					adapter.updateView();
				} else {
					String temp = "";
					items = new int[attendances.size()];
					for (int i = 0; i < attendances.size(); i++) {
						Attendance obj = new Attendance();
						obj = attendances.get(i);
						obj.status = "p";
						obj.late = "NA";
						items[i] = 0;

						attendances.set(i, obj);
					}

					if (attendances.size() > 0) {
						// textEmpty.setVisibility(View.GONE);
						adapter = new AttendanceAdapter();
						adapter.updateView();
					} else {
						// textEmpty.setText("No Subjects available in Offline mode");
						// textEmpty.setVisibility(View.VISIBLE);
					}
				}
			}
		}

	}

}
