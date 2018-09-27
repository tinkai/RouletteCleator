package com.tinkai.myroulette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RouletteListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<RouletteInfo> rouletteList;

    public RouletteListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRouletteList(ArrayList<RouletteInfo> rouletteList) {
        this.rouletteList = rouletteList;
    }

    @Override
    public int getCount() {
        return this.rouletteList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.rouletteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.rouletteList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.layout_roulette_row, parent, false);

        ((TextView)convertView.findViewById(R.id.roulette_list_name)).setText(rouletteList.get(position).getName());

        return convertView;
    }

}
