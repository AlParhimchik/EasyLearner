package com.example.swipecardlibrary.listener;

/**
 * Created by sashok on 28.9.17.
 */

public interface BaseCardAdapterListener<T> {

    public T getItem(int position);
    public int getItemCount();
}
