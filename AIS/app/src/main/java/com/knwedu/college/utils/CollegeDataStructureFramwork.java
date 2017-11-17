package com.knwedu.college.utils;

import org.json.JSONObject;

import android.util.Log;

public class CollegeDataStructureFramwork {
	public static class UserInfo {
		public String id, usertypeid, alt_email, email, mobile_no, alt_mobile_no,
				fname, lname, mname, organizationid, paystatus,
				is_use_practical, is_delete, activity_available,
				test_available, exam_for_org, assignment_available,
				exam_type_setting, default_password, atten_pub_lec_no,
				atten_mark_lec_no, timetable_week, is_use_group_practical,
				is_use_group_project, is_notification_active, is_use_project,
				is_mail_active, caption_lecture, caption_group, caption_level,
				caption_grade, caption_section, timetbl_change,
				special_class_set, caption_class, week_off_index,
				week_start_index, active_session_id, usercode, student_name,
				student_class, student_image, assignment_marking,
				classwork_marking, test_marking,session_student_id;
		public JSONObject object;

		public UserInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
				Log.d("id", c.getString("id").trim());
			} catch (Exception e) {
			}
			try {
				this.usercode = c.getString("user_code").trim();
				Log.d("usercode", c.getString("user_code").trim());
			} catch (Exception e) {
			}
			try {
				this.session_student_id = c.getString("session_student_id").trim();
				Log.d("session_student_id", c.getString("session_student_id").trim());
			} catch (Exception e) {
			}
			try {
				this.assignment_marking = c.getString("assignment_marking")
						.trim();
				Log.d("assignment_marking", c.getString("assignment_marking")
						.trim());
			} catch (Exception e) {
			}
			try {
				this.test_marking = c.getString("test_marking").trim();
				Log.d("test_marking", c.getString("test_marking").trim());
			} catch (Exception e) {
			}
			try {
				this.classwork_marking = c.getString("classwork_marking")
						.trim();
				Log.d("classwork_marking", c.getString("classwork_marking")
						.trim());
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
				this.email = c.getString("email").trim();

			} catch (Exception e) {
			}

			try {
				this.mobile_no = c.getString("mobile_no").trim();
				Log.d("mobile_no", c.getString("mobile_no").trim());
			} catch (Exception e) {
			}
			try {
				this.alt_mobile_no = c.getString("alt_mobile_no").trim();
				Log.d("alt_mobile_no", c.getString("alt_mobile_no").trim());
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
			try {
				this.student_name = c.getString("student_name").trim();
				Log.d("student_image", c.getString("student_image").trim());
			} catch (Exception e) {
			}
			try {
				this.student_class = c.getString("student_class").trim();
				Log.d("student_image", c.getString("student_image").trim());
			} catch (Exception e) {
			}
			try {
				this.student_image = c.getString("student_image").trim();
				Log.d("student_image", c.getString("student_image").trim());
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
				teacher, section_name, class_id, subject_relation_id,
				subject_type, group_name;
		public JSONObject object;
		public boolean check;
		public int row_id;

		public Subject(JSONObject c) {
			this.object = c;

			try {
				this.sub_code = c.getString("subject_code").trim();
			} catch (Exception e) {
			}
			try {
				this.subject = c.getString("subject").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher = c.getString("teacher").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_type = c.getString("subject_type").trim();
			} catch (Exception e) {
			}
			try {
				this.email = c.getString("email").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_relation_id = c.getString("subject_relation_id")
						.trim();
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
				this.classs = c.getString("term_name").trim();
			} catch (Exception e) {
			}
			try {
				this.section_id = c.getString("section_id").trim();
				Log.d("section_id", c.getString("section_id").trim());
			} catch (Exception e) {
			}
			try {
				this.section_name = c.getString("program_name").trim();
			} catch (Exception e) {
			}
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_name = c.getString("subject_name").trim();
			} catch (Exception e) {
			}
			try {
				this.group_name = c.getString("group_name").trim();
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

	public static class SubjectList {
		public String subject_code, program_name, subject_name,
				subject_relation_id, section_name, teacher, term_name, email,
				phone, image, subject_type, group_name;
		public JSONObject object;
		public boolean check;
		public int row_id;

		public SubjectList(JSONObject c) {
			this.object = c;
			try {
				this.subject_type = c.getString("subject_type").trim();
			} catch (Exception e) {
			}
			try {
				this.group_name = c.getString("group_name").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_code = c.getString("subject_code").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_name = c.getString("subject_name").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher = c.getString("teacher").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_relation_id = c.getString("subject_relation_id")
						.trim();
			} catch (Exception e) {
			}

			try {
				this.program_name = c.getString("program_name").trim();
			} catch (Exception e) {
			}
			try {
				this.term_name = c.getString("term_name").trim();
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
				this.image = c.getString("image").trim();
			} catch (Exception e) {
			}
		}

		public SubjectList() {

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
				marks, subject_id, comments,retake,subject_relation_id;
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
				this.subject_relation_id = c.getString("subject_relation_id").trim();
				//Log.d("subject_relation_id_______", c.getString("subject_relation_id").trim());
			} catch (Exception e) {
			}
			try {
				this.retake = c.getString("retake").trim();
				Log.d("retake_____", c.getString("retake").trim());
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
				this.created_date = c.getString("created_at").trim();
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
				this.marks = c.getString("marks").trim();
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

	public static class Announcement {
		public String id, sub_code, section_name, title, description,
				teacher_id, date, sub_name, attachment, file_name, type,subject_relation_id,submit_date,created_date;
		public JSONObject object;

		public boolean check;
		public int row_id;
		public String subject_id, section_id;

		public Announcement() {

		}

		public Announcement(JSONObject c) {
			super();
			this.object = c;
			try {
				this.created_date = c.getString("created_at").trim();
			} catch (Exception e) {
			}
			try {
				this.submit_date = c.getString("submit_date").trim();
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.subject_relation_id = c.getString("subject_relation_id").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.type = c.getString("subject_type").trim();
			} catch (Exception e) {
			}
			try {
				this.description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.date = c.getString("created_at").trim();
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
		public String id, user_code, fullname, email, mobile_no, address,
				alt_email, religion, blood_group, image, fname, mname, lname,
				pincode,image_path;
		public JSONObject object;

		public ParentProfileInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.user_code = c.getString("user_code").trim();
				Log.d("user_id", this.user_code);
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
				Log.d("id", this.id);
			} catch (Exception e) {
			}
			try {
				this.image_path = c.getString("image_path").trim();
				Log.d("image_path", this.image_path);
			} catch (Exception e) {
			}
			try {
				this.fullname = c.getString("name").trim() /*
															 * + " " +
															 * c.getString
															 * ("mname").trim()
															 * + " " +
															 * c.getString
															 * ("lname").trim()
															 */;
				Log.d("name", this.fullname);
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
				this.alt_email = c.getString("alt_email").trim();
				Log.d("alt_email", this.alt_email);
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
				this.pincode = c.getString("pin_code").trim();
				Log.d("pincode", c.getString("pincode").trim());
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
				bloodgroup,image_path;
		public JSONObject object;

		public TeacherProfileInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.user_code = c.getString("user_code").trim();
				Log.d("user_id", this.user_code);
			} catch (Exception e) {
			}
			try {
				this.image_path = c.getString("image_path").trim();
				Log.d("image_path", this.image_path);
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
				Log.d("pincode", c.getString("pincode").trim());
			} catch (Exception e) {
			}
			try {
				this.bloodgroup = c.getString("blood_group").trim();
				Log.d("bloodgroup", this.bloodgroup);
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
				mname, lname, fullname, class_name, class_roll, email, name,
				mobile_no, address, religion, blood_group, ailment, hostel, id,
				transport, disease, image, alt_email, pin_code, section_id,
				program,image_path;
		public JSONObject object;


		public StudentProfileInfo(JSONObject c) {
			super();
			this.object = c;
			try {
				this.user_id = c.getString("user_id").trim();
				Log.d("user_id", this.user_id);
			} catch (Exception e) {
			}
			try {
				this.image_path = c.getString("image_path").trim();
				Log.d("image_path", this.image_path);
			} catch (Exception e) {
			}
			try {
				this.name = c.getString("name").trim();
			} catch (Exception e) {
			}
			try {
				this.id = c.getString("id").trim();
				Log.d("id", this.id);
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
				this.class_name = c.getString("program").trim() + " "
						+ c.getString("term").trim();
				;
				Log.d("class_name", this.class_name);
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
				this.program = c.getString("program").trim();
				Log.d("program", c.getString("program").trim());
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
	public static class Campus {
		public String id,description, attachment, file_name,created_at,title;
		public JSONObject object;
		public boolean check;

		public Campus(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
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
				this.created_at = c.getString("created_at").trim();
			} catch (Exception e) {
			}
			try {
				this.title = c.getString("title").trim();
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
				section_id, teacher_id,retake;
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
				this.retake = c.getString("retake").trim();
				Log.d("retake_____", c.getString("retake").trim());
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
				group_id = "", lecture_num = "", sub_id = "", teacher_id = "",
				reason = "";
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
				this.reason = c.getString("reason").trim();

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
				this.student_id = c.getString("id").trim();
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
		public String id, class_id, fname, lname, group_id, classname, date,
				is_mark, sub_name, teacher_id, sub_id, sub_code, section_id,
				section_name, lecture_num, srid, timetable_id, program,
				program_term_id;
		public JSONObject object;
		public boolean check;

		public TeacherSubjectByTimeTable(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.class_id = c.getString("class_id").trim();
			} catch (Exception e) {
			}
			try {
				this.timetable_id = c.getString("timetable_id").trim();
			} catch (Exception e) {
			}
			try {
				this.program = c.getString("program").trim();
			} catch (Exception e) {
			}
			try {
				this.program_term_id = c.getString("program_term_id").trim();
			} catch (Exception e) {
			}
			try {
				this.srid = c.getString("subject_relation_id").trim();
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
				this.classname = c.getString("term").trim();
			} catch (Exception e) {
			}
			try {
				this.sub_name = c.getString("subject_name").trim();
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
				this.section_name = c.getString("program").trim();
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

	public static class Program {
		public String level_id, level_name, program_id, program_name, term_id,
				term_name;
		public boolean check;
		public JSONObject object;

		public Program(JSONObject c) {
			super();
			this.object = c;
			try {
				this.level_id = c.getString("level_id").trim();
			} catch (Exception e) {
			}
			try {
				this.level_name = c.getString("level_name").trim();
			} catch (Exception e) {
			}
			try {
				this.program_id = c.getString("program_id").trim();
			} catch (Exception e) {
			}
			try {
				this.program_name = c.getString("program_name").trim();
			} catch (Exception e) {
			}
			try {
				this.term_id = c.getString("term_id").trim();
			} catch (Exception e) {
			}
			try {
				this.term_name = c.getString("term_name").trim();
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

	public static class Carr {
		public String id, title, description, date, status, doc, file_name,prog_name,term_name,created_by;
		public JSONObject object;

		public Carr(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.prog_name = c.getString("program_name").trim();
			} catch (Exception e) {
			}
			try {
				this.term_name = c.getString("term_name").trim();
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
				this.date = c.getString("created_at").trim();
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
			try {
				this.created_by = c.getString("created_by").trim();
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

	public static class Carreer {
		public String id, job_title, job_description, organizaton_id,
				given_by, created_date, updated_at, is_delete, interview_date,student_name;
		public JSONObject object;

		public Carreer(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.job_title = c.getString("title").trim();
			} catch (Exception e) {
			}
			try {
				this.student_name = c.getString("student_name").trim();
			} catch (Exception e) {
			}
			try {
				this.interview_date = c.getString("interview_date").trim();
			} catch (Exception e) {
			}
			try {
				this.job_description = c.getString("description").trim();
			} catch (Exception e) {
			}
			try {
				this.organizaton_id = c.getString("organization_id").trim();
			} catch (Exception e) {
			}
			try {
				this.given_by = c.getString("given_by").trim();
			} catch (Exception e) {
			}
			try {
				this.created_date = c.getString("created_date").trim();
			} catch (Exception e) {
			}
			try {
				this.updated_at = c.getString("updated_at").trim();
			} catch (Exception e) {
			}
			try {
				this.is_delete = c.getString("is_delete").trim();
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
				created_date, date_to, date_from, status, reject_reason,file_name,attachment,show_date;
		public JSONObject object;
		public boolean check;

		public RequestsStatus(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.show_date = c.getString("show_date").trim();
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
				this.reason_title = c.getString("title").trim();
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
				this.date_from = c.getString("date_end").trim();
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

		public StatusStudent(){

		}

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
				this.subject = c.getString("subject").trim();
			} catch (Exception e) {
			}
			try {
				this.lecture = c.getString("lecture_num").trim();
			} catch (Exception e) {
			}
			try {
				this.late_reason = c.getString("reason").trim();
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
				this.roll_no = c.getString("user_code").trim();
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

	public static class ExamList {
		public String is_publish, is_schedule, id, section_id, class_id,
				name_of_exam, exam_term_id, class_name, section_name,
				is_result_publish, term_init_date;
		public boolean check;
		public JSONObject object;

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
		public String name, activity, date, description;
		public JSONObject object;
		public boolean check;

		public Orginization(JSONObject c) {
			this.object = c;
			try {
				this.name = c.getString("org_name").trim();
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
		public String fname, lname, class_name;
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
		public String sub_name, Out_of_marks, sub_id, Marks;
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

	public static class MenuItems {
		public String id, module_name, caption;
		public boolean check;
		public JSONObject object;

		public MenuItems(JSONObject c) {
			super();
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.module_name = c.getString("module_name").trim();
			} catch (Exception e) {
			}
			try {
				this.caption = c.getString("caption").trim();
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
				Title, Subject, Marks, Total_Marks, Type, header;
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
				this.Subject = c.getString("Subject").trim();
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

	public static class CDirectory {
		public String  designition, name, address, phone1, phone2, email;
		public JSONObject object;
		public boolean check;

		public CDirectory(){

		}

		public CDirectory(JSONObject c) {
			this.object = c;
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

	public static class userType {
		public String id, user_type_name;
		public JSONObject object;
		public boolean check;

		public userType() {

		}

		public userType(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.user_type_name = c.getString("user_type_name").trim();
				Log.d("user_type_name", c.getString("user_type_name").trim());
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

	public static class Feedback {
		public String id, teacher, description, date;
		public JSONObject object;
		public boolean check;

		public Feedback(JSONObject c) {
			this.object = c;
			try {
				this.id = c.getString("id").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher = c.getString("teacher").trim();
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
		}

		@Override
		public String toString() {
			if (object != null) {
				return object.toString();
			}
			return null;
		}

	}


	public static class SpecialClass {

		public String id, title, description, sdate,class_time,facility,teacher,
				attachment, program,term,session;
		public JSONObject object;
		public boolean check;

		public SpecialClass(JSONObject c) {
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
				this.program = c.getString("program").trim();
			} catch (Exception e) {
			}
			try {
				this.term = c.getString("term").trim();
			} catch (Exception e) {
			}
			try {
				this.sdate = c.getString("date").trim();
			} catch (Exception e) {
			}
			try {
				this.session = c.getString("session").trim();
			} catch (Exception e) {
			}
			try {
				this.class_time = c.getString("class_time").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
			try {
				this.teacher = c.getString("teacher").trim();
			} catch (Exception e) {
			}
			try {
				this.facility = c.getString("facility").trim();
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
				this.created_date = c.getString("created_at").trim();
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

	public static class TimeTable {
		public String subject, subject_name, week_day, lecture_num, section_id,
				original_lecture_num, classs, section, className, start, end,
				facility, teacher_name, is_project, is_practical, is_selective,
				week_index, day_index, group_id, group_name, sr_id, teacher_id,
				subject_type, class_type, program;
		public boolean check;
		public JSONObject object;

		public TimeTable(JSONObject c) {
			super();
			this.object = c;
			try {
				this.subject = c.getString("subject_name").trim();
			} catch (Exception e) {
			}
			try {
				this.program = c.getString("program").trim();
			} catch (Exception e) {
			}
			try {
				this.class_type = c.getString("class_type").trim();
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
				this.section_id = c.getString("program").trim();
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
				this.facility = c.getString("term").trim();
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
	/*
	 * "requests_view": "1", "requests_create": "0", "requests_edit": "0",
	 * "requests_delete": "0", "requests_mark": "0", "requests_publish": "0",
	 */
	public static class CollegePermission {
		public String assignment_add, test_add, activity_add, announcement_add,
				lessons_add, classwork_add, attendance_type,request_view,request_create,request_edit,
				request_delete,request_mark,request_publish,assignment_create,test_create,announcement_create,classwork_create,assignment_mark,
				assignment_publish,test_mark,test_publish,specialclass_create,attendance_mark,lessons_edit,feedback_create;
		public JSONObject object;

		public CollegePermission(JSONObject c) {
			super();
			this.object = c;
			try {
				this.lessons_edit = c.getString("lessons_edit").trim();
				Log.d("lessons_edit", c.getString("lessons_edit").trim());
			} catch (Exception e) {
			}
			this.object = c;
			try {
				this.feedback_create = c.getString("feedback_create").trim();
				Log.d("feedback_create", c.getString("feedback_create").trim());
			} catch (Exception e) {
			}
			try {
				this.assignment_add = c.getString("assignment_add").trim();
				Log.d("assignment_add", c.getString("assignment_add").trim());
			} catch (Exception e) {
			}
			try {
				this.assignment_create = c.getString("assignment_create").trim();
				Log.d("assignment_create", c.getString("assignment_create").trim());
			} catch (Exception e) {
			}
			try {
				this.specialclass_create = c.getString("specialclass_create").trim();
				Log.d("specialclass_create", c.getString("specialclass_create").trim());
			} catch (Exception e) {
			}
			try {
				this.announcement_create= c.getString("announcement_create").trim();
				Log.d("announcement_create", c.getString("announcement_create").trim());
			} catch (Exception e) {
			}
			try {
				this.classwork_create = c.getString("classwork_create").trim();
				Log.d("classwork_create", c.getString("classwork_create").trim());
			} catch (Exception e) {
			}
			try {
				this.test_create = c.getString("test_create").trim();
				Log.d("test_create", c.getString("test_create").trim());
			} catch (Exception e) {
			}
			try {
				this.attendance_mark = c.getString("attendance_mark").trim();
				Log.d("attendance_mark", c.getString("attendance_mark").trim());
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
			try {
				this.request_create = c.getString("requests_create").trim();
				Log.d("requests_create", c.getString("requests_create").trim());
			} catch (Exception e) {
			}
			try {
				this.request_delete = c.getString("requests_delete").trim();
				Log.d("requests_delete", c.getString("requests_delete").trim());
			} catch (Exception e) {
			}
			try {
				this.request_edit = c.getString("requests_edit").trim();
				Log.d("requests_edit", c.getString("requests_edit").trim());
			} catch (Exception e) {
			}
			try {
				this.request_mark = c.getString("requests_mark").trim();
				Log.d("requests_mark", c.getString("requests_mark").trim());
			} catch (Exception e) {
			}
			try {
				this.request_publish = c.getString("requests_publish").trim();
				Log.d("requests_publish", c.getString("requests_publish").trim());
			} catch (Exception e) {
			}
			try {
				this.request_view = c.getString("requests_view").trim();
				Log.d("requests_view", c.getString("requests_view").trim());
			} catch (Exception e) {
			}
			try {
				this.assignment_mark = c.getString("assignment_mark").trim();
				Log.d("assignment_mark", c.getString("assignment_mark").trim());
			} catch (Exception e) {
			}
			try {
				this.assignment_publish = c.getString("assignment_publish").trim();
				Log.d("assignment_publish", c.getString("assignment_publish").trim());
			} catch (Exception e) {
			}
			try {
				this.test_mark= c.getString("test_mark").trim();
				Log.d("test_mark", c.getString("test_mark").trim());
			} catch (Exception e) {
			}
			try {
				this.test_publish = c.getString("test_publish").trim();
				Log.d("test_publish", c.getString("test_publish").trim());
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

	/**
	 * Syllabus wise subject names
	 * @author Rajhrita
	 *
	 */
	public static class SyllabusSubject {
		public String id,title, description, attachment;
		public JSONObject object;

		public SyllabusSubject(JSONObject c) {
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
				this.description = c.getString("desc").trim();
			} catch (Exception e) {
			}
			try {
				this.attachment = c.getString("attachment").trim();
			} catch (Exception e) {
			}
		}

		public SyllabusSubject() {

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
		public String reminder_id, program_id,payment_date, reminder_send_date, fine_grace_date, fees_reminder_description,program_term_id;
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
				this.program_id = c.getString("program_id").trim();
			} catch (Exception e) {
			}
			try {
				this.program_term_id = c.getString("program_term_id").trim();
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



}
