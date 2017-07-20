package com.example.user.newcoffeepuzzle.ming_spndcoffelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;

public class Spndcoffeelist_Inser_text extends AppCompatActivity {
    String mem_name,spnd_name,spnd_amt,spnd_prod,spnd_enddate;
    TextView spn_inser_mem_name,spn_inser_spnd_name,spnd_inser_spnd_amt,spn_inser_spnd_prod,spn_inser_spnd_enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_spndcoffeelist__inser_text);

        Bundle bundle = getIntent().getExtras();
        mem_name = bundle.getString("mem_name");
        spnd_name = bundle.getString("spnd_name");
        spnd_amt = bundle.getString("spnd_amt");
        spnd_prod = bundle.getString("spnd_prod");
        spnd_enddate = bundle.getString("spnd_enddate");

        findText();

        spn_inser_mem_name.setText(mem_name);
        spn_inser_spnd_name.setText(spnd_name);
        spnd_inser_spnd_amt.setText(spnd_amt);
        spn_inser_spnd_prod.setText(spnd_prod);
        spn_inser_spnd_enddate.setText(spnd_enddate);
    }

    private void findText() {
        spn_inser_mem_name = (TextView) findViewById(R.id.spn_inser_mem_name);
        spn_inser_spnd_name = (TextView) findViewById(R.id.spn_inser_spnd_name);
        spnd_inser_spnd_amt = (TextView) findViewById(R.id.spnd_inser_spnd_amt);
        spn_inser_spnd_prod = (TextView) findViewById(R.id.spn_inser_spnd_prod);
        spn_inser_spnd_enddate = (TextView) findViewById(R.id.spn_inser_spnd_enddate);
    }
}
