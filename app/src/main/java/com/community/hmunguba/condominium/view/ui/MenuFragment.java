package com.community.hmunguba.condominium.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.view.adapter.GridMenuAdapter;


/// https://www.viralandroid.com/2016/04/android-gridview-with-image-and-text.html

public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final String TAG = MenuFragment.class.getSimpleName();

    Context mContext;

    public MenuFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        GridMenuAdapter menuAdapter = new GridMenuAdapter(mContext);
        GridView menuGridView = view.findViewById(R.id.menu_grid);
        menuGridView.setAdapter(menuAdapter);
        menuGridView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(mContext, "Clicked item : " + i, Toast.LENGTH_SHORT).show();

        switch (i) {
            case 0:
                Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            default:
                break;
        }
    }
}
