package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Announcement;
import com.knwedu.comschoolapp.R;

public class CollegeAnnouncementAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Announcement> assignments;
	public int[] check;
	private String date;

	public CollegeAnnouncementAdapter(Context context,
			ArrayList<Announcement> assignments) {
		this.context = context;
		this.assignments = assignments;
		check = new int[this.assignments.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.assignments != null) {
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.college_announcement_item,
					null);
			holder = new ViewHolder();
			holder.dateDisplay = (LinearLayout) convertView
					.findViewById(R.id.title_layout);
			holder.day = (TextView) convertView.findViewById(R.id.day_txt);
			holder.dayNum = (TextView) convertView
					.findViewById(R.id.day_num_txt);
			holder.description = (TextView) convertView
					.findViewById(R.id.description_txt);
			holder.subject = (TextView) convertView
					.findViewById(R.id.subject_txt);
			holder.attachment = (ImageView) convertView
					.findViewById(R.id.img_attachment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (assignments.get(position).check) {
			holder.dateDisplay.setVisibility(View.VISIBLE);
			holder.dayNum.setText(CollegeAppUtils.getDay(assignments
					.get(position).date));
			String year = assignments.get(position).date.substring(0, 4);
			String month = assignments.get(position).date.substring(5, 7);
			String day = assignments.get(position).date.substring(8, 10);
			holder.day.setText(Integer.parseInt(day) + "/"
					+ Integer.parseInt(month) + "/" + year);
		} else {
			holder.dateDisplay.setVisibility(View.GONE);
		}
		CollegeAppUtils.setFont(context, holder.description);
		CollegeAppUtils.setFont(context, holder.subject);
		if (CollegeAppUtils.GetSharedParameter(context,
				CollegeConstants.USERTYPEID).equalsIgnoreCase(
				CollegeConstants.USERTYPE_TEACHER)) {
			holder.description.setText(assignments.get(position).sub_name);
		} else {
			if (assignments.get(position).type.equalsIgnoreCase("2")) {
				holder.description.setText(assignments.get(position).sub_name
						+ " Practical");
			} else if (assignments.get(position).type.equalsIgnoreCase("1")) {
				holder.description.setText(assignments.get(position).sub_name
						+ " Project");

			} else {
				holder.description.setText(assignments.get(position).sub_name);
			}
		}
		holder.subject.setText(assignments.get(position).title);
		if (CollegeAppUtils.isOnline(context)) {
			if (!assignments.get(position).file_name.equals("")
					&& !assignments.get(position).attachment.equals("")) {
				holder.attachment.setVisibility(View.VISIBLE);
			} else {
				holder.attachment.setVisibility(View.GONE);
			}
		} else {

			holder.attachment.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		LinearLayout dateDisplay;
		TextView description, subject, dayNum, day;
		ImageView attachment;
	}
}