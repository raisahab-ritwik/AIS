package com.knwedu.ourschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.SuccessfulCandidate;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.ArrayList;

public class SuccessfulCandidateAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<SuccessfulCandidate> successfulCandidates;

	public SuccessfulCandidateAdapter(Context context, ArrayList<SuccessfulCandidate> successfulCandidates) {
		this.context = context;
		this.successfulCandidates = successfulCandidates;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.successfulCandidates != null) {
			return this.successfulCandidates.size();
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
			convertView = inflater.inflate(R.layout.career_successful_item_view, null);
			holder = new ViewHolder();
			holder.student_name = (TextView) convertView.findViewById(R.id.textViewTitle);
			holder.description = (TextView) convertView.findViewById(R.id.textViewDesc);
			holder.created_date = (TextView) convertView.findViewById(R.id.textViewInterviewTitle);
			holder.job_title = (TextView) convertView.findViewById(R.id.textViewInterviewDate);
			holder.interview_title = (TextView) convertView.findViewById(R.id.textViewSection);
			holder.section_name = (TextView) convertView.findViewById(R.id.textViewGiven);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.job_title);
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.section_name);
		SchoolAppUtils.setFont(context, holder.created_date);
		SchoolAppUtils.setFont(context, holder.student_name);
		SchoolAppUtils.setFont(context, holder.section_name);

		holder.job_title.setText(successfulCandidates.get(position).job_title);
		holder.description.setText(successfulCandidates.get(position).description);
		holder.interview_title.setText(successfulCandidates.get(position).interview_title);
		holder.section_name.setText(successfulCandidates.get(position).section_name);
		holder.student_name.setText(successfulCandidates.get(position).student_name);
		holder.created_date.setText(successfulCandidates.get(position).created_date);
		return convertView;
	}

	private class ViewHolder {
		TextView job_title, interview_title, description, created_date,section_name,student_name;
	}
}