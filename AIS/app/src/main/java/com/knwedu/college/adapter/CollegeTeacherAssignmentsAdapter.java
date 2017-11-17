package com.knwedu.college.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.college.CollegeTeacherAssignmentListActivity;
import com.knwedu.college.CollegeTeacherAssignmentResultsActivity;
import com.knwedu.college.CollegeTeacherTestListActivity;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Assignment;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Subject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;

public class CollegeTeacherAssignmentsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Assignment> assignments;
	public int[] check;
	private String date;
	private Subject subject;
	AlertDialog.Builder dialoggg;
	private ProgressDialog dialog;
	private int type_variable_assignment;
	private String type_permission_mark, page_title;
	private boolean isOnline;
	private String permission_mark, permission_publish;

	// private boolean internetAvailable = false;

	public CollegeTeacherAssignmentsAdapter(Context context,
			ArrayList<Assignment> assignments, Subject subject,
			int type_variable_assignment, boolean isOnline,
			String type_permission_mark, String page) {
		this.context = context;
		this.assignments = assignments;
		this.subject = subject;
		this.type_variable_assignment = type_variable_assignment;
		this.isOnline = isOnline;
		this.page_title = page;
		this.type_permission_mark = type_permission_mark;
		check = new int[this.assignments.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.assignments != null) {
			return this.assignments.size();
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
			convertView = inflater.inflate(
					R.layout.college_teacher_assignment_items, null);
			holder = new ViewHolder();
			holder.dateDisplay = (LinearLayout) convertView
					.findViewById(R.id.title_layout);
			holder.day = (TextView) convertView.findViewById(R.id.day_txt);
			holder.dayNum = (TextView) convertView
					.findViewById(R.id.day_num_txt);
			holder.title = (TextView) convertView.findViewById(R.id.title_txt);
			holder.subject = (TextView) convertView
					.findViewById(R.id.subject_txt);
			holder.publish = (ImageView) convertView
					.findViewById(R.id.img_publish);
			holder.mark = (ImageView) convertView.findViewById(R.id.img_mark);
			holder.attachment = (ImageView) convertView
					.findViewById(R.id.img_attachment);
			holder.tick = (ImageView) convertView.findViewById(R.id.img_tick);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (assignments.get(position).check) {
			holder.dateDisplay.setVisibility(View.VISIBLE);
			holder.dayNum.setText(CollegeAppUtils.getDay(assignments
					.get(position).submit_date));
			String year = assignments.get(position).submit_date.substring(0, 4);
			String month = assignments.get(position).submit_date
					.substring(5, 7);
			String day = assignments.get(position).submit_date.substring(8, 10);
			holder.day.setText(Integer.parseInt(day) + "/"
					+ Integer.parseInt(month) + "/" + year);

		} else {
			holder.dateDisplay.setVisibility(View.GONE);
		}
		CollegeAppUtils.setFont(context, holder.title);
		CollegeAppUtils.setFont(context, holder.subject);
		
		if (isOnline) {
			if (CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.COLLEGE_ASSIGNMENT_MARK).equals("1")) {
				holder.publish.setVisibility(View.GONE);
				holder.mark.setVisibility(View.VISIBLE);
				holder.tick.setVisibility(View.GONE);
				
				if ((assignments.get(position).is_published.equals("1"))
						&& (assignments.get(position).is_marked.equals("1"))) {
					holder.publish.setVisibility(View.GONE);
					holder.mark.setVisibility(View.GONE);
					holder.tick.setVisibility(View.VISIBLE);
				} else if ((assignments.get(position).is_published.equals("0"))
						&& (assignments.get(position).is_marked.equals("0"))) {
					holder.publish.setVisibility(View.GONE);
					holder.tick.setVisibility(View.GONE);

					if (type_permission_mark.equalsIgnoreCase("0")) {
						holder.mark.setVisibility(View.GONE);
					} else {
						holder.mark.setVisibility(View.VISIBLE);
					}

				} else if ((assignments.get(position).is_published.equals("0"))
						&& (assignments.get(position).is_marked.equals("1") && CollegeAppUtils
								.GetSharedParameter(
										context,
										CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH)
								.equals("1"))) {
					holder.publish.setVisibility(View.VISIBLE);
					holder.tick.setVisibility(View.GONE);
					holder.mark.setVisibility(View.VISIBLE);

				} else {
					holder.publish.setVisibility(View.GONE);
				}
			} else if (CollegeAppUtils.GetSharedParameter(context,
					CollegeConstants.COLLEGE_ASSIGNMENT_PUBLISH).equals("1")
					&& (assignments.get(position).is_marked.equals("1"))) {
				holder.publish.setVisibility(View.VISIBLE);
				holder.tick.setVisibility(View.GONE);
				holder.mark.setVisibility(View.GONE);

			}

			else {
				holder.publish.setVisibility(View.GONE);
				holder.mark.setVisibility(View.GONE);

				holder.tick.setVisibility(View.GONE);
			}
		} else {
			holder.publish.setVisibility(View.GONE);
			holder.mark.setVisibility(View.GONE);
			holder.attachment.setVisibility(View.GONE);
			holder.tick.setVisibility(View.GONE);
		}
		if (!assignments.get(position).file_name.equals("")
				&& !assignments.get(position).attachment.equals("")&&(isOnline)) {
			holder.attachment.setVisibility(View.VISIBLE);
		} else {

			holder.attachment.setVisibility(View.GONE);
		}
		holder.title.setText(assignments.get(position).title);
		holder.subject.setText(assignments.get(position).sub_name);
		holder.publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
						.GetSharedParameter(context, "id")));
				nameValuePairs.add(new BasicNameValuePair("activity_id",
						assignments.get(position).id));
				new UploadAsyntask().execute(nameValuePairs);

			}

		});
		holder.mark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						CollegeTeacherAssignmentResultsActivity.class);
				intent.putExtra(CollegeConstants.IS_ONLINE, true);
				intent.putExtra(CollegeConstants.ASSIGNMENTSTUDENT,
						assignments.get(position).object.toString());
				intent.putExtra(CollegeConstants.SUBJECT,
						subject.object.toString());
				context.startActivity(intent);
			}

		});
		return convertView;
	}

	private class ViewHolder {
		LinearLayout dateDisplay;
		TextView title, subject, dayNum, day;
		ImageView publish, mark, attachment, tick;
	}

	private class UploadAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setTitle(context.getResources().getString(R.string.publish));
			dialog.setMessage(context.getResources().getString(
					R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> namevaluepair = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					CollegeUrls.api_activity_publish);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						error = "Published Successfully";

						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				}

			} catch (JSONException e) {

			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {
				dialoggg = new AlertDialog.Builder(context);
				dialoggg.setTitle("Publish");
				dialoggg.setMessage(error);
				dialoggg.setNeutralButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								CollegeAppUtils.SetSharedBoolParameter(context,
										"update", true);
								notifyDataSetChanged();
								/*
								 * Intent intent = new Intent(context,
								 * TeacherMainActivity.class);
								 * intent.addFlags(Intent
								 * .FLAG_ACTIVITY_CLEAR_TOP);
								 * context.startActivity(intent);
								 */
								Intent intent = null;
								switch (type_variable_assignment) {
								case 1:
									intent = new Intent(
											context,
											CollegeTeacherAssignmentListActivity.class);
									break;
								case 2:
									intent = new Intent(
											context,
											CollegeTeacherTestListActivity.class);

									break;
								}
								intent.putExtra(CollegeConstants.IS_ONLINE,
										isOnline);

								if (isOnline) {
									intent.putExtra(
											CollegeConstants.TASSIGNMENT,
											subject.object.toString());
								} else {

									intent.putExtra(
											CollegeConstants.OFFLINE_SUBJECT_ID,
											subject.id);
								}

								intent.putExtra(CollegeConstants.PAGE_TITLE,
										page_title);
								context.startActivity(intent);

							}
						});
				dialoggg.show();
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(context, page_title, error);
					} else {
						CollegeAppUtils
								.showDialog(
										context,
										page_title,
										context.getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							context,
							page_title,
							context.getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}
}