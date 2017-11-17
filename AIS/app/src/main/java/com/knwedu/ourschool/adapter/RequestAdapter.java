package com.knwedu.ourschool.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class RequestAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<String> requests;
	public int[] check;

	public RequestAdapter(Context context, ArrayList<String> requests) {
		this.context = context;
		this.requests = requests;
		check = new int[this.requests.size()];
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.college_custom_request_text, null);
			holder = new ViewHolder();
			holder.requestText = (TextView) convertView
					.findViewById(R.id.textRequest);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (SchoolAppUtils.GetSharedParameter(context, Constants.LEAVE_REQUEST)
				.toString().equalsIgnoreCase(requests.get(position))) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_leave, 0, 0, 0);

		}
		if (SchoolAppUtils
					.GetSharedParameter(context, Constants.REQUEST_FOR_BOOK)
					.toString().equalsIgnoreCase(requests.get(position))) {
				holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.request_book, 0, 0, 0);

		}
		if (SchoolAppUtils
				.GetSharedParameter(context, Constants.REQUEST_FOR_ID_CARD)
				.toString().equalsIgnoreCase(requests.get(position))) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_idcard, 0, 0, 0);

		}
		if (SchoolAppUtils
				.GetSharedParameter(context, Constants.REQUEST_FOR_UNIFORM)
				.toString().equalsIgnoreCase(requests.get(position))) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_uniform, 0, 0, 0);

		}

		if (SchoolAppUtils
				.GetSharedParameter(context, Constants.SPECIAL_REQUEST)
				.toString().equalsIgnoreCase(requests.get(position))) {
			holder.requestText.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.request_special, 0, 0, 0);

		}
		holder.requestText.setText(requests.get(position));
		return convertView;
	}

	private class ViewHolder {
		TextView requestText;
	}
}
