package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Examreportcard;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class ExamsReportAdapter extends BaseAdapter {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;

	private ArrayList<Examreportcard> examreportcard = new ArrayList<Examreportcard>();
	private LayoutInflater mInflater;
	Context context;

	public ExamsReportAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void addItem(Context context,
			ArrayList<Examreportcard> Examreportcard) {
		this.context = context;
		examreportcard = Examreportcard;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (this.examreportcard != null) {
			return this.examreportcard.size();
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

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		int rowType = getItemViewType(position);
		holder = new ViewHolder();
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.item_exams_report, null);

			holder.text_subject_name = (TextView) convertView
					.findViewById(R.id.txt_items_exam_subject_name);
			holder.text_room_no = (TextView) convertView
					.findViewById(R.id.txt_items_exam_room_no);
			holder.text_start_time = (TextView) convertView
					.findViewById(R.id.txt_items_exam_start_time);
			holder.btn_attch = (ImageView) convertView
					.findViewById(R.id.img_attachment);
			holder.text_subject_name
					.setText(examreportcard.get(position).report_card_name);
			holder.btn_attch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(context)
							.setTitle("Select option")
							.setPositiveButton("View Document",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// continue with view
											SchoolAppUtils.viewDocument(
													context,
													SchoolAppUtils
															.GetSharedParameter(
																	context,
																	Constants.COMMON_URL)
															+ ""
															+ examreportcard
																	.get(position).report_card_link
															+ ""
															+ examreportcard
																	.get(position).report_card_id
															+ "/"
															+ SchoolAppUtils
																	.GetSharedParameter(
																			context,
																			Constants.CHILD_ID)
															+ "/"
															+ SchoolAppUtils
																	.GetSharedParameter(
																			context,
																			Constants.UORGANIZATIONID));
										}
									})
							.setNegativeButton("Download",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// download
											
											final DownloadTask downloadTask = new DownloadTask(context, examreportcard
													.get(position).report_card_name + ".pdf");
											downloadTask.execute(SchoolAppUtils
													.GetSharedParameter(
															context,
															Constants.COMMON_URL)
													+ ""
													+ examreportcard
															.get(position).report_card_link
													+ ""
													+ examreportcard
															.get(position).report_card_id
													+ "/"
													+ SchoolAppUtils
															.GetSharedParameter(
																	context,
																	Constants.CHILD_ID)
													+ "/"
													+ SchoolAppUtils
															.GetSharedParameter(
																	context,
																	Constants.UORGANIZATIONID));
																					
											

											/*Intent i = new Intent(
													Intent.ACTION_VIEW);
											i.setData(Uri.parse(SchoolAppUtils
													.GetSharedParameter(
															context,
															Constants.COMMON_URL)
													+ ""
													+ examreportcard
															.get(position).report_card_link
													+ ""
													+ examreportcard
															.get(position).report_card_id
													+ "/"
													+ SchoolAppUtils
															.GetSharedParameter(
																	context,
																	Constants.CHILD_ID)
													+ "/"
													+ SchoolAppUtils
															.GetSharedParameter(
																	context,
																	Constants.UORGANIZATIONID)));
											context.startActivity(i);
*/
										}
									})
							.setIcon(android.R.drawable.ic_dialog_info).show();

					// TODO Auto-generated method stub

				}
			});
			SchoolAppUtils.setFont(context, holder.text_start_time);
			holder.text_start_time.setAutoLinkMask(Linkify.ALL);

			holder.text_start_time.setText(SchoolAppUtils.GetSharedParameter(
					context, Constants.COMMON_URL)
					+ ""
					+ examreportcard.get(position).report_card_link
					+ ""
					+ examreportcard.get(position).report_card_id
					+ "/"
					+ SchoolAppUtils.GetSharedParameter(context,
							Constants.CHILD_ID)
					+ "/"
					+ SchoolAppUtils.GetSharedParameter(context,
							Constants.UORGANIZATIONID));
			Log.d("Url",
					SchoolAppUtils.GetSharedParameter(context,
							Constants.COMMON_URL)
							+ ""
							+ examreportcard.get(position).report_card_link
							+ ""
							+ examreportcard.get(position).report_card_id
							+ "/"
							+ SchoolAppUtils.GetSharedParameter(context,
									Constants.CHILD_ID)
							+ "/"
							+ SchoolAppUtils.GetSharedParameter(context,
									Constants.UORGANIZATIONID));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
		public TextView text_date, text_subject_name, text_room_no,
				text_start_time, text_end_time, text_term, text_class_id;
		public ImageView btn_result, btn_attch;
	}

}