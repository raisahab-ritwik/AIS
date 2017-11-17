package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AnnouncementActivity;
import com.knwedu.ourschool.ClassWorkActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Announcement;
import com.knwedu.ourschool.utils.SchoolAppUtils;


public class AnnouncementAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Announcement> assignments;
	public int []check;
	int type;
	private String date,title;
	public AnnouncementAdapter(Context context, ArrayList<Announcement> assignments,int type, String title)
	{
		this.context = context;
		this.assignments = assignments;
		check = new int[this.assignments.size()];
		this.type = type;
		this.title = title;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.assignments != null)
		{
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
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.announcement_item, null);
			holder = new ViewHolder();
			holder.dateDisplay = (LinearLayout) convertView.findViewById(R.id.title_layout);
			holder.mnLayout = (RelativeLayout) convertView.findViewById(R.id.main_layout);
			holder.day 	= (TextView)convertView.findViewById(R.id.day_txt);
			holder.dayNum 	= (TextView)convertView.findViewById(R.id.day_num_txt);
			holder.description 	= (TextView)convertView.findViewById(R.id.description_txt);
			holder.subject 	= (TextView)convertView.findViewById(R.id.subject_txt);
			holder.attachment = (ImageView) convertView
					.findViewById(R.id.img_attachment);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if(assignments.get(position).check)
		{
			holder.dateDisplay.setVisibility(View.VISIBLE);
			holder.dayNum.setText(SchoolAppUtils.getDay(assignments.get(position).date));
			String year = assignments.get(position).date.substring(0, 4);
			String month = assignments.get(position).date.substring(5, 7);
			String day = assignments.get(position).date.substring(8, 10);
			holder.day.setText(Integer.parseInt(day) + "/" + Integer.parseInt(month) + "/" + year);
		}
		else
		{
			holder.dateDisplay.setVisibility(View.GONE);
		}
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.subject);
		
		holder.description.setText(assignments.get(position).sub_name);
		holder.subject.setText(assignments.get(position).title);
		
		if (!assignments.get(position).file_name.equals("null")
				&& !assignments.get(position).attachment.equals("null")) {
			holder.attachment.setVisibility(View.VISIBLE);
		} else {

			holder.attachment.setVisibility(View.GONE);
		}

		holder.mnLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (type == 2) {
					Intent intent = new Intent(context,
							AnnouncementActivity.class);
					intent.putExtra(Constants.ANNOUNCEMENT,
							assignments.get(position).object.toString());
					intent.putExtra(Constants.PAGE_TITLE, title);
					context.startActivity(intent);
				} else {
					Intent intent = new Intent(context,
							ClassWorkActivity.class);
					intent.putExtra(Constants.ANNOUNCEMENT,
							assignments.get(position).object.toString());
					intent.putExtra(Constants.PAGE_TITLE, title);
					context.startActivity(intent);
				}

			}
		});
		return convertView;
	}
	private class ViewHolder
	{
		LinearLayout dateDisplay;
		TextView description, subject, dayNum, day;
		ImageView attachment;
		RelativeLayout mnLayout;
	}
}