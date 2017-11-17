package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.TeacherSchedule;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class SubjectScheduleAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<TeacherSchedule> weekly;
	private ArrayList<TeacherSchedule> daily;

	private LinearLayout linearLayout;

	public SubjectScheduleAdapter(Context context,
			ArrayList<TeacherSchedule> weekly, ArrayList<TeacherSchedule> daily) {
		this.context = context;
		this.weekly = weekly;
		this.daily = daily;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.weekly != null) {
			return this.weekly.size();
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
			convertView = inflater.inflate(R.layout.subject_schedule_items,
					null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.subject_txt);
			holder.marks = (TextView) convertView.findViewById(R.id.date_txt);
			holder.daily = (ImageButton) convertView
					.findViewById(R.id.button_daily);
			holder.getdoc = (ImageButton) convertView
					.findViewById(R.id.download_btn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.name);
		SchoolAppUtils.setFont(context, holder.marks);
		holder.name.setText(weekly.get(position).topic);
		/*
		 * holder.marks
		 * .setText(SchoolAppUtils.getDateFormat(weekly.get(position).date)
		 * .replace("-", "/") + " - " +
		 * SchoolAppUtils.getDateFormat(SchoolAppUtils
		 * .getLastDayofWeek(weekly.get(position).date, 6)));
		 */
		holder.marks.setText(weekly.get(position).start_date + " - "
				+ weekly.get(position).end_date);

		holder.daily.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						new ContextThemeWrapper(context, R.style.AlertDialogCustom));

				// set title
				alertDialogBuilder.setTitle("Daily Topic!");

				// set dialog message

				alertDialogBuilder.setCancelable(true);

				View view = inflater.inflate(R.layout.dialog_student_schedule,
						null);
				linearLayout = (LinearLayout) view
						.findViewById(R.id.layout_schedule_row);

				View rowView;
				ViewHolderSchedule viewHolder;

				try {
					linearLayout.removeAllViews();
				} catch (Exception ex) {
				}

				for (int i = daily.size() / weekly.size() * position; i < daily
						.size() / weekly.size() * (position + 1); i++) {
					rowView = inflater.inflate(
							R.layout.subject_schedule_each_row, null);
					viewHolder = new ViewHolderSchedule();
					viewHolder.topic = (TextView) rowView
							.findViewById(R.id.textView_topic);
					viewHolder.date = (TextView) rowView
							.findViewById(R.id.textView_date);
					viewHolder.edit = (ImageButton) rowView
							.findViewById(R.id.button_edit);
					viewHolder.getdoc = (ImageButton) rowView
							.findViewById(R.id.download_btn);
					if (daily.get(i).doc.length() > 0) {
						viewHolder.getdoc.setVisibility(View.VISIBLE);

						final String file_id = daily.get(i).id;
						viewHolder.getdoc.setVisibility(View.VISIBLE);
						viewHolder.getdoc
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										intent.setData(Uri
												.parse(Urls.api_get_schedule_doc
														+ "/2/" + file_id));
										context.startActivity(intent);
									}
								});
					} else {
						viewHolder.getdoc.setVisibility(View.GONE);
					}

					viewHolder.edit.setVisibility(View.GONE);

					viewHolder.topic.setText("Topic: " + daily.get(i).topic);
					viewHolder.date.setText("Date: "
							+ SchoolAppUtils.getDateFormat(daily.get(i).date));

					linearLayout.addView(rowView);

				}

				alertDialogBuilder.setView(view);
				alertDialogBuilder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				Button button = alertDialog
						.getButton(DialogInterface.BUTTON_NEGATIVE);
				button.setBackgroundColor(Color.BLUE);

			}
		});

		if (weekly.get(position).doc.length() > 0) {
			holder.getdoc.setVisibility(View.VISIBLE);
			holder.getdoc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(Urls.api_get_schedule_doc + "/1/"
							+ weekly.get(position).id));
					context.startActivity(i);
				}
			});
		} else {
			holder.getdoc.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView name, marks;
		ImageButton daily, getdoc;
	}

	private class ViewHolderSchedule {
		TextView topic, date;
		ImageButton edit, getdoc;
	}
}