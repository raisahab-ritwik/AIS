package com.knwedu.ourschool.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.SubjectList;
import com.knwedu.college.utils.CollegeDataStructureFramwork.SyllabusSubject;
import com.knwedu.comschoolapp.R;

public class SchoolSyllabusAdapter extends BaseExpandableListAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<String> terms;
	HashMap<String,ArrayList<String>> hashmapVal;
	
	public SchoolSyllabusAdapter(Context context, ArrayList<String> terms,HashMap<String,ArrayList<String>> hashmapVal) {
		this.context = context;
		this.terms = terms;
		this.hashmapVal = hashmapVal;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		TextView description;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return hashmapVal.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.hashmapVal.get(this.terms.get(groupPosition))
                .size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		 return this.terms.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		//System.out.println();
		return this.hashmapVal.get(this.terms.get(groupPosition))
                .get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			   LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   convertView = inf.inflate(R.layout.group_heading, null);
			  }
		
		TextView heading = (TextView) convertView.findViewById(R.id.heading);
		heading.setText(terms.get(groupPosition));
		  
		  return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		final String expandedListText = (String) getChild(groupPosition, childPosition);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.college_syllabus_list_item, null);
			holder = new ViewHolder();
			holder.description = (TextView) convertView
					.findViewById(R.id.description_txt);
				holder.description.setText(expandedListText);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
