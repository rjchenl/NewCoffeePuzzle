package com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/7/11.
 */

public class OrderStatusListFragment extends Fragment {
    private static final String TAG = "OrderStatusListFragment";
    List<OrderStatusVO> orderStatusVOList_value;
    List<OrderStatusVO> orderStatusVOList_status_unhandle;
    List<OrderStatusVO> orderStatusVOList_status_cancle;
    List<OrderStatusVO> orderStatusVOList_status_accept;
    List<OrderStatusVO> orderStatusVOList_status_shipped;
    List<OrderStatusVO> orderStatusVOList_status_complete;
    private String mem_id;
    private ListView lvOrderStatusList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rj_orderstatuslist_fragment,container,false);
        lvOrderStatusList = (ListView) view.findViewById(R.id.lvOrderStatusList);

        getOrderListDBdata();
        //開始分類
        Log.d(TAG, "onCreateView: 分類之前");
        orderStatusVOList_status_unhandle = new ArrayList<>();
        orderStatusVOList_status_cancle = new ArrayList<>();
        orderStatusVOList_status_accept = new ArrayList<>();
        orderStatusVOList_status_shipped = new ArrayList<>();
        orderStatusVOList_status_complete = new ArrayList<>();

        //開始分類
        for(OrderStatusVO orderstatusvo : orderStatusVOList_value){
            Log.d(TAG, "onCreateView: orderstatusvo.getOrd_add():"+orderstatusvo.getOrd_add());
            Log.d(TAG, "onCreateView: orderstatusvo.getStore_id():"+orderstatusvo.getStore_id());
            if(orderstatusvo.getOrd_shipping() == 1){

                orderStatusVOList_status_unhandle.add(orderstatusvo);

            }else if(orderstatusvo.getOrd_shipping() == 2){

                orderStatusVOList_status_cancle.add(orderstatusvo);
            }else if(orderstatusvo.getOrd_shipping() == 3){

                orderStatusVOList_status_accept.add(orderstatusvo);
            }else if(orderstatusvo.getOrd_shipping() == 4){

                orderStatusVOList_status_shipped.add(orderstatusvo);

            }else if(orderstatusvo.getOrd_shipping() == 5){

                orderStatusVOList_status_complete.add(orderstatusvo);
            }





        }

        //預設選擇"未處理"的訂單
        RadioButton rb_unhandle = (RadioButton) view.findViewById(R.id.rb_unhandle);
        rb_unhandle.setChecked(true);

        RadioGroup rgOrderStatus = (RadioGroup) view.findViewById(R.id.rgOrderStatus);
        rgOrderStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String clickString = radioButton.getText().toString();
                Log.d(TAG, "onCheckedChanged: clickString :"+clickString);
                switch (clickString){
                    case "未處理" :
                        Log.d(TAG, "onCheckedChanged: enter 未處理");
                        Log.d(TAG, "onCheckedChanged: orderStatusVOList_status_unhandle :"+orderStatusVOList_status_unhandle);
                        lvOrderStatusList.setAdapter(new OrderListStatusAdapter(getActivity(),orderStatusVOList_status_unhandle));
                        break;
                    case "不接單" :
                        Log.d(TAG, "onCheckedChanged: enter 不接單");
                        Log.d(TAG, "onCheckedChanged: orderStatusVOList_status_cancle:"+orderStatusVOList_status_cancle);
                        lvOrderStatusList.setAdapter(new OrderListStatusAdapter(getActivity(),orderStatusVOList_status_cancle));
                        break;
                    case "已接單":
                        Log.d(TAG, "onCheckedChanged: enter 已接單");
                        Log.d(TAG, "onCheckedChanged: orderStatusVOList_status_accept:"+orderStatusVOList_status_accept);
                        lvOrderStatusList.setAdapter(new OrderListStatusAdapter(getActivity(),orderStatusVOList_status_accept));
                        break;
                    case "已出貨":
                        Log.d(TAG, "onCheckedChanged: enter 已出貨");
                        Log.d(TAG, "onCheckedChanged: orderStatusVOList_status_shipped :"+orderStatusVOList_status_shipped);
                        lvOrderStatusList.setAdapter(new OrderListStatusAdapter(getActivity(),orderStatusVOList_status_shipped));
                        break;
                    case "完成訂單":
                        Log.d(TAG, "onCheckedChanged: enter 完成訂單");
                        Log.d(TAG, "onCheckedChanged: orderStatusVOList_status_complete :"+orderStatusVOList_status_complete);
                        lvOrderStatusList.setAdapter(new OrderListStatusAdapter(getActivity(),orderStatusVOList_status_complete));
                        break;
                    default :
                        Log.d(TAG, "onCheckedChanged: run default");

                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        lvOrderStatusList.setAdapter(new OrderListStatusAdapter(getActivity(),orderStatusVOList_status_unhandle));
    }

    private void getOrderListDBdata() {

        //會用到mem_id 先取得
        Profile profile = new Profile(getContext());
        mem_id = profile.getMemId();

        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "OrderlistServlet";
            orderStatusVOList_value = null;
            try {

                //連結資料庫取得物件資料
                orderStatusVOList_value = new OrderStatusListGetTask().execute(url,mem_id).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (orderStatusVOList_value == null || orderStatusVOList_value.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No spndcoffeelist found");
            }
        }

    }

    private class OrderListStatusAdapter extends BaseAdapter{
        Context context;
        List<OrderStatusVO> orderStatusVOList;

        public OrderListStatusAdapter(Context context, List<OrderStatusVO> orderStatusVOList) {
            this.context = context;
            this.orderStatusVOList = orderStatusVOList;
        }

        @Override
        public int getCount() {
            return orderStatusVOList.size();
        }

        @Override
        public Object getItem(int position) {
            return orderStatusVOList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView =layoutInflater.inflate(R.layout.rj_orderstatuslist_item_fragment,parent,false);
            }
            //取到當下那個VO物件
            OrderStatusVO orderStatusVO = orderStatusVOList.get(position);

            //將抓到的view 設上其值
            TextView tv_ord_id = (TextView) convertView.findViewById(R.id.tv_ord_id);
            tv_ord_id.setText(orderStatusVO.getOrd_id());
            TextView tv_store_name = (TextView) convertView.findViewById(R.id.tv_store_name);
            tv_store_name.setText(orderStatusVO.getStore_name());
            TextView tv_ord_pick = (TextView) convertView.findViewById(R.id.tv_ord_pick);
            switch (orderStatusVO.getOrd_pick()){
                case 1:
                    tv_ord_pick.setText("購物");
                    break;
                case 2:
                    tv_ord_pick.setText("外帶");
                    break;
                case 3:
                    tv_ord_pick.setText("外送");
                    break;
                default:
                    tv_ord_pick.setText("無法歸類");
                    break;
            }


            //領取QR CODE BUTTON設定
            Button btn = (Button) convertView.findViewById(R.id.bt_showIDqrcode);
            final Bundle bundle = new Bundle();
            bundle.putSerializable("orderStatusVO",orderStatusVO);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderListDialogFragment orderlistdialogfragment = new OrderListDialogFragment();
                    orderlistdialogfragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    orderlistdialogfragment.show(fragmentManager,"showQRcode");
                }
            });


            TextView tv_ord_time = (TextView) convertView.findViewById(R.id.tv_ord_time);
            tv_ord_time.setText(orderStatusVO.getOrd_time().toString());
            TextView tv_ord_total = (TextView) convertView.findViewById(R.id.tv_ord_total);
            tv_ord_total.setText(orderStatusVO.getOrd_total().toString());
            TextView tv_ord_shipping = (TextView) convertView.findViewById(R.id.tv_ord_shipping);
            switch (orderStatusVO.getOrd_shipping()){
                case 1:
                    tv_ord_shipping.setText("未處理");
                    btn.setVisibility(View.GONE);
                    break;
                case 2 :
                    tv_ord_shipping.setText("審核此筆交易失敗");
                    btn.setVisibility(View.GONE);
                    break;
                case 3:
                    tv_ord_shipping.setText("已接單");
                    btn.setVisibility(View.GONE);
                    break;
                case 4:
                    tv_ord_shipping.setText("已出貨");
                    break;
                case 5:
                    tv_ord_shipping.setText("交易完成");
                    btn.setVisibility(View.GONE);
                    break;
                default:
                    tv_ord_shipping.setText("無法歸類");
                    break;
            }
//            tv_ord_shipping.setText(orderStatusVO.getOrd_shipping().toString());




            //點選詳情按紐時
            final Bundle bundle2 = new Bundle();
            String ord_id = orderStatusVO.getOrd_id();
            bundle2.putString("ord_id",ord_id);


            Button checkDetailInfo = (Button) convertView.findViewById(R.id.checkDetailInfo);
            checkDetailInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    OrderdetailListFragment ordedetailfragment = new OrderdetailListFragment();
//                    ordedetailfragment.setArguments(bundle2);


//                    FragmentTransaction  fragmentTransaction = getFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.body,ordedetailfragment,"fragment");
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                    OrderDetailDialogFragment orderdetaildialogfragment = new OrderDetailDialogFragment();
                    orderdetaildialogfragment.setArguments(bundle2);
                    FragmentManager fragmentManager = getFragmentManager();
                    orderdetaildialogfragment.show(fragmentManager,"show detail");
                }
            });

            return convertView;
        }

    }


    public static class OrderListDialogFragment extends DialogFragment{

        private String jsonstr;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            //不顯示標題
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.rj_orderstatuslist_qrcode_dialogefragment,container,false);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            Bundle bundle = getArguments();
            OrderStatusVO orderStatusVO = (OrderStatusVO) bundle.getSerializable("orderStatusVO");

            //for ming
            Profile profile = new Profile(getContext());
            orderStatusVO.setMem_name(profile.getMem_name().toString());
            Gson gson = new Gson();
            jsonstr = gson.toJson(orderStatusVO);



            putQRcodeImage(view);

            //註冊離開button事件聆聽
            Button btleave = (Button) view.findViewById(R.id.dialoge_btleave);
            btleave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


        }

        private void putQRcodeImage(View view) {

            //startpaste
            // QR code 的內容
            String QRCodeContent = jsonstr;
            // QR code 寬度
            int QRCodeWidth = 800;
            // QR code 高度
            int QRCodeHeight = 800;
            // QR code 內容編碼
            Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            MultiFormatWriter writer = new MultiFormatWriter();
            try
            {

                String url = "https://www.google.com.tw/";
                // 容錯率姑且可以將它想像成解析度，分為 4 級：L(7%)，M(15%)，Q(25%)，H(30%)
                // 設定 QR code 容錯率為 H
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

                // 建立 QR code 的資料矩陣
                BitMatrix result = writer.encode(QRCodeContent, BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight, hints);
                // ZXing 還可以生成其他形式條碼，如：BarcodeFormat.CODE_39、BarcodeFormat.CODE_93、BarcodeFormat.CODE_128、BarcodeFormat.EAN_8、BarcodeFormat.EAN_13...

                //建立點陣圖
                Bitmap bitmap = Bitmap.createBitmap(QRCodeWidth, QRCodeHeight, Bitmap.Config.ARGB_8888);
                // 將 QR code 資料矩陣繪製到點陣圖上
                for (int y = 0; y<QRCodeHeight; y++)
                {
                    for (int x = 0;x<QRCodeWidth; x++)
                    {
                        bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                ImageView imgView = (ImageView) view.findViewById(R.id.orderlist_qrcode);
                // 設定為 QR code 影像
                imgView.setImageBitmap(bitmap);
            }
            catch (WriterException e)
            {

                e.printStackTrace();
            }
        }
    }


    public static class OrderDetailDialogFragment extends DialogFragment{

        List<OrderdetailVO> orderdetailVO_list;
        private ListView lvorderdetaillist;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            //不顯示標題
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.rj_orderlist_dialoge_fragmen,container,false);
            lvorderdetaillist = (ListView) view.findViewById(R.id.lvorderdetaillist);
            getOrdertailVO_DBdata();
            lvorderdetaillist.setAdapter(new OrderDetailItemAdapter(getActivity(),orderdetailVO_list));

            return view;
        }

        private void getOrdertailVO_DBdata() {
            Bundle bundle = getArguments();
            String ord_id = bundle.getString("ord_id");


            if (Common_RJ.networkConnected(getActivity())) {
                String url = Common_RJ.URL + "OrderdetailServlet_RJ";
                orderdetailVO_list = null;
                try {

                    //連結資料庫取得物件資料
                    orderdetailVO_list = new OrderDetailGetTask().execute(url,ord_id).get();
                } catch (Exception e) {

                }
                if (orderdetailVO_list == null || orderdetailVO_list.isEmpty()) {
                    Common_RJ.showToast(getActivity(), "No spndcoffeelist found");
                }
            }
        }

        public class OrderDetailItemAdapter extends BaseAdapter{
            Context context;
            List<OrderdetailVO> OrderdetailVO_List;

            public OrderDetailItemAdapter(Context context,List<OrderdetailVO> OrderdetailVO_List){
                this.context = context;
                this.OrderdetailVO_List = OrderdetailVO_List;
            }

            @Override
            public int getCount() {
                return OrderdetailVO_List.size();
            }

            @Override
            public Object getItem(int position) {
                return OrderdetailVO_List.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null){
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    convertView =layoutInflater.inflate(R.layout.rj_orderdetail_item_info_fragment,parent,false);
                }


                Log.d(TAG, "getView: here");
                OrderdetailVO orderdetailvo = OrderdetailVO_List.get(position);
                Log.d(TAG, "getView: orderdetailvo.getProd_name() : "+orderdetailvo.getProd_name());
                Log.d(TAG, "getView: orderdetailvo.getProd_price().toString() : "+orderdetailvo.getProd_price().toString());
                Log.d(TAG, "getView: orderdetailvo.getDetail_amt().toString() : "+orderdetailvo.getDetail_amt().toString());

                TextView tvprod_name = (TextView) convertView.findViewById(R.id.tvprod_name);
                Log.d(TAG, "getView: here2");
                tvprod_name.setText(orderdetailvo.getProd_name());
                TextView tvprod_price = (TextView) convertView.findViewById(R.id.tvprod_price);
                tvprod_price.setText(orderdetailvo.getProd_price().toString());
                TextView tvdetail_amt = (TextView) convertView.findViewById(R.id.tvdetail_amt);
                tvdetail_amt.setText(orderdetailvo.getDetail_amt().toString());




                return convertView;
            }
        }


    }





}
