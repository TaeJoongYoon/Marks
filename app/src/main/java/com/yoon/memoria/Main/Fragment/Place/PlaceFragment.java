package com.yoon.memoria.Main.Fragment.Place;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yoon.memoria.Custom.SimpleDividerItemDecoration;
import com.yoon.memoria.FollowList.FollowRecyclerViewAdapter;
import com.yoon.memoria.History.HistoryActivity;
import com.yoon.memoria.Model.Place;
import com.yoon.memoria.Quiz.QuizActivity;
import com.yoon.memoria.R;
import com.yoon.memoria.UidSingleton;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.FragmentPlaceBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PlaceFragment extends Fragment implements ValueEventListener, DatePickerDialog.OnDateSetListener,PlaceContract.View {
    private FragmentPlaceBinding binding;
    private DatabaseReference databaseReference;
    private UidSingleton uidSingleton = UidSingleton.getInstance();

    private PlaceRecyclerViewAdapter adapter;

    private Date now;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String date;

    private DatePickerDialog picker;

    public PlaceFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_place,container,false);
        now = new Date();
        date = dateFormat.format(now);

        initToolbar();
        setRecyclerView();
        init();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").child(date).removeEventListener(this);
    }

    public void initToolbar(){
        binding.placeToolbar.inflateMenu(R.menu.menu_place);

        binding.placeToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_place:
                    show();
            }
            return false;
        });
    }
    public void init(){
        binding.fab.setOnClickListener(
                view -> getActivity().startActivity(new Intent(getActivity(), QuizActivity.class))
        );
    }

    public void setRecyclerView(){
        binding.placeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new PlaceRecyclerViewAdapter(getActivity(),this);
        binding.placeRecyclerview.setAdapter(adapter);
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").child(date).addValueEventListener(this);
    }

    public void show(){
        picker = new DatePickerDialog(getActivity(),this, Calendar.getInstance().get(Calendar.YEAR)
                                                ,Calendar.getInstance().get(Calendar.MONTH)
                                                ,Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Place> places = new ArrayList<>(0);
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Place place = snapshot.getValue(Place.class);
            places.add(place);
        }
        adapter.addItems(places);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

        Intent intent = new Intent(getActivity(), HistoryActivity.class);
        intent.putExtra("year", calendar.get(Calendar.YEAR));
        intent.putExtra("month", calendar.get(Calendar.MONTH));
        intent.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
        startActivity(intent);
    }

    @Override
    public void delete(String Uid) {
        binding.placeProgressBar.setVisibility(View.VISIBLE);
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").child(date).removeEventListener(this);
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").child(date).child(Uid).removeValue();
        databaseReference.child("users").child(uidSingleton.getUid()).child("places").child(date).addValueEventListener(this);
        Util.makeToast(getActivity(),"삭제되었습니다!");
        binding.placeProgressBar.setVisibility(View.GONE);
    }
}
