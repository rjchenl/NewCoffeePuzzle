
package com.example.user.newcoffeepuzzle.hompage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etID_RJlogin;
    private EditText etPsw_RJlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();

    }

    private void findViews() {
        etID_RJlogin = (EditText) findViewById(R.id.etID_RJlogin);
        etPsw_RJlogin = (EditText) findViewById(R.id.etPsw);
    }


    public void onbtLoginClick_RJ(View view) {
        Intent intent = new Intent(this,SearchActivity.class);
//        Bundle bundle = new Bundle();
        String mem_id = etID_RJlogin.getText().toString();
//      Log.d("TAG", "onMember_btLoginClick: mem_id : "+mem_id);
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
        etID_RJlogin.setText("");
        etPsw_RJlogin.setText("");
        etID_RJlogin.requestFocus();
    }

    public void onForgetPswBtClick(View view) {
        String yourfuckingID =etID_RJlogin.getText().toString();
        String yourfuckingPsw = etPsw_RJlogin.getText().toString();

        Toast.makeText(this,yourfuckingID+"\n"+yourfuckingPsw,Toast.LENGTH_SHORT).show();
    }
}

