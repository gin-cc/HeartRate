package com.android.tweedle.heartrate.adapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.tweedle.heartrate.R;

import java.util.ArrayList;



public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> gData;
//    private ArrayList<ArrayList<BleDevice>> iData;
    private ArrayList<ArrayList<String>> itemData;
//    private ArrayList<ArrayList<BluetoothDevice>> mLeDevices;
//    private ArrayList<BluetoothDevice> devices;
    private Context mContext;

    public MyBaseExpandableListAdapter(ArrayList<String> gData,ArrayList<ArrayList<String>> iData, Context mContext) {
        this.gData = gData;
        this.itemData = iData;
//        this.mLeDevices = iData;
        this.mContext = mContext;
//        this.devices = new ArrayList<BluetoothDevice>();
    }

    //先添加到一组list里面，再整个添加到特定的小组标签下面
//    public void addDevice(BluetoothDevice device) {
//        if(!mLeDevices.contains(device)) {
//            devices.add(device);
//            mLeDevices.add(devices);
//        }
//    }
//    public void clear() {
//        mLeDevices.clear();
//    }



    /**
     *
     * 获取组的个数
     *
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {

        return gData.size();
    }

    /**
     *
     * 获取指定组中的子元素个数 同 getCount
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount(int groupPosition) {
 //报错 IndexOutOfBoundsException  Invalid index 1, size is 1
        return itemData.get(groupPosition).size();
    }

    /**
     *
     * 获取指定组中的数据
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public String getGroup(int groupPosition) {

        return gData.get(groupPosition);

    }

    /**
     *
     * 获取指定组中的指定子元素数据。同getDevice
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return itemData.get(groupPosition).get(childPosition);
    }

    /**
     *
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    /**
     *
     * 获取指定组中的指定子元素ID  同getItemId
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    /**
     *
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     *
     * 获取显示指定组的视图对象  //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.item_tv_group_name);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        //设置组名
        groupHolder.tv_group_name.setText(gData.get(groupPosition).toString());
        return convertView;
    }


    //取得显示给定分组给定子位置的数据用的视图
    /**
     *
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.tv_name = (TextView) convertView.findViewById(R.id.item_tv_name);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        //获取蓝牙广播名
        itemHolder.tv_name.setText(itemData.get(groupPosition).get(childPosition).toString());
        return convertView;
    }

    //设置子列表是否可选中
    /**
     *
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }


    private static class ViewHolderGroup{
        private TextView tv_group_name;
    }

    private static class ViewHolderItem{
        private TextView tv_name;
    }

//    public interface OnDeviceClickListener {
//        void onConnect(BleDevice bleDevice);
//
//        void onDisConnect(BleDevice bleDevice);
//
//        void onDetail(BleDevice bleDevice);
//    }
//
//    private OnDeviceClickListener mListener;
//
//    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
//        this.mListener = listener;
//    }

}

