package com.yoon.memoria.Main.Fragment.MyInfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.EventDecorator;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.OneDayDecorator;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.SaturdayDecorator;
import com.yoon.memoria.Main.Fragment.MyInfo.Decorator.SundayDecorator;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.R;
import com.yoon.memoria.SignIn.SignInActivity;
import com.yoon.memoria.Util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class MyInfoFragment extends Fragment implements MyInfoContract.View,OnDateSelectedListener{
    private StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private MyInfoPresenter presenter;

    @BindView(R.id.calendarView) MaterialCalendarView materialCalendarView;
    @BindView(R.id.myinfoToolbar) Toolbar toolbar;

    @BindView(R.id.myProfile) ImageView myProfile;
    @BindView(R.id.nickname_myinfo) TextView nickname_view;
    @BindView(R.id.postingNum_myinfo) TextView postNum;
    @BindView(R.id.followingNum_myinfo) TextView following;
    @BindView(R.id.followerNum_myinfo) TextView follower;

    private String nickname ="NICKNAME";

    private OneDayDecorator oneDayDecorator;
    private List<String> event = new ArrayList(0);
    private List<CalendarDay> calendarDays = new ArrayList(0);

    public MyInfoFragment() {
        presenter = new MyInfoPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myinfo, container, false);
        ButterKnife.bind(this,v);
        initToolbar();
        init();
        calendarinit();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.eventSetting(event);

        Observable<List<String>> event_observable = Observable.just(event);
        event_observable.subscribe(
                event -> calendarDays = presenter.eventMark(event),
                throwable -> {});

        materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,getActivity()));
    }

    public void initToolbar() {
        toolbar.inflateMenu(R.menu.menu_myinfo);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_myinfo:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                    Util.makeToast(getActivity(),"로그아웃 되었습니다!");
                    getActivity().finish();
                    break;
            }
            return false;
        });
    }

    public void init(){
        nickname = "태중이";
        nickname_view.setText(nickname);
    }
    public void calendarinit(){
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        materialCalendarView.setSelectedDate(instance.getTime());

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2035, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        oneDayDecorator = new OneDayDecorator(getActivity());
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator
        );
    }

    @Override
    public MyInfoContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        materialCalendarView.clearSelection();
        startActivity(presenter.toHistory(date,getActivity()));
    }
}
