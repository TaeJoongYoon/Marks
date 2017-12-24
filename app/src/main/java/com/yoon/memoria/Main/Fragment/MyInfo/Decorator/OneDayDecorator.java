package com.yoon.memoria.Main.Fragment.MyInfo.Decorator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.yoon.memoria.R;

import java.util.Date;

/**
 * Created by Yoon on 2017-12-17.
 */

public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;
    private Drawable drawable;


    public OneDayDecorator(Activity context) {
        date = CalendarDay.today();
        drawable = context.getResources().getDrawable(R.drawable.event_day);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new ForegroundColorSpan(Color.GREEN));
    }

    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
