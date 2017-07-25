package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.DeleteFavoriateTask;
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.Fav_storeVO;
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.FavoriateStoreInsertTask;
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.InsertFavoriateTask;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Helper;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_main.Utility;
import com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout.OrderListInsertTask;
import com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout.OrderListVO;
import com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout.OrderdetailVO;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;



public class StoreFragment extends Fragment {

    private StoreVO store = null;
    private boolean isTodayOpen = false;
    private String mem_id;
    private Spinner spinner_item;
    final List<String> selectedItemList = new ArrayList<>();
    private Button btSubmit_buytakeout;
    private List<ProductVO> productVOList = null;
    private List<StoreVO> storeList = null;
    private ExpandableHeightListView lvStoreitem;
    private String item_selected;
    private String item_selected_price;
    private int temp_inttotal;
    private TextView tvtotal;
    private Button btSubmit_buytakeout1;
    private String item_selected_id;
    private ProductVO shopingVO;
    private List<OrderdetailVO> orderdetailvolist;
    private String mem_add;
    private EditText et_takeout_position;
    private int TotoalItemCount;
    private TextView tvTotalItemCount;
    private String isEsixt;
    private String isPattenExist;
    private ImageView image;
    private TakeOutItemAdapter listAdapter;
    private ImageView cart;


//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }



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

        //得到從marker傳過來的StoreVO物件
        Bundle bundle = getArguments();
        store = (StoreVO) bundle.getSerializable("StoreVO");
        store.getMin_order();
        isTodayOpenCheck();
        orderdetailvolist = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.rj_fragment_storeinfo, container, false);



        cart = (ImageView) view.findViewById(R.id.ivcart);

        takeOutFunction(view);
        putCheckItems(view);
        putStorePhoto(view);
        judgeMyFavoriateIsExist(view);
        NavigationFunction(view);


        getProductVOList(view);

        return view;
    }

    private void NavigationFunction(View view) {
        //導航功能
        Profile profile = new Profile(getActivity());
        final LatLng nowPosition = Helper.getLatLngByAddress(profile.getCurrentPosition());
        final LatLng storePosition = Helper.getLatLngByAddress(store.getStore_add());
        //取得現在位置
        ImageView navigation = (ImageView) view.findViewById(R.id.navigation);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direct(nowPosition.latitude,nowPosition.longitude,storePosition.latitude,storePosition.longitude);
            }
        });
    }

    private void direct(double fromLat, double fromLng, double toLat,
                        double toLng) {
        String uriStr = String.format(Locale.US,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", fromLat,
                fromLng, toLat, toLng);
        Intent intent = new Intent();
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uriStr));
        startActivity(intent);
    }

    private void takeOutFunction(View view) {
        spinner_item = (Spinner) view.findViewById(R.id.spinner_item);
        btSubmit_buytakeout = (Button) view.findViewById(R.id.btSubmit_buytakeout);
        lvStoreitem = (ExpandableHeightListView) view.findViewById(R.id.lvStoreitem);
        lvStoreitem.setExpanded(true);
        //貼的
//        lvStoreitem.setOnTouchListener(new ListView.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        // Disallow ScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        // Allow ScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//
//                // Handle ListView touch events.
//                v.onTouchEvent(event);
//                return true;
//            }
//        });
        //方法二
//        View header = getLayoutInflater().inflate(R.layout.header, null);
//        View footer = getLayoutInflater().inflate(R.layout.footer, null);
//        lvStoreitem.addHeaderView(header);
//        lvStoreitem.addFooterView(footer);







        //網路上抄的
//        lvStoreitem.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        StoreFragment.setListViewHeightBasedOnChildren(lvStoreitem);

//        ExpandableListView: view = listAdapter.getView(0, view, lvStoreitem);
//        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(1073741823 , View.MeasureSpec.EXACTLY);
//        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(1073741823, View.MeasureSpec.EXACTLY);
//        view.measure(widthMeasureSpec, heightMeasureSpec);




        tvtotal = (TextView) view.findViewById(R.id.tvtotal);
        tvTotalItemCount = (TextView) view.findViewById(R.id.tvTotalItemCount);

        //設定外送地點預設為 會員註冊地址
        et_takeout_position = (EditText) view.findViewById(R.id.et_takeout_position);
        final Profile profile = new Profile(getActivity());
        et_takeout_position.setText(profile.getMem_add());

        //按下"使用現在位置按紐"
        View bt_useCurrentGPS_position = view.findViewById(R.id.bt_useCurrentGPS_position);
        bt_useCurrentGPS_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_takeout_position.setText(profile.getCurrentPosition());
            }
        });


        //按下送出選單後
        btSubmit_buytakeout1 = (Button) view.findViewById(R.id.btSubmit_buytakeout);
        onClickSubmitButton();
    }

    private void judgeMyFavoriateIsExist(View view) {

        image = (ImageView) view.findViewById(R.id.image);
        final String url = Common_RJ.URL + "fav_storeServlet";

        final String store_id = store.getStore_id();

        //查看這家店家和此使用者組合 是否存在在fav_store table中
        try {
            isPattenExist = new CheckMemStoreCombinationTask().execute(url,mem_id,store_id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //若此組合已存在的話boolean = true   設定image 為實心  再設定onclick會delete 資料  image 會轉空心
        if(isPattenExist.equals("true")){

            image.setImageResource(R.drawable.like);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkFavoriate(url, store_id);
                }
            });


            //沒有存在的話boolean = false  設定image 為空心  再設定onclick會insert 資料  Image 轉實心
        }else if(isPattenExist.equals("false")){

            image.setImageResource(R.drawable.unlike);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //insert 資料 image 轉實心
                    checkFavoriate(url, store_id);
                }
            });
        }
    }

    private void checkFavoriate(String url, String store_id) {
        try {
            isPattenExist = new CheckMemStoreCombinationTask().execute(url,mem_id,store_id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isPattenExist.equals("true")){
            try {
                new DeleteFavoriateTask().execute(url,mem_id,store_id).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            image.setImageResource(R.drawable.unlike);
            showToast("已刪除此店家收藏!!");
        }else{
            try {
                Fav_storeVO fav_storevo= new Fav_storeVO();
                fav_storevo.setMem_id(mem_id);
                fav_storevo.setStore_id(store_id);
                new InsertFavoriateTask().execute(url,fav_storevo).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            image.setImageResource(R.drawable.like);
            showToast("已新增此店家收藏!!");
        }
    }

    private void onClickSubmitButton() {


        btSubmit_buytakeout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderdetailvolist != null && !(orderdetailvolist.isEmpty())
                        && ((TotoalItemCount > store.getMin_order()) || (TotoalItemCount == store.getMin_order()))) {

                    //新增一筆task新增訂單
                    String url = Common_RJ.URL + "OrderlistServlet";
                    //得到mem_id
                    Profile profile = new Profile(getActivity());
                    mem_id = profile.getMemId();
                    //得到store_id
                    String store_id = store.getStore_id();
                    //得到總金額
                    int ord_total = temp_inttotal;
                    int ord_pick = 3;
                    int ord_shipping = 1;
                    String ord_time = new Timestamp(System.currentTimeMillis()).toString();
                    Log.d(TAG, "onClick: Timestamp :"+String.valueOf(ord_time));
                    //新增地址
                    String ord_add = et_takeout_position.getText().toString();
                    int score_seller = 1;

                    OrderListVO orderlistvo = new OrderListVO(mem_id, store_id, ord_total, ord_pick, ord_add, ord_shipping, ord_time, score_seller);
                    showToast("新增訂單成功!!!");
                    //新增訂單詳情列表

                    try {

                        //送出訂單 同時送出 訂單名細
                        new OrderListInsertTask().execute(url, orderlistvo, orderdetailvolist).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("購買數量未達外送門檻 :" + store.getMin_order() + "份");
                }

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        getStoreVOList();


    }


    private void isTodayOpenCheck() {


        //判斷今日是否營業

        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "isTodayOpenCheck: weekDay:"+weekDay);
        if (store != null) {

            if (weekDay   == Calendar.WEDNESDAY) {

                if (store != null && store.getWed_isopen() != null) {
                    int open = store.getWed_isopen();
                    if (open == 1) {
                        Timestamp opentime = store.getWed_open();
                        Timestamp closetime = store.getWed_close();
                        isTodayOpenCovertToTimestmp(opentime, closetime);
                    }
                }
            } else if (weekDay == Calendar.TUESDAY) {
                Log.d(TAG, "isTodayOpenCheck: step1");
                if (store.getTue_isopen() != null && store != null) {
                    Log.d(TAG, "isTodayOpenCheck: step2");
                    int open = store.getTue_isopen();
                    if (open == 1) {
                        Log.d(TAG, "isTodayOpenCheck: step3");
                        Timestamp opentime = store.getTue_open();
                        Timestamp closetime = store.getTue_close();
                        isTodayOpenCovertToTimestmp(opentime, closetime);
                        Log.d(TAG, "isTodayOpenCheck: step4");
                    }

                }

            } else if (weekDay == Calendar.FRIDAY) {
                if (store.getFri_isopen() != null && store != null) {
                    int open = store.getFri_isopen();
                    if (open == 1) {
                        Timestamp opentime = store.getFri_open();
                        Timestamp closetime = store.getFri_close();
                        isTodayOpenCovertToTimestmp(opentime, closetime);
                    }
                }

            } else if (weekDay == Calendar.SATURDAY) {
                if (store.getSat_isopen() != null && store != null) {
                    int open = store.getSat_isopen();
                    if (open == 1) {
                        Timestamp opentime = store.getSat_open();
                        Timestamp closetime = store.getSat_close();
                        isTodayOpenCovertToTimestmp(opentime, closetime);
                    }
                }

            } else if (weekDay == Calendar.SUNDAY) {
                if (store.getSun_isopen() != null && store != null) {
                    int open = store.getSun_isopen();
                    if (open == 1) {
                        Timestamp opentime = store.getSun_open();
                        Timestamp closetime = store.getSun_close();
                        isTodayOpenCovertToTimestmp(opentime, closetime);
                    }
                }
            } else if (weekDay == Calendar.MONDAY) {
                if (store.getMon_isopen() != null && store != null) {
                    int open = store.getMon_isopen();
                    if (open == 1) {
                        Timestamp opentime = store.getMon_open();
                        Timestamp closetime = store.getMon_close();
                        isTodayOpenCovertToTimestmp(opentime, closetime);
                    }
                }
            } else if (weekDay == Calendar.THURSDAY) {
                if (store.getThu_isopen() != null && store != null) {
                    int open = store.getThu_isopen();
                    if (open == 1) {
                        Timestamp opentime = store.getThu_open();
                        Timestamp closetime = store.getThu_close();
                        isTodayOpenCovertToTimestmp(opentime, closetime);
                    }
                }
            }
        }
    }


    public boolean isTodayOpenCovertToTimestmp(Timestamp opentime, Timestamp closetime) {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //取得現在時間的日期STR
        String nowmFormatStr = simpleDateFormat2.format(now);
        String nowDateStr = nowmFormatStr.substring(0, 11);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: now : " + nowmFormatStr);

        //取得營業時間後面時間STR
        String openFormatStr = simpleDateFormat2.format(opentime);
        String opensub = openFormatStr.substring(11);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: opensub:"+opensub);

        String closeFormatStr = simpleDateFormat2.format(closetime);
        String closesub = closeFormatStr.substring(11);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: closesub:"+closesub);

        //組合成timestamp物件
        String openformatset = nowDateStr + opensub;
        Log.d(TAG, "isTodayOpenCovertToTimestmp: openformatset:"+openformatset);
        Timestamp opentimestamp = Timestamp.valueOf(openformatset);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: opentimestamp:"+opentimestamp);

        String closeformatset = nowDateStr + closesub;
        Log.d(TAG, "isTodayOpenCovertToTimestmp: closeformatset:"+closeformatset);
        Timestamp closetimestamp = Timestamp.valueOf(closeformatset);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: closetimestamp:"+closetimestamp);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: opentimestamp.getTime():"+opentimestamp.getTime());
        Log.d(TAG, "isTodayOpenCovertToTimestmp: now:"+now);
        Log.d(TAG, "isTodayOpenCovertToTimestmp: closetimestamp.getTime():"+closetimestamp.getTime());



        if (now > opentimestamp.getTime() && now < closetimestamp.getTime()) {
            isTodayOpen = true;
            Log.d(TAG, "isTodayOpenCovertToTimestmp: isTodayOpen:"+isTodayOpen);
        }

        return isTodayOpen;
    }


    private void putCheckItems(View view) {
        if (store != null) {
            //放上店家名稱
            TextView store_name = (TextView) view.findViewById(R.id.store_name);
            store_name.setText(store.getStore_name());
            //放上今日是否營業
            CheckBox isOpen = (CheckBox) view.findViewById(R.id.isOpen);
            isOpen.setChecked(isTodayOpen);
//            isOpen.setEnabled(false);
            //放上資料庫的地址
            TextView store_add = (TextView) view.findViewById(R.id.store_add);
            String storeAddress = store.getStore_add();
            store_add.setText(storeAddress);
            //放上是否有wifi
            CheckBox is_wifi = (CheckBox) view.findViewById(R.id.is_wifi);
            if (store.getIs_wifi() == 1) {
                is_wifi.setChecked(true);
//                is_wifi.setEnabled(false);
            }
            //放上是否有插座
            CheckBox is_plug = (CheckBox) view.findViewById(R.id.is_plug);
            if (store.getIs_plug() == 1) {
                is_plug.setChecked(true);
//                is_plug.setEnabled(false);
            }
            //放上是否限時
            CheckBox is_time_limit = (CheckBox) view.findViewById(R.id.is_time_limit);
            if (store.getIs_time_limit() == 1) {
                is_time_limit.setChecked(true);
//                is_time_limit.setEnabled(false);
            }
            //放上是否賣正餐
            CheckBox is_meal = (CheckBox) view.findViewById(R.id.is_meal);
            if (store.getIs_meal() == 1) {
                is_meal.setChecked(true);
//                is_meal.setEnabled(false);
            }
            //放上是否賣甜點
            CheckBox is_dessert = (CheckBox) view.findViewById(R.id.is_dessert);
            if (store.getIs_dessert() == 1) {
                is_dessert.setChecked(true);
//                is_dessert.setEnabled(false);
            }
        }
    }

    private void putStorePhoto(View view) {
        if (store != null) {
            //放上店家照片from DB
            ImageView store_img = (ImageView) view.findViewById(R.id.store_img);
            String url = Common_RJ.URL + "StoreServlet";
            String store_id = store.getStore_id();
            int imageSize = 250;
            //執行拿照片
            new StoreGetImageTask(store_img).execute(url, store_id, imageSize);
        }
    }



    private void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


    private void getStoreVOList() {
        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "StoreServlet";

//            StoreGetAllTask task = new StoreGetAllTask();
//            task.setListener(new StoreGetAllTask.Listener() {
//                @Override
//                public void onGetStoresDone(List<StoreVO> storeVOs) {
//                    storeList = storeVOs;
//
//                }
//            });
//            task.execute(url);
            try {
                storeList = new StoreGetAllTask().execute(url).get();
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (storeList == null || storeList.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No storeList found");
            } else {
//                Common_RJ.showToast(getActivity(), "已有VO上的data開始連結view");
            }
        }
    }

    Map<String, String> item_price_map = new HashMap<>();
    Map<String, String> item_prodID_map = new HashMap<>();

    private TranslateAnimation getShakeAnimation() {
        TranslateAnimation shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
        shakeAnimation.setDuration(1000);
        CycleInterpolator cycleInterpolator = new CycleInterpolator(7);
        shakeAnimation.setInterpolator(cycleInterpolator);
        return shakeAnimation;
    }


    private void getProductVOList(View view) {
        String store_name = store.getStore_name();

        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "ProductServlet";

            try {

                //得到DB資料
                productVOList = new StoreGetProductTask().execute(url, store_name).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (productVOList == null || productVOList.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No productVOList found");
                //把該隱藏的物件隱藏起來
                LinearLayout takeout_position_linearlayout = (LinearLayout) view.findViewById(R.id.takeout_position_linearlayout);
                LinearLayout takeout_item_layout = (LinearLayout) view.findViewById(R.id.takeout_item_layout);
                takeout_position_linearlayout.setVisibility(View.GONE);
                takeout_item_layout.setVisibility(View.GONE);












            } else {
                //前置作業 將可外帶外送的商品放入SpinnerView
                String[] items = new String[productVOList.size()];
                int i = 0;
                for (ProductVO productVO : productVOList) {
                    String product_name = productVO.getProd_name();
                    String product_price = String.valueOf(productVO.getProd_price());
                    String product_ID = productVO.getProd_id();


                    //順便把product_name:product_price 的Mapping 關係存放起來
                    item_price_map.put(product_name, product_price);

                    //再把product_name:product_ID的Mapping關係存起來
                    item_prodID_map.put(product_name, product_ID);


                    //將商品名稱指定給矩陣好放入spinnerView的adapter
                    items[i] = product_name + "  price : "+product_price;
//                    items[i] = product_name;
                    i = i + 1;

                }
                //把商品名稱放入spinner
                ArrayAdapter<String> adapterPlace = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, items);


                adapterPlace
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_item.setAdapter(adapterPlace);
                spinner_item.setSelection(0, true);


                //按下選單後要發生的事
                Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //在listView 上新增選到的東西
//                        item_selected = parent.getItemAtPosition(position).toString();
                        String[] item_selected_array =
                                parent.getItemAtPosition(position).toString().split("  price : ");
                        for(String str : item_selected_array){
                            Log.d(TAG, "onItemSelected: "+str);
                        }
                        //按下後有動畫
                        Log.d(TAG, "onItemSelected: cart_rj:"+cart);
                        if(cart == null){
                            Log.d(TAG, "onItemSelected: cart_rj is null");
                        }
                        cart.startAnimation(getShakeAnimation());

                        item_selected = item_selected_array[0];
                        item_selected_price = item_price_map.get(item_selected);
                        item_selected_id = item_prodID_map.get(item_selected);
                        //舊的VO值要保留


                        //ordertail
                        OrderdetailVO orderdetailvo = new OrderdetailVO();
                        orderdetailvo.setProd_id(item_selected_id);
                        orderdetailvo.setProd_name(item_selected);
                        orderdetailvo.setProd_price(Integer.parseInt(item_selected_price));
                        orderdetailvo.setDetail_amt(1);
                        Integer orderDetail_amt = orderdetailvo.getDetail_amt();


                        //放入orderdetailvolist

                        orderdetailvolist.add(orderdetailvo);

                        //顯示在listView上
                        listAdapter = new TakeOutItemAdapter(getActivity(), orderdetailvolist);

                        lvStoreitem.setAdapter(listAdapter);


                        //設定總價 = 取得現有所有單價並加總
                        temp_inttotal = temp_inttotal + Integer.parseInt(item_selected_price) * orderDetail_amt;
                        tvtotal.setText(String.valueOf(temp_inttotal));

                        //取得所有商品數目加總
                        TotoalItemCount = TotoalItemCount + orderDetail_amt;
                        tvTotalItemCount.setText(String.valueOf(TotoalItemCount));


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



    private class TakeOutItemAdapter extends BaseAdapter {
        Context context;
        List<OrderdetailVO> orderdetailvolist;


        public TakeOutItemAdapter(Context context, List<OrderdetailVO> orderdetailvolist) {
            this.context = context;
            this.orderdetailvolist = orderdetailvolist;
        }

        @Override
        public int getCount() {

            return orderdetailvolist.size();
        }

        @Override
        public Object getItem(int position) {
            return orderdetailvolist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.rj_store_takeout_itemview, parent, false);
            }

            final OrderdetailVO orderdetailvo = orderdetailvolist.get(position);

            //設定商品名稱
            TextView tv_takeout_item_name = (TextView) convertView.findViewById(R.id.tv_takeout_item_name);
            tv_takeout_item_name.setText(orderdetailvo.getProd_name());
            //設定商品單價
            final TextView tv_cup = (TextView) convertView.findViewById(R.id.tv_cup);
            tv_cup.setText(String.valueOf(orderdetailvo.getProd_price()));


            ImageView add_item_count = (ImageView) convertView.findViewById(R.id.add_item_count);
            ImageView minus_item_count = (ImageView) convertView.findViewById(R.id.minus_item_count);
            //杯數tv
            final TextView tvcountOfCup = (TextView) convertView.findViewById(R.id.countOfCup);
            //舊的杯數要設上
            tvcountOfCup.setText(orderdetailvo.getDetail_amt().toString());
            //小計tv
            final TextView tvsubtotal = (TextView) convertView.findViewById(R.id.subtotal);


            //Spinner選出來後預設一個item的價格為單價
            tvsubtotal.setText(String.valueOf(orderdetailvo.getProd_price() * orderdetailvo.getDetail_amt()));


            final int price = orderdetailvo.getProd_price();


            //加號的設定
            add_item_count.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TotoalItemCount = TotoalItemCount + 1;
                    tvTotalItemCount.setText(String.valueOf(TotoalItemCount));
                    int count = orderdetailvo.getDetail_amt();
                    count = count + 1;
                    orderdetailvo.setDetail_amt(count);
                    //設定view杯數
                    tvcountOfCup.setText(String.valueOf(count));


                    //單項商品小計  單價 x 杯數
                    int subtotal = price * count;
                    //設定view小計值
                    tvsubtotal.setText(String.valueOf(subtotal));

                    temp_inttotal = temp_inttotal + price;
                    //設定總額
                    tvtotal.setText(String.valueOf(temp_inttotal));

                }
            });
            //減號設定
            minus_item_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int count = orderdetailvo.getDetail_amt();
                    if (count != 1 && TotoalItemCount != 1) {
                        //購買總量
                        TotoalItemCount = TotoalItemCount - 1;
                        tvTotalItemCount.setText(String.valueOf(TotoalItemCount));
                        //該商品數量
                        count = count - 1;
                        orderdetailvo.setDetail_amt(count);
                        tvcountOfCup.setText(String.valueOf(count));


                        //單項商品小計  單價 x 杯數
                        int subtotal = price * count;
                        tvsubtotal.setText(String.valueOf(subtotal));

                        temp_inttotal = temp_inttotal - price;
                        tvtotal.setText(String.valueOf(temp_inttotal));
                    } else {
                        //將此商品Delete
                        orderdetailvolist.remove(orderdetailvo);
                        //總數/總價跟著變
                        TotoalItemCount = TotoalItemCount-1;
                        tvTotalItemCount.setText(String.valueOf(TotoalItemCount));
                        temp_inttotal = temp_inttotal-price;
                        tvtotal.setText(String.valueOf(temp_inttotal));


                        notifyDataSetChanged();
                    }
                }
            });
            return convertView;
        }
    }

}
