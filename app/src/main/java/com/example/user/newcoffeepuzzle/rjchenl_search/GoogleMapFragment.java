package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.example.user.newcoffeepuzzle.rjchenl_main.Helper;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.location.LocationServices;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private final static String TAG = "GoogleMapFragment";
    private GoogleMap map;
    private EditText etLocationName;
    private Button btSubmit;
    private static final int REQ_PERMISSIONS = 0;
    // 定位管理器
    private LocationManager mLocationManager;
    // 定位監聽器
    private LocationListener mLocationListener;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CODE_RESOLUTION = 1;
    private Marker markerMe;
    public final String LM_GPS = LocationManager.GPS_PROVIDER;
    public final String LM_NETWORK = LocationManager.NETWORK_PROVIDER;
    private static List<StoreVO> storeList;
    private final SupportMapFragment mapp = new SupportMapFragment();
    private double current_Lat,current_Lng;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mapsview = inflater.inflate(R.layout.rj_fragmen_maps, container, false);

        mLocationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        findViews();
        replaceFragmentToContainer(mapp);
        //載入地理管理器和監聽器
        openGPS(getActivity());
        return mapsview;
    }
    // 在 resume 階段設定 mLocationListener 監聽器，以獲得地理位置的更新資料
    @Override
    public void onResume() {
        if (mLocationManager == null) {
            mLocationManager =
                    (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mLocationListener = new MyLocationListener();
        }
        // 獲得地理位置的更新資料 (GPS 與 NETWORK都註冊)
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //已要過權限
            return;
        }

        super.onResume();


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallbacks)
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .build();
        }
        googleApiClient.connect();


    }


    private void findViews() {
        etLocationName = (EditText) getActivity().findViewById(R.id.etLocationName);
        btSubmit = (Button) getActivity().findViewById(R.id.btSubmit);
    }


    public void openGPS(Context context) {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapp.getMapAsync(this);
    }



    private void switchFragment(Fragment fragment) {
        SearchActivity activity = (SearchActivity) getActivity();
        activity.switchFragment(fragment);
    }

    private void replaceFragmentToContainer(Fragment fragment){
        //使用動態新增Fragment 的方式
        //因為本身是在Fragment 上要置換另一個fragment 需要getChildFragmentManager幫忙
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Mapcontainer, fragment);
        fragmentTransaction.commit();
    }





    @Override
    public void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, mLocationListener);
    }

    private void askPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }


    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
        addStoresInfo();
        setSubmitLisntener();

    }

    private void setSubmitLisntener() {


        //search btsubmit listener
        Button submitButton = (Button) getActivity().findViewById(R.id.btSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //先清空 再增加原本的
                map.clear();
//                addStoresInfo();
                putMarkerOnMap();

                EditText etLocationName = (EditText) getActivity().findViewById(R.id.etLocationName);
                String locationName = etLocationName.getText().toString().trim();
                for (StoreVO storevo : storeList){
                    String store_name = storevo.getStore_name();
                    String store_add = storevo.getStore_add();
                    String store_name_add = store_name+store_add;

                    if (locationName.length() > 0 && store_name_add.contains(locationName)) {
                        //就去找符合字串的店家會員的店
                        //移動到的該店家
                        LatLng position = new LatLng(storevo.getLatitude(),storevo.getLongitude());
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(position).zoom(15).build();
                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        //mark要變色 顯示該店家//hlposition

                        Marker this_marker =map_forsearch.get(storevo);
                        this_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mapflag));
                        return;
                    }

                }

                if(locationName.length() > 0){
                    Log.d(TAG, "onClick: 我有跑這個");
                    showToast("沒有符合條件店家");
//                    locationNameToMarker(locationName);
                }else{
                    showToast("Location Name is empty");
                }

            }
        });
    }



//    private void locationNameToMarker(String locationName) {
//        map.clear();
//
//        Geocoder geocoder = new Geocoder(getActivity());
//        List<Address> addressList = null;
//        int maxResults = 1;
//        try {
//            addressList = geocoder
//                    .getFromLocationName(locationName, maxResults);
//
//        } catch (IOException e) {
//            Log.e(TAG, e.toString());
//        }
//
//        if (addressList == null || addressList.isEmpty()) {
//            showToast("Location name not found");
//        } else {
//            Address address = addressList.get(0);
//            LatLng position = new LatLng(address.getLatitude(), address.getLongitude());
//
//            String snippet = address.getAddressLine(0);
//
//            map.addMarker(new MarkerOptions()
//                    .position(position)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.coffeestoremapicon2))
//                    .title(locationName)
//                    .snippet(snippet));
//
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(position).zoom(15).build();
//            map.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(cameraPosition));
//        }
//
//    }

    private void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void setUpMap() {
        //取得目前位置
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);

        }
        map.getUiSettings().setZoomControlsEnabled(true);

        //設定自訂UIsetting
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());



        //預設位置為現在位置
        LatLng position = new LatLng(25.063, 121.518);

//        移動攝影機到預設位置
        map.moveCamera(CameraUpdateFactory.newLatLng(position));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position).zoom(15).build();
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

    Map<Marker, StoreVO> markerMap = new HashMap<>();
    Map<StoreVO,Marker> map_forsearch = new HashMap<>();

    private void addStoresInfo() {
        if(Common_RJ.networkConnected(getActivity())){

            String url = Common_RJ.URL+"StoreServlet";
            //可以先顯示地圖一邊跑task
//                StoreGetAllTask task = new StoreGetAllTask();
//                task.setListener(new StoreGetAllTask.Listener() {
//                    @Override
//                    public void onGetStoresDone(List<StoreVO> storeVOs) {
//                        storeList = storeVOs;
//                        putMarkerOnMap();
//                    }
//                });
//                task.execute(url);


            try {
                storeList = new StoreGetAllTask().execute(url).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            putMarkerOnMap();



            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //取出剛剛對應的map組合(marker,storeVO)
                    StoreVO storevo = markerMap.get(marker);

                    //利用bundle傳遞資訊
                    Fragment storeFragment = new StoreFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("StoreVO", storevo);
                    storeFragment.setArguments(bundle);

                    switchFragment(storeFragment);
                    return false;
                }
            });
        }
    }

    private void putMarkerOnMap() {
        if(storeList == null || storeList.isEmpty()){
            Common_RJ.showToast(getActivity(),"no storeList found");
        }else{
            //已有資料  開始連結view
            for (StoreVO stvo : storeList){
                if((stvo.getLongitude() != null||(!stvo.getLongitude().toString().trim().isEmpty())) &&
                        (stvo.getLatitude() != null||(!stvo.getLatitude().toString().trim().isEmpty()))){
                LatLng store_position = new LatLng(stvo.getLatitude(),stvo.getLongitude());
                Marker marker = map.addMarker(new MarkerOptions()
                        .title(stvo.getStore_name())
                        .position(store_position)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.coffeestoremapicon2))
                );
                //把當下這一組(marker,storeVO)相對應的關係存放在map中
                markerMap.put(marker, stvo);
                //for search
                map_forsearch.put(stvo,marker);
                }else{
                    Log.d(TAG, "putMarkerOnMap: stvo.getStore_name() : "+stvo.getStore_name());
                }
            }
        }
    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
        private final View infoWindow;

        MyInfoWindowAdapter() {
            infoWindow = View.inflate(
                    getContext(),
                    R.layout.rj_custom_info_window,
                    null);
        }


        @Override
        public View getInfoWindow(Marker marker) {

            //設定自訂title
            TextView markTitle = (TextView) infoWindow.findViewById(R.id.tvmarkTitle);
            String title = marker.getTitle();
            markTitle.setText(title);

            //設定自訂視窗
            TextView tv_snippet = (TextView) infoWindow.findViewById(R.id.tvmarkSnippet);
            String snippet = marker.getSnippet();
            tv_snippet.setText(snippet);


            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


    private GoogleApiClient.ConnectionCallbacks connectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    //需要使用者同意授權
                    if (ActivityCompat.checkSelfPermission(getActivity(),
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

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult result) {
                    showToast("GoogleApiClient connection failed");
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




        private class MyLocationListener implements LocationListener {
            @Override
            public void onLocationChanged(Location location) {
                //得到現在的經緯度
                lastLocation = location;
                GoogleMapFragment.this.current_Lat = lastLocation.getLatitude();
                GoogleMapFragment.this.current_Lng = lastLocation.getLongitude();
                double Lat = lastLocation.getLatitude();
                double Lgt = lastLocation.getLongitude();



                LatLng point = new LatLng(Lat,Lgt);
                String current_position = Helper.getAddressByLatLng(point);
                //每次位置改變時，將位置資訊傳給會員資料
                showToast(current_position);
                Profile profile = new Profile(getActivity());
                profile.setCurrentPosition(current_position);
                profile.setLat((float) Lat);
                profile.setLng((float) Lgt);

                showToast("現在經度:"+Lat+"/n"+"現在緯度"+Lgt);
                showMarkerMe(Lat,Lgt);
            }
        }

    private void showMarkerMe(double lat, double lng){
        if (markerMe != null) {
            markerMe.remove();
        }
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(new LatLng(lat, lng));
        markerOpt.title("媽!!!我在這裡!!");
        markerOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blueperson));
        markerMe = map.addMarker(markerOpt);
        Toast.makeText(getActivity(), "lat:" + lat + ",lng:" + lng, Toast.LENGTH_SHORT).show();
    }

}

