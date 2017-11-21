package com.yoon.memoria.Main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yoon.memoria.Main.Fragment.Map.MapContract;
import com.yoon.memoria.Main.Fragment.Map.MapFragment;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoContract;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoFragment;
import com.yoon.memoria.Main.Fragment.Place.PlaceContract;
import com.yoon.memoria.Main.Fragment.Place.PlaceFragment;
import com.yoon.memoria.R;


public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
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
        setContentView(R.layout.activity_main);


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

        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("지도"));
        tabLayout.addTab(tabLayout.newTab().setText("퀴즈"));
        tabLayout.addTab(tabLayout.newTab().setText("내 페이지"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
        mainTapPagerAdapter = new MainTapPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                                                        mapFragment,
                                                        placeFragment,
                                                        myInfoFragment);
        viewPager = (ViewPager) findViewById(R.id.mainPager);
        viewPager.setAdapter(mainTapPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}

