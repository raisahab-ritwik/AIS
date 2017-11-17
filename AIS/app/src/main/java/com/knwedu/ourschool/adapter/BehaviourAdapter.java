package com.knwedu.ourschool.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.Behaviour;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.nostra13.universalimageloader.core.ImageLoader;


public class BehaviourAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private ArrayList<Behaviour> behaviour;
	public int []check;
	private String date,type;

	public BehaviourAdapter(Context context, ArrayList<Behaviour> behaviour)
	{
		this.context = context;
		this.behaviour = behaviour;
		check = new int[this.behaviour.size()];
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public BehaviourAdapter(Context context, ArrayList<Behaviour> behaviour,String type)
	{
		this.context = context;
		this.behaviour = behaviour;
		check = new int[this.behaviour.size()];
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.type = type;
	}
	@Override
	public int getCount() {
		if(this.behaviour != null)
		{
			return this.behaviour.size();
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
			if(type.equalsIgnoreCase("teacher")) {
				convertView = inflater.inflate(R.layout.behaviour_item_teacher, null);
			}else{
				convertView = inflater.inflate(R.layout.behaviour_item, null);
			}
			holder = new ViewHolder();
			holder.textViewStudentName 	= (TextView)convertView.findViewById(R.id.textViewStudentName);
			holder.textViewTitle 	= (TextView)convertView.findViewById(R.id.textViewTitle);
			holder.textViewGrade 	= (TextView)convertView.findViewById(R.id.textViewGrade);
			holder.textViewCreatedDate 	= (TextView)convertView.findViewById(R.id.textViewCreatedDate);
			holder.textViewCreatedBy 	= (TextView)convertView.findViewById(R.id.textViewCreatedBy);
			holder.textViewDesc 	= (TextView)convertView.findViewById(R.id.textViewDesc);
			holder.textViewStatus 	= (TextView)convertView.findViewById(R.id.textViewStatus);
			holder.imgCredit = (ImageView)convertView.findViewById(R.id.imgCredit);
			holder.titleLayout= (RelativeLayout)convertView.findViewById(R.id.layout_title);
			holder.gradeLayout= (RelativeLayout)convertView.findViewById(R.id.layout_grade);
			holder.layout_desc= (RelativeLayout)convertView.findViewById(R.id.layout_desc);
			holder.textViewGrade.setVisibility(View.GONE);
			holder.gradeLayout.setVisibility(View.VISIBLE);

			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		SchoolAppUtils.setFont(context, holder.textViewStudentName);
		SchoolAppUtils.setFont(context, holder.textViewTitle);
		SchoolAppUtils.setFont(context, holder.textViewGrade);
		SchoolAppUtils.setFont(context, holder.textViewCreatedDate);
		SchoolAppUtils.setFont(context, holder.textViewCreatedBy);
		SchoolAppUtils.setFont(context, holder.textViewDesc);
		SchoolAppUtils.setFont(context, holder.textViewStatus);
		
		holder.textViewStudentName.setText(behaviour.get(position).student_name);
		//---- Setting Card Name/Grade Name --- //
		if(behaviour.get(position).card_name.equalsIgnoreCase("null") && behaviour.get(position).grade_name.equalsIgnoreCase("null"))
		{
			holder.titleLayout.setVisibility(View.GONE);
		} else {
			holder.titleLayout.setVisibility(View.VISIBLE);
			if (behaviour.get(position).card_name.equalsIgnoreCase("null")) {
				holder.textViewTitle.setText(behaviour.get(position).grade_name);
			} else {
				holder.textViewTitle.setText(behaviour.get(position).card_name);
			}
		}

		//---- Setting GradePoint/ credit image --- //
		if(behaviour.get(position).grade_point.equalsIgnoreCase("null") && behaviour.get(position).file_name.equalsIgnoreCase("null"))
		{
			holder.imgCredit.setVisibility(View.GONE);
			holder.gradeLayout.setVisibility(View.GONE);
		}else{
			holder.gradeLayout.setVisibility(View.VISIBLE);
			if(behaviour.get(position).grade_point.equalsIgnoreCase("null")){
				holder.imgCredit.setVisibility(View.VISIBLE);
				String imgUrl = Urls.image_url_behaviour + behaviour.get(position).file_name;
				Log.d("imgUrl",imgUrl);
				new ImageLoadTask(imgUrl, holder.imgCredit).execute();
				holder.textViewGrade.setVisibility(View.GONE);
			}else{
				holder.textViewGrade.setVisibility(View.VISIBLE);
				holder.textViewGrade.setText(behaviour.get(position).grade_point);
				holder.imgCredit.setVisibility(View.GONE);
			}
		}
		holder.textViewCreatedDate.setText(behaviour.get(position).created_date);
		holder.textViewCreatedBy.setText(behaviour.get(position).created_by);

		//---- Setting Card Description/Grade Description --- //
		if(behaviour.get(position).card_description.equalsIgnoreCase("null") && behaviour.get(position).grade_description.equalsIgnoreCase("null")){
			if(behaviour.get(position).description.length() == 0){
				holder.layout_desc.setVisibility(View.GONE);
			}else{
				holder.layout_desc.setVisibility(View.VISIBLE);
				holder.textViewDesc.setText(behaviour.get(position).description);
			}

		} else {
			holder.layout_desc.setVisibility(View.VISIBLE);
			if (behaviour.get(position).card_description.length() == 0 || behaviour.get(position).card_description.equalsIgnoreCase("null")) {
				holder.textViewDesc.setText(behaviour.get(position).grade_description);
			} else if (behaviour.get(position).grade_description.length() == 0 || behaviour.get(position).grade_description.equalsIgnoreCase("null")) {
				holder.textViewDesc.setText(behaviour.get(position).card_description);
			}
		}
		if(type.equalsIgnoreCase("teacher")){
			if(behaviour.get(position).status.equalsIgnoreCase("Publish")){
				holder.textViewStatus.setTextColor(Color.GREEN);
			} else if(behaviour.get(position).status.equalsIgnoreCase("Rejected")){
				holder.textViewStatus.setTextColor(Color.RED);
			} else{
				holder.textViewStatus.setTextColor(Color.YELLOW);
			}
			holder.textViewStatus.setText(behaviour.get(position).status);
		}


		return convertView;
	}
	private class ViewHolder
	{
		RelativeLayout titleLayout,gradeLayout,layout_desc;
		TextView textViewStudentName, textViewTitle, textViewGrade, textViewCreatedDate,textViewCreatedBy,textViewDesc,textViewStatus;
		ImageView imgCredit;
	}

	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

		private String url;
		private ImageView imageView;

		public ImageLoadTask(String url, ImageView imageView) {
			this.url = url;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				URL urlConnection = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
		}

	}
}