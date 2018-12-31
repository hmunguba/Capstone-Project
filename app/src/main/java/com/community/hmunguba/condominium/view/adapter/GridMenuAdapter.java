package com.community.hmunguba.condominium.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.utils.Utils;

public class GridMenuAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] menuItems;
    private final int[] menuItemsDrawable;

    public GridMenuAdapter(Context context) {
        this.mContext = context;
        menuItems = mContext.getResources().getStringArray(R.array.main_menu_items);

        String profileType = Utils.getProfileTypePreference(mContext);
        if (profileType.equals("condominium")) {
            menuItemsDrawable = Utils.getCondominiumMenuItemDrawables();
        } else {
            menuItemsDrawable = Utils.getResidentMenuItemDrawables();
        }
    }

    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.menu_item, null);
            TextView menuItemText = gridView.findViewById(R.id.menu_item_text);
            ImageView menuItemImage = gridView.findViewById(R.id.menu_item_image);
            menuItemText.setText(menuItems[i]);
            menuItemImage.setImageResource(menuItemsDrawable[i]);
        } else {
            gridView = (View) view;
        }
        return gridView;
    }
}
