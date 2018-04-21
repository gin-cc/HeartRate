package com.android.tweedle.heartrate.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tweedle.heartrate.dataHelper.HandleData;
import com.android.tweedle.heartrate.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private TextView forgetPSW;
    private TextView registered;
    private Button login;
    private HandleData handleData;//用于处理数据
    private static final String TAG = "LoginActivity";//用于打印日志的TAG参数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题拦
        setContentView(R.layout.activity_login);

        usernameEdit = (EditText)findViewById(R.id.login_username);
        passwordEdit = (EditText)findViewById(R.id.login_password);
        forgetPSW = (TextView)findViewById(R.id.login_tv_forget);
        registered = (TextView)findViewById(R.id.login_tv_registered);
        login = (Button) findViewById(R.id.login_btn);

        forgetPSW.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加下划线
        registered.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        login.setOnClickListener(this);//设置监听
        forgetPSW.setOnClickListener(this);
        registered.setOnClickListener(this);

        handleData = new HandleData(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:  //响应登录按钮
                String name =usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                //在数据库中查询，匹配用户信息
                SQLiteDatabase db = handleData.getDb();
                Cursor cursor = db.query("users", null, "name = ?", new String[]{name}, null, null, null);
                if(cursor.getCount() != 0){
                    while (cursor.moveToNext()){
//                        Log.d(TAG, "用户名："+name);
                        String psw = cursor.getString(cursor.getColumnIndex("password"));
//                        Log.d(TAG, "密码："+psw);
                        if(psw.equals(password)){
                            String signature = cursor.getString(cursor.getColumnIndex("signature"));
//                            Log.d(TAG, "个性签名："+signature);
                            Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
                            intent1.putExtra("userName",name);
                            intent1.putExtra("signature",signature);
                            startActivity(intent1);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"密码错误！",Toast.LENGTH_SHORT).show();
                            passwordEdit.setText("");

                        }
                    }
                    cursor.close();

                }
                else{
                    Toast.makeText(LoginActivity.this,"该用户不存在！",Toast.LENGTH_SHORT).show();
                    usernameEdit.setText("");
                    passwordEdit.setText("");
                }
                break;
            case R.id.login_tv_forget://响应忘记密码
                Intent intent2 = new Intent(LoginActivity.this,ChangePSWActivity.class);
                startActivity(intent2);
                break;
            case R.id.login_tv_registered: //响应注册
                Intent intent3 = new Intent(LoginActivity.this,SignInActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
