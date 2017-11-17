package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.BlogCommentsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.LoadImageAsyncTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.ourschool.vo.BlogBean;
import com.knwedu.ourschool.vo.BlogCommentsBean;

public class BlogActivity extends Activity {
	private ListView list;
	private BlogCommentsAdapter adapter;
	private ArrayList<BlogCommentsBean> comments;
	private ArrayList<Object> blogs;
	private TextView title, date, desc, createdBy;
	private ImageView imageView, attchment;
	private ProgressDialog dialog;
	private View view;
	private EditText comment;
	private Button submit;
	private String blogId = "";
	private String blogImage ="";
	private String page_title = "";
	private BlogBean blogBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_blog);
		page_title = getIntent().getStringExtra(Constants.PAGE_TITLE);
		SchoolAppUtils.loadAppLogo(BlogActivity.this,
				(ImageButton) findViewById(R.id.app_logo));

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

		InMobi.initialize(BlogActivity.this,
				getResources().getString(R.string.InMobi_Property_Id));
		IMBanner banner = (IMBanner) findViewById(R.id.banner);
		banner.loadBanner();

		((ImageButton) findViewById(R.id.back_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

		((TextView) findViewById(R.id.header_text)).setText(page_title);
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
			blogId = getIntent().getExtras().getString(Constants.BLOG_ID);
			blogImage = getIntent().getExtras().getString(Constants.BLOG_IMAGE);
			if (blogId.length() > 0) {
				initializeData();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(BlogActivity.this, Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(BlogActivity.this,
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(BlogActivity.this,
						Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("id", blogId));

		new GetBlogsAsyntask().execute(nameValuePairs);
	};

	private void initializeData() {

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (comment.getText().toString().trim().length() <= 0) {
					Toast.makeText(BlogActivity.this, "Enter Comment",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					String commentS = null;
					String dateS = null;
					commentS = comment.getText().toString();
					dateS = SchoolAppUtils.getCurrentDate();

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							6);
					nameValuePairs.add(new BasicNameValuePair("user_id",
							SchoolAppUtils.GetSharedParameter(
									BlogActivity.this, Constants.USERID)));
					nameValuePairs.add(new BasicNameValuePair("user_type_id",
							SchoolAppUtils.GetSharedParameter(
									BlogActivity.this, Constants.USERTYPEID)));
					nameValuePairs.add(new BasicNameValuePair(
							"organization_id", SchoolAppUtils
									.GetSharedParameter(BlogActivity.this,
											Constants.UORGANIZATIONID)));
					nameValuePairs.add(new BasicNameValuePair("id", blogId));
					nameValuePairs.add(new BasicNameValuePair("comment",
							commentS));
					nameValuePairs.add(new BasicNameValuePair("date", dateS));

					new AddCommentAsyntask().execute(nameValuePairs);
					comment.setText("");
				}
			}
		});

	}

	private void setListView() {

		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		if (blogs.size() > 0) {
			blogBean = (BlogBean) blogs.get(0);
			title.setText(blogBean.getTitle());
			date.setText(blogBean.getCreatedAt());
			desc.setText(blogBean.getDescription());
			createdBy.setText(blogBean.getCreatedBy());
			if (blogBean.getImage().equalsIgnoreCase("")) {
				attchment.setVisibility(View.INVISIBLE);
			} else {
				attchment.setVisibility(View.VISIBLE);
			}
			if (blogBean.getIsComment().equalsIgnoreCase("0")) {
				comment.setVisibility(View.INVISIBLE);
				submit.setVisibility(View.INVISIBLE);
			} else {
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
			new LoadImageAsyncTask(BlogActivity.this, imageView,
					Urls.image_url_blog, blogBean.getImage(), true).execute();
			if (comments != null) {
				adapter = new BlogCommentsAdapter(BlogActivity.this, comments);
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			if (blogBean.getIsComment().equalsIgnoreCase("0")) {
				comment.setVisibility(View.INVISIBLE);
				submit.setVisibility(View.INVISIBLE);
			}
		}

	}

	private class GetBlogsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				if (dialog.isShowing())

				{
					return;
				}
			}
			dialog = new ProgressDialog(BlogActivity.this);

			dialog.setTitle(page_title);

			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			comments = new ArrayList<BlogCommentsBean>();
			comments.clear();

			blogs = new ArrayList<Object>();
			blogs.clear();

			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension", Urls.api_blog_comment);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_blog_comment);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JsonParser jSONParser = new JsonParser();
						blogs = jSONParser.getBlogData(json.toString());

						if (blogs.size() > 0) {
							BlogBean blogBean = (BlogBean) blogs.get(0);
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
				setListView();
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(BlogActivity.this, page_title,
							error);
				} else {
					SchoolAppUtils.showDialog(BlogActivity.this, page_title,
							getResources().getString(R.string.unknown_response));
				}

			}
		}

	}

	private class AddCommentAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				if (dialog.isShowing())

				{
					return;
				}
			}
			dialog = new ProgressDialog(BlogActivity.this);

			dialog.setTitle(page_title);

			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			comments = new ArrayList<BlogCommentsBean>();
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension", Urls.api_blog_comment_add);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_blog_comment_add);

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
			if (result) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs.add(new BasicNameValuePair("user_id",
						SchoolAppUtils.GetSharedParameter(BlogActivity.this,
								Constants.USERID)));
				nameValuePairs.add(new BasicNameValuePair("user_type_id",
						SchoolAppUtils.GetSharedParameter(BlogActivity.this,
								Constants.USERTYPEID)));
				nameValuePairs.add(new BasicNameValuePair("organization_id",
						SchoolAppUtils.GetSharedParameter(BlogActivity.this,
								Constants.UORGANIZATIONID)));
				nameValuePairs.add(new BasicNameValuePair("id", blogId));

				new GetBlogsAsyntask().execute(nameValuePairs);
				Toast.makeText(getApplicationContext(),
						"Comment posted successfully.", Toast.LENGTH_LONG)
						.show();
			} else {

				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(BlogActivity.this, page_title,
							error);
				} else {
					SchoolAppUtils.showDialog(BlogActivity.this, page_title,
							getResources().getString(R.string.unknown_response));
				}

			}
		}

	}

	private void showDialog() {
		final String Url = Urls.base_url + Urls.api_get_bulletin_doc + "/"
				+ blogId;

		new AlertDialog.Builder(BlogActivity.this)
				.setTitle("Select option")
				.setPositiveButton("View Document",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with view
								SchoolAppUtils.imagePdfViewDocument(BlogActivity.this,
										Urls.base_url
												+ Urls.api_get_bulletin_doc
												+ "/" + blogId,blogImage);
							}
						})
				.setNegativeButton("Download",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// download
								
								final DownloadTask downloadTask = new DownloadTask(BlogActivity.this, blogBean.getImage());
								downloadTask.execute(Urls.base_url + Urls.api_get_bulletin_doc + "/" + blogId);
								

								/*Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(Urls.base_url
										+ Urls.api_get_bulletin_doc + "/"
										+ blogId));
								startActivity(i);*/

							}
						}).setIcon(android.R.drawable.ic_dialog_info).show();

	}
}
