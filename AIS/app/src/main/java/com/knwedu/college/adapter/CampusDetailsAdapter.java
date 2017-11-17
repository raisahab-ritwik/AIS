package com.knwedu.college.adapter;

import java.util.ArrayList;

import com.knwedu.comschoolapp.R;
import com.knwedu.college.utils.CollegeDataStructureFramwork.CampusActivity;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CampusDetailsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<CampusActivity> campusActivities;
	private boolean isView;

	public CampusDetailsAdapter(Context context, ArrayList<CampusActivity> campusActivities) {
		this.context = context;
		this.campusActivities = campusActivities;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.campusActivities != null) {
			return this.campusActivities.size();
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
			convertView = inflater.inflate(R.layout.campus_activity_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.txtPaymentPeriod);
			holder.description = (TextView) convertView.findViewById(R.id.txtPaymentDate);
			holder.created_by = (TextView) convertView.findViewById(R.id.txtPaymentAmount);
			holder.created_on = (TextView) convertView.findViewById(R.id.txtCreatedOn);
			holder.attachment = (ImageView) convertView.findViewById(R.id.download_btn);
			if (!campusActivities.get(position).file_name.equalsIgnoreCase("null")) {
				holder.attachment.setVisibility(View.VISIBLE);
			} else {
				holder.attachment.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.title);
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.created_by);
		SchoolAppUtils.setFont(context, holder.created_on);

		holder.title.setText(campusActivities.get(position).title);
		holder.description.setText(campusActivities.get(position).description);
		holder.created_by.setText(campusActivities.get(position).created_by);
		holder.created_on.setText(campusActivities.get(position).created_date);
		return convertView;
	}

	private class ViewHolder {
		TextView title, description, created_by, created_on;
		ImageView attachment;
	}
}