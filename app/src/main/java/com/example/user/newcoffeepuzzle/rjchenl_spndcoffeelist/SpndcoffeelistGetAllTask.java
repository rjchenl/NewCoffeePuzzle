package com.example.user.newcoffeepuzzle.rjchenl_spndcoffeelist;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by user on 2017/7/1.
 */

public class SpndcoffeelistGetAllTask extends AsyncTask<Object,Integer,List<SpndcoffeelistVO>> {
    private final static String TAG = "SpndcoffeelistGetAllTask";
    private final static String ACTION = "getAll";


    @Override
    protected List<SpndcoffeelistVO> doInBackground(Object[] params) {
        String url = params[0].toString();




        return null;
    }
}
