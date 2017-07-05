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
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_member.MemberGetAllTask;
import com.example.user.newcoffeepuzzle.rjchenl_member.MemberVO;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;

import java.util.List;




public class MemLoginFragment extends Fragment{
    private static final String TAG = "MemLoginFragment";

    private EditText edmember_acct;
    private EditText edmember_psw;
    private Button memLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.mem,container,false);

        findViews_Member(view);

        memLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMemACCT = edmember_acct.getText().toString();
                String userMemPSW = edmember_psw.getText().toString();
                String mem_id = null;
//                Toast.makeText(getContext(),userMemID,Toast.LENGTH_LONG);

                if (Common_ming.networkConnected(getActivity())){
                    String url = Common_ming.URL + "ming_Member_Servlet";
                    Login_MemVO login_memVO = null;
                    try{
                        login_memVO = new Login_Mem_GetId().execute(url,userMemACCT,userMemPSW).get();
                    }catch (Exception e ){
                        Log.d(TAG, e.toString());
                    }
                    if (login_memVO == null){
                        Common_ming.showToast(getContext(),R.string.Login_null);
                    }else {
                        if(login_memVO.getMem_acct().equals(userMemACCT)){
                            mem_id = login_memVO.getMem_id();
                        }
                        Intent intent = new Intent(getContext(),SearchActivity.class);
                        Profile profile = new Profile(getContext());

                                //將mem_id寫入profile
                        profile.setMemId(mem_id);
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
        edmember_acct = (EditText) view.findViewById(R.id.edmember_acct);
        edmember_psw = (EditText) view.findViewById(R.id.edmember_psw);
        memLogin = (Button) view.findViewById(R.id.memLogin);
    }

}
