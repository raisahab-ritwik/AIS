package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.MarksConsolidated;

public class ExamResultsAdapterNew extends BaseAdapter {

	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<MarksConsolidated> marks;
	int totalMarks;
	int marksobtained;

	public ExamResultsAdapterNew(Context context,
			ArrayList<MarksConsolidated> fellow) {
		this.context = context;
		this.marks = fellow;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.marks != null) {
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.exam_result_item_new, null);
			holder = new ViewHolder();
			holder.groupName = (TextView) convertView
					.findViewById(R.id.textView_subject_name_new);

			holder.subjectName = (TextView) convertView
					.findViewById(R.id.text_subject);
			// holder.subjectName =
			// (TextView)convertView.findViewById(R.id.textView_subject_name_new);
			holder.totalMarks = (TextView) convertView
					.findViewById(R.id.text_total_marks);
			holder.obtainedMarks = (TextView) convertView
					.findViewById(R.id.text_marks_obtained);

			holder.totalgrade = (TextView) convertView
					.findViewById(R.id.text_grade);
			holder.totalremarks = (TextView) convertView
					.findViewById(R.id.text_remarks);
			holder.tab2 = (TableRow) convertView.findViewById(R.id.tableRow2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (marks.get(position).check) {
			holder.groupName.setVisibility(View.VISIBLE);
			if (marks.get(position).group_name.equalsIgnoreCase("NA")) {
				holder.tab2.setVisibility(View.GONE);
				holder.groupName.setVisibility(View.GONE);
			}
		} else {

			holder.tab2.setVisibility(View.GONE);
			holder.groupName.setVisibility(View.GONE);

		}
		holder.groupName.setText(marks.get(position).subject_group);
		holder.subjectName.setText(marks.get(position).sub_name);
		holder.totalMarks.setText(marks.get(position).Out_of_marks);
		holder.obtainedMarks.setText(marks.get(position).Marks);
		holder.totalgrade.setText(marks.get(position).grade);
		holder.totalremarks.setText(marks.get(position).remarks);
		return convertView;
	}

	private class ViewHolder {
		TextView subjectName;
		TextView totalMarks;
		TextView obtainedMarks;
		TextView groupName;
		TextView totalMarksTheory;
		TextView totalgrade;
		TextView totalremarks;
		TableRow tab1, tab2;
	}

}
