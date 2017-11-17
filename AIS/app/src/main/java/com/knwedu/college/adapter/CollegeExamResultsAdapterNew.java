package com.knwedu.college.adapter;
/*package com.knwedu.ourschool.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.ourschool.utils.DataStructureFramwork.MarksConsolidated;


public class ExamResultsAdapterNew extends BaseAdapter{

	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<MarksConsolidated> marks;
	int totalMarks;
	int marksobtained;
		
	public ExamResultsAdapterNew(Context context, ArrayList<MarksConsolidated> fellow)
	{
		this.context = context;
		this.marks = fellow;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.marks != null)
		{
			return this.marks.size();
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
			//holder.totalMarks = (TextView)convertView.findViewById(R.id.textView_total_new);
		   // holder.obtainedMarks 	= (TextView)convertView.findViewById(R.id.textView_obtained_new);
			holder.totalMarksTheory = (TextView)convertView.findViewById(R.id.textView_total_theory_new);
			holder.totalMarksPractical 	= (TextView)convertView.findViewById(R.id.textView_total_practical_new);
			holder.totalMarksProject 	= (TextView)convertView.findViewById(R.id.textView_total_project_new);
			holder.obtainedMarksTheory = (TextView)convertView.findViewById(R.id.textView_obtained_theory_new);
			holder.obtainedMarksPractical 	= (TextView)convertView.findViewById(R.id.textView_obtained_practical_new);
			holder.obtainedMarksProject 	= (TextView)convertView.findViewById(R.id.textView_obtained_project_new);
			holder.practicalHeader 	= (TextView)convertView.findViewById(R.id.textView_practical_header_new);
			holder.theoryHeader 	= (TextView)convertView.findViewById(R.id.textView_theory_header_new);
			holder.projectHeader 	= (TextView)convertView.findViewById(R.id.textView_project_header_new);
			
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.subjectName.setText(marks.get(position).sub_name);
		//holder.totalMarks.setText("Total: " + totalMarks);
		//holder.obtainedMarks.setText("Obtained: "+ marksobtained);
		//holder.theoryHeader.setText(marks.get(position).sub_name);
		holder.totalMarksTheory.setText(marks.get(position).Out_of_marks);
		holder.obtainedMarksTheory.setText(marks.get(position).Marks);
		holder.practicalHeader.setText("");
		holder.totalMarksPractical.setText("");
		holder.obtainedMarksPractical.setText("");
		holder.projectHeader.setText("");
		holder.totalMarksProject.setText("");
		holder.obtainedMarksProject.setText("");

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
		TextView practicalHeader;
		TextView theoryHeader;
		TextView projectHeader;
	}

}
*/