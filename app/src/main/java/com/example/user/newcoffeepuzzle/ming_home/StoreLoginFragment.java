package com.example.user.newcoffeepuzzle.ming_home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_login_store.Login_StoreVO;
import com.example.user.newcoffeepuzzle.ming_login_store.Login_Store_GetId;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;

import java.util.HashSet;
import java.util.Set;


public class StoreLoginFragment extends Fragment{
    private static final String TAG = "StoreLoginFragment";
    private EditText edStore_acct,edStore_psw;
    Button StoreLogin ;
    private static final int REQ_PERMISSIONS = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.store,container,false);

        findView_Store(view);
        askPermissions();

        StoreLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userStore_acct = edStore_acct.getText().toString();
                String userStore_psw = edStore_psw.getText().toString();
//                Toast.makeText(getContext(),userStoreid,Toast.LENGTH_SHORT).show();

                if (Common_ming.networkConnected(getActivity())){
                    String url = Common_ming.URL + "ming_Store_Servlet";
                    Login_StoreVO login_storeVO = null;
                    try {
                        login_storeVO = new Login_Store_GetId().execute(url,userStore_acct,userStore_psw).get();
                    }catch (Exception e ){
                        Log.d(TAG, e.toString());
                    }
                    if (login_storeVO == null){
                        Common_ming.showToast(getContext(),R.string.Login_null);
                    }else {
                        String store_id = null;
                            if(login_storeVO.getStore_acct().equals(userStore_acct)){
                                store_id = login_storeVO.getStore_id();
                        }
                        //將store_id寫入profile
                        Profile_ming profile_ming = new Profile_ming(getContext());
                        profile_ming.setStoreId(store_id);
                        Intent intent = new Intent(getContext(), StoreActivity.class);
                        startActivity(intent);
                    }
                }else {
                    Common_ming.showToast(getContext(),R.string.Login_null);
                }
            }
        });

        return view;
    }

    private void askPermissions() {

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

    private void findView_Store(View view) {

        edStore_acct = (EditText) view.findViewById(R.id.edStore_acct);
        edStore_psw = (EditText) view.findViewById(R.id.edStore_psw);
        StoreLogin = (Button) view.findViewById(R.id.StoreLogin);
    }
}
