package com.example.user.newcoffeepuzzle.ming_Orderlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;

import java.util.List;

/**
 * Created by Java on 2017/6/29.
 */

public class Ordelist_Fragment extends Fragment{
    private final static String TAG = "ordelist_fragment";
    private RecyclerView ry_ordelist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ordelist_fragment, container, false);
        ry_ordelist = (RecyclerView) view.findViewById(R.id.ry_ordelist);
        ry_ordelist.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if (Common_ming.networkConnected(getActivity())){
            String url = Common_ming.URL + "Orderlist_Servlet";
            List<OrderlistVO> orderlistVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try{
                orderlistVOList = new Ordelist_GetAllTask().execute().get();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            if (orderlistVOList == null || orderlistVOList.isEmpty()){
                Common_ming.showToast(getActivity(), "no activity found");
            }else {
                ry_ordelist.setAdapter(new Ordelist_Fragment.Orders_RecyclerViewAdapter(getActivity(),orderlistVOList));
            }
        }else {
            Common_ming.showToast(getActivity(), "no network connection available");
        }

    }

    private class Orders_RecyclerViewAdapter extends RecyclerView.Adapter<Orders_RecyclerViewAdapter.ViewHolder>{
        public Orders_RecyclerViewAdapter(Context context, List<OrderlistVO> orderlistVOList) {
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

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
