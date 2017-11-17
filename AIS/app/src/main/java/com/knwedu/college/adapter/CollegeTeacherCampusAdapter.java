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
import com.knwedu.college.utils.CollegeDataStructureFramwork.Campus;
import com.knwedu.comschoolapp.R;

public class CollegeTeacherCampusAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Campus> campus;
	public int[] check;
	private String date;

	public CollegeTeacherCampusAdapter(Context context, ArrayList<Campus> campus) {
		this.context = context;
		this.campus = campus;
		check = new int[this.campus.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.campus != null) {
			return this.campus.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
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
		if (campus.get(position).check) {
			holder.dateDisplay.setVisibility(View.VISIBLE);
			holder.dayNum
					.setText(CollegeAppUtils.getDay(campus.get(position).created_at));
			String year = campus.get(position).created_at.substring(0, 4);
			String month = campus.get(position).created_at.substring(5, 7);
			String day = campus.get(position).created_at.substring(8, 10);
			holder.day.setText(Integer.parseInt(day) + "/"
					+ Integer.parseInt(month) + "/" + year);
		} else {
			holder.dateDisplay.setVisibility(View.GONE);
		}
		CollegeAppUtils.setFont(context, holder.description);
		CollegeAppUtils.setFont(context, holder.subject);

		holder.description.setText(campus.get(position).title);
		holder.subject.setText(campus.get(position).description);

		if (!campus.get(position).file_name.equals("")
				&& !campus.get(position).attachment.equals("")) {
			holder.attachment.setVisibility(View.VISIBLE);
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
