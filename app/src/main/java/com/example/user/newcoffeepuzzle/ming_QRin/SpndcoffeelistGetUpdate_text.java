package com.example.user.newcoffeepuzzle.ming_QRin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;

public class SpndcoffeelistGetUpdate_text extends AppCompatActivity {
    String mem_name,list_left,spnd_amt,spnd_prod;
    TextView spn_update_mem_name,spn_update_list_left,spnd_update_spnd_prod,spn_update_list_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_spndcoffeelistgetupdate_text);

        Bundle bundle = getIntent().getExtras();
        mem_name = bundle.getString("mem_name");
        list_left = bundle.getString("list_left");
        spnd_amt = bundle.getString("spnd_amt");
        spnd_prod = bundle.getString("spnd_prod");

        findText();

        spn_update_mem_name.setText(mem_name);
        spn_update_list_left.setText(list_left);
        spnd_update_spnd_prod.setText(spnd_prod);
        spn_update_list_amt.setText(spnd_amt);
    }

    private void findText() {
        spn_update_mem_name = (TextView) findViewById(R.id.spn_update_mem_name);
        spn_update_list_left = (TextView) findViewById(R.id.spn_update_list_left);
        spnd_update_spnd_prod = (TextView) findViewById(R.id.spnd_update_spnd_prod);
        spn_update_list_amt = (TextView) findViewById(R.id.spn_update_list_amt);
    }
}
