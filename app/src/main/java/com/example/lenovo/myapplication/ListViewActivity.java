package com.example.lenovo.myapplication;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends ListActivity {
    ListView mListView;
    List<BluetoothDevice> mStringList;
    ListAdapter mListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_list_view);
        mListView = findViewById(R.id.alv_lv_listview);
//        iniData();
//        initAdapter();
//        loadData();
    }

    private void iniData() {
        mStringList = new ArrayList<>();
    }

    private void loadData() {
        mListAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        mListAdapter = new ListAdapter(this, mStringList);
        mListView.setAdapter(mListAdapter);
    }
}
