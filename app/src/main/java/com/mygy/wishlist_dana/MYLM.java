package com.mygy.wishlist_dana;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MYLM extends LinearLayoutManager {
    private boolean isScrollEnabled = false;

    public MYLM(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}