package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

    private StoreVO store = null;
    private boolean isTodayOpen = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        store = (StoreVO) bundle.getSerializable("StoreVO");

        //判斷今日是否營業

        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //因為Calendar的day of week 是從禮拜日=1 開始算
        int weekday =weekDay+1;


        if(weekday== Calendar.WEDNESDAY){

            int open = store.getWed_isopen();
            if(open == 1){
                long now = calendar.getTimeInMillis();
                Timestamp opentime = store.getWed_open();
                Timestamp closetime = store.getWed_close();
                if(now > opentime.getTime() && now < closetime.getTime()){
                    isTodayOpen = true;
                }

            }

        }else if(weekday== Calendar.THURSDAY){
            int open = store.getThu_isopen();
            if(open == 1 ){
                long now = calendar.getTimeInMillis();
                Log.d(TAG, "onCreate: now :"+now);
                Timestamp opentime = store.getThu_open();
                Log.d(TAG, "onCreate: opentime : "+opentime.getTime());
                Timestamp closetime = store.getThu_close();
                Log.d(TAG, "onCreate: closetime : "+closetime.getTime());
                
                if(now > opentime.getTime() && now < closetime.getTime()){
                    isTodayOpen=true;
                }
            }
        }else{

        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_storeinfo,container,false);


        TextView store_name = (TextView) view.findViewById(R.id.store_name);
        store_name.setText(store.getStore_name());

        CheckBox isOpen = (CheckBox) view.findViewById(R.id.isOpen);

        isOpen.setChecked(isTodayOpen);




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








//                Log.d(TAG, "showAllActsRRRRRRRRRRRRR: "+android_weekday);









            }

        }




    }


}
