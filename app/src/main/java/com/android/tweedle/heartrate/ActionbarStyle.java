package com.android.tweedle.heartrate;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

public class ActionbarStyle extends AppCompatActivity{
    private Context mContext;
    private ActionBar actionBar;
    public ActionbarStyle(Context context){
        this.mContext = context;
        this.actionBar = getSupportActionBar();
    }

    private void setActionBarTitle() {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(mContext).inflate(R.layout.actionbar_title_layout, null);
//        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }
    private void setActionBarReturn(){
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);//设定左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角的图标加上一个返回的图标
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
