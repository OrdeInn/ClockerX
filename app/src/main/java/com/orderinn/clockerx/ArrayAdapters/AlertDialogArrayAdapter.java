package com.orderinn.clockerx.ArrayAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.orderinn.clockerx.R;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> itemArray;
    private Context context;


    public AlertDialogArrayAdapter(@NonNull Context context, ArrayList<String> itemArray) {
        super(context, android.R.layout.simple_list_item_1, itemArray);

        this.context = context;
        this.itemArray = itemArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        listItem = LayoutInflater.from(context).inflate(R.layout.basic_list_item, parent, false);
        TextView itemText = listItem.findViewById(R.id.itemText);
        itemText.setText(itemArray.get(position));

        return listItem;
    }
}
