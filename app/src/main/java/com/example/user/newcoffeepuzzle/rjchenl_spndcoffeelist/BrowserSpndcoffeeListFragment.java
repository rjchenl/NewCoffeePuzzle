package com.example.user.newcoffeepuzzle.rjchenl_spndcoffeelist;

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
import android.text.Html;
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
import com.example.user.newcoffeepuzzle.rjchenl_favoriatestore.getThisStoreInfoTask;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;
import com.example.user.newcoffeepuzzle.rjchenl_search.StoreFragment;
import com.example.user.newcoffeepuzzle.rjchenl_search.StoreVO;
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
 * Created by user on 2017/7/13.
 */

public class BrowserSpndcoffeeListFragment extends Fragment {
    private static final String TAG = "BrowserSpndcoffeeListFragment";
    private ListView spndList_view;
    private List<SpndcoffeeVO> coffeelist_value;
    private String mem_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rj_browserspndcoffeelist_fragment,container,false);
        spndList_view = (ListView) view.findViewById(R.id.browerspndcoffeelist_listview);
        //會用到mem_id 先取得
        Profile profile = new Profile(getContext());
        mem_id = profile.getMemId();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //取得物件資料
        getSpndDBdata();
        //將data與view做連結
        spndList_view.setAdapter(new BrowserSpndcoffeeAdapter(getActivity(),coffeelist_value));



    }

    private void getSpndDBdata() {
        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "SpndcoffeeServlet";
            coffeelist_value = null;
            try {

                //連結資料庫取得物件資料
                coffeelist_value = new BrowerSpndcoffeeGetAllTask().execute(url,mem_id).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (coffeelist_value == null || coffeelist_value.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No spndcoffeelist found");
            }
        }

    }

    private class BrowserSpndcoffeeAdapter extends BaseAdapter{
        Context context;
        List<SpndcoffeeVO> list;

        public BrowserSpndcoffeeAdapter(Context context, List<SpndcoffeeVO> list){
            this.context = context;
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
                convertView =layoutInflater.inflate(R.layout.rj_browserspndcoffeelist_item_fragment,parent,false);
            }

            //取到當下的VO物件;設定到view上
            final SpndcoffeeVO vo = list.get(position);

            //設定詳情
            TextView spndDetailInfo = (TextView) convertView.findViewById(R.id.spndDetailInfo);
            String htmlString="<u>詳情</u>";
            spndDetailInfo.setText(Html.fromHtml(htmlString));
            spndDetailInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StoreVO storevo = null;
                    //找這家店的資料
                    if(Common_RJ.networkConnected(getActivity())){
                        String url = Common_RJ.URL + "StoreServlet";
                        String store_id = vo.getStore_id();

                        try {
                            storevo = new getThisStoreInfoTask().execute(url,store_id).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //傳送資訊到店家fragment
                    Fragment storeFragment = new StoreFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("StoreVO",storevo);
                    storeFragment.setArguments(bundle);
                    switchFragment(storeFragment);
                }
            });

            TextView tv_spnd_id = (TextView) convertView.findViewById(R.id.tv_spnd_id);
            Log.d(TAG, "getView: vo.getSpnd_id() : "+vo.getSpnd_id());
            tv_spnd_id.setText(vo.getSpnd_id());
            TextView tv_store_name = (TextView) convertView.findViewById(R.id.tv_store_name);
            Log.d(TAG, "getView: vo.getStore_name(): "+vo.getStore_name());
            tv_store_name.setText(vo.getStore_name());
            TextView tv_spnd_name = (TextView) convertView.findViewById(R.id.tv_spnd_name);
            Log.d(TAG, "getView: vo.getSpnd_name() : "+vo.getSpnd_name());
            tv_spnd_name.setText(vo.getSpnd_name());
            TextView tv_spnd_prod = (TextView) convertView.findViewById(R.id.tv_spnd_prod);
            Log.d(TAG, "getView: vo.getSpnd_prod() : "+vo.getSpnd_prod());
            tv_spnd_prod.setText(vo.getSpnd_prod());
//            TextView spnd_enddate = (TextView) convertView.findViewById(R.id.spnd_enddate);
//            Log.d(TAG, "getView: vo.getSpnd_enddate() : "+vo.getSpnd_enddate());
//            spnd_enddate.setText(vo.getSpnd_enddate());
//            TextView tv_spnd_amt = (TextView) convertView.findViewById(R.id.tv_spnd_amt);
//            tv_spnd_amt.setText(vo.getSpnd_amt().toString());

            //多加mem_id到vo上
            vo.setMem_id(mem_id);


            Log.d(TAG, "getView: vo.getMem_id() : "+vo.getMem_id());

            //夾帶資訊到qrcode
            final Bundle bundle = new Bundle();
            bundle.putSerializable("vo",vo);

            //bt_attend
            //按下button顯示qr code 參加活動
            Button bt_attend = (Button) convertView.findViewById(R.id.bt_attend);
            bt_attend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AttendDialogFragment attend = new AttendDialogFragment();
                    attend.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    attend.show(fragmentManager,"show");
                }
            });
            return convertView;
        }

        private void switchFragment(Fragment fragment) {
            SearchActivity activity = (SearchActivity) getActivity();
            activity.switchFragment(fragment);
        }
    }

    public static class AttendDialogFragment extends DialogFragment{

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
            View view = inflater.inflate(R.layout.rj_attendspnd_dialoge,null);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Bundle bundle = getArguments();
            SpndcoffeeVO spndcoffeevo = (SpndcoffeeVO) bundle.getSerializable("vo");

            //for ming
            Profile profile = new Profile(getContext());
            spndcoffeevo.setMem_name(profile.getMem_name().toString());

            Gson gson = new Gson();
            String jsonin = gson.toJson(spndcoffeevo);

            //註冊離開button事件聆聽
            Button bt_toleave = (Button) view.findViewById(R.id.bt_toleave);
            bt_toleave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });




            //startpaste
            // QR code 的內容
            String QRCodeContent = jsonin;
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

                ImageView imgView = (ImageView) view.findViewById(R.id.attend_qr);
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
