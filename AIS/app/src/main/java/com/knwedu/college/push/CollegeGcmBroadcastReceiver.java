package com.knwedu.college.push;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.knwedu.college.utils.CollegeAppUtils;

public class CollegeGcmBroadcastReceiver extends WakefulBroadcastReceiver  {
	static final String TAG = "GCMDemo";
	public static final int NOTIFICATION_ID = 1;
	NotificationCompat.Builder builder;
	Context ctx;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		ctx = context;

		Log.d("Payload........", intent.getStringExtra("message").toString());

		String messageType = gcm.getMessageType(intent);

		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			sendNotification("Send error: " + intent.getExtras().toString());
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			sendNotification("Deleted messages on server: " + 
					intent.getExtras().toString());
		} else {
			String message=intent.getStringExtra("message");
			String title = intent.getStringExtra("title");



			if(message != null)
			{
 
			//	MediaPlayer mp=MediaPlayer.create(ctx, R.raw.sms_alert);
			//	mp.start();
				
				
				if(message.contains("announcement"))
				{
					CollegeAppUtils.SetSharedBoolParameter(context, "showannouncements", true);
					Intent intentnew = new Intent();
					intentnew.setAction("alerts");
					intentnew.putExtra("announcementincomming", true);
					context.sendBroadcast(intentnew);
				}
				if(message.contains("News : New School Announcement"))
				{
					CollegeAppUtils.SetSharedBoolParameter(context, "shownews", true);
					Intent intentnew = new Intent();
					intentnew.setAction("alerts");
					intentnew.putExtra("newincomming", true);
					context.sendBroadcast(intentnew);
				}

			}
		}
		
		// Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                CollegeGcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        /*setResultCode(Activity.RESULT_OK);*/
        
	}

	// Put the GCM message into a notification and post it.
	private void sendNotification(String msg) {

	}





}