package com.knwedu.ourschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.Interview;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.ArrayList;

public class InterviewAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Interview> interviews;

	public InterviewAdapter(Context context, ArrayList<Interview> interviews) {
		this.context = context;
		this.interviews = interviews;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.interviews != null) {
			return this.interviews.size();
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
			convertView = inflater.inflate(R.layout.career_interview_item_view, null);
			holder = new ViewHolder();
			holder.job_title = (TextView) convertView.findViewById(R.id.textViewTitle);
			holder.description = (TextView) convertView.findViewById(R.id.textViewDesc);
			holder.interview_title = (TextView) convertView.findViewById(R.id.textViewInterviewTitle);
			holder.submit_date = (TextView) convertView.findViewById(R.id.textViewInterviewDate);
			holder.section_name = (TextView) convertView.findViewById(R.id.textViewSection);
			holder.created_by = (TextView) convertView.findViewById(R.id.textViewGiven);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.job_title);
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.created_by);
		SchoolAppUtils.setFont(context, holder.submit_date);
		SchoolAppUtils.setFont(context, holder.created_by);
		SchoolAppUtils.setFont(context, holder.section_name);

		holder.job_title.setText(interviews.get(position).job_title);
		holder.description.setText(interviews.get(position).description);
		holder.created_by.setText(interviews.get(position).created_by);
		holder.interview_title.setText(interviews.get(position).interview_title);
		holder.submit_date.setText(interviews.get(position).submit_date);
		holder.section_name.setText(interviews.get(position).section_name);
		return convertView;
	}

	private class ViewHolder {
		TextView job_title, interview_title, description, submit_date,section_name,created_by;
	}
}