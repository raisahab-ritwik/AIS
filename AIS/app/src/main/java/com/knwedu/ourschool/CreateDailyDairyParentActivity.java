package com.knwedu.ourschool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.FileUtils;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CreateDailyDairyParentActivity extends FragmentActivity {

    private ProgressDialog dialog;
    private String page_title = "";
    private EditText editTextDesc, editTextTitle;
    private CheckBox checkBoxParentReply;
    private Button buttonCreate, buttonAttachment;
    private static final int FILE_SELECT_CODE = 0;
    private String path,encrypted_file_name;
    private int serverResponseCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_daily_view);
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
        RadioGroup radioGroupType = (RadioGroup) findViewById(R.id.radioGroupType);
        radioGroupType.setVisibility(View.GONE);
        LinearLayout title_layout = (LinearLayout) findViewById(R.id.title_layout);
        title_layout.setVisibility(View.GONE);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDesc = (EditText) findViewById(R.id.editTextDesc);

        checkBoxParentReply = (CheckBox) findViewById(R.id.checkBoxParentReply);
        checkBoxParentReply.setVisibility(View.INVISIBLE);

        buttonCreate = (Button) findViewById(R.id.buttonCreate);
        buttonAttachment = (Button) findViewById(R.id.buttonAttachment);


        buttonCreate.setVisibility(View.VISIBLE);
        buttonCreate.setText("Create Daily Diary");

        buttonAttachment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        buttonCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTitle.getText().toString().trim().isEmpty() || editTextTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateDailyDairyParentActivity.this,"Fields cannot be blank",Toast.LENGTH_LONG).show();
                }else{
                    if(path!=null && path.length()>0){
                        new UploadFileAsyntask().execute();
                    }else{
                        requetsForCreatingDaily("");
                    }
                }
            }
        });
    }

    private class UploadFileAsyntask extends AsyncTask<Void, Void, Void> {
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CreateDailyDairyParentActivity.this);
            dialog.setTitle("Updating Daily " + page_title);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            if (serverResponseCode == 200) {

                String uuploadfile_name = null;
                String uuuploadfile_name;

                final String[] tokens = path
                        .split("/");
                for (String s : tokens) {
                    System.out.println(s);
                    uuploadfile_name = s;
                }
                uuuploadfile_name = uuploadfile_name;
                if (uuuploadfile_name
                        .equalsIgnoreCase(getString(R.string.attachment))) {
                    uuuploadfile_name = "";
                }

                requetsForCreatingDaily(uuuploadfile_name);

            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            uploadFile(path);
            return null;
        }

    }

    private void requetsForCreatingDaily(String file_name) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
        nameValuePairs.add(
                new BasicNameValuePair("section_id", getIntent().getStringExtra("section")));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(this, Constants.UORGANIZATIONID)));
        nameValuePairs
                .add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(this, Constants.USERID)));

        nameValuePairs.add(
                new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(this, Constants.USERTYPEID)));

        nameValuePairs.add(
                new BasicNameValuePair("title", editTextTitle.getText().toString().trim()));
        nameValuePairs.add(
                new BasicNameValuePair("description", editTextDesc.getText().toString().trim()));
        nameValuePairs.add(
                new BasicNameValuePair("child_id", SchoolAppUtils.GetSharedParameter(this, Constants.CHILD_ID)));

        nameValuePairs.add(
                new BasicNameValuePair("is_comment", "Y"));

        if(file_name.isEmpty())
        {
            nameValuePairs.add(
                    new BasicNameValuePair("attachment", ""));
            nameValuePairs.add(
                    new BasicNameValuePair("file_name", ""));
        }else{
            nameValuePairs.add(
                    new BasicNameValuePair("attachment", encrypted_file_name));
            nameValuePairs.add(
                    new BasicNameValuePair("file_name", file_name));
        }


        new CreateDailyDiaryAsynTask().execute(nameValuePairs);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressLint("NewApi")
    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    ;

    private class CreateDailyDiaryAsynTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CreateDailyDairyParentActivity.this);
            dialog.setTitle(page_title);
            dialog.setMessage(getResources().getString(R.string.please_wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {
            List<NameValuePair> nameValuePairs = params[0];
            // Log parameters:
            Log.d("url extension: ", Urls.api_create_daily_diary_parent);
            String parameters = "";
            for (NameValuePair nvp : nameValuePairs) {
                parameters += nvp.getName() + "=" + nvp.getValue() + ",";
            }
            Log.d("Parameters: ", parameters);
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_create_daily_diary_parent);
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
                Toast.makeText(CreateDailyDairyParentActivity.this,error,Toast.LENGTH_LONG).show();
                finish();

            } else {
                if (error.length() > 0) {
                    SchoolAppUtils.showDialog(CreateDailyDairyParentActivity.this, page_title, error);
                } else {
                    SchoolAppUtils.showDialog(CreateDailyDairyParentActivity.this, page_title,
                            getResources().getString(R.string.error));
                }

            }
        }
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
            Toast.makeText(CreateDailyDairyParentActivity.this, "Please install a File Manager.",
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
                                    CreateDailyDairyParentActivity.this, selectedUri);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    Log.d("TAG", "File Path: " + path);
                    buttonAttachment.setText("File Attached");

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

//            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buttonAttachment.setText("Source File not exist :"
                            + buttonAttachment.getText().toString());
                }
            });

            return 0;

        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(Urls.upLoadFileFromDailyDiary);
                Log.d("test", "test");
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                Log.d("test", "test");
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);
                Log.d("test", "test");
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                InputStream stream = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(stream);

                // put output stream into a string
                BufferedReader br = new BufferedReader(isReader);

                String result = null;
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    encrypted_file_name = line;
                }

                System.out.println(encrypted_file_name);
                br.close();
                Log.i("uploadFile", "HTTP Response is : " + result + ": "
                        + serverResponseCode);

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                // close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

              //  dialog.dismiss();

                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonAttachment
                                .setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(CreateDailyDairyParentActivity.this,
                                "MalformedURLException", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

              //  dialog.dismiss();

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonAttachment.setText("Got Exception : see logcat ");
                        Toast.makeText(CreateDailyDairyParentActivity.this,
                                "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception",
                        "Exception : " + e.getMessage(), e);
            }
//            dialog.dismiss();

            return serverResponseCode;

        } // End else block
    }
}
