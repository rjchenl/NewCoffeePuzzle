
package com.example.user.newcoffeepuzzle.hompage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_activities.ActivityGetAllTask;
import com.example.user.newcoffeepuzzle.rjchenl_activities.ActivityListFragment;
import com.example.user.newcoffeepuzzle.rjchenl_activities.ActivityVO;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.example.user.newcoffeepuzzle.rjchenl_member.MemberGetAllTask;
import com.example.user.newcoffeepuzzle.rjchenl_member.MemberVO;
import com.example.user.newcoffeepuzzle.rjchenl_search.SearchActivity;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText etID_RJlogin;
    private EditText etPsw_RJlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rj_activity_login);

        findViews();

    }

    private void findViews() {
        etID_RJlogin = (EditText) findViewById(R.id.etID_RJlogin);
        etPsw_RJlogin = (EditText) findViewById(R.id.etPsw_RJlogin);
    }


    public void onbtLoginClick_RJ(View view) {
        Intent intent = new Intent(this,SearchActivity.class);
        String mem_acct = etID_RJlogin.getText().toString();

        Profile profile = new Profile(this);
        profile.setMem_acct(mem_acct);
        String mem_id=null;

        //順便得到mem_id參數
        if (Common_RJ.networkConnected(this)) {
            String url = Common_RJ.URL + "MemberServlet";
            List<MemberVO> memberList = null;
            try {
                memberList = new MemberGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (memberList == null || memberList.isEmpty()) {
                Common_RJ.showToast(this, "No memberList found");
            } else {
              //執行拿資料
                for (MemberVO membervo : memberList){
                    if(membervo.getMem_acct().equals(mem_acct)){
                        mem_id = membervo.getMem_id();
                    }

                }

            }
        }

        //將mem_id寫入profile
        profile.setMemId(mem_id);
        Log.d(TAG, "onbtLoginClick_RJ: mem_id : "+mem_id);






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

