package com.example.user.newcoffeepuzzle.rjchenl_order_list_takeout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;

import java.util.List;

/**
 * Created by user on 2017/7/15.
 */

public class OrderdetailListFragment extends Fragment {
    public static String TAG = "OrderdetailListFragment";

    ListView lvorderdetail_view;
    List<OrderdetailVO> orderdetailVO_list;



        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.rj_ordedetail_list_fragment,container,false);
            lvorderdetail_view = (ListView) view.findViewById(R.id.lvorderdetail);

            getOrdertailVO_DBdata();
            lvorderdetail_view.setAdapter(new OrderDetailItemAdapter(getActivity(),orderdetailVO_list));
            return view;
        }

        private void getOrdertailVO_DBdata() {
            Bundle bundle = getArguments();
            String ord_id = bundle.getString("ord_id");


            if (Common_RJ.networkConnected(getActivity())) {
                String url = Common_RJ.URL + "OrderdetailServlet_RJ";
                orderdetailVO_list = null;
                try {

                    //連結資料庫取得物件資料
                    orderdetailVO_list = new OrderDetailGetTask().execute(url,ord_id).get();
                } catch (Exception e) {

                }
                if (orderdetailVO_list == null || orderdetailVO_list.isEmpty()) {
                    Common_RJ.showToast(getActivity(), "No spndcoffeelist found");
                }
            }



        }


        public class OrderDetailItemAdapter extends BaseAdapter{

            Context context;
            List<OrderdetailVO> OrderdetailVO_List;

            public OrderDetailItemAdapter(Context context,List<OrderdetailVO> OrderdetailVO_List){
                this.context = context;
                this.OrderdetailVO_List = OrderdetailVO_List;
            }

            @Override
            public int getCount() {
                return OrderdetailVO_List.size();
            }

            @Override
            public Object getItem(int position) {
                return OrderdetailVO_List.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    convertView =layoutInflater.inflate(R.layout.rj_orderdetail_item_info_fragment,parent,false);
                }


                OrderdetailVO orderdetailvo = OrderdetailVO_List.get(position);

                TextView tvprod_name = (TextView) convertView.findViewById(R.id.tvprod_name);
                tvprod_name.setText(orderdetailvo.getProd_name());
                TextView tvprod_price = (TextView) convertView.findViewById(R.id.tvprod_price);
                tvprod_price.setText(orderdetailvo.getProd_price().toString());
                TextView tvdetail_amt = (TextView) convertView.findViewById(R.id.tvdetail_amt);
                tvdetail_amt.setText(orderdetailvo.getDetail_amt().toString());




                return convertView;
            }
        }

}
