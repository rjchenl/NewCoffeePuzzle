package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_activities.ActivityListFragment;
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.BrowseMyFavoriateStoreListFragment;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_member.MemberImageGetTask;
import com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout.OrderStatusListFragment;
import com.example.user.newcoffeepuzzle.rjchenl_spndcoffeelist.BrowserSpndcoffeeListFragment;
import com.example.user.newcoffeepuzzle.rjchenl_spndcoffeelist.MySpndcoffeeListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActionBarDrawerToggle actionbardrawertoggle;
    private FragmentManager fragmentManager;
    private static String TAG = "SearchActivity";
    private GoogleMap map;
    private ActionBar actionBar;
    private TextView etLocationName;
    private Button btSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rj_search_activity);



        findViews();
        askPermissions();
        setUpActionBar();
        initDrawer();
        initBody();

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
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_COARSE_LOCATION

        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
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

        Profile profile = new Profile(this);
        String mem_id = profile.getMemId();
        //使用navigationView 得到headerView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View navView = navigationView.getHeaderView(0);

//        TextView tvcurrent_position = (TextView) navView.findViewById(R.id.tvcurrent_position);
//        tvcurrent_position.setText(profile.getCurrentPosition());
//        Log.d(TAG, "initDrawer: profile.getCurrentPosition():"+profile.getCurrentPosition());

        TextView tvmember_name = (TextView) navView.findViewById(R.id.tvmember_name);
        tvmember_name.setText(profile.getMem_name());

        TextView tvmem_add = (TextView) navView.findViewById(R.id.tvmem_add);
        tvmem_add.setText(profile.getMem_add());

//        TextView tvmem_tel = (TextView) navView.findViewById(R.id.tvmem_tel);
//        tvmem_tel.setText(profile.getMem_tel());
//
//        TextView tvmem_email = (TextView) navView.findViewById(R.id.tvmem_email);
//        tvmem_email.setText(profile.getMem_email());
//
//        TextView tvmem_points = (TextView) navView.findViewById(R.id.tvmem_points);
//        tvmem_points.setText(profile.getMem_points().toString());

        ImageView member_image = (ImageView) navView.findViewById(R.id.member_image);
        //執行拿照片
        String url = Common_RJ.URL + "MemberServlet";
        int imageSize = 250;
        new MemberImageGetTask(member_image).execute(url,mem_id,imageSize);
//        member_image.setImageResource();




        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    item.setChecked(true);
                    drawerlayout.closeDrawers();
                    Fragment fragment;
                    switch(item.getItemId()){
                        case R.id.searchBar:
                            initBody();
                            break;
                        case R.id.browerActivities:
                            fragment = new ActivityListFragment();
                            switchFragment(fragment);
                            setTitle("瀏覽活動");
                            break;
                        case R.id.mysusCofee:
                            fragment = new MySpndcoffeeListFragment();
                            switchFragment(fragment);
                            setTitle("瀏覽我的寄杯");
                            break;
                        case R.id.orderStatusList:
                            fragment = new OrderStatusListFragment();
                            switchFragment(fragment);
                            setTitle("我的訂單");
                            break;
                        case R.id.susCoffeeStore:
                            fragment = new BrowserSpndcoffeeListFragment();
                            switchFragment(fragment);
                            setTitle("瀏覽寄杯活動");
                            break;
                        case R.id.collectedStore:
                            fragment = new BrowseMyFavoriateStoreListFragment();
                            switchFragment(fragment);
                            setTitle("我的收藏店家");
                            break;
                        case R.id.registerout:
                            finish();
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

        Fragment fragment = new GoogleMapFragment();
        switchFragment(fragment);
        setTitle("GoogleMap page");
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


    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.body, fragment,"fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

    }








    private void locationNameToMarker(String locationName){
        map.clear();

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            showToast("Location name not found");
        } else {
            Address address = addressList.get(0);
            LatLng position = new LatLng(address.getLatitude(), address.getLongitude());

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
