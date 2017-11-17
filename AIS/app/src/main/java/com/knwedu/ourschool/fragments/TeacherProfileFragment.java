package com.knwedu.ourschool.fragments;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.inmobi.monetization.IMBanner;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AimsTeacherAddSubjectActivity;
import com.knwedu.ourschool.ChangePasswordActivity;
import com.knwedu.ourschool.TeacherAddSubject;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.TeacherProfileInfo;
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

public class TeacherProfileFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;
	String appType;
	//private ImageView userImage;
	private TextView userid, name, email, phone, address, alternate_email,
			religion, pincode, blood_group, dob, educational_qualification, other_qualification, date_of_joining;
	private DisplayImageOptions options;
	CircleImageView profileImage;
	File cacheDir;
	private TextView address_title_txt, alt_email_title_txt, religion_title_txt, blood_group_title_txt,
			pin_code_title, dob_title, eduQualification_title, otherQualification_title, doj_title;

	private Button changePassBtn, showClasses, nameProfile,btn_add_sub;
	private TeacherProfileInfo userInfo;
	DatabaseAdapter mDatabase;
	ImageLoaderConfiguration config;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_teacher_profile, container,
				false);
		SchoolAppUtils.loadAppLogo(getActivity(), (ImageButton) view.findViewById(R.id.app_logo));
		appType = SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.APP_TYPE);
		getData();
		return view;
	}

	private void bindData(Cursor teacher_cursor) {
if(teacher_cursor.getCount()>0) {
	teacher_cursor.moveToFirst();
	userid.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_USER_CODE)));
	name.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_FULLNAME)));
	nameProfile.setText("  " + teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_FULLNAME)));
	email.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_EMAIL)));
	phone.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_MOBILE_NO)));
	address.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_ADDRESS)));
	religion.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_RELIGION)));
	alternate_email.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_ALT_EMAIL)));
	pincode.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_PINCODE)));
	blood_group.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_BLOODGROUP)));
	dob.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_DOB)));
	educational_qualification.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_EDUQUALIFICATION)));
    other_qualification.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_OTHERQUALIFICATION)));
	date_of_joining.setText(teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_DATEOFJOIN)));

	String imgUrl = Urls.image_url_userpic +"/" + teacher_cursor.getString(teacher_cursor.getColumnIndex(DatabaseAdapter.KEY_ROW_PRTEACHER_IMAGE));
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
		dob = (TextView) view.findViewById(R.id.dob_txt);
		educational_qualification=(TextView) view.findViewById(R.id.eduQualification_txt);
		other_qualification=(TextView)view.findViewById(R.id.otherQualification_txt);
		date_of_joining= (TextView) view.findViewById(R.id.doj_txt);
		btn_add_sub = (Button) view.findViewById(R.id.btn_add_sub);


		address_title_txt = (TextView) view.findViewById(R.id.address_title_txt);
		alt_email_title_txt  = (TextView) view.findViewById(R.id.alt_email_title_txt);
		religion_title_txt = (TextView) view.findViewById(R.id.religion_title_txt);
		blood_group_title_txt = (TextView) view.findViewById(R.id.blood_group_title_txt);
		pin_code_title = (TextView) view.findViewById(R.id.pin_code_title);
		dob_title = (TextView) view.findViewById(R.id.dob_title);
		eduQualification_title = (TextView) view.findViewById(R.id.eduQualification_title);
		otherQualification_title = (TextView) view.findViewById(R.id.otherQualification_title);
		doj_title = (TextView) view.findViewById(R.id.doj_title);

		if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
			address_title_txt.setVisibility(View.GONE);
			address.setVisibility(View.GONE);
			alt_email_title_txt.setVisibility(View.GONE);
			alternate_email.setVisibility(View.GONE);
			religion_title_txt.setVisibility(View.GONE);
			religion.setVisibility(View.GONE);
			blood_group_title_txt.setVisibility(View.GONE);
			blood_group.setVisibility(View.GONE);
			pin_code_title.setVisibility(View.GONE);
			pincode.setVisibility(View.GONE);
			dob_title.setVisibility(View.GONE);
			dob.setVisibility(View.GONE);
			eduQualification_title.setVisibility(View.GONE);
			educational_qualification.setVisibility(View.GONE);
			otherQualification_title.setVisibility(View.GONE);
			other_qualification.setVisibility(View.GONE);
			doj_title.setVisibility(View.GONE);
			date_of_joining.setVisibility(View.GONE);

		}


			btn_add_sub.setVisibility(View.VISIBLE);
			btn_add_sub.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (SchoolAppUtils.GetSharedParameter(getActivity(),
							Constants.APP_TYPE).equals(
							Constants.APP_TYPE_COMMON_STANDARD)) {
						Intent intent = new Intent(getActivity(),
								TeacherAddSubject.class);
						//intent.putExtra(Constants.IMAGE_URL, userInfo.image);
						//intent.putExtra(Constants.FULL_NAME, userInfo.fullname);
						getActivity().startActivity(intent);
					}else{
						Intent intent = new Intent(getActivity(),
								AimsTeacherAddSubjectActivity.class);
						//intent.putExtra(Constants.IMAGE_URL, userInfo.image);
						//intent.putExtra(Constants.FULL_NAME, userInfo.fullname);
						getActivity().startActivity(intent);

					}
				}
			});




		// -----------------add value in profile---------



		changePassBtn = (Button) view.findViewById(R.id.change_password_btn);
		showClasses = (Button) view.findViewById(R.id.class_subjects_btn);
		mDatabase = ((SchoolApplication) getActivity().getApplication()).getDatabase();
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
			nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
			nameValuePairs.add(new BasicNameValuePair("user_type_id", SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));
			nameValuePairs.add(new BasicNameValuePair("organization_id", SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
			new GetProfileAsyntask().execute(nameValuePairs);
		}
		else{
			mDatabase.open();
			Cursor teacher_cursor = mDatabase.getTeacherProfileInfo();
			bindData(teacher_cursor);
			mDatabase.close();
		}

	}

	private void initialize() {

		InMobi.initialize(getActivity(), getResources().getString(R.string.InMobi_Property_Id));
		InMobi.setLogLevel(LOG_LEVEL.DEBUG);
		IMBanner banner = (IMBanner) getActivity().findViewById(R.id.banner);
		banner.loadBanner();
		

		/*JSONObject object = null;
		try {
			object = new JSONObject(SchoolAppUtils.GetSharedParameter(
					getActivity(), Constants.USERINFO));
		} catch (Exception e) {

		}
		if (object != null) {
			userInfo = new UserInfo(object);
		}*/

		if (userInfo != null) {
			/*if (userInfo.thumb != null) {
				imageTemp.displayImage(
						URLs.GetImage + userInfo.thumb.replace("..", ""),
						userImage, options);
			}*/
			userid.setText(userInfo.email + "/ " + userInfo.mobile_no);
			name.setText(userInfo.fullname);
			nameProfile.setText("  " + userInfo.fullname);
			email.setText(userInfo.email);
			phone.setText(userInfo.mobile_no);
			if(userInfo.address.equals("null")){
				address.setText("");
			}
			else{
				address.setText(userInfo.address);

			}
			if(userInfo.religion.equals("null")) {
				religion.setText("");
			}else{
				religion.setText(userInfo.religion);

			}
			if(userInfo.alt_email.equals("null")) {
				alternate_email.setText("");

			}else{
				alternate_email.setText(userInfo.alt_email);
			}
			if(userInfo.pincode.equals("null")) {
				pincode.setText("");

			}else{
				pincode.setText(userInfo.pincode);
			}
			if(userInfo.bloodgroup.equals("null")) {
				blood_group.setText("");
			}else{
				blood_group.setText(userInfo.bloodgroup);

			}
			if(userInfo.dob.equals("null")) {
				dob.setText("");
			}else{
				dob.setText(userInfo.dob);

			}
			if(userInfo.educational_qualification.equals("null")) {
				educational_qualification.setText("");
			}else{
				educational_qualification.setText(userInfo.educational_qualification);

			}
			if(userInfo.other_qualification.equals("null")) {
				other_qualification.setText("");
			}else{
				other_qualification.setText(userInfo.other_qualification);

			}
			if(userInfo.date_of_joining.equals("null")) {
				date_of_joining.setText("");

			}else{
				date_of_joining.setText(userInfo.date_of_joining);
			}


		//	new LoadImageAsyncTask(getActivity(), userImage, Urls.image_url_userpic, userInfo.image, false).execute();
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
		/*showClasses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().startActivity(
						new Intent(getActivity(),
								TeacherClassesSubjectsListActivity.class));
			}
		});*/
		
		nameProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent(getActivity(),
						TeacherEditProfileActivity.class);
				intent.putExtra(Constants.IMAGE_URL, userInfo.image);
				intent.putExtra(Constants.TEACHER_INFO, userInfo.object.toString());
				getActivity().startActivity(intent);*/
				
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
//		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onStart() {
//		getActivity().registerReceiver(receiver,
//				new IntentFilter(Constants.UPDATEPROFILEBROADCAST));
		super.onStart();
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			initialize();
		}
	};

	
	private class GetProfileAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

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
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_profile_info);
			
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
				}
			else {
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
