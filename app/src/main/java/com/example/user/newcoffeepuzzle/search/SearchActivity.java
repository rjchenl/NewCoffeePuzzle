package com.example.user.newcoffeepuzzle.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.activities.ActivityListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActionBarDrawerToggle actionbardrawertoggle;
    private static final int REQ_PERMISSIONS = 0;
    private FragmentManager fragmentManager;
    private static String TAG = "SearchActivity";
    private GoogleMap map;
    private ActionBar actionBar;
    private TextView etLocationName;
    private Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViews();
        askPermissions();
        setUpActionBar();
        initDrawer();
//        initBody();

    }

    private void findViews() {
        etLocationName = (TextView) findViewById(R.id.etLocationName);
        btSubmit = (Button) findViewById(R.id.btSubmit);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //drawertoggle跟畫面取得連繫
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
        //建立drawer與toolbar間的toggle
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
                            fragment = new ActivityListFragment();
                            switchFragment(fragment);
                            setTitle("活動");
                            //讓搜尋列表和bt不見
                            etLocationName.setVisibility(View.INVISIBLE);
                            btSubmit.setVisibility(View.INVISIBLE);
                            break;
                        default:
                            initBody();
                            Log.d(TAG, "onNavigationItemSelected: initBody has be executed");
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void initBody() {
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
//        Fragment fragment = new GoogleMapFragment();
//        switchFragment(fragment);
//        setTitle("GoogleMap page");
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void setUpActionBar() {

        //放入toolbar 物件 原本的toolbar 要先去manifests註冊停用
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.getUiSettings().setZoomControlsEnabled(true);

        //map shows iii info
        LatLng position = new LatLng(24.9677420,121.1917000);
        map.addMarker(new MarkerOptions().position(position).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(position));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position).zoom(15).build();
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    public void onLocationNameClick(View view) {
        EditText etLocationName = (EditText) findViewById(R.id.etLocationName);
        String locationName = etLocationName.getText().toString().trim();
        if (locationName.length() > 0) {
            locationNameToMarker(locationName);
        } else {
            showToast("Location Name is empty");
        }
    }

    private void locationNameToMarker(String locationName){
        map.clear();
        //補充
/*        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });*/
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);

            //補充
//            addressList = geocoder.getFromLocation(24,121,maxResults);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            showToast("Location name not found");
        } else {
            Address address = addressList.get(0);

            LatLng position = new LatLng(address.getLatitude(),
                    address.getLongitude());

            String snippet = address.getAddressLine(0);

            map.addMarker(new MarkerOptions().position(position)
                    .title(locationName).snippet(snippet));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(15).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

    }



}
