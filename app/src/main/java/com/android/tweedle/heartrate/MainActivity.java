package com.android.tweedle.heartrate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private TabLayout tab_layout_;
    private ViewPager view_page_;
    private List< Fragment > fragments_;
    private CircleImageView circleImageView;
    private static final String TAG = "MainActivity";//用于打印日志的TAG参数

    private TextView headerName;//首页头像昵称
    private TextView headerDescribe;//首页个性宣言


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 获取head0
        View head_view_ = navigationView.getHeaderView(0);
       circleImageView = (CircleImageView) head_view_.findViewById(R.id.nav_header_img);
        headerName = (TextView)head_view_.findViewById(R.id.nav_header_name);
        headerDescribe = (TextView)head_view_.findViewById(R.id.nav_header_describe);
        circleImageView.setOnClickListener(this);

        //获取跳转页面带过来的数据
        Intent intent = getIntent();
        if(intent != null){
            headerName.setText(intent.getStringExtra("userName"));
            headerDescribe.setText(intent.getStringExtra("signature"));
        }



        // 添加tabview页面
        init_tab();

        //创建数据库
        SQLiteDatabase db = LitePal.getDatabase();
        if(db==null){
            Log.d(TAG, "创建数据库失败！");
        }


    }
    // 初始化tab页面
    private void init_tab(){
        final String str_tab_type_[] = { "心率检测", "历史记录" };

        // 构建tab
        tab_layout_ = (TabLayout) /*findViewById(R.id.app_bar_id).*/findViewById(R.id.tab_layout);
        view_page_ = (ViewPager) findViewById(R.id.page_view);

        fragments_ = new ArrayList<>();
        // 添加界面
        fragments_.add(ViewFragment.new_instance(R.layout.content_main));
        fragments_.add(ViewFragment.new_instance(R.layout.content_main));
        //fragments_.add(ViewFragment.new_instance(R.layout.content_main));
        //fragments_.add(ViewFragment.new_instance(R.layout.content_main));
        //fragments_.add(ViewFragment.new_instance(R.layout.content_main));

        //给viewPager设置适配器
        tab_layout_.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(view_page_){
            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

        view_page_.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments_.get(position);
            }

            @Override
            public int getCount() {
                return fragments_.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (0 <= position && str_tab_type_.length > position)
                    return str_tab_type_[position];
                return null;
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }
        });

        view_page_.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 关联页面
        tab_layout_.setupWithViewPager(view_page_);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
