package com.cfish.pieview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private PieView pieView;
    private PieChart pieChart;
    private ArrayList<PieData> mData = new ArrayList<>();

    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pieView = (PieView) findViewById(R.id.pieView);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieView.setStartAngle(-90);
        for (int i = 1; i< 8; i++) {
            PieData pie = new PieData("a"+i, i%2 + 1);
            pie.setName("区域"+i);
            pie.setValue(i%2 + 1);
            pie.setColor(mColors[i-1]);
            mData.add(pie);
        }
        //pieView.setData(mData);
        pieChart.setPieData(mData);

    }
}
