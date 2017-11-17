package com.knwedu.ourschool.adapter;

import java.io.File;
import java.util.ArrayList;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.DataStructureFramwork.PaymentHistory;
import com.knwedu.ourschool.WebViewActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DownloadTask;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class PaymentHistoryDetailsAdapter extends BaseAdapter {
	ViewHolder holder;
	private LayoutInflater inflater;
	Context context;
	private TextView header;
	private ArrayList<PaymentHistory> paymentHistories;
	public PaymentHistoryDetailsAdapter(Context context, ArrayList<PaymentHistory> paymentHistories)
	{
		this.context = context;
		this.paymentHistories = paymentHistories;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(this.paymentHistories != null)
		{
			return this.paymentHistories.size();
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
			convertView = inflater.inflate(R.layout.manage_fee_list_item, null);
			holder = new ViewHolder();
			holder.feetype 	= (TextView)convertView.findViewById(R.id.txtPaymentPeriod);
			holder.paymentDate 	= (TextView)convertView.findViewById(R.id.txtPaymentDate);
			holder.paymentAmount 	= (TextView)convertView.findViewById(R.id.txtPaymentAmount);
			holder.btnInvoice 	= (ImageView)convertView.findViewById(R.id.btnInvoice);
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		SchoolAppUtils.setFont(context, holder.feetype);
		SchoolAppUtils.setFont(context,holder.paymentDate);
		SchoolAppUtils.setFont(context, holder.paymentAmount );
		
		holder.feetype.setText(paymentHistories.get(position).fee_type);
		holder.paymentDate.setText(paymentHistories.get(position).payment_date);
		holder.paymentAmount.setText(paymentHistories.get(position).payment_amount);
		holder.btnInvoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showGetDocDialog(position);
			}
		});
		return convertView;
	}
	private class ViewHolder
	{
		TextView feetype, paymentDate, paymentAmount;
		ImageView btnInvoice;
	}

	private void showGetDocDialog(final int position) {
		final String filename = "receipt_"+paymentHistories.get(position).id+".pdf";
		new AlertDialog.Builder(context)
				.setTitle("Select option")
				.setPositiveButton("View Document",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								// continue with view
								Intent intent = new Intent(context, WebViewActivity.class);
								Log.d("url viewdoc>>>>>",Urls.api_get_payment_invoice_new + "/"
										+ paymentHistories.get(position).id + "/" + SchoolAppUtils.GetSharedParameter(context, Constants.UORGANIZATIONID) + File.separator+filename);

								intent.putExtra("url", Urls.api_get_payment_invoice_new + "/"
										+ paymentHistories.get(position).id + "/" + SchoolAppUtils.GetSharedParameter(context, Constants.UORGANIZATIONID) + File.separator+filename);
								context.startActivity(intent);
							}
						})
				.setNegativeButton("Download",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								// download only PDF files as report
								final DownloadTask downloadTask = new DownloadTask(context,filename);
								Log.d("url download>>>>>",Urls.api_get_payment_invoice_new+"/"
										+ paymentHistories.get(position).id+"/"+SchoolAppUtils.GetSharedParameter(context, Constants.UORGANIZATIONID));
								downloadTask.execute(Urls.api_get_payment_invoice_new+"/"
										+ paymentHistories.get(position).id+"/"+SchoolAppUtils.GetSharedParameter(context, Constants.UORGANIZATIONID));

							}
						}).setIcon(android.R.drawable.ic_dialog_info).show();
	}
}