package com.knwedu.ourschool.push;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.knwedu.ourschool.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class CommonUtilities {

    public static final String NOTIFICATION_TYPE="menu";
     public static String PREF_NAME ="app";

	// give your server registration url here
   // static final String SERVER_URL = URLs.GCMRegister; 

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

    public static void updateValueInSPQuiz(Context mContext,String key,String Value){
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String jsonStr = Value;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
            json.put("Quiz", 0);
        } catch (JSONException e) {
        }

       ed.clear();
        ed.putString(key, json.toString());
            ed.commit();

   }
    public static void updateValueInSPActivity(Context mContext,String key,String Value){
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String jsonStr = Value;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
            json.put("Activity", 0);
        } catch (JSONException e) {
        }

        ed.clear();
        ed.putString(key, json.toString());
        ed.commit();

    }

    public static void updateValueInSPAssignment(Context mContext,String key,String Value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String jsonStr = Value;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
            json.put("Assignment", 0);
        } catch (JSONException e) {
        }

        ed.clear();
        ed.putString(key, json.toString());
        ed.commit();
    }

    public static void updateValueInSPDailydiary(Context mContext,String key,String Value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String jsonStr = Value;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
            json.put("Daily Diary", 0);
        } catch (JSONException e) {
        }

        ed.clear();
        ed.putString(key, json.toString());
        ed.commit();
    }
    public static void updateValueInSPClasswork(Context mContext,String key,String Value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String jsonStr = Value;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
            json.put("Class Work", 0);
        } catch (JSONException e) {
        }

        ed.clear();
        ed.putString(key, json.toString());
        ed.commit();
    }
    public static void updateValueInSPBulletin(Context mContext,String key,String Value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String jsonStr = Value;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
            json.put("Bulletin", 0);
        } catch (JSONException e) {
        }

        ed.clear();
        ed.putString(key, json.toString());
        ed.commit();
    }
//    public static void updateValueInSP(Context mContext,String key,String Value,String key1){
//        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
//        SharedPreferences.Editor ed = sp.edit();
//
//        String jsonStr = Value;
//        JSONObject json = null;
//
//            try {
//                json = new JSONObject(jsonStr);
//                json.put(key1, 0);
//               // Log.d("Shared value", key1);
//            } catch (JSONException e) {
//
//
//            ed.clear();
//            ed.putString(key, json.toString());
//           // Log.d("Shared value2",key);
//
//            ed.commit();
//
//        }
//
//    }

    public static void updateValueInSP(Context mContext,String key,String Value,String key1){
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String jsonStr = Value;
        JSONObject json = null;

        try {
            json = new JSONObject(jsonStr);
            json.put(key1, 0);
            // Log.d("Shared value", key1);
        } catch (JSONException e) {

        }
            ed.clear();
            ed.putString(key, json.toString());
            // Log.d("Shared value2",key);
            ed.commit();



    }




    public static void saveInSP(Context mContext,String key,String Value){
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        ed.putString(key, Value);
        ed.commit();
        Log.d("test 2", Value);
    }


    public static void saveInSP(Context mContext, String key, int Value){
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(key, Value);
        ed.clear();
        ed.commit();
       // return 0;
    }
    public static int getFromSP(Context mContext,String key,int Value){
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getInt(key, Value);
    }

    public static String getFromSP(Context mcontext,String key){
        SharedPreferences sp = mcontext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }




}
