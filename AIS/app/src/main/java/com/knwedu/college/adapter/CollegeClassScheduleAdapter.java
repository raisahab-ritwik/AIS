package com.knwedu.college.adapter;
/*package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.ourschool.utils.DataStructureFramwork.ClassSchedule;
import com.knwedu.ourschool.utils.SchoolAppUtils;


public class ClassScheduleAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<ClassSchedule> assignments;
	public int []check;
	private String date;
	public ClassScheduleAdapter(Context context, ArrayList<ClassSchedule> assignments)
	{
		this.context = context;
		this.assignments = assignments;
		check = new int[this.assignments.size()];
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.assignments != null)
		{
			return this.assignments.size();
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
			convertView = inflater.inflate(R.layout.class_schedule_items, null);
			holder = new ViewHolder();
			holder.description 	= (TextView)convertView.findViewById(R.id.description_txt);
			holder.subject 	= (TextView)convertView.findViewById(R.id.subject_txt);
			holder.teacherName = (TextView) convertView.findViewById(R.id.teacher_txt);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.subject);
		SchoolAppUtils.setFont(context, holder.teacherName);
		String year = assignments.get(position).date.substring(0, 4);
		String month = assignments.get(position).date.substring(5, 7);
		String day = assignments.get(position).date.substring(8, 10);
		holder.teacherName.setText(Integer.parseInt(day) + "/" + Integer.parseInt(month) + "/" + year);
		holder.description.setText(assignments.get(position).description);
		holder.subject.setText(assignments.get(position).title);
		return convertView;
	}
	private class ViewHolder
	{
		TextView description, subject, teacherName;
	}
}*/