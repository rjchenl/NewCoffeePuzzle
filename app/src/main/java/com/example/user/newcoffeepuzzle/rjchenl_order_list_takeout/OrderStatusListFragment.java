package com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/7/11.
 */

public class OrderStatusListFragment extends Fragment {
    private static final String TAG = "OrderStatusListFragment";
    List<OrderStatusVO> orderStatusVOList_value;
    private String mem_id;
    private ListView lvOrderStatusList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rj_orderstatuslist_fragment,container,false);
        lvOrderStatusList = (ListView) view.findViewById(R.id.lvOrderStatusList);

        //會用到mem_id 先取得
        Profile profile = new Profile(getContext());
        mem_id = profile.getMemId();



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDBdata();
        lvOrderStatusList.setAdapter(new OrderListStatusAdapter(getActivity(),orderStatusVOList_value));
    }

    private void getDBdata() {
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

            Log.d(TAG, "getView: orderStatusVO.getOrd_pick() : "+orderStatusVO.getOrd_pick());

            tv_ord_pick.setText(orderStatusVO.getOrd_pick().toString());
            TextView tv_ord_time = (TextView) convertView.findViewById(R.id.tv_ord_time);
            tv_ord_time.setText(orderStatusVO.getOrd_time().toString());
            TextView tv_ord_total = (TextView) convertView.findViewById(R.id.tv_ord_total);
            tv_ord_total.setText(orderStatusVO.getOrd_total().toString());
            TextView tv_ord_shipping = (TextView) convertView.findViewById(R.id.tv_ord_shipping);
            tv_ord_shipping.setText(orderStatusVO.getOrd_shipping().toString());

            final Bundle bundle = new Bundle();
            bundle.putSerializable("orderStatusVO",orderStatusVO);

            Button btn = (Button) convertView.findViewById(R.id.bt_showIDqrcode);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderListDialogFragment orderlistdialogfragment = new OrderListDialogFragment();
                    orderlistdialogfragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    orderlistdialogfragment.show(fragmentManager,"showQRcode");
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








}
