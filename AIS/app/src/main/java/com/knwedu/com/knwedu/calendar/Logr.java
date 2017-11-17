package com.knwedu.com.knwedu.calendar;

import android.util.Log;

import com.knwedu.comschoolapp.BuildConfig;
import com.knwedu.comschoolapp.R;

/** Log utility class to handle the log tag and DEBUG-only logging. */
final class Logr {
  public static void d(String message) {
    if (BuildConfig.DEBUG) {
      Log.d("TimesSquare", message);
    }
  }

  public static void d(String message, Object... args) {
    if (BuildConfig.DEBUG) {
      d(String.format(message, args));
    }
  }
}
