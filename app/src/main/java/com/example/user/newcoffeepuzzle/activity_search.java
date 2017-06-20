package com.example.user.newcoffeepuzzle;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class activity_search extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActionBarDrawerToggle actionbardrawertoggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //放入toolbar 物件 原本的toolbar 要先去manifests註冊停用
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //把home鍵打開
        setUpActionBar();
        initDrawer();

    }

    private void initDrawer() {
        //建立drawer與toolbar間的連結
        final DrawerLayout drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionbardrawertoggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.app_name, R.string.app_name);
        actionbardrawertoggle.syncState();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            drawerlayout.addDrawerListener(actionbardrawertoggle);
        }else{
            drawerlayout.setDrawerListener(actionbardrawertoggle);
        }


    }

    private void setUpActionBar() {
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
}
