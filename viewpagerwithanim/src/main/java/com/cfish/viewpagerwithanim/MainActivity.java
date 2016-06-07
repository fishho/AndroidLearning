package com.cfish.viewpagerwithanim;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewpager;
    private PagerAdapter mAdapter;
    int[] imgRes = {R.drawable.c, R.drawable.d,
            R.drawable.h, R.drawable.j, R.drawable.k, R.drawable.l};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewpager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewpager.setPageMargin(10); //page间距
        mViewpager.setOffscreenPageLimit(6);//缓存页面数量
        mViewpager.setAdapter(mAdapter =  new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = new ImageView(MainActivity.this);
                view.setImageResource(imgRes[position]);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View)object);
            }

            @Override
            public int getCount() {
                return imgRes.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        mViewpager.setPageTransformer(true,new AlphaPageTransformer());
        //mViewpager.setPageTransformer(true,new RotatePageTransformer());
    }
}
