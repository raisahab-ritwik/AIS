package com.knwedu.ourschool.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.RequestToParentForClassActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ParentRequest;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ParentRequestAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<ParentRequest> requestStatus;
	private EditText edTxt;
	private ProgressDialog dialog;
	private int statusType;


	public ParentRequestAdapter(Context context, ArrayList<ParentRequest> requests)
	{
		this.context = context;
		this.requestStatus = requests;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.requestStatus != null)
		{
			return this.requestStatus.size();
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
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.parent_request_list_item, null);
			holder = new ViewHolder();
			holder.title 	= (TextView)convertView.findViewById(R.id.textViewTitle);
			holder.description 	= (TextView)convertView.findViewById(R.id.textViewDesc);
			holder.created_by = (TextView)convertView.findViewById(R.id.textViewCretedBy);
			holder.created_date 	= (TextView)convertView.findViewById(R.id.textViewCreatedDate);
			holder.status 	= (TextView)convertView.findViewById(R.id.textViewStatus);
			holder.comment = (TextView)convertView.findViewById(R.id.textViewComment);

			holder.approve = (ImageView)convertView.findViewById(R.id.imageViewApprove);
			holder.reject = (ImageView)convertView.findViewById(R.id.imageViewReject);

			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		RelativeLayout layout_comment = (RelativeLayout)convertView.findViewById(R.id.layout_comment);

		SchoolAppUtils.setFont(context, holder.title);
		SchoolAppUtils.setFont(context, holder.description);
		SchoolAppUtils.setFont(context, holder.created_by);
		SchoolAppUtils.setFont(context, holder.created_date);
		SchoolAppUtils.setFont(context, holder.status);
		SchoolAppUtils.setFont(context, holder.comment);

		holder.title.setText(requestStatus.get(position).title);
		holder.description.setText(requestStatus.get(position).description);
		holder.created_by.setText(requestStatus.get(position).created_by);
		holder.created_date.setText(requestStatus.get(position).created_date);

		holder.status.setText(requestStatus.get(position).status);
		holder.comment.setText(requestStatus.get(position).reply);

		if(requestStatus.get(position).status.equalsIgnoreCase("1")){
			holder.status.setVisibility(View.VISIBLE);
			layout_comment.setVisibility(View.GONE);
			holder.status.setText("Approved");
			holder.status.setTextColor(Color.GREEN);
			holder.approve.setVisibility(View.GONE);
			holder.reject.setVisibility(View.GONE);
		} else if(requestStatus.get(position).status.equalsIgnoreCase("2")){
			holder.status.setVisibility(View.VISIBLE);
			layout_comment.setVisibility(View.VISIBLE);
			holder.status.setText("Rejected");
			holder.status.setTextColor(Color.RED);
			holder.approve.setVisibility(View.GONE);
			holder.reject.setVisibility(View.GONE);
		} else{
			holder.status.setVisibility(View.GONE);
			holder.approve.setVisibility(View.VISIBLE);
			holder.reject.setVisibility(View.VISIBLE);
			layout_comment.setVisibility(View.GONE);
		}

		holder.reject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				statusType = 2;
				showDialog(position);
			}
		});

		holder.approve.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				statusType = 1;
				loadData(position,"null");
			}
		});


		return convertView;
	}
	private void loadData(int position, String msg) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("parent_id", SchoolAppUtils
				.GetSharedParameter(((RequestToParentForClassActivity) context),
						Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(
						((RequestToParentForClassActivity) context),
						Constants.UORGANIZATIONID)));
		nameValuePairs.add(new BasicNameValuePair("student_id", SchoolAppUtils
				.GetSharedParameter(((RequestToParentForClassActivity) context),
						Constants.CHILD_ID)));
		nameValuePairs
				.add(new BasicNameValuePair("status", statusType+""));
		nameValuePairs
				.add(new BasicNameValuePair("prid", requestStatus.get(position).parent_request_id));
		nameValuePairs
				.add(new BasicNameValuePair("reply", msg));

		new SaveRequestAsync().execute(nameValuePairs);
	}

	private class ViewHolder
	{
		TextView title,description,created_by,created_date, status,comment;
		ImageView approve,reject;
	}

	private void showDialog(final int position){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		//alertDialogBuilder.setTitle("Give Reject Reason");

		// set dialog message
		alertDialogBuilder.setCancelable(false);

		View view = inflater.inflate(R.layout.parent_request_dialog, null);
		Button btnSubmit = (Button) view.findViewById(R.id.btn_submit);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

		edTxt = (EditText) view.findViewById(R.id.dialog_txt_remarks);

		alertDialogBuilder.setView(view);

		// create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edTxt.getText().toString().isEmpty() || edTxt.getText().toString().length() == 0) {
					Toast.makeText(context, "Please enter reason of rejection", Toast.LENGTH_LONG).show();
				} else {
					loadData(position, edTxt.getText().toString());
					alertDialog.cancel();

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

	private class SaveRequestAsync extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";
		String success = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog((RequestToParentForClassActivity) context);
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
			Log.d("url extension", Urls.api_save_request_parent);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_save_request_parent);

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
				notifyDataSetChanged();
				((RequestToParentForClassActivity)context).finish();
			} else {
				Toast.makeText(context,error,Toast.LENGTH_LONG).show();
			}
		}
	}
}