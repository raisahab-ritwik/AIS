package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.StatusStudent;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class AttendanceAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<StatusStudent> statuses;
	public int[] check;
	private String date;

	public AttendanceAdapter(Context context,
			ArrayList<StatusStudent> assignments) {
		this.context = context;
		this.statuses = assignments;
		check = new int[this.statuses.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.statuses != null) {
			return this.statuses.size();
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

	public void setData(ArrayList<StatusStudent> data){
		this.statuses = data;

	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.student_attendance_items,
					null);
			holder = new ViewHolder();
			holder.day = (TextView) convertView.findViewById(R.id.day_txt);
			holder.subject = (TextView) convertView
					.findViewById(R.id.txt_subject_name);
			holder.dayNum = (TextView) convertView
					.findViewById(R.id.day_num_txt);
			holder.lecture = (TextView) convertView
					.findViewById(R.id.subject_name);

			holder.status = (TextView) convertView
					.findViewById(R.id.status_txt);
			holder.subject_txt = (TextView) convertView
					.findViewById(R.id.txt_subject);
			holder.lecture_txt = (TextView) convertView
					.findViewById(R.id.subject);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SchoolAppUtils.setFont(context, holder.dayNum);
		SchoolAppUtils.setFont(context, holder.day);
		SchoolAppUtils.setFont(context, holder.subject);
		SchoolAppUtils.setFont(context, holder.status);
		//SchoolAppUtils.setFont(context, holder.status);

		if (statuses.get(position).status.equals("1")) {
			holder.status.setText("Present");
		} else if (statuses.get(position).status.equals("2")) {
			holder.status.setText("Absent");
		} else if (statuses.get(position).status.equals("3")) {
			holder.status.setText("Late" + "  Reason : "
					+ statuses.get(position).late_reason);
		} else if (statuses.get(position).status.equals("4")) {
			holder.status.setText("Leave");
		}
//			//-------------extra feature add----------
//		} else if (statuses.get(position).status.equals("5")) {
//		holder.status.setText("Early_Depature");
//	}

		holder.dayNum
				.setText(SchoolAppUtils.getDay(statuses.get(position).date));
		String year = statuses.get(position).date.substring(0, 4);
		String month = statuses.get(position).date.substring(5, 7);
		String day = statuses.get(position).date.substring(8, 10);
		holder.subject.setText(statuses.get(position).lecture);
		holder.day.setText(Integer.parseInt(day) + "/"
				+ Integer.parseInt(month) + "/" + year);
		holder.lecture.setText(statuses.get(position).subject);
		if(statuses.get(position).subject.equalsIgnoreCase("null"))
		{
			holder.subject.setVisibility(View.GONE);
			holder.lecture.setVisibility(View.GONE);
			holder.subject_txt.setVisibility(View.GONE);
			holder.lecture_txt.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView dayNum, day, status, subject, lecture, subject_txt, lecture_txt;
	}
}