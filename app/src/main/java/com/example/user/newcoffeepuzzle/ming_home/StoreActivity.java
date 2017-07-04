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
import com.example.user.newcoffeepuzzle.ming_Orderlist.Ordelist_Fragment;
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
//有線條
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionbardrawertoggle.syncState();
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
                        case R.id.take_orders:
                            fragment = new Ordelist_Fragment();
                            switchFragment(fragment);
                            setTitle("外帶");
                            break;
                        case R.id.spndcoffeelist:
                            fragment = new Spndcoffelist_Fragment();
                            switchFragment(fragment);
                            setTitle("寄杯");
                            break;
                        default:
//                            store_Frame();
                            break;
                    }
                    return true;
                }
            });
        }

    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.store_Frame, fragment);
        fragmentTransaction.commit();

    }
}
