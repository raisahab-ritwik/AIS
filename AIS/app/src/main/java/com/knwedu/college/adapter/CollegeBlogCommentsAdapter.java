package com.knwedu.college.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.college.vo.CollegeBlogCommentsBean;
import com.knwedu.comschoolapp.R;

public class CollegeBlogCommentsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	String blogId;
	private ArrayList<CollegeBlogCommentsBean> comments;
	private String comment_id;
	private String userId;

	public CollegeBlogCommentsAdapter(Context context,
			ArrayList<CollegeBlogCommentsBean> comments, String blogId) {
		this.context = context;
		this.comments = comments;
		this.blogId = blogId;
		userId = CollegeAppUtils.GetSharedParameter(context, "id");
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.comments != null) {
			return this.comments.size();
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
			convertView = inflater.inflate(R.layout.college_blog_comment_items,
					null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name_txt);
			// holder.date = (TextView) convertView.findViewById(R.id.date_txt);
			holder.comment = (TextView) convertView
					.findViewById(R.id.comment_txt);
			holder.delete = (ImageView) convertView
					.findViewById(R.id.delete_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CollegeBlogCommentsBean bean = comments.get(position);
		holder.name.setText(bean.getName());
		// holder.date.setText(bean.getCreatedAt());
		holder.comment.setText(bean.getComment());
		comment_id = bean.getComment_id();
		Log.d("comment_id........", bean.getName()+"....."+comment_id);
		if (bean.getIs_delete().equals("0")) {
			holder.delete.setVisibility(View.GONE);
		} else {
			holder.delete.setVisibility(View.VISIBLE);
			holder.delete.setOnClickListener(new ClickListner(position,
					"delete"));
		}
		return convertView;
	}

	private class ViewHolder {
		TextView name, date, comment;
		ImageView delete;
	}

	class ClickListner implements OnClickListener {
		int mPosition;
		String mTag,mComment_id;

		public ClickListner(int position, String tag) {
			mPosition = position;
			mTag = tag;
		}

		@Override
		public void onClick(View v) {
			if (mTag.equalsIgnoreCase("delete")) {
				CollegeBlogCommentsBean bean = comments.get(mPosition);
				mComment_id = bean.getComment_id();
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(context, "id")));
				nameValuePairs.add(new BasicNameValuePair("comment_id",
						mComment_id));
				new deleteAsync().execute(nameValuePairs);
			}
		}
	}

	private class deleteAsync extends
			AsyncTask<List<NameValuePair>, String, Boolean> {
		private String error;

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension", CollegeUrls.api_blog_comment_delete);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_blog_comment_delete);

			try {
				if (json.getString("result").equalsIgnoreCase("1")) {
					error = "comment deleted Successfully";
					return true;
				} else {
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

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			showDialog(error);
		}
	}

	private void showDialog(String str) {
		final Dialog dialog2 = new Dialog(context);
		dialog2.setCanceledOnTouchOutside(false);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.custom_dialog);
		TextView text = (TextView) dialog2.findViewById(R.id.text);
		text.setText(str);
		text.setGravity(Gravity.CENTER);
		Button dialogButton = (Button) dialog2
				.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog2.dismiss();
				((Activity) context).finish();
			}
		});

		dialog2.show();
	}

}