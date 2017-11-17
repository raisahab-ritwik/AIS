package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StudentProfileInfo;
import com.knwedu.comschoolapp.R;

public class CollegeParentChildrenAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<StudentProfileInfo> studentProfiles;
	private String date;
	private ProgressDialog dialog;
	StudentProfileInfo user;
	boolean swap = false;

	public CollegeParentChildrenAdapter(Context context,
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
			convertView = inflater.inflate(R.layout.college_parent_child_items,
					null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name_txt);
			holder.classs = (TextView) convertView.findViewById(R.id.class_txt);
			holder.roll = (TextView) convertView.findViewById(R.id.roll_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.img.setVisibility(View.GONE);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(studentProfiles.get(position).name);
		holder.classs.setText(studentProfiles.get(position).class_name);
		holder.roll.setText(studentProfiles.get(position).class_roll);


		convertView.setBackgroundResource(R.drawable.childoff);
		Log.d("session_student_id.................", CollegeAppUtils
				.GetSharedParameter(context, "session_student_id"));
		Log.d("id.................", studentProfiles.get(position).id);

		if (studentProfiles.get(position).id.equalsIgnoreCase(CollegeAppUtils
				.GetSharedParameter(context, "session_student_id"))) {
			convertView.setBackgroundResource(R.drawable.childon);

		}
		
		return convertView;
	}

	private class ViewHolder {
		ImageView img;
		TextView name, classs, roll;
	}

}