package com.example.user.newcoffeepuzzle.ming_home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_login_store.Login_StoreVO;
import com.example.user.newcoffeepuzzle.ming_login_store.Login_Store_GetId;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;

import java.util.List;

/**
 * Created by Java on 2017/6/21.
 */

public class StoreLoginFragment extends Fragment{
    private static final String TAG = "StoreLoginFragment";
    private EditText edStoreid,edStorepassword;
    Button StoreLogin ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.store,container,false);

        findView_Store(view);

        StoreLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userStoreid = edStoreid.getText().toString();
                String UserStorepassword = edStorepassword.getText().toString();
                Toast.makeText(getContext(),userStoreid,Toast.LENGTH_SHORT).show();

                if (Common_ming.networkConnected(getActivity())){
                    String url = Common_ming.URL + "Mem_Servlet";
                    List<Login_StoreVO> login_storeVO = null;
                    try {
                        login_storeVO = new Login_Store_GetId().execute(url,userStoreid,UserStorepassword).get();
                    }catch (Exception e ){
                        Log.d(TAG, e.toString());
                    }
                    if (login_storeVO == null){
                        Common_ming.showToast(getContext(),R.string.Login_null);
                    }else {
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

    private void findView_Store(View view) {

        edStoreid = (EditText) view.findViewById(R.id.edStoreid);
        edStorepassword = (EditText) view.findViewById(R.id.edStorepassword);
        StoreLogin = (Button) view.findViewById(R.id.StoreLogin);
    }
}
