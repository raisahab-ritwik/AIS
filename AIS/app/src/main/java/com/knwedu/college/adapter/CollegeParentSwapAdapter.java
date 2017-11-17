package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeDataStructureFramwork.ParentProfileInfo;
import com.knwedu.comschoolapp.R;

public class CollegeParentSwapAdapter extends BaseAdapter {
	ViewHolder holder;
	Context context;
	ArrayList<ParentProfileInfo> assignments;
	private LayoutInflater inflater;
	
	public CollegeParentSwapAdapter(Context context,
			ArrayList<ParentProfileInfo> assignments) {
		this.context = context;
		this.assignments = assignments;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return assignments.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{

		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.college_announcement_item, null);
			holder = new ViewHolder();
			holder.description 	= (TextView)convertView.findViewById(R.id.description_txt);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.description.setText(assignments.get(position).fullname);
		return convertView;
	}
	private class ViewHolder
	{
		LinearLayout dateDisplay;
		TextView description, subject, dayNum, day;
	}
}
