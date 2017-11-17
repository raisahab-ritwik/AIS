package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.Directory;
import com.knwedu.ourschool.utils.SchoolAppUtils;


public class DirectoryAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Directory> fellow;
	private String date;
	public DirectoryAdapter(Context context, ArrayList<Directory> fellow)
	{
		this.context = context;
		this.fellow = fellow;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.fellow != null)
		{
			return this.fellow.size();
		}
		return 0;
	}

	public void setData(ArrayList<Directory> data){
		this.fellow = data;

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
			convertView = inflater.inflate(R.layout.directory_items, null);
			holder = new ViewHolder();
			holder.name 	= (TextView)convertView.findViewById(R.id.name_txt);
			holder.designation 	= (TextView)convertView.findViewById(R.id.designation_txt);
			holder.address 	= (TextView)convertView.findViewById(R.id.address_txt);
			holder.phone 	= (TextView)convertView.findViewById(R.id.phone_txt);
			holder.email 	= (TextView)convertView.findViewById(R.id.email_txt);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.name);
		SchoolAppUtils.setFont(context, holder.address);
		SchoolAppUtils.setFont(context, holder.designation);
		SchoolAppUtils.setFont(context, holder.email);
		SchoolAppUtils.setFont(context, holder.phone);
		
		holder.name.setText(fellow.get(position).name);
		holder.address.setText(fellow.get(position).address);
		holder.designation.setText(fellow.get(position).designition);
		holder.email.setText(fellow.get(position).email);
		holder.phone.setText(fellow.get(position).phone1);
		return convertView;
	}
	private class ViewHolder
	{
		TextView name, designation, address, phone, email;
	}
}