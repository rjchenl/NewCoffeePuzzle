package com.example.user.newcoffeepuzzle.ming_Orderlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;

import java.security.Timestamp;
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
        private LayoutInflater layoutInflater;
        private List<OrderlistVO> orderlistVOList;
        private boolean[] actExpanded;

        public Orders_RecyclerViewAdapter(Context context, List<OrderlistVO> orderlistVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.orderlistVOList = orderlistVOList;
            actExpanded = new boolean[orderlistVOList.size()];
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView ord_id,ord_total,ord_time;
            public ViewHolder(View itemView) {
                super(itemView);
                ord_id = (TextView) itemView.findViewById(R.id.ord_id);
                ord_total = (TextView) itemView.findViewById(R.id.ord_total);
                ord_time = (TextView) itemView.findViewById(R.id.ord_time);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.ordelist_item,parent,false);
            return new ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            OrderlistVO orderlistVO = orderlistVOList.get(position);
            String ord_id = orderlistVO.getOrd_id();
            holder.ord_id.setText(ord_id);
            Integer ord_total = orderlistVO.getOrd_total();
            holder.ord_total.setText(ord_total);
            Timestamp ord_time = orderlistVO.getOrd_time();
            holder.ord_time.setText(ord_time.toString());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderlistVOList.size();
        }

        private void expand(int position) {
            // 被點擊的資料列才會彈出內容，其他資料列的內容會自動縮起來
            // for (int i=0; i<newsExpanded.length; i++) {
            // newsExpanded[i] = false;
            // }
            // newsExpanded[position] = true;

            actExpanded[position] = !actExpanded[position];
            notifyDataSetChanged();
        }
    }
}
