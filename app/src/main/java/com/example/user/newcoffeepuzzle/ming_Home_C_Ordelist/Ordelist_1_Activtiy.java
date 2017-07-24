package com.example.user.newcoffeepuzzle.ming_Home_C_Ordelist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_Orderdetail.Orderdetail;
import com.example.user.newcoffeepuzzle.ming_Orderlist.Ordelist_1_GetAllTask;
import com.example.user.newcoffeepuzzle.ming_Orderlist.Ordelist_GetUpdate;
import com.example.user.newcoffeepuzzle.ming_Orderlist.Ordelist_Get_NO_Update;
import com.example.user.newcoffeepuzzle.ming_Orderlist.OrderlistVO;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;

import java.util.List;

public class Ordelist_1_Activtiy extends AppCompatActivity {
    private final static String TAG = "Ordelist_1_Activtiy";
    private RecyclerView ry_ordelist;
    private String store_id;
    private List<OrderlistVO> orderlistVOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_ordelist_1_fragment);

        ry_ordelist = (RecyclerView) findViewById(R.id.ry_ordelist);
        ry_ordelist.setLayoutManager(new LinearLayoutManager(this));

        Profile_ming profile_ming = new Profile_ming(this);
        store_id = profile_ming.getStoreId();

    }

    @Override
    public void onStart(){
        super.onStart();
        if (Common_ming.networkConnected(this)){
            String url = Common_ming.URL + "ming_Orderlist_Servlet";
            orderlistVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try{
                orderlistVOList = new Ordelist_1_GetAllTask().execute(url,store_id).get();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            progressDialog.cancel();
            if (orderlistVOList == null || orderlistVOList.isEmpty()){
                Common_ming.showToast(this, "no activity found");
            }else {
                ry_ordelist.setAdapter(new Ordelist_1_Activtiy.Orders_RecyclerViewAdapter(this, orderlistVOList));
            }
        }else {
            Common_ming.showToast(this, "no network connection available");
        }

    }

    private class Orders_RecyclerViewAdapter extends RecyclerView.Adapter<Ordelist_1_Activtiy.Orders_RecyclerViewAdapter.ViewHolder>{
        private LayoutInflater layoutInflater;
        private List<OrderlistVO> orderlistVOList;
        private boolean[] actExpanded;


        public Orders_RecyclerViewAdapter(Context context, List<OrderlistVO> orderlistVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.orderlistVOList = orderlistVOList;
            actExpanded = new boolean[orderlistVOList.size()];
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView ord_id,ord_total,ord_time,ord_shipping,ord_add;
            private Button Bt_yes,Bt_no;

            public ViewHolder(View itemView) {
                super(itemView);
                ord_id = (TextView) itemView.findViewById(R.id.ord_id);
                ord_total = (TextView) itemView.findViewById(R.id.ord_total);
                ord_time = (TextView) itemView.findViewById(R.id.ord_time);
                ord_shipping = (TextView) itemView.findViewById(R.id.ord_shipping);
                ord_add = (TextView) itemView.findViewById(R.id.ord_add);
                Bt_yes = (Button) itemView.findViewById(R.id.Bt_yes);
                Bt_no = (Button) itemView.findViewById(R.id.Bt_no);

            }
        }

        @Override
        public Ordelist_1_Activtiy.Orders_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.ming_ordelist_1_item,parent,false);

            return new Ordelist_1_Activtiy.Orders_RecyclerViewAdapter.ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(final Ordelist_1_Activtiy.Orders_RecyclerViewAdapter.ViewHolder holder, final int position) {
            final OrderlistVO orderlistVO = orderlistVOList.get(position);

            final String ord_id = orderlistVO.getOrd_id();
            holder.ord_id.setText(ord_id);
            Integer ord_total = orderlistVO.getOrd_total();
            holder.ord_total.setText(ord_total.toString());
            String ord_time = orderlistVO.getOrd_time();
            holder.ord_time.setText(ord_time);
            switch (orderlistVO.getOrd_shipping()){
                case 1:
                    holder.ord_shipping.setText("未處理");
                    break;
                case 2 :
                    holder.ord_shipping.setText("審核此筆交易失敗");
                    break;
                case 3:
                    holder.ord_shipping.setText("已接單");
                    break;
                case 4:
                    holder.ord_shipping.setText("已出貨");
                    break;
                case 5:
                    holder.ord_shipping.setText("交易完成");
                    break;
                default:
                    holder.ord_shipping.setText("無法歸類");
                    break;
            }
            String ord_add = orderlistVO.getOrd_add();
            holder.ord_add.setText(ord_add.toString());

            holder.Bt_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: I have entered");
                    if (Common_ming.networkConnected(Ordelist_1_Activtiy.this)){
                        String url = Common_ming.URL + "ming_Orderlist_Servlet";
                        List<OrderlistVO> orderlistVOList = null;

                        ProgressDialog progressDialog = new ProgressDialog(Ordelist_1_Activtiy.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                        try{
                            //沒有回傳值
                            orderlistVOList = new Ordelist_GetUpdate().execute(url,store_id,ord_id).get();
                            //要找的是最開始GETALL的List
                            Ordelist_1_Activtiy.this.orderlistVOList.remove(orderlistVO);
                            notifyDataSetChanged();

                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }
                        progressDialog.cancel();
                        if (orderlistVOList == null || orderlistVOList.isEmpty()){
                            Common_ming.showToast(Ordelist_1_Activtiy.this, "接受訂單");
                        }else {
                            ry_ordelist.setAdapter(new Ordelist_1_Activtiy.Orders_RecyclerViewAdapter(Ordelist_1_Activtiy.this,orderlistVOList));
                        }
                    }else {
                        Common_ming.showToast(Ordelist_1_Activtiy.this, "no network connection available");
                    }
//                    orderlistVOList.remove(orderlistVO);
//                    notifyDataSetChanged();
                }
            });

            holder.Bt_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Common_ming.networkConnected(Ordelist_1_Activtiy.this)){
                        String url = Common_ming.URL + "ming_Orderlist_Servlet";
                        List<OrderlistVO> orderlistVOList = null;

                        ProgressDialog progressDialog = new ProgressDialog(Ordelist_1_Activtiy.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                        try{
                            orderlistVOList = new Ordelist_Get_NO_Update().execute(url,store_id,ord_id).get();
                            Ordelist_1_Activtiy.this.orderlistVOList.remove(orderlistVO);
                            notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }
                        progressDialog.cancel();
                        if (orderlistVOList == null || orderlistVOList.isEmpty()){
                            Common_ming.showToast(Ordelist_1_Activtiy.this, "取消訂單");
                        }else {
                            ry_ordelist.setAdapter(new Ordelist_1_Activtiy.Orders_RecyclerViewAdapter(Ordelist_1_Activtiy.this,orderlistVOList));
                        }
                    }else {
                        Common_ming.showToast(Ordelist_1_Activtiy.this, "no network connection available");
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("ord_id",ord_id);
                    Intent intent = new Intent(Ordelist_1_Activtiy.this,Orderdetail.class);
                    intent.putExtras(bundle);
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
