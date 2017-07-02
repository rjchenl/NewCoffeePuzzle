package com.example.user.newcoffeepuzzle.rjchenl_spndcoffeelist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;

import java.util.List;

/**
 * Created by user on 2017/7/1.
 */

public class SpndcoffeeListFragment extends Fragment {
    private static final String TAG = "SpndcoffeeListFragment";
    private ListView spndList_view;
    private List<SpndcoffeelistVO> spndcoffeelist_value;
    private String mem_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.rj_spndcoffeelist_fragment,container,false);

        spndList_view = (ListView) view.findViewById(R.id.lvSpndcoffeelist);

        //會用到mem_id 先取得
        Profile profile = new Profile(getContext());
        mem_id = profile.getMemId();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //取得物件資料
        getDBdata();
        //將資料與view做連結
        spndList_view.setAdapter(new SpndCoffeeListAdapter(getActivity(),spndcoffeelist_value));

    }

    private void getDBdata() {
        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "SpndcoffeelistServlet";
            spndcoffeelist_value = null;
            try {

                //連結資料庫取得物件資料
                spndcoffeelist_value = new SpndcoffeelistGetMyspndlistTask().execute(url,mem_id).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (spndcoffeelist_value == null || spndcoffeelist_value.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No spndcoffeelist found");
            }
        }
    }

    private class SpndCoffeeListAdapter extends BaseAdapter{
        Context context;
        List<SpndcoffeelistVO> spndList_data;

        public SpndCoffeeListAdapter(Context context, List<SpndcoffeelistVO> spndList_data) {
            this.context = context;
            this.spndList_data = spndList_data;
        }


        @Override
        public int getCount() {
            return spndList_data.size();
        }

        @Override
        public Object getItem(int position) {
            return spndList_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //一開始什麼都還沒按的話  載入被選到的view的實體,也就是convertView
            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView =layoutInflater.inflate(R.layout.rj_spndcoffeelist_item_fragment,parent,false);
            }


            //取到當下那個view物件
            SpndcoffeelistVO spndcoffeelistVO = spndList_data.get(position);


            //將抓到的view 設上其值
            TextView sotre_name = (TextView) convertView.findViewById(R.id.sotre_name);
            TextView store_add = (TextView) convertView.findViewById(R.id.store_add);
            TextView list_left = (TextView) convertView.findViewById(R.id.list_left);

            //此二項待修正 (因為不知道傳過來的其它table物件用什麼裝)
            Log.d(TAG, "getView: spndcoffeelistVO.getStore_name() : "+spndcoffeelistVO.getStore_name());
            Log.d(TAG, "getView: spndcoffeelistVO.getStore_add() : "+spndcoffeelistVO.getStore_add());

            sotre_name.setText(spndcoffeelistVO.getStore_name());
            store_add.setText(spndcoffeelistVO.getStore_add());

            list_left.setText(String.valueOf(spndcoffeelistVO.getList_left()));


            return convertView;
        }
    }









}
