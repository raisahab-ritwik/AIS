package com.knwedu.ourschool;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.DailyDiaryStudent;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateDailyDairyStudentWiseActivity extends FragmentActivity {

    private ProgressDialog dialog;
    private ListView llStudents;
    private Button btnCreate;
    private ArrayList<DailyDiaryStudent> students = new ArrayList<DailyDiaryStudent>();
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_daily_student_activity);
        ((ImageButton) findViewById(R.id.back_btn))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        initializeElements();
    }

    private void initializeElements() {
        llStudents = (ListView)findViewById(R.id.listStudent);
        btnCreate = (Button)findViewById(R.id.buttonCreate);

        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonarrayStudent");

        students.clear();

        try {
            JSONArray array = new JSONArray(jsonArray);
            for (int i = 0; i < array.length(); i++) {

                DailyDiaryStudent student = new DailyDiaryStudent(array.getJSONObject(i));
                students.add(student);
            }
            adapter = new StudentAdapter(
                    CreateDailyDairyStudentWiseActivity.this,
                    students);
            llStudents.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requetsForCreatingDaily();

            }
        });
    }

    private void requetsForCreatingDaily() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
        nameValuePairs.add(
                new BasicNameValuePair("section_id", getIntent().getStringExtra("section_id")));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(this, Constants.UORGANIZATIONID)));
        nameValuePairs
                .add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(this, Constants.USERID)));

        nameValuePairs.add(
                new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(this, Constants.USERTYPEID)));
        try{
            nameValuePairs.add(
                    new BasicNameValuePair("student_id", makJsonObject(adapter.selectedStudents).toString()));
            Log.d("JSON",makJsonObject(adapter.selectedStudents).toString());
        }catch(JSONException e){
            Log.e("JSONException", e.toString());
        }


        nameValuePairs.add(
                new BasicNameValuePair("title", getIntent().getStringExtra("title")));
        nameValuePairs.add(
                new BasicNameValuePair("description", getIntent().getStringExtra("description")));

        nameValuePairs.add(
                new BasicNameValuePair("is_comment", getIntent().getStringExtra("is_comment")));
        nameValuePairs.add(
                new BasicNameValuePair("type", "1"));

        nameValuePairs.add(
                new BasicNameValuePair("attachment", getIntent().getStringExtra("attachment")));
        nameValuePairs.add(
                new BasicNameValuePair("file_name", getIntent().getStringExtra("file_name")));


        new CreateDailyDiaryAsynTask().execute(nameValuePairs);
    }

    private class CreateDailyDiaryAsynTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CreateDailyDairyStudentWiseActivity.this);
            dialog.setTitle("Create Daily Diary");
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_create_daily_diary);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_create_daily_diary);
            try {
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        error = json.getString("data");
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
                finish();
                Toast.makeText(CreateDailyDairyStudentWiseActivity.this,error,Toast.LENGTH_LONG).show();
                /**
                * Read value from SharedPref which is set in DailyDiaryClassListFragment.java
                */
                SharedPreferences pref = getApplicationContext().getSharedPreferences("DailyDiary", MODE_PRIVATE);

                Intent intent = new Intent(CreateDailyDairyStudentWiseActivity.this,TeacherDairyListActivity.class);
                intent.putExtra(Constants.PAGE_TITLE, pref.getString("PAGE_TITLE",""));
                intent.putExtra("class", pref.getString("class",""));
                intent.putExtra("section", pref.getString("section", ""));

                startActivity(intent);


            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(CreateDailyDairyStudentWiseActivity.this, "", error);
                } else {
                    SchoolAppUtils.showDialog(CreateDailyDairyStudentWiseActivity.this, "",
                            getResources().getString(R.string.error));
                }

            }
        }
    }

    public class StudentAdapter extends BaseAdapter {
        ViewHolder holder;
        private LayoutInflater inflater;
        Context context;
        private ArrayList<DailyDiaryStudent> students;
        private ArrayList<DailyDiaryStudent> selectedStudents = new ArrayList<DailyDiaryStudent>();
        private boolean isView;

        public StudentAdapter(Context context, ArrayList<DailyDiaryStudent> students) {
            this.context = context;
            this.students = students;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (this.students != null) {
                return this.students.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.daily_diary_create_student_wise_item_view, null);
                holder = new ViewHolder();
                holder.tvCode = (TextView) convertView.findViewById(R.id.tvCode);
                holder.checkBoxName = (CheckBox) convertView.findViewById(R.id.checkBoxName);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SchoolAppUtils.setFont(context, holder.tvCode);
            SchoolAppUtils.setFont(context, holder.checkBoxName);

            holder.checkBoxName.setText(students.get(position).fname+ " "+ students.get(position).lname);
            holder.tvCode.setText("("+students.get(position).user_code+")");

            holder.checkBoxName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectedStudents.add(students.get(position));
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView tvCode;
            CheckBox checkBoxName;
        }

    }

    /**
     * Prepare student JSON ARRAY
     * @param students
     * @return
     * @throws JSONException
     */
    public JSONObject makJsonObject(ArrayList<DailyDiaryStudent> students)
            throws JSONException {
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < students.size(); i++) {
            obj = new JSONObject();
            try {
                obj.put("id", students.get(i).id);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }

        JSONObject finalobject = new JSONObject();
        finalobject.put("student", jsonArray);
        return finalobject;
    }
}
