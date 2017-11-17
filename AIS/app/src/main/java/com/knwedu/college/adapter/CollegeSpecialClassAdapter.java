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
import com.knwedu.college.utils.CollegeDataStructureFramwork.SpecialClass;
import com.knwedu.comschoolapp.R;

public class CollegeSpecialClassAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	public int[] check;
	ArrayList<SpecialClass> specialClass;

	public CollegeSpecialClassAdapter(Context context,
			ArrayList<SpecialClass> specialClass) {
		this.context = context;
		this.specialClass = specialClass;
		check = new int[this.specialClass.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return specialClass.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.college_special_class_items, null);
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
		/*if (specialClass.get(position).check) {
			holder.dateDisplay.setVisibility(View.VISIBLE);
			holder.dayNum.setText(CollegeAppUtils.getDay(specialClass
					.get(position).submission_date));
			String year = specialClass.get(position).submission_date.substring(
					0, 4);
			String month = specialClass.get(position).submission_date
					.substring(5, 7);
			String day = specialClass.get(position).submission_date.substring(
					8, 10);
			holder.day.setText(Integer.parseInt(day) + "/"
					+ Integer.parseInt(month) + "/" + year);
		} else {
			holder.dateDisplay.setVisibility(View.GONE);
		}
		*/
		holder.day.setVisibility(View.GONE);
		holder.dayNum.setText(specialClass.get(position).sdate);
		CollegeAppUtils.setFont(context, holder.description);
		CollegeAppUtils.setFont(context, holder.subject);
		holder.description.setText(specialClass.get(position).term);
		holder.subject.setText(specialClass.get(position).title);

		if(specialClass.get(position).attachment.equals("")){
			holder.attachment.setVisibility(View.GONE);
		}


		return convertView;
	}

	private class ViewHolder {
		LinearLayout dateDisplay;
		TextView description, subject, dayNum, day, teacherName;
		ImageView marks, attachment;
	}
}
