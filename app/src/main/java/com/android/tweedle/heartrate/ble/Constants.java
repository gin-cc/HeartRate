package com.android.tweedle.heartrate.ble;

/**
 * Defines several constants used between {@link BluetoothService} and the UI.
 * 定义了在{蓝牙服务}和UI之间使用的几个常量。
 */
public interface Constants {
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
}
