package com.example.user.newcoffeepuzzle.ming_Orderdetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;

import java.util.List;

public class Orderdetail extends AppCompatActivity {
    private final static String TAG = "Orderdetail";
    private RecyclerView ry_orderdetail;
    String store_id;
    Bundle bundle = getIntent().getExtras();
    String ord_id = bundle.getString("ord_id");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_orderdetail_recyclerview);

        Profile_ming profile_ming = new Profile_ming(this);
        store_id = profile_ming.getStoreId();

        ry_orderdetail = (RecyclerView) findViewById(R.id.ry_orderdetail);
        ry_orderdetail.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart(){
        super.onStart();
        if (Common_ming.networkConnected(this)){
            String url = Common_ming.URL + "ming_Orderdetail_Servlet";
            List<OrderdetailVO> orderdetailVOlist = null;

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            try {
                orderdetailVOlist = new Orderdetail_GetAll().execute(url,store_id,ord_id).get();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            progressDialog.cancel();
            if (orderdetailVOlist == null || orderdetailVOlist.isEmpty()){
                Common_ming.showToast(this, "no activity found");
            }else {
                ry_orderdetail.setAdapter(new Orderdetail.Orderdetail_RecyclerViewAdapter(this, orderdetailVOlist));

            }
        }else {
            Common_ming.showToast(this, "no network connection available");
        }
    }

    public class Orderdetail_RecyclerViewAdapter extends RecyclerView.Adapter <Orderdetail.Orderdetail_RecyclerViewAdapter.ViewHolder>{
        private LayoutInflater layoutInflater;
        private List<OrderdetailVO> orderdetailVOlist;
        private boolean[] actExpanded;

        public Orderdetail_RecyclerViewAdapter(Context context, List<OrderdetailVO> orderdetailVOlist) {
            layoutInflater = LayoutInflater.from(context);
            this.orderdetailVOlist = orderdetailVOlist;
            actExpanded = new boolean[orderdetailVOlist.size()];
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView prod_id,prod_name,detail_amt,prod_price;
            public ViewHolder(View itemView) {
                super(itemView);
                prod_id = (TextView) itemView.findViewById(R.id.prod_id);
                prod_name = (TextView) itemView.findViewById(R.id.prod_name);
                detail_amt = (TextView) itemView.findViewById(R.id.detail_amt);
                prod_price = (TextView) itemView.findViewById(R.id.prod_price);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.ming_orderdetail_item,parent,false);
            return new ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            OrderdetailVO orderdetailVO = orderdetailVOlist.get(position);

            String prod_id = orderdetailVO.getProd_id();
            holder.prod_id.setText(prod_id);
            String prod_name = orderdetailVO.getProd_name();
            holder.prod_name.setText(prod_name);
            Integer detail_amt = orderdetailVO.getDetail_amt();
            holder.detail_amt.setText(detail_amt.toString());
            Integer prod_price = orderdetailVO.getProd_price();
            holder.prod_price.setText(prod_price.toString());

        }

        @Override
        public int getItemCount() {
            return orderdetailVOlist.size();
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
