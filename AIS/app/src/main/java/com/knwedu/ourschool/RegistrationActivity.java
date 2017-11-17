package com.knwedu.ourschool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.college.CollegeMainActivity;
import com.knwedu.college.CollegeParentMainActivity;
import com.knwedu.college.CollegeTeacherMainActivity;
import com.knwedu.college.adapter.CollegeListUserNameAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork;
import com.knwedu.college.utils.CollegeDataStructureFramwork.CollegePermission;
import com.knwedu.college.utils.CollegeDataStructureFramwork.userType;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassOfOrg;
import com.knwedu.ourschool.utils.DataStructureFramwork.Permission;
import com.knwedu.ourschool.utils.DataStructureFramwork.PermissionAdd;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.DataStructureFramwork.UserInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class RegistrationActivity extends Activity {

	ImageView logo;
	TextView schoolName, text_info;
	EditText edt_first_name, edt_last_name, edt_email, edt_phone, edt_password,
			edt_name_details;
	Spinner spinnerClass, spinnerSection;
	Switch switch_type;
	Button btn_reg;
	String selected_section_id, selected_class_id;
	String username = "";
	String password = "";
	String user_type_id = "";
	private boolean isSchool = true;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registration);

		logo = (ImageView) findViewById(R.id.image);
		schoolName = (TextView) findViewById(R.id.txt_institute_name);
		text_info = (TextView) findViewById(R.id.text_info);

		switch_type = (Switch) findViewById(R.id.switch_type);

		edt_first_name = (EditText) findViewById(R.id.edt_first_name);
		edt_last_name = (EditText) findViewById(R.id.edt_last_name);
		edt_email = (EditText) findViewById(R.id.edt_email);
		edt_phone = (EditText) findViewById(R.id.edt_phone);
		edt_password = (EditText) findViewById(R.id.edt_password);
		edt_name_details = (EditText) findViewById(R.id.edt_name_details);

		spinnerClass = (Spinner) findViewById(R.id.spinnerClass);
		spinnerSection = (Spinner) findViewById(R.id.spinnerSection);

		btn_reg = (Button) findViewById(R.id.btn_reg);
		selected_section_id = "0";
		selected_class_id = "0";

		if (SchoolAppUtils.GetSharedParameter(RegistrationActivity.this,
				Constants.INSTITUTION_TYPE).equalsIgnoreCase(
				Constants.INSTITUTION_TYPE_SCHOOL)) {
			isSchool = true;
		} else {
			isSchool = false;
		}

		if (!SchoolAppUtils.GetSharedParameter(RegistrationActivity.this,
				Constants.PASSWORD_PROTECTION).equals("1")) {
			edt_password.setHint(getResources().getString(
					R.string.choose_password));
		}

		try {
			ContextWrapper cw = new ContextWrapper(getApplicationContext());

			File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
			// Create imageDir
			File path = new File(directory, "applogo.png");
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(path));
			if (b != null) {
				logo.setImageBitmap(b);
			} else {
				logo.setImageResource(R.drawable.login_logo);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		schoolName.setText(SchoolAppUtils.GetSharedParameter(
				RegistrationActivity.this, Constants.INSTITUTION_NAME));

		text_info
				.setText(isSchool ? "You can be associated with multiple Class/Section after registration"
						: "You can be associated with multiple Program/Term after registration");

		switch_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					// Parent selected
					edt_name_details.setHint(getResources().getString(
							R.string.insert_student_name));
					text_info.setVisibility(View.VISIBLE);
					user_type_id = Constants.USERTYPE_PARENT;
				} else {
					// Student selected
					edt_name_details.setHint(getResources().getString(
							R.string.insert_parent_name));
					text_info.setVisibility(View.GONE);
					user_type_id = Constants.USERTYPE_STUDENT;
				}

			}
		});
		switch_type.setChecked(true);

		btn_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Validation
				int errorCount = 0;
				if (edt_first_name.getText().toString().isEmpty()) {
					edt_first_name.setError("Enter First Name");
					edt_first_name.requestFocus();
					errorCount++;
				} else {
					edt_first_name.setError(null);
				}
				if (edt_last_name.getText().toString().isEmpty()) {
					edt_last_name.setError("Enter Last Name");
					edt_last_name.requestFocus();
					errorCount++;
				} else {
					edt_last_name.setError(null);
				}

				if (!SchoolAppUtils
						.isValidEmail(edt_email.getText().toString())) {
					edt_email.setError("Enter valid Email");
					edt_email.requestFocus();
					errorCount++;
				} else {
					edt_email.setError(null);
				}

				if (edt_phone.getText().toString().isEmpty()) {
					edt_phone.setError("Enter Phone Number");
					edt_phone.requestFocus();
					errorCount++;
				} else {
					edt_phone.setError(null);
				}
				if (edt_password.getText().toString().isEmpty()) {
					edt_password.setError("Enter Password");
					edt_password.requestFocus();
					errorCount++;
				} else {
					edt_password.setError(null);
				}
				if (edt_name_details.getText().toString().isEmpty()) {
					edt_name_details.setError("Enter Full Name");
					edt_name_details.requestFocus();
					errorCount++;
				} else {
					edt_name_details.setError(null);
				}

				if (selected_section_id.equals("0")) {
					Toast.makeText(
							RegistrationActivity.this,
							isSchool ? "Select valid Class & Section"
									: "Select valid Program & Term",
							Toast.LENGTH_LONG).show();
					errorCount++;
				}

				// All fields validated
				if (errorCount == 0) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair(
							"organization_id", SchoolAppUtils
									.GetSharedParameter(
											RegistrationActivity.this,
											Constants.UORGANIZATIONID)));
					nameValuePairs.add(new BasicNameValuePair("first_name",
							edt_first_name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("last_name",
							edt_last_name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("email",
							edt_email.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("mobile_no",
							edt_phone.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("password",
							edt_password.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("class_id",
							selected_class_id));
					nameValuePairs.add(new BasicNameValuePair("section_id",
							selected_section_id));
					nameValuePairs.add(new BasicNameValuePair("password_set",
							SchoolAppUtils.GetSharedParameter(
									RegistrationActivity.this,
									Constants.PASSWORD_PROTECTION)));
					nameValuePairs.add(new BasicNameValuePair("user_type_id",
							user_type_id));
					nameValuePairs.add(new BasicNameValuePair("name_details",
							edt_name_details.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("devicetype",
							"android"));
					nameValuePairs.add(new BasicNameValuePair("devicetoken",
							getIntent().getStringExtra("devicetoken")));
					new RegisterAsync().execute(nameValuePairs);

				}

			}
		});

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("org_id", SchoolAppUtils
				.GetSharedParameter(RegistrationActivity.this,
						Constants.UORGANIZATIONID)));
		new SetClassListAsyn().execute(nameValuePairs);

	}

	// Class Section Listings
	private class SetClassListAsyn extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		ProgressDialog dialog;
		String error = "";
		ArrayList<ClassOfOrg> classes;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(RegistrationActivity.this);
			dialog.setTitle(getResources().getString(isSchool?R.string.fetch_classes: R.string.fetch_program));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> namevaluepair = params[0];

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlfrist(
					namevaluepair,
					SchoolAppUtils.GetSharedParameter(
							RegistrationActivity.this, Constants.COMMON_URL)
							+ Urls.api_class_section_list);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						classes = new ArrayList<ClassOfOrg>();
						classes.clear();
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							error = "Error in Data";
							return false;
						}

						for (int i = 0; i < array.length(); i++) {
							ClassOfOrg classOfOrg = new ClassOfOrg(
									array.getJSONObject(i));
							classes.add(classOfOrg);
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
				ArrayList<String> mList = new ArrayList<String>();
				mList.add(isSchool ? "Select Class" : "Select Program");

				for (int i = 0; i < classes.size(); i++) {
					mList.add(classes.get(i).class_name);
				}
				ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
						RegistrationActivity.this,
						R.layout.simple_spinner_dropdown_item_custom, mList);
				adapterClass
						.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
				spinnerClass.setAdapter(adapterClass);

				spinnerClass
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int n, long arg3) {
								ArrayList<String> mList = new ArrayList<String>();
								mList.add(isSchool ? "Select Section"
										: "Select Term");

								if (n > 0) {
									// TODO Auto-generated method stub
									ClassOfOrg classOfOrg = classes.get(n - 1);
									for (int i = 0; i < classOfOrg.section_List
											.size(); i++) {
										mList.add(classOfOrg.section_List
												.get(i).section_name);
									}

								}
								ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
										RegistrationActivity.this,
										R.layout.simple_spinner_dropdown_item_custom,
										mList);
								adapterClass
										.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom_new);
								spinnerSection.setAdapter(adapterClass);
								spinnerSection
										.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(
													AdapterView<?> arg0,
													View arg1, int n, long arg3) {
												// TODO Auto-generated method
												// stub
												if (n > 0) {
													selected_section_id = classes.get(spinnerClass
															.getSelectedItemPosition() - 1).section_List
															.get(n - 1).section_id;
													selected_class_id = classes.get(spinnerClass
															.getSelectedItemPosition() - 1).class_id;
												} else {
													selected_section_id = "0";
													selected_class_id = "0";
												}

											}

											@Override
											public void onNothingSelected(
													AdapterView<?> arg0) {
												// TODO Auto-generated method
												// stub

											}
										});
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

			} else {

				if (error.length() > 0) {
					SchoolAppUtils.showDialog(RegistrationActivity.this,
							"Registration", error);
				} else {
					SchoolAppUtils.showDialog(RegistrationActivity.this,
							"Registration",
							getResources().getString(R.string.error));
				}

			}

		}
	}

	// Registration
	private class RegisterAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		ProgressDialog dialog;
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(RegistrationActivity.this);
			dialog.setTitle(getResources().getString(
					R.string.registration_progress));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> namevaluepair = params[0];

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlfrist(
					namevaluepair,
					SchoolAppUtils.GetSharedParameter(
							RegistrationActivity.this, Constants.COMMON_URL)
							+ Urls.api_registration);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						try {
							JSONObject jObj = json.getJSONObject("data");
							username = jObj.getString("user_name");
							password = jObj.getString("password");

							error = "Registration Successful";
						} catch (Exception e) {
							return false;
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
				// Store User Id for farther use
				SchoolAppUtils.SetSharedParameter(RegistrationActivity.this,
						Constants.USER_LOGIN_ID, username);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						RegistrationActivity.this);
				builder.setTitle(error);
				builder.setMessage(
						"Your account information has been successfully sent to your email.")
						.setCancelable(false)
						.setNegativeButton("Continue Log in",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// TOdo
										/*
										 * Intent intent = getIntent();
										 * intent.putExtra("username",
										 * username);
										 * intent.putExtra("password",
										 * password);
										 * setResult(RESULT_FIRST_USER, intent);
										 * finish();
										 */

										List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
												7);
										nameValuePairs
												.add(new BasicNameValuePair(
														"email", username));
										nameValuePairs
												.add(new BasicNameValuePair(
														"username", username));
										nameValuePairs
												.add(new BasicNameValuePair(
														"password", password));
										nameValuePairs
												.add(new BasicNameValuePair(
														"devicetype", "android"));
										nameValuePairs.add(new BasicNameValuePair(
												"organization_id",
												SchoolAppUtils
														.GetSharedParameter(
																RegistrationActivity.this,
																Constants.UORGANIZATIONID)));
										nameValuePairs
												.add(new BasicNameValuePair(
														"devicetoken",
														getIntent()
																.getStringExtra(
																		"devicetoken")));
										try {
											nameValuePairs
													.add(new BasicNameValuePair(
															"version_number",
															getApplicationContext()
																	.getPackageManager()
																	.getPackageInfo(
																			getApplicationInfo().packageName,
																			0).versionName));
										} catch (NameNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										new SignInAsyntask()
												.execute(nameValuePairs);

									}
								})
						.setPositiveButton("May be Later",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// TOdo
										dialog.cancel();
										finish();
									}
								});

				builder.create().show();

			} else {

				if (error.length() > 0) {
					SchoolAppUtils.showDialog(RegistrationActivity.this,
							"Registration", error);
				} else {
					SchoolAppUtils.showDialog(RegistrationActivity.this,
							"Registration",
							getResources().getString(R.string.error));
				}

			}

		}
	}

	private class SignInAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		UserInfo userInfo;
		private ProgressDialog dialog;

		private String error = "";
		private Permission permission;
		private PermissionAdd permissionadd;

		CollegePermission per;
		private ArrayList<CollegeDataStructureFramwork.UserInfo> userInfoList;
		private ArrayList<CollegeDataStructureFramwork.userType> userTypeList;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(RegistrationActivity.this);
			dialog.setTitle(getResources().getString(R.string.signing_in));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			Log.d("institution type", SchoolAppUtils.GetSharedParameter(
					RegistrationActivity.this, Constants.INSTITUTION_TYPE));

			if (isSchool) {
				List<NameValuePair> nameValuePairs = params[0];
				JsonParser jParser = new JsonParser();
				JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
						Urls.api_login);

				// Log parameters:
				Log.d("url extension: ", Urls.api_login);
				String parameters = "";
				for (NameValuePair nvp : nameValuePairs) {
					parameters += nvp.getName() + "=" + nvp.getValue() + ",";
				}
				Log.d("Parameters: ", parameters);

				try {

					if (json != null) {
						if (json.getString("result").equalsIgnoreCase("1")) {
							JSONObject object = json.getJSONObject("data");
							String menu_tag = json.getString("menu_info");
							String menu_title = json.getString("menu_caption");
							JSONObject permission_config = json
									.getJSONObject("activity_permission");
							JSONObject permission_add = json
									.getJSONObject("permission");

							if (object != null) {
								// user details
								userInfo = new UserInfo(object);
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.USERTYPEID,
										userInfo.usertypeid);
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.USERID, userInfo.userid);
								SchoolAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												Constants.UALTEMAIL,
												userInfo.alt_email);
								SchoolAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												Constants.UMOBILENO,
												userInfo.mobile_no);
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.PASSWORD, password);
							}
							// Menu tag
							if (menu_tag != null) {
								Log.d("menu", menu_tag.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.MENU_TAGS,
										menu_tag.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.MENU_TITLES,
										menu_title.toString());
							}

							// Permissions
							if (permission_config != null) {
								permission = new Permission(permission_config);
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.ASSIGNMENT_MARK_PERMISSION,
										permission.assignment_permission
												.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.TEST_MARK_PERMISSION,
										permission.test_permission.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.ACTIVITY_MARK_PERMISSION,
										permission.activity_permission
												.toString());
							}

							// Permission Add
							if (permission_add != null) {
								permissionadd = new PermissionAdd(
										permission_add);
								SchoolAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												Constants.ASSIGNMENT_ADD_PERMISSION,
												permissionadd.assignment_add
														.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.TEST_ADD_PERMISSION,
										permissionadd.test_add.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.ACTIVITY_ADD_PERMISSION,
										permissionadd.activity_add.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.ANNOUNCEMENT_ADD_PERMISSION,
										permissionadd.announcement_add
												.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.COURSEWORK_ADD_PERMISSION,
										permissionadd.classwork_add.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.LESSONS_ADD_PERMISSION,
										permissionadd.lessons_add.toString());
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.ATTENDANCE_TYPE_PERMISSION,
										permissionadd.attendance_type
												.toString());
								Log.d("assignment",
										permissionadd.attendance_type
												.toString());
							}

							if (userInfo.usertypeid
									.equalsIgnoreCase(Constants.USERTYPE_PARENT)) {
								JSONArray child_info_arry = json
										.getJSONArray("child_info");
								JSONObject object_child_info = child_info_arry
										.getJSONObject(0);

								StudentProfileInfo selectedStudent = new StudentProfileInfo(
										object_child_info);
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.CHILD_ID,
										selectedStudent.user_id);
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.SELECTED_CHILD_OBJECT,
										object_child_info.toString());

							} else if (userInfo.usertypeid
									.equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {
								SchoolAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										Constants.CHILD_ID, userInfo.userid);

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
						error = getResources().getString(
								R.string.unknown_response);
					}

				} catch (JSONException e) {

				}
				return false;
			}

			// College
			else {
				List<NameValuePair> nameValuePairs = params[0];
				CollegeJsonParser jParser = new CollegeJsonParser();
				JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
						CollegeUrls.api_login);

				// Log parameters:
				Log.d("url extension: ", CollegeUrls.api_login);
				String parameters = "";
				for (NameValuePair nvp : nameValuePairs) {
					parameters += nvp.getName() + "=" + nvp.getValue() + ",";
				}
				Log.d("Parameters: ", parameters);

				userInfoList = new ArrayList<CollegeDataStructureFramwork.UserInfo>();
				userTypeList = new ArrayList<CollegeDataStructureFramwork.userType>();

				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray jsonData = json.getJSONArray("data");
						for (int i = 0; i < jsonData.length(); i++) {
							CollegeDataStructureFramwork.UserInfo collegeUserInfo = new CollegeDataStructureFramwork.UserInfo(
									jsonData.getJSONObject(i));
							SchoolAppUtils.SetSharedParameter(
									RegistrationActivity.this,
									Constants.USERID, collegeUserInfo.id);
							CollegeAppUtils.SetSharedParameter(context, "id",
									collegeUserInfo.id);
							CollegeAppUtils.SetSharedParameter(context,
									"session_student_id",
									collegeUserInfo.session_student_id);

							CollegeAppUtils.SetSharedParameter(context,
									"student_name",
									collegeUserInfo.student_name);
							CollegeAppUtils.SetSharedParameter(context,
									"student_class",
									collegeUserInfo.student_class);
							CollegeAppUtils.SetSharedParameter(context,
									"student_image",
									collegeUserInfo.student_image);
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.USERTYPEID,
									collegeUserInfo.usertypeid);
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.UALTEMAIL,
									collegeUserInfo.alt_email);
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.UMOBILENO,
									collegeUserInfo.mobile_no);
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.USERTYPEID,
									collegeUserInfo.usertypeid);
							userInfoList.add(collegeUserInfo);

						}

						JSONArray jsonUser = json.getJSONArray("user_types");
						for (int j = 0; j < jsonUser.length(); j++) {
							CollegeDataStructureFramwork.userType userTYpe = new userType(
									jsonUser.getJSONObject(j));
							CollegeAppUtils.SetSharedParameter(context,
									"role_id", userTYpe.id);
							userTypeList.add(userTYpe);
						}
						if (userTypeList.size() == 1) {
							String menu_tag = json.getString("menu_info");
							menu_tag.split("\\|");
							String menu_title = json.getString("menu_caption");
							JSONObject permission_add = json
									.getJSONObject("permissions");
							if (jsonData != null) {
								per = new CollegePermission(permission_add);
							}
							if (jsonData != null) {

								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_ASSIGNMENT_CREATE,
												per.assignment_create
														.toString());
								Log.d("VALUE OF ASSIGNMENT CREATE...",
										per.assignment_create.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_ANNOUNCEMENT_CREATE,
												per.announcement_create
														.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_CLASSWORK_CREATE,
												per.classwork_create.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_INTERNAL_CREATE,
												per.test_create.toString());
								// marking & publish

								CollegeAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										CollegeConstants.COLLEGE_INTERNAL_MARK,
										per.test_mark.toString());

								Log.d("VALUE OF TEST MARK...",
										per.test_mark.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_INTERNAL_PUBLISH,
												per.test_publish.toString());

								Log.d("VALUE OF TEST PUBLISH...",
										per.test_publish.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_ASSIGNMENT_MARK,
												per.assignment_mark.toString());

								Log.d("VALUE OF ASSIGNMENT MARK...",
										per.assignment_mark.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH,
												per.assignment_publish
														.toString());

								Log.d("VALUE OF ASSIGNMENT PUBLISH...",
										per.assignment_publish.toString());
								// attendance mark
								if (!CollegeAppUtils
										.GetSharedParameter(context,
												CollegeConstants.USERTYPEID)
										.equalsIgnoreCase(
												CollegeConstants.USERTYPE_PARENT)) {
									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_ATTENDANCE_MARK,
													per.attendance_mark
															.toString());

									// lessons edit
									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_LESSONS_EDIT,
													per.lessons_edit.toString());
								}

								/*
								 * //feedback create CollegeAppUtils
								 * .SetSharedParameter( SigninActivity.this,
								 * CollegeConstants.COLLEGE_FEEDBACK_CREATE,
								 * per.feedback_create.toString());
								 */
								// marking & publish

								CollegeAppUtils.SetSharedParameter(
										RegistrationActivity.this,
										CollegeConstants.COLLEGE_INTERNAL_MARK,
										per.test_mark.toString());

								Log.d("VALUE OF TEST MARK...",
										per.test_mark.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_INTERNAL_PUBLISH,
												per.test_publish.toString());

								Log.d("VALUE OF TEST PUBLISH...",
										per.test_publish.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_ASSIGNMENT_MARK,
												per.assignment_mark.toString());

								Log.d("VALUE OF ASSIGNMENT MARK...",
										per.assignment_mark.toString());
								CollegeAppUtils
										.SetSharedParameter(
												RegistrationActivity.this,
												CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH,
												per.assignment_publish
														.toString());

								Log.d("VALUE OF ASSIGNMENT PUBLISH...",
										per.assignment_publish.toString());

								if (!CollegeAppUtils
										.GetSharedParameter(context,
												CollegeConstants.USERTYPEID)
										.equalsIgnoreCase(
												CollegeConstants.USERTYPE_TEACHER)) {

									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_REQUEST_CREATE,
													per.request_create
															.toString());
									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_REQUEST_DELETE,
													per.request_delete
															.toString());
									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_REQUEST_EDIT,
													per.request_edit.toString());
									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_REQUEST_MARK,
													per.request_mark.toString());
									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_REQUEST_PUBLISH,
													per.request_publish
															.toString());
									CollegeAppUtils
											.SetSharedParameter(
													RegistrationActivity.this,
													CollegeConstants.COLLEGE_REQUEST_VIEW,
													per.request_view.toString());
									// feedback create
									CollegeAppUtils
											.SetSharedParameter(
													context,
													CollegeConstants.COLLEGE_FEEDBACK_CREATE,
													per.feedback_create
															.toString());

								}

							}

							if (menu_tag != null) {
								Log.d("menu", menu_tag.toString());
								CollegeAppUtils.SetSharedParameter(context,
										CollegeConstants.MENU_TAGS,
										menu_tag.toString());
								CollegeAppUtils.SetSharedParameter(context,
										CollegeConstants.MENU_TITLES,
										menu_title.toString());
							}
						}
						JSONObject jsonOrg = json.getJSONObject("org_config");
						if (jsonOrg != null) {
							CollegeDataStructureFramwork.UserInfo collegeUserInfo = new CollegeDataStructureFramwork.UserInfo(
									jsonOrg);
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.ASSIGNMENT_MARKING,
									collegeUserInfo.assignment_marking);
							Log.d("ASSIGNMENT MARKING",
									collegeUserInfo.assignment_marking);
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.TEST_MARKING,
									collegeUserInfo.test_marking);
							CollegeAppUtils.SetSharedParameter(context,
									CollegeConstants.CLASSWORK_MARKING,
									collegeUserInfo.classwork_marking);
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
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

			if (result) {

				if (isSchool) {
					SchoolAppUtils.SetSharedParameter(
							RegistrationActivity.this, Constants.USERINFO,
							userInfo.toString());
					if (SchoolAppUtils.GetSharedParameter(
							RegistrationActivity.this, Constants.USERTYPEID)
							.equalsIgnoreCase(Constants.USERTYPE_STUDENT)) {
						SchoolAppUtils.SetSharedBoolParameter(
								RegistrationActivity.this, Constants.UISSIGNIN,
								true);
						startActivity(new Intent(RegistrationActivity.this,
								MainActivity.class));
						finish();
					} else if (SchoolAppUtils.GetSharedParameter(
							RegistrationActivity.this, Constants.USERTYPEID)
							.equalsIgnoreCase(Constants.USERTYPE_TEACHER)) {

						SchoolAppUtils.showDialog(RegistrationActivity.this,
								getResources().getString(R.string.sign_in),
								"Teacher Can't Login directly from here");
					} else {

						SchoolAppUtils.SetSharedBoolParameter(
								RegistrationActivity.this, Constants.ISSIGNIN,
								true);
						SchoolAppUtils.SetSharedBoolParameter(
								RegistrationActivity.this,
								Constants.ISPARENTSIGNIN, true);

						startActivity(new Intent(RegistrationActivity.this,
								ParentMainActivity.class));
						finish();
					}
				}
				// College
				else {

					if (userTypeList.size() > 1) {
						SchoolAppUtils.SetSharedParameter(
								RegistrationActivity.this, Constants.USERID,
								"0");

						showDialog();
					} else {

						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								0);
						nameValuePairs.add(new BasicNameValuePair("id",
								CollegeAppUtils.GetSharedParameter(context,
										"id")));
						Log.d("PARAMS OF TEACHER", "" + nameValuePairs);
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

			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(RegistrationActivity.this,
							getResources().getString(R.string.sign_in), error);
				} else {
					SchoolAppUtils.showDialog(RegistrationActivity.this,
							getResources().getString(R.string.sign_in),
							getResources().getString(R.string.error));
				}
			}
		}

		private void showDialog() {
			// Strings to Show In Dialog with Radio Buttons
			final Dialog dialog2 = new Dialog(context);
			dialog2.setCanceledOnTouchOutside(false);
			dialog2.setCancelable(false);
			dialog2.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					SchoolAppUtils.SetSharedParameter(
							RegistrationActivity.this, Constants.USERID, "0");
				}
			});
			dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog2.setContentView(R.layout.custom_list_dialog);
			ListView listName = (ListView) dialog2.findViewById(R.id.listView1);

			CollegeListUserNameAdapter mAdapter = new CollegeListUserNameAdapter(
					context, userTypeList);
			listName.setAdapter(mAdapter);
			dialog2.show();
		}

	}

}
