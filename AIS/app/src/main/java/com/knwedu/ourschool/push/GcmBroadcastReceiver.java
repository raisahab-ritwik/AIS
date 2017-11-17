package com.knwedu.ourschool.push;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;
import com.knwedu.ourschool.utils.SchoolAppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver  {
	static final String TAG = "GCMDemo";
	public static final int NOTIFICATION_ID = 1;
	NotificationCompat.Builder builder;
	Context ctx;

	@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		ctx = context;

		//Log.d(TAG, "Received: " + extras.toString());
		Log.d("Payload........",intent.getStringExtra("message").toString());
		//Log.d("Payload moumita111",intent.getStringExtra("notifications").toString());
		//Log.d("payload NotificationInfo :",intent.getExtras().getString("notificationinfo"));
		//String data=intent.getExtras().getString("notificationinfo");

//		if (remoteMessage.getData().size() > 0) {
//			Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//			JsonObject jobj=new JsonObject(remoteMessage.getData());
////		}

		//Log.d("notification.. ",data);



		String messageType = gcm.getMessageType(intent);


		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			sendNotification("Send error: " + intent.getExtras().toString());
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			sendNotification("Deleted messages on server: " +
					intent.getExtras().toString());
		} else {
			String message=intent.getStringExtra("message");
			String title = intent.getStringExtra("title");
			//String NotificationInfo=intent.getExtras().getString("notificationInfo", "");

//			int menu = intent.getIntExtra("menu", 0);
			//CommonUtilities.saveInSP(context,"Title",title);
		   	//CommonUtilities.saveInSP(context,"Message",message);
//			CommonUtilities.saveInSP(context, "menu", menu);
//			CommonUtilities.saveInSP(context,"notificationinfo",notifications);

			//SharedPreferences prefs = context.getsharedPreferences("prefs name", Context.MODE_PRIVATE);


			//Log.d("notificationInfo after storing",data);


			if(message != null)
			{

			//	MediaPlayer mp=MediaPlayer.create(ctx, R.raw.sms_alert);
				//	mp.start();


				if(message.contains("announcement"))
				{
					SchoolAppUtils.SetSharedBoolParameter(context, "showannouncements", true);
					Intent intentnew = new Intent();
					intentnew.setAction("alerts");
					intentnew.putExtra("announcementincomming", true);
					context.sendBroadcast(intentnew);
				}
				if(message.contains("News : New School Announcement"))
				{
					SchoolAppUtils.SetSharedBoolParameter(context, "shownews", true);
					Intent intentnew = new Intent();
					intentnew.setAction("alerts");
					intentnew.putExtra("newincomming", true);
					context.sendBroadcast(intentnew);
				}

			}
		}


		/*JSONObject myjson = new JSONObject();
		try {
			JSONArray the_json_array = myjson.getJSONArray("notificationInfo");
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		/*Log.d("notificationInfo in JSON format", String.valueOf(myjson));*/
		// Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
       // setResultCode(Activity.RESULT_OK);

	}

	// Put the GCM message into a notification and post it.
	private void sendNotification(String msg) {

	}





}