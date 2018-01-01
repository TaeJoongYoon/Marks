package com.yoon.memoria.Main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.storage.FirebaseStorage;
import com.yoon.memoria.EventBus.ActivityResultEvent;
import com.yoon.memoria.EventBus.BusProvider;
import com.yoon.memoria.Main.Fragment.Map.MapContract;
import com.yoon.memoria.Main.Fragment.Map.MapFragment;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoContract;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoFragment;
import com.yoon.memoria.Main.Fragment.Place.PlaceContract;
import com.yoon.memoria.Main.Fragment.Place.PlaceFragment;
import com.yoon.memoria.StorageSingleton;
import com.yoon.memoria.R;
import com.yoon.memoria.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;
    private MainTapPagerAdapter mainTapPagerAdapter;

    private MapFragment mapFragment;
    private PlaceFragment placeFragment;
    private MyInfoFragment myInfoFragment;

    private MapContract.Presenter mapPresenter;
    private PlaceContract.Presenter placePresenter;
    private MyInfoContract.Presenter myInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setActivity(this);

        initFragment();
        initTabLayout();
        initViewPager();

        mapPresenter = mapFragment.getPresenter();
        placePresenter = placeFragment.getPresenter();
        myInfoPresenter = myInfoFragment.getPresenter();
    }

    public void initFragment(){
        mapFragment = new MapFragment();
        placeFragment = new PlaceFragment();
        myInfoFragment = new MyInfoFragment();
    }

    public void initTabLayout(){

        binding.mainTabLayout.addTab(binding.mainTabLayout.newTab().setText("지도"));
        binding.mainTabLayout.addTab(binding.mainTabLayout.newTab().setText("퀴즈"));
        binding.mainTabLayout.addTab(binding.mainTabLayout.newTab().setText("내 페이지"));
        binding.mainTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.mainPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initViewPager(){
        mainTapPagerAdapter = new MainTapPagerAdapter(getSupportFragmentManager(), binding.mainTabLayout.getTabCount(),
                                                        mapFragment,
                                                        placeFragment,
                                                        myInfoFragment);
        binding.mainPager.setAdapter(mainTapPagerAdapter);
        binding.mainPager.setOffscreenPageLimit(3);
        binding.mainPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.mainTabLayout));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }
}

