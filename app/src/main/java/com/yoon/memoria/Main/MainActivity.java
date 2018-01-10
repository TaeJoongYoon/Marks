package com.yoon.memoria.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.yoon.memoria.EventBus.ActivityResultEvent;
import com.yoon.memoria.EventBus.BusProvider;
import com.yoon.memoria.Main.Fragment.Map.MapContract;
import com.yoon.memoria.Main.Fragment.Map.MapFragment;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoContract;
import com.yoon.memoria.Main.Fragment.MyInfo.MyInfoFragment;
import com.yoon.memoria.Main.Fragment.Place.PlaceFragment;
import com.yoon.memoria.R;
import com.yoon.memoria.Util.Util;
import com.yoon.memoria.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ActivityMainBinding binding;
    private ViewPagerAdapter viewPagerAdapter;
    private MenuItem prevBottomNavigation;

    private GoogleApiClient googleApiClient = null;

    private MapFragment mapFragment;
    private PlaceFragment placeFragment;
    private MyInfoFragment myInfoFragment;

    private MapContract.Presenter mapPresenter;
    private MyInfoContract.Presenter myInfoPresenter;


    private static final int UPDATE_INTERVAL_MS = 1000 * 60 * 1;           // 20분
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000 * 60 * 1;   // 20분
    private LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                                                   .setInterval(UPDATE_INTERVAL_MS)
                                                                   .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setActivity(this);

        initFragment();
        initViewPager();
        initTabLayout();

        buildGoogleApiClient();

        mapPresenter = mapFragment.getPresenter();
        myInfoPresenter = myInfoFragment.getPresenter();
    }

    public void initFragment(){
        mapFragment = new MapFragment();
        placeFragment = new PlaceFragment();
        myInfoFragment = new MyInfoFragment();
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
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
                if (prevBottomNavigation != null)
                    prevBottomNavigation.setChecked(false);
                else
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);

                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevBottomNavigation = binding.bottomNavigation.getMenu().getItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Util.GPS_ENABLE_REQUEST_CODE:
                if (checkLocationServicesStatus()) {
                    if (!googleApiClient.isConnected()) {
                        googleApiClient.connect();
                    }
                    return;
                }
                break;
        }
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates(){
        if ( !checkLocationServicesStatus()) {
            showDialogForLocation();
        }
        else{
            TedRx2Permission.with(this)
                    .setRationaleTitle(R.string.rationale_title)
                    .setRationaleMessage(R.string.rationale_location_message)
                    .setDeniedMessage(R.string.rationale_denied_message)
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .request()
                    .subscribe(tedPermissionResult -> {
                        if (tedPermissionResult.isGranted()) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                            mapPresenter.setMyLocationEnable();
                        } else {
                            Util.makeToast(this, "권한 거부\n" + tedPermissionResult.getDeniedPermissions().toString());
                        }
                    }, throwable -> {
                    }, () -> {
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(googleApiClient != null && !googleApiClient.isConnected()){
            googleApiClient.connect();
            System.out.println("CCC connect");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            googleApiClient.disconnect();
        }
        clearApplicationCache(null);
    }

    private void clearApplicationCache(java.io.File dir){
        if(dir==null)
            dir = getCacheDir();
        else;
        if(dir==null)
            return;
        else;
        java.io.File[] children = dir.listFiles();
        try{
            for(int i=0;i<children.length;i++)
                if(children[i].isDirectory())
                    clearApplicationCache(children[i]);
                else children[i].delete();
        }
        catch(Exception e){}
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapPresenter.onConnected();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        if ( cause ==  CAUSE_NETWORK_LOST )
            Util.makeToast(this, "네트워크가 잠시 끊겼습니다.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED )
            Util.makeToast(this, "서비스가 일시적으로 중단되었습니다.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mapPresenter.onConnectionFailed();
    }

    @Override
    public void onLocationChanged(Location location) {
        mapPresenter.onLocationChanged(googleApiClient, location);
    }

    public GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }


    public void showDialogForLocation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" +
                "위치 설정을 수정하십시오.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent =
                        new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, Util.GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}

