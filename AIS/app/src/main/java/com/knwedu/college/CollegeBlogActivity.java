package com.knwedu.college;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.college.adapter.CollegeBlogCommentsAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.college.vo.CollegeBlogBean;
import com.knwedu.college.vo.CollegeBlogCommentsBean;
import com.knwedu.comschoolapp.R;

public class CollegeBlogActivity extends Activity {
	private ListView list;
	private CollegeBlogCommentsAdapter adapter;
	private ArrayList<CollegeBlogCommentsBean> comments;
	private ArrayList<Object> blogs;
	private TextView title, date, desc, createdBy;
	private ImageView imageView, attchment;
	private ProgressDialog dialog;
	private View view;
	private EditText comment;
	private Button submit;
	private String blogId;
	private String page_title = "";
	private CollegeBlogBean blogBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.college_activity_blog);
		page_title = getIntent().getStringExtra(CollegeConstants.PAGE_TITLE);
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

		/*
		 * InMobi.initialize(BlogActivity.this,
		 * getResources().getString(R.string.InMobi_Property_Id)); IMBanner
		 * banner = (IMBanner) findViewById(R.id.banner); banner.loadBanner();
		 */
		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		((TextView) findViewById(R.id.header_text)).setText("Blog Details");
		View child = getLayoutInflater().inflate(
				R.layout.blog_detail_screen_upper, null);
		title = (TextView) child.findViewById(R.id.title_txt);
		date = (TextView) child.findViewById(R.id.date_txt);
		desc = (TextView) child.findViewById(R.id.desc_txt);
		createdBy = (TextView) child.findViewById(R.id.creator_txt);
		imageView = (ImageView) child.findViewById(R.id.image_vieww);
		attchment = (ImageView) child.findViewById(R.id.attchment);
		list = (ListView) findViewById(R.id.listview);
		comment = (EditText) findViewById(R.id.add_comment_edt);
		submit = (Button) findViewById(R.id.submit_btn);
		list.addHeaderView(child);
        
		if (getIntent().getExtras() != null) {
			blogId = getIntent().getExtras()
					.getString(CollegeConstants.BLOG_ID);
			if (blogId.length() > 0) {
				initializeData();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(CollegeBlogActivity.this, "id")));
		nameValuePairs.add(new BasicNameValuePair("blog_id", blogId));
		new GetBlogsAsyntask().execute(nameValuePairs);
	};

	private void initializeData() {
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (comment.getText().toString().trim().length() <= 0) {
					Toast.makeText(CollegeBlogActivity.this, "Enter Comment",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					String commentS = null;
					String dateS = null;
					commentS = comment.getText().toString();
					dateS = CollegeAppUtils.getCurrentDate();

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							3);
					nameValuePairs.add(new BasicNameValuePair("id",
							CollegeAppUtils.GetSharedParameter(
									CollegeBlogActivity.this, "id")));
					nameValuePairs
							.add(new BasicNameValuePair("blog_id", blogId));
					nameValuePairs.add(new BasicNameValuePair("comment",
							commentS));
					new AddCommentAsyntask().execute(nameValuePairs);
					comment.setText("");
				}
			}
		});

	}

	private void setListView() {

		if (blogs.size() > 0) {
			blogBean = (CollegeBlogBean) blogs.get(0);
			title.setText(blogBean.getTitle());
			date.setText(blogBean.getCreatedAt());
			desc.setText(blogBean.getDescription());
			createdBy.setText(blogBean.getCreatedBy());
			if(blogBean.getfilename().equalsIgnoreCase(""))
			{
				attchment.setVisibility(View.INVISIBLE);
			}else
			{
				attchment.setVisibility(View.VISIBLE);
			}
			if(blogBean.getcommentstatus().equalsIgnoreCase("0"))
			{
				comment.setVisibility(View.INVISIBLE);
				submit.setVisibility(View.INVISIBLE);
			}else
			{
				comment.setVisibility(View.VISIBLE);
				submit.setVisibility(View.VISIBLE);
			}
			attchment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog();
				}
			});
			new CollegeLoadImageAsyncTask(CollegeBlogActivity.this, imageView,
					CollegeUrls.image_url_userpic, blogBean.getImage(), true)
					.execute();
			if (comments != null) {
				adapter = new CollegeBlogCommentsAdapter(
						CollegeBlogActivity.this, comments, blogId);
				list.setAdapter(adapter);
			}
		}

	}

	private class GetBlogsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				if (dialog.isShowing()) {
					return;
				}
			}
			dialog = new ProgressDialog(CollegeBlogActivity.this);
			dialog.setTitle(getResources().getString(R.string.blog));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			comments = new ArrayList<CollegeBlogCommentsBean>();
			comments.clear();

			blogs = new ArrayList<Object>();
			blogs.clear();
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension", CollegeUrls.api_blog_comment);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_blog_comment);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						CollegeJsonParser jSONParser = new CollegeJsonParser();
						blogs = jSONParser.getBlogData(json.toString());

						if (blogs.size() > 0) {
							CollegeBlogBean blogBean = (CollegeBlogBean) blogs
									.get(0);
							blogId = blogBean.getBlogId();
							comments = blogBean.getCommentList();
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
			if (result) {
				setListView();
			} else {
				setListView();
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(CollegeBlogActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeBlogActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeBlogActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	private class AddCommentAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				if (dialog.isShowing())

				{
					return;
				}
			}
			dialog = new ProgressDialog(CollegeBlogActivity.this);

			dialog.setTitle(getResources().getString(R.string.blog));

			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			comments = new ArrayList<CollegeBlogCommentsBean>();
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension", CollegeUrls.api_blog_comment_add);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_blog_comment_add);

			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
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
			if (result) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(CollegeBlogActivity.this, "id")));
				nameValuePairs.add(new BasicNameValuePair("blog_id", blogId));
				new GetBlogsAsyntask().execute(nameValuePairs);
				Toast.makeText(getApplicationContext(),
						"Comment posted successfully.", Toast.LENGTH_LONG)
						.show();
			} else {

				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(CollegeBlogActivity.this,
								page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										CollegeBlogActivity.this,
										page_title,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							CollegeBlogActivity.this,
							page_title,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	private void showDialog() {
		final String Url = CollegeUrls.base_url
				+ CollegeUrls.api_get_doc
				+ "/"
				+ CollegeAppUtils.GetSharedParameter(getApplicationContext(),
						"id") + "/" + blogBean.getBlogId() + "/" + "Blog";
		Log.d("URL OF TEACHER ASSIGNMENT..", Url);
		final Dialog dialog2 = new Dialog(CollegeBlogActivity.this);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.custom_selection_dwnld_dialog);
		TextView txtView = (TextView) dialog2.findViewById(R.id.txtView);
		TextView txtDwnld = (TextView) dialog2.findViewById(R.id.txtDownload);
		// if button is clicked, close the custom dialog
		txtView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						CollegeWebviewActivity.class);
				i.putExtra("Download_Teacher_Assignment", Url);
				i.putExtra("from", "TeacherAssignment");
				startActivity(i);
				dialog2.dismiss();
			}
		});
		txtDwnld.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(CollegeUrls.base_url
						+ CollegeUrls.api_get_doc
						+ "/"
						+ CollegeAppUtils.GetSharedParameter(
								getApplicationContext(), "id") + "/"
						+ blogBean.getBlogId() + "/" + "Blog"));
				startActivity(i);
				dialog2.dismiss();
			}
		});

		dialog2.show();

	}
}
