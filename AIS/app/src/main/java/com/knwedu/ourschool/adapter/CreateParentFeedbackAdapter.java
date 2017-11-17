package com.knwedu.ourschool.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Teachers;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateParentFeedbackAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Teachers> teachers;
	private ProgressDialog dialog;
	private int ratingVal;

	public CreateParentFeedbackAdapter(Context context, ArrayList<Teachers> teachers) {
		this.context = context;
		this.teachers = teachers;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (this.teachers != null) {
			return this.teachers.size();
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
			convertView = inflater.inflate(R.layout.parent_feedback_create_list_item_view, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.textViewName);
			holder.btnFeedback = (Button) convertView.findViewById(R.id.btnFeedback);



			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.name);

		holder.name.setText(teachers.get(position).fname + " " + teachers.get(position).mname + " " + teachers.get(position).lname);


		holder.btnFeedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(position);

			}
		});


		return convertView;
	}

	private class ViewHolder {
		TextView name;
		Button btnFeedback;
	}

	private void showDialog(final int position){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set dialog message
		alertDialogBuilder.setCancelable(false);

		View view = inflater.inflate(R.layout.create_parent_feedback_dialog, null);
		Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		final RatingBar rb = (RatingBar) view.findViewById(R.id.ratingBar);
		final EditText edtComment = (EditText) view.findViewById(R.id.dialog_txt_remarks);
		rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
										boolean fromUser) {
				// TODO Auto-generated method stub
				ratingVal = (int)rating;
			}
		});
		alertDialogBuilder.setView(view);

		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(rb.getRating()>0){
					loadData(position,edtComment.getText().toString(), ratingVal);
					alertDialog.cancel();
				}else{
					Toast.makeText(context,"Rating should be minimum 1",Toast.LENGTH_LONG).show();
				}

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

	private void loadData(int position,String msg, int rating) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(
						( context.getApplicationContext()),
						Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SchoolAppUtils.GetSharedParameter(
						( context.getApplicationContext()),
						Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("child_id",
				SchoolAppUtils.GetSharedParameter(
						( context.getApplicationContext()),
						Constants.CHILD_ID)));
		nameValuePairs.add(new BasicNameValuePair("star",rating+""));
		nameValuePairs.add(new BasicNameValuePair("comments", msg));
		nameValuePairs.add(new BasicNameValuePair("teacher_id",
				teachers.get(position).teacher_id));


		new SaveFeedbackAsynTask().execute(nameValuePairs);
	}

	private class SaveFeedbackAsynTask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		String success = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setTitle("Loading ");
			dialog.setMessage(context.getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];

			// Log parameters:
			Log.d("url extension", Urls.api_save_given_feedback);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_save_given_feedback);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						success = json.getString("data");
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} else {
					error = context.getString(R.string.unknown_response);
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
				Toast.makeText(context,success,Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context,error,Toast.LENGTH_LONG).show();
			}
		}
	}
}