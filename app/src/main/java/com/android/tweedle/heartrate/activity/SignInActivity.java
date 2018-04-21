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

//用户注册活动

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private EditText confirmPSW;
    private Button btnOk;
    private Button btnCancel;

    private HandleData handleData;//用于处理数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = (EditText)findViewById(R.id.sign_in_edt_name);
        password = (EditText)findViewById(R.id.sign_in_edt_psw);
        confirmPSW = (EditText)findViewById(R.id.sign_in_edt_psw_ok);
        btnOk = (Button)findViewById(R.id.sign_in_btn_ok);
        btnCancel = (Button)findViewById(R.id.sign_in_btn_cancel);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        handleData = new HandleData(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_btn_ok:
                String name = username.getText().toString();
                String psw = password.getText().toString();
                String psw2 = confirmPSW.getText().toString();
                //查询数据库中是否已存在该用户名
                SQLiteDatabase db = handleData.getDb();
                Cursor cursor = db.query("users", null, "name = ?", new String[]{name}, null, null, null);
                if(cursor.getCount()!= 0){
                    Toast.makeText(SignInActivity.this,"该用户已存在！",Toast.LENGTH_SHORT).show();
                    //清空信息，要求重新输入
                    username.setText("");
                    password.setText("");
                    confirmPSW.setText("");
                }
                else{ //如果不存在该用户则允许注册，添加数据到users表中
                    if(psw.equals(psw2)) {
                        if(handleData.insertDataToUsers(name,psw,"个性宣言")==-1){//插入数据到用户表中
                            Toast.makeText(SignInActivity.this,"操作失败！",Toast.LENGTH_SHORT).show();
                        }
                        else{ //插入数据成功
                            Intent intent1 = new Intent(SignInActivity.this,LoginActivity.class);
                            Toast.makeText(SignInActivity.this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                            startActivity(intent1);
                        }
                        }
                        else{
                            Toast.makeText(SignInActivity.this,"两次密码输入不同，请重新输入",Toast.LENGTH_SHORT).show();
                            password.setText("");
                            confirmPSW.setText("");
                        }
                    }
                    cursor.close();
                break;
            case R.id.sign_in_btn_cancel:
                finish();
                break;
        }

    }

}
