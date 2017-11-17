package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.LoadImageAsyncTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ParentChildrenAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<StudentProfileInfo> studentProfiles;
	private String date;

	// private ImageLoader imageTemp = ImageLoader.getInstance();
	// private DisplayImageOptions options;
	public ParentChildrenAdapter(Context context,
			ArrayList<StudentProfileInfo> studentProfiles) {
		this.context = context;
		this.studentProfiles = studentProfiles;
		/*
		 * options = new DisplayImageOptions.Builder()
		 * .showImageForEmptyUri(R.drawable.no_photo)
		 * .showImageOnFail(R.drawable.no_photo)
		 * .showStubImage(R.drawable.no_photo) .cacheInMemory() .cacheOnDisc()
		 * .build();
		 */
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
			convertView = inflater.inflate(R.layout.parent_child_items, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name_txt);
			holder.classs = (TextView) convertView.findViewById(R.id.class_txt);
			holder.roll = (TextView) convertView.findViewById(R.id.roll_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.image_view);
			holder.toggle = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(studentProfiles.get(position).fullname);
		holder.classs.setText(studentProfiles.get(position).class_section);
		holder.roll.setText(studentProfiles.get(position).class_roll);
		if (studentProfiles.get(position).user_id
				.equalsIgnoreCase(SchoolAppUtils.GetSharedParameter(context,
						Constants.CHILD_ID))) {
		//	convertView.setBackgroundResource(R.drawable.child_on);
			holder.toggle.setImageResource(R.drawable.on);

		}else{
			holder.toggle.setImageResource(R.drawable.off);
		}

		new LoadImageAsyncTask(context, holder.img, Urls.image_url_userpic,
				studentProfiles.get(position).image, false).execute();
		// imageTemp.displayImage(URLs.GetImage +
		// assignments.get(position).thumb.replace("..", ""), holder.img,
		// options);

		return convertView;
	}

	private class ViewHolder {
		ImageView img, toggle;
		TextView name, classs, roll;
	}
}