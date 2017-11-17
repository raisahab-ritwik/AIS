package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassOfOrg;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class CollegeParentClassAddActivity extends Activity {
	private EditText edt_first_name, edt_last_name;
	private Spinner spinnerClass, spinnerSection;
	private String selected_section_id;
	private Button btnAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_parent_class_add);
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

	}

	private void initialize() {

		selected_section_id = "0";
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		edt_first_name = (EditText) findViewById(R.id.edt_first_name);
		edt_last_name = (EditText) findViewById(R.id.edt_last_name);

		spinnerClass = (Spinner) findViewById(R.id.spinnerClass);
		spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("org_id", SchoolAppUtils
				.GetSharedParameter(CollegeParentClassAddActivity.this,
						Constants.UORGANIZATIONID)));
		new SetClassListAsyn().execute(nameValuePairs);
		btnAdd = (Button) findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (edt_first_name.getText().toString().isEmpty()) {
					edt_first_name.setError("Enter First Name");
					edt_first_name.requestFocus();
					return;
				} else {
					edt_first_name.setError(null);
				}
				if (edt_last_name.getText().toString().isEmpty()) {
					edt_last_name.setError("Enter Last Name");
					edt_last_name.requestFocus();
					return;
				} else {
					edt_last_name.setError(null);
				}
				if (selected_section_id.equals("0")) {
					SchoolAppUtils.showDialog(CollegeParentClassAddActivity.this,
							getResources().getString(R.string.class_section),
							"Select valid Program & Term");
					return;

				}
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						15);
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
				nameValuePairs.add(new BasicNameValuePair("section_id",
						selected_section_id));
				nameValuePairs.add(new BasicNameValuePair("fname",
						edt_first_name.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("lname",
						edt_last_name.getText().toString()));

				new AddClassSectionAsyntask().execute(nameValuePairs);

			}
		});
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
			dialog = new ProgressDialog(CollegeParentClassAddActivity.this);
			dialog.setTitle(getResources().getString(R.string.fetch_program));
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
							CollegeParentClassAddActivity.this, Constants.COMMON_URL)
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
				mList.add("Select Program");
				for (int i = 0; i < classes.size(); i++) {
					mList.add(classes.get(i).class_name);
				}
				ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(
						CollegeParentClassAddActivity.this,
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

								mList.add("Select Term");
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
										CollegeParentClassAddActivity.this,
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
													} else {
													selected_section_id = "0";
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
					SchoolAppUtils.showDialog(CollegeParentClassAddActivity.this,
							getResources().getString(R.string.my_children),
							error);
				} else {
					SchoolAppUtils.showDialog(CollegeParentClassAddActivity.this,
							getResources().getString(R.string.my_children),
							getResources().getString(R.string.error));
				}

			}

		}
	}

	private class AddClassSectionAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(CollegeParentClassAddActivity.this);
			dialog.setTitle("Adding "
					+ getResources().getString(R.string.my_children));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_class_section_add);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						error = "New Child added successfully";
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
				AlertDialog.Builder alert = new AlertDialog.Builder(
						CollegeParentClassAddActivity.this);
				alert.setTitle(getResources().getString(R.string.my_children));
				alert.setMessage(error);
				alert.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeParentClassAddActivity.this.finish();

							}
						});
				alert.show();
			} else {

				if (error.length() > 0) {
					SchoolAppUtils.showDialog(CollegeParentClassAddActivity.this,
							getResources().getString(R.string.my_children),
							error);
				} else {
					SchoolAppUtils.showDialog(CollegeParentClassAddActivity.this,
							getResources().getString(R.string.my_children),
							getResources().getString(R.string.error));
				}

			}

		}

	}

}
