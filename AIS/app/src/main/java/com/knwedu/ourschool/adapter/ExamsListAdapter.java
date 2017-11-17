package com.knwedu.ourschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.DataStructureFramwork.ExamSchedule;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.ArrayList;

public class ExamsListAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<ExamSchedule> examschedule = new ArrayList<DataStructureFramwork.ExamSchedule>();
	private boolean isView;

	public ExamsListAdapter(Context context, ArrayList<ExamSchedule> examschedule) {
		this.context = context;
		this.examschedule = examschedule;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.examschedule != null) {
			return this.examschedule.size();
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
			convertView = inflater.inflate(R.layout.item_exams, null);
			holder = new ViewHolder();

			holder.text_date = (TextView) convertView
					.findViewById(R.id.txt_items_exam_dated);
			holder.text_subject_name = (TextView) convertView
					.findViewById(R.id.txt_items_exam_subject_name);
			holder.text_room_no = (TextView) convertView
					.findViewById(R.id.txt_items_exam_room_no);
			holder.text_start_time = (TextView) convertView
					.findViewById(R.id.txt_items_exam_start_time);
			holder.text_end_time = (TextView) convertView
					.findViewById(R.id.txt_items_exam_end_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.text_date);
		SchoolAppUtils.setFont(context, holder.text_subject_name);
		SchoolAppUtils.setFont(context, holder.text_room_no);
		SchoolAppUtils.setFont(context, holder.text_start_time);
		SchoolAppUtils.setFont(context, holder.text_end_time);

		holder.text_date
				.setText(examschedule.get(position).schedule_date);
		holder.text_subject_name
				.setText(examschedule.get(position).sub_name);
		holder.text_room_no
				.setText(examschedule.get(position).facility_no);
		holder.text_start_time
				.setText(examschedule.get(position).start_time);
		holder.text_end_time
				.setText(examschedule.get(position).end_time);

		return convertView;
	}

	class ViewHolder {
		public TextView text_date, text_subject_name, text_room_no,
				text_start_time, text_end_time;
	}
}