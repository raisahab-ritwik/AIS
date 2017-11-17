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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.ClassFellowsAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ClassFellows;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.LoadImageAsyncTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class ClassFellowFragment extends Fragment {

	private ArrayList<ClassFellows> fellows;
	private ProgressDialog dialog;
	private String section_id;
	private static ImageView image;
	private static TextView name;
	private static TextView classs;
	private static RelativeLayout child_panel;
	private View view;
	private ListView listView;
	private ClassFellowsAdapter adapter;
	ImageButton addAssignment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.class_fellow_list, container, false);
		image = (ImageView) view.findViewById(R.id.image_view);
		name = (TextView) view.findViewById(R.id.name_txt);
		classs = (TextView) view.findViewById(R.id.class_txt);
		// child_panel = (RelativeLayout) view.findViewById(R.id.child_panel);
		// setUserImage(getActivity());

		//--------------------------------------------

			SchoolAppUtils.loadAppLogo(getActivity(),
					(ImageButton) view.findViewById(R.id.app_logo));


			initialize();

			return view;

	}

	private void initialize() {
		if (getActivity().getIntent().getExtras() != null) {
			String temp = getActivity().getIntent().getExtras()
					.getString("Section_id");
			section_id = temp;
		}

		listView = (ListView) view.findViewById(R.id.listview);

		if (fellows == null) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));

			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.CHILD_ID)));

			new GetAssignmentsAsyntask().execute(nameValuePairs);
		} else {
			adapter = new ClassFellowsAdapter(getActivity(), fellows);
			listView.setAdapter(adapter);
		}
	}

	public static void setUserImage(Context context) {
		if (SchoolAppUtils.GetSharedBoolParameter(context,
				Constants.ISPARENTSIGNIN)) {
			child_panel.setVisibility(View.VISIBLE);
		} else {
			child_panel.setVisibility(View.GONE);
		}
		JSONObject c = null;
		try {
			c = new JSONObject(SchoolAppUtils.GetSharedParameter(context,
					Constants.SELECTED_CHILD_OBJECT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c != null) {
			StudentProfileInfo info = new StudentProfileInfo(c);
			name.setText(info.fullname);
			classs.setText("Class: " + info.class_section);

			new LoadImageAsyncTask(context, image, Urls.image_url_userpic,
					info.image, true).execute();

		}
	}

	private class GetAssignmentsAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.class_fellows));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> url = params[0];
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(url,
					Urls.api_class_fellow);
			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array = json.getJSONArray("data");
						fellows = new ArrayList<ClassFellows>();
						for (int i = 0; i < array.length(); i++) {
							ClassFellows fellow = new ClassFellows(
									array.getJSONObject(i));

							if (!SchoolAppUtils.GetSharedParameter(
									getActivity(), Constants.CHILD_ID)
									.equalsIgnoreCase(fellow.id)) {

								fellows.add(fellow);
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
					adapter = new ClassFellowsAdapter(getActivity(), fellows);
					listView.setAdapter(adapter);
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
