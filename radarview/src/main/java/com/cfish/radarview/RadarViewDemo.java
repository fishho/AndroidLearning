package com.cfish.radarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RadarViewDemo extends AppCompatActivity {


    private RadarView mRadarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_view_demo);
        mRadarView = (RadarView) findViewById(R.id.radar_view);
        mRadarView.start();
       // mRadarView.setImageUrl("http://afaf.jpg");
    }
}
