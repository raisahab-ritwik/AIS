package com.knwedu.com.knwedu.calendar;

import android.app.ProgressDialog;
import android.view.ContextThemeWrapper;
import android.widget.TextView;
import com.knwedu.comschoolapp.R;

public class DefaultDayViewAdapter implements DayViewAdapter {
  @Override
  public void makeCellView(CalendarCellView parent) {
      TextView textView = new TextView(
              new ContextThemeWrapper(parent.getContext(), R.style.CalendarCell_CalendarDate));
      textView.setDuplicateParentStateEnabled(true);
      parent.addView(textView);
      parent.setDayOfMonthTextView(textView);
  }



}
