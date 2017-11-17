package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.college.vo.CollegeBlogBean;
import com.knwedu.comschoolapp.R;


public class CollegeBlogAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Object> blogs;
	private CollegeBlogBean blogBean;
	
	public CollegeBlogAdapter(Context context, ArrayList<Object> blogs)
	{
		this.context = context;
		this.blogs = blogs;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		if(this.blogs != null)
		{
			return this.blogs.size();
		}
		return 10;
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
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.college_blog_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.course_txt);
			holder.description 	= (TextView)convertView.findViewById(R.id.description_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.photo_icon);
			holder.noOfComments = (TextView) convertView.findViewById(R.id.no_of_comment_txt);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		blogBean = (CollegeBlogBean) blogs.get(position);
		
		holder.title.setText(blogBean.getTitle());
		holder.description.setText(blogBean.getDescription());
		holder.noOfComments.setText("No. of Comments: " + blogBean.getNo_comments());
		
		if (blogBean.getImage().length() > 0) {
			new CollegeLoadImageAsyncTask(convertView.getContext(), holder.img, CollegeUrls.image_url_userpic,
					blogBean.getImage(), false).execute();
		}
		int no_of_comments = blogBean.getCommentList().size();
		if(no_of_comments>0)
		{
			holder.noOfComments.setText(context.getResources().getText(R.string.number_of_comments_p) + 
					" " + 
					no_of_comments);
		}
		else
		{
			holder.noOfComments.setText(context.getResources().getText(R.string.number_of_comments_p) + " 0");
		}
		
		CollegeAppUtils.setFont(context, holder.description);
		CollegeAppUtils.setFont(context, holder.title);
		return convertView;
	}
	private class ViewHolder
	{
		TextView description, title, noOfComments;
		ImageView img,attchment;
	}
	
}