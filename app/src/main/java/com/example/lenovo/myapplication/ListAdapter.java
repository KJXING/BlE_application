package com.example.lenovo.myapplication;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 更新时间 ：2018/09/20 12:13
 * 描 述 ：
 * ============================================================
 */
public class ListAdapter extends BaseAdapter {
    Context mContext;
    List<BluetoothDevice> mStringList;

    public ListAdapter(Context context, List<BluetoothDevice> stringList) {
        mContext = context;
        mStringList = stringList;
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return mStringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mStringList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activity_list_view, null);
            holder.text = convertView.findViewById(R.id.ialv_tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice BTDevice = mStringList.get(position);
        holder.text.setText(BTDevice.getName()+" " + BTDevice.getAddress());
        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }
}
