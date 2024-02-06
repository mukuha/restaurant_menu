package com.mukuha.android.snacksmenu.events;

import com.mukuha.android.snacksmenu.model.DataItem;

import java.util.List;

public class DataItemsEvent {
    private List<DataItem> itemList;

    public DataItemsEvent(List<DataItem> itemList) { this.itemList = itemList; }

    public List<DataItem> getItemList() { return itemList; }

    public void setItemList(List<DataItem> itemList) { this.itemList = itemList; }
}
