package com.example.user.newcoffeepuzzle.ming_delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;


public class Delivery_Fragment_text extends AppCompatActivity {
    String mem_name,ord_id,ord_add,ord_time,store_id;
    TextView delivery_mem_name,delivery_ord_add,delivery_prod_name,delivery_prod_price,delivery_detail_amt,delivery_ore_time;
    private DeliveryVO deliveryVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_delivery__fragment_text);

        Bundle bundle = getIntent().getExtras();
        mem_name = bundle.getString("mem_name");
        ord_id = bundle.getString("ord_id");
        ord_add = bundle.getString("ord_add");
        ord_time = bundle.getString("ord_time");

        Profile_ming profile_ming = new Profile_ming(this);
        store_id = profile_ming.getStoreId();

        findText();

        if (Common_ming.networkConnected(this)) {
            String url = Common_ming.URL + "ming_Orderdetail_Servlet";
            deliveryVO = null;

            try {
                deliveryVO = new Delivery_GetAllTask().execute(url,ord_id).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Common_ming.showToast(this, "no network connection available");
        }

        delivery_mem_name.setText(mem_name);
        delivery_ord_add.setText(ord_add);
        delivery_prod_name.setText(deliveryVO.getProd_name());
        delivery_prod_price.setText(deliveryVO.getProd_price().toString());
        delivery_detail_amt.setText(deliveryVO.getDetail_amt().toString());
        delivery_ore_time.setText(ord_time);
    }

    private void findText() {
        delivery_mem_name = (TextView) findViewById(R.id.delivery_mem_name);
        delivery_ord_add = (TextView) findViewById(R.id.delivery_ord_add);
        delivery_prod_name = (TextView) findViewById(R.id.delivery_prod_name);
        delivery_prod_price = (TextView) findViewById(R.id.delivery_prod_price);
        delivery_detail_amt = (TextView) findViewById(R.id.delivery_detail_amt);
        delivery_ore_time = (TextView) findViewById(R.id.delivery_ore_time);
    }
}
