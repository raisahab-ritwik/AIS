package com.knwedu.ourschool.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ListView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.ParentChildLeaveActivity;
import com.knwedu.ourschool.ParentChildRequestActivity;
import com.knwedu.ourschool.ParentRequestStatusListActivity;
import com.knwedu.ourschool.RequestToParentForClassActivity;
import com.knwedu.ourschool.adapter.RequestAdapter;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import java.util.ArrayList;

public class RequestToParentFragment extends Fragment {
	private View view;
	private Button btnClass, btnMe;
	private ProgressDialog dialog;
	private ListView listRequest;
	private ArrayList<String> request;
	private RequestAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_request_to_parent, container,
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
		btnClass = (Button) view.findViewById(R.id.request_class);
	btnMe = (Button) view.findViewById(R.id.request_me);


		btnClass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						RequestToParentForClassActivity.class);
				intent.putExtra("type",1);
				startActivity(intent);
			}
		});

		btnMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						RequestToParentForClassActivity.class);
				intent.putExtra("type",2);
				startActivity(intent);
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
