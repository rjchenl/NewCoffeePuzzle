package com.example.user.newcoffeepuzzle;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.user.newcoffeepuzzle.home.MemFragment;
import com.example.user.newcoffeepuzzle.home.Page;
import com.example.user.newcoffeepuzzle.home.StoreFragment;
import com.example.user.newcoffeepuzzle.search.StoreActivity;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_layout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.Tab);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void btonClick (View view){
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new MemFragment(),"一般會員"));
            pageList.add(new Page(new StoreFragment(),"店家會員"));
        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }

    }
}
