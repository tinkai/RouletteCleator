package com.tinkai.myroulette;

import android.util.Log;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RouletteItemList {
    private ArrayList<EditText> itemNameList;
    private ArrayList<EditText> itemRatioList;

    private final int MIN_ITEM_NUM = 2;
    private final int MAX_ITEM_NUM = 12;

    public RouletteItemList() {
        this.itemNameList = new ArrayList<>();
        this.itemRatioList = new ArrayList<>();
    }

    public void addItemList(EditText nameText, EditText ratioText) {
        this.itemNameList.add(nameText);
        this.itemRatioList.add(ratioText);
    }

    public void setItemText(int pos, String name, String ratio) {
        this.setItemNameText(pos, name);
        this.setItemRatioText(pos, ratio);
    }

    public void setItemNameText(int pos, String name) {
        this.itemNameList.get(pos).setText(name, TextView.BufferType.NORMAL);
    }

    public void setItemRatioText(int pos, String ratio) {
        this.itemRatioList.get(pos).setText(ratio, TextView.BufferType.NORMAL);
    }

    public void removeItemList(int pos) {
        this.itemNameList.remove(pos);
        this.itemRatioList.remove(pos);
    }

    public int getSize() {
        return this.itemNameList.size();
    }

    public String getStringName(int pos) {
        return String.valueOf(this.itemNameList.get(pos).getText());
    }

    public String getStringRatio(int pos) {
        return String.valueOf(this.itemRatioList.get(pos).getText());
    }

    public boolean isOverMax() {
        if (this.getSize() >= MAX_ITEM_NUM) return true;
        return false;
    }

    public boolean isUnderMin() {
        if (this.getSize() <= MIN_ITEM_NUM) return true;
        return false;
    }
}
