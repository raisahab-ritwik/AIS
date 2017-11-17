package com.knwedu.ourschool.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.DirectoryAdapter;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Directory;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class SchoolInfoFragment extends Fragment {
	private ArrayList<Directory> fellows;
	private ProgressDialog dialog;
	private View view;
	private ListView list;
	private DirectoryAdapter adapter;
	private DatabaseAdapter mDatabase;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_teacher_assignment_list,
				container, false);
		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));

		initialize();
		return view;
	}

	private void initialize() {
		list = (ListView) view.findViewById(R.id.listview);
		fellows = new ArrayList<Directory>();
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getDatabase();
		mDatabase.open();
		fellows.clear();

		fellows = mDatabase.getAllDirectory();
		mDatabase.close();

		adapter = new DirectoryAdapter(getActivity(), fellows);
		list.setAdapter(adapter);

		if(SchoolAppUtils.isOnline(getActivity())) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));

			GetDirectoryAsyntask mGetDirectoryAsyntask = new GetDirectoryAsyntask(getActivity(),list,adapter,mDatabase);
			mGetDirectoryAsyntask.execute(nameValuePairs);


		}
		else {

			SchoolAppUtils.showDialog(getActivity(), getActivity()
					.getTitle().toString(), "You are offline.");
		}

	}

	private class GetDirectoryAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		Context mContext;
		ListView lstv;
		DirectoryAdapter mDirAdapter;
		String error = "";
		DatabaseAdapter mDatabaseAdapter;
		public  GetDirectoryAsyntask(Context cntx, ListView list,DirectoryAdapter adapter,DatabaseAdapter mDatabaseAdapter){

			this.mContext = cntx;
			this.mDirAdapter = adapter;
			this.lstv = list;
			this.mDatabaseAdapter = mDatabaseAdapter;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getActivity().getTitle().toString());
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nvp = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nvp,
					Urls.api_school_info);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						mDatabaseAdapter.open();
						mDatabaseAdapter.deleteAllDirectory();


						fellows = new ArrayList<Directory>();
						for (int i = 0; i < array.length(); i++) {
							Directory assignment = new Directory(
									array.getJSONObject(i));
							fellows.add(assignment);
							mDatabase.addDirectory(assignment);
						}
						mDatabase.close();
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
				if (fellows != null) {
					//adapter = new DirectoryAdapter(getActivity(), fellows);
					//list.setAdapter(adapter);
					mDirAdapter.setData(fellows);
					mDirAdapter.notifyDataSetChanged();

				}
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), error);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(),
							getResources().getString(R.string.error));
				}

			}
		}

	}
}
