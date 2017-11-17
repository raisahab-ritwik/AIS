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

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.ChangePasswordActivity;
import com.knwedu.ourschool.ClassFellowListActivity;
import com.knwedu.ourschool.db.DatabaseAdapter;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.ParentProfileInfo;
import com.knwedu.ourschool.utils.DataStructureFramwork.StudentProfileInfo;
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

public class ProfileFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;
	//private ImageView userImage;
	private TextView userid, name, classs, classRoll, email, phone, address,
			religion, bloodGroup, dob, reg_no, parentName, hostel, transport, parentEmail,
			parentPhone;
	private DisplayImageOptions options;
	CircleImageView profileImage;
	File cacheDir;
	private TextView address_titile, religion_title, blood_titile, dob_title, parent_titile,
			p_email_titile, p_ph_titile, hostel_title, transport_title;
	String appType;

	private Button classFellowBtn, changePassBtn, nameProfile;

	StudentProfileInfo userInfo;
	ParentProfileInfo parentInfo;
	DatabaseAdapter mDatabase;
	ImageLoaderConfiguration config;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_profile, container, false);
		SchoolAppUtils.loadAppLogo(getActivity(), (ImageButton) view.findViewById(R.id.app_logo));
		appType = SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.APP_TYPE);

		initialize();
		return view;
	}

	private void bindData(Cursor cursor_student, Cursor cursor_parent){
		if (SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.USERTYPEID).equalsIgnoreCase(
				Constants.USERTYPE_STUDENT)) {
			if(cursor_student.getCount() > 0 ) {
				cursor_student.moveToFirst();
				cursor_parent.moveToFirst();

				nameProfile.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_FULLNAME)));
				userid.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_USER_CODE)));
				name.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_FULLNAME)));
				classs.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_CLASS_SECTION)));
				classRoll.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_CLASS_ROLL)));
				email.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_EMAIL)));
				phone.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_MOBILE_NO)));
				address.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_ADDRESS)));
				parentEmail.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_EMAIL)));
				parentPhone.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_MOBILE_NO)));

				religion.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_RELIGION)));
				bloodGroup.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_BLOOD_GROUP)));
				dob.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_DOB)));
				reg_no.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_REG_NO)));
				parentName.setText(cursor_parent.getString(cursor_parent.getColumnIndex(DatabaseAdapter.KEY_ROW_PRPARENT_FULLNAME)));

				if (cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_HOSTEL)) != null) {
					if (!cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_HOSTEL)).equalsIgnoreCase("null")) {
						hostel.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_HOSTEL)));
					} else {
						hostel.setText("Not Subscribed to School Hostel");
					}
				} else {
					hostel.setText("Not Subscribed to School Hostel");
				}
				if (cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_TRANSPORT)) != null) {
					if (!cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_TRANSPORT)).equalsIgnoreCase("null")) {
						transport.setText(cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_TRANSPORT)));
					} else {
						transport
								.setText("Not Subscribed to School Transportation");
					}
				} else {
					transport.setText("Not Subscribed to School Transportation");
				}

			}else{

				SchoolAppUtils.showDialog(getActivity(), getActivity()
						.getTitle().toString(), "Please try with better internet.");

			}

			if (SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.USERTYPEID).equalsIgnoreCase(
					Constants.USERTYPE_STUDENT)) {
				String imgUrl = Urls.image_url_userpic + "/" + cursor_student.getString(cursor_student.getColumnIndex(DatabaseAdapter.KEY_ROW_PRSTUDENT_IMAGE));
				ImageLoader.getInstance()
						.displayImage(imgUrl, profileImage, options);

			}

		}

	}


		private void getData() {
			SchoolAppUtils.SetSharedParameter(getActivity(),Constants.FULL_NAME,userInfo.fullname);
			SchoolAppUtils.SetSharedParameter(getActivity(),Constants.STUDENT_CLASS_SECTION,userInfo.class_section);
			nameProfile.setText(userInfo.fullname);
			userid.setText(userInfo.email + " / "+userInfo.mobile_no);
			name.setText(userInfo.fullname);
			classs.setText(userInfo.class_section);
			if(userInfo.class_roll.equals("null")){
				classRoll.setText("");
			}else{
				classRoll.setText(userInfo.class_roll);
			}

			email.setText(userInfo.email);
			phone.setText(userInfo.mobile_no);
			String test = userInfo.mobile_no;
			if(userInfo.address.equals("null")){
				address.setText("");
			}else{
				address.setText(userInfo.address);
			}
			if(parentInfo.email.equals("null")){
				parentEmail.setText("");
			}else {
				parentEmail.setText(parentInfo.email);
			}
			if(parentInfo.mobile_no.equals("null")){
				parentPhone.setText("");
			}else {
				parentPhone.setText(parentInfo.mobile_no);
			}

			if(userInfo.religion.equals("null")){
				religion.setText("");
			}else {
				religion.setText(userInfo.religion);
			}
			if(userInfo.blood_group.equals("null")){
				bloodGroup.setText("");
			}else {
				bloodGroup.setText(userInfo.blood_group);
			}
			if(userInfo.dob.equals("null")){
				dob.setText("");
			}else {
				dob.setText(userInfo.dob);
			}
			if(userInfo.reg_no.equals("null")){
				reg_no.setText("");
			}else {
				reg_no.setText(userInfo.reg_no);
			}
			if(parentInfo.fullname.equals("null")){
				parentName.setText("");
			}else {
				parentName.setText(parentInfo.fullname);
			}




			if (userInfo.hostel != null) {
				if (!userInfo.hostel.equalsIgnoreCase("null")) {
					hostel.setText(userInfo.hostel);
				} else {
					hostel.setText("Not Subscribed to School Hostel");
				}
			} else {
				hostel.setText("Not Subscribed to School Hostel");
			}
			if (userInfo.transport != null) {
				if (!userInfo.transport.equalsIgnoreCase("null")) {
					transport.setText(userInfo.transport);
				} else {
					transport
							.setText("Not Subscribed to School Transportation");
				}
			} else {
				transport.setText("Not Subscribed to School Transportation");
			}

			if (SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.USERTYPEID).equalsIgnoreCase(
					Constants.USERTYPE_STUDENT)) {
					//new LoadImageAsyncTask(getActivity(), userImage,
				//			Urls.image_url_userpic, userInfo.image, false).execute();

				String imgUrl = Urls.image_url_userpic +"/" + userInfo.image;
				ImageLoader.getInstance()
						.displayImage(imgUrl, profileImage, options);
			}

		//}

		classFellowBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ClassFellowListActivity.class);
				intent.putExtra("Section_id", userInfo.section_id);
				getActivity().startActivity(intent);
			}
		});

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

	}

	private void initialize() {


		changePassBtn = (Button) view.findViewById(R.id.change_password_btn);
		nameProfile = (Button) view.findViewById(R.id.name_txt_profile);
		profileImage = (CircleImageView) view.findViewById(R.id.image_vieww);
		userid = (TextView) view.findViewById(R.id.userid_txt);

		name = (TextView) view.findViewById(R.id.name_txt);
		classs = (TextView) view.findViewById(R.id.class_txt);
		classRoll = (TextView) view.findViewById(R.id.class_roll_txt);
		reg_no = (TextView) view.findViewById(R.id.reg_no_txt);
		email = (TextView) view.findViewById(R.id.email_txt);
		phone = (TextView) view.findViewById(R.id.phone_txt);
		address = (TextView) view.findViewById(R.id.address_txt);
		parentEmail = (TextView) view.findViewById(R.id.parent_email_txt);
		parentPhone = (TextView) view.findViewById(R.id.parent_phone_txt);

		religion = (TextView) view.findViewById(R.id.religion_txt);
		bloodGroup = (TextView) view.findViewById(R.id.blood_group_txt);
		dob = (TextView) view.findViewById(R.id.dob_txt);
		parentName = (TextView) view.findViewById(R.id.parent_name_txt);
		hostel = (TextView) view.findViewById(R.id.hostel_txt);
		transport = (TextView) view.findViewById(R.id.transport_txt);


		address_titile = (TextView) view.findViewById(R.id.address_titile);
		religion_title = (TextView) view.findViewById(R.id.religion_title);
		blood_titile  = (TextView) view.findViewById(R.id.blood_titile);
		dob_title = (TextView) view.findViewById(R.id.dob_title);
		parent_titile = (TextView) view.findViewById(R.id.parent_titile);
		p_email_titile = (TextView) view.findViewById(R.id.p_email_titile);
		p_ph_titile = (TextView) view.findViewById(R.id.p_ph_titile);
		hostel_title = (TextView) view.findViewById(R.id.hostel_title);
		transport_title = (TextView) view.findViewById(R.id.transport_title);

		if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)){
			address_titile.setVisibility(View.GONE);
			religion.setVisibility(View.GONE);
			religion_title.setVisibility(View.GONE);
			address.setVisibility(View.GONE);
			blood_titile.setVisibility(View.GONE);
			bloodGroup.setVisibility(View.GONE);
			dob_title.setVisibility(View.GONE);
			dob.setVisibility(View.GONE);
			parent_titile.setVisibility(View.GONE);
			parentName.setVisibility(View.GONE);
			p_email_titile.setVisibility(View.GONE);
			parentEmail.setVisibility(View.GONE);
			p_ph_titile.setVisibility(View.GONE);
			parentPhone.setVisibility(View.GONE);
			hostel_title.setVisibility(View.GONE);
			hostel.setVisibility(View.GONE);
			transport_title.setVisibility(View.GONE);
			transport.setVisibility(View.GONE);


		}

		classFellowBtn = (Button) view.findViewById(R.id.class_fellow_btn);
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


		if (SchoolAppUtils.GetSharedParameter(getActivity(),
				Constants.USERTYPEID).equalsIgnoreCase(
				Constants.USERTYPE_PARENT)) {
			view.findViewById(R.id.pic_layout).setVisibility(View.GONE);
		} else {
			classFellowBtn.setVisibility(View.GONE);
		}

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
			nameValuePairs.add(new BasicNameValuePair("student_id", SchoolAppUtils
					.GetSharedParameter(getActivity(), Constants.CHILD_ID)));

			new GetProfileAsyntask().execute(nameValuePairs);
		}
		else{

			mDatabase.open();
			Cursor student_cursor = mDatabase.getStudentProfileInfo();
			Cursor parent_cursor = mDatabase.getParentProfileInfo();
			bindData(student_cursor, parent_cursor);
			mDatabase.close();


		}

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
		// getActivity().registerReceiver(receiver, new
		// IntentFilter(Constants.UPDATEPROFILEBROADCAST));
		super.onStart();
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			initialize();

			// Intent inte = new Intent();
			// inte.setAction(Constants.UPDATEPROFILEBROADCAST);
			// context.sendBroadcast(inte);
		}
	};

	private class OfflineGetProfileAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getResources().getString(R.string.profile));
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();*/
		}
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			mDatabase.open();
			//StudentProfileInfo st = new StudentProfileInfo();
			//st = mDatabase.getStudentProfileInfo();
			//userInfo = st;
			//userInfo = mDatabase.getStudentProfileInfo();
			//parentInfo = new ParentProfileInfo();
			//parentInfo = mDatabase.getParentProfileInfo();

			//mDatabase.close();


			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			//getData();

			/*if (userInfo != null) {

			} else {
				SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), getResources()
						.getString(R.string.please_try_again));
			}*/



		}

	}

	private class GetProfileAsyntask extends
			AsyncTask<List<NameValuePair>, Void, Boolean> {
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
			String url = "";
			if (SchoolAppUtils.GetSharedParameter(getActivity(),
					Constants.USERTYPEID).equalsIgnoreCase(
					Constants.USERTYPE_STUDENT)) {
				url = Urls.api_profile_info;
			} else {
				url = Urls.api_child_profile_info;
			}
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, url);

			// Log parameters:
			Log.d("url extension", url);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			try {

				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						mDatabase.open();
						JSONObject object = null;
						if (SchoolAppUtils.GetSharedParameter(getActivity(),
								Constants.USERTYPEID).equalsIgnoreCase(
								Constants.USERTYPE_STUDENT)) {
							object = json.getJSONObject("info");
						} else {
							object = json.getJSONObject("child_info");
						}

						if (object != null) {
							userInfo = new StudentProfileInfo(object);
							mDatabase.deleteAllStudentProfile();
							mDatabase.addStudentProfile(userInfo);
						}
						// --------------------Parent
						// info-----------------------------//
						JSONObject objectParent = json
								.getJSONObject("parent_info");
						if (objectParent != null) {
							parentInfo = new ParentProfileInfo(objectParent);
							mDatabase.deleteAllParentProfile();
							mDatabase.addParentProfile(parentInfo);
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
				e.printStackTrace();

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

					getData();

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
