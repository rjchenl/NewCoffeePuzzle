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
import android.widget.Toast;


import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_home.StoreLoginFragment;

import com.example.user.newcoffeepuzzle.ming_home.MemLoginFragment;
import com.example.user.newcoffeepuzzle.ming_home.Page;

import com.example.user.newcoffeepuzzle.ming_login_store.Login_Store_GetId;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_spndcoffelist.SpndcoffelistVO;
import com.example.user.newcoffeepuzzle.ming_home.StoreActivity;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;


import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private final static String TAG = "HomePageActivity";
    EditText edStoreid,edStorepassword;
    private EditText etID;
    private EditText etPsw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_layout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.Tab);
        tabLayout.setupWithViewPager(viewPager);

        findView_Store();
        findViews();
    }
    private void findViews() {
        etID = (EditText) findViewById(R.id.etID);
        etPsw = (EditText) findViewById(R.id.etPsw);
    }

    private void findView_Store() {

        View store_layout = getLayoutInflater().inflate(R.layout.store, null);
        edStoreid = (EditText) store_layout.findViewById(R.id.edStoreid);
        edStorepassword = (EditText) store_layout.findViewById(R.id.edStorepassword);


    }

    public void btonClick (View view){
        String userStoreid = edStoreid.getText().toString();
        String UserStorepassword = edStorepassword.getText().toString();
        Toast.makeText(this,userStoreid,Toast.LENGTH_SHORT).show();
        Log.d(TAG, "btonClick: userStoreid : "+userStoreid);
        Log.d(TAG, "btonClick: UserStorepassword : "+UserStorepassword);
        Log.d(TAG, "btonClick: edStoreid ： "+edStoreid);

        if (Common_ming.networkConnected(this)){
            String url = Common_ming.URL + "Store_Servlet";
            List<SpndcoffelistVO> login_storeVO = null;
            try {
                login_storeVO = new Login_Store_GetId().execute(url,userStoreid,UserStorepassword).get();
            }catch (Exception e ){
                Log.d(TAG, e.toString());
            }
            if (login_storeVO == null){
                Common_ming.showToast(this,R.string.Login_null);
            }else {
                Intent intent = new Intent(this, StoreActivity.class);
                startActivity(intent);
            }
        }else {
            Common_ming.showToast(this,R.string.Login_null);
        }
    }

    public void onbtLoginClick(View view) {
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new MemLoginFragment(),"一般會員"));
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
