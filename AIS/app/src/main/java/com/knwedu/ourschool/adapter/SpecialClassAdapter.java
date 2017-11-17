package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.SpecialClass;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class SpecialClassAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	ArrayList<SpecialClass> specialClass;

	public SpecialClassAdapter(Context context,
			ArrayList<SpecialClass> specialClass) {
		this.context = context;
		this.specialClass = specialClass;
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
			convertView = inflater.inflate(R.layout.special_class_item, null);
			holder = new ViewHolder();

			holder.class_section = (TextView) convertView
					.findViewById(R.id.class_section_txt);
			holder.title = (TextView) convertView
					.findViewById(R.id.title_txt);
			holder.description = (TextView) convertView
					.findViewById(R.id.description_txt);
			holder.name = (TextView) convertView
					.findViewById(R.id.name_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.class_section.setText((specialClass.get(position).classs + " " + specialClass.get(position).section_name));
		holder.title.setText(specialClass.get(position).title);
		holder.description.setText(specialClass.get(position).description);
		holder.name.setText(specialClass.get(position).fname + " " + specialClass.get(position).lname);
		
		if(SchoolAppUtils.GetSharedParameter(context, Constants.USERTYPEID).equals(Constants.USERTYPE_TEACHER)){
			holder.name.setVisibility(View.GONE);
		}else{
			holder.class_section.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView class_section, title, description, name;
	}
}
