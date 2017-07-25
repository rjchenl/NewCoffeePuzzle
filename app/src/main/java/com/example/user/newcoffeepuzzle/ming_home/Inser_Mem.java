package com.example.user.newcoffeepuzzle.ming_home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.hompage.HomePageActivity;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;

import static android.content.ContentValues.TAG;

public class Inser_Mem extends AppCompatActivity {
    EditText inser_memid,inser_mem_psw,inser_mem_name,inser_mem_nanber,inser_mem_mail,inser_mem_add;
    Button inser_bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ming_inser__mem);

        findEdText();

        inser_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inser_memid.getText().toString().matches("") || inser_mem_psw.getText().toString().matches("") ||
                        inser_mem_name.getText().toString().matches("") || inser_mem_nanber.getText().toString().matches("") ||
                        inser_mem_mail.getText().toString().matches("") || inser_mem_add.getText().toString().matches("")){
                    Toast toast = Toast.makeText(Inser_Mem.this, "欄位不能是空白!!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }else if(inser_mem_mail.getText().toString().matches("^[_a-z0-9-]+([._a-z0-9-]+)*@[a-z0-9-]+([.a-z0-9-]+)*$")){
                    Toast toast = Toast.makeText(Inser_Mem.this, "e-mail格式正確!!", Toast.LENGTH_LONG);
                    toast.show();

                }else {
                    Toast toast = Toast.makeText(Inser_Mem.this, "e-mail格式錯誤!!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if(Common_ming.networkConnected(Inser_Mem.this)){
                    String url = Common_ming.URL + "ming_Member_Servlet";

                    String mem_id = inser_memid.getText().toString();
                    String mem_psw = inser_mem_psw.getText().toString();
                    String mem_name = inser_mem_name.getText().toString();
                    String mem_nanber = inser_mem_nanber.getText().toString();
                    String mem_mail = inser_mem_mail.getText().toString();
                    String mem_add = inser_mem_add.getText().toString();

                    Object inser = null;
                    try {
                        inser = new MemGetInsert().execute(url,mem_id,mem_psw,mem_name,mem_nanber,mem_mail,mem_add).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(Inser_Mem.this, "此帳號以存在!!", Toast.LENGTH_LONG);
                        toast.show();
                        Log.d(TAG, "intent: + intent" + inser);
                    }
                    Common_ming.showToast(Inser_Mem.this,R.string.inser_ok);
                    Intent intent = new Intent(Inser_Mem.this,HomePageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void findEdText() {
        inser_memid = (EditText) findViewById(R.id.inser_memid);
        inser_mem_psw = (EditText) findViewById(R.id.inser_mem_psw);
        inser_mem_name = (EditText) findViewById(R.id.inser_mem_name);
        inser_mem_nanber = (EditText) findViewById(R.id.inser_mem_nanber);
        inser_mem_mail = (EditText) findViewById(R.id.inser_mem_mail);
        inser_mem_add = (EditText) findViewById(R.id.inser_mem_add);

        inser_bt = (Button) findViewById(R.id.inser_bt);
    }


}
