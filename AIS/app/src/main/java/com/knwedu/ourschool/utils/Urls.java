package com.knwedu.ourschool.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class Urls {
	
	public static final String url_institute_list = "http://119.81.123.116/knwall_comercial/superadmin/android-api";
	public static final String url_ais_institute_list = "https://kolkataschool.knwedu.com/mobile/getOrganizationDetails";
//	public static final String url_institute_list = "http://166.62.85.235/superadmin/android-api";
	public static final String url_region_list = "http://119.81.123.116/knwall_comercial/superadmin/regions";
	//public static final String url_playstore_version_common = "http://119.81.123.116/knwall_comercial/superadmin/version";
	public static final String url_playstore_version_common = "http://superadmin.knwedu.com/version";
	public static final String api_class_section_list = "information/getClassSec";
	public static final String api_class_section_add = "information_new/insertNew";
	public static final String api_registration = "information_new/saveUserInfo";

	public static String base_url;
	public static String upload_url;
	public static String upLoadServerUri;
	public static String upLoadServerUriweeklyplan;
	//Upload File from create Daily Dairy
	public static String upLoadFileFromDailyDiary;
	public static String upload_announcement_classwork_upload;

	public static String image_url_userpic;
	public static String image_url_blog;
	//Behaviour
	public static String image_url_behaviour;
	public static String api_get_bulletin_doc;
	public static String api_get_weeklyplan_doc;

	public static String api_get_campus_doc;
	public static String api_get_payment_invoice;

	public static String api_get_payment_invoice_new;
	public static String api_download_fee_doc ;
	//Download Attachment for DailyDiary
	public static String api_get_dialy_diary_attachment;
	public static String api_get_doc;
	public static String api_get_schedule_doc;
	public static String api_get_announcement_classwork_doc;
	public static String api_get_bulatine_doc;
	public static String api_get_request_doc;
	public static String api_get_doc_exam;
	public static String api_ais_class_section_list  = "mobile/getAllClassSectionAIS";
	public static String api_aims_class_section_list = "mobile/getAllClassSectionAIMS";
	public static String api_aims_get_student = "mobile/getStudentBysction";
	public static String api_aims_check_student_status = "mobile/checkStudentStatus";
	public static String api_aims_get_student_new = "mobile/getStudentList";
	public static String api_aims_student_list = "mobile/getStudentlistBysection";
	public static String api_aims_get_subject = "mobile/getSubjectBySectionId";
	public static String api_ais_subscribe = "mobile/assignRoute";
	public static String api_ais_save_location = "mobile/postLatLong";
	public static String api_ais_switch_change = "mobile/switchNoti";
	public static String api_ais_remove_alert = "mobile/delNoti";
	public static String api_ais_get_bus_location = "mobile/getlatLongforMobile";
	public static String api_ais_syllabus_list = "mobile/getAisSchedule";
	public static String api_ais_help = "mobile/sendMail";
	public static String api_ais_exam_list = "exams/getAisSchedule";
	public static String api_ais_vehicle_status = "mobile/vehicleStatus";
	public static String api_ais_bus_list = "mobile/getRouteDetailsAndroid";
	public static String api_ais_alert_list = "mobile/getPosition";

	public static String api_ais_student_parent_register = "mobile/saveAisData";
	public static String api_ais_teacher_register = "mobile/teacherSignUpAIS";
	public static String api_aims_register ="mobile/saveAimsData";

	public static String api_ais_add_child = "mobile/addMoreChild";
	public static String api_aims_add_child = "mobile/addMoreChildAims";
	public static String api_ais_daily_diary = "mobile/getDiaryDetails";

	public static String api_ais_getCommission = "mobile/getPaymentGatewayAIS";
	public static String api_ais_getCcavenueDetails = "mobile/getCcavenueParamsAIS";
	public static String api_aims_getCcavenueDetails = "mobile/getCCavenueParams";
	public static String api_ais_techer_get_subject = "mobile/teacherSubjectListAISAndroid";

	public static String api_ais_subject_save = "mobile/teacherSelfSubjectAssignAIS";
	public static String api_ais_exam_download = "exams/download";
	public static String api_ais_syllabus_downlaod = "mobile/download";
	public static String api_ais_daily_diary_download = "mobile/downloadDiary";
	public static String api_get_Organization_details="mobile/getorganizationDetails";
	public static String api_login = "login/loginMobile";
	public static String api_signout = "login/signOutMobile";
	public static String api_org_forget_password = "login/forgetPassMob";
	public static String api_splash_version = "login/MobileSplashVersionCheck";
	public static String api_students_info = "mobile/ofllineGetStudent";
	public static String api_blog_comment_delete = "broadcast/blog_delete_user";
	public static String api_profile_info = "user/getProfile";
	public static String api_child_profile_info = "user/getChildProfile";
	public static String api_change_password = "user/changePass";
	public static String api_teacher_subject_students = "user/getStudentsBySec";
	public static String api_edit_profile = "user/saveProfile";
	public static String api_parent_childrens = "user/getChildren";
	public static String api_get_student_by_section = "user/getStudentBySection";
	public static String api_parent_child_request_save = "user/saveRequest";
	public static String api_parent_child_request_upload = "user/upload";
	public static String api_parent_request_status = "user/getAllRequest";
	public static String api_save_mail_mobile = "user/saveMailMobile";

	public static String api_attendance_info = "mobile/mobile_attendence";
	public static String api_offline_attendance_student = "mobile/mobile_attendence_offline";
	public static String api_activity = "activities/getActivities";
	public static String api_activity_save = "activities/save";
	public static String ais_api_daily_diary_save = "mobile/saveDiary";
	public static String api_activity_publish = "activities/publish";
	public static String api_activity_get = "activities/getActivities";
	public static String api_activity_save_marks = "activities/saveMarks";
	public static String api_activity_get_marks = "activities/getMarks";
	public static String api_activity_get_subjects = "activities/getSubjects";
	public static String api_activity_get_subjects_weeklyplan = "mobile/getAllSubjectforWeekly";
	
	public static String api_activity_get_class_section = "activities/getClassSection";
	public static String api_graph = "activities/compareMarks";
	public static String api_graph_exam_result = "exams/graph_data_mobile";
	public static String api_graph_exam_term = "exams/comparison_graph_data_mobile";
	public static String api_org_activity = "activities/getOrgActivity";
	public static String api_report_card_download_mobile = "mobile/getResultScholastic";
	
	public static String api_blog_list = "broadcast/getBlog";
	public static String api_blog_comment = "broadcast/getComment";
	public static String api_blog_comment_add = "broadcast/insertComment";
	public static String api_announcement_create = "broadcast/create";

	//Behaviour
	public static String api_behaviour_create = "activities/saveBehaviour";
	public static String api_announcement_view = "broadcast/view";
	public static String api_behaviour_view = "activities/getBehaviour";
	public static String api_get_grade_card = "activities/getgradeCard";

	public static String api_schedule_save = "broadcast/save";
	public static String api_weekly_plan_save = "mobile/editWeeklyPlan";
	public static String api_broadcast_viewbulletin = "broadcast/viewBulletin";
	public static String api_get_week_lesson = "broadcast/getWeekLesson";
	public static String api_get_week_plan = "mobile/viewAllWeeklyPlan";
	public static String api_get_week_plan_list = "mobile/viewAllDateOfWeeklyPlan";

	public static String api_get_all_week_mob = "mobile/getAllWeekMob";
	public static String api_get_all_type_mob = "mobile/getAllTypeMob";
	public static String api_get_create_week_plan = "mobile/createWeeklyPlanMob";
	public static String api_save_week_plan = "mobile/saveWeeklyPlanMob";

	
	public static String api_activity_get_subjects_teacher = "mobile/getAllSubjectForTeacher";
	public static String api_submit_all_attendance = "mobile/submitallattendance";
	public static String api_get_all_student_attendance = "mobile/getAllStudentForAttendance";
	public static String api_timetable_user = "mobile/getTimeTableForUser";
	public static String api_attendance_student_parent = "mobile/getAttenForStuPar";

	public static String api_exam_list = "exams/exam";
	public static String api_exam_schedule = "exams/schedule";
	public static String api_report_card = "exams/report_card";
	public static String api_exam_result = "exams/student_result";

	public static String api_school_info = "mobile/getAllSchoolDirectory";
	public static String api_password = "login/checkpassword";
	public static String api_teacher_offline_attendance_user = "mobile/getTeacherMarkAttendance";
	public static String api_class_fellow ="user/getStudent";
	public static String api_reply_bulletin_save = "information/replyBulletin";
	public static String api_special_class = "broadcast/getSpecialclass";
	public static String api_privacy_policy = "https://www.knwedu.com/mobile-privacy.html";
	public Urls(String base) {
		base_url = base;
		upload_url = base_url + "activities/upload/UploadToServer.php";
		upLoadServerUri = base_url + "broadcast/upload/";
		upLoadServerUriweeklyplan = base_url + "mobile/uploadWeeklyPlanEdit";
		upLoadFileFromDailyDiary = base_url + "mobile/uploadDailyDiary";
		upload_announcement_classwork_upload = base_url
				+ "broadcast/uploadBroadcast";

		URL netUrl;
		try {
			netUrl = new URL(base_url);
		    String host = netUrl.getHost().split("\\.")[0];
			image_url_userpic = base_url + "uploads/" + host + "/user_pic/";
			image_url_blog = base_url + "uploads/" + host + "/blog/";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		api_get_doc = base_url + "activities/download/";
		api_get_bulletin_doc = "broadcast/download_mob_blog";
		api_get_schedule_doc = base_url + "broadcast/download/";
		api_get_weeklyplan_doc = base_url + "mobile/downloadWeeklyPlan";
		api_get_announcement_classwork_doc = base_url
				+ "broadcast/downloadBroadcast/";
		api_get_bulatine_doc = base_url + "broadcast/downloadBulletin/";
		api_get_request_doc = base_url + "user/download/";
		api_get_doc_exam = base_url + "exams/download_mob/";
		api_get_campus_doc = base_url + "activities/downloadcampus";
		api_get_payment_invoice = base_url + "mobile/generateInvoice";

		api_get_payment_invoice_new= base_url + "mobile/generatePaymentInvoice";
		api_download_fee_doc= base_url +"mobile/downloadFeeDoc";
		//Daily Diary
		api_get_dialy_diary_attachment = base_url + "mobile/downloadDailyDiary";

		// Behaviour
		image_url_behaviour = base_url + "uploads/kolkataschool/behaviour/";

	}

	// Phase 2 modules
	public static String api_web_access = "mobile/web_access";
	public static String api_syllabus = "mobile/getsyllabus";
	public final static String api_event = "mobile/getEventDetails";
	public final static String api_get_tracking_details = "mobile/getTrackingDetails";
	public final static String api_tracking_details = "mobile/getMapMobile/";
	public static String api_hostel = "activities/getHostel";
	public static String api_transport = "activities/getTransport";

	// Manage Fees
	public static String api_payment_generate_checksum = "mobile/feePaymentGenerateChecksumMobile";
	public static String api_payment_verification_checksum = "mobile/feePaymentVerifyChecksumMobile";
	public static String api_get_payment_history_details = "mobile/getFeeDetailsMobile";
	public static String api_get_payment_reminder_details = "mobile/getReminderDetailsMobile";
	public static String api_get_payment_details = "mobile/getStudentPaymentDetails";

	//PayUMoney
	public static String api_payment_generate_checksum_payumoney = "mobile/feePaymentPayumoneyMobile";
	//PayTM
	public static String api_paytm_details = "mobile/getPaytmParams";

	//Payment success url
	public static String api_payumoney_success = "mobile/responsePayumoney";
	public static String api_paytm_success = "mobile/paytmResponse";

	// Campus
	public static String api_campus_details = "activities/getCampus";

	// Career
	public static String api_career_list = "activities/getCareer";
	public static String api_interview_list = "activities/getInterview";
	public static String api_success_list = "activities/getSuccesscandidate";

	//Attendance View
	public static String api_attendance_view = "mobile/getAllStudentAttendance";

	//Parent Request
	public static String api_request_parent = "mobile/parentRequestMobile";
	public static String api_save_request_parent = "mobile/saveRequestForParent";

	//Parent Feedback
	public static String api_get_parent_feedback = "activities/getParentFeddback";
	public static String api_delete_parent_feedback = "activities/getDeleteFeedback";
	public static String api_get_teachers = "activities/getTeacherList";
	public static String api_save_given_feedback = "activities/saveFeedback";

	//Attendance View in Calendar
	public static String api_student_attendance = "mobile/getAllStudentAttendance";
	public static String api_get_attendance_setting = "mobile/getOneTimeAttendanceSettings";

	//Daily Diary
	public static String api_get_daily_diary_details = "mobile/getDailyDiaryDetails";
	public static String api_save_daily_comment = "mobile/saveDiaryComment";
	public static String api_delete_daily_diary = "mobile/getDeleteDiary";
	public static String api_create_daily_diary = "mobile/createDailyDiary";
	public static String api_edit_daily_diary = "mobile/saveEditDiary";
	public static String api_get_student_details = "mobile/getStudentClassSectionDetails";
	public static String api_create_daily_diary_parent = "mobile/createDailyDiaryParent";

	// Premium Service
	public static String api_premium_service_login = "premiumgraph/getLogin";
	public static String api_premium_graph_sujectwise = "mobile/getInitialPremiumGraph";
	public static String api_premium_graph_total = "mobile/getInitialPremiumGraphTotal";
	public static String api_premium_service_status_check = "mobile/getPremiumPaymentStatus";


	//notification
	public static String api_updatenotification = "mobile/updateBadgeCount";

	//get registration number for new aims fees module

	public static String api_get_registration_no_mobile = "mobile/getRegistrationNumber";

	//get documents for aims fee

	public static String api_get_documents_aims_fee = "mobile/getFeeDoc";
	//payment history new
	public static String api_get_payment_history_details_new = "mobile/getFeeHistory";





}
