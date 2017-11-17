package com.knwedu.college;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.comschoolapp.R;

public class CollegeSubjectDetailActivity extends Activity {
	// private ListView list;
	private TextView name, phone, subject_txt, email;
	private LinearLayout name_layout, email_layout;
	private ProgressDialog dialog;
	private Subject info;
	private ImageView userImage;
	int num_teacher_name;
	TextView[] teacher_name;
	TextView[] teacher_email;

	// private Button classFellowBtn;
	// private ImageLoader imageTemp = ImageLoader.getInstance();
	// private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_subject_detail_lesson_plan);
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

	private void initialize() {
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		((TextView) findViewById(R.id.header_text)).setText("Academics");
		// list = (ListView) findViewById(R.id.listview);
		userImage = (ImageView) findViewById(R.id.image_vieww);
		// name_layout = (LinearLayout) findViewById(R.id.teacher_name_layout);
		email_layout = (LinearLayout) findViewById(R.id.teacher_email_layout);
		// name = (TextView) findViewById(R.id.teacher_name_txt);
		// email = (TextView) findViewById(R.id.email_txt);
		// phone = (TextView) findViewById(R.id.phone_txt);
		subject_txt = (TextView) findViewById(R.id.subject_txt);

		// classFellowBtn = (Button) findViewById(R.id.class_fellow_btn);
		/*
		 * options = new DisplayImageOptions.Builder()
		 * .showImageForEmptyUri(R.drawable.no_photo)
		 * .showImageOnFail(R.drawable.no_photo)
		 * .showStubImage(R.drawable.no_photo) .cacheInMemory() .cacheOnDisc()
		 * .build();
		 */
		if (getIntent().getExtras() != null) {
			String temp = getIntent().getExtras().getString("StudentSubject");
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object != null) {
					info = new Subject(object);
				}
			}
		}
		if (info != null) {
			initializeData();
		}

	}

	private void initializeData() {
		if (info != null) {
			// name.setText(info.fname + " " + info.lname);
			// email.setText(info.email);
			// phone.setText(info.phone);

			String[] tech_name = info.teacher.split("\\,");
			num_teacher_name = tech_name.length;
			// teacher_name = new TextView[num_teacher_name];// create an empty
			// array;

			/*
			 * for (int i = 0; i < num_teacher_name; i++) { // create a new
			 * textview final TextView rowTextView = new TextView(this);
			 * 
			 * // set some properties of rowTextView or something
			 * rowTextView.setText(tech_name[i]);
			 * 
			 * rowTextView.setBackgroundResource(R.drawable.textbox);
			 * rowTextView.setTextColor(Color.WHITE); rowTextView.setPadding(16,
			 * 16, 16, 16); // add the textview to the linearlayout
			 * name_layout.addView(rowTextView);
			 * 
			 * // save a reference to the textview for later teacher_name[i] =
			 * rowTextView; }
			 */
			// total number of textviews to add
			String[] tech_email = info.email.split("\\,");

			teacher_email = new TextView[num_teacher_name]; // create an empty
															// array;

			for (int i = 0; i < num_teacher_name; i++) {
				// create a new textview
				final TextView rowTextView = new TextView(this);

				// set some properties of rowTextView or something

				CollegeAppUtils.setFont(CollegeSubjectDetailActivity.this,
						rowTextView);
				rowTextView.setAutoLinkMask(Linkify.ALL);
				if (tech_email.length > 0)
					rowTextView.setText(tech_name[i] + "\n" + tech_email[i]);
				else
					rowTextView.setText(tech_name[i]);
				rowTextView.setBackgroundResource(R.drawable.textbox);
				rowTextView.setTextColor(Color.WHITE);
				rowTextView.setPadding(16, 16, 16, 16);
				// add the textview to the linearlayout
				email_layout.addView(rowTextView);

				// save a reference to the textview for later
				teacher_email[i] = rowTextView;
			}
			subject_txt.setText(info.sub_name);
			if (info.image != null) {
				/*
				 * new LoadImageAsyncTask(CollegeSubjectDetailActivity.this,
				 * userImage, Urls.base_url + Urls.image_url_userpic,
				 * info.image, true).execute();
				 */
			}

			/*
			 * classFellowBtn.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { Intent intent = new
			 * Intent(SubjectDetailActivity.this,
			 * ClassFellowListActivity.class); intent.putExtra("Section_id",
			 * info.section_id); startActivity(intent); } });
			 */
		}

	}

}
