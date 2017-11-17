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
import com.knwedu.college.utils.CollegeDataStructureFramwork.Feedback;
import com.knwedu.comschoolapp.R;

public class CollegeFeedbackAdapter extends BaseAdapter {

	Context context;
	ArrayList<Feedback> feedbcak;
	ViewHolder holder;
	public int[] check;
	private LayoutInflater inflater;

	public CollegeFeedbackAdapter(Context context, ArrayList<Feedback> feedbcak) {

		this.context = context;
		this.feedbcak = feedbcak;
		check = new int[this.feedbcak.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feedbcak.size();
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
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.college_custom_feedback,
					null);
			holder = new ViewHolder();
			holder.dateDisplay = (LinearLayout) convertView
					.findViewById(R.id.title_layout);
			holder.day = (TextView) convertView.findViewById(R.id.day_txt);
			holder.dayNum = (TextView) convertView
					.findViewById(R.id.day_num_txt);
			holder.title = (TextView) convertView.findViewById(R.id.title_txt);
			holder.subject = (TextView) convertView
					.findViewById(R.id.subject_txt);
			holder.publish = (ImageView) convertView
					.findViewById(R.id.img_publish);
			holder.mark = (ImageView) convertView.findViewById(R.id.img_mark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (feedbcak.get(position).check) {
			holder.dateDisplay.setVisibility(View.VISIBLE);
			holder.dayNum.setText(CollegeAppUtils.getDay(feedbcak
					.get(position).date));
			String year = feedbcak.get(position).date.substring(0, 4);
			String month = feedbcak.get(position).date
					.substring(5, 7);
			String day = feedbcak.get(position).date.substring(8, 10);
			holder.day.setText(Integer.parseInt(day) + "/"
					+ Integer.parseInt(month) + "/" + year);

		} else {
			holder.dateDisplay.setVisibility(View.GONE);
		}
		CollegeAppUtils.setFont(context, holder.title);
		CollegeAppUtils.setFont(context, holder.subject);
		holder.title.setText(feedbcak.get(position).teacher);
		holder.subject.setText(feedbcak.get(position).description);
		return convertView;
	}
	private class ViewHolder {
		LinearLayout dateDisplay;
		TextView title, subject, dayNum, day;
		ImageView publish, mark;
	}

}
