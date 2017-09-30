package com.example.wordscardlibrary.listener;

import com.example.wordscardlibrary.model.WordInterface;

/**
 * Created by sashok on 28.9.17.
 */

public interface CardListener<T extends WordInterface> {
    public void onSwipedLeft();
    public void onSwipedRight();
    public void onCardClicked(T cur_item);
    public void onFlipAnimEnded(T item);
    public void onDataSetChanged();
    public void onPositiveButtonClicked();
    public void onNegativeButtonClicked();
}
