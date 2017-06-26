package com.example.user.newcoffeepuzzle.take_orders;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.activities.Activities_fragment;
import com.example.user.newcoffeepuzzle.activities.ActivityGetAllTask;
import com.example.user.newcoffeepuzzle.activities.ActivityVO;
import com.example.user.newcoffeepuzzle.main.Common;

import java.util.List;

/**
 * Created by Java on 2017/6/26.
 */

public class Take_Orders_Fragment extends Fragment{
    private final static String TAG = "take_orders_fragment";
    private RecyclerView ry_take_orders;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.take_orders_fragment,container,false);
        ry_take_orders =(RecyclerView) view.findViewById(R.id.ry_take_orders);
        ry_take_orders.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "Spndcoffelist_Servlet";
            List<Take_OrdersVO> take_ordersVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try {
                take_ordersVOList = new Take_Orders_GetAllTask().execute(url).get();
                Log.d(TAG, "onStart: here");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            if(take_ordersVOList == null || take_ordersVOList.isEmpty()){
                Common.showToast(getActivity(), "no activity found");
            }else{
                ry_take_orders.setAdapter(new Take_Orders_Fragment.Take_Orders_RecyclerViewAdapter(getActivity(), take_ordersVOList));
            }
            progressDialog.cancel();

        }else{
            Common.showToast(getActivity(), "no network connection available");
        }
    }

    private class Take_Orders_RecyclerViewAdapter extends RecyclerView.Adapter<Take_Orders_RecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Take_OrdersVO> take_ordersVOList;
        private boolean[] actExpanded;

        public Take_Orders_RecyclerViewAdapter(Context context, List<Take_OrdersVO> take_ordersVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.take_ordersVOList=take_ordersVOList;
            actExpanded = new boolean[take_ordersVOList.size()];
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);

            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }


    }
}
