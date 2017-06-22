package com.example.user.newcoffeepuzzle.search;

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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.activities.Activities_fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity  {

    private ActionBarDrawerToggle actionbardrawertoggle;
    private static final int REQ_PERMISSIONS = 0;
    private FragmentManager fragmentManager;
    private static String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        fragmentManager = getSupportFragmentManager();
        askPermissions();
        setUpActionBar();
        initDrawer();
//        initBody();

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
        Log.d(TAG, "initBody: 1");
        Fragment majorFragment = fragmentManager.findFragmentById(R.id.undersearch);
        Log.d(TAG, "initBody: 2");
        if(majorFragment == null){
            GoogleMapFragment googlemapframent = new GoogleMapFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.undersearch,googlemapframent,TAG);
            transaction.commit();
            Log.d(TAG, "initBody: 3");
        }else{
            showToast("fragment attached");
            Log.d(TAG, "initBody: 4");
        }

    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void setUpActionBar() {

        //放入toolbar 物件 原本的toolbar 要先去manifests註冊停用i
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.undersearch, fragment);
        fragmentTransaction.commit();

    }



}
