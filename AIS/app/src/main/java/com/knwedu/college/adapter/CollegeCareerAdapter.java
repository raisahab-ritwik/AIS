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
import com.knwedu.college.utils.CollegeDataStructureFramwork.Carreer;
import com.knwedu.comschoolapp.R;

public class CollegeCareerAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	int type;
	ArrayList<Carreer> news;

	public CollegeCareerAdapter(Context context, ArrayList<Carreer> news,
			int type) {
		this.context = context;
		this.news = news;
		this.type = type;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.news != null) {
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.college_news_items, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title_txt);
			holder.description = (TextView) convertView
					.findViewById(R.id.description_txt);
			holder.img = (ImageView) convertView
					.findViewById(R.id.next);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CollegeAppUtils.setFont(context, holder.description);
		CollegeAppUtils.setFont(context, holder.title);
		if (type == 3) {
			holder.title.setText(news.get(position).student_name);
			holder.description.setVisibility(View.GONE);
			holder.img.setVisibility(View.GONE);
		} else {
			holder.title.setText(news.get(position).job_title);
			holder.description.setText(news.get(position).job_description);
		}
		
		return convertView;
	}

	private class ViewHolder {
		TextView title, description;
		ImageView img;
	}
}
