package com.knwedu.college.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.knwedu.college.adapter.CollegeDirectoryAdapter;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeDataStructureFramwork.CDirectory;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CollegeSchoolInfoFragment extends Fragment {
	private ArrayList<CDirectory> fellows;
	private ProgressDialog dialog;
	private View view;
	private ListView list;
	private CollegeDirectoryAdapter adapter;
	private CollegeDBAdapter mDatabase;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_teacher_assignment_list, container, false);
		initialize();
		return view;
	}
	private void initialize()
	{
/*		list = (ListView) view.findViewById(R.id.listview);
		if (fellows != null) {
			adapter = new CollegeDirectoryAdapter(getActivity(),fellows);
			list.setAdapter(adapter);

		} else {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));

			new GetAssignmentsAsyntask().execute(nameValuePairs);
		}*/
		list = (ListView) view.findViewById(R.id.listview);
		fellows = new ArrayList<CDirectory>();
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getCollegeDatabase();
		mDatabase.open();
		fellows.clear();

		fellows = mDatabase.getAllDirectory();
		mDatabase.close();

		adapter = new CollegeDirectoryAdapter(getActivity(), fellows);
		list.setAdapter(adapter);

		if(CollegeAppUtils.isOnline(getActivity())) {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					CollegeAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));

			new GetAssignmentsAsyntask(getActivity(),list,adapter,mDatabase).execute(nameValuePairs);

		}
		else{
			CollegeAppUtils.showDialog(getActivity(), getActivity()
					.getTitle().toString(), "Data may vary as it is in offline mode now");
		}


	}
	
	private class GetAssignmentsAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean>
	{
		Context mContext;
		ListView lstv;
		CollegeDirectoryAdapter mDirAdapter;
		String error = "";
		CollegeDBAdapter mDatabaseAdapter;

		public  GetAssignmentsAsyntask(Context cntx, ListView list,CollegeDirectoryAdapter adapter,CollegeDBAdapter mDatabaseAdapter){

			this.mContext = cntx;
			this.mDirAdapter = adapter;
			this.lstv = list;
			this.mDatabaseAdapter = mDatabaseAdapter;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.please_wait));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			if(fellows.size() == 0) {
				dialog.show();
			}
		}
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp, CollegeUrls.api_college_directory);
			try {

				if(json!=null){
					if(json.getString("result").equalsIgnoreCase("1")){
						JSONArray array;
						try
						{
							array = json.getJSONArray("info");
						}
						catch (Exception e)
						{
							return true;
						}
						mDatabaseAdapter.open();
						mDatabaseAdapter.deleteAllDirectory();
						fellows = new ArrayList<CDirectory>();
						for(int i = 0; i < array.length(); i++)
						{
							CDirectory assignment = new CDirectory(array.getJSONObject(i));
							fellows.add(assignment);
							mDatabase.addDirectory(assignment);
						}
						mDatabase.close();
						return true;
					}
					else
					{
						try{error = json.getString("data");}catch (Exception e) {}
						return false;
					}
				}

			}
			catch (JSONException e) {

			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if(dialog != null)
			{
				dialog.dismiss();
				dialog = null;
			}
			if(result)
			{
				if(fellows != null)
				{
					adapter = new CollegeDirectoryAdapter(getActivity(), fellows);
					mDirAdapter.setData(fellows);
					mDirAdapter.notifyDataSetChanged();
				}
			}
			else
			{
				if(error != null)
				{
					if(error.length() > 0)
					{
						CollegeAppUtils.showDialog(getActivity(),getResources().getString(R.string.error), error);
					}
					else
					{
						CollegeAppUtils.showDialog(getActivity(),getResources().getString(R.string.error),
								getResources().getString(R.string.please_check_internet_connection));
					}
				}
				else
				{
					CollegeAppUtils.showDialog(getActivity(),getResources().getString(R.string.error),
							getResources().getString(R.string.please_check_internet_connection));
				}

			}
		}

	}
}
