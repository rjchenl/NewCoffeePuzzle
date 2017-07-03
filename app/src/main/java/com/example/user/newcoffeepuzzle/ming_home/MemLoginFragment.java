package com.example.user.newcoffeepuzzle.ming_home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;

/**
 * Created by Java on 2017/6/21.
 */

public class MemLoginFragment extends Fragment{

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
                Intent intent = new Intent(getContext(),SearchActivity.class);
//        Bundle bundle = new Bundle();
                String mem_id = etID_member_minglogin.getText().toString();


                Profile profile = new Profile(getContext());
                profile.setMemId(mem_id);
                startActivity(intent);
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
