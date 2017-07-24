package com.example.user.newcoffeepuzzle.ming_Home_C_Ordelist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_Orderdetail.Orderdetail;
import com.example.user.newcoffeepuzzle.ming_Orderdetail.Orderdetail_3;
import com.example.user.newcoffeepuzzle.ming_Orderlist.Ordelist_3_GetAllTask;
import com.example.user.newcoffeepuzzle.ming_Orderlist.OrderlistVO;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;
import com.example.user.newcoffeepuzzle.rjchenl_main.Helper;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.GoogleMapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Ordelist_3_Activtiy extends AppCompatActivity {
    private final static String TAG = "Ordelist_3_Activtiy";
    private RecyclerView ry_ordelist_3;
    private String store_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_ordelist_3_fragment);

        ry_ordelist_3 = (RecyclerView) findViewById(R.id.ry_ordelist_3);
        ry_ordelist_3.setLayoutManager(new LinearLayoutManager(this));

        Profile_ming profile_ming = new Profile_ming(this);
        store_id = profile_ming.getStoreId();
    }



    @Override
    public void onStart(){
        super.onStart();
        if (Common_ming.networkConnected(this)){
            String url = Common_ming.URL + "ming_Orderlist_Servlet";
            List<OrderlistVO> orderlistVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try{
                orderlistVOList = new Ordelist_3_GetAllTask().execute(url,store_id).get();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            progressDialog.cancel();
            if (orderlistVOList == null || orderlistVOList.isEmpty()){
                Common_ming.showToast(this, "no activity found");
            }else {
                ry_ordelist_3.setAdapter(new Ordelist_3_Activtiy.Orders_3_RecyclerViewAdapter(this,orderlistVOList));
            }
        }else {
            Common_ming.showToast(this, "no network connection available");
        }

    }

    public class Orders_3_RecyclerViewAdapter extends RecyclerView.Adapter<Ordelist_3_Activtiy.Orders_3_RecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<OrderlistVO> orderlistVOList;
        private boolean[] actExpanded;

        public Orders_3_RecyclerViewAdapter(Context context, List<OrderlistVO> orderlistVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.orderlistVOList = orderlistVOList;
            actExpanded = new boolean[orderlistVOList.size()];

        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView ord_id_3,ord_total_3,ord_time_3,ord_shipping_3,ord_add_3;
            public ViewHolder(View itemView) {
                super(itemView);
                ord_id_3 = (TextView) itemView.findViewById(R.id.ord_id_3);
                ord_total_3 = (TextView) itemView.findViewById(R.id.ord_total_3);
                ord_time_3 = (TextView) itemView.findViewById(R.id.ord_time_3);
                ord_shipping_3 = (TextView) itemView.findViewById(R.id.ord_shipping_3);
                ord_add_3 = (TextView) itemView.findViewById(R.id.ord_add_3);
            }
        }

        @Override
        public Ordelist_3_Activtiy.Orders_3_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.ming_ordelist_3_item,parent,false);
            return new Ordelist_3_Activtiy.Orders_3_RecyclerViewAdapter.ViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(Ordelist_3_Activtiy.Orders_3_RecyclerViewAdapter.ViewHolder holder, int position) {
            OrderlistVO orderlistVO = orderlistVOList.get(position);

            final String ord_id_3 = orderlistVO.getOrd_id();
            holder.ord_id_3.setText(ord_id_3);
            Integer ord_total_3 = orderlistVO.getOrd_total();
            holder.ord_total_3.setText(ord_total_3.toString());
            String ord_time_3 = orderlistVO.getOrd_time();
            holder.ord_time_3.setText(ord_time_3);
            switch (orderlistVO.getOrd_shipping()){
                case 1:
                    holder.ord_shipping_3.setText("未處理");
                    break;
                case 2 :
                    holder.ord_shipping_3.setText("審核此筆交易失敗");
                    break;
                case 3:
                    holder.ord_shipping_3.setText("已接單");
                    break;
                case 4:
                    holder.ord_shipping_3.setText("已出貨");
                    break;
                case 5:
                    holder.ord_shipping_3.setText("交易完成");
                    break;
                default:
                    holder.ord_shipping_3.setText("無法歸類");
                    break;
            }
            String ord_add_3 = orderlistVO.getOrd_add();
            holder.ord_add_3.setText(ord_add_3);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("ord_id_3",ord_id_3);
                    Intent intent = new Intent(Ordelist_3_Activtiy.this,Orderdetail_3.class);
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
