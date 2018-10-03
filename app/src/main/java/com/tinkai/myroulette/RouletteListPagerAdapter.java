package com.tinkai.myroulette;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouletteListPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private static final int PAGE_NUM = 2;
    private RouletteInfo rouletteInfo;

    public RouletteListPagerAdapter(Context context, RouletteInfo rouletteInfo) {
        super();
        inflater = LayoutInflater.from(context);
        this.rouletteInfo = rouletteInfo;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout layout;
        if(position == 0){
            layout = (LinearLayout)inflater.inflate(R.layout.roulette_row_page1, null);
            TextView text = layout.findViewById(R.id.roulette_list_name);
            text.setText(rouletteInfo.getName());
        }else{
            layout = (LinearLayout)inflater.inflate(R.layout.roulette_row_page2, null);
        }

        container.addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }

    public RouletteInfo getRouletteInfo() {
        return rouletteInfo;
    }
}
