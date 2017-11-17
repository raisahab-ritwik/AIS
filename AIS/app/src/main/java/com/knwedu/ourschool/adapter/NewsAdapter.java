package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.News;
import com.knwedu.ourschool.utils.SchoolAppUtils;


public class NewsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	ArrayList<News> news;
	public NewsAdapter(Context context, ArrayList<News> news)
	{
		this.context = context;
		this.news = news;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.news != null)
		{
			return news.size();
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
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.news_items, null);
			holder = new ViewHolder();
			holder.title 	= (TextView)convertView.findViewById(R.id.title_txt);
			holder.description 	= (TextView)convertView.findViewById(R.id.description_txt);
			holder.date_txt = (TextView)convertView.findViewById(R.id.date_text);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.title);
		if(news.get(position).class_name!=null){
			holder.title.setText(news.get(position).class_name);
			holder.description.setText(news.get(position).title);
		}else{
			holder.title.setText(news.get(position).title);
			holder.description.setText(news.get(position).description);
		}
		holder.date_txt.setText(news.get(position).date);
		return convertView;
	}
	private class ViewHolder
	{
		TextView title, description, date_txt;
	}
}