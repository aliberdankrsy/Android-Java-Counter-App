package com.aliberdankrsy.coolzikirmatik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    private ArrayList<String> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtZikirMetni;
        TextView txtTarihSaat;
    }

    public MyAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.record_list_item_layout, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.record_list_item_layout, parent, false);
            viewHolder.txtZikirMetni = convertView.findViewById(R.id.textViewZikirMetni);
            viewHolder.txtTarihSaat = convertView.findViewById(R.id.textViewTarihSaat);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        // Zikir metnini ve tarih-saat bilgisini ilgili TextView'lara yerleştirin
        String[] parts = dataSet.get(position).split(" - ");
        viewHolder.txtZikirMetni.setText(parts[0]); // Zikir metni
        viewHolder.txtTarihSaat.setText("Tarih ve Saat: " + parts[1]); // Tarih ve saat bilgisi

        return result;
    }
}
