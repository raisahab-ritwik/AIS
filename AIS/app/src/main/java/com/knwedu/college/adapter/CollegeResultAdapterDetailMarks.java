package com.knwedu.college.adapter;
/*package com.knwedu.ourschool.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
			holder.subjectName 	= (TextView)convertView.findViewById(R.id.textView_subject_name_new);
			holder.totalMarks = (TextView)convertView.findViewById(R.id.textView_total_new);
			holder.obtainedMarks 	= (TextView)convertView.findViewById(R.id.textView_obtained_new);
			holder.totalMarksTheory = (TextView)convertView.findViewById(R.id.textView_total_theory_new);
			holder.totalMarksPractical 	= (TextView)convertView.findViewById(R.id.textView_total_practical_new);
			holder.totalMarksProject 	= (TextView)convertView.findViewById(R.id.textView_total_project_new);
			holder.obtainedMarksTheory = (TextView)convertView.findViewById(R.id.textView_obtained_theory_new);
			holder.obtainedMarksPractical 	= (TextView)convertView.findViewById(R.id.textView_obtained_practical_new);
			holder.obtainedMarksProject 	= (TextView)convertView.findViewById(R.id.textView_obtained_project_new);
			
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

			
		Log.d("title", results.get(position).Type + "|" + results.get(position).Title);
		holder.subjectName.setText(results.get(position).header);
		holder.totalMarks.setText(results.get(position).Total_Marks);
		holder.obtainedMarks.setText(results.get(position).Marks);
		holder.totalMarksTheory.setText(results.get(position).Subject_Total_Marks);
		holder.obtainedMarksTheory.setText(results.get(position).Subject_Marks);
		holder.totalMarksPractical.setText(results.get(position).Practical_Total_Marks);
		holder.obtainedMarksPractical.setText(results.get(position).Practical_Marks);
		holder.totalMarksProject.setText(results.get(position).Project_Total_marks);
		holder.obtainedMarksProject.setText(results.get(position).Project_Marks);
		
		
		
		return convertView;
	}
	
	private class ViewHolder
	{
		TextView subjectName;
		TextView totalMarks;
		TextView obtainedMarks;
		TextView totalMarksTheory;
		TextView totalMarksPractical;
		TextView totalMarksProject;
		TextView obtainedMarksTheory;
		TextView obtainedMarksPractical;
		TextView obtainedMarksProject;
	}

}
*/