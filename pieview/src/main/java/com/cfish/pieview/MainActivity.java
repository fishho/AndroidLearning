package com.cfish.pieview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private PieView pieView;
    private ArrayList<PieData> mData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pieView = (PieView) findViewById(R.id.pieView);
        pieView.setStartAngle(-90);
        for (int i = 1; i< 8; i++) {
            PieData pie = new PieData("a"+i, i%2 + 1);
            mData.add(pie);
        }
        pieView.setData(mData);
    }
}
