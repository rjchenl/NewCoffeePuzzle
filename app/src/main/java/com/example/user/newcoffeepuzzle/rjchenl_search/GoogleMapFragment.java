package com.example.user.newcoffeepuzzle.rjchenl_search;

import android.Manifest;
import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private final static String TAG = "GoogleMapFragment";
    private GoogleMap map;
    private EditText etLocationName;
    private Button btSubmit;
    // 定位管理器
    private LocationManager mLocationManager;
    // 定位監聽器
    private LocationListener mLocationListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mapsview = inflater.inflate(R.layout.activity_maps, container, false);
        findViews();
        showAllActs();

        //實做地理監聽器
        mLocationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();

        return mapsview;
    }

    private void findViews() {
        etLocationName = (EditText) getActivity().findViewById(R.id.etLocationName);
        btSubmit = (Button) getActivity().findViewById(R.id.btSubmit);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();

        setSubmitLisntener();



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


        addStores();





    }

    private void addStores() {
        if(Common_RJ.networkConnected(getActivity())){
            String url = Common_RJ.URL+"StoreServlet";
            List<StoreVO> storeList = null;

            try {
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
                    );
                }

            }
        }






//        final String store1 = "有一家店";
//
//        //新增店家資訊
//        LatLng store = new LatLng(24.9647814, 121.1886704);
//        map.addMarker(new MarkerOptions()
//                .position(store)
//                .title(store1)
//        );
//
//        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                //置換storeInfofragment
//                Fragment storeFragment = new StoreLoginFragment();
//                switchFragment(storeFragment);
//                return false;
//            }
//        });
    }

    private void switchFragment(Fragment storeFragment) {
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.body, storeFragment);
        fragmentTransaction.commit();
    }

    private void showAllActs() {
        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "StoreServlet";
            List<StoreVO> storeList = null;
            Log.d(TAG, "showAllActs: enter");

            try {
                storeList = new StoreGetAllTask().execute(url).get();
                Log.d(TAG, "showAllActs: in try");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (storeList == null || storeList.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No storeList found");
            } else {

                Common_RJ.showToast(getActivity(), "get the data into view");
            }

        }
    }


    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            //得到經緯度
            double Lat = location.getLatitude();
            double Lgt = location.getLongitude();
            showToast("現在經度:"+Lat+"/n"+"現在緯度"+Lgt);


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
}
