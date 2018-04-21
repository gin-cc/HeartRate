package com.android.tweedle.heartrate.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tweedle.heartrate.dataHelper.HandleData;
import com.android.tweedle.heartrate.R;

public class ChangePSWActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText name;
    private EditText newPSW;
    private EditText confirmPSW;
    private Button ok;
    private Button cancel;
    private HandleData handleData;//用于处理数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        name = (EditText)findViewById(R.id.chang_edt_name);
        newPSW = (EditText)findViewById(R.id.chang_edt_psw);
        confirmPSW = (EditText)findViewById(R.id.chang_edt_psw_ok);
        ok = (Button)findViewById(R.id.chang_btn_ok);
        cancel = (Button)findViewById(R.id.chang_btn_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        handleData = new HandleData(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chang_btn_ok:
                String user_name = name.getText().toString();
                String psw = newPSW.getText().toString();
                String psw2 = confirmPSW.getText().toString();
                SQLiteDatabase db = handleData.getDb();
                Cursor cursor = db.query("users", null, "name = ?", new String[]{user_name}, null, null, null);
                if(cursor.getCount() != 0){
                    if(psw.equals(psw2)){
                        if(handleData.updateUsers(user_name,psw)!= 0){
                            Toast.makeText(ChangePSWActivity.this,"修改密码成功，请重新登录",Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(ChangePSWActivity.this,LoginActivity.class);
                            startActivity(intent1);
                        }
                        else {
                            Toast.makeText(ChangePSWActivity.this,"操作失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ChangePSWActivity.this,"两次密码输入不同！",Toast.LENGTH_SHORT).show();
                        newPSW.setText("");
                        confirmPSW.setText("");
                    }

                }else{
                    Toast.makeText(ChangePSWActivity.this,"该用户不存在！",Toast.LENGTH_SHORT).show();
                    name.setText("");
                    newPSW.setText("");
                    confirmPSW.setText("");
                }

                break;
            case R.id.chang_btn_cancel:
                finish();
                break;
        }
    }
}
