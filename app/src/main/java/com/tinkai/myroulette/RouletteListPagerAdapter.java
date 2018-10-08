package com.tinkai.myroulette;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouletteListPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private static final int PAGE_NUM = 2;
    RouletteInfo rouletteInfo;
    RouletteListActivity rouletteListActivity;
    int height;

    public RouletteListPagerAdapter(Context context, RouletteListActivity rouletteListActivity, RouletteInfo rouletteInfo, int height) {
        super();

        this.rouletteListActivity = rouletteListActivity;
        this.inflater = LayoutInflater.from(context);
        this.rouletteInfo = rouletteInfo;
        this.height =height;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LinearLayout layout;
        if(position == 0){
            layout = (LinearLayout)inflater.inflate(R.layout.roulette_row_page1, null);
            TextView text = layout.findViewById(R.id.roulette_name_text_view);
            text.setText(rouletteInfo.getName());

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rouletteListActivity.startEditRouletteActivity(rouletteInfo);
                }
            });

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    rouletteListActivity.setUseRoulette(rouletteInfo);
                    return true;
                }
            });
        }else{
            layout = (LinearLayout)inflater.inflate(R.layout.roulette_row_page2, null);
            Button deleteButton = layout.findViewById(R.id.roulette_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rouletteListActivity.deleteRoulette(rouletteInfo, height);
                }
            });
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
}
