package com.knwedu.ourschool.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.SubjectStudent;

public class SubjectStudentsAdapter extends BaseAdapter  {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	ArrayList<SubjectStudent> student;
	private String date;
	public SubjectStudentsAdapter(Context context, ArrayList<SubjectStudent> student)
	{
		this.context = context;
		this.student =student;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.student != null)
		{
			return this.student.size();
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
			convertView = inflater.inflate(R.layout.class_fellow_items, null);
			holder = new ViewHolder();
			holder.name 	= (TextView)convertView.findViewById(R.id.name_txt);
			holder.rollno=(TextView) convertView.findViewById(R.id.rollno_text);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		//SchoolAppUtils.setFont(context, holder.name);
		//SchoolAppUtils.setFont(context, holder.rollno);
		holder.name.setText(student.get(position).fname + " "
		+student.get(position).lname);
		holder.rollno.setText(student.get(position).roll_no );
		return convertView;
	}
	private class ViewHolder
	{
		TextView name,rollno;
	
	}
}
