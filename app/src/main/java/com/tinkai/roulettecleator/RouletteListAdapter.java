package com.tinkai.roulettecleator;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class RouletteListAdapter extends ArrayAdapter<RouletteInfo> {
    private LayoutInflater layoutInflater = null;
    private static final float BUTTON_WIDTH_XP = 70f;
    private int margin;
    private RouletteListActivity rouletteListActivity;

    public RouletteListAdapter(Context context, RouletteListActivity rouletteListActivity, int resource, ArrayList<RouletteInfo> rouletteList) {
        super(context, resource, rouletteList);
        layoutInflater = LayoutInflater.from(context);

        this.rouletteListActivity =rouletteListActivity;

        float density = getContext().getResources().getDisplayMetrics().density;
        int buttonWidthPX = (int) (BUTTON_WIDTH_XP * density + 0.5f);

        WindowManager wm = (WindowManager)getContext().getSystemService(getContext().WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point realSize = new Point();
        display.getRealSize(realSize);

        this.margin = realSize.x - buttonWidthPX;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_roulette_row, parent, false);
        }

        ViewPager viewPager = convertView.findViewById(R.id.roulette_list_viewpager);
        viewPager.setPageMargin(-margin);
        RouletteListPagerAdapter adapter = new RouletteListPagerAdapter(getContext(), this.rouletteListActivity, getItem(position), position);
        viewPager.setAdapter(adapter);

        return convertView;
    }
}
