package com.example.user.newcoffeepuzzle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.user.newcoffeepuzzle.activities.Activities_fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActionBarDrawerToggle actionbardrawertoggle;
    private static final int REQ_PERMISSIONS = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        askPermissions();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getMaps();
        //把home鍵打開
        setUpActionBar();
        initDrawer();
//        initBody();

    }

    private void getMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionbardrawertoggle.syncState();
    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }

    private void initDrawer() {
        //建立drawer與toolbar間的連結
        final DrawerLayout drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionbardrawertoggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.app_name, R.string.app_name);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            drawerlayout.addDrawerListener(actionbardrawertoggle);
        }else{
            drawerlayout.setDrawerListener(actionbardrawertoggle);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    item.setChecked(true);
                    drawerlayout.closeDrawers();
                    Fragment fragment;
                    switch(item.getItemId()){
                        case R.id.browerActivities:
                            fragment = new Activities_fragment();
                            switchFragment(fragment);
                            setTitle("活動");
                            break;
                        default:
                            initBody();
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void initBody() {
        Fragment fragment = new Activities_fragment();
        switchFragment(fragment);
        setTitle("活動列表");
    }

    private void setUpActionBar() {

        //放入toolbar 物件 原本的toolbar 要先去manifests註冊停用
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.map, fragment);
        fragmentTransaction.commit();

    }



}
