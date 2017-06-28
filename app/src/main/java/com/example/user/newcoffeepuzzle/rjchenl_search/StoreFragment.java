package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Java on 2017/6/21.
 */

public class StoreFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_storeinfo,container,false);
        Log.d(TAG, "onCreateView: enter");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: enter");
        showAllActs();
    }

    private void showAllActs() {
        if(Common_RJ.networkConnected(getActivity())){
            String url = Common_RJ.URL+"StoreServlet";
            List<StoreVO> storeList = null;

            try {
                storeList = new StoreGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }

            if(storeList == null || storeList.isEmpty()){
                Common_RJ.showToast(getActivity(),"No storeList found");
            }else{
                Common_RJ.showToast(getActivity(),"已有VO上的data開始連結view");


                //放入店名
                List<StoreVO> store_List = new ArrayList<>();
                for(StoreVO store : store_List){
                    TextView store_name = (TextView) getActivity().findViewById(R.id.store_name);
                    store_name.setText(store.getStore_name());
                }



                //判斷今日是否營業
//                Date date = new Date(System.currentTimeMillis());
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-(E) HH:mm:ss");
//                String time = dateFormat.format(date);
//                String android_weekday,current_time;
//                android_weekday=time.substring(12,14);
                //取得資料庫中所選取的店營業日期



//                Log.d(TAG, "showAllActsRRRRRRRRRRRRR: "+android_weekday);









            }

        }




    }
}
