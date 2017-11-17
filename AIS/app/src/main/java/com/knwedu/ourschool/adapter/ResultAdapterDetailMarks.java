package com.knwedu.ourschool.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.MarksDetails;


public class ResultAdapterDetailMarks extends BaseAdapter{

	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<MarksDetails> results;
		
	public ResultAdapterDetailMarks(Context context, ArrayList<MarksDetails> fellow)
	{
		this.context = context;
		this.results = fellow;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.results != null)
		{
			return this.results.size();
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
			convertView = inflater.inflate(R.layout.exam_result_item_new, null);
			holder = new ViewHolder();
			holder.groupName 	= (TextView)convertView.findViewById(R.id.textView_subject_name_new);
			holder.subjectName 	= (TextView)convertView.findViewById(R.id.text_subject);
			holder.totalMarks = (TextView)convertView.findViewById(R.id.text_total_marks);
			holder.obtainedMarks 	= (TextView)convertView.findViewById(R.id.text_marks_obtained);
			//holder.totalMarksTheory = (TextView)convertView.findViewById(R.id.text_marks_obtained);
			holder.totalgrade 	= (TextView)convertView.findViewById(R.id.text_grade);
			holder.totalremarks 	= (TextView)convertView.findViewById(R.id.text_remarks);
		
			holder.tab2 	= (TableRow)convertView.findViewById(R.id.tableRow2);

			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		if(results.get(position).check)
		{
			holder.groupName.setVisibility(View.VISIBLE);
			//holder.tab1.setVisibility(View.VISIBLE);
			//holder.title.setText(subjects.get(position).classs + " " + subjects.get(position).section_name);
			if (results.get(position).subject_group.equalsIgnoreCase("NA")) {
				holder.tab2.setVisibility(View.GONE);
				holder.groupName.setVisibility(View.GONE);
			} 
		}
		else
		{   
			holder.tab2.setVisibility(View.GONE);
			holder.groupName.setVisibility(View.GONE);
		}
			
		Log.d("title", results.get(position).Type + "|" + results.get(position).Title);
		holder.groupName.setText(results.get(position).subject_group);
		holder.subjectName.setText(results.get(position).header);
		holder.totalMarks.setText(results.get(position).Subject_Total_Marks);
		holder.obtainedMarks.setText(results.get(position).Subject_Marks);
		//holder.totalMarksTheory.setText(results.get(position).Subject_Total_Marks);
		holder.totalgrade.setText(results.get(position).grade);
		holder.totalremarks.setText(results.get(position).remarks);
		
		
		
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView subjectName;
		TextView totalMarks;
		TextView groupName;
		TextView obtainedMarks;
		TextView totalMarksTheory;
		TextView totalgrade;
		TextView totalremarks;
		TableRow tab1,tab2;
		
	}

}
