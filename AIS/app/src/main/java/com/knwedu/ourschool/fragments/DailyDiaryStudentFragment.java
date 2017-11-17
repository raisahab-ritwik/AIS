package com.knwedu.ourschool.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.CreateDailyDairyActivity;
import com.knwedu.ourschool.EditDailyDairyActivity;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.TeacherDairyListActivity;
import com.knwedu.ourschool.WebViewActivity;
import com.knwedu.ourschool.adapter.TeacherAdapterBehaviour;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork;
import com.knwedu.ourschool.utils.DataStructureFramwork.Subject;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DailyDiaryStudentFragment extends Fragment implements AdapterView.OnItemSelectedListener {
	private ArrayList<DataStructureFramwork.DailyDiary> dailies = new ArrayList<DataStructureFramwork.DailyDiary>();
	private ProgressDialog dialog;
	private TextView header;
	private ListView listView;
	private TeacherDailyDiaryListAdapter adapter;
	private View view;
	ImageButton addAssignment;
	private TextView textEmpty;
	public DatabaseAdapter mDatabase;
	private String page_title = "";
	private Spinner spinnerType;
	private int spinnerTypeVal;
	private ImageButton imgAddAssingment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mDatabase = ((SchoolApplication) getActivity().getApplication())
				.getDatabase();
		view = inflater.inflate(R.layout.activity_teacher_diary_list,
				container, false);

		RelativeLayout header_layout = (RelativeLayout)view.findViewById(R.id.header_layout);
		header_layout.setVisibility(View.GONE);

		textEmpty = (TextView) view.findViewById(R.id.textEmpty);

		spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
		spinnerType.setOnItemSelectedListener(this);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),R.array.diary_type,R.layout.spinner_text);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(adapter);

		listView = (ListView) view.findViewById(R.id.listview);
		imgAddAssingment = (ImageButton)view.findViewById(R.id.add_assignment);
		imgAddAssingment.setVisibility(View.GONE);
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// On selecting a spinner item
		spinnerTypeVal = position+1;
		loadData();
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		spinnerTypeVal = 1;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("Google Analytics", "Tracking Start");
		EasyTracker.getInstance(getActivity()).activityStart(getActivity());

	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("Google Analytics", "Tracking Stop");
		EasyTracker.getInstance(getActivity()).activityStop(getActivity());
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	private void loadData() {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.UORGANIZATIONID)));
		nameValuePairs
				.add(new BasicNameValuePair("user_id", SchoolAppUtils
						.GetSharedParameter(getActivity(),
								Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("child_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));

		nameValuePairs.add(new BasicNameValuePair("type",
				spinnerTypeVal+""));

		new GetDailyDiaryDetailsAsyntask().execute(nameValuePairs);
	}

	private class GetDailyDiaryDetailsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(page_title);
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> namevaluepair = params[0];
			Log.d("url extension: ", Urls.api_get_daily_diary_details);
			String parameters = "";
			for (NameValuePair nvp : namevaluepair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
					Urls.api_get_daily_diary_details);
			dailies.clear();
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
							for (int i = 0; i < array.length(); i++) {
								DataStructureFramwork.DailyDiary daily = new DataStructureFramwork.DailyDiary(
										array.getJSONObject(i));
								dailies.add(daily);
							}
						} catch (Exception e) {
							error = "No data found";
							return false;
						}


						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} else {
					error = getResources().getString(R.string.unknown_response);
				}

			} catch (JSONException e) {
				error = "Please check with better internet connection";
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
				if (dailies != null) {

					if (dailies.size() > 0) {
						textEmpty.setVisibility(View.GONE);
						adapter = new TeacherDailyDiaryListAdapter(
								getActivity(),
								dailies);
						listView.setAdapter(adapter);

					} else {
						textEmpty.setText("No data found");
						textEmpty.setVisibility(View.VISIBLE);
					}

				}
			} else {
				listView.setAdapter(null);
				textEmpty.setText(error.toString());
				textEmpty.setVisibility(View.VISIBLE);
			}
		}
	}

	public class TeacherDailyDiaryListAdapter extends BaseAdapter {
		ViewHolder holder;
		private LayoutInflater inflater;
		Context context;
		private ArrayList<DataStructureFramwork.DailyDiary> dailies;
		public int[] check;
		private String date, type;
		private ProgressDialog dialog;

		public TeacherDailyDiaryListAdapter(Context context, ArrayList<DataStructureFramwork.DailyDiary> dailies) {
			this.context = context;
			this.dailies = dailies;
			check = new int[this.dailies.size()];
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			if (this.dailies != null) {
				return this.dailies.size();
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
				convertView = inflater.inflate(R.layout.daily_diary_student_adapter_view, null);
				holder = new ViewHolder();
				holder.textViewStudent = (TextView) convertView.findViewById(R.id.textViewStudent);
				holder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
				holder.textViewCreatedDate = (TextView) convertView.findViewById(R.id.textViewCreatedDate);
				holder.textViewCreatedBy = (TextView) convertView.findViewById(R.id.textViewCreatedBy);
				holder.textViewDesc = (TextView) convertView.findViewById(R.id.textViewDesc);
				holder.imageViewComment = (ImageView) convertView.findViewById(R.id.imageViewComment);
				holder.imageViewComment.setVisibility(View.GONE);
				holder.layout_student = (RelativeLayout) convertView.findViewById(R.id.layout_student);
				holder.imageViewViewDoc = (ImageView) convertView.findViewById(R.id.imageViewViewDoc);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}


			SchoolAppUtils.setFont(context, holder.textViewStudent);
			SchoolAppUtils.setFont(context, holder.textViewTitle);
			SchoolAppUtils.setFont(context, holder.textViewCreatedDate);
			SchoolAppUtils.setFont(context, holder.textViewCreatedBy);
			SchoolAppUtils.setFont(context, holder.textViewDesc);

			if(dailies.get(position).name.equalsIgnoreCase("null")){
				holder.layout_student.setVisibility(View.GONE);
			} else{
				holder.layout_student.setVisibility(View.VISIBLE);
			}

			if(dailies.get(position).file_name.equalsIgnoreCase("null")|| dailies.get(position).file_name.isEmpty()){
				holder.imageViewViewDoc.setVisibility(View.GONE);
			}else{
				holder.imageViewViewDoc.setVisibility(View.VISIBLE);
			}

			if(dailies.get(position).is_comment.equalsIgnoreCase("Y") && !dailies.get(position).comment.equalsIgnoreCase("null"))
			{
				holder.imageViewComment.setVisibility(View.VISIBLE);
			} else{
				holder.imageViewComment.setVisibility(View.GONE);
			}

			holder.imageViewViewDoc.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showGetDocDialog(position, dailies.get(position).file_name);
				}
			});

			holder.textViewTitle.setText(dailies.get(position).title);
			holder.textViewDesc.setText(dailies.get(position).description);
			holder.textViewStudent.setText(dailies.get(position).name);
			holder.textViewCreatedBy.setText(dailies.get(position).created);
			holder.textViewCreatedDate.setText(dailies.get(position).created_date);

			holder.imageViewComment.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showViewDialog(position);
				}
			});

			return convertView;
		}

		private class ViewHolder {
			RelativeLayout titleLayout, gradeLayout, layout_desc, layout_student;
			TextView textViewStudent, textViewTitle, textViewGrade, textViewCreatedDate, textViewCreatedBy, textViewDesc, textViewStatus;
			ImageView imageViewComment, imageViewViewDoc;
		}

		private void showViewDialog(final int position) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			// set dialog message
			alertDialogBuilder.setCancelable(false);

			View view = inflater.inflate(R.layout.alert_dialog, null);
			Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
			final TextView showComment = (TextView) view.findViewById(R.id.view_remarks);

			alertDialogBuilder.setView(view);

			// create alert dialog
			final AlertDialog alertDialog = alertDialogBuilder.create();
			showComment.setText(dailies.get(position).comment);
			// show it
			alertDialog.show();

			btnSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.cancel();
				}
			});

		}

		private void showGetDocDialog(final int position, final String filename) {
			new AlertDialog.Builder(context)
					.setTitle("Select option")
					.setPositiveButton("View Document",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int which) {
									// continue with view
									Intent intent = new Intent(context, WebViewActivity.class);
									intent.putExtra("url", Urls.api_get_dialy_diary_attachment + "/" + dailies.get(position).id + File.separator + filename);
									context.startActivity(intent);
								}
							})
					.setNegativeButton("Download",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int which) {
									// download only PDF files as report
									final DownloadTask downloadTask = new DownloadTask(context, filename);
									downloadTask.execute(Urls.api_get_dialy_diary_attachment + "/"
											+ dailies.get(position).id);

								}
							}).setIcon(android.R.drawable.ic_dialog_info).show();
		}

		private void showCreateCommentDialog(final int position) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			// set dialog message
			alertDialogBuilder.setCancelable(false);

			View view = inflater.inflate(R.layout.edit_comment_dialog, null);
			Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
			Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
			final EditText createComment = (EditText) view.findViewById(R.id.create_remarks);

			alertDialogBuilder.setView(view);

			// create alert dialog
			final AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();

			btnSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					loadCommentData(position, createComment.getText().toString().trim());
					alertDialog.cancel();
				}
			});
			btnCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.cancel();
				}
			});

		}

		private void showDeleteDialog(final int position) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			// set dialog message
			alertDialogBuilder.setCancelable(false);

			View view = inflater.inflate(R.layout.parent_feedback_dialog, null);
			Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
			Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
			alertDialogBuilder.setView(view);

			// create alert dialog
			final AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

			btnSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					requestDeleteData(position);
					notifyDataSetChanged();
					alertDialog.cancel();
				}
			});

			btnCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.cancel();
				}
			});

		}

		private void loadCommentData(int position, String comment) {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(context,
							Constants.UORGANIZATIONID)));
			nameValuePairs
					.add(new BasicNameValuePair("user_id", SchoolAppUtils
							.GetSharedParameter(context,
									Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("daily_diary_id", dailies.get(position).id));

			nameValuePairs.add(new BasicNameValuePair("comment",
					comment.trim()));

			new SaveCommentAsyncTask().execute(nameValuePairs);
		}

		private void requestDeleteData(int position) {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(context,
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("daily_diary_id", dailies.get(position).id));

			new DeleteDiaryAsyncTask().execute(nameValuePairs);
		}

		private class SaveCommentAsyncTask extends
				AsyncTask<List<NameValuePair>, Void, Boolean> {
			String error;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = new ProgressDialog(context);
				dialog.setTitle("Saving Comment");
				dialog.setMessage(context.getResources().getString(R.string.please_wait));
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected Boolean doInBackground(List<NameValuePair>... params) {

				List<NameValuePair> namevaluepair = params[0];
				Log.d("url extension: ", Urls.api_save_daily_comment);
				String parameters = "";
				for (NameValuePair nvp : namevaluepair) {
					parameters += nvp.getName() + "=" + nvp.getValue() + ",";
				}
				Log.d("Parameters: ", parameters);
				JsonParser jParser = new JsonParser();
				JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
						Urls.api_save_daily_comment);
				try {

					if (json != null) {
						if (json.getString("result").equalsIgnoreCase("1")) {
							error = json.getString("data");
							return true;
						} else {
							try {
								error = json.getString("data");
							} catch (Exception e) {
							}
							return false;
						}
					} else {
						error = context.getResources().getString(R.string.unknown_response);
					}

				} catch (JSONException e) {
					error = "Something went wrong";
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
					Toast.makeText(context, error, Toast.LENGTH_LONG).show();
					loadData();
				} else {
					Toast.makeText(context, error, Toast.LENGTH_LONG).show();
				}
			}
		}

		private class DeleteDiaryAsyncTask extends
				AsyncTask<List<NameValuePair>, Void, Boolean> {
			String error;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog = new ProgressDialog(context);
				dialog.setTitle("Deleting");
				dialog.setMessage(context.getResources().getString(R.string.please_wait));
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected Boolean doInBackground(List<NameValuePair>... params) {

				List<NameValuePair> namevaluepair = params[0];
				Log.d("url extension: ", Urls.api_delete_daily_diary);
				String parameters = "";
				for (NameValuePair nvp : namevaluepair) {
					parameters += nvp.getName() + "=" + nvp.getValue() + ",";
				}
				Log.d("Parameters: ", parameters);
				JsonParser jParser = new JsonParser();
				JSONObject json = jParser.getJSONFromUrlnew(namevaluepair,
						Urls.api_delete_daily_diary);
				try {

					if (json != null) {
						if (json.getString("result").equalsIgnoreCase("1")) {
							error = json.getString("data");
							return true;
						} else {
							try {
								error = json.getString("data");
							} catch (Exception e) {
							}
							return false;
						}
					} else {
						error = context.getResources().getString(R.string.unknown_response);
					}

				} catch (JSONException e) {
					error = "Something went wrong";
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
					Toast.makeText(context, error, Toast.LENGTH_LONG).show();
					loadData();
					notifyDataSetChanged();
				} else {
					Toast.makeText(context, error, Toast.LENGTH_LONG).show();
				}
			}
		}

	}
}
