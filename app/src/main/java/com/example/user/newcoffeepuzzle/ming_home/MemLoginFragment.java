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
import com.example.user.newcoffeepuzzle.ming_login_mem.Login_MemVO;
import com.example.user.newcoffeepuzzle.ming_login_mem.Login_Mem_GetId;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;

import java.util.List;



public class MemLoginFragment extends Fragment{
    private static final String TAG = "MemLoginFragment";
    private EditText etID_member_minglogin;
    private EditText tvPSW_member_mimgLogin;
    private Button memLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.mem,container,false);

        findViews_Member(view);

        memLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMemID = etID_member_minglogin.getText().toString();
                String userMemPSW = tvPSW_member_mimgLogin.getText().toString();
//                Toast.makeText(getContext(),userMemID,Toast.LENGTH_LONG);

                if (Common_ming.networkConnected(getActivity())){
                    String url = Common_ming.URL + "ming_Member_Servlet";
                    Login_MemVO login_memVOList = null;
                    try{
                        login_memVOList = new Login_Mem_GetId().execute(url,userMemID,userMemPSW).get();
                    }catch (Exception e ){
                        Log.d(TAG, e.toString());
                    }
                    if (login_memVOList == null){
                        Common_ming.showToast(getContext(),R.string.Login_null);
                    }else {
                        Intent intent = new Intent(getContext(),SearchActivity.class);
                        Profile profile = new Profile(getContext());
                        profile.setMemId(userMemID);
                        startActivity(intent);
                    }
                }else {
                    Common_ming.showToast(getContext(),R.string.Login_null);
                }
            }
        });

        return view;
    }

    private void findViews_Member(View view) {
        etID_member_minglogin = (EditText) view.findViewById(R.id.etID_member_minglogin);
        tvPSW_member_mimgLogin = (EditText) view.findViewById(R.id.tvPSW_member_mimgLogin);
        memLogin = (Button) view.findViewById(R.id.memLogin);
    }

}
