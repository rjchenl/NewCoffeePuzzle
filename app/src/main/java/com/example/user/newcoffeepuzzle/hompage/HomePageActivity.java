package com.example.user.newcoffeepuzzle.hompage;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_home.StoreLoginFragment;

import com.example.user.newcoffeepuzzle.ming_home.MemFragment;
import com.example.user.newcoffeepuzzle.ming_home.Page;

import com.example.user.newcoffeepuzzle.ming_login_store.Login_StoreVO;
import com.example.user.newcoffeepuzzle.ming_login_store.Login_Store_GetId;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_take_orders.SpndcoffelistVO;
import com.example.user.newcoffeepuzzle.ming_take_orders.StoreActivity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private final static String TAG = "HomePageActivity";
    EditText edStoreid,edStorepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_layout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.Tab);
        tabLayout.setupWithViewPager(viewPager);

        findView();
    }

    private void findView() {
        edStoreid = (EditText) findViewById(R.id.edStoreid);
        edStorepassword = (EditText) findViewById(R.id.edStorepassword);
    }

    public void btonClick (View view){
        String UserStoreid = edStoreid.getText().toString();
        String UserStorepassword = edStorepassword.getText().toString();
        if (Common_ming.networkConnected(this)){
            String url = Common_ming.URL + "Store_Servlet";
            List<SpndcoffelistVO> login_storeVO = null;
            try {
                login_storeVO = new Login_Store_GetId().execute(url,UserStoreid,UserStorepassword).get();
            }catch (Exception e ){
                Log.d(TAG, e.toString());
            }
            if (login_storeVO == null){
                Common_ming.showToast(this,R.string.Login_null);
            }else {
                Intent intent = new Intent(this, StoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("login_storeVO", (Serializable) login_storeVO);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }else {
            Common_ming.showToast(this,R.string.Login_null);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new MemFragment(),"一般會員"));
            pageList.add(new Page(new StoreLoginFragment(),"店家會員"));
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
