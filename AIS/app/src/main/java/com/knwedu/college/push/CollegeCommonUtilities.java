package com.knwedu.college.push;

import android.content.Context;
import android.content.Intent;

public final class CollegeCommonUtilities {
	
	// give your server registration url here
   // static final String SERVER_URL = URLs.GCMRegister; 
    public static final String NOTIFICATION_TYPE="menu";
    public static String PREF_NAME ="app";

    // Google project id
    public static final String SENDER_ID = "359433549350"; 

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "Al Wahda School GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "knwedu.alwahda.schoolapp.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
