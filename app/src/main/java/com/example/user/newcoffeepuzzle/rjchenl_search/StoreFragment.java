package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.FavoriateStoreInsertTask;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by Java on 2017/6/21.
 */

public class StoreFragment extends Fragment{

    private StoreVO store = null;
    private boolean isTodayOpen = false;
    private String mem_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //得到傳過來的StoreVO物件
        Bundle bundle = getArguments();
        store = (StoreVO) bundle.getSerializable("StoreVO");

        isTodayOpenCheck();


    }

    private void isTodayOpenCheck() {


        //判斷今日是否營業

        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        if(weekDay== Calendar.WEDNESDAY){

            int open = store.getWed_isopen();
            if(open == 1){
                Timestamp opentime = store.getWed_open();
                Timestamp closetime = store.getWed_close();
                isTodayOpenCovertToTimestmp(opentime,closetime);
            }

        }else if(weekDay== Calendar.THURSDAY){
            int open = store.getThu_isopen();
            if(open == 1 ){
                Timestamp opentime = store.getThu_open();
                Timestamp closetime = store.getThu_close();
                isTodayOpenCovertToTimestmp(opentime,closetime);

            }

        }else if(weekDay == Calendar.FRIDAY){
            int open = store.getFri_isopen();
            if(open == 1 ){
                Timestamp opentime = store.getFri_open();
                Timestamp closetime = store.getFri_close();
                isTodayOpenCovertToTimestmp(opentime,closetime);
            }

        }else if(weekDay == Calendar.SATURDAY){
            int open = store.getSat_isopen();
            if(open == 1){
                Timestamp opentime = store.getSat_open();
                Timestamp closetime = store.getSat_close();
                isTodayOpenCovertToTimestmp(opentime,closetime);
            }

        }else if(weekDay == Calendar.SUNDAY){
            int open = store.getSun_isopen();
            if(open == 1){
                Timestamp opentime = store.getSun_open();
                Timestamp closetime = store.getSun_close();
                isTodayOpenCovertToTimestmp(opentime,closetime);
            }
        }else if(weekDay == Calendar.MONDAY){
            int open = store.getMon_isopen();
            if(open ==1 ){
                Timestamp opentime = store.getMon_open();
                Timestamp closetime = store.getMon_close();
                isTodayOpenCovertToTimestmp(opentime,closetime);
            }
        }else if(weekDay == Calendar.THURSDAY){
            int open = store.getTue_isopen();
            if(open == 1 ){
                Timestamp opentime = store.getMon_open();
                Timestamp closetime = store.getMon_close();
                isTodayOpenCovertToTimestmp(opentime,closetime);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //得到從登入傳過來的使用者資訊
//        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
//        mem_id = preferences.getString("mem_id", "");
        Profile profile = new Profile(getActivity());
        mem_id = profile.getMemId();
    }

    public boolean isTodayOpenCovertToTimestmp(Timestamp opentime, Timestamp closetime ){
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //取得現在時間的日期STR
        String nowmFormatStr  =  simpleDateFormat2.format(now);
        String nowDateStr = nowmFormatStr.substring(0,11);

        //取得營業時間後面時間STR
        String openFormatStr  =  simpleDateFormat2.format(opentime);
        String opensub = openFormatStr.substring(11);

        String closeFormatStr  =  simpleDateFormat2.format(closetime);
        String closesub = closeFormatStr.substring(11);

        //組合成timestamp物件
        String openformatset =nowDateStr+opensub;
        Timestamp opentimestamp = Timestamp.valueOf(openformatset);

        String closeformatset = nowDateStr+closesub;
        Timestamp closetimestamp = Timestamp.valueOf(closeformatset);

        if(now > opentimestamp.getTime() && now < closetimestamp.getTime()){
            isTodayOpen=true;
        }

            return isTodayOpen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.rj_fragment_storeinfo,container,false);

        //放上店家名稱
        TextView store_name = (TextView) view.findViewById(R.id.store_name);
        store_name.setText(store.getStore_name());
        //放上今日是否營業
        CheckBox isOpen = (CheckBox) view.findViewById(R.id.isOpen);
        isOpen.setChecked(isTodayOpen);
        //放上資料庫的地址
        TextView store_add = (TextView) view.findViewById(R.id.store_add);
        String storeAddress = store.getStore_add();
        store_add.setText(storeAddress);
        //放上是否有wifi
        CheckBox is_wifi = (CheckBox) view.findViewById(R.id.is_wifi);
        if(store.getIs_wifi() == 1){
            is_wifi.setChecked(true);
        }
        //放上是否有插座
        CheckBox is_plug = (CheckBox) view.findViewById(R.id.is_plug);
        if(store.getIs_plug() == 1){
            is_plug.setChecked(true);
        }
        //放上是否限時
        CheckBox is_time_limit = (CheckBox) view.findViewById(R.id.is_time_limit);
        if(store.getIs_time_limit() == 1){
            is_time_limit.setChecked(true);
        }
        //放上是否賣正餐
        CheckBox is_meal = (CheckBox) view.findViewById(R.id.is_meal);
        if(store.getIs_meal() == 1) {
            is_meal.setChecked(true);
        }
        //放上是否賣甜點
        CheckBox is_dessert = (CheckBox) view.findViewById(R.id.is_dessert);
        if(store.getIs_dessert() == 1 ){
            is_dessert.setChecked(true);
        }

        //放上店家照片from DB
        ImageView store_img = (ImageView) view.findViewById(R.id.store_img);
        String url = Common_RJ.URL + "StoreServlet";
        String store_id = store.getStore_id();
        int imageSize = 250;
            //執行拿照片
        Log.d(TAG, "onCreateView: before doing StoreGetImageTask");
        new StoreGetImageTask(store_img).execute(url,store_id,imageSize);
        Log.d(TAG, "onCreateView: after doing StoreGetImageTask");


        //註冊收藏店家功能
        ImageView heart = (ImageView) view.findViewById(R.id.like);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增一筆收藏店家資料到資料庫
                String url = Common_RJ.URL + "fav_storeServlet";
                try {

                    String store_id = store.getStore_id();
                    
                    new FavoriateStoreInsertTask().execute(url,mem_id,store_id).get();

                    //置換愛心圖片
//                    ImageView image = (ImageView) view.findViewById(R.id.like);
//                    image.setImageResource(R.drawable.unlike);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                showToast("我按了愛心");
            }
        });


        return view;
    }

    private void showToast(String s ) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
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



            }

        }




    }


}
