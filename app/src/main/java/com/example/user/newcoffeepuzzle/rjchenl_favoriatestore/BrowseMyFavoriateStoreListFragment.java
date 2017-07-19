package com.example.user.newcoffeepuzzle.rjchenl_favoriatestore;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Helper;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;
import com.example.user.newcoffeepuzzle.rjchenl_search.StoreFragment;
import com.example.user.newcoffeepuzzle.rjchenl_search.StoreVO;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class BrowseMyFavoriateStoreListFragment extends Fragment {
    private static final String TAG = "BrowseMyFavoriateStoreListFragment";
    private RecyclerView rc_myfavoriatestore;
    private String mem_id;
    private LatLng currentPosition;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rj_browsemyfavoriatestorelist_fragment,container,false);
        rc_myfavoriatestore = (RecyclerView) view.findViewById(R.id.rc_myfavoriatestore);
        //使用LinearLayoutManager 採垂直顯示
        rc_myfavoriatestore.setLayoutManager(new LinearLayoutManager(getActivity()));

        Profile profile = new Profile(getContext());
        currentPosition = Helper.getLatLngByAddress(profile.getCurrentPosition());





        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Profile profile = new Profile(getContext());
        mem_id = profile.getMemId();
        getMyfavoriateSotre_fromDB();



    }

    private void getMyfavoriateSotre_fromDB() {
        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "fav_storeServlet";
            List<Fav_storeVO> fav_storeVO_list = null;
            try {
                fav_storeVO_list = new GetStoreInfoByIdTask().execute(url, mem_id).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (fav_storeVO_list == null || fav_storeVO_list.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No fav_storeVO_list found");
            } else {
                rc_myfavoriatestore.setAdapter(new MyfavoriateStoreRecyclerViewAdapter(getActivity(), fav_storeVO_list));
            }
        }

    }


    private class MyfavoriateStoreRecyclerViewAdapter extends RecyclerView.Adapter
            <MyfavoriateStoreRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Fav_storeVO> Fav_storeVO_list;

        public MyfavoriateStoreRecyclerViewAdapter(Context context,List<Fav_storeVO> Fav_storeVO_list){
            layoutInflater = LayoutInflater.from(context);
            this.Fav_storeVO_list = Fav_storeVO_list;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_store_name,tv_store_add,tv_distance,StoredetailInfo;
            ImageView iv_takemegotoicon;


            public MyViewHolder(View itemView) {
                super(itemView);
                tv_store_name = (TextView) itemView.findViewById(R.id.tv_store_name);
                tv_store_add = (TextView) itemView.findViewById(R.id.tv_store_add);
                tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
                StoredetailInfo = (TextView) itemView.findViewById(R.id.StoredetailInfo);
                iv_takemegotoicon = (ImageView) itemView.findViewById(R.id.iv_takemegotoicon);
            }
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.rj_browsemyfavoriatestore_item_fragment,parent,false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {



            //收入資訊
            final Fav_storeVO fav_storeVO = Fav_storeVO_list.get(position);
            holder.tv_store_add.setText(fav_storeVO.getStore_add());
            holder.tv_store_name.setText(fav_storeVO.getStore_name());


            //計算店家距離
            final LatLng sotre_latlng = Helper.getLatLngByAddress(fav_storeVO.getStore_add());
            float[] results = new float[1];
            Location.distanceBetween(currentPosition.latitude,
                    currentPosition.longitude, sotre_latlng.latitude,
                    sotre_latlng.longitude, results);
            float result_km = results[0]/1000;
            holder.tv_distance.setText(String.format("%.2f", result_km)+"  KM ");

            //放入導航圖片
            holder.iv_takemegotoicon.setImageResource(R.drawable.map_location);
            //按下導航圖片
            holder.iv_takemegotoicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    direct(currentPosition.latitude, currentPosition.longitude
                            , sotre_latlng.latitude, sotre_latlng.longitude);
                }
            });

            //放入詳情
            String htmlString="<u>詳情</u>";
            holder.StoredetailInfo.setText(Html.fromHtml(htmlString));
            //點選詳情
            holder.StoredetailInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StoreVO storevo = null;
                    //找這家店的資料
                    if(Common_RJ.networkConnected(getActivity())){
                        String url = Common_RJ.URL + "StoreServlet";
                        String store_id = fav_storeVO.getStore_id();

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






        }

        @Override
        public int getItemCount() {
            return Fav_storeVO_list.size();
        }

        private void switchFragment(Fragment fragment) {
            SearchActivity activity = (SearchActivity) getActivity();
            activity.switchFragment(fragment);
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


