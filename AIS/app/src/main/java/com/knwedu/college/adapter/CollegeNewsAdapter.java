package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Carr;
import com.knwedu.comschoolapp.R;

public class CollegeNewsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	ArrayList<Carr> news;

	public CollegeNewsAdapter(Context context, ArrayList<Carr> news) {
		this.context = context;
		this.news = news;
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CollegeAppUtils.setFont(context, holder.description);
		CollegeAppUtils.setFont(context, holder.title);
		if ((news.get(position).prog_name.equalsIgnoreCase("null"))
				&& (news.get(position).term_name.equalsIgnoreCase("null"))) {
			holder.title.setText(news.get(position).title);

		} else if (news.get(position).term_name.equalsIgnoreCase("null")) {
			holder.title.setText(news.get(position).prog_name + " "
					+ news.get(position).title);
		} else {
			holder.title.setText(news.get(position).prog_name + " "
					+ news.get(position).term_name + " "
					+ news.get(position).title);

		}

		holder.description.setText(news.get(position).description);
		return convertView;
	}

	private class ViewHolder {
		TextView title, description;
	}
}