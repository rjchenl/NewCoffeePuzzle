package com.example.user.newcoffeepuzzle.ming_home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.user.newcoffeepuzzle.R;

/**
 * Created by Java on 2017/6/21.
 */

public class StoreLoginFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.store,container,false);


        return view;
    }
}
