package com.knwedu.college.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.college.utils.CollegeDataStructureFramwork.RequestsStatus;
import com.knwedu.comschoolapp.R;


public class CollegeRequestAdapter extends BaseAdapter
{
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<RequestsStatus> requests;
	public int []check;
	String label;

	public CollegeRequestAdapter(Context context,
			ArrayList<RequestsStatus> requests) 
	{
		this.context = context;
		this.requests = requests;
		check = new int[this.requests.size()];
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return requests.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) 
		{
			convertView = inflater.inflate(R.layout.college_custom_request_text, null);
			holder = new ViewHolder();
			holder.requestText = (TextView) convertView.findViewById(R.id.textRequest);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		label = requests.get(position).reason_title;
		if (label.equals("Leave Request")) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_leave, 0, R.drawable.arrow_right, 0);

		}
		else if (label.equals("Books Request")) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_book, 0, R.drawable.arrow_right, 0);

		}
		else if (label.equals("ID Request")) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_idcard, 0, R.drawable.arrow_right, 0);

		}

		else if (label.equals("Special Requeast")) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_special, 0, R.drawable.arrow_right, 0);

		}
		else{
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_special, 0, R.drawable.arrow_right, 0);

		}





		
		holder.requestText.setText(requests.get(position).reason_title);
		return convertView;
	}
	private class ViewHolder
	{
		TextView requestText;
	}
}
