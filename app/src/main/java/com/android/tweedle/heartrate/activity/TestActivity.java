package com.android.tweedle.heartrate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.tweedle.heartrate.R;
import com.android.tweedle.heartrate.achart.ChartFactory;
import com.android.tweedle.heartrate.achart.HearRateAchart;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


public class TestActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Animation mAnimation;
    private LinearLayout chartLyt;
    private HearRateAchart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        GraphicalView chartView = ChartFactory.getLineChartView(TestActivity.this, mChart.getDataSet(), mChart.getRenderer());

        chartLyt = (LinearLayout)findViewById(R.id.chart);
        chartLyt.addView(chartView);

        XYSeries series = new XYSeries("测试表格");
        int hour = 0;
        for(int i=0;i<10;i++){
            series.add(hour,i);
        }
        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);  //线宽
        renderer.setColor(Color.RED);//颜色
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);//尺寸用像素表示
        // we add point markers
        renderer.setPointStyle(PointStyle.CIRCLE);//设置样式
        renderer.setPointStrokeWidth(3);
        //创建控制整个图表的渲染器并为每个系列添加单个渲染器：
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        // We want to avoid black border
        // transparent margins
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid
        //创建视图

    }
}


//operationPage(index);
//                switch (index){
//                    case 0://心率检测

//                        Toast.makeText(context,"case 1",Toast.LENGTH_SHORT).show();
//       动画

//                        AnimationSet aniSet = new AnimationSet(true);
//                        final int ANITIME = 1200;
//
//                        // 尺寸变化动画，设置尺寸变化
//                        ScaleAnimation scaleAni = new ScaleAnimation(0.98f, 1.1f, 0.98f, 1.24f,
//                                   Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                        scaleAni.setDuration(ANITIME);              // 设置动画效果时间
//                        scaleAni.setRepeatMode(Animation.RESTART);  // 重新播放
//                        scaleAni.setRepeatCount(Animation.INFINITE);// 循环播放
//                        aniSet.addAnimation(scaleAni);  // 将动画效果添加到动画集中
//
//                        // 透明度变化
//                        AlphaAnimation alphaAni = new AlphaAnimation(1f, 0.05f);
//                        alphaAni.setDuration(ANITIME);              // 设置动画效果时间
//                        alphaAni.setRepeatMode(Animation.RESTART);  // 重新播放
//                        alphaAni.setRepeatCount(Animation.INFINITE);// 循环播放
//                        aniSet.addAnimation(alphaAni);  // 将动画效果添加到动画集中
//                        //有问题
//                        myImage.startAnimation(aniSet);
//                        break;
//                    case 1://历史记录
//                        break;
//                }