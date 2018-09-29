package com.tinkai.myroulette;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<ItemInfo> itemList;

    public ItemListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemList(ArrayList<ItemInfo> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.itemList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_row, parent, false);

            ItemInfo item = this.itemList.get(position);

            EditText itemName = convertView.findViewById(R.id.item_name_edit_text);
            itemName.setText(item.getName());
            EditText itemRatio = convertView.findViewById(R.id.item_ratio_edit_text);
            itemRatio.setText(item.getRatio());
        }

        TextView itemNum = convertView.findViewById(R.id.item_number_text_view);
        itemNum.setText(String.valueOf(position+1) + ".");

        ItemInfo item = this.itemList.get(position);

        EditText itemName = convertView.findViewById(R.id.item_name_edit_text);
        String name = String.valueOf(itemName.getText());
        item.setName(name);

        EditText itemRatio = convertView.findViewById(R.id.item_ratio_edit_text);
        String ratio = String.valueOf(itemRatio.getText());
        item.setRatio(ratio);

        return convertView;
    }
}
