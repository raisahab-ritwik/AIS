package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.RequestsStatus;
import com.knwedu.ourschool.utils.SchoolAppUtils;


public class RequestStatusAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<RequestsStatus> requestStatus;
	private String date;
	public RequestStatusAdapter(Context context, ArrayList<RequestsStatus> requests)
	{
		this.context = context;
		this.requestStatus = requests;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.requestStatus != null)
		{
			return this.requestStatus.size();
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
			convertView = inflater.inflate(R.layout.request_status_items, null);
			holder = new ViewHolder();
			holder.title 	= (TextView)convertView.findViewById(R.id.title_txt);
			holder.status 	= (TextView)convertView.findViewById(R.id.status_txt);
			holder.name = (TextView)convertView.findViewById(R.id.name_txt);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.title);
		boolean check = false;
/*		if(requestStatus.get(position). != null)
		{
			if(!requestStatus.get(position).tittle.equalsIgnoreCase("null"))
			{
				holder.title.setText(requestStatus.get(position).tittle);
				check = true;
			}
		}
		if(!check)
		{
			if(requestStatus.get(position).requesttype.equalsIgnoreCase("1"))
			{
				holder.title.setText("Card");
			}
			else if(requestStatus.get(position).requesttype.equalsIgnoreCase("2"))
			{
				holder.title.setText("Uniform");
			}
			else if(requestStatus.get(position).requesttype.equalsIgnoreCase("3"))
			{
				holder.title.setText("Books");
			}
			else if(requestStatus.get(position).requesttype.equalsIgnoreCase("4"))
			{
				holder.title.setText("Special");
			}
		}
		
*/		
		
		holder.title.setText(requestStatus.get(position).reason_title);

		holder.name.setText(requestStatus.get(position).fname + " " + requestStatus.get(position).lname);
		
		
		if(requestStatus.get(position).status.equalsIgnoreCase("0"))
		{
			holder.status.setText(R.string.pending);
			holder.status.setTextColor(Color.YELLOW);
		}
		else if(requestStatus.get(position).status.equalsIgnoreCase("1"))
		{
			holder.status.setText(R.string.approved);
			holder.status.setTextColor(Color.GREEN);
		}
		else if(requestStatus.get(position).status.equalsIgnoreCase("2"))
		{
			holder.status.setText(R.string.rejected);
			holder.status.setTextColor(Color.RED);
		}
		return convertView;
	}
	private class ViewHolder
	{
		TextView title, status, name;
	}
}