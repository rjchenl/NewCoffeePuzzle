package com.example.user.newcoffeepuzzle.ming_Orderlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.newcoffeepuzzle.R;

import java.util.ArrayList;
import java.util.List;


public class Ordelist_C_Fragment extends Fragment{
    
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ming_ordelist_c_home,container,false);
        ViewPager Ordelist_viewPager = (ViewPager) view.findViewById(R.id.Ordelist_viewPager);
        Ordelist_viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));//Support不支援

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.Ordelist_Tab);
        tabLayout.setupWithViewPager(Ordelist_viewPager);

        return view;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Ordelist_Page> ordelist_page;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            ordelist_page= new ArrayList<>();
            ordelist_page.add(new Ordelist_Page(new Ordelist_1_Fragment(),"未接單"));
            ordelist_page.add(new Ordelist_Page(new Ordelist_2_Fragment(),"已接單"));
            ordelist_page.add(new Ordelist_Page(new Ordelist_3_Fragment(),"完成訂單"));
        }

        @Override
        public Fragment getItem(int position) {
            return ordelist_page.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return ordelist_page.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ordelist_page.get(position).getTitle();
        }
    }
}
