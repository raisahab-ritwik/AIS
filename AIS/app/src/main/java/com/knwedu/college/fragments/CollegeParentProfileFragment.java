package com.knwedu.college.fragments;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.college.CollegeChangePasswordActivity;
import com.knwedu.college.CollegeParentClassListActivity;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.ParentProfileInfo;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollegeParentProfileFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;

	// private ImageLoader imageTemp = ImageLoader.getInstance();
	// private DisplayImageOptions options;

	//private ImageView userImage;
	private TextView userid, name, email, phone, address, religion,
			blood_group, pincode, alternate_email;
	private DisplayImageOptions options;
	CircleImageView profileImage;
	File cacheDir;
	private String page_title = "";
	private Button changePassBtn, nameProfile, myClassSection;
	ParentProfileInfo userInfo;
	private Bitmap mIcon11;
	CollegeDBAdapter mDatabase;
	ImageLoaderConfiguration config;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_parent_profile,
				container, false);

		myClassSection = (Button) view.findViewById(R.id.btn_class_section);
		if(!SchoolAppUtils.GetSharedParameter(getActivity(), Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)){
			myClassSection.setVisibility(View.INVISIBLE);
		}
		getData();
		return view;
	}


	private void bindData(Cursor cursor_parent ){

		if(cursor_parent.getCount()>0) {
			cursor_parent.moveToFirst();

			userid.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_USER_CODE)));
			name.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_FULLNAME)));
			nameProfile.setText("  " + cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_FULLNAME)));
			email.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_EMAIL)));
			phone.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_MOBILE_NO)));
			address.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_ADDRESS)));
			religion.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_RELIGION)));
			alternate_email.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_ALT_EMAIL)));
			pincode.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_PINCODE)));
			blood_group.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_BLOOD_GROUP)));

			String imgUrl = cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_IMAGE_PATH))
					+"/" + cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_IMAGE));
			ImageLoader.getInstance()
					.displayImage(imgUrl, profileImage, options);


		}


	}

	@Override
	public void onResume() {
		super.onResume();
		if (CollegeAppUtils.GetSharedBoolParameter(getActivity(), "update")) {
			CollegeAppUtils.SetSharedBoolParameter(getActivity(), "update",
					false);
			initialize();
		}
	}

	private void getData() {
//		ImageView adImageView = (ImageView)view.findViewById(R.id.adImageView);
//		if(Integer.parseInt(SchoolAppUtils.GetSharedParameter(getActivity(),
//				Constants.USERTYPEID)) == 5){
//			adImageView.setVisibility(View.VISIBLE);
//
//		}else{
//			adImageView.setVisibility(View.GONE);
//		}
//		adImageView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				startActivity(new Intent(getActivity(),
//						AdvertisementWebViewActivity.class));
//			}
//		});

		page_title = getActivity().getTitle().toString();
		profileImage = (CircleImageView) view.findViewById(R.id.image_vieww);
		userid = (TextView) view.findViewById(R.id.userid_txt);
		name = (TextView) view.findViewById(R.id.name_txt);
		nameProfile = (Button) view.findViewById(R.id.name_txt_profile);
		email = (TextView) view.findViewById(R.id.email_txt);
		phone = (TextView) view.findViewById(R.id.phone_txt);
		address = (TextView) view.findViewById(R.id.address_txt);
		religion = (TextView) view.findViewById(R.id.religion_txt);
		pincode = (TextView) view.findViewById(R.id.pin_code_txt);
		blood_group = (TextView) view.findViewById(R.id.blood_group_txt);
		alternate_email = (TextView) view.findViewById(R.id.alt_email_txt);
		changePassBtn = (Button) view.findViewById(R.id.change_password_btn);
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getCollegeDatabase();
		cacheDir = StorageUtils.getCacheDirectory(getContext());
		config = new ImageLoaderConfiguration.Builder(getContext()).memoryCacheExtraOptions(480, 800) // default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
				.threadPriority(Thread.NORM_PRIORITY - 2) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13) // default
				.diskCache(new UnlimitedDiskCache(cacheDir)) // default
				.diskCacheFileCount(1000)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(getContext())) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);


		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.damy)
				.showImageForEmptyUri(R.drawable.damy)
				.showImageOnFail(R.drawable.damy)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		if (CollegeAppUtils.isOnline(getActivity())) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new GetProfileAsyntask().execute(nameValuePairs);
		}
		else{
			mDatabase.open();
			Cursor parent_cursor = mDatabase.getParentProfileInfo();
			bindData( parent_cursor);
			mDatabase.close();
		}

	}

	private void initialize() {



		if (userInfo != null) {
			userid.setText(userInfo.user_code);
			name.setText(userInfo.fullname);
			nameProfile.setText("  " + userInfo.fullname);
			email.setText(userInfo.email);
			phone.setText(userInfo.mobile_no);
			address.setText(userInfo.address);
			religion.setText(userInfo.religion);
			alternate_email.setText(userInfo.alt_email);
			pincode.setText(userInfo.pincode);
			blood_group.setText(userInfo.blood_group);
			/*
			 * new CollegeLoadImageAsyncTask(getActivity(), userImage,
			 * CollegeUrls.image_url_userpic, userInfo.image, true) .execute();
			 */
			/*if (userInfo.image != null) {
				new CollegeLoadImageAsyncTask(getActivity(), userImage,
						userInfo.image_path + "/", userInfo.image, true)
						.execute();
			}*/


			String imgUrl = userInfo.image_path +"/" + userInfo.image;
			ImageLoader.getInstance()
					.displayImage(imgUrl, profileImage, options);
		}

		changePassBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						CollegeChangePasswordActivity.class);
				intent.putExtra(CollegeConstants.IMAGE_URL, userInfo.image);
				intent.putExtra(CollegeConstants.FULL_NAME, userInfo.fullname);
				getActivity().startActivity(intent);

			}
		});

		nameProfile.setOnClickListener(null);
		myClassSection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						CollegeParentClassListActivity.class);
				getActivity().startActivity(intent);
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
		// getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onStart() {

		super.onStart();
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			initialize();
		}
	};

	private class GetProfileAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {

		String error;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.profile));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {

			List<NameValuePair> nameValuePairs = params[0];
			CollegeJsonParser jParser = new CollegeJsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					CollegeUrls.api_profile_info);

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_profile_info);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						mDatabase.open();
						JSONObject object = json.getJSONObject("info");
						if (object != null) {
							userInfo = new ParentProfileInfo(object);
							mDatabase.deleteAllParentProfile();
							mDatabase.addParentProfile(userInfo);
						}
						mDatabase.close();
						// /-----------------------for image----------------//
						if (userInfo.image.length() > 0) {
							mIcon11 = null;
							try {
								InputStream in = new java.net.URL(
										CollegeUrls.image_url_userpic
												+ userInfo.image).openStream();
								mIcon11 = BitmapFactory.decodeStream(in);
							} catch (Exception e) {
								Log.e("Error", e.getMessage());
								e.printStackTrace();
							}
						}
						// ---------------------------------------------------//
						return true;
					} else {
						try {
							error = json.getString("info");
						} catch (Exception e) {
						}
						return false;
					}
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

				if (userInfo != null) {

					initialize();

				} else {
					CollegeAppUtils
							.showDialog(getActivity(), CollegeAppUtils
									.GetSharedParameter(getActivity(),
											page_title), getResources()
									.getString(R.string.please_try_again));
				}

			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(getActivity(), page_title,
								error);
					} else {
						CollegeAppUtils
								.showDialog(
										getActivity(),
										page_title,

										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							getActivity(),
							page_title,

							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}
	}
}
