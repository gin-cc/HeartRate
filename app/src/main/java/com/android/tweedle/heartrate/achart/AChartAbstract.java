package com.android.tweedle.heartrate.achart;

import android.content.Context;
import android.content.Intent;

public interface AChartAbstract {
    /**
     * 获取一个当前类型图标的Intent实例
     */
    public Intent getIntent(Context context);

}
