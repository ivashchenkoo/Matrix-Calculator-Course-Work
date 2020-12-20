package com.example.matrixcalculatorcw;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdapterOperationList extends ArrayAdapter<ItemOperation> implements Filterable {
    private ArrayList<ItemOperation> auxItemList;
    private int[] colorArray;
    private Context context;

    AdapterOperationList(Context context) {
        super(context, R.layout.item_operation, initItemOperationList(context));
        this.auxItemList = getArrayListItems();
        this.context = context;
        colorArray = getColors();
    }

    private static class ViewHolder {
        ImageView iconView;
        TextView titleView;
        TextView descriptionView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemOperation itemOperation = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_operation, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.iconView = convertView.findViewById(R.id.item_operation_icon);
            viewHolder.titleView = convertView.findViewById(R.id.item_operation_title);
            viewHolder.descriptionView = convertView.findViewById(R.id.item_operation_description);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.iconView.setImageDrawable(
                TextDrawable.builder().beginConfig().textColor(colorArray[0]).endConfig().buildRound(itemOperation.getIcon(), colorArray[1])
        );
        viewHolder.titleView.setText(itemOperation.getTitle());
        viewHolder.descriptionView.setText(Html.fromHtml(itemOperation.getDescription()));

        return convertView;
    }

    private static ArrayList<ItemOperation> initItemOperationList(Context context) {
        ArrayList<List<String>> stringArray = new ArrayList<>();
        ArrayList<ItemOperation> aux = new ArrayList<>();

        stringArray.add(Arrays.asList(context.getResources().getStringArray(R.array.operation_title)));
        stringArray.add(Arrays.asList(context.getResources().getStringArray(R.array.operation_description)));
        stringArray.add(Arrays.asList(context.getResources().getStringArray(R.array.operation_icon)));
        stringArray.add(Arrays.asList(context.getResources().getStringArray(R.array.operation_number)));

        for (int i = 0; i < stringArray.get(0).size(); ++i)
            aux.add(new ItemOperation(
                    i, stringArray.get(0).get(i), stringArray.get(1).get(i), stringArray.get(2).get(i),
                    Integer.parseInt(stringArray.get(3).get(i)))
            );

        return aux;
    }

    private ArrayList<ItemOperation> getArrayListItems() {
        ArrayList<ItemOperation> arrayList = new ArrayList<>();

        for (int i = 0; i < getCount(); ++i)
            arrayList.add(getItem(i));

        return arrayList;
    }

    private int[] getColors() {
        boolean mode = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_darkmode), false);
        int[] colors = new int[2];

        colors[0] = context.getResources().getColor(mode ? android.R.color.black : android.R.color.white);
        colors[1] = context.getResources().getColor(mode ? R.color.colorAccentInverse : R.color.colorAccent);

        return colors;
    }
}