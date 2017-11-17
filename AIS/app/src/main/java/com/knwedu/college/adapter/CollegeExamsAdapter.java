package com.knwedu.college.adapter;
/*package com.knwedu.ourschool.adapter;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ExamList;
import com.knwedu.ourschool.utils.DataStructureFramwork.ExamSchedule;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class ExamsAdapter extends BaseAdapter {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;

	private ArrayList<ExamList> examlist = new ArrayList<ExamList>();
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
	private ArrayList<ExamSchedule> examschedule = new ArrayList<ExamSchedule>();

	private LayoutInflater mInflater;

	public ExamsAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void addItem(ArrayList<ExamSchedule> examSchedule) {
		examschedule = examSchedule;
		notifyDataSetChanged();
	}

	public void addSectionHeaderItem(ArrayList<ExamList> exams, int size) {
		examlist = exams;
		sectionHeader.add(size);
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		int size;
		if (examlist.size() > 0) {
			size = examlist.size();
		} else {
			size = examschedule.size();
		}
		return size;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		int rowType = getItemViewType(position);
		holder = new ViewHolder();
		if (convertView == null) {
			switch (rowType) {
			case TYPE_ITEM:
				convertView = mInflater.inflate(R.layout.item_exams, null);
				holder.text_date = (TextView) convertView
						.findViewById(R.id.txt_items_exam_dated);
				holder.text_subject_name = (TextView) convertView
						.findViewById(R.id.txt_items_exam_subject_name);
				holder.text_room_no = (TextView) convertView
						.findViewById(R.id.txt_items_exam_room_no);
				holder.text_start_time = (TextView) convertView
						.findViewById(R.id.txt_items_exam_start_time);
				holder.text_end_time = (TextView) convertView
						.findViewById(R.id.txt_items_exam_end_time);
				holder.text_date
						.setText(examschedule.get(position).schedule_date);
				holder.text_subject_name
						.setText(examschedule.get(position).sub_name);
				holder.text_room_no
						.setText(examschedule.get(position).facility_no);
				holder.text_start_time
						.setText(examschedule.get(position).start_time);
				holder.text_end_time
						.setText(examschedule.get(position).end_time);
				break;
			case TYPE_SEPARATOR:
				convertView = mInflater.inflate(R.layout.items_exams_header,
						null);
				holder.text_term = (TextView) convertView
						.findViewById(R.id.txt_header_exam_term);
				holder.text_class_id = (TextView) convertView
						.findViewById(R.id.txt_header_exam_class);
				holder.text_date = (TextView) convertView
						.findViewById(R.id.txt_header_exam_date);
				holder.text_date.setText(examlist.get(position).term_init_date);
				holder.text_term.setText(examlist.get(position).name_of_exam);
				holder.text_class_id.setText(examlist.get(position).class_name + " " + examlist.get(position).section_name);
				holder.btn_result = (ImageView) convertView
						.findViewById(R.id.btn_result);
				if (examlist.get(position).is_result_publish.equals("1")
						&& (SchoolAppUtils.GetSharedParameter(
								convertView.getContext(), Constants.USERTYPEID)
								.equals(Constants.USERTYPE_PARENT) || SchoolAppUtils
								.GetSharedParameter(convertView.getContext(),
										Constants.USERTYPEID).equals(
										Constants.USERTYPE_STUDENT))) {
					holder.btn_result.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub

							Intent intent = new Intent(arg0.getContext(),
									ExamResultsListActivity.class);
							intent.putExtra("EXAM_TERM_ID",
									examlist.get(position).exam_term_id);
							arg0.getContext().startActivity(intent);
						}
					});
					holder.btn_result.setVisibility(View.VISIBLE);
				} else {
					//holder.btn_result.setText("No Result");
					holder.btn_result.setClickable(false);
					holder.btn_result.setVisibility(View.INVISIBLE);
				}
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
		public TextView text_date, text_subject_name, text_room_no,
				text_start_time, text_end_time, text_term, text_class_id;
		public ImageView btn_result;
	}

	@Override
	public ExamList getItem(int position) {
		return examlist.get(position);

	}

}*/