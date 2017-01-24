package com.riverauction.riverauction.feature.consult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.riverauction.riverauction.R;

public class BoardPagerItem {
    private final int titleResId;
    private final int index;

    public BoardPagerItem(int titleResId, int index) {
        this.titleResId = titleResId;
        this.index = index;
    }

    public View createTabView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tabView = inflater.inflate(R.layout.item_my_lesson_tab_pager, null);

        TextView titleView = (TextView) tabView.findViewById(R.id.my_lesson_pager_title);
        titleView.setText(titleResId);
        return tabView;
    }
}