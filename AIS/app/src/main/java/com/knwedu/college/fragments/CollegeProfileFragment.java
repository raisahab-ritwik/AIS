package com.knwedu.college.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

import com.knwedu.college.CollegeChangePasswordActivity;
import com.knwedu.college.db.CollegeDBAdapter;
import com.knwedu.college.utils.CollegeAppUtils;
import com.knwedu.college.utils.CollegeConstants;
import com.knwedu.college.utils.CollegeDataStructureFramwork.ParentProfileInfo;
import com.knwedu.college.utils.CollegeDataStructureFramwork.Program;
import com.knwedu.college.utils.CollegeDataStructureFramwork.StudentProfileInfo;
import com.knwedu.college.utils.CollegeJsonParser;
import com.knwedu.college.utils.CollegeLoadImageAsyncTask;
import com.knwedu.college.utils.CollegeUrls;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.db.SchoolApplication;
import com.knwedu.ourschool.utils.Constants;
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


public class CollegeProfileFragment extends Fragment {
	private View view;
	private ProgressDialog dialog;

	//private ImageView userImage;
	private TextView userid, name, classs, email, phone, address, religion,
			bloodGroup, parentName, hostel, transport, parentEmail,
			parentPhone, programName, termName,pincode;
	private DisplayImageOptions options;
	CircleImageView profileImage;
	File cacheDir;
	private Button classFellowBtn, changePassBtn, nameProfile;
	private StudentProfileInfo userInfo;
	private Program program;
	private JSONArray parentArray;
	private String pageTitle = "";
	private ParentProfileInfo parentInfo;
	private ArrayList<ParentProfileInfo> parent;
	CollegeDBAdapter mDatabase;
	ImageLoaderConfiguration config;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.college_fragment_profile, container, false);
		pageTitle = getActivity().getTitle().toString();
		initialize();
		return view;
	}

	private void bindData(Cursor cursor_student, Cursor cursor_parent, Cursor program_cursor) {
		if(cursor_student.getCount()>0){
			cursor_student.moveToFirst();

			nameProfile.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_FULLNAME)));
			userid.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_USER_CODE)));
			name.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_FULLNAME)));

			if(program_cursor.getCount()>0) {
				program_cursor.moveToFirst();
				classs.setText(program_cursor.getString(program_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_PROGRAM_ROW_LEVEL_NAME)));
				programName.setText(program_cursor.getString(program_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_PROGRAM_ROW_PROGRAM_NAME)));
				termName.setText(program_cursor.getString(program_cursor.getColumnIndex(CollegeDBAdapter.TABLE_COL_PROGRAM_ROW_TERM_NAME)));
			}

			email.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_EMAIL)));
			phone.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_MOBILE_NO)));
			address.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_ADDRESS)));
			pincode.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_PIN_CODE)));

			if(cursor_parent.getCount()>0){

				cursor_parent.moveToFirst();
			parentEmail.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_EMAIL)));
			parentPhone.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_MOBILE_NO)));
			parentName.setText(cursor_parent.getString(cursor_parent.getColumnIndex(CollegeDBAdapter.TABLE_COL_PARENT_ROW_FULLNAME)));

			}

			religion.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_RELIGION)));
			bloodGroup.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_BLOOD_GROUP)));

			if (cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_HOSTEL)) != null) {
				if (!cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_HOSTEL)).equalsIgnoreCase("null")) {
					hostel.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_HOSTEL)));
				} else {
					hostel.setText("Not Subscribed to School Hostel");
				}
			} else {
				hostel.setText("Not Subscribed to School Hostel");
			}
			if (cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_TRANSPORT)) != null) {
				if (!cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_TRANSPORT)).equalsIgnoreCase("null")) {
					transport.setText(cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_TRANSPORT)));
				} else {
					transport
							.setText("Not Subscribed to School Transportation");
				}
			} else {
				transport.setText("Not Subscribed to School Transportation");
			}

			if (CollegeAppUtils.GetSharedParameter(getActivity(),
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_STUDENT)) {

				String imgUrl = cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_IMAGE_PATH))
						+ "/" + cursor_student.getString(cursor_student.getColumnIndex(CollegeDBAdapter.TABLE_COL_STUDENT_ROW_IMAGE));
				ImageLoader.getInstance()
						.displayImage(imgUrl, profileImage, options);
			}



		}


	}

	private void getData() {
		if (userInfo != null) {
			CollegeAppUtils.SetSharedParameter(getActivity(), Constants.FULL_NAME,userInfo.fullname);
			CollegeAppUtils.SetSharedParameter(getActivity(),Constants.STUDENT_CLASS_SECTION,program.program_name+"-"+program.term_name);
			nameProfile.setText(userInfo.fullname);
			userid.setText(userInfo.user_code);
			name.setText(userInfo.fullname);
			classs.setText(program.level_name);
			programName.setText(program.program_name);
			termName.setText(program.term_name);
			email.setText(userInfo.email);
			phone.setText(userInfo.mobile_no);
			address.setText(userInfo.address);
			pincode.setText(userInfo.pin_code);
			
			parentEmail.setText(parentInfo.email);

			parentPhone.setText(parentInfo.mobile_no);

			parentName.setText(parentInfo.fullname);

			religion.setText(userInfo.religion);
			bloodGroup.setText(userInfo.blood_group);

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

			if (CollegeAppUtils.GetSharedParameter(getActivity(),
					CollegeConstants.USERTYPEID).equalsIgnoreCase(
					CollegeConstants.USERTYPE_STUDENT)) {

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

		}

		classFellowBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent(getActivity(),
				 * ClassFellowListActivity.class); intent.putExtra("Section_id",
				 * userInfo.section_id); getActivity().startActivity(intent);
				 */
			}
		});

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

	}

	private void initialize() {

		changePassBtn = (Button) view.findViewById(R.id.change_password_btn);
		nameProfile = (Button) view.findViewById(R.id.name_txt_profile);
		profileImage = (CircleImageView) view.findViewById(R.id.image_vieww);
		userid = (TextView) view.findViewById(R.id.userid_txt);

		name = (TextView) view.findViewById(R.id.name_txt);
		classs = (TextView) view.findViewById(R.id.class_txt);
		programName = (TextView) view.findViewById(R.id.program_txt);
		termName = (TextView) view.findViewById(R.id.term_txt);
		email = (TextView) view.findViewById(R.id.email_txt);
		phone = (TextView) view.findViewById(R.id.phone_txt);
		address = (TextView) view.findViewById(R.id.address_txt);
		parentEmail = (TextView) view.findViewById(R.id.parent_email_txt);
		parentPhone = (TextView) view.findViewById(R.id.parent_phone_txt);
		// parentEmail1 = (TextView) view.findViewById(R.id.parent_email_txt2);
		// parentPhone2 = (TextView) view.findViewById(R.id.parent_phone_txt2);
		pincode = (TextView) view.findViewById(R.id.pin_code_txt);
		religion = (TextView) view.findViewById(R.id.religion_txt);
		bloodGroup = (TextView) view.findViewById(R.id.blood_group_txt);
		parentName = (TextView) view.findViewById(R.id.parent_name_txt);
		// parentName2 = (TextView) view.findViewById(R.id.parent_name_txt2);
		hostel = (TextView) view.findViewById(R.id.hostel_txt);
		transport = (TextView) view.findViewById(R.id.transport_txt);
		classFellowBtn = (Button) view.findViewById(R.id.class_fellow_btn);
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

		mDatabase = ((SchoolApplication) getActivity().getApplication()).getCollegeDatabase();

		if (CollegeAppUtils.GetSharedParameter(getActivity(),
				CollegeConstants.USERTYPEID).equalsIgnoreCase(
				CollegeConstants.USERTYPE_PARENT)) {
			view.findViewById(R.id.pic_layout).setVisibility(View.GONE);
		} else {
			classFellowBtn.setVisibility(View.GONE);
		}


		if (CollegeAppUtils.isOnline(getActivity())) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
			nameValuePairs.add(new BasicNameValuePair("id", CollegeAppUtils
					.GetSharedParameter(getActivity(), "id")));
			new GetProfileAsyntask().execute(nameValuePairs);
		}
		else{
			mDatabase.open();
			Cursor student_cursor = mDatabase.getStudentProfileInfo();
			Cursor parent_cursor = mDatabase.getParentProfileInfo();
			Cursor program_cursor = mDatabase.getProgramProfileInfo();
			bindData(student_cursor, parent_cursor, program_cursor);
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
			String url = "";
			url = CollegeUrls.api_child_profile_info;

			/*
			 * if (SchoolAppUtils.GetSharedParameter(getActivity(),
			 * Constants.USERTYPEID).equalsIgnoreCase(
			 * Constants.USERTYPE_STUDENT)) { url = Urls.api_profile_info; }
			 * else { url = Urls.api_child_profile_info; }
			 */

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
						object = json.getJSONObject("info");
						/*
						 * if (SchoolAppUtils.GetSharedParameter(getActivity(),
						 * Constants.USERTYPEID).equalsIgnoreCase(
						 * Constants.USERTYPE_STUDENT)) { object =
						 * json.getJSONObject("info"); } else { object =
						 * json.getJSONObject("child_info"); }
						 */
						if (object != null) {
							userInfo = new StudentProfileInfo(object);
							mDatabase.deleteAllStudentProfile();
							mDatabase.addStudentProfile(userInfo);
						}
						// --------------------Parent
						// info-----------------------------//
						JSONObject objectParent = object
								.getJSONObject("program");
						if (objectParent != null) {
							program = new Program(objectParent);
							mDatabase.deleteAllProgramProfile();
							mDatabase.addProgramProfile(program);
						}
						JSONObject Parent = json.getJSONObject("parent");
						if (Parent != null) {
							parentInfo = new ParentProfileInfo(Parent);
							mDatabase.deleteAllParentProfile();
							mDatabase.addParentProfile(parentInfo);
						}
						/*
						 * parentArray = json.getJSONArray("parents"); parent =
						 * new ArrayList<ParentProfileInfo>();s for (int i = 0;
						 * i < parentArray.length(); i++) { parentInfo = new
						 * ParentProfileInfo( parentArray.getJSONObject(i));
						 * Log.d("PARAMS OF PARENT...",
						 * ""+parentArray.getJSONObject(i));
						 * parent.add(parentInfo); }
						 */
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
