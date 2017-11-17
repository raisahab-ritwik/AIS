package com.knwedu.college.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.knwedu.college.CollegeBlogActivity;
import com.knwedu.college.adapter.CollegeBlogAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.college.vo.CollegeBlogBean;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class CollegeBlogListFragment extends Fragment {
	private ListView list;
	private CollegeBlogAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private String pageTitle = "";
	private ArrayList<Object> blogs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_blog_list, container, false);


		initialize();
		return view;
	}

	private void initialize() {

		//-----------------------------
//		InMobi.initialize(getActivity(),
//				getResources().getString(R.string.InMobi_Property_Id));
//		InMobi.setLogLevel(InMobi.LOG_LEVEL.DEBUG);
//		IMBanner banner = (IMBanner) view.findViewById(R.id.banner);
//		banner.loadBanner();
		//-----------------------------
		pageTitle = getActivity().getTitle().toString();
		list = (ListView) view.findViewById(R.id.listview);
	}

	@Override
	public void onResume() {
		super.onResume();
		String url;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
				.GetSharedParameter(getActivity(), "id")));
		new GetBlogsAsyntask().execute(nameValuePairs);
	};

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			CollegeBlogBean blogBean = (CollegeBlogBean) blogs.get(position);
			Intent intent = new Intent(getActivity(), CollegeBlogActivity.class);
			intent.putExtra(CollegeConstants.BLOG_ID, blogBean.getBlogId());
			intent.putExtra(CollegeConstants.PAGE_TITLE, pageTitle);
			getActivity().startActivity(intent);
		}
	};

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

	private class GetBlogsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.blog));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", CollegeUrls.api_blog_list);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			blogs = new ArrayList<Object>();
			blogs.clear();
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_blog_list);



			if (json != null) {
				try {
					if (json.getString("result").equalsIgnoreCase("1")) {
						CollegeJsonParser jSONParser = new CollegeJsonParser();
						blogs = jSONParser.getBlogData(json.toString());

//						//--------------moumita-------
//						if (blogs.size() < 1) {
//
//							error = "No Blogs found";
//						}
						return true;
					}
					else {
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
			}

			return false;
//
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {
				if (blogs != null && blogs.size() > 0) {
					adapter = new CollegeBlogAdapter(getActivity(), blogs);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}
				else {

//---------------------------------
//					if (error.length() > 0) {
//						CollegeAppUtils.showDialog(getActivity(),
//								CollegeAppUtils.GetSharedParameter(
//										getActivity(),
//										CollegeConstants.PAGE_TITLE), error);
//					}
				}
			} else {
				if (blogs != null) {
					adapter = new CollegeBlogAdapter(getActivity(), blogs);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(getActivity(), pageTitle,
								error);
					} else {
						CollegeAppUtils
								.showDialog(
										getActivity(),
										pageTitle,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							getActivity(),
							pageTitle,
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

}
