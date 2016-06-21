package com.cfish.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private WeatherChartView weatherChartView;
    private SimpleLineChart simpleLineChart;
    private ArrayList<LineData> lineDatas = new ArrayList<>();
    private String[] time = new String[6];
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 //       weatherChartView = (WeatherChartView) findViewById(R.id.chart_View);
        simpleLineChart = (SimpleLineChart) findViewById(R.id.calendar_view);
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
//
//        weatherChartView.setTempDay(new int[] {14, 15, 16, 17, 9, 9});
//        weatherChartView.setTempNight(new int[] {7, 5, 9, 10 ,3, 2});
//
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
//        String today = format.format(date);
//        time[1] = today;
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(cal.DATE,-1);
//        time[0] = format.format(cal.getTime());
//        cal.add(cal.DATE,2);
//        time[2] = format.format(cal.getTime());
//        cal.add(cal.DATE,1);
//        time[3] = format.format(cal.getTime());
//        cal.add(cal.DATE,1);
//        time[4] = format.format(cal.getTime());
//        cal.add(cal.DATE,1);
//        time[5] = format.format(cal.getTime());
//        Log.d("DFish",time[5]);
//        weatherChartView.setTime(time);
//        weatherChartView.invalidate();

        String[] xItem = {"1","2","3","4","5","6","7","8"};
        String[] yItem = {"10k","20k","30k","40k","50k"};
        if (simpleLineChart == null) {
            Log.e("Fish","null!!!");
        }
        simpleLineChart.setXItem(xItem);
        simpleLineChart.setYItem(yItem);

        HashMap<Integer, Integer> pointMap = new HashMap<>();
        for (int i = 0; i < xItem.length; i++) {
            pointMap.put(i, (int) (Math.random()*5));
        }
        simpleLineChart.setData(pointMap);
    }
}
