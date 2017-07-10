package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.FavoriateStoreInsertTask;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout.OrderListInsertTask;
import com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout.OrderdetailVO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by Java on 2017/6/21.
 */

public class StoreFragment extends Fragment{

    private StoreVO store = null;
    private boolean isTodayOpen = false;
    private String mem_id;
    private Spinner spinner_item;
    final List<String> selectedItemList = new ArrayList<>();
    private Button btSubmit_buytakeout;
    private List<ProductVO> productVOList = null;
    private List<StoreVO> storeList = null;
    private ListView lvStoreitem;
    private String item_selected;
    private String item_selected_price;
    private List<ProductVO> shoopingVOList;
    private int temp_inttotal;
    private TextView tvtotal;
    private Button btSubmit_buytakeout1;
    private String item_selected_id;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //得到從登入傳過來的使用者資訊
//        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
//        mem_id = preferences.getString("mem_id", "");
        Profile profile = new Profile(getActivity());
        mem_id = profile.getMemId();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //得到傳過來的StoreVO物件
        Bundle bundle = getArguments();
        store = (StoreVO) bundle.getSerializable("StoreVO");
        isTodayOpenCheck();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.rj_fragment_storeinfo,container,false);
        spinner_item = (Spinner) view.findViewById(R.id.spinner_item);
        btSubmit_buytakeout = (Button) view.findViewById(R.id.btSubmit_buytakeout);
        lvStoreitem = (ListView) view.findViewById(R.id.lvStoreitem);
        tvtotal = (TextView) view.findViewById(R.id.tvtotal);

        //按下送出選單後
        btSubmit_buytakeout1 = (Button) view.findViewById(R.id.btSubmit_buytakeout);
        btSubmit_buytakeout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增一筆task新增訂單
                String url = Common_RJ.URL + "OrderlistServlet";
                //得到mem_id
                Profile profile = new Profile(getActivity());
                mem_id = profile.getMemId();
                //得到store_id
                String store_id = store.getStore_id();
                //得到總金額
                int  ord_total = temp_inttotal;
                int ord_pick = 2;
                int ord_shipping = 1;
                Timestamp ord_time = new Timestamp(System.currentTimeMillis());
                String ord_add="";
                int score_seller = 1;

                //新增訂單詳情列表
                List<OrderdetailVO> orderdetailvolist= new ArrayList<>();
                //取得購買數量
                orderdetailvolist.add(new OrderdetailVO(item_selected_id,item_selected,Integer.valueOf(item_selected_price),Integer.valueOf(item_selected_price)));




                try {
                    new OrderListInsertTask().execute(url,mem_id,store_id,ord_total,ord_pick,ord_add,ord_shipping,ord_time,score_seller).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });



        putCheckItems(view);
        putStorePhoto(view);
        myFavoriateFunction(view);



        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: enter");
        getStoreVOList();
        getProductVOList();

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



    public boolean isTodayOpenCovertToTimestmp(Timestamp opentime, Timestamp closetime ){
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //取得現在時間的日期STR
        String nowmFormatStr  =  simpleDateFormat2.format(now);
        String nowDateStr = nowmFormatStr.substring(0,11);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: now : "+nowmFormatStr);

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



    private void putCheckItems(View view) {
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
    }

    private void putStorePhoto(View view) {
        //放上店家照片from DB
        ImageView store_img = (ImageView) view.findViewById(R.id.store_img);
        String url = Common_RJ.URL + "StoreServlet";
        String store_id = store.getStore_id();
        int imageSize = 250;
        //執行拿照片
        Log.d(TAG, "onCreateView: before doing StoreGetImageTask");
        new StoreGetImageTask(store_img).execute(url,store_id,imageSize);
        Log.d(TAG, "onCreateView: after doing StoreGetImageTask");
    }

    private void myFavoriateFunction(View view) {
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
    }

    private void showToast(String s ) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


    private void getStoreVOList() {
        if(Common_RJ.networkConnected(getActivity())){
            String url = Common_RJ.URL+"StoreServlet";

            StoreGetAllTask task = new StoreGetAllTask();
            task.setListener(new StoreGetAllTask.Listener() {
                @Override
                public void onGetStoresDone(List<StoreVO> storeVOs) {
                    storeList = storeVOs;

                }
            });
            task.execute(url);
//                storeList = new StoreGetAllTask().execute(url).get();


            if(storeList == null || storeList.isEmpty()){
                Common_RJ.showToast(getActivity(),"No storeList found");
            }else{
                Common_RJ.showToast(getActivity(),"已有VO上的data開始連結view");
            }
        }
    }

    Map<String,String> item_price_map= new HashMap<>();
    Map<String,String> item_prodID_map = new HashMap<>();
    Map<String,Integer> item_count_map = new HashMap<>();

    private void getProductVOList(){
        String store_name = store.getStore_name();

        if(Common_RJ.networkConnected(getActivity())){
            String url = Common_RJ.URL+"ProductServlet";

            try {

                //得到DB資料
                productVOList = new StoreGetProductTask().execute(url,store_name).get();
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }

            if(productVOList == null || productVOList.isEmpty()){
                Common_RJ.showToast(getActivity(),"No productVOList found");
            }else{
                //前置作業 將可外帶外送的商品放入SpinnerView
                String[] items = new String[productVOList.size()];
                int i =0;
                for(ProductVO productVO : productVOList){
                    String product_name = productVO.getProd_name();
                    String product_price = String.valueOf(productVO.getProd_price());
                    String product_ID = productVO.getProd_id();



                    //順便把product_name:product_price 的Mapping 關係存放起來
                    item_price_map.put(product_name,product_price);

                    //再把product_name:product_ID的Mapping關係存起來
                    item_prodID_map.put(product_name,product_ID);

                    //將商品名稱指定給矩陣好放入spinnerView的adapter
                    items[i] = product_name;
                    i = i+1;

                }
                //把商品名稱放入spinner
                ArrayAdapter<String> adapterPlace = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, items);
                adapterPlace
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_item.setAdapter(adapterPlace);
                spinner_item.setSelection(0, true);


                shoopingVOList = new ArrayList<>();

                //按下選單後要發生的事
                Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //在listView 上新增選到的東西
                        item_selected = parent.getItemAtPosition(position).toString();
                        item_selected_price = item_price_map.get(item_selected);
                        item_selected_id = item_prodID_map.get(item_selected);

                        shoopingVOList.add(new ProductVO(item_selected,Integer.valueOf(item_selected_price),item_selected_id));




                        //顯示在listView上
                        lvStoreitem.setAdapter(new TakeOutItemAdapter(getActivity(), shoopingVOList));

                        //設定總價 = 取得現有所有單價並加總
                        temp_inttotal = temp_inttotal+Integer.parseInt(item_selected_price);
                        tvtotal.setText(String.valueOf(temp_inttotal));


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };
                //設定Listener在商品選單上
                spinner_item.setOnItemSelectedListener(listener);


                }
            }
        }


    private class TakeOutItemAdapter extends BaseAdapter{
        Context context;
        List<ProductVO> shoopingVOList;
        

        public TakeOutItemAdapter(Context context, List<ProductVO> shoopingVOList) {
            this.context = context;
            this.shoopingVOList = shoopingVOList;
        }

        @Override
        public int getCount() {

            return shoopingVOList.size();
        }

        @Override
        public Object getItem(int position) {
            return shoopingVOList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int number;


            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.rj_store_takeout_itemview,parent,false);
            }

            final ProductVO shopingVO = shoopingVOList.get(position);

            TextView tv_takeout_item_name = (TextView) convertView.findViewById(R.id.tv_takeout_item_name);
            tv_takeout_item_name.setText(shopingVO.getProd_name());

            final TextView tv_cup = (TextView) convertView.findViewById(R.id.tv_cup);
            tv_cup.setText(String.valueOf(shopingVO.getProd_price()));



            ImageView add_item_count = (ImageView) convertView.findViewById(R.id.add_item_count);
            ImageView minus_item_count = (ImageView) convertView.findViewById(R.id.minus_item_count);
            final TextView tvcountOfCup = (TextView) convertView.findViewById(R.id.countOfCup);
            final TextView tvsubtotal = (TextView) convertView.findViewById(R.id.subtotal);


            //Spinner選出來後預設一個item的價格為單價
            tvsubtotal.setText(String.valueOf(shopingVO.getProd_price()));


            final int price = shopingVO.getProd_price();
            //加減符號的設定
            add_item_count.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int countofcups = Integer.parseInt((tvcountOfCup.getText().toString()));
                    countofcups = countofcups +1;
                    tvcountOfCup.setText(String.valueOf(countofcups));

                    //單項商品小計  單價 x 杯數

                    int subtotal = price*countofcups;
                    tvsubtotal.setText(String.valueOf(subtotal));

                    temp_inttotal = temp_inttotal+price;
                    tvtotal.setText(String.valueOf(temp_inttotal));
                }
            });

            minus_item_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer countofcups = Integer.parseInt((tvcountOfCup.getText().toString()));
                    countofcups = countofcups-1;
                    tvcountOfCup.setText(String.valueOf(countofcups));

                    //單項商品小計  單價 x 杯數
                    int price = shopingVO.getProd_price();
                    int subtotal = price*countofcups;
                    tvsubtotal.setText(String.valueOf(subtotal));

                    temp_inttotal = temp_inttotal-price;
                    tvtotal.setText(String.valueOf(temp_inttotal));
                }
            });

            return convertView;
        }
    }









}
