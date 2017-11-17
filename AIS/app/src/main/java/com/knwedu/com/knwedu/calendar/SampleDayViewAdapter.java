package com.knwedu.com.knwedu.calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.knwedu.com.knwedu.calendar.CalendarCellView;
import com.knwedu.com.knwedu.calendar.DayViewAdapter;
import com.knwedu.comschoolapp.R;

public class SampleDayViewAdapter implements DayViewAdapter {
  @Override
  public void makeCellView(CalendarCellView parent) {
      View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view_custom, null);
      parent.addView(layout);
      parent.setDayOfMonthTextView((TextView) layout.findViewById(R.id.day_view));
  }
}
