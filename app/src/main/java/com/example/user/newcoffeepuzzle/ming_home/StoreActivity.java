package com.example.user.newcoffeepuzzle.ming_home;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_Home_C_Ordelist.HomeFragment;
import com.example.user.newcoffeepuzzle.ming_QRin.QRin_Fragment;
import com.example.user.newcoffeepuzzle.ming_delivery.Delivery_Fragment;
import com.example.user.newcoffeepuzzle.ming_spndcoffelist.Spndcoffeelist_Inser;
import com.example.user.newcoffeepuzzle.ming_spndcoffelist.Spndcoffelist_Fragment;

public class StoreActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionbardrawertoggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_store_activity);
//        //放入toolbar 物件 原本的toolbar 要先去manifests註冊停用
//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        initDrawer();
    }

    private void store_Frame() {
        Fragment frangment = new HomeFragment();
        switchFragment(frangment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        store_Frame();
    }

    //有線條
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionbardrawertoggle.syncState();
        setTitle("首頁");
    }

    private void initDrawer() {

        final DrawerLayout store_drawerlayout = (DrawerLayout) findViewById(R.id.store_drawerlayout);
        Toolbar store_toolbar = (Toolbar) findViewById(R.id.store_toolbar);
        actionbardrawertoggle = new ActionBarDrawerToggle(this, store_drawerlayout,store_toolbar, R.string.app_name, R.string.app_name);
        NavigationView store_gavigation = (NavigationView) findViewById(R.id.store_gavigation);
        if (store_gavigation != null) {
            store_gavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    store_drawerlayout.closeDrawers();
                    Fragment fragment;
                    switch (menuItem.getItemId()) {
                        case R.id.store_home:
                            fragment = new HomeFragment();
                            switchFragment(fragment);
                            setTitle("主頁");
                            break;
                        case R.id.spndcoffeelist_inser:
                            fragment = new Spndcoffeelist_Inser();
                            switchFragment(fragment);
                            setTitle("新增寄杯訂單");
                            break;
                        case R.id.spndcoffeelist:
                            fragment = new Spndcoffelist_Fragment();
                            switchFragment(fragment);
                            setTitle("寄杯");
                            break;
                        case R.id.delivery:
                            fragment = new Delivery_Fragment();
                            switchFragment(fragment);
                            setTitle("外送確認");
                            break;
                        case R.id.QRin:
                            fragment = new QRin_Fragment();
                            switchFragment(fragment);
                            setTitle("掃描器");
                            break;
                        case R.id.out:
                            finish();
                            break;
                        default:
//                            HomeFragment();
                            break;
                    }
                    return true;
                }
            });
        }

    }

    //Fragment的交易:讓Fragment可以刪除、新增上去
    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.store_Frame, fragment);
        fragmentTransaction.commit();

    }
}
