package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.android.gms.location.LocationServices;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private final static String TAG = "GoogleMapFragment";
    private GoogleMap map;
    private EditText etLocationName;
    private Button btSubmit;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mapsview = inflater.inflate(R.layout.activity_maps, container, false);
        findViews();
        //載入地理管理器和監聽器
        inflateLocationManager();
        openGPS(getActivity());
        return mapsview;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpLocationManager();

    }

    private void inflateLocationManager() {
        mLocationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
    }

    private void findViews() {
        etLocationName = (EditText) getActivity().findViewById(R.id.etLocationName);
        btSubmit = (Button) getActivity().findViewById(R.id.btSubmit);
    }

    // 開啟 GPS
    public void openGPS(Context context) {
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.commit();
    }

    private void setUpLocationManager() {
        //實做地理監聽器
        inflateLocationManager();
        if (mLocationManager == null) {
            inflateLocationManager();
        }

        //授權
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)==
                PackageManager.PERMISSION_GRANTED) {
            //如果有授權就做...
        }
        Set<String> permissionsRequest = new HashSet<>();
        boolean hasPermission = true;
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
                permissionsRequest.add(permission);
            }
            // 獲得地理位置的更新資料 (GPS 與 NETWORK都註冊)
            mLocationManager
                    .requestLocationUpdates(LM_GPS, 0, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LM_NETWORK, 0, 0,
                    mLocationListener);

        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
        setSubmitLisntener();
        addStoresInfo();

    }

    private void setSubmitLisntener() {
        //search btsubmit listener
        Button submitButton = (Button) getActivity().findViewById(R.id.btSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLocationName = (EditText) getActivity().findViewById(R.id.etLocationName);
                String locationName = etLocationName.getText().toString().trim();
                if (locationName.length() > 0) {
                    locationNameToMarker(locationName);
                } else {
                    showToast("Location Name is empty");
                }
            }
        });
    }


    private void locationNameToMarker(String locationName) {
        map.clear();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            showToast("Location name not found");
        } else {
            Address address = addressList.get(0);
            LatLng position = new LatLng(address.getLatitude(), address.getLongitude());

            String snippet = address.getAddressLine(0);

            map.addMarker(new MarkerOptions().position(position)
                    .title(locationName).snippet(snippet));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(15).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

    }

    private void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void setUpMap() {
//        取得目前位置
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);

        }
        map.getUiSettings().setZoomControlsEnabled(true);

        //一開始在中央資策會顯示一個demo marker
        LatLng position = new LatLng(24.9677420, 121.1917000);
        map.addMarker(new MarkerOptions()
                .position(position)
                .title("Marker in iii"));

//        移動攝影機到預設位置
        map.moveCamera(CameraUpdateFactory.newLatLng(position));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position).zoom(15).build();
        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));







    }

    private void addStoresInfo() {
        if(Common_RJ.networkConnected(getActivity())){
            String url = Common_RJ.URL+"StoreServlet";
            List<StoreVO> storeList = null;

            try {
                //取得物件資料
                storeList = new StoreGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.d(TAG,e.toString());
            }
            if(storeList == null || storeList.isEmpty()){
                Common_RJ.showToast(getActivity(),"no storeList found");
            }else{
                //已有資料  開始連結view
                for (StoreVO stvo : storeList){
                    LatLng  store_position = new LatLng(stvo.getLatitude(),stvo.getLongitude());
                    map.addMarker(new MarkerOptions()
                            .title(stvo.getStore_name())
                            .position(store_position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.coffeestoremapicon2))
                    );
                }
            }
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    switchFragment(new StoreFragment());
                    return false;
                }
            });
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
                                googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) mLocationListener);
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                    showToast("GoogleMap onConnectionSuspended");
                }

            };


        private class MyLocationListener implements LocationListener {
            @Override
            public void onLocationChanged(Location location) {
                //得到經緯度
                lastLocation = location;
                double Lat = lastLocation.getLatitude();
                double Lgt = lastLocation.getLongitude();

                showToast("現在經度:"+Lat+"/n"+"現在緯度"+Lgt);
                showMarkerMe(Lat,Lgt);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                showToast("GPS已開啟");

            }

            @Override
            public void onProviderDisabled(String provider) {

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

