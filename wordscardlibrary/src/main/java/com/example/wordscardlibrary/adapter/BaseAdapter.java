package com.example.wordscardlibrary.adapter;


import com.example.wordscardlibrary.listener.BaseCardAdapterListener;

import java.util.Random;

/**
 * Created by sashok on 28.9.17.
 */

public abstract class BaseAdapter<T> implements BaseCardAdapterListener<T> {
    protected  T currentItem;
    public T getRandomItem() {
        if (getItemCount()==0) return null;
        int position = new Random().nextInt(getItemCount());
        currentItem=getItem(position);
        return currentItem;
    }
    public T getCurrentItem(){
        return currentItem;
    }

    public void notifyDataSetChanged(){

    }
}
