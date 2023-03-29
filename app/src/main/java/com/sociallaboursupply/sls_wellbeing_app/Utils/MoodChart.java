package com.sociallaboursupply.sls_wellbeing_app.Utils;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;
import com.sociallaboursupply.sls_wellbeing_app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MoodChart {

    public static void drawChart(List<MoodModel> moods, LineChart lineChart, Activity activity) {
        // TODO: add y axis labels
        // TODO: see if possible to detect darkmode and redraw
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> xAxisLabels = new ArrayList<>();

        int max = moods.size();
        if (max > 10) max = 10;
        for (int i = 0; i < max; i++) {
            MoodModel mood = moods.get(moods.size()-max+i);
            String status = mood.getStatus();
            int rating = 0;
            if (status == null) {
                continue;
            } else if (status.contentEquals(MoodModel.HAPPY)) {
                entries.add(new Entry(i, 3));
            } else if (status.contentEquals(MoodModel.NEUTRAL)) {
                entries.add(new Entry(i, 2));
            } else if (status.contentEquals(MoodModel.SAD)) {
                entries.add(new Entry(i, 1));
            }
            DateFormat dateFormat = new SimpleDateFormat("MMM-d");
            String month = dateFormat.format(mood.getDate());
            xAxisLabels.add(month);
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");

        // Hide left and right axis values, legend and grid lines
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getLegend().setEnabled(false);

        // Set x axis labels
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        lineChart.getXAxis().setTextColor(activity.getColor(R.color.medGrey));

        // Touch settings
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);

        // Settings for the chart data
        dataSet.setColor(activity.getColor(R.color.medGrey));
        dataSet.setValueTextColor(activity.getColor(R.color.medGrey));
        dataSet.setHighLightColor(Color.RED);
        dataSet.setCircleColor(activity.getColor(R.color.medGrey));
        dataSet.setCircleHoleColor(activity.getColor(R.color.medGrey));
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(1.5f);
        dataSet.setHighlightEnabled(false);
        dataSet.setDrawCircles(true);

        lineChart.setData(new LineData(dataSet));
        lineChart.invalidate();
    }
}
