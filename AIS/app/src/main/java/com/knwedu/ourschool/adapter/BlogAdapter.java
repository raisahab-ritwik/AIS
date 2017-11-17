package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.vo.BlogBean;


public class BlogAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Object> blogs;
	
	public BlogAdapter(Context context, ArrayList<Object> blogs)
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
			convertView = inflater.inflate(R.layout.blog_item, null);
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
		
		BlogBean blogBean = (BlogBean) blogs.get(position);
		
		holder.title.setText(blogBean.getTitle());
		holder.description.setText(blogBean.getDescription());
		holder.noOfComments.setText("No. of Comments: " + blogBean.getNo_comments());
		
		/*if (blogBean.getImage().length() > 0) {
			new LoadImageAsyncTask(convertView.getContext(), holder.img, Urls.image_url_blog,
					blogBean.getImage(), false).execute();
		}*/
		/*int no_of_comments = blogBean.getCommentList().size();
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
		*/
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.title);
		return convertView;
	}
	private class ViewHolder
	{
		TextView description, title, noOfComments;
		ImageView img;
	}
}