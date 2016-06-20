package com.cfish.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private WeatherChartView weatherChartView;
    private ArrayList<LineData> lineDatas = new ArrayList<>();
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherChartView = (WeatherChartView) findViewById(R.id.chart_View);
//        lineChart =  (LineChart) findViewById(R.id.lineChart);
//
//        for (int i= 0 ; i< 8; i++) {
//            LineData lineData = new LineData();
//            lineData.setColor(mColors[i]);
//            lineData.setName("i"+i);
//            float[][] value = {{1,1,1,1,1,1,1,1},{i,i,i,i,i,i,i,i}};
//            lineData.setValue(value);
//            lineDatas.add(lineData);
//        }
//
//        lineChart.setLineDatas(lineDatas);

        weatherChartView.setTempDay(new int[] {14, 15, 16, 17, 9, 9});
        weatherChartView.setTempNight(new int[] {7, 5, 9, 10 ,3, 2});
        weatherChartView.invalidate();
    }
}
