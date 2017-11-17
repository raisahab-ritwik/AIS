package com.knwedu.college;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Carreer;
import com.knwedu.comschoolapp.R;

public class CollegeCareerActivity extends Activity {
	private TextView description, header, date;
	private Carreer news;
	private Button getDocBtn;
	private TextView headerText,textViewGivenBy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_news);
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
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
		description = (TextView) findViewById(R.id.assignment_txt);
		header = (TextView) findViewById(R.id.header_2_text);
		getDocBtn = (Button) findViewById(R.id.download_btn);
		headerText = (TextView) findViewById(R.id.header_text);
		textViewGivenBy = (TextView) findViewById(R.id.textViewGivenBy);
		headerText.setText("Career");
		getDocBtn.setVisibility(View.GONE);
		date = (TextView) findViewById(R.id.date_text);
		if (getIntent().getExtras() != null) {
			String temp = getIntent().getExtras().getString(
					CollegeConstants.NEWS);
			if (temp != null) {
				JSONObject object = null;
				try {
					object = new JSONObject(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object != null) {
					news = new Carreer(object);
				}
			}
		}
		if (news != null) {
			header.setText("" + news.job_title);
			description.setText("" + news.job_description);
			try {
				if (!news.created_date.equalsIgnoreCase("null"))
					date.setText("" + news.created_date);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				date.setText("");
			}

		}
		textViewGivenBy.setText(news.given_by);
	}

}
