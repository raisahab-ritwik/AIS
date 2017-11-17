package com.knwedu.ourschool.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.ParentChildLeaveActivity;
import com.knwedu.ourschool.ParentChildRequestActivity;
import com.knwedu.ourschool.ParentRequestStatusListActivity;
import com.knwedu.ourschool.adapter.RequestAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class ParentRequestFragment extends Fragment {
	private View view;
	private Button leave, uniform, books, card, special, showRequestStatus;
	private ProgressDialog dialog;
	private ListView listRequest;
	private ArrayList<String> request;
	private RequestAdapter mAdapter;
	private TextView textnodata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_parent_request, container,
				false);
		SchoolAppUtils.loadAppLogo(getActivity(),
				(ImageButton) view.findViewById(R.id.app_logo));
		initialize();
		return view;
	}

	private void initialize() {
		/*
		 * leave = (Button) view.findViewById(R.id.request_leave_btn); uniform =
		 * (Button) view.findViewById(R.id.request_uniform_btn); books =
		 * (Button) view.findViewById(R.id.request_books_btn); card = (Button)
		 * view.findViewById(R.id.request_student_card_btn); special = (Button)
		 * view.findViewById(R.id.request_special_btn);
		 */

		textnodata=(TextView) view.findViewById(R.id.textnodata) ;
		showRequestStatus = (Button) view.findViewById(R.id.request_status_btn);
		request = new ArrayList<String>();
		if (!SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.LEAVE_REQUEST)
				.toString().equalsIgnoreCase("0")) {
			request.add(SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.LEAVE_REQUEST).toString());
		}
		if (!SchoolAppUtils
				.GetSharedParameter(getActivity(),
						Constants.REQUEST_FOR_ID_CARD).toString()
				.equalsIgnoreCase("0")) {
			request.add(SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.REQUEST_FOR_ID_CARD).toString());
		}
		if (!SchoolAppUtils
				.GetSharedParameter(getActivity(),
						Constants.REQUEST_FOR_UNIFORM).toString()
				.equalsIgnoreCase("0")) {
			request.add(SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.REQUEST_FOR_UNIFORM).toString());
		}
		if (!SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.REQUEST_FOR_BOOK)
				.toString().equalsIgnoreCase("0")) {
			request.add(SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.REQUEST_FOR_BOOK).toString());
		}
		if (!SchoolAppUtils
				.GetSharedParameter(getActivity(), Constants.SPECIAL_REQUEST)
				.toString().equalsIgnoreCase("0")) {
			request.add(SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.SPECIAL_REQUEST).toString());
		}
		mAdapter = new RequestAdapter(getActivity(), request);
		listRequest = (ListView) view.findViewById(R.id.listRequest);
		listRequest.setAdapter(mAdapter);
		listRequest.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent i = null;
				if (position == 0) {
					i = new Intent(getActivity(),
							ParentChildLeaveActivity.class);
					i.putExtra("request_id", "1");
					i.putExtra(
							Constants.PAGE_TITLE,
							SchoolAppUtils.GetSharedParameter(getActivity(),
									Constants.LEAVE_REQUEST).toString());
				} else if (position == 1) {
					i = new Intent(getActivity(),
							ParentChildRequestActivity.class);
					i.putExtra("request_id", "2");
					i.putExtra(
							Constants.PAGE_TITLE,
							SchoolAppUtils.GetSharedParameter(getActivity(),
									Constants.REQUEST_FOR_ID_CARD).toString());
				} else if (position == 2) {
					i = new Intent(getActivity(),
							ParentChildRequestActivity.class);
					i.putExtra("request_id", "3");
					i.putExtra(
							Constants.PAGE_TITLE,
							SchoolAppUtils.GetSharedParameter(getActivity(),
									Constants.REQUEST_FOR_UNIFORM).toString());
				} else if (position == 3) {
					i = new Intent(getActivity(),
							ParentChildRequestActivity.class);
					i.putExtra("request_id", "4");
					i.putExtra(
							Constants.PAGE_TITLE,
							SchoolAppUtils.GetSharedParameter(getActivity(),
									Constants.REQUEST_FOR_BOOK).toString());
				} else if (position == 4) {
					i = new Intent(getActivity(),
							ParentChildRequestActivity.class);
					i.putExtra("request_id", "5");
					i.putExtra(
							Constants.PAGE_TITLE,
							SchoolAppUtils.GetSharedParameter(getActivity(),
									Constants.SPECIAL_REQUEST).toString());
				}
				startActivity(i);

			}

		});
		/*
		 * leave.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { startActivity(new
		 * Intent(getActivity(), ParentChildLeaveActivity.class)); } });
		 * card.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(getActivity(), ParentChildRequestActivity.class);
		 * intent.putExtra("type", 2); startActivity(intent); } });
		 * 
		 * uniform.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(getActivity(), ParentChildRequestActivity.class);
		 * intent.putExtra("type", 3); startActivity(intent);
		 * 
		 * } });
		 * 
		 * books.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(getActivity(), ParentChildRequestActivity.class);
		 * intent.putExtra("type", 4); startActivity(intent); } });
		 * 
		 * special.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(getActivity(), ParentChildRequestActivity.class);
		 * intent.putExtra("type", 5); startActivity(intent); } });
		 */
		showRequestStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ParentRequestStatusListActivity.class);
				startActivity(intent);
			}
		});
		//-----------------for insurance-----------------------
		ImageView adImageView = (ImageView)view.findViewById(R.id.adImageView);

		if(Integer.parseInt(SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.USERTYPEID))== 5 && (SchoolAppUtils.GetSharedParameter(getActivity(),Constants.INSURANCE_AVIALABLE).equalsIgnoreCase("1")))
		{
			adImageView.setVisibility(View.VISIBLE);
		}
		else{
			adImageView.setVisibility(View.GONE);
		}

		adImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AD_URL));
//				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				browserIntent.setPackage("com.android.chrome");
//				try {
//					startActivity(browserIntent);
//				} catch (ActivityNotFoundException ex) {
//					// Chrome browser presumably not installed so allow user to choose instead
//					browserIntent.setPackage(null);
//					startActivity(browserIntent);
//				}
				startActivity(new Intent(getActivity(),
						AdvertisementWebViewActivity.class));
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}
}
