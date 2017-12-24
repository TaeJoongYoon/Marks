package com.yoon.memoria.Main.Fragment.MyInfo.Decorator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.yoon.memoria.R;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Yoon on 2017-12-17.
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private Drawable drawable;

    public EventDecorator(int color, Collection<CalendarDay> dates,Activity context) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        drawable = context.getResources().getDrawable(R.drawable.event_day);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new DotSpan(5, Color.RED)); // 날자밑에 점
    }
}
