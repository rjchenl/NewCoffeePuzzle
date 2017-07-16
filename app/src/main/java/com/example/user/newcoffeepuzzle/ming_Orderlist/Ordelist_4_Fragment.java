package com.example.user.newcoffeepuzzle.ming_Orderlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.user.newcoffeepuzzle.ming_Orderdetail.Orderdetail;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;

import java.util.List;



public class Ordelist_4_Fragment extends Fragment{
    private final static String TAG = "ming_ordelist_4_fragment";
    private RecyclerView ry_ordelist_4;
    private String store_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ming_ordelist_4_fragment, container, false);
        ry_ordelist_4 = (RecyclerView) view.findViewById(R.id.ry_ordelist_4);
        ry_ordelist_4.setLayoutManager(new LinearLayoutManager(getActivity()));
        Profile_ming profile_ming = new Profile_ming(getContext());
        store_id = profile_ming.getStoreId();
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        if (Common_ming.networkConnected(getActivity())){
            String url = Common_ming.URL + "ming_Orderlist_Servlet";
            List<OrderlistVO> orderlistVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try{
                orderlistVOList = new Ordelist_4_GetAllTask().execute(url,store_id).get();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            progressDialog.cancel();
            if (orderlistVOList == null || orderlistVOList.isEmpty()){
                Common_ming.showToast(getActivity(), "no activity found");
            }else {
                ry_ordelist_4.setAdapter(new Ordelist_4_Fragment.Orders_4_RecyclerViewAdapter(getActivity(),orderlistVOList));
            }
        }else {
            Common_ming.showToast(getActivity(), "no network connection available");
        }

    }

    public class Orders_4_RecyclerViewAdapter extends RecyclerView.Adapter<Ordelist_4_Fragment.Orders_4_RecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<OrderlistVO> orderlistVOList;
        private boolean[] actExpanded;

        public Orders_4_RecyclerViewAdapter(Context context, List<OrderlistVO> orderlistVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.orderlistVOList = orderlistVOList;
            actExpanded = new boolean[orderlistVOList.size()];

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView ord_id_4,ord_total_4,ord_time_4,ord_shipping_4;

            public ViewHolder(View itemView) {
                super(itemView);
                ord_id_4 = (TextView) itemView.findViewById(R.id.ord_id_4);
                ord_total_4 = (TextView) itemView.findViewById(R.id.ord_total_4);
                ord_time_4 = (TextView) itemView.findViewById(R.id.ord_time_4);
                ord_shipping_4 = (TextView) itemView.findViewById(R.id.ord_shipping_4);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.ming_ordelist_4_item,parent,false);
            return new Ordelist_4_Fragment.Orders_4_RecyclerViewAdapter.ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            OrderlistVO orderlistVO = orderlistVOList.get(position);

            String ord_id_4 = orderlistVO.getOrd_id();
            holder.ord_id_4.setText(ord_id_4);
            Integer ord_total_4 = orderlistVO.getOrd_total();
            holder.ord_total_4.setText(ord_total_4.toString());
            String ord_time_4 = orderlistVO.getOrd_time();
            holder.ord_time_4.setText(ord_time_4);
            Integer ord_shipping_4 = orderlistVO.getOrd_shipping();
            holder.ord_shipping_4.setText(ord_shipping_4.toString());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),Orderdetail.class);
                    startActivity(intent);
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
