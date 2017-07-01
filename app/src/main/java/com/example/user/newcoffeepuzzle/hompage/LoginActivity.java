package com.example.user.newcoffeepuzzle.hompage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;
import com.example.user.newcoffeepuzzle.rjchenl_search.StoreFragment;

public class LoginActivity extends AppCompatActivity {

    private EditText etID;
    private EditText etPsw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();

    }

    private void findViews() {
        etID = (EditText) findViewById(R.id.etID);
        etPsw = (EditText) findViewById(R.id.etPsw);
    }


    public void onbtLoginClick(View view) {
        Intent intent = new Intent(this,SearchActivity.class);
//        Bundle bundle = new Bundle();
        String mem_id = etID.getText().toString();
//      Log.d("TAG", "onbtLoginClick: mem_id : "+mem_id);
//        bundle.putString("mem_id",mem_id);
//        StoreFragment storeFragment = new StoreFragment();
//        storeFragment.setArguments(bundle);
//        intent.putExtras(bundle);


        //以下封裝成prifile.set方法
        //        SharedPreferences preferences = getSharedPreferences("profile", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("mem_id", mem_id);
//        editor.apply();

        Profile profile = new Profile(this);
        profile.setMemId(mem_id);
        startActivity(intent);

    }

    public void onResendClick(View view) {
        etID.setText("");
        etPsw.setText("");
        etID.requestFocus();
    }

    public void onForgetPswBtClick(View view) {
        String yourfuckingID =etID.getText().toString();
        String yourfuckingPsw = etPsw.getText().toString();

        Toast.makeText(this,yourfuckingID+"\n"+yourfuckingPsw,Toast.LENGTH_SHORT).show();
    }
}
