package com.knwedu.ourschool.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.knwedu.ourschool.SyllabusDetailsActivity;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.SyllabusSubject;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.SchoolSyllabusAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class SchoolSyllabusFragment extends Fragment {
	private ExpandableListView list;
	private SchoolSyllabusAdapter adapter;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<String> sessionNames;
	private HashMap<String, ArrayList<String>> sessionHashmap;
	private HashMap<String, ArrayList<SyllabusSubject>> syllabusHashmap;
	private TextView textEmpty;
	private int type;
	//private TextView textnodata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.syllabus_fragment, container, false);
		initialize();
		return view;
	}
	
	public SchoolSyllabusFragment(int type) {
		super();
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	private void initialize() {
		//textnodata=(TextView) view.findViewById(R.id.textnodata) ;
		sessionNames = new ArrayList<String>();
		sessionNames.clear();
		sessionHashmap = new HashMap<String, ArrayList<String>>();
		syllabusHashmap = new HashMap<String, ArrayList<SyllabusSubject>>();
		list = (ExpandableListView) view.findViewById(R.id.listview);
		textEmpty = (TextView) view.findViewById(R.id.textEmpty);
		textEmpty.setVisibility(View.GONE);
		list.setVisibility(View.VISIBLE);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		nameValuePairs.add(
				new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		if(type == 1){
			nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		}
		new GetSyllabusAsyntask().execute(nameValuePairs);

		list.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {
				Intent intent = new Intent(getActivity(), SyllabusDetailsActivity.class);
				SyllabusSubject syllabus = syllabusHashmap.get(sessionNames.get(groupPosition)).get(childPosition);
				intent.putExtra(CollegeConstants.PAGE_TITLE, getActivity().getTitle().toString());
				intent.putExtra("sub_id", syllabus.id);
				intent.putExtra("sub_title", syllabus.title);
				intent.putExtra("sub_desc", syllabus.description);
				intent.putExtra("sub_attachment", syllabus.attachment);
				getActivity().startActivity(intent);
				return false;
			}
		});
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

	private class GetSyllabusAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.syllabus));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			ArrayList<String> subjectNames = null;
			ArrayList<SyllabusSubject> subjects = null;
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_syllabus);
			Log.d("url extension", Urls.api_syllabus);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							return true;
						}
						// Term name array
						sessionNames = new ArrayList<String>();
						sessionHashmap.clear();
						syllabusHashmap.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.optJSONObject(i);
							Iterator<String> iterator = object.keys();
							/**
							 * Term loop
							 */
							while (iterator.hasNext()) {
								String key = (String) iterator.next();
								sessionNames.add(key);
								JSONArray array1 = object.optJSONArray(key);
								/**
								 * Termwise subject loop
								 */
								for (int k = 0; k < array1.length(); k++) {
									JSONObject subjectKey = array1.getJSONObject(k);
									Iterator<String> iterator1 = subjectKey.keys();

									while (iterator1.hasNext()) {
										String key1 = (String) iterator1.next();
										if (subjectKey.get(key1) instanceof JSONObject) {
											SyllabusSubject subject = new SyllabusSubject(
													(JSONObject) subjectKey.get(key1));
											System.out.println("subject" + subject);
											if (!sessionHashmap.containsKey(key)) {
												subjectNames = new ArrayList<String>();
												subjectNames.add(key1);
												// Adding value in hashmap
												// <TermName, Array Of subject
												// Names
												sessionHashmap.put(key, subjectNames);
												// Adding value in hashmap
												// <TermName, Array Of syllabus
												// objects
												subjects = new ArrayList<SyllabusSubject>();
												subjects.add(subject);
												syllabusHashmap.put(key, subjects);
											} else {
												subjectNames.add(key1);
												subjects.add(subject);
											}
										}
									}
								}
							}
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

			if (result) {
				adapter = new SchoolSyllabusAdapter(getActivity(), sessionNames, sessionHashmap);
				list.setAdapter(adapter);
				for (int i = 0; i < sessionNames.size(); i++) {
					list.expandGroup(i);
				}
			} else {
				textEmpty.setText("No Syllabus Created Yet");
				textEmpty.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}
		}
	}
}
