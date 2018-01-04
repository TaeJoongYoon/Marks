package com.yoon.memoria.Main.Fragment.MyInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.yoon.memoria.databinding.FragmentMyinfoBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;

public class MyInfoFragment extends Fragment implements MyInfoContract.View,OnDateSelectedListener{
    private FragmentMyinfoBinding binding;
    private MyInfoPresenter presenter;
    private DatabaseReference databaseReference;

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_myinfo,container,false);
        initToolbar();
        init();
        calendarinit();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.eventSetting(databaseReference, event);

        Observable<List<String>> event_observable = Observable.just(event);
        event_observable.subscribe(
                event -> calendarDays = presenter.eventMark(event),
                throwable -> {});

        binding.calendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,getActivity()));
    }

    public void initToolbar() {
        binding.myinfoToolbar.inflateMenu(R.menu.menu_myinfo);

        binding.myinfoToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.myinfo_edit:
                    presenter.show();
                    break;
                case R.id.myinfo_logout:
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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(getUid()).child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nickname = dataSnapshot.getValue(String.class);
                binding.nicknameMyinfo.setText(nickname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void calendarinit(){
        binding.calendarView.setOnDateChangedListener(this);
        binding.calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        binding.calendarView.setSelectedDate(instance.getTime());

        binding.calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2035, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        oneDayDecorator = new OneDayDecorator(getActivity());
        binding.calendarView.addDecorators(
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
        binding.calendarView.clearSelection();
        startActivity(presenter.toHistory(date,getActivity()));
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void nicknameEdit(EditText editText){
        String temp = editText.getText().toString();
        binding.nicknameMyinfo.setText(temp);
        databaseReference.child("users").child(getUid()).child("nickname").setValue(temp);
    }

    @Override
    public Context getAct(){
        return getActivity();
    }
}
