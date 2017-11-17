package com.knwedu.ourschool;

import java.io.IOException;
import java.net.URISyntaxException;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Announcement;
import com.knwedu.ourschool.utils.DataStructureFramwork.Assignment;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassFellows;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.FileUtils;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class TeacherAddBehaviourActivity extends FragmentActivity {
    private TextView ttitle, title, header;
    private Button date, submit, select_file;
    private Subject subject;
    private EditText description, titleEdt, marksEdt;/* codeEdt */
    private ProgressDialog dialog,dialog1;
    AlertDialog.Builder dialoggg;
    private static final int FILE_SELECT_CODE = 0;
    private String encrypted_file_name = null;
    int serverResponseCode = 0;
    public DatabaseAdapter mDatabase;
    private ClassFellows class_fellow;
    private boolean internetAvailable = false;
    String path = "null";
    private String page_title = "";
    TextView dateText;
    private Spinner spinnerGrade, spinnerCard;
    private RadioGroup rbGroup;
    private int type;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private ArrayList<String> spinnerValues = new ArrayList<String>();
    HashMap<String, String> mapGrade = new HashMap<String, String>();
    HashMap<String, String> mapCard = new HashMap<String, String>();
    private int selectedGradePos,selectedCardPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_teacher_behaviour);
        page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
        SchoolAppUtils.loadAppLogo(TeacherAddBehaviourActivity.this,
                (ImageButton) findViewById(R.id.app_logo));

        mDatabase = ((SchoolApplication) getApplication()).getDatabase();
        internetAvailable = getIntent().getBooleanExtra(Constants.IS_ONLINE,
                false);
        initialize();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedUri = data.getData();
                    if (selectedUri == null) {
                        return;
                    }

                    try {
                        path = FileUtils.getPath(this, selectedUri);
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (TextUtils.isEmpty(path)) {
                        try {

                            path = FileUtils.getDriveFileAbsolutePath(
                                    TeacherAddBehaviourActivity.this, selectedUri);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    Log.d("TAG", "File Path: " + path);
                    select_file.setText(path);

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Google Analytics", "Tracking Start");
        EasyTracker.getInstance(this).activityStart(this);
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Google Analytics", "Tracking Stop");
        EasyTracker.getInstance(this).activityStop(this);
    }

    private void initialize() {
        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        ttitle = (TextView) findViewById(R.id.ttitle_txt);
        title = (TextView) findViewById(R.id.title_txt);
        submit = (Button) findViewById(R.id.submit_btn);

        description = (EditText) findViewById(R.id.description_edt);
        header = (TextView) findViewById(R.id.header_text);
        header.setText(page_title);

        spinnerGrade = (Spinner) findViewById(R.id.spinnerGrade);
        spinnerCard = (Spinner) findViewById(R.id.spinnerCard);
        rbGroup = (RadioGroup) findViewById(R.id.rbGroup);

        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGradePos = 0;
                String idVal = mapGrade.get(spinnerValues.get(position));
                if(idVal!= null){
                    selectedGradePos = Integer.parseInt(idVal);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGradePos = 0;
            }
        });

        spinnerCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCardPos = 0;
                Log.d("hello",mapCard+"");
                if(!spinnerValues.get(position).equalsIgnoreCase("Select Card")){
                    String idVal = mapCard.get(spinnerValues.get(position));
                    Log.d("hello",spinnerValues.get(position));
                    Log.d("hello",idVal);
                    if(idVal!= null) {
                        selectedCardPos = Integer.parseInt(idVal);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCardPos = 0;
            }
        });

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedCardPos = 0;
                selectedGradePos = 0;
                if (checkedId == R.id.grade) {
                    type = 1;
                    description.setVisibility(View.GONE);
                    spinnerGrade.setVisibility(View.VISIBLE);
                    spinnerCard.setVisibility(View.GONE);
                    fetchGradeCard(type);
                } else if (checkedId == R.id.card) {
                    type = 2;
                    description.setVisibility(View.GONE);
                    spinnerGrade.setVisibility(View.GONE);
                    spinnerCard.setVisibility(View.VISIBLE);
                    fetchGradeCard(type);
                } else {
                    description.setVisibility(View.VISIBLE);
                    spinnerGrade.setVisibility(View.GONE);
                    spinnerCard.setVisibility(View.GONE);
                }
            }

        });

		/*
         * LocationManager locMan = (LocationManager)
		 * getSystemService(LOCATION_SERVICE); long networkTS =
		 * locMan.getLastKnownLocation
		 * (LocationManager.NETWORK_PROVIDER).getTime(); Date dt = new
		 * Date(networkTS); header.setText(dt.toString());
		 */

        if (getIntent().getExtras() != null) {
            if (internetAvailable) {
                String temp = getIntent().getExtras().getString(
                        Constants.TSUBJECT);
                if (temp != null) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(temp);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (object != null) {
                        subject = new Subject(object);
                    }
                }
                String tempclassfellow = getIntent().getExtras().getString(
                        Constants.CLASSFELLOW);
                if (temp != null) {
                    JSONObject object_classfellow = null;
                    try {
                        object_classfellow = new JSONObject(tempclassfellow);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (object_classfellow != null) {
                        class_fellow = new ClassFellows(object_classfellow);

                    }
                }
            } /*
			 * else {
			 * 
			 * findViewById(R.id.txt_offline).setVisibility(View.VISIBLE);
			 * //select_file.setVisibility(View.GONE); String subject_id =
			 * getIntent().getExtras().getString( Constants.OFFLINE_SUBJECT_ID);
			 * mDatabase.open(); subject = mDatabase.getSubject(subject_id);
			 * mDatabase.close(); }
			 */

        }
        if (subject != null) {
            ttitle.setText(subject.classs + " " + subject.section_name);
            title.setText(class_fellow.fname + " " + class_fellow.lname);
        } else {
            ttitle.setText("");
            title.setText("");
        }

        // Auto select grade when Add screen is opened
        rbGroup.check(R.id.grade);
        fetchGradeCard(1);
        spinnerGrade.setVisibility(View.VISIBLE);


        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				if (description.getText().toString().length() <= 0) {
//					Toast.makeText(TeacherAddBehaviourActivity.this,
//							"Enter Description", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (!internetAvailable) {
//					// new OfflineAnnouncementAddAsync().execute();
//				} else {
//
//					/*
//					 * if (select_file.getText().toString()
//					 * .equalsIgnoreCase(getString(R.string.attachment))) {
//					 */
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
//							15);
//					nameValuePairs.add(new BasicNameValuePair("user_type_id",
//							SchoolAppUtils.GetSharedParameter(
//									getApplicationContext(),
//									Constants.USERTYPEID)));
//					nameValuePairs.add(new BasicNameValuePair(
//							"organization_id", SchoolAppUtils
//							.GetSharedParameter(
//									getApplicationContext(),
//									Constants.UORGANIZATIONID)));
//					nameValuePairs
//							.add(new BasicNameValuePair("user_id",
//									SchoolAppUtils.GetSharedParameter(
//											getApplicationContext(),
//											Constants.USERID)));
//					nameValuePairs.add(new BasicNameValuePair("section_id",
//							subject.section_id));
//					nameValuePairs.add(new BasicNameValuePair("student_id",
//							class_fellow.id));
//					nameValuePairs.add(new BasicNameValuePair("description",
//							description.getText().toString()));
//					new UploadAsyntask().execute(nameValuePairs);
//
//				}
                if (selectedGradePos > 0 || !description.getText().toString().isEmpty() || selectedCardPos>0) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                            15);
                    nameValuePairs.add(new BasicNameValuePair("user_type_id",
                            SchoolAppUtils.GetSharedParameter(
                                    getApplicationContext(),
                                    Constants.USERTYPEID)));
                    nameValuePairs
                            .add(new BasicNameValuePair("user_id",
                                    SchoolAppUtils.GetSharedParameter(
                                            getApplicationContext(),
                                            Constants.USERID)));
                    nameValuePairs.add(new BasicNameValuePair("student_id",
                            class_fellow.id));
                    nameValuePairs.add(new BasicNameValuePair("organization_id",
                            SchoolAppUtils.GetSharedParameter(
                                    getApplicationContext(),
                                    Constants.UORGANIZATIONID)));
                    nameValuePairs.add(new BasicNameValuePair("section_id",
                            subject.section_id));
                    nameValuePairs.add(new BasicNameValuePair("grade_id",
                            selectedGradePos + ""));
                    nameValuePairs.add(new BasicNameValuePair("card_id", selectedCardPos+""));
                    nameValuePairs.add(new BasicNameValuePair("description",
                            description.getText().toString().trim()));

                    new UploadAsyntask().execute(nameValuePairs);
                } else {
                    Toast.makeText(TeacherAddBehaviourActivity.this,"Field cannot be empty",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void fetchGradeCard(int type) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                15);
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(
                        getApplicationContext(),
                        Constants.USERTYPEID)));
        nameValuePairs
                .add(new BasicNameValuePair("user_id",
                        SchoolAppUtils.GetSharedParameter(
                                getApplicationContext(),
                                Constants.USERID)));
        nameValuePairs.add(new BasicNameValuePair("student_id",
                class_fellow.id));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(
                        getApplicationContext(),
                        Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("type",
                type + ""));
        new FetchGradeCardItemsAsyncTask().execute(nameValuePairs);
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
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar newDate = Calendar.getInstance();
            newDate.set(year, month, day);
            Calendar minDate = Calendar.getInstance();
            if (minDate != null && minDate.after(newDate)) {
                Toast.makeText(
                        TeacherAddBehaviourActivity.this,
                        "Please set todays or future dates," + " not past date",
                        Toast.LENGTH_LONG).show();
                onCreateDialog(getArguments());
                return;
            }

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
        }
    }

    private class UploadAsyntask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(TeacherAddBehaviourActivity.this);
            dialog.setTitle("Creating " + page_title);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> namevaluepair = params[0];
            JsonParser jParser = new JsonParser();
            String parameters = "";
            for (NameValuePair nvp : namevaluepair) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);
            JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
                    Urls.api_behaviour_create);
            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        error = page_title + " created successfully";

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
                TeacherAddBehaviourActivity.this.finish();
            } else {
                SchoolAppUtils.showDialog(
                        TeacherAddBehaviourActivity.this,
                        getTitle().toString(),
                        getResources().getString(
                                R.string.please_check_internet_connection));
            }
//			if (result) {
//				dialoggg = new AlertDialog.Builder(
//						TeacherAddBehaviourActivity.this);
//				dialoggg.setTitle(page_title);
//				dialoggg.setMessage(error);
//				dialoggg.setNeutralButton(R.string.ok,
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								SchoolAppUtils.SetSharedBoolParameter(
//										TeacherAddBehaviourActivity.this,
//										"update", true);
//								TeacherAddBehaviourActivity.this.finish();
//							}
//						});
//				dialoggg.show();
//			} else {
//				if (error != null) {
//					if (error.length() > 0) {
//						SchoolAppUtils.showDialog(
//								TeacherAddBehaviourActivity.this, getTitle()
//										.toString(), error);
//					} else {
//						SchoolAppUtils
//								.showDialog(
//										TeacherAddBehaviourActivity.this,
//										getTitle().toString(),
//										getResources()
//												.getString(
//														R.string.please_check_internet_connection));
//					}
//				} else {
//					SchoolAppUtils.showDialog(
//							TeacherAddBehaviourActivity.this,
//							getTitle().toString(),
//							getResources().getString(
//									R.string.please_check_internet_connection));
//				}
//
//			}
        }

    }

    private class FetchGradeCardItemsAsyncTask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog1 = new ProgressDialog(TeacherAddBehaviourActivity.this);
//            dialog1.setTitle("Fetching");
//            dialog1.setMessage(getResources().getString(R.string.please_wait));
//            dialog1.setCanceledOnTouchOutside(false);
//            dialog1.setCancelable(false);
//            dialog1.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> namevaluepair = params[0];
            JsonParser jParser = new JsonParser();
            Log.d("url extension: ", Urls.api_get_grade_card);
            String parameters = "";
            for (NameValuePair nvp : namevaluepair) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);
            JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
                    Urls.api_get_grade_card);
            try {

                if (json != null) {
                    mapGrade.clear();
                    spinnerValues.clear();
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array = json.getJSONArray("data");
                        if (type == 1) {
                            spinnerValues.add("Select Grade");
                            for (int i = 0; i < array.length(); i++) {
                                mapGrade.put(array.getJSONObject(i).getString("grade_name"), array.getJSONObject(i).getString("id"));
                                spinnerValues.add(array.getJSONObject(i).getString("grade_name"));
                            }
                        } else {
                            spinnerValues.add("Select Card");
                            for (int i = 0; i < array.length(); i++) {
                                mapCard.put(array.getJSONObject(i).getString("card_name"), array.getJSONObject(i).getString("id"));
                                spinnerValues.add(array.getJSONObject(i).getString("card_name"));
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
//            if (dialog1 != null) {
//                dialog1.dismiss();
//                dialog1 = null;
//            }
            if (result) {
                spinnerArrayAdapter = new ArrayAdapter<String>(TeacherAddBehaviourActivity.this, R.layout.spinner_text, spinnerValues);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                if (type == 1) {
                    spinnerGrade.setAdapter(spinnerArrayAdapter);
                } else {
                    spinnerCard.setAdapter(spinnerArrayAdapter);
                }
            }
        }

    }

    class OfflineAnnouncementAddAsync extends
            AsyncTask<String, Assignment, Boolean> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... params) {
            long row_id = 0;
            mDatabase.open();
            Announcement announcement = new Announcement();
            announcement.date = SchoolAppUtils.getCurrentDate();
            announcement.description = description.getText().toString().trim();
            announcement.title = titleEdt.getText().toString().trim();
            announcement.file_name = path;
            announcement.attachment = "null";
            announcement.subject_id = subject.id;
            announcement.section_id = subject.section_id;

            row_id = mDatabase.addAnnouncement(announcement, 2);
            mDatabase.close();

            return (row_id > 0);
        }

        @Override
        protected void onProgressUpdate(Assignment... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(
                    TeacherAddBehaviourActivity.this);
            dialog.setTitle("New " + page_title);
            // mDatabase.close();
            if (result) {
                dialog.setMessage(page_title + " created in Offline Mode");
            } else {
                dialog.setMessage("Error in inserting Data");
            }

            dialog.setNegativeButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            dialog.show();
        }

    }
}
