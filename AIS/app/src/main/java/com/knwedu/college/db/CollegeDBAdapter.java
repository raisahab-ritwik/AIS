package com.knwedu.college.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.knwedu.college.utils.CollegeDataStructureFramwork;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Announcement;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Attendance;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Student;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TeacherSchedule;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TeacherSubjectByTimeTable;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TimeTable;
import com.knwedu.college.utils.CollegeDataStructureFramwork.CDirectory;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StudentProfileInfo;
import com.knwedu.college.utils.CollegeDataStructureFramwork.ParentProfileInfo;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TeacherProfileInfo;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Program;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StatusStudent;
import com.knwedu.ourschool.utils.DataStructureFramwork;


public class CollegeDBAdapter {

	private CollegeDatabaseHelper DBHelper;

	private SQLiteDatabase db;
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 4;
	private final Context context;

	// Logcat Log
	private static final String TAG = "DatabaseHelperSubject";

	// Database Name
	private static final String DATABASE_NAME = "teachersManager_college";

	// tables name
	private static final String TABLE_TEACHER_SUBJECT = "teacher_subject";
	private static final String TABLE_TEACHER_ASSIGNMENT = "teacher_assignment";
	private static final String TABLE_TEACHER_ANNOUNCEMENT = "teacher_announcement";
	private static final String TABLE_STUDENT_INFO = "student_info";
	private static final String TABLE_TEACHER_ATTENDANCE = "teacher_attendance";
	private static final String TABLE_TEACHER_TIMETABLE = "teacher_timetable";
	private static final String TABLE_TEACHER_STUDENT = "teacher_student";
	private static final String TABLE_TEACHER_LESSON_PLAN = "teacher_schedule";
	private static final String TABLE_TEACHER_LESSON_PLAN_UPLOAD = "teacher_lessonplan";
	private static final String TABLE_DAILY_LESSON_PLAN = "teacher_lesson_plan_daily";
	private static final String TABLE_WEEKLY_LESSON_PLAN = "teacher_lesson_plan_weekly";
	private static final String TABLE_DIRECTORY ="directory_off_line";
	private static final String TABLE_STUDENT_PROFILE = "student_profile_offline";
	private static final String TABLE_PARENT_PROFILE = "parent_profile_offline";
	private static final String TABLE_TEACHER_PROFILE = "teacher_profile_offline";
	private static final String TABLE_PROGRAM_PROFILE = "program_profile_offline";
	private static final String TABLE_STUD_PARENT_ATTENDANCE = "student_parent_attendance";

	// TABLE_DIRECTORY Columns names
	private static final String KEY_ROW_DIR_NAME = "name";
	private static final String KEY_ROW_DIR_DESIGNATION = "designition";
	private static final String KEY_ROW_DIR_PHNO ="phone1";
	private static final String KEY_ROW_DIR_EMAIL = "email";

	// TABLE_PROGRAM_PROFILE Colounm

	public static final String TABLE_COL_PROGRAM_ROW_LEVEL_NAME = "level_name";
	public static final String TABLE_COL_PROGRAM_ROW_PROGRAM_NAME = "program_name";
	public static final String TABLE_COL_PROGRAM_ROW_TERM_NAME = "term_name";



	// TABLE_STUDENT_PROFILE Colounm Names

	public static final String TABLE_COL_STUDENT_ROW_USER_ID = "user_id";
	public static final String TABLE_COL_STUDENT_ROW_USERTYPEID = "usertypeid";
	public static final String TABLE_COL_STUDENT_ROW_ORGANIZATIONID = "organizationid";
	public static final String TABLE_COL_STUDENT_ROW_USER_CODE = "user_code";
	public static final String TABLE_COL_STUDENT_ROW_FULLNAME = "fullname";
	public static final String TABLE_COL_STUDENT_ROW_CLASS_NAME = "class_name";
	public static final String TABLE_COL_STUDENT_ROW_CLASS_ROLL = "class_roll";
	public static final String TABLE_COL_STUDENT_ROW_EMAIL = "email";
	public static final String TABLE_COL_STUDENT_ROW_NAME = "name";
	public static final String TABLE_COL_STUDENT_ROW_MOBILE_NO = "mobile_no";
	public static final String TABLE_COL_STUDENT_ROW_ADDRESS = "address";
	public static final String TABLE_COL_STUDENT_ROW_RELIGION = "religion";
	public static final String TABLE_COL_STUDENT_ROW_BLOOD_GROUP = "blood_group";
	public static final String TABLE_COL_STUDENT_ROW_AILMENT = "ailment";
	public static final String TABLE_COL_STUDENT_ROW_HOSTEL = "hostel";
	public static final String TABLE_COL_STUDENT_ROW_ID = "id";
	public static final String TABLE_COL_STUDENT_ROW_TRANSPORT = "transport";
	public static final String TABLE_COL_STUDENT_ROW_DISEASE = "disease";
	public static final String TABLE_COL_STUDENT_ROW_IMAGE = "image";
	public static final String TABLE_COL_STUDENT_ROW_ALT_EMAIL = "alt_email";
	public static final String TABLE_COL_STUDENT_ROW_PIN_CODE = "pin_code";
	public static final String TABLE_COL_STUDENT_ROW_SECTION_ID = "section_id";
	public static final String TABLE_COL_STUDENT_ROW_PROGRAM = "program";
	public static final String TABLE_COL_STUDENT_ROW_IMAGE_PATH = "image_path";

	// TABLE_PARENT_PROFILE Colounms name


	public static final String TABLE_COL_PARENT_ROW_ID = "id";
	public static final String TABLE_COL_PARENT_ROW_USER_CODE = "user_code";
	public static final String TABLE_COL_PARENT_ROW_FULLNAME = "fullname";
	public static final String TABLE_COL_PARENT_ROW_EMAIL = "email";
	public static final String TABLE_COL_PARENT_ROW_MOBILE_NO = "mobile_no";
	public static final String TABLE_COL_PARENT_ROW_ADDRESS = "address";
	public static final String TABLE_COL_PARENT_ROW_ALT_EMAIL = "alt_email";
	public static final String TABLE_COL_PARENT_ROW_RELIGION = "religion";
	public static final String TABLE_COL_PARENT_ROW_BLOOD_GROUP = "blood_group";
	public static final String TABLE_COL_PARENT_ROW_IMAGE = "image";
	public static final String TABLE_COL_PARENT_ROW_PINCODE = "pincode";
	public static final String TABLE_COL_PARENT_ROW_IMAGE_PATH = "image_path";


	// TABLE_STUD_PARENT_ATTENDANCE Colounms name
	private static final String KEY_ROW_SATT_DATE ="date";
	private static final String KEY_ROW_SATT_STATUS ="status";
	private static final String KEY_ROW_SATT_SUB_NAME ="sub_name";
	private static final String KEY_ROW_SATT_LECTURE_NUM ="lecture_num";
	private static final String KEY_ROW_SATT_LATE_REASON ="late_reason";
	private static final String KEY_ROW_SATT_UID = "userid";


	// TABLE_TEACHER_PROFILE Colounms name

	public static final String TABLE_COL_TEACHER_ROW_USER_CODE="user_code";
	public static final String TABLE_COL_TEACHER_ROW_FULLNAME="fullname";
	public static final String TABLE_COL_TEACHER_ROW_EMAIL="email";
	public static final String TABLE_COL_TEACHER_ROW_MOBILE_NO="mobile_no";
	public static final String TABLE_COL_TEACHER_ROW_ADDRESS="address";
	public static final String TABLE_COL_TEACHER_ROW_ALT_EMAIL="alt_email";
	public static final String TABLE_COL_TEACHER_ROW_RELIGION="religion";
	public static final String TABLE_COL_TEACHER_ROW_IMAGE="image";
	public static final String TABLE_COL_TEACHER_ROW_PINCODE="pincode";
	public static final String TABLE_COL_TEACHER_ROW_BLOODGROUP="bloodgroup";
	public static final String TABLE_COL_TEACHER_ROW_IMAGE_PATH="image_path";






	// Common column names
	private static final String KEY_ROW_ID = "row_id";
	private static final String KEY_LESSON_DAILY_ID = "id";
	private static final String KEY_LESSON_TOPIC = "topic";
	private static final String KEY_LESSON_DATE = "date";
	private static final String KEY_LESSON_DOC = "doc";
	// TEACHER Subject Table Columns names
	private static final String KEY_SUB_NAME = "sub_name";
	private static final String KEY_T_ID = "id";
	private static final String KEY_CLASS = "class";
	private static final String KEY_SECTION_NAME = "section_name";
	private static final String KEY_CLASS_ID = "class_id";
	private static final String KEY_SECTION_ID = "section_id";
	private static final String KEY_IS_PRACTICAL = "is_practical";
	private static final String KEY_IS_PROJECT = "is_project";
	// TEACHER ASSIGNMENT Table Columns names
	private static final String KEY_TYPE = "type";
	private static final String KEY_SUBMIT_DATE = "submit_date";
	private static final String KEY_CREATED_DATE = "created_date";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_TOTAL_MARKS = "total_marks";
	private static final String KEY_MARKS = "marks";
	private static final String KEY_TITLE = "title";
	private static final String KEY_IS_MARKED = "is_marked";
	private static final String KEY_IS_PUBLISHED = "is_published";
	private static final String KEY_FILE_NAME = "file_name";
	private static final String KEY_ATTACHMENT = "attachment";
	private static final String KEY_SUBJECT_ID = "subject_id";
	private static final String KEY_COMMENTS = "comments";

	// STUDENT INFO TABLE COLUMNS NAMES
	private static final String KEY_STUDENT_ID = "student_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_ROLL_NO = "roll_no";
	private static final String KEY_STU_SUBJECT_ID = "subject_id";
	private static final String KEY_STU_SECTION_ID = "section_id";
	private static final String KEY_STU_TEACHER_ID = "teacher_id";

	// TEACHER Timetable Table Columns names
	private static final String KEY_T_ID_STUDENT_TIMETABLE = "id";
	private static final String KEY_SUB_NAME_TIMETABLE = "sub_name";
	private static final String KEY_CLASS_TIMETABLE = "class";
	private static final String KEY_SECTION_NAME_TIMETABLE = "section_name";
	private static final String KEY_SECTION_ID_TIMETABLE = "section_id";
	private static final String KEY_IS_PRACTICAL_TIMETABLE = "is_practical";
	private static final String KEY_IS_PROJECT_TIMETABLE = "is_project";
	private static final String KEY_IS_SELECTIVE_TIMETABLE = "is_selective";
	private static final String KEY_WEEK_INDEX_TIMETABLE = "week_index";
	private static final String KEY_LECTURE_NUM_TIMETABLE = "lecture_num";
	private static final String KEY_START_TIMETABLE = "start";
	private static final String KEY_END_TIMETABLE = "end";
	private static final String KEY_FACILITY_TIMETABLE = "facility";
	private static final String KEY_DAY_INDEX_TIMETABLE = "day_index";
	private static final String KEY_GROUP_ID_TIMETABLE = "group_id";
	private static final String KEY_GROUP_NAME_TIMETABLE = "group_name";
	private static final String KEY_SR_ID_TIMETABLE = "sr_id";
	private static final String KEY_TEACHER_ID_TIMETABLE = "teacher_id";
	private static final String KEY_SUBJECT_TYPE_TIMETABLE = "subject_type";
	private static final String KEY_WEEK_DAY_TIMETABLE = "week_day";

	// Teacher Lesson Plan Table Columns names
	private static final String KEY_START_DATE = "start_date";
	private static final String KEY_TOPIC = "topic";
	private static final String KEY_LESSON_ID = "id";
	private static final String KEY_END_DATE = "end_date";
	private static final String KEY_DOC = "doc";
	private static final String KEY_DAILY_SCHEDULE = "daily_schedule";

	// TEACHER Student Table Columns names
	private static final String KEY_T_ID_STUDENT = "id";
	private static final String KEY_SECTION_ID_STUDENT = "section_id";
	private static final String KEY_SUBJECT_ID_STUDENT = "subject_id";
	private static final String KEY_STUDENT_ID_STUDENT = "student_id";
	private static final String KEY_ROLL_STUDENT = "roll";
	private static final String KEY_STUDENT_NAME = "student_name";
	private static final String KEY_LEAVE_REASON = "leave_master_reasson";

	// ANNOUNCEMENT TABLE COLUMN NAMES
	private static final String KEY_SUB_CODE = "sub_code";
	private static final String KEY_TEACHER_ID = "teacher_id";
	private static final String KEY_TEACHER_NAME = "teacher";
	private static final String KEY_PROGRAM_NAME = "program_name";
	private static final String KEY_TERM_NAME = "term_name";
	private static final String KEY_SUBJECT = "subject";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_UPDATED_AT = "updated_at";
	private static final String KEY_CODE = "code";
	// TEACHER Attendance Table Columns names
	private static final String KEY_STU_ID_ATTENDANCE = "student_id";
	private static final String KEY_STUDENT_NAME_ATTENDANCE = "student_name";
	private static final String KEY_LEAVE_ATTENDANCE = "leave_master_reasson";
	private static final String KEY_ROLL_NO_ATTENDANCE = "roll_no";
	private static final String KEY_SECTION_ID_ATTENDANCE = "section_id";
	private static final String KEY_TEACHER_ID_ATTENDANCE = "teacher_id";
	private static final String KEY_SRID_ATTENDANCE = "srid";
	private static final String KEY_GROUP_ID_ATTENDANCE = "group_id";
	private static final String KEY_LECTURE_NUM_ATTENDANCE = "lecture_num";
	private static final String KEY_DATE_ATTENDANCE = "dateSelected";
	private static final String KEY_LATE_ATTENDANCE = "late";
	private static final String KEY_ATT_STATUS_ATTENDANCE = "att_status";
	private static final String KEY_SUBJECT_RELATION_ID = "subject_relation_id";



	static String CREATE_PROGRAM_PROFILE_OFFLINE = "CREATE TABLE "
			+ TABLE_PROGRAM_PROFILE + "("
			+ TABLE_COL_PROGRAM_ROW_LEVEL_NAME + " TEXT,"
			+ TABLE_COL_PROGRAM_ROW_PROGRAM_NAME + " TEXT,"
			+ TABLE_COL_PROGRAM_ROW_TERM_NAME + " TEXT)";





	static String CREATE_STUDENT_PROFILE_OFFLINE = "CREATE TABLE "
			+ TABLE_STUDENT_PROFILE + "("
			+ TABLE_COL_STUDENT_ROW_USER_ID + " TEXT," + TABLE_COL_STUDENT_ROW_USERTYPEID + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_ORGANIZATIONID + " TEXT," + TABLE_COL_STUDENT_ROW_USER_CODE + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_FULLNAME + " TEXT," + TABLE_COL_STUDENT_ROW_CLASS_NAME + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_CLASS_ROLL + " TEXT," + TABLE_COL_STUDENT_ROW_EMAIL + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_NAME + " TEXT," + TABLE_COL_STUDENT_ROW_MOBILE_NO + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_ADDRESS + " TEXT," + TABLE_COL_STUDENT_ROW_RELIGION + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_BLOOD_GROUP + " TEXT," + TABLE_COL_STUDENT_ROW_AILMENT + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_HOSTEL + " TEXT," + TABLE_COL_STUDENT_ROW_ID + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_TRANSPORT + " TEXT," + TABLE_COL_STUDENT_ROW_DISEASE + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_IMAGE + " TEXT," + TABLE_COL_STUDENT_ROW_ALT_EMAIL + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_PIN_CODE + " TEXT," + TABLE_COL_STUDENT_ROW_SECTION_ID + " TEXT,"
			+ TABLE_COL_STUDENT_ROW_PROGRAM + " TEXT," + TABLE_COL_STUDENT_ROW_IMAGE_PATH + " TEXT )";



	static String CREATE_PARENT_PROFILE_OFFLINE = "CREATE TABLE "
			+ TABLE_PARENT_PROFILE + "("
			+ TABLE_COL_PARENT_ROW_ID + " TEXT," + TABLE_COL_PARENT_ROW_USER_CODE + " TEXT,"
			+ TABLE_COL_PARENT_ROW_FULLNAME + " TEXT," + TABLE_COL_PARENT_ROW_EMAIL + " TEXT,"
			+ TABLE_COL_PARENT_ROW_MOBILE_NO + " TEXT," + TABLE_COL_PARENT_ROW_ADDRESS + " TEXT,"
			+ TABLE_COL_PARENT_ROW_ALT_EMAIL + " TEXT," + TABLE_COL_PARENT_ROW_RELIGION + " TEXT,"
			+ TABLE_COL_PARENT_ROW_BLOOD_GROUP + " TEXT," + TABLE_COL_PARENT_ROW_IMAGE + " TEXT,"
			+ TABLE_COL_PARENT_ROW_PINCODE + " TEXT," + TABLE_COL_PARENT_ROW_IMAGE_PATH + " TEXT )";



	static String CREATE_TEACHER_PROFILE_OFFLINE = "CREATE TABLE "
			+ TABLE_TEACHER_PROFILE + "("
			+ TABLE_COL_TEACHER_ROW_USER_CODE + " TEXT," + TABLE_COL_TEACHER_ROW_FULLNAME + " TEXT,"
			+ TABLE_COL_TEACHER_ROW_EMAIL + " TEXT," + TABLE_COL_TEACHER_ROW_MOBILE_NO + " TEXT,"
			+ TABLE_COL_TEACHER_ROW_ADDRESS + " TEXT," + TABLE_COL_TEACHER_ROW_ALT_EMAIL + " TEXT,"
			+ TABLE_COL_TEACHER_ROW_RELIGION + " TEXT," + TABLE_COL_TEACHER_ROW_IMAGE + " TEXT,"
			+ TABLE_COL_TEACHER_ROW_PINCODE + " TEXT," + TABLE_COL_TEACHER_ROW_BLOODGROUP + " TEXT,"
			+ TABLE_COL_TEACHER_ROW_IMAGE_PATH + " TEXT )";


	static String CREATE_STUDENT_PARENT_ATTENDANCE_TABLE = "CREATE TABLE "
			+ TABLE_STUD_PARENT_ATTENDANCE + "(" + KEY_ROW_SATT_DATE
			+ " TEXT," + KEY_ROW_SATT_STATUS + " TEXT," + KEY_ROW_SATT_SUB_NAME
			+ " TEXT," + KEY_ROW_SATT_LECTURE_NUM + " TEXT," + KEY_ROW_SATT_LATE_REASON + " TEXT,"
			+ KEY_ROW_SATT_UID + " TEXT )";



	static String CREATE_DIRECTORY_OFFLINE ="CREATE TABLE "
			+ TABLE_DIRECTORY + "("
			+ KEY_ROW_DIR_DESIGNATION + " TEXT,"
			+ KEY_ROW_DIR_EMAIL + " TEXT,"
			+ KEY_ROW_DIR_NAME + " TEXT,"
			+ KEY_ROW_DIR_PHNO + " TEXT )";

	static String CREATE_TEACHER_SUBJECT_TABLE = "CREATE TABLE "
			+ TABLE_TEACHER_SUBJECT + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_SUB_CODE + " TEXT," + KEY_SUB_NAME
			+ " TEXT," + KEY_TEACHER_NAME + " TEXT," + KEY_SUBJECT_RELATION_ID
			+ " TEXT," + KEY_PROGRAM_NAME + " TEXT," + KEY_TERM_NAME + " TEXT,"
			+ KEY_SUBJECT + " TEXT," + KEY_EMAIL + " TEXT" + ")";

	static String CREATE_TEACHER_LESSON_PLAN_TABLE = "CREATE TABLE "
			+ TABLE_TEACHER_LESSON_PLAN + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_START_DATE + " TEXT," + KEY_TOPIC
			+ " TEXT," + KEY_LESSON_ID + " TEXT," + KEY_END_DATE + " TEXT,"
			+ KEY_DOC + " TEXT," + KEY_DAILY_SCHEDULE + " TEXT" + ")";

	static String CREATE_TEACHER_LESSON_PLAN_UPLOAD_TABLE = "CREATE TABLE "
			+ TABLE_TEACHER_LESSON_PLAN_UPLOAD + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_TOPIC + " TEXT," + KEY_LESSON_ID
			+ " TEXT," + KEY_DOC + " TEXT," + KEY_FILE_NAME + " TEXT" + ")";

	static String CREATE_TEACHER_LESSON_PLAN_WEEKLY_TABLE = "CREATE TABLE "
			+ TABLE_WEEKLY_LESSON_PLAN + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_TOPIC + " TEXT," + KEY_LESSON_ID
			+ " TEXT," + KEY_DOC + " TEXT," + KEY_FILE_NAME + " TEXT" + ")";

	static String CREATE_DAILY_LESSON_PLAN_TABLE = "CREATE TABLE "
			+ TABLE_DAILY_LESSON_PLAN + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_DOC + " TEXT," + KEY_LESSON_DATE
			+ " INTEGER," + KEY_TOPIC + " TEXT," + KEY_LESSON_DAILY_ID
			+ " TEXT" + ")";

	static String CREATE_TEACHER_ASSIGNMENT_TABLE = "CREATE TABLE "
			+ TABLE_TEACHER_ASSIGNMENT + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_T_ID + " TEXT," + KEY_TYPE
			+ " INTEGER," + KEY_SUBMIT_DATE + " TEXT," + KEY_CREATED_DATE
			+ " TEXT," + KEY_SUBJECT_RELATION_ID + " TEXT," + KEY_DESCRIPTION
			+ " TEXT," + KEY_TOTAL_MARKS + " TEXT," + KEY_MARKS + " TEXT,"
			+ KEY_TITLE + " TEXT," + KEY_IS_MARKED + " TEXT,"
			+ KEY_IS_PUBLISHED + " TEXT," + KEY_FILE_NAME + " TEXT,"
			+ KEY_ATTACHMENT + " TEXT" + ")";

	static String CREATE_STUDENT_INFO_TABLE = "CREATE TABLE "
			+ TABLE_STUDENT_INFO + "(" + KEY_ROW_ID + " INTEGER PRIMARY KEY,"
			+ KEY_STUDENT_ID + " TEXT," + KEY_NAME + " TEXT," + KEY_ROLL_NO
			+ " TEXT," + KEY_STU_SECTION_ID + " TEXT," + KEY_STU_SUBJECT_ID
			+ " TEXT," + KEY_STU_TEACHER_ID + " TEXT" + ")";

	static String CREATE_TEACHER_ANNOUNCEMENT_TABLE = "CREATE TABLE "
			+ TABLE_TEACHER_ANNOUNCEMENT + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_CREATED_AT + " TEXT," + KEY_TYPE
			+ " INTEGER," + KEY_SUBJECT_RELATION_ID + " TEXT," + KEY_TITLE
			+ " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_ATTACHMENT + " TEXT,"
			+ KEY_FILE_NAME + " TEXT" + ")";

	static String CREATE_TABLE_TEACHER_STUDENT = "CREATE TABLE "
			+ TABLE_TEACHER_STUDENT + "(" + KEY_T_ID_STUDENT
			+ " INTEGER PRIMARY KEY," + KEY_SECTION_ID_STUDENT + " TEXT,"
			+ KEY_SUBJECT_ID_STUDENT + " TEXT," + KEY_STUDENT_ID_STUDENT
			+ " TEXT," + KEY_ROLL_STUDENT + " TEXT," + KEY_STUDENT_NAME
			+ " TEXT," + KEY_LEAVE_REASON + " TEXT" + ")";

	static String CREATE_TABLE_TEACHER_TIMETABLE = "CREATE TABLE "
			+ TABLE_TEACHER_TIMETABLE + "(" + KEY_T_ID_STUDENT_TIMETABLE
			+ " INTEGER PRIMARY KEY," + KEY_SUB_NAME_TIMETABLE + " TEXT,"
			+ KEY_CLASS_TIMETABLE + " TEXT," + KEY_IS_PRACTICAL_TIMETABLE
			+ " TEXT," + KEY_IS_PROJECT_TIMETABLE + " TEXT,"
			+ KEY_IS_SELECTIVE_TIMETABLE + " TEXT," + KEY_SECTION_ID_TIMETABLE
			+ " TEXT," + KEY_SECTION_NAME_TIMETABLE + " TEXT,"
			+ KEY_WEEK_INDEX_TIMETABLE + " TEXT," + KEY_LECTURE_NUM_TIMETABLE
			+ " TEXT," + KEY_START_TIMETABLE + " TEXT," + KEY_END_TIMETABLE
			+ " TEXT," + KEY_FACILITY_TIMETABLE + " TEXT,"
			+ KEY_DAY_INDEX_TIMETABLE + " TEXT," + KEY_GROUP_ID_TIMETABLE
			+ " TEXT," + KEY_GROUP_NAME_TIMETABLE + " TEXT,"
			+ KEY_SR_ID_TIMETABLE + " TEXT," + KEY_TEACHER_ID_TIMETABLE
			+ " TEXT," + KEY_SUBJECT_TYPE_TIMETABLE + " TEXT,"
			+ KEY_WEEK_DAY_TIMETABLE + " TEXT" + ")";

	static String CREATE_TABLE_TEACHER_ATTENDANCE = "CREATE TABLE "
			+ TABLE_TEACHER_ATTENDANCE + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY," + KEY_STU_ID_ATTENDANCE + " TEXT,"
			+ KEY_SECTION_ID_ATTENDANCE + " TEXT," + KEY_TEACHER_ID_ATTENDANCE
			+ " TEXT," + KEY_SRID_ATTENDANCE + " TEXT,"
			+ KEY_GROUP_ID_ATTENDANCE + " TEXT," + KEY_LECTURE_NUM_ATTENDANCE
			+ " TEXT," + KEY_DATE_ATTENDANCE + " TEXT," + KEY_LATE_ATTENDANCE
			+ " TEXT," + KEY_ATT_STATUS_ATTENDANCE + " TEXT,"
			+ KEY_STUDENT_NAME_ATTENDANCE + " TEXT," + KEY_ROLL_NO_ATTENDANCE
			+ " TEXT," + KEY_LEAVE_ATTENDANCE + " TEXT" + ")";

	public CollegeDBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new CollegeDatabaseHelper(context);
	}

	private static class CollegeDatabaseHelper extends SQLiteOpenHelper {
		CollegeDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_TEACHER_SUBJECT_TABLE);
				db.execSQL(CREATE_TEACHER_ASSIGNMENT_TABLE);
				db.execSQL(CREATE_STUDENT_INFO_TABLE);
				db.execSQL(CREATE_TABLE_TEACHER_STUDENT);
				db.execSQL(CREATE_TEACHER_ANNOUNCEMENT_TABLE);
				db.execSQL(CREATE_TABLE_TEACHER_TIMETABLE);
				db.execSQL(CREATE_TEACHER_LESSON_PLAN_TABLE);
				db.execSQL(CREATE_DAILY_LESSON_PLAN_TABLE);
				db.execSQL(CREATE_TEACHER_LESSON_PLAN_UPLOAD_TABLE);
				db.execSQL(CREATE_TEACHER_LESSON_PLAN_WEEKLY_TABLE);
				db.execSQL(CREATE_TABLE_TEACHER_ATTENDANCE);
				db.execSQL(CREATE_DIRECTORY_OFFLINE);
				db.execSQL(CREATE_STUDENT_PROFILE_OFFLINE);
				db.execSQL(CREATE_PARENT_PROFILE_OFFLINE);
				db.execSQL(CREATE_TEACHER_PROFILE_OFFLINE);
				db.execSQL(CREATE_PROGRAM_PROFILE_OFFLINE);
				db.execSQL(CREATE_STUDENT_PARENT_ATTENDANCE_TABLE);


			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER_SUBJECT);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER_ASSIGNMENT);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_INFO);
			db.execSQL("DROP TABLE IF EXISTS "
					+ CREATE_TEACHER_ANNOUNCEMENT_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TEACHER_STUDENT);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TEACHER_TIMETABLE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ CREATE_TEACHER_LESSON_PLAN_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_DAILY_LESSON_PLAN_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ CREATE_TEACHER_LESSON_PLAN_UPLOAD_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ CREATE_TEACHER_LESSON_PLAN_WEEKLY_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ CREATE_TABLE_TEACHER_ATTENDANCE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ TABLE_DIRECTORY);
			db.execSQL("DROP TABLE IF EXISTS "
					+ TABLE_STUDENT_PROFILE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ TABLE_PARENT_PROFILE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ TABLE_TEACHER_PROFILE);
			db.execSQL("DROP TABLE IF EXISTS"
			+ TABLE_PROGRAM_PROFILE);

			db.execSQL("DROP TABLE IF EXISTS "
					+ TABLE_STUD_PARENT_ATTENDANCE);

			onCreate(db);
			onUpgrade(db, oldVersion, newVersion);
		}
	}

	public CollegeDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		DBHelper.close();
	}

	public Long addLesson(TeacherSchedule schdule) {
		ContentValues values = new ContentValues();
		values.put(KEY_START_DATE, schdule.start_date);
		values.put(KEY_TOPIC, schdule.topic);
		values.put(KEY_LESSON_ID, schdule.id);
		values.put(KEY_END_DATE, schdule.end_date);
		values.put(KEY_DOC, schdule.doc);
		values.put(KEY_DAILY_SCHEDULE, schdule.daily_schedule);
		return db.insert(TABLE_TEACHER_LESSON_PLAN, null, values);
	}

	public void addAttenstatus(String student_id, String section_id,
			String teacher_id, String srid, String group_id,
			String lecture_num, String dateSelected, String late,
			int att_status, String stu_name, String roll, String leave) {

		ContentValues values = new ContentValues();
		values.put(KEY_STU_ID_ATTENDANCE, student_id);
		values.put(KEY_SECTION_ID_ATTENDANCE, section_id);
		values.put(KEY_TEACHER_ID_ATTENDANCE, teacher_id);
		values.put(KEY_SRID_ATTENDANCE, srid);
		values.put(KEY_GROUP_ID_ATTENDANCE, group_id);
		values.put(KEY_LECTURE_NUM_ATTENDANCE, lecture_num);
		values.put(KEY_DATE_ATTENDANCE, dateSelected);
		values.put(KEY_LATE_ATTENDANCE, late);
		values.put(KEY_ATT_STATUS_ATTENDANCE, att_status);
		values.put(KEY_STUDENT_NAME_ATTENDANCE, stu_name);
		values.put(KEY_ROLL_NO_ATTENDANCE, roll);
		values.put(KEY_LEAVE_ATTENDANCE, leave);

		// Inserting Row
		db.insert(TABLE_TEACHER_ATTENDANCE, null, values);
	}

	public Long addLessonUpload(TeacherSchedule schdule) {
		ContentValues values = new ContentValues();
		// values.put(KEY_TYPE, type);
		values.put(KEY_TOPIC, schdule.topic);
		values.put(KEY_LESSON_ID, schdule.id);
		values.put(KEY_DOC, schdule.doc);
		values.put(KEY_FILE_NAME, schdule.file);
		// values.put(KEY_DAILY_SCHEDULE , schdule.daily_schedule);
		return db.insert(TABLE_TEACHER_LESSON_PLAN_UPLOAD, null, values);
	}

	public void addDirectory(CDirectory mdirectory){
		ContentValues dirvalues = new ContentValues();
		dirvalues.put(KEY_ROW_DIR_DESIGNATION,mdirectory.designition);
		dirvalues.put(KEY_ROW_DIR_EMAIL,mdirectory.email);
		dirvalues.put(KEY_ROW_DIR_NAME,mdirectory.name);
		dirvalues.put(KEY_ROW_DIR_PHNO, mdirectory.phone1);
		long row_entry = db.insert(TABLE_DIRECTORY, null, dirvalues);
	}



	public void addStudentParentAttendance(StatusStudent status, String userid)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_ROW_SATT_DATE, status.date);
		values.put(KEY_ROW_SATT_STATUS, status.status);
		values.put(KEY_ROW_SATT_SUB_NAME, status.subject);
		values.put(KEY_ROW_SATT_LECTURE_NUM, status.lecture);
		values.put(KEY_ROW_SATT_LATE_REASON , status.late_reason);
		values.put(KEY_ROW_SATT_UID , userid );
		db.insert(TABLE_STUD_PARENT_ATTENDANCE, null, values);

	}


	public void addProgramProfile(Program stdProgram){
		ContentValues values = new ContentValues();
		values.put(TABLE_COL_PROGRAM_ROW_LEVEL_NAME, stdProgram.level_name);
		values.put(TABLE_COL_PROGRAM_ROW_PROGRAM_NAME, stdProgram.program_name);
		values.put(TABLE_COL_PROGRAM_ROW_TERM_NAME, stdProgram.term_name);
		db.insert(TABLE_PROGRAM_PROFILE,null,values);

	}



	public void addStudentProfile(StudentProfileInfo stdProfile){
		Long test;
		ContentValues values = new ContentValues();
		values.put(TABLE_COL_STUDENT_ROW_USER_ID, stdProfile.user_id);
		values.put(TABLE_COL_STUDENT_ROW_USERTYPEID, stdProfile.usertypeid);
		values.put(TABLE_COL_STUDENT_ROW_ORGANIZATIONID, stdProfile.organizationid);
		values.put(TABLE_COL_STUDENT_ROW_USER_CODE, stdProfile.user_code);
		values.put(TABLE_COL_STUDENT_ROW_FULLNAME, stdProfile.fullname);
		values.put(TABLE_COL_STUDENT_ROW_CLASS_NAME, stdProfile.class_name);
		values.put(TABLE_COL_STUDENT_ROW_CLASS_ROLL, stdProfile.class_roll);
		values.put(TABLE_COL_STUDENT_ROW_EMAIL, stdProfile.email);
		values.put(TABLE_COL_STUDENT_ROW_NAME, stdProfile.name);
		values.put(TABLE_COL_STUDENT_ROW_MOBILE_NO, stdProfile.mobile_no);
		values.put(TABLE_COL_STUDENT_ROW_ADDRESS, stdProfile.address);
		values.put(TABLE_COL_STUDENT_ROW_RELIGION, stdProfile.religion);
		values.put(TABLE_COL_STUDENT_ROW_BLOOD_GROUP, stdProfile.blood_group);
		values.put(TABLE_COL_STUDENT_ROW_AILMENT, stdProfile.ailment);
		values.put(TABLE_COL_STUDENT_ROW_HOSTEL, stdProfile.hostel);
		values.put(TABLE_COL_STUDENT_ROW_ID, stdProfile.id);
		values.put(TABLE_COL_STUDENT_ROW_TRANSPORT, stdProfile.transport);
		values.put(TABLE_COL_STUDENT_ROW_DISEASE, stdProfile.disease);
		values.put(TABLE_COL_STUDENT_ROW_IMAGE, stdProfile.image);
		values.put(TABLE_COL_STUDENT_ROW_ALT_EMAIL, stdProfile.alt_email);
		values.put( TABLE_COL_STUDENT_ROW_PIN_CODE, stdProfile.pin_code);
		values.put(TABLE_COL_STUDENT_ROW_SECTION_ID, stdProfile.section_id);
		values.put(TABLE_COL_STUDENT_ROW_PROGRAM, stdProfile.program);
		values.put( TABLE_COL_STUDENT_ROW_IMAGE_PATH, stdProfile.image_path);
		test = db.insert(TABLE_STUDENT_PROFILE,null,values);
	}



	public void addParentProfile(ParentProfileInfo prntProfile){
		ContentValues values = new ContentValues();
		values.put(TABLE_COL_PARENT_ROW_ID, prntProfile.id);
		values.put(TABLE_COL_PARENT_ROW_USER_CODE, prntProfile.user_code);
		values.put(TABLE_COL_PARENT_ROW_FULLNAME, prntProfile.fullname);
		values.put(TABLE_COL_PARENT_ROW_EMAIL, prntProfile.email);
		values.put(TABLE_COL_PARENT_ROW_MOBILE_NO, prntProfile.mobile_no);
		values.put(TABLE_COL_PARENT_ROW_ADDRESS, prntProfile.address);
		values.put(TABLE_COL_PARENT_ROW_ALT_EMAIL, prntProfile.alt_email);
		values.put(TABLE_COL_PARENT_ROW_RELIGION, prntProfile.religion);
		values.put(TABLE_COL_PARENT_ROW_BLOOD_GROUP, prntProfile.blood_group);
		values.put(TABLE_COL_PARENT_ROW_IMAGE, prntProfile.image);
		values.put(TABLE_COL_PARENT_ROW_PINCODE, prntProfile.pincode);
		values.put(TABLE_COL_PARENT_ROW_IMAGE_PATH, prntProfile.image_path);
		db.insert(TABLE_PARENT_PROFILE,null,values);

	}



	public void addTeacherProfile(TeacherProfileInfo tchProfile){
		ContentValues values = new ContentValues();
		values.put(TABLE_COL_TEACHER_ROW_USER_CODE, tchProfile.user_code);
		values.put(TABLE_COL_TEACHER_ROW_FULLNAME, tchProfile.fullname);
		values.put(TABLE_COL_TEACHER_ROW_EMAIL, tchProfile.email);
		values.put(TABLE_COL_TEACHER_ROW_MOBILE_NO, tchProfile.mobile_no);
		values.put(TABLE_COL_TEACHER_ROW_ADDRESS, tchProfile.address);
		values.put(TABLE_COL_TEACHER_ROW_ALT_EMAIL, tchProfile.alt_email);
		values.put(TABLE_COL_TEACHER_ROW_RELIGION, tchProfile.religion);
		values.put(TABLE_COL_TEACHER_ROW_IMAGE, tchProfile.image);
		values.put(TABLE_COL_TEACHER_ROW_PINCODE, tchProfile.pincode);
		values.put(TABLE_COL_TEACHER_ROW_BLOODGROUP, tchProfile.bloodgroup);
		values.put(TABLE_COL_TEACHER_ROW_IMAGE_PATH, tchProfile.image_path);
		long i =  db.insert(TABLE_TEACHER_PROFILE,null,values);

	}



	public Long addLessonWeekly(TeacherSchedule schdule) {
		ContentValues values = new ContentValues();
		// values.put(KEY_TYPE, type);
		values.put(KEY_TOPIC, schdule.topic);
		values.put(KEY_LESSON_ID, schdule.id);
		values.put(KEY_DOC, schdule.doc);
		values.put(KEY_FILE_NAME, schdule.file);
		// values.put(KEY_DAILY_SCHEDULE , schdule.daily_schedule);
		return db.insert(TABLE_WEEKLY_LESSON_PLAN, null, values);
	}

	public void addLessonDaily(TeacherSchedule schdule) {
		ContentValues values = new ContentValues();
		values.put(KEY_LESSON_DOC, schdule.doc);
		values.put(KEY_LESSON_DATE, schdule.date);
		values.put(KEY_LESSON_TOPIC, schdule.topic);
		values.put(KEY_LESSON_DAILY_ID, schdule.id);
		db.insert(TABLE_DAILY_LESSON_PLAN, null, values);
	}

	public void addStudents(Attendance student) {

		ContentValues values = new ContentValues();
		values.put(KEY_SECTION_ID_STUDENT, student.section_id);
		values.put(KEY_SUBJECT_ID_STUDENT, student.sub_id);
		values.put(KEY_STUDENT_ID_STUDENT, student.student_id);
		values.put(KEY_ROLL_STUDENT, student.roll_no);
		values.put(KEY_STUDENT_NAME, student.student_name);
		values.put(KEY_LEAVE_REASON, student.leave_master_reason);

		// Inserting Row
		db.insert(TABLE_TEACHER_STUDENT, null, values);
	}

	// insert Subjects
	public void addSubjects(Subject subject) {

		ContentValues values = new ContentValues();
		// values.put(KEY_ROW_ID, subject.getRowId());
		values.put(KEY_SUB_CODE, subject.sub_code);
		values.put(KEY_SUB_NAME, subject.sub_name);
		values.put(KEY_TEACHER_NAME, subject.teacher);
		values.put(KEY_SUBJECT_RELATION_ID, subject.subject_relation_id);
		values.put(KEY_PROGRAM_NAME, subject.section_name);
		values.put(KEY_TERM_NAME, subject.classs);
		values.put(KEY_SUBJECT, subject.subject);
		values.put(KEY_EMAIL, subject.email);
		// Inserting Row
		db.insert(TABLE_TEACHER_SUBJECT, null, values);
	}

	// insert Timetable
	public void addTimetable(TimeTable time) {

		ContentValues values = new ContentValues();
		// values.put(KEY_ROW_ID, subject.getRowId());
		values.put(KEY_SUB_NAME_TIMETABLE, time.subject);
		values.put(KEY_CLASS_TIMETABLE, time.classs);
		values.put(KEY_IS_PRACTICAL_TIMETABLE, time.is_practical);
		values.put(KEY_IS_PROJECT_TIMETABLE, time.is_project);
		values.put(KEY_IS_SELECTIVE_TIMETABLE, time.is_practical);
		values.put(KEY_SECTION_ID_TIMETABLE, time.section_id);
		values.put(KEY_SECTION_NAME_TIMETABLE, time.section);
		values.put(KEY_WEEK_INDEX_TIMETABLE, "");
		values.put(KEY_LECTURE_NUM_TIMETABLE, time.lecture_num);
		values.put(KEY_START_TIMETABLE, time.start);
		values.put(KEY_END_TIMETABLE, time.end);
		values.put(KEY_FACILITY_TIMETABLE, time.facility);
		values.put(KEY_DAY_INDEX_TIMETABLE, time.day_index);
		values.put(KEY_GROUP_ID_TIMETABLE, time.group_id);
		values.put(KEY_GROUP_NAME_TIMETABLE, time.group_name);
		values.put(KEY_SR_ID_TIMETABLE, time.sr_id);
		values.put(KEY_TEACHER_ID_TIMETABLE, time.teacher_id);
		values.put(KEY_SUBJECT_TYPE_TIMETABLE, time.subject_type);
		values.put(KEY_WEEK_DAY_TIMETABLE, time.week_day);

		// Inserting Row
		db.insert(TABLE_TEACHER_TIMETABLE, null, values);
	}

	public void addStudentInfo(Student student) {

		ContentValues values = new ContentValues();
		// values.put(KEY_ROW_ID, subject.getRowId());
		values.put(KEY_STUDENT_ID, student.id);
		values.put(KEY_NAME, student.name);
		values.put(KEY_ROLL_NO, student.roll_no);
		values.put(KEY_STU_SECTION_ID, student.section_id);
		values.put(KEY_STU_SUBJECT_ID, student.subject_id);
		values.put(KEY_STU_TEACHER_ID, student.teacher_id);
		// Inserting Row
		db.insert(TABLE_STUDENT_INFO, null, values);
	}

	// Insert Assignments, Quiz, Activity
	public Long addAssignment(Assignment assignments, int type) {

		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, type);
		values.put(KEY_SUBMIT_DATE, assignments.submit_date);
		values.put(KEY_CREATED_DATE, assignments.created_date);
		values.put(KEY_SUBJECT_RELATION_ID, assignments.subject_relation_id);
		values.put(KEY_DESCRIPTION, assignments.description);
		values.put(KEY_TOTAL_MARKS, assignments.total_marks);
		values.put(KEY_MARKS, assignments.marks);
		values.put(KEY_TITLE, assignments.title);
		values.put(KEY_IS_MARKED, assignments.is_marked);
		values.put(KEY_IS_PUBLISHED, assignments.is_published);
		values.put(KEY_FILE_NAME, assignments.file_name);
		values.put(KEY_ATTACHMENT, assignments.attachment);
		// Inserting Row
		return db.insert(TABLE_TEACHER_ASSIGNMENT, null, values);
	}

	// Insert Announcements & Course work
	public Long addAnnouncement(Announcement announcement, int type) {

		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, type);
		values.put(KEY_CREATED_AT, announcement.date);
		values.put(KEY_SUBJECT_RELATION_ID, announcement.subject_relation_id);
		values.put(KEY_TITLE, announcement.title);
		values.put(KEY_DESCRIPTION, announcement.description);
		values.put(KEY_ATTACHMENT, announcement.attachment);
		values.put(KEY_FILE_NAME, announcement.file_name);
		// Inserting Row
		return db.insert(TABLE_TEACHER_ANNOUNCEMENT, null, values);
	}

	// Getting All Timetable

	public ArrayList<TimeTable> getAllTimetable() {
		ArrayList<TimeTable> timetableList = new ArrayList<TimeTable>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_TIMETABLE;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			TimeTable subject = new TimeTable();
			subject.subject = cursor.getString(1);
			subject.classs = cursor.getString(2);
			subject.is_practical = cursor.getString(3);
			subject.is_project = cursor.getString(4);
			subject.is_selective = cursor.getString(5);
			subject.section_id = cursor.getString(6);
			subject.section = cursor.getString(7);
			subject.week_index = cursor.getString(8);
			subject.lecture_num = cursor.getString(9);
			subject.start = cursor.getString(10);
			subject.end = cursor.getString(11);
			subject.facility = cursor.getString(12);
			subject.day_index = cursor.getString(13);
			subject.group_id = cursor.getString(14);
			subject.group_name = cursor.getString(15);
			subject.sr_id = cursor.getString(16);
			subject.teacher_id = cursor.getString(17);
			subject.subject_type = cursor.getString(18);
			subject.week_day = cursor.getString(19);

			// Adding contact to list
			timetableList.add(subject);
			cursor.moveToNext();
		}
		// return contact list
		return timetableList;
	}




	public Cursor getProgramProfileInfo(){
		String query = "SELECT * FROM "+ TABLE_PROGRAM_PROFILE ;
		Cursor cursor = db.rawQuery(query, null);
		return cursor;

	}

	public Cursor getStudentProfileInfo(){
		String query = "SELECT * FROM "+ TABLE_STUDENT_PROFILE ;
		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}


	public Cursor getParentProfileInfo(){
		String query = "SELECT * FROM "+ TABLE_PARENT_PROFILE ;
		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}

	public Cursor getTeacherProfileInfo() {
		String query = "SELECT * FROM " + TABLE_TEACHER_PROFILE;
		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}



	public ArrayList<StatusStudent> getAttendanceByDate(String date, String uid){
		ArrayList<StatusStudent> st = new ArrayList<StatusStudent>();

		String query = "SELECT * FROM "+ TABLE_STUD_PARENT_ATTENDANCE + " where "+ KEY_ROW_SATT_DATE +" = '"+ date+
				"' AND " + KEY_ROW_SATT_UID + " = '" + uid + "'";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();

		while(cursor.isAfterLast() == false){
			StatusStudent status = new StatusStudent();
			status.date = cursor.getString(cursor.getColumnIndex(KEY_ROW_SATT_DATE));
			status.late_reason = cursor.getString(cursor.getColumnIndex(KEY_ROW_SATT_LATE_REASON));
			status.status = cursor.getString(cursor.getColumnIndex(KEY_ROW_SATT_STATUS));
			status.lecture = cursor.getString(cursor.getColumnIndex(KEY_ROW_SATT_LECTURE_NUM));
			status.subject = cursor.getString(cursor.getColumnIndex(KEY_ROW_SATT_SUB_NAME));
			st.add(status);
			cursor.moveToNext();

		}
		return  st;
	}




	public ArrayList<CDirectory> getAllDirectory()
	{
		ArrayList<CDirectory> mDirectoryList = new ArrayList<CDirectory>();
		String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORY;
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {
			CDirectory mDir = new CDirectory();
			mDir.name = cursor.getString(2);
			mDir.designition = cursor.getString(0);
			mDir.phone1 = cursor.getString(3);
			mDir.email = cursor.getString(1);

			mDirectoryList.add(mDir);
			cursor.moveToNext();
		}
		return mDirectoryList;

	}

	// Getting All Attendance

	public ArrayList<Attendance> getAllAttendanceforUpload(String lecture_num) {
		ArrayList<Attendance> attendance = new ArrayList<Attendance>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ATTENDANCE
				+ " WHERE " + lecture_num;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Attendance student = new Attendance();
			student.student_id = cursor.getString(1);
			Log.d("student_id", cursor.getString(1));
			student.section_id = cursor.getString(2);
			Log.d("section_id", cursor.getString(2));
			student.teacher_id = cursor.getString(3);
			Log.d("teacher_id", cursor.getString(3));
			student.sr_id = cursor.getString(4);
			Log.d("sr_id", cursor.getString(4));
			student.group_id = cursor.getString(5);
			Log.d("group_id", cursor.getString(5));
			student.lecture_num = cursor.getString(6);
			Log.d("lecture_num", cursor.getString(6));
			student.date = cursor.getString(7);
			Log.d("date", cursor.getString(7));
			student.status = cursor.getString(9);
			Log.d("status", cursor.getString(9));
			student.late = cursor.getString(8);
			Log.d("late", cursor.getString(8));
			student.student_name = cursor.getString(10);
			Log.d("student_name", cursor.getString(10));
			student.roll_no = cursor.getString(11);
			Log.d("roll_no", cursor.getString(11));
			student.leave_master_reason = cursor.getString(12);
			Log.d("leave_master_reason", cursor.getString(12));
			// Adding contact to list
			attendance.add(student);
			cursor.moveToNext();
		}
		// return contact list
		return attendance;
	}

	// Getting list of lesson
	public ArrayList<TeacherSchedule> getAllDailyLessonPlan(String subject_id,
			String section_id) {
		ArrayList<TeacherSchedule> lessonList = new ArrayList<TeacherSchedule>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DAILY_LESSON_PLAN;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			TeacherSchedule schdule = new TeacherSchedule();

			schdule.row_id = Integer.parseInt(cursor.getString(0));

			schdule.doc = cursor.getString(1);
			// Log.d("file in DB", cursor.getString(1));
			schdule.date = cursor.getString(2);
			// Log.d("date in DB", cursor.getString(2));
			schdule.topic = cursor.getString(3);
			// Log.d("topic in DB", cursor.getString(3));
			schdule.id = cursor.getString(4);
			// Log.d("id in DB", cursor.getString(4));
			// Adding contact to list
			lessonList.add(schdule);
			cursor.moveToNext();
		}
		// return contact list
		return lessonList;
	}

	public ArrayList<TeacherSchedule> getAllLessonPlan(String subject_id,
			String section_id) {
		ArrayList<TeacherSchedule> lessonList = new ArrayList<TeacherSchedule>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_LESSON_PLAN;
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			TeacherSchedule schdule = new TeacherSchedule();
			schdule.row_id = Integer.parseInt(cursor.getString(0));
			schdule.start_date = cursor.getString(1);
			// Log.d("start_date in DB", cursor.getString(1));
			schdule.topic = cursor.getString(2);
			// Log.d("topic in DB", cursor.getString(2));
			schdule.id = cursor.getString(3);
			// Log.d("id in DB", cursor.getString(3));
			schdule.end_date = cursor.getString(4);
			// Log.d("end_date in DB", cursor.getString(4));
			schdule.doc = cursor.getString(5);
			// Log.d("doc in DB", cursor.getString(5));
			schdule.daily_schedule = cursor.getString(6);
			// Log.d("daily_schedule", cursor.getString(6));
			// Adding contact to list
			lessonList.add(schdule);
			cursor.moveToNext();
		}
		// return contact list
		return lessonList;
	}

	// Getting single subject
	public Subject getSubject(String id) {

		Cursor cursor = db.query(TABLE_TEACHER_SUBJECT, new String[] {
				KEY_ROW_ID, KEY_SUB_CODE, KEY_SUB_NAME, KEY_TEACHER_NAME,
				KEY_SUBJECT_RELATION_ID, KEY_PROGRAM_NAME, KEY_TERM_NAME,
				KEY_SUBJECT, KEY_EMAIL }, KEY_SUBJECT_RELATION_ID + "=?",
				new String[] { id }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Subject subject = new Subject();
		subject.row_id = Integer.parseInt(cursor.getString(0));
		subject.sub_code = cursor.getString(1);
		subject.sub_name = cursor.getString(2);
		subject.teacher = cursor.getString(3);
		subject.subject_relation_id = cursor.getString(4);
		subject.section_name = cursor.getString(5);
		subject.classs = cursor.getString(6);
		subject.subject = cursor.getString(7);
		subject.email = cursor.getString(8);

		cursor.close();
		return subject;
	}

	// Getting single subject
	public Assignment getAssignment(int row_id, int type) {

		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ASSIGNMENT
				+ " WHERE " + KEY_TYPE + "=" + type + " AND " + KEY_ROW_ID
				+ "=" + row_id;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();
		Assignment assign = new Assignment();
		assign.row_id = Integer.parseInt(cursor.getString(0));
		assign.id = cursor.getString(1);
		assign.submit_date = cursor.getString(3);
		assign.created_date = cursor.getString(4);
		assign.subject_relation_id = cursor.getString(5);
		assign.description = cursor.getString(6);
		assign.total_marks = cursor.getString(7);
		assign.marks = cursor.getString(8);
		assign.title = cursor.getString(9);
		assign.is_marked = cursor.getString(10);
		assign.is_published = cursor.getString(11);
		assign.file_name = cursor.getString(12);
		assign.attachment = cursor.getString(13);
		
		cursor.close();
		return assign;
	}

	public TeacherSchedule getLessonPLan(String section_id, String subject_id) {

		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ASSIGNMENT
				+ " WHERE " + KEY_STU_SECTION_ID + "=" + section_id + " AND "
				+ KEY_STU_SUBJECT_ID;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();
		TeacherSchedule assign = new TeacherSchedule();
		assign.row_id = Integer.parseInt(cursor.getString(0));
		assign.start_date = cursor.getString(1);
		assign.topic = cursor.getString(2);
		assign.id = cursor.getString(3);
		assign.end_date = cursor.getString(4);
		assign.doc = cursor.getString(5);

		cursor.close();
		return assign;
	}

	// Getting single student info
	public Student getStudInfo(String section_id, String subject_id,
			String teacher_id) {

		String selectQuery = "SELECT  * FROM " + TABLE_STUDENT_INFO + " WHERE "
				+ KEY_STU_SECTION_ID + "=" + section_id + " AND "
				+ KEY_STU_SUBJECT_ID + "=" + subject_id + " AND "
				+ KEY_STU_TEACHER_ID + "=" + teacher_id;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();
		Student info = new Student();
		info.row_id = Integer.parseInt(cursor.getString(0));
		info.id = cursor.getString(1);
		info.name = cursor.getString(3);
		info.roll_no = cursor.getString(4);
		info.total_marks = cursor.getString(5);
		info.comment = cursor.getString(6);

		cursor.close();
		return info;
	}

	// Getting single course work
	public Announcement getAnnouncement(int row_id, int type) {

		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ANNOUNCEMENT
				+ " WHERE " + KEY_TYPE + "=" + type + " AND " + KEY_ROW_ID
				+ "=" + row_id;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();
		Announcement announcement = new Announcement();
		announcement.row_id = Integer.parseInt(cursor.getString(0));
		announcement.date = cursor.getString(1);
		announcement.subject_relation_id = cursor.getString(2);
		announcement.title = cursor.getString(4);
		announcement.description = cursor.getString(5);
		announcement.attachment = cursor.getString(6);
		announcement.file_name = cursor.getString(7);
		cursor.close();
		return announcement;
	}

	// Getting All Subjects
	public ArrayList<Subject> getAllSubject() {
		ArrayList<Subject> subjectList = new ArrayList<Subject>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_SUBJECT;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Subject subject = new Subject();
			subject.row_id = Integer.parseInt(cursor.getString(0));
			subject.sub_code = cursor.getString(1);
			subject.sub_name = cursor.getString(2);
			subject.teacher = cursor.getString(3);
			subject.subject_relation_id = cursor.getString(4);
			subject.section_name = cursor.getString(5);
			subject.classs = cursor.getString(6);
			subject.subject = cursor.getString(7);
			subject.email = cursor.getString(8);

			// Adding contact to list
			subjectList.add(subject);
			cursor.moveToNext();
		}
		cursor.close();
		// return contact list
		return subjectList;
	}

	// Getting All Class
	public ArrayList<TeacherSubjectByTimeTable> getAllSubjectforattendance(
			String day) {
		ArrayList<TeacherSubjectByTimeTable> subjectList = new ArrayList<TeacherSubjectByTimeTable>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_TIMETABLE
				+ " WHERE WEEK_DAY='" + day + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			TeacherSubjectByTimeTable subject = new TeacherSubjectByTimeTable();
			subject.sub_name = cursor.getString(1);
			subject.lecture_num = cursor.getString(9);
			subject.classname = cursor.getString(2);
			subject.section_name = cursor.getString(7);
			subject.section_id = cursor.getString(6);
			subject.srid = cursor.getString(16);
			subject.teacher_id = cursor.getString(17);
			subject.group_id = cursor.getString(14);
			// Adding contact to list
			subjectList.add(subject);
			cursor.moveToNext();
		}
		// return contact list
		return subjectList;
	}

	// Getting All Assignments
	public ArrayList<Assignment> getAllAssignments(int type, String subject_id) {
		ArrayList<Assignment> assignmentList = new ArrayList<Assignment>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ASSIGNMENT
				+ " WHERE " + KEY_TYPE + "=" + type + " AND "
				+ KEY_SUBJECT_RELATION_ID + "=" + subject_id;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Assignment assign = new Assignment();
			assign.row_id = Integer.parseInt(cursor.getString(0));
			assign.id = cursor.getString(1);
			assign.submit_date = cursor.getString(3);
			assign.created_date = cursor.getString(4);
			assign.subject_relation_id = cursor.getString(5);
			assign.description = cursor.getString(6);
			assign.total_marks = cursor.getString(7);
			assign.marks = cursor.getString(8);
			assign.title = cursor.getString(9);
			assign.is_marked = cursor.getString(10);
			assign.is_published = cursor.getString(11);
			assign.file_name = cursor.getString(12);
			assign.attachment = cursor.getString(13);
		
			// Adding contact to list
			assignmentList.add(assign);
			cursor.moveToNext();
		}
		cursor.close();
		// return contact list
		return assignmentList;
	}

	public ArrayList<Attendance> getAllAttendance(String id) {
		ArrayList<Attendance> subjectList = new ArrayList<Attendance>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_STUDENT
				+ " WHERE subject_id='" + id + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Attendance attendance = new Attendance();
			attendance.section_id = cursor.getString(1);
			attendance.sub_id = cursor.getString(2);
			attendance.student_id = cursor.getString(3);
			attendance.roll_no = cursor.getString(4);
			attendance.student_name = cursor.getString(5);
			attendance.leave_master_reason = cursor.getString(6);

			// Adding contact to list
			subjectList.add(attendance);
			cursor.moveToNext();
		}
		// return contact list
		return subjectList;
	}

	// Getting All Announcements
	public ArrayList<Announcement> getAllAnnouncements(int type,
			String subject_id) {
		ArrayList<Announcement> announcementList = new ArrayList<Announcement>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ANNOUNCEMENT
				+ " WHERE " + KEY_TYPE + "=" + type + " AND "
				+ KEY_SUBJECT_RELATION_ID + "=" + subject_id;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Announcement announcement = new Announcement();
			announcement.row_id = Integer.parseInt(cursor.getString(0));
			announcement.date = cursor.getString(1);
			announcement.subject_relation_id = cursor.getString(2);
			announcement.title = cursor.getString(4);
			announcement.description = cursor.getString(5);
			announcement.attachment = cursor.getString(6);
			announcement.file_name = cursor.getString(7);
			announcementList.add(announcement);
			cursor.moveToNext();
		}
		cursor.close();
		// return contact list
		return announcementList;
	}

	// Getting all student info
	public ArrayList<Student> getAllStudentInfo(String student_id,
			String roll_no) {
		ArrayList<Student> studentList = new ArrayList<Student>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ASSIGNMENT
				+ " WHERE " + KEY_STUDENT_ID + "=" + student_id + " AND "
				+ KEY_ROLL_NO + "=" + roll_no;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Student info = new Student();
			info.row_id = Integer.parseInt(cursor.getString(0));
			info.id = cursor.getString(1);
			info.name = cursor.getString(2);
			info.roll_no = cursor.getString(3);
			info.section_id = cursor.getString(4);
			info.subject_id = cursor.getString(5);
			info.teacher_id = cursor.getString(6);
			// Adding contact to list
			studentList.add(info);
			cursor.moveToNext();
		}
		cursor.close();
		// return contact list
		return studentList;
	}

	// Getting All Assignment Table data for upload
	public ArrayList<Assignment> getAllAssignmentsForUpload() {
		ArrayList<Assignment> assignmentList = new ArrayList<Assignment>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ASSIGNMENT;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Assignment assign = new Assignment();
			assign.row_id = Integer.parseInt(cursor.getString(0));
			assign.id = cursor.getString(1);
			assign.type = cursor.getInt(2);
			assign.submit_date = cursor.getString(3);
			assign.created_date = cursor.getString(4);
			assign.subject_relation_id = cursor.getString(5);
			assign.description = cursor.getString(6);
			assign.total_marks = cursor.getString(7);
			assign.marks = cursor.getString(8);
			assign.title = cursor.getString(9);
			assign.is_marked = cursor.getString(10);
			assign.is_published = cursor.getString(11);
			assign.file_name = cursor.getString(12);
			assign.attachment = cursor.getString(13);
		
			// Adding contact to list
			assignmentList.add(assign);
			cursor.moveToNext();
		}
		cursor.close();
		// return contact list
		return assignmentList;
	}

	// getting all announcement table for uploading
	public ArrayList<Announcement> getAllAnnouncementforUpload() {
		ArrayList<Announcement> announcementList = new ArrayList<Announcement>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEACHER_ANNOUNCEMENT;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			Announcement announcement = new Announcement();
			announcement.row_id = Integer.parseInt(cursor.getString(0));
			announcement.type = cursor.getString(2);
			announcement.date = cursor.getString(1);
			announcement.subject_relation_id = cursor.getString(3);
			announcement.title = cursor.getString(4);
			announcement.description = cursor.getString(5);
			announcement.attachment = cursor.getString(6);
			announcement.file_name = cursor.getString(7);
			announcementList.add(announcement);
			cursor.moveToNext();
		}
		cursor.close();
		// return contact list
		return announcementList;
	}

	public ArrayList<TeacherSchedule> getUploadLessonPlan() {
		ArrayList<TeacherSchedule> lessonList = new ArrayList<TeacherSchedule>();
		// Select All Query
		String selectQuery = "SELECT  * FROM "
				+ TABLE_TEACHER_LESSON_PLAN_UPLOAD;
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			TeacherSchedule schdule = new TeacherSchedule();
			schdule.row_id = Integer.parseInt(cursor.getString(0));

			schdule.topic = cursor.getString(1);
			// Log.d("start_date in DB", cursor.getString(1));
			schdule.id = cursor.getString(2);
			// Log.d("topic in DB", cursor.getString(2));
			schdule.doc = cursor.getString(3);
			// Log.d("id in DB", cursor.getString(3));
			schdule.file = cursor.getString(4);
			// Log.d("end_date in DB", cursor.getString(4));
			// schdule.doc = cursor.getString(5);
			// Log.d("doc in DB", cursor.getString(5));

			/*
			 * * schdule.daily_schedule = cursor.getString(6);
			 * Log.d("daily_schedule", cursor.getString(6));
			 */
			// Adding contact to list
			lessonList.add(schdule);
			cursor.moveToNext();
		}
		// return contact list
		return lessonList;
	}

	public ArrayList<TeacherSchedule> getWeeklyLessonPlan() {
		ArrayList<TeacherSchedule> lessonList = new ArrayList<TeacherSchedule>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_WEEKLY_LESSON_PLAN;
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			TeacherSchedule schdule = new TeacherSchedule();
			schdule.row_id = Integer.parseInt(cursor.getString(0));
			schdule.topic = cursor.getString(1);
			// Log.d("start_date in DB", cursor.getString(1));
			schdule.id = cursor.getString(2);
			// Log.d("topic in DB", cursor.getString(2));
			schdule.doc = cursor.getString(3);
			// Log.d("id in DB", cursor.getString(3));
			schdule.file = cursor.getString(4);
			// Adding contact to list
			lessonList.add(schdule);
			cursor.moveToNext();
		}
		// return contact list
		return lessonList;
	}

	// Getting All lecture_num Attendance
	public ArrayList<String> getAlllectureAttendance() {
		ArrayList<String> attendance_lecture = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT DISTINCT " + KEY_LECTURE_NUM_ATTENDANCE
				+ " FROM " + TABLE_TEACHER_ATTENDANCE;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {// Adding contact to list
			attendance_lecture.add(cursor.getString(cursor
					.getColumnIndex(KEY_LECTURE_NUM_ATTENDANCE)));
			Log.d("lecture_num", cursor.getString(cursor
					.getColumnIndex(KEY_LECTURE_NUM_ATTENDANCE)));
			cursor.moveToNext();
		}
		cursor.close();
		// return contact list
		return attendance_lecture;
	}

	public void deleteAllStudents() {
		db.delete(TABLE_TEACHER_STUDENT, null, null);
	}

	public void deleteAllDirectory()
	{
		db.delete(TABLE_DIRECTORY,null,null);
	}

	public void deleteAllLessonPlan() {
		db.delete(TABLE_TEACHER_LESSON_PLAN_UPLOAD, null, null);
	}

	// Deleting all subject
	public void deleteAllSubjects() {
		db.delete(TABLE_TEACHER_SUBJECT, null, null);
	}


	public void deleteAllStudentProfile(){
		db.delete(TABLE_STUDENT_PROFILE,null,null);
	}
	public void deleteAllParentProfile(){
		db.delete(TABLE_PARENT_PROFILE,null,null);
	}
	public void deleteAllTeacherProfile(){
		db.delete(TABLE_TEACHER_PROFILE,null,null);
	}
	public void deleteAllProgramProfile(){
		db.delete(TABLE_PROGRAM_PROFILE,null,null);
	}

	public void deleteStudentParentAttendance(){
		db.delete(TABLE_STUD_PARENT_ATTENDANCE, null, null);
	}


	public void deleteAllTimetable() {
		db.delete(TABLE_TEACHER_TIMETABLE, null, null);
	}


	public int getStudetParentAttendanceCount(String date, String userid){
		String query = "SELECT * FROM "+ TABLE_STUD_PARENT_ATTENDANCE + " where "+ KEY_ROW_SATT_DATE +" = '"+ date+
				"' AND " + KEY_ROW_SATT_UID + " = '" + userid + "'";
		Cursor cursor = db.rawQuery(query, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}

	// Getting contacts Count
	public int getSubjectCount() {
		Cursor c = db.query(TABLE_TEACHER_SUBJECT, new String[] { KEY_ROW_ID },
				null, null, null, null, null);
		return c == null ? 0 : c.getCount();
	}

	// Deleting selected Assignment
	public boolean deleteAssignment(int row_id) {
		int deleteCount = db.delete(TABLE_TEACHER_ASSIGNMENT, KEY_ROW_ID + "="
				+ row_id, null);
		return (deleteCount > 0);
	}

	// Deleting Announcement
	public boolean deleteAnnouncement(int row_id) {
		int deleteCount = db.delete(TABLE_TEACHER_ANNOUNCEMENT, KEY_ROW_ID
				+ "=" + row_id, null);
		return (deleteCount > 0);
	}

	public boolean deleteLessonPlan(int row_id) {
		int deleteCount = db.delete(TABLE_TEACHER_LESSON_PLAN_UPLOAD,
				KEY_ROW_ID + "=" + row_id, null);
		return (deleteCount > 0);
	}

	public boolean deleteWeeklyLessonPlan(int row_id) {
		int deleteCount = db.delete(TABLE_WEEKLY_LESSON_PLAN, KEY_ROW_ID + "="
				+ row_id, null);
		return (deleteCount > 0);
	}

	// Getting Assignment, Quiz, Activity Count
	public int getAssignmentCount() {
		Cursor c = db.query(TABLE_TEACHER_ASSIGNMENT,
				new String[] { KEY_ROW_ID }, null, null, null, null, null);
		return c == null ? 0 : c.getCount();
	}

	// Getting Announcement, Coursework Count
	public int getAnnounceCount() {
		Cursor c = db.query(TABLE_TEACHER_ANNOUNCEMENT,
				new String[] { KEY_ROW_ID }, null, null, null, null, null);
		return c == null ? 0 : c.getCount();
	}

	public int getLessonCount() {
		Cursor c = db.query(TABLE_TEACHER_LESSON_PLAN_UPLOAD,
				new String[] { KEY_ROW_ID }, null, null, null, null, null);
		return c == null ? 0 : c.getCount();
	}

	public int getLessonCountWeekly() {
		Cursor c = db.query(TABLE_WEEKLY_LESSON_PLAN,
				new String[] { KEY_ROW_ID }, null, null, null, null, null);
		return c == null ? 0 : c.getCount();
	}

	// Deleting Attendance
	public boolean deleteAttendance(String lecture_num) {
		int deleteCount = db.delete(TABLE_TEACHER_ATTENDANCE,
				KEY_LECTURE_NUM_ATTENDANCE + "=" + lecture_num, null);
		return (deleteCount > 0);
	}

	// Getting Attendance Count
	public int getAttendanceCount() {
		String selectQuery = "SELECT DISTINCT " + KEY_LECTURE_NUM_ATTENDANCE
				+ " FROM " + TABLE_TEACHER_ATTENDANCE;
		Cursor c = db.rawQuery(selectQuery, null);
		return c == null ? 0 : c.getCount();
	}

}
