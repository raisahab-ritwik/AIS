package com.knwedu.ourschool.fragments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.adapter.SlideAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class HelpFragment extends Fragment {
	private FlipViewController flipView;

	private Button syncButton;
	private SlideAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		flipView = new FlipViewController(getActivity(),
				FlipViewController.HORIZONTAL);
		adapter = new SlideAdapter(getActivity());
		flipView.setAdapter(adapter);
		handleButton();

		return flipView;
	}

	@Override
	public void onResume() {
		super.onResume();
		flipView.onResume();
		System.out.println("RESUME");
		if(SchoolAppUtils.isOnline(getActivity()) && (SchoolAppUtils.GetSharedIntParameter(getActivity(),
				Constants.TUTORIAL_FILE_COUNT) == 0 || SchoolAppUtils.GetSharedIntParameter(getActivity(),
				Constants.TUTORIAL_FILE_COUNT) > SchoolAppUtils.countTutorialFiles(getActivity()))){
			
			System.out.println("Downloading Tutorial......");
			GetFilesTask task = new GetFilesTask(getActivity());
			task.execute();
			
		}
		
	}

	@Override
	public void onPause() {
		super.onPause();
		flipView.onPause();
		System.out.println("PAUSE");

	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("Google Analytics", "Tracking Start");
		EasyTracker tracker = EasyTracker.getInstance(getActivity());
		tracker.set(Fields.SCREEN_NAME, "Help Screen");
		tracker.send(MapBuilder.createAppView().build());
		//tracker.activityStart(getActivity());

	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d("Google Analytics", "Tracking Stop");
		EasyTracker.getInstance(getActivity()).activityStop(getActivity());
		
	}

	private void handleButton() {
		syncButton = (Button) getActivity().findViewById(
				R.id.show_monthly_weekly);
		syncButton.setText("Refresh");
		syncButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
				getResources().getDrawable(R.drawable.refresh), null, null,
				null);
		syncButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!SchoolAppUtils.isOnline(getActivity())) {
					Toast.makeText(getActivity(), "No Internet",
							Toast.LENGTH_LONG).show();
					return;
				}

				
				GetFilesTask task = new GetFilesTask(getActivity());
				task.execute();

			}
		});
		syncButton.setVisibility(View.GONE);
	}

	private class GetFilesTask extends AsyncTask<Void, Integer, Boolean> {
		private Activity context;

		public GetFilesTask(Activity context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// delete previous images
			SchoolAppUtils.deleteTutorialFiles(context);
			SchoolAppUtils.SetSharedIntParameter(getActivity(), Constants.TUTORIAL_FILE_COUNT, 0);

			syncButton.setText("Downloading..");
			syncButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.pagedownload_icon),
					null, null, null);
			syncButton.setClickable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					11);
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));
			
			JsonParser jParser = new JsonParser();
//			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
//					Urls.api_tutorial_files);
			
			try {
				
				JSONObject json = new JSONObject(getResources().getString(R.string.url));
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						int total = json.getInt("total");
						SchoolAppUtils.SetSharedIntParameter(getActivity(), Constants.TUTORIAL_FILE_COUNT, total);
						String url = json.getString("url");
						String file_name = json.getString("file_name");

						for (int count = 1; count <= total; count++) {
							downloadImage(url + file_name + count + ".png", "tutorial_" + count + ".png");
							adapter.addData("tutorial_" + count + ".png");
							publishProgress(count);
						}
						return true;
					} else {
						return false;
					}
				}
				return false;
			} catch (JSONException e) {
				return false;
			}
			
			
		}

		private void downloadImage(String urlString, String file_name) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(urlString);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// download the file
				input = connection.getInputStream();
				output = new FileOutputStream(context.getFilesDir() + "/"
						+ file_name);

				byte data[] = new byte[4096];
				int count;
				while ((count = input.read(data)) != -1) {

					output.write(data, 0, count);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}

			

		}

		protected void onProgressUpdate(Integer... progress) {
			int total = SchoolAppUtils.GetSharedIntParameter(context, Constants.TUTORIAL_FILE_COUNT);
			syncButton.setText("Downloading.. " + progress[0] + "/" + total);
			adapter.notifyDataSetChanged();

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			syncButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.refresh), null, null,
					null);
			syncButton.setText("Refresh");
			syncButton.setClickable(true);

		}

	}

}
