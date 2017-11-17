package com.knwedu.ourschool.fragments;

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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AddChildActivity;
import com.knwedu.ourschool.AdvertisementWebViewActivity;
import com.knwedu.ourschool.AimsAddChildActivity;
import com.knwedu.ourschool.ChangePasswordActivity;
import com.knwedu.ourschool.MainActivity;
import com.knwedu.ourschool.ParentClassListActivity;
import com.knwedu.ourschool.ParentMainActivity;
import com.knwedu.ourschool.PrivacyPolicyActivity;
import com.knwedu.ourschool.SigninActivity;
import com.knwedu.ourschool.SplashActivity;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ParentProfileInfo;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.LoadImageAsyncTask;
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

public class ParentProfileFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;


	//private ImageView userImage;
	private TextView userid, name, email, phone, address, religion,
			blood_group, dob, pincode, alternate_email;
	private TextView address_title_txt, alt_email_title_txt, religion_title_txt,blood_group_title_txt,
			pin_code_title, dob_title;


	private DisplayImageOptions options;
	CircleImageView profileImage;
	File cacheDir;

	private Button changePassBtn, nameProfile, myClassSection,btn_add_child;
	ParentProfileInfo userInfo;
	private Bitmap mIcon11;
	DatabaseAdapter mDatabase;
	ImageLoaderConfiguration config;
	String appType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_parent_profile, container,
				false);

		myClassSection = (Button) view.findViewById(R.id.btn_class_section);
		appType = SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.APP_TYPE);

		if(!SchoolAppUtils.GetSharedParameter(getActivity(), Constants.APP_TYPE).equals(Constants.APP_TYPE_COMMON_STANDARD)){
			myClassSection.setVisibility(View.INVISIBLE);
		}
		getData();
		return view;
	}

	private void bindData(Cursor cursor_parent){
		String test ="test";
		int count = cursor_parent.getCount();
		if(cursor_parent.getCount() > 0) {
			cursor_parent.moveToFirst();

			userid.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_USER_CODE)));
			name.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_FULLNAME)));
			nameProfile.setText("  " + cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_FULLNAME)));
			email.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_EMAIL)));
			phone.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_MOBILE_NO)));
			address.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_ADDRESS)));
			religion.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_RELIGION)));
			alternate_email.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_ALT_EMAIL)));
			pincode.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_PINCODE)));
			blood_group.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_BLOOD_GROUP)));
			dob.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_DOB)));
			String imgUrl = Urls.image_url_userpic +"/" + cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_IMAGE));
			ImageLoader.getInstance()
					.displayImage(imgUrl, profileImage, options);

		}
		else{

			SchoolAppUtils.showDialog(getActivity(), getActivity()
					.getTitle().toString(), "Please try with better internet.");

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (SchoolAppUtils.GetSharedBoolParameter(getActivity(), "update")) {
			SchoolAppUtils.SetSharedBoolParameter(getActivity(), "update",
					false);
			initialize();
		}
	}

	private void getData() {
		/*
		 * String url = URLs.GetProfile + "email=" +
		 * SchoolAppUtils.GetSharedParameter(getActivity(), Constants.PUSERNAME)
		 * + "&password=" + SchoolAppUtils.GetSharedParameter(getActivity(),
		 * Constants.PPASSWORD);
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
		dob = (TextView) view.findViewById(R.id.dob_txt);
		alternate_email = (TextView) view.findViewById(R.id.alt_email_txt);
		changePassBtn = (Button) view.findViewById(R.id.change_password_btn);
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getDatabase();
		btn_add_child = (Button)view.findViewById(R.id.btn_add_child);

		address_title_txt = (TextView)view.findViewById(R.id.address_title_txt);
		alt_email_title_txt  = (TextView)view.findViewById(R.id.alt_email_title_txt);
		religion_title_txt  = (TextView)view.findViewById(R.id.religion_title_txt);
		blood_group_title_txt  = (TextView)view.findViewById(R.id.blood_group_title_txt);
		pin_code_title  = (TextView)view.findViewById(R.id.pin_code_title);
		dob_title  = (TextView)view.findViewById(R.id.dob_title);




		if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
			address_title_txt.setVisibility(View.GONE);
			alt_email_title_txt.setVisibility(View.GONE);
			religion_title_txt.setVisibility(View.GONE);
			blood_group_title_txt.setVisibility(View.GONE);
			pin_code_title.setVisibility(View.GONE);
			dob_title.setVisibility(View.GONE);

			address.setVisibility(View.GONE);
			alternate_email.setVisibility(View.GONE);
			religion.setVisibility(View.GONE);
			blood_group.setVisibility(View.GONE);
			pincode.setVisibility(View.GONE);
			dob.setVisibility(View.GONE);


		}


			btn_add_child.setVisibility(View.VISIBLE);
			btn_add_child.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {


					if (SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.APP_TYPE).equals(
							Constants.APP_TYPE_COMMON_STANDARD)) {
					Intent intent = new Intent(getActivity(),
							AddChildActivity.class);
					//intent.putExtra(Constants.IMAGE_URL, userInfo.image);
					//intent.putExtra(Constants.FULL_NAME, userInfo.fullname);
					getActivity().startActivity(intent);
					}else{
						Intent intent = new Intent(getActivity(),
								AimsAddChildActivity.class);
						getActivity().startActivity(intent);



					}
				}
			});




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



		if (SchoolAppUtils.isOnline(getActivity())) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("user_type_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id",
					SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.UORGANIZATIONID)));

			new GetProfileAsyntask().execute(nameValuePairs);
		}else{
			mDatabase.open();
			Cursor parent_cursor = mDatabase.getParentProfileInfo();
			bindData(parent_cursor);
			mDatabase.close();
		}

	}

	private void initialize() {


		/*
		 * options = new DisplayImageOptions.Builder()
		 * .showImageForEmptyUri(R.drawable.no_photo)
		 * .showImageOnFail(R.drawable.no_photo)
		 * .showStubImage(R.drawable.no_photo).cacheInMemory()
		 * .cacheOnDisc().build();
		 */

		/*
		 * JSONObject object = null; try { object = new
		 * JSONObject(SchoolAppUtils.GetSharedParameter( getActivity(),
		 * Constants.PUSERINFO)); } catch (Exception e) {
		 * 
		 * } if (object != null) { parentProfileInfo = new
		 * ParentProfileInfo(object); }
		 */
		if (userInfo != null) {
			userid.setText(userInfo.email + " / "+ userInfo.mobile_no);
			name.setText(userInfo.fname + " " + userInfo.mname + " " + userInfo.lname);
			nameProfile.setText("  " + userInfo.fname + " " + userInfo.mname + " " + userInfo.lname);

			email.setText(userInfo.email);
			phone.setText(userInfo.mobile_no);
			if(userInfo.address.equals("null")){
				address.setText("");
			}else {
				address.setText(userInfo.address);
			}
			if(userInfo.religion.equals("null")){
				religion.setText("");
			}else {
				religion.setText(userInfo.religion);
			}
			if(userInfo.alt_email.equals("null")){
				alternate_email.setText("");
			}else {
				alternate_email.setText(userInfo.alt_email);
			}
			if(userInfo.pincode.equals("null")){
				pincode.setText("");
			}else {
				pincode.setText(userInfo.pincode);
			}
			if(userInfo.blood_group.equals("null")) {
				blood_group.setText("");
			}else{
				blood_group.setText(userInfo.blood_group);
			}
			if(userInfo.dob.equals("null")){
				dob.setText("");
			}else{
				dob.setText(userInfo.dob);
			}

			/*
			new LoadImageAsyncTask(getActivity(), userImage,
					Urls.image_url_userpic, userInfo.image, false).execute();*/

			String imgUrl = Urls.image_url_userpic +"/" + userInfo.image;
			ImageLoader.getInstance()
					.displayImage(imgUrl, profileImage, options);
			

		}

		changePassBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ChangePasswordActivity.class);
				intent.putExtra(Constants.IMAGE_URL, userInfo.image);
				intent.putExtra(Constants.FULL_NAME, userInfo.fullname);
				getActivity().startActivity(intent);
			}
		});

		nameProfile.setOnClickListener(null);
		myClassSection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ParentClassListActivity.class);
				getActivity().startActivity(intent);
			}
		});
//		ImageView adImageView = (ImageView)view.findViewById(R.id.adImageView);
//		adImageView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AD_URL));
////				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				browserIntent.setPackage("com.android.chrome");
////				try {
////					startActivity(browserIntent);
////				} catch (ActivityNotFoundException ex) {
////					// Chrome browser presumably not installed so allow user to choose instead
////					browserIntent.setPackage(null);
////					startActivity(browserIntent);
////				}
//
//				startActivity(new Intent(getActivity(),
//						AdvertisementWebViewActivity.class));
//			}
//		});
//	}
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
		// getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onStart() {
		/*
		 * getActivity().registerReceiver(receiver, new
		 * IntentFilter(Constants.UPDATEPROFILEBROADCAST));
		 */super.onStart();
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
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs,
					Urls.api_profile_info);

			// Log parameters:
			Log.d("url extension", Urls.api_profile_info);
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
							mDatabase.close();
						}
						mDatabase.close();
						// /-----------------------for image----------------//
						if (userInfo.image.length() > 0) {
							mIcon11 = null;
							try {
								InputStream in = new java.net.URL(
										Urls.image_url_userpic + userInfo.image)
										.openStream();
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
				}else {
					error = getResources().getString(R.string.unknown_response);
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
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), getResources()
							.getString(R.string.please_try_again));
				}

			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), error);
				}else{
					SchoolAppUtils.showDialog(getActivity(), getActivity()
							.getTitle().toString(), getResources().getString(R.string.error));
				}
			}
		}
	}


}
