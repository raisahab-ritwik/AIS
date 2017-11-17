package com.knwedu.college.fragments;

import java.io.File;
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

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.inmobi.monetization.IMBanner;
import com.knwedu.college.CollegeChangePasswordActivity;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.TeacherProfileInfo;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;
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

public class CollegeTeacherProfileFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;

	//private ImageView userImage;
	private TextView userid, name, email, phone, address, alternate_email,
			religion, pincode, blood_group;

	private DisplayImageOptions options;
	CircleImageView profileImage;
	File cacheDir;

	private Button changePassBtn, showClasses, nameProfile;
	private TeacherProfileInfo userInfo;
	private String pageTitle = "";
	private CollegeDBAdapter mDatabase;
	ImageLoaderConfiguration config;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_teacher_profile, container,
				false);
		getData();
		return view;
	}
	private void bindData(Cursor teacher_cursor){

		if(teacher_cursor.getCount()>0){
			teacher_cursor.moveToFirst();

			userid.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_USER_CODE)));
			name.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_FULLNAME)));
			nameProfile.setText("  " + teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_FULLNAME)));
			email.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_EMAIL)));
			phone.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_MOBILE_NO)));
			address.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_ADDRESS)));
			religion.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_RELIGION)));
			alternate_email.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_ALT_EMAIL)));
			pincode.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_PINCODE)));
			blood_group.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_BLOODGROUP)));
			String imgUrl = teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_IMAGE_PATH))
					+"/" + teacher_cursor.getString(teacher_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_TEACHER_ROW_IMAGE));

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
		pageTitle = getActivity().getTitle().toString();
		/*
		 * String url = URLs.GetProfile + "email=" +
		 * SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERNAME)
		 * + "&password=" + SchoolAppUtils.GetSharedParameter(getActivity(),
		 * Constants.PASSWORD); Log.d("Url", url);
		 */


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
		showClasses = (Button) view.findViewById(R.id.class_subjects_btn);
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
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new GetProfileAsyntask().execute(nameValuePairs);
		}else{
			mDatabase.open();
			Cursor teacher_cursor = mDatabase.getTeacherProfileInfo();
			bindData(teacher_cursor);
			mDatabase.close();
		}

	}

	private void initialize() {
		InMobi.initialize(getActivity(),
				getResources().getString(R.string.InMobi_Property_Id));
		InMobi.setLogLevel(LOG_LEVEL.DEBUG);
		IMBanner banner = (IMBanner) getActivity().findViewById(R.id.banner);
		banner.loadBanner();


		/*
		 * JSONObject object = null; try { object = new
		 * JSONObject(SchoolAppUtils.GetSharedParameter( getActivity(),
		 * Constants.USERINFO)); } catch (Exception e) {
		 * 
		 * } if (object != null) { userInfo = new UserInfo(object); }
		 */

		if (userInfo != null) {
			/*
			 * if (userInfo.thumb != null) { imageTemp.displayImage(
			 * URLs.GetImage + userInfo.thumb.replace("..", ""), userImage,
			 * options); }
			 */
			userid.setText(userInfo.user_code);
			name.setText(userInfo.fullname);
			nameProfile.setText("  " + userInfo.fullname);
			email.setText(userInfo.email);
			phone.setText(userInfo.mobile_no);
			address.setText(userInfo.address);
			religion.setText(userInfo.religion);
			alternate_email.setText(userInfo.alt_email);
			pincode.setText(userInfo.pincode);
			blood_group.setText(userInfo.bloodgroup);

			/*if(userInfo.image!=null)
			{
			new CollegeLoadImageAsyncTask(getActivity(), userImage,
					userInfo.image_path + "/",
					userInfo.image, true).execute();
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
		showClasses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * getActivity().startActivity( new Intent(getActivity(),
				 * TeacherClassesSubjectsListActivity.class));
				 */
			}
		});

		nameProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				 * Intent intent = new Intent(getActivity(),
				 * TeacherEditProfileActivity.class);
				 * intent.putExtra(Constants.IMAGE_URL, userInfo.image);
				 * intent.putExtra(Constants.TEACHER_INFO,
				 * userInfo.object.toString());
				 * getActivity().startActivity(intent);
				 */

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
		// getActivity().registerReceiver(receiver,
		// new IntentFilter(Constants.UPDATEPROFILEBROADCAST));
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
					CollegeUrls.api_teacher_profile_info);

			// Log parameters:
			Log.d("url extension", CollegeUrls.api_teacher_profile_info);
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
							userInfo = new TeacherProfileInfo(object);
							mDatabase.deleteAllTeacherProfile();
							mDatabase.addTeacherProfile(userInfo);

						}
						mDatabase.close();

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
							.showDialog(
									getActivity(),
									pageTitle,
									getResources().getString(
											R.string.please_try_again));
				}
			} else {
				if (error != null) {
					if (error.length() > 0) {
						CollegeAppUtils.showDialog(getActivity(), pageTitle,
								error);
					} else {
						CollegeAppUtils
								.showDialog(
										getActivity(),
										pageTitle,
										getResources()
												.getString(
														R.string.please_check_internet_connection));
					}
				} else {
					CollegeAppUtils.showDialog(
							getActivity(),
							pageTitle,
							getResources().getString(
									R.string.please_check_internet_connection));
				}
			}
		}
	}
}
