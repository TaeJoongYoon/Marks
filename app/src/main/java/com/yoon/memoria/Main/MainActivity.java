package com.yoon.memoria.Main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.yoon.memoria.EventBus.ActivityResultEvent;
import com.yoon.memoria.EventBus.BusProvider;
import com.yoon.memoria.Main.Fragment.Map.MapContract;
import com.yoon.memoria.Main.Fragment.Map.MapFragment;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoContract;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoFragment;
import com.yoon.memoria.Main.Fragment.Place.PlaceContract;
import com.yoon.memoria.Main.Fragment.Place.PlaceFragment;
import com.yoon.memoria.R;
import com.yoon.memoria.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;
    private ViewPagerAdapter viewPagerAdapter;
    private MenuItem prevBottomNavigation;

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
        initViewPager();
        initTabLayout();

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
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_one:
                    binding.mainPager.setCurrentItem(0);
                    return true;
                case R.id.action_two:
                    binding.mainPager.setCurrentItem(1);
                    return true;
                case R.id.action_three:
                    binding.mainPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });
    }

    public void initViewPager(){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 3,
                                                        mapFragment,
                                                        placeFragment,
                                                        myInfoFragment);
        binding.mainPager.setAdapter(viewPagerAdapter);
        binding.mainPager.setOffscreenPageLimit(3);
        binding.mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevBottomNavigation != null) {
                    prevBottomNavigation.setChecked(false);
                }
                else
                {
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);
                }

                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevBottomNavigation = binding.bottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }

}

