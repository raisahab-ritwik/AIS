package com.knwedu.ourschool.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class DataStructureFramwork {

	public static class UserInfo {
		public String userid, usertypeid, alt_email,org_email, mobile_no, fname, lname,
				mname, organizationid, paystatus, id, is_use_practical,
				is_delete, activity_available, test_available, exam_for_org,
				assignment_available, exam_type_setting, default_password,
				atten_pub_lec_no, atten_mark_lec_no, timetable_week,
				is_use_group_practical, is_use_group_project,
				is_notification_active, is_use_project, is_mail_active,
				caption_lecture, caption_group, caption_level, caption_grade,
				caption_section, timetbl_change, special_class_set,
				caption_class, week_off_index, week_start_index,
				active_session_id;
		public JSONObject object;

		public UserInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.userid = c.getString("user_id").trim();
				Log.d("user_id", c.getString("user_id").trim());
			} catch (Exception e) {
			}
			try {
				this.usertypeid = c.getString("user_type_id").trim();
				Log.d("user_type_id", c.getString("user_type_id").trim());
			} catch (Exception e) {
			}
			try {
				this.organizationid = c.getString("organization_id").trim();
				Log.d("organization_id", c.getString("organization_id").trim());
			} catch (Exception e) {
			}
			try {
				this.paystatus = c.getString("pay_status").trim();
				Log.d("pay_status", c.getString("pay_status").trim());
			} catch (Exception e) {
			}
			try {
				this.alt_email = c.getString("alt_email").trim();
				Log.d("alt_email", c.getString("alt_email").trim());
			} catch (Exception e) {
			}
			try {
				this.org_email = c.getString("email").trim();
			} catch (Exception e) {
			}
			try {
				this.mobile_no = c.getString("mobile_no").trim();
				Log.d("mobile_no", c.getString("mobile_no").trim());
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
				Log.d("id", c.getString("id").trim());
			} catch (Exception e) {
			}
			try {
				this.active_session_id = c.getString("active_session_id")
						.trim();
				Log.d("active_session_id", c.getString("active_session_id")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.week_start_index = c.getString("week_start_index").trim();
				Log.d("week_start_index", c.getString("week_start_index")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.week_off_index = c.getString("week_off_index").trim();
				Log.d("week_off_index", c.getString("week_off_index").trim());
			} catch (Exception e) {
			}
			try {
				this.timetbl_change = c.getString("timetbl_change").trim();
				Log.d("timetbl_change", c.getString("timetbl_change").trim());
			} catch (Exception e) {
			}
			try {
				this.special_class_set = c.getString("special_class_set")
						.trim();
				Log.d("special_class_set", c.getString("special_class_set")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.caption_class = c.getString("caption_class").trim();
				Log.d("caption_class", c.getString("caption_class").trim());
			} catch (Exception e) {
			}
			try {
				this.caption_section = c.getString("caption_section").trim();
				Log.d("caption_section", c.getString("caption_section").trim());
			} catch (Exception e) {
			}
			try {
				this.caption_grade = c.getString("caption_grade").trim();
				Log.d("caption_grade", c.getString("caption_grade").trim());
			} catch (Exception e) {
			}
			try {
				this.caption_level = c.getString("caption_level").trim();
				Log.d("caption_level", c.getString("caption_level").trim());
			} catch (Exception e) {
			}
			try {
				this.caption_group = c.getString("caption_group").trim();
				Log.d("caption_group", c.getString("caption_group").trim());
			} catch (Exception e) {
			}
			try {
				this.caption_lecture = c.getString("caption_lecture").trim();
				Log.d("caption_lecture", c.getString("caption_lecture").trim());
			} catch (Exception e) {
			}
			try {
				this.is_notification_active = c.getString(
						"is_notification_active").trim();
				Log.d("is_notification_active",
						c.getString("is_notification_active").trim());
			} catch (Exception e) {
			}
			try {
				this.is_mail_active = c.getString("is_mail_active").trim();
				Log.d("is_mail_active", c.getString("is_mail_active").trim());
			} catch (Exception e) {
			}
			try {
				this.is_use_project = c.getString("is_use_project").trim();
				Log.d("is_use_project", c.getString("is_use_project").trim());
			} catch (Exception e) {
			}
			try {
				this.is_use_practical = c.getString("is_use_practical").trim();
				Log.d("is_use_practical", c.getString("is_use_practical")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.is_use_group_project = c.getString("is_use_group_project")
						.trim();
				Log.d("is_use_group_project",
						c.getString("is_use_group_project").trim());
			} catch (Exception e) {
			}
			try {
				this.is_use_group_practical = c.getString(
						"is_use_group_practical").trim();
				Log.d("is_use_group_practical",
						c.getString("is_use_group_practical").trim());
			} catch (Exception e) {
			}
			try {
				this.timetable_week = c.getString("timetable_week").trim();
				Log.d("timetable_week", c.getString("timetable_week").trim());
			} catch (Exception e) {
			}
			try {
				this.atten_mark_lec_no = c.getString("atten_mark_lec_no")
						.trim();
				Log.d("atten_mark_lec_no", c.getString("atten_mark_lec_no")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.atten_pub_lec_no = c.getString("atten_pub_lec_no").trim();
				Log.d("atten_pub_lec_no", c.getString("atten_pub_lec_no")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.default_password = c.getString("default_password").trim();
				Log.d("default_password", c.getString("default_password")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.exam_type_setting = c.getString("exam_type_setting")
						.trim();
				Log.d("exam_type_setting", c.getString("exam_type_setting")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.exam_for_org = c.getString("exam_for_org").trim();
				Log.d("exam_for_org", c.getString("exam_for_org").trim());
			} catch (Exception e) {
			}
			try {
				this.assignment_available = c.getString("assignment_available")
						.trim();
				Log.d("assignment_available",
						c.getString("assignment_available").trim());
			} catch (Exception e) {
			}
			try {
				this.test_available = c.getString("test_available").trim();
				Log.d("test_available", c.getString("test_available").trim());
			} catch (Exception e) {
			}
			try {
				this.activity_available = c.getString("activity_available")
						.trim();
				Log.d("activity_available", c.getString("activity_available")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.is_delete = c.getString("is_delete").trim();
				Log.d("is_delete", c.getString("is_delete").trim());
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
				Log.d("fname", c.getString("fname").trim());
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
				Log.d("lanme", c.getString("lname").trim());
			} catch (Exception e) {
			}
			try {
				this.mname = c.getString("mname").trim();
				Log.d("mname", c.getString("mname").trim());
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

		/*
		 * public void updateObject() { if (object == null) { object = new
		 * JSONObject(); } try { object.put("user_id", this.userid); } catch
		 * (JSONException e) { e.printStackTrace(); } try {
		 * object.put("user_type_id", this.usertypeid); } catch (JSONException
		 * e) { e.printStackTrace(); } try { object.put("organization_id",
		 * this.organizationid); } catch (JSONException e) {
		 * e.printStackTrace(); } try { object.put("pay_status",
		 * this.paystatus); } catch (JSONException e) { e.printStackTrace(); }
		 * 
		 * }
		 */

	}

	public static class Subject {
		public String sub_code, section_id, classs, subject, sub_name, email,
				phone, fname, lname, image, id, is_project, is_practical,
				section_name, class_id;
		public JSONObject object;
		public boolean check;
		public int row_id;

		public Subject(JSONObject c) {
			this.object = c;

			try {
				this.sub_code = c.getString("sub_code").trim();
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
			} catch (Exception e) {
			}
			try {
				this.is_project = c.getString("is_project").trim();
			} catch (Exception e) {
			}
			try {
				this.is_practical = c.getString("is_practical").trim();
			} catch (Exception e) {
			}

			try {
				this.phone = c.getString("mobile_no").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.image = c.getString("image").trim();
			} catch (Exception e) {
			}
			try {
				this.classs = c.getString("class").trim();
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
				Log.d("section_id", c.getString("section_id").trim());
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_name = c.getString("sub_name").trim();
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
		}

		public Subject() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Assignment {
		public String id, title, teacher_name, total_marks, description,
				file_name, submit_date, created_date, is_marked, is_published,
				assignment_class, section_name, attachment, sub_code, sub_name,
				marks, subject_id, comments;
		public boolean check;
		public JSONObject object;
		public int row_id, type;
		public String section_id;

		public Assignment() {

		}

		public Assignment(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
				Log.d("id_____", c.getString("id").trim());
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}
			try {
				this.submit_date = c.getString("submit_date").trim();
			} catch (Exception e) {
			}
			try {
				this.is_marked = c.getString("is_marked").trim();
			} catch (Exception e) {
			}
			try {
				this.is_published = c.getString("is_published").trim();
			} catch (Exception e) {
			}
			try {
				this.total_marks = c.getString("total_marks").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher_name = c.getString("teacher_name").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.is_published = c.getString("is_published").trim();
			} catch (Exception e) {
			}
			try {
				this.assignment_class = c.getString("class").trim();
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_code = c.getString("sub_code").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_name = c.getString("sub_name").trim();
			} catch (Exception e) {
			}
			try {
				this.marks = c.getString("student_marks").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_id = c.getString("subject_id").trim();
			} catch (Exception e) {
			}
			try {
				this.comments = c.getString("comments").trim();
			} catch (Exception e) {
			}

		}

	}

	public static class Behaviour {
		public String description, student_name,grade_name,grade_point,grade_description,card_description,card_name,file_name,section_id,created_date,created_by,status;
		public JSONObject object;

		public Behaviour() {

		}

		public Behaviour(JSONObject c) {
			super();
			this.object = c;
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.student_name = c.getString("student_name").trim();
			} catch (Exception e) {
			}
			try {
				this.grade_name = c.getString("grade_name").trim();
			} catch (Exception e) {
			}
			try {
				this.grade_point = c.getString("grade_point").trim();
			} catch (Exception e) {
			}
			try {
				this.grade_description = c.getString("grade_description").trim();
			} catch (Exception e) {
			}
			try {
				this.card_description = c.getString("card_description").trim();
			} catch (Exception e) {
			}
			try {
				this.card_name = c.getString("card_name").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.created_by = c.getString("created_by").trim();
			} catch (Exception e) {
			}
			try {
				this.status = c.getString("status").trim();
			} catch (Exception e) {
			}
		}

	}

	public static class Announcement {
		public String id, sub_code, section_name, title, description,
				teacher_id, date, sub_name, attachment, file_name;
		public JSONObject object;

		public boolean check;
		public int row_id, type;
		public String subject_id, section_id;

		public Announcement() {

		}

		public Announcement(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_code = c.getString("sub_code").trim();
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_name = c.getString("sub_name").trim();
			} catch (Exception e) {
			}

			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	// Parent Profile information
	public static class ParentProfileInfo {
		public String user_code, fullname, email, mobile_no, address,
				alt_email, religion, blood_group, dob, image, fname, mname,
				lname, pincode;
		public JSONObject object;

		public ParentProfileInfo(){}

		public ParentProfileInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.user_code = c.getString("user_code").trim();
			} catch (Exception e) {
			}
			try {
//				this.fullname = c.getString("fname").trim() + " "
//						+ c.getString("mname").trim() + " "
//						+ c.getString("lname").trim();
				this.fullname = c.getString("name").trim();
				Log.d("name", this.fullname);
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
			} catch (Exception e) {
			}
			try {
				this.mobile_no = c.getString("mobile_no").trim();
			} catch (Exception e) {
			}
			try {
				this.address = c.getString("address").trim();
			} catch (Exception e) {
			}
			try {
				this.alt_email = c.getString("alt_email").trim();
			} catch (Exception e) {
			}
			try {
				this.religion = c.getString("religion").trim();
			} catch (Exception e) {
			}
			try {
				this.blood_group = c.getString("blood_group").trim();
			} catch (Exception e) {
			}
			try {
				this.pincode = c.getString("pin_code").trim();
			} catch (Exception e) {
			}

			try {
				this.image = c.getString("image").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.mname = c.getString("mname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.dob = c.getString("dob").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	// Teacher Profile information
	public static class TeacherProfileInfo {
		public String user_code, fullname, email, mobile_no, address,
				alt_email, religion, image, fname, mname, lname, pincode,
				bloodgroup, dob, educational_qualification, other_qualification,date_of_joining;
		public JSONObject object;

		public TeacherProfileInfo(){}

		public TeacherProfileInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.user_code = c.getString("user_code").trim();
			} catch (Exception e) {
			}
			try {
				this.fullname = c.getString("fname").trim() + " "
						+ c.getString("mname").trim() + " "
						+ c.getString("lname").trim();
				Log.d("name", this.fullname);
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
			} catch (Exception e) {
			}
			try {
				this.mobile_no = c.getString("mobile_no").trim();
			} catch (Exception e) {
			}
			try {
				this.address = c.getString("address").trim();
			} catch (Exception e) {
			}
			try {
				this.religion = c.getString("religion").trim();
			} catch (Exception e) {
			}
			try {
				this.alt_email = c.getString("alt_email").trim();
				Log.d("alt_email", this.alt_email);
			} catch (Exception e) {
			}
			try {
				this.image = c.getString("image").trim();
				Log.d("image", this.image);
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
				Log.d("fname", this.fname);
			} catch (Exception e) {
			}
			try {
				this.mname = c.getString("mname").trim();
				Log.d("mname", this.mname);
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
				Log.d("lname", this.lname);
			} catch (Exception e) {
			}
			try {
				this.pincode = c.getString("pin_code").trim();
			} catch (Exception e) {
			}
			try {
				this.bloodgroup = c.getString("blood_group").trim();
			} catch (Exception e) {
			}
			try {
				this.dob = c.getString("dob").trim();
			} catch (Exception e) {
			}
			try {
				this.educational_qualification = c.getString("educational_qualification").trim();
			} catch (Exception e) {
			}
			try {
				this.other_qualification = c.getString("other_qualification").trim();
			} catch (Exception e) {
			}
			try {
				this.date_of_joining = c.getString("date_of_joining").trim();
			} catch (Exception e) {
			}



		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	// Student Profile information
	public static class StudentProfileInfo {
		public String user_id, usertypeid, organizationid, user_code, fname,
				mname, lname, fullname, class_section, class_roll, email,
				mobile_no, address, religion, blood_group, dob, reg_no,
				ailment, hostel, transport, disease, image, alt_email,
				pin_code, section_id, classs, section;
		public JSONObject object;

		public StudentProfileInfo(){}

		public StudentProfileInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.user_id = c.getString("user_id").trim();
				Log.d("user_id", this.user_id);
			} catch (Exception e) {
			}
			try {
				this.usertypeid = c.getString("user_type_id").trim();
				Log.d("user_type_id", c.getString("user_type_id").trim());
			} catch (Exception e) {
			}
			try {
				this.organizationid = c.getString("organization_id").trim();
				Log.d("organization_id", c.getString("organization_id").trim());
			} catch (Exception e) {
			}
			try {
				this.user_code = c.getString("user_code").trim();
				Log.d("user_code", this.user_code);
			} catch (Exception e) {
			}
			try {
				this.fullname = c.getString("fname").trim() + " "
						+ c.getString("mname").trim() + " "
						+ c.getString("lname").trim();
				Log.d("name", this.fullname);
			} catch (Exception e) {
			}
			try {
				this.class_section = c.getString("class").trim() + " "
						+ c.getString("section_name").trim();
				;
				Log.d("class_name", this.class_section);
			} catch (Exception e) {
			}
			try {
				this.class_roll = c.getString("roll_no").trim();
				Log.d("class_roll", this.class_roll);
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
				Log.d("email", this.email);
			} catch (Exception e) {
			}
			try {
				this.mobile_no = c.getString("mobile_no").trim();
				Log.d("mobile_number", this.mobile_no);
			} catch (Exception e) {
			}
			try {
				this.address = c.getString("address").trim();
				Log.d("address", this.address);
			} catch (Exception e) {
			}
			try {
				this.religion = c.getString("religion").trim();
				Log.d("religion", this.religion);
			} catch (Exception e) {
			}

			try {
				this.blood_group = c.getString("blood_group").trim();
				Log.d("blood_group", this.blood_group);
			} catch (Exception e) {
			}

			try {
				this.ailment = c.getString("ailment").trim();
				Log.d("ailment", this.ailment);
			} catch (Exception e) {
			}

			try {
				this.hostel = c.getString("name").trim();
				Log.d("hostel", this.hostel);
			} catch (Exception e) {
			}
			try {
				this.transport = c.getString("route").trim();
				Log.d("transport", this.transport);
			} catch (Exception e) {
			}
			try {
				this.image = c.getString("image").trim();
				Log.d("image", this.image);
			} catch (Exception e) {
			}
			try {
				this.disease = c.getString("disease").trim();
				Log.d("disease", this.disease);
			} catch (Exception e) {
			}

			try {
				this.religion = c.getString("religion").trim();
				Log.d("religion", this.religion);
			} catch (Exception e) {
			}
			try {
				this.alt_email = c.getString("alt_email").trim();
				Log.d("blood_group", this.alt_email);
			} catch (Exception e) {
			}

			try {
				this.fname = c.getString("name").trim();
				Log.d("name", this.fname);
			} catch (Exception e) {
			}
//			try {
//				this.mname = c.getString("mname").trim();
//				Log.d("mname", this.mname);
//			} catch (Exception e) {
//			}
//			try {
//				this.lname = c.getString("lname").trim();
//				Log.d("lname", this.lname);
//			} catch (Exception e) {
//			}
			try {
				this.pin_code = c.getString("pin_code").trim();
				Log.d("pincode", c.getString("pincode").trim());
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
				Log.d("section_id", c.getString("section_id").trim());
			} catch (Exception e) {
			}
			try {
				this.dob = c.getString("dob").trim();
			} catch (Exception e) {
			}
			try {
				this.reg_no = c.getString("registration_no").trim();
			} catch (Exception e) {
			}
			try {
				this.classs = c.getString("class").trim();

			} catch (Exception e) {
			}
			try {
				this.section = c.getString("section_name").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}


	public static class AisSyllabusData{
		public String id, title, class_name, section_name, attachment, file_name;
		public JSONObject object;

		public AisSyllabusData(JSONObject c){
			this.object = c;

			try {
				this.id = c.getString("id").trim();
				this.title = c.getString("title").trim();
				this.class_name = c.getString("class").trim();
				this.section_name = c.getString("section_name").trim();
				this.attachment = c.getString("attachment").trim();
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}


	}

	public static class SubjectStudent {
		public String roll_no, fname, lname;
		public JSONObject object;
		public boolean check;

		public SubjectStudent(JSONObject c) {
			this.object = c;
			try {
				this.roll_no = c.getString("roll_no").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	public static class Student {
		public String id, roll_no, name, total_marks, comment, subject_id,
				section_id, teacher_id;
		public boolean check;
		public JSONObject object;
		public int row_id;

		// default constructor
		public Student() {

		}

		public Student(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("student_id").trim();
			} catch (Exception e) {
			}
			try {
				this.roll_no = c.getString("roll_no").trim();
			} catch (Exception e) {
			}
			try {
				this.name = c.getString("name").trim();
			} catch (Exception e) {
			}
			try {
				this.total_marks = c.getString("marks").trim();
			} catch (Exception e) {
			}

			try {
				this.comment = c.getString("comment").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Attendance {
		public String student_name = "", roll_no = "", class_id = "",
				student_id = "", date = "", leave_master_reason = "",
				status = "", sr_id = "", late = "", section_id = "",
				group_id = "", lecture_num = "", sub_id = "", teacher_id = "";
		public boolean check;
		public JSONObject object;

		public Attendance(JSONObject c) {
			super();
			this.object = c;
			try {
				this.student_name = c.getString("student_name").trim();

			} catch (Exception e) {
			}
			try {
				this.sub_id = c.getString("sub_id").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher_id = c.getString("teacher_id").trim();
			} catch (Exception e) {
			}
			try {
				this.roll_no = c.getString("roll_no").trim();
			} catch (Exception e) {
			}
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("date").trim();
			} catch (Exception e) {
			}
			try {
				this.status = c.getString("status").trim();
			} catch (Exception e) {
			}
			try {
				this.leave_master_reason = c.getString("leave_master_reason")
						.trim();
			} catch (Exception e) {
			}
			try {
				this.sr_id = c.getString("sr_id").trim();
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
			} catch (Exception e) {
			}
			try {
				this.group_id = c.getString("group_id").trim();
			} catch (Exception e) {
			}
			try {
				this.lecture_num = c.getString("lecture_num").trim();
			} catch (Exception e) {
			}
			try {
				this.late = c.getString("late").trim();
			} catch (Exception e) {
			}
			try {
				this.student_id = c.getString("student_id").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

		public Attendance() {

		}
	}

	public static class TeacherSubjectByTimeTable {
		public String class_id, fname, lname, group_id, classname, date,
				is_mark, sub_name, teacher_id, sub_id, sub_code, section_id,
				section_name, lecture_num, srid;
		public JSONObject object;
		public boolean check;

		public TeacherSubjectByTimeTable(JSONObject c) {
			this.object = c;
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("date").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.is_mark = c.getString("is_mark").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.classname = c.getString("class").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_name = c.getString("sub_name").trim();
			} catch (Exception e) {
			}
			try {
				this.srid = c.getString("sr_id").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher_id = c.getString("teacher_id").trim();
			} catch (Exception e) {
			}
			try {
				this.group_id = c.getString("group_id").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_id = c.getString("sub_id").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_code = c.getString("sub_code").trim();
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
			try {
				this.lecture_num = c.getString("lecture_num").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

		public TeacherSubjectByTimeTable() {

		}
	}

	public static class Test {
		public String id, title, attachment, description, date, subject, fname,
				lname, marks, date_created, marked, code;
		public boolean check;
		public JSONObject object;

		public Test(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.code = c.getString("code").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("date").trim();
			} catch (Exception e) {
			}
			try {
				this.subject = c.getString("subject").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.marks = c.getString("marks").trim();
			} catch (Exception e) {
			}
			try {
				this.date_created = c.getString("date_created").trim();
			} catch (Exception e) {
			}
			try {
				this.marked = c.getString("marked").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class News {
		public String id, class_name, title, description, date, status, doc, file_name;
		public JSONObject object;

		public News(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}

			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.class_name = c.getString("class_name").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.status = c.getString("status").trim();
			} catch (Exception e) {
			}
			try {
				this.doc = c.getString("doc").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class RequestsStatus {
		public String id, fname, lname, reason_title, description,
				created_date, date_to, date_from, status, reject_reason,
				attachment, file_name;
		public JSONObject object;
		public boolean check;

		public RequestsStatus(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}

			try {
				this.reason_title = c.getString("reason_title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}

			try {
				this.date_to = c.getString("date_to").trim();
			} catch (Exception e) {
			}
			try {
				this.date_from = c.getString("date_from").trim();
			} catch (Exception e) {
			}
			try {
				this.status = c.getString("status").trim();
			} catch (Exception e) {
			}
			try {
				this.reject_reason = c.getString("reject_reason").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("doc").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class StatusStudent {
		public String date, status, subject, lecture, late_reason;
		public boolean check;
		public JSONObject object;

		public StatusStudent(){}

		public StatusStudent(JSONObject c) {
			super();
			this.object = c;
			try {
				this.date = c.getString("date").trim();
			} catch (Exception e) {
			}
			try {
				this.status = c.getString("status").trim();
			} catch (Exception e) {
			}
			try {
				this.subject = c.getString("sub_name").trim();
			} catch (Exception e) {
			}
			try {
				this.lecture = c.getString("lecture_num").trim();
			} catch (Exception e) {
			}
			try {
				this.late_reason = c.getString("late_reason").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class ClassFellows {
		public String lname, fname, roll_no, id;
		public JSONObject object;
		public boolean check;

		public ClassFellows(JSONObject c) {
			this.object = c;
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.roll_no = c.getString("roll_no").trim();
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class TeacherWeeklyplan {
		public String week_id, week_start_date, type_id, type_name, sr_id,
				file, daily_plan, daily_schedule, date, topic, daily_id, wm_id,is_publish,
				wpm_id,id,weekly_plan_id,weekly_date,attachment,week_date,day_name;

		public JSONObject object;
		public boolean check;
		public int row_id;

		public TeacherWeeklyplan() {

		}

		public TeacherWeeklyplan(JSONObject c) {
			this.object = c;

			try {
				this.is_publish = c.getString("is_publish").trim();
			} catch (Exception e) {
			}
			try {
				this.week_start_date = c.getString("week_start_date").trim();
			} catch (Exception e) {
			}
			try {
				this.day_name = c.getString("day_name").trim();
			} catch (Exception e) {
			}
			try {
				this.week_date = c.getString("week_date").trim();
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.weekly_plan_id = c.getString("weekly_plan_id").trim();
			} catch (Exception e) {
			}
			try {
				this.weekly_date = c.getString("weekly_date").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
			try {
				this.wpm_id = c.getString("wpm_id").trim();
			} catch (Exception e) {
			}
			try {
				this.wm_id = c.getString("wm_id").trim();
			} catch (Exception e) {
			}
			try {
				this.daily_id = c.getString("daily_id").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("date").trim();
			} catch (Exception e) {
			}
			try {
				this.topic = c.getString("topic").trim();
			} catch (Exception e) {
			}
			try {
				this.type_id = c.getString("type_id").trim();
			} catch (Exception e) {
			}

			try {
				this.week_id = c.getString("week_id").trim();
			} catch (Exception e) {
			}

			try {
				this.sr_id = c.getString("sr_id").trim();
			} catch (Exception e) {
			}

			try {
				this.type_name = c.getString("type_name").trim();
			} catch (Exception e) {
			}

			try {
				this.file = c.getString("file").trim();
			} catch (Exception e) {
			}
			try {
				this.daily_schedule = c.getString("daily_plan").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class TeacherSchedule {
		public String start_date, topic, id, date, end_date, file, doc,
				daily_schedule;

		public JSONObject object;
		public boolean check;
		public int row_id;

		public TeacherSchedule() {

		}

		public TeacherSchedule(JSONObject c) {
			this.object = c;

			try {
				this.topic = c.getString("topic").trim();
			} catch (Exception e) {
			}

			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}

			try {
				this.start_date = c.getString("start_date").trim();
			} catch (Exception e) {
			}

			try {
				this.end_date = c.getString("end_date").trim();
			} catch (Exception e) {
			}

			try {
				this.date = c.getString("date").trim();
			} catch (Exception e) {
			}

			try {
				this.file = c.getString("file").trim();
			} catch (Exception e) {
			}
			try {
				this.doc = c.getString("doc").trim();
			} catch (Exception e) {
			}
			try {
				this.daily_schedule = c.getString("daily_schedule").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class TimeTable {
		public String subject, subject_name, week_day, lecture_num, section_id,
				original_lecture_num, classs, section, className, start, end,
				facility, teacher_name, is_project, is_practical, is_selective,
				week_index, day_index, group_id, group_name, sr_id, teacher_id,
				break_txt, subject_type;
		public boolean check;
		public JSONObject object;

		public TimeTable(JSONObject c) {
			super();
			this.object = c;
			try {
				this.subject = c.getString("sub_name").trim();
			} catch (Exception e) {
				this.subject = "null";
			}
			try {
				this.break_txt = c.getString("break").trim();
			} catch (Exception e) {
			}
			try {
				this.week_index = c.getString("week_index").trim();
			} catch (Exception e) {
			}
			try {
				this.is_project = c.getString("is_project").trim();
			} catch (Exception e) {
			}
			try {
				this.is_practical = c.getString("is_practical").trim();
			} catch (Exception e) {
			}
			try {
				this.is_selective = c.getString("is_selective").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_name = c.getString("subject_name").trim();
			} catch (Exception e) {
			}

			try {
				this.section_id = c.getString("section_id").trim();
			} catch (Exception e) {
			}

			try {
				this.week_day = c.getString("week_day").trim();
			} catch (Exception e) {
			}

			try {
				this.lecture_num = c.getString("lecture_num").trim();
			} catch (Exception e) {
			}

			try {
				this.classs = c.getString("class").trim();
			} catch (Exception e) {
			}

			try {
				this.className = c.getString("className").trim();
			} catch (Exception e) {
			}

			try {
				this.section = c.getString("section_name").trim();
			} catch (Exception e) {
			}

			try {
				this.original_lecture_num = c.getString("original_lecture_num")
						.trim();
			} catch (Exception e) {
			}

			try {
				this.day_index = c.getString("day_index").trim();
			} catch (Exception e) {
			}

			try {
				this.group_id = c.getString("group_id").trim();
			} catch (Exception e) {
			}

			try {
				this.group_name = c.getString("group_name").trim();
			} catch (Exception e) {
			}

			try {
				this.sr_id = c.getString("sr_id").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher_id = c.getString("teacher_id").trim();
			} catch (Exception e) {
			}

			try {
				this.subject_type = c.getString("subject_type").trim();
			} catch (Exception e) {
			}

			try {
				this.week_day = c.getString("week_day").trim();
			} catch (Exception e) {
			}
			try {
				this.start = c.getString("start").trim();
			} catch (Exception e) {
			}

			try {
				this.end = c.getString("end").trim();
			} catch (Exception e) {
			}

			try {
				this.facility = c.getString("facility").trim();
			} catch (Exception e) {
			}

			try {
				this.teacher_name = c.getString("teacher_name").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

		public TimeTable() {

		}

	}

	public static class ClassSchedule {
		public String title, description, date, date_to;
		public JSONObject object;
		public boolean check;

		public ClassSchedule(JSONObject c) {
			this.object = c;
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("date").trim();
			} catch (Exception e) {
			}
			try {
				this.date_to = c.getString("date_to").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Examreportcard {
		public String report_card_name, report_card_id, report_card_link;
		public JSONObject object;
		public boolean check;

		public Examreportcard(JSONObject c) {
			this.object = c;
			try {
				this.report_card_name = c.getString("report_card_name").trim();
			} catch (Exception e) {
			}
			try {
				this.report_card_id = c.getString("report_card_id").trim();
			} catch (Exception e) {
			}
			try {
				this.report_card_link = c.getString("link").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public class ExamList {
		 String is_publish, is_schedule, id, section_id, class_id,
				name_of_exam, exam_term_id, class_name, section_name,
				is_result_publish, term_init_date, attachment, file_name;
		public boolean check;
		public JSONObject object;

		public String getIs_publish() {
			return is_publish;
		}

		public String getIs_schedule() {
			return is_schedule;
		}

		public String getId() {
			return id;
		}

		public String getSection_id() {
			return section_id;
		}

		public String getClass_id() {
			return class_id;
		}

		public String getName_of_exam() {
			return name_of_exam;
		}

		public String getExam_term_id() {
			return exam_term_id;
		}

		public String getClass_name() {
			return class_name;
		}

		public String getSection_name() {
			return section_name;
		}

		public String getIs_result_publish() {
			return is_result_publish;
		}

		public String getTerm_init_date() {
			return term_init_date;
		}

		public String getAttachment() {
			return attachment;
		}

		public String getFile_name() {
			return file_name;
		}

		public boolean isCheck() {
			return check;
		}

		public JSONObject getObject() {
			return object;
		}

		public ExamList(JSONObject c) {

			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.is_publish = c.getString("is_publish").trim();
			} catch (Exception e) {
			}
			try {
				this.is_schedule = c.getString("is_schedule").trim();
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
			} catch (Exception e) {
			}
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}
			try {
				this.name_of_exam = c.getString("name_of_exam").trim();
			} catch (Exception e) {
			}
			try {
				this.exam_term_id = c.getString("exam_term_id").trim();
			} catch (Exception e) {
			}
			try {
				this.class_name = c.getString("class").trim();
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
			try {
				this.is_result_publish = c.getString("is_result_publish")
						.trim();
			} catch (Exception e) {
			}
			try {
				this.term_init_date = c.getString("term_init_date").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class ExamSchedule {
		public String sub_name, facility_no, schedule_date, start_time,
				end_time;
		public boolean check;
		public JSONObject object;

		public ExamSchedule(JSONObject c) {
			super();
			this.object = c;
			try {
				this.sub_name = c.getString("sub_name").trim();
			} catch (Exception e) {
			}
			try {
				this.facility_no = c.getString("facility_no").trim();
			} catch (Exception e) {
			}
			try {
				this.schedule_date = c.getString("schedule_date").trim();
			} catch (Exception e) {
			}
			try {
				this.start_time = c.getString("start_time").trim();
			} catch (Exception e) {
			}
			try {
				this.end_time = c.getString("end_time").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Orginization {
		public String name, activity, date, description, student_id;
		public JSONObject object;
		public boolean check;

		public Orginization(JSONObject c) {
			this.object = c;
			try {
				this.name = c.getString("org_name").trim();
			} catch (Exception e) {
			}
			try {
				this.student_id = c.getString("studentId").trim();
			} catch (Exception e) {
			}
			try {
				this.activity = c.getString("activity_name").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Examdetail {
		public String fname, lname, class_name, address, school_name, exam,
				session, roll_no, rank, remarks, comments;
		public boolean check;
		public JSONObject object;

		public Examdetail(JSONObject c) {
			super();
			this.object = c;
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.class_name = c.getString("class_name").trim();
			} catch (Exception e) {
			}
			try {
				this.school_name = c.getString("school_name").trim();
			} catch (Exception e) {
			}
			try {
				this.address = c.getString("address").trim();
			} catch (Exception e) {
			}
			try {
				this.roll_no = c.getString("roll_no").trim();
			} catch (Exception e) {
			}
			try {
				this.exam = c.getString("exam").trim();
			} catch (Exception e) {
			}
			try {
				this.session = c.getString("session_name").trim();
			} catch (Exception e) {
			}

			try {
				this.rank = c.getString("rank").trim();
			} catch (Exception e) {
			}

			try {
				this.remarks = c.getString("remarks").trim();
			} catch (Exception e) {
			}
			try {
				this.comments = c.getString("comments").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class MarksConsolidated {
		public String sub_name, Out_of_marks, sub_id, Marks, grade, remarks,
				subject_group, group_name;
		public boolean check;
		public JSONObject object;

		public MarksConsolidated(JSONObject c) {
			super();
			this.object = c;
			try {
				this.sub_name = c.getString("sub_name").trim();
			} catch (Exception e) {
			}
			try {
				this.remarks = c.getString("Remarks").trim();
			} catch (Exception e) {
				this.remarks = "";
			}
			try {
				this.grade = c.getString("Grade").trim();
			} catch (Exception e) {
				this.grade = "";
			}
			try {
				this.group_name = c.getString("group_name").trim();
			} catch (Exception e) {
				this.grade = "";
			}
			try {
				this.subject_group = c.getString("group_name").trim();
			} catch (Exception e) {
				this.subject_group = "";
			}

			try {
				this.Out_of_marks = c.getString("Out_of_marks").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_id = c.getString("sub_id").trim();
			} catch (Exception e) {
			}
			try {
				this.Marks = c.getString("Marks").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class MarksDetails {
		public String Subject_Marks, Subject_Total_Marks, Project_Marks,
				Project_Total_marks, Practical_Marks, Practical_Total_Marks,
				Title, Subject, Marks, Total_Marks, Type, header,
				subject_group, grade, remarks;
		public boolean check;
		public JSONObject object;

		public MarksDetails(JSONObject c) {
			super();
			this.object = c;
			try {
				this.Type = c.getString("Type").trim();
			} catch (Exception e) {
				this.Type = "";
			}

			try {
				this.grade = c.getString("Grade").trim();
			} catch (Exception e) {
				this.Type = "";
			}
			try {
				this.remarks = c.getString("Remarks").trim();
			} catch (Exception e) {
				this.Type = "";
			}
			try {
				this.Subject = c.getString("Subject").trim();
			} catch (Exception e) {
				this.Subject = "";
			}

			try {
				this.subject_group = c.getString("subject_Group").trim();
			} catch (Exception e) {
				this.Subject = "";
			}

			try {
				this.Title = c.getString("Title").trim();
			} catch (Exception e) {
				this.Title = "";
			}

			try {
				this.Subject_Marks = c.getString("Subject_Marks").trim();
			} catch (Exception e) {
				this.Subject_Marks = "";
			}
			try {
				this.Subject_Total_Marks = c.getString("Subject_Total_Marks")
						.trim();
			} catch (Exception e) {
				this.Subject_Total_Marks = "";
			}
			try {
				this.Project_Marks = c.getString("Project_Marks").trim();
			} catch (Exception e) {
				this.Project_Marks = "";
			}

			try {
				this.Project_Total_marks = c.getString("Project_Total_marks")
						.trim();
			} catch (Exception e) {
				this.Project_Total_marks = "";
			}
			try {
				this.Practical_Marks = c.getString("Practical_Marks").trim();
			} catch (Exception e) {
				this.Practical_Marks = "";
			}
			try {
				this.Practical_Total_Marks = c.getString(
						"Practical_Total_Marks").trim();
			} catch (Exception e) {
				this.Practical_Total_Marks = "";
			}

			try {
				this.Marks = c.getString("Marks").trim();
			} catch (Exception e) {
				this.Marks = "";
			}
			try {
				this.Total_Marks = c.getString("Total_Marks").trim();
			} catch (Exception e) {
				this.Total_Marks = "";
			}

			this.header = (this.Type.equals("") ? "" : this.Type + ": ")
					+ (this.Title.equals("") ? "" : (this.Title + ": "))
					+ this.Subject;

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Directory {
		public String id,code, designition, name, address, phone1, phone2, email;
		public JSONObject object;
		public boolean check;
		public Directory()
		{

		}

		public Directory(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.code = c.getString("code").trim();
			} catch (Exception e) {
			}
			try {
				this.designition = c.getString("designition").trim();
			} catch (Exception e) {
			}
			try {
				this.name = c.getString("name").trim();
			} catch (Exception e) {
			}
			try {
				this.address = c.getString("address").trim();
			} catch (Exception e) {
			}
			try {
				this.phone1 = c.getString("phone1").trim();
			} catch (Exception e) {
			}
			try {
				this.phone2 = c.getString("phone2").trim();
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Subjectversion {
		public String version_name;
		public JSONObject object;

		public Subjectversion(JSONObject c) {
			this.object = c;
			try {
				this.version_name = c.getString("data").trim();
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	public static class PermissionAdd {
		public String assignment_add, test_add, activity_add, announcement_add,
				lessons_add, classwork_add, attendance_type;
		public JSONObject object;

		public PermissionAdd(JSONObject c) {
			super();
			this.object = c;
			try {
				this.assignment_add = c.getString("assignment_add").trim();
				Log.d("assignment_add", c.getString("assignment_add").trim());
			} catch (Exception e) {
			}
			try {
				this.test_add = c.getString("test_add").trim();
				Log.d("test_add", c.getString("test_add").trim());
			} catch (Exception e) {
			}
			try {
				this.activity_add = c.getString("activity_add").trim();
				Log.d("activity_add", c.getString("activity_add").trim());
			} catch (Exception e) {
			}
			try {
				this.announcement_add = c.getString("announcement_add").trim();
				Log.d("announcement_add", c.getString("announcement_add")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.classwork_add = c.getString("classwork_add").trim();
				Log.d("activity_add", c.getString("classwork_add").trim());
			} catch (Exception e) {
			}
			try {
				this.lessons_add = c.getString("lessons_add").trim();
				Log.d("lessons_add", c.getString("lessons_add").trim());
			} catch (Exception e) {
			}

			try {
				this.attendance_type = c.getString("attendance_type").trim();
				Log.d("attendace_type", c.getString("attendace_type").trim());
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	public static class Permission {
		public String assignment_permission, test_permission,
				activity_permission;
		public JSONObject object;

		public Permission(JSONObject c) {
			super();
			this.object = c;
			try {
				this.assignment_permission = c.getString(
						"assignment_mark_active").trim();
				Log.d("assignment_permission",
						c.getString("assignment_mark_active").trim());
			} catch (Exception e) {
			}
			try {
				this.test_permission = c.getString("test_mark_active").trim();
				Log.d("test_permission", c.getString("test_mark_active").trim());
			} catch (Exception e) {
			}
			try {
				this.activity_permission = c.getString("activity_mark_active")
						.trim();
				Log.d("activity_permission", c
						.getString("activity_mark_active").trim());
			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	public static class Request {
		public String leave_request, request_for_id, request_for_uniform,
				request_for_book, special_request;
		public JSONObject object;

		public Request(JSONObject c) {
			super();
			this.object = c;
			try {
				this.leave_request = c.getString("L_L").trim();

			} catch (Exception e) {
			}
			try {
				this.request_for_id = c.getString("R_I").trim();

			} catch (Exception e) {
			}
			try {
				this.request_for_uniform = c.getString("R_U").trim();

			} catch (Exception e) {
			}
			try {
				this.request_for_book = c.getString("R_B").trim();

			} catch (Exception e) {
			}
			try {
				this.special_request = c.getString("S_R").trim();

			} catch (Exception e) {
			}

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	public static class Region {
		public String id, name;
		public JSONObject object;

		public Region(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}

			try {
				this.name = c.getString("name").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class ClassOfOrg {
		public String class_id, class_name;
		public ArrayList<SectionOfClass> section_List;

		public ClassOfOrg(JSONObject c) {
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}

			try {
				this.class_name = c.getString("class_name").trim();
			} catch (Exception e) {
			}

			try {
				this.section_List = new ArrayList<SectionOfClass>();
				section_List.clear();
				JSONArray array = c.getJSONArray("section_info");
				for (int i = 0; i < array.length(); i++) {
					SectionOfClass section = new SectionOfClass(
							array.getJSONObject(i));
					section_List.add(section);
				}
			} catch (Exception e) {
			}
		}
	}


	public static class GatewayData{
		public String pg_master_id, pg_commission, pg_convenience_fee;

		public GatewayData(JSONObject c){
			try {
				this.pg_master_id = c.getString("pg_master_id").trim();
			} catch (Exception e) {
			}
			try {
				this.pg_commission = c.getString("pg_commission").trim();
			} catch (Exception e) {
			}
			try {
				this.pg_convenience_fee = c.getString("pg_convenience_fee").trim();
			} catch (Exception e) {
			}
		}
	}

	public static class SectionOfClass {
		public String section_id, section_name;

		public SectionOfClass(JSONObject c) {
			try {
				this.section_id = c.getString("section_id").trim();
			} catch (Exception e) {
			}

			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
		}
	}

	public static class AisTeacherSubject{
		public String sub_code, id, sub_name;
		public JSONObject object;

		public AisTeacherSubject(JSONObject c){
			this.object = c;


			try{
				this.sub_code = c.getString("sub_code").trim();
			}catch (Exception e) {
			}
			try{
				this.id = c.getString("id").trim();
			}catch (Exception e) {
			}
			try{
				this.sub_name = c.getString("sub_name").trim();
			}catch (Exception e) {
			}
		}
	}
	
	public static class SpecialClass {
		public String title, description, fname, lname, classs, section_name;
		public JSONObject object;

		public SpecialClass(JSONObject c) {
			this.object = c;
		
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
		
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.classs = c.getString("class").trim();
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	public static class Event {
		public String event_name, event_description, event_start_date, event_end_date;
		public JSONObject object;

		public Event(JSONObject c) {
			this.object = c;

			try {
				this.event_name = c.getString("event_name").trim();
			} catch (Exception e) {
			}
			try {
				this.event_description = c.getString("event_description").trim();
			} catch (Exception e) {
			}

			try {
				this.event_start_date = c.getString("event_start_date").trim();
			} catch (Exception e) {
			}
			try {
				this.event_end_date = c.getString("event_end_date").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}

	public static class PaymentHistory {
		public String fee_id, fees_period, fees_payment_date, fees_total_amount,organization_id,payment_amount,fee_type,id,payment_date;
		public JSONObject object;

		public PaymentHistory(JSONObject c) {
			this.object = c;
			/*try {
				this.fee_id = c.getString("student_fees_id").trim();
			} catch (Exception e) {
			}
			try {
				this.fees_period = c.getString("fees_months").trim();
			} catch (Exception e) {
			}
			try {
				this.fees_payment_date = c.getString("fees_payment_date").trim();
			} catch (Exception e) {
			}
			try {
				this.fees_total_amount = c.getString("fees_total_amount").trim();
			} catch (Exception e) {
			}*/

			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.organization_id = c.getString("organization_id").trim();
			} catch (Exception e) {
			}
			try {
				this.payment_amount = c.getString("payment_amount").trim();
			} catch (Exception e) {
			}
			try {
				this.fee_type = c.getString("payment_reason").trim();
			} catch (Exception e) {
			}
			try {
				this.payment_date = c.getString("payment_date").trim();
			} catch (Exception e) {
			}
		}

		public PaymentHistory() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class PaymentReminder {
		public String reminder_id, class_id,payment_date, reminder_send_date, fine_grace_date, fees_reminder_description;
		public JSONObject object;

		public PaymentReminder(JSONObject c) {
			this.object = c;
			try {
				this.reminder_id = c.getString("reminder_id").trim();
			} catch (Exception e) {
			}
			try {
				this.payment_date = c.getString("payment_date").trim();
			} catch (Exception e) {
			}
			try {
				this.reminder_send_date = c.getString("reminder_send_date").trim();
			} catch (Exception e) {
			}
			try {
				this.fine_grace_date = c.getString("fine_grace_date").trim();
			} catch (Exception e) {
			}
			try {
				this.fees_reminder_description = c.getString("fees_reminder_description").trim();
			} catch (Exception e) {
			}
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}
		}

		public PaymentReminder() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class PaymentReminderDeatils {
		public String fees_details_id, fees_amount, fees_name, fine_amount, sub_total,fees_due_date;
		public JSONObject object;

		public PaymentReminderDeatils(JSONObject c) {
			this.object = c;
			try {
				this.fees_details_id = c.getString("fees_details_id").trim();
			} catch (Exception e) {
			}
			try {
				this.fees_amount = c.getString("fees_amount").trim();
			} catch (Exception e) {
			}
			try {
				this.fees_name = c.getString("fees_name").trim();
			} catch (Exception e) {
			}
			try {
				this.fine_amount = c.getString("fine_amount").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_total = c.getString("sub_total").trim();
			} catch (Exception e) {
			}
			try {
				this.fees_due_date = c.getString("fees_due_date").trim();
			} catch (Exception e) {
			}
		}

		public PaymentReminderDeatils() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class GatewayDetails {
		public String gateway_id, pg_image,pg_commission,pg_convenience_fee,merchant_id,merchant_key,created_at,updated_at,order_id;
		public JSONObject object;

		public GatewayDetails(JSONObject c) {
			this.object = c;
			try {
				this.gateway_id = c.getString("pg_master_id").trim();
			} catch (Exception e) {
			}
			try {
				this.pg_image = c.getString("pg_name").trim();
			} catch (Exception e) {
			}
			try {
				this.pg_commission = c.getString("pg_commission").trim();
			} catch (Exception e) {
			}
			try {
				this.pg_convenience_fee = c.getString("pg_convenience_fee").trim();
			} catch (Exception e) {
			}
			try {
				this.merchant_id = c.getString("merchant_id").trim();
			} catch (Exception e) {
			}
			try {
				this.merchant_key = c.getString("merchant_key").trim();
			} catch (Exception e) {
			}
			try {
				this.created_at = c.getString("created_at").trim();
			} catch (Exception e) {
			}
			try {
				this.updated_at = c.getString("updated_at").trim();
			} catch (Exception e) {
			}
			try {
				this.order_id = c.getString("order_id").trim();
			} catch (Exception e) {
			}
		}

		public GatewayDetails() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class PayUMoneyDetails {
		public String txnid, key,merchant_id,amount,salt,firstname,email,phone,productinfo,surl,furl,service_provider,gateway_type,hash;
		public JSONObject object;

		public PayUMoneyDetails(JSONObject c) {
			this.object = c;
			try {
				this.txnid = c.getString("txnid").trim();
			} catch (Exception e) {
			}
			try {
				this.key = c.getString("key").trim();
			} catch (Exception e) {
			}
			try {
				this.merchant_id = c.getString("merchant_id").trim();
			} catch (Exception e) {
			}
			try {
				this.salt = c.getString("salt").trim();
			} catch (Exception e) {
			}
			try {
				this.amount = c.getString("amount").trim();
			} catch (Exception e) {
			}
			try {
				this.firstname = c.getString("firstname").trim();
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
			} catch (Exception e) {
			}
			try {
				this.phone = c.getString("phone").trim();
			} catch (Exception e) {
			}
			try {
				this.productinfo = c.getString("productinfo").trim();
			} catch (Exception e) {
			}
			try {
				this.surl = c.getString("surl").trim();
			} catch (Exception e) {
			}
			try {
				this.furl = c.getString("furl").trim();
			} catch (Exception e) {
			}
			try {
				this.service_provider = c.getString("service_provider").trim();
			} catch (Exception e) {
			}
			try {
				this.gateway_type = c.getString("gateway_type").trim();
			} catch (Exception e) {
			}
			try {
				this.hash = c.getString("hash").trim();
			} catch (Exception e) {
			}
		}

		public PayUMoneyDetails() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class PayUMoneyResponse {
		public String status, firstname,amount,txnid,hash,key,productinfo,email,payuMoneyId,unmappedstatus,bank_ref_num,PG_TYPE,mode;
		public JSONObject object;

		public PayUMoneyResponse(JSONObject c) {
			this.object = c;
			try {
				this.status = c.getString("status").trim();
			} catch (Exception e) {
			}
			try {
				this.firstname = c.getString("firstname").trim();
			} catch (Exception e) {
			}
			try {
				this.amount = c.getString("amount").trim();
			} catch (Exception e) {
			}
			try {
				this.txnid = c.getString("txnid").trim();
			} catch (Exception e) {
			}
			try {
				this.hash = c.getString("hash").trim();
			} catch (Exception e) {
			}
			try {
				this.key = c.getString("key").trim();
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
			} catch (Exception e) {
			}
			try {
				this.productinfo = c.getString("productinfo").trim();
			} catch (Exception e) {
			}
			try {
				this.payuMoneyId = c.getString("payuMoneyId").trim();
			} catch (Exception e) {
			}
			try {
				this.unmappedstatus = c.getString("unmappedstatus").trim();
			} catch (Exception e) {
			}
			try {
				this.bank_ref_num = c.getString("bank_ref_num").trim();
			} catch (Exception e) {
			}
			try {
				this.PG_TYPE = c.getString("pg_TYPE").trim();
			} catch (Exception e) {
			}
			try {
				this.mode = c.getString("mode").trim();
			} catch (Exception e) {
			}
		}

		public PayUMoneyResponse() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class PayTMDetails {
		public String MID, INDUSTRY_TYPE_ID, CHANNEL_ID, WEBSITE, REQUEST_TYPE, ORDER_ID, TXN_AMOUNT, ORDER_DETAILS, CALLBACK_URL, CUST_ID, MOBILE_NO, EMAIL, CHECKSUMHASH;
		public JSONObject object;

		public PayTMDetails(JSONObject c) {
			this.object = c;
			try {
				this.MID = c.getString("MID").trim();
			} catch (Exception e) {
			}
			try {
				this.INDUSTRY_TYPE_ID = c.getString("INDUSTRY_TYPE_ID").trim();
			} catch (Exception e) {
			}
			try {
				this.CHANNEL_ID = c.getString("CHANNEL_ID").trim();
			} catch (Exception e) {
			}
			try {
				this.WEBSITE = c.getString("WEBSITE").trim();
			} catch (Exception e) {
			}
			try {
				this.REQUEST_TYPE = c.getString("REQUEST_TYPE").trim();
			} catch (Exception e) {
			}
			try {
				this.ORDER_ID = c.getString("ORDER_ID").trim();
			} catch (Exception e) {
			}
			try {
				this.TXN_AMOUNT = c.getString("TXN_AMOUNT").trim();
			} catch (Exception e) {
			}
			try {
				this.ORDER_DETAILS = c.getString("ORDER_DETAILS").trim();
			} catch (Exception e) {
			}
			try {
				this.CALLBACK_URL = c.getString("CALLBACK_URL").trim();
			} catch (Exception e) {
			}
			try {
				this.CUST_ID = c.getString("CUST_ID").trim();
			} catch (Exception e) {
			}
			try {
				this.MOBILE_NO = c.getString("MOBILE_NO").trim();
			} catch (Exception e) {
			}
			try {
				this.EMAIL = c.getString("EMAIL").trim();
			} catch (Exception e) {
			}
			try {
				this.CHECKSUMHASH = c.getString("CHECKSUMHASH").trim();
			} catch (Exception e) {
			}
		}

		public PayTMDetails() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class CampusActivity {
		public String id, title, description, created_by, created_date, attachment, file_name;
		public JSONObject object;

		public CampusActivity(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.created_by = c.getString("created_by").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
		}

		public CampusActivity() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

    public static class Hostel {
        public String hostel_code, hostel_name;
        public String hostel_room_no,hostel_bed_no;

        public Hostel() {

        }

        public Hostel(JSONObject c) {
            super();

            try {
                this.hostel_code = c.getString("hostel_code").trim();
            } catch (Exception e) {
            }
            try {
                this.hostel_name = c.getString("hostel_name").trim();
            } catch (Exception e) {
            }
            try {
                this.hostel_room_no = c.getString("hostel_room").trim();

            } catch (Exception e) {
            }
			try {
				this.hostel_bed_no = c.getString("bed").trim();

			} catch (Exception e) {
			}
        }

    }

    public static class Transport {
        public String transport_code, transport_route_name;
        public String transport_desc, transport_pick_point, transport_drop_point;

        public Transport() {

        }

        public Transport(JSONObject c) {
            super();

            try {
                this.transport_code = c.getString("transport_code").trim();
            } catch (Exception e) {
            }
            try {
                this.transport_route_name = c.getString("transport_route_name").trim();
            } catch (Exception e) {
            }
            try {
                this.transport_desc = c.getString("transport_desc").trim();
                ;
            } catch (Exception e) {
            }
            try {
                this.transport_pick_point = c.getString("transport_pick_point").trim();
            } catch (Exception e) {
            }
            try {
                this.transport_drop_point = c.getString("transport_drop_point").trim();
            } catch (Exception e) {
            }
        }

    }

	public static class Career {
		public String title, description, created_by, created_date;
		public JSONObject object;

		public Career(JSONObject c) {
			this.object = c;
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.created_by = c.getString("created_by").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
		}

		public Career() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}
	public static class Interview {
		public String job_title, interview_title, description, submit_date,section_name,created_by;
		public JSONObject object;

		public Interview(JSONObject c) {
			this.object = c;
			try {
				this.job_title = c.getString("job_title").trim();
			} catch (Exception e) {
			}
			try {
				this.interview_title = c.getString("interview_title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.submit_date = c.getString("submit_date").trim();
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
			try {
				this.created_by = c.getString("created_by").trim();
			} catch (Exception e) {
			}
		}

		public Interview() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}
	public static class SuccessfulCandidate {
		public String job_title, interview_title, description, created_date,section_name,student_name;
		public JSONObject object;

		public SuccessfulCandidate(JSONObject c) {
			this.object = c;
			try {
				this.job_title = c.getString("job_title").trim();
			} catch (Exception e) {
			}
			try {
				this.interview_title = c.getString("interview_title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("section_name").trim();
			} catch (Exception e) {
			}
			try {
				this.student_name = c.getString("student_name").trim();
			} catch (Exception e) {
			}
		}

		public SuccessfulCandidate() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class ParentRequest {
		public String parent_request_id, title, description, created_date, status,
				reply, created_by;
		public JSONObject object;

		public ParentRequest(JSONObject c) {
			this.object = c;
			try {
				this.parent_request_id = c.getString("parent_request_id").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.reply = c.getString("reply").trim();
			} catch (Exception e) {
			}
			try {
				this.created_by = c.getString("created_by").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.status = c.getString("status").trim();
			} catch (Exception e) {
			}
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class ParentFeedbackList {
		public String star, fname, mname, lname,comments,feedback_id;
		public JSONObject object;

		public ParentFeedbackList(JSONObject c) {
			this.object = c;
			try {
				this.star = c.getString("star").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.mname = c.getString("mname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.comments = c.getString("comments").trim();
			} catch (Exception e) {
			}
			try {
				this.feedback_id = c.getString("feedback_id").trim();
			} catch (Exception e) {
			}
		}

		public ParentFeedbackList() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class Teachers {
		public String teacher_id, fname, mname, lname,section_id;
		public JSONObject object;

		public Teachers(JSONObject c) {
			this.object = c;
			try {
				this.teacher_id = c.getString("teacher_id").trim();
			} catch (Exception e) {
			}
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.mname = c.getString("mname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
			} catch (Exception e) {
			}
		}

		public Teachers() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class DailyDiary {
		public String user_id, created, id, title,description,file_name,attachment,name,created_date, is_comment,comment;
		public JSONObject object;

		public DailyDiary(JSONObject c) {
			this.object = c;
			try {
				this.user_id = c.getString("user_id").trim();
			} catch (Exception e) {
			}
			try {
				this.created = c.getString("created").trim();
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.file_name = c.getString("file_name").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
			try {
				this.name = c.getString("name").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.is_comment = c.getString("is_comment").trim();
			} catch (Exception e) {
			}
			try {
				this.comment = c.getString("comment").trim();
			} catch (Exception e) {
			}
		}

		public DailyDiary() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class DailyDiaryStudent {
		public String fname, lname, id, roll_no,user_code;
		public JSONObject object;

		public DailyDiaryStudent(JSONObject c) {
			this.object = c;
			try {
				this.fname = c.getString("fname").trim();
			} catch (Exception e) {
			}
			try {
				this.lname = c.getString("lname").trim();
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.roll_no = c.getString("roll_no").trim();
			} catch (Exception e) {
			}
			try {
				this.user_code = c.getString("user_code").trim();
			} catch (Exception e) {
			}
		}

		public DailyDiaryStudent() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class PremiumServiceSubject {
		public String mrks_obtain, fullmarks, highest, student_rank,sub_name;
		public JSONObject object;

		public PremiumServiceSubject(JSONObject c) {
			this.object = c;
			try {
				this.mrks_obtain = c.getString("mrks_obtain").trim();
			} catch (Exception e) {
			}
			try {
				this.fullmarks = c.getString("fullmarks").trim();
			} catch (Exception e) {
			}
			try {
				this.highest = c.getString("highest").trim();
			} catch (Exception e) {
			}
			try {
				this.student_rank = c.getString("student_rank").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_name = c.getString("sub_name").trim();
			} catch (Exception e) {
			}
		}

		public PremiumServiceSubject() {

		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}

	public static class PremiumServiceTotal {
		public String mrks_obtain, total_marks, highest_marks, rank;
		public JSONObject object;

		public PremiumServiceTotal(JSONObject c) {
			this.object = c;
			try {
				this.mrks_obtain = c.getString("mrks_obtain").trim();
			} catch (Exception e) {
			}
			try {
				this.total_marks = c.getString("total_marks").trim();
			} catch (Exception e) {
			}
			try {
				this.highest_marks = c.getString("highest_marks").trim();
			} catch (Exception e) {
			}
			try {
				this.rank = c.getString("rank").trim();
			} catch (Exception e) {
			}
		}
		public PremiumServiceTotal() {

		}
		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}
	}
}
