package com.knwedu.ourschool.fragments;

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

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.BlogActivity;
import com.knwedu.ourschool.adapter.BlogAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.knwedu.ourschool.vo.BlogBean;

public class BlogListFragment extends Fragment {
	private ListView list;
	private BlogAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Object> blogs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_blog_list, container, false);
		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));
		initialize();
		return view;
	}

	private void initialize() {

		InMobi.initialize(getActivity(),
				getResources().getString(R.string.InMobi_Property_Id));
		InMobi.setLogLevel(InMobi.LOG_LEVEL.DEBUG);
		IMBanner banner = (IMBanner) view.findViewById(R.id.banner);
		banner.loadBanner();
		list = (ListView) view.findViewById(R.id.listview);
	}

	@Override
	public void onResume() {
		super.onResume();
		String url;
		/*
		 * url = URLs.GetBlogs; url = url + "email="
		 * +SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERNAME)
		 * + "&password=" +SchoolAppUtils.GetSharedParameter(getActivity(),
		 * Constants.PASSWORD);
		 */List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.UORGANIZATIONID)));

		new GetBlogsAsyntask().execute(nameValuePairs);
	};

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			BlogBean blogBean = (BlogBean) blogs.get(position);
			Intent intent = new Intent(getActivity(), BlogActivity.class);
			intent.putExtra(Constants.BLOG_ID, blogBean.getBlogId());
			intent.putExtra(Constants.PAGE_TITLE, getActivity().getTitle()
					.toString());
			intent.putExtra(Constants.BLOG_IMAGE, blogBean.getImage());
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
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getActivity().getTitle().toString());
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_blog_list);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			blogs = new ArrayList<Object>();
			blogs.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_blog_list);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JsonParser jSONParser = new JsonParser();
						blogs = jSONParser.getBlogData(json.toString());
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
				if (blogs != null && blogs.size() > 0) {
					adapter = new BlogAdapter(getActivity(), blogs);
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), "No Blog data");

				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), error);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(),
							getResources().getString(R.string.error));
				}

			}
		}

	}

}
