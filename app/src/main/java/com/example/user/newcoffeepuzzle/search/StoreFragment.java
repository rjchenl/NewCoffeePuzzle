package com.example.user.newcoffeepuzzle.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.main.Common;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by Java on 2017/6/21.
 */

public class StoreFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_storeinfo,container,false);
        Log.d(TAG, "onCreateView: enter");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: enter");
        showAllActs();
    }

    private void showAllActs() {
        if(Common.networkConnected(getActivity())){
            String url = Common.URL+"StoreServlet";
            List<StoreVO> storeList = null;
            Log.d(TAG, "showAllActs: enter");

            try {
                storeList = new StoreGetAllTask().execute(url).get();
                Log.d(TAG, "showAllActs: in try");
            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }

            if(storeList == null || storeList.isEmpty()){
                Common.showToast(getActivity(),"No storeList found");
            }else{

                Common.showToast(getActivity(),"get the data into view");
            }

        }




    }
}
