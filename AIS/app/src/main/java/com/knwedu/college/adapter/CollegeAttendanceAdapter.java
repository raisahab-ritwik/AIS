package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StatusStudent;
import com.knwedu.comschoolapp.R;

public class CollegeAttendanceAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<StatusStudent> statuses;
	public int[] check;
	private String date;

	public CollegeAttendanceAdapter(Context context,
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

	public void setData(ArrayList<StatusStudent> data){
		this.statuses = data;

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
			convertView = inflater.inflate(R.layout.college_student_attendance_items,
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CollegeAppUtils.setFont(context, holder.dayNum);
		CollegeAppUtils.setFont(context, holder.day);
		CollegeAppUtils.setFont(context, holder.subject);
		CollegeAppUtils.setFont(context, holder.status);
		if (statuses.get(position).status.equals("1")) {
			holder.status.setText("Present");
		} else if (statuses.get(position).status.equals("0")) {
			holder.status.setText("Absent");
		} else if (statuses.get(position).status.equals("2")) {
			holder.status.setText("Late" + "  Reason : "
					+ statuses.get(position).late_reason);
		} else if (statuses.get(position).status.equals("3")) {
			holder.status.setText("Leave");
		}

		holder.dayNum
				.setText(CollegeAppUtils.getDay(statuses.get(position).date));
		String year = statuses.get(position).date.substring(0, 4);
		String month = statuses.get(position).date.substring(5, 7);
		String day = statuses.get(position).date.substring(8, 10);
		holder.subject.setText(statuses.get(position).lecture);
		holder.day.setText(Integer.parseInt(day) + "/"
				+ Integer.parseInt(month) + "/" + year);
		holder.lecture.setText(statuses.get(position).subject);
		return convertView;
	}

	private class ViewHolder {
		TextView dayNum, day, status, subject, lecture;
	}
}