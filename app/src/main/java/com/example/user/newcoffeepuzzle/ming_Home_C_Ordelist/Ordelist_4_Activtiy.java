package com.example.user.newcoffeepuzzle.ming_Home_C_Ordelist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_Orderdetail.Orderdetail;
import com.example.user.newcoffeepuzzle.ming_Orderdetail.Orderdetail_4;
import com.example.user.newcoffeepuzzle.ming_Orderlist.Ordelist_4_GetAllTask;
import com.example.user.newcoffeepuzzle.ming_Orderlist.OrderlistVO;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;
import com.example.user.newcoffeepuzzle.rjchenl_main.Helper;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.GoogleMapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class Ordelist_4_Activtiy extends AppCompatActivity {
    private final static String TAG = "Ordelist_4_Activtiy";
    private RecyclerView ry_ordelist_4;
    ImageView ord_map_route;
    private String store_id;
    // 定位管理器
    private LocationManager mLocationManager;
    // 定位監聽器
    private LocationListener mLocationListener;
    private Location lastLocation;
    private double current_Lat,current_Lng;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CODE_RESOLUTION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_ordelist_4_fragment);

        Log.d(TAG, "onCreate: step1");
        mLocationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new Ordelist_4_Activtiy.MyLocationListener();
        Log.d(TAG, "onCreate: step2");
        openGPS(this);

        ry_ordelist_4 = (RecyclerView) findViewById(R.id.ry_ordelist_4);
        ry_ordelist_4.setLayoutManager(new LinearLayoutManager(this));

        Profile_ming profile_ming = new Profile_ming(this);
        store_id = profile_ming.getStoreId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //需要使用者同意授權
                        if (ActivityCompat.checkSelfPermission(Ordelist_4_Activtiy.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED){
                            lastLocation = LocationServices.FusedLocationApi
                                    .getLastLocation(googleApiClient);  //取得手機中最後一筆定位位址
                            LocationRequest locationRequest = LocationRequest.create()       //真正開始定位(現在位址)，需耗電
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setSmallestDisplacement(10);
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    googleApiClient, locationRequest,  mLocationListener);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        showToast("GoogleMap onConnectionSuspended");
                    }

                };

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult result) {
                        showToast("GoogleApiClient connection failed");
                        if (!result.hasResolution()) {
                            GoogleApiAvailability.getInstance().getErrorDialog(
                                    Ordelist_4_Activtiy.this,
                                    result.getErrorCode(),
                                    0
                            ).show();
                            return;
                        }
                        try {
                            result.startResolutionForResult(
                                    Ordelist_4_Activtiy.this,
                                    REQUEST_CODE_RESOLUTION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Exception while starting resolution activity");
                        }
                    }
                };


        if (mLocationManager == null) {
            mLocationManager =
                    (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            mLocationListener = new Ordelist_4_Activtiy.MyLocationListener();
        }
        // 獲得地理位置的更新資料 (GPS 與 NETWORK都註冊)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //已要過權限
            return;
        }




        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallbacks)
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .build();
        }
        googleApiClient.connect();

    }

    private void openGPS(Context context) {
        // 開啟 GPS
        boolean gps = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Toast.makeText(context, "GPS : " + gps + ", Network : " + network,
                Toast.LENGTH_SHORT).show();
        if (gps || network) {
            return;
        } else {
            // 開啟手動GPS設定畫面
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
    }



    @Override
    public void onStart(){
        super.onStart();
        if (Common_ming.networkConnected(this)){
            String url = Common_ming.URL + "ming_Orderlist_Servlet";
            List<OrderlistVO> orderlistVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try{
                orderlistVOList = new Ordelist_4_GetAllTask().execute(url,store_id).get();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            progressDialog.cancel();
            if (orderlistVOList == null || orderlistVOList.isEmpty()){
                Common_ming.showToast(this, "no activity found");
            }else {
                ry_ordelist_4.setAdapter(new Ordelist_4_Activtiy.Orders_4_RecyclerViewAdapter(this,orderlistVOList));
            }
        }else {
            Common_ming.showToast(this, "no network connection available");
        }

    }

    public class Orders_4_RecyclerViewAdapter extends RecyclerView.Adapter<Ordelist_4_Activtiy.Orders_4_RecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<OrderlistVO> orderlistVOList;
        private boolean[] actExpanded;
        private double lat;
        private double lng;

        public Orders_4_RecyclerViewAdapter(Context context, List<OrderlistVO> orderlistVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.orderlistVOList = orderlistVOList;
            actExpanded = new boolean[orderlistVOList.size()];

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView ord_id_4,ord_total_4,ord_time_4,ord_shipping_4,ord_add_4;

            public ViewHolder(View itemView) {
                super(itemView);
                ord_id_4 = (TextView) itemView.findViewById(R.id.ord_id_4);
                ord_total_4 = (TextView) itemView.findViewById(R.id.ord_total_4);
                ord_time_4 = (TextView) itemView.findViewById(R.id.ord_time_4);
                ord_shipping_4 = (TextView) itemView.findViewById(R.id.ord_shipping_4);
                ord_add_4 = (TextView) itemView.findViewById(R.id.ord_add_4);
                ord_map_route = (ImageView) itemView.findViewById(R.id.ord_map_route);

            }
        }

        @Override
        public Ordelist_4_Activtiy.Orders_4_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.ming_ordelist_4_item,parent,false);
            return new Ordelist_4_Activtiy.Orders_4_RecyclerViewAdapter.ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(Ordelist_4_Activtiy.Orders_4_RecyclerViewAdapter.ViewHolder holder, int position) {
            OrderlistVO orderlistVO = orderlistVOList.get(position);

            final String ord_id_4 = orderlistVO.getOrd_id();
            holder.ord_id_4.setText(ord_id_4);
            Integer ord_total_4 = orderlistVO.getOrd_total();
            holder.ord_total_4.setText(ord_total_4.toString());
            String ord_time_4 = orderlistVO.getOrd_time();
            holder.ord_time_4.setText(ord_time_4);
            switch (orderlistVO.getOrd_shipping()){
                case 1:
                    holder.ord_shipping_4.setText("未處理");
                    break;
                case 2 :
                    holder.ord_shipping_4.setText("審核此筆交易失敗");
                    break;
                case 3:
                    holder.ord_shipping_4.setText("已接單");
                    break;
                case 4:
                    holder.ord_shipping_4.setText("已出貨");
                    break;
                case 5:
                    holder.ord_shipping_4.setText("交易完成");
                    break;
                default:
                    holder.ord_shipping_4.setText("無法歸類");
                    break;
            }
            String ord_add_4 = orderlistVO.getOrd_add();
            LatLng locationToSend = Helper.getLatLngByAddress(ord_add_4);
            lat = locationToSend.latitude;
            lng = locationToSend.longitude;
//            direct(current_Lat,current_Lng, lat, lng);

            holder.ord_add_4.setText(ord_add_4);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("ord_id_4",ord_id_4);
                    Intent intent = new Intent(Ordelist_4_Activtiy.this,Orderdetail_4.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            ord_map_route.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    direct(current_Lat,current_Lng, lat, lng);
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderlistVOList.size();
        }
        private void expand(int position) {
            // 被點擊的資料列才會彈出內容，其他資料列的內容會自動縮起來
            // for (int i=0; i<newsExpanded.length; i++) {
            // newsExpanded[i] = false;
            // }
            // newsExpanded[position] = true;

            actExpanded[position] = !actExpanded[position];
            notifyDataSetChanged();
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //得到現在的經緯度
            lastLocation = location;
            Ordelist_4_Activtiy.this.current_Lat = lastLocation.getLatitude();
            Ordelist_4_Activtiy.this.current_Lng = lastLocation.getLongitude();
            double Lat = lastLocation.getLatitude();
            double Lgt = lastLocation.getLongitude();



            LatLng point = new LatLng(Lat,Lgt);
            String current_position = Helper.getAddressByLatLng(point);
            //每次位置改變時，將位置資訊傳給會員資料
            showToast("現在經度:"+Lat+"/n"+"現在緯度"+Lgt);

            Profile profile = new Profile(Ordelist_4_Activtiy.this);
            profile.setCurrentPosition(current_position);
            profile.setLat((float) Lat);
            profile.setLng((float) Lgt);

        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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
}
