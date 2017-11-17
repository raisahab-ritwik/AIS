package com.knwedu.college.adapter;
/*package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.ourschool.utils.DataStructureFramwork.Orginization;
import com.knwedu.ourschool.utils.SchoolAppUtils;


public class OrganizationAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	ArrayList<Orginization> orginization;
	public OrganizationAdapter(Context context, ArrayList<Orginization> orginization)
	{
		this.context = context;
		this.orginization = orginization;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.orginization != null)
		{
			return orginization.size();
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
			convertView = inflater.inflate(R.layout.orginization_items, null);
			holder = new ViewHolder();
			holder.title 	= (TextView)convertView.findViewById(R.id.title_txt);
			holder.description 	= (TextView)convertView.findViewById(R.id.description_txt);
			holder.date 	= (TextView)convertView.findViewById(R.id.date_txt);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.title);
		SchoolAppUtils.setFont(context, holder.date);
		holder.title.setText(orginization.get(position).name+" Activity : "+orginization.get(position).activity);
		holder.description.setText(orginization.get(position).description);
		holder.date.setText(orginization.get(position).date);
		return convertView;
	}
	private class ViewHolder
	{
		TextView title, description, date;
	}
}*/