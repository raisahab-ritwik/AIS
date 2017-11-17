package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AssignmentActivity;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.WebViewActivity;
import com.knwedu.ourschool.utils.DataStructureFramwork.CampusActivity;
import com.knwedu.ourschool.utils.DataStructureFramwork.Event;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class EventDetailsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Event> events;
	private boolean isView;

	public EventDetailsAdapter(Context context, ArrayList<Event> events) {
		this.context = context;
		this.events = events;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.events != null) {
			return this.events.size();
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
			convertView = inflater.inflate(R.layout.details_screen, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.txtEventName);
			holder.startdate = (TextView) convertView.findViewById(R.id.txtStartDate);
			holder.enddate = (TextView) convertView.findViewById(R.id.txtEndDate);
			holder.desc = (TextView) convertView.findViewById(R.id.txtDesc);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.title);
		SchoolAppUtils.setFont(context, holder.startdate);
		SchoolAppUtils.setFont(context, holder.enddate);
		SchoolAppUtils.setFont(context, holder.desc);

		holder.title.setText(events.get(position).event_name);
		holder.startdate.setText(events.get(position).event_start_date);
		holder.enddate.setText(events.get(position).event_end_date);
		holder.desc.setText(events.get(position).event_description);


		return convertView;
	}

	private class ViewHolder {
		TextView title, startdate, desc, enddate;
	}
}