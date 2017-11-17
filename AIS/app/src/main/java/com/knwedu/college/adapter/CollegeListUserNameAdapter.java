package com.knwedu.college.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.CollegeTeacherMainActivity;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.CollegePermission;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TimeTable;
import com.knwedu.college.utils.CollegeDataStructureFramwork.UserInfo;
import com.knwedu.college.utils.CollegeDataStructureFramwork.userType;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeListUserNameAdapter extends BaseAdapter {
	Context context;
	// private String roleId;
	private String id;
	ArrayList<userType> userTypeList;
	private LayoutInflater inflater;
	private ArrayList<TimeTable> timeTable;
	ViewHolder holder;
	public int[] check;
	private ArrayList<UserInfo> userInfoList;
	private ProgressDialog dialog;
	public CollegeDBAdapter mDatabase;

	public CollegeListUserNameAdapter(Context context,
			ArrayList<userType> userTypeList) {
		this.context = context;
		this.userTypeList = userTypeList;
		check = new int[this.userTypeList.size()];
		// this.roleId = roleId;
		mDatabase = ((com.knwedu.ourschool.db.SchoolApplication) context.getApplicationContext()).getCollegeDatabase();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}



	@Override
	public int getCount() {

		return userTypeList.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.college_custom_dialog_list_items, null);
			holder = new ViewHolder();
			holder.txtNamae = (TextView) convertView
					.findViewById(R.id.textUserName);
			holder.username = (RadioButton) convertView
					.findViewById(R.id.rdBtnUserName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CollegeAppUtils.setFont(context, holder.txtNamae);

		holder.txtNamae.setText(userTypeList.get(position).user_type_name);
		holder.username
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						/*
						 * roleId = CollegeAppUtils.GetSharedParameter(context,
						 * "role_id");
						 */
						id = CollegeAppUtils.GetSharedParameter(context, "id");
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								1);
						nameValuePairs.add(new BasicNameValuePair("role_id",
								userTypeList.get(position).id));
						nameValuePairs.add(new BasicNameValuePair("id", id));
						Log.d("Paramzs....", "" + nameValuePairs);
						new UserTypeAsync().execute(nameValuePairs);

					}
				});
		return convertView;

	}

	private class ViewHolder {
		RadioButton username;
		TextView txtNamae;
	}

	class UserTypeAsync extends AsyncTask<List<NameValuePair>, String, Boolean> {
		UserInfo userInfo;
		String error;
		CollegePermission per;

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			userInfoList = new ArrayList<UserInfo>();
			userTypeList = new ArrayList<userType>();
			List<NameValuePair> nvp = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					CollegeUrls.api_user_type);

			try {
				if (json.getString("result").equalsIgnoreCase("1")) {
					JSONArray jsonData = json.getJSONArray("data");
					for (int i = 0; i < jsonData.length(); i++) {
						userInfo = new UserInfo(jsonData.getJSONObject(i));
						SchoolAppUtils.SetSharedParameter(context,
								Constants.USERID, userInfo.id);
						CollegeAppUtils.SetSharedParameter(context, "id",
								userInfo.id);
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.USERTYPEID,
								userInfo.usertypeid);
						CollegeAppUtils.SetSharedParameter(context,
								"student_name", userInfo.student_name);
						CollegeAppUtils.SetSharedParameter(context,
								"student_class", userInfo.student_class);
						CollegeAppUtils.SetSharedParameter(context,
								"session_student_id",
								userInfo.session_student_id);
						CollegeAppUtils.SetSharedParameter(context,
								Constants.CHILD_ID,
								userInfo.session_student_id);

						CollegeAppUtils.SetSharedParameter(context,
								"student_image", userInfo.student_image);
						//CollegeAppUtils.SetSharedParameter(context,
						//		CollegeConstants.UORGANIZATIONID, "1");
						CollegeAppUtils.SetSharedParameter(context, CollegeConstants.UORGANIZATIONID,
								userInfo.organizationid);
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.UALTEMAIL, userInfo.alt_email);
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.UMOBILENO,
								userInfo.alt_mobile_no);
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.USERTYPEID,
								userInfo.usertypeid);
						userInfoList.add(userInfo);

					}
					String menu_tag = json.getString("menu_info") + "|webaccess";
					menu_tag.split("\\|");
					String menu_title = json.getString("menu_caption") + "|Web Access";
					JSONObject permission_add = json
							.getJSONObject("permissions");
					if (jsonData != null) {
						per = new CollegePermission(permission_add);
					}
					if (jsonData != null) {

					
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.COLLEGE_SPECIALCLASS_CREATE,
								per.specialclass_create.toString());

						// attendance mark
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.COLLEGE_ATTENDANCE_MARK,
								per.attendance_mark.toString());

						// lessons edit
						if(per.lessons_edit != null){
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.COLLEGE_LESSONS_EDIT,
								per.lessons_edit.toString());}

						// marking & publish

						if (menu_tag
								.contains(Constants.MENU_TEACHER_ASSIGNMENT)) {
							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_ASSIGNMENT_CREATE,
											per.assignment_create
													.toString());
							Log.d("VALUE OF ASSIGNMENT CREATE...",
									per.assignment_create.toString());

							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_ASSIGNMENT_MARK,
											per.assignment_mark
													.toString());

							Log.d("VALUE OF ASSIGNMENT MARK...",
									per.assignment_mark.toString());
							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH,
											per.assignment_publish
													.toString());

							Log.d("VALUE OF ASSIGNMENT PUBLISH...",
									per.assignment_publish.toString());
						}
						if (menu_tag
								.contains(Constants.MENU_TEACHER_ANNOUNCEMENT)) {
							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_ANNOUNCEMENT_CREATE,
											per.announcement_create
													.toString());
						}
						if (menu_tag
								.contains(Constants.MENU_TEACHER_COURSE_WORK)) {
							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_CLASSWORK_CREATE,
											per.classwork_create
													.toString());
						}
						if (menu_tag
								.contains(Constants.MENU_TEACHER_QUIZ)) {
							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_INTERNAL_CREATE,
											per.test_create.toString());
							// marking & publish

							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_INTERNAL_MARK,
											per.test_mark.toString());

							Log.d("VALUE OF TEST MARK...",
									per.test_mark.toString());
							CollegeAppUtils
									.SetSharedParameter(
											context,
											CollegeConstants.COLLEGE_INTERNAL_PUBLISH,
											per.test_publish.toString());

							Log.d("VALUE OF TEST PUBLISH...",
									per.test_publish.toString());
						}

						// attendance mark

						if (!CollegeAppUtils.GetSharedParameter(context,
								CollegeConstants.USERTYPEID).equalsIgnoreCase(
								CollegeConstants.USERTYPE_TEACHER)) {
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_REQUEST_CREATE,
									per.request_create.toString());
							Log.d("VALUE OF REQUEST CREATE...",
									per.request_create.toString());

							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_REQUEST_DELETE,
									per.request_delete.toString());
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_REQUEST_EDIT,
									per.request_edit.toString());
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_REQUEST_MARK,
									per.request_mark.toString());
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_REQUEST_PUBLISH,
									per.request_publish.toString());
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_REQUEST_VIEW,
									per.request_view.toString());

							// feedback create
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_FEEDBACK_CREATE,
									per.feedback_create.toString());

							// feedback create
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.COLLEGE_FEEDBACK_CREATE,
									per.feedback_create.toString());

						}

					}

					if (menu_tag != null) {
						Log.d("menu", menu_tag.toString());
						CollegeAppUtils
								.SetSharedParameter(context,
										CollegeConstants.MENU_TAGS,
										menu_tag.toString());
						CollegeAppUtils.SetSharedParameter(context,
								CollegeConstants.MENU_TITLES,
								menu_title.toString());
					}

					return true;
				} else {
					try {
						error = json.getString("data");
					} catch (Exception e) {
					}
					return false;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						0);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(context, "id")));
				Log.d("PARAMS OF TEACHER", "" + nameValuePairs);
				new GetTimetableListAsyntaskCollege().execute(nameValuePairs);
				
			}
		}
	}

	private class GetTimetableListAsyntaskCollege extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(context);
			dialog.setTitle(context.getResources().getString(R.string.fetching_offline));
			dialog.setMessage(context.getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			timeTable = new ArrayList<TimeTable>();
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					CollegeUrls.api_timetable_user);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						timeTable = new ArrayList<TimeTable>();
						for (int i = 0; i < array.length(); i++) {
							TimeTable attendance = new TimeTable(
									array.getJSONObject(i));
							timeTable.add(attendance);
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

			if (timeTable != null) {
				if (timeTable.size() > 0) {
					mDatabase.open();
					// Delete all data in database
					mDatabase.deleteAllTimetable();
					mDatabase.close();
					for (int i = 0; i < timeTable.size(); i++) {
						Log.d("Insert: ", "Inserting ..");
						mDatabase.open();
						mDatabase.addTimetable(timeTable.get(i));
						mDatabase.close();
					}
					

				}
			}
			if (CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_STUDENT)) {
				context.startActivity(new Intent(context,
						CollegeMainActivity.class));
				((Activity) context).finish();
			} else if (CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_PARENT)) {
				context.startActivity(new Intent(context,
						CollegeParentMainActivity.class));
				((Activity) context).finish();
			} else if (CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_TEACHER)) {
				context.startActivity(new Intent(context,
						CollegeTeacherMainActivity.class));
				((Activity) context).finish();
			}

		}
	}
}
