package com.knwedu.college.utils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class CollegeAppUtils {
	public static SharedPreferences preferences;
	public static String SHARED_PREFERENCES_NAME="smonteschoolapp_preferences";
	public static int SHARED_PREFERENCES_MODE=0;
	public static String termName = "";
	
	/**
	 * @param context The context of the activity that uses the function
	 * 
	 * @return ture, if networks seems to be connected else false
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	/**********************************************************************
	 * @param context  The context of the activity that uses the function 
	 * 
	 * @param key 	Key of the parameter to set value
	 * 					  *
	 * @param value		value of the parameter to set					  *
	 *********************************************************************/
	public static void SetSharedParameter(Context context,String key, String value ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		preferences.edit().putString(key, value).commit();

	}


	/**
	 * @param context The context of the activity that uses the function
	 * 
	 * @param key Key of the parameter to set value
	 * 
	 * @return the value corresponding to the key
	 */
	public static String GetSharedParameter(Context context,String key ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		return preferences.getString(key, "0");

	}

	/**********************************************************************
	 * @param context  The context of the activity that uses the function 
	 * 
	 * @param key 	Key of the parameter to set value
	 * 					  *
	 * @param value		value of the parameter to set					  *
	 *********************************************************************/
	public static void SetSharedLongParameter(Context context,String key, long value ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		preferences.edit().putLong(key, value).commit();

	}

	/**
	 * @param context The context of the activity that uses the function
	 * 
	 * @param key Key of the parameter to set value
	 * 
	 * @return the value corresponding to the key
	 */
	public static long GetSharedLongParameter(Context context,String key ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		return preferences.getLong(key, 0);

	}

	/**
	 * @param context The context of the activity that uses the function
	 * 
	 * @param key Key of the parameter to set value
	 * 
	 * @param value the value corresponding to the key
	 */
	public static void SetSharedIntParameter(Context context,String key, int value ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		preferences.edit().putInt(key, value).commit();

	}

	/**
	 * @param context The context of the activity that uses the function
	 * 
	 * @param key Key of the parameter to set value
	 * 
	 * @return the value corresponding to the key
	 */
	public static int GetSharedIntParameter(Context context,String key ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		return preferences.getInt(key, 0);

	}

	/**
	 * @param context The context of the activity that uses the function
	 * 
	 * @param key Key of the parameter to set value
	 * 
	 * @param value the value corresponding to the key
	 */
	public static void SetSharedBoolParameter(Context context,String key, boolean value ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		preferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * @param context The context of the activity that uses the function
	 * 
	 * @param key key Key of the parameter to set value
	 * 
	 * @return the value corresponding to the key
	 */
	public static boolean GetSharedBoolParameter(Context context,String key ){
		preferences = context.getSharedPreferences(CollegeAppUtils.SHARED_PREFERENCES_NAME, CollegeAppUtils.SHARED_PREFERENCES_MODE);
		return preferences.getBoolean(key, false);

	}
	public static void showDialog(Context context, String title, String message)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNeutralButton(android.R.string.ok, null);
		dialog.show();
		
	}
	
	public static void expand(final View v) {
		v.measure(MeasureSpec.makeMeasureSpec(android.view.ViewGroup.LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY));
	    final int targtetHeight = v.getMeasuredHeight();

	    v.getLayoutParams().height = 0;
	    v.setVisibility(View.VISIBLE);
	    Animation a = new Animation()
	    {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            v.getLayoutParams().height = interpolatedTime == 1
	                    ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT
	                    : (int)(targtetHeight * interpolatedTime);
	            v.requestLayout();
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
	    v.startAnimation(a);
	}

	public static void collapse(final View v) {
	    final int initialHeight = v.getMeasuredHeight();

	    Animation a = new Animation()
	    {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            if(interpolatedTime == 1){
	                v.setVisibility(View.GONE);
	            }else{
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
	                v.requestLayout();
	            }
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
	    v.startAnimation(a);
	}
	public static String getCurrentDate(){
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		String format = s.format(new Date());
		//Log.e("current date", format);
		return format;
	}
	public static String getDay(String date)
	{
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		
		Calendar calender = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
		int dayCount = calender.get(Calendar.DAY_OF_WEEK);
		if(dayCount == Calendar.MONDAY)
		{
			return "Mon";
		}
		else if(dayCount == Calendar.TUESDAY)
		{
			return "Tue";
		}
		else if(dayCount == Calendar.WEDNESDAY)
		{
			return "Wed";
		}
		else if(dayCount == Calendar.THURSDAY)
		{
			return "Thu";
		}
		else if(dayCount == Calendar.FRIDAY)
		{
			return "Fri";
		}
		else if(dayCount == Calendar.SATURDAY)
		{
			return "Sat";
		}
		else if(dayCount == Calendar.SUNDAY)
		{
			return "Sun";
		}
		return null;
	}
	
	public static String addDays(String date, int count)
	{
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		
		Calendar calender = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day) + count);
		int dayCount = calender.get(Calendar.DAY_OF_WEEK);
		String yearSet = Integer.toString(calender.get(Calendar.YEAR));
		String monthSet = Integer.toString(calender.get(Calendar.MONTH) + 1);
		String daySet = Integer.toString(calender.get(Calendar.DAY_OF_MONTH));
		
		
		return yearSet + "/" + monthSet + "/" + daySet;
	}
	
	public static String getLastDayofWeek(String date, int count)
	{
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		
		Calendar calender = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day) + count);
		int dayCount = calender.get(Calendar.DAY_OF_WEEK);
		String yearSet = Integer.toString(calender.get(Calendar.YEAR));
		String monthSet = Integer.toString(calender.get(Calendar.MONTH) + 1);
		String daySet = Integer.toString(calender.get(Calendar.DAY_OF_MONTH));
		
		if(Integer.parseInt(daySet)<10)
		{
			daySet = "0" + daySet;
		}
		
		return yearSet + "-" + monthSet + "-" + daySet;
	}
	
	public static String getDayDifferent(String date)
	{
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		
		Calendar calender = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
		int dayCount = calender.get(Calendar.DAY_OF_WEEK);
		
		if(Integer.parseInt(month) > 8)
		{
			month = "" + (Integer.parseInt(month) +1);
		}
		else
		{
			month = "0" + (Integer.parseInt(month) +1);
		}
		if(dayCount == Calendar.MONDAY)
		{
			return "Mon, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.TUESDAY)
		{
			return "Tue, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.WEDNESDAY)
		{
			return "Wed, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.THURSDAY)
		{
			return "Thu, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.FRIDAY)
		{
			return "Fri, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.SATURDAY)
		{
			return "Sat, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.SUNDAY)
		{
			return "Sun, " + day + "/" + month + "/" + year;
		}
		return null;
	}
	
	public static String getDayDifferentDif(String date)
	{
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		
		Calendar calender = new GregorianCalendar(Integer.parseInt(year), (Integer.parseInt(month) - 1), Integer.parseInt(day));
		int dayCount = calender.get(Calendar.DAY_OF_WEEK);
		
		if(Integer.parseInt(month) > 8)
		{
			month = "" + (Integer.parseInt(month));
		}
		else
		{
			month = "0" + (Integer.parseInt(month));
		}
		if(dayCount == Calendar.MONDAY)
		{
			return "Mon, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.TUESDAY)
		{
			return "Tue, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.WEDNESDAY)
		{
			return "Wed, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.THURSDAY)
		{
			return "Thu, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.FRIDAY)
		{
			return "Fri, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.SATURDAY)
		{
			return "Sat, " + day + "/" + month + "/" + year;
		}
		else if(dayCount == Calendar.SUNDAY)
		{
			return "Sun, " + day + "/" + month + "/" + year;
		}
		return null;
	}
	public static OnTouchListener touchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {{
           switch(event.getAction()) {
               case MotionEvent.ACTION_DOWN:
                    v.setBackgroundColor(Color.TRANSPARENT);
                    break;
               case MotionEvent.ACTION_UP:
                    v.setBackgroundColor(Color.TRANSPARENT);
                    break;
           }
        return true;
      }}
	};
	
	public static void setFont(Context context, TextView textView)
	{
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "QwarsIcons.ttf");
		textView.setTypeface(tf);
	}
	
	public static long getDiffBTDates(String dateStart, String dateStop)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 
		Date d1 = null;
		Date d2 = null;
 
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
 
			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
 
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
 
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			
			return diffDays;
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 999;
	}


	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}


	public static void imagePdfViewDocument(final Context context, String url, String fileName) {
		WebView mWebView = new WebView(context);
		mWebView.setWebViewClient(new WebViewClient() {
			ProgressDialog dialog = new ProgressDialog(context);

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// progressBar.setVisibility(View.VISIBLE);
				dialog.setTitle("Fetching information");
				dialog.setMessage("Please Wait...");
				dialog.show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// progressBar.setVisibility(View.GONE);
				dialog.dismiss();
			}
		});

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);

		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		int width = display.getWidth();

		String data = "<body style='margin:125px 0px 0px 0px; vertical-align: middle; padding:0px; margin'><center><img width=\"" + width + "\" src=\"" + url
				+ "\" /></center></body></html>";
		String extension = fileName.substring(fileName.length() - 4);
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")) {
			mWebView.loadData(data, "text/html", null);
		} else {
			mWebView.loadUrl(
					"https://docs.google.com/gview?embedded=true&url=" + url);
		}
		((Activity) context).setContentView(mWebView);

	}




	
	public static String getDateFormat(String dateStr)
	{
		return dateStr;
		/*
		String tempStr = dateStr;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	    dateFormat.setLenient(false);
	    boolean isValid = false;
	    
	    try {
	      dateFormat.parse(dateStr.trim());
	      isValid = true;
	    } catch (ParseException pe) {
	    	isValid = false;
	    }
	    
	    if(isValid == true)
	    {
	    	DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	    	DateFormat targetFormat = new SimpleDateFormat("dd MMMMM yyyy", Locale.ENGLISH);
	    	Date date;
			try {
				date = originalFormat.parse(dateStr);
				//Mon Sep 01 00:00:00 GMT+05:30 2014 S5
				String formattedDate = targetFormat.format(date);
				String splitter = " ";
				String [] array = formattedDate.split(splitter);
				String day = array[0];
				String month = array[1];
				String year = array[2];
				
				if(Integer.parseInt(day)<10){
					day = "" + day.charAt(1);
				}
				
				month = month.substring(0, 3);
				
				formattedDate = day + " " + month + " " + year;
				
				return formattedDate;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return tempStr;
			}
	    	
	    }
	    else
	    {
	    	return tempStr;
	    }
	    
	*/
		}
	
	// Convert namevaluepair to json
	public JSONObject nameValuePairToJson(List<NameValuePair> nameValuePairs){
		JSONObject JSONObjectData = new JSONObject();

	    for (NameValuePair nameValuePair : nameValuePairs) {
	        try {
	            JSONObjectData.put(nameValuePair.getName(), nameValuePair.getValue());
	        } catch (JSONException e) {

	        }
	    }
	    return JSONObjectData;
	}
	
	// Convert json to namevaluepair
	public List<NameValuePair> jsonToNameValuePair(JSONObject jsonObject){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (int i = 0; i < jsonObject.names().length(); i++) {
			try {
				nameValuePairs.add(new BasicNameValuePair(jsonObject.names().getString(i),jsonObject.getString(jsonObject.names().getString(i))));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return nameValuePairs;
	}
	
	
}

