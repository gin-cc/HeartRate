package com.android.tweedle.heartrate.achart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;


public class HearRateAchart implements AChartAbstract {
    public Intent getIntent(Context context) {
//        Intent mIntent = null;
        Intent mIntent= ChartFactory.getBarChartIntent(context, getDataSet(), getRenderer(), BarChart.Type.STACKED, "当月开票金额树状图");
        return mIntent;
    }

    /**
     * 构造数据
     * @return
     */
    public XYMultipleSeriesDataset getDataSet() {
        // 构造数据
        XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
        CategorySeries barSeries = new CategorySeries("2014年3月");
        barSeries.add(865.5969);
        barSeries.add(2492.6479);
        barSeries.add(891.0137);
        barSeries.add(0.0);
        barSeries.add(691.0568);
        barDataset.addSeries(barSeries.toXYSeries());
        return barDataset;
    }

    /**
     * 构造渲染器
     * @return
     */
    public XYMultipleSeriesRenderer getRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//      renderer.setChartTitle("当月开票");
//      // 设置标题的字体大小
//      renderer.setChartTitleTextSize(16);
        renderer.setXTitle("事业部");
        renderer.setYTitle("单位(万元)");
        renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.WHITE);
        // 设置X轴的最小数字和最大数字
        renderer.setXAxisMin(0.5);
        renderer.setXAxisMax(5.5);
        // 设置Y轴的最小数字和最大数字
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(3000);
        renderer.addXTextLabel(1, "电网");
        renderer.addXTextLabel(2, "通信");
        renderer.addXTextLabel(3, "宽带");
        renderer.addXTextLabel(4, "专网");
        renderer.addXTextLabel(5, "轨交");
        renderer.setZoomButtonsVisible(true);
        // 设置渲染器允许放大缩小
        renderer.setZoomEnabled(true);
        // 消除锯齿
        renderer.setAntialiasing(true);
        // 设置背景颜色
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.GRAY);
        // 设置每条柱子的颜色
        SimpleSeriesRenderer sr = new SimpleSeriesRenderer();
        sr.setColor(Color.YELLOW);
        renderer.addSeriesRenderer(sr);
        // 设置每个柱子上是否显示数值
        renderer.getSeriesRendererAt(0).setDisplayBoundingPoints(true);
//        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        // X轴的近似坐标数  (这样不显示横坐标)
        renderer.setXLabels(0);
        // Y轴的近似坐标数
        renderer.setYLabels(6);
        // 刻度线与X轴坐标文字左侧对齐
        renderer.setXLabelsAlign(Paint.Align.LEFT);
        // Y轴与Y轴坐标文字左对齐
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        // 允许左右拖动,但不允许上下拖动.
        renderer.setPanEnabled(true, false);
        // 柱子间宽度
        renderer.setBarSpacing(0.5f);
        // 设置X,Y轴单位的字体大小
        renderer.setAxisTitleTextSize(20);
        return renderer;
    }

}
