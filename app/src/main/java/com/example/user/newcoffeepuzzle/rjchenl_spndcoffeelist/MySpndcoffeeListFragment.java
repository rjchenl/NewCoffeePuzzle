package com.example.user.newcoffeepuzzle.rjchenl_spndcoffeelist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.example.user.newcoffeepuzzle.rjchenl_main.Helper;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;
import com.example.user.newcoffeepuzzle.rjchenl_search.StoreFragment;
import com.example.user.newcoffeepuzzle.rjchenl_search.StoreVO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by user on 2017/7/1.
 */

public class MySpndcoffeeListFragment extends Fragment {
    private static final String TAG = "MySpndcoffeeListFragment";
    private ListView spndList_view;
    private List<SpndcoffeelistVO> spndcoffeelist_value;
    private String mem_id;
    private  Profile profile;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CODE_RESOLUTION = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.rj_spndcoffeelist_fragment,container,false);

        spndList_view = (ListView) view.findViewById(R.id.lvSpndcoffeelist);

        //會用到mem_id 先取得
        profile = new Profile(getContext());
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

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallbacks)
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .build();
        }
        googleApiClient.connect();
    }
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult result) {
                    if (!result.hasResolution()) {
                        GoogleApiAvailability.getInstance().getErrorDialog(
                                getActivity(),
                                result.getErrorCode(),
                                0
                        ).show();
                        return;
                    }
                    try {
                        result.startResolutionForResult(
                                getActivity(),
                                REQUEST_CODE_RESOLUTION);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Exception while starting resolution activity");
                    }
                }
            };

    private class SpndCoffeeListAdapter extends BaseAdapter{
        Context context;
        List<SpndcoffeelistVO> spndList_data;
        Map<ImageView,String> map= new HashMap<>();
        private AlertDialogFragment alertDialogFragment;

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




            //取到當下那個VO物件
            final SpndcoffeelistVO spndcoffeelistVO = spndList_data.get(position);

            spndcoffeelistVO.setMem_name(profile.getMem_name().toString());


            //將抓到的view 設上其值
            TextView sotre_name = (TextView) convertView.findViewById(R.id.sotre_name);
            TextView store_add = (TextView) convertView.findViewById(R.id.store_add);
            TextView list_left = (TextView) convertView.findViewById(R.id.list_left);
            TextView originCount = (TextView) convertView.findViewById(R.id.originCount);
            TextView MySpndStoreDetail = (TextView) convertView.findViewById(R.id.MySpndStoreDetail);
            TextView tv_spnd_prod2 = (TextView) convertView.findViewById(R.id.tv_spnd_prod2);


            sotre_name.setText(spndcoffeelistVO.getStore_name());
            store_add.setText(spndcoffeelistVO.getStore_add());
            list_left.setText(String.valueOf(spndcoffeelistVO.getList_left()));
            originCount.setText(String.valueOf(spndcoffeelistVO.getList_amt()));
            tv_spnd_prod2.setText(spndcoffeelistVO.getSpnd_prod());
            String htmlString="<u>詳情</u>";
            MySpndStoreDetail.setText(Html.fromHtml(htmlString));
            MySpndStoreDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String store_id = spndcoffeelistVO.getStore_id();
                    StoreVO storevo = null;
                    //有store_id 得到完整的StoreVO
                    if(Common_RJ.networkConnected(getActivity())){
                        String url = Common_RJ.URL + "StoreServlet";


                        try {
                            storevo = new getThisStoreInfoTask().execute(url,store_id).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Bundle bundle = new Bundle(getCount());
                    StoreFragment storeFragment = new StoreFragment();
                    bundle.putSerializable("StoreVO",storevo);
                    storeFragment.setArguments(bundle);
                    switchFragment(storeFragment);
                }
            });


            //按下導航
            ImageView MapNavigation = (ImageView) convertView.findViewById(R.id.MapNavigation);
            MapNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取得目前經緯度
                    double current_lat =lastLocation.getLatitude();
                    double current_lng =lastLocation.getLongitude();


                    //取得店家位置的經緯度
                    LatLng store_latlng = Helper.getLatLngByAddress(spndcoffeelistVO.getStore_add().toString());
                    double store_lat = store_latlng.latitude;
                    double store_lng = store_latlng.longitude;

                    direct(current_lat,current_lng,store_lat,store_lng);

                }
            });


            //夾帶資訊到qrcode
            final Bundle bundle = new Bundle();
            bundle.putSerializable("spndcoffeelistVO",spndcoffeelistVO);


            //點擊顯示QR_code
            Button showQRcode = (Button) convertView.findViewById(R.id.showQRcode);
            showQRcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //show QRcode
                    AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
                    alertDialogFragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();

                    alertDialogFragment.show(fragmentManager,"alert");


                }
            });



            return convertView;
        }


        private void switchFragment(Fragment fragment) {
            SearchActivity activity = (SearchActivity) getActivity();
            activity.switchFragment(fragment);
        }
    }

    public static class AlertDialogFragment extends DialogFragment {


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
            //載入xml layout
            View view = inflater.inflate(R.layout.rj_alertdialog, null);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Profile profile = new Profile(getContext());

            Bundle bundle = getArguments();
            SpndcoffeelistVO spndcoffeelistVO = (SpndcoffeelistVO) bundle.getSerializable("spndcoffeelistVO");
            String List_id  =spndcoffeelistVO.getList_id();
            String List_left = spndcoffeelistVO.getList_left().toString();

            //for ming
            spndcoffeelistVO.setMem_name(profile.getMem_name().toString());

            Gson gson = new Gson();
            String json = gson.toJson(spndcoffeelistVO);



            //註冊離開button事件聆聽
            Button btleave = (Button) view.findViewById(R.id.btleave);
            btleave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
//            放入qr code image
            ImageView qrimage = (ImageView) view.findViewById(R.id.qrimage);
            qrimage.setImageResource(R.drawable.default_image);

            //startpaste
            // QR code 的內容
            String QRCodeContent = json;
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

                ImageView imgView = (ImageView) view.findViewById(R.id.qrimage);
                // 設定為 QR code 影像
                imgView.setImageBitmap(bitmap);
            }
            catch (WriterException e)
            {

                e.printStackTrace();
            }


        }


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


    private GoogleApiClient.ConnectionCallbacks connectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "GoogleApiClient connected");
                    //需要使用者同意授權
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        lastLocation = LocationServices.FusedLocationApi
                                .getLastLocation(googleApiClient);  //取得手機中最後一筆定位位址
                        LocationRequest locationRequest = LocationRequest.create()       //真正開始定位(現在位址)，需耗電
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setSmallestDisplacement(1000);
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest, locationListener);
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                  //do nothing
                }
            };

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;
        }
    };








}
