package com.android.tweedle.heartrate.activity;


import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.tweedle.heartrate.R;
import com.android.tweedle.heartrate.adapter.MyBaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Set;


public class DeviceListActivity extends AppCompatActivity {

    private ArrayList<String> gData = null;
//    private ArrayList<ArrayList<BleDevice>> iData = null;
    private ArrayList<BluetoothDevice> lData = null;
    private ArrayList<ArrayList<BluetoothDevice>> iData = null;
//    private ArrayList<String> lData = null;
    private Context mContext;
    private ExpandableListView expandableListView;
    private MyBaseExpandableListAdapter myExListDeviceAdapter = null;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter localBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final long SCAN_PERIOD = 10000;

    private ScanCallback mLeScanCallback  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
//        setCustomActionBar();
        mHandler = new Handler();
        // Initializes Bluetooth adapter.初始化蓝牙适配器
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //设备扫描回调
        mLeScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                myExListDeviceAdapter.addDevice(result.getDevice());
                myExListDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };


        //获取顶部导航栏
       ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);//设定左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角的图标加上一个返回的图标
        }

        mContext = DeviceListActivity.this;
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);


        //列表数据准备
        iData = new ArrayList<ArrayList<BluetoothDevice>>();
        gData = new ArrayList<String>();
        gData.add("已配对");
        gData.add("其它设备");

        //已配对
        lData = new ArrayList<BluetoothDevice>();
        localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
         Set<BluetoothDevice> pairedDevices =  localBluetoothAdapter.getBondedDevices();
        //        如果有配对的设备，请将每个设备添加到已配对下
        if (pairedDevices.size() > 0) {
            for(BluetoothDevice device:pairedDevices){
                    lData.add(device);
            }
        }
        iData.add(lData);


//        lData = new ArrayList<BluetoothDevic e>();
//        lData.add("HC-06");
//        iData.add(lData);

        //其他设备
//        lData = new ArrayList<String>();
//        lData.add("HHH");
//        iData.add(lData);


        myExListDeviceAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        expandableListView.setAdapter(myExListDeviceAdapter);


        //获取小组中的数量，默认展开
//        int groupCount = myExListDeviceAdapter.getGroupCount();
//        if(groupCount>= 0){
//            for (int i=0; i<groupCount; ++i) {
//                //IndexOutOfBoundsException
//                expandableListView.expandGroup(i);
//            }
//        }


        //为列表设置点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            //如果列表中的设备被点击了应该设置开始连接设备并且终止设备扫描

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //终止设备扫描
                if (mScanning) {
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(mLeScanCallback);
                    mScanning = false;
                }
//                Toast.makeText(mContext, "你点击了：" + iData.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //加载菜单资源，添加控件
        getMenuInflater().inflate(R.menu.actionbar_search_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();// back button
                return true;
            case R.id.menu_search://点击了搜索按钮
                scanLeDevice(true); //开始扫描设备
                new AlertDialog.Builder(this)
                        .setTitle("HeartRate蓝牙")
                        .setMessage("搜索设备中...")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scanLeDevice(false); //取消扫描设备
                            }
                        })
                        .show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(mLeScanCallback);
//                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.getBluetoothLeScanner().startScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(mLeScanCallback);
        }
//        invalidateOptionsMenu();
    }



}
