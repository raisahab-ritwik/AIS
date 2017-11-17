package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;

public class ParentClassSectionAdapter  extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<StudentProfileInfo> studentProfiles;

	public ParentClassSectionAdapter(Context context,
			ArrayList<StudentProfileInfo> studentProfiles) {
		this.context = context;
		this.studentProfiles = studentProfiles;
	
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.studentProfiles != null) {
			return this.studentProfiles.size();
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
			convertView = inflater.inflate(R.layout.class_section_item, null);
			holder = new ViewHolder();
			holder.classs = (TextView) convertView.findViewById(R.id.class_txt);
			holder.section = (TextView) convertView.findViewById(R.id.section_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.classs.setText(studentProfiles.get(position).classs);
		holder.section.setText(studentProfiles.get(position).section);
		return convertView;
	}

	private class ViewHolder {
		TextView classs, section;
	}
}