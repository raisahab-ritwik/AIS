package com.knwedu.college.fragments;
/*package com.knwedu.ourschool.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.knwedu.ourschool.GraphViewExamActivity;
import com.knwall.knwedumycollegeapp.R;
import com.knwedu.ourschool.adapter.ExamsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ExamList;
import com.knwedu.ourschool.utils.DataStructureFramwork.ExamSchedule;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ExamsListFragment extends Fragment {
	private ListView list;
	private ExamsAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<ExamList> exams;
	private ArrayList<ExamSchedule> examschedule;
	private LinearLayout headerLayout;
	AlertDialog.Builder dialoggg;

	private Button showMonthWeek;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_list, container, false);
		initialize();
		return view;
	}

	private void initialize() {
		InMobi.initialize(view.getContext(), getResources().getString(R.string.InMobi_Property_Id));
		IMBanner banner = (IMBanner) view.findViewById(R.id.banner);
		banner.loadBanner();
		
		
		showMonthWeek = (Button) getActivity().findViewById(
				R.id.show_monthly_weekly);
		if (SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.USERTYPEID).equals(Constants.USERTYPE_PARENT)
				|| SchoolAppUtils.GetSharedParameter(getActivity(),
						Constants.USERTYPEID)
						.equals(Constants.USERTYPE_STUDENT)) {
			handleGraphButton();
		}
		list = (ListView) view.findViewById(R.id.listview);
		if (exams != null) {
			adapter = new ExamsAdapter(getActivity());
			for (int i = 0; i < exams.size(); i++) {
				adapter.addSectionHeaderItem(exams, i);
			}
			list.setAdapter(adapter);

		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.CHILD_ID)));

			new GetExamAsyntask().execute(nameValuePairs);
		}

	}

	private void handleGraphButton() {
		showMonthWeek.setText("Graph");
		showMonthWeek.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.graph), null, null, null);
		showMonthWeek.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						GraphViewExamActivity.class);
				intent.putExtra("api_url", Urls.api_graph_exam_term);
				startActivity(intent);
			}
		});
		showMonthWeek.setVisibility(View.VISIBLE);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	private class GetExamAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.exam));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> nameValuePair = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_exam_list);
			String parameters = "";
			for (NameValuePair nvp : nameValuePair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
					Urls.api_exam_list);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("exam_list");
						} catch (Exception e) {
							return true;
						}
						exams = new ArrayList<ExamList>();
						for (int i = 0; i < array.length(); i++) {
							ExamList exam = new ExamList(array.getJSONObject(i));
							exams.add(exam);
						}
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

			if (exams != null) {
				
				if(exams.size()>0){
					adapter = new ExamsAdapter(getActivity());
					for (int i = 0; i < exams.size(); i++) {
						adapter.addSectionHeaderItem(exams, i);
					}
					list.setAdapter(adapter);
					list.setOnItemClickListener(listener);
				}else{
					SchoolAppUtils.showDialog(
							getActivity(),
							getResources().getString(R.string.error), "No Exam data found");
				}

				
			}

			else {

				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils.showDialog(getActivity(), getResources()
								.getString(R.string.error), error);
						headerLayout.setVisibility(View.INVISIBLE);
					} else {
						SchoolAppUtils
								.showDialog(
										getActivity(),
										getResources()
												.getString(R.string.error),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("exam_term_id", exams
					.get(arg2).exam_term_id));
			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.CHILD_ID)));

			new GetExamScheduleAsyntask().execute(nameValuePairs);

		}
	};

	private class GetExamScheduleAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.exam));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			
			List<NameValuePair> nameValuePair = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_exam_schedule);
			String parameters = "";
			for (NameValuePair nvp : nameValuePair) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			
			
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePair,
					Urls.api_exam_schedule);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						examschedule = new ArrayList<ExamSchedule>();
						for (int i = 0; i < array.length(); i++) {
							ExamSchedule exam = new ExamSchedule(
									array.getJSONObject(i));
							examschedule.add(exam);
						}
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

			if (examschedule != null) {
				
				if(examschedule.size() > 0){
					adapter = new ExamsAdapter(getActivity());
					adapter.notifyDataSetChanged();
					for (int i = 0; i < examschedule.size(); i++) {
						adapter.addItem(examschedule);
					}
					list.setAdapter(adapter);
				}else{
					SchoolAppUtils.showDialog(
							getActivity(),
							getResources().getString(R.string.error), "No Exam Schedule found");
				}

				
				list.setOnItemClickListener(null);
				showMonthWeek.setVisibility(View.GONE);
			}

			else {

				if (error != null) {
					if (error.length() > 0) {
						SchoolAppUtils.showDialog(getActivity(), getResources()
								.getString(R.string.error), error);

					} else {
						SchoolAppUtils
								.showDialog(
										getActivity(),
										getResources()
												.getString(R.string.error),
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					SchoolAppUtils.showDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.please_check_internet_connection));
				}

			}
		}

	}

}
*/