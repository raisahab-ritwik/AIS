package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.RequestsStatus;
import com.knwedu.comschoolapp.R;


public class CollegeRequestStatusAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<RequestsStatus> requestStatus;
	private String date;
	public CollegeRequestStatusAdapter(Context context, ArrayList<RequestsStatus> requests)
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
			convertView = inflater.inflate(R.layout.college_request_status_items, null);
			holder = new ViewHolder();
			holder.title 	= (TextView)convertView.findViewById(R.id.title_txt);
			holder.status 	= (TextView)convertView.findViewById(R.id.status_txt);
			holder.attachment = (ImageView)convertView.findViewById(R.id.img_attachment);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		CollegeAppUtils.setFont(context, holder.title);
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
		holder.status.setText(requestStatus.get(position).status);
		String test = requestStatus.get(position).attachment;
		if(!requestStatus.get(position).attachment.equals("")) {
			holder.attachment.setVisibility(View.VISIBLE);
		}
		else{
			holder.attachment.setVisibility(View.GONE);
		}

		/*
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
		}*/
		return convertView;
	}
	private class ViewHolder
	{
		TextView title, status;
		ImageView attachment;
	}
}