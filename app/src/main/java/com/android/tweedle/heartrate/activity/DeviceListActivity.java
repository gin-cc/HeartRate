package com.android.tweedle.heartrate.activity;


import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.support.v4.app.FragmentTransaction;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tweedle.heartrate.R;
import com.android.tweedle.heartrate.adapter.MyBaseExpandableListAdapter;
import com.android.tweedle.heartrate.ble.BluetoothFragment;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class DeviceListActivity extends AppCompatActivity {

    private static final String TAG = "DeviceListActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRAS_DEVICE_NAME = "device_name";

    private ArrayList<String> gData = null;
    private ArrayList<ArrayList<String>> itemData = null;
    private ArrayList<String> listData = null;

    private Context mContext;
    private ExpandableListView expandableListView;
    private MyBaseExpandableListAdapter myExListDeviceAdapter = null;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter localBluetoothAdapter;

    private boolean mScanning;
    private Handler mHandler;

    private static final long SCAN_PERIOD = 10000;

    private ScanCallback mLeScanCallback  ;
    private ArrayList<String> newDeviceList = new ArrayList<String>();


    private BluetoothSocket clientSocket;
    private OutputStream os;
    private final UUID le_uuid = UUID.fromString("49535343-fe7d-4ae5-8fa9-9fafd205e455");
    private BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);



//        if(savedInstanceState == null){
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            BluetoothFragment fragment = new BluetoothFragment();
//            transaction.replace(1,fragment);
//            transaction.commit();
//
//        }

//        setCustomActionBar();
        mHandler = new Handler();
        // Initializes Bluetooth adapter.初始化蓝牙适配器
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();



        //获取顶部导航栏
       ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);//设定左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角的图标加上一个返回的图标
        }

        mContext = DeviceListActivity.this;
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);

        //设置广播信息过滤
        //当设备被发现时注册广播 每搜索到一个设备就会发送一个广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //注册蓝牙搜索广播接收者，接受并处理结果
        this.registerReceiver(mReceiver, filter);

        // 当全部搜索完成后发送该广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //列表数据准备
        itemData = new ArrayList<ArrayList<String>>();
        gData = new ArrayList<String>();
        gData.add("已配对");
        gData.add("其它设备");

        //已配对
        listData = new ArrayList<String>();
        //获取已配对的蓝牙设备
        localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
         Set<BluetoothDevice> pairedDevices =  localBluetoothAdapter.getBondedDevices();
        //        如果有配对的设备，请将每个设备添加到已配对下
        if (pairedDevices.size() > 0) {
            for(BluetoothDevice device:pairedDevices){
                    listData.add(device.getName()+"\n"+ device.getAddress());
            }
        }
        else{
            listData.add("没有已配对设备");
        }
        itemData.add(listData);

        //其他设备
//        listData = new ArrayList<String>();
//        if(!newDeviceList.isEmpty()){
//            itemData.add(newDeviceList);
//        }else {
//            listData.add("没有其他可用设备");
//            itemData.add(listData);
//        }


        myExListDeviceAdapter = new MyBaseExpandableListAdapter(gData,itemData,mContext);
        expandableListView.setAdapter(myExListDeviceAdapter);


        //获取小组中的数量，默认展开已配对设备
        expandableListView.expandGroup(0);

//        int groupCount = myExListDeviceAdapter.getGroupCount();
//        if(groupCount>= 0){
//            for (int i=0; i<groupCount; ++i) {
//                if(!myExListDeviceAdapter.getGroup(i).isEmpty())
//                {
//                    expandableListView.expandGroup(i);
//                }

//                if(myExListDeviceAdapter.(i)!= 0){
//                    expandableListView.expandGroup(i);
//                }
//                if(expandableLis)
                //IndexOutOfBoundsException
//            }
//        }


        //为列表设置点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            //如果列表中的设备被点击了应该设置开始连接设备并且终止设备扫描

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mBluetoothAdapter.cancelDiscovery();
                Log.d(TAG, "点击了列表中的设备！");
                //获取设备MAC地址，即是视图中的最后17个字符

                String info = itemData.get(groupPosition).get(childPosition).toString();
                String[] res = info.split("\n");
                Log.d(TAG, "deviceName: "+res[0]);
                Log.d(TAG, "deviceAddress: "+res[1]);
                try {
                    if(device == null){
                        //获得远程设备
                        device = mBluetoothAdapter.getRemoteDevice(res[1]);
                    }
                    if(clientSocket == null){
                        clientSocket = device.createRfcommSocketToServiceRecord(le_uuid);
                        clientSocket.connect();
                        os = clientSocket.getOutputStream();
                        Toast.makeText(v.getContext(),"连接上了"+res[0],Toast.LENGTH_SHORT).show();
                    }
//                    if(os!= null){
//                        os.write("蓝牙信息来了",getBytes("utf-8"));
//                    }

                }catch (Exception e){

                }

                //创建结果Intent并包含MAC地址
                Intent intent = new Intent();
                intent.putExtra(EXTRAS_DEVICE_NAME,res[0]);
                intent.putExtra(EXTRA_DEVICE_ADDRESS, res[1]);
                startActivity(intent);
                //设置结果并完成此活动
//                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单资源，添加控件/ I
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
                Log.d(TAG, "onOptionsItemSelected: 点击了搜索设备");
                //如果当前正在搜索，就先取消搜索
                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                }
                doDiscovery();   //开始扫描设备

                new AlertDialog.Builder(this)
                        .setTitle("HeartRate蓝牙")
                        .setMessage("搜索设备中...")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                scanLeDevice(false); //取消扫描设备
                                mBluetoothAdapter.cancelDiscovery();//取消扫描设备
                            }
                        })
                        .show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 确保我们不再需要去发现蓝牙
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        //取消注册广播听众
        this.unregisterReceiver(mReceiver);
    }
    /**
     * 用蓝牙适配器启动设备发现
     */
    private void doDiscovery() {
        Log.d(TAG, "进入doDiscovery()");
        // 如果我们已经发现了，停止搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //请求搜索发现
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 侦听发现设备的广播接收器。
     * 发现完成
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // 当发现一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //从Intent中获取蓝牙设备对象
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //如果它已经配对，跳过它，因为它已经在列表中了
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDeviceList.add(device.getName()+"\n"+ device.getAddress());
                }
                if(!newDeviceList.isEmpty()){
                    itemData.add(newDeviceList);
                    //展开新发现设备列表
                    expandableListView.expandGroup(1);
                }
                // 搜索结束
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (newDeviceList.isEmpty()) { //没有发现设备
                    String noDevices = "没有发现设备";
                    newDeviceList.add(noDevices);
                    itemData.add(newDeviceList);
//                    Toast.makeText(DeviceListActivity.this,noDevices,Toast.LENGTH_SHORT).show();
//                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };




}
