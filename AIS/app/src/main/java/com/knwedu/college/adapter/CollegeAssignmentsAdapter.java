package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knwedu.college.CollegeAssignmentActivity;
import com.knwedu.college.CollegeTestActivity;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.comschoolapp.R;




public class CollegeAssignmentsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Assignment> assignments;
	public int []check;
	int type;
	private String date,title;
	public CollegeAssignmentsAdapter(Context context, ArrayList<Assignment> assignments,int type, String title)
	{
		this.context = context;
		this.assignments = assignments;
		check = new int[this.assignments.size()];
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.type = type;
		this.title = title;
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
			convertView = inflater.inflate(R.layout.college_assignment_items, null);
			holder = new ViewHolder();
			holder.dateDisplay = (LinearLayout) convertView.findViewById(R.id.title_layout);
			holder.day 	= (TextView)convertView.findViewById(R.id.day_txt);
			holder.dayNum 	= (TextView)convertView.findViewById(R.id.day_num_txt);
			holder.description 	= (TextView)convertView.findViewById(R.id.description_txt);
			holder.subject 	= (TextView)convertView.findViewById(R.id.subject_txt);
			holder.teacherName = (TextView) convertView.findViewById(R.id.teacher_txt);
			holder.marks = (ImageView) convertView.findViewById(R.id.marks_txt);
			holder.mnLayout = (RelativeLayout) convertView.findViewById(R.id.main_layout);
			//holder.marks_label = (TextView) convertView.findViewById(R.id.marks_label_txt);
			holder.attachment = (ImageView) convertView
					.findViewById(R.id.img_attachment);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if(assignments.get(position).check)
		{
			holder.dateDisplay.setVisibility(View.VISIBLE);
			holder.dayNum.setText(CollegeAppUtils.getDay(assignments.get(position).submit_date));
			String year = assignments.get(position).submit_date.substring(0, 4);
			String month = assignments.get(position).submit_date.substring(5, 7);
			String day = assignments.get(position).submit_date.substring(8, 10);
			holder.day.setText(Integer.parseInt(day) + "/" + Integer.parseInt(month) + "/" + year);
		}
		else
		{
			holder.dateDisplay.setVisibility(View.GONE);
		}
		CollegeAppUtils.setFont(context, holder.description);
		CollegeAppUtils.setFont(context, holder.subject);
		CollegeAppUtils.setFont(context, holder.teacherName);
		holder.teacherName.setText(assignments.get(position).teacher_name);
		holder.description.setText(assignments.get(position).title);
		holder.subject.setText(assignments.get(position).sub_name);
		if(assignments.get(position).is_published.equalsIgnoreCase("0"))
	    {
			holder.marks.setVisibility(View.GONE);
	    }
	    else
	    {
	    	holder.marks.setVisibility(View.VISIBLE);
	    }
		if (!assignments.get(position).file_name.equals("")
				&& !assignments.get(position).attachment.equals("")&&(CollegeAppUtils.isOnline(context))) {
			holder.attachment.setVisibility(View.VISIBLE);
		} else {

			holder.attachment.setVisibility(View.GONE);
		}

		holder.mnLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(type == 1){
				Intent intent = new Intent(context,
						CollegeAssignmentActivity.class);
				intent.putExtra(CollegeConstants.ASSIGNMENT_TYPE, 1);
				intent.putExtra(CollegeConstants.ASSIGNMENT,
						assignments.get(position).object.toString());
				intent.putExtra(CollegeConstants.PAGE_TITLE, title);
				context.startActivity(intent);
				}
				else if(type == 2){
					Intent intent = new Intent(context, CollegeTestActivity.class);
					intent.putExtra(CollegeConstants.ASSIGNMENT_TYPE, 2);
					intent.putExtra(CollegeConstants.TEST,
							assignments.get(position).object.toString());
					intent.putExtra(CollegeConstants.PAGE_TITLE, title);
					context.startActivity(intent);


				}


			}
		});


			
		return convertView;
	}
	private class ViewHolder
	{
		LinearLayout dateDisplay;
		TextView description, subject, dayNum, day, teacherName; 
		ImageView marks, attachment;
		RelativeLayout mnLayout;
	}
}