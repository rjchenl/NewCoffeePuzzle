package com.example.user.newcoffeepuzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.search.SearchActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etID;
    private EditText etPsw;
    private String test2;


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
