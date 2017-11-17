package com.knwedu.ourschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.Career;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.ArrayList;

public class CareerListAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Career> careers;
	private boolean isView;

	public CareerListAdapter(Context context, ArrayList<Career> careers) {
		this.context = context;
		this.careers = careers;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.careers != null) {
			return this.careers.size();
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
			convertView = inflater.inflate(R.layout.career_item_view, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.textViewTitle);
			holder.description = (TextView) convertView.findViewById(R.id.textViewDesc);
			holder.created_on = (TextView) convertView.findViewById(R.id.textViewCretedDate);
			holder.created_by = (TextView) convertView.findViewById(R.id.textViewGivenBy);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.title);
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.created_by);
		SchoolAppUtils.setFont(context, holder.created_on);

		holder.title.setText(careers.get(position).title);
		holder.description.setText(careers.get(position).description);
		holder.created_by.setText(careers.get(position).created_by);
		holder.created_on.setText(careers.get(position).created_date);
		return convertView;
	}

	private class ViewHolder {
		TextView title, description, created_by, created_on;
	}
}